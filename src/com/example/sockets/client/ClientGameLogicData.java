package com.example.sockets.client;

import com.example.sockets.shared.WorldPosition;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientGameLogicData {
    private final ClientGameLogic clientGameLogic;
    private int playerId;
    private boolean isMovingUp;
    private boolean isMovingRight;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private final WorldPosition localPosition;
    private final ConcurrentMap<Integer, ClientEntity> entities;

    public ClientGameLogicData(ClientGameLogic clientGameLogic) {
        this.playerId = -1;
        this.clientGameLogic = clientGameLogic;
        this.localPosition = new WorldPosition(0, 0);
        this.entities = new ConcurrentHashMap<>();
    }

    public void handleMovement() {
        if(isMovingUp() || isMovingRight() || isMovingDown() || isMovingLeft()) {
            if(isMovingUp()) { localPosition.setY(localPosition.getY() - 5); }
            if(isMovingRight()) { localPosition.setX(localPosition.getX() + 5); }
            if(isMovingDown()) { localPosition.setY(localPosition.getY() + 5); }
            if(isMovingLeft()) { localPosition.setX(localPosition.getX() - 5); }
            try {
                this.clientGameLogic.getMainClient().getClientUDPSender().updateLocalPosition();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createNewEntity(int entityId, WorldPosition worldPosition) {
        entities.putIfAbsent(entityId, new ClientEntity(entityId, worldPosition));
    }

    public void removeEntityById(int entityId) {
        entities.remove(entityId);
    }

    public ClientEntity getEntityById(int entityId) {
        return entities.getOrDefault(entityId, null);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        if(this.playerId == -1) {
            ClientEntity localPlayerEntity = getEntityById(playerId);
            if(localPlayerEntity != null) {
                WorldPosition localPlayerEntityPosition = localPlayerEntity.getWorldPosition();
                getLocalPosition().setPosition(localPlayerEntityPosition.getX(), localPlayerEntityPosition.getY());
            }
        }
        this.playerId = playerId;
    }

    public ConcurrentMap<Integer, ClientEntity> getEntities() {
        return entities;
    }

    public WorldPosition getLocalPosition() {
        return localPosition;
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public void setMovingUp(boolean movingUp) {
        isMovingUp = movingUp;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public void setMovingRight(boolean movingRight) {
        isMovingRight = movingRight;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public void setMovingDown(boolean movingDown) {
        isMovingDown = movingDown;
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        isMovingLeft = movingLeft;
    }

    public ClientGameLogic getClientGameLogic() {
        return clientGameLogic;
    }
}
