package trajan;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class Client {
	
	static int number;
	static String serverIP;
	static String serverPort;
	static String serverName;
	
	public static void main(String[] args) throws RemoteException {
		// TO-DO: add args check
		number = Integer.parseInt(args[0]);
		serverIP = args[1];
		serverPort = args[2];
		serverName = args[3];
		
		System.out.println("Client starting...");
		ProcInterface remote = null;
		try {
			remote = (ProcInterface) Naming.lookup("rmi://" + serverIP + ':' + serverPort + '/'  + serverName);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		
		System.err.println(remote.processNumber(number));

	}
}
