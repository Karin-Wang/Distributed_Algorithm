package byzantium;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

//NOTE:
//MSG TYPES
//1 - Notification: false/retreat
//2 - Notification: true/attack
//-1 - Proposal: false/retreat
//-2 - Proposal: true/attack
//0 - Proposal: undecided

public class CaptainImpl extends UnicastRemoteObject implements CaptainInterface{
// Fields
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG_FLAG = false;
	private String name_ = "Captain";
	private String genName_ = "General";
	private String genIP_ = "127.0.0.1";
	private GeneralInterface genHandle_ = null;
	private int capCount_ = 0;
	private int disloyalCapCount_ = 0;
	private boolean isLoyal_ = false;
	private boolean isPrepFinished = false;
	private int chance_send = 50;
	private int chance_notFasify = 50;
	// Consensus related
	private int round_ = 0;
	private boolean decided_ = false;
	private int localIdea_;
	private ArrayList<Integer> notificationMsg = new ArrayList<Integer>();
	private ArrayList<Integer> proposalMsg = new ArrayList<Integer>();
	private ArrayList<Integer> notificationBuffer = new ArrayList<Integer>();
	private ArrayList<Integer> notificationBufferRound = new ArrayList<Integer>();
	private ArrayList<Integer> proposalMsgBuffer = new ArrayList<Integer>();
	private ArrayList<Integer> proposalMsgBufferRound = new ArrayList<Integer>();
	private boolean nextRoundRdy = false;
	private ArrayList<Integer> pastDecs_ = new ArrayList<Integer>();
	//private ArrayList<ArrayList<Integer> > notificationMsgList_ = new ArrayList<ArrayList<Integer> >();
	//private ArrayList<ArrayList<Integer> > proposalMsgList_ = new ArrayList<ArrayList<Integer> >();
	
	
	// This list does not contain the cap himself
	ArrayList<CaptainInterface> colList_ = new ArrayList<CaptainInterface>();
// Constructors
	protected CaptainImpl(String in_capName, String in_genIP, boolean in_isLoyal) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		super();
		name_ = in_capName;
		genIP_ = in_genIP;
		isLoyal_ = in_isLoyal;
		
		if (randomCheck(50))
			localIdea_ = 2;
		else localIdea_ = 1;
		
		try {
			Naming.rebind(name_, this);
		} catch (Exception e) {	DebugTool.printAndExit(name_ + " rebind failure.");}
		
