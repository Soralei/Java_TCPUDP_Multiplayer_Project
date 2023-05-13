package com.example.sockets.client;

import java.io.IOException;

public class ClientGameLogic {
    private final MainClient mainClient;
    private final ClientGameLogicData clientGameLogicData;
    private boolean active;
    private double TICKS_PER_SECOND;

    public ClientGameLogic(MainClient mainClient, boolean startImmediately) {
        this.mainClient = mainClient;
        this.clientGameLogicData = new ClientGameLogicData(this);
        if(startImmediately) { activate(); }
        setTICKS_PER_SECOND(60.0);
    }

    public void tick() {
        getClientGameLogicData().handleMovement();
        mainClient.getClientGUI().doRepaint();
    }

    public void activate() {
        active = true;
        long BEFORE = System.nanoTime();
        double SKIP_TICKS = 1000000000 / getTICKS_PER_SECOND();
        double DELTA = 0;
        while(isActive() && mainClient.isRunning()) {
            long NOW = System.nanoTime();
            DELTA += (NOW - BEFORE) / SKIP_TICKS;
            BEFORE = NOW;
            while(DELTA >= 1) {
                tick();
                DELTA--;
            }
        }
        if(isActive()) { disable(); }
    }

    public void disable() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public double getTICKS_PER_SECOND() {
        return TICKS_PER_SECOND;
    }

    public void setTICKS_PER_SECOND(double TICKS_PER_SECOND) {
        this.TICKS_PER_SECOND = TICKS_PER_SECOND;
    }

    public MainClient getMainClient() {
        return mainClient;
    }

    public ClientGameLogicData getClientGameLogicData() {
        return clientGameLogicData;
    }
}
