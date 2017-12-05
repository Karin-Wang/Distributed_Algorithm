package byzantium;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GeneralInterface extends Remote {
	public void registerCaptain(CaptainInterface in_capHandle) throws RemoteException;
	public String getName() throws RemoteException;
	public void writeLog(String in_log) throws RemoteException;
	public void bufferLog(String in_log) throws RemoteException;
}
