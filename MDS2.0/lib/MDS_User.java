/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;



public final class MDS_User {



    private static MDS_User usr = new MDS_User();


    private MDS_User() {}
    
    
    
    public static MDS_User getMDS_User() {
        return usr;
    }
    
    
    
//    public static void addInputListeners(Component c) {
//        c.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                MDS.getGlobalInputEventManager().fireGlobalInputEvent(e);
//            }
//        });    
//        c.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                MDS.getGlobalInputEventManager().fireGlobalInputEvent(e);
//                //Console.println("4234234");
//            }
//        });    
//    } 
    
    
    
    public void showAboutDialog(MDS_Frame parent, String title, ImageIcon i, String text) {
    
    
    
    
        class About extends MDS_Dialog implements ActionListener {
        
            
            
            JComponent contentPane; 
            JLabel lblIcon = new JLabel();
            MDS_TextArea  txtaDetails = new MDS_TextArea();
            JScrollPane scrlp = new JScrollPane(txtaDetails);
            ImageIcon icon;
            JButton btnOk = new JButton("Ok");
            MDS_Button btnAbout = new MDS_Button("About MDS");
            MDS_Frame parentFrame = null;
        
        
            public About(MDS_Frame parent, String title, ImageIcon i, String text) {
                super(parent, "About "+title);
                contentPane = (JComponent) this.getContentPane();
                contentPane.setLayout(null);                
                this.setSize(400, 400);
                this.setCenterScreen();
                /*
                this.addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameClosed(InternalFrameEvent e) {
                        MDS_UIManager.setParentComponent_Disabled(false, parentFrame.getContentPane());
                        MDS_UIManager.setParent_MenuBar_Disabled(false, parentFrame);                         
                    }});            
                
                parentFrame = parent;
                MDS_UIManager.setChildFrameLayer_EqualTo_Parent(this, parent);
                MDS_UIManager.setParentComponent_Disabled(true, parent.getContentPane());
                MDS_UIManager.setParent_MenuBar_Disabled(true, parent); 
                */
                lblIcon.setBounds(10, 10, 60, 60);
                icon = ImageManipulator.createScaledImageIcon(i,48 ,48 ,ImageManipulator.ICON_SCALE_TYPE);
                lblIcon.setIcon(icon);
                contentPane.add(lblIcon);
                txtaDetails.setBackground(UIManager.getColor("Label.background"));
                txtaDetails.setEditable(false);
                txtaDetails.setText(text);
                scrlp.setBounds(70, 10, 300, 300);
                contentPane.add(scrlp);
                btnOk.setBounds(310, 325, 60, 30);
                btnOk.addActionListener(this);
                contentPane.add(btnOk);
                btnAbout.setBounds(200, 325, 100, 30);
                btnAbout.addActionListener(this);
                contentPane.add(btnAbout);                
                //parent.setInternalFrameDialog(this);
                //this.setVisible(true);
                this.showDialog();
                //MDS_UIManager.halt_While_Visible(this);
                        
            }
            
            
            
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Ok")) {
                    this.dispose();
                    //MDS_UIManager.setParentComponent_Disabled(false, parentFrame.getContentPane());
                    //MDS_UIManager.setParent_MenuBar_Disabled(false, parentFrame); 
                } else if(e.getActionCommand().equals("About MDS")) {
                    MDS.About_MDS();
                }           
            }
            
            
            
        } 
               
        new About(parent, title, i, text);    

    } 
    
    
    
    public JMenuItem createMenuItem(String name ,ImageIcon icon ,ActionListener l) {
        JMenuItem mniTemp;
        mniTemp = new JMenuItem(name, icon);
        mniTemp.addActionListener(l);
        return mniTemp;
    }
    
    
    
    public JMenuItem createMenuItem(String name,ActionListener l) {
        JMenuItem mniTemp;
        mniTemp = new JMenuItem(name);       
        mniTemp.addActionListener(l);
        return mniTemp;
    }
    
    
    
    public JMenuItem createMenuItem(String name, ImageIcon icon, ActionListener l, KeyStroke ks, int mnemonic) {
        JMenuItem mniTemp = createMenuItem(name, icon, l);
        mniTemp.setAccelerator(ks);
        mniTemp.setMnemonic(mnemonic);
        return mniTemp;
    }    
    
    
    
    
    public JMenuItem createMenuItem(String name,ActionListener l, KeyStroke ks, int mnemonic) {
        JMenuItem mniTemp = createMenuItem(name, l);
        mniTemp.setAccelerator(ks);
        mniTemp.setMnemonic(mnemonic);
        return mniTemp;
    }      
    
    
    
    public JMenuItem createMenuItem(String name,ActionListener l, KeyStroke ks) {
        JMenuItem mniTemp = createMenuItem(name, l);
        mniTemp.setAccelerator(ks);
        return mniTemp;
    }          
    
    
    
    public JMenuItem createMenuItem(String name,ActionListener l, int mnemonic) {
        JMenuItem mniTemp = createMenuItem(name, l);
        mniTemp.setMnemonic(mnemonic);
        return mniTemp;
    } 
    
    
}    