package com.example.sockets.Client;

import com.example.sockets.Shared.WorldPosition;

import java.util.HashMap;
import java.util.Map;

public class LocalData {
    private final Map<Integer, ClientEntity> clientEntities;
    private int clientPlayerId;

    public LocalData() {
        this.clientEntities = new HashMap<>();
        this.clientPlayerId = -1;
    }

    public int getClientPlayerId() {
        return clientPlayerId;
    }

    public void setClientPlayerId(int clientPlayerId) {
        this.clientPlayerId = clientPlayerId;
    }

    public void addClientEntity(int id, WorldPosition worldPosition) {
        clientEntities.put(id, new ClientEntity(id, worldPosition));
    }

    public void updateClientEntityPosition(int id, WorldPosition worldPosition) {
        if(clientEntities.get(id) != null) {
            clientEntities.get(id).setWorldPosition(worldPosition);
        }
    }

    public void removeClientEntity(int id) {
        clientEntities.remove(id);
    }

    public Map<Integer, ClientEntity> getClientEntities() {
        return clientEntities;
    }
}
