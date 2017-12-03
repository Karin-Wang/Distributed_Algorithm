package byzantium;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GeneralImpl extends UnicastRemoteObject implements GeneralInterface {
// Fields
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG_FLAG = true;
	private String name_ = "General";
	private int capCount_ = 0;
	private boolean isLoyal_ = false;
	// This list contains all caps
	ArrayList<CaptainInterface> capList_ = new ArrayList<CaptainInterface>();
// Constructors
	protected GeneralImpl(int in_capCount, boolean in_isLoyal) throws RemoteException, InterruptedException {
		super();
		capCount_ = in_capCount;
		isLoyal_ = in_isLoyal;
		try {
			Naming.rebind(name_, this);
		} catch (Exception e) {	DebugTool.printAndExit(name_ + " rebind failure.");}
		
		if (DEBUG_FLAG && true) DebugTool.print(name_ + " rebind success.");
		
		// Wait for caps
		while (capList_.size() != capCount_) {Thread.sleep(1000);}
		if (DEBUG_FLAG && true) DebugTool.print("All captains reported in.");
		
		// Tell each cap how to contact other caps
		for (int i = 0; i < capList_.size(); ++i) {
			ArrayList<CaptainInterface> tempList = new ArrayList<CaptainInterface>();
			for (int j = 0; j < capList_.size(); ++j) {
				if (i==j) continue;
				tempList.add(capList_.get(j));
			}
			capList_.get(i).setCapList(tempList);
		}
		
		// Operation
		operation();
	}
// Methods
	private void operation() {
		if (DEBUG_FLAG && true) DebugTool.print(name_ + " has finished prep. Entering operation stage.");
	}
	private void broadcastToCaps() {
		
	}
// Getters & Setters
// Remote Methods
	@Override
	public void registerCaptain(CaptainInterface in_capHandle) throws RemoteException {
		capList_.add(in_capHandle);
		if (DEBUG_FLAG && true) DebugTool.print(in_capHandle.getName() + " reporting in.");
	}
	@Override
	public String getName() throws RemoteException {return name_;}
}
