import java.io.*;
import java.util.*;

class widest_path{

    int num;
    int size,start,end;
    int weight[][];
    int[] touch,distance;
    int nearest_Vertex;


    void init(){
        weight = new int[size][size];
        touch = new int[size];
        distance = new int[size];
    }

    void readData(){

        try{
            BufferedReader br = new BufferedReader(new FileReader(".\\rsc\\262_widest_input.txt"));
            this.num = Integer.parseInt(br.readLine()); // 1라인 사례
                while(num>0){

                    String parsing;
                    String parsing_buffer[];
                    int i,j,w,m;

                    parsing=br.readLine();
                    parsing_buffer = parsing.split(" ");
                    this.size = Integer.parseInt(parsing_buffer[0]);
                    this.init();
                    m = Integer.parseInt(parsing_buffer[1]);
                    this.start = Integer.parseInt(parsing_buffer[2])-1;
                    this.end = Integer.parseInt(parsing_buffer[3])-1;

                        while(m>0){
                            parsing=br.readLine();                            
                            parsing_buffer = parsing.split(" ");
                            i = Integer.parseInt(parsing_buffer[0])-1;
                            j = Integer.parseInt(parsing_buffer[1])-1;
                            w = Integer.parseInt(parsing_buffer[2]);                
                            weight[i][j] = w;
                            weight[j][i] = w;
                            m--;
                        }

                    this.givelife();
                    this.run();
                    this.shortest_path(end+1);

                    num--;
                }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    void show_weight(){
        for(int i = 0 ; i < size ; i++){
            System.out.println(Arrays.toString(weight[i]));
        }
    }

    void givelife(){
        for(int i = 0 ; i < size ; i++){
            for(int j = 0 ; j < size ; j++){
                if(weight[i][j]==0) weight[i][j]=-1;
            }
        }
    }

    void shortest_path(int end){
        if(start+1==end){
            System.out.println("시점과 종점이 같은 요구입니다.");
            return;
        }
        if(end>size||end<1){
            System.out.println("종점의 범위를 벗어난 요구입니다.");
            return;
        }
        int e = end-1;
        String result="v"+(end);
        int cnt = 0;
        while(touch[e]!=start&&cnt<size){
            result = ("v"+(touch[e]+1)) + " " + result;
            e = touch[e];
            cnt ++;
        }
        if(cnt==size){
            System.out.println("해당 포인트로 가능 경로가 존재하지 않습니다.");
        }else{
            result = ("v"+(start+1)) + " " + result;
            System.out.println("v" + (start+1) + " to v" + (end) + " \'s shortest path is " + result);
            System.out.println("v" + (start+1) + " to v" + (end) + " \'s shortest weight is " + -1*distance[end-1]);            
        }
        
    }

    void run(){
        int max = -1;
        for(int i = 0 ; i < size ; i++){
                distance[i] = weight[start][i]; // 초기 정점은 start, 기준으로 거리 설정
                touch[i] = start;
        }

        for(int j = 0 ; j < size ; j++){

            //start로부터 가장 큰 가중치를 가지는 정점을 고른다.
            max = 0;
            for(int i = 0 ; i < size ; i++){
                if(max<distance[i]){
                    // 음수라는 것은 이미 Y집합으로 이동했다는 것을 의미. 혹은, 연결이 없음을 의미
                    // 처음에는 연결 없음을 나타내는 -1 조차도, 알고리즘이 진행되면서 연결값==양수가 할당되어
                    // 해당 반복문에 들어올 수 있다.
                    //
                    max = distance[i];
                    nearest_Vertex = i;
                }
            }
            // System.out.println(Arrays.toString(distance));
            // System.out.println(Arrays.toString(touch));
            // System.out.println(" neareast : "+nearest_Vertex);            
            
            for(int i = 0 ; i < size ; i++){
                // V-Y에 남아있는 정점을 대상으로
           
                    if(Math.min(distance[nearest_Vertex] , weight[nearest_Vertex][i])>Math.abs(distance[i])){
                        
                        // 기존의 Y집합으로부터 최대 가중치가, 신규 nearest_Vertex에 의해
                        // 더 큰 대역폭을 가지는 경유 방법이 생겼다면 그쪽으로 최신화시켜준다.
                        //
                        // 최단 거리 다익스트라에서는 큰 값을 기준으로 최적해를 구했으므로, 딱히 음수의 값을 고려하지
                        // 않았지만 여기서는 최소값도 고려하므로 비교 대상이 되는 distance[i]의 절대치를 사용한다.
                        //
                        distance[i] =  Math.min(distance[nearest_Vertex] , weight[nearest_Vertex][i]);
                        // touch를 추적하는 것으로 경로를 구할 수 있다.
                        touch[i] = nearest_Vertex;
                    }

            }
                // 음수화 시켜주는게 Y집합으로 이동이다.
                distance[nearest_Vertex] = -1*distance[nearest_Vertex];

        }

    }


    public static void main(String args[]){
        widest_path wp; 
        wp = new widest_path();
        wp.readData();
    }


}



