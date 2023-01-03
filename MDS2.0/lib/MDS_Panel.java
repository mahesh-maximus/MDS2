/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MDS_Panel extends JPanel {



    public MDS_Panel() {
        super();
        //MDS_User.addInputListeners(this);
    } 
    
    
    
    public MDS_Panel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        //MDS_User.addInputListeners(this);
    } 
    
    
    
    public MDS_Panel(LayoutManager layout) {
        super(layout);
        //MDS_User.addInputListeners(this);
    }
    
    
    
    public MDS_Panel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        //MDS_User.addInputListeners(this);
    }  
    
    
    
}    