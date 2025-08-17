import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class WhiteBG extends JPanel {

    private BufferedImage canvas;

    public WhiteBG() {
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
        drawWhiteBackground();
        drawLightning(getWidth() / 2, 0, getWidth() / 2, 300);
    }

    // ---------------- Background ----------------
    private void drawWhiteBackground() {
        int width = getWidth();
        int height = getHeight();
        Color white = Color.WHITE;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                putPixel(x, y, white);
            }
        }
    }

    // ---------------- Lightning ----------------
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

        // เปลี่ยนเป็นสายฟ้าสีดำ
        Color[] lightningColors = {
            new Color(0, 0, 0, 180), // ดำโปร่งนิดหน่อย
            new Color(0, 0, 0, 220), // ดำเข้มขึ้น
            Color.BLACK               // ดำสนิท
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

    // ---------------- Main ----------------
    public static void main(String[] args) {
        JFrame frame = new JFrame("White Background with Black Thunder");
        WhiteBG panel = new WhiteBG();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
