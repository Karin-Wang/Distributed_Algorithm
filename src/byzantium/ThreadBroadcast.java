package byzantium;

import java.rmi.RemoteException;

public class ThreadBroadcast extends Thread { 
	private CaptainInterface handle_ = null;
	String msg_;
	int delay_;
	boolean send_;
	String sender_;
	
	ThreadBroadcast(String in_sender, CaptainInterface in_handle, String in_msg, int in_delay) {
		handle_ = in_handle;
		msg_ = in_msg;
		delay_ = in_delay;
		send_ = true;
		sender_ = in_sender;
	}
	
	public void run() {
		try {
			Thread.sleep(delay_);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			handle_.recvMessage(sender_, msg_);
		} catch (RemoteException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
