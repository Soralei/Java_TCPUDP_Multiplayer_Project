package com.example.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyClient {
    private final Socket clientSocket;
    private final ObjectInputStream oInputStream;
    private final ClientServerReader clientServerReader;
    private boolean shouldReadFromServer;

    private HashMap<Integer, NetworkedEntity> entities;

    public MyClient(String ip, int port) throws IOException {
        this.clientSocket = new Socket(ip, port);
        this.oInputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.clientServerReader = new ClientServerReader();
        this.shouldReadFromServer = true;
        clientServerReader.start();
    }

    public class ClientServerReader extends Thread {
        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            while(clientSocket.isConnected()) {
                if(shouldReadFromServer) {
                    try {
                        if(oInputStream.available() > 0) {
                            System.out.println("Client: Receiving data... " + oInputStream.available());
                            if(oInputStream.readInt() == 1) {
                                entities = (HashMap<Integer, NetworkedEntity>)oInputStream.readObject();
                                System.out.println("Client: Received entities: ");
                                System.out.println(entities);
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public HashMap<Integer, NetworkedEntity> getEntities() {
        return entities;
    }

    public void setEntities(HashMap<Integer, NetworkedEntity> entities) {
        this.entities = entities;
    }

    public boolean isShouldReadFromServer() {
        return shouldReadFromServer;
    }

    public void setShouldReadFromServer(boolean shouldReadFromServer) {
        this.shouldReadFromServer = shouldReadFromServer;
    }

    public ClientServerReader getClientServerReader() {
        return clientServerReader;
    }

    public ObjectInputStream getoInputStream() {
        return oInputStream;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
