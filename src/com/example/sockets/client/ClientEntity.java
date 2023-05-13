package com.example.sockets.client;

import com.example.sockets.shared.WorldPosition;

public class ClientEntity {
    private final int entityId;
    private final WorldPosition worldPosition;

    public ClientEntity(int entityId, WorldPosition worldPosition) {
        this.entityId = entityId;
        this.worldPosition = worldPosition;
    }

    public int getEntityId() {
        return entityId;
    }

    public WorldPosition getWorldPosition() {
        return worldPosition;
    }
}
