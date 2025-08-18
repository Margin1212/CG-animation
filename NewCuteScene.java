import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewCuteScene extends JPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cute Field Animation - Rule Based Fixed");
        NewCuteScene panel = new NewCuteScene();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // SKY
        g2.setColor(new Color(200, 240, 255));
        Polygon sky = new Polygon();
        sky.addPoint(0, 0);
        sky.addPoint(getWidth(), 0);
        sky.addPoint(getWidth(), getHeight());
        sky.addPoint(0, getHeight());
        g2.fillPolygon(sky);

        // MOUNTAINS
        g2.setColor(new Color(100, 180, 200));
        drawBezierMountain(g2, 0, 350, 150, 200, 250, 200, 300, 350);

        g2.setColor(new Color(80, 160, 180));
        drawBezierMountain(g2, 200, 350, 350, 150, 450, 150, 600, 350);

        // GRASS
        g2.setColor(new Color(160, 220, 160));
        Polygon grass = new Polygon();
        grass.addPoint(0, 350);
        grass.addPoint(getWidth(), 350);
        grass.addPoint(getWidth(), getHeight());
        grass.addPoint(0, getHeight());
        g2.fillPolygon(grass);

        // CLOUDS
        g2.setColor(Color.WHITE);
        fillMidpointCircle(g2, 120, 100, 20);
        fillMidpointCircle(g2, 150, 90, 25);
        fillMidpointCircle(g2, 180, 100, 20);
        fillMidpointCircle(g2, 370, 80, 20);
        fillMidpointCircle(g2, 400, 70, 25);
        fillMidpointCircle(g2, 430, 80, 20);

        // TREES
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

    // ---------------- FILL CIRCLE USING POLYGON ----------------
    private void fillCirclePolygon(Graphics2D g2, int xc, int yc, int r) {
        Polygon circle = new Polygon();
        int segments = 40;
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            int x = xc + (int) (r * Math.cos(angle));
            int y = yc + (int) (r * Math.sin(angle));
            circle.addPoint(x, y);
        }
        g2.fillPolygon(circle);
    }

    // ---------------- BEZIER CURVE MOUNTAIN ----------------
    private void drawBezierMountain(Graphics2D g2, int x0, int y0,
                                   int x1, int y1, int x2, int y2,
                                   int x3, int y3) {
        Polygon mountain = new Polygon();
        for (double t = 0; t <= 1.0; t += 0.01) {
            double xt = Math.pow(1 - t, 3) * x0 +
                        3 * Math.pow(1 - t, 2) * t * x1 +
                        3 * (1 - t) * t * t * x2 +
                        t * t * t * x3;
            double yt = Math.pow(1 - t, 3) * y0 +
                        3 * Math.pow(1 - t, 2) * t * y1 +
                        3 * (1 - t) * t * t * y2 +
                        t * t * t * y3;
            mountain.addPoint((int) xt, (int) yt);
        }
        mountain.addPoint(x3, getHeight());
        mountain.addPoint(x0, getHeight());
        g2.fillPolygon(mountain);
    }

    // ---------------- FILLED MIDPOINT CIRCLE ----------------
    private void fillMidpointCircle(Graphics2D g2, int xc, int yc, int r) {
        fillCirclePolygon(g2, xc, yc, r); // ใช้ Polygon แทน drawLine
    }

    // ---------------- BRESENHAM LINE ----------------
    private void drawBresenhamLine(Graphics2D g2, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        while (true) {
            g2.drawLine(x1, y1, x1, y1);
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x1 += sx; }
            if (e2 < dx) { err += dx; y1 += sy; }
        }
    }
}

