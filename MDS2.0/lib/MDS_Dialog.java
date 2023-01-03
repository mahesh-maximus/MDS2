/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;



public class MDS_Dialog extends MDS_Frame {



    private MDS_Frame ownerFrame;
    private MDS_Dialog me;
    private int ownerFrameDefaultCloseOperation = 0; 
    private boolean ownerFrameIsIconifiable; 
    private boolean ownerFrameIsMaximizable;
    private boolean ownerFrameIsResizable;
    private boolean ownerFrameIsClosable;
    
    private boolean isMenuBarEnabled;
    private boolean isOwnerEnabled;
    private boolean isHaltWhile_Visible  = true;



    public MDS_Dialog(MDS_Frame owner, String title) {
        super(title, false, true);    
        ownerFrame = owner;
        initialize();        
    }
    
    
    
    public MDS_Dialog(MDS_Frame owner, String title, boolean menuBarEnabled, boolean ownerEnabled) {
        super(title, false, true);
        ownerFrame = owner;
        //*********************************************
        isMenuBarEnabled = menuBarEnabled;
        isOwnerEnabled = ownerEnabled;
        isHaltWhile_Visible = true;        
        initialize();         
		//**********************************************
    }
    
    
    
    public MDS_Dialog(MDS_Dialog owner, String title) {
        super(title, false, true);
        ownerFrame = (MDS_Frame)owner;     
        initialize();        
    }
    
    
    
    private void initialize() {
        
        this.showInTaskBar(false);
        
        MDS.getBaseWindow().getDesktop().remove(this); 
        MDS.getBaseWindow().getDesktop().add(this, new Integer(ownerFrame.getLayer()));
                
        if(!isOwnerEnabled) {
            MDS_UIManager.setComponentTreeEnabled(false, ownerFrame.getContentPane());
        }
        
        if(!isMenuBarEnabled) {
            MDS_UIManager.setMenuBarEnabled(false, ownerFrame);        
        }
               
        ownerFrameDefaultCloseOperation = ownerFrame.getDefaultCloseOperation(); 
        ownerFrame.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);

        ownerFrameIsIconifiable = ownerFrame.isIconifiable();
        if(ownerFrameIsIconifiable) {
            ownerFrame.setIconifiable(false);
        }
        
        ownerFrameIsMaximizable = ownerFrame.isMaximizable();
        if(ownerFrameIsMaximizable) {
            ownerFrame.setMaximizable(false);
        }
        
        ownerFrameIsResizable = ownerFrame.isResizable();
        if(ownerFrameIsResizable) {
            ownerFrame.setResizable(false);
        }
        
        ownerFrameIsClosable = ownerFrame.isClosable();
        if(ownerFrameIsClosable) {
            ownerFrame.setClosable(false);
        }
        
        me = this;
        
        ownerFrame.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameActivated(InternalFrameEvent e) {
                //MDS.getBaseWindow().getDesktop().getDesktopManager().activateFrame(me);  
                try {
                    MDS.getBaseWindow().getDesktop().getDesktopManager().activateFrame(me); 
                    me.setIcon(false);
                    me.setVisible(true);
                    me.setSelected(true);
                } catch(Exception ex) {
                    //ex.printStackTrace();
                }                                       
            }}); 
            
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                MDS_UIManager.setComponentTreeEnabled(true, ownerFrame.getContentPane());
                MDS_UIManager.setMenuBarEnabled(true, ownerFrame); 
                ownerFrame.setDefaultCloseOperation(ownerFrameDefaultCloseOperation);   

                ownerFrame.setIconifiable(ownerFrameIsIconifiable);     
                ownerFrame.setMaximizable(ownerFrameIsMaximizable);                      
                ownerFrame.setResizable(ownerFrameIsResizable);               
                ownerFrame.setClosable(ownerFrameIsClosable);
                
                try {
                    ownerFrame.setSelected(true);
                } catch(Exception ex) {} 
                
                me = null; 
            }});    
            
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                me = null;
            }});                  
        
                                                          
    }
    
    
    
    public void setVisible(boolean b) {
        super.setVisible(b);       
        if(!b) {
            //me = null;
        }
    }
    
    
    
    public void showDialog() {  
        super.setVisible(true);             
        MDS_UIManager.halt_While_Visible(this);
    }
    
    
/*    
    public void disposeDialog() {
        super.setVisible(false);
    }
*/    
    
    
    public void dispose() {
        super.dispose();
        super.setVisible(false);
    }
    
    
    
}    