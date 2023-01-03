/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.io.*; 
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;



public class MDS_Frame extends JInternalFrame implements InternalFrameListener, ComponentListener {


    
    MDS_UIManager uim;
    JInternalFrame ifrDialog;

    
    private boolean frameTitle_Visiblility_In_TaskBar = true;
    private int frameId = 0;
    static boolean in_The_Process_of_DeiconifyInternalFrame = false;;
    
    
    
    public MDS_Frame() {
    
        super();
        initialise();
            
    }
    
    
    
    public MDS_Frame(String title) {
    
        super(title);
        initialise();
            
    }
    
    
    
    
    public MDS_Frame(String title, boolean resizable) {
    
        super(title, resizable);
        initialise();
        
    }
    
    
    
    public MDS_Frame(String title, boolean resizable, boolean closable) {
    
        super(title, resizable, closable);
        initialise(); 
        
    }
    
    
    
    public MDS_Frame(String title, boolean resizable, boolean closable, boolean maximizable) {
    
        super(title, resizable, closable, maximizable);
        initialise();
        
    }
    
    
    
    public MDS_Frame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
    
        super(title, resizable, closable, maximizable, iconifiable);
        initialise();   
          
    }    
    
    
    
    public MDS_Frame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, ImageIcon i) {
    
        super(title, resizable, closable, maximizable, iconifiable);
        this.setFrameIcon(i);
        initialise();   
          
    }
    
    
    
    private void initialise() {
		
        this.addInternalFrameListener(this);
        this.addComponentListener(this);
        uim =  MDS.getMDS_UIManager();
        uim.addFrame(this);
        frameId = uim.getContainerId();
    }
    
    
    
    public int getFrameId() {
        return frameId;
    }
    
    
    

    public void setCenterScreen() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (d.getWidth()/2) - (this.getWidth()/2);
        int y = (int) (d.getHeight()/2) - (this.getHeight()/2);
        this.setLocation(x, y);
    } 
    
    
    
    public void setFrameIcon(ImageIcon i) {
        super.setFrameIcon(ImageManipulator.createScaledImageIcon(i, 14, 16, ImageManipulator.ICON_SCALE_TYPE));
    }
    
    
    /*
    public void setFrameTitle_Visible_In_TaskBar(boolean b) {
        frameTitle_Visiblility_In_TaskBar = b;
    }*/
    
    
    
    public void showInTaskBar(boolean b) {
        frameTitle_Visiblility_In_TaskBar = b;
    }    
    
    
    
    public boolean isFrameTitle_Visible_In_TaskBar() {
        return frameTitle_Visiblility_In_TaskBar;       
    }    
    
    
    
    public void setInternalFrameDialog(JInternalFrame ifrd) {
        ifrDialog = ifrd;
    }
    
    
    
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if(aFlag) {
            MDS.getSound().playSound(new File("resources\\sound\\Launch.wav"));
        }
        
    }
    
    
    
    public void dispose() {
        super.dispose();
        MDS.getSound().playSound(new File("resources\\sound\\KDE_Close_Window.wav"));        
    } 
    
      
    
    public void internalFrameActivated(InternalFrameEvent e) {
        if(ifrDialog != null) {
           ifrDialog.setVisible(true);   
           MDS.getBaseWindow().getDesktop().getDesktopManager().activateFrame(ifrDialog);
           try {
               ifrDialog.setSelected(true); 
           } catch(Exception ex) {
               
           }    
        }
    }
    
    
    
    public void internalFrameClosed(InternalFrameEvent e) {}
    
    
    
    public void internalFrameClosing(InternalFrameEvent e) {
        //uim.removeFrame(this);    
    }
    
    
    
    public void internalFrameDeactivated(InternalFrameEvent e) {}
    
    
    
    public void internalFrameDeiconified(InternalFrameEvent e) {
        this.setVisible(true);
        MDS.getSound().playSound(new File("resources\\sound\\KDE_Window_DeIconify.wav"));    
    }
    
    
    
    public void internalFrameIconified(InternalFrameEvent e) {
        this.setVisible(false);
        MDS.getSound().playSound(new File("resources\\sound\\KDE_Window_Iconify.wav"));
    }
    
    
    
    public void internalFrameOpened(InternalFrameEvent e) {}
    
    
    
    public void componentHidden(ComponentEvent e) {}



    public void componentMoved(ComponentEvent e) {}



    public void componentResized(ComponentEvent e) {
        if(isMaximum()) {
            try {
                //this.setMaximum(false);
                //this.setBounds(0,0,800,595);
            } catch(Exception ex) {
        
            }
        }
    }



    public void componentShown(ComponentEvent e) {}  
    
    

}