import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private String sender;
	private String receiver;
	private String message;
	private Clock clock;
	
	public Message(String sender, String receiver, String message, Clock vClock) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.clock = vClock;
	}
	
	public Message(String sender, String message, Clock vClock) {
		this.sender = sender;
		this.message = message;
		this.clock = vClock;
	}
	
	public String strReceive() {
		String s = "P"+receiver + " <-- P" + sender + ": " + message;
		return s;
	}
	
	public String strBroadcast() {
		String s= "Broadcast: P" + sender + " send " + message + ", C" +sender + " = " +  clock.toString();
		return s;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock vClock) {
		this.clock = vClock;
	}
}
