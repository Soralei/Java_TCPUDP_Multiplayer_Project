package com.example.sockets.server;

import com.example.sockets.shared.ActionMapping;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerClientInputManager extends Thread {
    private final ServerClient serverClient;

    public ServerClientInputManager(ServerClient serverClient) {
        this.serverClient = serverClient;
    }

    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(getServerClient().getTcpSocket().getInputStream());
            while(getServerClient().getMainServer().isRunning() && !serverClient.getTcpSocket().isClosed()) {
                if(dataInputStream.available() > 0) {
                    handleAction(dataInputStream);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAction(DataInputStream dataInputStream) {
        try {
            int action = dataInputStream.readInt();
            if(action == ActionMapping.CLIENT_TCP_SYNC_UDP_PORT.ordinal()) {
                setServerClientUDPPort(dataInputStream);
            } else if (action == ActionMapping.CLIENT_TCP_DISCONNECT.ordinal()) {
                clientDisconnect();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setServerClientUDPPort(DataInputStream dataInputStream) {
        try {
            serverClient.setUdpPort(dataInputStream.readInt());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clientDisconnect() {
        getServerClient().disconnect();
    }

    public ServerClient getServerClient() {
        return serverClient;
    }
}
