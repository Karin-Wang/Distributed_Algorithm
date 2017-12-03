package byzantium;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CaptainInterface extends Remote {
	public String getName() throws RemoteException;
	public void setCapList(ArrayList<CaptainInterface> in_list) throws RemoteException;
}
