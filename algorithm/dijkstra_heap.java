import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.*;

class dijkstra{

    int size;       // 입력 사례의 사이즈 = 정점의 개수
    int weight[][]; // 가중치 배열
    // int touch[];    // 최단 거리를 구했을 때 종점이 i인 경우 그 i와 함께 엣지를 구성하는 정점의 인덱스를 가짐 
    int length[];   // 각 정점으로의 최단거리
    int near_vertex_tmp;    // 최단거리경로에 포함될 정점을 가리키는 임시 변수

    dijkstra(int size){ //  초기화
        this.size = size;
        weight = new int[size][size];
        // touch = new int[size];
        length = new int[size];
    }

    void init(){    // 랜덤으로 가중치 설정
        for(int i = 0 ; i < size ; i++){
            for(int j = 0 ; j < i ; j++){
                weight[i][j] = (int)(Math.random()*(size));
                if(weight[i][j]==0) weight[i][j] = Integer.MAX_VALUE/2;
                weight[j][i] = weight[i][j];
            }
        }   
    }

    void run(int start){

            Queue<Pair<Integer,Integer>> queue = new PriorityBlockingQueue<Pair<Integer,Integer>>();
            // 시점을 기준으로 각 정점을 vi, vi와 시점이 구성하는 엣지의 가중치를 ei라고 했을 때
            // 최소 힙에 <ei,vi> 형태로 저장함.

            for(int i = 0 ; i < size ; i++ ){
                if(weight[start][i]==0 || weight[start][i]==Integer.MAX_VALUE/2) continue;
                // 비연결을 나타내는 MAX_VALUE/2 자기자신인 0의 값은 넣지 않고 거름
                queue.add(new Pair<Integer,Integer>(weight[start][i],i));
                length[i] = weight[start][i];
                // 최단 거리 값을 초기화함.
                // touch[i] = start;
                // 최단 거리 페어 값을 초기화함.
            }

            Iterator<Pair<Integer,Integer>> it;
            Pair<Integer,Integer> target;
            int near_vertex;
            // 힙에서 꺼내온 정점을 담는 변수            
            int near_weight;
            // 힙에서 꺼내온 가중치를 담는 변수


            while(queue.size()!=0){
                target = queue.poll();
                near_weight = target.getLeft();
                near_vertex = target.getRight();
                // 힙에서 가장 작은 가중치를 지닌 엣지와 그 종점을 가져온다.
                                
                if(length[near_vertex] < 0) continue;
                // 만약 해당 종점이 이미 계산된 후라면 패스한다.

                if(length[near_vertex]!=near_weight){
                // 만약, 경로가 바뀌었다면 이전 length의 값과 힙에서
                // 가져온 가중치의 값이 다를 것이다.
                //
                // 
                // 따라서, 현재 near_vertex의 짝을 알 수 있다면
                // 그 짝의 값을 touch[near_vertex]에 넣어
                // 경로를 파악할 수 있지만, 그러려면 Pair 구조를 
                // 확장해서 시점도 같이 저장하게 하는 수 밖에 없어서 
                // 경로를 구하는 기능은 포기한다.
                }
                
                length[near_vertex] = -1 * near_weight;
                // 최단 거리 정보를 갱신한다.
                


                for(int i = 0 ; i < size ; i++){
                    if(weight[near_vertex][i]==0 || weight[near_vertex][i]==Integer.MAX_VALUE/2) continue;
                    if(length[i] < 0) continue;
                    if(i==start) continue;
                    queue.add(new Pair<Integer,Integer>(weight[near_vertex][i]+near_weight,i));       
                }


            }
    }


    void show_weight(){
        System.out.println(Arrays.deepToString(weight));
    }

    // void show_touch(){
    //     System.out.println("touch array = > "+Arrays.toString(touch));        
    // }
    
    void show_length(){
        System.out.print("length array = > [");
        for(int i = 0 ; i < size-1 ; i++){
            System.out.print(Math.abs(length[i])+", ");
        }
        System.out.println(Math.abs(length[size-1])+"]");
    }


    public static void main(String args[]){
            int size = 5;
        dijkstra s = new dijkstra(size);
        s.init();
        s.run(0);
        s.show_weight();
        s.show_length();
        // s.show_touch();

        new Thread(new dijkstra_graph(size,s.weight,s.weight)).start();

    }


}



class dijkstra_graph extends JFrame implements Runnable{
    int size;
    int[][] weight,edge;
    dijkstra_graph(int size,int[][] weight,int[][] edge){
        this.size = size;
        this.weight = weight;
        this.edge = edge;
    }

