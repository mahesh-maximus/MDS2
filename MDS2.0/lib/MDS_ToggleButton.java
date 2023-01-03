/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;



public class MDS_ToggleButton extends JToggleButton{



    public MDS_ToggleButton() {
        super();    
    }
    
    
    
    public MDS_ToggleButton(Action a) {
        super(a);    
    }
    
    
    
    public MDS_ToggleButton(Icon icon) {
        super(icon);   
    }
    
    
    
    public MDS_ToggleButton(Icon icon, boolean selected) {
        super(icon, selected);    
    }
    
    
    
    public MDS_ToggleButton(String text) {
        super(text);    
    }
    
    
    
    public MDS_ToggleButton(String text, boolean selected) {
        super(text, selected);   
    }
    
    
    
    public MDS_ToggleButton(String text, Icon icon) {
        super(text, icon);    
    }
    
    
    
    public MDS_ToggleButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);  
    }
    
    
    
}    