package byzantium;

public class RepositoryItem {
// Fields
	public String ip;
	public String port;
	public String name;
	public boolean isGeneral;
	public boolean isTraitor;
	public boolean isActive;
	public PersonInterface remoteHandle;
// Constructors
	protected RepositoryItem(String i_ip, String i_port, String i_name) {
		ip = i_ip;
		port = i_port;
		name = i_name;
		isGeneral = false;
		isTraitor = false;
		isActive = false;
		remoteHandle = null;
	}
	protected RepositoryItem(String i_ip, String i_port, String i_name, boolean i_isGeneral, boolean i_isTraitor) {
		ip = i_ip;
		port = i_port;
		name = i_name;
		isGeneral = i_isGeneral;
		isTraitor = i_isTraitor;
		isActive = false;
		remoteHandle = null;
	}
	protected RepositoryItem(String i_ip, String i_port, String i_name, boolean i_isGeneral, boolean i_isTraitor, PersonInterface i_remoteHandle) {
		ip = i_ip;
		port = i_port;
		name = i_name;
		isGeneral = i_isGeneral;
		isTraitor = i_isTraitor;
		isActive = false;
		remoteHandle = i_remoteHandle;
	}
}
