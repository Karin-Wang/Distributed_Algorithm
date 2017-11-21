import java.rmi.Remote;
import java.rmi.RemoteException;

public interface My_interface_RMI extends Remote{
	 public void receiveMessage(Message m, int sendId) throws RemoteException;
	 public Clock getClock() throws RemoteException;
	 public void setClock(Clock clockNew) throws RemoteException;
	 public void registerProcess(int length) throws RemoteException;
	 
}