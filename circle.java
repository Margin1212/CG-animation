import java.awt.*;
import javax.swing.*;

class circle extends JPanel{
    
    public static void main(String[] args) {
        circle m = new circle();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Circle and Ellipse");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void paintComponent(Graphics g) {
        g.setColor(Color.green);
        Midpoint_Circle(g, 300,300, 250);


        g.setColor(Color.orange);
        Midpoint_ellipse(g, 300, 300, 100, 200);
        


    }

    public void Midpoint_Circle (Graphics g ,int xc ,int yc , int r){
        int x =0;
        int y =r;
        int D =1-r;

        while (x <= y){
            plot (g, x +xc , y +yc,2);
            plot (g, -x +xc , y +yc,2);
            plot (g, x +xc , -y +yc,2);
            plot (g, -x +xc , -y +yc,2);
            plot (g, y +xc , x +yc,2);
            plot (g, -y +xc , x +yc,2);
            plot (g, y +xc , -x +yc,2);
            plot (g, -y +xc , -x +yc,2);

            x++;

            if(D >= 0){
                y--;
                D = D +2 * x - 2 * y + 1;
            }
            else{
                D =D +2 * x + 1;

            }
        }

    }


    public void Midpoint_ellipse(Graphics g ,int xc , int yc , int a , int b){

        int x ,y ,D ;

        //Region 1
        x= 0;
        y= b;
        D= Math.round(b*b - a*a*b + a*a/4);

        while (b*b*x <= a*a*y){

            plot (g, x +xc , y +yc,2);
            plot (g, -x +xc , y +yc,2);
            plot (g, x +xc , -y +yc,2);
            plot (g, -x +xc , -y +yc,2);
            
            x++;
            D = D + 2*b*b*x + b*b ;

            if(D>=0){
                y--;
                D =D -2*a*a*y;
            }
        }

        //Region 1
        x= a;
        y= 0;
        D= Math.round(a*a - b*b*a + b*b/4);

        while (b*b*x >= a*a*y){

            plot (g, x +xc , y +yc,2);
            plot (g, -x +xc , y +yc,2);
            plot (g, x +xc , -y +yc,2);
            plot (g, -x +xc , -y +yc,2);

            y++;
            D = D +2*a*a*y + a*a;

            if(D>=0){
                x--;
                D = D - 2*b*b*x;
            }
        }




    }




    public void plot(Graphics g, int x, int y,int s) {
        // g.setColor(Color.black);
        g.fillRect(x, y, s, s);
    }




}
