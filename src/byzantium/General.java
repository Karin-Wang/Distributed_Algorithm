package byzantium;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class General {
// Fields
	private static boolean DEBUG_FLAG = false;
// Constructors
// Main
	// java byzantium.General -(int)[number of captains] -(boolean)[is loyal]
	public static void main(String[] args) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
	// Check args
		if (args.length != 2) DebugTool.printAndExit("java byzantium.General -(int)[number of captains] -(boolean)[is loyal]");
		int captainCount = Integer.parseInt(args[0]);
		if (captainCount < 5) DebugTool.printAndExit("ERROR: At least 6 captains required.");
		boolean isLoyal = Boolean.parseBoolean(args[1]);
		
		if (DEBUG_FLAG && true) DebugTool.print("captainCount = " + captainCount + "\nisLoyal = " + isLoyal);
	// Init
		GeneralImpl local = new GeneralImpl(captainCount, isLoyal);
	}
// Methods
// Getters & Setters
// Remote Methods
}
