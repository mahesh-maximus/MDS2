/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import java.lang.reflect.*;
import java.util.*;
import java.util.jar.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;
import java.net.*;
import java.util.jar.*;



public final class ProcessManager {



    private static ProcessManager prm = new ProcessManager();
    ShowLoadingProcess slprc = null;
    private StringTable st = new StringTable();
    
    
    
    private ProcessManager() {
        slprc = new ShowLoadingProcess();    
    }
    
    
    
    public static ProcessManager getProcessManager() {
        return prm;
    }
    
    
    
    public void instantiate(Constructor cont, Object[] initargs) {
    
    
        class Instantiate extends Thread {
        
           
        
            Constructor co;
            Object[] ar;
            
            
            public Instantiate(Constructor c, Object[] a) {
                super("Instantiate");
                co = c;
                ar = a;
                this.start();
            }
            
            
            
            public void run() {
            
                Random rand = new Random(); 
                long id = rand.nextLong();            
            
                try{
                    slprc.startShowing(id);
                    co.newInstance(ar);  
                    slprc.endShowing(id);
                } catch(Exception ex) {
                    if(ex instanceof ClassNotFoundException) {
                        slprc.endShowing(id);
                        throw new RuntimeException("There is no class called '"+co.getName()+"' in class path.");
                        //MDS_OptionPane.showMessageDialog("There is no class called '"+co.getName()+"' in class path.", "Class Instantiater", JOptionPane.ERROR_MESSAGE);                  
                    } else {
                        slprc.endShowing(id);
                        throw new RuntimeException(ex);
                        //MDS.getExceptionManager().showException(ex);
                    }         
                } 
            }
        }     
        
        new Instantiate(cont, initargs); 
                 
    }
    
    
    
    public void execute(File file) {
        String argv[] = new String[] {};
        execute(file, argv);    
    }
    
    
  
    public void execute(File file, MDS_Console console) {
    	String argv[] = new String[] {};
    	execute(file, argv, console);
    }    
    
    
    
    public void execute(File file, String ar[]) {
    	execute(file, ar, null);
    }
	
    
    
    
    public void execute(File file, String ar[], MDS_Console console) {
    

        class Execute extends Thread {
        
           
        
            File f;
            String argv[];
            MDS_Console console;
            
            
            
            public Execute(File f1, String arg[], MDS_Console console) {
                super("Execute");
                f= f1;
                argv = arg;
                this.console = console;
                this.start();
            }
            
            
            
            public void run() {
                
                Random rand = new Random(); 
                long id = rand.nextLong();
            
                try{
                    slprc.startShowing(id);
                    MDS_ClassLoader ld = new MDS_ClassLoader();
                    Class c1 = ld.loadClass(f, false);
                    Method m = c1.getMethod("MDS_Main", new Class[] { argv.getClass() });
                    Object s= m.invoke(null, new Object[] { argv }); 
                } catch(Exception ex) {
                    if(ex instanceof ClassNotFoundException) {
                        slprc.endShowing(id);
                        if(console == null)
                        	throw new RuntimeException("There is no class called '"+f.getName()+"' in class path.");
//                        	MDS_OptionPane.showMessageDialog(st.get(12)+"\n"+f.getName(), "Class Loader", JOptionPane.ERROR_MESSAGE); 
                    	else 
                    		console.printError(ex.toString());
                    		console.displayPrompt();
                    } else if(ex instanceof NoSuchMethodException) {
                        slprc.endShowing(id);
                        if(console == null)
                        	throw new RuntimeException("'"+f.getPath()+"' is not a valid MDS application.");
//                        	MDS_OptionPane.showMessageDialog("'"+f.getPath()+"' is not a valid MDS application.", "Class Loader", JOptionPane.ERROR_MESSAGE);                    
                    	else 
                    		console.printError(ex.toString());   
                    		console.displayPrompt();	                 
                    } else {                       
                        slprc.endShowing(id);
                        if(console == null) {
	                       	System.out.println(ex.toString());
	                       	throw new RuntimeException(ex);
	                        //MDS.getExceptionManager().showException(ex);
                         } else 
                    		console.printError(ex.toString()); 
                    		console.displayPrompt();	   
                    }         
                } finally {
                    slprc.endShowing(id);
                } 
            }
			
			
        }       
        
        new Execute(file, ar, console);

    }
    
    
    
    public void executeJar(File file) {
        String argv[] = new String[] {};
        executeJar(file, argv);    
    }
    
    
    
