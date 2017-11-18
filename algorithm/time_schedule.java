import java.util.*;
import java.util.concurrent.*;

class time_schedule{
    
    void random_generator(PriorityBlockingQueue<Pair<Integer,Integer,Integer>> q,int size){
        int start = -1,end = -1;
        for(int i = 0 ; i < size ; i++){
            while(start >= end){
                start = (int)(Math.random()*24)+1;
                end = (int)(Math.random()*24)+1;
            }
            q.add(new Pair<Integer,Integer,Integer>(start,end,i+1));
            start = end = -1;
        }
    }

    void show_q(PriorityBlockingQueue<Pair<Integer,Integer,Integer>> l){
        Iterator<Pair<Integer,Integer,Integer>> it = l.iterator();
        Pair<Integer,Integer,Integer> p;
        while(it.hasNext()){
            p = it.next();
            System.out.println("ID : "+p.getID()+" Start : "+p.getLeft()+" End : "+p.getRight());
        }
    }

    public static void main(String args[]){
        int size = 5;
        time_schedule t = new time_schedule();
        PriorityBlockingQueue<Pair<Integer,Integer,Integer>> q = new PriorityBlockingQueue<Pair<Integer,Integer,Integer>>();
        t.random_generator(q,size);
        t.show_q(q);

        int opt[] = new int[size+1];
        int start = -1,end = -1;

        Pair<Integer,Integer,Integer> p1,p2;
        while(q.size()>1){
              p1 = q.poll();
              p2 = q.poll();
              start = p1.getLeft();
              end = p1.getRight();

              if(p1.getRight()<p2.getLeft()){
                  start = p1.getLeft();
                  end = p2.getRight();
                  opt[p1.getID()] = 1;
                  opt[p2.getID()] = 1;                  
              }else if(p1.getRight()==p2.getRight()){
                  start = Math.min(p1.getLeft(),p2.getRight());
                  end = p1.getRight();
                  if(p1.getLeft()==start) opt[p1.getID()] = 1;
                  else opt[p2.getID()] = 1;
              }else{
                  q.add(p1);
                  opt[p1.getID()]=1;
                  continue;
              }
              q.add(new Pair<Integer,Integer,Integer>(start,end,0));
        }

        System.out.println("스케줄 된 것.");

        for(int i = 1 ; i <= size ; i++){
            if(opt[i]==1) System.out.println(i);
        }

        System.out.println(start+" "+end);
    }    


}

class Pair<Left extends Comparable<? super Left>,Right extends Comparable<? super Right>,ID> implements Comparable<Pair<Left,Right,ID>>{

    private Left l;
    private Right r;
    private ID id;

    Pair(Left l, Right r,ID id){
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
        int result = this.getRight().compareTo(target.getRight());
        if(result==0) result = this.getLeft().compareTo(target.getLeft());
        return result;
    }

}