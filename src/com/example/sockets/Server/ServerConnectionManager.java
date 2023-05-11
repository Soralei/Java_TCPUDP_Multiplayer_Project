package com.example.sockets.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerConnectionManager extends Thread {
    private final ServerManager serverManager;

    public ServerConnectionManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void run() {
        super.run();
        while(serverManager.getTcpSocket() != null && !serverManager.getTcpSocket().isClosed()) {
            try {
                Socket clientSocket = serverManager.getTcpSocket().accept();
                synchronized (serverManager.getServerClientData()) {
                    serverManager.handleClientConnects(clientSocket);
                }
            } catch (SocketException e) {
                if(!e.getMessage().equals("Socket closed")) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