    public void executeJar(File jarFile, String args[]) {
    	
    	class ExecuteJar extends Thread {
    		
    		File jf;
    		String[] ar;
    		
    		public ExecuteJar(File jf, String ar[]) {
    			super("Execute Jar");
    			this.jf = jf;
    			this. ar = ar;
    			this.start();	
    		}
    		
    		
    		
    		public void run() {
    			Random rand = new Random(); 
                long id = rand.nextLong();
    			try {
                    slprc.startShowing(id);                	
	    			System.out.println("Execute JAR ... ");
	    			JarFile jarFile = new JarFile(jf);
	    			Manifest mf = jarFile.getManifest();
	    			System.out.println("SIZE : "+mf.getMainAttributes().size());
	    			String mainClassName = mf.getMainAttributes().getValue("Main-Class");
	    			System.out.println("Manifest Main Class : "+mainClassName);
	    			MDS_JarClassLoader jcls = new MDS_JarClassLoader(jf);
	    			
	    			Class cls = jcls.loadClass(mainClassName, false);
                    Method m = cls.getMethod("MDS_Main", new Class[] { ar.getClass() });
                    Object s= m.invoke(null, new Object[] { ar }); 
	    			
    			} catch(Exception ex) {
                    if(ex instanceof ClassNotFoundException) {
                        slprc.endShowing(id);
                        throw new RuntimeException("There is no class called '"+jf.getName()+"' in class path.");
//                        MDS_OptionPane.showMessageDialog(st.get(12)+"\n"+jf.getName(), "JAR Class Loader", JOptionPane.ERROR_MESSAGE); 
                    } else if(ex instanceof NoSuchMethodException) {
                        slprc.endShowing(id);
                        throw new RuntimeException("'"+jf.getPath()+"' is not a valid MDS Jar File.");
//                        MDS_OptionPane.showMessageDialog("'"+jf.getPath()+"' is not a valid MDS Jar File.", "JAR Class Loader", JOptionPane.ERROR_MESSAGE);                    
                    } else {                       
                        slprc.endShowing(id);
                       	System.out.println(ex.toString());
                       	throw new RuntimeException(ex);
                        //MDS.getExceptionManager().showException(ex);
                    }         
                } finally {
                    slprc.endShowing(id);
                } 
    			
    		}
    		
    	}
    	
    	new ExecuteJar(jarFile, args);
    	
    }
    
    
    
    public boolean isMDS_Executable(File f) {
    	boolean b = false;	
        try {
            MDS_ClassLoader ld = new MDS_ClassLoader();    
           	MDS_JarClassLoader jld = new MDS_JarClassLoader();
            if(f.getPath().endsWith(".class")) b = ld.isExecutable(f);
            else if(f.getPath().endsWith(".jar")) b = jld.isExecutable(f);	
        } catch(ClassFormatError ex) {
            b = false;
        }
        
        System.out.println("isMDS_Executable : "+new MDS_JarClassLoader().isExecutable(f));
        
        return b;
            
    }    
    
    
    
    
    public void showRunCommandDialog() {
    
    
        
        class RunCommand extends MDS_Frame implements ActionListener {
        
        
        
            JComponent contentPane;
            String idText = "Enter the name of the application you want to run \n\n or the URL you want to view";
            JLabel lblIcon = new JLabel(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-exec.png"));
            JLabel lblText1 = new JLabel("Enter the name of the application you want to run");
            JLabel lblText2 = new JLabel("or the URL you want to view.");
            JLabel lblText3 = new JLabel("Command");
            MDS_TextField txfdCommand = new MDS_TextField();
            
            MDS_Button btnRun = new MDS_Button("Run");
            MDS_Button btnCancel = new MDS_Button("Cancel");
            
            MDS_Frame me;
            
            
            public RunCommand() {
                //super("Run Command", false ,true);
                super("Run Command",false, true, false, false, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-exec.png"));
                contentPane = (JComponent) this.getContentPane();
                contentPane.setLayout(null);
                lblIcon.setBounds(-20, -20, 100, 100);
                contentPane.add(lblIcon);
                
                lblText1.setBounds(50, 20, 300, 25);
                contentPane.add(lblText1);
                
                lblText2.setBounds(50, 40, 300, 25);
                contentPane.add(lblText2);
                
                lblText3.setBounds(20, 80, 70, 25);
                contentPane.add(lblText3);  
                
                me = this;
                txfdCommand.setBounds(85, 80, 240, 25);
                txfdCommand.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == e.VK_ENTER) {
                            execute();
                            //execute(txfdCommand.getText());
                            //me.dispose();
                        }
                    }
                });                
                contentPane.add(txfdCommand); 
                
                btnCancel.setBounds(240, 120, 80, 30);
                btnCancel.addActionListener(this);
                contentPane.add(btnCancel);
                
                btnRun.setBounds(150, 120, 70, 30);
                btnRun.addActionListener(this);
                contentPane.add(btnRun);
                
                this.setBounds(8, 500 ,350, 200);
                this.setVisible(true);
            }
            
            
            
