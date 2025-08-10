import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
class Lab_5 extends JPanel implements Runnable {


    double circleMove = 0;                    //  |
    double squareRotate = 0;                  //  |
    boolean switchDirection = false;          //  | ส่วนนี้เพิ่มตัวแปรที่ต้องการให้ขยับออซัมติง
    double squareMove = 0;                    //  |
    int millisecound = 0;                     //  |



    public static void main(String[] args) {
        Lab_5 m = new Lab_5();                              //  |
        JFrame f=new JFrame();                              //  |
        f.add(m);                                           //  |
        f.setTitle("AllGraphics");                    //  |   อันนี้ปกติไม่มีไร
        f.setSize(600,600);                    //  |
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //  |
        f.setVisible(true);                               //  |

        (new Thread(m)).start();                            //  |อันนี้ต้องเพิ่มเพื่อให้มันวาดรูปใหม่
    }

    @Override                                               //  |   อันนี้ต้องโอเวอไรด์เพื่อให้วาดภาพใหม่ได้ซ้ำมั้ง
    public void paintComponent(Graphics g)  {

        Graphics2D g2 = (Graphics2D) g;                     //  |  ต้องใช้ Graphics2D เพราะจะให้มันหมุนหรือทำต่างๆได้


        g2.setColor(Color.WHITE);                           //  |  อันนี้คือวาดหน้ากระดาษขาวใหม่
        g2.fillRect(0, 0, 600, 600);       //  |

        g2.setColor(Color.BLACK);
        g2.translate(circleMove, 0);
        g2.drawOval(0, 0, 100, 100);
        g2.translate(-circleMove, 0);

        
        g2.rotate(squareRotate, 300, 300);
        g2.drawRect(200, 200, 200, 200);
        g2.rotate(-squareRotate, 300, 300);

        g2.setColor(Color.RED);
        g2.translate(squareMove+20, -squareMove);
        g2.drawRect(0, 470, 100, 100);
        g2.translate(-squareMove+20, squareMove);

    }

    public void run() {
        double lastTime = System.currentTimeMillis();
        double currentTime, elapsedTime;

        while (true) {
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            if(switchDirection) {
                circleMove -= 50.0 * elapsedTime / 100.0; 
            }else circleMove += 50.0 * elapsedTime / 1000.0;//to the right

            if (circleMove >= 500.0 || (switchDirection && circleMove < 0)) {
                switchDirection = !switchDirection; // Switch direction back
            }
            //start at 3 seconds
            if(millisecound >= 3000) {
                if (squareMove >= 470.0 && millisecound > 3000){ // Reset square position
                }else squareMove += 100 * elapsedTime / 1000.0;
            }
            squareRotate += 0.5 * elapsedTime / 1000.0;
            millisecound += elapsedTime;
            // Display
            repaint();
        }
    }
}
