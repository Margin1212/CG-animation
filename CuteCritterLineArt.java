import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class CuteCritterLineArt extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // พื้นหลัง
        g2.setColor(new Color(255, 250, 240));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // --------- หางสีฟ้า ---------
        int tailCenterX = 400;
        int tailCenterY = 300;
        int tailRX = 80;
        int tailRY = 120;
        Ellipse2D tail = new Ellipse2D.Double(tailCenterX - tailRX, tailCenterY - tailRY, tailRX * 2, tailRY * 2);
        g2.setColor(new Color(173, 216, 230)); // ฟ้าอ่อน
        g2.fill(tail);

        // --------- หนามหาง (เส้นดำ) ---------
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);
        for (int i = -70; i <= 70; i += 15) {
            double angle = Math.toRadians(i);
            double x1 = tailCenterX + tailRX * Math.cos(angle);
            double y1 = tailCenterY + tailRY * Math.sin(angle);

            double x2 = tailCenterX + (tailRX + 20) * Math.cos(angle);
            double y2 = tailCenterY + (tailRY + 20) * Math.sin(angle);

            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }

        // --------- ลำตัวสีขาว ---------
        int bodyX = 250, bodyY = 200, bodyW = 200, bodyH = 200;
        Ellipse2D body = new Ellipse2D.Double(bodyX, bodyY, bodyW, bodyH);
        g2.setColor(Color.WHITE);
        g2.fill(body);
        g2.setColor(Color.BLACK);
        g2.draw(body);

        // --------- หู ---------
        Ellipse2D earLeft = new Ellipse2D.Double(230, 150, 60, 60);
        Ellipse2D earRight = new Ellipse2D.Double(360, 150, 60, 60);
        g2.setColor(Color.WHITE);
        g2.fill(earLeft);
        g2.fill(earRight);
        g2.setColor(Color.BLACK);
        g2.draw(earLeft);
        g2.draw(earRight);

        // --------- ตา ---------
        Ellipse2D eyeLeft = new Ellipse2D.Double(290, 260, 25, 25);
        Ellipse2D eyeRight = new Ellipse2D.Double(365, 260, 25, 25);
        g2.setColor(Color.BLACK);
        g2.fill(eyeLeft);
        g2.fill(eyeRight);

        // --------- แก้มชมพู ---------
        Ellipse2D cheekLeft = new Ellipse2D.Double(270, 280, 30, 20);
        Ellipse2D cheekRight = new Ellipse2D.Double(380, 280, 30, 20);
        g2.setColor(new Color(255, 182, 193));
        g2.fill(cheekLeft);
        g2.fill(cheekRight);

        // --------- จมูก ---------
        Ellipse2D nose = new Ellipse2D.Double(340, 275, 15, 15);
        g2.setColor(new Color(135, 206, 235));
        g2.fill(nose);

        // --------- ปาก (Bézier) ---------
        g2.setColor(Color.BLACK);
        CubicCurve2D mouth = new CubicCurve2D.Double(335, 290, 340, 300, 350, 300, 355, 290);
        g2.draw(mouth);

        // --------- หนวด ---------
        g2.draw(new Line2D.Double(260, 270, 240, 260));
        g2.draw(new Line2D.Double(260, 280, 235, 280));
        g2.draw(new Line2D.Double(260, 290, 240, 300));

        g2.draw(new Line2D.Double(430, 270, 450, 260));
        g2.draw(new Line2D.Double(430, 280, 455, 280));
        g2.draw(new Line2D.Double(430, 290, 450, 300));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cute Character");
        CuteCritterLineArt panel = new CuteCritterLineArt();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
