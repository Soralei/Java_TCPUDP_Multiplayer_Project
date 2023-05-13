package com.example.sockets.server;

public class ServerGameLogic extends Thread {
    private final MainServer mainServer;

    private double TICKS_PER_SECOND;

    public ServerGameLogic(MainServer mainServer) {
        this.mainServer = mainServer;
        this.TICKS_PER_SECOND = 120.0;
    }

    public void run() {
        long BEFORE = System.nanoTime();
        double SKIP_TICKS = 1000000000 / getTICKS_PER_SECOND();
        double DELTA = 0;
        while(mainServer.isRunning()) {
            long NOW = System.nanoTime();
            DELTA += (NOW - BEFORE) / SKIP_TICKS;
            BEFORE = NOW;
            while(DELTA >= 1) {
                tick();
                DELTA--;
            }
        }
    }

    public void tick() {
        mainServer.getServerGUI().doRepaint();
    }

    public MainServer getMainServer() {
        return mainServer;
    }

    public double getTICKS_PER_SECOND() {
        return TICKS_PER_SECOND;
    }

    public void setTICKS_PER_SECOND(double TICKS_PER_SECOND) {
        this.TICKS_PER_SECOND = TICKS_PER_SECOND;
    }
}
