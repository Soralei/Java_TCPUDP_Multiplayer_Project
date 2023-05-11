package com.example.sockets.Client;

import com.example.sockets.Shared.WorldPosition;

import java.util.HashMap;
import java.util.Map;

public class LocalData {
    private final Map<Integer, ClientEntity> clientEntities;

    public LocalData() {
        this.clientEntities = new HashMap<>();
    }

    public void addClientEntity(int id, WorldPosition worldPosition) {
        clientEntities.put(id, new ClientEntity(id, worldPosition));
    }

    public void updateClientEntityPosition(int id, WorldPosition worldPosition) {
        clientEntities.get(id).setWorldPosition(worldPosition);
    }

    public void removeClientEntity(int id) {

    }

    public Map<Integer, ClientEntity> getClientEntities() {
        return clientEntities;
    }
}
