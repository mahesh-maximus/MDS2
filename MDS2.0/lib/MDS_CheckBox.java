/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.event.*;



public class MDS_CheckBox extends JCheckBox {



    public MDS_CheckBox() {
        super();
        initialize();
    }
    
    
    
    public MDS_CheckBox(Action a) {
        super(a);
        initialize();
    }
    
    
    
    public MDS_CheckBox(Icon icon) {
        super(icon);
        initialize();
    }
    
    
    
    public MDS_CheckBox(Icon icon, boolean selected) {
        super(icon, selected);
        initialize();
    } 
    
    
    
    public MDS_CheckBox(String text) {
        super(text);
        initialize();
    }
    
    
    
    public MDS_CheckBox(String text, boolean selected) {
        super(text, selected);
        initialize();
    }
    
    
    
    public MDS_CheckBox(String text, Icon icon) {
        super(text, icon);
        initialize();
    }
    
    
    
    public MDS_CheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        initialize();
    }
    
    
    
    private void initialize() {
    
    }
    
    
}    
    
   
    
    
    
    