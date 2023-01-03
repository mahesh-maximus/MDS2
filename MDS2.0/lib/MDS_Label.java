/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;



public class MDS_Label extends JLabel implements MouseListener {



    public MDS_Label() {
        super();
    } 
    
    
    
    public MDS_Label(Icon image) {
        super(image);
    }
    
    
    
    public MDS_Label(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }
    
    
    
    public MDS_Label(String text) {
        super(text);
    }
    
    
    
    public MDS_Label(String text, Icon icon) {
        super(text);
        this.setIcon(icon);
    }    
    
    
    
    public MDS_Label(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }
    
    
    
    public MDS_Label(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }
    
    
    
    public void setHyperlinkTextViewEnabled(boolean b) {
        if(b) {
            this.addMouseListener(this);
        } else {
            this.removeMouseListener(this);
        }
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }



    public void mouseExited(MouseEvent e){
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }



    public void mousePressed(MouseEvent e){}
        
        
        
    public void mouseReleased(MouseEvent e) {}      
    
    
    
}    
    
    
    