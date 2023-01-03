/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;



public class MDS_Table extends JTable implements MouseListener {



    public MDS_Table() {
        super();
        initialize();
    }
    
    
    
    public MDS_Table(TableModel dm) {
        super(dm);
        initialize();
        this.addMouseListener(this);
    }  
    
    
    
    private void initialize() {   
    }      



    public int getRowForLocation(int y) {
        int rowHeight = this.getRowHeight();
        int currentRow = y/rowHeight;
        
        return currentRow;
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){
        if(e.getClickCount() == 1) {
            MDS.getSound().playSound(new File("Media\\Sound\\Select.wav"));
        }
    }
    
    
    
    public void mouseReleased(MouseEvent e) {} 
    
     
    
    
    
}    