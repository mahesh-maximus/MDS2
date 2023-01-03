
public class WinUI {





    static {
        try{
            System.loadLibrary("WinUI");
        } catch(UnsatisfiedLinkError ex){
            System.out.println(ex.toString());
            System.out.println("(Console.dll not found).\n");  
        }
    }



    public native boolean findWindow(String clazz, String caption);



    public native void showWindow(String clazz, String caption);



    public native void sendMessage(String clazz, String caption, String msg);



    public native long getValidationCode();
 


}

