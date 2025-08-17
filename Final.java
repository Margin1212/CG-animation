import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class Final extends JPanel implements Runnable {
    private BufferedImage sprite;       // เฉพาะตัวละคร (โปร่งใส)
    private boolean spriteBuilt = false;

    // ตัวแปรอนิเมชัน
    private double offsetX = 0;
    private boolean goRight = true;

    private static final int W = 600, H = 600;

    // สี (มีอัลฟาใน getRGB อยู่แล้ว = ทึบแสง)
    private static final int COL_OUT  = Color.BLACK.getRGB();
    private static final int COL_BODY = new Color(255,230,140).getRGB();
    private static final int COL_TAIL = new Color(173,216,230).getRGB();
    private static final int COL_PINK = new Color(255,153,204).getRGB();

    public static void main(String[] args) {
        Final m = new Final();
        JFrame f = new JFrame("hoohoo-1");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(W, H);
        f.add(m);
        f.setVisible(true);
        new Thread(m).start();
    }

    public Final() {
        // ทำสไปรต์โปร่งใส
        sprite = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1) วาดพื้นหลังคงที่ (ไม่ขยับ)
        Graphics2D g2 = (Graphics2D) g.create();
        // ตัวอย่างพื้นหลังแบบง่าย: ท้องฟ้าขาว+พื้นสีอ่อน
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, W, H);
        g2.setColor(new Color(230, 245, 230));
        g2.fillRect(0, 420, W, H - 420);

        // 2) ถ้ายังไม่ประกอบสไปรต์ ให้สร้างครั้งเดียว
        if (!spriteBuilt) {
            Graphics gs = sprite.getGraphics();
            buildSprite(gs);  // วาดตัวละครลงสไปรต์โปร่งใส
            gs.dispose();
            spriteBuilt = true;
        }

        // 3) วาดสไปรต์ด้วยการ translate → ขยับเฉพาะตัวละคร
        g2.translate(offsetX, 0);
        g2.drawImage(sprite, 0, 0, null);
        g2.dispose();
    }

    @Override
    public void run() {
        long last = System.currentTimeMillis();
        double speed = 120; // px/s
        int minX = -200, maxX = 200;

        while (true) {
            long now = System.currentTimeMillis();
            double dt = (now - last) / 1000.0;
            last = now;

            if (goRight) offsetX += speed * dt; else offsetX -= speed * dt;
            if (offsetX >= maxX) { offsetX = maxX; goRight = false; }
            if (offsetX <= minX) { offsetX = minX; goRight = true;  }

            repaint();
            try { Thread.sleep(16); } catch (InterruptedException ignored) {}
        }
    }

    /* ===== สร้างสไปรต์ตัวละคร (โปร่งใส) ===== */
    private void buildSprite(Graphics g) {
        // อย่าเคลียร์เป็นสีขาว! ปล่อยให้โปร่งใส (ARGB = 0) จะทับพื้นหลังได้สวย
        // วาดเส้น/ถมสีด้วยฟังก์ชัน rasterization ของคุณ (เหมือนเดิมทุกอย่าง)
        drawTailDecorOnSprite();
        headFilled();
        faceFilled();
    }

    /* ====== วาดลง sprite โดยตรง: ใช้ setPixel(sprite,...) ====== */
    private void setPixel(int x, int y, int rgb) {
        if (x>=0 && y>=0 && x<W && y<H) sprite.setRGB(x, y, rgb);
    }

    private void drawLineBresenham(int x1, int y1, int x2, int y2, int rgb) {
        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1, err = dx - dy;
        while (true) {
            setPixel(x1, y1, rgb);
            if (x1 == x2 && y1 == y2) break;
            int e2 = err << 1;
            if (e2 > -dy) { err -= dy; x1 += sx; }
            if (e2 <  dx) { err += dx; y1 += sy; }
        }
    }

    private void drawHSpan(int x1, int x2, int y, int rgb) {
        if (y < 0 || y >= H) return;
        if (x1 > x2) { int t=x1; x1=x2; x2=t; }
        x1 = Math.max(0, x1); x2 = Math.min(W-1, x2);
        for (int x = x1; x <= x2; x++) setPixel(x, y, rgb);
    }

    private void fillEllipseMidpoint(int xc, int yc, int rx, int ry, int rgb) {
        int x = 0, y = ry;
        long rx2 = 1L*rx*rx, ry2 = 1L*ry*ry;
        long dx = 0, dy = 2*rx2*y;
        long d1 = ry2 - rx2*ry + rx2/4;
        while (dx < dy) {
            drawHSpan(xc - x, xc + x, yc + y, rgb);
            drawHSpan(xc - x, xc + x, yc - y, rgb);
            if (d1 < 0) { x++; dx += 2*ry2; d1 += dx + ry2; }
            else { x++; y--; dx += 2*ry2; dy -= 2*rx2; d1 += dx - dy + ry2; }
        }
        long d2 = (long)(ry2*(x+0.5)*(x+0.5) + rx2*(y-1)*(y-1) - rx2*ry2);
        while (y >= 0) {
            drawHSpan(xc - x, xc + x, yc + y, rgb);
            drawHSpan(xc - x, xc + x, yc - y, rgb);
            if (d2 > 0) { y--; dy -= 2*rx2; d2 += rx2 - dy; }
            else { y--; x++; dx += 2*ry2; dy -= 2*rx2; d2 += dx - dy + rx2; }
        }
    }

    private void drawQuadBezier(int x0,int y0,int x1,int y1,int x2,int y2,int rgb){
        int px=x0, py=y0; int steps=100;
        for (int i=1;i<=steps;i++){
            double t=(double)i/steps, it=1-t;
            int x=(int)Math.round(it*it*x0 + 2*it*t*x1 + t*t*x2);
            int y=(int)Math.round(it*it*y0 + 2*it*t*y1 + t*t*y2);
            drawLineBresenham(px, py, x, y, rgb);
            px=x; py=y;
        }
    }

    private java.util.List<Point> sampleQuadBezier(int x0,int y0,int x1,int y1,int x2,int y2,int steps){
        java.util.List<Point> pts=new ArrayList<>(steps+1);
        for(int i=0;i<=steps;i++){
            double t=(double)i/steps, it=1-t;
            int x=(int)Math.round(it*it*x0 + 2*it*t*x1 + t*t*x2);
            int y=(int)Math.round(it*it*y0 + 2*it*t*y1 + t*t*y2);
            if (pts.isEmpty() || pts.get(pts.size()-1).x!=x || pts.get(pts.size()-1).y!=y)
                pts.add(new Point(x,y));
        }
        return pts;
    }

    private void fillPolygonScanline(int[] xs, int[] ys, int n, int rgb) {
        int minY = ys[0], maxY = ys[0];
        for (int i=1;i<n;i++){ if (ys[i]<minY) minY=ys[i]; if (ys[i]>maxY) maxY=ys[i]; }
        for (int y=minY; y<=maxY; y++){
            ArrayList<Integer> xsList=new ArrayList<>();
            int j=n-1;
            for(int i=0;i<n;i++){
                int yi=ys[i], yj=ys[j], xi=xs[i], xj=xs[j];
                boolean cross=(yi<y && yj>=y) || (yj<y && yi>=y);
                if (cross && yj!=yi){
                    int x=xi + (int)Math.floor((double)(y-yi)*(xj-xi)/(double)(yj-yi));
                    xsList.add(x);
                }
                j=i;
            }
            Collections.sort(xsList);
            for (int k=0;k+1<xsList.size();k+=2)
                drawHSpan(xsList.get(k), xsList.get(k+1), y, rgb);
        }
    }

    /* ===== วาดองค์ประกอบของตัวละครลง sprite ===== */
    private void drawTailDecorOnSprite() {
        int cx=380, cy=260, rx=85, ry=135;
        for (int i=-140;i<=70;i+=15){
            double a=Math.toRadians(i);
            int x1=(int)(cx + rx*Math.cos(a));
            int y1=(int)(cy + ry*Math.sin(a));
            int x2=(int)(cx + (rx+20)*Math.cos(a));
            int y2=(int)(cy + (ry+20)*Math.sin(a));
            drawLineBresenham(x1,y1,x2,y2,COL_OUT);
        }
    }

    private void headFilled(){
        // หาง/ตัว/หัว/หู
        fillEllipseMidpoint(275+105, 105+155, 105, 155, COL_TAIL);
        fillEllipseMidpoint(195+105, 275+90, 105, 90, COL_BODY);
        fillEllipseMidpoint(165+132, 170+125, 132, 125, COL_BODY);
        fillEllipseMidpoint(175+45, 120+45, 45, 45, COL_BODY);
        fillEllipseMidpoint(330+45, 120+45, 45, 45, COL_BODY);

        // มือ/ขา (โพลีจาก bezier)
        fillHandLeft(COL_BODY);
        fillHandRight(COL_BODY);
        fillLegLeft(COL_BODY);
        fillLegRight(COL_BODY);
    }

    private void faceFilled(){
        // ขนแก้ม (เส้น)
        drawCheekHairs();

        // ตา
        fillEllipseMidpoint(225+20, 270+17, 20, 17, COL_OUT);
        fillEllipseMidpoint(315+20, 270+17, 20, 17, COL_OUT);
        fillEllipseMidpoint(225+20, 272+12, 20, 12, COL_OUT);
        fillEllipseMidpoint(315+20, 272+12, 20, 12, COL_OUT);

        // ตาฟ้า
        fillEllipseMidpoint(275+16, 270+16, 16, 16, COL_TAIL);

        // แก้มชมพู
        fillEllipseMidpoint(185+25, 295+20, 25, 20, COL_PINK);
        fillEllipseMidpoint(345+25, 295+20, 25, 20, COL_PINK);

        // ปาก
        fillEllipseMidpoint(288+2, 310+2, 2, 2, COL_OUT);
        drawQuadBezier(290,315, 280,335, 265,320, COL_OUT);
        drawQuadBezier(290,315, 300,335, 315,320, COL_OUT);
        drawQuadBezier(283,340, 290,345, 298,340, COL_OUT);
    }

    private void fillHandLeft(int rgb){
        java.util.List<Point> pts=new ArrayList<>();
        pts.add(new Point(200,375));
        pts.add(new Point(160,375));
        pts.addAll(sampleQuadBezier(160,375, 140,390, 170,400, 40));
        pts.add(new Point(205,395));
        pts.add(new Point(200,375));
        fillPolyFromPoints(pts, rgb);
    }
    private void fillHandRight(int rgb){
        java.util.List<Point> pts=new ArrayList<>();
        pts.add(new Point(375,355));
        pts.add(new Point(370,385));
        pts.addAll(sampleQuadBezier(370,385, 360,405, 390,395, 40));
        pts.add(new Point(405,360));
        pts.add(new Point(375,355));
        fillPolyFromPoints(pts, rgb);
    }
    private void fillLegLeft(int rgb){
        java.util.List<Point> pts=new ArrayList<>();
        pts.add(new Point(250,440));
        pts.add(new Point(250,470));
        pts.addAll(sampleQuadBezier(250,470, 260,480, 280,470, 40));
        pts.add(new Point(280,450));
        pts.add(new Point(250,440));
        fillPolyFromPoints(pts, rgb);
    }
    private void fillLegRight(int rgb){
        java.util.List<Point> pts=new ArrayList<>();
        pts.add(new Point(310,450));
        pts.add(new Point(310,470));
        pts.addAll(sampleQuadBezier(310,470, 320,480, 340,470, 40));
        pts.add(new Point(340,445));
        pts.add(new Point(310,450));
        fillPolyFromPoints(pts, rgb);
    }
    private void drawCheekHairs(){
        drawQuadBezier(170,275,145,270,135,275, COL_OUT);
        drawQuadBezier(170,300,155,295,135,305, COL_OUT);
        drawQuadBezier(170,320,150,320,140,330, COL_OUT);
        drawQuadBezier(180,340,165,340,155,350, COL_OUT);
        drawQuadBezier(425,275,450,270,460,275, COL_OUT);
        drawQuadBezier(425,300,440,295,460,305, COL_OUT);
        drawQuadBezier(425,320,445,320,455,330, COL_OUT);
        drawQuadBezier(420,340,435,340,445,350, COL_OUT);
    }
    private void fillPolyFromPoints(java.util.List<Point> boundary, int rgb){
        int n=boundary.size(); int[] xs=new int[n], ys=new int[n];
        for(int i=0;i<n;i++){ xs[i]=boundary.get(i).x; ys[i]=boundary.get(i).y; }
        fillPolygonScanline(xs, ys, n, rgb);
    }
}
