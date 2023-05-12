package com.example.sockets.Client;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class ClientUDPReceiver extends Thread {
    private final ClientManager clientManager;

    public ClientUDPReceiver(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            byte[] packetBuffer = new byte[32];
            DatagramPacket packet = new DatagramPacket(packetBuffer, packetBuffer.length);
            try {
                clientManager.getUdpSocket().receive(packet);
            } catch (IOException e) {
                if(!e.getMessage().equals("Socket closed")) {
                    throw new RuntimeException(e);
                }
            }

            ByteBuffer buffer = ByteBuffer.wrap(packetBuffer);

            int dataAction = buffer.getInt();
            if(dataAction == DataActionMapping.ENTITY_POSITION_CHANGE.ordinal()) {
                int entityId = buffer.getInt();
                int entityPosX = buffer.getInt();
                int entityPosY = buffer.getInt();
                clientManager.getLocalData().updateClientEntityPosition(entityId, new WorldPosition(entityPosX, entityPosY));
            }
        }
    }
}
