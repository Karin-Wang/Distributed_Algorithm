package pericles;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import napoleon.ProcInterface;

public class Citizen extends UnicastRemoteObject implements CitizenInterface {
// Static Fields
	// Inited by Boule
	// Yes. Im making them public.
	// Nail me on a cross for it.
	// They are not going to see this code anyway.
	public static ArrayList<String> citizenList;
	public static ArrayList<Integer> citizenPopularity;
	private static final long serialVersionUID = 1L;
// Non-static Fields
	private int id;
	private String name;
	private int popularity;
	private int id_prev;
	private int id_next;
	private CitizenInterface remote_prev;
	private CitizenInterface remote_next;
// Constructor
	protected Citizen(int i_id) throws RemoteException {
		super();
		// Init values
		id = i_id;
		name = citizenList.get(i_id);
		popularity = citizenPopularity.get(i_id);
		id_prev = id - 1;
		if (id_prev < 0)
			id_prev = citizenList.size() - 1;
		id_next = id + 1;
		if (id_next >= citizenList.size())
			id_next = 0;
		
		System.err.println("Citizen " + name + " enters the boule. The number of his seat is " + (id+1) + ". His popularity is " + popularity + ".");
	}
// Public Methods
	// RMI init. I separated this from constructors for easy maintance.
	public CitizenInterface registerHimself() throws MalformedURLException, RemoteException, NotBoundException {
		try {
			Naming.rebind(name, this);
		} catch (Exception e) {	System.err.println("Initial binding failure: " + e); System.exit(1); }
		return (CitizenInterface) Naming.lookup(name);
	}
	public void findNeighbourRemoteHandle() throws MalformedURLException, RemoteException, NotBoundException {
		remote_prev = (CitizenInterface) Naming.lookup(citizenList.get(id_prev));
		remote_next = (CitizenInterface) Naming.lookup(citizenList.get(id_next));
		if (true) {
			System.err.println("I'm " + name + ". And this is what I heard from RMIregistry:");
			System.err.println("I heard from my prev neibour, that he's " + remote_prev.getName() + " and has a popularity of " + remote_prev.getPopularity());
			System.err.println("I heard from my next neibour, that he's " + remote_next.getName() + " and has a popularity of " + remote_next.getPopularity());
		}
	}
	// Debug purpose
	public void printNeighbours() {
		System.err.println("On the left of " + name + " is " + citizenList.get(id_prev) + ", and on his right is " + citizenList.get(id_next));
	}
// Static Methods
	
// Remote Methods
	@Override
	public int getID() throws RemoteException {
		return id;
	}
	@Override
	public String getName() throws RemoteException {
		return name;
	}
	@Override
	public int getPopularity() throws RemoteException {
		return popularity;
	}
}
