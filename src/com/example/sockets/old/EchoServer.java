package com.example.sockets.old;

import java.io.*;
import java.net.*;

public class EchoServer {

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        while((inputLine = in.readLine()) != null) {
            if(".".equals(inputLine)) {
                out.println("good bye");
                break;
            }
            out.println(inputLine);
        }
    }

    public static void main(String[] args) throws IOException {
        EchoServer server = new EchoServer();
        server.start(4444);
    }
}
