package napoleon;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;

import napoleon.ProcImpl;

public class Proc {
	
	// For now the list of all processes are stored as static fields here.
	// May want to make it changable in run-time in the future.
	private static int procCount_ = 3;
	public static int getProcCount() {return procCount_;}
	private static ArrayList<String> procNameList_ = new ArrayList(Arrays.asList(
			"Roma",
			"Berlin",
			"Paris" ));
	private static ArrayList<String> procAddressList_ = new ArrayList(Arrays.asList(
			"//127.0.0.1:117/",
			"//127.0.0.1:118/",
			"//127.0.0.1:119/" ));
	private static int localID_;
	

	public static void main(String[] args) throws RemoteException, InterruptedException {
		// Create local object for this process.
		localID_ = Integer.parseInt(args[0]);
		ProcImpl localImpl_ = new ProcImpl(procNameList_.get(localID_));
		
		// Register local process on local registry.
		// RMIregistry needs to be ready beforehand.
		try {
			Naming.rebind(procAddressList_.get(localID_) + procNameList_.get(localID_), localImpl_);
		} catch (Exception e) {	System.err.println("Initial binding failure: " + e); System.exit(1); }
		System.err.println("Initial binding success. " + localImpl_.getProcessName() + " ready.");
		
		// Look for fellow processes on remote registry.
		int someProcessIsNotReady = 1;
		while (someProcessIsNotReady == 1) {
			Thread.sleep(1000);
			someProcessIsNotReady = 0;
			for (int iter = 0; iter < procCount_; ++ iter) {
				try {
					if (iter == localID_) continue;
					ProcInterface remoteProc_ = (ProcInterface) Naming.lookup(procAddressList_.get(iter) + procNameList_.get(iter));
					remoteProc_.exchangePleasantries("Greetings from " + localImpl_.getProcessName());
				} catch (Exception e) {	System.err.println("Naming.rebind exception: " + e); someProcessIsNotReady = 1; }
			}
		}
		
		System.err.println("All process ready.");
		// Do some weird shit.
		
		
		
	}
	
}
