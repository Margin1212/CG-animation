import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MountainThunderFixed extends JPanel {

    private BufferedImage canvas;

    public MountainThunderFixed() {
        canvas = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        drawSceneOnCanvas();

        g2.drawImage(canvas, 0, 0, null);
    }

    // ---------------- Scene Drawing ----------------
    private void drawSceneOnCanvas() {
        drawSkyGradient();
        drawLightning(getWidth() / 2, 0, getWidth() / 2, 300);
        drawGoldenMountain();
    }

    // ---------------- Sky Background (Bresenham-based) ----------------
    private void drawSkyGradient() {
        int height = getHeight();
        int width = getWidth();

        Color top = new Color(255, 223, 105);
        Color bottom = new Color(212, 175, 55);

        for (int y = 0; y < height; y++) {
            float ratio = (float) y / height;
            int r = (int) (top.getRed() * (1 - ratio) + bottom.getRed() * ratio);
            int g = (int) (top.getGreen() * (1 - ratio) + bottom.getGreen() * ratio);
            int b = (int) (top.getBlue() * (1 - ratio) + bottom.getBlue() * ratio);

            Color gradientColor = new Color(r, g, b);

            // ใช้ Bresenham เพื่อวาดเส้นแนวนอนเติมสีพื้นหลัง
            bresenhamLine(0, y, width, y, gradientColor);
        }
    }

    // ---------------- Mountain (Bezier + Polygon) ----------------
    private void drawGoldenMountain() {
        int[] p0 = {0, 600};
        int[] p1 = {150, 300};
        int[] p2 = {450, 300};
        int[] p3 = {600, 600};

        List<int[]> bezierPoints = bezierCurve(p0, p1, p2, p3, 200);

        Polygon mountain = new Polygon();
        for (int[] pt : bezierPoints) {
            mountain.addPoint(pt[0], pt[1]);
        }
        mountain.addPoint(600, 600);
        mountain.addPoint(0, 600);

        // ใช้ fillPolygon() ที่ได้รับอนุญาต
        Graphics2D g2 = canvas.createGraphics();
        g2.setColor(new Color(218, 165, 32));
        g2.fillPolygon(mountain);
        g2.dispose();
    }

    // ---------------- Lightning (Bresenham-based) ----------------
    private void drawLightning(int startX, int startY, int endX, int endY) {
        int segments = 8;
        int[] xOffset = {0, -12, 18, -8, 22, -16, 12, -10, 0};

        int[] xPoints = new int[segments + 1];
        int[] yPoints = new int[segments + 1];

        xPoints[0] = startX;
        yPoints[0] = startY;

        for (int i = 1; i <= segments; i++) {
            xPoints[i] = xPoints[i - 1] + xOffset[i];
            yPoints[i] = startY + (i * (endY - startY)) / segments;
        }

        Color[] lightningColors = {
            new Color(255, 255, 100, 150),
            new Color(255, 255, 180, 200),
            Color.WHITE
        };
        
        int[] thicknesses = {14, 10, 5};

        for (int j = 0; j < lightningColors.length; j++) {
            Color color = lightningColors[j];
            int thickness = thicknesses[j];
            
            for (int i = 0; i < segments; i++) {
                bresenhamThickLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1], color, thickness);
            }
        }
    }

    // ---------------- Algorithms ----------------
    private void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight()) {
            canvas.setRGB(x, y, color.getRGB());
        }
    }

    private void bresenhamLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            putPixel(x0, y0, color);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    private void bresenhamThickLine(int x0, int y0, int x1, int y1, Color color, int thickness) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;
    
        while (true) {
            for (int i = 0; i < thickness; i++) {
                putPixel(x0, y0 + i, color);
            }
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    private List<int[]> bezierCurve(int[] p0, int[] p1, int[] p2, int[] p3, int steps) {
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double x = Math.pow(1 - t, 3) * p0[0] +
                       3 * t * Math.pow(1 - t, 2) * p1[0] +
                       3 * (1 - t) * t * t * p2[0] +
                       t * t * t * p3[0];

            double y = Math.pow(1 - t, 3) * p0[1] +
                       3 * t * Math.pow(1 - t, 2) * p1[1] +
                       3 * (1 - t) * t * t * p2[1] +
                       t * t * t * p3[1];

            points.add(new int[]{(int) x, (int) y});
        }
        return points;
    }

    // ---------------- Main ----------------
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mountain Thunder");
        MountainThunderFixed panel = new MountainThunderFixed();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}