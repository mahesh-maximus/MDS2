/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;



public class MDS_TabbedPane extends JTabbedPane implements ChangeListener {



    public MDS_TabbedPane() {
        super();
        initialize();
    }
    
    
    
    public MDS_TabbedPane(int tabPlacement) {
        super(tabPlacement);
        initialize();
    }
    
    
    
    public MDS_TabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
        initialize();
    }
    
    
    
    private void initialize() {
        this.addChangeListener(this);   
    }
    
    
    
    public void stateChanged(ChangeEvent e) {
        MDS.getSound().playSound(new File("Media\\Sound\\Select.wav"));
    }     
    
    
    
}    