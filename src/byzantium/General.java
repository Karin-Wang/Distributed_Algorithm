package byzantium;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class General {
// Fields
	private static boolean DEBUG_FLAG = true;
// Constructors
// Main
	// java byzantium.General -(int)[number of captains] -(boolean)[is loyal]
	public static void main(String[] args) throws RemoteException, InterruptedException {
	// Check args
		if (args.length != 2) DebugTool.printAndExit("java byzantium.General -(int)[number of captains] -(boolean)[is loyal]");
		int captainCount = Integer.parseInt(args[0]);
		if (captainCount < 6) DebugTool.printAndExit("ERROR: At least 6 captains required.");
		boolean isLoyal = Boolean.parseBoolean(args[1]);
		
		if (DEBUG_FLAG && true) DebugTool.print("captainCount = " + captainCount + "\nisLoyal = " + isLoyal);
	// Init
		GeneralImpl local = new GeneralImpl(captainCount, isLoyal);
	}
// Methods
// Getters & Setters
// Remote Methods
}
