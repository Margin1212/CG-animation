import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;
import javax.swing.*;

class Final extends JPanel{
    BufferedImage buffer;

    
    public static void main(String[] args) {
        Final m = new Final();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("hoohoo-1");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        // new Timer(16, e -> {
        //     m.offsetX += m.direction * 2; // ขยับทีละ 2 px

        //     // ถ้าชนขอบ 600 หรือ 0 ให้สลับทิศ
        //     if (m.offsetX >= 600) {
        //         m.direction = -1;
        //     } else if (m.offsetX <= 0) {
        //         m.direction = 1;
        //     }

        //     m.repaint();
        // }).start();
    }

    public Final() {
        buffer = new BufferedImage(600,600,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setColor(Color.WHITE); 
        g2.fillRect(0,0,600,600);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics g2 = buffer.getGraphics();
        
        //g2.translate(offsetX, 0);


        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // g2.setColor(Color.BLACK);

        CuteFieldScene bg =new CuteFieldScene();
        //bg.drawMountains(g2);

        
        
        head(g2);
        face(g2);
        
        g.drawImage(buffer,0,0,null);
    

    }




    private void head(Graphics g){
        // --- หัว (Ellipse) ---
        g.setColor(Color.BLACK);
        drawEllipseMidpoint(g, 165+132, 170+125, 132, 125);
        floodFill(250, 250, new Color(255,230,140));

        // --- หาง (Ellipse) ---
        g.setColor(Color.BLACK);
        drawEllipseMidpoint(g, 275+105, 105+155, 105, 155); // (cx,cy,rx,ry)
        floodFill(380, 260, new Color(173,216,230));

        // Wool tail → ใช้ Bresenham Line
        int tailCenterX = 380;
        int tailCenterY = 260;
        int tailRX = 85;
        int tailRY = 135;
        for (int i = -140; i <= 70; i += 15) {
            double angle = Math.toRadians(i);
            int x1 = (int)(tailCenterX + tailRX * Math.cos(angle));
            int y1 = (int)(tailCenterY + tailRY * Math.sin(angle));
            int x2 = (int)(tailCenterX + (tailRX + 20) * Math.cos(angle));
            int y2 = (int)(tailCenterY + (tailRY + 20) * Math.sin(angle));
            drawLineBresenham(g, x1, y1, x2, y2);
        }

        // --- ตัว (Ellipse) ---
        drawEllipseMidpoint(g, 195+105, 275+90, 105, 90);
        floodFill(280, 350, new Color(255,230,140));


        

        // --- แก้มซ้าย (Bezier curve + Line) ---
        drawLineBresenham(g, 170, 275, 135, 275);
        drawQuadBezier(g, 135, 275, 125, 315, 155, 350);
        drawLineBresenham(g, 155, 350, 180, 340);
        drawLineBresenham(g, 180, 340, 170, 275);

        // --- แก้มขวา ---
        drawLineBresenham(g, 425, 275, 460, 275);
        drawQuadBezier(g, 460, 275, 465, 315, 445, 350);
        drawLineBresenham(g, 445, 350, 420, 340);
        drawLineBresenham(g, 420, 340, 425, 275);

        // --- หูซ้าย ---
        drawEllipseMidpoint(g, 175+45, 120+45, 45, 45);
        floodFill(200, 150, new Color(255,230,140));

        // --- หูขวา ---
        drawEllipseMidpoint(g, 330+45, 120+45, 45, 45);
        floodFill(200, 150, new Color(255,230,140));

        // --- เส้นในหู (Bezier curves) ---
        drawQuadBezier(g, 215,190, 215,180, 230,175);
        drawQuadBezier(g, 230,175, 215,160, 220,140);
        drawQuadBezier(g, 220,165, 210,165, 205,170);

        drawQuadBezier(g, 370,190, 365,180, 350,175);
        drawQuadBezier(g, 350,175, 365,160, 360,140);
        drawQuadBezier(g, 360,165, 365,160, 380,170);

        // --- มือซ้าย ---
        drawLineBresenham(g,200,375,160,375);
        drawQuadBezier(g,160,375,140,390,170,400);
        drawLineBresenham(g,170,400,205,395);
        drawLineBresenham(g,205,395,200,375);

        // --- มือขวา ---
        drawLineBresenham(g,375,355,370,385);
        drawQuadBezier(g,370,385,360,405,390,395);
        drawLineBresenham(g,390,395,405,360);
        drawLineBresenham(g,405,360,375,355);

        // --- ขาซ้าย ---
        drawLineBresenham(g,250,440,250,470);
        drawQuadBezier(g,250,470,260,480,280,470);
        drawLineBresenham(g,280,470,280,450);
        drawLineBresenham(g,280,450,250,440);

        // --- ขาขวา ---
        drawLineBresenham(g,310,450,310,470);
        drawQuadBezier(g,310,470,320,480,340,470);
        drawLineBresenham(g,340,470,340,445);
        drawLineBresenham(g,340,445,310,450);
    }



    private void face(Graphics g){
        // --- ดวงตา (วงรี) ---
        drawEllipseMidpoint(g, 225+20, 270+17, 20, 17); // ตาซ้าย
        floodFill(240, 280, Color.BLACK);
        drawEllipseMidpoint(g, 225+20, 272+12, 20, 12); // ตาซ้าย inner
        floodFill(330, 280, Color.BLACK);
        drawEllipseMidpoint(g, 315+20, 270+17, 20, 17); // ตาขวา
        floodFill(330, 280, Color.BLACK);
        drawEllipseMidpoint(g, 315+20, 272+12, 20, 12); // ตาขวา inner
        floodFill(330, 280, Color.BLACK);

        // --- ดวงตาสีฟ้า ---
        drawEllipseMidpoint(g, 275+16, 270+16, 16, 16); 
        floodFill(285, 280, new Color(173,216,230));

        // --- แก้มชมพู ---
        drawEllipseMidpoint(g, 185+25, 295+20, 25, 20);
        floodFill(200, 310, new Color(255,153,204));
        drawEllipseMidpoint(g, 345+25, 295+20, 25, 20);
        floodFill(360, 310, new Color(255,153,204));

        // --- ปาก ---
        drawEllipseMidpoint(g, 288+2, 310+2, 2, 2); // จุดกลางปาก

        // ปากโค้งซ้าย
        drawQuadBezier(g, 290,315, 280,335, 265,320);

        // ปากโค้งขวา
        drawQuadBezier(g, 290,315, 300,335, 315,320);

        // เส้นปากล่าง
        drawQuadBezier(g, 283,340, 290,345, 298,340);

        // --- ขนแก้มซ้าย ---
        drawQuadBezier(g,170,275,145,270,135,275);
        drawQuadBezier(g,170,300,155,295,135,305);
        drawQuadBezier(g,170,320,150,320,140,330);
        drawQuadBezier(g,180,340,165,340,155,350);

        // --- ขนแก้มขวา ---
        drawQuadBezier(g,425,275,450,270,460,275);
        drawQuadBezier(g,425,300,440,295,460,305);
        drawQuadBezier(g,425,320,445,320,455,330);
        drawQuadBezier(g,420,340,435,340,445,350);
    }










    // Bresenham Line
    private void drawLineBresenham(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            g.drawLine(x1, y1, x1, y1); // pixel
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x1 += sx; }
            if (e2 < dx) { err += dx; y1 += sy; }
        }
    }

    // Midpoint Ellipse
    private void drawEllipseMidpoint(Graphics g, int xc, int yc, int rx, int ry) {
        int x = 0, y = ry;
        double d1 = (ry*ry) - (rx*rx*ry) + (0.25*rx*rx);
        int dx = 2 * ry * ry * x;
        int dy = 2 * rx * rx * y;

        while (dx < dy) {
            plot4(g, xc, yc, x, y);
            if (d1 < 0) {
                x++;
                dx += 2 * ry * ry;
                d1 += dx + (ry*ry);
            } else {
                x++; y--;
                dx += 2 * ry * ry;
                dy -= 2 * rx * rx;
                d1 += dx - dy + (ry*ry);
            }
        }

        double d2 = ((ry*ry) * ((x+0.5)*(x+0.5))) + ((rx*rx) * ((y-1)*(y-1))) - (rx*rx*ry*ry);
        while (y >= 0) {
            plot4(g, xc, yc, x, y);
            if (d2 > 0) {
                y--;
                dy -= 2 * rx * rx;
                d2 += (rx*rx) - dy;
            } else {
                y--; x++;
                dx += 2 * ry * ry;
                dy -= 2 * rx * rx;
                d2 += dx - dy + (rx*rx);
            }
        }
    }

    private void plot4(Graphics g, int xc, int yc, int x, int y) {
        g.drawLine(xc+x, yc+y, xc+x, yc+y);
        g.drawLine(xc-x, yc+y, xc-x, yc+y);
        g.drawLine(xc+x, yc-y, xc+x, yc-y);
        g.drawLine(xc-x, yc-y, xc-x, yc-y);
    }

    // Quadratic Bezier
    private void drawQuadBezier(Graphics g, int x0, int y0, int x1, int y1, int x2, int y2) {
        int prevX = x0, prevY = y0;
        for (double t = 0; t <= 1; t += 0.01) {
            int x = (int)((1-t)*(1-t)*x0 + 2*(1-t)*t*x1 + t*t*x2);
            int y = (int)((1-t)*(1-t)*y0 + 2*(1-t)*t*y1 + t*t*y2);
            drawLineBresenham(g, prevX, prevY, x, y);
            prevX = x; prevY = y;
        }
    }

    // Flood Fill (4-direction)
    private void floodFill(int x, int y, Color fillColor) {
        int target = buffer.getRGB(x,y);
        int replacement = fillColor.getRGB();
        if (target == replacement) return;

        int w = buffer.getWidth(), h = buffer.getHeight();
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x,y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            if (p.x < 0 || p.y < 0 || p.x >= w || p.y >= h) continue;
            if (buffer.getRGB(p.x,p.y) != target) continue;
            buffer.setRGB(p.x,p.y,replacement);
            stack.push(new Point(p.x+1,p.y));
            stack.push(new Point(p.x-1,p.y));
            stack.push(new Point(p.x,p.y+1));
            stack.push(new Point(p.x,p.y-1));
        }
    }



    
    

    

    

}