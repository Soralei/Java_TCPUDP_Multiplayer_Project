package com.example.sockets.client;

import com.example.sockets.shared.ActionMapping;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public record ClientUDPSender(MainClient mainClient) {
    public void updateLocalPosition() throws IOException {
        ClientGameLogicData gameLogicData = mainClient.getClientGameLogic().getClientGameLogicData();
        byte[] data = ByteBuffer.allocate(16)
                .putInt(ActionMapping.CLIENT_SEND_LOCAL_POSITION.ordinal())
                .putInt(gameLogicData.getPlayerId())
                .putInt(gameLogicData.getLocalPosition().getX())
                .putInt(gameLogicData.getLocalPosition().getY())
                .array();
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(mainClient.getRemoteIpAddress()), mainClient.getRemoteUdpPort());
        mainClient.getUdpSocket().send(packet);
    }
}