		DebugTool.print(name_ + " rebind success.");
		
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
		if (randomNumber(1, 100) <= in_th) return true;
		else return false;
	}
	private void operation() throws RemoteException, InterruptedException {
		if (DEBUG_FLAG && true) DebugTool.print(name_ + " has finished prep. Entering operation stage.");
		//notificationMsgList_.add(new ArrayList<Integer>());
		//proposalMsgList_.add(new ArrayList<Integer>());
		pastDecs_.add(localIdea_);
		while (true) {
			//nextRoundRdy = false;
			//genHandle_.unreadyForNextRound();
			//notificationMsg.clear();
			//proposalMsg.clear();
			
			
			
			ReentrantLock lock = new ReentrantLock ();
			lock.lock();
			DebugTool.print("locking");
			try {
				++round_;
				notificationMsg.clear();
				proposalMsg.clear();
				for (int iter = 0; iter < notificationBuffer.size(); ++iter)
					if (notificationBufferRound.get(iter) == round_)
						notificationMsg.add(notificationBuffer.get(iter));
				for (int iter = 0; iter < proposalMsgBuffer.size(); ++iter)
					if (proposalMsgBufferRound.get(iter) == round_)
						proposalMsg.add(proposalMsgBuffer.get(iter));
				
				ArrayList<Integer> temp1 = new ArrayList<Integer>();
				ArrayList<Integer> temp1r = new ArrayList<Integer>();
				for (int iter = 0; iter < notificationBuffer.size(); ++iter)
					if (notificationBufferRound.get(iter) > round_) {
						temp1.add(notificationBuffer.get(iter));
						temp1r.add(notificationBufferRound.get(iter));
					}
				ArrayList<Integer> temp2 = new ArrayList<Integer>();
				ArrayList<Integer> temp2r = new ArrayList<Integer>();
				for (int iter = 0; iter < proposalMsgBuffer.size(); ++iter)
					if (proposalMsgBufferRound.get(iter) > round_) {
						temp2.add(proposalMsgBuffer.get(iter));
						temp2r.add(proposalMsgBufferRound.get(iter));
					}
				
				notificationBuffer.clear();
				notificationBufferRound.clear();
				proposalMsgBuffer.clear();
				proposalMsgBufferRound.clear();
				
				notificationBuffer = temp1;
				notificationBufferRound = temp1r;
				proposalMsgBuffer = temp2;
				proposalMsgBufferRound = temp2r;
				
				
				
			} finally {
				lock.unlock();
				DebugTool.print("unlocking");
			}
			

			
			if (decided_) {
				if (isLoyal_)
					broadcastToCols(localIdea_, 100, 100);
				else
					broadcastToCols(localIdea_, chance_send, chance_notFasify);
				
				if (isLoyal_)
					broadcastToCols(0-localIdea_, 100, 100);
				else
					broadcastToCols(0-localIdea_, chance_send, chance_notFasify);
				genHandle_.decisionMade(name_, localIdea_);
				//System.exit(0);
				break;
			}
			
			
			//genHandle_.writeLog("[" + name_ + "]" + " Begin round " + (round_));
			DebugTool.print("Round " + round_);
			if (isLoyal_)
				broadcastToCols(localIdea_, 100, 100);
			else
				broadcastToCols(localIdea_, chance_send, chance_notFasify);

			while (notificationMsg.size() < capCount_ - disloyalCapCount_) {
				Thread.sleep(1000);
				DebugTool.print("still waiting for N: " + ((capCount_ - disloyalCapCount_) - notificationMsg.size()));
				//genHandle_.writeLog("[" + name_ + "]" + " still waiting for N: " + ((capCount_ - disloyalCapCount_) - notificationMsg.size()));
			}
			
			//DebugTool.print("Enough N msg");
			//genHandle_.writeLog("[" + name_ + "]" + " recv N msg from " + (capCount_ - disloyalCapCount_) + " caps");
			
			int count_2 = 0;
			int count_1 = 0;
			for (int iter = 0; iter < capCount_ - disloyalCapCount_; ++iter) {
				if (notificationMsg.get(iter) == 2)
					++count_2;
				else if (notificationMsg.get(iter) == 1)
					++count_1;
			}
			
			DebugTool.print("N" + " " + count_2 + " for attack" + " " + count_1 + " for retreat");
			//genHandle_.writeLog("[" + name_ + "]" + " N" + " " + count_2 + " for attack" + " " + count_1 + " for retreat");
			if (count_2*2 > (capCount_ + disloyalCapCount_)) {
				DebugTool.print("P:  " + -2);
				if (isLoyal_)
					broadcastToCols(-2, 100, 100);
				else
					broadcastToCols(-2, chance_send, chance_notFasify);
			}
			else if (count_1*2 > (capCount_ + disloyalCapCount_)) {
				DebugTool.print("P:  " + 1);
				if (isLoyal_)
					broadcastToCols(-1, 100, 100);
				else
					broadcastToCols(-1, chance_send, chance_notFasify);
			}
			else {
				DebugTool.print("P:  " + 0);
				if (isLoyal_)
					broadcastToCols(0, 100, 100);
				else
					broadcastToCols(0, chance_send, chance_notFasify);
			}
				
			
			
			while (proposalMsg.size() < capCount_ - disloyalCapCount_) {
				Thread.sleep(1000);
				//genHandle_.writeLog("[" + name_ + "]" + " still waiting for N: " + ((capCount_ - disloyalCapCount_) - proposalMsg.size()));
				//DebugTool.print("still waiting for N: " + ((capCount_ - disloyalCapCount_) - proposalMsg.size()));
			}
			//DebugTool.print("Enough P msg");
			
			//genHandle_.writeLog("[" + name_ + "]" + " recv P msg from " + (capCount_ - disloyalCapCount_) + " caps");
			
			int count_n2 = 0;
			int count_n1 = 0;
			int count_0 = 0;
			for (int iter = 0; iter < capCount_ - disloyalCapCount_; ++iter) {
				if (proposalMsg.get(iter) == -2)
					++count_n2;
				else if (proposalMsg.get(iter)  == -1)
					++count_n1;
				else if (proposalMsg.get(iter)  == 0)
					++count_0;
			}
			
			DebugTool.print("P" + " " + count_n2 + " for attack" + " " + count_n1 + " for retreat" + " " + count_0 + " undecided");
			
			//genHandle_.writeLog("[" + name_ + "]" + " P" + " " + count_n2 + " for attack" + " " + count_n1 + " for retreat" + " " + count_0 + " undecided");
			
			if (count_n2 > disloyalCapCount_) {
				DebugTool.print("Many -2");
				localIdea_ = 2;
				if (count_n2 > 3 * disloyalCapCount_) {
					decided_ = true;
					DebugTool.print("Many Many -2");
				}
					
			}
			else if (count_n1 > disloyalCapCount_) {
				DebugTool.print("Many -1");
				localIdea_ = 1;
				if (count_n1 > 3 * disloyalCapCount_) {
					decided_ = true;
					DebugTool.print("Many Many -1");
				}
					
			}
			else {
				DebugTool.print("Random choose");
				if (randomCheck(50))
					localIdea_ = 2;
				else 
					localIdea_ = 1;
			}
			
			pastDecs_.add(localIdea_);
			{
			String strr = new String();
			strr += "round " + round_ + " \n";
			for (int jter: pastDecs_) {
				if (jter == 2)
					strr += "A" + ", ";
				else if (jter == 1)
					strr += "R" + ", ";
				
				
			}
			strr = strr.substring(0, strr.length() - 1);
			

			genHandle_.writeLog("[" + name_ + "]" + "  Temp Val Used:   " + strr);
			}
			/*
			if(decided_)
				genHandle_.decisionMade(localIdea_);
			
			if(genHandle_.isConsensusReached())
				break;
				*/
			
			
			//genHandle_.readyForNextRound();
			//while (!nextRoundRdy) {
			//	Thread.sleep(100);
			//	nextRoundRdy = genHandle_.isAllReady();
			//}
			///notificationMsgList_.add(new ArrayList<Integer>());
			//proposalMsgList_.add(new ArrayList<Integer>());
		}
		
		//DebugTool.print("Final: " + localIdea_);
		//genHandle_.writeLog("[" + name_ + "]" + " Final: " + localIdea_);
		//System.exit(0);
		
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
	public synchronized void recvMessage(String in_sender, int in_msg, int i_round) throws RemoteException, InterruptedException {
		//Thread.sleep(5000);
		//DebugTool.print(in_sender + " says: " + in_msg);
				
		if (i_round < round_) return;
		else if (i_round == round_) {
			if (in_msg > 0) {
				notificationMsg.add(in_msg);
			} else {
				proposalMsg.add(in_msg);
			}
		} else {
			if (in_msg > 0) {
				notificationBuffer.add(in_msg);
				notificationBufferRound.add(i_round);
			} else {
				proposalMsgBuffer.add(in_msg);
				proposalMsgBufferRound.add(i_round);
			}
		}

			
		DebugTool.print("from " + in_sender + ": " + in_msg);
			
		//genHandle_.writeLog("[" + in_sender + "]" + "-->" + "[" + name_ + "]" + " Msg Recv" + " - \"" + in_msg + "\"");
	}
	@Override
	public void broadcastToCols(int in_msg, int in_sendChance, int in_msgCorrectChance) throws RemoteException {
		// in_sendChance and in_msgCorrectChance should be determined in operation(). 
		// They range from 0-1. Values lower than 0 will be treated as 0, and value above 100 will be treated as 100.
		// if the process is loyal, in_msgCorrectChance should be 1, and in_msgCorrectChance should be 1.
		if (in_sendChance > 100) in_sendChance = 100;
		if (in_msgCorrectChance > 100) in_msgCorrectChance = 100;
		if (in_sendChance < 0) in_sendChance = 0;
		if (in_msgCorrectChance < 0) in_msgCorrectChance = 0;
		

		if (in_msg>0)
			DebugTool.print("[" + name_ + "]" + " " + "Init N Broadcast.");
		else
			DebugTool.print("[" + name_ + "]" + " " + "Init P Broadcast.");
		//if (in_msg>0)
		//	genHandle_.writeLog("[" + name_ + "]" + " " + "Init N Broadcast.");
		//else
		//	genHandle_.writeLog("[" + name_ + "]" + " " + "Init P Broadcast.");
		
		ArrayList<ThreadBroadcast> threadList = new ArrayList<ThreadBroadcast>();
		for (int iter = 0; iter < colList_.size(); ++iter) {
			int msg = in_msg;
			
			// If the check is not passed, dont do anything for this captain.
			if (!randomCheck(in_sendChance)) {
				//DebugTool.print("Skip sending message to " + colList_.get(iter).getName());
				//genHandle_.writeLog("[" + name_ + "]" + "-->" + "[" + colList_.get(iter).getName() + "]" + " Cancel Msg");
				continue;
			}
			
			// If the check is not passed, change message to false message.
			if (!randomCheck(in_msgCorrectChance)) {
				
				if (msg == 2)
					msg = 1;
				else if (msg == 1)
					msg = 2;
				else if (msg == 0) {
					if (randomCheck(50))
						msg = -1;
					else
						msg = -2;
				}
				else if (msg == -1) {
					if (randomCheck(50))
						msg = 0;
					else
						msg = -2;
				}
				else if (msg == -2) {
					if (randomCheck(50))
						msg = -1;
					else
						msg = 0;
				}
				DebugTool.print("Falsifying message to " + colList_.get(iter).getName() + "  " + msg);
				//genHandle_.writeLog("[" + name_ + "]" + "-->" + "[" + colList_.get(iter).getName() + "]" + " Falsify Msg");
			}
			else {
				//DebugTool.print("Sending true message to " + colList_.get(iter).getName());
				//genHandle_.writeLog("[" + name_ + "]" + "-->" + "[" + colList_.get(iter).getName() + "]" + " Send True Msg");
			}
			//if(isLoyal_)
				threadList.add(new ThreadBroadcast(name_, colList_.get(iter), msg, randomNumber(200, 500), genHandle_, name_, colList_.get(iter).getName(), round_));
			//else
			//	threadList.add(new ThreadBroadcast(name_, colList_.get(iter), msg, randomNumber(500, 600), genHandle_, name_, colList_.get(iter).getName(), round_));
			
		}
		
		for (ThreadBroadcast threadIter : threadList) {
			threadIter.start();
		}
		
		if (DEBUG_FLAG && true) DebugTool.print("");
	}
	@Override
	public boolean isDisloyal() throws RemoteException {
		return !isLoyal_;
	}
	@Override
	public void notifyDisloyalCount(int in_count, int in_disCount) throws RemoteException {
		capCount_ = in_count;
		disloyalCapCount_ = in_disCount;
	}
	@Override
	public synchronized void shutdown() throws RemoteException {
		System.exit(0);
	}
	@Override
	public void greenForNextRound() throws RemoteException {
		nextRoundRdy = true;
	}
	@Override
	public ArrayList<Integer> getHistory(int i_round) throws RemoteException {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < round_-1; ++i)
			temp.add(pastDecs_.get(i));
		return null;
	}
}
