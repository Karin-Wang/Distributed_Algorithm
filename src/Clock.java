import java.io.Serializable;

public class Clock implements Serializable{

	private static final long serialVersionUID = 1L;
	private int[] vector;

	public Clock(int size){
		vector = new int[size];
	}
	
	public Clock(int[] vector){
		this.vector = vector;
	}
	
	public void increment(int i){
		this.vector[i]++;
	}
	
	public Clock getMax(Clock A){
		Clock B = this;
		int[] maxClock = new int[A.vector.length];
		for(int i = 0; i < A.vector.length;i++){
			maxClock[i] = Math.max(A.vector[i], B.vector[i]);
		}
		return new Clock(maxClock);
	}
	
	public boolean receiveCondition(Clock receiver, int p){
		Clock message = this;
//		System.out.println("p: "+p+ " msg clock : "+ message.toString()+ "receiver clock : "+receiver.toString());
//		System.out.println("first check: P: "+p+" "+receiver.vector[p]+" "+(message.vector[p] -1));
		if (receiver.vector[p] == (message.vector[p] -1)){
//			System.out.println("still alive");
			for (int k = 0; k<receiver.vector.length; k++){
				if (k != p){
					if (receiver.vector[k] < message.vector[k]){
					return false;
				}
				}
			}
			return true;
			
		}
		else{
			return false;
		}
	}
	
	
	public boolean Equal(Clock clock){
		for(int i = 0; i < vector.length;i++){
			if(vector[i] != clock.vector[i])
				return false;			
		}
		return true;
	}
	
	public String toString(){
		String s = "[";
		for(int i = 0; i < vector.length; i++){
			s = s + vector[i] + ",";
		}
		s = s.substring(0, s.length()-1);
		s = s + "]";
		return s;
	
	}
	

}
