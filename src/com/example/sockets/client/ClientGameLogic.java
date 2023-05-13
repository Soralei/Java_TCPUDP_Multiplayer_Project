package com.example.sockets.client;

public class ClientGameLogic extends Thread {
    private final MainClient mainClient;
    private final ClientGameLogicData clientGameLogicData;
    private double TICKS_PER_SECOND;

    public ClientGameLogic(MainClient mainClient) {
        this.mainClient = mainClient;
        this.clientGameLogicData = new ClientGameLogicData(this);
        this.TICKS_PER_SECOND = 60.0;
    }

    public void run() {
        long BEFORE = System.nanoTime();
        double SKIP_TICKS = 1000000000 / getTICKS_PER_SECOND();
        double DELTA = 0;
        while(mainClient.isRunning()) {
            long NOW = System.nanoTime();
            DELTA += (NOW - BEFORE) / SKIP_TICKS;
            BEFORE = NOW;
            while(DELTA >= 1) {
                tick();
                DELTA--;
            }
        }
        getMainClient().getTcpSender().sendDisconnectSignal();
    }

    public void tick() {
        getClientGameLogicData().handleMovement();
        mainClient.getClientGUI().doRepaint();
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
