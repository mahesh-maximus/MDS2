/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.event.*;



public class MDS_Slider extends JSlider {



    public MDS_Slider() {
        super();
        initialize();
    }
    
    
    
    public MDS_Slider(BoundedRangeModel brm) {
        super(brm);
        initialize();
    }
    
    
    
    public MDS_Slider(int orientation) {
        super(orientation);
        initialize();
    }
    
    
    
    public MDS_Slider(int min, int max) {
        super(min, max);
        initialize();
    }
    
    
    
    public MDS_Slider(int min, int max, int value) {
        super(min, max, value);
        initialize();
    }
    
    
    
    public MDS_Slider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
        initialize();
    }
    
    
    
    private void initialize() {
    } 
    
    
}       