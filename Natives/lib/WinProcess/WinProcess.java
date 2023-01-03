import java.util.*;



public class WinProcess {



    public static final int SW_HIDE = 0;
    public static final int SW_MAXIMIZE = 1;
    public static final int SW_MINIMIZE = 2;
    public static final int SW_RESTORE = 3;
    public static final int SW_SHOW = 4;
    public static final int SW_SHOWDEFAULT = 5;
    public static final int SW_SHOWMAXIMIZED = 6;
    public static final int SW_SHOWMINIMIZED = 7;
    public static final int SW_SHOWMINNOACTIVE = 8;
    public static final int SW_SHOWNA = 9;
    public static final int SW_SHOWNOACTIVATE = 10;
    public static final int SW_SHOWNORMAL = 11;



    static {
        try{
            System.loadLibrary("WinProcess");
        } catch(UnsatisfiedLinkError ex){
            System.out.println(ex.toString());
            System.out.println("(Console.dll not found).\n");  
        }
    }




    public native long createProcess(String name);



    public native long createProcess(String applicationName, String commandLine, boolean inheritHandles, String currentDirectory);



    public native boolean terminateProcess(long id);	



    public native void executeExe(String path, String parameters, int showCmd);



    public native long getValidationCode();



} 