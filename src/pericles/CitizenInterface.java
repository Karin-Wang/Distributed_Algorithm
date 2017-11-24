package pericles;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CitizenInterface extends Remote {
	public int getID() throws RemoteException;;
	public String getName() throws RemoteException;;
	public int getPopularity() throws RemoteException;;
}
