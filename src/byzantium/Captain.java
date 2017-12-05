package byzantium;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Captain {
// Fields
	private static boolean DEBUG_FLAG = false;
// Constructors
// Main
	// java byzantium.Captain -(String)[ip of General] -(boolean)[is loyal] -(String)[Captain name]
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		// Check args
		if (args.length != 3) DebugTool.printAndExit("java byzantium.Captain -(String)[ip of General] -(boolean)[is loyal]");
		String captainName = args[2];
		String generalIP = args[0];
		boolean isLoyal = Boolean.parseBoolean(args[1]);
		
		if (DEBUG_FLAG && true) DebugTool.print("captainName = " + captainName + "\ngeneralIP = " + generalIP + "\nisLoyal = " + isLoyal);
		// Init
		CaptainImpl local = new CaptainImpl(captainName, generalIP, isLoyal);
	}
// Methods
// Getters & Setters
// Remote Methods
}
