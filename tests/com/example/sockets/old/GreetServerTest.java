package com.example.sockets.old;

import com.example.sockets.old.GreetClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class GreetServerTest {
    @Test
    void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() throws IOException {
        GreetClient client = new GreetClient();
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("hello server");
        Assertions.assertEquals("hello client", response);
    }
}