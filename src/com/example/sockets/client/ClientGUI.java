package com.example.sockets.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientGUI {
    private final MainClient mainClient;

    private final JFrame jFrame;

    public ClientGUI(MainClient mainClient) {
        this.mainClient = mainClient;

        jFrame = new JFrame("CLIENT");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setSize(300, 300);
        jFrame.setLocationRelativeTo(null);
        jFrame.setContentPane(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);
                ClientGameLogicData data = mainClient.getClientGameLogic().getClientGameLogicData();
                data.getEntities().forEach((k, v) -> g.drawRect(v.getWorldPosition().getX() - (10 / 2), v.getWorldPosition().getY() - (10 / 2), 10, 10));
                g.setColor(Color.PINK);
                g.drawRect(data.getLocalPosition().getX() - (16 / 2), data.getLocalPosition().getY() - (16 / 2), 16, 16);
            }
        });
        jFrame.setVisible(true);

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mainClient.setRunning(false);
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(getjFrame().isActive()) {
                    if(e.getID() == KeyEvent.KEY_PRESSED) {
                        if(e.getKeyCode() == KeyEvent.VK_W) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingUp(true); }
                        if(e.getKeyCode() == KeyEvent.VK_D) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingRight(true); }
                        if(e.getKeyCode() == KeyEvent.VK_S) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingDown(true); }
                        if(e.getKeyCode() == KeyEvent.VK_A) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingLeft(true); }
                    }
                    if(e.getID() == KeyEvent.KEY_RELEASED) {
                        if(e.getKeyCode() == KeyEvent.VK_W) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingUp(false); }
                        if(e.getKeyCode() == KeyEvent.VK_D) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingRight(false); }
                        if(e.getKeyCode() == KeyEvent.VK_S) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingDown(false); }
                        if(e.getKeyCode() == KeyEvent.VK_A) { mainClient.getClientGameLogic().getClientGameLogicData().setMovingLeft(false); }
                    }
                }
                return false;
            }
        });
    }

    public void doRepaint() {
        jFrame.repaint();
    }

    public MainClient getMainClient() {
        return mainClient;
    }

    public JFrame getjFrame() {
        return jFrame;
    }
}
