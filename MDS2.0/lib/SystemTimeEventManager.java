/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;



public final class SystemTimeEventManager implements SystemCycleThreadListener, MDS_PropertyChangeListener
											, SystemKeyListener, SystemMouseListener, SystemMouseWheelListener {


    
    private static SystemTimeEventManager stem = new SystemTimeEventManager();
    private long screenSaveLaunchCount = System.currentTimeMillis();
    private long screenSaveLaunchWaitTime = 60000;
    private String screenSaverName = "";
    
    private Logger log = Logger.getLogger("SystemTimeEventManager");
    
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    
    
    
    private SystemTimeEventManager() {
        loadPrperties(ppm.getProperty(DisplayProperties.PROPERTY_NAME));   
        VirtualThreading.create_SCT_VT(this, "SystemTimeEventManager");
        SystemKeyEventManager.getSystemKeyEventManager().addSystemKeyListener(this);
        SystemMouseEventManager.getSystemMouseEventManager().addSystemMouseListener(this);
        SystemMouseWheelEventManager.getSystemMouseWheelEventManager().addSystemMouseWheelListener(this);
        ppm.addMDS_PropertyChangeListener(this);
    }
    
    
    
    private void loadPrperties(MDS_Property prop) {
    	screenSaverName = (String)prop.getSupProperty(DisplayProperties.PROPERTY_SCREEN_SAVER_NAME);
    	screenSaveLaunchWaitTime = Long.parseLong(prop.getSupProperty(DisplayProperties.PROPERTY_SCREEN_SAVER_LAUNCH_WAIT_TIME)) * 60000;
    }
    
    
    
    public static SystemTimeEventManager getSystemTimeEventManager() {
        return stem;
    }
    
    
    
    public long getSystemCycle_EventInterval() {
        return 1000;
    }



    public void autoExecuteSubRoutine() {
    	if(screenSaveLaunchWaitTime > 0) {
//	    	System.out.println((System.currentTimeMillis() - screenSaveLaunchCount) + " : " + screenSaveLaunchWaitTime);
	        if((System.currentTimeMillis() - screenSaveLaunchCount) > screenSaveLaunchWaitTime) {
	            MDS.getProcessManager().execute(new File(MDS.getBinaryPath(), screenSaverName+".class"));
	        }  
    	}
    }
    
    
    public void propertyChanged(MDS_PropertyChangeEvent e) {
    	if(e.getPropertyName().equals(DisplayProperties.PROPERTY_NAME)) {
			loadPrperties(e.getProperty());
    	}
    } 
    	
    	
    public void systemKeyPressed(SystemKeyEvent e) {
    	screenSaveLaunchCount = System.currentTimeMillis();
    }
    
    
    public void systemKeyReleased(SystemKeyEvent e){} 
    	
    	
    	
    public void systemMousePressedEvent(SystemMouseEvent e) {
    	screenSaveLaunchCount = System.currentTimeMillis();	
    }
    
    
    public void systemMouseReleasedEvent(SystemMouseEvent e) {}	
    	
    	
    	
    public void systemMouseWheelMoved(SystemMouseWheelEvent e) {
    	screenSaveLaunchCount = System.currentTimeMillis();		
    }	
    
    
    
}    