/**
 * @(#)SystemMouseWheelEvent.java
 *
 *
 * @author 
 * @version 1.00 2007/11/25
 */


public class SystemMouseWheelEvent extends SystemMouseEvent {
	
	
	
	int wheelRotation;
	

    public SystemMouseWheelEvent(int x, int y, int wheelRotation, int modifiers) {
    	super(x, y, SystemMouseEvent.NOBUTTON, modifiers);
    	this.wheelRotation = wheelRotation;
    }
    
    
    public int getWheelRotation() {
    	return wheelRotation;
    }
    
    
}