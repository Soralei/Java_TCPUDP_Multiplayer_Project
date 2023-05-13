package com.example.sockets.server;

import com.example.sockets.shared.ActionMapping;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerClient {
    private static int nextClientId = 0;
    public final MainServer mainServer;
    private final int serverClientId;
    private final Socket tcpSocket;
    private final DataOutputStream dataOutputStream;
    private int udpPort;

    public ServerClient(MainServer mainServer, Socket tcpSocket) {
        try {
            this.mainServer = mainServer;
            this.serverClientId = nextClientId++;
            this.tcpSocket = tcpSocket;
            this.dataOutputStream = new DataOutputStream(getTcpSocket().getOutputStream());
            new ServerClientInputManager(this).start();
            this.udpPort = -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        // Remove on the server.
        mainServer.removeServerClientById(getServerClientId());
        mainServer.removeNetworkEntityById(getServerClientId());

        // Attempt to close the client socket.
        try { getTcpSocket().close(); } catch (IOException e) { throw new RuntimeException(e); }

        // Notify all remaining clients of that the client entity was removed.
        getMainServer().getServerClients().forEach((k, v) -> {
            try {
                v.getDataOutputStream().writeInt(ActionMapping.SERVER_TCP_REMOVE_ENTITY.ordinal());
                v.getDataOutputStream().writeInt(getServerClientId());
                v.getDataOutputStream().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void syncSpecificEntity(NetworkEntity entity) {
        try {
            getDataOutputStream().writeInt(ActionMapping.SERVER_TCP_SYNC_ENTITY.ordinal());
            getDataOutputStream().writeInt(entity.getEntityId());
            getDataOutputStream().writeInt(entity.getWorldPosition().getX());
            getDataOutputStream().writeInt(entity.getWorldPosition().getY());
            getDataOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void syncCurrentEntities() {
        getMainServer().getNetworkEntities().forEach((k, v) -> {
            try {
                getDataOutputStream().writeInt(ActionMapping.SERVER_TCP_SYNC_ENTITY.ordinal());
                getDataOutputStream().writeInt(k);
                getDataOutputStream().writeInt(v.getWorldPosition().getX());
                getDataOutputStream().writeInt(v.getWorldPosition().getY());
                getDataOutputStream().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void syncEntityPosition(NetworkEntity entity) {
        try {
            byte[] data = ByteBuffer
                    .allocate(16)
                    .putInt(ActionMapping.SERVER_UDP_SYNC_ENTITY_POSITION.ordinal())
                    .putInt(entity.getEntityId())
                    .putInt(entity.getWorldPosition().getX())
                    .putInt(entity.getWorldPosition().getY())
                    .array();
            DatagramPacket packet = new DatagramPacket(data, data.length, getTcpSocket().getInetAddress(), getUdpPort());
            getMainServer().getUdpSocket().send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void syncPlayerId() {
        try {
            getDataOutputStream().writeInt(ActionMapping.SERVER_TCP_SYNC_PLAYER_ID.ordinal());
            getDataOutputStream().writeInt(getServerClientId());
            getDataOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getServerClientId() {
        return serverClientId;
    }

    public Socket getTcpSocket() {
        return tcpSocket;
    }

    public MainServer getMainServer() {
        return mainServer;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }
}
