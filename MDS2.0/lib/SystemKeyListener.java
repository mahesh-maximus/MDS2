/**
 * @(#)SystemKeyListener.java
 *
 *
 * @author 
 * @version 1.00 2007/11/24
 */

import java.util.*;

public interface SystemKeyListener extends EventListener {
	
	
	public void systemKeyPressed(SystemKeyEvent e);
	
	public void systemKeyReleased(SystemKeyEvent e);
    
    
}