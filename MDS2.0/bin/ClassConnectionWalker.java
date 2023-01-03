/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree .*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;



public class ClassConnectionWalker extends MDS_Frame implements ActionListener {



    private MDS_User usr = MDS.getUser();

    private JComponent contentPane;
    private JMenuBar mnbCCW = new JMenuBar();
    private JMenu mnuFile = new JMenu("File");
    private JMenuItem mniOpen = usr.createMenuItem("Open Class", this, MDS_KeyStroke.getOpen(), KeyEvent.VK_O);
    private JMenuItem mniOpenFile = usr.createMenuItem("Open Class File", this);
    private JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    private JMenu mnuView = new JMenu("View");
    private JCheckBoxMenuItem cmniDeclaredInfo = new JCheckBoxMenuItem("Show Declared Information Only");
    private JMenu mnuHelp = new JMenu("Help");
    private JMenuItem mniAbout = usr.createMenuItem("About ...", this, KeyEvent.VK_A);
    
    private JToolBar tlbCCW = new JToolBar();
    private MDS_TabbedPane tbpClassHolder = new MDS_TabbedPane(); 
    
    private boolean urlClass = false;
    private String currentClass_p;
    private boolean showAllInformation = false;
    private  ClassConnectionWalker ccwFrame = this;
      


