/**
 * @(#)OperatingSystemEventManager.java
 *
 *
 * @author 
 * @version 1.00 2007/10/28
 */


public class OperatingSystemEventManager {
	
	
	
	private static OperatingSystemEventManager osem = new OperatingSystemEventManager();
	
	
	
	public static OperatingSystemEventManager getOperatingSystemEventManager() {
		return osem;
	}


	public static void fireWindowMessageEvent(int x) {
		System.out.println("WASANA MAHESH WASANA ...... "+x);
	}
	

    
    
    
    
}