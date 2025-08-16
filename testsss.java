import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

class testsss extends JPanel{
    private int offsetX = 0;
    private int direction = 1;   // 1 = ไปทางขวา, -1 = ไปทางซ้าย

    
    public static void main(String[] args) {
        testsss m = new testsss();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("hoohoo-1");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        new Timer(16, e -> {
            m.offsetX += m.direction * 2; // ขยับทีละ 2 px

            // ถ้าชนขอบ 600 หรือ 0 ให้สลับทิศ
            if (m.offsetX >= 600) {
                m.direction = -1;
            } else if (m.offsetX <= 0) {
                m.direction = 1;
            }

            m.repaint();
        }).start();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.translate(offsetX, 0);


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.BLACK);

        CuteFieldScene bg =new CuteFieldScene();
        //bg.drawMountains(g2);

        
        
        head(g2);
        face(g2);

    

    }




    private void head(Graphics2D g2){
        GeneralPath path = new GeneralPath();

        //หาง
        Ellipse2D tailShape = new Ellipse2D.Double(275, 105, 210, 310);
        g2.setColor(new Color(173, 216, 230)); // สีตัว
        g2.fill(tailShape);
        g2.setColor(Color.BLACK);
        //g2.draw(tailShape);

        //wool tail

        int tailCenterX = 380;
        int tailCenterY = 260;
        int tailRX = 85;
        int tailRY = 135;

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);
        for (int i = -140; i <= 70; i += 15) {
            double angle = Math.toRadians(i);
            double x1 = tailCenterX + tailRX * Math.cos(angle);
            double y1 = tailCenterY + tailRY * Math.sin(angle);

            double x2 = tailCenterX + (tailRX + 20) * Math.cos(angle);
            double y2 = tailCenterY + (tailRY + 20) * Math.sin(angle);

            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }




        // ตัว
        Ellipse2D bodyShape = new Ellipse2D.Double(195, 275, 210, 180);
        g2.setColor(new Color(255, 230, 140));
        g2.fill(bodyShape);
        g2.setColor(Color.BLACK);
        //g2.draw(bodyShape);

        // หัว
        Ellipse2D headShape = new Ellipse2D.Double(165, 170, 264, 250);
        g2.setColor(new Color(255, 230, 140)); // สีตัว
        g2.fill(headShape);
        g2.setColor(Color.BLACK);
        //g2.draw(headShape);

        //แก้ม
        GeneralPath cheackShape1 = new GeneralPath();
        {cheackShape1.moveTo(170, 275);   
        cheackShape1.lineTo(135, 275);    
        cheackShape1.quadTo(125, 315, 155, 350); 
        cheackShape1.lineTo(180, 340);
        cheackShape1.closePath();
        }
        g2.setColor(new Color(255, 230, 140)); // สีตัว
        g2.fill(cheackShape1);
        g2.setColor(Color.BLACK);
        //g2.draw(cheackShape1);
        GeneralPath cheackShape2 = new GeneralPath();
        {cheackShape2.moveTo(425, 275);   
        cheackShape2.lineTo(460, 275);    
        cheackShape2.quadTo(465, 315, 445, 350); 
        cheackShape2.lineTo(420, 340);
        cheackShape2.closePath();
        }
        g2.setColor(new Color(255, 230, 140)); // สีตัว
        g2.fill(cheackShape2);
        g2.setColor(Color.BLACK);
        //g2.draw(cheackShape2);

        

        // หูซ้าย
        Ellipse2D earLeft = new Ellipse2D.Double(175, 120, 90, 90);
        g2.setColor(new Color(255, 230, 140));
        g2.fill(earLeft);
        g2.setColor(Color.BLACK);
        //g2.draw(earLeft);

        // หูขวา
        Ellipse2D earRight = new Ellipse2D.Double(330, 120, 90, 90);
        g2.setColor(new Color(255, 230, 140));
        g2.fill(earRight);
        g2.setColor(Color.BLACK);
        //g2.draw(earRight);


        
        //in ear
        {path.moveTo(215, 190);   
        path.quadTo(215, 180, 230, 175); 
        path.quadTo(215, 160, 220, 140);
        g2.draw(path);}
        {path.moveTo(220, 165);   
        path.quadTo(210, 165, 205, 170); 
        g2.draw(path);}

        {path.moveTo(370, 190);   
        path.quadTo(365, 180, 350, 175); 
        path.quadTo(365, 160, 360, 140);
        g2.draw(path);}

        {path.moveTo(360, 165);   
        path.quadTo(365, 160, 380, 170); 
        g2.draw(path);}



        
        //hand
        GeneralPath hand1 = new GeneralPath();
        {hand1.moveTo(200, 375);   
        hand1.lineTo(160, 375);    
        hand1.quadTo(140, 390, 170, 400); 
        hand1.lineTo(205, 395);
        hand1.closePath();
        }
        g2.setColor(new Color(255, 230, 140)); // สีตัว
        g2.fill(hand1);
        g2.setColor(Color.BLACK);
        g2.draw(hand1);

        GeneralPath hand2 = new GeneralPath();
        {hand2.moveTo(375, 355);   
        hand2.lineTo(370, 385);    
        hand2.quadTo(360, 405, 390, 395); 
        hand2.lineTo(405, 360);
        hand2.closePath();
        }
        g2.setColor(new Color(255, 230, 140)); // สีตัว
        g2.fill(hand2);
        g2.setColor(Color.BLACK);
        g2.draw(hand2);

        

        //leg
        GeneralPath leg1 = new GeneralPath();
        {leg1.moveTo(250, 440);   
        leg1.lineTo(250, 470);    
        leg1.quadTo(260, 480, 280, 470); 
        leg1.lineTo(280, 450);
        leg1.closePath();
        }
        g2.setColor(new Color(255, 230, 140));
        g2.fill(leg1);
        g2.setColor(Color.BLACK);
        g2.draw(leg1);

        GeneralPath leg2 = new GeneralPath();
        {leg2.moveTo(310, 450);   
        leg2.lineTo(310, 470);    
        leg2.quadTo(320, 480, 340, 470); 
        leg2.lineTo(340, 445);
        leg2.closePath();
        }
        g2.setColor(new Color(255, 230, 140));
        g2.fill(leg2);
        g2.setColor(Color.BLACK);
        g2.draw(leg2);

        // {g2.draw(getBounds());
        // path.moveTo(310, 450);   
        // path.lineTo(310, 470);    
        // path.quadTo(320, 480, 340, 470); 
        // path.lineTo(340, 445);
        // g2.draw(path);}


    }


    private void face(Graphics2D g2){
        GeneralPath path = new GeneralPath();
        g2.setStroke(new BasicStroke(6));
        g2.draw(new Ellipse2D.Double(225, 270, 40, 35));
        g2.draw(new Ellipse2D.Double(225, 272, 40, 25));
        g2.draw(new Ellipse2D.Double(315, 270, 40, 35));
        g2.draw(new Ellipse2D.Double(315, 272, 40, 25));

        g2.setStroke(new BasicStroke(3));



        //the blue one
        Ellipse2D blue = new Ellipse2D.Double(275, 270, 33, 33);
        g2.setColor(new Color(173, 216, 230));
        g2.fill(blue);
        g2.setColor(Color.BLACK);
        //g2.draw(blue);

        Ellipse2D pink1 = new Ellipse2D.Double(185, 295, 50, 40);
        g2.setColor(new Color(255,153,204));
        g2.fill(pink1);
        g2.setColor(Color.BLACK);
        //g2.draw(pink1);
        Ellipse2D pink2 = new Ellipse2D.Double(345, 295, 50, 40);
        g2.setColor(new Color(255,153,204));
        g2.fill(pink2);
        g2.setColor(Color.BLACK);
        //g2.draw(pink2);

        //g2.draw(new Ellipse2D.Double(185, 295, 50, 40));
        //g2.draw(new Ellipse2D.Double(345, 295, 50, 40));

        //mount
        {
        g2.draw(new Ellipse2D.Double(288, 310, 5, 5));
        g2.draw(new Ellipse2D.Double(288, 310, 3, 3));
        path.moveTo(290, 315);   
        path.quadTo(280, 335, 265, 320); 
        g2.draw(path);}
        {path.moveTo(290, 315);   
        path.quadTo(300, 335, 315, 320); 
        g2.draw(path);}

        {path.moveTo(283, 340);   
        path.quadTo(290, 345, 298, 340); 
        g2.draw(path);}


        //cheack wool
        {path.moveTo(170, 275);       
        path.quadTo(145, 270, 135, 275); 
        g2.draw(path);}
        {path.moveTo(170, 300);       
        path.quadTo(155, 295, 135, 305); 
        g2.draw(path);}
        {path.moveTo(170, 320);       
        path.quadTo(150, 320, 140, 330); 
        g2.draw(path);}
        {path.moveTo(180, 340);       
        path.quadTo(165, 340, 155, 350); 
        g2.draw(path);}

        {path.moveTo(425, 275);       
        path.quadTo(450, 270, 460, 275); 
        g2.draw(path);}
        {path.moveTo(425, 300);       
        path.quadTo(440, 295, 460, 305); 
        g2.draw(path);}
        {path.moveTo(425, 320);       
        path.quadTo(445, 320, 455, 330); 
        g2.draw(path);}
        {path.moveTo(420, 340);       
        path.quadTo(435, 340, 445, 350); 
        g2.draw(path);}


    }














    
    

    

    

}