/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;
import java.util.logging.*;


public class MDS_Main {
	
	
	
	private static Logger log = Logger.getLogger("MDS_Main");



    public static void main(String arg[]) {

        String vers = System.getProperty("java.version");
        log.info("Java(TM) SE Runtime Environment : "+vers);
        //!vers.equals("1.6.0_03"
        if(vers.compareTo("1.6.0_03") < 0) {
            JOptionPane.showMessageDialog(null, "MDS must be run with Java Runtime Envirenment 1.6.0_03 or higher version VM.", "Unsupported Java Runtime Version", JOptionPane.ERROR_MESSAGE);
            System.exit(0);                   
        } 
        
        MDS.checkDisplayMode();
            	
        MDS.registerNatives();
        
		OperatingSystem.getOperatingSystem().allocateConsole();	
		
		OperatingSystem.getOperatingSystem().setConsoleTitle("MDS 2.0 [Log Console]");	
			
		OperatingSystem.getOperatingSystem().setConsoleForegroundColor(OperatingSystem.COLOR_INTENSITY_GREEN);	
					
		MDS.addNativeConsoleHandler();


		log.info("Setting main thread priority to MAX_PRIORITY.");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);  	

        Runtime.getRuntime().addShutdownHook(new JVM_ShutdownHook());
	
		
        try {
        	OperatingSystem.getOperatingSystem().createMutex(MDS.getMDS_Name());
        } catch (Exception ex) {
        	ex.printStackTrace();
            OperatingSystem.getOperatingSystem().showWindow("SunAwtFrame", "MDS 2.0 [Developer's Edition]");
            System.exit(0);
        }
        
		//OperatingSystem.getOperatingSystem().setHighPriorityClass();
        

        if(arg.length == 1 && arg[0].equals(MDS.DEBUG)) MDS.initialise_MDS(true);
        else MDS.initialise_MDS(false);
		
		
    }
    
    
    
    
    



}