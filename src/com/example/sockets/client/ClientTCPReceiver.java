package com.example.sockets.client;

import com.example.sockets.shared.ActionMapping;
import com.example.sockets.shared.WorldPosition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ClientTCPReceiver extends Thread {
    private final MainClient mainClient;

    public ClientTCPReceiver(MainClient mainClient) {
        this.mainClient = mainClient;
    }

    public void run() {
        try {
            InputStream stream = mainClient.getTcpSocket().getInputStream();
            while(mainClient.isRunning()) {
                if(stream.available() > 0) {
                    byte[] receivedBytes = stream.readAllBytes();
                    ByteBuffer byteBuffer = ByteBuffer.wrap(receivedBytes);
                    int action = byteBuffer.getInt();
                    handleAction(byteBuffer, action);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAction(ByteBuffer byteBuffer, int action) {
        if(action == ActionMapping.SERVER_SYNC_ID.ordinal()) {
            setLocalPlayerId(byteBuffer);
        } else if (action == ActionMapping.SERVER_SYNC_ENTITY.ordinal()) {
            updateOrAddEntity(byteBuffer);
        } else if (action == ActionMapping.SERVER_REMOVE_ENTITY.ordinal()) {
            entityRemoved(byteBuffer);
        }
    }

    private void setLocalPlayerId(ByteBuffer byteBuffer) {
        int serverId = byteBuffer.getInt();
        mainClient.getClientGameLogic().getClientGameLogicData().setPlayerId(serverId);
    }

    private void updateOrAddEntity(ByteBuffer byteBuffer) {
        int entityId = byteBuffer.getInt();
        int posX = byteBuffer.getInt();
        int posY = byteBuffer.getInt();
        ClientGameLogicData gameLogicData = getMainClient().getClientGameLogic().getClientGameLogicData();
        ClientEntity clientEntity = gameLogicData.getEntityById(entityId);
        if(clientEntity != null) {
            clientEntity.getWorldPosition().setPosition(posX, posY);
        } else {
            gameLogicData.createNewEntity(entityId, new WorldPosition(posX, posY));
        }
    }

    private void entityRemoved(ByteBuffer byteBuffer) {
        int entityId = byteBuffer.getInt();
        ClientGameLogicData gameLogicData = getMainClient().getClientGameLogic().getClientGameLogicData();
        gameLogicData.removeEntityById(entityId);
    }

    public MainClient getMainClient() {
        return mainClient;
    }
}
