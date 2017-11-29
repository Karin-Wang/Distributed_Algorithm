package victoria;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Command extends UnicastRemoteObject implements CommandInterface{
// Fields
	private static final long serialVersionUID = 1L;
	private ArrayList<ProcInterface> procList;
	private int procCount;
// Constructors
	protected Command(int i_count) throws RemoteException {
		super();
		procCount = i_count;
		procList = new ArrayList<ProcInterface>();
		procList.clear();
	}
// Main
	public static void main(String[] args) throws RemoteException, InterruptedException {
		// Take one args: number of actual processes
		if (args.length != 1) { System.err.println("No."); System.exit(1); }
		if (Integer.parseInt(args[0]) < 3) { System.err.println("No."); System.exit(1); }
		
		// Register Command
		Command localCommand = new Command(Integer.parseInt(args[0]));
		try {
			Naming.rebind("Command", localCommand);
		} catch (Exception e) {	System.err.println("Initial binding failure: " + e); System.exit(1); }
		
		while (!localCommand.isAllProcReady()) {Thread.sleep(500);}
		
		System.err.println("All procs are here.");
		
		localCommand.bridgeRing();
		localCommand.beginElection();
	}
// Methods
	public boolean isAllProcReady() { if (procList.size() != procCount) return false; else return true;}
	public void bridgeRing() throws RemoteException {
		for (int iter = 0; iter != procList.size(); ++iter) {
			int next = iter + 1;
			if (next == procList.size()) next = 0;
			procList.get(iter).remoteRecvNextProcHandle(procList.get(next));
		}
	}
	public void beginElection() {
		procList.get(0)
	}
// Getters & Setters
// Remote Methods
	@Override
	public void remoteRegisterProc(ProcInterface i_handle) throws RemoteException {
		procList.add(i_handle);
	}
}
