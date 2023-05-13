package com.example.sockets.client;

import com.example.sockets.shared.ActionMapping;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class ClientUDPReceiver extends Thread {
    private final MainClient mainClient;

    public ClientUDPReceiver(MainClient mainClient) {
        this.mainClient = mainClient;
    }

    public void run() {
        try {
            while(mainClient.isRunning()) {
                byte[] packetBytes = new byte[32];
                DatagramPacket receivedPacket = new DatagramPacket(packetBytes, packetBytes.length);
                mainClient.getUdpSocket().receive(receivedPacket);
                ByteBuffer byteBuffer = ByteBuffer.wrap(packetBytes);
                int action = byteBuffer.getInt();
                handleAction(byteBuffer, action);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAction(ByteBuffer byteBuffer, int action) {
        if(action == ActionMapping.SERVER_SYNC_POSITIONS.ordinal()) {
            syncEntityPosition(byteBuffer);
        }
    }

    private void syncEntityPosition(ByteBuffer byteBuffer) {
        int entityId = byteBuffer.getInt();
        int posX = byteBuffer.getInt();
        int posY = byteBuffer.getInt();
        ClientGameLogicData gameLogicData = getMainClient().getClientGameLogic().getClientGameLogicData();
        ClientEntity clientEntity = gameLogicData.getEntityById(entityId);
        if(clientEntity != null) {
            clientEntity.getWorldPosition().setPosition(posX, posY);
        }
    }

    public MainClient getMainClient() {
        return mainClient;
    }
}
