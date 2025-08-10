import java.awt.*;
import javax.swing.*;

class line extends JPanel{
    public static void main(String[] args) {
    line m = new line();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("First Swing");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void paintComponent(Graphics g) {

        g.setColor(Color.gray);
        Naive(g,100,100,400,200);
        Naive(g,400,300,100,200);
        Naive(g,100,100,200,400);

        // g.setColor(Color.red);
        // DDALine(g, 100, 100, 400,200);
        // DDALine(g, 400, 300, 100,200);
        // DDALine(g, 100, 100, 200,400);

        // g.setColor(Color.blue);
        // Bresenham(g, 100, 100, 400,200);
        // Bresenham(g, 400, 300, 100,200);
        // Bresenham(g, 100, 100, 200,400);


    }   


    private void Naive(Graphics g, int x1, int y1, int x2, int y2) {
        float dx = x2-x1;
        float dy = y2- y1;
        float b = y1 - (dy/dx) * x1;
        
        if (x1 > x2) {
            int tempX = x1, tempY = y1;
            x1 = x2; y1 = y2;
            x2 = tempX; y2 = tempY;
        }

        for (int x = x1; x <= x2; x++) {
            int y = Math.round((dy / dx) * x + b);
            g.drawLine(x, y, x, y);
        }

        g.setColor(Color.gray);
    }


    private void DDALine (Graphics g, int x1 , int y1 ,int x2 , int y2){
        float dx = x2 - x1;
        float dy = y2 - y1;

        if (Math.abs(dx) > Math.abs(dy)) {
            float m = dy / dx;
            float y = y1;
            if (x1 > x2) {
                int tempX = x1, tempY = y1;
                x1 = x2; y1 = y2;
                x2 = tempX; y2 = tempY;
                y = y1;
            }
            for (int x = x1; x <= x2; x++) {
                g.drawLine(x, Math.round(y), x, Math.round(y));
                y += m;
            }
        } else {
            float m_inv = dx / dy;
            float x = x1;
            if (y1 > y2) {
                int tempX = x1, tempY = y1;
                x1 = x2; y1 = y2;
                x2 = tempX; y2 = tempY;
                x = x1;
            }
            for (int y = y1; y <= y2; y++) {
                g.drawLine(Math.round(x), y, Math.round(x), y);
                x += m_inv;
            }
        }
    }


    private void Bresenham (Graphics g ,int x1 , int y1 , int x2 , int y2){
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean steep = dy > dx;

        if (steep) {
            int temp = dx;
            dx = dy;
            dy = temp;
        }

        int D = 2 * dy - dx;
        int x = x1, y = y1;

        for (int i = 0; i <= dx; i++) {
            g.drawLine(x, y, x, y);

            if (D > 0) {
                if (steep) x += sx;
                else y += sy;
                D -= 2 * dx;
            }

            if (steep) y += sy;
            else x += sx;

            D += 2 * dy;
        }
    }



}


    








