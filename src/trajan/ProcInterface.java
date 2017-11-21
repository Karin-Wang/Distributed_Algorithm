package trajan;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcInterface extends Remote {
	public int processNumber(int i_num) throws RemoteException;
}