    void init(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new inner_panel());
        makeGUI();

        pack();
        setVisible(true);
    }

    private class inner_panel extends JPanel{

        int WIDTH = 500;
        int HEIGHT = 500;
        int[][] weight, edge;
        int size;
        java.util.List<Point2D.Double> vertexList = new ArrayList<Point2D.Double>();
        double radius = WIDTH/3;
        
        inner_panel(){
            this.weight = dijkstra_graph.this.weight;
            this.edge = dijkstra_graph.this.edge;
            this.size = dijkstra_graph.this.size;            
            setPreferredSize(new Dimension(WIDTH,HEIGHT));
            setVertexList(size);
        }

        void setVertexList(int n){
            double coord_X = -1,coord_Y=-1;
            double theta = -1;
            for(int i = 0 ; i < n ; i++){
                theta = i*360/n;
                theta = Math.toRadians(theta);
                coord_X = WIDTH/2 + radius * Math.cos(theta);
                coord_Y = HEIGHT/2 + radius * Math.sin(theta);                
                vertexList.add(new Point2D.Double(coord_X,coord_Y));
            }
        }

        public void paintComponent(Graphics g){
            drawEdge(g);
            drawWeight(g);
            drawCircleDot(g);
        }

        void drawCircleDot(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            Color backup_clr = g.getColor();
            BasicStroke backup_strk = (BasicStroke)g2.getStroke();
            Line2D.Double dot;
            Point2D.Double point;
            g.setColor(Color.RED);
            g2.setStroke(new BasicStroke(8));
            for(int i = 0 ; i < size ; i++){
                point= vertexList.get(i);
                dot = new Line2D.Double(point,point);
                g2.draw(dot);
            }
            g.setColor(backup_clr);
            g2.setStroke(backup_strk);
        }

        void drawWeight(Graphics g){
            Point2D.Double p1,p2;
            g.setFont(new Font("ds",1,24));
            for(int i = 0 ; i < size ; i++){
                for(int j = 0 ; j < i ; j++){
                    if(weight[i][j]!=0){
                        p1 = vertexList.get(i);
                        p2 = vertexList.get(j);
                        String s = ""+Math.abs(weight[i][j]);
                        if(weight[i][j]==Integer.MAX_VALUE/2) s = "";
                        g.drawString(s,(int)(p2.getX()+p1.getX())/2-10,(int)(p2.getY()+p1.getY())/2-10);
                    }
                }   
            }
        }

        void drawEdge(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            Color backup_clr = g.getColor();
            BasicStroke backup_strk = (BasicStroke)g2.getStroke();
            Line2D.Double line;
            Point2D.Double p1,p2;            

            g2.setStroke(new BasicStroke(3));
            for(int i = 0 ; i < size ; i++){
                for(int j = 0 ; j < i ; j++){
                    if(weight[i][j]!=Integer.MAX_VALUE/2){
                        g.setColor(Color.GREEN);
                        p1 = vertexList.get(i);
                        p2 = vertexList.get(j);
                        line = new Line2D.Double(p1,p2);
                        g2.draw(line);
                    }
                }   
            }
            g.setColor(backup_clr);
            g2.setStroke(backup_strk);
        }
    }

    public void run(){
        init();
        try{
            while(true){
                Thread.sleep(200);
                repaint();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void makeGUI(){

    }

}




class Pair<Left extends Comparable<? super Left>,Right extends Comparable<? super Right>> implements Comparable<Pair<Left,Right>>{
// 페어를 구성하는 Left 자료형은 일단 Comparable 인터페이스를 구현했고, 해당
// 인터페이스는 Left를 하위 객체로 가지는 슈퍼 클래스에 대한 비교문으로 구현되어 있다.
// 라고 JVM에게 알려 탬플릿을 작성하게 한다.

    private Left left;
    private Right right;

    Pair(Left left,Right right){
        this.left = left;
        this.right = right;
    }

    public Left getLeft(){
        return left;
    }

    public Right getRight(){
        return right;
    }

    public boolean equals(Object o){
        if (!(o instanceof Pair)) return false;
        Pair target = (Pair) o;
        return this.left.equals(target.getLeft()) && this.right.equals(target.getRight());
    }

    public int compareTo(Pair<Left,Right> c){
        int result = this.getLeft().compareTo(c.getLeft());
        if(result == 0) result = this.getRight().compareTo(c.getRight());
        return result;
    }

}