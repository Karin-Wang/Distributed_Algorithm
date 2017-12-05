package byzantium;

import java.rmi.RemoteException;

public class ThreadBroadcast extends Thread { 
	private CaptainInterface handle_ = null;
	String msg_;
	int delay_;
	boolean send_;
	String sender_;
	GeneralInterface genHandle_ = null;
	String senderName_ = null;
	String recvName_ = null;
	
	ThreadBroadcast(String in_sender, CaptainInterface in_handle, String in_msg, int in_delay, GeneralInterface in_gen, String in_senderName, String in_recvName) {
		handle_ = in_handle;
		msg_ = in_msg;
		delay_ = in_delay;
		send_ = true;
		sender_ = in_sender;
		genHandle_ = in_gen;
		senderName_ = in_senderName;
		recvName_ = in_recvName;
	}
	
	public void run() {
		try {
			genHandle_.writeLog("[" + senderName_ + "]" + "-->" + "[" + recvName_ + "]" + " Msg Sent" + " - \"" + msg_ + "\"");
			Thread.sleep(delay_);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
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
