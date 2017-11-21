package trajan;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	
	static String serverName;
	static int serverPort;
	
	public static void main(String[] args) {
		serverPort = Integer.parseInt(args[0]);
		serverName = args[1];
		try {
			System.out.println("Server starting...");
			ProcImpl impl = new ProcImpl();
			Registry registry = LocateRegistry.getRegistry(serverPort);
			registry.rebind(serverName, impl);
			System.out.println("Server ready.");
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(1);
		}
	}
}
