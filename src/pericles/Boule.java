package pericles;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Boule {
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
	// Constants
		// List of names.
		// This should not be changed during program.
		// I would add 'const' if Java allows me. Sigh....
		ArrayList<String> citizenListPool = new ArrayList<String>(Arrays.asList(
				"Alexander",
				"Andreas",
				"Damon",
				//"Theo",
				"Xander",
				"Kostas",
				"Thanos",
				"Dimitris",
				"Christos",
				"Manos",
				"Peter",
				"Aris",
				"Panagiotis",
				"Giorgos",
				"Apostolis",
				"Manolis"));
		
		// Self-explanatory. I added this line so the style is consistent.
		if (args.length != 1) {
			System.err.println("java pericles.Boule [number-of-process]");
			System.exit(1);
		}
		
		// Get the process count user really wants
		int citizenCount = Integer.parseInt(args[0]);
		if ((citizenCount > citizenListPool.size()) || (citizenCount < 3)) {
			System.err.println("Seriously? Come on, choose another number.");
			System.exit(1);
		}
		
		// Prep the names and IDs for the process
		ArrayList<String> citizenList = new ArrayList<String>();
		citizenList.clear();
		ArrayList<Integer> citizenPopularity = new ArrayList<Integer>();
		citizenPopularity.clear();
		
		for (int iter = 0; iter < citizenCount; ++iter) {
			Random r = new Random();
			citizenList.add(citizenListPool.get(iter));
			// Generate a random number. Check for duplicated number.
			// Re-gen until an unique id spawns.
			Boolean duplicatedID = true;
			while (duplicatedID) {
				duplicatedID = false;
				int id = r.nextInt(3 * citizenCount - 1) + 1;
				for (int jter = 0; jter < citizenPopularity.size(); ++jter) {
					if (citizenPopularity.get(jter) == id) {
						// Duplicated ID found. Try again.
						duplicatedID = true;
						break;
					}
				}
				// Unique ID. Pushback to list.
				if (!duplicatedID)
					citizenPopularity.add(id);
			}
		}
		
		// Print the list. Debug purpose.
		Boolean debugFlagPrintList = true;
		if (debugFlagPrintList)
			for (int iter = 0; iter < citizenCount; ++iter)
				System.err.println("Citizen." + (iter + 1) + ": " + citizenList.get(iter) + "\tPopularity: " + citizenPopularity.get(iter));
		System.err.println("");

		// Init process
		Citizen.citizenList = citizenList;
		Citizen.citizenPopularity = citizenPopularity;
		ArrayList<Citizen> citizenPointer = new ArrayList<Citizen>();
		citizenPointer.clear();
		for (int iter = 0; iter < citizenCount; ++iter) {
			citizenPointer.add(new Citizen(iter));
		}
		System.err.println("");
		
		// Show neibours
		Boolean debugShowNeibours = true;
		if (debugShowNeibours)
			for (int iter = 0; iter < citizenCount; ++iter)
				citizenPointer.get(iter).printNeighbours();
		System.err.println("");
		
		// Collect process remote handles.
		// Not sure if I need this here in Boule.
		// But I did it nevertheless
		ArrayList<CitizenInterface> citizenRemoteHandle = new ArrayList<CitizenInterface>();
		citizenRemoteHandle.clear();
		for (int iter = 0; iter < citizenCount; ++iter) {
			citizenRemoteHandle.add(citizenPointer.get(iter).registerHimself());	
		}
		System.err.println("");
		
		// Tell process to find their neibours remotely
		for (int iter = 0; iter < citizenCount; ++iter) {
			citizenPointer.get(iter).findNeighbourRemoteHandle();
			System.err.println("");
		}
		System.err.println("");
		
		// Election time.
		// MAKE ATHENS GREAT AGAIN
		
		// While the leader is not deicied, keep doing the loop
			// Round N
			
		
		


	}
	

}
