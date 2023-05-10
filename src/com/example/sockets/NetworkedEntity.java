package com.example.sockets;

import java.io.Serializable;

public class NetworkedEntity implements Serializable {
    private static int entityIdCounter = 0;
    private final int entityId;
    private int positionX;
    private int positionY;

    public NetworkedEntity(int positionX, int positionY) {
        this.entityId = entityIdCounter++;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
