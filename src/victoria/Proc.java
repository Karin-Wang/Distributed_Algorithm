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
	private int procCount;
	private String hostURL;
	CommandInterface commandHandle;
	ProcInterface nextProcHandle;
	private int tid, ntid, nntid;
	private boolean isLeader;
	private boolean isActive;
// Constructors
	protected Proc(int i_id, String i_hostURL) throws RemoteException {
		super();
		id = i_id;
		hostURL = i_hostURL;
		tid = id;
		isActive = true;
		isLeader = false;
	}
// Main
	public static void main(String[] args) throws NumberFormatException, RemoteException, MalformedURLException, NotBoundException {
		// Take two args: id and host url
		if (args.length != 2) { System.err.println("No."); System.exit(1); }
		if (Integer.parseInt(args[0]) <= 0 ) { System.err.println("No."); System.exit(1); }
		
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
	public void remoteSendTID(int i) throws RemoteException {
		if (i == procCount) return;
		else {
			if (isActive)
				nextProcHandle.remoteRecvNTID(tid);
			else
				nextProcHandle.remoteRecvNTID(ntid);
		}
		nextProcHandle.remoteSendTID(i+1);
	}
	@Override
	public void remoteRecvNTID(int i_id) throws RemoteException {
		ntid = i_id;
	}
	@Override
	public void remoteSendMAX(int i) throws RemoteException {
		if (i == procCount) return;
		else {
			if (isActive)
				nextProcHandle.remoteRecvNNTID(Math.max(tid, ntid));
			else
				nextProcHandle.remoteRecvNNTID(nntid);
		}
		nextProcHandle.remoteSendMAX(i+1);
	}
	@Override
	public void remoteRecvNNTID(int i_id) throws RemoteException {
		nntid = i_id;
	}
	@Override
	public boolean remoteIsLeader() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int remoteFindLeader(int i) throws RemoteException {
		if (isActive) return tid;
		else if (i == procCount) return -1;
		else return nextProcHandle.remoteFindLeader(i+1);
	}
	@Override
	public void remoteRecvProcCount(int i_count) throws RemoteException {
		procCount = i_count;
	}
	@Override
	public ProcInterface remoteFindFirstActiveProcess() throws RemoteException, MalformedURLException, NotBoundException {
		if (isActive) return (ProcInterface) Naming.lookup(id + "");
		else return nextProcHandle.remoteFindFirstActiveProcess();
	}
	@Override
	public int remoteCheckSurvivor(int i, int i_round) throws RemoteException {
		if (i == procCount) return 0;
		else if (isActive) {
			System.err.println("Round: " + i_round + ", " + tid + " is still active.");
			return nextProcHandle.remoteCheckSurvivor(i+1, i_round) + 1;
		}
		else return nextProcHandle.remoteCheckSurvivor(i+1, i_round);	
	}
	@Override
	public void remoteCheckIDs(int i) throws RemoteException {
		if (i == procCount) return;
		if (isActive) {
			System.err.println("tid: " + tid + "   ntid: " + ntid + "   nntid: " + nntid );
			//if ((ntid == tid) && (ntid == nntid) && (nntid == tid)) isLeader = true;
			if ((ntid >= tid) && (ntid >= nntid)) tid = ntid;
			else isActive = false;
		}
		nextProcHandle.remoteCheckIDs(i+1);
	}
}
