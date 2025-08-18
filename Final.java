import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class Final extends JPanel implements Runnable {
    private BufferedImage sprite;         // เฉพาะตัวละคร (โปร่งใส)
    private boolean spriteBuilt = false;

    private CuteFieldSceneColored bg = new CuteFieldSceneColored();
    private BlackBG blackBG = new BlackBG();
    private WhiteBG whiteBG = new WhiteBG();
    private MounThun mounThun = new MounThun();
    private Goldmou goldmou = new Goldmou();

    // ===== คุมลำดับเหตุการณ์ =====
    // 0 = เลื่อนเข้ากลาง, 1 = สลับ Black/White, 2 = ฉากเดิมค้าง, 3 = ฉาก MounThun ค้าง
    private int phase = 0;
    private long phaseStart = 0L;

    // หมุนตอนสลับขาว/ดำ
    private double angle = 0.0;     // หน่วย radian
    private double ROT_TURNS = 1.0; // หมุนกี่รอบตลอด Phase 1 (เช่น 1 = 1 รอบ)


    // ===== ระยะเวลา (ms) =====
    private int SLIDE_DURATION_MS    = 2000; // เลื่อนเข้ากลาง
    private static final int ALT_TOTAL_MS = 1000; // รวมเวลาสลับ BG
    private static final int ALT_SLOT_MS  = 100;  // ระยะต่อช่วง
    private static final int PHASE2_HOLD_MS = 1000; // โชว์ฉากเดิมค้าง ก่อนเข้า phase 3

    // ===== จุดเริ่ม–จบ (แกน X) =====
    private double START_X = -600;
    private double END_X   = 0;

    // ===== easing =====
    private boolean USE_EASING = true;
    private double easeInOut(double t) {
        return (t < 0.5) ? (2*t*t) : (1 - Math.pow(-2*t + 2, 2) / 2);
    }

    // ตำแหน่งตัวละคร (เลื่อนแกน X)
    private double offsetX = START_X;
    

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
        sprite = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        phaseStart = System.currentTimeMillis();
    }








    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        long elapsed = System.currentTimeMillis() - phaseStart;

        if (phase == 0) {
            // ------- Phase 0: ตัวละครเลื่อนเข้ากลาง -------
            g.drawImage(bg.getImage(), 0, 0, null);

            if (!spriteBuilt) {
                Graphics gs = sprite.getGraphics();
                buildSprite(gs); // วาดตัวละครลง sprite (ARGB โปร่งใส)
                gs.dispose();
                spriteBuilt = true;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(offsetX, 0);
            g2.drawImage(sprite, 0, 0, null);
            g2.dispose();

        } else if (phase == 1) {
            if (!spriteBuilt) {
            Graphics gs = sprite.getGraphics();
            buildSprite(gs);
            gs.dispose();
            spriteBuilt = true;
            }

            final long totalSlots = ALT_TOTAL_MS / ALT_SLOT_MS;
            long slot = elapsed / ALT_SLOT_MS;

        // 1) วาดพื้นหลังสลับ
        if (slot < totalSlots) {
            if (slot % 2 == 0) g.drawImage(blackBG.getImage(), 0, 0, null);
            else               g.drawImage(whiteBG.getImage(), 0, 0, null);

            // 2) วาดตัวละคร "หมุน" อยู่กลางเฟรม
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(END_X + W/2.0, H/2.0); // ย้าย origin ไปกึ่งกลางภาพ
            g2.rotate(angle);                   // หมุนรอบจุดกึ่งกลาง
            g2.drawImage(sprite, -W/2, -H/2, null); // วาดให้ sprite จอดันจุดกึ่งกลางพอดี
            g2.dispose();
        } else {
            phase = 2;
            phaseStart = System.currentTimeMillis();
            angle = 0.0; // รีเซ็ตมุมหลังจบการสลับ
            g.drawImage(bg.getImage(), 0, 0, null);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(END_X, 0);
            g2.drawImage(sprite, 0, 0, null);
            g2.dispose();
        }

        } else if (phase == 2) {
            // ------- Phase 3: ใช้ฉากใหม่ MounThun ตัวละครอยู่กลาง -------
            g.drawImage(mounThun.getImage(), 0, 0, null);
            if (spriteBuilt) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.translate(END_X, 0);
                g2.drawImage(sprite, 0, 0, null);
                g2.dispose();
            }

        } else if (phase == 3) {
        // ------- Phase 2: กลับพื้นหลังเดิม ตัวละครหยุดกลาง -------
            g.drawImage(goldmou.getImage(), 0, 0, null);
            if (spriteBuilt) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.translate(END_X, 0);
                g2.drawImage(sprite, 0, 0, null);
                g2.dispose();
            }
        }
    }








    @Override
    public void run() {
        while (true) {
            long now = System.currentTimeMillis();

            if (phase == 0) {
                // ใช้เวลา SLIDE_DURATION_MS เลื่อนจาก START_X → END_X
                double rawT = (now - phaseStart) / (double) SLIDE_DURATION_MS; // 0..1+
                double t = Math.max(0, Math.min(1, rawT));
                if (USE_EASING) t = easeInOut(t);

                offsetX = START_X + (END_X - START_X) * t;

                if (rawT >= 1.0) {
                    offsetX = END_X;
                    phase = 1;                 // ไป Phase 1 (สลับ BG)
                    phaseStart = now;
                }

            } else if (phase == 1) {
            // ความคืบหน้า 0..1 ตลอดช่วง ALT_TOTAL_MS
            double progress = (now - phaseStart) / (double) ALT_TOTAL_MS;
            if (progress < 0) progress = 0;
            if (progress > 1) progress = 1;

            // คำนวณมุม (2π rad = 1 รอบ) * จำนวนรอบที่อยากหมุน
            angle = 10 * Math.PI * ROT_TURNS * progress;

            // เมื่อครบเวลา ให้ไป Phase 2
            if ((now - phaseStart) >= ALT_TOTAL_MS) {
                phase = 2;
                phaseStart = now;
                angle = 0.0; // รีเซ็ตมุม
            }

            } else if (phase == 2) {
                // โชว์ฉากเดิมค้าง แล้วไป phase 3
                if ((now - phaseStart) >= PHASE2_HOLD_MS) {
                    phase = 3;
                    phaseStart = now;
                }
                offsetX = END_X;

            } else {
                // Phase 3: ค้างฉาก MounThun
                offsetX = END_X;
            }

            repaint();
            try { Thread.sleep(16); } catch (InterruptedException ignored) {}
        }
    }














    /* ===== สร้างสไปรต์ตัวละคร (โปร่งใส) ===== */
    private void buildSprite(Graphics g) {
        // อย่าเคลียร์เป็นสีขาว! ปล่อยให้โปร่งใส (ARGB = 0)
        drawTailDecorOnSprite();
        headFilled();
        faceFilled();
    }

    /* ====== วาดลง sprite โดยตรง: ใช้ setPixel(sprite,...) ====== */
    private void setPixel(int x, int y, int rgb) {
        if (x>=0 && y>=0 && x<W && y<H) sprite.setRGB(x, y, rgb);
    }

    // วาดขอบจากจุดโพลิกอน (ใช้เส้นธรรมดา)
    private void strokeFromPoints(java.util.List<Point> pts, int rgb) {
        for (int i = 0; i < pts.size() - 1; i++) {
            Point a = pts.get(i), b = pts.get(i + 1);
            drawLineBresenham(a.x, a.y, b.x, b.y, rgb);
        }
        // ปิดรูป
        Point last = pts.get(pts.size() - 1), first = pts.get(0);
        drawLineBresenham(last.x, last.y, first.x, first.y, rgb);
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
        fillEllipseMidpoint(275+105, 105+155, 105, 155, COL_TAIL); // หาง
        fillEllipseMidpoint(195+105, 275+90, 105, 90, COL_BODY);   // ตัว
        fillEllipseMidpoint(165+132, 170+125, 132, 125, COL_BODY); // หัว
        fillEllipseMidpoint(175+45, 120+45, 45, 45, COL_BODY);     // หูซ้าย
        fillEllipseMidpoint(330+45, 120+45, 45, 45, COL_BODY);     // หูขวา
        // มือ/ขา
        fillHandLeft(COL_BODY);
        fillHandRight(COL_BODY);
        fillLegLeft(COL_BODY);
        fillLegRight(COL_BODY);
    }

    private void faceFilled(){
        drawCheekHairs();
        // วงตา
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
        strokeFromPoints(pts, COL_OUT);
    }
    private void fillHandRight(int rgb){
        java.util.List<Point> pts=new ArrayList<>();
        pts.add(new Point(375,355));
        pts.add(new Point(370,385));
        pts.addAll(sampleQuadBezier(370,385, 360,405, 390,395, 40));
        pts.add(new Point(405,360));
        pts.add(new Point(375,355));
        fillPolyFromPoints(pts, rgb);
        strokeFromPoints(pts, COL_OUT);
    }
    private void fillLegLeft(int rgb){
        java.util.List<Point> pts=new ArrayList<>();
        pts.add(new Point(250,440));
        pts.add(new Point(250,470));
        pts.addAll(sampleQuadBezier(250,470, 260,480, 280,470, 40));
        pts.add(new Point(280,450));
        pts.add(new Point(250,440));
        fillPolyFromPoints(pts, rgb);
        strokeFromPoints(pts, COL_OUT);
    }
    private void fillLegRight(int rgb){
        java.util.List<Point> pts=new ArrayList<>();
        pts.add(new Point(310,450));
        pts.add(new Point(310,470));
        pts.addAll(sampleQuadBezier(310,470, 320,480, 340,470, 40));
        pts.add(new Point(340,445));
        pts.add(new Point(310,450));
        fillPolyFromPoints(pts, rgb);
        strokeFromPoints(pts, COL_OUT);
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
