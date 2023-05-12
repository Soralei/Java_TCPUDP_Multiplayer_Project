package com.example.sockets.Client;

import com.example.sockets.Shared.DataActionMapping;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.Socket;

public class ClientManager {
    private final Socket tcpSocket;
    private final DatagramSocket udpSocket;
    private final ObjectInputStream objectInputStream;
    private final DataOutputStream dataOutputStream;
    private final LocalData localData;
    private final String remoteIpAddress;
    private final int tcpRemotePort;
    private final int udpRemotePort;
    private final int udpLocalPort;

    public ClientManager(String remoteIpAddress, int tcpRemotePort, int udpRemotePort) {
        this.tcpRemotePort = tcpRemotePort;
        this.udpRemotePort = udpRemotePort;
        this.remoteIpAddress = remoteIpAddress;
        try {
            // Sets up TCP socket by attempting to connect to the server.
            this.tcpSocket = new Socket(remoteIpAddress, tcpRemotePort);
            // Sets up UDP socket by trying to bind to any available port.
            this.udpSocket = new DatagramSocket();
            // Set up input and output streams for communication back and forth.
            this.objectInputStream = new ObjectInputStream(this.tcpSocket.getInputStream());
            this.dataOutputStream = new DataOutputStream(this.tcpSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Store the UDP port.
        this.udpLocalPort = this.udpSocket.getLocalPort();

        // Set up local data which holds the entities the client knows about.
        // Relies on the server to communicate this information.
        this.localData = new LocalData();

        // Set up the TCP and UDP receiver threads.
        // Allows us to receive TCP and UDP data from the server.
        new ClientTCPReceiver(this).start();
        new ClientUDPReceiver(this).start();

        // Send the local UDP port to the server so that it can communicate back.
        try {
            dataOutputStream.writeInt(DataActionMapping.UDP_REGISTER_PORT.ordinal());
            dataOutputStream.writeInt(this.udpLocalPort);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Gracefully shuts down the client by closing TCP and UDP ports.
    public void shutdown() {
        try { tcpSocket.close(); } catch (IOException e) { e.printStackTrace(); }
        udpSocket.close();
    }

    public Socket getTcpSocket() {
        return tcpSocket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public LocalData getLocalData() {
        return localData;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public int getTcpRemotePort() {
        return tcpRemotePort;
    }

    public int getUdpRemotePort() {
        return udpRemotePort;
    }

    public int getUdpLocalPort() {
        return udpLocalPort;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }
}
