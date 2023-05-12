package com.example.sockets;

import com.example.sockets.Client.ClientManager;
import com.example.sockets.Shared.DataActionMapping;
import com.example.sockets.Shared.WorldPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainClient extends Thread {

    private String remoteIpAddress;
    private int tcpRemotePort;
    private int udpRemotePort;
    private WorldPosition localPosition;
    private boolean isMovingUp;
    private boolean isMovingRight;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isRunning;

    public MainClient(String remoteIpAddress, int tcpRemotePort, int udpRemotePort) {
        this.remoteIpAddress = remoteIpAddress;
        this.tcpRemotePort = tcpRemotePort;
        this.udpRemotePort = udpRemotePort;
        this.localPosition = new WorldPosition(50, 50);
        this.isMovingUp = false;
        this.isMovingRight = false;
        this.isMovingDown = false;
        this.isMovingLeft = false;
        this.isRunning = true;
        this.start();
    }

    @Override
    public void run() {
        super.run();

        ClientManager clientManager = new ClientManager(remoteIpAddress, tcpRemotePort, udpRemotePort);

        JFrame frame = new JFrame("MAIN CLIENT");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                g.drawRect(localPosition.x(), localPosition.y(), 10, 10);

                g.setColor(Color.GREEN);
                clientManager.getLocalData().getClientEntities().forEach((k, v) -> g.drawRect(v.getWorldPosition().x(), v.getWorldPosition().y(), 10, 10));
            }
        };
        frame.setContentPane(panel);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(e.getID() == KeyEvent.KEY_PRESSED) {
                    if(e.getKeyCode() == KeyEvent.VK_W) { isMovingUp = true; }
                    if(e.getKeyCode() == KeyEvent.VK_D) { isMovingRight = true; }
                    if(e.getKeyCode() == KeyEvent.VK_S) { isMovingDown = true; }
                    if(e.getKeyCode() == KeyEvent.VK_A) { isMovingLeft = true; }
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if(e.getKeyCode() == KeyEvent.VK_W) { isMovingUp = false; }
                    if(e.getKeyCode() == KeyEvent.VK_D) { isMovingRight = false; }
                    if(e.getKeyCode() == KeyEvent.VK_S) { isMovingDown = false; }
                    if(e.getKeyCode() == KeyEvent.VK_A) { isMovingLeft = false; }
                }
                return false;
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isRunning = false;
            }
        });

        while(isRunning) {
            try { Thread.sleep(30); } catch (InterruptedException e) { throw new RuntimeException(e); }

            if(isMovingUp || isMovingRight || isMovingDown || isMovingLeft) {
                if(isMovingUp) { localPosition = new WorldPosition(localPosition.x(), localPosition.y() - 5); }
                if(isMovingRight) { localPosition = new WorldPosition(localPosition.x() + 5, localPosition.y()); }
                if(isMovingDown) { localPosition = new WorldPosition(localPosition.x(), localPosition.y() + 5); }
                if(isMovingLeft) { localPosition = new WorldPosition(localPosition.x() - 5, localPosition.y()); }

                byte[] bytes = new byte[4];
                bytes[0] = (byte)DataActionMapping.ENTITY_POSITION_CHANGE.ordinal();
                bytes[1] = (byte)localPosition.x();
                bytes[2] = (byte)localPosition.y();
                try {
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(clientManager.getRemoteIpAddress()), clientManager.getUdpRemotePort());
                    clientManager.getUdpSocket().send(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            frame.repaint();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
