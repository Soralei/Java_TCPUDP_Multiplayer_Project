package com.example.sockets.Server;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private final ServerSocket socket;
    private final Map<Integer, ServerClientData> serverClientData;
    private final Map<Integer, NetworkedEntity> networkedEntities;

    public ServerManager(int port) {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.serverClientData = new HashMap<>();
        this.networkedEntities = new HashMap<>();
        ServerConnectionManager serverConnectionManager = new ServerConnectionManager(this);
        serverConnectionManager.start();
    }

    // Notifies a client of that an entity has been registered on the server
    private void sendClientEntity(NetworkedEntity networkedEntity, ServerClientData serverClientData) {
        ObjectOutputStream output = serverClientData.getObjectOutputStream();
        try {
            output.writeInt(DataActionMapping.ENTITY_ADDED);
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

    // Broadcasts the entity's changed position to all connected clients.
    public void networkEntityPositionChange(NetworkedEntity networkedEntity, WorldPosition worldPosition) {
        serverClientData.forEach((k, v) -> {
            ObjectOutputStream output = v.getObjectOutputStream();
            try {
                output.writeInt(DataActionMapping.ENTITY_POSITION_CHANGE);
                output.writeInt(networkedEntity.getUniqueEntityId());
                output.writeInt(worldPosition.x());
                output.writeInt(worldPosition.y());
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Handles new clients and syncs them with the server
    public void handleClientConnects(Socket clientSocket) {
        try {
            // Create the client on the server
            ServerClientData newClient = new ServerClientData(clientSocket, new ObjectOutputStream(clientSocket.getOutputStream()));

            // Add the client to the server
            serverClientData.put(newClient.getClientId(), newClient);

            // Update the client with current entities
            networkedEntities.forEach((k, v) -> networkEntityRegister(v, newClient));

            // Add a player entity to represent the client
            new ServerPlayer(this, "Player ID#" + NetworkedEntity.getNextUniqueId() + 1, new WorldPosition(50, 50));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public Map<Integer, ServerClientData> getServerClientData() {
        return serverClientData;
    }

    public Map<Integer, NetworkedEntity> getNetworkedEntities() {
        return networkedEntities;
    }
}
