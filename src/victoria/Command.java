package victoria;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Command extends UnicastRemoteObject implements CommandInterface{
// Fields
	private static final long serialVersionUID = 1L;
	private ArrayList<ProcInterface> procList;
	private int procCount;
	private int electionRound;
	private int leaderID;
// Constructors
	protected Command(int i_count) throws RemoteException {
		super();
		procCount = i_count;
		procList = new ArrayList<ProcInterface>();
		procList.clear();
		electionRound = 0;
	}
// Main
	public static void main(String[] args) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
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
			procList.get(iter).remoteRecvProcCount(procList.size());
			procList.get(iter).remoteRecvNextProcHandle(procList.get(next));
		}
	}
	public void beginElection() throws RemoteException, MalformedURLException, NotBoundException {
		while(true) {
			++electionRound;
			System.err.println("Election round: " + electionRound);
			
			// Find first active process
			ProcInterface firstActiveProcess = procList.get(0).remoteFindFirstActiveProcess();
			firstActiveProcess.remoteSendTID(0);
			firstActiveProcess.remoteSendMAX(0);
			firstActiveProcess.remoteCheckIDs(0);
			int survivorCount = firstActiveProcess.remoteCheckSurvivor(0, electionRound);
			if (survivorCount == 1) {
				leaderID = findLeader();
				System.err.println("Leader: " + leaderID);
				break;
			}
		}
	}
	public int findLeader() throws RemoteException {
		return procList.get(0).remoteFindLeader(0);
	}
// Getters & Setters
	public int getLeaderID() {return leaderID;}
// Remote Methods
	@Override
	public void remoteRegisterProc(ProcInterface i_handle) throws RemoteException {
		procList.add(i_handle);
	}
}
