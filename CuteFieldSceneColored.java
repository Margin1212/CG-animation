import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CuteFieldSceneColored {   // << ไม่ต้อง extends JPanel แล้วก็ได้
    private BufferedImage canvas;
    private boolean rendered = false;  // cache ไว้ เรนเดอร์ครั้งเดียวพอ

    public CuteFieldSceneColored() {
        canvas = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
    }

    /** ให้ Final เรียกใช้: คืนภาพพื้นหลัง */
    public BufferedImage getImage() {
        if (!rendered) {
            renderToCanvas();
            rendered = true;
        }
        return canvas;
    }

    /** ถ้าภายหลังอยากเปลี่ยนขนาด/บังคับวาดใหม่ เรียก method นี้ได้ */
    public void rerender() {
        renderToCanvas();
        rendered = true;
    }

    // --------------------- วาดพื้นหลังลง canvas ---------------------
    private void renderToCanvas() {
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

        // 4. Clouds
        drawGradientCloud(120, 100, 20, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(150,  90, 25, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(180, 100, 20, new Color(255,255,255), new Color(200,230,255));

        drawGradientCloud(370,  80, 20, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(400,  70, 25, new Color(255,255,255), new Color(200,230,255));
        drawGradientCloud(430,  80, 20, new Color(255,255,255), new Color(200,230,255));
    
        // TREES
        Graphics2D g2 = canvas.createGraphics();
        drawTree(g2, 100, 420);
        drawTree(g2, 500, 430);
        
    
    
    }


    // ---------------- DRAW TREE ----------------
    private void drawTree(Graphics2D g2, int baseX, int baseY) {
        // trunk
        g2.setColor(new Color(120, 70, 20));
        int trunkWidth = 20, trunkHeight = 60;
        Polygon trunk = new Polygon();
        trunk.addPoint(baseX - trunkWidth / 2, baseY);
        trunk.addPoint(baseX + trunkWidth / 2, baseY);
        trunk.addPoint(baseX + trunkWidth / 2, baseY - trunkHeight);
        trunk.addPoint(baseX - trunkWidth / 2, baseY - trunkHeight);
        g2.fillPolygon(trunk);

        // leaves
        g2.setColor(new Color(40, 180, 60));
        drawBezierLeaf(g2, baseX, baseY - trunkHeight - 10, 25);
        drawBezierLeaf(g2, baseX - 20, baseY - trunkHeight, 20);
        drawBezierLeaf(g2, baseX + 20, baseY - trunkHeight, 20);
        drawBezierLeaf(g2, baseX, baseY - trunkHeight - 30, 20);
    }

     // ---------------- BEZIER LEAF ----------------
    private void drawBezierLeaf(Graphics2D g2, int xc, int yc, int r) {
        Polygon leaf = new Polygon();
        int segments = 8;
        double angleStep = 2 * Math.PI / segments;

        for (int i = 0; i < segments; i++) {
            double t1 = i * angleStep;
            double t2 = (i + 1) * angleStep;

            double x0 = xc + r * Math.cos(t1);
            double y0 = yc + r * Math.sin(t1);
            double x3 = xc + r * Math.cos(t2);
            double y3 = yc + r * Math.sin(t2);

            double x1 = xc + r * 1.2 * Math.cos(t1 + angleStep * 0.25);
            double y1 = yc + r * 1.2 * Math.sin(t1 + angleStep * 0.25);
            double x2 = xc + r * 1.2 * Math.cos(t2 - angleStep * 0.25);
            double y2 = yc + r * 1.2 * Math.sin(t2 - angleStep * 0.25);

            for (double t = 0; t <= 1.0; t += 0.01) {
                double xt = Math.pow(1 - t, 3) * x0 +
                            3 * Math.pow(1 - t, 2) * t * x1 +
                            3 * (1 - t) * t * t * x2 +
                            t * t * t * x3;
                double yt = Math.pow(1 - t, 3) * y0 +
                            3 * Math.pow(1 - t, 2) * t * y1 +
                            3 * (1 - t) * t * t * y2 +
                            t * t * t * y3;
                leaf.addPoint((int) xt, (int) yt);
            }
        }
        g2.fillPolygon(leaf);
    }

    // ---------------- Gradient Mountain using Bezier + Scanline ----------------
    private void drawGradientMountain(int x0, int y0, int x1, int y1,
                                      int x2, int y2, int x3, int y3,
                                      Color bottomColor, Color topColor) {
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

        int minY = y0;
        for (Point p : topEdge) if (p.y < minY) minY = p.y;

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
                    (int)(topColor.getRed()   * ratio + bottomColor.getRed()   * (1-ratio)),
                    (int)(topColor.getGreen() * ratio + bottomColor.getGreen() * (1-ratio)),
                    (int)(topColor.getBlue()  * ratio + bottomColor.getBlue()  * (1-ratio))
            );

            for (int xFill = leftX; xFill <= rightX; xFill++)
                setPixel(xFill, yScan, c);
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
                    (int)(topColor.getRed()   * (1 - ratio) + bottomColor.getRed()   * ratio),
                    (int)(topColor.getGreen() * (1 - ratio) + bottomColor.getGreen() * ratio),
                    (int)(topColor.getBlue()  * (1 - ratio) + bottomColor.getBlue()  * ratio)
            );

            for (int xFill = leftX; xFill <= rightX; xFill++)
                setPixel(xFill, yScan, c);
        }
    }

    // ---------------- Pixel Helper ----------------
    private void setPixel(int x, int y, Color color) {
        if (x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight())
            canvas.setRGB(x, y, color.getRGB());
    }
}
