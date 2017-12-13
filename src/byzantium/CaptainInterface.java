package byzantium;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//NOTE:
//MSG TYPES
//1 - Notification: false/retreat
//2 - Notification: true/attack
//-1 - Proposal: false/retreat
//-2 - Proposal: true/attack
//0 - Proposal: undecided

public interface CaptainInterface extends Remote {
	public String getName() throws RemoteException;
	public void setCapList(ArrayList<CaptainInterface> in_list) throws RemoteException;
	public void recvMessage(String in_sender, int in_msg, int i_round) throws RemoteException, InterruptedException;
	public void broadcastToCols(int in_msg, int in_sendChance, int in_msgCorrectChance) throws RemoteException;
	public boolean isDisloyal() throws RemoteException;
	public void notifyDisloyalCount(int in_count, int in_disCount) throws RemoteException;
	public void shutdown() throws RemoteException;	
	public void greenForNextRound() throws RemoteException;
	public ArrayList<Integer> getHistory(int i_round) throws RemoteException;
}
