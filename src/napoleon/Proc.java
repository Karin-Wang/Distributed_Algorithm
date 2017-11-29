package napoleon;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import napoleon.ProcImpl;

public class Proc {
	
	// For now the list of all processes are stored as static fields here.
	// May want to make it changable in run-time in the future.
	private static int procCount_ = 3;
	public static int getProcCount() {return procCount_;}
	private static ArrayList<String> procNameList_ = new ArrayList<String>(Arrays.asList(
			"Roma",
			"Berlin",
			"Paris" ));
	private static ArrayList<String> procAddressList_ = new ArrayList<String>(Arrays.asList(
			"//127.0.0.1:1099/",
			"//127.0.0.1:1099/",
			"//127.0.0.1:1099/" ));
	private static ArrayList<Integer> procStatusList_ = new ArrayList<Integer>(Arrays.asList(0, 0, 0));
	private static int localID_;
	
	public static int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt(max-min) + min;
	}
	
	public static void main(String[] args) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
		// Create local object for this process.
		localID_ = Integer.parseInt(args[0]);
		ProcImpl localImpl_ = new ProcImpl(localID_ ,"" + procNameList_.get(localID_));
		
		// Register local process on local registry.
		// RMIregistry needs to be ready beforehand.
		try {
			Naming.rebind(procAddressList_.get(localID_) + procNameList_.get(localID_), localImpl_);
		} catch (Exception e) {	System.err.println("Initial binding failure: " + e); System.exit(1); }
		System.err.println("Initial binding success. " + localImpl_.getProcessName() + " ready.");
		procStatusList_.set(localID_, 1);
		
		// Look for fellow processes on remote registry.
		Boolean someProcessIsNotReady = true;
		while (someProcessIsNotReady) {
			// Do want the information to spam the cmd window
			Thread.sleep(1000);
			// Assume the loop can be escaped this time around
			someProcessIsNotReady = false;
			// Try to contact whoever has not been contacted yet
			for (int iter = 0; iter < procCount_; ++iter )
				if (procStatusList_.get(iter) == 0) {
					try {
						ProcInterface remoteProc_ = (ProcInterface) Naming.lookup(procAddressList_.get(iter) + procNameList_.get(iter));
						System.err.println(remoteProc_.exchangePleasantries("Greetings from " + localImpl_.getProcessName()));
						procStatusList_.set(iter, 1);
					} catch (Exception e) {	System.err.println("No response from " + procNameList_.get(iter)); }
				}
			
			// If any one of the process is not ready, keep repeating the loop.
			for (int iter = 0; iter < procCount_; ++iter )
				if (procStatusList_.get(iter) == 0)
					someProcessIsNotReady = true;
		}
		
		System.err.println("All process ready.");
		// Do some weird shit.
		
		// Push urls of other process to local process
		for (int iter = 0; iter < procCount_; ++iter ) {
			//if (iter == localID_) continue;
			localImpl_.pushProcessURLList(procAddressList_.get(iter) + procNameList_.get(iter));
		}
		localImpl_.printURLList();
		
		// This is not a good way to do it, but Im doing it anyway cuz Im lazy.
		if (localID_ == 0) {
			//Thread.sleep(randomNumber(5000, 7000));
			localImpl_.broadcast("My very first broadcast.");
		}
		else if (localID_ == 1) {
			
		}
		else if (localID_ == 2) {
			
		}
		else;
	}
	
}
