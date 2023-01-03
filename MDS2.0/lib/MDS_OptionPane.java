/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;



public final class MDS_OptionPane {



    public static int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE; 
    public static int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    public static int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public static int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;
    

    
    
    public MDS_OptionPane() {}
    
    
    
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        return showConfirmDialog(parentComponent, message, title, optionType, JOptionPane.QUESTION_MESSAGE);
    } 
    
    
    
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
        MDS_Frame opParentFrame = (MDS_Frame)parentComponent;
        int ownerFrameDefaultCloseOperation = opParentFrame.getDefaultCloseOperation();
        boolean ownerFrameIsIconifiable = opParentFrame.isIconifiable(); 
        boolean ownerFrameIsMaximizable = opParentFrame.isMaximizable();
        boolean ownerFrameIsResizable = opParentFrame.isResizable();
        boolean ownerFrameIsClosable = opParentFrame.isClosable();
        
        opParentFrame.setDefaultCloseOperation(opParentFrame.DO_NOTHING_ON_CLOSE);
                
        if(ownerFrameIsIconifiable) {
            opParentFrame.setIconifiable(false);
        }
        
        if(ownerFrameIsMaximizable) {
            opParentFrame.setMaximizable(false);
        }
        
        if(ownerFrameIsResizable) {
            opParentFrame.setResizable(false);
        }
        
        if(ownerFrameIsClosable) {
            opParentFrame.setClosable(false);
        }            
        
        JOptionPane op = new JOptionPane(message);
        op.setOptionType(optionType);
        op.setMessageType(messageType);
        //MDS.getSound().playSound(new File("Media\\Sound\\Notify.wav")); 
        playMessageTypeSound(messageType);
        JInternalFrame opFrame = op.createInternalFrame(parentComponent,title);      
        MDS.getBaseWindow().getDesktop().remove(opFrame); 
        MDS.getBaseWindow().getDesktop().add(opFrame, new Integer(opParentFrame.getLayer()));
        MDS_UIManager.setComponentTreeEnabled(false, opParentFrame.getContentPane());
        MDS_UIManager.setMenuBarEnabled(false, opParentFrame);
        ((MDS_Frame)parentComponent).setInternalFrameDialog(opFrame);
        //opParentFrame.setDefaultCloseOperation(MDS_Frame.DO_NOTHING_ON_CLOSE);
        opFrame.setVisible(true);
        MDS_UIManager.halt_While_Visible(opFrame);  
        MDS_UIManager.setComponentTreeEnabled(true, opParentFrame.getContentPane());
        MDS_UIManager.setMenuBarEnabled(true, opParentFrame);
        //opParentFrame.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE);
        
        opParentFrame.setIconifiable(ownerFrameIsIconifiable);     
        opParentFrame.setMaximizable(ownerFrameIsMaximizable);                      
        opParentFrame.setResizable(ownerFrameIsResizable);               
        opParentFrame.setClosable(ownerFrameIsClosable);
        opParentFrame.setDefaultCloseOperation(ownerFrameDefaultCloseOperation);
        
        try {
            opParentFrame.setSelected(true);
        } catch(Exception ex) {}          
        
        return Integer.parseInt(String.valueOf(op.getValue()));
       
    }    
    
    
    
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        
        MDS_Frame opParentFrame = (MDS_Frame)parentComponent;
        int ownerFrameDefaultCloseOperation = opParentFrame.getDefaultCloseOperation();
        boolean ownerFrameIsIconifiable = opParentFrame.isIconifiable(); 
        boolean ownerFrameIsMaximizable = opParentFrame.isMaximizable();
        boolean ownerFrameIsResizable = opParentFrame.isResizable();
        boolean ownerFrameIsClosable = opParentFrame.isClosable();
                    
        opParentFrame.setDefaultCloseOperation(opParentFrame.DO_NOTHING_ON_CLOSE);
                
        if(ownerFrameIsIconifiable) {
            opParentFrame.setIconifiable(false);
        }
        
        if(ownerFrameIsMaximizable) {
            opParentFrame.setMaximizable(false);
        }
        
        if(ownerFrameIsResizable) {
            opParentFrame.setResizable(false);
        }
        
        if(ownerFrameIsClosable) {
            opParentFrame.setClosable(false);
        }      
                
        JOptionPane op = new JOptionPane(message);

        op.setMessageType(messageType);
        playMessageTypeSound(messageType);
        JInternalFrame opFrame = op.createInternalFrame(parentComponent,title);      
        
        MDS.getBaseWindow().getDesktop().remove(opFrame); 
        MDS.getBaseWindow().getDesktop().add(opFrame, new Integer(opParentFrame.getLayer()));
        //MDS.getBaseWindow().getDesktop().add(opFrame,JLayeredPane.DEFAULT_LAYER);
        
        MDS_UIManager.setComponentTreeEnabled(false, opParentFrame.getContentPane());
        MDS_UIManager.setMenuBarEnabled(false, opParentFrame);
        ((MDS_Frame)parentComponent).setInternalFrameDialog(opFrame);
        //opParentFrame.setDefaultCloseOperation(MDS_Frame.DO_NOTHING_ON_CLOSE);
        opFrame.setVisible(true);
        MDS_UIManager.halt_While_Visible(opFrame);  
        MDS_UIManager.setComponentTreeEnabled(true, opParentFrame.getContentPane());
        MDS_UIManager.setMenuBarEnabled(true, opParentFrame);
        
        opParentFrame.setIconifiable(ownerFrameIsIconifiable);     
        opParentFrame.setMaximizable(ownerFrameIsMaximizable);                      
        opParentFrame.setResizable(ownerFrameIsResizable);               
        opParentFrame.setClosable(ownerFrameIsClosable);
        opParentFrame.setDefaultCloseOperation(ownerFrameDefaultCloseOperation);
        
        try {
            opParentFrame.setSelected(true);
        } catch(Exception ex) {}   
              
    }
    
    
    
    public static void showMessageDialog(Object message, String title, int messageType) {
        playMessageTypeSound(messageType);
        JOptionPane.showInternalMessageDialog(MDS.getBaseWindow().getDesktop(), message, title, messageType);
    }
    
    
    
    public static String showInputDialog(Component parentComponent, Object message, String title, int messageType) {        
        return showInputDialog(parentComponent, message, title, JOptionPane.OK_CANCEL_OPTION, messageType);          
    }
    
    
    
    public static String showInputDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
        return showInputDialog(parentComponent, message, title, optionType, messageType, false, false);     
    }
	
	
	
	public static String showInputDialog(Component parentComponent, Object message, String title, int optionType, int messageType, boolean menuBarEnabled, boolean ownerEnabled) {  
        String r = "";
        MDS_Frame opParentFrame = (MDS_Frame)parentComponent;
        int ownerFrameDefaultCloseOperation = opParentFrame.getDefaultCloseOperation(); 
        boolean ownerFrameIsIconifiable = opParentFrame.isIconifiable(); 
        boolean ownerFrameIsMaximizable = opParentFrame.isMaximizable();
        boolean ownerFrameIsResizable = opParentFrame.isResizable();
        boolean ownerFrameIsClosable = opParentFrame.isClosable();
        
        opParentFrame.setDefaultCloseOperation(opParentFrame.DO_NOTHING_ON_CLOSE);
                
        if(ownerFrameIsIconifiable) {
            opParentFrame.setIconifiable(false);
        }
        
        if(ownerFrameIsMaximizable) {
            opParentFrame.setMaximizable(false);
        }
        
        if(ownerFrameIsResizable) {
            opParentFrame.setResizable(false);
        }
        
        if(ownerFrameIsClosable) {
            opParentFrame.setClosable(false);
        }           
        
        JOptionPane op = new JOptionPane(message);
        op.setOptionType(optionType);
        op.setMessageType(messageType);
        op.setWantsInput(true);
        //MDS.getSound().playSound(new File("Media\\Sound\\Launch.wav")); 
        playMessageTypeSound(messageType);
        JInternalFrame opFrame = op.createInternalFrame(parentComponent,title);      
        MDS.getBaseWindow().getDesktop().remove(opFrame); 
        MDS.getBaseWindow().getDesktop().add(opFrame, new Integer(opParentFrame.getLayer()));
        MDS_UIManager.setComponentTreeEnabled(ownerEnabled, opParentFrame.getContentPane());
        MDS_UIManager.setMenuBarEnabled(menuBarEnabled, opParentFrame);
        ((MDS_Frame)parentComponent).setInternalFrameDialog(opFrame);
        //opParentFrame.setDefaultCloseOperation(MDS_Frame.DO_NOTHING_ON_CLOSE);
        opFrame.setVisible(true);
        MDS_UIManager.halt_While_Visible(opFrame);  
        r = String. valueOf(op.getInputValue());
        if (r.equals(JOptionPane.UNINITIALIZED_VALUE)) {
            r = null;
        }
        MDS_UIManager.setComponentTreeEnabled(true, opParentFrame.getContentPane());
        MDS_UIManager.setMenuBarEnabled(true, opParentFrame);
        //opParentFrame.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE);
        
        opParentFrame.setIconifiable(ownerFrameIsIconifiable);     
        opParentFrame.setMaximizable(ownerFrameIsMaximizable);                      
        opParentFrame.setResizable(ownerFrameIsResizable);               
        opParentFrame.setClosable(ownerFrameIsClosable);
        opParentFrame.setDefaultCloseOperation(ownerFrameDefaultCloseOperation);
        
        try {
            opParentFrame.setSelected(true);
        } catch(Exception ex) {}          
        
        return r;	
	}
    
    
    
    public static MDS_Frame getEmptyMessageFrame(Component parentComponent, Component com, String title, Dimension size) {


        
        class OpFrame extends MDS_Frame {
        
        
        
            MDS_Frame opParentFrame;
            int ownerFrameDefaultCloseOperation; 
            boolean ownerFrameIsIconifiable; 
            boolean ownerFrameIsMaximizable;
            boolean ownerFrameIsResizable;
            boolean ownerFrameIsClosable;            
        
        
        
            public OpFrame(Component parentComponent, Component com, String title, Dimension size) {
                super(title);
                opParentFrame = (MDS_Frame)parentComponent;
                ownerFrameDefaultCloseOperation = opParentFrame.getDefaultCloseOperation();
                ownerFrameIsIconifiable = opParentFrame.isIconifiable(); 
                ownerFrameIsMaximizable = opParentFrame.isMaximizable();
                ownerFrameIsResizable = opParentFrame.isResizable();
                ownerFrameIsClosable = opParentFrame.isClosable();
        
                opParentFrame.setDefaultCloseOperation(opParentFrame.DO_NOTHING_ON_CLOSE);
                
                if(ownerFrameIsIconifiable) {
                    opParentFrame.setIconifiable(false);
                }
        
                if(ownerFrameIsMaximizable) {
                    opParentFrame.setMaximizable(false);
                }
        
                if(ownerFrameIsResizable) {
                    opParentFrame.setResizable(false);
                }
        
                if(ownerFrameIsClosable) {
                    opParentFrame.setClosable(false);
                }           
                            
                MDS.getBaseWindow().getDesktop().remove(this); 
                MDS.getBaseWindow().getDesktop().add(this, new Integer(opParentFrame.getLayer()));
                MDS_UIManager.setComponentTreeEnabled(false, opParentFrame.getContentPane());
                MDS_UIManager.setMenuBarEnabled(false, opParentFrame);
                ((MDS_Frame)parentComponent).setInternalFrameDialog(this);
                //opParentFrame.setDefaultCloseOperation(MDS_Frame.DO_NOTHING_ON_CLOSE);                
                this.setSize(size);
                this.setCenterScreen();
                JComponent contentPane =  (JComponent) this.getContentPane();
                contentPane.setLayout(new BorderLayout());
                contentPane.add(com, BorderLayout.CENTER);
                this.setVisible(true);            
            } 
            
            
            
            public void dispose() {
                super.dispose();
                MDS_UIManager.setComponentTreeEnabled(true, opParentFrame.getContentPane());
                MDS_UIManager.setMenuBarEnabled(true, opParentFrame);
                //opParentFrame.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE);                                
                opParentFrame.setIconifiable(ownerFrameIsIconifiable);     
                opParentFrame.setMaximizable(ownerFrameIsMaximizable);                      
                opParentFrame.setResizable(ownerFrameIsResizable);               
                opParentFrame.setClosable(ownerFrameIsClosable);
                opParentFrame.setDefaultCloseOperation(ownerFrameDefaultCloseOperation);
        
                try {
                    opParentFrame.setSelected(true);
                } catch(Exception ex) {}              
            }
            
        }
        
            
                  
        return new OpFrame(parentComponent, com, title, size); 
               
    } 
    
    
    
    private static void playMessageTypeSound(int messageType) {
        if(messageType == JOptionPane.ERROR_MESSAGE) {
            MDS.getSound().playSound(new File("Media\\Sound\\Error.wav"));                      
        } else if(messageType == JOptionPane.INFORMATION_MESSAGE) {
            MDS.getSound().playSound(new File("Media\\Sound\\Information.wav")); 
        } else if(messageType == JOptionPane.WARNING_MESSAGE) {
            MDS.getSound().playSound(new File("Media\\Sound\\Notify.wav")); 
        } else if(messageType == JOptionPane.QUESTION_MESSAGE) {
            MDS.getSound().playSound(new File("Media\\Sound\\Information.wav")); 
        } else if(messageType == JOptionPane.PLAIN_MESSAGE) {
            MDS.getSound().playSound(new File("Media\\Sound\\Information.wav")); 
        }    
    } 
    
  
    

    
    
  
}