    public ClassConnectionWalker() {
        super("Class Connection Walker",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-blockdevice.png"));       
        contentPane = (JComponent) this.getContentPane(); 
        contentPane.add(tlbCCW, BorderLayout.NORTH);
        
        mnuFile.add(mniOpen);
        mnuFile.add(mniOpenFile);
        mnuFile.add(mniExit);
        mnuFile.setMnemonic('F');
        mnbCCW.add(mnuFile);
        mnuView.setMnemonic('V');
        mnbCCW.add(mnuView);
        cmniDeclaredInfo.setState(true);
        cmniDeclaredInfo.addActionListener(this);
        mnuView.add(cmniDeclaredInfo);
        mnuHelp.add(mniAbout);
        mnuHelp.setMnemonic('H');
        mnbCCW.add(mnuHelp); 
        this.setJMenuBar(mnbCCW);
        this.setBounds(0, 0, 800, 600);
        this.setVisible(true);                           
    }

    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open Class")) {
            urlClass = false;
            String tabName = "";
            String cName = "";
            cName = MDS_OptionPane.showInputDialog(this, "Type the full quilified class Name you want to see.\n eg: java.lang.Object", "Class Connection Walker", JOptionPane.QUESTION_MESSAGE);
            
            if(cName == null) {
                cName = "";            
            }
            if(!cName.equals("")) {
                currentClass_p = cName;
                loadClassDetails(cName);               
            } else {
            
            }
        } else if(e.getActionCommand().equals("Open Class File")) {
            urlClass = true; 
            String tabName = "";
            String cName = "";
            MDS_FileChooser fc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            Vector v = new Vector();
            v.add("class");
            fc.setFilter(v);
            if(fc.showDiaog(this) == fc.APPROVE_OPTION) {   
                currentClass_p = fc.getFile().getPath();
                loadClassDetails(fc.getFile().getPath());                                                         
            }   
        } else if(e.getActionCommand().equals("Show Declared Information Only")) {
            if(cmniDeclaredInfo.getState()) {
                showAllInformation = false;  
                loadClassDetails(currentClass_p);        
            } else {
                showAllInformation = true;
                loadClassDetails(currentClass_p); 
            }                              
        } else if(e.getActionCommand().equals("About ...")) {
            usr.showAboutDialog(this, "Class Connection Walker", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"ClassConnectionWalkerpng.png"), MDS.getAbout_Mahesh());
        } else if(e.getActionCommand().equals("Exit")) {
            this.dispose();    
        }
    }
    
    

    
    
    
    public static void MDS_Main(String arg[]) {  
        ClassConnectionWalker ccw = new ClassConnectionWalker();
        if(arg.length == 0) {
            ccw.currentClass_p = "java.lang.Object";
            ccw.loadClassDetails("java.lang.Object");  
        } else if(arg.length == 1) {
            ccw.currentClass_p = arg[0];
            ccw.loadClassDetails(arg[0]); 
        }        
    }
    
    
    
    private void loadClassDetails(String cls) { 
    
    
    
        class ClassConnectionPanel extends MDS_Panel {
    
    
    
            JSplitPane sltpV1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            JSplitPane sltpH1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            JSplitPane sltpH2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            JSplitPane sltpH3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            JSplitPane sltpH4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
            MDS_TableModel tmdlFields = new MDS_TableModel();
            MDS_TableModel tmdlMethods = new MDS_TableModel();
            MDS_TableModel tmdlConstuctors = new MDS_TableModel();
            MDS_TableModel tmdlInnerClasses = new MDS_TableModel();
            
            MDS_Table tblFields = new MDS_Table(tmdlFields);
            MDS_Table tblConstractors = new MDS_Table(tmdlConstuctors);
            MDS_Table tblMethods = new MDS_Table(tmdlMethods);
            MDS_Table tblInnerClasses = new MDS_Table(tmdlInnerClasses);
        
            DefaultListModel dlmdl = new DefaultListModel();
            JList lstInterfaces = new JList(dlmdl);
        
            DefaultMutableTreeNode root; 
            MDS_Tree treInheretance;
            
            String cls_p;
            Class currentClass;
    
    
    
            public ClassConnectionPanel(String cl) {
            
                cls_p = cl;
         
                this.setLayout(new BorderLayout());
            
                sltpV1.setDividerLocation(150);
                sltpV1.setDividerSize(5);
                sltpH1.setDividerLocation(350);
                sltpH1.setDividerSize(5);
                sltpH2.setDividerLocation(200);
                sltpH2.setDividerSize(5);
                sltpH3.setDividerLocation(90);
                sltpH3.setDividerSize(5);
                sltpH4.setDividerLocation(250);
                sltpH4.setDividerSize(5);
            
                sltpH1.setTopComponent(new JLabel(""));
                lstInterfaces.setToolTipText("Interfaces");
                sltpH1.setBottomComponent(new JScrollPane(lstInterfaces));
         
                sltpV1.setLeftComponent(sltpH1); 
            
                tblFields.setToolTipText("Fields");
                sltpH3.setTopComponent(new JScrollPane(tblFields));
            
                tblConstractors.setToolTipText("Constructors");
                sltpH3.setBottomComponent(new JScrollPane(tblConstractors));
            
                sltpH2.setTopComponent(sltpH3);
            
                tblMethods.setToolTipText("Methods");
                sltpH4.setTopComponent(new JScrollPane(tblMethods));
            
                tblInnerClasses.setToolTipText("Inner Classes");
                sltpH4.setBottomComponent(new JScrollPane(tblInnerClasses));           
            
                sltpH2.setBottomComponent(sltpH4);
            
                sltpV1.setRightComponent(sltpH2);
            
                this.add(sltpV1, BorderLayout.CENTER);  
            
                tmdlFields.addColumn("Modifiers");
                tmdlFields.addColumn("Type"); 
                tmdlFields.addColumn("Name");
            
                tmdlConstuctors.addColumn("Modifiers");
                tmdlConstuctors.addColumn("Name");
                tmdlConstuctors.addColumn("Parameter Types");
                tmdlConstuctors.addColumn("Exception Types");
            
                tmdlMethods.addColumn("Modifiers");
                tmdlMethods.addColumn("Return Type");
                tmdlMethods.addColumn("Name");
                tmdlMethods.addColumn("Parameter Types");
                tmdlMethods.addColumn("Exception Types");
            
                tmdlInnerClasses.addColumn("Modifiers");
                tmdlInnerClasses.addColumn("Name");
                          
            }
            
            
            
            private boolean loadClass(String cls) {
                try {
                    if(cls.endsWith(".class")) {
                        File cf = new File(cls);
                        MDS_ClassLoader cld = new MDS_ClassLoader();
                        currentClass = cld.loadClass(cf, false);            
                    } else {
                        currentClass = Class.forName(cls);
                    }
                } catch(Exception ex) {
                    if(ex instanceof ClassNotFoundException) {
                        MDS_OptionPane.showMessageDialog(ccwFrame, "The Class you mentioned is not found : "+cls, "Class Connection Walker", JOptionPane.ERROR_MESSAGE);
                        return false;                   
                    } else {
                        MDS_OptionPane.showMessageDialog(ccwFrame, ex.toString(), "Class Connection Walker", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }             
                }
        
                return true;
        
            }            
         
        
        
        
            private void loadClassInfo() {
            
                if(!loadClass(cls_p)) return;
            
                try {
                
                    Field fields[];
                    
                    if(showAllInformation) {
                        fields = currentClass.getFields();
                    } else {
                        fields = currentClass.getDeclaredFields();
                    }
                    
                    for(int x= 0; x < fields.length; x++) {
                        Vector vctFields = new Vector();
                    
                        vctFields.addElement(Modifier.toString(fields[x].getModifiers()));
                        vctFields.addElement(fields[x].getType().getName());
                        vctFields.addElement(fields[x].getName());
                    
                        tmdlFields.addRow(vctFields);
                    
                    }
                    
                    Constructor constructors[];
                
                    if(showAllInformation) {
                        constructors = currentClass.getConstructors();
                    } else {
                        constructors = currentClass.getDeclaredConstructors();
                    }
                    
                    for(int x= 0; x < constructors.length; x++) {
                        Vector vctConstructors = new Vector();
                      
                        vctConstructors.addElement(Modifier.toString(constructors[x].getModifiers()));
                        vctConstructors.addElement(constructors[x].getName());
                    
                        Class parameterTypes[] =  constructors[x].getParameterTypes();  
                        String parameters = "";
                    
                      
                        for(int y= 0; y < parameterTypes.length; y++) {
                            if(y == parameterTypes.length - 1 ) {
                                parameters = parameters.concat(parameterTypes[y].getName()); 
                            } else {
                                parameters = parameters.concat(parameterTypes[y].getName()+", ");
                            }
                        }
                    
                        vctConstructors.addElement(parameters);
                    
                        Class exceptionTypes[] =  constructors[x].getExceptionTypes();  
                        String exceptions = "";
                    
                    
                        for(int y= 0; y < exceptionTypes.length; y++) {
                            if(y == parameterTypes.length - 1 ) {
                                exceptions = exceptions.concat(exceptionTypes[y].getName()); 
                            } else {
                                exceptions = exceptions.concat(exceptionTypes[y].getName()+", ");
                            }
                        }
                    
                        vctConstructors.addElement(exceptions);                    
                    
                        tmdlConstuctors.addRow(vctConstructors);
                    
                    }                 
                    
                    Method methods[];
                    
                    if(showAllInformation) {
                        methods = currentClass.getMethods();
                    } else {
                        methods = currentClass.getDeclaredMethods();
                    }
                    
                    for(int x= 0; x < methods.length; x++) {
                        Vector vctMethods = new Vector();
                     
                        vctMethods.addElement(Modifier.toString(methods[x].getModifiers()));
                        vctMethods.addElement(methods[x].getReturnType().getName()); 
                        vctMethods.addElement(methods[x].getName()); 

                        Class parameterTypes[] =  methods[x].getParameterTypes();  
                        String parameters = "";
                    
                        for(int y= 0; y < parameterTypes.length; y++) {
                            if(y == parameterTypes.length - 1 ) {
                                parameters = parameters.concat(parameterTypes[y].getName()); 
                            } else {
                                parameters = parameters.concat(parameterTypes[y].getName()+", ");
                            }
                        }
                                    
                        vctMethods.addElement(parameters);
                                      
                        Class exceptionTypes[] =  methods[x].getExceptionTypes();  
                        String exceptions = "";
                    
                    
                        for(int y= 0; y < exceptionTypes.length; y++) {
                            if(y == parameterTypes.length - 1 ) {
                                exceptions = exceptions.concat(exceptionTypes[y].getName()); 
                            } else {
                                exceptions = exceptions.concat(exceptionTypes[y].getName()+", ");
                            }
                        }
                    
                        vctMethods.addElement(exceptions);  
                                        
                        tmdlMethods.addRow(vctMethods);
                    
                    }
                
                    Class innerClasses[] = currentClass.getClasses();
                    
                    if(showAllInformation) {
                        innerClasses = currentClass.getClasses();
                    } else {
                        innerClasses = currentClass.getDeclaredClasses();
                    }
                    
                    for(int x= 0; x < innerClasses.length; x++) {
                        Vector vctInnerClasses = new Vector();   
                        
                        vctInnerClasses.addElement(Modifier.toString(innerClasses[x].getModifiers()));
                        vctInnerClasses.addElement(innerClasses[x].getName());
                        
                        tmdlInnerClasses.addRow(vctInnerClasses);
                        
                    } 
                
                
                
                    Class interfaces[] = currentClass.getInterfaces();              
                    
                    
                    for(int x= 0; x < interfaces.length; x++) {
                   
                        dlmdl.addElement(interfaces[x].getName());     

                    } 
                
                
                    Vector vctInheritance = new Vector();
                 
                    Class tempClass = currentClass;
                    
                
                    while(tempClass.getSuperclass() != null) {
                        vctInheritance.addElement(tempClass.getSuperclass().getName());
                        tempClass  = tempClass.getSuperclass(); 
                    }
                
                    DefaultMutableTreeNode previousNode = null;
                    DefaultMutableTreeNode node;
                    
                    for(int x = vctInheritance.size()-1; x >= 0; x--) {
                                             
                        if(x == vctInheritance.size()-1) {
                            root = new DefaultMutableTreeNode(vctInheritance.elementAt(x));
                            previousNode = root;
                        } else {
                            node = new DefaultMutableTreeNode(vctInheritance.elementAt(x));
                            previousNode.add(node);
                            previousNode = node;
                        }                         
                    
                    }        
                
                    node = new DefaultMutableTreeNode(currentClass.getName()); 
                    if(previousNode != null) previousNode.add(node);       
                
                    treInheretance = new MDS_Tree(root);
                    //treInheretance.expandRow(rowCount);
                    
                    treInheretance.setToolTipText("Class Inheritance");
                    sltpH1.setTopComponent(new JScrollPane(treInheretance));
      
                    if(tbpClassHolder.getTabCount() != 0) {
                        tbpClassHolder.removeTabAt(0);
                    }       
                
                    tbpClassHolder.add(currentClass.getName() ,this);
                    contentPane.add(tbpClassHolder, BorderLayout.CENTER);                
                
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                  
            }
        
        
    
        }
        
        
        ClassConnectionPanel ccp = new ClassConnectionPanel(cls);
        ccp.loadClassInfo();
        contentPane.validate();
        
    } //*      
    
    
    
}    