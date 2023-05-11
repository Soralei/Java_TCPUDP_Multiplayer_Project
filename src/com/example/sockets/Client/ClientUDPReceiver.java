package com.example.sockets.Client;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ClientUDPReceiver extends Thread {
    private final ClientManager clientManager;

    public ClientUDPReceiver(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            byte[] buf = new byte[64];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                clientManager.getUdpSocket().receive(packet);
            } catch(SocketException e) {
                if(!e.getMessage().equals("Socket closed")) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf); DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream)) {
                int dataAction = dataInputStream.readInt();
                if(dataAction == DataActionMapping.ENTITY_POSITION_CHANGE) {
                    int entityId = dataInputStream.readInt();
                    int entityPosX = dataInputStream.readInt();
                    int entityPosY = dataInputStream.readInt();
                    clientManager.getLocalData().updateClientEntityPosition(entityId, new WorldPosition(entityPosX, entityPosY));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
