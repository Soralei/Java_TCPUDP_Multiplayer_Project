package com.example.sockets.Client;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import java.io.IOException;
import java.net.DatagramPacket;

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

            System.out.println("Checking bytes");
            for (byte b : packetBuffer) {
                System.out.println(b);
            }
            System.out.println("Done checking bytes");

            int nextByte = 0;
            int dataAction = packetBuffer[nextByte++];
            if(dataAction == DataActionMapping.ENTITY_POSITION_CHANGE.ordinal()) {
                int entityId = packetBuffer[nextByte++];
                int entityPosX = packetBuffer[nextByte++];
                int entityPosY = packetBuffer[nextByte++];
                System.out.println("entityId " + entityId + " entityPosX " + entityPosX + " entityPosY" + entityPosY);
                clientManager.getLocalData().updateClientEntityPosition(entityId, new WorldPosition(entityPosX, entityPosY));
            }
        }
    }
}
