package com.example.sockets.Server;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private final ServerSocket tcpSocket;
    private final DatagramSocket udpSocket;
    private final Map<Integer, ServerClientData> serverClientData;
    private final Map<Integer, NetworkedEntity> networkedEntities;
    private final int tcpPort;
    private final int udpPort;

    public ServerManager(int tcpPort, int udpPort) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        try {
            this.tcpSocket = new ServerSocket(tcpPort);
            this.udpSocket = new DatagramSocket(udpPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.serverClientData = new HashMap<>();
        this.networkedEntities = new HashMap<>();
        ServerConnectionManager serverConnectionManager = new ServerConnectionManager(this);
        serverConnectionManager.start();
        ServerUDPReceiver serverUDPReceiver = new ServerUDPReceiver(this);
        serverUDPReceiver.start();
    }

    // Gracefully shuts down the server by closing the TCP and UDP ports.
    public void shutdown() {
        try { tcpSocket.close(); } catch (IOException e) { throw new RuntimeException(e); }
        udpSocket.close();
    }

    // Remove the client and its corresponding network entity upon disconnect
    // and notify all other clients via TCP
    public void handleServerClientDisconnect(ServerClientData serverClientData) {
        int clientPlayerId = serverClientData.getServerPlayer().getUniqueEntityId();
        getNetworkedEntities().remove(clientPlayerId);
        getServerClientData().remove(serverClientData.getClientId());
        getServerClientData().forEach((k, v) -> {
            ObjectOutputStream objectOutputStream = v.getObjectOutputStream();
            try {
                objectOutputStream.writeInt(DataActionMapping.ENTITY_REMOVED.ordinal());
                objectOutputStream.writeInt(clientPlayerId);
                objectOutputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Notifies a client of that an entity has been registered on the server.
    private void sendClientEntity(NetworkedEntity networkedEntity, ServerClientData serverClientData) {
        ObjectOutputStream output = serverClientData.getObjectOutputStream();
        try {
            output.writeInt(DataActionMapping.ENTITY_ADDED.ordinal());
            output.writeInt(networkedEntity.getUniqueEntityId());
            output.writeInt(networkedEntity.getWorldPosition().x());
            output.writeInt(networkedEntity.getWorldPosition().y());
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Broadcasts that the entity has been registered to all connected clients.
    public void networkEntityRegister(NetworkedEntity networkedEntity) {
        networkedEntities.put(networkedEntity.getUniqueEntityId(), networkedEntity);
        serverClientData.forEach((k, v) -> sendClientEntity(networkedEntity, v));
    }

    // Tells a specific client that an entity has been registered.
    public void networkEntityRegister(NetworkedEntity networkedEntity, ServerClientData serverClientData) {
        sendClientEntity(networkedEntity, serverClientData);
    }

    // Broadcasts the entity's changed position to all connected clients via UDP
    public void networkEntityPositionChange(NetworkedEntity networkedEntity, WorldPosition worldPosition) {
        getServerClientData().forEach((k, v) -> {
            byte[] packetBytes = ByteBuffer.allocate(16)
                    .putInt(DataActionMapping.ENTITY_POSITION_CHANGE.ordinal())
                    .putInt(networkedEntity.getUniqueEntityId())
                    .putInt(worldPosition.x())
                    .putInt(worldPosition.y())
                    .array();

            DatagramPacket packet = new DatagramPacket(packetBytes, packetBytes.length, v.getTcpSocket().getInetAddress(), v.getUdpPort());
            try {
                udpSocket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Handles new clients and syncs them with the server.
    public void handleClientConnects(Socket clientSocket) {
        // Create the client on the server.
        ServerClientData newClient = new ServerClientData(this, clientSocket);

        // Add the client to the server
        serverClientData.put(newClient.getClientId(), newClient);

        // Update the client with current entities.
        networkedEntities.forEach((k, v) -> networkEntityRegister(v, newClient));

        WorldPosition worldPosition = new WorldPosition((int)(Math.random() * (50 + 100)), (int)(Math.random() * (50 + 100)));
        // Add a player entity to represent the client.
        ServerPlayer serverPlayer = new ServerPlayer(this, "Player ID#" + NetworkedEntity.getNextUniqueId() + 1, worldPosition);

        // Associates the player entity with the server client.
        newClient.setServerPlayer(serverPlayer);

        // Let the player know what entity represents them by sending them their created networked entity's id.
        ObjectOutputStream output = newClient.getObjectOutputStream();
        try {
            output.writeInt(DataActionMapping.NOTIFY_CLIENT_LOCAL_PLAYER_ID.ordinal());
            output.writeInt(serverPlayer.getUniqueEntityId());
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerSocket getTcpSocket() {
        return tcpSocket;
    }

    public Map<Integer, ServerClientData> getServerClientData() {
        return serverClientData;
    }

    public Map<Integer, NetworkedEntity> getNetworkedEntities() {
        return networkedEntities;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }
}
