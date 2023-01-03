
import javax.swing. *;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
//import com.sun.tools.javac.v8.*;
//import com.sun.tools.javac.*;
import javax.swing.text.*; 
import javax.tools.*; 
import java.util.logging.*;




public class Maximus extends MDS_Frame implements ActionListener, MouseListener, MenuListener, Runnable {

    private static char qt = '"'; 
    private static final String quote = String.valueOf(qt);
    
    private MDS_User usr = MDS.getUser();
    private FileManager fm = MDS.getFileManager();
    private StringTable st = new StringTable(); 
    private ProcessManager prm = MDS.getProcessManager();
     
    private JMenuBar mnb = new JMenuBar();
    
    private MDS_Menu mnuFile = new MDS_Menu("File", KeyEvent.VK_F);
    private JMenuItem mniNewClass = usr.createMenuItem("New Class", this, MDS_KeyStroke.getNew(), KeyEvent.VK_N);
    private JMenuItem mniAddJavaFile = usr.createMenuItem("Add Java Source File", this, MDS_KeyStroke.getNew(), KeyEvent.VK_J);
	private JMenuItem mniSave = usr.createMenuItem("Save ...", this, MDS_KeyStroke.getSave(), KeyEvent.VK_S);
    private JMenuItem mniSaveAs = usr.createMenuItem("Save As ...", this,  KeyEvent.VK_A);
    private JMenuItem mniSaveAll = usr.createMenuItem("Save All", this);
    private JMenuItem mniPageSetup = usr.createMenuItem("Page Setup", this);
    private JMenuItem mniPrint = usr.createMenuItem("Print ...", this, MDS_KeyStroke.getPrint(), KeyEvent.VK_P);
    private JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    
    private MDS_Menu mnuEdit = new MDS_Menu("Edit", KeyEvent.VK_E);
    private JMenuItem mniUndo = usr.createMenuItem("Undo", this, MDS_KeyStroke.getUndo(), KeyEvent.VK_Z);
    private JMenuItem mniCut = usr.createMenuItem("Cut", this, MDS_KeyStroke.getCopy(), KeyEvent.VK_C);
    private JMenuItem mniCopy = usr.createMenuItem("Copy", this, MDS_KeyStroke.getCut(), KeyEvent.VK_X);
    private JMenuItem mniPaste = usr.createMenuItem("Paste", this, MDS_KeyStroke.getPaste(), KeyEvent.VK_V);
    private JMenuItem mniDelete = usr.createMenuItem("Delete", this, MDS_KeyStroke.getDelete(), KeyEvent.VK_D);
    private JMenuItem mniSelectAll = usr.createMenuItem("Select All", this, MDS_KeyStroke.getSelectAll(), KeyEvent.VK_A);
    private JMenuItem mniGoTo = usr.createMenuItem("GoTo ...", this, MDS_KeyStroke.getSelectAll(), KeyEvent.VK_G);
	private JMenuItem mniFind = usr.createMenuItem("Find ...", this, MDS_KeyStroke.getSelectAll(), KeyEvent.VK_F);	

    private MDS_Menu mnuProject = new MDS_Menu("Project", KeyEvent.VK_P);
    private JMenuItem mniNewProject = usr.createMenuItem("New ...", this, KeyEvent.VK_N);
    private JMenuItem mniOpenProject = usr.createMenuItem("Open ...", this, KeyEvent.VK_O);
    private JMenuItem mniCloseProject = usr.createMenuItem("Close ...", this);
    private JMenuItem mniRuntimeProperties = usr.createMenuItem("Runtime Properties", this);
    
    private MDS_Menu mnuBuild = new MDS_Menu("Build", KeyEvent.VK_B);
    private JMenuItem mniCompile = usr.createMenuItem("Compile ...", this);
    private JMenuItem mniCompileAll = usr.createMenuItem("Compile *.java", this);
    
    private MDS_Menu mnuRun = new MDS_Menu("Run", KeyEvent.VK_R);
    private JMenuItem mniRun = usr.createMenuItem("Run Project", this);
    private JMenuItem mniConfigurations = usr.createMenuItem("Configuration", this);
    
    private MDS_Menu mnuTools = new MDS_Menu("Tools", KeyEvent.VK_T);
    private JMenuItem mniCCW = usr.createMenuItem("Class Connection Walker", this);
    private JMenuItem mniVTM = usr.createMenuItem("Virtual Threading Manager", this);
	private JMenuItem mniJBrowser = usr.createMenuItem("Java Class Browser", this);
	private JMenuItem mniJarFileCreator = usr.createMenuItem("Jar File Creator", this);
	private JMenuItem mniSysMgr = usr.createMenuItem("System Manager", this);
    
    private MDS_Menu mnuFormat = new MDS_Menu("Format", KeyEvent.VK_F);
    private JMenuItem mniFont = usr.createMenuItem("Font", this, KeyEvent.VK_F);
    private JMenuItem mniColorBackground = usr.createMenuItem("Background Color", this);
	private JMenuItem mniColorForeground = usr.createMenuItem("Foreground Color", this);    
     
    private MDS_Menu mnuHelp = new MDS_Menu("Help", KeyEvent.VK_H);
    private JMenuItem mniAbout = usr.createMenuItem("About", this, KeyEvent.VK_A);

    private JComponent contentPane;
    
    private MDS_ToolBar tlbMax = new MDS_ToolBar();
    
    private MDS_Button btnNew = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-filenew.png"));     
    private MDS_Button btnOpen = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-fileopen.png"));     
    private MDS_Button btnSave = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-filesave.png")); 
	private MDS_Button btnSaveAs = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-filesaveas.png"));  
    private MDS_Button btnSaveAll = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-save_all.png"));  
	private MDS_Button btnCompile = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-compile.png"));  
    private MDS_Button btnCompileAll = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-compileall.png"));      
	private MDS_Button btnRun = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-player_play.png"));  
	   
    private MDS_Button btnCut = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcut.png"));     
    private MDS_Button btnCopy = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcopy.png"));     
    private MDS_Button btnPaste = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editpaste.png"));     
    //private MDS_Button btnDelete = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"delete.png"));     
    private MDS_Button btnSearch = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-search.png"));     
    private MDS_Button btnGoTo = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-goto.png")); 
	private MDS_Button btnPrint = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-device-printer1.png")); 
	
	private MDS_Button btnCCW = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-device-blockdevice.png", 22 ,22 , ImageManipulator.ICON_SCALE_TYPE));  
	private MDS_Button btnJavaBrowser = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-app-ksplash.png", 22 ,22 , ImageManipulator.ICON_SCALE_TYPE));  
	private MDS_Button btnJarFileCreator = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-action-knewstuff.png", 22 ,22 , ImageManipulator.ICON_SCALE_TYPE));
	private MDS_Button btnVT = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-app-kbounce.png", 22 ,22 , ImageManipulator.ICON_SCALE_TYPE));  
    private MDS_Button btnSystemManager = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-systemtray.png", 22 ,22 , ImageManipulator.ICON_SCALE_TYPE));
	
    private DefaultMutableTreeNode rootProject;    
    private DefaultTreeModel dtmdlFiles;     
    private MDS_Tree treFiles = new MDS_Tree();
    private JScrollPane scrlSourceFile = new JScrollPane();
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private JSplitPane splpBase = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private Neo_TextArea txtaOutput = new Neo_TextArea();
    private MDS_Label lblStatusBar = new MDS_Label("");
    
    private Maximus max = null;
    
    private String projectName = "";
    private String mainClassName = "";
    private String applicationParameters = "";
    private File projectFile = null;
    private String projectPath = "";
    private Vector vctProjectScrFiles = new Vector();
    private JavaSourceFile currentJavaScrFile; 
    private boolean projectShouldSave = false;
    private boolean compileAll = true;
    private boolean shouldRun = false;
    private boolean projectAlreadyOpened = false;
    
    private Thread checkIllegalityThread;
    //private static Hashtable illegalClassList = new Hashtable();
    private static Hashtable illegalClassList = MDS_ClassLoader.getIllegalAndRecommendedClassList();
    private static Hashtable illegalClassNameList = MDS_ClassLoader.getIllegalAndRecommendedClassNameList();
    private Hashtable htProjectProperties = new Hashtable();
    private static final String PROJECT_NAME = "1";
    private static final String MAIN_CLASS_NAME = "2";
    private static final String APPLICATION_PARAMETERS = "3";
    
    private static Logger log = Logger.getLogger("Maximus");
    
     
    
    

