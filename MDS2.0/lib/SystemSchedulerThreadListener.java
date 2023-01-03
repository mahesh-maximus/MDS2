/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.util.*;



public interface SystemSchedulerThreadListener extends EventListener {



    public long getSystemScheduler_EventInterval();



    public void systemSchedulerEvent();
    


}