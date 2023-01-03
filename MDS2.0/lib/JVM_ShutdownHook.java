
public class JVM_ShutdownHook extends Thread {
    
    
    
    public JVM_ShutdownHook() {
        super("MDS Shutdown Hook");
    }
    
    
    
    public void run() {
//        if(MDS.fexMonitorID != 0) {
//            //WinProcess.terminateProcess(MDS.fexMonitorID);
//        }
//        if(HalfLife.getProcessId() != 0) {
//        	//WinProcess.terminateProcess(HalfLife.getProcessId());
//        }
		/*
        if(Mp3Player.wmp != null) {
            Mp3Player.wmp.destroy();
        } */ 
        System.out.println("<<<<<<<<<<<<<<< JVM_ShutdownHook >>>>>>>>>>>>>>");	
        	
        	          
    }
        
        
        
} 