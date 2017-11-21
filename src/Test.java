import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test{
	
	static final int port = 1099;
	static final int processNumber = 4;
	
	public static void main(String args[]) throws RemoteException {
		if(args.length>0){     
			
			int processId = Integer.parseInt(args[0]);
			Process node = new Process(port, processId, processNumber, 4);
			node.notifyP();
		}
		else{
			LocateRegistry.createRegistry(port);
			for(int i=0;i<processNumber;i++){
				int processId = i;
				Process process = new Process(port, processId, processNumber, 10);
				process.notifyP();
			}
		}
    }
}
