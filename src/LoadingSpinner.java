import javax.swing.*;
import java.awt.*;

public class LoadingSpinner extends JPanel implements Runnable {
    private volatile boolean running = false;
    private double angle = 0;

    public LoadingSpinner() {
        setOpaque(false);
        setPreferredSize(new Dimension(100, 100)); // Flexible size
    }

    public void start() {
        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!running) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color overlay = new Color(0, 0, 0, 50);
        g2d.setColor(overlay);
        g2d.fillRect(0, 0, getWidth(), getHeight());


        int size = Math.min(getWidth(), getHeight())*1/10;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;


        g2d.setStroke(new BasicStroke(8));

        g2d.setColor(new Color(179, 179, 184, 255));
        g2d.drawArc(x, y, size, size, 0, 360);


        g2d.setColor(new Color(32, 102, 242, 255));

        g2d.rotate(angle, getWidth() / 2, getHeight() / 2);
        g2d.drawArc(x, y, size, size, 0, 90);

        g2d.dispose();
    }

    @Override
    public void run() {
        while (running) {
            angle += Math.PI / 20;
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}