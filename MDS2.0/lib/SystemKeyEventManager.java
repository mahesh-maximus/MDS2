/**
 * @(#)SystemKeyListenerEventManager.java
 *
 *
 * @author 
 * @version 1.00 2007/11/24
 */
 
import java.util.*;
import javax.swing.event.*; 


public final class SystemKeyEventManager {
	
	
	private static SystemKeyEventManager skem = new SystemKeyEventManager();
	
	private EventListenerList listenerList = new EventListenerList();
	

    private SystemKeyEventManager() {}
    
    
    public static SystemKeyEventManager getSystemKeyEventManager() {
    	return skem;
    }
    
    protected void fireSystemKeyPressedEvent(int keyCode, int modifiers) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==SystemKeyListener.class) {
                ((SystemKeyListener)listeners[i+1]).systemKeyPressed(new SystemKeyEvent(keyCode, modifiers));                 
            }                       
        }      	
//		System.out.println("fireKeyPressed ...... keyCode "+keyCode + " modifiers "+modifiers);
	}
	
	
	protected void fireSystemKeyReleasedEvent(int keyCode, int modifiers) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==SystemKeyListener.class) {
                ((SystemKeyListener)listeners[i+1]).systemKeyReleased(new SystemKeyEvent(keyCode, modifiers));                 
            }                       
        }   		
//		System.out.println("fireKeyReleased ...... keyCode "+keyCode);
	}
	
	
    public void addSystemKeyListener(SystemKeyListener l) {
        listenerList.add(SystemKeyListener.class, l);
    }


    public void removeSystemKeyListener(SystemKeyListener l) {
        listenerList.remove(SystemKeyListener.class, l);
    }
   	
    
    
}