import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class MountainThunder extends JPanel {

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

        // Draw lightning before mountain
        drawLightning(g2, getWidth() / 2, 0, getWidth() / 2, 300);

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

    // Draw a lightning bolt (zigzag) with fixed offsets (no randomness)
    private void drawLightning(Graphics2D g2, int startX, int startY, int endX, int endY) {
        int segments = 8; // number of zigzag segments

        // Fixed horizontal offsets for zigzag shape
        int[] xOffset = {0, -12, 18, -8, 22, -16, 12, -10, 0};

        int[] xPoints = new int[segments + 1];
        int[] yPoints = new int[segments + 1];

        xPoints[0] = startX;
        yPoints[0] = startY;

        for (int i = 1; i < segments; i++) {
            xPoints[i] = xPoints[i - 1] + xOffset[i];
            yPoints[i] = startY + (i * (endY - startY)) / segments;
        }

        xPoints[segments] = endX;
        yPoints[segments] = endY;

        // Outer glow (very thick yellow)
        g2.setStroke(new BasicStroke(14f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 255, 100, 150));
        for (int i = 0; i < segments; i++) {
            g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }

        // Middle glow (bright yellow)
        g2.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 255, 180, 200));
        for (int i = 0; i < segments; i++) {
            g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }

        // Core lightning (white, sharp)
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.WHITE);
        for (int i = 0; i < segments; i++) {
            g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mountain Thunder");
        MountainThunder panel = new MountainThunder();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
