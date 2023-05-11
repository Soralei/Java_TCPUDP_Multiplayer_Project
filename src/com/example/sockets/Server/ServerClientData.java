package com.example.sockets.Server;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientData {
    private static int nextClientId = 0;
    private final int clientId;
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;

    public ServerClientData(Socket socket, ObjectOutputStream objectOutputStream) {
        this.clientId = nextClientId++;
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
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
}
