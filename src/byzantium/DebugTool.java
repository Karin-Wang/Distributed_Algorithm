package byzantium;

public class DebugTool {
	public static void print(String i_str) {
		System.err.println(i_str);
	}
	public static void printAndExit(String i_str) {
		System.err.println(i_str);
		System.exit(1);
	}
	
}
