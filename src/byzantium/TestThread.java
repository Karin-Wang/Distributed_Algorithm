package byzantium;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TestThread extends Thread{
	int i_; String ip_;
	TestThread(int i, String ip) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException
	{
		DebugTool.print("new cap " + i);
		i_ = i;
		ip_ = ip;
		
	}
	public void run()
	{
		try {
			new CaptainImpl("Captain_" + i_, ip_, true);
		} catch (RemoteException | MalformedURLException | NotBoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void hahaha() throws RemoteException, MalformedURLException, NotBoundException, InterruptedException
	{
		
	}
}
