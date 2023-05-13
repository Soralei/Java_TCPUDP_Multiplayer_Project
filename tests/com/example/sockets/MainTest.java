package com.example.sockets;

import com.example.sockets.server.MainServer;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    void name() {
        MainServer mainServer = new MainServer(5555, 5556);

        while(mainServer.isRunning()) {
            // Keep running.
        }
    }
}
