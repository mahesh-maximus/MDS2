public class MDS {



    static {
        try{
            System.loadLibrary("Nmds");
        } catch(UnsatisfiedLinkError ex){
            System.out.println(ex.toString());
            System.out.println("(Console.dll not found).\n");  
        }
    }



    public native boolean isPrevInstance();



    public native boolean terminatWinExplorer(); 	



    public native long getValidationCode();




} 