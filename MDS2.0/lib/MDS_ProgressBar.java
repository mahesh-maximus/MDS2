/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;



public class MDS_ProgressBar extends JProgressBar {



    public MDS_ProgressBar() {
        super();
    }
    
    
    
    public MDS_ProgressBar(BoundedRangeModel newModel) {
        super(newModel);
    }
    
    
    
    public MDS_ProgressBar(int orient) {
        super(orient);
    }
    
    
    
    public MDS_ProgressBar(int min, int max) {
        super(min, max);
    }
    
    
    
    public MDS_ProgressBar(int orient, int min, int max) {
        super(orient, min, max);
    }
    
    
    
}    
    
    
    
    