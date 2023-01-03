/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;



public class ControlCenter extends MDS_Frame implements MouseListener, ActionListener {



    MDS_User usr = MDS.getUser();

    JComponent contentPane;
    JMenuBar mnbControlCenter = new JMenuBar();
    MDS_Menu mnuFile = new MDS_Menu("File", KeyEvent.VK_F);
    JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    JMenu mnuView = new JMenu("View");
    MDS_Menu mnuHelp = new MDS_Menu("Help", KeyEvent.VK_H);
    JMenuItem mniAbout = usr.createMenuItem("About", this, KeyEvent.VK_A);
    
    
    MDS_TableModel MDS_tbm;
    MDS_Table tblTools;


    public ControlCenter() {
        super("Control Center",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kcontrol.png"));  
        contentPane = (JComponent) this.getContentPane();
         
        contentPane.setLayout(new BorderLayout());
        
        initialize_Tools_Table();
        contentPane.add(new JScrollPane(tblTools), BorderLayout.CENTER);
        
        mnuFile.add(mniExit);
        mnbControlCenter.add(mnuFile);
        //mnbControlCenter.add(mnuView);
        mnuHelp.add(mniAbout);
        mnbControlCenter.add(mnuHelp);
        
        this.setJMenuBar(mnbControlCenter);
        this.setBounds(0, 0, 800, 600);
        this.setVisible(true);  
    }
    
    
    
    private void initialize_Tools_Table() {
        MDS_tbm = new MDS_TableModel();
        tblTools = new MDS_Table(MDS_tbm);
        tblTools.addMouseListener(this);
        
        MDS_tbm.addColumn("");
        MDS_tbm.addColumn("Name");
        MDS_tbm.addColumn("Description");
        
        tblTools.getColumn("").setMaxWidth(60);
        tblTools.getColumn("Name").setMinWidth(100);
        tblTools.getColumn("Name").setMaxWidth(100);
        tblTools.setRowHeight(60);
        
        Vector data = new Vector();
        
        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-samba.png"));
        data.addElement("Display");
        data.addElement("Change the appearance of your desktop, such as the backgrount, screen saver and theme.");
        
        MDS_tbm.addRow(data); 
        
        data = new Vector();

        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-action-equalizer.png"));
        data.addElement("Sound");
        data.addElement("Configure the setting for your speakers.");
        
        MDS_tbm.addRow(data);              
 
        data = new Vector();

        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-network.png"));
        data.addElement("Networking");
        data.addElement("Change the Network settings.");
        
        MDS_tbm.addRow(data);      
        
        data = new Vector();

        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-package_graphics.png"));
        data.addElement("Graphics");
        data.addElement("Sets the Rendering Hints");
        
        MDS_tbm.addRow(data);           
        
        data = new Vector();

        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-folder_crystal.png"));
        data.addElement("File Manager");
        data.addElement("Configure MDS file manager");
        
        MDS_tbm.addRow(data);      
        
        data = new Vector();

        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-kbounce.png"));
        data.addElement("Virtual Threading");
        data.addElement("Configure MDS Virtual Threading");
        
        MDS_tbm.addRow(data);    
        
        //data = new Vector();

        //data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"Exception-Manager.png"));
        //data.addElement("Exception");
        //data.addElement("");
        
        //MDS_tbm.addRow(data);                                
        
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){
        if(e.getClickCount() == 2) {
        ProcessManager prm = MDS.getProcessManager();
        String toolName = String.valueOf(tblTools.getValueAt(tblTools.getSelectedRow() , 1));  
            if(toolName.equals("Display")) {
                prm.execute(new File(MDS.getBinaryPath(),"DisplayProperties.class"));      
            } else if(toolName.equals("Sound")) {
                prm.execute(new File(MDS.getBinaryPath(),"SoundProperties"));
            } else if(toolName.equals("Graphics")) {
                prm.execute(new File(MDS.getBinaryPath(),"GraphicProperties"));
            } else if(toolName.equals("File Manager")) {
                prm.execute(new File(MDS.getBinaryPath(),"FileManagerProperties"));
            } else if(toolName.equals("Networking")) {
                prm.execute(new File(MDS.getBinaryPath(),"NetworkProperties"));
            } else if(toolName.equals("Virtual Threading")) {
                prm.execute(new File(MDS.getBinaryPath(),"VirtualThreadingManager"));
            }

        }
    }
        
        
        
    public void mouseReleased(MouseEvent e) {}       
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Exit")) {
            this.dispose();
        } else if(e.getActionCommand().equals("About")) {
            MDS.getUser().showAboutDialog(this, "Control Center", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"settings.png"), MDS.getAbout_Mahesh());
        }
    }             
    
    
    
    public static void MDS_Main(String arg[]) {
        new ControlCenter();
    }
    
    
    
}    