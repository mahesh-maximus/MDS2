/**
 * @(#)OperatingSystem.java
 *
 *
 * @author 
 * @version 1.00 2007/10/14
 */


public class OperatingSystem {
	
	
	
    public static final int DRIVE_UNKNOWN = 10;
    public static final int DRIVE_NO_ROOT_DIR = 20;
    public static final int DRIVE_REMOVABLE = 30;
    public static final int DRIVE_FIXED = 40;
    public static final int DRIVE_REMOTE = 50;
    public static final int DRIVE_CDROM = 60;
    public static final int DRIVE_RAMDISK = 70;
    public static final int UNRECOGNIZED_DRIVE = 80;
    
    public static final int COLOR_WHITE = 0;
    public static final int COLOR_INTENSITY_YELLOW = 1;
    public static final int COLOR_INTENSITY_BLUE = 2;
    public static final int COLOR_INTENSITY_GREEN = 3;
    public static final int COLOR_INTENSITY_RED = 4;
    
    public static final int WH_CALLWNDPROC = 0;
    public static final int WH_CALLWNDPROCRET = 1;
    public static final int WH_CBT = 2;
    public static final int WH_DEBUG = 3;
    public static final int WH_FOREGROUNDIDLE = 4;
    public static final int WH_GETMESSAGE = 5;
    public static final int WH_JOURNALPLAYBACK = 6;
    public static final int WH_JOURNALRECORD = 7;
    public static final int WH_KEYBOARD = 8;
    public static final int WH_KEYBOARD_LL = 9;
    public static final int WH_MOUSE = 10;
    public static final int WH_MOUSE_LL = 11;
    public static final int WH_MSGFILTER = 12;
    public static final int WH_SHELL = 13;
    public static final int WH_SYSMSGFILTER = 14;
    
    
    private static OperatingSystem os = new OperatingSystem();
    
    private SystemMouseEventManager smem = SystemMouseEventManager.getSystemMouseEventManager();
    private SystemMouseWheelEventManager smwem = SystemMouseWheelEventManager.getSystemMouseWheelEventManager();
    private SystemKeyEventManager skem = SystemKeyEventManager.getSystemKeyEventManager();
    
    
    
    private OperatingSystem() {}
    
    
    
	public static OperatingSystem getOperatingSystem() {
		return os;
	}    	  	
	
	
	
	public  native void createMutex(String name) throws MutexAlreadyExistsException;
	
	
	
    public  native int getDriveType(String rootDir);



    public  native String getVolumeName(String rootDir); 



    public  native String getFileSystemName(String rootDir); 



    public  native void setDriveVolumeLabel(String rootDir , String label);
    
    
    
//    public native void getVolumeInformation(String rootDir, String volumeName, 
//    														long serialNumber,
//    														long maxComponentLen, 
//    														long fileSystemFlags, 
//    														String fileSystemName);
    
    
    
    public  native boolean setHighPriorityClass();
    
    
    
    public  native long createProcess(String CommandLine);
	
	
	
	public  native long createProcess(String applicationName, String commandLine, boolean inheritHandles, String currentDirectory);



    public  native boolean terminateProcess(long pid);
    
    
    
    public  native boolean allocateConsole();	
    	
    	
    	
    public  native void showWindow(String className, String windowName);
    
    
    
    public native void setWindowsHook(int idHook, String className, String windowName);
    
    
    
	public native void unHookWindowsHook(int idHook);
	
	
	
	public native void printConsole(String text); 
		
		
		
	public native void printConsole(String text, int color); 
		
		
		
	public native void setConsoleTitle(String text);
	
	
	
	public native void ejectRemovableMedia(String drive) throws NativeLibrayException;
	
	
	
	public native void setConsoleForegroundColor(int color); 
		   
		
		
	private void fireSystemMousePressedEvent(int x, int y, int button, int modifiers) {//int x, int y, int button
		smem.fireSystemMousePressedEvent(x, y, button, modifiers);
	}
	
	
	private void fireSystemMouseReleasedEvent(int x, int y, int button, int modifiers) {//int x, int y, int button
		smem.fireSystemMouseReleasedEvent(x, y, button, modifiers);	
	}	
		
	
	private void fireSystemMouseWheelMovedEvent(int x, int y, int wheelRotation, int modifiers) {
		smwem.fireSystemMouseWheelMovedEvent(x, y, wheelRotation, modifiers);
	}
	
	
	private void fireSystemKeyPressedEvent(int keyCode, int modifiers) {
		skem.fireSystemKeyPressedEvent(keyCode, modifiers);
	}
	
	
	private void fireSystemKeyReleasedEvent(int keyCode, int modifiers) {
		skem.fireSystemKeyReleasedEvent(keyCode, modifiers);
	}
		
		
						

	
    
}