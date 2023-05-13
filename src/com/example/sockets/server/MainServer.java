package com.example.sockets.server;

import com.example.sockets.shared.WorldPosition;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MainServer {
    private final int tcpPort;
    private final int udpPort;
    private final ServerSocket tcpSocket;
    private final DatagramSocket udpSocket;
    private final ConcurrentMap<Integer, ServerClient> serverClients;
    private final ConcurrentMap<Integer, NetworkEntity> networkEntities;
    private final ServerGUI serverGUI;
    private boolean running;
    private final ServerGameLogic serverGameLogic;

    public MainServer(int tcpPort, int udpPort) {
        try {
            this.tcpPort = tcpPort;
            this.udpPort = udpPort;
            this.tcpSocket = new ServerSocket(tcpPort);
            this.udpSocket = new DatagramSocket(udpPort);
            this.running = true;
            this.serverClients = new ConcurrentHashMap<>();
            this.networkEntities = new ConcurrentHashMap<>();
            this.serverGUI = new ServerGUI(this);
            new ServerTCPConnector(this).start();
            new ServerUDPReceiver(this).start();
            this.serverGameLogic = new ServerGameLogic(this);
            this.serverGameLogic.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void createServerClient(Socket clientSocket) {
        ServerClient newClient = new ServerClient(this, clientSocket);
        NetworkEntity newEntity = new NetworkEntity(newClient.getServerClientId(), new WorldPosition((int)(Math.random() * 101), (int)(Math.random() * 101)));
        getServerClients().put(newClient.getServerClientId(), newClient);
        getNetworkEntities().put(newEntity.getEntityId(), newEntity);
        newClient.syncCurrentEntities();
        newClient.syncPlayerId();
        getServerClients().forEach((k, v) -> v.syncSpecificEntity(newEntity));
    }

    public void broadcastEntityPosition(NetworkEntity networkEntity) {
        getServerClients().forEach((k, v) -> {
            v.syncEntityPosition(networkEntity);
        });
    }

    public void removeServerClientById(int serverClientId) {
        getServerClients().remove(serverClientId);
    }

    public void removeNetworkEntityById(int entityId) {
        getNetworkEntities().remove(entityId);
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public ServerSocket getTcpSocket() {
        return tcpSocket;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public ConcurrentMap<Integer, ServerClient> getServerClients() {
        return serverClients;
    }

    public ServerGUI getServerGUI() {
        return serverGUI;
    }

    public ConcurrentMap<Integer, NetworkEntity> getNetworkEntities() {
        return networkEntities;
    }

    public ServerGameLogic getServerGameLogic() {
        return serverGameLogic;
    }
}
