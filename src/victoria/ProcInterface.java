package victoria;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcInterface extends Remote {
	public void remoteRecvNextProcHandle(ProcInterface i_handle) throws RemoteException;
	public void remoteRecvProcCount(int i_count) throws RemoteException;
	public int remoteGetID() throws RemoteException;
	public void remoteSendTID(int i) throws RemoteException;
	public void remoteRecvNTID(int i_id) throws RemoteException;
	public void remoteSendMAX(int i) throws RemoteException;
	public void remoteRecvNNTID(int i_id) throws RemoteException;
	public void remoteCheckIDs(int i) throws RemoteException;
	public boolean remoteIsLeader() throws RemoteException;
	public int remoteFindLeader(int i) throws RemoteException;
	public int remoteCheckSurvivor(int i, int i_round) throws RemoteException;
	public ProcInterface remoteFindFirstActiveProcess() throws RemoteException, MalformedURLException, NotBoundException;
}
