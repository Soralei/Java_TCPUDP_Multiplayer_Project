package com.example.sockets.server;

import com.example.sockets.shared.WorldPosition;

public class NetworkEntity {
    private final int entityId;
    private final WorldPosition worldPosition;

    public NetworkEntity(int entityId, WorldPosition worldPosition) {
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
