import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GoldenMountainColoredFixed extends JPanel {

    private BufferedImage canvas;

    public GoldenMountainColoredFixed() {
        canvas = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
        drawSceneOnCanvas();
    }

    private void drawSceneOnCanvas() {
        drawSkyGradient();
        drawGoldenMountain();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null);
    }

    // ---------------- SKY ----------------
    private void drawSkyGradient() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Color top = new Color(255, 223, 105);
        Color bottom = new Color(212, 175, 55);

        for (int y = 0; y < height; y++) {
            float ratio = (float) y / (height - 1);
            int r = (int) (top.getRed() * (1 - ratio) + bottom.getRed() * ratio);
            int g = (int) (top.getGreen() * (1 - ratio) + bottom.getGreen() * ratio);
            int b = (int) (top.getBlue() * (1 - ratio) + bottom.getBlue() * ratio);
            int color = new Color(r, g, b).getRGB();

            for (int x = 0; x < width; x++) {
                canvas.setRGB(x, y, color);
            }
        }
    }

    // ---------------- MOUNTAIN ----------------
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

        int minY = 300;
        int maxY = 600;
        Color top = new Color(255, 215, 0);
        Color bottom = new Color(184, 134, 11);

        for (int y = minY; y <= maxY; y++) {
            float ratio = (float) (y - minY) / (maxY - minY);
            int r = (int) (top.getRed() * (1 - ratio) + bottom.getRed() * ratio);
            int g = (int) (top.getGreen() * (1 - ratio) + bottom.getGreen() * ratio);
            int b = (int) (top.getBlue() * (1 - ratio) + bottom.getBlue() * ratio);
            int color = new Color(r, g, b).getRGB();

            int[] intersections = scanlineIntersections(mountain, y);
            for (int i = 0; i < intersections.length; i += 2) {
                for (int x = intersections[i]; x <= intersections[i + 1]; x++) {
                    canvas.setRGB(x, y, color);
                }
            }
        }
    }

    private int[] scanlineIntersections(Polygon poly, int y) {
        List<Integer> xs = new ArrayList<>();
        int n = poly.npoints;
        for (int i = 0; i < n; i++) {
            int x0 = poly.xpoints[i];
            int y0 = poly.ypoints[i];
            int x1 = poly.xpoints[(i + 1) % n];
            int y1 = poly.ypoints[(i + 1) % n];
            if ((y0 <= y && y1 > y) || (y1 <= y && y0 > y)) {
                int x = x0 + (y - y0) * (x1 - x0) / (y1 - y0);
                xs.add(x);
            }
        }
        xs.sort(Integer::compare);
        int[] arr = new int[xs.size()];
        for (int i = 0; i < xs.size(); i++) arr[i] = xs.get(i);
        return arr;
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Golden Mountain Colored Fixed");
        GoldenMountainColoredFixed panel = new GoldenMountainColoredFixed();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
