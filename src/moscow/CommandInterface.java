package moscow;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommandInterface extends Remote {
	public void remoteRegisterProc(ProcInterface i_handle) throws RemoteException;
}
