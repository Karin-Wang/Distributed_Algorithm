package byzantium;

import java.rmi.RemoteException;

public class ThreadBroadcast extends Thread { 
	private CaptainInterface handle_ = null;
	int msg_;
	int delay_;
	boolean send_;
	String sender_;
	GeneralInterface genHandle_ = null;
	String senderName_ = null;
	String recvName_ = null;
	int round_;
	
	ThreadBroadcast(String in_sender, CaptainInterface in_handle, int in_msg, int in_delay, GeneralInterface in_gen, String in_senderName, String in_recvName, int i_round) {
		handle_ = in_handle;
		msg_ = in_msg;
		delay_ = in_delay;
		send_ = true;
		sender_ = in_sender;
		genHandle_ = in_gen;
		senderName_ = in_senderName;
		recvName_ = in_recvName;
		round_ = i_round;
	}
	
	public void run() {
		try {
			//genHandle_.writeLog("[" + senderName_ + "]" + "-->" + "[" + recvName_ + "]" + " Msg Sent" + " - \"" + msg_ + "\"");
			//DebugTool.print("delay from "+ senderName_ + " to " + recvName_ + " is " +delay_);
			Thread.sleep(delay_);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//DebugTool.print("delay from "+ senderName_ + " to " + recvName_ + " is over");
			handle_.recvMessage(sender_, msg_, round_);
		} catch (RemoteException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
