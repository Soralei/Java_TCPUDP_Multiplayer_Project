package com.example.sockets;

import com.example.sockets.Server.NetworkedEntity;
import com.example.sockets.Server.ServerManager;
import com.example.sockets.Server.ServerPlayer;
import com.example.sockets.Shared.WorldPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainServer extends Thread {
    private final int tcpPort;
    private final int udpPort;
    private boolean isRunning;

    public MainServer(int tcpPort, int udpPort) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.isRunning = true;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        ServerManager serverManager = new ServerManager(this.tcpPort, this.udpPort);

        JFrame frame = new JFrame("MAIN SERVER");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                serverManager.getNetworkedEntities().forEach((k, v) -> g.drawRect(v.getWorldPosition().x(), v.getWorldPosition().y(), 10, 10));
            }
        };
        frame.setContentPane(panel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isRunning = false;
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(frame.isActive()) {
                    if(e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                        spawnClient();
                    }
                }
                return false;
            }
        });

        while(isRunning) {
            frame.repaint();
        }
    }

    public void spawnClient() {
        new MainClient("127.0.0.1", tcpPort, udpPort);
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
