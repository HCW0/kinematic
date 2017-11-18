import java.util.*;
import java.util.concurrent.*;


class schedule{
    int size = 5;
    int schedule_set[][];
    int opt[][];

    schedule(){
        schedule_set = new int[size+1][2];
        opt = new int[size+1][2];
    }

    void random_Generator(int size){
        for(int i = 1; i <= size; i++){
            schedule_set[i][0] = (int)(Math.random()*size)+1; // 이득
            schedule_set[i][1] = (int)(Math.random()*size)+1; // 마감시간      
        }
    }

    void init_q(PriorityBlockingQueue<Pair<Integer,Integer,Integer>> q){
        for(int i = 1 ; i <= size ; i++){
            for(int j = 1 ; j <= schedule_set[i][1] ; j++){
                q.add(new Pair<Integer,Integer,Integer>(schedule_set[i][0],j,i));
            }
        }
    }

    void show_set(){
        System.out.println("이득\t\t마감시간");
        for(int i = 1 ; i <= size ; i++){
            System.out.println("\t"+schedule_set[i][0]+ "\t\t" +schedule_set[i][1]);
        }
    }

    
    void show_opt(){
        for(int i = 1 ; i <= size ; i++){
            System.out.print(opt[i][1]+ " ");
        }
        System.out.println();
    }

    void show_q(PriorityBlockingQueue<Pair<Integer,Integer,Integer>> q){
        Iterator<Pair<Integer,Integer,Integer>> it = q.iterator();
        while(it.hasNext()){
            Pair<Integer,Integer,Integer> tmp = it.next();
            System.out.println("스케줄 번호 : "+tmp.getID()+" 이익 : "+tmp.getLeft()+" 마감시간 : "+tmp.getRight());
        }
    }

    public static void main(String args[]){
        schedule s = new schedule();
        PriorityBlockingQueue<Pair<Integer,Integer,Integer>> q = new PriorityBlockingQueue<Pair<Integer,Integer,Integer>>();
        s.random_Generator(s.size);
        s.init_q(q);
        s.show_set();

        LABEL :
        while(q.size()>0){
            Pair<Integer,Integer,Integer> p = q.poll();
            if(s.opt[p.getRight()][0]<p.getLeft()){

                for(int i = 1 ; i <= s.size ; i++){
                    if(s.opt[i][1] == p.getID()) continue LABEL;     
                }
                s.opt[p.getRight()][0] = p.getLeft();
                s.opt[p.getRight()][1] = p.getID();
            }
        }
        s.show_opt();    
     
    }

}

class Pair<Left extends Comparable<? super Left>,Right extends Comparable<? super Right>,ID> implements Comparable<Pair<Left,Right,ID>>{

    private Left l;
    private Right r;
    private ID id;

    Pair(Left l,Right r,ID id){
        this.l = l;
        this.r = r;
        this.id = id;
    }

    public Left getLeft(){
        return this.l;
    }

    public Right getRight(){
        return this.r;
    }

    public ID getID(){
        return this.id;
    }

    public boolean equals(Object o){
        if(!(o instanceof Pair)) return false;
        Pair target = (Pair)o;
        return this.getLeft().equals(target.getLeft())&&this.getRight().equals(target.getRight())&&this.getID().equals(target.getID());
    }

    public int compareTo(Pair<Left,Right,ID> target){
        int result = this.getLeft().compareTo(target.getLeft());
        if(result == 0) result = this.getRight().compareTo(target.getRight());
        return -1*result;
    }

}