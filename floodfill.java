import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

class floodfill extends JPanel{
    BufferedImage buffer;
    public static void main(String[] args) {
        floodfill m = new floodfill();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Bezier curve,polygons,flood-fill");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void paintComponent(Graphics g) {
        // g.setColor(Color.red);
        // g.fillRect(100, 500,3,3);
        // g.fillRect(200, 200,3,3);
        // g.fillRect(500, 200,3,3);
        // g.fillRect(550, 500,3,3);
        

        // g.setColor(Color.blue);
        // BezierCurve(g, 100, 500,
        // 200, 200,
        // 550, 500 ,
        // 500, 200
        // );

        // Polygon poly = new Polygon();
        //     poly.addPoint(150, 150);
        //     poly.addPoint(250, 100);
        //     poly.addPoint(325, 125);
        //     poly.addPoint(375, 225);
        //     poly.addPoint(400, 325);
        //     poly.addPoint(275, 375);
        //     poly.addPoint(100, 300);
        //     g.drawPolygon(poly);



        buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffer.createGraphics();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 600, 600);

            int xPoly[] = {150, 250, 325, 375, 400, 275, 100};
            int yPoly[] = {150, 100, 125, 225, 325, 375, 300};
            Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);

            g2.setColor(Color.BLACK);   
            g2.drawPolygon(poly);

            FloodFill(20, 150, Color.WHITE.getRGB(), Color.GREEN.getRGB());

            g.drawImage(buffer, 0, 0, null);

            

    }  

    private void BezierCurve (Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){

        //double t = 0.5 ;

        for (double t=0;t<=1;t+=0.001){
            int x = (int)Math.round((Math.pow(1-t,3)) * x1 
                    + 3 * t * Math.pow(1-t,2) *x2
                    +3 * Math.pow(t,2) *(1-t) *x3
                    +Math.pow(t,3) *x4) ;
            int y = (int)Math.round((Math.pow(1-t,3)) * y1 
                    + 3 * t * Math.pow(1-t,2) *y2
                    +3 * Math.pow(t,2) *(1-t) *y3
                    +Math.pow(t,3) *y4) ;

            g.drawRect(x,y,1,1);
        }
    }


    private void FloodFill(int x, int y, int targetColor, int replacementColor) {
        if (targetColor == replacementColor) return;
        if (x < 0 || x >= buffer.getWidth() || y < 0 || y >= buffer.getHeight()) return;
        if (buffer.getRGB(x, y) != targetColor) return;

        Queue<Point> Q = new LinkedList<>();
        Q.add(new Point(x, y));
        buffer.setRGB(x, y, replacementColor);

        while (!Q.isEmpty()) {
            Point p = Q.remove();

            
            fill(Q, p.x + 1, p.y, targetColor, replacementColor);
            fill(Q, p.x - 1, p.y, targetColor, replacementColor);
            fill(Q, p.x, p.y + 1, targetColor, replacementColor);
            fill(Q, p.x, p.y - 1, targetColor, replacementColor);
        }
    }

    private void fill(Queue<Point> Q, int x, int y, int targetColor, int replacementColor) {
        if (x < 0 || x >= buffer.getWidth() || y < 0 || y >= buffer.getHeight()) return;
        if (buffer.getRGB(x, y) == targetColor) {
            buffer.setRGB(x, y, replacementColor);
            Q.add(new Point(x, y));
        }
    }

}