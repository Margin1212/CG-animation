import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CuteScene extends JPanel {

    private BufferedImage canvas;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cute Field Scene Colored");
        CuteScene panel = new CuteScene();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public CuteScene() {
        canvas = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Sky
        for (int y = 0; y < canvas.getHeight(); y++)
            for (int x = 0; x < canvas.getWidth(); x++)
                canvas.setRGB(x, y, new Color(200, 240, 255).getRGB());

        // 2. Grass
        for (int y = 350; y < canvas.getHeight(); y++)
            for (int x = 0; x < canvas.getWidth(); x++)
                canvas.setRGB(x, y, new Color(160, 220, 160).getRGB());

        // 3. Mountains
        drawGradientMountain(0, 350, 150, 200, 250, 200, 300, 350,
                new Color(100,180,200), new Color(150,210,230));
        drawGradientMountain(200, 350, 350, 150, 450, 150, 600, 350,
                new Color(80,160,180), new Color(120,190,210));

        // 4. Clouds with gradient
        drawGradientCloud(120, 100, 20, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(150, 90, 25, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(180, 100, 20, new Color(255,255,255), new Color(200,230,255));

        drawGradientCloud(370, 80, 20, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(400, 70, 25, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(430, 80, 20, new Color(255,255,255), new Color(200,230,255));

        g.drawImage(canvas, 0, 0, null);
    }

    // ---------------- Gradient Mountain using Bezier + Scanline ----------------
    private void drawGradientMountain(int x0, int y0, int x1, int y1,
                                      int x2, int y2, int x3, int y3,
                                      Color bottomColor, Color topColor) {
        // Compute top edge using Bezier
        ArrayList<Point> topEdge = new ArrayList<>();
        for (double t = 0; t <= 1.0; t += 0.001) {
            int xt = (int)(Math.pow(1 - t, 3) * x0 +
                    3 * Math.pow(1 - t, 2) * t * x1 +
                    3 * (1 - t) * t * t * x2 +
                    t * t * t * x3);
            int yt = (int)(Math.pow(1 - t, 3) * y0 +
                    3 * Math.pow(1 - t, 2) * t * y1 +
                    3 * (1 - t) * t * t * y2 +
                    t * t * t * y3);
            topEdge.add(new Point(xt, yt));
        }

        // Fill mountain with vertical scanline
        int minY = y0, maxY = y0;
        for (Point p : topEdge) {
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        for (int yScan = minY; yScan <= y0; yScan++) {
            int leftX = Integer.MAX_VALUE;
            int rightX = Integer.MIN_VALUE;
            for (Point p : topEdge) {
                if (p.y == yScan) {
                    if (p.x < leftX) leftX = p.x;
                    if (p.x > rightX) rightX = p.x;
                }
            }
            if (leftX > rightX) continue;

            double ratio = (double)(y0 - yScan) / (y0 - minY + 0.01);
            ratio = Math.min(Math.max(ratio, 0), 1);
            Color c = new Color(
                    (int)(topColor.getRed() * ratio + bottomColor.getRed() * (1-ratio)),
                    (int)(topColor.getGreen() * ratio + bottomColor.getGreen() * (1-ratio)),
                    (int)(topColor.getBlue() * ratio + bottomColor.getBlue() * (1-ratio))
            );

            for (int xFill = leftX; xFill <= rightX; xFill++) {
                setPixel(xFill, yScan, c);
            }
        }
    }

    // ---------------- Gradient Cloud using Midpoint Circle ----------------
    private void drawGradientCloud(int xc, int yc, int r, Color topColor, Color bottomColor) {
        int x = 0, y = r;
        int d = 1 - r;
        ArrayList<Point> circlePoints = new ArrayList<>();

        while (x <= y) {
            circlePoints.add(new Point(x, y));
            circlePoints.add(new Point(y, x));
            circlePoints.add(new Point(-x, y));
            circlePoints.add(new Point(-y, x));
            circlePoints.add(new Point(x, -y));
            circlePoints.add(new Point(y, -x));
            circlePoints.add(new Point(-x, -y));
            circlePoints.add(new Point(-y, -x));

            if (d < 0) d += 2 * x + 3;
            else { d += 2 * (x - y) + 5; y--; }
            x++;
        }

        int minY = yc - r;
        int maxY = yc + r;
        for (int yScan = minY; yScan <= maxY; yScan++) {
            int leftX = Integer.MAX_VALUE;
            int rightX = Integer.MIN_VALUE;
            for (Point p : circlePoints) {
                int px = xc + p.x;
                int py = yc + p.y;
                if (py == yScan) {
                    if (px < leftX) leftX = px;
                    if (px > rightX) rightX = px;
                }
            }
            if (leftX > rightX) continue;

            double ratio = (double)(yScan - minY) / (maxY - minY + 0.01);
            ratio = Math.min(Math.max(ratio, 0), 1);
            Color c = new Color(
                    (int)(topColor.getRed() * (1 - ratio) + bottomColor.getRed() * ratio),
                    (int)(topColor.getGreen() * (1 - ratio) + bottomColor.getGreen() * ratio),
                    (int)(topColor.getBlue() * (1 - ratio) + bottomColor.getBlue() * ratio)
            );

            for (int xFill = leftX; xFill <= rightX; xFill++) {
                setPixel(xFill, yScan, c);
            }
        }
    }

    // ---------------- Pixel Helper ----------------
    private void setPixel(int x, int y, Color color) {
        if (x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight())
            canvas.setRGB(x, y, color.getRGB());
    }
}
