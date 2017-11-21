import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class Process extends UnicastRemoteObject implements My_interface_RMI {

	private static final long serialVersionUID = 1L;
	private int processId;
	private int processNum;
	private Registry registry;
	public Clock clock;
	private int index;
	private int totalRounds;
	private int round;
	ArrayList<Message> buffer = new ArrayList<Message>();


	protected Process(int registryPort, int processId, int processNum, int roundNum) throws RemoteException {
		super();
		this.processId = processId;
		this.processNum = processNum;
		this.setRegistry(LocateRegistry.getRegistry(registryPort));
		clock = new Clock(this.processNum);
		try {
			this.getRegistry().bind(Integer.toString(this.getProcessId()),this);
		} catch (AlreadyBoundException e) {
			System.out.println("Already bound" + e);
		}
		index = this.getRegistry().list().length-1;
		totalRounds = roundNum;
		round = 0;
	}
	
	public void notifyP(){
		String[] processes;
		try {
			processes = this.getRegistry().list();
			for(String processId: processes){
				My_interface_RMI remoteProcess = getRemoteProcess(processId);
				remoteProcess.registerProcess(processes.length);
			}
			while(buffer.size()!=0){
				for(int i = 0;i < buffer.size(); i++){
					Message messageB = buffer.get(i);
					
					if(messageB.getClock().receiveCondition(this.getClock(), Integer.parseInt(messageB.getSender()))){	
						this.setClock(this.getClock().getMax(messageB.getClock()));
						System.out.println( "[End: retry from buffer]successful receive:" + messageB.strReceive() +", C"+this.getProcessId()+ " = " +this.getClock().toString());
						buffer.remove(messageB); 				
					}	
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in notifyOthers" + e);
		}
	
	}
	
	public void broadcast(){
		final Process currentProcess = this;
	
		new Thread(new Runnable(){
			public void run(){
				try{
					String[] remoteIds = currentProcess.registry.list();
					while(currentProcess.round < currentProcess.totalRounds){
						currentProcess.getClock().increment(index);
						Clock currentClock = currentProcess.getClock();
						currentProcess.round++;
						
						String currentId = Integer.toString(currentProcess.processId);
						String message = "#" + currentProcess.round + " msg ";
						Message m = new Message(currentId, message, currentClock);
						String bMsg = m.strBroadcast();
						System.out.println(bMsg);
						
						for(String remoteId: remoteIds){
							
							My_interface_RMI remoteProcess = currentProcess.getRemoteProcess(remoteId);
							if(!currentId.equals(remoteId)){
								Message mwr = new Message(currentId, remoteId, message, currentClock);
								System.out.println("P"+currentId +" --> P" + remoteId + " " + message + " , C"+getProcessId()+" = "+ currentClock.toString());
								Thread.sleep(randomNumber(5,1000));
								remoteProcess.receiveMessage(mwr, Integer.parseInt(currentId));
							}	
						}
						Thread.sleep(randomNumber(5,50));
					}
					
				}catch (Exception e) {
					System.out.println("BroadCast Exception: " + e);
				}
			}
			
		}).start();
		
	}


	public void receiveMessage(Message m, int sendId) throws RemoteException {		
		if (m.getClock().receiveCondition(this.getClock(), sendId)){
			this.setClock(this.getClock().getMax(m.getClock()));
			System.out.println("successful receive: " + m.strReceive() +", C"+this.getProcessId()+ " = " +this.getClock().toString());	
		}
		else{
			System.out.println("to buffer: " + m.strReceive() +", C"+this.getProcessId()+ " = " +this.getClock().toString() + " ; "+ "tm = "+m.getClock().toString());	
			synchronized(this) {
				Clock lastClock = this.getClock();
				boolean condition = true;
				boolean retry_m = false;
				while(condition){
					for(int i = 0;i < buffer.size(); i++){
						Message messageB = buffer.get(i);
						
						if(messageB.getClock().receiveCondition(this.getClock(), Integer.parseInt(messageB.getSender()))){	
							this.setClock(this.getClock().getMax(messageB.getClock()));
							System.out.println( "[retry from buffer]successful receive:" + messageB.strReceive() +", C"+this.getProcessId()+ " = " +this.getClock().toString());
							buffer.remove(messageB); 				
						}	
					}
					if(lastClock.Equal(this.getClock())){
						condition = false;
					}
					
					else {
						lastClock = this.getClock();
					}
					if((!retry_m) && m.getClock().receiveCondition(this.getClock(),Integer.parseInt(m.getSender()))){
						this.setClock(this.getClock().getMax(m.getClock()));
						System.out.println( "[retry]successful receive:" + m.strReceive() +", C"+this.getProcessId()+ " = " +this.getClock().toString());
						retry_m = true;
						condition = true;
					}
				
			}
				if(!retry_m)buffer.add(m);
				System.out.println("buffer:" + buffer.size());
			}
			}
		
	}
	
	public void registerProcess(int length) throws RemoteException {

		if(length == processNum){
			broadcast();
			
		}
	}
		
	private My_interface_RMI getRemoteProcess(String ProcessStringId) throws AccessException, RemoteException, NotBoundException {
		My_interface_RMI remoteProcess = (My_interface_RMI) this.getRegistry().lookup(ProcessStringId);
		return remoteProcess;
	}
	
	public Clock getClock() throws RemoteException{
		return clock;		
	}
	
	public void setClock(Clock clockNew) throws RemoteException{
		clock = clockNew;		
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public int getProcessId() {
		return this.processId;
	}


	public int randomNumber(int min, int max){
		Random r = new Random();
		return r.nextInt(max-min) + min;
	}
	

}


