
import javax.swing.*;
import java.awt.*;



public class DragIcon implements Runnable {  
    
    
    Thread thread;
    MDS_Window window;  
    ImageIcon icon;
    Mouse mouse = new Mouse();
    MDS_Point previousPos = new MDS_Point(0, 0);
    MDS_Point mousePos;
    
    
    public DragIcon() {
            
    }
        
        
        
    public void showDragIcon(ImageIcon i) {
        icon = i;
        window = new MDS_Window(JLayeredPane.POPUP_LAYER);
        window.setOpaque(false);
        window.setLayout(new BorderLayout());
        window.add(new MDS_Label(i), BorderLayout.CENTER);
        window.setSize(i.getIconWidth(), i.getIconHeight());
        mousePos = mouse.getMousePointerPos();
        window.setLocation(mousePos.getX(), mousePos.getY());
        window.setVisible(true);
        thread = new Thread(this);
        thread.start(); 
    }
        
        
        
    public void hideDragIcon() {
        if(window != null) {
            window.setVisible(false);
        }
        window.dispose();
        if(thread != null) {
            thread.interrupt();
        }
        thread = null;
    }
        
        
        
    public void run() {
        try {
            while(true) {
                thread.sleep(100);
                mousePos = mouse.getMousePointerPos();
                if(!previousPos.equals(mousePos)) {
                    previousPos = mousePos;
                    window.setLocation(mousePos.getX(), mousePos.getY());
                }
            }
        } catch(InterruptedException ex) {
            
        }
    }
        
}