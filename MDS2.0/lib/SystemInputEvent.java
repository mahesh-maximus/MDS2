/**
 * @(#)SystemInputEvent.java
 *
 *
 * @author 
 * @version 1.00 2007/11/24
 */


public class SystemInputEvent {
	
	
	public static final int ALT_DOWN_MASK = 512; 
	public static final int ALT_GRAPH_DOWN_MASK = 8192; 
	public static final int ALT_GRAPH_MASK = 32; 
	public static final int ALT_MASK = 8; 
	public static final int BUTTON1_DOWN_MASK = 1024; 
	public static final int BUTTON1_MASK = 16; 
	public static final int BUTTON2_DOWN_MASK = 2048; 
	public static final int BUTTON2_MASK = 8; 
	public static final int BUTTON3_DOWN_MASK = 4096; 
	public static final int BUTTON3_MASK = 4; 
	public static final int CTRL_DOWN_MASK = 128; 
	public static final int CTRL_MASK = 2; 
	public static final int META_DOWN_MASK = 256; 
	public static final int META_MASK = 4; 
	public static final int SHIFT_DOWN_MASK = 64; 
	public static final int SHIFT_MASK = 1; 
		
		
	private int modifiers;	
		
	
	private	SystemInputEvent() {}	
		

    public SystemInputEvent(int modifiers) {
    	this.modifiers = modifiers;
    }
    
    public boolean isShiftDown() {
        return (modifiers & SHIFT_MASK) != 0;
    }


    public boolean isControlDown() {
        return (modifiers & CTRL_MASK) != 0;
    }


    public boolean isMetaDown() {
        return (modifiers & META_MASK) != 0;
    }


    public boolean isAltDown() {
        return (modifiers & ALT_MASK) != 0;
    }


    public boolean isAltGraphDown() {
        return (modifiers & ALT_GRAPH_MASK) != 0;
    }    
    
    
}