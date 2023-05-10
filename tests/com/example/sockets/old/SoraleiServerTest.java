package com.example.sockets.old;

import com.example.sockets.old.SoraleiClient;
import com.example.sockets.old.SoraleiServer;
import org.junit.jupiter.api.*;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SoraleiServerTest {
    static SoraleiServer server;
    static SoraleiClient client1;
    static SoraleiClient client2;
    static SoraleiClient client3;
    static SoraleiClient client4;
    static SoraleiClient client5;

    @BeforeAll
    static void beforeAll() throws IOException {
        server = new SoraleiServer(4444);
    }

    @AfterAll
    static void afterAll() throws IOException {
        server.shutDown();
    }

    @Test
    @Order(1)
    void connectClient() throws IOException {
        client1 = new SoraleiClient("127.0.0.1", 4444);
        client2 = new SoraleiClient("127.0.0.1", 4444);
        client3 = new SoraleiClient("127.0.0.1", 4444);
        client4 = new SoraleiClient("127.0.0.1", 4444);
        client5 = new SoraleiClient("127.0.0.1", 4444);
        System.out.println("Client1 bytes? " + client1.getdIn().available());
    }

    @Test
    @Order(2)
    void getConnectedClients() throws IOException {
        int index = 0;
        for (SoraleiServer.ClientThread connectedClient : server.getConnectedClients()) {
            System.out.println("Is client #" + index++ + " connected? " + connectedClient.getClientSocket().isConnected());
        }
    }

    @Test
    @Order(3)
    void sendClientsData() throws IOException {
        int index = 0;
        for (SoraleiServer.ClientThread connectedClient : server.getConnectedClients()) {
            System.out.println("Attempting to send data to client #" + index++);
            connectedClient.sendByte();
        }
    }

    @Test
    @Order(4)
    void getClientsData() throws IOException {
        System.out.println("Client1 bytes? " + client1.getdIn().available());
    }
}