
class testMain{

    public static void main(String args[]){
            try{
                    new Thread(new prim(15,1200)).start();
            }catch(Exception e){
                    System.out.println(e.getMessage());
            }
    }

}