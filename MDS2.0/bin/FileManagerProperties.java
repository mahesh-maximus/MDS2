/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;




public class FileManagerProperties extends ControlModule implements ChangeListener, ActionListener {



    private static FileManagerProperties fp;
    private static boolean fp_Visibility = false;
    
    MDS_TextField txfdBufferSize = new MDS_TextField("1024 kb");
    //MDS_List lstBufferSize; 
    MDS_Slider sldBufferSize = new MDS_Slider(512, 2048);
    MDS_CheckBox chkbNetDrives = new MDS_CheckBox("Do not detect network drives"); 
    MDS_CheckBox chkbHiddenFiles = new MDS_CheckBox("Do not show hidden files"); 


    public FileManagerProperties() {
        super("File Manager Properties", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_crystal.png"));
        MDS_Panel panel = new MDS_Panel(new BorderLayout());
        MDS_Panel pnlFileCopy = new MDS_Panel(new BorderLayout());
        pnlFileCopy.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"File copy buffer size"));
        /*
        Vector vctValues = new Vector();
        for(int x =512; x<=2048; x++) {
            vctValues.addElement(String.valueOf(x));
        }   
        lstBufferSize = new MDS_List(vctValues);
        JScrollPane scrl_lstBufferSize = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);                
        scrl_lstBufferSize.setViewportView(lstBufferSize);                      
        pnlFileCopy.add(scrl_lstBufferSize, BorderLayout.WEST);
        */
        
        MDS_Panel pnl1 = new MDS_Panel(new BorderLayout());
        txfdBufferSize.setEditable(false);
        pnl1.add(txfdBufferSize, BorderLayout.NORTH);
        pnlFileCopy.add(pnl1, BorderLayout.WEST);
        sldBufferSize.setPaintTicks(true);
        sldBufferSize.setPaintLabels(true);
        //sldBufferSize.setMinorTickSpacing(4);
        sldBufferSize.setMajorTickSpacing(512); 
        sldBufferSize.setName("BufferSize");  
        sldBufferSize.addChangeListener(this); 
        sldBufferSize.setValue(1024);       
        pnlFileCopy.add(sldBufferSize, BorderLayout.CENTER);
        panel.add(pnlFileCopy, BorderLayout.NORTH);
        
        MDS_Panel pnlFolderOptions = new MDS_Panel(new GridLayout(2, 1));
        pnlFolderOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Folder Options"));
        pnlFolderOptions.add(chkbNetDrives);
        pnlFolderOptions.add(chkbHiddenFiles);
        panel.add(pnlFolderOptions, BorderLayout.CENTER);
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                fp_Visibility = false;
                fp = null;                      
            }
        });        
        
        this.addPanel(panel);
        this.setSize(400, 220);
        this.setCenterScreen();
        this.setVisible(true); 
        
        fp_Visibility = true;
        fp = this;        
        
    }
    
    
    
    public void stateChanged(ChangeEvent e) {
        MDS_Slider sldEvt = (MDS_Slider)e.getSource();
        if(sldEvt.getName().equals("BufferSize")) {
            txfdBufferSize.setText(String.valueOf(sldEvt.getValue())+ " kb");
        }            
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Ok")) {
            this.dispose();
        }
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!fp_Visibility) {
            new FileManagerProperties();
        } else {
            try{
                FileManagerProperties.fp.setIcon(false);
                FileManagerProperties.fp.setSelected(true);
            } catch(Exception ex) {}
        }
    }      
    
    
    
}    