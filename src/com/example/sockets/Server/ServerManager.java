package com.example.sockets.Server;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import java.io.*;
import java.net.*;
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
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); DataOutputStream dataInputStream = new DataOutputStream(byteArrayOutputStream)) {
            getServerClientData().forEach((k, v) -> {
                v.getTcpSocket().getInetAddress();
                try {
                    dataInputStream.writeInt(DataActionMapping.ENTITY_POSITION_CHANGE.ordinal());
                    dataInputStream.writeInt(networkedEntity.getUniqueEntityId());
                    dataInputStream.writeInt(worldPosition.x());
                    dataInputStream.writeInt(worldPosition.y());
                    dataInputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                byte[] bytesToSend = byteArrayOutputStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(bytesToSend, bytesToSend.length, v.getTcpSocket().getInetAddress(), v.getUdpPort());
                try { udpSocket.send(packet); } catch (IOException e) { throw new RuntimeException(e); }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Handles new clients and syncs them with the server
    public void handleClientConnects(Socket clientSocket) {
        // Create the client on the server
        ServerClientData newClient = new ServerClientData(clientSocket);

        // Add the client to the server
        serverClientData.put(newClient.getClientId(), newClient);

        // Update the client with current entities
        networkedEntities.forEach((k, v) -> networkEntityRegister(v, newClient));

        // Add a player entity to represent the client
        new ServerPlayer(this, "Player ID#" + NetworkedEntity.getNextUniqueId() + 1, new WorldPosition((int)(Math.random() * (50 + 100)), (int)(Math.random() * (50 + 100))));
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
