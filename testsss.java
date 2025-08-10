import java.awt.*;
import javax.swing.*;

class testsss extends JPanel{
    
    public static void main(String[] args) {
        testsss m = new testsss();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("hoohoo");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void paintComponent(Graphics g) {

    

    

    }


    private void BezierCurve (Graphics g,int size, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){

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
            plot(g, x, y, size);
        }
    }



    public void Midpoint_Circle (Graphics g ,int xc ,int yc , int r ,int size,int q1 ,int q2 ,int q3 ,int q4 ,int q5 ,int q6 ,int q7 ,int q8 ){
        int x =0;
        int y =r;
        int D =1-r;

        while (x <= y){
            if(q1 !=0){
                plot (g, x +xc , y +yc,size);}
            if(q2 !=0){
            plot (g, -x +xc , y +yc,size);}
            if(q3 !=0){
            plot (g, x +xc , -y +yc,size);}
            if(q4 !=0){
            plot (g, -x +xc , -y +yc,size);}
            if(q5 !=0){
            plot (g, y +xc , x +yc,size);}
            if(q6 !=0){
            plot (g, -y +xc , x +yc,size);}
            if(q7 !=0){
            plot (g, y +xc , -x +yc,size);}
            if(q8 !=0){
            plot (g, -y +xc , -x +yc,size);}

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
    

    public void Ellipse(Graphics g ,int xc , int yc , int a , int b){
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

        //Region 2
        x= a;
        y= 0;
        D= Math.round(a*a - b*b*a + b*b/4);

        while (b*b*x >= a*a*y){

            // plot (g, x +xc , y +yc,2);
            // plot (g, -x +xc , y +yc,2);
            // plot (g, x +xc , -y +yc,2);
            // plot (g, -x +xc , -y +yc,2);

            y++;
            D = D +2*a*a*y + a*a;

            if(D>=0){
                x--;
                D = D - 2*b*b*x;
            }
        }
    }
    

    public void plot(Graphics g, int x, int y,int s) {
        
        g.fillRect(x, y, s, s);
    }

}