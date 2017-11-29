package victoria;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import pericles.CitizenInterface;


public class Proc extends UnicastRemoteObject implements ProcInterface {
// Fields
	private static final long serialVersionUID = 1L;
	private int id;
	private String hostURL;
	CommandInterface commandHandle;
	ProcInterface nextProcHandle;
// Constructors
	protected Proc(int i_id, String i_hostURL) throws RemoteException {
		super();
		id = i_id;
		hostURL = i_hostURL;
	}
// Main
	public static void main(String[] args) throws NumberFormatException, RemoteException, MalformedURLException, NotBoundException {
		// Take two args: id and host url
		if (args.length != 2) { System.err.println("No."); System.exit(1); }
		
		// Register Proc
		Proc localProc = new Proc(Integer.parseInt(args[0]), args[1]);
		try {
			Naming.rebind("" + localProc.getID() , localProc);
		} catch (Exception e) {	System.err.println("Initial binding failure: " + e); System.exit(1); }
		
		localProc.findCommand();
		
	}
// Methods
	public void findCommand() throws MalformedURLException, RemoteException, NotBoundException {
		commandHandle = (CommandInterface) Naming.lookup(hostURL);
		commandHandle.remoteRegisterProc((ProcInterface) Naming.lookup(id + ""));
	}
// Getters & Setters
	public int getID() { return id; }
// Remote Methods
	@Override
	public void remoteRecvNextProcHandle(ProcInterface i_handle) throws RemoteException {
		nextProcHandle = i_handle;
		System.err.println("next: " + nextProcHandle.remoteGetID());
	}
	@Override
	public int remoteGetID() throws RemoteException { return id; }
	@Override
	public int remoteInitElection() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
}
