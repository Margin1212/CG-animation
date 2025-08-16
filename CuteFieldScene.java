import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class CuteFieldScene extends JPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cute Field Animation");
        CuteFieldScene panel = new CuteFieldScene();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 1. sky
        g2.setColor(new Color(200, 240, 255));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 2. mountain
        drawMountains(g2);

        // 3. cloud
        drawCloud(g2, 100, 80);
        drawCloud(g2, 350, 60);

        // 4. grass
        g2.setColor(new Color(160, 220, 160));
        g2.fillRect(0, 350, getWidth(), 250);

    }

    // ---------------- PART 1 : MOUNTAINS ----------------
    public void drawMountains(Graphics2D g2) {
        g2.setColor(new Color(100, 180, 200));
        Path2D.Double mountain1 = new Path2D.Double();
        mountain1.moveTo(0, 350);
        mountain1.curveTo(150, 200, 250, 200, 300, 350);
        mountain1.closePath();
        g2.fill(mountain1);

        g2.setColor(new Color(80, 160, 180));
        Path2D.Double mountain2 = new Path2D.Double();
        mountain2.moveTo(200, 350);
        mountain2.curveTo(350, 150, 450, 150, 600, 350);
        mountain2.closePath();
        g2.fill(mountain2);
    }

    // ---------------- PART 2 : CLOUDS ----------------
    private void drawCloud(Graphics2D g2, int x, int y) {
        g2.setColor(Color.WHITE);
        g2.fillOval(x, y, 40, 40);
        g2.fillOval(x + 30, y - 10, 50, 50);
        g2.fillOval(x + 60, y, 40, 40);
    }

    // ---------------- PART 3 : MIDPOINT CIRCLE ALGORITHM ----------------
    private void drawMidpointCircle(Graphics2D g2, int xc, int yc, int r) {
        int x = 0, y = r;
        int d = 1 - r;
        while (x <= y) {
            plotCirclePoints(g2, xc, yc, x, y);
            if (d < 0) {
                d += 2 * x + 3;
            } else {
                d += 2 * (x - y) + 5;
                y--;
            }
            x++;
        }
    }

    private void plotCirclePoints(Graphics2D g2, int xc, int yc, int x, int y) {
        g2.fillRect(xc + x, yc + y, 1, 1);
        g2.fillRect(xc - x, yc + y, 1, 1);
        g2.fillRect(xc + x, yc - y, 1, 1);
        g2.fillRect(xc - x, yc - y, 1, 1);
        g2.fillRect(xc + y, yc + x, 1, 1);
        g2.fillRect(xc - y, yc + x, 1, 1);
        g2.fillRect(xc + y, yc - x, 1, 1);
        g2.fillRect(xc - y, yc - x, 1, 1);
    }
}
