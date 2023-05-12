package com.example.sockets.Server;

import com.example.sockets.Shared.DataActionMapping;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerClientTCPReceiver extends Thread {
    private final ServerClientData serverClientData;

    public ServerClientTCPReceiver(ServerClientData serverClientData) {
        this.serverClientData = serverClientData;
    }

    @Override
    public void run() {
        super.run();
        try(DataInputStream dataInputStream = new DataInputStream(serverClientData.getTcpSocket().getInputStream())) {
            while(serverClientData.getTcpSocket().isConnected()) {
                if(dataInputStream.available() > 0) {
                    int dataAction = dataInputStream.readInt();
                    if(dataAction == DataActionMapping.UDP_REGISTER_PORT.ordinal()) {
                        serverClientData.setUdpPort(dataInputStream.readInt());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
