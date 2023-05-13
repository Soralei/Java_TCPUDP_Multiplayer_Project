package com.example.sockets.client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public class MainClient {
    private final int remoteTcpPort;
    private final int remoteUdpPort;
    private final int localUdpPort;
    private boolean running;
    private final String remoteIpAddress;
    private final Socket tcpSocket;
    private final DatagramSocket udpSocket;
    private final ClientGUI clientGUI;
    private final ClientGameLogic clientGameLogic;
    private final ClientTCPSender tcpSender;
    private final ClientUDPSender clientUDPSender;

    public MainClient(int remoteTcpPort, int remoteUdpPort, String remoteIpAddress) {
        try {
            this.clientGameLogic = new ClientGameLogic(this);
            this.remoteTcpPort = remoteTcpPort;
            this.remoteUdpPort = remoteUdpPort;
            this.remoteIpAddress = remoteIpAddress;
            this.tcpSocket = new Socket(remoteIpAddress, remoteTcpPort);
            this.udpSocket = new DatagramSocket();
            this.running = true;
            this.localUdpPort = this.udpSocket.getLocalPort();
            new ClientTCPReceiver(this).start();
            new ClientUDPReceiver(this).start();
            this.tcpSender = new ClientTCPSender(this);
            tcpSender.SendUDPPort();
            this.clientUDPSender = new ClientUDPSender(this);
            this.clientGUI = new ClientGUI(this);
            this.clientGameLogic.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientTCPSender getTcpSender() {
        return tcpSender;
    }

    public int getRemoteTcpPort() {
        return remoteTcpPort;
    }

    public int getRemoteUdpPort() {
        return remoteUdpPort;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public int getLocalUdpPort() {
        return localUdpPort;
    }

    public Socket getTcpSocket() {
        return tcpSocket;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ClientGUI getClientGUI() {
        return clientGUI;
    }

    public ClientGameLogic getClientGameLogic() {
        return clientGameLogic;
    }

    public ClientUDPSender getClientUDPSender() {
        return clientUDPSender;
    }
}
