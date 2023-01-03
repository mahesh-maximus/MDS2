/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.util.*;




public interface ClipBoardListener extends EventListener {



    public void clipBoard_CopyTo();
    
    
    
    public void clipBoard_MoveTo(); 
    
    
    
    public void clipBoard_Paste_Copied();
    
    
    
    public void clipBoard_Paste_Moved();
    
    
    
    public void clipBoard_Empty();

} 