package byzantium;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class CaptainImpl extends UnicastRemoteObject implements CaptainInterface{
// Fields
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG_FLAG = true;
	private String name_ = "Captain";
	private String genName_ = "General";
	private String genIP_ = "127.0.0.1";
	private GeneralInterface genHandle_ = null;
	private int capCount_ = 0;
	private boolean isLoyal_ = false;
	private boolean isPrepFinished = false;
	// This list does not contain the cap himself
	ArrayList<CaptainInterface> colList_ = new ArrayList<CaptainInterface>();
// Constructors
	protected CaptainImpl(String in_capName, String in_genIP, boolean in_isLoyal) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		super();
		name_ = in_capName;
		genIP_ = in_genIP;
		isLoyal_ = in_isLoyal;
		try {
			Naming.rebind(name_, this);
		} catch (Exception e) {	DebugTool.printAndExit(name_ + " rebind failure.");}
		
		if (DEBUG_FLAG && true) DebugTool.print(name_ + " rebind success.");
		
		// Find General
		try {
			genHandle_ = (GeneralInterface) Naming.lookup("//" + genIP_ + ":1099/" + genName_);
		}
		catch (Exception e) { DebugTool.printAndExit("Finding general failed.");}
		genHandle_.registerCaptain((CaptainInterface) Naming.lookup(name_));
		if (DEBUG_FLAG && true) DebugTool.print("Registered with the general.");
		
		// Until the cap gets list from the general
		while (!isPrepFinished) {Thread.sleep(1000);}
		operation();
	}
// Methods
	private void operation() {
		if (DEBUG_FLAG && true) DebugTool.print(name_ + " has finished prep. Entering operation stage.");
	}
// Getters & Setters
// Remote Methods
	@Override
	public String getName() throws RemoteException {return name_;}
	@Override
	public void setCapList(ArrayList<CaptainInterface> in_list) throws RemoteException {
		colList_ = in_list;
		String tempStr = "My fellow captains: ";
		if (DEBUG_FLAG && true) 
			for (CaptainInterface handle : colList_)
				tempStr += handle.getName() + " ";
		if (DEBUG_FLAG && true) 
			DebugTool.print(tempStr);
		isPrepFinished = true;
	}
}
