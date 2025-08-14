import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Random;

public class GoldenMountainStatic extends JPanel {
    private Random rand = new Random();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Enable anti-aliasing for smooth curves and lines
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw golden sky background
        GradientPaint sky = new GradientPaint(0, 0, new Color(255, 223, 105),
                                              0, getHeight(), new Color(212, 175, 55));
        g2.setPaint(sky);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw golden mountain
        drawGoldenMountain(g2);

    }

    // Draw a single large golden mountain using Bezier curves
    private void drawGoldenMountain(Graphics2D g2) {
        GradientPaint goldMountain = new GradientPaint(0, 300, new Color(255, 215, 0),
                                                       0, 600, new Color(184, 134, 11));
        g2.setPaint(goldMountain);

        Path2D.Double mountain = new Path2D.Double();
        mountain.moveTo(0, 600);
        mountain.curveTo(150, 300, 450, 300, 600, 600); // Bezier curve for mountain shape
        mountain.closePath();
        g2.fill(mountain);
    }

    // Draw a coin using the Midpoint Circle Algorithm
    private void drawCoin(Graphics2D g2, int centerX, int centerY) {
        g2.setColor(new Color(255, 223, 0)); // Gold color
        int radius = 6;

        // Midpoint Circle Algorithm initialization
        int x = radius;
        int y = 0;
        int decisionOver2 = 1 - x; // Decision parameter

        while (y <= x) {
            plotCirclePoints(g2, centerX, centerY, x, y);
            y++;
            if (decisionOver2 <= 0) {
                decisionOver2 += 2 * y + 1;
            } else {
                x--;
                decisionOver2 += 2 * (y - x) + 1;
            }
        }
    }

    // Draw 8 symmetrical points of a circle
    private void plotCirclePoints(Graphics2D g2, int cx, int cy, int x, int y) {
        g2.drawLine(cx + x, cy + y, cx + x, cy + y);
        g2.drawLine(cx - x, cy + y, cx - x, cy + y);
        g2.drawLine(cx + x, cy - y, cx + x, cy - y);
        g2.drawLine(cx - x, cy - y, cx - x, cy - y);
        g2.drawLine(cx + y, cy + x, cx + y, cy + x);
        g2.drawLine(cx - y, cy + x, cx - y, cy + x);
        g2.drawLine(cx + y, cy - x, cx + y, cy - x);
        g2.drawLine(cx - y, cy - x, cx - y, cy - x);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Golden Mountain - Static Image");
        GoldenMountainStatic panel = new GoldenMountainStatic();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
