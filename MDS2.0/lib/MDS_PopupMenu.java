/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;
import java.awt.*;
import java.io.*;


public class MDS_PopupMenu extends JPopupMenu implements ScreenSaverListener{



    private ScreenSaverEventManager ssem = MDS.getScreenSaverEventManager(); 



    public MDS_PopupMenu() {
        super();
		setLightWeightPopupEnabled(true);
		setLightWeightPopupEnabled(true);
        ssem.addScreenSaverListener(this);
    }
    
    
    
    public MDS_PopupMenu(String label) {
        super (label);
		setLightWeightPopupEnabled(true);
		setLightWeightPopupEnabled(true);
        ssem.addScreenSaverListener(this);
    }
    
    
    
    public void show(Component invoker, int x, int y) {
        SwingUtilities.updateComponentTreeUI(this);
        super.show(invoker, x, y);
        MDS.getSound().playSound(new File("Media\\Sound\\Launch.wav"));

    }   
    
    
    
    public void loadScreenSaver() {
        if(this.isVisible()) {
            this.setVisible(false);
            this.firePopupMenuWillBecomeInvisible();
            this.firePopupMenuCanceled();    
        }    
    }
    
    
    
    public void teminateScreenSaver() {
    
    }  



}