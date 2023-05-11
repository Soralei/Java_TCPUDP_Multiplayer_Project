package com.example.sockets.Client;

import com.example.sockets.Shared.WorldPosition;

public class ClientEntity {
    private final int uniqueId;
    private WorldPosition worldPosition;

    public ClientEntity(int uniqueId, WorldPosition worldPosition) {
        this.uniqueId = uniqueId;
        this.worldPosition = worldPosition;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public WorldPosition getWorldPosition() {
        return worldPosition;
    }

    public void setWorldPosition(WorldPosition worldPosition) {
        this.worldPosition = worldPosition;
    }
}
