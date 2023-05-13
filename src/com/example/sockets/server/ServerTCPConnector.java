package com.example.sockets.server;

import java.io.IOException;
import java.net.Socket;

public class ServerTCPConnector extends Thread {
    private final MainServer mainServer;

    public ServerTCPConnector(MainServer mainServer) {
        this.mainServer = mainServer;
    }

    public void run() {
        try {
            while(getMainServer().isRunning()) {
                Socket clientSocket = getMainServer().getTcpSocket().accept();
                getMainServer().createServerClient(clientSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MainServer getMainServer() {
        return mainServer;
    }
}
