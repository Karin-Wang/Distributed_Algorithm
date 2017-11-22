package napoleon;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class ProcImpl extends UnicastRemoteObject implements ProcInterface {

	private static ArrayList<String> procURLList_ = new ArrayList<String>();
	
	private String processName_;
	public String getProcessName() { return processName_; }
	private int processID_;
	public int getProcessID() { return processID_; }
	private ArrayList<ProcInterface> processHandles_ = new ArrayList<ProcInterface>();
	
	private static final long serialVersionUID = 1L;
	
	public static int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt(max-min) + min;
	}

	protected ProcImpl(int i_id, String in_str) throws RemoteException {
		super();
		processID_ = i_id;
		processName_ = in_str;
		procURLList_.clear();
		processHandles_.clear();
	}

	@Override
	public String exchangePleasantries(String in_msg) throws RemoteException {
		System.err.println("Message received: " + in_msg);
		return processName_ + " acknowledged your message.";
	}
	
	public void pushProcessURLList(String i_url) throws MalformedURLException, RemoteException, NotBoundException {
		procURLList_.add(i_url);
		processHandles_.add((ProcInterface) Naming.lookup(i_url));
	}

	public void printURLList() {
		System.err.println("URL List:");
		for (int iter = 0; iter != procURLList_.size(); ++iter) {
			System.err.println(procURLList_.get(iter));
		}
	}
	
	public void broadcast(String i_msg) throws RemoteException {
		for (int iter = 0; iter != procURLList_.size(); ++iter) {
			if (iter == processID_) {
				System.err.println("Broadcasting: " + i_msg);
			}
			else {
				processHandles_.get(iter).receiveMessage(i_msg);
			}
		}
	}

	// TBI comm with delay
	@Override
	public Boolean receiveMessage(String i_msg, int i_delay) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	// Delay-free comm
	@Override
	public Boolean receiveMessage(String i_msg) throws RemoteException {
		System.err.println(i_msg);
		return false;
	}
	

}
