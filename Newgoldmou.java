
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Newgoldmou {

    private final BufferedImage canvas;
    private boolean rendered = false; // cache: เรนเดอร์ครั้งเดียว (อยากวาดใหม่ให้เรียก rerender)

    public Newgoldmou() {
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
        drawClouds();
        drawGoldenMountain();  // ภูเขาทอง
        drawCastles();
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

    // ---------------- Clouds ----------------
    private void drawClouds() {
        Color cloudColor = new Color(255, 255, 255); // white clouds with color on sky

        // Cloud 1 (left side)
        drawCloud(100, 150, 1.0f, cloudColor);

        // Cloud 2 (right side)
        drawCloud(400, 100, 1.2f, cloudColor);

        // Cloud 3 (center top)
        drawCloud(250, 200, 0.8f, cloudColor);
    }

    private void drawCloud(int cx, int cy, float scale, Color color) {
        // Middle lobe
        filledMidpointCircle(cx, cy, (int)(50 * scale), color);

        // Left lobe
        filledMidpointCircle(cx - (int)(40 * scale), cy + (int)(10 * scale), (int)(30 * scale), color);

        // Right lobe
        filledMidpointCircle(cx + (int)(40 * scale), cy + (int)(10 * scale), (int)(35 * scale), color);

        // Top lobe
        filledMidpointCircle(cx, cy - (int)(20 * scale), (int)(25 * scale), color);
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

    // ---------------- Castles ----------------
    private void drawCastles() {
        // Left castle (left of mountain)
        drawCastle(20, 600, true);

        // Right castle (right of mountain)
        drawCastle(440, 600, false);
    }

    private void drawCastle(int baseX, int baseY, boolean isLeft) {
        Color golden = new Color(255, 215, 0); // สีทองอ่อนหลัก
        Color darkGolden = new Color(184, 134, 11); // สีทองเข้ม

        Graphics2D g2 = canvas.createGraphics();

        // Base wall
        int castleWidth = 120; // ความกว้างปราสาท
        int castleHeight = 120; // ความสูงปราสาท
        int endX = isLeft ? baseX + castleWidth : baseX + castleWidth;
        Polygon base = new Polygon();
        base.addPoint(baseX, baseY);
        base.addPoint(endX, baseY);
        base.addPoint(endX, baseY - castleHeight);
        base.addPoint(baseX, baseY - castleHeight);
        g2.setColor(golden);
        g2.fillPolygon(base);

        // Left tower (two tiers)
        int towerWidth = 30;
        int towerHeight = 220;
        int towerX = isLeft ? baseX : endX - towerWidth;
        Polygon leftTowerBase = new Polygon();
        leftTowerBase.addPoint(towerX, baseY - castleHeight);
        leftTowerBase.addPoint(towerX + towerWidth, baseY - castleHeight);
        leftTowerBase.addPoint(towerX + towerWidth, baseY - (castleHeight + 60));
        leftTowerBase.addPoint(towerX, baseY - (castleHeight + 60));
        g2.setColor(golden);
        g2.fillPolygon(leftTowerBase);

        Polygon leftTowerTop = new Polygon();
        leftTowerTop.addPoint(towerX + 5, baseY - (castleHeight + 60));
        leftTowerTop.addPoint(towerX + towerWidth - 5, baseY - (castleHeight + 60));
        leftTowerTop.addPoint(towerX + towerWidth - 5, baseY - towerHeight);
        leftTowerTop.addPoint(towerX + 5, baseY - towerHeight);
        g2.setColor(golden);
        g2.fillPolygon(leftTowerTop);

        // Left tower roof (Bezier curve)
        int[] p0r = {towerX - 5, baseY - towerHeight};
        int[] p1r = {towerX + towerWidth / 2, baseY - (towerHeight + 30)};
        int[] p2r = {towerX + towerWidth / 2, baseY - (towerHeight + 20)};
        int[] p3r = {towerX + towerWidth + 5, baseY - towerHeight};
        List<int[]> leftRoofPoints = bezierCurve(p0r, p1r, p2r, p3r, 30);
        Polygon leftRoof = new Polygon();
        for (int[] pt : leftRoofPoints) leftRoof.addPoint(pt[0], pt[1]);
        leftRoof.addPoint(towerX + towerWidth + 5, baseY - towerHeight);
        leftRoof.addPoint(towerX - 5, baseY - towerHeight);
        g2.setColor(darkGolden);
        g2.fillPolygon(leftRoof);

        // Middle tower (two tiers, higher)
        towerX = isLeft ? baseX + (castleWidth / 2) - (towerWidth / 2) : baseX + (castleWidth / 2) - (towerWidth / 2);
        int middleTowerHeight = 260; // Higher than side towers
        Polygon middleTowerBase = new Polygon();
        middleTowerBase.addPoint(towerX, baseY - castleHeight);
        middleTowerBase.addPoint(towerX + towerWidth, baseY - castleHeight);
        middleTowerBase.addPoint(towerX + towerWidth, baseY - (castleHeight + 60));
        middleTowerBase.addPoint(towerX, baseY - (castleHeight + 60));
        g2.setColor(golden);
        g2.fillPolygon(middleTowerBase);

        Polygon middleTowerTop = new Polygon();
        middleTowerTop.addPoint(towerX + 5, baseY - (castleHeight + 60));
        middleTowerTop.addPoint(towerX + towerWidth - 5, baseY - (castleHeight + 60));
        middleTowerTop.addPoint(towerX + towerWidth - 5, baseY - middleTowerHeight);
        middleTowerTop.addPoint(towerX + 5, baseY - middleTowerHeight);
        g2.setColor(golden);
        g2.fillPolygon(middleTowerTop);

        // Middle tower roof (Bezier curve)
        p0r = new int[]{towerX - 5, baseY - middleTowerHeight};
        p1r = new int[]{towerX + towerWidth / 2, baseY - (middleTowerHeight + 30)};
        p2r = new int[]{towerX + towerWidth / 2, baseY - (middleTowerHeight + 20)};
        p3r = new int[]{towerX + towerWidth + 5, baseY - middleTowerHeight};
        List<int[]> middleRoofPoints = bezierCurve(p0r, p1r, p2r, p3r, 30);
        Polygon middleRoof = new Polygon();
        for (int[] pt : middleRoofPoints) middleRoof.addPoint(pt[0], pt[1]);
        middleRoof.addPoint(towerX + towerWidth + 5, baseY - middleTowerHeight);
        middleRoof.addPoint(towerX - 5, baseY - middleTowerHeight);
        g2.setColor(darkGolden);
        g2.fillPolygon(middleRoof);

        // Right tower (two tiers)
        towerX = isLeft ? endX - towerWidth : baseX;
        Polygon rightTowerBase = new Polygon();
        rightTowerBase.addPoint(towerX, baseY - castleHeight);
        rightTowerBase.addPoint(towerX + towerWidth, baseY - castleHeight);
        rightTowerBase.addPoint(towerX + towerWidth, baseY - (castleHeight + 60));
        rightTowerBase.addPoint(towerX, baseY - (castleHeight + 60));
        g2.setColor(golden);
        g2.fillPolygon(rightTowerBase);

        Polygon rightTowerTop = new Polygon();
        rightTowerTop.addPoint(towerX + 5, baseY - (castleHeight + 60));
        rightTowerTop.addPoint(towerX + towerWidth - 5, baseY - (castleHeight + 60));
        rightTowerTop.addPoint(towerX + towerWidth - 5, baseY - towerHeight);
        rightTowerTop.addPoint(towerX + 5, baseY - towerHeight);
        g2.setColor(golden);
        g2.fillPolygon(rightTowerTop);

        // Right tower roof (Bezier curve)
        p0r = new int[]{towerX - 5, baseY - towerHeight};
        p1r = new int[]{towerX + towerWidth / 2, baseY - (towerHeight + 30)};
        p2r = new int[]{towerX + towerWidth / 2, baseY - (towerHeight + 20)};
        p3r = new int[]{towerX + towerWidth + 5, baseY - towerHeight};
        List<int[]> rightRoofPoints = bezierCurve(p0r, p1r, p2r, p3r, 30);
        Polygon rightRoof = new Polygon();
        for (int[] pt : rightRoofPoints) rightRoof.addPoint(pt[0], pt[1]);
        rightRoof.addPoint(towerX + towerWidth + 5, baseY - towerHeight);
        rightRoof.addPoint(towerX - 5, baseY - towerHeight);
        g2.setColor(darkGolden);
        g2.fillPolygon(rightRoof);

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

    private void filledMidpointCircle(int cx, int cy, int r, Color color) {
        if (r <= 0) return;
        int x = r;
        int y = 0;
        int err = 1 - r;

        while (x >= y) {
            // Fill horizontal lines in all octants
            bresenhamLine(cx - x, cy + y, cx + x, cy + y, color);
            bresenhamLine(cx - x, cy - y, cx + x, cy - y, color);
            bresenhamLine(cx - y, cy + x, cx + y, cy + x, color);
            bresenhamLine(cx - y, cy - x, cx + y, cy - x, color);

            y++;
            if (err < 0) {
                err += 2 * y + 1;
            } else {
                x--;
                err += 2 * (y - x) + 1;
            }
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

    // ---------------- Main Method ----------------
    public static void main(String[] args) {
        // รันใน Event Dispatch Thread เพื่อความปลอดภัยของ GUI
        SwingUtilities.invokeLater(() -> {
            // สร้างอินสแตนซ์ของ goldmoug
            Newgoldmou goldmoug = new Newgoldmou();
            
            // ดึงภาพจาก goldmoug
            goldmoug.rerender(); // เรียก rerender เพื่อให้แน่ใจว่าเรนเดอร์ใหม่
            BufferedImage image = goldmoug.getImage();
            
            // สร้าง JFrame เพื่อแสดงผล
            JFrame frame = new JFrame("Goldmoug Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            
            // เพิ่ม JPanel เพื่อวาดภาพ
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, null);
                }
            };
            
            frame.add(panel);
            frame.setLocationRelativeTo(null); // จัดกึ่งกลางหน้าจอ
            frame.setVisible(true);
        });
    }
}

