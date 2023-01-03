/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
public class DesktopIconEvent {



    MDS_DesktopIcon di;



    public DesktopIconEvent(MDS_DesktopIcon d) {
        di = d;
    }
    
    
    
    public String getIconName() {
        return di.getName(); 
    }
    
    
    
}    