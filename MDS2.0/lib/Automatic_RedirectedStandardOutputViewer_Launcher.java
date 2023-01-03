
import java.io.*;
import java.util.*;



public final class Automatic_RedirectedStandardOutputViewer_Launcher implements RedirectedStandardOutputListener {



    private static Automatic_RedirectedStandardOutputViewer_Launcher arsvl = new Automatic_RedirectedStandardOutputViewer_Launcher();
	//private static boolean rstovLoaded = false;


    private Automatic_RedirectedStandardOutputViewer_Launcher() {

        Vector vctOut = MDS.getRedirectedStandardOutput().getOutputHistory();   
        Vector vctError = MDS.getRedirectedStandardOutput().getErrorOutputHistory();
        
        if(vctOut.size() > 0 || vctError.size() > 0) {
            MDS.getProcessManager().execute(new File(MDS.getBinaryPath(), "RedirectedStandardOutputViewer.class"), new String[] {"1"});
        }
        
        MDS.getRedirectedStandardOutput().addRedirectedStandardOutputListener(this);

    }
    
    
    
    public static Automatic_RedirectedStandardOutputViewer_Launcher getAutomatic_RedirectedStandardOutputViewer_Launcher() {
        return arsvl;
    }
    
    
    
    public void rsdoNewOutputText(String text) {
//        if(!rstovLoaded) {
//        MDS.getProcessManager().execute(new File(MDS.getBinaryPath(), "RedirectedStandardOutputViewer.class"), new String[] {"0"});
//        	rstovLoaded = true;
//        }
    }
    
    
    
    public void rsdoErrorText(String text) {
//    	if(!rstovLoaded) {
//        MDS.getProcessManager().execute(new File(MDS.getBinaryPath(), "RedirectedStandardOutputViewer.class"), new String[] {"1"});  
//    		rstovLoaded = true;
//    	}
    }     
    
    
    
}    