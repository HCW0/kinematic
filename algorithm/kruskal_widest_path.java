
import java.util.*;
import java.io.*;


class kruskal_widest_path{

    int size;
    int parent[];
    int weight[][]; // 상수
    int edge[][];   // 상수
    union_find uf = new union_find();

    kruskal_widest_path(int size){
        this.size = size;
        parent = new int[size];
        weight = new int[size][size];
        edge = new int[size][size];        
        for(int i = 0 ; i < size ; i++){
            parent[i] = -1;
        }
    }

    boolean isEnd(){
        for(int i = 0 ; i < parent.length ; i++){
            if(parent[i]==-1*size) return false;
        }
            return true;
    }

    void run(){
        int min=Integer.MAX_VALUE/2;
        int target_i=-1,target_j=-1;
        while(isEnd()){
            //여기가 크루스칼 알고리즘의 단위 연산
            min = Integer.MAX_VALUE/2;
            for(int i = 0 ; i < size ; i++){
                for(int j = 0 ; j < i ; j++){
                    if(weight[i][j]<min&&weight[i][j]>0){
                        min = weight[i][j];
                        target_i = i;
                        target_j = j;
                    }//엣지 중에 가장 작은 엣지 선택
                }   
            }
            if(uf.find(target_i)==uf.find(target_j)){
                weight[target_i][target_j] *= -1; // 가중치를 보존한채 음수로 만들어서 검색 대상에서 						제외시킴.
                continue; // 두 정점이 서로서 집합이 아니라면 순환을 만드므로 거름
            }else{
                uf.merge(target_i,target_j);
                edge[target_i][target_j] = edge[target_j][target_i] = 100000 - min; // 엣지 등록
            }
        }
    }

    void init_weight(BufferedReader br,int m) throws Exception{
        String[] parsing;
        for(int i = 1 ; i <= m ; i ++){
            parsing = br.readLine().split(" ");
            weight[Integer.parseInt(parsing[0])-1][Integer.parseInt(parsing[1])-1] = 100000 - Integer.parseInt(parsing[2]);        
            weight[Integer.parseInt(parsing[1])-1][Integer.parseInt(parsing[0])-1] = weight[Integer.parseInt(parsing[0])-1][Integer.parseInt(parsing[1])-1];
        }
    }


    String path;
    void DPS(int s,int t,String code){
        String tmp;
            for(int i = 0 ; i < size ; i ++){
                if(edge[s][i]>0&&i!=t){
                        edge[s][i]=0;
                        edge[i][s]=0;
                        tmp = code;
                        tmp += (","+(i+1));
                        DPS(i,t,tmp);
                }else if(edge[s][i]>0&&i==t){
                    code += ","+(i+1);
                    path = code;
                }
            }
    }

    int generate_answer(){
        String[] parsing = path.split(",");
        int widest = Integer.MAX_VALUE;
        for(int i = 0 ; i < parsing.length-1 ; i++){
            int tmp = 100000 - Math.abs(weight[Integer.parseInt(parsing[i])-1][Integer.parseInt(parsing[i+1])-1]);
            widest = widest < tmp ? widest : tmp;
        }
        return widest;
    }


    private class union_find{

        int find(int value){
            if(parent[value]<0) return value;
            parent[value] = find(parent[value]);
            return find(parent[value]);
        }

        void merge(int a, int b){
            a = find(a);
            b = find(b);
            if(a==b) return;
            parent[a] += parent[b]; // p[] 의 음수 크기는 곧, 해당 index를 루트로하는 집합의 크기를 					나타낸다.
            parent[b] = a;
        }

    }


    public static void main(String args[]){

         try{
            BufferedReader br = new BufferedReader(new FileReader(".\\rsc\\262_widest_input.txt"));
            int case_num = Integer.parseInt(br.readLine());
            kruskal_widest_path self;
            String[] parsing;
            int n,m,s,t;

            while(case_num>0){
                case_num--;
                parsing = br.readLine().split(" ");
                n = Integer.parseInt(parsing[0]);
                m = Integer.parseInt(parsing[1]);
                s = Integer.parseInt(parsing[2]);
                t = Integer.parseInt(parsing[3]);
                self = new kruskal_widest_path(n);
                self.init_weight(br,m);
                self.run();
                self.DPS(s-1,t-1,""+(s));
                System.out.println(self.generate_answer());
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

}

