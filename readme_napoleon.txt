Version 1.0:
Napoleon package now supports delay-free broadcasting on multiple process on different machines (within LAN)
ATM there is no need to download file from remote, so no need for securityManager, yet.

to use package napoleon:
1:Connect three different machines ABC to the same LAN. Make sure ABC can ping each other without any problem

2:Change the content of procAddressList_ (in Proc.java) to the LAN IP address of A/B/C.
eg:
private static ArrayList<String> procAddressList_ = new ArrayList<String>(Arrays.asList(
			"//192.168.1.3:117/",
			"//192.168.1.3:118/",
			"//192.168.1.12:119/" ));
means
process A will be ran on 192.168.1.3, its rmiregistry will be open on port 117
process B will be ran on 192.168.1.3 as well, its rmiregistry will be open on port 118
process C will be ran on 192.168.1.12, its rmiregistry will be open on port 119

3: start rmiregistry on ABC, at correct directory, with correct port number.
eg:
on machine A:
cd /d D:\Files\Documents\GitHub\Distributed_Algorithm\bin
start rmiregistry 117

4: start process
eg:
on machine A:
java napoleon.Proc 0
replace 0 with 1/2 on B/C




