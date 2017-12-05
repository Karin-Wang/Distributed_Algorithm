package byzantium;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CaptainInterface extends Remote {
	public String getName() throws RemoteException;
	public void setCapList(ArrayList<CaptainInterface> in_list) throws RemoteException;
	public void recvMessage(String in_sender, String in_msg) throws RemoteException, InterruptedException;
	public void broadcastToCols(String in_msg, int in_sendChance, int in_msgCorrectChance) throws RemoteException;
	public boolean isDisloyal() throws RemoteException;
	public void notifyDisloyalCount(int in_count, int in_disCount) throws RemoteException;
}
