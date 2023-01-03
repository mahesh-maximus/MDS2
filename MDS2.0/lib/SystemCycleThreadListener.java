/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */

import java.util.*;



public interface SystemCycleThreadListener extends EventListener {



    public long getSystemCycle_EventInterval();



    public void autoExecuteSubRoutine();
    
    
     
}    