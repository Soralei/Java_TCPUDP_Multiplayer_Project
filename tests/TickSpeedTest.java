import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TickSpeedTest {
    public int x = 100;
    public int y = 100;

    @Test
    void name() {
        JFrame frame = new JFrame("Tick Test");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);
                g.drawRect(x, y, 10, 10);
            }
        });
        frame.setVisible(true);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(e.getID() == KeyEvent.KEY_PRESSED) {
                    if(e.getKeyCode() == KeyEvent.VK_W) { y -= 5; }
                    if(e.getKeyCode() == KeyEvent.VK_D) { x += 5; }
                    if(e.getKeyCode() == KeyEvent.VK_S) { y += 5; }
                    if(e.getKeyCode() == KeyEvent.VK_A) { x -= 5; }
                }
                return false;
            }
        });


        long BEFORE = System.nanoTime();
        double TICKS_PER_SECOND = 60.0;
        double SKIP_TICKS = 1000000000 / TICKS_PER_SECOND;
        double DELTA = 0;
        while(true) {
            long NOW = System.nanoTime();
            DELTA += (NOW - BEFORE) / SKIP_TICKS;
            BEFORE = NOW;
            while(DELTA >= 1) {
                System.out.println("Tick.");
                frame.repaint();
                DELTA--;
            }
        }
    }
}
