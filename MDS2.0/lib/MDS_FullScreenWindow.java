/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import javax.swing.*;



public class MDS_FullScreenWindow extends MDS_Panel {



    public MDS_FullScreenWindow() {   
        //super(JLayeredPane.POPUP_LAYER);
    }
    
    
    
    public void setVisible(boolean aFlag) {
        MDS.getBaseWindow().setFullScreenWindow(this);
    }
    
    
    
    public void dispose() {
        //	super.dispose();
        MDS.getBaseWindow().removeFullScreenWindow(this);
    }  



}