/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;
import java.awt.event.*;



public class MDS_Button extends JButton {



    public MDS_Button() {
        super();
        initialize();
    }
    
    
    
    public MDS_Button(Action a) {
        super(a);
        initialize();
    }
    
    
    
    public MDS_Button(Icon icon) {
        super(icon);
        initialize();
    }
    
    
    
    public MDS_Button(String text) {
        super(text);
        initialize();
    } 
    
    
    
    public MDS_Button(String text, Icon icon) {
        super(text, icon);
        initialize();
    }
    
    
    
    private void initialize() {
   
    }
    
    
    
}    