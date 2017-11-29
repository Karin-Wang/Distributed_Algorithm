package victoria;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcInterface extends Remote {
	public void remoteRecvNextProcHandle(ProcInterface i_handle) throws RemoteException;
	public int remoteGetID() throws RemoteException;
	public int remoteInitElection() throws RemoteException;
}
