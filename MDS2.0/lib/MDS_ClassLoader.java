
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.jar.*;
 


public class MDS_ClassLoader {



    private File classFile;
    private String className;
    private URL urlClassPath;
    private UrlClassLoader cld; 
    private boolean checkForIllegalClasses = true;
    
    private FileManager fm = MDS.getFileManager();
    private static Hashtable illegalClasses = new Hashtable();
    private static Hashtable illegalClassNames = new Hashtable();
    
 
 
    static {
    	
    	/*
        //AWT 
        illegalClasses.put("java.awt.Button", "MDS_Button");   
        illegalClasses.put("java.awt.Canvas", "MDS_Panel");
        illegalClasses.put("java.awt.Checkbox", "MDS_Checkbox");
        illegalClasses.put("java.awt.Choice", "MDS_Choice");
        //illegalClasses.put("java.awt.Component", "JComponent");
        //illegalClasses.put("java.awt.Container", "MDS_Container");
        illegalClasses.put("java.awt.Dialog", "MDS_Dialog");
        illegalClasses.put("java.awt.FileDialog", "MDS_FileChooser");
        illegalClasses.put("java.awt.Frame", "MDS_Frame");
        illegalClasses.put("java.awt.Label", "MDS_Label");
        illegalClasses.put("java.awt.List", "MDS_List");
        illegalClasses.put("java.awt.ScrollBar", "MDS_ScrollBar");
        illegalClasses.put("java.awt.ScrollPane", "MDS_ScrollPane");
        illegalClasses.put("java.awt.TextArea", "MDS_TextArea");
        illegalClasses.put("java.awt.TextField", "MDS_TextField");
        illegalClasses.put("java.awt.TextComponent", "MDS_TextComponent");      
        illegalClasses.put("java.awt.Window", "MDS_Window");
        
        //SWING
        //illegalClasses.put("javax.swing.AbstractButton", "MDS_AbstractButton");
        illegalClasses.put("javax.swing.JButton", "MDS_Button");
        illegalClasses.put("javax.swing.JCheckBox", "MDS_CheckBox");
        illegalClasses.put("javax.swing.JCheckBoxMenuItem", "MDS_CheckBoxMenuItem");
        illegalClasses.put("javax.swing.JColorChooser", "MDS_ColorChooser");
        illegalClasses.put("javax.swing.JComboBox", "MDS_ComboBox");
        //illegalClasses.put("javax.swing.JComponent", "MDS_Component");
        illegalClasses.put("javax.swing.JDesktopPane", "MDS_DesktopPane");
        illegalClasses.put("javax.swing.JDialog", "MDS_Dialog");
        illegalClasses.put("javax.swing.JEditorPane", "MDS_EditorPane");
        illegalClasses.put("javax.swing.JFileChooser", "MDS_FileChooser");
        illegalClasses.put("javax.swing.JFormattedTextField", "MDS_FormattedTextField");   
        illegalClasses.put("javax.swing.JFrame", "MDS_Frame");
        illegalClasses.put("javax.swing.JInternalFrame", "MDS_Frame");
        //illegalClasses.put("javax.swing.JLabel", "MDS_Label");
        //illegalClasses.put("javax.swing.JLayeredPane", "MDS_LayeredPane");
        illegalClasses.put("javax.swing.JList", "MDS_List");
        //illegalClasses.put("javax.swing.JMenu", "MDS_Menu");
        illegalClasses.put("javax.swing.JOptionPane", "MDS_OptionPane");
        illegalClasses.put("javax.swing.JPanel", "MDS_Panel");
        illegalClasses.put("javax.swing.JPasswordField", "MDS_PasswordField");
        illegalClasses.put("javax.swing.JPopupMenu", "MDS_PopupMenu");
        illegalClasses.put("javax.swing.JProgressBar", "MDS_ProgressBar");
        illegalClasses.put("javax.swing.JRadioButton", "MDS_RadioButton");
        illegalClasses.put("javax.swing.JScrollBar", "MDS_ScrollBar"); 
        //illegalClasses.put("javax.swing.JScrollPane", "MDS_ScrollPane");  
        illegalClasses.put("javax.swing.JSlider", "MDS_Slider");   
        illegalClasses.put("javax.swing.JSpinner", "MDS_Spinner");
        illegalClasses.put("javax.swing.JTabbedPane", "MDS_TabbedPane");
        illegalClasses.put("javax.swing.JTable", "MDS_Table");
        illegalClasses.put("javax.swing.JTextArea", "MDS_TextArea");
        illegalClasses.put("javax.swing.JTextField", "MDS_TextField");
        illegalClasses.put("javax.swing.JTextPane", "MDS_TextPane");
        illegalClasses.put("javax.swing.JToggleButton", "MDS_ToggleButton");
        illegalClasses.put("javax.swing.JToolBar", "MDS_ToolBar");
        illegalClasses.put("javax.swing.JTree", "MDS_Tree");
        illegalClasses.put("javax.swing.JWindow", "MDS_Window");
        illegalClasses.put("javax.swing.JWindow", "MDS_Window");
        
        //Other Java Classes
        illegalClasses.put("java.lang.ClassLoader", "MDS_ClassLoader");
        illegalClasses.put("java.lang.Thread", "MDS_Thread");
        illegalClasses.put("java.lang.ThreadGroup", "MDS_ThreadGroup");
        illegalClasses.put("java.lang.Toolkit", "MDS_System");
        illegalClasses.put("java.lang.Runtime", "MDS_System");
        illegalClasses.put("java.lang.System", "MDS_System & Console");
        
        //MDS Classes
        //illegalClasses.put("", ""); 
        
        
        //==============================================================================
        
        //AWT 
        illegalClassNames.put("Button", "MDS_Button");   
        illegalClassNames.put("Canvas", "MDS_Panel");
        illegalClassNames.put("Checkbox", "MDS_Checkbox");
        illegalClassNames.put("java.awt.Choice", "MDS_Choice");
        //illegalClassNames.put("Component", "JComponent");
        //illegalClassNames.put("Container", "MDS_Container");
        illegalClassNames.put("Dialog", "MDS_Dialog");
        illegalClassNames.put("FileDialog", "MDS_FileChooser");
        illegalClassNames.put("Frame", "MDS_Frame");
        illegalClassNames.put("Label", "MDS_Label");
        illegalClassNames.put("List", "MDS_List");
        illegalClassNames.put("ScrollBar", "MDS_ScrollBar");
        illegalClassNames.put("ScrollPane", "MDS_ScrollPane");
        illegalClassNames.put("TextArea", "MDS_TextArea");
        illegalClassNames.put("TextField", "MDS_TextField");
        illegalClassNames.put("TextComponent", "MDS_TextComponent");      
        illegalClassNames.put("Window", "MDS_Window");
        
        //SWING
        //illegalClassNames.put("AbstractButton", "MDS_AbstractButton");
        illegalClassNames.put("JButton", "MDS_Button");
        illegalClassNames.put("JCheckBox", "MDS_CheckBox");
        illegalClassNames.put("JCheckBoxMenuItem", "MDS_CheckBoxMenuItem");
        illegalClassNames.put("JColorChooser", "MDS_ColorChooser");
        illegalClassNames.put("JComboBox", "MDS_ComboBox");
        //illegalClassNames.put("jJComponent", "MDS_Component");
        illegalClassNames.put("JDesktopPane", "MDS_DesktopPane");
        illegalClassNames.put("JDialog", "MDS_Dialog");
        illegalClassNames.put("JEditorPane", "MDS_EditorPane");
        illegalClassNames.put("JFileChooser", "MDS_FileChooser");
        illegalClassNames.put("JFormattedTextField", "MDS_FormattedTextField");   
        illegalClassNames.put("JFrame", "MDS_Frame");
        illegalClassNames.put("JInternalFrame", "MDS_Frame");
        //illegalClassNames.put("JLabel", "MDS_Label");
        //illegalClassNames.put("JLayeredPane", "MDS_LayeredPane");
        illegalClassNames.put("JList", "MDS_List");
        //illegalClassNames.put("JMenu", "MDS_Menu");
        illegalClassNames.put("JOptionPane", "MDS_OptionPane");
        illegalClassNames.put("JPanel", "MDS_Panel");
        illegalClassNames.put("JPasswordField", "MDS_PasswordField");
        illegalClassNames.put("JPopupMenu", "MDS_PopupMenu");
        illegalClassNames.put("JProgressBar", "MDS_ProgressBar");
        illegalClassNames.put("JRadioButton", "MDS_RadioButton");
        illegalClassNames.put("JScrollBar", "MDS_ScrollBar"); 
        //illegalClassNames.put("JScrollPane", "MDS_ScrollPane");  
        illegalClassNames.put("JSlider", "MDS_Slider");   
        illegalClassNames.put("JSpinner", "MDS_Spinner");
        illegalClassNames.put("JTabbedPane", "MDS_TabbedPane");
        illegalClassNames.put("JTable", "MDS_Table");
        illegalClassNames.put("JTextArea", "MDS_TextArea");
        illegalClassNames.put("JTextField", "MDS_TextField");
        illegalClassNames.put("JTextPane", "MDS_TextPane");
        illegalClassNames.put("JToggleButton", "MDS_ToggleButton");
        illegalClassNames.put("JToolBar", "MDS_ToolBar");
        illegalClassNames.put("JTree", "MDS_Tree");
        illegalClassNames.put("JWindow", "MDS_Window");
        //illegalClassNames.put("JWindow", "MDS_Window");
        
        //Other Java Classes
        illegalClassNames.put("ClassLoader", "MDS_ClassLoader");
        illegalClassNames.put("Thread", "MDS_Thread");
        illegalClassNames.put("ThreadGroup", "MDS_ThreadGroup");
        illegalClassNames.put("Toolkit", "MDS_System");
        illegalClassNames.put("Runtime", "MDS_System");
        illegalClassNames.put("System", "MDS_System & Console");        
        
        */   
    }    
  


