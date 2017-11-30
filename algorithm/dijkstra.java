import java.io.*;
import java.util.*;

class dijkstra{

    int size,start;
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
            BufferedReader br = new BufferedReader(new FileReader(".\\rsc\\weight.txt"));
            this.size = Integer.parseInt(br.readLine()); // 1라인 정점수
            this.init();
            this.start = Integer.parseInt(br.readLine())-1; //2라인 시작 정점
            String parsing;
            String parsing_buffer[];
            int i,j,w;
            while((parsing=br.readLine())!=null){
                parsing_buffer = parsing.split(" ");
                i = Integer.parseInt(parsing_buffer[0])-1;
                j = Integer.parseInt(parsing_buffer[1])-1;
                w = Integer.parseInt(parsing_buffer[2]);                
                weight[i][j] = w;
                weight[j][i] = w;
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
                if(weight[i][j]==0) weight[i][j]=Integer.MAX_VALUE/2;
            }
        }
    }

    void shortest_path(int end){
        if(start==end){
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
        int min = -1;
        for(int i = 0 ; i < size ; i++){
                distance[i] = weight[start][i]; // 초기 정점은 start, 기준으로 거리 설정
                touch[i] = start;
        }

        for(int j = 0 ; j < size ; j++){

            //start로부터 가장 가까운 정점을 고른다.
            min = Integer.MAX_VALUE/2;
            for(int i = 0 ; i < size ; i++){
                if(min>distance[i]&&distance[i]>0){
                    // 음수라는 것은 이미 Y집합으로 이동했다는 것을 의미.
                    min = distance[i];
                    nearest_Vertex = i;
                }
            }
            // 가장 가까운 정점을 기준으로 distance touch를 재설정해준다.
            for(int i = 0 ; i < size ; i++){
                // V-Y에 남아있는 정점을 대상으로
                    if(distance[nearest_Vertex] + weight[nearest_Vertex][i]<distance[i]){
                        // 기존의 Y집합으로부터 최단 거리가, 신규 nearest_Vertex에 의해
                        // 더 가까운 최단 경유 방법이 생겼다면 그쪽으로 최신화시켜준다.
                        //
                        distance[i] =  distance[nearest_Vertex] + weight[nearest_Vertex][i];
                        // 각 점에서 가장 가까운 Y 정점도 nearest_Vertex로 최신화시켜준다.
                        // touch를 추적하는 것으로 경로를 구할 수 있다.
                        touch[i] = nearest_Vertex;
                    }
                
            }
                  
                // 음수화 시켜주는게 Y집합으로 이동이다.
            distance[nearest_Vertex] = -1*distance[nearest_Vertex];

        }

    }


    public static void main(String args[]){
        dijkstra wp = new dijkstra();
        wp.readData();
        wp.givelife();
        wp.show_weight();
        wp.run();
        wp.shortest_path(5);
    }


}



