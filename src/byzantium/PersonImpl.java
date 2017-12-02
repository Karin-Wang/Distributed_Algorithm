package byzantium;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PersonImpl extends UnicastRemoteObject implements PersonInterface {
// Fields
	private static final long serialVersionUID = 1L;
	private int captainCount_;
	private ArrayList<RepositoryItem> localRepo_ = new ArrayList<RepositoryItem>();
// Constructors
	protected PersonImpl() throws RemoteException {
		super();
	}
	protected PersonImpl(int i_captainCount) throws RemoteException {
		super();
	}
// Main
// Methods
// Getters & Setters
// Remote Methods
}
