package com.example.sockets.Client;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ClientTCPReceiver extends Thread {
    private final ClientManager clientManager;

    public ClientTCPReceiver(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        super.run();
        ObjectInputStream input = clientManager.getObjectInputStream();
        while(clientManager.getTcpSocket() != null && clientManager.getTcpSocket().isConnected()) {
            try {
                if(input.available() > 0) {
                    int dataAction = input.readInt();

                    if(dataAction == DataActionMapping.ENTITY_POSITION_CHANGE) {
                        int uniqueId = input.readInt();
                        int posX = input.readInt();
                        int posY = input.readInt();
                        clientManager.getLocalData().updateClientEntityPosition(uniqueId, new WorldPosition(posX, posY));
                    }

                    if(dataAction == DataActionMapping.ENTITY_ADDED) {
                        int uniqueId = input.readInt();
                        int posX = input.readInt();
                        int posY = input.readInt();
                        clientManager.getLocalData().addClientEntity(uniqueId, new WorldPosition(posX, posY));
                    }

                    if(dataAction == DataActionMapping.ENTITY_REMOVED) {
                        int uniqueId = input.readInt();
                        clientManager.getLocalData().removeClientEntity(uniqueId);
                    }
                }
            } catch (SocketException e) {
                if(!e.getMessage().equals("Socket closed")) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}