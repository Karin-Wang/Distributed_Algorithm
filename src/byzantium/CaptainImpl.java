package byzantium;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class CaptainImpl extends UnicastRemoteObject implements CaptainInterface{
// Fields
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG_FLAG = false;
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
	private static int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt(max-min) + min;
	}
	private static boolean randomCheck(int in_th) {
		if (randomNumber(0, 100) < in_th) return true;
		else return false;
	}
	private void operation() throws RemoteException {
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
	@Override
	public void recvMessage(String in_sender, String in_msg) throws RemoteException, InterruptedException {
		//Thread.sleep(5000);
		//DebugTool.print(in_sender + " says: " + in_msg);
		genHandle_.writeLog("[" + in_sender + "]" + "-->" + "[" + name_ + "]" + " Msg Recv" + " - \"" + in_msg + "\"");
	}
	@Override
	public void broadcastToCols(String in_msg, int in_sendChance, int in_msgCorrectChance) throws RemoteException {
		// in_sendChance and in_msgCorrectChance should be determined in operation(). 
		// They range from 0-1. Values lower than 0 will be treated as 0, and value above 100 will be treated as 100.
		// if the process is loyal, in_msgCorrectChance should be 1, and in_msgCorrectChance should be 1.
		if (in_sendChance > 100) in_sendChance = 100;
		if (in_msgCorrectChance > 100) in_msgCorrectChance = 100;
		if (in_sendChance < 0) in_sendChance = 0;
		if (in_msgCorrectChance < 0) in_msgCorrectChance = 0;
		
		if (DEBUG_FLAG && true) DebugTool.print("Broadcasting... Message: " + in_msg);
		genHandle_.writeLog("[" + name_ + "]" + " " + "Init Broadcast.");
		
		ArrayList<ThreadBroadcast> threadList = new ArrayList<ThreadBroadcast>();
		for (int iter = 0; iter < colList_.size(); ++iter) {
			String msg = in_msg;
			
			// If the check is not passed, dont do anything for this captain.
			if (!randomCheck(in_sendChance)) {
				if (DEBUG_FLAG && true) DebugTool.print("Skip sending message to " + colList_.get(iter).getName());
				genHandle_.writeLog("[" + name_ + "]" + "-->" + "[" + colList_.get(iter).getName() + "]" + " Cancel Msg");
				continue;
			}
			
			// If the check is not passed, change message to false message.
			if (!randomCheck(in_msgCorrectChance)) {
				if (DEBUG_FLAG && true) DebugTool.print("Falsifying message to " + colList_.get(iter).getName());
				msg = "The general says we should retreat.";
				genHandle_.writeLog("[" + name_ + "]" + "-->" + "[" + colList_.get(iter).getName() + "]" + " Falsify Msg");
			}
			else {
				if (DEBUG_FLAG && true) DebugTool.print("Sending true message to " + colList_.get(iter).getName());
				genHandle_.writeLog("[" + name_ + "]" + "-->" + "[" + colList_.get(iter).getName() + "]" + " Send True Msg");
			}
			
			threadList.add(new ThreadBroadcast(name_, colList_.get(iter), msg, randomNumber(3000, 6000), genHandle_, name_, colList_.get(iter).getName()));
		}
		
		for (ThreadBroadcast threadIter : threadList) {
			threadIter.start();
		}
		
		if (DEBUG_FLAG && true) DebugTool.print("");
	}
}
