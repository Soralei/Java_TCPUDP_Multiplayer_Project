package com.example.sockets.Server;

import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class ServerUDPReceiver extends Thread {
    private final ServerManager serverManager;

    public ServerUDPReceiver(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void run() {
        super.run();
        while(!serverManager.getUdpSocket().isClosed()) {
            byte[] packetBuffer = new byte[64];
            DatagramPacket packet = new DatagramPacket(packetBuffer, packetBuffer.length);
            try {
                serverManager.getUdpSocket().receive(packet);
            } catch (IOException e) {
                if(!e.getMessage().equals("Socket closed")) {
                    throw new RuntimeException(e);
                }
            }

            ByteBuffer packetBytes = ByteBuffer.wrap(packetBuffer);

            int dataAction = packetBytes.getInt();
            int posX = packetBytes.getInt();
            int posY = packetBytes.getInt();
            if(dataAction == DataActionMapping.ENTITY_POSITION_CHANGE.ordinal()) {
                serverManager.getServerClientData().forEach((k, v) -> {
                    if(v.getTcpSocket().getInetAddress().equals(packet.getAddress()) && packet.getPort() == v.getUdpPort()) {
                        serverManager.getNetworkedEntities().get(v.getClientId()).setWorldPos(new WorldPosition(posX, posY));
                    }
                });
            }
        }
    }
}
