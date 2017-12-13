package wsnh;

import java.io.IOException;

public class Wsnh {
	public static boolean intToBoolean(int i) { if (i == 1 ) return true; else return false; }
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		
		if (args.length < 5) return;
		
		boolean isGeneralLocal = intToBoolean(Integer.parseInt(args[0]));
		String generalIP = args[1];
		int totalCapCount = Integer.parseInt(args[2]);
		int startCapInx = Integer.parseInt(args[3]);
		int endCapInx = Integer.parseInt(args[4]);
		int localCapCount = endCapInx - startCapInx + 1;
		
		if (args.length < 5 + localCapCount) return;
		
		boolean localCapLoyalty[] = new boolean[localCapCount];
		int localCapInx[] = new int[localCapCount];
		
		for (int i = 0; i < localCapCount; ++i) {
			localCapLoyalty[i] = intToBoolean(Integer.parseInt(args[5 + i]));
			localCapInx[i] = startCapInx + i;
		}
		
		
		
		rt.exec("cmd /c start cmd.exe /K \"start rmiregistry\"");
		
		Thread.sleep(1000);
		
		if (isGeneralLocal) {
			System.err.println("cmd /c start cmd.exe /K \"java byzantium.General " + totalCapCount + " true\"");
			rt.exec("cmd /c start cmd.exe /K \"java byzantium.General " + totalCapCount + " true\"");
			Thread.sleep(1000);
		}
		
		
		
		for (int i = 0; i < localCapCount; ++i) {
			System.err.println("cmd /c start cmd.exe /K \"java byzantium.Captain " + generalIP + " " + localCapLoyalty[i] + " Captain_" + localCapInx[i] + "\"");
			rt.exec("cmd /c start cmd.exe /K \"java byzantium.Captain " + generalIP + " " + localCapLoyalty[i] + " Captain_" + localCapInx[i] + "\"");
		}
	}
}
