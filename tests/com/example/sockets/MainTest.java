package com.example.sockets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private MainServer mainServer;

    @BeforeEach
    void setUp() {
        mainServer = new MainServer(5555, 5556);
    }

    @AfterEach
    void tearDown() {
        mainServer.setRunning(false);
    }

    @Test
    void name() throws InterruptedException {
        while(mainServer.isRunning()) { Thread.sleep(100); }
    }
}