/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
import java.util.*;
 
 

public interface FileSystemListener extends EventListener {



    public void fileSystemUpdated(FileSystemEvent e);
    
    
    
}    