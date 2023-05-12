package com.example.sockets.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientData {
    private static int nextClientId = 0;
    private final int clientId;
    private final ServerManager serverManager;
    private final Socket tcpSocket;
    private final ObjectOutputStream objectOutputStream;
    private int udpPort;
    private ServerPlayer serverPlayer;

    public ServerClientData(ServerManager serverManager, Socket tcpSocket) {
        this.serverManager = serverManager;
        this.clientId = nextClientId++;
        this.tcpSocket = tcpSocket;
        this.udpPort = -1;
        try { this.objectOutputStream = new ObjectOutputStream(this.tcpSocket.getOutputStream()); } catch (IOException e) { throw new RuntimeException(e); }

        new ServerClientTCPReceiver(this).start();
    }

    public int getClientId() {
        return clientId;
    }

    public Socket getTcpSocket() {
        return tcpSocket;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public void setServerPlayer(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }
}
