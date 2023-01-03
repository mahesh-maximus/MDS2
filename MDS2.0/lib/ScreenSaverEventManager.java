
import javax.swing.event.*;
import java.util.*;



public class ScreenSaverEventManager {



    private static ScreenSaverEventManager ssem = new ScreenSaverEventManager();
    private EventListenerList listenerList = new EventListenerList();



    private ScreenSaverEventManager() {}
    
    
    
    public static ScreenSaverEventManager getScreenSaverEventManager() {
        return ssem;
    }
    
    
    
    public void addScreenSaverListener(ScreenSaverListener l) {
        listenerList.add(ScreenSaverListener.class, l);    
    }
    
    
    
    public void removeScreenSaverListener(ScreenSaverListener l) {
        listenerList.remove(ScreenSaverListener.class, l);
    }
        
    
    
    public void fire_SCR_LoadEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==ScreenSaverListener.class) {
                ((ScreenSaverListener)listeners[i+1]).loadScreenSaver();                                                                          
            }                       
        }         
    }
    
    
    
    public void fire_SCR_TerminateEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==ScreenSaverListener.class) {
                ((ScreenSaverListener)listeners[i+1]).teminateScreenSaver();                                                                          
            }                       
        }     
    }
    
    
    
}    