            private void execute() {
                Random rand = new Random(); 
                long id = rand.nextLong();
                String text = txfdCommand.getText();
                File f = null;
                String argv[] = new String[] {};
                if(!text.endsWith(".class")) text = text.concat(".class");
                if(text.indexOf(":") < 0) f = new File(MDS.getBinaryPath(), text); 
                else f = new File(text);
                try{
                    slprc.startShowing(id);
                    MDS_ClassLoader ld = new MDS_ClassLoader();
                    Class c1 = ld.loadClass(f, false);
                    Method m = c1.getMethod("MDS_Main", new Class[] { argv.getClass() });
                    Object s= m.invoke(null, new Object[] { argv }); 
                    me.dispose();
                } catch(Exception ex) {
                    if(ex instanceof ClassNotFoundException) {
                        slprc.endShowing(id);
                        MDS_OptionPane.showMessageDialog(me, st.get(12)+"\n"+f.getPath(), "Class Loader", JOptionPane.ERROR_MESSAGE); 
                    } else if(ex instanceof NoSuchMethodException) {
                        slprc.endShowing(id);
                        MDS_OptionPane.showMessageDialog(me, "'"+f.getPath()+"' is not a valid MDS application.", "Class Loader", JOptionPane.ERROR_MESSAGE);                    
                    } else {                       
                        slprc.endShowing(id);
                        throw new RuntimeException(ex);
                        //MDS.getExceptionManager().showException(ex);
                    }
                    txfdCommand.requestFocus();
                    txfdCommand.selectAll();         
                } finally {
                    slprc.endShowing(id);
                }             
            }
            
            
            
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Run")) {
                    if(!txfdCommand.getText().equals("")) execute(); 
                    //execute(txfdCommand.getText());
                    //this.dispose();
                } else if(e.getActionCommand().equals("Cancel")) {
                    this.dispose();
                }
            }
            
            
            
        }  
        
        new RunCommand();  
    }
    
    
    
    
    
    class ShowLoadingProcess implements SystemSchedulerThreadListener {
    
    
        
//        int x = 824;
//        int y = 668;
//        int w = 200;
//        int h = 70;     
        	
        int x = 800;
        int y = 668;
        int w = 200;
        int h = 70;
        	
        Vector vctLoadingProcessList = new Vector();
        MDS_Window window = null;
        
    
    
        public ShowLoadingProcess() {/*
            window = new MDS_Window(JLayeredPane.POPUP_LAYER);
            window.setLocation(x,y);
            window.setSize(w, h);
            window.setLayout(new BorderLayout());

            window.add(new LdnIcon(), BorderLayout.CENTER);
            window.setOpaque(false);            
            
            window.setVisible(false);
            
            VirtualThreading.create_SST_VT(this, "ShowLoadingProcess");  */  
        }
		
		
		
		private void initialize() {
            window = new MDS_Window(JLayeredPane.POPUP_LAYER);
            window.setLocation(x,y);
            window.setSize(w, h);
            window.setLayout(new BorderLayout());

            window.add(new LdnIcon(), BorderLayout.CENTER);
            window.setOpaque(false);            
            
            window.setVisible(false);
            
            VirtualThreading.create_SST_VT(this, "ShowLoadingProcess");  		
		}
        
        
        
        public void startShowing(long id) {
			if(MDS.isDesktopLoaded() && window == null) initialize();
            vctLoadingProcessList.addElement(new Long(id));
        }
        
        
        
        public void endShowing(long id) {
            vctLoadingProcessList.removeElement(new Long(id));
        }
        
        
        
        public long getSystemScheduler_EventInterval() {
            return 100;
        }



        public void systemSchedulerEvent() {
            if(vctLoadingProcessList.size() != 0) {
                if(!window.isVisible()) {
                    window.setVisible(true);  
                    //MDS.getBaseWindow().getDesktop().paintImmediately(x,y,w,h); 
                }
            } else {
                if(window.isVisible()) {
                    window.setVisible(false);
                    //MDS.getBaseWindow().getDesktop().paintImmediately(x,y,w,h);
                }
            }
        }
        
        
        
        
        class LdnIcon extends MDS_Panel {
        
        
        
            public LdnIcon() {
                this.setOpaque(false);
            }
            
            
            
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                g2d.drawImage(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH + "48-filesys-exec.png"), 0, 0, null);
                
                g2d.setFont(new Font("Dialog", Font.BOLD, 30));
                FontRenderContext frc = g2d.getFontRenderContext();
                TextLayout tl = new TextLayout("Loading ...", g2d.getFont(), frc);
                Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(50, 40));//40, 40
                g2d.setColor(Color.black);
                g2d.draw(sha);
                g2d.setColor(Color.red);
                g2d.fill(sha);                
            }            
            
            
        }            
        
        
        
    }    
    
    
 
      
        
}    
    
    