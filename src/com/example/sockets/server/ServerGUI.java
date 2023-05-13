package com.example.sockets.server;

import com.example.sockets.client.MainClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ServerGUI {
    private final MainServer mainServer;
    private final JFrame jFrame;

    public ServerGUI(MainServer mainServer) {
        this.mainServer = mainServer;

        jFrame = new JFrame("SERVER");
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setContentPane(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                getMainServer().getNetworkEntities().forEach((k, v) -> g.drawRect(v.getWorldPosition().getX() - (10/2), v.getWorldPosition().getY() - (10/2), 10, 10));
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(getjFrame().isActive()) {
                    if(e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                        new MainClient(getMainServer().getTcpPort(), getMainServer().getUdpPort(), "127.0.0.1");
                    }
                }
                return false;
            }
        });

        jFrame.setVisible(true);
    }

    void doRepaint() {
        jFrame.repaint();
    }

    public MainServer getMainServer() {
        return mainServer;
    }

    public JFrame getjFrame() {
        return jFrame;
    }
}
