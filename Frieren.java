import java.awt.*;
import javax.swing.*;

class Frieren extends JPanel{
    
    public static void main(String[] args) {
        Frieren m = new Frieren();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Frieren");
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void paintComponent(Graphics g) {




        
        //hair
    g.setColor(Color.black);
    Midpoint_Circle(g, 300,300, 150,2,0,0,0,0,1,1,1,1);
    Midpoint_Circle(g, 310,295, 160,2,0,0,1,1,0,0,1,1);
    BezierCurve(g,2, 260, 190,
        200, 250,
        200, 330 ,
        200, 370);
        BezierCurve(g, 2,200,370,
        190,370,
        180,355,
        170,345);
        BezierCurve(g, 2,195,369,
        190,380,
        190,390,
        193,407);
    BezierCurve(g, 2,340,190,
        400,250,
        400,330,
        380,370);
        BezierCurve(g, 2,380,370,
        410,370,
        420,355,
        430,345);
        BezierCurve(g, 2,405,367,
        410,380,
        410,390,
        407,407);

        //tail left
    BezierCurve(g, 2,240,150,
        150,130,
        150,150,
        120,288);
        BezierCurve(g, 2,113,318,
        110,380,
        80,440,
        140,500);
        //tail right
    BezierCurve(g, 2,380,150,
        540,80,
        540,300,
        520,500);


        // /\
    BezierCurve(g,2, 285, 215,
        270, 250,
        260, 260 ,
        211, 280);
    BezierCurve(g,2, 285, 215,
        310, 250,
        320, 270 ,
        389, 280);


    //eire
    BezierCurve(g,2, 150, 300,
        50, 250,
        120, 330 ,
        162, 360
        );
        BezierCurve(g,2, 150, 315,
        137, 303,
        127, 303 ,
        110, 295
        );

    BezierCurve(g,2, 450, 300,
        590, 250,
        500, 330 ,
        437, 360
        );
        BezierCurve(g,2, 455, 315,
        460, 305,
        475, 305 ,
        500, 295
        );

        //face
    Midpoint_Circle(g,300, 285, 152, 2, 1, 1,0,0,0,0,0,0);


        //cute
    BezierCurve(g,2, 280, 310,
        250, 250,
        150, 310 ,
        280, 310);
    BezierCurve(g,2, 320, 310,
        350, 250,
        450, 310 ,
        320, 310);

        //eye
    BezierCurve(g,4, 260, 340,
        240, 320,
        230, 320 ,
        210, 340);
        BezierCurve(g,4, 210, 340,
        215, 350,
        215, 350 ,
        215, 350);
        BezierCurve(g,4, 220, 330,
        210, 330,
        210, 327 ,
        210, 325);

        Midpoint_Circle(g,240, 345, 17, 2, 1, 1,1,1,1,1,1,1);
        Midpoint_Circle(g, 241,343,5,4,1,1,1,1,1,1,1,1);
        BezierCurve(g,2, 230, 358,
        230, 344,
        250, 344 ,
        250, 358);

    BezierCurve(g,4, 330, 340,
        350, 320,
        360, 320 ,
        380, 340);
        BezierCurve(g,4, 380, 340,
        375, 350,
        375, 350 ,
        375, 350);
        BezierCurve(g,4, 345, 327,
        335, 327,
        335, 327 ,
        330, 321);

        Midpoint_Circle(g,360, 345, 17, 2, 1, 1,1,1,1,1,1,1);
        Midpoint_Circle(g, 361,343,5,4,1,1,1,1,1,1,1,1);
        BezierCurve(g,2, 350, 358,
        350, 344,
        370, 344 ,
        370, 358);


        //nouse
    plot(g, 265, 370,3);

        //mouse
    BezierCurve(g,3, 270, 400,
        280, 405,
        310, 400 ,
        310, 390);


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

            //g.drawRect(x,y,size,size);
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
    

    
    public void plot(Graphics g, int x, int y,int s) {
        // g.setColor(Color.black);
        g.fillRect(x, y, s, s);
    }

}