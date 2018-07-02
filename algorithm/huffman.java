import java.util.*;

class huffman{
    int size;
    int weight[][];
    String weight_code[];
		// bring commit points
    huffman(int size){
        this.size = size;
        weight = new int[size][2];
        weight_code = new String[size];
    }

    void init_weight(){
        for(int i = 0 ; i < size ; i++){
            weight[i][0] = i + 97;
            weight[i][1] = (int)(Math.random()*100)+1;
        }
    }

    void init_q(PriorityQueue<Node> q){
        for(int i = 0 ; i < size ; i++){
            q.add(new NodeBuilder().setChar((char)weight[i][0]).setFrequncy(weight[i][1]).build());
        }
    }

    public static void main(String args[]){

            huffman s = new huffman(5);
            PriorityQueue<Node> q = new PriorityQueue<Node>();
            s.init_weight();
            s.init_q(q);

            Node n1,n2;
            Node root = new Node();
            while(q.size()>1){
            // 허프만 트리를 통한 코드 분배
                n1 = q.poll();
                n2 = q.poll();
                root = new NodeBuilder().setFrequncy(n1.frequency+n2.frequency).setLeft(n2).setRight(n1).build();
                q.add(root);                
            }

            s.DPS(root,"");
            System.out.println("문자 빈도수 코드");
            for(int i = 0 ; i < s.size ; i++){
                System.out.println((char)s.weight[i][0]+" , "+s.weight[i][1]+" , "+s.weight_code[i]);
            }

            
    }

    void DPS(Node root,String code){
    // 깊이 탐색하는 놈
    // 비재귀로 구현
    String tmp;
    Node start = root;
        if(start.left!=null){
            tmp = code + "0";            
            DPS(start.left,tmp);
        }
        if(start.right!=null){
            tmp = code + "1";                        
            DPS(start.right,tmp);
        }
        if(start.right==null&&start.left==null){
            int target = root.data - 97;
            weight_code[target] = code;
        }

    }

}

class Node implements Comparable{
    char data;
    int frequency;
    Node right;
    Node left;
    Node(){}
    Node(char data,int frequency,Node right,Node left){
        this.data = data;
        this.frequency = frequency;
        this.right = right;
        this.left = left;
    }

    public boolean qeuals(Object o){
        if(!(o instanceof Node)) return false;
        Node target = (Node)o;
        return this.data == target.data;
    }

    public int compareTo(Object o){
        Node target = (Node)o;
        if(this.frequency > target.frequency){
            return -1;
        }else if(this.frequency==target.frequency){
            return 0;
        }else{
            return 1;
        }
    }

}

class NodeBuilder{
    char data;
    int frequency;
    Node right;
    Node left;

    NodeBuilder setChar(char data){
        this.data = data;
        return this;
    }    

    NodeBuilder setFrequncy(int frequency){
        this.frequency = frequency;
        return this;
    }
    
    NodeBuilder setRight(Node right){
        this.right = right;
        return this;
    }        
    
    NodeBuilder setLeft(Node left){
        this.left = left;
        return this;
    }    

    Node build(){
        return new Node(data,frequency,right,left);
    }

}
