package byzantium;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GeneralImpl extends UnicastRemoteObject implements GeneralInterface {
// Fields
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG_FLAG = false;
	private String name_ = "General";
	private int capCount_ = 0;
	private boolean isLoyal_ = false;
	private GeneralInterface handleSelf_ = null;
	// This list contains all caps
	ArrayList<CaptainInterface> capList_ = new ArrayList<CaptainInterface>();
	ArrayList<String> logBuffer_ = new ArrayList<String>();
// Constructors
	protected GeneralImpl(int in_capCount, boolean in_isLoyal) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
		super();
		capCount_ = in_capCount;
		isLoyal_ = in_isLoyal;
		try {
			Naming.rebind(name_, this);
		} catch (Exception e) {	DebugTool.printAndExit(name_ + " rebind failure.");}
		
		handleSelf_ = (GeneralInterface) Naming.lookup(name_);
		
		if (DEBUG_FLAG && true) DebugTool.print(name_ + " rebind success.");
		
		// Wait for caps
		while (capList_.size() != capCount_) {Thread.sleep(1000);}
		if (DEBUG_FLAG && true) DebugTool.print("All captains reported in.");
		writeLog("[" + name_ + "]" + " " + "All Captains Rdy");
		
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
	private static int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt(max-min) + min;
	}
	private static boolean randomCheck(int in_th) {
		if (randomNumber(0, 100) < in_th) return true;
		else return false;
	}
	private void operation() throws InterruptedException, RemoteException {
		if (DEBUG_FLAG && true) DebugTool.print(name_ + " has finished prep. Entering operation stage.");
		Thread.sleep(3000);
		
	/* This section is for tesing only. */
		// TODO: write functions to determine the chance to send, and chance to send correct msg.
		// 67 and 50 should give it around 50% to send true message.
		broadcastToCaps("The order is to attack.", 80, 50);
		//if (DEBUG_FLAG && true) DebugTool.print("Broadcasting completed...\n");
		
		// Test broadcasting for captain_2
		//for (int iter = 0; iter < capList_.size(); ++iter) {
			Thread.sleep(10000);
			capList_.get(2).broadcastToCols("The general says we should attack.", 80, 50);
		//}
	/* The section above is for tesing only. */
		
	}
	private void broadcastToCaps(String in_msg, int in_sendChance, int in_msgCorrectChance) throws RemoteException, InterruptedException {
		// in_sendChance and in_msgCorrectChance should be determined in operation(). 
		// They range from 0-1. Values lower than 0 will be treated as 0, and value above 100 will be treated as 100.
		// if the process is loyal, in_msgCorrectChance should be 1, and in_msgCorrectChance should be 1.
		if (in_sendChance > 100) in_sendChance = 100;
		if (in_msgCorrectChance > 100) in_msgCorrectChance = 100;
		if (in_sendChance < 0) in_sendChance = 0;
		if (in_msgCorrectChance < 0) in_msgCorrectChance = 0;
		
		if (DEBUG_FLAG && true) DebugTool.print("Broadcasting... Message: " + in_msg);
		writeLog("[" + name_ + "]" + " " + "Init Broadcast.");
		
		ArrayList<ThreadBroadcast> threadList = new ArrayList<ThreadBroadcast>();
		for (int iter = 0; iter < capList_.size(); ++iter) {
			String msg = in_msg;
			
			// If the check is not passed, dont do anything for this captain.
			if (!randomCheck(in_sendChance)) {
				if (DEBUG_FLAG && true) DebugTool.print("Skip sending message to " + "Captain_" + iter);
				writeLog("[" + name_ + "]" + "-->" + "[" + capList_.get(iter).getName() + "]" + " Cancel Msg");
				
				continue;
			}
			
			// If the check is not passed, change message to false message.
			if (!randomCheck(in_msgCorrectChance)) {
				if (DEBUG_FLAG && true) DebugTool.print("Falsifying message to " + "Captain_" + iter);
				msg = "The order is to retreat.";
				writeLog("[" + name_ + "]" + "-->" + "[" + capList_.get(iter).getName() + "]" + " Falsify Msg");
			}
			else {
				if (DEBUG_FLAG && true) DebugTool.print("Sending true message to " + "Captain_" + iter);
				writeLog("[" + name_ + "]" + "-->" + "[" + capList_.get(iter).getName() + "]" + " Send True Msg");
			}
			
			threadList.add(new ThreadBroadcast(name_, capList_.get(iter), msg, randomNumber(3000, 6000), handleSelf_, name_, capList_.get(iter).getName()));
		}
		
		for (ThreadBroadcast threadIter : threadList) {
			threadIter.start();
		}
		
		if (DEBUG_FLAG && true) DebugTool.print("");
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
	@Override
	public void writeLog(String in_log) throws RemoteException {
		Date date=new Date(); 
		DebugTool.print(new SimpleDateFormat("HH:mm:ss'.'SSS").format(date) + " " + in_log);
	}
	@Override
	public void bufferLog(String in_log) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
