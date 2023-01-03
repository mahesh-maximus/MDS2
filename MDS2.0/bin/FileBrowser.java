/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
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




    
public class FileBrowser extends MDS_Frame implements MouseListener, TreeExpansionListener, KeyListener,
      ActionListener, TreeSelectionListener, TreeWillExpandListener, MenuListener, FileSystemListener, MouseMotionListener, 
      DragSourceListener, DragGestureListener, DropTargetListener, ListSelectionListener, ItemListener, MDS_FilePopupMenuListener {
      
      
    
    private MDS_User usr = MDS.getUser();
    private FileManager fm = MDS.getFileManager();
    private DiskDriveManager ddm = MDS.getDiskDriveManager();
    
    private JComponent contentPane; 
    private JMenuBar mnbFileBrowser = new JMenuBar();
    private MDS_Menu mnuFile = new MDS_Menu("File", KeyEvent.VK_F);
    private JMenuItem mniOpen = usr.createMenuItem("Open", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"open.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this, MDS_KeyStroke.getOpen(), KeyEvent.VK_O);
    private JMenuItem mniCompress = usr.createMenuItem("Compress", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds-zip.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniDecompress = usr.createMenuItem("Decompress", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds-zip.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenu mnuSendTo = new JMenu("Send To");
    private JMenuItem mniHomeDir = usr.createMenuItem("Home Directory", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"home.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniFloppy = usr.createMenuItem("3.5 Floppy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"fdd.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
            
    private JMenu mnuNew = new JMenu("New");
    private JMenuItem mniFolder = usr.createMenuItem("Folder", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"folder_blue.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniText = usr.createMenuItem("Text Document", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds-text.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniHTML = usr.createMenuItem("HTML Document", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds-text-html.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniBatch = usr.createMenuItem("MSDOS Batch File", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"msdos batch.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    
    
    private JMenuItem mniDelete = usr.createMenuItem("Delete", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"delete.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniRename = usr.createMenuItem("Rename", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"rename.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniProperties = usr.createMenuItem("Properties", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"file-properties.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniClose = usr.createMenuItem("Close", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"open.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);

    private MDS_Menu mnuEdit = new MDS_Menu("Edit", KeyEvent.VK_E);
    private JMenuItem mniCut = usr.createMenuItem("Cut", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"cut.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this, MDS_KeyStroke.getCopy(), KeyEvent.VK_X);
    private JMenuItem mniCopy = usr.createMenuItem("Copy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"copy.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this, MDS_KeyStroke.getCut(), KeyEvent.VK_C);
    private JMenuItem mniPaste = usr.createMenuItem("Paste", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"paste.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this, MDS_KeyStroke.getPaste(), KeyEvent.VK_V);
    private JMenuItem mniCopyTo = usr.createMenuItem("Copy To", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"copy to.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    private JMenuItem mniMoveTo = usr.createMenuItem("Move To", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"move to.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    
    private MDS_Menu mnuView = new MDS_Menu("View", KeyEvent.VK_V);
    private JMenuItem mniStatusBar = usr.createMenuItem("Status Bar", this);
    
    private MDS_Menu mnuTools = new MDS_Menu("Tools", KeyEvent.VK_T);
    private JMenuItem mniFind = usr.createMenuItem("Find Files", this, MDS_KeyStroke.getFind(), KeyEvent.VK_F);
    private JMenuItem mniStdo = usr.createMenuItem("Redirected StandardOutput Viewer", this);
    private JMenuItem mniClipBoard = usr.createMenuItem("Clip Board Viewer", this);
    
    private MDS_Menu mnuHelp = new MDS_Menu("Help", KeyEvent.VK_H);
    private JMenuItem mniAbout = usr.createMenuItem("About", this, KeyEvent.VK_A);

    private MDS_ToolBar tlbFileBrowser = new MDS_ToolBar();
    private MDS_Button btnBack;   
    private MDS_Button btnForword;        
    private MDS_Button btnUp;       
    private MDS_Button btnRefresh;           
    private MDS_Button btnHome;
    private MDS_Button btnCut;
    private MDS_Button btnCopy;
    private MDS_Button btnPaste;
    private MDS_Button btnPrint;
    private MDS_Button btnFind;   
    private MDS_Button btnDelete;
    
    private JSplitPane slpFileBrowser = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);       
    private MDS_Panel pnlLeft = new MDS_Panel();
    private MDS_Panel pntRight = new MDS_Panel();
    
    MDS_Tree treFolders;
    DefaultTreeModel dtmFolders;
    MDS_TreeCellRenderer tcRenderer;
    
    MDS_Label lblAddress = new MDS_Label("Address  ");
    MDS_ComboBox cboDirectory_Location = new MDS_ComboBox();
    MDS_Button btnGo = new MDS_Button(" Go >>"); 
    MDS_Label lblStatusBar = new MDS_Label();
    
    FB_Table tblContent;
    FB_TableModel FB_tm;
    DefaultListSelectionModel dlsmdl;
    
    final String DISK_DRIVES = "Disk Drives";
    
    String current_Directory_Location = DISK_DRIVES;
    File current_File = null;
    Vector vctVisitedLocations = new Vector();
    Vector vctDiskDrives = new Vector();
    
    FB_LocationQueue fb_lq = new FB_LocationQueue();
    
    
    MDS_Clipboard clipBoard = MDS.getClipboard(); 
    
    final String driveEmpty = "< Drive is Empty >";
    //MDS_Logo logo = new MDS_Logo();
    FileBrowser fb;
    DragIcon dragIcon = new DragIcon();
    
    OperatingSystem os = OperatingSystem.getOperatingSystem();
    
    Logger log = Logger.getLogger("FileBrowser");
    
    private FileSystemListener fsl = this;
    
    
    
    public FileBrowser() {    
        super("File Browser",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_crystal.png"));
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        MDS_Panel pnlToolBar_AddreesBar_Container = new MDS_Panel();
        pnlToolBar_AddreesBar_Container.setLayout(new BorderLayout());

        MDS_Panel pnlToolBar_Logo_Container = new MDS_Panel();
        
        pnlToolBar_Logo_Container.setLayout(new BorderLayout());
        
        create_tblFileBrowser_Buttons();
        
        cboDirectory_Location.addKeyListener(this);
        //cboDirectory_Location.addItemListener(this);
        cboDirectory_Location.setEditable(true);
        
        tlbFileBrowser.setFloatable(false);
        
        tlbFileBrowser.addSeparator();
        
        pnlToolBar_Logo_Container.add(tlbFileBrowser, BorderLayout.CENTER);
        
        pnlToolBar_Logo_Container.add(new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"MDS.png", 36, 36, ImageManipulator.ICON_SCALE_TYPE)), BorderLayout.EAST);
        //pnlToolBar_Logo_Container.add(logo, BorderLayout.EAST);

        
        pnlToolBar_AddreesBar_Container.add(pnlToolBar_Logo_Container, BorderLayout.NORTH);
        pnlToolBar_AddreesBar_Container.add(lblAddress, BorderLayout.WEST);
        pnlToolBar_AddreesBar_Container.add(cboDirectory_Location, BorderLayout.CENTER);
        btnGo.addActionListener(this);
        pnlToolBar_AddreesBar_Container.add(btnGo, BorderLayout.EAST);
        
        contentPane.add(pnlToolBar_AddreesBar_Container, BorderLayout.NORTH);
        
        slpFileBrowser.setDividerLocation(150);
        slpFileBrowser.setDividerSize(3);
        
        pnlLeft.setLayout(new BorderLayout());
        Initialize_FolderTreeViewer();
        pnlLeft.add(new JScrollPane(treFolders),BorderLayout.CENTER);
        slpFileBrowser.setLeftComponent(pnlLeft);
        
        initialize_tblContent();
        pntRight.setLayout(new BorderLayout());
        pntRight.add(new JScrollPane(tblContent), BorderLayout.CENTER); 
        slpFileBrowser.setRightComponent(pntRight);
        
        contentPane.add(slpFileBrowser, BorderLayout.CENTER);
        
        contentPane.add(lblStatusBar, BorderLayout.SOUTH);
        
        initialize_FileBrowser();
        
        mnuFile.add(mniOpen);
        mnuFile.add(mniCompress);
        mnuFile.addSeparator();
        
        mnuSendTo.add(mniHomeDir);
        mnuSendTo.add(mniFloppy);           
        mnuFile.add(mnuSendTo);
        mnuFile.addSeparator();
        
        mnuNew.add(mniFolder);
        mnuNew.add(mniText);
        mnuNew.add(mniHTML);
        mnuNew.add(mniBatch);
        mnuFile.add(mnuNew);
        
        mnuFile.addSeparator();
        mnuFile.add(mniDelete);
        mnuFile.add(mniRename);
        mnuFile.add(mniProperties);
        mnuFile.addSeparator();
        mnuFile.add(mniClose);
        mnuFile.addMenuListener(this);
        mnbFileBrowser.add(mnuFile);
        
        mnuEdit.add(mniCut);
        mnuEdit.add(mniCopy);
        mnuEdit.add(mniPaste);
        mnuEdit.add(mniCopyTo);
        mnuEdit.add(mniMoveTo);
        mnuEdit.addMenuListener(this);
        mnbFileBrowser.add(mnuEdit);
        
        mniStatusBar.setEnabled(false);
        mnuView.add(mniStatusBar);
        mnbFileBrowser.add(mnuView);
        
        mnuTools.add(mniFind);
        mnuTools.add(mniStdo);
        mnuTools.add(mniClipBoard);
        mnbFileBrowser.add(mnuTools);
        
        mnuHelp.add(mniAbout);
        mnbFileBrowser.add(mnuHelp);
                                                    
        this.setJMenuBar(mnbFileBrowser);
        this.setBounds(0,0,800,600);
        
        //this.addKeyListener(this);
        MDS.getFileSystemEventManager().addFileSystemListener(this);
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(tblContent, DnDConstants.ACTION_COPY_OR_MOVE, this);         
        
        new DropTarget(tblContent, this);   
        	
        FileSystemEventManager.getFileSystemEventManager().addFileSystemListener(this);	     
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
           		log.info("removeFileSystemListener");
				FileSystemEventManager.getFileSystemEventManager().removeFileSystemListener(fsl);	                  
            }
        });          
        
        this.setVisible(true);
        
        fb = this;
        
        updateMenuSystem();
        
        updateToolBar();        
        
    }
    
    
    
    public FileBrowser(File f) { 
        this();
        if(f.isDirectory()) {
            if(f.getPath().endsWith("\\")) {
                setCurrentLocation(f.getPath());
            } else {
                setCurrentLocation(f.getPath()+"\\");
            }    
        } else {
            setCurrentLocation(f.getParent()+"\\");
            set_tblContent_SelectedItem(f); 
        }    
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        if(arg.length == 1) {
            new FileBrowser(new File(arg[0]));
        } else {
            new FileBrowser();
        }    
    }    
    
    
    
    private void initialize_FileBrowser() {
        File[] roots = fm.getRootDrives();
        for(int count = 0; count < roots.length; count++) {
            vctDiskDrives.addElement(roots[count].getPath());
        }
                  
    }     
    
    
    
    private void create_tblFileBrowser_Buttons() {
    
        btnBack = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "left.png"));  
        btnBack.setActionCommand("Back");
        tlbFileBrowser.add(btnBack); 
        btnBack.addActionListener(this);    
        btnForword = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "right.png"));  
        btnForword.setActionCommand("Forword");
        btnForword.addActionListener(this);
        tlbFileBrowser.add(btnForword);     
        btnUp = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "up.png"));    
        btnUp.setActionCommand("Up");
        btnUp.addActionListener(this);
        tlbFileBrowser.add(btnUp);  
        btnHome = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"home.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnHome.setActionCommand("Home");
        btnHome.addActionListener(this);
        tlbFileBrowser.add(btnHome);               
        tlbFileBrowser.addSeparator();  
        btnRefresh = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "refresh.png"));
        btnRefresh.setActionCommand("Reshresh");
        btnRefresh.addActionListener(this);
        tlbFileBrowser.add(btnRefresh); 
        tlbFileBrowser.addSeparator();      
        btnCut = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"cut.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnCut.setActionCommand("Cut");
        btnCut.addActionListener(this);
        tlbFileBrowser.add(btnCut);
        btnCopy = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"copy.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnCopy.setActionCommand("Copy");
        btnCopy.addActionListener(this);
        tlbFileBrowser.add(btnCopy);  
        btnPaste = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"paste.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnPaste.setActionCommand("Paste");
        btnPaste.addActionListener(this);
        tlbFileBrowser.add(btnPaste);  
        btnDelete = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"delete.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnDelete.setActionCommand("Delete");
        btnDelete.addActionListener(this);
        tlbFileBrowser.add(btnDelete);   
        tlbFileBrowser.addSeparator();            
        btnPrint = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"print.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnPrint.setActionCommand("Print");
        btnPrint.addActionListener(this);
        tlbFileBrowser.add(btnPrint);                                                            
        btnFind = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "search.png"));     
        btnFind.setActionCommand("Find");
        btnFind.addActionListener(this);
        tlbFileBrowser.add(btnFind);      
        
    }
    
    
    
    private String convertTreePath_ToActualPath(Object[] nodes) {

        String path = "";

        for(int countNodes = 0; countNodes < nodes.length; countNodes++) {

            if (countNodes >= 1) {
                if(countNodes ==1) {
                    path = path.concat(String.valueOf(nodes[countNodes]));
                } else {
                    path = path.concat(String.valueOf(nodes[countNodes]+"\\"));
           
                }
            }

        }
    
        return path;
        	
   }        
    
    
    
    private void Initialize_FolderTreeViewer() {
        tcRenderer = new MDS_TreeCellRenderer();
        TreeNode root = create_FolderTree_Roots();
        dtmFolders = new DefaultTreeModel(root);
        treFolders = new MDS_Tree(dtmFolders);
        treFolders.setEditable(false);
                 
        tcRenderer.setClosedIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-fileopen.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        tcRenderer.setOpenIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-fileopen.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        tcRenderer.setLeafIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-fileopen.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        treFolders.setCellRenderer(tcRenderer);            
        
        
        treFolders.addMouseListener(this);
        treFolders.addTreeExpansionListener(this);
        treFolders.addTreeSelectionListener(this);
        treFolders.addTreeWillExpandListener(this);
        
    }
    
    
    
    private TreeNode create_FolderTree_Roots() {
    
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Disk Dirves");
        
        String[] rDrives = fm.getRootDrives_AsStrings();
        
        for(int x= 0; x < rDrives.length; x++) {
            DefaultMutableTreeNode  dmtn = new DefaultMutableTreeNode(rDrives[x]); 
            dmtn.add(new DefaultMutableTreeNode(driveEmpty));                                              
            root.add(dmtn);            
          }
        
        return root;

    }
    
    
    
    private void initialize_tblContent() {
        
        FB_tm = new FB_TableModel();         
        tblContent = new FB_Table(FB_tm);  
        tblContent.addMouseMotionListener(this);        
        tblContent.addKeyListener(this);
        tblContent.setRowHeight(55);
        tblContent.addMouseListener(this);
        tblContent.setAutoscrolls(true);

        dlsmdl = (DefaultListSelectionModel)tblContent.getSelectionModel(); 
        dlsmdl.addListSelectionListener(this);
        tblContent.setSelectionModel(dlsmdl);  
        
        FB_tm.addColumn("#");
        FB_tm.addColumn("##");
        FB_tm.addColumn("###");
        FB_tm.addColumn("####");
        FB_tm.addColumn("#####");


        settblContent_SystemRoots();
        
        fb_lq.addNewLocation("Disk Drives");
        
        set_cboDirectory_Location_Text(DISK_DRIVES);

    }
    
    
    
    private void settblContent_SystemRoots() {
    try {
        DiskDrives dd = new DiskDrives();
        
        removetblContent();

        set_tblContent_to_Fit_RootDrives();
                  
        File[] rd = fm.getRootDrives();

        for(int x= 0; x < rd.length; x++) {
            Vector data = new Vector();
             
             
            switch(os.getDriveType(rd[x].getPath())) {
                case OperatingSystem.DRIVE_UNKNOWN:
                    //Comment
                    data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-3floppy.png"));
                    data.addElement(rd[x].getPath());   
                    data.addElement(os.getVolumeName(rd[x].getPath())); 
                    data.addElement("Unknown Drive");   
                    data.addElement(os.getFileSystemName(rd[x].getPath()));     
                         
                    break;
                 case OperatingSystem.DRIVE_NO_ROOT_DIR:
                    //Comment
                    data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-3floppy.png"));
                    data.addElement(rd[x].getPath());     
                    data.addElement(os.getVolumeName(rd[x].getPath()));  
                    data.addElement(""); 
                    data.addElement(os.getFileSystemName(rd[x].getPath()));                  
                    
                    break;
                case OperatingSystem.DRIVE_REMOVABLE:
                    FileSystemView fsv = FileSystemView.getFileSystemView(); 
                    if(fsv.isFloppyDrive(rd[x])) { 
                        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-3floppy.png"));
                        data.addElement(rd[x].getPath());              
                        data.addElement(""); 
                        data.addElement("Floppy Drive");  
                        data.addElement("");     
                    } else {
                        data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-usbpendrivet.png"));
                        data.addElement(rd[x].getPath());              
                        data.addElement(""); 
                        data.addElement("Removable Drvie");  
                        data.addElement("");                          
                    }              
                    break;
                case OperatingSystem.DRIVE_FIXED:
                    data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-hdd.png"));
                    data.addElement(rd[x].getPath());
                    data.addElement(os.getVolumeName(rd[x].getPath())); 
                    data.addElement("Local Drive");
                    data.addElement(os.getFileSystemName(rd[x].getPath()));                                              
                    break;
                case OperatingSystem.DRIVE_REMOTE:
                    data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-nfs.png"));
                    data.addElement(rd[x].getPath());          
                    data.addElement(os.getVolumeName(rd[x].getPath())); 
                    data.addElement("Remote Drive");  
                    data.addElement(os.getFileSystemName(rd[x].getPath()));           
                    break;
                case OperatingSystem.DRIVE_CDROM:
                    data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-cdrom.png"));
                    data.addElement(rd[x].getPath());
                    data.addElement(os.getVolumeName(rd[x].getPath()));                       
                    data.addElement("CD Drive"); 
                    data.addElement(os.getFileSystemName(rd[x].getPath()));                                     
                    break;
                case OperatingSystem.DRIVE_RAMDISK:
                    data.addElement(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"jazdisk.png"));
                    data.addElement(rd[x].getPath());    
                    data.addElement(os.getVolumeName(rd[x].getPath()));     
                    data.addElement("RAM Disk");   
                    data.addElement(os.getFileSystemName(rd[x].getPath()));           
                    break;        
                default:
                    throw new RuntimeException("File Browser roots initialization error.");
                    //MDS.getExceptionManager().showException(new RuntimeException("MDS_FileChooser roots initialization error."));   
            
            }

            FB_tm.addRow(data);
            
            
            //fb_lq.addNewLocation("Disk Drives");
            
                                                                                                                                                                                 
        }
        
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    private void set_tblContent_to_Fit_RootDrives() {
        Vector vctColumns = new Vector();
        vctColumns.addElement("");
        vctColumns.addElement("Drive");
        vctColumns.addElement("Volume Label");
        vctColumns.addElement("Type");
        vctColumns.addElement("File System");
            
        FB_tm.setColumnIdentifiers(vctColumns);
        
        tblContent.setRowHeight(70); //60              
    }        
    
    
    
    private void set_tblContent_to_Fit_FilDirs() {
        Vector vctColumns = new Vector();
        vctColumns.addElement("");
        vctColumns.addElement("Name");
        vctColumns.addElement("Size");
        vctColumns.addElement("Type");
        vctColumns.addElement("Last Modified");
            
        FB_tm.setColumnIdentifiers(vctColumns);
        
        tblContent.setRowHeight(54);//40  
        
        tblContent.getColumn("").setMaxWidth(54);//35

        //tblContent.setRowHeight(54);  
        
        //tblContent.getColumn("").setMaxWidth(50);
        
        tblContent.getColumn("Size").setMaxWidth(60); 
        tblContent.getColumn("Size").setPreferredWidth(55);
                  
    }
    
    
    
    private void set_tblContent(String dirPath) {
        
        
        class LoadContent extends Thread {
        
        
        
            String path;
        
        
        
            public LoadContent(String dPath) {
                super();
                path = dPath;
                this.start();
            }
            
            
            
            public void run() {
        
                //MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.WAIT_CURSOR));
                //logo.showLoading();
                //Console.println("1  "+String.valueOf(System.currentTimeMillis()));
                long sTime = System.currentTimeMillis();
                int nDirs = 0;
                int nFiles = 0;    
            
                removetblContent();

                set_tblContent_to_Fit_FilDirs(); 
            
                try {
                    File[] fd = fm.getContent_Directories(path);
                    nDirs = fd.length;
                    for(int x= 0; x < fd.length; x++) {
                        Vector data = new Vector();
                        //data.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-fs-directory.png", 48, 48, ImageManipulator.ICON_SCALE_TYPE));
                        data.addElement(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(fd[x], MDS_VolatileImageLibrary.ICON_SIZE_48x48));
                        data.addElement(fd[x].getName());
                        data.addElement(String.valueOf(fm.getFormatedFileSize(fd[x].length())));
                        data.addElement("Dir"); 
                        data.addElement(fm.getLastModified_As_String(fd[x].lastModified()));
                                    
                        FB_tm.addRow(data);            
                    }
        
                    File[] f = fm.getContent_Files(path);
                    nFiles = f.length;  
            
                    for(int x= 0; x < f.length; x++) {
                        Vector data = new Vector();
                        //data.addElement(getFileType_Icon(f[x].getName()));
                        //data.addElement(ImageManipulator.createScaledImageIcon(fm.getFileType_Icon(f[x]),48 ,52 ,ImageManipulator.ICON_SCALE_TYPE));
                        data.addElement(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f[x], MDS_VolatileImageLibrary.ICON_SIZE_48x48));
                        data.addElement(f[x].getName());
                        data.addElement(String.valueOf(fm.getFormatedFileSize(f[x].length())));
                        data.addElement(fm.getFileType(f[x].getName()));
                        data.addElement(fm.getLastModified_As_String(f[x].lastModified()));                             
  
                        FB_tm.addRow(data);           
                    }
                    
                    double time = System.currentTimeMillis()-sTime;
                    lblStatusBar.setText("No of Dirs : "+String.valueOf(nDirs)+"  No of Files : "+String.valueOf(nFiles)+"     Loading time : "+String.valueOf(time/1000)+" sec"); 
        
                    //logo.stopLoading();
                    //MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.DEFAULT_CURSOR));
                           
                } catch(Exception ex) {
                    //logo.stopLoading();
                    //MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.DEFAULT_CURSOR));
                }
        
               //Console.println("2  "+String.valueOf(System.currentTimeMillis()));
                 
            }
        
            
        }
        
        
        new LoadContent(dirPath);    
        	
        FileSystemEventManager.getFileSystemEventManager().setChangeNotificationDirectory(this, new File(dirPath));	         

    }
    
    
    
    private void set_tblContent_AddFile(File f) {
        Vector data;
        if(f.isDirectory()) {
            data = new Vector();
            data.addElement(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
            data.addElement(f.getName());
            data.addElement(String.valueOf(fm.getFormatedFileSize(f.length())));
            data.addElement("Dir"); 
            data.addElement(fm.getLastModified_As_String(f.lastModified()));
                                   
            FB_tm.addRow(data);                
        } else {
            data = new Vector();
            data.addElement(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
            data.addElement(f.getName());
            data.addElement(String.valueOf(fm.getFormatedFileSize(f.length())));
            data.addElement(fm.getFileType(f.getName()));
            data.addElement(fm.getLastModified_As_String(f.lastModified()));                             
             
            FB_tm.addRow(data);                  
        }
    }
    
    
    
    private void set_tblContent_RemoveFile(File f) {
        int count = 0;
        while(count < tblContent.getRowCount()) {
            if(String.valueOf(tblContent.getValueAt(count, 1)).equals(f.getName())) {
                FB_tm.removeRow(count);                         
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
                    FB_tm.setValueAt(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48), count, 0);
                    FB_tm.setValueAt(f.getName(),count , 1);
                    FB_tm.setValueAt(String.valueOf(fm.getFormatedFileSize(f.length())), count , 2);
                    FB_tm.setValueAt("Dir",count , 3); 
                    FB_tm.setValueAt(fm.getLastModified_As_String(f.lastModified()), count , 4);               
                } else {
                   FB_tm.setValueAt(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48), count , 0);
                   FB_tm.setValueAt(f.getName(), count , 1);
                   FB_tm.setValueAt(String.valueOf(fm.getFormatedFileSize(f.length())), count , 2);
                   FB_tm.setValueAt(fm.getFileType(f.getName()), count , 3);
                   FB_tm.setValueAt(fm.getLastModified_As_String(f.lastModified()), count , 4);                                              
                }
                break;
            }
            count++;
        }         
    }
    
    
    
    private void set_tblContent_SelectedItem(File f) {
        int count = 0;
        while(count < tblContent.getRowCount()) {
            String name = String.valueOf(tblContent.getValueAt(count, 1));
            if(name.equals(f.getName())) {
                tblContent.setRowSelectionInterval(count, count);
                break;    
            }
            count++;
        }        
    }
    
    
    
    public void setCurrentLocation(String path) {
        if(path.equals(DISK_DRIVES)) {
            settblContent_SystemRoots();
            current_Directory_Location = DISK_DRIVES;  
            set_cboDirectory_Location_Text(DISK_DRIVES);  
            lblStatusBar.setText("");
            //updateMenuSystem(path);
        } else {
            current_Directory_Location = path;
            set_tblContent(path);
            set_cboDirectory_Location_Text(path);
            //updateMenuSystem(path);
        }    
    }
    
    
    
    private void removetblContent() {
        Vector v = FB_tm.getDataVector();
        v.removeAllElements(); 
        tblContent.clearSelection();                  
    }
    
    
    
    public void set_cboDirectory_Location_Text(String text) {
        cboDirectory_Location.addItem(text);
        cboDirectory_Location.setSelectedItem(text);
    }
    
    
    
    private void go() {
        String location = String.valueOf(cboDirectory_Location.getSelectedItem());
        if(location.equalsIgnoreCase(DISK_DRIVES)) {
            settblContent_SystemRoots();
        } else {
            File f = new File(location);
                
            if(!f.exists()) {
                MDS_OptionPane.showMessageDialog(this, "File, Folder does not exists.", "File Browser", JOptionPane.ERROR_MESSAGE);
                updateMenuSystem();
                updateToolBar();  
                return;
            }
                
            if(f.isDirectory()) {
                if(f.getPath().endsWith("\\")) {
                    setCurrentLocation(f.getPath());            
                } else {
                    setCurrentLocation(f.getPath()+"\\"); 
                }
            } else {
                fm.executeFile(location);
            }
        }        
    }        
  
    
    
    
    private void updateMenuSystem() {
        if(current_Directory_Location.equals(DISK_DRIVES)) {
            if(tblContent.getSelectedRow() == -1) {
                mniOpen.setEnabled(false);
            } else {
                mniOpen.setEnabled(true);	
            }
            mniCompress.setEnabled(false);
            mnuSendTo.setEnabled(false);
            mnuNew.setEnabled(false);
            mniRename.setEnabled(false);
            mniDelete.setEnabled(false);
            mniProperties.setEnabled(false);
            
            mniCut.setEnabled(false);
            if(tblContent.getSelectedRow() != -1) {
                mniCopy.setEnabled(true);
                mniCopyTo.setEnabled(true);
                if(!clipBoard.isEmpty()) {
                    if(clipBoard.getCurrentContentType() == clipBoard.CONTENT_TYPE_FILE) {
                        mniPaste.setEnabled(true);
                    }
                } else {
                    mniPaste.setEnabled(false);  
                }
            } else {
                mniCopy.setEnabled(false);
                mniCopyTo.setEnabled(false);
                mniPaste.setEnabled(false);            
            }
            mniMoveTo.setEnabled(false);
        } else {
            if(tblContent.getSelectedRow() != -1) {
                mniOpen.setEnabled(true);
                mniCompress.setEnabled(false);
                mnuSendTo.setEnabled(true);
                mnuNew.setEnabled(true);
                mniRename.setEnabled(true);
                mniDelete.setEnabled(true);
                mniProperties.setEnabled(true);
            
                mniCut.setEnabled(true);
                
                if(!clipBoard.isEmpty()) {
                    if(clipBoard.getCurrentContentType() == clipBoard.CONTENT_TYPE_FILE) {
                        mniPaste.setEnabled(true);
                    }
                } else {
                    mniPaste.setEnabled(false);  
                }
                
                mniCopy.setEnabled(true);
                mniCopyTo.setEnabled(true);                    
                mniMoveTo.setEnabled(true);                
            } else {
                mniCompress.setEnabled(false);
                mnuSendTo.setEnabled(false);
                mnuNew.setEnabled(false);
                mniRename.setEnabled(false);
                mniDelete.setEnabled(false);
                mniProperties.setEnabled(false);
            
                mniCut.setEnabled(false);
                mniCopy.setEnabled(false);
                mniCopyTo.setEnabled(false);                    
                mniPaste.setEnabled(false);
                mniMoveTo.setEnabled(false);                
            }  
        }
        
        updateToolBar();
          
    }
    
    
    
    private void updateToolBar() {
        if(current_File == null) {
            btnPrint.setEnabled(false);  
        } else {
            if(!current_File.isDirectory()) {    
                Vector vctPrintableFiles = new Vector();
                vctPrintableFiles.add("class");
                vctPrintableFiles.add("txt");
                vctPrintableFiles.add("gif");
                vctPrintableFiles.add("jpg");
                vctPrintableFiles.add("jpeg");
                vctPrintableFiles.add("png");
                vctPrintableFiles.add("html");
                vctPrintableFiles.add("htm");
                vctPrintableFiles.add("c");
                vctPrintableFiles.add("cpp");
                vctPrintableFiles.add("cxx");
            
                if(vctPrintableFiles.contains(fm.getFileExtension(current_File.getName()))) {
                    btnPrint.setEnabled(true);
                } else {
                    btnPrint.setEnabled(false);
                }
                
            }    
                               
        }
        
        if(current_Directory_Location.equals(DISK_DRIVES)) {
            btnCut.setEnabled(false);
            btnCopy.setEnabled(false);
            btnDelete.setEnabled(false);
            btnPaste.setEnabled(false);            
        } else {
            btnCut.setEnabled(true);
            btnCopy.setEnabled(true);
            btnDelete.setEnabled(true);
            if(!clipBoard.isEmpty()) {
                if(clipBoard.getCurrentContentType() == clipBoard.CONTENT_TYPE_FILE) {                
                    btnPaste.setEnabled(true); 
                }          
            } else {
                btnPaste.setEnabled(false);
            }    
        }
                    
    }
    
    
    
    public void refresh_FB_Table() {
        set_tblContent(current_Directory_Location); 
    }
    
    
    
    public void refresh() {
        refresh_FB_Table();
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){
        
        current_File = null;
                     
        if(e.getSource().getClass().getName().equals("javax.swing.JTree")) {         
            int selRow = treFolders.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = treFolders.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        //System.out.println("S");
                    } else if(e.getClickCount() == 2) {
                        //System.out.println("D");
                    }                       
                }   
                                
        } else if(e.getSource().getClass().getName().equals("FileBrowser$FB_Table")) {
            if(e.getClickCount() == 2) {

                boolean goOut = false;
                
                if(tblContent.getColumnName(1).equals("Drive")) {
                    String path = String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() , 1));                      
                    setCurrentLocation(path);
                     
                    goOut = true;
                    
                    fb_lq.addNewLocation(current_Directory_Location);
                                                            
                }   
                    
                if(goOut) return;
                                   
                if(tblContent.getColumnName(4).equals("Last Modified")) {
                
                    if(tblContent.getValueAt(tblContent.getSelectedRow() , 3).equals("Dir")) {
                        String path = String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() ,1)+"\\");
                        //current_Directory_Location = current_Directory_Location.concat(path);                   
                        setCurrentLocation(current_Directory_Location.concat(path));                                          
                                                    
                        fb_lq.addNewLocation(current_Directory_Location);
                                                                          
                    } else {
                        String name = String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() ,1));
                        String absPath = current_Directory_Location.concat(name);
                        current_File = new File(absPath);
                        fm.executeFile(absPath);
                    }
                                           
                }
                                   
            } else if(e.getClickCount() == 1 && e.getButton() == e.BUTTON3) {
                int rowNo = tblContent.getRowForLocation(e.getY());   
                dlsmdl.setSelectionInterval(rowNo, rowNo);
                
                if(tblContent.getColumnName(4).equals("Last Modified")) {
                
                    if(tblContent.getValueAt(tblContent.getSelectedRow() , 3).equals("Dir")) {
                        fm.showFilePopupMenu(this, tblContent, e.getX(), e.getY(), new File(current_Directory_Location+tblContent.getValueAt(tblContent.getSelectedRow() , 1)+"\\"));
                    } else {
                        fm.showFilePopupMenu(this, tblContent, e.getX(), e.getY(), new File(current_Directory_Location+tblContent.getValueAt(tblContent.getSelectedRow() , 1)));
                    }
                } else {
                    ddm.showDrivePopupMenu(this, tblContent, e.getX(), e.getY(), new File(String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() , 1))));
                }    

                  
            } else if(e.getClickCount() == 1) {
                if(tblContent.getColumnName(4).equals("Last Modified")) {
                
                    if(!tblContent.getValueAt(tblContent.getSelectedRow() , 3).equals("Dir")) {
                        current_File = new File(current_Directory_Location+tblContent.getValueAt(tblContent.getSelectedRow() , 1));
                    } else {
                        current_File = new File(current_Directory_Location+tblContent.getValueAt(tblContent.getSelectedRow() , 1)); 
                    }   
                    
                }
                                 
            } 
            
        }
        
        updateMenuSystem();
        
        updateToolBar();
        
    }
    
    
    

    public void mouseReleased(MouseEvent e) {} 
    
    
    
    public void mouseDragged(MouseEvent e) {
        //Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        //((JTable)e.getSource()).scrollRectToVisible(r);
    
    }
    
    
    
    public void mouseMoved(MouseEvent e) {
        if(e.getSource().getClass().getName().equals("FileBrowser$FB_Table")) {               
            int row = tblContent.getRowForLocation(e.getY());
            if(tblContent.getColumnName(4).equals("Last Modified")) {
                String type = String.valueOf(tblContent.getValueAt(row, 3));
                String name = String.valueOf(tblContent.getValueAt(row, 1));
                if(!type.equals("Dir")) {
                    //Console.println(type);
                    if(type.equals("GIF Image") || type.equals("JPEG Image") || type.equals("PNG Image")) {
                        //Console.println(current_Directory_Location+name);
                        //tblContent.showToolTip(null);
                        //tblContent.setToolTipText(String.valueOf(row));
                        tblContent.showToolTip(new ImageIcon(current_Directory_Location+name), row);
                    } else {
                        //tblContent.setToolTipText(null);
                        tblContent.hideToolTip();
                    }
                } else {
                    //tblContent.setToolTipText(null);
                    tblContent.hideToolTip();
                }
            }
         
            
            

        }            
    }
    
    
    
    public void valueChanged(ListSelectionEvent e) {
        if(tblContent.getColumnName(4).equals("Last Modified")) {
            int row = tblContent.getSelectedRow();
            if(row != -1) {
                current_File = new File(current_Directory_Location+tblContent.getValueAt(tblContent.getSelectedRow() , 1));  
                //Console.println(current_File.getPath());
            }
        }
    }
    
    
    
    public void treeCollapsed(TreeExpansionEvent e) {}
    
    
    
    public void treeExpanded(TreeExpansionEvent e) {}
    
    
    
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode dmtnLocation = (DefaultMutableTreeNode)treFolders.getLastSelectedPathComponent();
        String path = convertTreePath_ToActualPath(dmtnLocation.getPath());
        if(path.equals("")) {
            setCurrentLocation(DISK_DRIVES);
        } else {
            setCurrentLocation(path);           
        } 
        //System.out.println(path);
    }
    
    
    
    public void treeWillCollapse(TreeExpansionEvent event) {
    
    }
    
    
    
    public void treeWillExpand(TreeExpansionEvent e) {
    
        try {
        
            MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.WAIT_CURSOR));
    
            Object[] count = e.getPath().getPath();
        
            if(count.length == 1) {
                MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }
            File[] f =  fm.getContent_Directories(convertTreePath_ToActualPath(e.getPath().getPath())); 
                    
            boolean goOut = false; 
                    
            if(vctVisitedLocations.contains(convertTreePath_ToActualPath(e.getPath().getPath()))) {
                goOut = true; 
            } else {
                vctVisitedLocations.addElement(convertTreePath_ToActualPath(e.getPath().getPath()));
            }
                    
            if(goOut) {
                MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.DEFAULT_CURSOR)); 
                return;                                   
            }    
            DefaultMutableTreeNode removalNode = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
            removalNode.removeAllChildren();   
                 
            DefaultMutableTreeNode selectedNode = null;
            DefaultMutableTreeNode newNode = null;
            DefaultMutableTreeNode newNode2= null;
        
            for(int x = 0; x < f.length; x++) {  
                            
                selectedNode = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();               
                newNode = new DefaultMutableTreeNode(f[x].getName());                
            
                File[] f2 = fm.getContent_Directories(convertTreePath_ToActualPath(e.getPath().getPath())+f[x].getName()+"\\");
                
                for(int y = 0; y < f2.length; y++) {                               
                    newNode2 = new DefaultMutableTreeNode(f2[y].getName());
                    newNode.add(newNode2);
                }
                          
                dtmFolders.insertNodeInto(newNode, selectedNode,selectedNode.getChildCount());
            }
        
            DefaultMutableTreeNode del =(DefaultMutableTreeNode) selectedNode.getFirstChild();
           
            if(del.getUserObject().equals(driveEmpty)) {
                selectedNode.remove(del);         
            }
            
            MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.DEFAULT_CURSOR));
        
        } catch(Exception ex) {
             
             MDS_UIManager.setFrameCursor(fb, new Cursor(Cursor.DEFAULT_CURSOR));
             
            if(ex instanceof NullPointerException) {
                //JOptionPane.showInternalMessageDialog(this,"Unable to access the location", "File Manager", JOptionPane.INFORMATION_MESSAGE);
                MDS_OptionPane.showMessageDialog(fb ,"Unable to access the specified device.", "File Browser", JOptionPane.INFORMATION_MESSAGE);
            } else {
            	throw new RuntimeException(ex);
                //MDS.getExceptionManager().showException(ex);
            }
        
        }
        
        
    }
    
    
    
    public void keyPressed(KeyEvent e) {
        /*
        if(e.getSource().getClass().getName().equals("javax.swing.JTable")) {
        
            String loc = fb_lq.get_Before();
            
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {                        
                if(!loc.equals("")) {
                    setCurrentLocation(loc);
                    if(loc.equals(DISK_DRIVES)) {
                        setCurrentLocation(DISK_DRIVES);
                    }                               
                }              
            }                   
        }*/
                  
    }



    public void keyReleased(KeyEvent e) {}



    public void keyTyped(KeyEvent e) {} 
    
    
    
    public void actionPerformed(ActionEvent e) {
    
        ProcessManager prm = MDS.getProcessManager();
    
        if(e.getActionCommand().equals("Up")) {
            File f = new File(current_Directory_Location);
            String newloc = null;
            if(vctDiskDrives.contains(f.getPath())) {
                setCurrentLocation(DISK_DRIVES);                
            } else {                
                newloc = f.getParent();
                                   
                if(!vctDiskDrives.contains(newloc)) {
                   newloc = newloc.concat("\\");
                }
                                                 
                 setCurrentLocation(newloc);          

            } 
            
        } else if(e.getActionCommand().equals("Back")) {
            String loc = fb_lq.get_Before();

            if(!loc.equals("")) {
                setCurrentLocation(loc);
                  
                if(loc.equals("Disk Drives")) {
                    setCurrentLocation(DISK_DRIVES);
                }
                            
            }                       
        
        } else if(e.getActionCommand().equals("Forword")) {
            String loc = fb_lq.get_After();
            if(!loc.equals("")) {
                setCurrentLocation(loc);     
             }                                 
        } else if(e.getActionCommand().equals("Home")) {
            setCurrentLocation(System.getProperty("user.home")+"\\"); 
        } else if(e.getActionCommand().equals("Refresh")) {
            setCurrentLocation(current_Directory_Location);            
        } else if(e.getActionCommand().equals("Cut")) {
            clipBoard.setContent(current_File ,clipBoard.STATUS_MOVED);  
        } else if(e.getActionCommand().equals("Copy")) {
            if(current_Directory_Location == DISK_DRIVES) {             
                clipBoard.setContent(new File(String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() , 1))) ,clipBoard.STATUS_COPIED);           
            } else {
                clipBoard.setContent(current_File ,clipBoard.STATUS_COPIED);
            }
        } else if(e.getActionCommand().equals("Paste")) {
            try {
                File f = (File)clipBoard.getContent();
                if(current_Directory_Location != DISK_DRIVES) {
                    //Console.println(f.getPath()+"  * "+ current_Directory_Location+f.getName());
                    if(f.exists()) {
                        if(f.isDirectory()) {
                            MDS.getFileManager().copyFile(f, new File(current_Directory_Location) ,true);
                        } else {
                            MDS.getFileManager().copyFile(f, new File(current_Directory_Location+f.getName()), true); 
                        }
                    } else {
                        MDS_OptionPane.showMessageDialog(this.getListener__MDS_Frame(), "File not found ("+f.getPath()+")", "File Copy", JOptionPane.ERROR_MESSAGE);    
                    }
                } else {
                    
                }
                //MDS.getFileManager().copy(file.getPath(), des+file.getName(), true);     
            } catch (Exception ex) {
            	throw new RuntimeException(ex);
                //MDS.getExceptionManager().showException(ex); 
            }
        } else if(e.getActionCommand().equals("Print")) {
            if(current_File.getName().endsWith("png") || current_File.getName().endsWith("gif") || current_File.getName().endsWith("jpg") || current_File.getName().endsWith("jpeg")) {
                MDS.getPrinter().print(new ImageIcon(current_File.getPath())); 
            }                    
        } else if(e.getActionCommand().equals("Find")) {
            prm.execute(new File(MDS.getBinaryPath(),"FindFiles"));
        } else if(e.getActionCommand().equals("Clip Board Viewer")) { 
            prm.execute(new File(MDS.getBinaryPath(),"ClipBoardViewer"));
        } else if(e.getActionCommand().equals("Delete")) {
            if(MDS_OptionPane.showConfirmDialog(this.getListener__MDS_Frame(), "Are you sure you want to delete "+ current_File.getName(), "Confirm File Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                if(current_File.isDirectory()) {
                    fm.deleteDir(current_File);
                } else {
                    if(!current_File.delete()) {
                        MDS_OptionPane.showMessageDialog(this.getListener__MDS_Frame(), "Uable to Delete the File / Directory.", "Error Deleting File / Directory", JOptionPane.ERROR_MESSAGE);                          
                    } else {
                        //this.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.DELETE_FILE));
////                        MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DELETE_FILE, current_File));
                    }
                }    
            }            
        } else if(e.getActionCommand().equals("Rename")) {
            String nfn = "";  
            nfn = MDS_OptionPane.showInputDialog(this.getListener__MDS_Frame(), "Type new file / directory name.", "Rename File / Directory", JOptionPane.QUESTION_MESSAGE);      
            if(nfn == null) {
                nfn="";
            }       
            if(!nfn.equals("")) { 
                File fn = new File(fm.getFilePathOnly(current_File.getPath())+nfn);
                //System.out.println(fn.getPath());
                if(!current_File.renameTo(fn)) {
                    MDS_OptionPane.showMessageDialog(this, "Uable to rename the Directory.", "Error Renaming File / Directory", JOptionPane.ERROR_MESSAGE);      
                } else {
                    //this.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.RENAME_FILE));  
                    //MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.RENAME_FILE, current_File));  
////                    MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.RENAME_FILE, fn, current_File));
                }        
            }                
        } else if(e.getActionCommand().equals("Properties")) {
            fm.showFileProperties(current_File);
        } else if(e.getActionCommand().equals("Home Directory")) {
            if(current_File.isDirectory()) {
                MDS.getFileManager().copyFile(current_File, new File(System.getProperty("user.home")+"\\"),true);    
            } else {
                MDS.getFileManager().copyFile(current_File, new File(System.getProperty("user.home")+"\\"+current_File.getName()), true);    
            }                 
        } else if(e.getActionCommand().equals("3.5 Floppy")) {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            if(fsv.isFloppyDrive(new File("A:\\"))) {
                if(current_File.isDirectory()) {
                    MDS.getFileManager().copyFile(current_File, new File("A:\\"),true);    
                } else {
                    MDS.getFileManager().copyFile(current_File, new File("A:\\"), true);    
                }                        
            }      
        } else if(e.getActionCommand().equals("Folder")) {
            try {
                FileSystemView fsv = FileSystemView.getFileSystemView();
                File f = fsv.createNewFolder(new File(current_Directory_Location));
                //this.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.ADD_FILE)); 
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f));
            } catch (Exception ex) {
                MDS_OptionPane.showMessageDialog(this, "Uable to create the file.", "Error Creating File / Directory", JOptionPane.ERROR_MESSAGE); 
            }
        } else if(e.getActionCommand().equals("Text Document")) {
            String nfn = "";  
            nfn = MDS_OptionPane.showInputDialog(this, "New text file Name", "New text file", JOptionPane.QUESTION_MESSAGE);      
            if(!nfn.equals("")) {
                File f = new File(current_Directory_Location+nfn);
                if(!nfn.endsWith(".txt")) {
                    f = new File(current_Directory_Location+nfn+".txt");
                }
                if(!f.exists()) {
                    try {
                        f.createNewFile();
                        //this.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.ADD_FILE));
////                        MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f));
                    } catch(Exception ex) {
                        MDS_OptionPane.showMessageDialog(this, ex.getMessage(), "Error Creating File", JOptionPane.ERROR_MESSAGE); 
                    }    
                } else {
                    MDS_OptionPane.showMessageDialog(this, "File already exists ("+nfn+")", "File Creation", JOptionPane.INFORMATION_MESSAGE);    
                }
            }                
        } else if(e.getActionCommand().equals("HTML Document")) {
            String nfn = "";  
            nfn = MDS_OptionPane.showInputDialog(this, "New Html file Name", "New Html file", JOptionPane.QUESTION_MESSAGE);      
            if(!nfn.equals("")) {
                File f = new File(current_Directory_Location+nfn);
                if(!nfn.endsWith(".htm") || !nfn.endsWith(".html")) {
                    f = new File(current_Directory_Location+nfn+".html");
                }
                if(!f.exists()) {
                    try {
                        f.createNewFile();
                        //this.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.ADD_FILE));
////                        MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f));
                    } catch(Exception ex) {
                        MDS_OptionPane.showMessageDialog(this, ex.getMessage(), "Error Creating File", JOptionPane.ERROR_MESSAGE); 
                    }    
                } else {
                    MDS_OptionPane.showMessageDialog(this, "File already exists ("+nfn+")", "File Creation", JOptionPane.INFORMATION_MESSAGE);    
                }
            }            
        } else if(e.getActionCommand().equals("MSDOS Batch File")) {
        
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "File Browser", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"file-browser.png"), MDS.getAbout_Mahesh()); 
            updateMenuSystem();
            updateToolBar();
        } else if(e.getActionCommand().equals("Find Files")) {
            prm.execute(new File(MDS.getBinaryPath(),"FindFiles"));     
        } else if(e.getActionCommand().equals("Redirected StandardOutput Viewer")) {
            prm.execute(new File(MDS.getBinaryPath(),"RedirectedStandardOutputViewer"));  
        } else if(e.getActionCommand().equals(" Go >>")) {
            go();
        } else if(e.getActionCommand().equals("Open")) {
            if(tblContent.getColumnName(1).equals("Drive")) {
                String path = String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() , 1));                      
                setCurrentLocation(path);                    
            } else {
////                this.openFile(new FileSystemEvent(current_File));
            }  
        } else if(e.getActionCommand().equals("Copy To")) {
            String des = "";
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this.getListener__MDS_Frame()) ==  fmdc.APPROVE_OPTION) {
                    des = fmdc.getPath();
                if(current_File.isDirectory()) {
                    MDS.getFileManager().copyFile(current_File, new File(des), true);
                } else {
                    MDS.getFileManager().copyFile(current_File, new File(des+current_File.getName()), true);    
                }                         
            }         
        } else if(e.getActionCommand().equals("Move To")) {
            String des = "";
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this.getListener__MDS_Frame()) ==  fmdc.APPROVE_OPTION) {
                des = fmdc.getPath();
                if(current_File.isDirectory()) {
                	throw new ConcurrentModificationException("File / Directory deletion error. File / Directory is being used by another program or IO stream");
                    //MDS.getExceptionManager().showException(new ConcurrentModificationException("File / Directory deletion error. File / Directory is being used by another program or IO stream"));    
                } else {
                    if(!current_File.canWrite()) {
                        if(MDS_OptionPane.showConfirmDialog(this.getListener__MDS_Frame(), "Are you sure you want move the Read Only file : "+current_File.getName(), "Confirm File Move",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {      
                            MDS.getFileManager().moveFile(current_File, new File(des+current_File.getName()), true); 
                        }                                
                    } else {
                        MDS.getFileManager().moveFile(current_File, new File(des+current_File.getName()), true); 
                    }
                }
            }          
        }
 
    }
    
    
    
    public void itemStateChanged(ItemEvent e) {
    
    }
    
    
    
    public void menuCanceled(MenuEvent e) {}
    
    
    
    public void menuDeselected(MenuEvent e) {}
    
    
    
    public void menuSelected(MenuEvent e)  {
        updateMenuSystem();
    }
    
    
    
    public void fileSystemUpdated(FileSystemEvent e) {
////        if(e.getType() == e.REFRESH) {
////            setCurrentLocation(current_Directory_Location);   
////        } else if(e.getFile() != null) { 
////            if(e.getFile().getName().equals(DISK_DRIVES)) {
////                setCurrentLocation(DISK_DRIVES);            
////            } else if(fm.getFilePathOnly(e.getFile().getPath()).equals(current_Directory_Location)) {                 
////                if(e.getActualType() == e.ABSTRACT_REMOVE_FILE) { 
////                    set_tblContent_RemoveFile(e.getFile());
////                } else if(e.getActualType() == e.ABSTRACT_ADD_FILE) { 
////                    set_tblContent_AddFile(e.getFile());
////                } else if(e.getActualType() == e.ABSTRACT_EDIT_FILE) {
////                    set_tblContent_EditFile(e.getFile(), e.previousFile());
////                }
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
        if(f.isDirectory()) {
            setCurrentLocation(f.getPath()+"\\");    
        } else {
            fm.executeFile(f.getPath());
        }    
    }
    
    
    
    public MDS_Frame getListener__MDS_Frame() {
        return this;
    }
    
    
    
    public void updateFileBrowser() {
        set_tblContent(current_Directory_Location);
    }
    
    
    
    public void dragGestureRecognized(DragGestureEvent event) {
        if(!current_Directory_Location.equals("Disk Drives")) {
            String draggedValue = current_Directory_Location+String.valueOf(tblContent.getValueAt(tblContent.getSelectedRow() , 1));    
            Transferable transferable = new MDS_FileTransferable(draggedValue);
            event.startDrag(null, transferable, this);                    
            File f = new File(draggedValue);
            if(f.isDirectory()) {
                dragIcon.showDragIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48)); 
            } else {
                dragIcon.showDragIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48));    
            }
        }
        
    }
       
        
        
    public void dragEnter(DragSourceDragEvent event) {}
	
	
	
    public void dragOver(DragSourceDragEvent event) {}
	
	
	
    public void dragExit(DragSourceEvent event) {}
	
	
	
    public void dropActionChanged(DragSourceDragEvent event) {}



    public void dragDropEnd(DragSourceDropEvent event) {
        dragIcon.hideDragIcon();
        MDS.getBaseWindow().getDesktop().repaint();
    }
    
    
    //====================================              
    
    
    public void dragEnter(DropTargetDragEvent dtde) {}
    
    
    
    public void dragExit(DropTargetEvent e) {}
    
    
    
    public void dragOver(DropTargetDragEvent e) {
    
        Point p = e.getLocation(); 
        Double d = new Double(p.getY());
        int row = tblContent.getRowForLocation(d.intValue());
        tblContent.setRowSelectionInterval(row, row);
        if(!current_File.isDirectory()) {
            //e.rejectDrag();
            //return;            
        } else {
 
        }
        
    }
    
    
    
    public void drop(DropTargetDropEvent e) {
        try { 
            Transferable transferable = e.getTransferable();
            File f = new File(String.valueOf(transferable.getTransferData(DataFlavor.stringFlavor)));
            if(f.exists()) {
                if(current_File.getPath().equals(f.getPath())) {
                    e.rejectDrop();
                    MDS_OptionPane.showMessageDialog(this, "Cannot move '"+f.getName()+"' The source and the destination file names are the same.", "Error Moving File or Directory", JOptionPane.ERROR_MESSAGE);       
                } else {
                    
                    e.dropComplete(true);
                    
                    if(MDS_OptionPane.showConfirmDialog(this, "Are you sure you want to copy '"+ f.getName()+"'\n\n"+"Source : "+f.getPath()+"\nDestination : "+current_Directory_Location+"\n\n", "File Browser [Confirm File Copy]", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    
                        if(f.isDirectory()) {
                            MDS.getFileManager().copyFile(f, new File(current_Directory_Location), true);
                        } else {
                            MDS.getFileManager().copyFile(f, new File(current_Directory_Location, f.getName()), true);    
                        }                    
                                        
                    }                    
                    
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public void dropActionChanged(DropTargetDragEvent dtde) {}
    
    
    
    
    
    class FB_Table extends MDS_Table  {
    
    
    
        final int TOOL_TIP_TYPE_IMAGE = 1; 
        
        private int toolTipType = -1;
        
        private ImageIcon imageIcon;
    
    
    
        public FB_Table() {
            super();
        }
        
        
        
        public FB_Table(TableModel dm) {
            super(dm);        
        }
        
        
        public JToolTip createToolTip() {           
            JToolTip tip = new JToolTip();
            if(toolTipType == TOOL_TIP_TYPE_IMAGE) {
                tip.setLayout(null);
                Color c = UIManager.getColor("ToolTip.background");
                Color bkColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
                tip.setForeground(bkColor); 
                PicturePreview ppv = new PicturePreview(imageIcon);              
                tip.add(ppv);
                tip.setPreferredSize(new Dimension(ppv.getWidth()+5, ppv.getHeight()+5));
            }    
            tip.setComponent(this);
            return tip;        
        }
        
        
        
        public void showToolTip(ImageIcon ii, int row) {
            imageIcon = ii;
            toolTipType = TOOL_TIP_TYPE_IMAGE;
            Random rand = new Random();
            setToolTip(String.valueOf(row));
        }
        
        
        
        public void hideToolTip() {
            setToolTip(null);
        } 
        
        
        
        public void setToolTipText(String text) {}
        
        
        
        private void setToolTip(String text) {
            String oldText = getToolTipText();
            putClientProperty(TOOL_TIP_TEXT_KEY, text);
            ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
            if (text != null) {
               if (oldText == null) {
                    toolTipManager.registerComponent(this);
               }
            } else {
                toolTipManager.unregisterComponent(this);
            }            
        }

        
        
        
        
        class PicturePreview extends JPanel {
    
    
    
            ImageIcon thumbnail = null;
        
        
        
            public PicturePreview(ImageIcon tmpIcon) {
                this.setOpaque(false);
                if(tmpIcon.getIconWidth() > 90) {
                    thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_FAST));
                } else {
                    thumbnail = tmpIcon;
                }   
     
                this.setSize(thumbnail.getIconWidth()+5, thumbnail.getIconHeight()+5);
                repaint(); 
                              
            }               
            
            
            
            public void paint(Graphics g) {
               super.paint(g);
               if(thumbnail != null) {                
                   thumbnail.paintIcon(this, g, 5, 5);
               }
            }                                               
        }
                                              
    }


 
    
    
    class FB_TreeCellRenderer extends DefaultTreeCellRenderer {  
    
    
    
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            
            return this;
            
        }


 
    }    
   
    
          
    

    class FB_TableModel extends DefaultTableModel {
    
    
    
        public FB_TableModel() {
            super();
        }
    
     
    
        public FB_TableModel(Object[][] data, Object[] columns) {
            super(data, columns);
        }
    
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    
        public Class getColumnClass(int column) {
            Vector v = (Vector) dataVector.elementAt(0);
    
            return v.elementAt(column).getClass();
        }
    
    
    } 
    
    
    
    
         
    class FB_LocationQueue {
    
    
        
        final int MAXSIZE = 10;
         
        Vector vctQueue;
        int pointer = -1;
        int stopPoint = -1;
        
        boolean lock = true;
        
        String previouslyAdded_Location = null;
    
    
    
        public FB_LocationQueue() {
            vctQueue = new  Vector();
        }
        
        
        
        public void addNewLocation(String l) {
            
            lock = false;
            
            if(stopPoint == pointer) {
                vctQueue.addElement(l);
                pointer++;
                stopPoint = pointer;
            }
            
            if(stopPoint>pointer) {
                pointer++;
                Object o = vctQueue.set(pointer,l);
                                
            }
            
        }
        
        
        
        public String get_Before() {
        String loc = "";
        boolean beep = true;
        
            if(!lock) {
                if(pointer!=0) {
                    pointer--;
                    loc = String.valueOf(vctQueue.elementAt(pointer));
                    beep = false;
                }
            } 
            
            if(beep) Toolkit.getDefaultToolkit().beep();
            return loc;
        }
        
        
        
        public String get_After() {
            String loc = "";
            boolean beep = true;
            
            if(!lock) {              
                if(stopPoint>pointer){
                    pointer++;
                    loc = String.valueOf(vctQueue.elementAt(pointer));
                    beep = false;
                }
            }
            
            if(beep) Toolkit.getDefaultToolkit().beep();
            
            return loc; 
        }
        
        
    }      
    
    
}
 