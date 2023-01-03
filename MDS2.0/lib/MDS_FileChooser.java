
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.util.logging.*;




public class MDS_FileChooser extends MDS_Panel implements MouseListener, MouseMotionListener, ActionListener, MDS_TextListener, ItemListener, FocusListener 
                                                          , FileSystemListener, DragSourceListener, DragGestureListener, DropTargetListener, MDS_FilePopupMenuListener {

    
    public static final int OPEN_DIALOG = 390434;
    public static final int SAVE_DIALOG = 54334;
    private int dialogType = -1;
    
    public static final int CANCEL_OPTION = 32;   
    public static final int APPROVE_OPTION = 442;
    public static final int ERROR_OPTION = 752;
    
    public static final String SYSTEM_ROOTS = "System Roots";
    private String currentLocation = SYSTEM_ROOTS; 
    private File currentFile = null;   
    private String selectedItem = null;
    private String shouldBeSelectedItem = null;
    private String suggestedFileName = null;
    
    private FileManager fm = MDS.getFileManager();
    private DiskDrives dd = new DiskDrives();
    private MDS_User usr = MDS.getUser();
    private DiskDriveManager ddm = MDS.getDiskDriveManager(); 
    private StringTable st = new StringTable();   
    
    private int closedOption = ERROR_OPTION;
    
    private MDS_Dialog dlg = null;
    
    private MDS_Panel pnlTop = new MDS_Panel(new BorderLayout());
    private MDS_ToolBar tlbTop = new MDS_ToolBar();
    private MDS_Button btnUp = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-up.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
    private MDS_Button btnRefresh = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-reload.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
    private MDS_Button btnNewFolder = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-fileopen.png"));
    private DefaultComboBoxModel dcbmLocation = new DefaultComboBoxModel();
    private MDS_ComboBox cboLocation = new MDS_ComboBox(dcbmLocation);    
    
    private MDS_Panel pnlCenter = new MDS_Panel(new BorderLayout());
    private MDS_ToolBar tlbShortcuts = new MDS_ToolBar(MDS_ToolBar.VERTICAL);
    private MDS_Button btnDiskDrives = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-hdd_unmount.png"));
    private MDS_Button btnHome = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-kfm_home.png"));
    private MDS_Button btnSystem = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-tux.png"));     
    private MDS_ListModel lstmdlContent = new MDS_ListModel();
    private MDS_List lstContent = new MDS_List(lstmdlContent);
    
    private MDS_Panel pnlBottum = new MDS_Panel(new BorderLayout());
    private MDS_Button btnOpen_Save = new MDS_Button("Open");
    private MDS_Button btnCancel = new MDS_Button("Cancel"); 
    private MDS_TextField txtfFileName = new MDS_TextField();
    private MDS_ComboBox cboFilter = new MDS_ComboBox();  
    
    private Vector vctSystemRoots = new Vector(); 
    private Vector vctFilter = new Vector();
    private String filter = "";
    private boolean showAllFiles = true;
    
    private PicturePreviewer ppv = new PicturePreviewer();
    private boolean picturePreviewerVisible = false;
    private DragIcon dragIcon = new DragIcon();
    
    private OperatingSystem os = OperatingSystem.getOperatingSystem();
    
    private Logger log = Logger.getLogger("MDS_FileChooser");
    
    private FileSystemListener fsl = this;
        

    public MDS_FileChooser(int dt) {
        super(new BorderLayout());
        dialogType = dt;
        File[] roots = fm.getRootDrives();
        for(int count = 0; count < roots.length; count++) {
            vctSystemRoots.addElement(roots[count].getPath());
        }          
        
        createTopPanel();
        this.add(pnlTop, BorderLayout.NORTH);      
        createCenterPanel(); 
        this.add(pnlCenter, BorderLayout.CENTER); 
        createBottumPanel();
        this.add(pnlBottum, BorderLayout.SOUTH); 
        
        //MDS.getFileSystemEventManager().addFileSystemListener(this);
        
        setLocation(SYSTEM_ROOTS); 
        
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(lstContent, DnDConstants.ACTION_COPY_OR_MOVE, this);         
        new DropTarget(lstContent, this); 
        		        
        
    }
    
    
    
    private void createTopPanel() {
        tlbTop.setFloatable(false);
        btnUp.setActionCommand("Up");
        btnUp.addActionListener(this);
        tlbTop.add(btnUp);
        btnRefresh.setActionCommand("Refresh");
        btnRefresh.addActionListener(this);
        tlbTop.add(btnRefresh);
        btnNewFolder.setActionCommand("New Folder");
        btnNewFolder.addActionListener(this);        
        tlbTop.add(btnNewFolder);
        tlbTop.addSeparator();         
        //cboLocation.setEditable(true);
        JScrollPane scrl = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cboLocation.setRenderer(new CboLocationRenderer());
        scrl.setViewportView(cboLocation); 
        tlbTop.add(scrl);       
        pnlTop.add(tlbTop, BorderLayout.CENTER);  
    }
    
    
    
    private void createCenterPanel() {
        tlbShortcuts.setFloatable(false);
        btnDiskDrives.setActionCommand("DiskDrives");
        btnDiskDrives.addActionListener(this);
        tlbShortcuts.add(btnDiskDrives);
        btnHome.addActionListener(this);
        btnHome.setActionCommand("Home");
        tlbShortcuts.add(btnHome);
        btnSystem.addActionListener(this);
        btnSystem.setActionCommand("System");
        tlbShortcuts.add(btnSystem);    
        pnlCenter.add(tlbShortcuts, BorderLayout.WEST);
        lstContent.addMouseMotionListener(this);
        lstContent.addMouseListener(this);
        lstContent.setVisibleRowCount(0);
        lstContent.setLayoutOrientation(JList.VERTICAL_WRAP);
        pnlCenter.add(new JScrollPane(lstContent), BorderLayout.CENTER); 
             	  
    }
    
    
    
    private void createBottumPanel() {
        MDS_Panel pnlLeft = new MDS_Panel(new GridLayout(2, 1));
        pnlLeft.add(new MDS_Label(" Name    "));
        pnlLeft.add(new MDS_Label(" Filter  "));
        pnlBottum.add(pnlLeft, BorderLayout.WEST);
        
        MDS_Panel pnlMiddle = new MDS_Panel(new GridLayout(2, 1));
        txtfFileName.addTextListener(this);
        txtfFileName.addFocusListener(this);
        pnlMiddle.add(txtfFileName);
        cboFilter.addItem("All Files");
        pnlMiddle.add(cboFilter);
        pnlBottum.add(pnlMiddle, BorderLayout.CENTER);
        
        MDS_Panel pnlButtons = new MDS_Panel(new GridLayout(2, 1));
        
        if(dialogType == SAVE_DIALOG) {
            btnOpen_Save.setText("Save");    
        }
        btnOpen_Save.addActionListener(this);
        pnlButtons.add(btnOpen_Save);
        btnCancel.addActionListener(this);
        pnlButtons.add(btnCancel);
        MDS_Panel pnlRight = new MDS_Panel(new BorderLayout());
        pnlRight.add(pnlButtons, BorderLayout.CENTER);        
        pnlRight.add(new MDS_Label("  "), BorderLayout.WEST);
        pnlBottum.add(pnlRight, BorderLayout.EAST);       
    }
    
    
    
    public int showDiaog(Component parentComponent) {
        
        MDS_Frame parentFrame = (MDS_Frame)parentComponent;
               
        String title = "";
        
        if(dialogType == SAVE_DIALOG) {
            title = "Save As";
        } else if(dialogType == OPEN_DIALOG) {
            title = "Open";
        }
        
        if(suggestedFileName != null) txtfFileName.setText(suggestedFileName);       
               
        dlg = new MDS_Dialog(parentFrame, title);
        JComponent contentPane = (JComponent) dlg.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        dlg.setSize(500, 340);
        dlg.setCenterScreen();
        
        dlg.setResizable(true);
        
        
        FileSystemEventManager.getFileSystemEventManager().addFileSystemListener(this);
          

        dlg.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
				FileSystemEventManager.getFileSystemEventManager().removeFileSystemListener(fsl);                     
            }
        });                   
                 
                 
        dlg.showDialog();
                                
        return closedOption;          
    }
    
    
    
    public void setFilter(Vector v) {
        for(int x=0;x<=v.size()-1;x++) {
            vctFilter.addElement(v.elementAt(x));
            filter = filter.concat( "*."+v.elementAt(x));
            showAllFiles = false;
        } 
        
        if(filter != null) {
            cboFilter.addItem(filter);
            cboFilter.setSelectedItem(filter); 
            cboFilter.addItemListener(this);      
        }  
    }
    
    
    
    public int getClosedOption() {
        return closedOption;
    }         
    
    
    
    public File getFile() {
        return currentFile; 
    }
    
    
    
    public String getPath() {
        return currentFile.getPath();
    }
    
    
    
    public void setSuggestedFileName(String name) {
        suggestedFileName = name;    
    }
    
    
    
    public void setPicturePreviewerVisible(boolean b) {
        if(b) {
            this.add(ppv, BorderLayout.EAST);
            picturePreviewerVisible = true;
        } else {
            this.remove(ppv);
            picturePreviewerVisible = false;
        }     
    }
    
    
    
    private void set_lstContent_SystemRoots() {
        
        try {
        
            if((DefaultListCellRenderer)lstContent.getCellRenderer() instanceof LstContentRenderer_SystemRoots){
            } else{     
                lstContent.setCellRenderer(new LstContentRenderer_SystemRoots());
            }             
            selectedItem = null;            
            lstmdlContent.removeAllElements();
            File[] rd = fm.getRootDrives();

            for(int x= 0; x < rd.length; x++) {
                lstmdlContent.addElement(rd[x].getPath());                                                                                                                                                                            
            }
                    
        } catch(Exception ex) {
            ex.printStackTrace();
        }
                        
    }
    
    
    
    private void setLocation(String loc) {
        if(loc.equals(SYSTEM_ROOTS)) {
            currentLocation = SYSTEM_ROOTS;
            btnNewFolder.setEnabled(false);
            set_lstContent_SystemRoots();    
            set_cboLocation(currentLocation);
        } else {
            if(loc.endsWith("\\")) {
                currentLocation = loc;
            } else {
                currentLocation = loc+"\\";
            }
            btnNewFolder.setEnabled(true);
            set_cboLocation(currentLocation);
            
            FileSystemEventManager.getFileSystemEventManager().setChangeNotificationDirectory(this, new File(currentLocation));
            
            set_lstContent(currentLocation);    
        }
        
        if(dialogType == SAVE_DIALOG) {
            btnOpen_Save.setText("Save"); 
            if(!txtfFileName.getText().equals("")) {
                File f = new File(txtfFileName.getText());
                if(!f.isDirectory()) {
                    txtfFileName.setText(f.getName());
                }    
            }           
        }
		
		
		//lstContent.validate();
        
    } 
    
    
    
    private void set_cboLocation(String loc) {
        int preLocIndex = dcbmLocation.getIndexOf(loc);
        if(preLocIndex != -1) {
            dcbmLocation.removeElementAt(preLocIndex);
        }
        
        dcbmLocation.addElement(loc);
        cboLocation.setSelectedItem(loc);  
    }   
    
    
    
    private void set_lstContent(String dirPath) {
    
        
        class LoadContent extends Thread {
        
        
        
            String path;
        
        
        
            public LoadContent(String dPath) {
                super();
                path = dPath;
              
                if((DefaultListCellRenderer)lstContent.getCellRenderer() instanceof LstContentRenderer_FileDirs){
                } else{ 
                    lstContent.setCellRenderer(new LstContentRenderer_FileDirs());    
                }      
                	
                lstContent.setCellRenderer(null); 

                
                this.start();
            }
            
            
            
            public void run() {
        
                //MDS_UIManager.setFrameCursor(mfb, new Cursor(Cursor.WAIT_CURSOR));
               
                long sTime = System.currentTimeMillis();
                int nDirs = 0;
                int nFiles = 0;    
                
                selectedItem = null;                
                lstmdlContent.removeAllElements();
                
//                if((DefaultListCellRenderer)lstContent.getCellRenderer() instanceof LstContentRenderer_FileDirs){
//                } else{ 
//                    lstContent.setCellRenderer(new LstContentRenderer_FileDirs());    
//                } 
                	
            
            	//lstContent.setVisible(false);
            
                try {
                    File[] fd = fm.getContent_Directories(path);
                    nDirs = fd.length;
                    for(int x= 0; x < fd.length; x++) {
                        lstmdlContent.addElement(fd[x].getName());
                    }
        
                    File[] f = fm.getContent_Files(path);
                    nFiles = f.length;  
                    for(int x= 0; x < f.length; x++) {
                        if(showAllFiles) {
                            lstmdlContent.addElement(f[x].getName());
                        } else if(vctFilter.contains(fm.getFileExtension(f[x].getName()))) {
                            lstmdlContent.addElement(f[x].getName());
                        }         
                    }
                    
                    double time = System.currentTimeMillis()-sTime;
                    
                    if(shouldBeSelectedItem != null) lstContent.setSelectedValue(shouldBeSelectedItem, true);
                    shouldBeSelectedItem = null;
                    //MDS_UIManager.setFrameCursor(mfb, new Cursor(Cursor.DEFAULT_CURSOR));
                           
                } catch(Exception ex) {
                    //MDS_UIManager.setFrameCursor(mfb, new Cursor(Cursor.DEFAULT_CURSOR));
                    lstContent.setVisible(true);
                    ex.printStackTrace();
                }
        		
        		lstContent.setCellRenderer(new LstContentRenderer_FileDirs());	
        		//lstContent.setVisible(true);
        		//lstContent.validate();
                 
            }
        
            
        }
        
        new LoadContent(dirPath);  
            
    } 
    
    
    
    private void lstContent_AddItem(File f) {
        if(f.isDirectory()) {
            lstmdlContent.addElement(f.getName());    
        } else {
            if(showAllFiles) {
                lstmdlContent.addElement(f.getName());
            } else if(vctFilter.contains(fm.getFileExtension(f.getName()))) {
                lstmdlContent.addElement(f.getName());
            }    
        } 
        
        lstContent.setSelectedValue(f.getName(), true);
                
    }
    
    
    
    private void lstContent_RemoveItem(File f) {
    	if(lstmdlContent.contains(f.getName()))
    		lstmdlContent.removeElement(f.getName()); 
//        if(f.isDirectory()) {
//            lstmdlContent.removeElement(f.getName());    
//        } else {
//            if(showAllFiles) {
//                lstmdlContent.removeElement(f.getName());
//            } else if(vctFilter.contains(fm.getFileExtension(f.getName()))) {
//                lstmdlContent.removeElement(f.getName());
//            }    
//        }        
    } 
    
    
    
    private void lstContent_EditItem(File f, File preF) {
        int index = lstmdlContent.indexOf(preF.getName());
    
        if(f.isDirectory()) {
            lstmdlContent.setElementAt(f.getName(), index);    
        } else {
            if(showAllFiles) {
                lstmdlContent.setElementAt(f.getName(), index);
            } else if(vctFilter.contains(fm.getFileExtension(f.getName()))) {
                lstmdlContent.setElementAt(f.getName(), index);
            }    
        } 
        
        lstContent.setSelectedValue(f.getName(), true);
                
    }       
    
    
    
    public void mouseClicked(MouseEvent e){
        int index = lstContent.locationToIndex(e.getPoint());
        lstContent.setSelectedIndex(index);
        String value = String.valueOf(lstmdlContent.elementAt(index));
        selectedItem = value;
        
        if(currentLocation.equals(SYSTEM_ROOTS)) { 
            if(dialogType == SAVE_DIALOG) {
                if(selectedItem == null) {
                    btnOpen_Save.setText("Save");
                } else {
                    btnOpen_Save.setText("Open");
                }           
            }
        } else {
            File f = new File(currentLocation+value);
            if(dialogType == SAVE_DIALOG) {
                if(f.isDirectory()) {    
                    btnOpen_Save.setText("Open");
                } else {
                    btnOpen_Save.setText("Save");
                }         
            }
            if(!f.isDirectory()) {    
                txtfFileName.setText(f.getName());
                ppv.loadImage(f);
            }
        }         
        
        if(e.getButton() == e.BUTTON1) {
            if(e.getClickCount() == 2) {
                if(currentLocation.equals(SYSTEM_ROOTS)) {
                    setLocation(value);
                } else {
                    File f = new File(currentLocation+value);
                    if(f.isDirectory()) {
                        setLocation(f.getPath());
                    } else {
                        if(dialogType == SAVE_DIALOG) {
                            MDS_OptionPane.showMessageDialog(dlg, "SAVE 1  \n", dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);                                                    
                        } else {
                            currentFile = f;
                            closedOption = APPROVE_OPTION;
                            dlg.dispose();
                        }                         
                    }    
                }
            }
        } else if(e.getButton() == e.BUTTON3) {
            if(e.getClickCount() == 1) {
                if(currentLocation.equals(SYSTEM_ROOTS)) { 
                    ddm.showDrivePopupMenu(this, lstContent, e.getX(), e.getY(), new File(selectedItem));    
                } else {
                    String loc = currentLocation;
                    if(!loc.endsWith("\\")) {
                        loc = loc+"\\";
                    }
                    fm.showFilePopupMenu(this, lstContent, e.getX(), e.getY(), new File(loc+selectedItem)); 
                }
            }    
        }                            
    }



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){}
        
        
        
    public void mouseReleased(MouseEvent e) {}
    
    
    
    public void mouseDragged(MouseEvent e) {}
    
    
    
    public void mouseMoved(MouseEvent e) {
    /*
        int index = lstContent.locationToIndex(new Point(e.getY(), e.getX()));
        File f = new File(currentLocation, String.valueOf(lstmdlContent.elementAt(index)));
        Console.println(f.getPath());
        if(f.isDirectory()) {
                 
        } else {
            lstContent.setToolTipText("<html>Type : "+f.getName()+"<p>Last Modified : "+fm.getLastModified_As_String(f.lastModified())+"<p>Size : "+fm.getFormatedFileSize(f.length())+"</html>");  
        } 
    */     
    }  

    
    
    public void actionPerformed(ActionEvent e) {
    
        //Max f10, ok2;
    
        if(e.getActionCommand().equals("Up")) {
            File f1 = new File(currentLocation);
            String newloc = null;        
            
            if(vctSystemRoots.contains(f1.getPath())) {
                setLocation(SYSTEM_ROOTS);                
            } else {    
                if(!currentLocation.equals(SYSTEM_ROOTS)) {            
                    newloc = f1.getParent();                          
                    if(!vctSystemRoots.contains(newloc)) {
                        newloc = newloc.concat("\\");
                    }                                               
                    setLocation(newloc);
                }          
            }
        } else if(e.getActionCommand().equals("Open")) {
            if(currentLocation.equals(SYSTEM_ROOTS)) {
                if(!txtfFileName.getText().equals("")) {
                    boolean ok2 = false;
                    if(selectedItem != null) ok2 = true;
                        if(ok2) {          
                            setLocation(selectedItem);    
                        } else {                  
                            File f2 = new File(txtfFileName.getText()); 
                            // ## VERIFY  ##
                            if(!fm.isLegalFileName(f2)) {
                                MDS_OptionPane.showMessageDialog(dlg, st.get(6), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE); 
                                return;
                            }                              
                            if(f2.exists()) {
                                if(f2.isDirectory()) {
                                    setLocation(f2.getPath());   
                                } else {
                                    currentFile = f2;
                                    closedOption = APPROVE_OPTION;
                                    dlg.dispose();                             
                                }
                            } else {
                                MDS_OptionPane.showMessageDialog(dlg, txtfFileName.getText()+"\n"+st.get(1)+"\n"+st.get(2), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);                        
                            }
                        }    
                } else if(selectedItem != null) {
                    setLocation(selectedItem);    
                }       
            } else { 
                if(!txtfFileName.getText().equals("")) {
                    boolean ok1 = false;
                    if(selectedItem != null) ok1 = true;
                        if(ok1) {
                            File f6 = new File(currentLocation, selectedItem);
                            if(f6.isDirectory()) {
                                setLocation(f6.getPath());    
                            } else {
                                currentFile = f6;
                                closedOption = APPROVE_OPTION;
                                dlg.dispose();                                 
                            }
                        } else {
                            File f3 = new File(currentLocation, txtfFileName.getText()); 
                            // ## VERIFY  ##
                            if(!fm.isLegalFileName(f3)) {
                                MDS_OptionPane.showMessageDialog(dlg, st.get(6), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE); 
                                return;
                            }                              
                            if(f3.exists()) {
                                if(f3.isDirectory()) {
                                    setLocation(f3.getPath());
                                } else {
                                    currentFile = f3;
                                    closedOption = APPROVE_OPTION;
                                    dlg.dispose();                                 
                                }  
                            } else {                     
                                File f4 = new File(txtfFileName.getText());
                                // ## VERIFY  ##
                                if(!fm.isLegalFileName(f4)) {
                                    MDS_OptionPane.showMessageDialog(dlg, st.get(6), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE); 
                                    return;
                                }                                
                                if(f4.exists()) {
                                    if(f4.isDirectory()) {
                                        setLocation(f4.getPath());
                                    } else {
                                        currentFile = f4;
                                        closedOption = APPROVE_OPTION;
                                        dlg.dispose();
                                    }                           
                                } else {
                                    MDS_OptionPane.showMessageDialog(dlg, txtfFileName.getText()+"\n"+st.get(1)+"\n"+st.get(2), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }    
                } else {
                    // ***** ERROR
                    if(selectedItem == null) return;
                    File f5 = new File(currentLocation, selectedItem);
                    if(f5.isDirectory()) {
                        setLocation(f5.getPath());    
                    } else {
                        lstContent.clearSelection();
                        selectedItem = null;
                    }
                }        
            }
        } else if(e.getActionCommand().equals("Save")) {
            if(currentLocation.equals(SYSTEM_ROOTS)) {
                // ***** ERROR
                if(!txtfFileName.getText().equals("")) {
                    File f7 = new File(txtfFileName.getText());
                    // ## VERIFY  ##
                    if(!fm.isLegalFileName(f7)) {
                        MDS_OptionPane.showMessageDialog(dlg, st.get(6), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE); 
                        return;
                    }                      
                    if(f7.exists()) {
                        if(f7.isDirectory()) {
                            setLocation(f7.getPath());
                        } else {
                            MDS_OptionPane.showMessageDialog(dlg, "SAVE 2 \n", dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);                                                       
                        }    
                    } else {
                        if(!f7.isDirectory()) {
                            // ***** SAVE 3
                            MDS_OptionPane.showMessageDialog(dlg, st.get(3), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);                      
                        } else {
                            MDS_OptionPane.showMessageDialog(dlg, st.get(3), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);  
                        }
                    }
                }    
            } else {
                // ***** ERROR
                if(!txtfFileName.getText().equals("")) {
                    File f8 = new File(txtfFileName.getText());
                    // ## VERIFY  ##  
                    if(!fm.isLegalFileName(f8)) {
                        MDS_OptionPane.showMessageDialog(dlg, st.get(6), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE); 
                        return;
                    }    
                    if(f8.exists()) {
                        if(f8.isDirectory()) {
                            setLocation(f8.getPath());
                        } else {
                            MDS_OptionPane.showMessageDialog(dlg, "SAVE 4 \n", dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);                                                         
                        }    
                    } else {
                        if(!f8.isDirectory()) {
                            // ***** SAVE 5
                            if(!txtfFileName.getText().equals("")) {
                                File f10 = new File(currentLocation, f8.getName());
                                if(f10.exists()) {
                                    int r = MDS_OptionPane.showConfirmDialog(dlg, f10.getName()+" "+st.get(7)+"\n"+st.get(8), dlg.getTitle(), JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);    
                                    if(r == JOptionPane.YES_OPTION) {
                                        if(f10.delete()) {
                                            currentFile = f10;
                                            closedOption = APPROVE_OPTION;
                                            dlg.dispose();                                      
                                        } else {
                                            MDS_OptionPane.showMessageDialog(dlg, st.get(9)+" "+f10.getName()+": "+st.get(10)+"\n"+st.get(11), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE); 
                                        }   
                                    }
                                } else {
                                    currentFile = f10;
                                    closedOption = APPROVE_OPTION;
                                    dlg.dispose();
                                }                                
                            }
                        } else {
                            MDS_OptionPane.showMessageDialog(dlg, st.get(3), dlg.getTitle(), JOptionPane.INFORMATION_MESSAGE);  
                        }
                    }                      
                }        
            }    
        } else if(e.getActionCommand().equals("Cancel")) {
            currentFile = null;
            closedOption = CANCEL_OPTION;
            dlg.dispose();            
        } else if(e.getActionCommand().equals("DiskDrives")) {
            setLocation(SYSTEM_ROOTS);
        } else if(e.getActionCommand().equals("Home")) {
            setLocation(System.getProperty("user.home")+"\\");
        } else if(e.getActionCommand().equals("System")) {
            setLocation(System.getProperty("user.dir")+"\\");    
        } else if(e.getActionCommand().equals("New Folder")) {
             try {
                 FileSystemView fsv = FileSystemView.getFileSystemView();
                 File f9 = fsv.createNewFolder(new File(currentLocation));
////                 MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f9));
             } catch (Exception ex) {
                 ex.printStackTrace();
                 MDS_OptionPane.showMessageDialog(dlg, st.get(5), "File Chooser", JOptionPane.ERROR_MESSAGE); 
             }
        } else if(e.getActionCommand().equals("Refresh")) {
            setLocation(currentLocation);
        }   
    } 
    
    
    
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==cboFilter) {
            if(e.getStateChange() == ItemEvent.SELECTED) {    
                if(cboFilter.getSelectedItem().equals(filter)) {
                    showAllFiles = false;
                } else {
                    showAllFiles = true;
                }  
                setLocation(currentLocation);               
            }
        }           
    }
    
    
    
    public void textChanged(MDS_TextEvent e) {}  
    
    
    
    public void focusGained(FocusEvent e) {
        if(dialogType == SAVE_DIALOG) {
            btnOpen_Save.setText("Save");    
        }       
    }
    
    
    
    public void focusLost(FocusEvent e) {}  
    
    
    
    
    public void fileSystemUpdated(FileSystemEvent e) {
    	log.info("fileSystemUpdated");
        File oldFile = e.getOldFile();
        File newFile = e.getNewFile();
    
////        if(e.getType() == e.REFRESH) {
////            if(!currentLocation.equals(SYSTEM_ROOTS)) {
////                setLocation(currentLocation);
////            }  
////        } else 
        //if(e.getFile() != null) {     
//            if(fm.getFilePathOnly(e.getPathF()).equals(currentLocation)) {                 
                if(e.getType() == e.FILE_DELETED) {
                	log.info("FILE_DELETED oldFile : "+oldFile.getPath()); 
                    lstContent_RemoveItem(oldFile);        
                } else if(e.getType() == e.FILE_CREATED) {
                	log.info("FILE_CREATED"); 
                    lstContent_AddItem(newFile);     
                } else if(e.getType() == e.FILE_RENAMED) {
                	log.info("FILE_RENAMED"); 
                    lstContent_EditItem(newFile, oldFile);    
                }
//            }
        //}        
    }   
    
    
    
    public void openFile(File f) {
        if(f.isDirectory()) {
            setLocation(f.getPath());    
        } else {
            fm.executeFile(f.getPath());
        }    
    }
    
    
    
    public MDS_Frame getListener__MDS_Frame() {
        return (MDS_Frame)dlg;
    } 
    
    
    
    public void dragGestureRecognized(DragGestureEvent event) {
        //Console.println("dragGestureRecognized");
        
        if(!currentLocation.equals(SYSTEM_ROOTS)) {
            String draggedValue;
            /*
            if(currentPath.charAt(currentPath.length()-1)=='\\') {
                draggedValue = currentPath+(String)vctContent.elementAt(lstContent.getSelectedIndex());    
            } else {
                draggedValue = currentPath+"\\"+(String)vctContent.elementAt(lstContent.getSelectedIndex());  
            }*/
            
            draggedValue = currentLocation+(String)lstContent.getSelectedValue();
            
            Transferable transferable = new MDS_FileTransferable(draggedValue);
            event.startDrag(null, transferable, this);                    
            File f = new File(draggedValue);
            if(f.isDirectory()) {
                dragIcon.showDragIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_24x24)); 
            } else {
                dragIcon.showDragIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_24x24));    
            }
        }
        
    }   
       
        
        
    public void dragEnter(DragSourceDragEvent event) {}
	
	
	
    public void dragOver(DragSourceDragEvent e) {
        lstContent.clearSelection();
    }
	
	
	
    public void dragExit(DragSourceEvent event) {}
	
	
	
    public void dropActionChanged(DragSourceDragEvent event) {}



    public void dragDropEnd(DragSourceDropEvent event) {
        dragIcon.hideDragIcon();
        MDS.getBaseWindow().getDesktop().repaint();
    }
    
    
    //====================================              
    
    
    public void dragEnter(DropTargetDragEvent dtde) {}
    
    
    
    public void dragExit(DropTargetEvent e) {}
    
    
    
    public void dragOver(DropTargetDragEvent e) {}
    
    
    
    public void drop(DropTargetDropEvent e) {
       
        try { 
            Transferable transferable = e.getTransferable();
            File f = new File(String.valueOf(transferable.getTransferData(DataFlavor.stringFlavor)));
            if(f.exists()) {
                 
                if(currentLocation.equals(fm.getFilePathOnly(f.getPath()))) {
                    e.rejectDrop();
                    MDS_OptionPane.showMessageDialog(dlg, "Cannot move '"+f.getName()+"' The source and the destination file paths are the same.", "Error Moving File or Directory", JOptionPane.ERROR_MESSAGE);                  
                } else { 
                
                    //Console.println("Scr  "+f.getPath());   
                    //Console.println("Des  "+currentLocation+f.getName());
                    
                    e.dropComplete(true);
                   
                    if(MDS_OptionPane.showConfirmDialog(dlg, "Are you sure you want to copy '"+ f.getPath()+"'\n\n"+"Source : "+f.getPath()+"\nDestination"+currentLocation+f.getName()+"\n\n", "File Chooser [Confirm File Copy]", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if(f.isDirectory()) {
                            MDS.getFileManager().copyFile(f, new File(currentLocation), true);
                        } else {
                            MDS.getFileManager().copyFile(f, new File(currentLocation, f.getName()), true);    
                        }                     
                    }
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public void dropActionChanged(DropTargetDragEvent dtde) {} 
    	
    	
    	
	            
    
    
    
    
    
    class LstContentRenderer_SystemRoots extends DefaultListCellRenderer {
    
    
    
        public LstContentRenderer_SystemRoots() {
            setOpaque(true);
        }
        
        
        
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)  {
	      String text = String.valueOf(value);

            switch(os.getDriveType(text)) {
                case OperatingSystem.DRIVE_UNKNOWN: 
                    setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "root-unknown.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));    
                    break;
                case OperatingSystem.DRIVE_NO_ROOT_DIR:
                    setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "64-device-nfs_unmount.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
                	break;
                case OperatingSystem.DRIVE_REMOVABLE:
                    FileSystemView fsv = FileSystemView.getFileSystemView(); 
                    if(fsv.isFloppyDrive(new File(text))) { 
                        setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-device-3floppy_unmount.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
                    } else {
                        setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "64-device-usbpendrivet.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE)); 
                    }              
                    break;
                case OperatingSystem.DRIVE_FIXED:
                    setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-device-hdd_unmount.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
                    this.setToolTipText(text);
                    break;
                case OperatingSystem.DRIVE_REMOTE:
                    setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "64-device-nfs_unmount.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
                    break;
                case OperatingSystem.DRIVE_CDROM:
                    setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "64-device-cdrom_unmount.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
                    break;
                case OperatingSystem.DRIVE_RAMDISK:
                    setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "jazdisk.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));  
                    break;        
                default:
                                
            }
	      
	        this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 12));
	        this.setText(text);
	        //this.setToolTipText(text);
            
            if(!isSelected) {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            } else {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            
	      return this;
	      
	  }        
    
    }
    
    
    
    
    
    class LstContentRenderer_FileDirs extends DefaultListCellRenderer {
    
    
    
        public LstContentRenderer_FileDirs() {
            setOpaque(true);
        }
        
        
        
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)  {
	        String text = String.valueOf(value);

            File f = new File(currentLocation+text);

			
            //setIcon(ImageManipulator.createScaledImageIcon(fm.getFileType_Icon(f), 24, 24, ImageManipulator.ICON_SCALE_TYPE));
	        
	        //setIcon(ImageManipulator.createScaledImageIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f), 24, 24, ImageManipulator.ICON_SCALE_TYPE));
	        setIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_24x24));
	        
	        this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 12));
	        this.setText(text);
	        //this.setToolTipText(text);
        
            if(!isSelected) {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            } else {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            
	      return this;
	      
	    }        
    
    }
    
    
    
    
    
    private class CboLocationRenderer extends DefaultListCellRenderer {
    
    
    
        public CboLocationRenderer() {
            setOpaque(true);
        }
        
        
        
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)  {
	      String text = String.valueOf(value);

            if(text.equals(SYSTEM_ROOTS)) {
                setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-hdd_unmount.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));  
                setText(text);  
            } else {
                setIcon(ImageManipulator.createScaledImageIcon(fm.getDefault_Directory_Icon(), 16, 16, ImageManipulator.ICON_SCALE_TYPE));  
                setText(text);                
            } 
	      //this.setToolTipText(text);
        
            if(!isSelected) {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            } else {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            
	      return this;
	  }    
	      
    }
    
    
    
    
    
    class PicturePreviewer extends MDS_Panel {
    
    
    
        ImageIcon thumbnail = null;
        
        
        
        public PicturePreviewer() {
            this.setPreferredSize(new Dimension(100, 50));
            setBorder(new BevelBorder(BevelBorder.LOWERED));
        }
        
        
        
        public void loadImage(File f) {
            if (f == null) {
                thumbnail = null;
            } else {
               ImageIcon tmpIcon = new ImageIcon(f.getPath());
               if(tmpIcon.getIconWidth() > 90) {
	                  thumbnail = new ImageIcon(
                  tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
               } else {
                  thumbnail = tmpIcon;
              }   
              repaint(); 
           }		             
        } 
        
        
        
        public void paint(Graphics g) {
           super.paint(g);
           if(thumbnail != null) {
               int x = getWidth()/2 - thumbnail.getIconWidth()/2;
               int y = getHeight()/2 - thumbnail.getIconHeight()/2;
               if(y < 0) {
                  y = 0;
               }
   
               if(x < 5) {
                  x = 5;
               }
               thumbnail.paintIcon(this, g, x, y);
           }
        }                       
        
        
    }        
	  
       	          
        
        
    
    
      
                                  
}