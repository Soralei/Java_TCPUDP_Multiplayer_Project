package com.example.sockets.client;

import com.example.sockets.shared.ActionMapping;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ClientTCPSender {
    private final MainClient mainClient;
    private final DataOutputStream dataOutputStream;

    public ClientTCPSender(MainClient mainClient) throws IOException {
        this.mainClient = mainClient;
        this.dataOutputStream = new DataOutputStream(mainClient.getTcpSocket().getOutputStream());
    }

    public void SendUDPPort() {
        try {
            dataOutputStream.writeInt(ActionMapping.CLIENT_TCP_SYNC_UDP_PORT.ordinal());
            dataOutputStream.writeInt(mainClient.getLocalUdpPort());
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDisconnectSignal() {
        try {
            dataOutputStream.writeInt(ActionMapping.CLIENT_TCP_DISCONNECT.ordinal());
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MainClient getMainClient() {
        return mainClient;
    }

    public OutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