    public MDS_ClassLoader() {}
    
    
    
    public static Hashtable getIllegalAndRecommendedClassList() {
        Hashtable ht = new Hashtable();
        Enumeration enrKeys = illegalClasses.keys();
        
        while(enrKeys.hasMoreElements()) {
            Object key = enrKeys.nextElement();
            ht.put(key, illegalClasses.get(key));
        }
        return ht;

    }
    
    
    
    public static Hashtable getIllegalAndRecommendedClassNameList() {
        Hashtable ht = new Hashtable();
        Enumeration enrKeys = illegalClassNames.keys();
        
        while(enrKeys.hasMoreElements()) {
            Object key = enrKeys.nextElement();
            ht.put(key, illegalClassNames.get(key));
        }
        return ht;

    }    
    
    
    
    public final Class loadClass(File cf, boolean resolve) throws ClassNotFoundException {

        if(!cf.isDirectory()) {
            classFile = cf;  
            className = fm.getFileName_WithoutExtention(cf.getName());
            String path = cf.getParent();
            if(!path.endsWith("\\")) path = path.concat("\\");
            try {      
                urlClassPath = new URL("file:"+path);  
            } catch (Exception ex) {}
            
            cld = new UrlClassLoader(new URL[] {urlClassPath});
            Class c1 = cld.loadClass(className);   
            if(c1 == null) throw new ClassNotFoundException(cf.getPath());          
            return c1;                          
        }

        return null;
    
    }
    
    
    
