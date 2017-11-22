package napoleon;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ProcImpl extends UnicastRemoteObject implements ProcInterface {

	private String processName_;
	
	public String getProcessName() { return processName_; }
	
	private static final long serialVersionUID = 1L;

	protected ProcImpl(String in_str) throws RemoteException {
		super();
		processName_ = in_str;
	}

	@Override
	public String exchangePleasantries(String in_msg) throws RemoteException {
		System.err.println("Message received: " + in_msg);
		return processName_ + "acknowledged your message.";
	}

	

}
