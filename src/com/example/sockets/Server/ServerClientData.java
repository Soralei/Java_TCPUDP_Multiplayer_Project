package com.example.sockets.Server;

import com.example.sockets.Shared.DataActionMapping;

import java.io.*;
import java.net.Socket;

public class ServerClientData {
    private static int nextClientId = 0;
    private final int clientId;
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private int udpPort;

    public ServerClientData(Socket socket, ObjectOutputStream objectOutputStream) {
        this.clientId = nextClientId++;
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.udpPort = -1;

        new ServerClientTCPReceiver(this).start();
    }

    public int getClientId() {
        return clientId;
    }

    public Socket getSocket() {
        return socket;
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
}
