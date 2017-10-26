import java.awt.*;
import javax.swing.*;

public class bezier{

    DoublePoint set[] = new DoublePoint[4];       


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
        set[3] = new DoublePoint(400.0,400.0);    
        set[1] = p1;
        set[2] = p2;
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
        MainActivity m = new MainActivity();
        m.init();
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


class MainActivity extends JFrame{

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 1000;

    bezier b = new bezier();

    void init(){
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setTitle("bezier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void paint(Graphics g){
        super.paint(g);
        
        // 좌표계 그리기
        g.drawLine(WINDOW_WIDTH/2,0,WINDOW_WIDTH/2,WINDOW_HEIGHT);
        g.drawLine(0,WINDOW_HEIGHT/2,WINDOW_WIDTH,WINDOW_HEIGHT/2);

        
        // 베지어 곡선 그리기
        g.setColor(Color.RED);
        b.setBezierParameters(new DoublePoint(130.0,0.0),new DoublePoint(260.0,400.0));
        double u = 0.0;
        DoublePoint drawingPoint;
        while(u<1.0){
            drawingPoint = b.getBezierLinearCombination(u);
            g.drawOval((int)(WINDOW_WIDTH/2+drawingPoint.x),(int)(WINDOW_HEIGHT/2-drawingPoint.y),1,1);
            u+=0.001;
        }

        
        // 베지어 곡선 기준 점 그리기
        g.setColor(Color.BLUE);
        for(int i = 0 ; i <= 3 ; i++){
            g.fillOval(WINDOW_WIDTH/2+(int)b.set[i].x,WINDOW_HEIGHT/2-(int)b.set[i].y,9,9);
            g.drawString("P"+i,WINDOW_WIDTH/2+(int)b.set[i].x+3,WINDOW_HEIGHT/2-(int)b.set[i].y-3);
        }


    }

}