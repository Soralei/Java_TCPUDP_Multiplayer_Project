package com.example.sockets.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientManager {
    private final Socket socket;
    private final ObjectInputStream objectInputStream;
    private final LocalData localData;
    private final ClientInputManager clientInputManager;

    public ClientManager(String ipAddress, int port) {
        try {
            this.socket = new Socket(ipAddress, port);
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.localData = new LocalData();
        this.clientInputManager = new ClientInputManager(this);
        this.clientInputManager.start();
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public LocalData getLocalData() {
        return localData;
    }

    public ClientInputManager getClientInputManager() {
        return clientInputManager;
    }
}
