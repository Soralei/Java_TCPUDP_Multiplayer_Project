package com.example.sockets.client;

import com.example.sockets.shared.ActionMapping;
import com.example.sockets.shared.WorldPosition;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientTCPReceiver extends Thread {
    private final MainClient mainClient;

    public ClientTCPReceiver(MainClient mainClient) {
        this.mainClient = mainClient;
    }

    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(mainClient.getTcpSocket().getInputStream());
            while(mainClient.isRunning()) {
                if(dataInputStream.available() > 0) {
                    int action = dataInputStream.readInt();
                    handleAction(dataInputStream, action);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAction(DataInputStream dataInputStream, int action) {
        if(action == ActionMapping.SERVER_TCP_SYNC_PLAYER_ID.ordinal()) {
            setLocalPlayerId(dataInputStream);
        } else if (action == ActionMapping.SERVER_TCP_SYNC_ENTITY.ordinal()) {
            updateOrAddEntity(dataInputStream);
        } else if (action == ActionMapping.SERVER_TCP_REMOVE_ENTITY.ordinal()) {
            entityRemoved(dataInputStream);
        }
    }

    private void setLocalPlayerId(DataInputStream dataInputStream) {
        try {
            int serverId = dataInputStream.readInt();
            mainClient.getClientGameLogic().getClientGameLogicData().setPlayerId(serverId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateOrAddEntity(DataInputStream dataInputStream) {
        try {
            int entityId = dataInputStream.readInt();
            int posX = dataInputStream.readInt();
            int posY = dataInputStream.readInt();
            ClientGameLogicData gameLogicData = getMainClient().getClientGameLogic().getClientGameLogicData();
            ClientEntity clientEntity = gameLogicData.getEntityById(entityId);
            if(clientEntity != null) {
                clientEntity.getWorldPosition().setPosition(posX, posY);
            } else {
                gameLogicData.createNewEntity(entityId, new WorldPosition(posX, posY));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void entityRemoved(DataInputStream dataInputStream) {
        try {
            int entityId = dataInputStream.readInt();
            ClientGameLogicData gameLogicData = getMainClient().getClientGameLogic().getClientGameLogicData();
            gameLogicData.removeEntityById(entityId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MainClient getMainClient() {
        return mainClient;
    }
}
