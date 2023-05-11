package com.example.sockets;

import com.example.sockets.Client.ClientManager;
import com.example.sockets.Server.ServerManager;
import com.example.sockets.Shared.WorldPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UDPTest {
    ServerManager serverManager;

    ClientManager c1;
    ClientManager c2;
    ClientManager c3;
    ClientManager c4;
    ClientManager c5;

    @BeforeEach
    void setUp() {
        serverManager = new ServerManager(5555, 5556);
        System.out.println("Server started.");
        c1 = new ClientManager("127.0.0.1", 5555, 5556);
        c2 = new ClientManager("127.0.0.1", 5555, 5556);
        c3 = new ClientManager("127.0.0.1", 5555, 5556);
        c4 = new ClientManager("127.0.0.1", 5555, 5556);
        c5 = new ClientManager("127.0.0.1", 5555, 5556);
        System.out.println("Clients connected");
    }

    @AfterEach
    void tearDown() {
        c1.shutdown();
        c2.shutdown();
        c3.shutdown();
        c4.shutdown();
        c5.shutdown();
        System.out.println("Clients shut down.");
        serverManager.shutdown();
        System.out.println("Server shut down.");
    }

    @Test
    void name() throws InterruptedException {
        Thread.sleep(100);

        System.out.println("Scrambling server entity positions...");
        serverManager.getNetworkedEntities().forEach((k, v) -> v.setWorldPos(new WorldPosition((int)(Math.random() * 101), (int)(Math.random() * 101))));

        Thread.sleep(100);

        System.out.println("Printing server entities...");
        serverManager.getNetworkedEntities().forEach((k, v) -> System.out.println("Server entity#" + v.getUniqueEntityId() + " has " + v.getWorldPosition()));

        System.out.println("Printing local entities for c1...");
        c1.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c2...");
        c2.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c3...");
        c3.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c4...");
        c4.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c5...");
        c5.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));

        Thread.sleep(100);

        System.out.println("Scrambling server entity positions...");
        serverManager.getNetworkedEntities().forEach((k, v) -> v.setWorldPos(new WorldPosition((int)(Math.random() * 101), (int)(Math.random() * 101))));

        Thread.sleep(100);

        System.out.println("Printing server entities...");
        serverManager.getNetworkedEntities().forEach((k, v) -> System.out.println("Server entity#" + v.getUniqueEntityId() + " has " + v.getWorldPosition()));

        System.out.println("Printing local entities for c1...");
        c1.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c2...");
        c2.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c3...");
        c3.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c4...");
        c4.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));
        System.out.println("Printing local entities for c5...");
        c5.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client entity #" + k + " has " + v.getWorldPosition()));

        Thread.sleep(100);
    }
}
