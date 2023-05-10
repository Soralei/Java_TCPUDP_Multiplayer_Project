package com.example.sockets;

public class Player extends NetworkedEntity {
    private String name;
    public Player(String name, int positionX, int positionY) {
        super(positionX, positionY);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
