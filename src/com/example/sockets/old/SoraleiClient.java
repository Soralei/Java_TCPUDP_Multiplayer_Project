package com.example.sockets.old;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class SoraleiClient extends Thread {
    private final Socket socket;
    private final DataInputStream dIn;

    public SoraleiClient(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.dIn = new DataInputStream(this.socket.getInputStream());
    }

    public DataInputStream getdIn() {
        return dIn;
    }

    public void stopConnection() throws IOException {
        socket.close();
    }
}
