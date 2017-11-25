package pericles;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CitizenInterface extends Remote {
	public int getID() throws RemoteException;
	public String getName() throws RemoteException;
	public int getPopularity() throws RemoteException;
	public int getPopularityElec() throws RemoteException;
	public Boolean isPassive() throws RemoteException;
	public void findNeighbourRemoteHandle() throws MalformedURLException, RemoteException, NotBoundException;
	public void printNeighbours() throws RemoteException;
	public void sendPopularityToNext() throws RemoteException;
	public void recvPopularityFromPrev(int i_pop) throws RemoteException;
	public void sendMaxToNext() throws RemoteException;
	public void recvMaxFromPrev(int i_max) throws RemoteException;
	public void comparePopularityValues() throws RemoteException;
}