    public final Class loadClass(File cf) throws ClassNotFoundException {
        return loadClass(cf, false);
    }    
    
    
    
    public final boolean isExecutable(File cf) {
        checkForIllegalClasses = false;
        
        boolean b = true;
        
        try{
            String ar[] = new String[] {};
            Class c1 = this.loadClass(cf);
            Method m = c1.getMethod("MDS_Main", new Class[] { ar.getClass() });
        } catch(Exception ex) {
            if(ex instanceof ClassNotFoundException) {
                b = false;
            } else if(ex instanceof NoSuchMethodException) {
                b = false;
            }
        } catch(NoClassDefFoundError er) {
            b = false; 
        } finally {
            checkForIllegalClasses = true;
        }
        
        return b;
                 
    }
    
    
    
    
    
    private class UrlClassLoader  extends URLClassLoader {
    
    
    
        private Map classes = new HashMap();
    
    
    
        public UrlClassLoader(URL[] urls) {
            super(urls);
        }
        
        
        

        protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {  
/*
     // check if class already loaded
      Class cl = (Class)classes.get(name);

      if (cl == null) // new class
      {  try
         {  // check if system class
            Console.println("1 "+name);
            return findSystemClass(name);
         }
         catch (ClassNotFoundException e) {}
         catch (NoClassDefFoundError e) {}

         // load class bytes--details depend on class loader

         byte[] classBytes = loadClassBytes(name);
         if (classBytes == null)
            throw new ClassNotFoundException(name);

         cl = defineClass(name, classBytes,
            0, classBytes.length);
         if (cl == null)
            throw new ClassNotFoundException(name);

         classes.put(name, cl); // remember class
      }

      if (resolve) resolveClass(cl);
      
      

      return cl;
*/


			//System.out.println("CLS :  "+name);

            Class cl = (Class)classes.get(name);
            Class sysCls = null;
            
            
            if (cl == null) { 
                try {  
                    sysCls = findSystemClass(name);   
                    //Console.println(sysCls.getName());        
                    if(checkForIllegalClasses) checkIllegalClass(sysCls.getName());
                    return sysCls;
                }
                catch (ClassNotFoundException e) {}
                catch (NoClassDefFoundError e) {}
                catch (IllegalClassException e) {  
                    showIllegalClassInfo(sysCls, e);
                    return null;
                }

                byte[] classBytes = loadClassBytes(name);

                if (classBytes == null)
                throw new ClassNotFoundException(name);

                cl = defineClass(name, classBytes, 0, classBytes.length);
                if (cl == null)
                    throw new ClassNotFoundException(name);

                classes.put(name, cl);
            }

            if (resolve) resolveClass(cl);

            return cl;
            
       }
       
       
       

