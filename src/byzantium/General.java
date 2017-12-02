package byzantium;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class General {
// Fields

// Constructors
// Main
	public static void main(String[] args) throws RemoteException {
	// Check args
		if (args.length != 1)
			DebugTool.printAndExit("ERROR: Wrong argument count.");
		if ((Integer.parseInt(args[0]) < 6) || (Integer.parseInt(args[0]) > Repository.list.size()))
			DebugTool.printAndExit("ERROR: Wrong argument.");
	
	// Init
		GeneralImpl local = new GeneralImpl();
	
	// Get info about the process itself
		
		
	// Create local repo
	}
// Methods
// Getters & Setters
// Remote Methods
}
