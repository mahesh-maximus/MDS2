
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.*;
import java.net.*;
import java.lang.reflect.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.util.logging.*;




public class MatrixFileBrowser extends MDS_Frame implements MouseListener, ActionListener, MouseMotionListener, 
                               FileSystemListener, MenuListener, ListSelectionListener, ClipBoardListener, DragSourceListener, DragGestureListener, DropTargetListener, MDS_FilePopupMenuListener {



    private MatrixFileBrowser mfb = this;

    private FileManager fm = MDS.getFileManager();
//    private DiskDrives dd = new DiskDrives();
    private MDS_User usr = MDS.getUser();
    private DiskDriveManager ddm = MDS.getDiskDriveManager();
    private MDS_Clipboard clipBoard = MDS.getClipboard();
    private MDS_Compression cmpr = new MDS_Compression(); 
    private ProcessManager prm = MDS.getProcessManager();
    
    private JComponent contentPane;

    private JMenuBar menuBar = new JMenuBar();
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
  
    private MDS_ToolBar toolBar = new MDS_ToolBar();
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
    
    private MDS_Label lblAddress = new MDS_Label("Address  ");
    private MDS_ComboBox cboLocation = new MDS_ComboBox();
    private MDS_Button btnGo = new MDS_Button(" Go >>"); 
    private MDS_Label lblStatusBar = new MDS_Label(); 
	private MDS_ProgressBar prgbLoadContent = new MDS_ProgressBar(SwingConstants.HORIZONTAL, 0, 100);   
    
    private MDS_Panel pnlSummery = new MDS_Panel();
    private JScrollPane scrlpSummery = new JScrollPane(pnlSummery, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private MDS_ListModel lstmdlContent = new MDS_ListModel();
    private MDS_List lstContent = new MDS_List(lstmdlContent);
    private JScrollPane scrlp_Content = new JScrollPane(lstContent);
 
    private MDS_Panel pnlSumContainer = new MDS_Panel(new BorderLayout());
    private MDS_Panel pnlSumContainer_1 = new MDS_Panel(new BorderLayout());
    private Sum_SystemTasks sum_ST = new Sum_SystemTasks();
    private Sum_OtherPlaces sum_OP = new Sum_OtherPlaces(mfb);
  
    public final String SYSTEM_ROOTS = "System Roots";
    private String current_Location = SYSTEM_ROOTS;
    private File current_Open_File = null;
    private File selected_File = null;
    private Vector vctSystemRoots = new Vector();
    
    private LocationQueue lq = new LocationQueue();

    private DragIcon dragIcon = new DragIcon();
    
    private OperatingSystem os = OperatingSystem.getOperatingSystem();
    
    private FileSystemEventManager fsem = FileSystemEventManager.getFileSystemEventManager();
 
 	private Logger log = Logger.getLogger("MatrixFileBrowser");
 	
 	private FileSystemListener fsl = this;
  

    public MatrixFileBrowser() {
        super("Matrix File Browser",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_html.png"));   
        
        
        
        mnuFile.add(mniOpen);
        mnuFile.add(mniCompress);
        //mniCompress.setEnabled(false);
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
        menuBar.add(mnuFile);
        
        mnuEdit.add(mniCut);
        mnuEdit.add(mniCopy);
        mnuEdit.add(mniPaste);
        mnuEdit.add(mniCopyTo);
        mnuEdit.add(mniMoveTo);
        mnuEdit.addMenuListener(this);
        menuBar.add(mnuEdit);
        
        mniStatusBar.setEnabled(false);
        mnuView.add(mniStatusBar);
        menuBar.add(mnuView);
        
        mnuTools.add(mniFind);
        mnuTools.add(mniStdo);
        mnuTools.add(mniClipBoard);
        menuBar.add(mnuTools);
        
        mnuHelp.add(mniAbout);
        menuBar.add(mnuHelp);
                                                    
        this.setJMenuBar(menuBar);      	    
        
        contentPane = (JComponent)this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        MDS_Panel pnlToolBar_AddreesBar_Container = new MDS_Panel();
        pnlToolBar_AddreesBar_Container.setLayout(new BorderLayout());

        MDS_Panel pnlToolBar_Logo_Container = new MDS_Panel();
        
        pnlToolBar_Logo_Container.setLayout(new BorderLayout()); 
        
        create_toolBar_Buttons();   
        
        //cboDirectory_Location.addKeyListener(this);
        //cboDirectory_Location.addItemListener(this);
        cboLocation.setEditable(true);
        cboLocation.setRenderer(new CboLocationRenderer());
        
        toolBar.setFloatable(false);
        
        toolBar.addSeparator();
        
        pnlToolBar_Logo_Container.add(toolBar, BorderLayout.CENTER);
        
        pnlToolBar_Logo_Container.add(new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"MDS.png", 36, 36, ImageManipulator.ICON_SCALE_TYPE)), BorderLayout.EAST);
        //pnlToolBar_Logo_Container.add(logo, BorderLayout.EAST);

        pnlToolBar_AddreesBar_Container.add(pnlToolBar_Logo_Container, BorderLayout.NORTH);
        pnlToolBar_AddreesBar_Container.add(lblAddress, BorderLayout.WEST);
        pnlToolBar_AddreesBar_Container.add(cboLocation, BorderLayout.CENTER);
        btnGo.addActionListener(this);
        pnlToolBar_AddreesBar_Container.add(btnGo, BorderLayout.EAST);
        
        contentPane.add(pnlToolBar_AddreesBar_Container, BorderLayout.NORTH);            
        
        pnlSummery.setBackground(Color.white); 
        pnlSummery.setLayout(new BorderLayout());
        
        pnlSumContainer.setOpaque(false);
        pnlSumContainer.add(sum_ST, BorderLayout.NORTH);
        pnlSumContainer.add(sum_OP, BorderLayout.SOUTH);
        pnlSummery.add(pnlSumContainer, BorderLayout.NORTH);
        pnlSumContainer_1.setOpaque(false);
        pnlSummery.add(pnlSumContainer_1, BorderLayout.CENTER);
        
        scrlpSummery.setPreferredSize(new Dimension(200,200));
        contentPane.add(scrlpSummery, BorderLayout.WEST);
		
		lblStatusBar.setLayout(new BorderLayout());
		//prgbLoadContent.setPreferredSize(new Dimension(200, 7));
		//lblStatusBar.add(prgbLoadContent, BorderLayout.EAST);
        contentPane.add(lblStatusBar, BorderLayout.SOUTH);
        
        initialize_lstContent();
        
        //mfb = this;
        
        File[] roots = fm.getRootDrives();
        for(int count = 0; count < roots.length; count++) {
            vctSystemRoots.addElement(roots[count].getPath());
        }  
        
        clipBoard.addClipBoardListener(this);      
        
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(lstContent, DnDConstants.ACTION_COPY_OR_MOVE, this);         
        
        new DropTarget(lstContent, this);   
        
        MDS.getFileSystemEventManager().addFileSystemListener(this);	
        	
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
           		log.info("removeFileSystemListener");
				FileSystemEventManager.getFileSystemEventManager().removeFileSystemListener(fsl);	                  
            }
        });        	
                
        contentPane.add(scrlp_Content, BorderLayout.CENTER);
        this.setBounds(0,0,800,600);            
        this.setVisible(true);                       
    }
    
    
    
    public static void MDS_Main(String arg[]) {
    
    	try {
        	MatrixFileBrowser mfb1 = new MatrixFileBrowser();
        	mfb1.setLocation(mfb1.SYSTEM_ROOTS);
    	} catch(Exception ex) {
    		//System.out.println(ex.)
    		ex.printStackTrace();
    	}

    } 
    
    
    
    private void initialize_lstContent() {
        lstContent.addMouseListener(this);  
        lstContent.addMouseMotionListener(this);
        lstContent.addListSelectionListener(this);
    }
    
    
    
    private void create_toolBar_Buttons() {
    
        btnBack = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-previous.png"));  
        btnBack.setActionCommand("Back");
        toolBar.add(btnBack); 
        btnBack.addActionListener(this);    
        btnForword = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-next.png"));  
        btnForword.setActionCommand("Forword");
        btnForword.addActionListener(this);
        toolBar.add(btnForword);     
        btnUp = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-up.png"));    
        btnUp.setActionCommand("Up");
        btnUp.addActionListener(this);
        toolBar.add(btnUp);  
        btnHome = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-gohome.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnHome.setActionCommand("Home");
        btnHome.addActionListener(this);
        toolBar.add(btnHome);               
        toolBar.addSeparator();  
        btnRefresh = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-reload.png"));
        btnRefresh.setActionCommand("Reshresh");
        btnRefresh.addActionListener(this);
        toolBar.add(btnRefresh); 
        toolBar.addSeparator();      
        btnCut = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcut.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnCut.setActionCommand("Cut");
        btnCut.addActionListener(this);
        toolBar.add(btnCut);
        btnCopy = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcopy.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnCopy.setActionCommand("Copy");
        btnCopy.addActionListener(this);
        toolBar.add(btnCopy);  
        btnPaste = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editpaste.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnPaste.setActionCommand("Paste");
        btnPaste.addActionListener(this);
        toolBar.add(btnPaste);  
        btnDelete = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editdelete.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnDelete.setActionCommand("Delete");
        btnDelete.addActionListener(this);
        toolBar.add(btnDelete);   
        toolBar.addSeparator();            
        btnPrint = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-device-printer1.png", 24 ,24 , ImageManipulator.ICON_SCALE_TYPE));     
        btnPrint.setActionCommand("Print");
        btnPrint.addActionListener(this);
        toolBar.add(btnPrint);                                                            
        btnFind = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-search.png"));     
        btnFind.setActionCommand("Find Files");
        btnFind.addActionListener(this);
        toolBar.add(btnFind);      
        
    }       
    
    
    
    private void set_lstContent_to_Fit_SystemRoots() {
        if((DefaultListCellRenderer)lstContent.getCellRenderer() instanceof LstContentRenderer_SystemRoots){
        } else{     
            lstContent.setCellRenderer(new LstContentRenderer_SystemRoots());
            lstContent.setLayoutOrientation(JList.VERTICAL_WRAP);
            lstContent.setVisibleRowCount(3);
            lstContent.setFixedCellWidth(100);
            lstContent.setFixedCellHeight(100);
        }    
    }
    
    
    
    private void set_tblContent_SystemRoots() {
        
        try {
            
            lstmdlContent.removeAllElements();
            set_lstContent_to_Fit_SystemRoots();   
            File[] rd = fm.getRootDrives();

            for(int x= 0; x < rd.length; x++) {
                lstmdlContent.addElement(rd[x].getPath());                                                                                                                                                                            
            }
        
        } catch(Exception ex) {
            ex.printStackTrace();
        }
                        
    }
    
    
    
    private void set_lstContent_to_Fit_FileDirs() {    
        if((DefaultListCellRenderer)lstContent.getCellRenderer() instanceof LstContentRenderer_FileDirs){
        } else{ 
            lstContent.setVisibleRowCount(0);
            lstContent.setCellRenderer(new LstContentRenderer_FileDirs());    
            lstContent.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            lstContent.setFixedCellWidth(90);
            lstContent.setFixedCellHeight(90);   
        }     
    }
    
    
    
    private void set_lstContent(String dirPath) {
    
        
        class LoadContent extends Thread {
        
        
        
            String path;
        
        
        
            public LoadContent(String dPath) {
                super();
                path = dPath;
                this.start();
            }
            
            
            
            public void run() {
        
                MDS_UIManager.setFrameCursor(mfb, new Cursor(Cursor.WAIT_CURSOR));
               
                long sTime = System.currentTimeMillis();
                int nDirs = 0;
                int nFiles = 0;    
            
                lstmdlContent.removeAllElements();
                
                set_lstContent_to_Fit_FileDirs(); 
				
				//prgbLoadContent.setValue(0);
            
                try {
					/*
					File[] temp = fm.getContent(path);
					double fileCount = temp.length;
					double count = 0;
					Double pctg;*/
                    File[] fd = fm.getContent_Directories(path);
                    nDirs = fd.length;
                    for(int x= 0; x < fd.length; x++) {
                        lstmdlContent.addElement(fd[x].getName());
						/*
						count++;
						pctg = new Double(Math.abs(count/fileCount*100));
						prgbLoadContent.setValue(pctg.intValue());
						prgbLoadContent.repaint();*/
						
                    }
        
                    File[] f = fm.getContent_Files(path);
                    nFiles = f.length;  
                    for(int x= 0; x < f.length; x++) {
                        lstmdlContent.addElement(f[x].getName()); 
						/*
						count++;
						pctg = new Double(Math.abs(count/fileCount*100));
						prgbLoadContent.setValue(pctg.intValue());
						prgbLoadContent.repaint();   */
                    }
                    
                    double time = System.currentTimeMillis()-sTime;
                    lblStatusBar.setText("No of Dirs : "+String.valueOf(nDirs)+"  No of Files : "+String.valueOf(nFiles)+"     Loading time : "+String.valueOf(time/1000)+" sec");      
                    //prgbLoadContent.setValue(0);
					  
                    MDS_UIManager.setFrameCursor(mfb, new Cursor(Cursor.DEFAULT_CURSOR));
                           
                } catch(Exception ex) {
                    MDS_UIManager.setFrameCursor(mfb, new Cursor(Cursor.DEFAULT_CURSOR));
                }
        
                 
            }
        
            
        }
        
        new LoadContent(dirPath);     
    }
    
    
    
    private void set_cboLocation(String val) {
        cboLocation.removeItem(val);
        cboLocation.addItem(val);
        cboLocation.setSelectedItem(val);       
    }
    
    
    
    private void setLocation(String loc,  boolean addLQ) {
        if(loc.equals(SYSTEM_ROOTS)) {
            current_Open_File = null;
            current_Location = loc;
            set_cboLocation(loc);
            set_tblContent_SystemRoots(); 
            if(addLQ) lq.addNewLocation(loc);         
        } else {
            current_Open_File = null;
            if(!loc.endsWith("\\")) current_Location = loc+"\\"; 
            else current_Location = loc;
            set_cboLocation(current_Location);
            set_lstContent(current_Location); 
            if(addLQ) {lq.addNewLocation(current_Location);} 
            	
            fsem.setChangeNotificationDirectory(this, new File(current_Location));	
            	        
        }
        update_ToolBar();
    }
    
    
    
    private void setLocation(String loc) {
        setLocation(loc, true);    
    }
    
    
    
    private void setUNV_File(JComponent com, File f) {
        scrlp_Content.setViewportView(null);
        scrlp_Content.setViewportView(com);
        current_Open_File = f;
        set_cboLocation(f.getPath());        
    }
    
    
    
    private void open_UNV(File f) {
        String fx = fm.getFileExtension(f.getName());
        if(fx.equals("txt") || fx.equals("rtf") || fx.equals("c") || fx.equals("h") || fx.equals("cpp") || fx.equals("cxx") || fx.equals("java")) {
            setUNV_File(new UNV_TextFile(f), f);
        } else if(fx.equals("jpg") || fx.equals("jpeg") || fx.equals("gif") || fx.equals("png")) {
            setUNV_File(new UNV_ImageFile(f), f);
        } else if(fx.equals("html") || fx.equals("htm")) {
            setUNV_File(new UNV_HTML_File(f), f);
        } else if(fx.equals("jar")) {
            setUNV_File(new UNV_JarFile(f), f);
        } else if(fx.equals("zip")) {
            setUNV_File(new UNV_ZipFile(f), f);
        } else {
            fm.executeFile(f);
        }
    }
    
    
    
    private void set_lstContent_EditFile(File f, File pf) {
        int count = 0;
        while(count < lstmdlContent.getSize()) {
            if(String.valueOf(lstmdlContent.getElementAt(count)).equals(pf.getName())) {
                lstmdlContent.setElementAt(f.getName(), count);
                break;           
            }
            count++;
        }        
    }
    
    
    
    private void set_lstContent_AddFile(File f) {
        lstmdlContent.addElement(f.getName()); 
        setSelected_File(f);               
    }
    
    
    
    private void set_lstContent_RemoveFile(File f) {
        lstmdlContent.removeElement(f.getName());    
    }
    
    
    
    private void setSelected_File(File f) {
        lstContent.setSelectedValue(f.getName(), true);
    }
    
    
    
    private void update_ToolBar() {
        if(current_Location.equals(SYSTEM_ROOTS)) {
            if(lstContent.getSelectedIndex() != -1) {
                btnCut.setEnabled(false);
                btnCopy.setEnabled(true);
                btnPaste.setEnabled(false);
                btnDelete.setEnabled(false);
                btnPrint.setEnabled(false);
            } else {
                btnCut.setEnabled(false);
                btnCopy.setEnabled(false);
                btnPaste.setEnabled(false);
                btnDelete.setEnabled(false);
                btnPrint.setEnabled(false);           
            }       
        } else {
            if(lstContent.getSelectedIndex() != -1) {
                btnCut.setEnabled(true);
                btnCopy.setEnabled(true);
                if(!clipBoard.isEmpty()) {
                    if(clipBoard.getCurrentContentType() == clipBoard.CONTENT_TYPE_FILE) {
                        btnPaste.setEnabled(true);
                    }
                } else {
                    btnPaste.setEnabled(false);  
                }   
                btnDelete.setEnabled(true);
                if(selected_File.isDirectory()) {
                    btnPrint.setEnabled(false);       
                } else {
                    Vector vctPrintableFiles = new Vector();
                    vctPrintableFiles.add("gif");
                    vctPrintableFiles.add("jpg");
                    vctPrintableFiles.add("jpeg");
                    vctPrintableFiles.add("png");  
                    if(vctPrintableFiles.contains(fm.getFileExtension(selected_File.getName()))) {
                        btnPrint.setEnabled(true);
                    } else {
                        btnPrint.setEnabled(false);
                    }                        
                }                    
            } else {
                btnCut.setEnabled(false);
                btnCopy.setEnabled(false);
                if(!clipBoard.isEmpty()) {
                    if(clipBoard.getCurrentContentType() == clipBoard.CONTENT_TYPE_FILE) {
                        btnPaste.setEnabled(true);
                    }
                } else {
                    btnPaste.setEnabled(false);  
                }   
                btnDelete.setEnabled(false);
                btnPrint.setEnabled(false);             
            }       
        }    
    }
    
    
    
    public void mouseClicked(MouseEvent e){
        int index = lstContent.locationToIndex(e.getPoint());
        String value = String.valueOf(lstmdlContent.elementAt(index));
        if(e.getButton() == e.BUTTON1) {
            if (e.getClickCount() == 2) {
                if(current_Location == SYSTEM_ROOTS) {
                    setLocation(value);
                } else {
                    File f = new File(current_Location+value);
                    if(f.isDirectory()) {
                        setLocation(current_Location+value+"\\");      
                    } else {
                    
                        String fx = fm.getFileExtension(f.getName());
                        
                        if(fx.equals("txt") || fx.equals("rtf") || fx.equals("c") || fx.equals("h") || fx.equals("cpp") || fx.equals("cxx") || fx.equals("java")) {
                            setUNV_File(new UNV_TextFile(f), f);
                        } else if(fx.equals("jpg") || fx.equals("jpeg") || fx.equals("gif") || fx.equals("png")) {
                            setUNV_File(new UNV_ImageFile(f), f);
                        } else if(fx.equals("html") || fx.equals("htm")) {
                            setUNV_File(new UNV_HTML_File(f), f);
                        } else if(fx.equals("jar")) {
                        	if(MDS.getProcessManager().isMDS_Executable(f)) {
                        		fm.executeFile(f);
                        	} else {
                            	setUNV_File(new UNV_JarFile(f), f);
                        	}
                        } else if(fx.equals("zip")) {
                            setUNV_File(new UNV_ZipFile(f), f);
                        } else {
                            fm.executeFile(f);
                        }    
                    }
                } 
            } else if (e.getClickCount() == 1) {
                 if(current_Location == SYSTEM_ROOTS) {
                     pnlSumContainer_1.removeAll();
                     pnlSumContainer_1.add(new Sum_SystemRoots(new File(value)), BorderLayout.NORTH);     
                     pnlSumContainer_1.validate();
                 } else {
                     pnlSumContainer_1.removeAll();
                     pnlSumContainer_1.add(new Sum_FileDirDetails(new File(current_Location+value)), BorderLayout.NORTH);
                     pnlSumContainer_1.validate();
                     pnlSumContainer_1.repaint();
                 }    
                 
                 scrlpSummery.validate();
                 
            }  
        } else if(e.getButton() == e.BUTTON3) { 
            lstContent.setSelectedIndex(index);
            if(current_Location.equals(SYSTEM_ROOTS)) {
                ddm.showDrivePopupMenu(this, lstContent, e.getX(), e.getY(), new File(value)); 
            } else {
                fm.showFilePopupMenu(this, lstContent, e.getX(), e.getY(), new File(current_Location+value));
            }
        }      
    }



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){}
        
        
        
    public void mouseReleased(MouseEvent e) {}
    
    
    
    public void mouseDragged(MouseEvent e) {}
    
    
    
    public void mouseMoved(MouseEvent e) {/*
        int index = lstContent.locationToIndex(e.getPoint());
        String value = String.valueOf(lstmdlContent.elementAt(index));
        if(current_Location == SYSTEM_ROOTS) {
            lstContent.setToolTipText(value);     
        } else {
            lstContent.setToolTipText(value);     
        }   */
    }   
    
    
    
    public void actionPerformed(ActionEvent e) {    
    
        if(e.getActionCommand().equals("Up")) {
        
            if(current_Location.equals(SYSTEM_ROOTS)) return;
        
            File f = new File(current_Location);
            String newloc = null;        
            
            if(current_Open_File != null) {
                f = new File(current_Open_File.getPath());    
            }
            
            if(vctSystemRoots.contains(f.getPath())) {
                scrlp_Content.setViewportView(null);
                scrlp_Content.setViewportView(lstContent);
                setLocation(SYSTEM_ROOTS);                
            } else {                
                newloc = f.getParent();
                                   
                if(!vctSystemRoots.contains(newloc)) {
                   newloc = newloc.concat("\\");
                }
                scrlp_Content.setViewportView(null);
                scrlp_Content.setViewportView(lstContent);                                                 
                setLocation(newloc);          

            }             
        } else if(e.getActionCommand().equals("Back")) {
            String loc = lq.get_Before();
            if(!loc.equals("")) {
                scrlp_Content.setViewportView(null);
                scrlp_Content.setViewportView(lstContent);            
                setLocation(loc, false);     
            }   
        } else if(e.getActionCommand().equals("Forword")) {
            String loc = lq.get_After();
            if(!loc.equals("")) {
                scrlp_Content.setViewportView(null);
                scrlp_Content.setViewportView(lstContent);            
                setLocation(loc, false);     
            }
        } else if(e.getActionCommand().equals("Open")) {
            if(current_Location.equals(SYSTEM_ROOTS)) {
                setLocation(selected_File.getPath()+"\\");
            } else {
                if(selected_File.isDirectory()) {
                    setLocation(selected_File.getPath()+"\\");
                } else {
//                    this.openFile(new FileSystemEvent(selected_File));
                }
            }
        } else if(e.getActionCommand().equals("Compress")) {
            if(selected_File.isDirectory()) {
                cmpr.compress_Multiple_ZIP(selected_File);
            } else {
                cmpr.compress_Multiple_ZIP(selected_File);
            }    
        } else if(e.getActionCommand().equals("Decompress")) {
            if(selected_File.isDirectory()) {
                cmpr.decompress_Multiple_ZIP(selected_File);
            } else {
                cmpr.decompress_Multiple_ZIP(selected_File);
            }    
        } else if(e.getActionCommand().equals("Home Directory")) {
            if(selected_File.isDirectory()) {
                MDS.getFileManager().copyFile(selected_File, new File(System.getProperty("user.home")+"\\"),true);    
            } else {
                MDS.getFileManager().copyFile(selected_File, new File(System.getProperty("user.home")+"\\"+selected_File.getName()), true);    
            }           
        } else if(e.getActionCommand().equals("3.5 Floppy")) {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            if(fsv.isFloppyDrive(new File("A:\\"))) {
                if(selected_File.isDirectory()) {
                    MDS.getFileManager().copyFile(selected_File, new File("A:\\"),true);    
                } else {
                    MDS.getFileManager().copyFile(selected_File, new File("A:\\"+selected_File.getName()), true);    
                }                        
            }          
        } else if(e.getActionCommand().equals("Folder")) {
            try {
                FileSystemView fsv = FileSystemView.getFileSystemView();
                File f = fsv.createNewFolder(new File(current_Location));
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f));
            } catch (Exception ex) {
                MDS_OptionPane.showMessageDialog(this, "Uable to create the Directory.", "Error Creating Directory", JOptionPane.ERROR_MESSAGE); 
            }        
        } else if(e.getActionCommand().equals("Text Document")) {
            /*
            String nfn = "";  
            nfn = MDS_OptionPane.showInputDialog(this, "New text file Name", "New text file", JOptionPane.QUESTION_MESSAGE);      
            if(!nfn.equals("")) {
                File f = new File(current_Location, nfn);
                if(!nfn.endsWith(".txt")) {
                    f = new File(current_Location, nfn+".txt");
                }
                if(!f.exists()) {
                    try {
                        f.createNewFile();
                        //this.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.ADD_FILE));
                        MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f));
                    } catch(Exception ex) {
                        MDS_OptionPane.showMessageDialog(this, ex.getMessage(), "Error Creating File", JOptionPane.ERROR_MESSAGE); 
                    }    
                } else {
                    MDS_OptionPane.showMessageDialog(this, "File already exists ("+nfn+")", "File Creation", JOptionPane.INFORMATION_MESSAGE);    
                }
            }*/  
            fm.showCreateNewFile_Dialog(this, current_Location, ".txt", "Text");        
        } else if(e.getActionCommand().equals("HTML Document")) {
            fm.showCreateNewFile_Dialog(this, current_Location, ".html", "HTML"); 
        } else if(e.getActionCommand().equals("MSDOS Batch File")) {
            fm.showCreateNewFile_Dialog(this, current_Location, ".bat", "Batch"); 
        } else if(e.getActionCommand().equals("Delete")) {
            if(MDS_OptionPane.showConfirmDialog(this.getListener__MDS_Frame(), "Are you sure you want to delete "+ selected_File.getName(), "Confirm File Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                if(selected_File.isDirectory()) {
                    fm.deleteDir(selected_File);
                } else {
                    if(!selected_File.delete()) {
                        MDS_OptionPane.showMessageDialog(this.getListener__MDS_Frame(), "Uable to Delete the File / Directory.", "Error Deleting File / Directory", JOptionPane.ERROR_MESSAGE);                          
                    } else {
////                        MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DELETE_FILE, selected_File));
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
                File fn = new File(fm.getFilePathOnly(selected_File.getPath())+nfn);
                if(!selected_File.renameTo(fn)) {
                    MDS_OptionPane.showMessageDialog(this, "Uable to rename the Directory.", "Error Renaming File / Directory", JOptionPane.ERROR_MESSAGE);      
                } else {
////                    MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.RENAME_FILE, fn, selected_File));
                }        
            }         
        } else if(e.getActionCommand().equals("Properties")) {
            fm.showFileProperties(selected_File);
        } else if(e.getActionCommand().equals("Cut")) {
            clipBoard.setContent(selected_File ,clipBoard.STATUS_MOVED);
        } else if(e.getActionCommand().equals("Copy")) {
            clipBoard.setContent(selected_File ,clipBoard.STATUS_COPIED);
        } else if(e.getActionCommand().equals("Paste")) {
            try {
                File f = (File)clipBoard.getContent();
                if(!current_Location.equals(SYSTEM_ROOTS)) {
                    if(f.exists()) {
                        if(f.isDirectory()) {
                            MDS.getFileManager().copyFile(f, new File(current_Location) ,true);
                        } else {
                            MDS.getFileManager().copyFile(f, new File(current_Location+f.getName()), true); 
                        }
                    } else {
                        MDS_OptionPane.showMessageDialog(this.getListener__MDS_Frame(), "File not found ("+f.getPath()+")", "File Copy", JOptionPane.ERROR_MESSAGE);    
                    }
                } else {
                    
                }
            } catch (Exception ex) {
            	throw new RuntimeException(ex);
                //MDS.getExceptionManager().showException(ex); 
            }      
        } else if(e.getActionCommand().equals("Copy To")) {
            String des = "";
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this.getListener__MDS_Frame()) ==  fmdc.APPROVE_OPTION) {
                    des = fmdc.getPath();
                if(selected_File.isDirectory()) {
                    MDS.getFileManager().copyFile(selected_File, new File(des), true);
                } else {
                    MDS.getFileManager().copyFile(selected_File, new File(des+selected_File.getName()), true);    
                }                         
            }         
        } else if(e.getActionCommand().equals("Move To")) {
            String des = "";
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this.getListener__MDS_Frame()) ==  fmdc.APPROVE_OPTION) {
                des = fmdc.getPath();
                if(selected_File.isDirectory()) {
                	throw new ConcurrentModificationException("File / Directory deletion error. File / Directory is being used by another program or IO stream");
                    //MDS.getExceptionManager().showException(new ConcurrentModificationException("File / Directory deletion error. File / Directory is being used by another program or IO stream"));    
                } else {
                    if(!selected_File.canWrite()) {
                        if(MDS_OptionPane.showConfirmDialog(this.getListener__MDS_Frame(), "Are you sure you want move the Read Only file : "+selected_File.getName(), "Confirm File Move",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {      
                            MDS.getFileManager().moveFile(selected_File, new File(des+selected_File.getName()), true); 
                        }                                
                    } else {
                        MDS.getFileManager().moveFile(selected_File, new File(des+selected_File.getName()), true); 
                    }
                }
            }         
        } else if(e.getActionCommand().equals("Find Files")) {
            if(current_Location.equals(SYSTEM_ROOTS)) prm.execute(new File(MDS.getBinaryPath(), "FindFiles.class"));
            else prm.execute(new File(MDS.getBinaryPath(), "FindFiles.class"), new String[] {current_Location});   
        } else if(e.getActionCommand().equals("Clip Board Viewer")) { 
            prm.execute(new File(MDS.getBinaryPath(), "ClipBoardViewer.class"));
        } else if(e.getActionCommand().equals("Redirected StandardOutput Viewer")) {
            prm.execute(new File(MDS.getBinaryPath(), "RedirectedStandardOutputViewer.class"));  
        } else if(e.getActionCommand().equals(" Go >>")) {
            String text = String.valueOf(cboLocation.getSelectedItem());
            if(text.equalsIgnoreCase(SYSTEM_ROOTS)) {
                setLocation(SYSTEM_ROOTS);    
            } else {
                File f = new File(text);
                if(!f.exists()) {
                    if(f.isDirectory()) MDS_OptionPane.showMessageDialog(this, "Directory does not exists. \n"+f.getPath(), "Matrix File Browser", JOptionPane.ERROR_MESSAGE);
                    else MDS_OptionPane.showMessageDialog(this, "File does not exists.\n"+f.getPath(), "Matrix File Browser", JOptionPane.ERROR_MESSAGE);
                    update_ToolBar();  
                    return;
                }                
                if(f.isDirectory()) {
                    setLocation(f.getPath());
                } else {
                    open_UNV(f);    
                }
            }              
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "Matrix File Browser", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"matrix-fileBrowser.png"), MDS.getAbout_Mahesh()); 
            update_ToolBar(); 
        } else if(e.getActionCommand().equals("Print")) {
            if(selected_File.getName().endsWith("png") || selected_File.getName().endsWith("gif") || selected_File.getName().endsWith("jpg") || selected_File.getName().endsWith("jpeg")) {
                MDS.getPrinter().print(new ImageIcon(selected_File.getPath())); 
            }         
        }           

    }
    
    
    
    public void fileSystemUpdated(FileSystemEvent e) {
////////        if(e.getType() == e.REFRESH) {
////////            setLocation(current_Location);
////////        } else if(e.getFile() != null) {
////////            if(fm.getFilePathOnly(e.getFile().getPath()).equals(current_Location)) { 
////////                if(e.getActualType() == e.ABSTRACT_REMOVE_FILE) { 
////////                    set_lstContent_RemoveFile(e.getFile());
////////                } else if(e.getActualType() == e.ABSTRACT_ADD_FILE) {
////////                    set_lstContent_AddFile(e.getFile());
////////                } else if(e.getActualType() == e.ABSTRACT_EDIT_FILE) {
////////                    if(e.previousFile() != null) set_lstContent_EditFile(e.getFile(), e.previousFile());
////////                }                
////////            }
////////        }

//		System.out.println("XXXXXXXXXXXXXXX fileSystemUpdated");


    	log.info("fileSystemUpdated");
        File oldFile = e.getOldFile();
        File newFile = e.getNewFile();
        
        if(e.getType() == e.FILE_DELETED) {
        	log.info("FILE_DELETED oldFile : "+oldFile.getPath()); 
            set_lstContent_RemoveFile(oldFile);        
        } else if(e.getType() == e.FILE_CREATED) {
        	log.info("FILE_CREATED"); 
            set_lstContent_AddFile(newFile);     
        } else if(e.getType() == e.FILE_RENAMED) {
        	log.info("FILE_RENAMED"); 
            set_lstContent_EditFile(newFile, oldFile);    
        }        

    }
    
    
    
    public void openFile(File f) {
        if(f.isDirectory()) {
            setLocation(f.getPath()+"\\");    
        } else {
            fm.executeFile(f.getPath());
        }    
    }
    
    
    
    public MDS_Frame getListener__MDS_Frame() {
        return this;
    }
    
    
    
    public void menuCanceled(MenuEvent e) {}
    
    
    
    public void menuDeselected(MenuEvent e) {}
    
    
    
    public void menuSelected(MenuEvent e)  {                 
        if(e.getSource() == mnuFile) {
            if(current_Location.equals(SYSTEM_ROOTS)) {
                if(lstContent.getSelectedIndex() != -1) {
                    mniOpen.setEnabled(true);
                    mniCompress.setText("Compress");
                    mniCompress.setActionCommand("Compress");
                    mniCompress.setEnabled(false);
                    mniHomeDir.setEnabled(false);
                    mniFloppy.setEnabled(false);
                    mniFolder.setEnabled(false);
                    mniText.setEnabled(false);
                    mniHTML.setEnabled(false);
                    mniBatch.setEnabled(false);
                    mniDelete.setEnabled(false);
                    mniRename.setEnabled(true);
                    mniProperties.setEnabled(true);
                } else {
                    mniOpen.setEnabled(false);
                    mniCompress.setText("Compress");
                    mniCompress.setActionCommand("Compress");
                    mniCompress.setEnabled(false);
                    mniHomeDir.setEnabled(false);
                    mniFloppy.setEnabled(false);
                    mniFolder.setEnabled(false);
                    mniText.setEnabled(false);
                    mniHTML.setEnabled(false);
                    mniBatch.setEnabled(false);
                    mniDelete.setEnabled(false);                    
                    mniRename.setEnabled(false);
                    mniProperties.setEnabled(false);                    
                }
            } else {
                if(lstContent.getSelectedIndex() != -1) {
                    if(selected_File.isDirectory()) {
                        mniCompress.setText("Compress");
                        mniCompress.setActionCommand("Compress");
                    } else {
                        if(selected_File.getName().endsWith(".zip")) {
                            mniCompress.setText("Decompress");
                            mniCompress.setActionCommand("Decompress");    
                        } else {
                            mniCompress.setText("Compress");
                            mniCompress.setActionCommand("Compress");
                        }
                    }
                    mniCompress.setEnabled(true);        
                    mniHomeDir.setEnabled(true);
                    mniFloppy.setEnabled(true);
                    mniDelete.setEnabled(true);                    
                    mniRename.setEnabled(true);
                    mniProperties.setEnabled(true);                                        
                } else {
                    mniCompress.setText("Compress"); 
                    mniCompress.setActionCommand("Compress");  
                    mniCompress.setEnabled(false);
                    mniHomeDir.setEnabled(false);
                    mniFloppy.setEnabled(false);  
                    mniDelete.setEnabled(false);                    
                    mniRename.setEnabled(false);
                    mniProperties.setEnabled(false);                                  
                }
                mniFolder.setEnabled(true);
                mniText.setEnabled(true);
                mniHTML.setEnabled(true);
                mniBatch.setEnabled(true);                
            }             
        } else if(e.getSource() == mnuEdit) {
            if(current_Location.equals(SYSTEM_ROOTS)) {
                if(lstContent.getSelectedIndex() != -1) {
                    mniCut.setEnabled(false);
                    mniCopy.setEnabled(true);
                    mniPaste.setEnabled(false);
                    mniCopyTo.setEnabled(true);
                    mniMoveTo.setEnabled(false);                        
                } else {
                    mniCut.setEnabled(false);
                    mniCopy.setEnabled(false);
                    mniPaste.setEnabled(false);
                    mniCopyTo.setEnabled(false);
                    mniMoveTo.setEnabled(false);
                }
            } else {
                if(lstContent.getSelectedIndex() != -1) {
                    mniCut.setEnabled(true);
                    mniCopy.setEnabled(true);
                    if(!clipBoard.isEmpty()) {
                        if(clipBoard.getCurrentContentType() == clipBoard.CONTENT_TYPE_FILE) {
                            mniPaste.setEnabled(true);
                        }
                    } else {
                        mniPaste.setEnabled(false);  
                    }                    
                    mniCopyTo.setEnabled(true);
                    mniMoveTo.setEnabled(true);                 
                } else {
                    mniCut.setEnabled(false);
                    mniCopy.setEnabled(false);
                    if(!clipBoard.isEmpty()) {
                        if(clipBoard.getCurrentContentType() == clipBoard.CONTENT_TYPE_FILE) {
                            mniPaste.setEnabled(true);
                        }
                    } else {
                        mniPaste.setEnabled(false);  
                    }
                    mniCopyTo.setEnabled(false);
                    mniMoveTo.setEnabled(false);                
                }
            }            
        }        
    }
    
    
    
    public void valueChanged(ListSelectionEvent e) {
        if(lstContent.getSelectedIndex() != -1) {
            if(current_Location.equals(SYSTEM_ROOTS)) {
                selected_File = new File(String.valueOf(lstContent.getSelectedValue()));   
            } else {
                selected_File = new File(current_Location, String.valueOf(lstContent.getSelectedValue())); 
            }
        } else {
            selected_File = null;
        }
        update_ToolBar();
    }
    
    
    
    public void clipBoard_CopyTo() {
        update_ToolBar();
    }
    
    
    
    public void clipBoard_MoveTo() {
        update_ToolBar();
    }
    
    
    
    public void clipBoard_Paste_Copied() {
        update_ToolBar();
    }
    
    
    
    public void clipBoard_Paste_Moved() {}
    
    
    
    public void clipBoard_Empty() {
        update_ToolBar();
    }
    
    
    
    public void keyPressed(KeyEvent e) {}



    public void keyReleased(KeyEvent e) {}



    public void keyTyped(KeyEvent e) {}
    
    
    
    public void dragGestureRecognized(DragGestureEvent event) {
        if(!current_Location.equals(SYSTEM_ROOTS)) {
            String draggedValue = current_Location+String.valueOf(lstContent.getSelectedValue());    
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
    
    
    
    public void dragOver(DropTargetDragEvent e) {}
    
    
    
    public void drop(DropTargetDropEvent e) {
        
        try { 
            Transferable transferable = e.getTransferable();
            File f = new File(String.valueOf(transferable.getTransferData(DataFlavor.stringFlavor)));
            if(f.exists()) {
                String parent = f.getParent();
                if(!parent.endsWith("\\")) parent = parent.concat("\\"); 
                if(current_Location.equals(parent)) {
                    e.rejectDrop();
                    MDS_OptionPane.showMessageDialog(this, "Cannot move '"+f.getName()+"' The source and the destination file names are the same.", "Error Moving File or Directory", JOptionPane.ERROR_MESSAGE);       
                } else {

                    e.dropComplete(true);
                    
                    if(MDS_OptionPane.showConfirmDialog(this, "Are you sure you want to copy '"+ f.getName()+"'\n\n"+"Source : "+f.getPath()+"\nDestination : "+current_Location+"\n\n", "File Browser [Confirm File Copy]", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    
                        if(f.isDirectory()) {
                            MDS.getFileManager().copyFile(f, new File(current_Location), true);
                        } else {
                            MDS.getFileManager().copyFile(f, new File(current_Location, f.getName()), true);    
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
	        setVerticalAlignment(SwingConstants.CENTER);
	        setHorizontalAlignment(SwingConstants.CENTER);
	        setVerticalTextPosition(SwingConstants.BOTTOM);
	        setHorizontalTextPosition(SwingConstants.CENTER);
	        setIconTextGap(-3);

            switch(os.getDriveType(text)) {
                case OperatingSystem.DRIVE_UNKNOWN:     
                    setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"root-unknown.png")); 
                    break;
                case OperatingSystem.DRIVE_NO_ROOT_DIR:
                    setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"network-drive.png"));      
                	break;
                case OperatingSystem.DRIVE_REMOVABLE:
                    FileSystemView fsv = FileSystemView.getFileSystemView(); 
                    if(fsv.isFloppyDrive(new File(text))) { 
                        setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-3floppy.png"));
                    } else {
                        setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-usbpendrivet.png"));
                    }              
                    break;
                case OperatingSystem.DRIVE_FIXED:
                    setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"64-device-hdd.png"));
                    this.setToolTipText(text);
                    break;
                case OperatingSystem.DRIVE_REMOTE:
                    setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"network-drive.png"));
                    break;
                case OperatingSystem.DRIVE_CDROM:
                    setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"cdd.png"));
                    break;
                case OperatingSystem.DRIVE_RAMDISK:
                    setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"jazdisk.png")); 
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
	        setVerticalAlignment(SwingConstants.CENTER);
	        setHorizontalAlignment(SwingConstants.CENTER);
	        setVerticalTextPosition(SwingConstants.BOTTOM);
	        setHorizontalTextPosition(SwingConstants.CENTER);
	        //setIconTextGap(10);
				
            File f = new File(current_Location+text);
            /*
            if(f.getParent().equals(System.getProperty("user.dir"))) {
                if(fm.getFileExtension(f.getName()).equals("class")) {
                    try {
                        Class cls = Class.forName(fm.getFileName_WithoutExtention(f.getName()));    
                        Method m = c1s.getMethod("MDS_Main", new Class[] { args.getClass() });
                        if(m != null) {
                            setIcon();    
                        }
                    } catch(Exception ex) {}
                }
            } else {*/
            //MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f);
			
            setIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(f, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
	      //}  
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
                setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "disks.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));  
                setText(text);  
            } else {
                File f = new File(text);
                if(f.isDirectory()) {
                    setIcon(ImageManipulator.createScaledImageIcon(fm.getDefault_Directory_Icon(), 16, 16, ImageManipulator.ICON_SCALE_TYPE));  
                    setText(text);
                } else {
                    setIcon(ImageManipulator.createScaledImageIcon(fm.getFileType_Icon(f), 16, 16, ImageManipulator.ICON_SCALE_TYPE));  
                    setText(text);            
                }                
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
    
    
    
    
    
    class Sum_SystemTasks extends MDS_Panel implements MouseListener {
    
    
    
        private HyperlinkLabel hplbSysInfo = new HyperlinkLabel("View System Information", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"System Information.png"));
        private HyperlinkLabel hplbChangeSettings = new HyperlinkLabel("Change Settings", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"settings.png"));
        private HyperlinkLabel hplbSystemManager = new HyperlinkLabel("System Manager", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"TaskManager.png"));
        private HyperlinkLabel hplbClipBoardViewer = new HyperlinkLabel("Clip Board Viewer", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"clipBoard.png"));
    
    
    
        public Sum_SystemTasks() {
            super(new GridLayout(4, 1, 0, 5));
            this.setOpaque(false);
            this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),"System Tasks"));           
            hplbSysInfo.addMouseListener(this);
            this.add(hplbSysInfo);
            hplbChangeSettings.addMouseListener(this);
            this.add(hplbChangeSettings);
            hplbSystemManager.addMouseListener(this);
            this.add(hplbSystemManager);
            hplbClipBoardViewer.addMouseListener(this);
            this.add(hplbClipBoardViewer);
        }
        
        
        
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == e.BUTTON1) {
                if (e.getClickCount() == 1) {
                    if(e.getSource() == hplbSysInfo) {
                        prm.execute(new File(MDS.getBinaryPath(), "SystemInformation.class"));
                    } else if(e.getSource() == hplbChangeSettings) {  
                        prm.execute(new File(MDS.getBinaryPath(), "ControlCenter.class"));
                    } else if(e.getSource() == hplbSystemManager) {
                        prm.execute(new File(MDS.getBinaryPath(), "SystemManager.class"));
                    } else if(e.getSource() == hplbClipBoardViewer) {
                        prm.execute(new File(MDS.getBinaryPath(), "ClipBoardViewer.class"));
                    }  
                }
            }    
        }
        
        
        
        public void mouseEntered(MouseEvent e) {}
        
        
        
        public void mouseExited(MouseEvent e) {}
        
        
        
        public void mousePressed(MouseEvent e) {}
        
        
        
        public void mouseReleased(MouseEvent e) {}
        
    } 
    
    
    


    class Sum_OtherPlaces extends MDS_Panel implements MouseListener {
    
    
    
        private HyperlinkLabel hplbSysRoots = new HyperlinkLabel("System Roots", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"disks.png"));
        private HyperlinkLabel hplbHome = new HyperlinkLabel("User Home", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"home.png"));
        private HyperlinkLabel hplbMDS = new HyperlinkLabel("MDS Directory", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds.png"));
        private MatrixFileBrowser mtx = null;
    
    
        public Sum_OtherPlaces(MatrixFileBrowser mt) {
            super(new GridLayout(3, 1, 0, 5));
            mtx = mt;
            this.setOpaque(false);
            this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),"Other Places"));           
            hplbSysRoots.addMouseListener(this);
            this.add(hplbSysRoots);
            hplbHome.addMouseListener(this);
            this.add(hplbHome);
            hplbMDS.addMouseListener(this);
            this.add(hplbMDS);            
        }
        
        
        
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == e.BUTTON1) {
                if (e.getClickCount() == 1) {
                    if(e.getSource() == hplbSysRoots) {
                        mtx.setLocation(SYSTEM_ROOTS); 
                    } else if(e.getSource() == hplbHome) {  
                        mtx.setLocation(fm.getUserHomeDir());
                    } else if(e.getSource() == hplbMDS) {  
                        mtx.setLocation(fm.getMDS_Dir().getPath());
                    }   
                }
            }    
        }
        
        
        
        public void mouseEntered(MouseEvent e) {}
        
        
        
        public void mouseExited(MouseEvent e) {}
        
        
        
        public void mousePressed(MouseEvent e) {}
        
        
        
        public void mouseReleased(MouseEvent e) {}        
        
    } 
    
    
    
    
    
    class Sum_SystemRoots extends MDS_Panel {
    
    
    
        private MDS_Label lblRootName;
        private MDS_Label lblType;
        private MDS_Label lblFileSystem;
        private MDS_Label lblFreeSpace;
        private MDS_Label lblTotalSpace;

    
        public Sum_SystemRoots(File f) {
            super(new GridLayout(5, 1, 0, 5));
            this.setOpaque(false);
            this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),"Details"));           
            
            lblRootName = new MDS_Label();
            lblRootName.setFont(new Font(lblRootName.getFont().getName(), lblRootName.getFont().getStyle(), 12));
            
            lblType = new MDS_Label();
            lblType.setFont(new Font(lblType.getFont().getName(), Font.PLAIN, 11));

            lblFileSystem = new MDS_Label();
            lblFileSystem.setFont(new Font(lblFileSystem.getFont().getName(), Font.PLAIN, 11));

            lblFreeSpace = new MDS_Label();
            lblFreeSpace.setFont(new Font(lblFreeSpace.getFont().getName(), Font.PLAIN, 11));
            
            lblTotalSpace = new MDS_Label();
            lblTotalSpace.setFont(new Font(lblFreeSpace.getFont().getName(), Font.PLAIN, 11));                                    
            
            switch(os.getDriveType(f.getPath())) {
                case OperatingSystem.DRIVE_UNKNOWN: 
                    lblRootName.setText(f.getPath());
                    lblType.setText("Drive Unknown");                    
                    break;
                case OperatingSystem.DRIVE_NO_ROOT_DIR:
                    lblRootName.setText(f.getPath());
                    lblType.setText("No Root Dir");
                	  break;
                case OperatingSystem.DRIVE_REMOVABLE:
                    FileSystemView fsv = FileSystemView.getFileSystemView(); 
                    if(fsv.isFloppyDrive(new File(f.getPath()))) { 
                        lblRootName.setText(f.getPath());
                        lblType.setText("Floppy Drvie");
                    } else {
                        lblRootName.setText(f.getPath()); 
                        lblType.setText("Removable Drvie");   
                    }              
                    break;
                case OperatingSystem.DRIVE_FIXED:
                    lblRootName.setText(os.getVolumeName(f.getPath())+" ["+f.getPath()+"]"); 
                    lblType.setText("Fixed Drive");
                    lblFileSystem.setText("File System: "+os.getFileSystemName(f.getPath()));
                    lblFreeSpace.setText("Free Space: "+fm.getFormatedFileSize(f.getFreeSpace()));
                    lblTotalSpace.setText("Total Size: "+fm.getFormatedFileSize(f.getTotalSpace()));
                    break;
                case OperatingSystem.DRIVE_REMOTE:
                    lblRootName.setText(f.getPath());
                    lblType.setText("Remote Drive");
                    break;
                case OperatingSystem.DRIVE_CDROM:
                    lblRootName.setText(f.getPath());
                    lblType.setText("CD Drive");
                    break;
                case OperatingSystem.DRIVE_RAMDISK:
                    lblRootName.setText(f.getPath());
                    lblType.setText("Ram Disk");
                    break;        
                default:
                                
            }            
            
            this.add(lblRootName);
            this.add(lblType);
            this.add(lblFileSystem);
            this.add(lblFreeSpace);
            this.add(lblTotalSpace);
        }
        
    }           
    
    
    
    
    
    class Sum_FileDirDetails extends MDS_Panel {
    
    
    
        MDS_Label lblPreview = new MDS_Label();
        MDS_Panel pnlDetails = new MDS_Panel();
    
    
    
        public Sum_FileDirDetails(File f) {
            super();
            if(!f.isDirectory()) {
                String ex = fm.getFileExtension(f.getName());
                if(ex.equals("png") || ex.equals("gif") || ex.equals("jpg")) {
                    this.setLayout(new BorderLayout()); 
                    ImageIcon tmpIcon = new ImageIcon(f.getPath());
                    if(tmpIcon.getIconWidth() > 160) {
                        lblPreview.setIcon(new ImageIcon(tmpIcon.getImage().getScaledInstance(160, -1, Image.SCALE_FAST)));
                    } else {
                        lblPreview.setIcon(tmpIcon);
                    }
	              lblPreview.setVerticalAlignment(SwingConstants.CENTER);
	                lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
                    this.add(lblPreview, BorderLayout.NORTH); 
                    
                    pnlDetails.setOpaque(false);
                    pnlDetails.setLayout(new GridLayout(5, 1, 0, 5));
                    pnlDetails.add(new MDS_Label(f.getName()));
                    pnlDetails.add(new Sum_Display_Label(fm.getFileType(f.getName())));
                    pnlDetails.add(new Sum_Display_Label("Dimensions: "+String.valueOf(tmpIcon.getIconWidth())+" x "+String.valueOf(tmpIcon.getIconHeight())));
                    pnlDetails.add(new Sum_Display_Label("Size: "+fm.getFormatedFileSize(f.length())));
                    pnlDetails.add(new Sum_Display_Label("Last Modified: "+fm.getLastModified_As_String(f.lastModified())));                             
                    this.add(pnlDetails, BorderLayout.SOUTH);
                } else {
                    this.setLayout(new GridLayout(4, 1, 0, 5)); 
                    this.add(new MDS_Label(f.getName()));
                    this.add(new Sum_Display_Label(fm.getFileType(f.getName())));
                    this.add(new Sum_Display_Label("Size: "+fm.getFormatedFileSize(f.length())));
                    this.add(new Sum_Display_Label("Last Modified: "+fm.getLastModified_As_String(f.lastModified())));                        
                }
            } else {
                this.setLayout(new GridLayout(3, 1, 0, 5));
                this.add(new MDS_Label(f.getName()));
                this.add(new Sum_Display_Label("Size: "+fm.getFormatedFileSize(f.length())));
                this.add(new Sum_Display_Label("Last Modified: "+fm.getLastModified_As_String(f.lastModified())));                 
            }
            this.setOpaque(false);
            this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),"File Directory Details")); 

        }
        
    }
    
    
    
    
    
    class Sum_FileDirTasks extends MDS_Panel {
    
    
    
        public Sum_FileDirTasks() {
        
        }
        
        
        
    }    
    
    
    
    
    
    class HyperlinkLabel extends MDS_Label {
    
    
    
         public HyperlinkLabel(String text, ImageIcon i) {
             super(text, ImageManipulator.createScaledImageIcon(i,20 ,20 ,ImageManipulator.ICON_SCALE_TYPE));
             this.setHyperlinkTextViewEnabled(true);
             this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 11));
         }
         
    }
    
    
    
    
    
    class Sum_Display_Label extends MDS_Label {
    
    
    
        public Sum_Display_Label(String text) {
            super(text);
            this.setOpaque(false);
            this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 11));
        }
        
        
        
    } 
    
    
    
    
    
    class UNV_TextFile extends MDS_TextArea {
    
    
    
        File file;
        UNV_TextFile u_tf;
    
    
    
        public UNV_TextFile(File f) {
            this.setText("Loading ....");
            this.setEditable(false);
            u_tf = this;
            file = f;
            loadSourceCode();    
        }
        
        
        
        public void loadSourceCode() {
            
            class LoadText extends Thread {
            
            
            
                public LoadText() {
                    super();
                    u_tf.setText("");
                    start();
                }
                
                
                
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String s;
                        while((s = br.readLine())!= null) {
                            u_tf.append(s+"\n");
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                    
                }
             
            }
            
            new LoadText();
            	
        }     

        
    }
    
    
    
    
    
    class UNV_ImageFile extends MDS_Label {
    
    
    
        public UNV_ImageFile(File f) {
            super(new ImageIcon(f.getPath()));        
        }
        
    }
    
    
    
    
    
    class UNV_HTML_File extends MDS_EditorPane { 
    
    
    
        public UNV_HTML_File(File f) {
            setEditable(false);
            try {
                setPage(new URL("file:" + f.getPath()));
            } catch(Exception ex) {
                setText("Error: " + ex.toString());
            }
        }
        
        
    }
    
    
    
    
    
    class UNV_JarFile extends JarFileContent {
    
    
    
        public UNV_JarFile(File f) {
            super(mfb);
            loadJarFileInfo(f);
        }
        
    } 
    
    
    
    
    
    class UNV_ZipFile extends ZipFileContent {
    
    
    
        public UNV_ZipFile(File f) {
            super(mfb);
            loadZipFileInfo(f);
        }
        
    }          
    
    
    
    
    
    class LocationQueue {
    
    
        
        final int MAXSIZE = 10;
         
        Vector vctQueue;
        int pointer = -1;
        int stopPoint = -1;
        
        boolean lock = true;
        
        String previouslyAdded_Location = null;
    
    
    
        public LocationQueue() {
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