import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.*;
import javax.swing.*;

public class bezier{

    DoublePoint set[] = new DoublePoint[4];       

    bezier(double p1x,double p1y, double p2x, double p2y){
        set[1] = new DoublePoint(p1x,p1y);
        set[2] = new DoublePoint(p2x,p2y);
    }


    int factorial(int start, int n){
        int result = 1;
        while(n>=start&&n!=1){
            result *= n;
            n--;
        }
        return result;
    }

    int get_Combination(int n, int r){
        return factorial(r+1,n)/(factorial(1,n-r));
    }

    void setBezierParameters(DoublePoint p1,DoublePoint p2){
        set[0] = new DoublePoint(0,0);
        set[1] = p1;
        set[2] = p2;
        set[3] = new DoublePoint(1.0,1.0);    
    }

    double getBezierFactor(int i,double u){
        return get_Combination(3,i)*Math.pow(u,i)*Math.pow(1-u,3-i);
    }

    DoublePoint getBezierLinearCombination(double u){
        DoublePoint result = new DoublePoint(0.0,0.0);
        for(int i = 0 ; i <= 3 ; i++){
            result.x += getBezierFactor(i,u)*set[i].x;
            result.y += getBezierFactor(i,u)*set[i].y;            
        }
        return result;
    }

    public static void main(String args[]){
        Thread th = new Thread(new MainActivity());
        th.start();
    }


}


class DoublePoint extends Point{
    double x,y;
    DoublePoint(double x,double y){
        super((int)x,(int)y);
        this.x=x;
        this.y=y;
    }
}


class MainActivity extends JFrame implements Runnable{

    Ball b;
    GameBoard mainPane;
    bezier ease_in = new bezier(0.44,0.22,0.88,0.88);
    bezier ease_out = new bezier(0.88,0.88,0.44,0.22);    

    void init(){
   
        setTitle("bezier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        mainPane = new GameBoard(800,600);
        this.setContentPane(mainPane);
        gameObject_init();
        add

        pack();
        setVisible(true);
    }


    void gameObject_init(){
        b = new Ball(200,200);        
        Booster booster1 = new Booster(440,440,".\\rsc\\pic\\booster.png");
        b.addBooster(booster1);
        mainPane.add(b);
    }


    public void paint(Graphics g){
        super.paint(g);
    }
    
    public void run(){
        init();

        while(true){
            try{
                    Thread.sleep(10);
                    b.ball_kinematic_processing();
                    b.apply_force(0.999);
                    b.gravity_force();
                    b.stop_check();
                    // b.zero_force();
                    
                    repaint();
            }catch(Exception e){
                    e.printStackTrace();
            }
        }

    }
    

}

class GameBoard extends JPanel{
    int width = 0;
    int height = 0 ;

    GameBoard(int width,int height){
        // setLayout(null);
        this.width = width;
        this.height = height;
    }

    public Dimension getPreferredSize(){
        return new Dimension(width,height);
    }
    public Dimension getMaximumSize(){
        return new Dimension(width,height);
    }
    public Dimension getMinimumSize(){
        return new Dimension(width,height);
    }

    
    // public void paint(Graphics g){
    //     super.paint(g);
    // }
    

}

class Ball extends JComponent{

    double x = 0.0;
    double y = 0.0;
    double dx = 1.0;
    double dy = 1.0;    
    double speed = 1.0;
    int width = 800;
    int height = 600;

    Booster booster1;

    Ball(double x, double y){
        this.x =x;
        this.y =y;
        setPreferredSize(new Dimension(width,height));
    }

    void addBooster(Booster booster1){
        this.booster1 = booster1;
    }

    void setCoord(double x, double y){
        this.x =x ;
        this.y =y;      
    }

    void apply_force(double mul){
        this.speed *= mul;
        
    }

    void zero_force(){
        this.speed = 0;
    }

    void gravity_force(){
        if(Math.abs(speed)>0.001)
             this.dy += 0.98;
    }

    void ball_kinematic_processing(){

        if(y+12.5+speed*dy>height || y-12.5+speed*dy<0){
            dy = -dy;
            dy *=0.999;
        }
        
        if(x+12.5+speed*dx>width || x-12.5+speed*dx<0){
            dx = -dx;
            dx *=0.999;
        }

        x += speed*dx;
        y += speed*dy;

    }

    void stop_check(){
        if(speed<.000001) speed = 0;
    }

    void ball_collide_processing(Ellipse2D e2d){
        if(e2d.intersects(booster1.x,booster1.y,25,25)&&booster1.flag)
            {booster1.run(this);
            }else if(!e2d.intersects(booster1.x,booster1.y,25,25)){
                booster1.flag = true;
            }
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(booster1.img,(int)booster1.x,(int)booster1.y,this);       

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.RED);
        Ellipse2D.Double e2d = new Ellipse2D.Double(x-12.5,y-12.5,25,25); // 중심맞추기
        g2.fill(e2d);

        ball_collide_processing(e2d);
    }

}



class GameObject{

    double x = 0.0;
     double y = 0.0;
    Image img;

    GameObject(double x, double y, String url){
        this.x = x;
        this.y = y;
        img = Toolkit.getDefaultToolkit().getImage(url);
    }

    void run(){

    }

}

class Booster extends GameObject{
    Booster(double x,double y, String url){
        super(x,y,url);
    }

    boolean flag = true;

    void run(Ball ball){
        if(flag){
            ball.apply_force(1.1);
            ball.dx = ball.dx>0?2*ball.dx:-2*ball.dx;
            flag = false;
        }
    }
}


class TestMouse extends MouseAdapter{

    GameObject mouseTag ;

    TestMouse(GameObject mouseTag){
        this.mouseTag = mouseTag;
    }

    public mouseMoved(MouseMotionEvent mme){
            mouseTag.setLocation(mme.getX(),mme.getY());
    }
}