
import java.awt.event.*;



public abstract class ScreenSaver extends MDS_FullScreenWindow implements MouseListener, MouseMotionListener {



    private ScreenSaverEventManager ssem = MDS.getScreenSaverEventManager();  



    public ScreenSaver() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        ssem.fire_SCR_LoadEvent();
    }
    
    
    
    public abstract void terminate_Scr();
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){
        terminate_Scr();
        ssem.fire_SCR_TerminateEvent();
    }
        
        
        
    public void mouseReleased(MouseEvent e) {}  
    
    
    
    public void mouseDragged(MouseEvent e) {}
    
    
    
    public void mouseMoved(MouseEvent e) {
        terminate_Scr();
        ssem.fire_SCR_TerminateEvent();    
    }   
    
    
    
}    