    public Maximus() {
        super("Maximus",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-phppg.png"));       

        //mniNewClass.setEnabled(false);
        mnuFile.add(mniNewClass);
		mnuFile.add(mniAddJavaFile);
        mnuFile.add(mniSave);
        mnuFile.add(mniSaveAs);
        mnuFile.add(mniSaveAll);
        mnuFile.addSeparator();
        //mnuFile.add(mniPageSetup);
        mnuFile.add(mniPrint);
        mnuFile.addSeparator();
        mnuFile.add(mniExit);
        mnuFile.addMenuListener(this);
        mnb.add(mnuFile);
        
        mnuEdit.add(mniUndo);
        mnuEdit.add(mniCut);
        mnuEdit.add(mniCopy);
        mnuEdit.add(mniPaste);
        mnuEdit.add(mniDelete);
        mnuEdit.addSeparator();
        mnuEdit.add(mniSelectAll);
		mnuEdit.addSeparator();
		mnuEdit.add(mniGoTo);
		mnuEdit.add(mniFind);
        mnuEdit.addMenuListener(this);
        mnb.add(mnuEdit);
        
        mnuProject.add(mniNewProject);
        mnuProject.add(mniOpenProject);
        mnuProject.add(mniCloseProject); 
        mnuProject.add(mniRuntimeProperties);
        mnuProject.addMenuListener(this);       
        mnb.add(mnuProject);
        
        mnuBuild.add(mniCompile);
        mnuBuild.add(mniCompileAll);
        mnuBuild.addMenuListener(this);
        mnb.add(mnuBuild);
        
        mnuRun.addMenuListener(this);
        mnuRun.add(mniRun);
        mnuRun.add(mniConfigurations);
        mnb.add(mnuRun);
        
        mnuTools.add(mniCCW);
        mnuTools.add(mniVTM);
		mnuTools.add(mniJBrowser);
		mnuTools.add(mniJarFileCreator);
		mnuTools.add(mniSysMgr);
        mnb.add(mnuTools);
        
        mnuFormat.addMenuListener(this);
        mnuFormat.add(mniFont);
        mnuFormat.add(mniColorBackground);
		mnuFormat.add(mniColorForeground);
        //mnuFormat.addMenuListener(this);
        mnb.add(mnuFormat);
        
        mnuHelp.add(mniAbout);
        //mnuHelp.addMenuListener(this);
        mnb.add(mnuHelp);        
        
        contentPane = (JComponent) this.getContentPane(); 
        contentPane.setLayout(new BorderLayout());
        
		btnNew.setActionCommand("New Class");
		btnNew.addActionListener(this);
        tlbMax.add(btnNew);
		btnOpen.setActionCommand("Open ...");
		btnOpen.addActionListener(this);		
        tlbMax.add(btnOpen);
		btnSave.setActionCommand("Save ...");
		btnSave.addActionListener(this);
        tlbMax.add(btnSave);
		btnSaveAs.setActionCommand("Save As ...");
		btnSaveAs.addActionListener(this);
		tlbMax.add(btnSaveAs);
		btnSaveAll.setActionCommand("Save All");
		btnSaveAll.addActionListener(this);
		tlbMax.add(btnSaveAll);
		tlbMax.addSeparator();
		btnCompile.setActionCommand("Compile ...");
		btnCompile.addActionListener(this);		
        tlbMax.add(btnCompile);
		btnCompileAll.setActionCommand("Compile *.java");
		btnCompileAll.addActionListener(this);
		tlbMax.add(btnCompileAll);
		btnRun.setActionCommand("Run Project");
		btnRun.addActionListener(this);
		tlbMax.add(btnRun);
		tlbMax.addSeparator();
		btnCut.setActionCommand("Cut");
		btnCut.addActionListener(this);		
		tlbMax.add(btnCut);
		btnCopy.setActionCommand("Copy");
		btnCopy.addActionListener(this);
		tlbMax.add(btnCopy);
		btnPaste.setActionCommand("Paste");
		btnPaste.addActionListener(this);
		tlbMax.add(btnPaste);
		tlbMax.addSeparator(); 
		btnSearch.setActionCommand("Find ...");
		btnSearch.addActionListener(this);
		tlbMax.add(btnSearch);
		btnGoTo.setActionCommand("GoTo ...");
		btnGoTo.addActionListener(this);
		tlbMax.add(btnGoTo);
		tlbMax.addSeparator();
		btnPrint.setActionCommand("Print ...");
		btnPrint.addActionListener(this);
        tlbMax.add(btnPrint);
		tlbMax.addSeparator();
		tlbMax.addSeparator();
		btnCCW.setActionCommand(mniCCW.getActionCommand());
		btnCCW.addActionListener(this);
		tlbMax.add(btnCCW);
		btnJavaBrowser.setActionCommand(mniJBrowser.getActionCommand());
		btnJavaBrowser.addActionListener(this);
		tlbMax.add(btnJavaBrowser);
		btnJarFileCreator.setActionCommand(mniJarFileCreator.getActionCommand());
		btnJarFileCreator.addActionListener(this);
		tlbMax.add(btnJarFileCreator);		
		btnVT.setActionCommand(mniVTM.getActionCommand());
		btnVT.addActionListener(this);
		tlbMax.add(btnVT);
		btnSystemManager.setActionCommand(mniSysMgr.getActionCommand());
		btnSystemManager.addActionListener(this);
		tlbMax.add(btnSystemManager);
        tlbMax.setFloatable(false);
        contentPane.add(tlbMax, BorderLayout.NORTH);
        
        rootProject = new DefaultMutableTreeNode("< Project >");
        dtmdlFiles = new DefaultTreeModel(rootProject);  
        treFiles.setModel(dtmdlFiles);     
        
        splitPane.setDividerSize(5);   
        splitPane.setDividerLocation(150);
        treFiles.addMouseListener(this);
        splitPane.setLeftComponent(new JScrollPane(treFiles));
        splitPane.setRightComponent(scrlSourceFile);
        splpBase.setDividerSize(5);   
        splpBase.setDividerLocation(600);         
        splpBase.setTopComponent(splitPane);
        txtaOutput.setBackground(Color.black);
        txtaOutput.setForeground(new Color(250, 175, 3));
        splpBase.setBottomComponent(new JScrollPane(txtaOutput));       
        contentPane.add(splpBase, BorderLayout.CENTER);
        lblStatusBar.setBorder(BorderFactory.createEtchedBorder());    
        contentPane.add(lblStatusBar, BorderLayout.SOUTH);
        
        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);           
        
        this.setJMenuBar(mnb);
        this.setSize(800, 600);
        this.setCenterScreen();
        max = this;
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                if(anyScrFileShouldSave()) {
                    int r = MDS_OptionPane.showConfirmDialog(max, st.get(8), "Maximus", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                    if(r == JOptionPane.YES_OPTION) {
                        //saveProject(); 
                        saveScrFiles();
                        max.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE);
                        max.dispose();        
                    } else if(r == JOptionPane.NO_OPTION) {
                        max.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE); 
                        max.dispose();
                    } else if(r == JOptionPane.CANCEL_OPTION) {
                        
                    }
                } else {
                    max.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE); 
                    max.dispose();                
                }
  
            }
        });         
        
        //initialize_illegalClassList();
		
		updateToolBar();
        
        this.setVisible(true);
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        Maximus max = new Maximus();
        if(arg.length>=1) {
            max.openProject(new File(arg[0]));
        }
    }
    
    
    
    private void addSourceFile(JavaSourceFile scrf) {
        scrlSourceFile.setViewportView(scrf);
    }
    
    
    
    private void initialize_ProjectFiles() {
        rootProject = new DefaultMutableTreeNode("< Project >");
        dtmdlFiles = new DefaultTreeModel(rootProject);
        treFiles.setModel(dtmdlFiles);      
    }
    
    
    
    private void createNewProject() {
        
        class CreateNewProject extends MDS_Dialog implements ActionListener, MDS_TextListener {
        
        
            
            private String path = System.getProperty("user.home")+"\\"; 
            //private String path = "E:\\prj\\";
            
            private JComponent contentPane;
            private MDS_Panel pnl1 = new MDS_Panel(new GridLayout(1,1));
            private MDS_TextField txtfName = new MDS_TextField();
            private MDS_Panel pnl2 = new MDS_Panel(new BorderLayout());
            private MDS_TextField txtfPath = new MDS_TextField(path);
            private MDS_Panel pnl3 = new MDS_Panel(new BorderLayout());
            private MDS_TextField txtfProjectFile = new MDS_TextField();
            private MDS_Button btnBrowse = new MDS_Button("Browse");        
            private MDS_Button btnOk = new MDS_Button("Ok");
            private MDS_Button btnCancel = new MDS_Button("Cancel");   
            private MDS_Panel pnlButtonContainer = new MDS_Panel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
            
            
            
            public CreateNewProject() {
                super(max, "New Project");
                contentPane =(JComponent)this.getContentPane();
                contentPane.setLayout(new GridLayout(4, 1));
                pnl1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Project Name"));
                txtfName.addTextListener(this);
                pnl1.add(txtfName);
                contentPane.add(pnl1);
                pnl2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Project Path"));
                txtfPath.setEditable(false);
                pnl2.add(txtfPath, BorderLayout.CENTER);
                btnBrowse.addActionListener(this);
                pnl2.add(btnBrowse, BorderLayout.EAST);
                contentPane.add(pnl2); 
                pnl3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Project file path"));
                txtfProjectFile.setEditable(false);
                pnl3.add(txtfProjectFile, BorderLayout.CENTER);  
                contentPane.add(pnl3);              
                btnOk.addActionListener(this);
                btnOk.setEnabled(false);             
                pnlButtonContainer.add(btnOk); 
                btnCancel.addActionListener(this);
                pnlButtonContainer.add(btnCancel);                ;
                contentPane.add(pnlButtonContainer);
                this.setSize(350,250);
                this.setCenterScreen();
                this.showDialog();    
            }
            
            
            
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Browse")) { 
                    boolean b = btnOk.isEnabled();           
                    MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
                    if(fmdc.showDiaog(this) ==  fmdc.APPROVE_OPTION) {
                        path = fmdc.getPath(); //+txtfName.getText();
                        if(!txtfPath.getText().equals("")) {
                            if(!txtfName.getText().equals("")) {
                                txtfPath.setText(path+txtfName.getText()+"\\"); 
                                txtfProjectFile.setText(path+txtfName.getText()+"\\"+txtfName.getText()+".max");
                            } else {
                                txtfPath.setText(path); 
                                txtfProjectFile.setText("");
                            }    
                        } else {
                            txtfPath.setText(path);
                        }    
                    }                     
                    
                    if(!b) btnOk.setEnabled(false);
                    
                } else if(e.getActionCommand().equals("Ok")) {  
                    if(fm.isValidFileName(txtfName.getText())) {  
                        projectName = txtfName.getText(); 
                        projectFile = new File(txtfProjectFile.getText());
                        File f = new File(txtfPath.getText());
                        f.mkdir();
                        projectPath = f.getPath();
                        if(!projectPath.endsWith("\\")) projectPath = projectPath.concat("\\");
                        Vector v = new Vector();
                        v.addElement(projectName);
                                       
                        rootProject = new DefaultMutableTreeNode(projectName);
                        dtmdlFiles = new DefaultTreeModel(rootProject);
                        treFiles.setModel(dtmdlFiles);
                        
                        //mniNewClass.setEnabled(true);
                        
                        this.dispose();   
                        
                        new Create_MDS_JavaProject();
						
						updateToolBar();
                         
                    } else {
                        MDS_OptionPane.showMessageDialog(this, st.get(6), "Maximus", JOptionPane.ERROR_MESSAGE);     
                        txtfName.requestFocus();
                        txtfName.selectAll();   
                    }
                } else if(e.getActionCommand().equals("Cancel")) { 
                    this.dispose();
                }
            }
            
            
            
            public void textChanged(MDS_TextEvent e) {
                if(e.getText().equals("")) {
                    btnOk.setEnabled(false);   
                } else {
                    btnOk.setEnabled(true);   
                }
                            
                if(!txtfName.getText().equals("")) {
                    txtfPath.setText(path+txtfName.getText()+"\\");    
                } else {
                    txtfPath.setText(path);
                }
                
                if(e.getText().equals("")) {
                    txtfProjectFile.setText("");
                } else {
                    txtfProjectFile.setText(path+e.getText()+"\\"+e.getText()+".max");
                }                
                 
            }
        } 
        
        new CreateNewProject();
           
    }
    
    
    
    private void createNewClass() {
    
        class CreateNewClass extends Neo_Dialog implements MDS_TextListener {
        
        
        
            MDS_Panel pnlBase = new MDS_Panel(new GridLayout(2, 1));
            MDS_Panel pnlClassInfo = new MDS_Panel(new GridLayout(2, 1, 0, 10));
            MDS_TextField txtfPackage = new MDS_TextField();
            MDS_TextField txtfName = new MDS_TextField();
            MDS_Panel pnlOptions = new MDS_Panel(new GridLayout(2, 1));
            MDS_CheckBox chkMain = new MDS_CheckBox("Generate main method");
            MDS_CheckBox chkConstructor = new MDS_CheckBox("Generate default constructor");
        
        
            public CreateNewClass() {
                super("Create New Class");
                
                pnlClassInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Class Information"));
                MDS_Panel pnl1 = new MDS_Panel(new BorderLayout());
                pnl1.add(new MDS_Label("Package    "), BorderLayout.WEST);
                txtfPackage.setEnabled(false);
                btnOk.setEnabled(false);
                pnl1.add(txtfPackage, BorderLayout.CENTER);
                pnlClassInfo.add(pnl1);
                
                MDS_Panel pnl2 = new MDS_Panel(new BorderLayout());
                pnl2.add(new MDS_Label("Class name  "), BorderLayout.WEST);
                txtfName.addTextListener(this);
                pnl2.add(txtfName, BorderLayout.CENTER);
                pnlClassInfo.add(pnl2);      
                pnlBase.add(pnlClassInfo);
                
                pnlOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Options"));
                pnlOptions.add(chkMain);
                pnlOptions.add(chkConstructor);
                pnlBase.add(pnlOptions);   
                this.addPanel(pnlBase);       
                this.setSize(400, 250);
                this.setCenterScreen();
                this.showDialog();    
            }
            
            
            
            public void textChanged(MDS_TextEvent e) {
                if(e.getText().equals("")) {
                    btnOk.setEnabled(false);   
                } else {
                    btnOk.setEnabled(true);   
                }               
            } 
            
            
            
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Ok")) { 
                    if(fm.isValidFileName(txtfName.getText())) { 
                        File f = new File(projectPath+txtfName.getText()+".java");
                        if(f.exists()) {
                            //MDS_OptionPane.showMessageDialog(this, st.get(14)+"\n"+txtfName.getText(), "Neo", JOptionPane.ERROR_MESSAGE);     
                            if(MDS_OptionPane.showConfirmDialog(this, st.get(14), "File Already Exits", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            	rootProject.add(new DefaultMutableTreeNode(f.getName()));
                            	dtmdlFiles.reload();
                            	JavaSourceFile jsf = new JavaSourceFile(f);   
                                vctProjectScrFiles.addElement(jsf);   
                            	currentJavaScrFile = jsf;
                            	saveProject();
                            	jsf.showSourceFile();								
                            } else {
 								txtfName.requestFocus();
                            	txtfName.selectAll();  
                            	return; 
                            } 							                          
                        } else {
                            try {
                                PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                                pw.println("");
                                pw.println("public class "+txtfName.getText()+" {");
                                pw.println("");
                                pw.println("");
                                pw.println("");
                                if(chkConstructor.isSelected()) {
                                    pw.println("    public "+txtfName.getText()+"() {}");
                                    pw.println("");
                                    pw.println("");
                                    pw.println("");
                                }
                                if(chkMain.isSelected()) {
                                    pw.println("    public static void MDS_Main (String argv[]) {}");
                                    pw.println("");
                                }
                                pw.println("}");  
                                pw.close();
                                rootProject.add(new DefaultMutableTreeNode(f.getName()));
                                dtmdlFiles.reload();
                                JavaSourceFile jsf = new JavaSourceFile(f);   
                                vctProjectScrFiles.addElement(jsf);   
                                currentJavaScrFile = jsf;
                                saveProject();
                                jsf.showSourceFile();                             
                            } catch(Exception ex) {
                            
                            }    
                        }
						
						updateToolBar();
						
                        this.dispose();
                    } else {
                        MDS_OptionPane.showMessageDialog(this, st.get(6), "Maximus", JOptionPane.ERROR_MESSAGE);     
                        txtfName.requestFocus();
                        txtfName.selectAll();                       
                    }        
                } else if(e.getActionCommand().equals("Cancel")) { 
                    this.dispose();
                }
            }                           
            
            
            
        } 
        
        new CreateNewClass();
           
    }
	
	
	
    private void refresh_Editing() {
        mniUndo.setEnabled(false);
        if(currentJavaScrFile.getSelectedText() == null) {
            mniCut.setEnabled(false);
            mniCopy.setEnabled(false);
            mniDelete.setEnabled(false);
        } else {
            mniCut.setEnabled(true);
            mniCopy.setEnabled(true);
            mniDelete.setEnabled(true);
        }

        if(currentJavaScrFile.isEditable()) {
            JTextField jtxtDummy = new JTextField();
            jtxtDummy.paste();
            if(jtxtDummy.getText().equals("")) {
                mniPaste.setEnabled(false);
            } else {
                mniPaste.setEnabled(true);
            }
            
            jtxtDummy = null;
            
        } else {
            mniCut.setEnabled(false);
            mniDelete.setEnabled(false);
            mniPaste.setEnabled(false);
        } 

        if(currentJavaScrFile.getText().equals("")) {
            mniSelectAll.setEnabled(false);
        } else {
            mniSelectAll.setEnabled(true);
        }

    }	
    
    
    
    private void deleteClass() {
        String fName = String.valueOf(((DefaultMutableTreeNode)treFiles.getLastSelectedPathComponent()).getUserObject());
        
        if(MDS_OptionPane.showConfirmDialog(this, st.get(13)+" "+fName+"?", "Neo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            
            rootProject.removeAllChildren();
            dtmdlFiles.reload();
            
            for(int x=0;x<=vctProjectScrFiles.size()-1;x++) {
                if(((JavaSourceFile)vctProjectScrFiles.elementAt(x)).getFile().getName().equals(fName)) {
                    ((JavaSourceFile)vctProjectScrFiles.elementAt(x)).getFile().delete();  
                    vctProjectScrFiles.removeElementAt(x);                  
                }    
            }
            
            for(int x=0;x<=vctProjectScrFiles.size()-1;x++) {
                rootProject.add(new DefaultMutableTreeNode(((JavaSourceFile)vctProjectScrFiles.elementAt(x)).getFile().getName()));
            }
            
            saveProject();
            addSourceFile(null);
            currentJavaScrFile = null;            
                
            dtmdlFiles.reload();
			
			updateToolBar();
               
        }  
            
    }
    
    
    
    private void addClass(File f, boolean mainClass) {
        rootProject.add(new DefaultMutableTreeNode(f.getName()));
        dtmdlFiles.reload();
        JavaSourceFile jsf = new JavaSourceFile(f);   
        vctProjectScrFiles.addElement(jsf);   
        currentJavaScrFile = jsf;
        if(mainClass) {
            String mc = f.getName().substring(0, f.getName().length()-4); 
            mainClassName = mc+"class";
        }
        saveProject();
        jsf.showSourceFile();  
		
		updateToolBar();
		    
    }
    
    
    
    private void saveProject() {
        try {
            Vector vctData = new Vector();

            htProjectProperties = new Hashtable();
            htProjectProperties.put(PROJECT_NAME, projectName);
            htProjectProperties.put(MAIN_CLASS_NAME, mainClassName);
            htProjectProperties.put(APPLICATION_PARAMETERS, applicationParameters);
            
            vctData.addElement(htProjectProperties);
            
            for(int x=0;x<=vctProjectScrFiles.size()-1;x++) {
                vctData.addElement(((JavaSourceFile)vctProjectScrFiles.elementAt(x)).getFile().getName());    
            }
            
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(projectFile.getPath())));
            oos.writeObject(vctData);
            oos.close();
            
            projectShouldSave = false;
            
        } catch(Exception ex) {
            ex.printStackTrace();
            //Console.println(ex.toString());
        } 
		
		
		updateToolBar();
		   
    }
    
    
    
    private void saveScrFiles() {
        for(int x=0;x<=vctProjectScrFiles.size()-1;x++) {
            JavaSourceFile jsf = (JavaSourceFile)vctProjectScrFiles.elementAt(x);    
            if(jsf.shouldSave()) jsf.save();
        }        
    }
    
    
    
    private boolean anyScrFileShouldSave() {
        int x=0;
        boolean b = false;
        
        while(x<=vctProjectScrFiles.size()-1) {
            JavaSourceFile jsf = (JavaSourceFile)vctProjectScrFiles.elementAt(x);    
           
            if(jsf.shouldSave()) {
                b = true;
                break;       
            }
            x++;
        }      
        
        return b;
        
    }
    
    
    
    private void openProject(File f) {
        if(f != null) {
            try {
                projectFile = f;
                projectPath = f.getParent();
                if(!projectPath.endsWith("\\")) projectPath = projectPath.concat("\\");
                
                //projectName = fm.getFileName_WithoutExtention(f.getName());                
            
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                Vector vctData = (Vector)ois.readObject();
                htProjectProperties = (Hashtable)vctData.elementAt(0);
                projectName = String.valueOf(htProjectProperties.get(PROJECT_NAME));
                mainClassName = String.valueOf(htProjectProperties.get(MAIN_CLASS_NAME));
                applicationParameters = String.valueOf(htProjectProperties.get(APPLICATION_PARAMETERS));
                rootProject = new DefaultMutableTreeNode(projectName);
                dtmdlFiles = new DefaultTreeModel(rootProject);
                treFiles.setModel(dtmdlFiles); 
                
                vctProjectScrFiles.removeAllElements();
                                                
                for(int x=1;x<=vctData.size()-1;x++) {
                    rootProject.add(new DefaultMutableTreeNode(vctData.elementAt(x)));    
                    JavaSourceFile jsf = new JavaSourceFile(new File(projectPath+String.valueOf(vctData.elementAt(x))), false);   
                    vctProjectScrFiles.addElement(jsf);                   
                } 
                
                dtmdlFiles.reload();  
                
                //mniNewClass.setEnabled(true); 
                
                this.setTitle("Maximus - "+projectName);    
                               
            } catch(Exception ex) {
            
            }    
        } else {
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            Vector v = new Vector();
            v.add("max");
            fmfc.setFilter(v);
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {  
                if(projectAlreadyOpened) {
                    projectAlreadyOpened = false;
                    initialize_ProjectFiles();              
                    addSourceFile(null);                    
                } 
                openProject(new File(fmfc.getPath())); 
                //mniNewClass.setEnabled(true);                    
            }             
        }
		
		
		updateToolBar();
		
		
    }
    
    
    
    public void openSelectedFile() {
        String fName = String.valueOf(((DefaultMutableTreeNode)treFiles.getLastSelectedPathComponent()).getUserObject());
        
        int x = 0;
        boolean found = false;
        while(x<=vctProjectScrFiles.size()-1 &&  !found) {
            JavaSourceFile jsf = (JavaSourceFile)vctProjectScrFiles.elementAt(x);
            if(jsf.getFile().getName().equals(fName)) {
            	log.info("jsf.showSourceFile()");
                jsf.showSourceFile();
                //currentScrFileIndex = vctProjectScrFiles.indexOf(jsf); 
                currentJavaScrFile = jsf;
                found = true;
            }
            x++;
        }
                    
        if(!found) {
            JavaSourceFile jsf = new JavaSourceFile(new File(projectPath+fName));
            vctProjectScrFiles.addElement(jsf);
            jsf.showSourceFile();        
            currentJavaScrFile = jsf;
        }
		
		
		updateToolBar();            
    
    }
	
	
	
	private void updateToolBar() {
        int selRow = 0;
        try {
            selRow = treFiles.getSelectionPath().getPathCount();    
        } catch(Exception ex) {
            
        }
        if(currentJavaScrFile == null) {
			/*
            mniSave.setEnabled(false);
            //mniSaveAs.setEnabled(false);
            //mniPrint.setEnabled(false);            
            mniUndo.setEnabled(false);
            mniCut.setEnabled(false);
            mniCopy.setEnabled(false);
            mniPaste.setEnabled(false);
            mniDelete.setEnabled(false);
            mniSelectAll.setEnabled(false);*/
			
			btnSave.setEnabled(false);
			btnCut.setEnabled(false);
			btnCopy.setEnabled(false);
			btnPaste.setEnabled(false);
			btnPrint.setEnabled(false);
			btnSearch.setEnabled(false);
			btnGoTo.setEnabled(false);
			
	
            
        } else {
            if(currentJavaScrFile.shouldSave()) {
                //mniSave.setEnabled(true);
				btnSave.setEnabled(true);
            } else {
                //mniSave.setEnabled(false);
				btnSave.setEnabled(false);
            } 
            
			/*
            //mniSaveAs.setEnabled(true);
            //mniPrint.setEnabled(true);          
            mniUndo.setEnabled(true);
            mniCut.setEnabled(true);
            mniCopy.setEnabled(true);
            mniPaste.setEnabled(true);
            mniDelete.setEnabled(true);
            mniSelectAll.setEnabled(true);     
			*/       
			
			btnCut.setEnabled(true);
			btnCopy.setEnabled(true);
			btnPaste.setEnabled(true);
			btnPrint.setEnabled(true);
			btnSearch.setEnabled(true);
			btnGoTo.setEnabled(true);			
            
        } 
        
        if(selRow >= 2) {
			/*
            mniCompile.setEnabled(true);
            mniPrint.setEnabled(true); 
            mniSaveAs.setEnabled(true);*/
			
			btnCompile.setEnabled(true);
			btnCompileAll.setEnabled(true);
			btnPrint.setEnabled(true);
			btnSaveAs.setEnabled(true);
			btnSaveAll.setEnabled(true);
			btnRun.setEnabled(true); 
        } else {
			/*
            mniCompile.setEnabled(false);
            mniPrint.setEnabled(false);
            mniSaveAs.setEnabled(false); */
			
			btnCompile.setEnabled(false);
			btnCompileAll.setEnabled(false);
			btnPrint.setEnabled(false);
			btnSaveAs.setEnabled(false);
			btnSaveAll.setEnabled(false);
			btnRun.setEnabled(false);
        }
                   

        if(projectName.equals("")) {
			/*
            mniCloseProject.setEnabled(false);
            mniCompileAll.setEnabled(false);
            mniSaveAll.setEnabled(false);
            mniRun.setEnabled(false);
            mniConfigurations.setEnabled(false);
            mniRuntimeProperties.setEnabled(false);
            mniFont.setEnabled(false);
            mniColor.setEnabled(false);*/
			
			//btnCompileAll.setEnabled(false);
			//btnSaveAll.setEnabled(false);
			//btnRun.setEnabled(false);
			
			btnNew.setEnabled(false);
			
			
			
        } else {
			/*
            mniCloseProject.setEnabled(true);
            mniCompileAll.setEnabled(true);
            mniSaveAll.setEnabled(true);
            mniRun.setEnabled(true);
            mniConfigurations.setEnabled(true);
            mniRuntimeProperties.setEnabled(true);  
            mniFont.setEnabled(true);
            mniColor.setEnabled(true); */
			
			//btnCompileAll.setEnabled(true);
			//btnSaveAll.setEnabled(true);
			//btnRun.setEnabled(true); 
			btnNew.setEnabled(true);
        } 	
	}
	
	
	
	public void complieCurrentFile() {
	    openSelectedFile();
	    saveScrFiles();
	    compileAll = false;
	    checkIllegalityThread = new Thread(this);
	    checkIllegalityThread.start();  		
	}
    
    
    
    public void run() {
        try {
        
            boolean illegal = false;
            Vector vctFiles = new Vector();

            txtaOutput.setText("Checking illegal classes ...\n");

            if(splpBase.getDividerLocation() > 450) {
                splpBase.setDividerLocation(450);
            }
            
            if(compileAll) {
                Vector vctFilter = new Vector();
                vctFilter.addElement("java");            
                vctFiles = fm.getContent_Files(projectPath, vctFilter);
            } else {
                vctFiles.addElement(currentJavaScrFile.getFile());
            }
            
            for(int x=0;x<=vctFiles.size()-1;x++) {
                        
                File f = (File)vctFiles.elementAt(x);
                StreamTokenizer stkz = new StreamTokenizer(new FileReader(f));    
                stkz.slashSlashComments(true);
                stkz.slashStarComments(true);
                stkz.wordChars('_','_');
                stkz.wordChars('.','.');
                stkz.ordinaryChar('.');
                while(stkz.nextToken() != StreamTokenizer.TT_EOF) {
                    String st = "";
                    switch(stkz.ttype) {
                        case StreamTokenizer.TT_EOL:
                            //s = new String("EOL");
                            break;
                        case StreamTokenizer.TT_NUMBER:
                            Double d = new Double(stkz.nval);
                            st = String.valueOf(d.longValue());
                            break;
                        case StreamTokenizer.TT_WORD:
                            st = stkz.sval; 
                            break;
                        default:
                            st = String.valueOf((char)stkz.ttype);
                    }
                
                    String illegality = "";
                
                    if(st.equals("package")) {
                        illegality = f.getName()+":"+String.valueOf(stkz.lineno())+": packages are not allowed under MDS development environment";               
                        illegal = true;
                        txtaOutput.append(illegality+"\n");
                    } else if(illegalClassList.containsKey(st)) {
                        
                        illegality = f.getName()+":"+String.valueOf(stkz.lineno())+": you must use "+String.valueOf(illegalClassList.get(st))+" instead of "+st;
                        illegal = true;
                        txtaOutput.append(illegality+"\n");
                    } else if(illegalClassNameList.containsKey(st)) {
                        illegality = f.getName()+":"+String.valueOf(stkz.lineno())+": you must use "+String.valueOf(illegalClassNameList.get(st))+" instead of "+st;
                        illegal = true;
                        txtaOutput.append(illegality+"\n");
                    }  
                                
                }
            
            }
            
            
            if(!illegal) {
            	
            	System.out.println("Compiling Mahesh...........");
            
                txtaOutput.append("Compiling ...\n");
                int errCount = 0;
            
                if(compileAll) {
					//System.out.println("ALL");
					/*
                    for(int x=0;x<=vctFiles.size()-1;x++) {                  
                        File f = (File)vctFiles.elementAt(x);
                        PrintWriter pw = new PrintWriter(new CompileOutput()); 
						String s[] = {f.getPath()};
						Main.compile(s, pw);       
                    }*/
					String s[] = new String[vctFiles.size()];   
					
					for(int x=0;x<=vctFiles.size()-1;x++) {
						s[x] = ((File)vctFiles.elementAt(x)).getPath(); 
					}                                
//////                    PrintWriter pw = new PrintWriter(new CompileOutput()); 
//////					Main.compile(s, pw); 

		       		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		       		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		
		      		Iterable<? extends JavaFileObject> compilationUnits1 = 
		      			fileManager.getJavaFileObjectsFromFiles(vctFiles);       		
		       		
		       		compiler.getTask(null, fileManager, new CompileOutput(), null, null, compilationUnits1).call();

					     
                } else {
					//System.out.println("ONE");
					/*
                    for(int x=0;x<=vctFiles.size()-1;x++) {                  
                        File f = (File)vctFiles.elementAt(x);
                        PrintWriter pw = new PrintWriter(new CompileOutput());  
		                String s[] = {f.getPath()};
						errCount = Main.compile(s, pw);       
                    } */
					
//////                        File f = currentJavaScrFile.getFile();
//////                        PrintWriter pw = new PrintWriter(new CompileOutput());  
//////		                String s[] = {f.getPath()};	
//////						errCount = Main.compile(s, pw);	

					System.out.println("WM : "+currentJavaScrFile.getFile());

			
		       		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		       		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			
		      		Iterable<? extends JavaFileObject> compilationUnits1 = 
		      			fileManager.getJavaFileObjectsFromFiles(Arrays.asList(currentJavaScrFile.getFile()));       		
		       		//DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		       		compiler.getTask(null, fileManager, new CompileOutput(), null, null, compilationUnits1).call();
		       		
		       							
					
												

                } 
                
                if(errCount == 0) {
                    txtaOutput.append("error(s) 0");    
                }
                   
            }
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
    	ProcessManager prm = MDS.getProcessManager();
    	
        if(e.getActionCommand().equals("New ...")) {
            if(!projectName.equals("")) { 
                if(anyScrFileShouldSave()) {
                    int r = MDS_OptionPane.showConfirmDialog(max, st.get(8), "Maximus", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(r == JOptionPane.YES_OPTION) {
                        saveScrFiles(); 
                        initialize_ProjectFiles();                     
                        addSourceFile(null);   
                        projectName = "";
                        createNewProject();
                        //new Create_MDS_JavaProject();
                    } else if(r == JOptionPane.NO_OPTION) {
                        initialize_ProjectFiles();              
                        addSourceFile(null);
                        projectName = ""; 
                        createNewProject();
                        new Create_MDS_JavaProject();
                    } else if(r == JOptionPane.CANCEL_OPTION) {
                            
                    }
                } else {
                    initialize_ProjectFiles();              
                    addSourceFile(null);  
                    projectName = "";               
                    createNewProject();
                    //new Create_MDS_JavaProject();
                }             
            } else {
                projectName = "";
                createNewProject();
                //new Create_MDS_JavaProject();
            }    
        } else if(e.getActionCommand().equals("New Class")) { 
            createNewClass();    
		} else if(e.getActionCommand().equals("Add Java Source File")) {
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            Vector v = new Vector();
            v.add("java");
            fmfc.setFilter(v);
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) { 	
				File f = fmfc.getFile();
				if(f.getName().endsWith(".java")) {	
					if(!f.getParent().equals(projectFile.getParent())) {
						MDS_OptionPane.showMessageDialog(this, "Java source file must be located in the project file directory.", "Maximus", JOptionPane.ERROR_MESSAGE);
						return; 	
					}
			        rootProject.add(new DefaultMutableTreeNode(f.getName()));
			        dtmdlFiles.reload();
			        JavaSourceFile jsf = new JavaSourceFile(f);   
			        vctProjectScrFiles.addElement(jsf);   
			        currentJavaScrFile = jsf;
			        saveProject();
			        jsf.showSourceFile(); 	
				} else {
					MDS_OptionPane.showMessageDialog(this, "Invalid Java source file.", "Maximus", JOptionPane.ERROR_MESSAGE);
				}	
            }		
        } else if(e.getActionCommand().equals("Save ...")) {
            if(currentJavaScrFile != null) {
                currentJavaScrFile.save();
            }
        } else if(e.getActionCommand().equals("Save All")) {
            saveScrFiles();              
        } else if(e.getActionCommand().equals("Save As ...")) {
            openSelectedFile();
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.SAVE_DIALOG);          
            Vector v = new Vector();
            v.add("java");
            fmfc.setFilter(v);
            fmfc.setSuggestedFileName(currentJavaScrFile.getFile().getName());
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) { 
                try {
                    FileWriter fw = new FileWriter(new File(fmfc.getPath()));
                    fw.write(currentJavaScrFile.getText(), 0, currentJavaScrFile.getText().length());
                    fw.close();
                } catch(Exception ex) {}                       
            }                        
        } else if(e.getActionCommand().equals("Open ...")) {  
            if(!projectName.equals("")) {  
                if(anyScrFileShouldSave()) {
                    int r = MDS_OptionPane.showConfirmDialog(max, st.get(8), "Maximus", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(r == JOptionPane.YES_OPTION) {
                        saveScrFiles(); 
                        //initialize_ProjectFiles();                     
                        //addSourceFile(null);  
                        projectAlreadyOpened = true;
                        openProject(null);
                    } else if(r == JOptionPane.NO_OPTION) {
                        //initialize_ProjectFiles();              
                        //addSourceFile(null); 
                        projectAlreadyOpened = true;
                        openProject(null);
                    } else if(r == JOptionPane.CANCEL_OPTION) {
                            
                    }
                } else {
                    projectAlreadyOpened = true;
                    openProject(null);         
                }  
            } else {
                projectAlreadyOpened = true;
                openProject(null);                  
            }          
        } else if(e.getActionCommand().equals("Close ...")) { 
            if(anyScrFileShouldSave()) {
                int r = MDS_OptionPane.showConfirmDialog(max, st.get(8), "Maximus", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                if(r == JOptionPane.YES_OPTION) {
                    saveScrFiles();
                    initialize_ProjectFiles();                      
                    addSourceFile(null);
                    projectName = "";
                    currentJavaScrFile = null;
                    //mniNewClass.setEnabled(false);   
                } else if(r == JOptionPane.NO_OPTION) {
                    initialize_ProjectFiles();              
                    addSourceFile(null); 
                    projectName = "";
                    currentJavaScrFile = null;
                    //mniNewClass.setEnabled(false);
                } else if(r == JOptionPane.CANCEL_OPTION) {
                            
                }
            } else {
                initialize_ProjectFiles();         
                addSourceFile(null);  
                projectName = ""; 
                currentJavaScrFile = null;
                //mniNewClass.setEnabled(false);             
            }         
        } else if(e.getActionCommand().equals("Compile ...")) {
            openSelectedFile();
            saveScrFiles();
            compileAll = false;
            checkIllegalityThread = new Thread(this);
            checkIllegalityThread.start();       
        } else if(e.getActionCommand().equals("Compile *.java")) {
            saveScrFiles();
            compileAll = true;
            checkIllegalityThread = new Thread(this);
            checkIllegalityThread.start();              
        } else if(e.getActionCommand().equals("Runtime Properties")) {
            new RunTimeProperties();        
        } else if(e.getActionCommand().equals("Configuration")) {
            new RunTimeProperties(); 
        } else if(e.getActionCommand().equals("Run Project")) {
            if(!mainClassName.equals("")) {
                if(!mainClassName.endsWith(".class")) mainClassName = mainClassName.concat(".class");
                prm.execute(new File(projectPath, mainClassName));
            } else {
                shouldRun = true;
                new RunTimeProperties(); 
            }
        } else if(e.getActionCommand().equals("Open")) {
            openSelectedFile();           
        } else if(e.getActionCommand().equals("Compile")) {
            openSelectedFile();
            saveScrFiles();
            compileAll = false;
            checkIllegalityThread = new Thread(this);
            checkIllegalityThread.start();              
        } else if(e.getActionCommand().equals("Add New Class")) {
            createNewClass();  
        } else if(e.getActionCommand().equals("Delete Class")) {
            openSelectedFile();
            deleteClass(); 
        } else if(e.getActionCommand().equals("Undo")) {
            
        } else if(e.getActionCommand().equals("Cut")) {
            if(currentJavaScrFile != null) {
                currentJavaScrFile.cut();
            }
        } else if(e.getActionCommand().equals("Copy")) {
            if(currentJavaScrFile != null) {
                currentJavaScrFile.copy();
            }        
        } else if(e.getActionCommand().equals("Paste")) {
            if(currentJavaScrFile != null) {
                currentJavaScrFile.paste();
            }        
        } else if(e.getActionCommand().equals("Delete")) {
            if(currentJavaScrFile != null) {
                StringBuffer text = new StringBuffer(currentJavaScrFile.getText());
                int start = currentJavaScrFile.getSelectionStart();
                int end = currentJavaScrFile.getSelectionEnd();
                String unDeletedText = text.delete(start, end).toString();
                currentJavaScrFile.setText(unDeletedText);                
            }        
        } else if(e.getActionCommand().equals("Select All")) {
            if(currentJavaScrFile != null) {
                currentJavaScrFile.selectAll();    
            }        
        } else if(e.getActionCommand().equals("Print ...")) {
            openSelectedFile();
        } else if(e.getActionCommand().equals("Class Connection Walker")) {
            prm.execute(new File(MDS.getBinaryPath(), "ClassConnectionWalker.class"));
        } else if(e.getActionCommand().equals("Virtual Threading Manager")) {
            prm.execute(new File(MDS.getBinaryPath(), "VirtualThreadingManager.class"));
		} else if(e.getActionCommand().equals("Java Class Browser")) {
			prm.execute(new File(MDS.getBinaryPath(), "JavaClassBrowser.class"));            
		} else if(e.getActionCommand().equals("Jar File Creator")) {
			prm.execute(new File(MDS.getBinaryPath(), "JarFileCreator.class"));
		} else if(e.getActionCommand().equals("System Manager")) {
			prm.execute(new File(MDS.getBinaryPath(), "SystemManager.class"));	
        } else if(e.getActionCommand().equals("Exit")) {
            this.dispose();
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "Neo", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-Maximus.png"), MDS.getAbout_Mahesh());
        } else if(e.getActionCommand().equals("GoTo ...")) {
			currentJavaScrFile.showGotoDialog(this);
        } else if(e.getActionCommand().equals("Find ...")) {
			currentJavaScrFile.showFindDialog(this);
        } else if(e.getActionCommand().equals("Background Color")) {
          	MDS_ColorChooser cc = new MDS_ColorChooser(currentJavaScrFile.getBackground());
            if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                currentJavaScrFile.setForeground(cc.getColor());
            }		
        } else if(e.getActionCommand().equals("Foreground Color")) {
           MDS_ColorChooser cc = new MDS_ColorChooser(currentJavaScrFile.getForeground());
            if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                currentJavaScrFile.setForeground(cc.getColor());
            }		
        } else if(e.getActionCommand().equals("Font")) {
            MDS_FontChooser fc = new MDS_FontChooser(currentJavaScrFile.getFont());
            if(fc.showFontChooser(this) == fc.APPROVE_OPTION) {
                currentJavaScrFile.setFont(fc.getFont());
            }			
        }
		
		
		updateToolBar();
		
         
    }
    
    
    
    public void mouseClicked(MouseEvent e){
    
        treFiles.setSelectionRow(treFiles.getRowForLocation(e.getX(), e.getY()));
        
        if(e.getButton() == e.BUTTON3) {
            if(!treFiles.isSelectionEmpty()) {
                if(!treFiles.isRowSelected(0)) {
                    MDS_PopupMenu pm = new MDS_PopupMenu();
                    JMenuItem mniOpen = new JMenuItem("Open");
                    JMenuItem mniNewClass = new JMenuItem("Add New Class");
                    JMenuItem mniDeleteClass = new JMenuItem("Delete Class");
                    JMenuItem mniCompile = new JMenuItem("Compile");
                    mniOpen.addActionListener(this);
                    pm.add(mniOpen);
                    pm.addSeparator();
                    mniNewClass.addActionListener(this);
                    pm.add(mniNewClass);
                    mniDeleteClass.addActionListener(this);
                    pm.add(mniDeleteClass);
                    pm.addSeparator();
                    mniCompile.addActionListener(this);
                    pm.add(mniCompile); 
                    //treFiles.setSelectionPath(new TreePath(new String[] {"projectName", "currentJavaScrFile.getFile().getName()"}));
                    pm.show(treFiles, e.getX(), e.getY());
                }
            } 
        }
        
        //System.err.println(currentJavaScrFile.getCaretPosition());
        
        //System.err.println("Mahesh DDS");
        //treFiles.setSelectionPath(currentTreePath);
        
    }



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){
        int selRow = treFiles.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = treFiles.getPathForLocation(e.getX(), e.getY());

        if(e.getButton() == e.BUTTON1) {
            if(e.getClickCount() == 2) {
                if(selRow != -1 && selRow != 0) {
                    openSelectedFile();    
                }    
            }
        }
    }
        

        
    public void mouseReleased(MouseEvent e) {}
    
    
    
    public void menuCanceled(MenuEvent e) {}
    
    
    
    public void menuDeselected(MenuEvent e) {}
    
    
    
    public void menuSelected(MenuEvent e) {
        
        int selRow = 0;
        try {
            selRow = treFiles.getSelectionPath().getPathCount();    
        } catch(Exception ex) {
            
        }
        if(currentJavaScrFile == null) {
            mniSave.setEnabled(false);
            //mniSaveAs.setEnabled(false);
            //mniPrint.setEnabled(false);            
            mniUndo.setEnabled(false);
            mniCut.setEnabled(false);
            mniCopy.setEnabled(false);
            mniPaste.setEnabled(false);
            mniDelete.setEnabled(false);
            mniSelectAll.setEnabled(false);
			mniGoTo.setEnabled(false);
			mniFind.setEnabled(false);
			mniColorForeground.setEnabled(false);
			mniColorBackground.setEnabled(false);
			
	
            
        } else {
            if(currentJavaScrFile.shouldSave()) {
                mniSave.setEnabled(true);
            } else {
                mniSave.setEnabled(false);
            } 
            
            //mniSaveAs.setEnabled(true);
            //mniPrint.setEnabled(true);          
            mniUndo.setEnabled(true);
            mniCut.setEnabled(true);
            mniCopy.setEnabled(true);
            mniPaste.setEnabled(true);
            mniDelete.setEnabled(true);
            mniSelectAll.setEnabled(true);        
			mniGoTo.setEnabled(true);
			mniFind.setEnabled(true); 
			mniColorForeground.setEnabled(true);
			mniColorBackground.setEnabled(true);
			mniFont.setEnabled(true);	
			
			refresh_Editing();		   
            
        } 
        
        if(selRow >= 2) {
            mniCompile.setEnabled(true);
			mniCompileAll.setEnabled(true);
			mniSaveAll.setEnabled(true);
            mniPrint.setEnabled(true); 
            mniSaveAs.setEnabled(true);
            mniRun.setEnabled(true);
            mniConfigurations.setEnabled(true);
            mniRuntimeProperties.setEnabled(true);  			
        } else {
			mniCompileAll.setEnabled(false);
            mniCompile.setEnabled(false);
			mniSaveAll.setEnabled(false);
            mniPrint.setEnabled(false);
            mniSaveAs.setEnabled(false); 
            mniRun.setEnabled(false);
            mniConfigurations.setEnabled(false);
            mniRuntimeProperties.setEnabled(false);	
			mniFont.setEnabled(false);		
        }
                   

        if(projectName.equals("")) {
			mniNewClass.setEnabled(false);
			mniAddJavaFile.setEnabled(false);
            mniCloseProject.setEnabled(false);
            //mniCompileAll.setEnabled(false);
            //mniSaveAll.setEnabled(false);
            //mniRun.setEnabled(false);
            //mniConfigurations.setEnabled(false);
            //mniRuntimeProperties.setEnabled(false);
            //mniFont.setEnabled(false);
            //mniColor.setEnabled(false);
			
        } else {
			mniNewClass.setEnabled(true);
			mniAddJavaFile.setEnabled(true);
            mniCloseProject.setEnabled(true);
            //mniCompileAll.setEnabled(true);
            //mniSaveAll.setEnabled(true);
            //mniRun.setEnabled(true);
            //mniConfigurations.setEnabled(true);
            //mniRuntimeProperties.setEnabled(true);  
            //mniFont.setEnabled(true);
            //mniColor.setEnabled(true); 
			 
        }                        
        
             
    }     
    
    
    
    
    
    private class Neo_Dialog extends MDS_Dialog implements ActionListener {
    
    
     
        MDS_Button btnOk = new MDS_Button("Ok");
        MDS_Button btnCancel = new MDS_Button("Cancel");      
        private MDS_Panel pnlButtonContainer = new MDS_Panel(new FlowLayout(FlowLayout.RIGHT, 5, 10));    
    
    
    
        public Neo_Dialog(String title) {
            super(max, title);                
            this.getContentPane().setLayout(new BorderLayout());
            btnOk.addActionListener(this);
            pnlButtonContainer.add(btnOk);
            btnCancel.addActionListener(this);
            pnlButtonContainer.add(btnCancel);
            this.getContentPane().add(pnlButtonContainer, BorderLayout.SOUTH);
        }
        
        
        
        public void addPanel(MDS_Panel panel) {
            this.getContentPane().add(panel, BorderLayout.CENTER);    
        }
        
        
        
        public void actionPerformed(ActionEvent e) {}
        
        
        
    } 
    
    
    
    
    
    private class JavaSourceFile extends MDS_TextArea implements MDS_TextListener, CaretListener, MouseListener, ActionListener { 
    
    
    
        JavaSourceFile jsf;
        File scrFile;
		Document document;
        boolean shouldSave = false;
        boolean scrLoaded = false;
		
		MDS_PopupMenu popup;
		JMenuItem mniCompile;
		JMenuItem mniRun;
		JMenuItem mniUndo;
		JMenuItem mniRedo;
		JMenuItem mniCut;
		JMenuItem mniCopy;
		JMenuItem mniPaste;
		JMenuItem mniDelete;
		JMenuItem mniSelect_All;		
    
    
    
        public JavaSourceFile(File f) {
            jsf = this;
			document = this.getDocument();
            this.setFont(new Font("Courier", Font.PLAIN, 13));
            scrFile = f;
            loadFile(f);
            this.addTextListener(this);      
			this.addCaretListener(this);
			this.addMouseListener(this);
			//this.setBackground(new Color(220, 212, 192));
			//this.setForeground(new Color(49, 160, 0));
			this.setPopupMenuEnabled(false);
            scrLoaded = true; 
			
		    popup = new MDS_PopupMenu("Mahesh");
		    mniCompile = new JMenuItem("Compile", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-compile.png"));
		    mniCompile.addActionListener(this);
		    mniCompile.setMnemonic('p');
		    popup.add(mniCompile);
		    mniRun = new JMenuItem("Run", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-player_play.png"));
		    mniRun.addActionListener(this);
		    mniRun.setMnemonic('R');
		    popup.add(mniRun);				
		    popup.addSeparator();
		    mniUndo = new JMenuItem("Undo", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-player_play.png"));
		    mniUndo.addActionListener(this);
		    mniUndo.setMnemonic('U');
		    popup.add(mniUndo);
		    mniRedo = new JMenuItem("Redo", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-player_play.png"));
		    mniRedo.addActionListener(this);
		    mniRedo.setMnemonic('d');
		    popup.add(mniRedo);			
		    popup.addSeparator();
		    mniCut = new JMenuItem("Cut", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcut.png"));
		    mniCut.addActionListener(this);
		    mniCut.setMnemonic('t');
		    popup.add(mniCut);
		    mniCopy = new JMenuItem("Copy", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcopy.png"));
		    mniCopy.addActionListener(this);
		    mniCopy.setMnemonic('C');
		    popup.add(mniCopy);
		    mniPaste = new JMenuItem("Paste", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editpaste.png"));
		    mniPaste.addActionListener(this);
		    mniPaste.setMnemonic('P');
		    popup.add(mniPaste);
		    mniDelete = new JMenuItem("Delete", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editdelete.png"));
		    mniDelete.addActionListener(this);
		    mniDelete.setMnemonic('D');
		    popup.add(mniDelete);
		    popup.addSeparator();
		    mniSelect_All = new JMenuItem("      Select All");
		    mniSelect_All.addActionListener(this);
		    mniSelect_All.setMnemonic('A');
		    popup.add(mniSelect_All);
		    this.add(popup);			
			
        }
        
        
        
        public JavaSourceFile(File f, boolean load) {
			/*
            jsf = this;
			document = this.getDocument();
            this.setFont(new Font("Courier", Font.PLAIN, 13));
            scrFile = f;
            if(load) loadFile(scrFile);
            this.addTextListener(this); 
			this.addCaretListener(this);*/
			this(f);
            scrLoaded = load;          
        }
		
		
		
  		/**
    	* Returns the length of the specified line.
    	* @param line The line
    	*/
  		public int getLineLength(int line)
  		{
    		Element lineElement = document.getDefaultRootElement().getElement(line);
    		if (lineElement == null)
      			return -1;
    		else
      			return lineElement.getEndOffset() - lineElement.getStartOffset() - 1;
  		}
		
		
		
  		/**
    	* Returns the start offset of the specified line.
    	* @param line The line
    	* @return The start offset of the specified line, or -1 if the line is
    	* invalid
    	*/
  		public int getLineStartOffset(int line)
  		{
    		Element lineElement = document.getDefaultRootElement().getElement(line);
    		if (lineElement == null)
      			return -1;
    		else
      			return lineElement.getStartOffset();
  		}
		
		
		

  		/**
    	* Returns the end offset of the specified line.
    	* @param line The line
    	* @return The end offset of the specified line, or -1 if the line is
    	* invalid.
    	*/
  		public int getLineEndOffset(int line)
  		{
    		Element lineElement = document.getDefaultRootElement().getElement(line);
    		if(lineElement == null)
      			return -1;
    		else
      			return lineElement.getEndOffset();
  		}
		
		
		
  		/**
    	* Returns the text on the specified line.
    	* @param lineIndex The line
    	* @return The text, or null if the line is invalid
    	*/
  		public final String getLineText(int lineIndex) throws BadLocationException
  		{
    		int start = getLineStartOffset(lineIndex);
    		return getText(start, getLineEndOffset(lineIndex) - start - 1);
  		}
		
		
		
		public int getTotalNumLines() {
    		Element map = document.getDefaultRootElement();	
			return map.getElementCount();		
		}
		
		
		
		public void showGotoDialog(MDS_Frame frm) {
			
            String line = "";
			int lineNo = 0;  
            line = MDS_OptionPane.showInputDialog(frm, "Line number between (1 and "+ String.valueOf(getTotalNumLines())+ "):", "GoTo ...",JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, true, true);      
            if(line == null) {
                lineNo = 0;
				return;
            } else {
				lineNo = Integer.parseInt(line);
            }      
            if(lineNo>0) {
				this.grabFocus();
				this.select(getLineStartOffset(lineNo), getLineEndOffset(lineNo)); 
            }		
		}
		
		
		
		public void showFindDialog(MDS_Frame frm) {

			
			class FindText extends MDS_Dialog implements ActionListener {
			
			
			
				MDS_Panel panel1 = new MDS_Panel(new BorderLayout());
				MDS_Label lblFind = new MDS_Label("Find : ");
				MDS_TextField txfdSearchText = new MDS_TextField();
				MDS_Button btnFind = new MDS_Button("Find");
				
				MDS_Panel panel2 = new MDS_Panel(new BorderLayout());
				MDS_CheckBox chkbIgnoreCase = new MDS_CheckBox("Ignore Case");
				MDS_Button btnCancel = new MDS_Button("Cancel");
				
				JavaSourceFile jsf;
				
			
			
			
				public FindText(MDS_Frame owner, JavaSourceFile f) {
					super(owner, "Find", true, true);
					jsf = f;
					this.setLayout(null);
					
					lblFind.setBounds(10, 10, 50, 26);
					this.add(lblFind); 	
					
					txfdSearchText.setBounds(60, 10, 200, 26);	
					this.add(txfdSearchText);
					
					btnFind.setBounds(270, 10, 75, 26);
					btnFind.addActionListener(this);
					this.add(btnFind);	
					
					chkbIgnoreCase.setBounds(10, 45, 150, 26);
					this.add(chkbIgnoreCase);
					
					btnCancel.setBounds(270, 45, 75, 26);
					btnCancel.addActionListener(this);
					this.add(btnCancel);
							
					
					this.setSize(370, 115);
					this.setCenterScreen();
					this.showDialog();					
					
				}
				
				
				
				public void actionPerformed(ActionEvent e) {
					if(e.getActionCommand().equals("Find")) {
						if(txfdSearchText.getText().equals("")) return;
						try {
							jsf.find(jsf.getCaretPosition(), new LiteralSearchMatcher(txfdSearchText.getText(), "",chkbIgnoreCase.isSelected()));
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					} else if(e.getActionCommand().equals("Cancel")) {
						this.dispose();
					}					
					
					
				}
			}	
			
			new FindText(frm, this);
				
		}	
		
		
		


											
        
        
        
        public void refresh() {
            loadFile(scrFile);
        }
        
        
        
        public void showSourceFile() {
            this.setEnabled(true);
            addSourceFile(this); 
            if(!scrLoaded) loadFile(scrFile);
            setTitle("Neo - "+projectName+"["+scrFile.getPath()+"]"); 
        }
        
        
        
        public File getFile() {
            return scrFile;
        }
        
        
        
        public boolean shouldSave() {
            return shouldSave;
        }
        
        
        
        public void save() {
            if(shouldSave) {
                try {
                    FileWriter fw = new FileWriter(scrFile);
                    fw.write(this.getText(), 0, this.getText().length());
                    fw.close();  
////                    MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, scrFile));
                    shouldSave = false; 
                } catch (Exception ex) {
                    MDS_OptionPane.showMessageDialog(this, ex.toString(), "Any Edit", JOptionPane.ERROR_MESSAGE);
                }               
            }
        }
		
		
		
	    private void refresh_Editing() {
	        mniUndo.setEnabled(false);
	        if(this.getSelectedText() == null) {
	            mniCut.setEnabled(false);
	            mniCopy.setEnabled(false);
	            mniDelete.setEnabled(false);
	        } else {
	            mniCut.setEnabled(true);
	            mniCopy.setEnabled(true);
	            mniDelete.setEnabled(true);
	        }

	        if(this.isEditable()) {
	            JTextField jtxtDummy = new JTextField();
	            jtxtDummy.paste();
	            if(jtxtDummy.getText().equals("")) {
	                mniPaste.setEnabled(false);
	            } else {
	                mniPaste.setEnabled(true);
	            }
	            
	            jtxtDummy = null;
	            
	        } else {
	            mniCut.setEnabled(false);
	            mniDelete.setEnabled(false);
	            mniPaste.setEnabled(false);
	        } 

	        if(this.getText().equals("")) {
	            mniSelect_All.setEnabled(false);
	        } else {
	            mniSelect_All.setEnabled(true);
	        }

	    }		
		
		
		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Undo")) {

	        } else if(e.getActionCommand().equals("Cut")) {
	            this.cut();
	        } else if(e.getActionCommand().equals("Copy")) {
	            this.copy();
	        } else if(e.getActionCommand().equals("Paste")) {
	            this.paste();
	        } else if(e.getActionCommand().equals("Delete")) {
	            StringBuffer text = new StringBuffer(this.getText());
	            int start = this.getSelectionStart();
	            int end = this.getSelectionEnd();
	            String unDeletedText = text.delete(start, end).toString();
	            this.setText(unDeletedText);
	        } else if(e.getActionCommand().equals("      Select All")) {
	            this.selectAll();
	        }  else if(e.getActionCommand().equals("Compile")) {
				complieCurrentFile(); 					
	        }  else if(e.getActionCommand().equals("Run")) {
				MDS.getProcessManager().execute(getFile());
	        }		
		}
		
		
		
	    public void mouseClicked(MouseEvent e) {}



	    public void mouseEntered(MouseEvent e) {}



	    public void mouseExited(MouseEvent e) {}



	    public void mousePressed(MouseEvent e) {
	        if(e.getButton() == e.BUTTON3) {
	            if(this.isEnabled()) {
	                refresh_Editing();
	                popup.show(this, e.getX(), e.getY());
	            }
	        }
	    }



	    public void mouseReleased(MouseEvent e) {}		
        
        
        
        public void textChanged(MDS_TextEvent e) {
            shouldSave = true;
            projectShouldSave = true;  
			//System.out.println(this.getRows()); 
			
			if(!btnSave.isEnabled()) btnSave.setEnabled(true);
        }
		
		
		
		public void caretUpdate(CaretEvent e) {
			int off = this.getCaretPosition();
    		Element map = document.getDefaultRootElement();
    		int currLine = map.getElementIndex(off);	
			int numLines = map.getElementCount();		
			lblStatusBar.setText(String.valueOf(currLine+1)+"/"+String.valueOf(numLines));
		}
        
        
        
        private void loadFile(File f) {
			log.info("Loading source file.");
            class LoadFile implements Runnable {
        
        
        
                Thread thrLoad;
                File file;

        
        
                public LoadFile(File f) {
                    file = f;
                    thrLoad = new Thread(this);
                    jsf.setText(""); //BUG: if this removed, duplicate file data.
                    thrLoad.start();        
                }
            
            
            
                public void run() {
                    try {
                        int off = 0;
                        int len = 1024;        
                        long fLen = file.length();        
                        long cfLen = 0;      
                             
                        DataInputStream disIn = new DataInputStream(new FileInputStream(file));                
                        while(disIn.available() != 0) {            
                            if((fLen - cfLen) <= len) {
                                String slen = String.valueOf(fLen - cfLen); 
                                Integer i = new Integer(slen); 
                                len = i.intValue();   
                            } else {            
                                cfLen = cfLen+len;
                            }
            
                            byte b[] = new byte[len];
                            disIn.read(b,off,len);
                            jsf.append(new String(b));                       
                        } 
                    
                        disIn.close(); 
                                          
                        //showSourceFile();
                        scrLoaded = true;
                       
                    } catch (Exception ex) {
                    	log.severe(ex.getMessage());
                    }                                   
                }    
            }    
        
            new LoadFile(f);
                       
        }    
		
		

  		public boolean find(final int start, LiteralSearchMatcher lsm) throws Exception
  		{
    		//LiteralSearchMatcher matcher = new LiteralSearchMatcher("class", "",true);
			LiteralSearchMatcher matcher = lsm;
    		Segment text = new Segment();
    		Document buffer = this.getDocument();
    		buffer.getText(start, buffer.getLength() - start, text);

			int[] match = matcher.nextMatch(text.toString());
			if (match != null)
    		{
				this.grabFocus();	
		      	this.select(start + match[0], start + match[1]);
		      	return true;
    		} else
      			return false;
  		}		
		
		
	//*******************************************************************************
	
		public class LiteralSearchMatcher
		{

  			private char[] search;
  			private String replace;
  			private boolean ignoreCase;



  			public LiteralSearchMatcher(String search, String replace, boolean ignoreCase)
  			{
    			if (ignoreCase)
      				this.search = search.toUpperCase().toCharArray();
   				 else
      				this.search = search.toCharArray();
    				this.replace = replace;
    				this.ignoreCase = ignoreCase;
  			}



  			public int[] nextMatch(String text)
  			{
    			return nextMatch(text, 0);
  			}

  			public int[] nextMatch(String text, int index)
  			{
   				 char[] textChars = text.toCharArray();

			    int searchLen = search.length;
			    int len = textChars.length - searchLen + 1;
    			if (index >= len)
      				return null;

    			int result = -1;

    			if (ignoreCase)
    			{
					loop: for (int i = index; i < len; i++)
      				{
        				if (Character.toUpperCase(textChars[i]) == search[0])
        				{
          					for (int j = 1; j < searchLen; j++)
          					{
           						if (Character.toUpperCase(textChars[i + j]) != search[j])
            					{
					          		i += j - 1;
					          		continue loop;
            					}
          					}

          					result = i;
          					break loop;
        				}
      				}
    			} else {
					loop: for (int i = index; i < len; i++)
      				{
        				if (textChars[i] == search[0])
        				{
          					for (int j = 1; j < searchLen; j++)
          					{
           						if (textChars[i + j] != search[j])
            					{
              						i += j - 1;
              						continue loop;
            					}
          					}

          					result = i;
          					break loop;
        				}
      				}
    			}

    			if (result == -1)
      				return null;
   			 	else
    			{
      				int[] match = { result, result + searchLen };
      				return match;
    			}
  			}
		}



//****************************************	    
              
    }
    
    
    
    
    
      private class CompileOutput implements DiagnosticListener {
      	
      	
      	
      	public void report(Diagnostic diagnostic) {
      		txtaOutput.append(diagnostic+"\n");	
      	}
        
        
        
    }  
    
    
    
    
   
//    private class CompileOutput extends StringWriter {
//    
//    
//    
//        public CompileOutput() {
//            super();
//        }
//        
//        
//        
//        public void write(String str, int off, int len) {
//            txtaOutput.append(str+"\n");
//        }
//        
//        
//        
//    }
    
    
    
    
    
    private class Create_MDS_JavaProject extends Neo_Dialog implements ActionListener {
    
    
    
        MDS_Panel pnlBase = new MDS_Panel(new BorderLayout());

        MDS_TabbedPane tbdpType = new MDS_TabbedPane(MDS_TabbedPane.TOP);
        
        ConsoleTypes ctps = new ConsoleTypes();      
        ApplicationTypes atps = new ApplicationTypes();
        
    
        public Create_MDS_JavaProject() {
            super("MDS Java Project");
            tbdpType.add(ctps, "Console Application Types");
            tbdpType.add(atps, "Application Types");
            pnlBase.add(tbdpType, BorderLayout.CENTER);
            this.addPanel(pnlBase);
            this.setSize(300, 300);
            this.setCenterScreen();
            this.showDialog();
        }
        
        
        
        public void actionPerformed(ActionEvent e) {                
            if(e.getActionCommand().equals("Ok")) {
                if(tbdpType.getSelectedIndex() == 0) {
                    ctps.detectApplicationType();
                } else if(tbdpType.getSelectedIndex() == 1) {
                    atps.detectApplicationType();
                } 
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) { 
                this.dispose();
            }
        }
        
        
        
        
        
        private class ConsoleTypes extends MDS_Panel {
        
        
        
            MDS_RadioButton rbtnConsole1 = new MDS_RadioButton("An emplty project"); 
            MDS_RadioButton rbtnConsole2 = new MDS_RadioButton("A simple application");
            MDS_RadioButton rbtnConsole3 = new MDS_RadioButton("A Hello World application");
            MDS_RadioButton rbtnConsole4 = new MDS_RadioButton("Virtual Threaded");

            ButtonGroup btnGroup = new ButtonGroup();
            
            
        
            public ConsoleTypes() {
                //this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Console Application Types"));
                this.setLayout(new GridLayout(4,1));
                this.add(rbtnConsole1);
                rbtnConsole1.setSelected(true);
                btnGroup.add(rbtnConsole1);
                this.add(rbtnConsole2);
                btnGroup.add(rbtnConsole2);
                this.add(rbtnConsole3);
                btnGroup.add(rbtnConsole3);
                this.add(rbtnConsole4);
                btnGroup.add(rbtnConsole4);
            }
            
            
            
            public void detectApplicationType() {
                if(rbtnConsole1.isSelected()) {
                    create_EmptyProject();    
                } else if(rbtnConsole2.isSelected()) {
                    create_SimpleApplication();
                } else if(rbtnConsole3.isSelected()) {
                    create_HelloWorldApplication(); 
                } else if(rbtnConsole4.isSelected()) {
                    create_VirtualThreadedApplication();
                }   
            } 
            
            
            
            private void create_EmptyProject() {}
            
            
            
            private void create_SimpleApplication() {
                try {
                    File f = new File(projectPath+"SimpleApplication.java");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                    pw.println("");
                    pw.println("");
                    pw.println("public class SimpleApplication {");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public static void MDS_Main(String argv[]) {");
                    pw.println("");
                    pw.println("    }");
                    pw.println("");
                    pw.println("}");  
                    pw.close();
                    addClass(f, true);                    
                } catch(Exception ex) {}
            }
            
            
            
            private void create_HelloWorldApplication() {
                try {
                    File f = new File(projectPath+"HelloWorldApplication.java");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                    pw.println("");
                    pw.println("");
                    pw.println("public class HelloWorldApplication {");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public static void MDS_Main(String argv[]) {");
                    pw.println("        Console.println("+quote+"Hello World !"+quote+");");
                    pw.println("    }");
                    pw.println("");
                    pw.println("}");  
                    pw.close();
                    addClass(f, true);                    
                } catch(Exception ex) {}
            }
            
            
            
            private void create_VirtualThreadedApplication() {
                try {
                    File f = new File(projectPath+"VirtualThreadedApplication.java");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                    pw.println("");
                    pw.println("");
                    pw.println("public class VirtualThreadedApplication implements SystemSchedulerThreadListener {");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    private int count = 0;");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public VirtualThreadedApplication() {");
                    pw.println("        VirtualThreading.create_SST_VT(this, "+quote+"VT1"+quote+");");
                    pw.println("        Console.println("+quote+"Registering virtual thread named VT1"+quote+");");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public long getSystemScheduler_EventInterval() {");
                    pw.println("        return 500;");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");  
                    pw.println("    public void systemSchedulerEvent() {");
                    pw.println("        Console.println("+quote+"Hello from Virtual Threading."+quote+");");
                    pw.println("        count++;");
                    pw.println("        if(count >= 20) {");
                    pw.println("            Console.println("+quote+"Unregistering virtual thread named VT1"+quote+");");
                    pw.println("            VirtualThreading.terminate_SST_VT(this);");
                    pw.println("        }");
                    pw.println("    }");  
                    pw.println("");
                    pw.println("");
                    pw.println("");                
                    pw.println("    public static void MDS_Main(String argv[]) {");
                    pw.println("        new VirtualThreadedApplication();");    
                    pw.println("    }");
                    pw.println("");
                    pw.println("}");  
                    pw.close();
                    addClass(f, true);                    
                } catch(Exception ex) {}            
            }            
            
            
            
        } 
        
        
        
        
        
        private class ApplicationTypes extends MDS_Panel {   
        
        
        
            MDS_RadioButton rbtnApplication1 = new MDS_RadioButton("An emplty project"); 
            MDS_RadioButton rbtnApplication2 = new MDS_RadioButton("A simple application");
            MDS_RadioButton rbtnApplication3 = new MDS_RadioButton("A Hello World application");
            MDS_RadioButton rbtnApplication4 = new MDS_RadioButton("Screen Saver");        
            MDS_RadioButton rbtnApplication5 = new MDS_RadioButton("Sound Player"); 
        
            ButtonGroup btnGroup = new ButtonGroup();
            
            
            
            public ApplicationTypes() {
                //this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Application Types"));
                this.setLayout(new GridLayout(5,1));
                this.add(rbtnApplication1);
                rbtnApplication1.setSelected(true);
                btnGroup.add(rbtnApplication1);
                this.add(rbtnApplication2);
                btnGroup.add(rbtnApplication2);
                this.add(rbtnApplication3);
                btnGroup.add(rbtnApplication3);
                this.add(rbtnApplication4);
                btnGroup.add(rbtnApplication4);    
                this.add(rbtnApplication5);
                btnGroup.add(rbtnApplication5);         
            }
            
            
            
            public void detectApplicationType() {
                if(rbtnApplication1.isSelected()) {
                    create_EmptyProject();    
                } else if(rbtnApplication2.isSelected()) {
                    create_SimpleApplication();
                } else if(rbtnApplication3.isSelected()) {
                    create_HelloWorldApplication(); 
                } else if(rbtnApplication4.isSelected()) {
                    create_ScreenSaver();
                } else if(rbtnApplication5.isSelected()) {
                    soundPlayer();
                }                 
            }
            
            
            
            public void create_EmptyProject() {}
            
            
            
            public void create_SimpleApplication() {
                try {
                    File f = new File(projectPath+"SimpleApplication.java");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                    pw.println("");
                    pw.println("");
                    pw.println("public class SimpleApplication {");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public SimpleApplication() {");
                    pw.println("        MDS_Frame frame = new MDS_Frame("+quote+"Frame"+quote+", true, true, true, true);");
                    pw.println("        frame.setSize(300, 300);");
                    pw.println("        frame.setCenterScreen();");
                    pw.println("        frame.setVisible(true);");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public static void MDS_Main(String argv[]) {");
                    pw.println("        new SimpleApplication();");
                    pw.println("    }");
                    pw.println("");
                    pw.println("}");  
                    pw.close();
                    addClass(f, true);                    
                } catch(Exception ex) {}            
            }
            
            
            
            public void create_HelloWorldApplication() {
                try {
                    File f = new File(projectPath+"HelloWorldApplication.java");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                    pw.println("");
                    pw.println("import javax.swing.*;");
                    pw.println("import javax.swing.event.*;");
                    pw.println("import java.awt.*;");
                    pw.println("import java.awt.event.*;");
                    pw.println("import java.util.*;");
                    pw.println("import java.io.*;");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("public class HelloWorldApplication extends MDS_Frame implements ActionListener, Runnable {");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    private MDS_User usr = MDS.getUser();");
                    pw.println("");
                    pw.println("    private JComponent contentPane;");
                    pw.println("");
                    pw.println("    private JMenuBar mnb = new JMenuBar();");
                    pw.println("    private MDS_Menu mnuFile = new MDS_Menu("+quote+"File"+quote+", KeyEvent.VK_F);");
                    pw.println("    private JMenuItem mniOpen = usr.createMenuItem("+quote+"Open"+quote+", this, KeyEvent.VK_O);");
                    pw.println("    private JMenuItem mniExit = usr.createMenuItem("+quote+"Exit"+quote+", this, KeyEvent.VK_X);");
                    pw.println("    private MDS_Menu mnuHelp = new MDS_Menu("+quote+"Help"+quote+", KeyEvent.VK_H);");
                    pw.println("    private JMenuItem mniAbout = usr.createMenuItem("+quote+"About"+quote+", this, KeyEvent.VK_A);");               
                    pw.println("");
                    pw.println("    private MDS_TextArea txtaText = new MDS_TextArea();");
                    pw.println("");
                    pw.println("    private Thread threadLoadFile;");
                    pw.println("    private File file;");
                    pw.println("    private boolean shouldClose = false;");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public HelloWorldApplication() {");
                    pw.println("        super("+quote+"Hello World"+quote+", true, true, true, true);");
                    pw.println("        contentPane = (JComponent)this.getContentPane();");
                    pw.println("        contentPane.setLayout(new BorderLayout());");
                    pw.println("        mnuFile.add(mniOpen);");
                    pw.println("        mnuFile.addSeparator();");
                    pw.println("        mnuFile.add(mniExit);");
                    pw.println("        mnb.add(mnuFile);");
                    pw.println("        mnuHelp.add(mniAbout);");
                    pw.println("        mnb.add(mnuHelp);");
                    pw.println("        this.setJMenuBar(mnb);");
                    pw.println("        contentPane.add(new MDS_ScrollPane(txtaText), BorderLayout.CENTER);");
                    pw.println("        ");
                    pw.println("        this.addInternalFrameListener(new InternalFrameAdapter() {");
                    pw.println("            public void internalFrameClosed(InternalFrameEvent e) {");
                    pw.println("                shouldClose = true;");
                    pw.println("                if(threadLoadFile != null) {");
                    pw.println("                    threadLoadFile.interrupt();   ");
                    pw.println("                }");
                    pw.println("                threadLoadFile = null;"); 
                    pw.println("            }");
                    pw.println("         });");
                    pw.println("        ");                     
                    pw.println("        this.setSize(600, 500);");
                    pw.println("        this.setCenterScreen();");
                    pw.println("        this.setVisible(true);");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public void run() {");
                    pw.println("        try {");
                    pw.println("            BufferedReader in = new BufferedReader(new FileReader(file));");
                    pw.println("            String s= new String();");
                    pw.println("            while((s = in.readLine())!= null && !shouldClose) txtaText.append(s+"+quote+"\\"+"n"+quote+");");
                    pw.println("        } catch(Exception ex) {}");
                    pw.println("        ");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public void actionPerformed(ActionEvent e) {");
                    pw.println("        if(e.getActionCommand().equals("+quote+"Open"+quote+")) {");
                    pw.println("            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);");
                    pw.println("            Vector v = new Vector();");  
                    pw.println("            v.addElement("+quote+"java"+quote+");");                  
                    pw.println("            fmfc.setFilter(v);");
                    pw.println("            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {");
                    pw.println("                file = fmfc.getFile();");
                    pw.println("                threadLoadFile = new Thread(this, "+quote+"Hello World file loaing"+quote+");");
                    pw.println("                threadLoadFile.start();");
                    pw.println("                ");
                    pw.println("            }");
                    pw.println("        } else if(e.getActionCommand().equals("+quote+"Exit"+quote+")) {");
                    pw.println("            this.dispose();");
                    pw.println("        } else if(e.getActionCommand().equals("+quote+"About"+quote+")) {");
                    pw.println("            MDS_OptionPane.showMessageDialog(this, "+quote+"Hello World Allpication built to MDS Platform."+quote+", "+quote+"MDS"+quote+", MDS_OptionPane.INFORMATION_MESSAGE);");
                    pw.println("        }");
                    pw.println("");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public static void MDS_Main(String argv[]) {");
                    pw.println("        new HelloWorldApplication();");
                    pw.println("    }");
                    pw.println("");
                    pw.println("}");  
                    pw.close();
                    addClass(f, true);                    
                } catch(Exception ex) {}               
            }
            
            
            
            private void create_ScreenSaver() {
                try {
                    File f = new File(projectPath+"ScrRectangle.java");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                    pw.println("");
                    pw.println("import java.awt.*;");
                    pw.println("import java.util.*;");
                    pw.println("");
                    pw.println("");
                    pw.println(""); 
                    pw.println("public class ScrRectangle extends ScreenSaver implements Runnable {");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    private int x;");
                    pw.println("    private int y;");
                    pw.println("    private int r;");
                    pw.println("    private int g;");
                    pw.println("    private int b;");
                    pw.println("    private Thread thread;");
                    pw.println("    private static boolean running = false;");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public ScrRectangle() {");
                    pw.println("        super();");
                    pw.println("        running = true;");
                    pw.println("        this.setBackground(Color.black);");
                    pw.println("        this.setVisible(true);");
                    pw.println("        thread = new Thread(this);");
                    pw.println("        thread.setPriority(Thread.MIN_PRIORITY);");
                    pw.println("        thread.start();");   
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public void run() {");
                    pw.println("        while(running) {");
                    pw.println("            Random rnd = new Random();");
                    pw.println("            x = Math.abs(rnd.nextInt()) % (800 - 300);");
                    pw.println("            y = Math.abs(rnd.nextInt()) % (600 - 210);");
                    pw.println("            r = Math.abs(rnd.nextInt()) % 255;");
                    pw.println("            g = Math.abs(rnd.nextInt()) % 255;");
                    pw.println("            b = Math.abs(rnd.nextInt()) % 255;");            
                    pw.println("            this.repaint();");
                    pw.println("            try {");
                    pw.println("                thread.sleep(1000);");
                    pw.println("            } catch(InterruptedException ex) {}");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public static boolean isRunning() {");
                    pw.println("        return running;");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public void terminate_Scr() {");
                    pw.println("        this.dispose();");
                    pw.println("        running = false;");
                    pw.println("        if(thread != null) {");
                    pw.println("            thread.interrupt();   ");
                    pw.println("        }");
                    pw.println("        thread = null;"); 
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public void paintComponent(Graphics grp) {");
                    pw.println("        super.paintComponent(grp);");
                    pw.println("        grp.setColor(new Color(r, g, b));");
                    pw.println("        grp.fillRoundRect(x, y, 300, 210, 5, 5);");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public static void MDS_Main(String arg[]) { ");
                    pw.println("        if(!ScrRectangle.isRunning()) {");
                    pw.println("            new ScrRectangle();");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println("");
                    pw.println("}");
                    pw.close();
                    addClass(f, true);                      
                } catch(Exception ex) {}                 
            }
            
            
            
            private void soundPlayer() {
                try {
                    File f = new File(projectPath+"SoundPlayer.java");
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f));
                    pw.println("");
                    pw.println("import javax.swing.*;");
                    pw.println("import javax.swing.event.*;");
                    pw.println("import java.awt.*;");
                    pw.println("import java.awt.event.*;");
                    pw.println("import java.io.*;");
                    pw.println("import java.util.*;");
                    pw.println("import javax.sound.sampled.*;");
                    pw.println("import javax.sound.midi.*;");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("public class SoundPlayer extends MDS_Frame implements ActionListener{");  
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    private MDS_Sound snd = MDS.getSound();");
                    pw.println("    private JComponent contentPane;");
                    pw.println("    private MDS_Label lblFileName = new MDS_Label();");
                    pw.println("    private MDS_Panel pnlButtons = new MDS_Panel(new FlowLayout());");
                    pw.println("    private MDS_Button btnOpen = new MDS_Button("+quote+"Open"+quote+");");
                    pw.println("    private MDS_Button btnPlay = new MDS_Button("+quote+"Play"+quote+");");
                    pw.println("    private MDS_Button btnStop = new MDS_Button("+quote+"Stop"+quote+");");
                    pw.println("    private File file;");
                    pw.println("    private Sequencer sequencer;");
                    pw.println("    private Object currentSound;");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public SoundPlayer() {");
                    pw.println("        super("+quote+"Sound Player"+quote+", false, true, false, false);");
                    pw.println("        contentPane =(JComponent)this.getContentPane();");
                    pw.println("        contentPane.setLayout(new GridLayout(2,1));");
                    pw.println("        btnOpen.addActionListener(this);");
                    pw.println("        pnlButtons.add(btnOpen);");
                    pw.println("        btnPlay.addActionListener(this);");
                    pw.println("        pnlButtons.add(btnPlay);");
                    pw.println("        btnStop.addActionListener(this);");
                    pw.println("        pnlButtons.add(btnStop);");
                    pw.println("        contentPane.add(lblFileName);");
                    pw.println("        lblFileName.setHorizontalAlignment(SwingConstants.CENTER);");
                    pw.println("        contentPane.add(pnlButtons);");
                    pw.println("        ");
                    pw.println("        this.addInternalFrameListener(new InternalFrameAdapter() {");
                    pw.println("            public void internalFrameClosed(InternalFrameEvent e) {");
                    pw.println("                stopSound();");
                    pw.println("            }");
                    pw.println("         });");
                    pw.println("        ");                    
                    pw.println("        this.setSize(400, 200);");
                    pw.println("        this.setCenterScreen();");
                    pw.println("        this.setVisible(true);");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    public void actionPerformed(ActionEvent e) {");
                    pw.println("        if(e.getActionCommand().equals("+quote+"Open"+quote+")) {");
                    pw.println("            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);");
                    pw.println("            Vector v = new Vector();");  
                    pw.println("            v.addElement("+quote+"mid"+quote+");");
                    pw.println("            v.addElement("+quote+"wav"+quote+");");                  
                    pw.println("            fmfc.setFilter(v);");
                    pw.println("            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {");
                    pw.println("                file = fmfc.getFile();");
                    pw.println("                lblFileName.setText(file.getName());"); 
                    pw.println("            }");
                    pw.println("        } else if(e.getActionCommand().equals("+quote+"Play"+quote+")) {");
                    pw.println("            sequencer = snd.getSequencer();");
                    pw.println("            currentSound = snd.loadSound(file, sequencer);");
                    pw.println("            snd.playSound(currentSound, sequencer);");
                    pw.println("        } else if(e.getActionCommand().equals("+quote+"Stop"+quote+")) {");
                    pw.println("            stopSound();");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println("");
                    pw.println("");
                    pw.println("");
                    pw.println("    private void stopSound() {");
                    pw.println("        if (currentSound instanceof Clip) {");
                    pw.println("            ((Clip)currentSound).stop();");
                    pw.println("        } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {");
                    pw.println("            sequencer.stop();");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println("");
                    pw.println("    public static void MDS_Main(String arg[]) { ");
                    pw.println("        new SoundPlayer();");
                    pw.println("    }");
                    pw.println("");
                    pw.println("}");
                    pw.close();
                    addClass(f, true);                      
                } catch(Exception ex) {}                                    
            }   
            
            
        }    
        
        
        
    }
    
    
    
    
    
    private class RunTimeProperties extends Neo_Dialog implements ActionListener{
    
    
    
        MDS_Panel pnlBase = new MDS_Panel(new GridLayout(2,1, 5, 5));
        MDS_TextField txtfMainClass = new MDS_TextField();
        MDS_TextField txtfAppParameters = new MDS_TextField();
        MDS_Panel pnl1 = new MDS_Panel(new BorderLayout());
        MDS_Panel pnl2 = new MDS_Panel(new BorderLayout());
        
    
    
        public RunTimeProperties() {
            super("Runtime Properties");
            pnl1.add(new MDS_Label("Main class"), BorderLayout.NORTH);
            if(!mainClassName.equals("")) txtfMainClass.setText(mainClassName);
            pnl1.add(txtfMainClass, BorderLayout.SOUTH);
            pnlBase.add(pnl1);
            pnl2.add(new MDS_Label("Application Parameters"), BorderLayout.NORTH);
            pnl2.add(txtfAppParameters, BorderLayout.SOUTH);
            pnlBase.add(pnl2);
            this.addPanel(pnlBase);
            this.setSize(300, 170);
            this.setCenterScreen();
            this.setVisible(true);        
        }    
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Ok")) {
                if(!txtfMainClass.getText().endsWith(".class")) {
                    mainClassName = txtfMainClass.getText()+".class";    
                } else {
                    mainClassName = txtfMainClass.getText();
                }
                applicationParameters = txtfAppParameters.getText();  
                saveProject();  
                
                if(shouldRun) {
                    if(!mainClassName.endsWith(".class")) mainClassName = mainClassName.concat(".class");
                    prm.execute(new File(projectPath, mainClassName));
                    shouldRun = false;                
                }
                       
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                this.dispose();
            }
        }
        
        
        
    }    
    
    
    
    
    
    private class Neo_TextArea extends MDS_TextArea {
    
    
    
        public Neo_TextArea() {
            this.setFont(new Font("Courier", Font.PLAIN, 13));
			this.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					System.out.println("WS SSS CC NEW");
					
					//currentJavaScrFile.
					//scrlSourceFile.
				}
			});
        }
        
        
        
        public void append(String text) {
            super.append(text);
            Document doc = this.getDocument();
            this.setCaretPosition(doc.getLength());        
        }
        
        
        
        public void setText(String text) {
            super.setText(text);
            Document doc = this.getDocument();
            this.setCaretPosition(doc.getLength());          
        }
        
        
        
    }                      
    
    
    
}    