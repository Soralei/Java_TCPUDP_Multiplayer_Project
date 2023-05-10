package com.example.sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyServer {
    private final ServerSocket serverSocket;
    private final ArrayList<ServerClientSocket> serverClientSockets;
    private final ClientConnectionManager clientConnectionManager;
    private boolean shouldAcceptConnections;

    private final Map<Integer, NetworkedEntity> networkedEntites;

    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        serverClientSockets = new ArrayList<>();
        this.clientConnectionManager = new ClientConnectionManager();
        this.shouldAcceptConnections = true;
        clientConnectionManager.start();
        this.networkedEntites = new HashMap<>();
    }

    public void broadcastNetworkedEntities() throws IOException {
        int index = 0;
        for (ServerClientSocket socket : serverClientSockets) {
            System.out.println("Broadcasting networked entities to Socket #" + index);
            socket.getObjectOutputStream().writeInt(1);
            socket.getObjectOutputStream().writeObject(networkedEntites);
            socket.getObjectOutputStream().flush();
        }
    }

    public void addNetworkedEntity(NetworkedEntity entity) {
        networkedEntites.put(entity.getEntityId(), entity);
    }

    public Map<Integer, NetworkedEntity> getNetworkedEntites() {
        return networkedEntites;
    }

    public void startAcceptingConnections() {
        shouldAcceptConnections = true;
    }

    public void stopAcceptingConnections() {
        shouldAcceptConnections = false;

    }

    public static class ServerClientSocket {
        private final Socket socket;
        private final ObjectOutputStream objectOutputStream;

        public ServerClientSocket(Socket socket) throws IOException {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        }

        public ObjectOutputStream getObjectOutputStream() {
            return objectOutputStream;
        }

        public Socket getSocket() {
            return socket;
        }
    }

    public class ClientConnectionManager extends Thread {
        @Override
        public void run() {
            while(!serverSocket.isClosed()) {
                if(shouldAcceptConnections) {
                    try {
                        Socket clientConnectSocket = serverSocket.accept();
                        if(!shouldAcceptConnections) {
                            clientConnectSocket.close();
                        } else {
                            serverClientSockets.add(new ServerClientSocket(clientConnectSocket));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Do nothing.
                }
            }
        }
    }

    public boolean isShouldAcceptConnections() {
        return shouldAcceptConnections;
    }

    public void setShouldAcceptConnections(boolean shouldAcceptConnections) {
        this.shouldAcceptConnections = shouldAcceptConnections;
    }

    public ClientConnectionManager getClientConnectionManager() {
        return clientConnectionManager;
    }

    public ArrayList<ServerClientSocket> getServerClientSockets() {
        return serverClientSockets;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
