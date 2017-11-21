package trajan;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ProcImpl extends UnicastRemoteObject implements ProcInterface {

	private static final long serialVersionUID = 1L;

	protected ProcImpl() throws RemoteException {
		super();
	}

	@Override
	public int processNumber(int i_num) throws RemoteException {
		return i_num * 2 + 3;
	}

}
