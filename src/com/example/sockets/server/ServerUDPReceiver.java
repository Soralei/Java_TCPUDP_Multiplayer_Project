package com.example.sockets.server;

import com.example.sockets.shared.ActionMapping;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class ServerUDPReceiver extends Thread {
    private final MainServer mainServer;

    public ServerUDPReceiver(MainServer mainServer) {
        this.mainServer = mainServer;
    }

    public void run() {
        while(getMainServer().isRunning()) {
            try {
                byte[] data = new byte[32];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                getMainServer().getUdpSocket().receive(packet);
                ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                handleAction(byteBuffer, packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleAction(ByteBuffer byteBuffer, DatagramPacket packet) {
        int action = byteBuffer.getInt();
        if(action == ActionMapping.CLIENT_UDP_SEND_LOCAL_POSITION.ordinal()) {
            clientRequestsPosition(byteBuffer, packet);
        }
    }

    private void clientRequestsPosition(ByteBuffer byteBuffer, DatagramPacket packet) {
        int clientPlayerId = byteBuffer.getInt();
        int posX = byteBuffer.getInt();
        int posY = byteBuffer.getInt();
        ServerClient serverClient = getMainServer().getServerClients().get(clientPlayerId);
        if(serverClient != null) {
            if(serverClient.getUdpPort() == packet.getPort()) {
               NetworkEntity player = getMainServer().getNetworkEntities().get(clientPlayerId);
               player.getWorldPosition().setPosition(posX, posY);
               getMainServer().broadcastEntityPosition(player);
            }
        }
    }

    public MainServer getMainServer() {
        return mainServer;
    }
}
