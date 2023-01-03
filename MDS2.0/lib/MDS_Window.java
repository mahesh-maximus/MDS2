/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;



public class MDS_Window extends MDS_Panel {



    private Integer layer;



    public MDS_Window() {
        this.setOpaque(true);
    }
    
    
    
    public MDS_Window(Integer l) {
        layer = l;
        this.setOpaque(true);
    }
    
    
    
    public void setVisible(boolean b) {
        super.setVisible(b);
        if(b) {
            if(layer == null) {
                MDS.getBaseWindow().getDesktop().add(this ,JLayeredPane.DEFAULT_LAYER);
            } else {
                MDS.getBaseWindow().getDesktop().add(this ,layer);                
            }
        } else {
            MDS.getBaseWindow().getDesktop().remove(this);
        }
    }
	
	public void ashow() {
		super.setVisible(true);
	}
    
    
    
    public void dispose() {
    
    }
    
    
    
}    