package com.example.sockets.Server;

import com.example.sockets.Shared.WorldPosition;

import java.io.Serializable;

public class NetworkedEntity implements Serializable {
    private static int nextUniqueId = 0;
    private final ServerManager serverManager;
    private final int uniqueEntityId;
    private WorldPosition worldPosition;

    public NetworkedEntity(ServerManager serverManager, WorldPosition worldPosition) {
        this.uniqueEntityId = nextUniqueId++;
        this.serverManager = serverManager;
        this.worldPosition = worldPosition;
        serverManager.networkEntityRegister(this);
    }

    public int getUniqueEntityId() {
        return uniqueEntityId;
    }

    public void setWorldPos(WorldPosition worldPosition) {
        this.worldPosition = worldPosition;
        serverManager.networkEntityPositionChange(this, worldPosition);
    }

    public WorldPosition getWorldPosition() {
        return worldPosition;
    }

    public static int getNextUniqueId() {
        return nextUniqueId;
    }
}
