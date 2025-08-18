import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Goldmou {

    private final BufferedImage canvas;
    private boolean rendered = false; // cache: เรนเดอร์ครั้งเดียว (อยากวาดใหม่ให้เรียก rerender)

    public Goldmou() {
        canvas = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
    }

    /** ให้ Final เรียกเพื่อขอรูปพื้นหลัง */
    public BufferedImage getImage() {
        if (!rendered) {
            renderToCanvas();
            rendered = true;
        }
        return canvas;
    }

    /** บังคับวาดใหม่ (ถ้าต้องการปรับ/สุ่มลายอีกครั้ง) */
    public void rerender() {
        renderToCanvas();
        rendered = true;
    }

    // ---------------- Scene Drawing ----------------
    private void renderToCanvas() {
        drawSkyGradient();
        drawGoldenMountain();  // ภูเขาทอง
    }

    // ---------------- Sky Background (scanline ด้วย Bresenham แนวนอน) ----------------
    private void drawSkyGradient() {
        int width  = canvas.getWidth();
        int height = canvas.getHeight();

        Color top    = new Color(255, 223, 105);
        Color bottom = new Color(212, 175, 55);

        for (int y = 0; y < height; y++) {
            float ratio = (float) y / (float) height;
            int r = (int) (top.getRed()   * (1 - ratio) + bottom.getRed()   * ratio);
            int g = (int) (top.getGreen() * (1 - ratio) + bottom.getGreen() * ratio);
            int b = (int) (top.getBlue()  * (1 - ratio) + bottom.getBlue()  * ratio);
            Color gradientColor = new Color(r, g, b);

            // วาดเส้นแนวนอนเต็มความกว้าง (0..width-1)
            bresenhamLine(0, y, width - 1, y, gradientColor);
        }
    }

    // ---------------- Mountain (Bezier + Polygon fill) ----------------
    private void drawGoldenMountain() {
        int[] p0 = {0, 600};
        int[] p1 = {150, 300};
        int[] p2 = {450, 300};
        int[] p3 = {600, 600};

        List<int[]> bezierPoints = bezierCurve(p0, p1, p2, p3, 200);

        Polygon mountain = new Polygon();
        for (int[] pt : bezierPoints) mountain.addPoint(pt[0], pt[1]);
        mountain.addPoint(600, 600);
        mountain.addPoint(0, 600);

        Graphics2D g2 = canvas.createGraphics();
        g2.setColor(new Color(218, 165, 32)); // golden
        g2.fillPolygon(mountain);
        g2.dispose();
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
            int e2 = err << 1;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 <  dx) { err += dx; y0 += sy; }
        }
    }

    private List<int[]> bezierCurve(int[] p0, int[] p1, int[] p2, int[] p3, int steps) {
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double x = Math.pow(1 - t, 3) * p0[0]
                     + 3 * t * Math.pow(1 - t, 2) * p1[0]
                     + 3 * (1 - t) * t * t * p2[0]
                     + t * t * t * p3[0];

            double y = Math.pow(1 - t, 3) * p0[1]
                     + 3 * t * Math.pow(1 - t, 2) * p1[1]
                     + 3 * (1 - t) * t * t * p2[1]
                     + t * t * t * p3[1];

            points.add(new int[]{(int) x, (int) y});
        }
        return points;
    }
}
