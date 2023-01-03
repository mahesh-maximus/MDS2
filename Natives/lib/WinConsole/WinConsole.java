import java.util.*;



public class WinConsole {




    static {
        try{
            System.loadLibrary("WinConsole");
        } catch(UnsatisfiedLinkError ex){
            System.out.println(ex.toString());
            System.out.println("(Console.dll not found).\n");  
        }
    }




    //public native long createProcess(String name);



    //public native long createProcess(String applicationName, String commandLine, boolean inheritHandles, String currentDirectory);



    public native boolean allocateConsole();	



    //public native void executeExe(String path, String parameters, int showCmd);



    public native long getValidationCode();



} 