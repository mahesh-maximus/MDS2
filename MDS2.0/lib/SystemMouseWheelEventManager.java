/**
 * @(#)SystemMouseWheelEventManager.java
 *
 *
 * @author 
 * @version 1.00 2007/11/24
 */
 
 import java.util.*;
import javax.swing.event.*; 


public final class SystemMouseWheelEventManager {
	
	
	private static SystemMouseWheelEventManager smwem = new SystemMouseWheelEventManager();
	
	private EventListenerList listenerList = new EventListenerList();
	
	

    private SystemMouseWheelEventManager() {
    }
    
    
    
    public static SystemMouseWheelEventManager getSystemMouseWheelEventManager() {
    	return smwem;	
    }
    
    
    protected void fireSystemMouseWheelMovedEvent(int x, int y, int wheelRotation, int modifiers) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==SystemMouseWheelListener.class) {
                ((SystemMouseWheelListener)listeners[i+1]).systemMouseWheelMoved(new SystemMouseWheelEvent(x, y, wheelRotation, modifiers));                 
            }                       
        }       	
//		System.out.println("mouseWheelMoved ...... x"+x+" y"+y+" wheelRotation "+wheelRotation);
	}
	
	
    public void addSystemMouseWheelListener(SystemMouseWheelListener l) {
        listenerList.add(SystemMouseWheelListener.class, l);
    }


    public void removeSystemMouseWheelListener(SystemMouseWheelListener l) {
        listenerList.remove(SystemMouseWheelListener.class, l);
    }	
    
    
}