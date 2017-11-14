import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

class prim extends JFrame implements Runnable{

    int size;                   // 입력 사례 : size개의 정점
    int weight[][];             // 각 정점이 이루는 엣지의 가중치를 담는 배열
    int edge[][];               // 해당 프로그램은 완전 그래프를 가지고 최소 신장 트리를 찾음. 즉, 완전 그래프의 엣지 집합 E에서 임의의 최소 신장 트리에 대한
                                // 서브 엣지 집합이 필요한데, 그 역할을 하며 내부는 0으로 초기화 되고 최소 신장 트리의 정점이라면 1로 나타냄.
    int distance[];             // 단위 연산에사 사용되는 배열로, i번째 원소는 '1번째 정점이 다른 정점과 이루는 최소 가중치 거리'를 나타냄.
    int nearest_target[];       // 위의 distance 배열과 같이 i번째 정점과 가장 가까이에 있는 원소를 나타냄.
    
    Random r = new Random();    // 랜덤으로 가중치 설정
    
    java.util.List<Point2D.Double> V_Y = new ArrayList<Point2D.Double>();   // 랜더링을 하기 위한 점을 저장하는 컬렉션
    java.util.List<Integer> vertex_Set = new ArrayList<Integer>();          // 프림 알고리즘에서는 보통 정점 집합 V,Y에 대해 V-Y의 원소를 Y에 전부 옮기는 것을
                                                                            // 알고리즘 완료 조건으로 삼는데, 해당 컬렉션은 V-Y를 나타낸다.
    
    int pWIDTH,pHEIGHT;         // 캔버스의 크기

    prim(int size,int edge){    // 해당 스윙 스레드를 생성하는데에 정점 개수랑 화면 사이즈(정방 윈도우)를 정할 수 있음
        this.size = size;
        this.weight = new int[size][size];
        this.edge = new int[size][size];
        this.distance = new int[size];
        this.nearest_target = new int[size];
        pWIDTH = pHEIGHT = edge;
    }

    void init(){    // 각 배열을 초기화하는 함수
        for(int i = 0 ; i < size ; i++){
            nearest_target[i] = 0;
            if(i!=0) vertex_Set.add(i);
            for(int j = 0; j < size ; j++){
                edge[i][j] = 0;
                if(i==j) weight[i][j] = 0;
                if(i>j) {
                    weight[i][j] = weight[j][i] = r.nextInt(size)+1;    // 랜덤으로 엣지들의 가중치가 정해진다.
                }                                        
            }
        }
    }

    void gui_init(){
        init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new inner_JPanel());
        getContentPane().add(new prim_Button("Prim!"));
        getContentPane().add(new init_Button("Init!"));        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void show_array(){

        System.out.println(Arrays.deepToString(weight));
        System.out.println(Arrays.deepToString(edge));
        
        
    }

    private class inner_JPanel extends JPanel{
        boolean flag = true;    // 최초 1회에만, 각 정점의 정보를 arrayList에 추가하면 되므로 간단히 플래그를 사용해서 최초 시행 여부를 검사한다.

        inner_JPanel(){
            setPreferredSize(new Dimension(pWIDTH,pHEIGHT));
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            circle_dot(g);
            interpolate_weight(g);
            draw_Label_dot(g);
            draw_Label_weight(g);
        }

        void circle_dot(Graphics g){    // 정점이 개수에 따라 원형으로 정점의 좌표를 선정하고 그리는 메서드.
            int radius = pWIDTH/3;
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(3));
            double degree;
            Point2D.Double p;
            for(int i = 0 ; i<size ; i++){
                degree = Math.toRadians((360/size)*i);
                p = new Point2D.Double(pWIDTH/2+radius*Math.cos(degree),pHEIGHT/2+radius*Math.sin(degree));
                g2.draw(new Line2D.Double(p,p));
                if(flag) V_Y.add(p);
            }
            flag = false;
        }

        void interpolate_weight(Graphics g){    // 각 정점 사이에 선을 긋는 메서드, edge 배열에 의해 최소 신장 트리의 정점이 선정되어 있다면 그쪽도 다른색으로 그려준다.
            Graphics2D g2 = (Graphics2D)g;
            for(int i = 0 ; i < size ; i++){
                for(int j = 0 ; j < size ; j++){
                    if(edge[i][j]==0&&edge[j][i]==0){
                        g2.draw(new Line2D.Double(V_Y.get(i),V_Y.get(j)));
                    }else{
                        Color backup = g.getColor();
                        g.setColor(Color.GREEN);
                        g2.draw(new Line2D.Double(V_Y.get(i),V_Y.get(j)));
                        g.setColor(backup);                            
                    }
                }   
            }
        }

        void draw_Label_dot(Graphics g){    // 각 정점의 라벨을 그리는 메서드
            Color backup = g.getColor();
            g.setFont(new Font("TimesRoman", Font.PLAIN, 23)); 
            g.setColor(Color.RED);
            Point2D.Double p;
            for(int i = 0 ; i < size ; i++){
                p = V_Y.get(i);
                g.drawString(("V"+i),(int)p.getX()-10,(int)p.getY()-10);
            }
            g.setColor(backup);
        }

        void draw_Label_weight(Graphics g){ // 각 엣지의 가중치를 그리는 메서드
            Point2D.Double p,p2;
            Color backup = g.getColor();
            g.setFont(new Font("TimesRoman", Font.PLAIN, 23)); 
            g.setColor(Color.BLUE);
            for(int i = 0 ; i < size ; i++){
                for(int j = 0 ; j < size ; j++){
                    p = V_Y.get(i);
                    p2 = V_Y.get(j);
                    if(i>j){
                        g.drawString((""+weight[i][j]),(int)((p.getX()+p2.getX())/2)-10,(int)((p.getY()+p2.getY())/2)-10);
                    }
                }   
            }
            g.setColor(backup);            
        }

    }

    private class prim_Button extends JButton{
        prim_Button(String s){
            this.setPreferredSize(new Dimension(100,40));            
            this.setAction(new AbstractAction(){
                    public void actionPerformed(ActionEvent ae){
                        prim_init();
                        prim_run();
                    }
            });
            this.setText(s);
        }
    }
    private class init_Button extends JButton{
        init_Button(String s){
            this.setPreferredSize(new Dimension(100,40));
            this.setAction(new AbstractAction(){
                    public void actionPerformed(ActionEvent ae){
                        prim.this.init();
                    }
            });
            this.setText(s);
        }
    }

    void prim_init(){
        for(int i = 0 ; i < size ; i++){
            distance[i] = weight[i][0];
            nearest_target[i] = 0;
        }
        // 타겟 초기화
    }

    void prim_run(){
       //프림 알고리즘 단위연산이 여기에 위치한다.
        int min = Integer.MAX_VALUE;
        int v = 0;

        while(vertex_Set.size()!=0){
            min = Integer.MAX_VALUE;
            for(int i = 1 ; i < size ; i++){
                if(distance[i]<min && distance[i]>0){
                    min = distance[i];
                    v = i;        
                }
            }

            edge[v][nearest_target[v]] = 1;
            int index = vertex_Set.indexOf(v);
            vertex_Set.remove(index);

            for(int i = 1 ; i < size ; i++){
                if(distance[i]>weight[i][v]){
                    distance[i] = weight[i][v];
                    nearest_target[i] = v;
                }
            }

        }


    }

    public void run(){
        gui_init();
        while(true){
            try{
                // init();
                // prim_init();
                // prim_run(); // night fever
                Thread.sleep(300);
                repaint();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}

