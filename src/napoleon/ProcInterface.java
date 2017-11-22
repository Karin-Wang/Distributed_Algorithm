package napoleon;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcInterface extends Remote {
	public String exchangePleasantries(String in_msg) throws RemoteException;
}
