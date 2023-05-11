package com.example.sockets;

import com.example.sockets.Client.ClientManager;
import com.example.sockets.Server.ServerManager;
import com.example.sockets.Shared.WorldPosition;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ConnectClientWithServerTest {
    @Test
    void ServerSendClientDataTest() throws InterruptedException {
        ServerManager serverManager = new ServerManager(5555);

        Thread.sleep(100);

        System.out.println("Client1 connects");
        ClientManager clientManager1 = new ClientManager("127.0.0.1", 5555);
        System.out.println("Client2 connects");
        ClientManager clientManager2 = new ClientManager("127.0.0.1", 5555);
        System.out.println("Client3 connects");
        ClientManager clientManager3 = new ClientManager("127.0.0.1", 5555);

        Thread.sleep(100);

        System.out.println("Server networked entities: " + serverManager.getNetworkedEntities());
        System.out.println("Client1 entities: " + clientManager1.getLocalData().getClientEntities());
        System.out.println("Client2 entities: " + clientManager2.getLocalData().getClientEntities());
        System.out.println("Client3 entities: " + clientManager3.getLocalData().getClientEntities());

        Thread.sleep(100);

        System.out.println("Client4 connects");
        ClientManager clientManager4 = new ClientManager("127.0.0.1", 5555);

        Thread.sleep(100);

        System.out.println("Server networked entities: " + serverManager.getNetworkedEntities());
        System.out.println("Client1 entities: " + clientManager1.getLocalData().getClientEntities());
        System.out.println("Client2 entities: " + clientManager2.getLocalData().getClientEntities());
        System.out.println("Client3 entities: " + clientManager3.getLocalData().getClientEntities());
        System.out.println("Client4 entities: " + clientManager4.getLocalData().getClientEntities());

        Thread.sleep(100);

        System.out.println("Moving server entity #0 to 10, 10");
        serverManager.getNetworkedEntities().get(0).setWorldPos(new WorldPosition(10, 10));

        Thread.sleep(100);

        System.out.println("Checking entity data for client 1...");
        clientManager1.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client1 entity " + k + " has " + v.getWorldPosition()));

        System.out.println("Checking entity data for client 2...");
        clientManager2.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client2 entity " + k + " has " + v.getWorldPosition()));

        System.out.println("Checking entity data for client 4...");
        clientManager4.getLocalData().getClientEntities().forEach((k, v) -> System.out.println("Client4 entity " + k + " has " + v.getWorldPosition()));

        Thread.sleep(100);

        try { serverManager.getSocket().close(); } catch (IOException e) { throw new RuntimeException(e); }

        Thread.sleep(1000);
    }
}