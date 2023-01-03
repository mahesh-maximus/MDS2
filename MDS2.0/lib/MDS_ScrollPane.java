
import java.awt.*;
import javax.swing.*;



public class MDS_ScrollPane extends JScrollPane {



    public MDS_ScrollPane() {
        super();
        initialize();
    }
    
    
    
    public MDS_ScrollPane(Component view) {
        super(view);
        initialize();
    }
    
    
    
    public MDS_ScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        initialize();
    }
    
    
    
    public MDS_ScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        initialize();
    } 
    
    
    
    public void initialize() {

    }
    
    
    /*
    public MDS_ScrollBar getHorizontalScrollBar() {
        return(MDS_ScrollBar) super.getHorizontalScrollBar();     
    }
    
    
    
    public MDS_ScrollBar getVerticalScrollBar() {
        return(MDS_ScrollBar) super.getVerticalScrollBar();
    }*/
    
    
    
}    