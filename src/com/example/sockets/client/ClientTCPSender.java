package com.example.sockets.client;

import com.example.sockets.shared.ActionMapping;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ClientTCPSender {
    private final MainClient mainClient;
    private final OutputStream outputStream;

    public ClientTCPSender(MainClient mainClient) throws IOException {
        this.mainClient = mainClient;
        this.outputStream = mainClient.getTcpSocket().getOutputStream();
    }

    public void SendUDPPort() throws IOException {
        byte[] bytes = ByteBuffer.allocate(16).putInt(ActionMapping.CLIENT_SYNC_UDP_PORT.ordinal()).putInt(mainClient.getLocalUdpPort()).array();
        outputStream.write(bytes);
        outputStream.flush();
    }

    public MainClient getMainClient() {
        return mainClient;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
