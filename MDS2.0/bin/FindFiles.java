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
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.util.logging.*;


public class FindFiles extends MDS_Frame implements Runnable, ActionListener, MouseListener, FileSystemListener, 
                                                    DragSourceListener, DragGestureListener, MDS_FilePopupMenuListener {



    JComponent contentPane;
    MDS_User usr = MDS.getUser();
    JMenuBar mnbFind = new JMenuBar();
    MDS_Menu mnuFile = new MDS_Menu("File", 'F');
    JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    MDS_Menu mnuHelp = new MDS_Menu("Help", 'H');
    JMenuItem mniAbout = usr.createMenuItem("About", this);
    
    MDS_TextField txtName = new MDS_TextField("*.*");
    MDS_TextField txtContains = new MDS_TextField();
    MDS_TextField txtLocation = new MDS_TextField("");
    
    MDS_Button btnFind_Stop = new MDS_Button("Find");
    MDS_Button btnBrowse = new MDS_Button("Browse");
    
    MDS_Label lblCurrentLocation = new MDS_Label("");
    MDS_Label lblStatus = new MDS_Label("");
    
    MDS_Table tblContent;
    MDS_TableModel tbmdl; 
    DefaultListSelectionModel dlsmdl;
    
    MDS_Dialog dlgRefresh;
    
    Thread thrdFind;  
    
    String searchLocation = ""; 
    
    boolean stop = false;
    
    int nFiles = 0;
    int nDirs = 0;
    
    FileManager fm = MDS.getFileManager();
    DragIcon dragIcon = new DragIcon();
    
    Logger log = Logger.getLogger("FindFiles");
   
    
    

    public FindFiles() {
        super("Find Files",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kfind.png"));        
        contentPane = (JComponent) this.getContentPane(); 
        contentPane.setLayout(new BorderLayout());
        
        mnuFile.add(mniExit);
        mnbFind.add(mnuFile);
        mnuHelp.add(mniAbout);
        mnbFind.add(mnuHelp);
        this.setJMenuBar(mnbFind);
        
        MDS_Panel pnlHolder = new MDS_Panel();
        pnlHolder.setLayout(new BorderLayout());
        
        MDS_Panel pnlInput = new MDS_Panel();
        pnlInput.setLayout(new GridLayout(3, 1));
        MDS_Panel pnlR1 = new MDS_Panel();
        pnlR1.setLayout(new BorderLayout());
        pnlR1.add(new MDS_Label("Name        "), BorderLayout.WEST);
        pnlR1.add(txtName, BorderLayout.CENTER);
        txtName.selectAll();
        
        MDS_Panel pnlR2 = new MDS_Panel();
        pnlR2.setLayout(new BorderLayout());
        pnlR2.add(new MDS_Label("Contains  "), BorderLayout.WEST);
        txtContains.setEditable(false);
        pnlR2.add(txtContains, BorderLayout.CENTER);       

        MDS_Panel pnlR3 = new MDS_Panel();
        pnlR3.setLayout(new BorderLayout());
        pnlR3.add(new MDS_Label("Location   "), BorderLayout.WEST);
        pnlR3.add(txtLocation, BorderLayout.CENTER);  
        
        MDS_Panel pnlButtons = new MDS_Panel();
        pnlButtons.setLayout(new GridLayout(2, 1));
        btnFind_Stop.addActionListener(this);
        pnlButtons.add(btnFind_Stop);
        btnBrowse.addActionListener(this);
        pnlButtons.add(btnBrowse);
                
        pnlInput.add(pnlR1); 
        pnlInput.add(pnlR2);
        pnlInput.add(pnlR3);
        
        pnlHolder.add(pnlInput, BorderLayout.CENTER);
        pnlHolder.add(pnlButtons, BorderLayout.EAST);
        pnlHolder.add(lblCurrentLocation, BorderLayout.SOUTH);
        contentPane.add(pnlHolder,BorderLayout.NORTH);
        
        tbmdl = new MDS_TableModel();         
        tblContent = new MDS_Table(tbmdl);
        dlsmdl = (DefaultListSelectionModel)tblContent.getSelectionModel(); 
        tblContent.addMouseListener(this);
        Vector vctColumns = new Vector();
        vctColumns.addElement("");
        vctColumns.addElement("Name");
        vctColumns.addElement("In Directory");
        vctColumns.addElement("Size");
        vctColumns.addElement("Type");
        vctColumns.addElement("Last Modified");
                
        tbmdl.setColumnIdentifiers(vctColumns);
            
        tblContent.setRowHeight(54);  
            
        tblContent.getColumn("").setMaxWidth(54);
            
        tblContent.getColumn("Size").setMaxWidth(60); 
        tblContent.getColumn("Size").setPreferredWidth(60);  
        
        contentPane.add(new JScrollPane(tblContent),BorderLayout.CENTER); 
        contentPane.add(lblStatus, BorderLayout.SOUTH);
        
        MDS.getFileSystemEventManager().addFileSystemListener(this);
        
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(tblContent, DnDConstants.ACTION_COPY_OR_MOVE, this);          
        
        this.setBounds(0, 0, 800, 600);
        this.setVisible(true);          
    }
    
    
    
    public FindFiles(String location) {
        this();
        txtLocation.setText(location);
    }
    
    
    
    public void run() {
        dive(searchLocation); 
        //find(new File(searchLocation));
        btnFind_Stop.setText("Find");
        btnBrowse.setEnabled(true);
        String status = "No of Dirs : "+String.valueOf(nDirs)+"  No of Files : "+String.valueOf(nFiles); 
        lblStatus.setText(status);
        if(dlgRefresh != null) {
            dlgRefresh.dispose();
        }
        tblContent.repaint();
    } 
    	
//    private void find(File f) {
//    	try {
//	    	HalfLifeClientManager hcm = HalfLifeClientManager.getHalfLifeClientManager();
//	    	int id = hcm.createHalfLifeClient_FindFile(f);
//	    	hc = hcm.getHalfLifeClient(id);
//	    	HL_TaskFindFile_Result rs = null;
//	    	
//	        while((rs = hc.readFindFileResult()) != null) {
//	        	if(!rs.isCompleted()) {    
//	        		identify(rs.getFile());	
//	        	} else {
//	        		break;
//	        	}
//	        }
//    	}catch(Exception ex) {
//    		ex.printStackTrace();
//    	}
//    		
//    }	
    
    
    
    public synchronized void dive(String name) {
        try {
            File file = new File(name);
            if (file != null && file.isDirectory()) {
                String files[] = file.list();
                for (int i = 0; i < files.length; i++) {
                    File leafFile = new File(file.getAbsolutePath(), files[i]);
                    if (leafFile.isDirectory()) {
                        identify(leafFile);   
                        lblStatus.setText(leafFile.getPath());
                        if(!stop) {
                            dive(leafFile.getAbsolutePath());
                        }
                        //System.out.println("Dir "+leafFile.getName());
                    } else {
                        //addSound(leafFile);
                        identify(leafFile);
                        //System.out.println(leafFile.getName());
                    }
                }
            } else if (file != null && file.exists()) {
                //addSound(file);
                identify(file);
                //System.out.println(file.getName());
            }
        } catch (SecurityException ex) {
            //System.out.println(ex.toString()); 
        } catch (Exception ex) {
            //System.out.println(ex.toString());
        }
    }
    
    
    
    private synchronized void identify(File f) {

        String search_extension = "";
        if(txtName.getText().trim().equals("*.*")) {
            if(!f.isDirectory()) {
                addToContent(f);
            }
        } else if(txtName.getText().trim().startsWith("*.")) {
            search_extension = txtName.getText().substring(2);
            if(!f.isDirectory()) {
                //System.out.println("1  "+search_extension+"  2      "+f.getName());     
                if(MDS.getFileManager().getFileExtension(f.getName()).equalsIgnoreCase(search_extension)) {
                    addToContent(f); 
                }        
            }    
        } else { 
            int len = txtName.getText().length();
            int fnLen = f.getName().length();
            int b = 0;
            int e = len;
            boolean found = false;
            
            while(e <= fnLen && !found) {
                if(f.getName().substring(b, e).equalsIgnoreCase(txtName.getText())) {
                    addToContent(f);
                    found = true;             
                }
                
                b++;
                e++;                
                
            }
              
        }
    }
    
    
    
    private boolean isLegalFile(File f) {
        boolean legal = false; 
        String search_extension = "";
        if(txtName.getText().trim().equals("*.*")) {
            if(!f.isDirectory()) {
                legal = true;
            }
        } else if(txtName.getText().trim().startsWith("*.")) {
            search_extension = txtName.getText().substring(2);
            if(!f.isDirectory()) {
                //System.out.println("1  "+search_extension+"  2      "+f.getName());     
                if(MDS.getFileManager().getFileExtension(f.getName()).equalsIgnoreCase(search_extension)) {
                    legal = true; 
                }        
            }    
        } else { 
            int len = txtName.getText().length();
            int fnLen = f.getName().length();
            int b = 0;
            int e = len;
            boolean found = false;
            
            while(e <= fnLen && !found) {
                if(f.getName().substring(b, e).equalsIgnoreCase(txtName.getText())) {
                    legal = true;
                    found = true;             
                }
                
                b++;
                e++;                
                
            }
              
        } 
        return legal;   
    } 
    
    
    
    private synchronized void addToContent(File f) {
    
        FileManager fm = MDS.getFileManager();
                
        Vector data = new Vector();
        //data.addElement(ImageManipulator.createScaledImage(fm.getFileType_Icon(f.getName()),31 ,35 ,ImageManipulator.ICON_SCALE_TYPE));
        //data.addElement("");
        data.addElement(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
        data.addElement(f.getName());
        data.addElement(fm.getFilePathOnly(f.getPath()));
        data.addElement(String.valueOf(MDS.getFileManager().getFormatedFileSize(f.length())));
        data.addElement(fm.getFileType(f.getName()));
        data.addElement(fm.getLastModified_As_String(f.lastModified()));                             
        nFiles = nFiles + 1;         
        tbmdl.addRow(data);       
    }
    
    
    
    private void set_tblContent_AddFile(File f) {
        
        if(f.isDirectory()) {
             
        } else {
            Vector data = new Vector();
            data.addElement(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
            data.addElement(f.getName());
            data.addElement(fm.getFilePathOnly(f.getPath()));
            data.addElement(String.valueOf(MDS.getFileManager().getFormatedFileSize(f.length())));
            data.addElement(fm.getFileType(f.getName()));
            data.addElement(fm.getLastModified_As_String(f.lastModified()));                             
                 
            tbmdl.addRow(data);                  
        }
    }
        
        
        
    private void set_tblContent_RemoveFile(File f) {
        int count = 0;
        while(count < tblContent.getRowCount()) {
            if(String.valueOf(tblContent.getValueAt(count, 1)).equals(f.getName())) {
                tbmdl.removeRow(count);                         
                break;
            }
            count++;
        } 
    }
        
        
        
    private void set_tblContent_EditFile(File f, File pf) {
        int count = 0;
         while(count < tblContent.getRowCount()) {
            if(String.valueOf(tblContent.getValueAt(count, 1)).equals(pf.getName())) {
                if(f.isDirectory()) {
               
                } else {
                    tbmdl.setValueAt(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48), count, 0);
                    tbmdl.setValueAt(f.getName(), count, 1);
                    tbmdl.setValueAt(fm.getFilePathOnly(f.getPath()), count, 2);
                    tbmdl.setValueAt(String.valueOf(MDS.getFileManager().getFormatedFileSize(f.length())), count, 3);
                    tbmdl.setValueAt(fm.getFileType(f.getName()), count, 4);
                    tbmdl.setValueAt(fm.getLastModified_As_String(f.lastModified()), count, 5);                                                 
                }
            break;
            }
            count++;
        }         
    }    
    
    
    
    private void find() {
        if(!txtName.getText().equals("") && !txtLocation.getText().equals("")) {
                
            File fLocation = new File(txtLocation.getText()); 
                
            if(!fLocation.exists()) {
                MDS_OptionPane.showMessageDialog(this, "Location does not exists.", "Find Files", JOptionPane.ERROR_MESSAGE);
                txtLocation.requestFocus();
                txtLocation.selectAll();
                return;
            }                
               
            if(!txtLocation.getText().endsWith("\\")) {
                txtLocation.setText(txtLocation.getText()+"\\");
            }
                             
            
            searchLocation = txtLocation.getText();                
               
            btnFind_Stop.setText("Stop");
            btnBrowse.setEnabled(false);
            Vector v = tbmdl.getDataVector();
            v.removeAllElements();    
            tblContent.clearSelection();  
            nFiles = 0; 
            nDirs = 0;             
            thrdFind = new Thread(this, "Find Files"); 
            thrdFind.start();  
        }      
    }    
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Find")) {
            /*
            if(!txtName.getText().equals("") && !txtLocation.getText().equals("")) {
                
                File fLocation = new File(txtLocation.getText()); 
                
                if(!fLocation.exists()) {
                    MDS_OptionPane.showMessageDialog(this, "Location does not exists.", "Find Files", JOptionPane.ERROR_MESSAGE);
                    txtLocation.requestFocus();
                    txtLocation.selectAll();
                    return;
                }                
                
                if(!txtLocation.getText().endsWith("\\")) {
                    txtLocation.setText(txtLocation.getText()+"\\");
                }
                             
            
                searchLocation = txtLocation.getText();                
                
                btnFind_Stop.setText("Stop");
                Vector v = tbmdl.getDataVector();
                v.removeAllElements();    
                tblContent.clearSelection();  
                nFiles = 0; 
                nDirs = 0;             
                thrdFind = new Thread(this); 
                thrdFind.start();
            } */
            
            find();
              
        } else if(e.getActionCommand().equals("Browse")) {
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this) ==  fmdc.APPROVE_OPTION) {
                //System.out.println(fmdc.getPath());
                searchLocation = fmdc.getPath();
                txtLocation.setText(searchLocation); 
            }            
        } else if(e.getActionCommand().equals("Stop")) {
            stop = true; 
            btnFind_Stop.setText("Find"); 
        } else if(e.getActionCommand().equals("Exit")) {
            this.dispose();
        } else if(e.getActionCommand().equals("About")) {
            MDS.getUser().showAboutDialog(this, "Find Files", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"search.png"), MDS.getAbout_Mahesh());
        }
    }
    
    
    
    public void mouseClicked(MouseEvent e) {	
        if(e.getButton() == e.BUTTON1) {
            if(e.getClickCount() == 2) {
                MDS.getFileManager().executeFile(String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow(), 2)) + String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow(), 1)));
            } 
        } else if(e.getButton() == e.BUTTON3) {
            int rowNo = tblContent.getRowForLocation(e.getY());
            dlsmdl.setSelectionInterval(rowNo, rowNo);
            MDS.getFileManager().showFilePopupMenu(this, tblContent, e.getX(), e.getY(), new File((String)tblContent.getValueAt(tblContent.getSelectedRow() , 2)+(String)tblContent.getValueAt(tblContent.getSelectedRow() , 1)));
        }    
    }
            
            
            
    public void mouseEntered(MouseEvent e) {}
            
            
            
    public void mouseExited(MouseEvent e) {}
            
            
            
    public void mousePressed(MouseEvent e) {}
            
            
            
    public void mouseReleased(MouseEvent e) {} 
    
    
    
    public void fileSystemUpdated(FileSystemEvent e) {
        
////        if(e.getType() == e.REFRESH && searchLocation.equals("")) return;
////     
////
////        if(MDS.getFileManager().getFilePathOnly(e.getFile().getPath()).startsWith(searchLocation)) {   
////            
////            if(!isLegalFile(e.getFile())) return;
////            
////            if(e.getActualType() == e.ABSTRACT_REMOVE_FILE) { 
////                set_tblContent_RemoveFile(e.getFile());
////            } else if(e.getActualType() == e.ABSTRACT_ADD_FILE) { 
////                set_tblContent_AddFile(e.getFile());
////            } else if(e.getActualType() == e.ABSTRACT_EDIT_FILE) {
////                set_tblContent_EditFile(e.getFile(), e.previousFile());
////            }                             
////        }      

    	log.info("fileSystemUpdated");
        File oldFile = e.getOldFile();
        File newFile = e.getNewFile();
        
        if(e.getType() == e.FILE_DELETED) {
        	log.info("FILE_DELETED oldFile : "+oldFile.getPath()); 
            set_tblContent_RemoveFile(oldFile);        
        } else if(e.getType() == e.FILE_CREATED) {
        	log.info("FILE_CREATED"); 
            set_tblContent_AddFile(newFile);     
        } else if(e.getType() == e.FILE_RENAMED) {
        	log.info("FILE_RENAMED"); 
            set_tblContent_EditFile(newFile, oldFile);    
        }        
               
    }
    
    
    
    public void openFile(File f) {
        MDS.getFileManager().executeFile(f.getPath());
    }
    
    
    
    public MDS_Frame getListener__MDS_Frame() {
        return this;
    }
    
    
    
    public void dragGestureRecognized(DragGestureEvent event) {        
        String draggedValue = String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() , 2)+"\\") + String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow(), 1));    
        Transferable transferable = new MDS_FileTransferable(draggedValue);
        event.startDrag(null, transferable, this);                    
        File f = new File(draggedValue);
        if(f.isDirectory()) {
            dragIcon.showDragIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48)); 
        } else {
            dragIcon.showDragIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48));    
        }
        
    }
       
        
        
    public void dragEnter(DragSourceDragEvent event) {
        //Console.println("dragEnter");
    }
	
	
	
    public void dragOver(DragSourceDragEvent event) {
        //Console.println("dragOver");
    }
	
	
	
    public void dragExit(DragSourceEvent event) {
        //Console.println("dragExit"); 
    }
	
	
	
    public void dropActionChanged(DragSourceDragEvent event) {
        //Console.println("dropActionChanged");
	}



    public void dragDropEnd(DragSourceDropEvent event) {
        //Console.println("dragDropEnd");
        dragIcon.hideDragIcon();
        MDS.getBaseWindow().getDesktop().repaint();
    }            
    
    
    
    public static void MDS_Main(String arg[]) { 
        if(arg.length == 1) {
            new FindFiles(arg[0]);
        } else {
            new FindFiles();
        }
    }
    
    
    
}    