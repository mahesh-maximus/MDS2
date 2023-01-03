/**
 * @(#)SystemMouseEventManager.java
 *
 *
 * @author 
 * @version 1.00 2007/11/24
 */
import java.util.*;
import javax.swing.event.*; 


public final class SystemMouseEventManager {
	
	
	static SystemMouseEventManager smem = new SystemMouseEventManager();
	
	private EventListenerList listenerList = new EventListenerList();
	

    private SystemMouseEventManager() {}
    
    
    public static SystemMouseEventManager getSystemMouseEventManager() {
    	return smem;
    }
    
    
	protected void fireSystemMousePressedEvent(int x, int y, int button, int modifiers) {//int x, int y, int button
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==SystemMouseListener.class) {
                ((SystemMouseListener)listeners[i+1]).systemMousePressedEvent(new SystemMouseEvent(x, y, button, modifiers));                 
            }                       
        }   	
//		System.out.println("mousePressed ...... x"+x+" y"+y+" button "+button);
	}
	
	
	protected void fireSystemMouseReleasedEvent(int x, int y, int button, int modifiers) {//int x, int y, int button
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==SystemMouseListener.class) {
                ((SystemMouseListener)listeners[i+1]).systemMouseReleasedEvent(new SystemMouseEvent(x, y, button, modifiers));                 
            }                       
        }   	
//		System.out.println("mouseReleased ...... x"+x+" y"+y+" button "+button);
	}	 
		

    public void addSystemMouseListener(SystemMouseListener l) {
        listenerList.add(SystemMouseListener.class, l);
    }


    public void removeSystemMouseListener(SystemMouseListener l) {
        listenerList.remove(SystemMouseListener.class, l);
    }		  
    
    
}