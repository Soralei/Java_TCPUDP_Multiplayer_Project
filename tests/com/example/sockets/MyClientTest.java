package com.example.sockets;

import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MyClientTest {
    private static MyServer myServer;
    @BeforeAll
    static void beforeAll() throws IOException {
        myServer = new MyServer(5555);
    }

    @AfterAll
    static void afterAll() throws IOException {
        myServer.getServerSocket().close();
    }

    @Test
    void clientConnects() throws IOException {
        MyClient myClient = new MyClient("127.0.0.1", 5555);
        myServer.addNetworkedEntity(new Player("test player #1", 25, 25));
        myServer.addNetworkedEntity(new Player("test player #2", 25, 25));
        myServer.broadcastNetworkedEntities();
    }
}