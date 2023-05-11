package com.example.sockets.Server;

import com.example.sockets.Shared.WorldPosition;

public class ServerPlayer extends NetworkedEntity {
    private String name;

    public ServerPlayer(ServerManager serverManager, String name, WorldPosition worldPosition) {
        super(serverManager, worldPosition);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