        private byte[] loadClassBytes(String cname) {  
        	//Console.println(cname);
            FileInputStream in = null;
            try {
                in = new FileInputStream(new File(urlClassPath.getFile(), cname+".class"));
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    byte b = (byte)(ch);
                    buffer.write(b);
                }
                in.close();
                return buffer.toByteArray();
            }
            catch (IOException e) {  
                if (in != null){
                    try { in.close(); } catch (IOException e2) { }
                }  
                return null;
            }
        }
        
        
        
        private void checkIllegalClass(String name) throws IllegalClassException {
            if(illegalClasses.containsKey(name)) {
                throw new IllegalClassException(name);
            }    
        }
        
        
        
        private void showIllegalClassInfo(Class clazz, Exception e) {

            class IllegalClassInfo extends MDS_Frame implements ActionListener {
            
            
            
                JComponent contentPane;
                MDS_Button btnOk = new MDS_Button("Ok");
            
            
            
                public IllegalClassInfo(Class cls , Exception ex) {
                    super();
                    putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
                    showInTaskBar(false); 
                    setSize(440, 340);
                    setCenterScreen();
                    MDS.getBaseWindow().getDesktop().add(this ,JLayeredPane.DRAG_LAYER);
                    contentPane = (JComponent)this.getContentPane();
                    contentPane.setLayout(new BorderLayout());
                    MDS_Label lblTitle = new MDS_Label("Class Loader [Illegal Class]");
                    //lblTitle.setHorizontalTextPosition(SwingConstants.CENTER);
                    lblTitle.setFont(new Font(lblTitle.getFont().getName(), lblTitle.getFont().getStyle(), 20));
                    MDS_Panel pnlTitle = new MDS_Panel(new FlowLayout(FlowLayout.CENTER));                    
                    pnlTitle.add(lblTitle);
                    contentPane.add(pnlTitle, BorderLayout.NORTH);
                    MDS_Label lblIcon = new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"stop1.png"));
                    contentPane.add(lblIcon, BorderLayout.WEST);
                    MDS_TextArea txtaMessage = new MDS_TextArea();
                    txtaMessage.setBackground(UIManager.getColor("Label.background"));
                    txtaMessage.setEditable(false);   
                    
                    String lineNo = "N/A";
                    String methodName = "N/A";
                    
                    int x = 0;
                    StackTraceElement[] stElement = ex.getStackTrace();
                    
                    while(x < stElement.length) {
                        if(stElement[x].getClassName().equals(className)) {
                            lineNo = String.valueOf(stElement[x].getLineNumber());
                            methodName = stElement[x].getMethodName();
                        }
                        x++;    
                    }
                    
                    String message = "Class named "+className+" tried to load a class ("+cls.getName()+")\nwhich is "+
                                     "not supported by the MDS. SO IT IS HIGHLY RECOMMENDED TO\n"+ 
                                     "USE ("+illegalClasses.get(cls.getName())+") INSTEAD OF ("+cls.getName()+").\n"+
                                     "OTHERVISE YOUR APPLICATION WILL BE STOPPED EVERYTIME\n"+
                                     "IT TRIES TO LOAD A ILLEGAL CLASS.\n\nIllegal Class Details\n======================================================\n"+
                                     "Main Class: "+className+"\n"+
                                     "Path:"+classFile.getPath()+"\n"+
                                     "Method Name:"+methodName+"\n"+
                                     "Line No: "+lineNo+"\n"+
                                     "Illegal Class: "+cls.getName()+"\n"+
                                     "Recommended Class: "+illegalClasses.get(cls.getName());
                                     //ex.printStackTrace();
                    txtaMessage.setText(message);
                    contentPane.add(new JScrollPane(txtaMessage), BorderLayout.CENTER);
                    contentPane.add(new MDS_Label("   "), BorderLayout.EAST);
                    MDS_Panel pnlButtons = new MDS_Panel();
                    pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
                    btnOk.addActionListener(this);
                    pnlButtons.add(btnOk);
                    contentPane.add(pnlButtons, BorderLayout.SOUTH);
                    setVisible(true);     
                }
                
                
                
                public void actionPerformed(ActionEvent e) {
                    this.dispose();
                }
                
                
                
            }        
            
            new IllegalClassInfo(clazz, e);
            
        } 
        
        
    }    
    
    
    
}    