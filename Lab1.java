import java.awt.*;
import javax.swing.*;

class Lab1 extends JPanel{
    public static void main(String[] args) {
        Lab1 m = new Lab1();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("First Swing");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void paintComponent(Graphics g) {

    g.drawString("Hello", 40, 40);
    g.setColor(Color.BLUE);
    g.fillRect(130, 30, 100, 80);
    g.drawOval(30, 130, 50, 60);
    g.setColor(Color.RED);
    g.drawLine(0, 0, 200, 30);
    g.fillOval(130, 130, 50, 60);
    g.drawArc(30, 200, 40, 50, 90, 60);
    g.fillArc(30, 130, 40, 50, 180, 40);
        for (int m=0;m<10;m++){
            int tmp=(m*10)+245;
            plot(g, tmp, tmp);
        }
    }   

    private void plot(Graphics g, int x, int y) {
        g.setColor(Color.gray);
        g.fillRect(x, y, 10, 10);
    }
}