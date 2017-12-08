
package byzantium;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class TestClass {
// Fields
	private static boolean DEBUG_FLAG = false;
// Constructors
// Main
	// java byzantium.Captain -(String)[ip of General] -(boolean)[is loyal] -(String)[Captain name]
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
		// Check args
		if (args.length != 2) DebugTool.printAndExit("java byzantium.Captain -(String)[ip of General] -(int)count");
		int count = Integer.parseInt(args[1]);
		String generalIP = args[0];
		boolean isLoyal = Boolean.parseBoolean(args[1]);
		ArrayList<TestThread> list_ = new ArrayList<TestThread>();
		// Init
		for (int iter = 0; iter < count; ++iter) {
			DebugTool.print("new thread: " + iter);
			list_.add(new TestThread(iter, generalIP));
		}
		
		for (int iter = 0; iter < count; ++iter) {
			DebugTool.print("run thread: " + iter);
			list_.get(iter).run();
		}
	}
// Methods
// Getters & Setters
// Remote Methods
}
