package com.example.sockets.old;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SoraleiServer {
    private final ServerSocket serverSocket;
    private final ArrayList<ClientThread> connectedClients;

    public SoraleiServer(int serverPort) throws IOException {
        this.serverSocket = new ServerSocket(serverPort);
        connectedClients = new ArrayList<>();
        new ConnectionManager().start();
    }

    public void shutDown() throws IOException {
        serverSocket.close();
    }

    public class ConnectionManager extends Thread {
        public void run() {
            while(!serverSocket.isClosed()) {
                try {
                    new ClientThread(serverSocket.accept()).start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public class ClientThread extends Thread {
        private final Socket clientSocket;
        private DataOutputStream dOut;
        public ClientThread(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.dOut = new DataOutputStream(this.clientSocket.getOutputStream());
            connectedClients.add(this);
        }

        public void sendByte() throws IOException {
            dOut.writeUTF("Writing UTF stuff");
            dOut.flush();
        }

        public Socket getClientSocket() {
            return clientSocket;
        }
    }

    public ArrayList<ClientThread> getConnectedClients() {
        return connectedClients;
    }
}
