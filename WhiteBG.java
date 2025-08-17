import java.awt.*;
import java.awt.image.BufferedImage;

public class WhiteBG {

    private final BufferedImage canvas;
    private boolean rendered = false; // cache: เรนเดอร์ครั้งเดียว (อยากสุ่มใหม่ก็เรียก rerender)

    public WhiteBG() {
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

    /** บังคับวาดใหม่ (ถ้าต้องการปรับอะไร) */
    public void rerender() {
        renderToCanvas();
        rendered = true;
    }

    // ---------------- Scene Drawing ----------------
    private void renderToCanvas() {
        drawWhiteBackground();
        int w = canvas.getWidth();
        drawLightning(w / 2, 0, w / 2, 300); // สายฟ้าดำบนพื้นขาว
    }

    // ---------------- Background ----------------
    private void drawWhiteBackground() {
        int w = canvas.getWidth(), h = canvas.getHeight();
        int rgb = Color.WHITE.getRGB();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                canvas.setRGB(x, y, rgb);
            }
        }
    }

    // ---------------- Lightning (สีดำ) ----------------
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
            new Color(0, 0, 0, 180),
            new Color(0, 0, 0, 220),
            Color.BLACK
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
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 <  dx) { err += dx; y0 += sy; }
        }
    }
}
