/**
 * @(#)SystemMouseEvent.java
 *
 *
 * @author 
 * @version 1.00 2007/11/25
 */


public class SystemMouseEvent extends SystemInputEvent  {
	
	
	public static final int BUTTON1 = 1; 	
	public static final int BUTTON2 = 2; 
	public static final int BUTTON3 = 3;
	public static final int NOBUTTON = 0; 
	
	
	int x;
	int y;	
	int button;	 

    public SystemMouseEvent(int x, int y, int button, int modifiers) {
    	super(modifiers);
    	this.x = x;
    	this.y = y;
    	this.button = button;	
    }
    
    public int getButton() {
    	return button;
    }
    
    
}