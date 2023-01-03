/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.border.*;
import java.io.*;
import java.awt.image.*;
import java.util.logging.*;
import java.text.*;


public final class MDS {


    
    public static long fexMonitorID; 
    public static final String DEBUG = "-debug";
    private static boolean debugMode = false;
    private static boolean desktopLoaded = false;
    private static boolean mdsClassLoaded = false;
    
    private static BaseWindow bw;
    
    private static Messenger msgr;
    private static BitExchanger btex;
    private static DesktopSplashSreen dss;
	
	
	
	static MDS_Panel pnlConsole = new MDS_Panel(new BorderLayout());
//	static MDS_Console stCon;

	static SplashScreen splashScreen  = SplashScreen.getSplashScreen();  
	static Graphics2D splashScreenGraphics = splashScreen.createGraphics();
	   	
	
	public static final String CONSOLE = "CONSOLE";
	public static final String GUI = "DESKTOP";
	static String insterfaseType = "";
    
    private static Logger log = Logger.getLogger("MDS");
    
    
    private MDS() {}
	
	
	
	private static void initializeConsole() {
//		stCon = new MDS_Console(false, false, false, MDS_Console.DISPLAY_MODE_FULL_SCREEN);
//		stCon.setDisplayOnly(true);
//		stCon.setPromptVisible(false);
//		stCon.setBgColor(Color.black);
//		stCon.setOutputColor(Color.white);
//		stCon.setSelectionColor(Color.black);
//		stCon.setConsoleFont(new Font("Courier", Font.BOLD, 18));
//
//		pnlConsole.add(stCon, BorderLayout.CENTER);	
//		//MDS.getBaseWindow().setFullScreenWindow(pnlConsole);
//		
//		stCon._grabFocus();
//		
//		try {
//		stCon.print("Press F8 for Console mode, Press Enter for GUI mode or else wait till 10 seconds MDS will automatically load GUI mode. ");
//		
//			for(int x = 1; x<21; x++) {
//				stCon._grabFocus();
//				stCon.print("["+String.valueOf(x)+"]/r", Color.green);
//				Thread.sleep(1000);
//					if(insterfaseType == CONSOLE || insterfaseType == GUI) {
//					    stCon.println("\n");
//						stCon.println("Loading .....");
//						stCon.println("");
//						break;
//					}
//			}		
//
//		} catch(InterruptedException ex) {}
	}
    

    
    public static void initialise_MDS(boolean dbm) {
 
        if(mdsClassLoaded) return;
        else mdsClassLoaded = true;
        
        if(dbm) debugMode = true;
    		                        
    	loadMDS_Classes();		

    }
    
    
    public static String getMDS_Name() {
    	return "MDS 2.0";
    }
    
    
    public static boolean isHalfLifeServerStarted() {
    	return true;
    }
	
	
	
	private static void loadMDS_Classes() {
	
	
		try {	
			
			log.info("initiate VirtualThreading");	        
			Class.forName("VirtualThreading");	          
						
			initializeConsole();
							
			printSplashScreen("Loading ProcessManager");	
			getProcessManager(); 
			printSplashScreen("Loading DiskDriveManager");
			getDiskDriveManager();		
			printSplashScreen("Loading FileManager");
			getFileManager();
			printSplashScreen("Loading Sound");		
			getSound();
			printSplashScreen("Loading Printer");	
			getPrinter();				
					
			printSplashScreen("Loading Graphics");
			getMDS_Graphics();
			printSplashScreen("Loading VolatileImageLibrary");
			getMDS_VolatileImageLibrary();
			printSplashScreen("Loading Clipboard");
			getClipboard();
			printSplashScreen("Loading User");
			getUser();
			printSplashScreen("Loading RedirectedStandardOutput");
					    
			getRedirectedStandardOutput();
			//				    Automatic_RedirectedStandardOutputViewer_Launcher.getAutomatic_RedirectedStandardOutputViewer_Launcher(); 
						
			printSplashScreen("Loading SystemTimeEventManager");
			getSystemTimeEventManager();
			printSplashScreen("Loading FileSystemEventManager");
			getFileSystemEventManager();
				
			printSplashScreen("ScreenSaverEventManager");
										
			bw = BaseWindow.getBaseWindow();
						
			getMDS_UIManager();
			getScreenSaverEventManager(); 
			
			log.info("initiateDesktop");	       
			bw.initiateDesktop(); 
			 
				 	
			log.info("loadDesktop");                      
			bw.loadDesktop();
			
			log.info("Validate Desktop components");            
			bw.validate();
			            
			desktopLoaded = true;
						
			
			getMDS_ExceptionManager();
						
			bw.getDesktop().createPrimaryItems();
			            
			dss = new DesktopSplashSreen();
			dss.loadingDesktopShortcuts();
			bw.getDesktop().loadDesktopShortcuts();
			bw.getDesktop().repaint();
			            
			dss.loadingTaskBar();
			getProcessManager().execute(new File(MDS.getBinaryPath(), "MaheshTaskBar.class"));
			//getProcessManager().execute(new File(MDS.getBinaryPath(), "TaskBar.class"));
			getProcessManager().execute(new File(MDS.getBinaryPath(), "QuickLauncher.class"));
			            
			dss.loadingMessenger();
			msgr = new Messenger();  
			            
			dss.loadingBitExchanger();
			btex = new BitExchanger(); 
			            
			log.info("Load VirtualThreadingDebugger");            
			dss.loadingVirtualThreadingDebugger();
			log.info("Refresh VirtualThreadingDebugger");
			VirtualThreadingDebugger.getCurrent_VTDBG().refresh(); 
			            
			dss.close();  
					
			bw.repaint();
						
			Automatic_RedirectedStandardOutputViewer_Launcher.getAutomatic_RedirectedStandardOutputViewer_Launcher(); 
						        
			} catch(Exception ex) {
				if(dss != null) {
			    	dss.close();
			    }
			 	ex.printStackTrace();    
			}	
			
	}
	
	
//////////	public static void loadDesktop() {
//////////		if(!isDesktopLoaded()) {
//////////			loadMDS_Classes();			
//////////		}
//////////	}


	public static void addNativeConsoleHandler() {

		
		class NativeConsoleHandler extends ConsoleHandler {
			
			
			OperatingSystem os = OperatingSystem.getOperatingSystem();
			
			
			
			public NativeConsoleHandler() {
				super();
////				this.setFormatter(new NativeConsoleFormatter());
			}
			
			public void close() {
			}
			
			
			
			public void flush() {}
			
			
			
			public synchronized void publish(LogRecord record) {
				if (!isLoggable(record)) {
				    return;
				}
				String msg;
				try {
			 	    msg = getFormatter().format(record);
				} catch (Exception ex) {
				    reportError(null, ex, ErrorManager.FORMAT_FAILURE);
				    return;
				}
				try {
				    os.printConsole(msg, 3);
				} catch (Exception ex) {
				    reportError(null, ex, ErrorManager.WRITE_FAILURE);
				}
			
			}
			
			
			
			class NativeConsoleFormatter extends java.util.logging.Formatter {
			
    			Date dat = new Date();
   				private final static String format = "{0,date} {0,time}";
   				private MessageFormat formatter;			
			    private Object args[] = new Object[1];
							
				private String lineSeparator = "\n";

				
				public synchronized String format(LogRecord record) {
					StringBuffer sb = new StringBuffer();
					// Minimize memory allocations here.
					dat.setTime(record.getMillis());
					args[0] = dat;
					StringBuffer text = new StringBuffer();
					if (formatter == null) {
					    formatter = new MessageFormat(format);
					}
					formatter.format(args, text, null);
					sb.append(text);
					sb.append(" ");
					if (record.getSourceClassName() != null) {	
					    sb.append(record.getSourceClassName());
					} else {
					    sb.append(record.getLoggerName());
					}
					if (record.getSourceMethodName() != null) {	
					    sb.append(" ");
					    sb.append(record.getSourceMethodName());
					}
					sb.append(lineSeparator);
					String message = formatMessage(record);
					sb.append(record.getLevel().getLocalizedName());
					sb.append(": ");
					sb.append(message);
					sb.append(lineSeparator);
					if (record.getThrown() != null) {
					    try {
					        StringWriter sw = new StringWriter();
					        PrintWriter pw = new PrintWriter(sw);
					        record.getThrown().printStackTrace(pw);
					        pw.close();
							sb.append(sw.toString());
					    } catch (Exception ex) {
					    }
					}
					return sb.toString();
				}
			}
		
			
		}
		
		
	
			log.setUseParentHandlers(false);
			log.addHandler(new NativeConsoleHandler());
			
			log.info("System logging started.");
			

			
		
//	        MDS.registerNatives();
//	        
//			OperatingSystem.getOperatingSystem().allocateConsole();	
//			
//			while(true) {
//				try {
//					Thread.sleep(1000);
//				}catch(Exception ex) {}
//			}		

	}
	
	
	
	public static void setInterface(String cmd) {
		insterfaseType = cmd;
	}
    
    
    
    public static boolean isDebugMode() {
        return debugMode;
    }
    
    
    
    private static void kill_MDS_Launcher() {
        try {
            Process prc = Runtime.getRuntime().exec("bin\\native\\Kmdsl.exe");
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occourred while trying to execute 'Kmdsl.exe'", "MDS", JOptionPane.ERROR_MESSAGE);                    
        }    
    }
    
    
    
    public static void initializeWin32_Classes() {
        //wcls = new Win32_Classes();
    }
    
    
    
    public static void registerNatives() {
    
        if(mdsClassLoaded) return;
        
        try {
            System.loadLibrary("mds");
//            if(Console.getValidationCode() != 279341) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'Console.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }
        } catch(UnsatisfiedLinkError er) {
            File f = new File("mds.dll");
            kill_MDS_Launcher();
            if(f.exists()) {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'Console.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Native library not found 'Console.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
                
//        try {
//            System.loadLibrary("Mouse");
//            if(Mouse.getValidationCode() != 345359) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'Mouse.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }            
//        } catch(UnsatisfiedLinkError ex) {
//            File f = new File("Mouse.dll");
//            kill_MDS_Launcher();
//            if(f.exists()) {
//                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'Mouse.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Native library not found 'Mouse.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            }
//            System.exit(0);
//        } 
        
//        try {
//            System.loadLibrary("DiskDrives");
//            if(DiskDrives.getValidationCode() != 632176) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'DiskDrives.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }             
//        } catch(UnsatisfiedLinkError ex) {
//            File f = new File("DiskDrives.dll");
//            kill_MDS_Launcher();
//            if(f.exists()) {
//                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'DiskDrives.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Native library not found 'DiskDrives.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            }
//            System.exit(0);
//        }  
        
        
        
//        try {
//            System.loadLibrary("WinUI");
//            if(WinUI.getValidationCode() != 948903) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'WinUI.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }             
//        } catch(UnsatisfiedLinkError ex) {
//            File f = new File("WinUI.dll");
//            kill_MDS_Launcher();
//            if(f.exists()) {
//                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'WinUI.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Native library not found 'WinUI.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            }
//            System.exit(0);
//        } 
        
        
//        try {
//            System.loadLibrary("WinCommon");
//            if(WinCommon.getValidationCode() != 901321) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'WinCommon.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }             
//        } catch(UnsatisfiedLinkError ex) {
//            File f = new File("WinCommon.dll");
//            kill_MDS_Launcher();
//            if(f.exists()) {
//                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'WinUI.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Native library not found 'WinCommon.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            }
//            System.exit(0);
//        } 
        
        
//        try {
//            System.loadLibrary("Nmds");
//            if(MDS.getValidationCode() != 194307) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'Nmds.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }             
//        } catch(UnsatisfiedLinkError ex) {
//            File f = new File("Nmds.dll");
//            kill_MDS_Launcher();
//            if(f.exists()) {
//                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'WinUI.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Native library not found 'Nmds.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            }
//            System.exit(0);
//        } 
//		
//		
//        try {
//            System.loadLibrary("WinProcess");
//            if(WinProcess.getValidationCode() != 409932) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'WinProcess.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }             
//        } catch(UnsatisfiedLinkError ex) {
//            File f = new File("WinProcess.dll");
//            kill_MDS_Launcher();
//            if(f.exists()) {
//                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'WinProcess.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Native library not found 'WinProcess.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            }
//            System.exit(0);
//        } 	
//		
//		
//        try {
//            System.loadLibrary("WinConsole");
//            if(WinProcess.getValidationCode() != 409932) {
//                JOptionPane.showMessageDialog(null, "Invalid native library. 'WinConsole.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//                System.exit(0);
//            }             
//        } catch(UnsatisfiedLinkError ex) {
//            File f = new File("WinConsole.dll");
//            kill_MDS_Launcher();
//            if(f.exists()) {
//                JOptionPane.showMessageDialog(null, "An error occurred while trying to load 'WinConsole.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Native library not found 'WinConsole.dll'.", "MDS", JOptionPane.ERROR_MESSAGE);
//            }
//            System.exit(0);
//        } 			                
                         
       
    }
    
    
    
    private static void parintAbout() {
//        stCon.println("                                                                               ");
//        stCon.println("                                                                                    ** WELCOME TO MAHESH DESKTOP SYSTEM **");
//        stCon.println("                   "); 
//        stCon.println("                                                                                                            **  MDS 1.0 **");               
//        stCon.println("                                                                               ");
//        stCon.println("Designed and Developed by: Mahesh Dharamasena.                               ");
//        stCon.println("For Technical Support call: 081-2499725.                                     ");
//        stCon.println("E-mail   : mahesh_ksl@yahoo.com");
//        stCon.println("         : mahesh_ksl@gmail.com");
//        stCon.println("                                                                               ");      
        
    }
    
    public static void printSplashScreen(String text) {
    	splashScreenGraphics.setColor(Color.WHITE);
    	splashScreenGraphics.fillRect(3, 377, 330, 20);
    	splashScreenGraphics.setColor(Color.BLACK);
    	splashScreenGraphics.setFont(new Font(splashScreenGraphics.getFont().getFontName(), Font.BOLD, splashScreenGraphics.getFont().getSize()));
    	splashScreenGraphics.drawString(text, 5, 390);
    	splashScreen.update();    		
    }     
    
    

    public static void close() { 
        getMDS_PropertiesManager().store();    
        getMDS_PropertiesManager().close();
		/*
        if(Mp3Player.wmp != null) {
            Mp3Player.wmp.destroy();
        }*/
        System.exit(0);
    }
    
    
    
    public static native boolean isPrevInstance();
    
    
    
    public static native long getValidationCode();
    
    
    
    public static native boolean setHighPriorityClass();
    
    
    
    public static String getLibraryPath() {
        return getCurrentDir() + "lib\\"; 
    }
    
    
    
    public static String getBinaryPath() {
        return getCurrentDir() + "bin\\";   	  
    }
	
	
	
	public static String getJdkPath() {
		//return getCurrentDir()+"java\\jdk1.6.0\\BIN\\";	
		return getJavaHome()+"\\bin\\";
	}
	
	
	
	public static String getJdkToolsPath() {
		return getCurrentDir()+"java\\jdk1.6.0\\LIB\\Tools.jar";
	}
	
	public static String getJavaHome() {
		String s = System.getProperty("java.home");
		if(!s.endsWith("\\")) s = s.concat("\\");  
        return s;  
	}
    
    
    
    public static boolean isDesktopLoaded() {
        return desktopLoaded;    
    }
    
    
    
    public static String getCurrentDir() {
        String s = System.getProperty("user.dir");
        if(!s.endsWith("\\")) s = s.concat("\\");  
        return s;    
    }
    
    
    
    public static BaseWindow getBaseWindow() {
        return bw;
    }
    
    
    
    public static ProcessManager getProcessManager() {
        return ProcessManager.getProcessManager();
    }   
    
    
    
    public  static MDS_UIManager getMDS_UIManager() {
        return MDS_UIManager.getMDS_UIManager();
    }
       
    
    
    public  static  MDS_Graphics getMDS_Graphics() {
        return MDS_Graphics.getMDS_Graphics();
    }
    
    
    
    public static FileManager getFileManager() {
        return FileManager.getFileManager();
    }
    
    
    
    public static DiskDriveManager getDiskDriveManager() {
        return DiskDriveManager.getDiskDriveManager();
    }    
    
    
    
    public static MDS_PropertiesManager getMDS_PropertiesManager() {
        return MDS_PropertiesManager.getMDS_PropertiesManager();
    } 
    
    
    
    public static MDS_Sound getSound() {
        return MDS_Sound.getMDS_Sound();
    }
    
    
    
    public static MDS_VolatileImageLibrary getMDS_VolatileImageLibrary() {
        return MDS_VolatileImageLibrary.getMDS_VolatileImageLibrary();
    }  
    
    
    
    public static MDS_Clipboard getClipboard() {
        return MDS_Clipboard.getMDS_Clipboard();
    }
    
    
    
    public static MDS_User getUser() {
        return MDS_User.getMDS_User();
    }
    
    
    
    public static Messenger getMessenger() {
        return msgr;
    }
    
    
    
    public static BitExchanger getBitExchanger() {
        return btex;
    }
    
    
    
    public static RedirectedStandardOutput getRedirectedStandardOutput() {
        return RedirectedStandardOutput.getRedirectedStandardOutput();
    }
    
    
    
    public static MDS_ExceptionManager getMDS_ExceptionManager() {
       return MDS_ExceptionManager.getMDS_ExceptionManager();
    }
    
    
    
    public static Win32WMI_Classes getWin32WMI_Classes() {
        //initializeWin32_Classes();
        return Win32WMI_Classes.getWin32WMI_Classes();
    }
    
    
    
    public static MDS_Printer getPrinter() {
        return MDS_Printer.getMDS_Printer();
    }
    
    
    
    public static SystemTimeEventManager getSystemTimeEventManager() {
        return SystemTimeEventManager.getSystemTimeEventManager();
    }
    
    
    
    public static FileSystemEventManager getFileSystemEventManager() {
        return FileSystemEventManager.getFileSystemEventManager();
    }
    
    
    
    public static ScreenSaverEventManager getScreenSaverEventManager() {
        return ScreenSaverEventManager.getScreenSaverEventManager();
    } 
    
    
    
    public static String getAbout_Mahesh() {
        return "MDS is Designed and Developed by Mahesh Dharmasena.\nE-Mail : mahesh_ksl@yahoo.com";
    }
    
    
    
    public static void About_MDS() { 
    
    
    
        class IconDetials implements Icon {
        
        
        
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);                
                g2.setFont(new Font("Dialog", Font.BOLD, 17));
                g2.setColor(Color.yellow);                   
                g2.drawString("MDS is Designed and Developed by Mahesh Dharmasena.", 5, 20);
                g2.setFont(new Font("Dialog", Font.BOLD, 12));
                g2.setColor(Color.white);
                g2.drawString("Contact : ",5, 45);
                g2.drawString("4F 'Sampatha' Madawala Road Polgola Kandy Sri Lanka.",5, 60);
                g2.drawString("E mail : mahesh_ksl@yahoo.com",5, 75);
                g2.setFont(new Font("Dialog", Font.BOLD, 17));
                g2.setColor(Color.yellow);
                g2.drawString("MDS",5, 110);
                g2.setFont(new Font("Dialog", Font.PLAIN, 12));
                g2.setColor(Color.white);
                g2.drawString("is a simple graphical desktop environment for Windows Platform. This MDS" ,45, 110);
                g2.drawString("project is an effort to create a complete, free and easy-to-use desktop environment" , 5, 125);
                g2.drawString("for Windows users andpowerful application development framework for MDS" ,5, 140);
                g2.drawString("developers.", 5, 155);
                g2.setFont(new Font("Dialog", Font.PLAIN, 12));
                g2.drawString("/* Imagination(MDS Framework) is more important than the knoladge(Java, C++," ,5, 210);
                g2.drawString("Windows Architecture). */" ,5, 230);
            }
        
        

            public int getIconWidth() {
                return 0;
            }
        
        

            public int getIconHeight() {
                return 0;
            } 
            
            
        }     
        
        
    
        MDS_Frame frm = new MDS_Frame("About MDS [Mahesh Desktop System]",false, true, false, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "MDS.png"));  
        JComponent contentPane = (JComponent) frm.getContentPane(); 
        contentPane.setLayout(new BorderLayout());
        MDS_Panel pnlIcons = new MDS_Panel();
        pnlIcons.setLayout(new FlowLayout());  
        pnlIcons.setBorder(BorderFactory.createEtchedBorder());
        pnlIcons.setBounds(10, 10, 90, 200);
        MDS_Label lblMDS = new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "MDS.png"));
        pnlIcons.add(lblMDS);       
        contentPane.add(BorderLayout.WEST, pnlIcons);
        //MDS_TextArea  txtaDetails = new MDS_TextArea();
        //txtaDetails.setBackground(UIManager.getColor("Label.background"));
        //txtaDetails.setBackground(Color.black);
        //txtaDetails.setForeground(Color.white);
        //txtaDetails.setEditable(false);        
        //JScrollPane scrlp = new JScrollPane(txtaDetails, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);       
        //contentPane.add(BorderLayout.CENTER, scrlp);
        MDS_Label lblDetails = new MDS_Label(new IconDetials());
        //Graphics2D g2d = (Graphics2D)pnlDetails.getGraphics(); 
        lblDetails.setBackground(Color.black);
        lblDetails.setOpaque(true);
        contentPane.add(lblDetails, BorderLayout.CENTER);
        MDS_Panel pnlButtons = new MDS_Panel();
        pnlButtons.setBorder(BorderFactory.createEtchedBorder());
        pnlButtons.setLayout(new FlowLayout());
        MDS_Button btnDev = new MDS_Button("Development");
        btnDev.addActionListener(new MDS_ActionAdapter() {
            public void actionPerformed(ActionEvent e) {
                String[] fName ={"User\\Help\\MDS_Help.htm"};
                getProcessManager().execute(new File(MDS.getBinaryPath(),"WebBrowser"), fName);    
            }
        });    
        pnlButtons.add(btnDev); 
        MDS_Button btnDoc = new MDS_Button("Documentation");
        btnDoc.addActionListener(new MDS_ActionAdapter() {
            public void actionPerformed(ActionEvent e) {
                String[] fName ={"User\\Help\\MDS_Help.htm"};
                getProcessManager().execute(new File(MDS.getBinaryPath(),"WebBrowser"), fName);                
            }
        });    
       
        pnlButtons.add(btnDoc);
        contentPane.add(BorderLayout.SOUTH, pnlButtons);
        frm.setSize(550, 500);
        frm.setCenterScreen();
        frm.setVisible(true);
        
        pnlIcons.repaint();  
        
        MDS.getSound().playSound(new File("Media\\Sound\\yahoo.wav"));
        
        String text = " MDS is Designed and Developed by Mahesh Dharmasena \n E-Mail mahesh_ksl@yahoo.com"+
                      "\n\n MDS is only a framework most of it's sub components are \n unfinished, if any one is interested to"+
                      " finish it please contact me.\n\n"+
                      "** Imagination(MDS Framework) is more important than the knoladge(Java, C). **";
        
        //txtaDetails.setText(text);
        
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);        
        
        //g2d.setFont(new Font("Dialog", Font.BOLD, 12));
        //g2d.setColor(Color.white);        
        //g2d.drawString("MDS is Designed and Developed by Mahesh Dharmasena ", 0, 0);
                   
    }
    
    
    
    public static void checkDisplayMode() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
          
        DisplayMode currentDisplayMode = gs.getDisplayMode();
        if(currentDisplayMode.getWidth() != 1024 || currentDisplayMode.getHeight() != 768) {
            
            JOptionPane.showMessageDialog(null, "Screen resolution should be 1024 by 768 pixels", "Unsupported Resolution", JOptionPane.ERROR_MESSAGE);
            int r = JOptionPane.showConfirmDialog(null, "MDS does not support your current display resolution "+ currentDisplayMode.getWidth()+" by "+ currentDisplayMode.getHeight() +".\n\nRequired display resolution 800 by 600.", "MDS", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if(r == JOptionPane.YES_OPTION) {
                try {
                    Runtime.getRuntime().exec("rundll32.exe shell32.dll,Control_RunDLL desk.cpl,,4");
                    System.exit(0);
                } catch(Exception ex1){}
            } else {
                System.exit(0);
            }
        }    
    }
    
    
    
    
    
    private static class DesktopSplashSreen {
    
    
    
        BufferedImage bfi;
        Graphics2D g2d;
        Color c1 = new Color(13, 89, 255, 100);
    
    
    
        public DesktopSplashSreen() {
            bfi = bw.getDesktop().getDesktopBufferedImage();
            g2d = bfi.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(new Font("Dialog", Font.BOLD, 12));
            drawRect();
            //g2d.drawString("The great pleasure in life is doing what people", 200, 400);
        }
        
        
        
        private void drawRect() {
            Color c1 = new Color(255, 255, 255, 60);//13, 89, 255, 100
            g2d.drawImage(bfi , 220, 205, 580, 415, 220, 205, 580, 415, null);
            g2d.drawRoundRect(225, 210, 350, 200, 10, 10);  //225, 210, 350, 200, 10, 10  
            g2d.setColor(c1);
            g2d.fillRoundRect(225, 210, 351, 201, 10, 10);  //225, 210, 351, 201, 10, 10                  
            g2d.setColor(Color.white);                  
        }
        
        
        
        public void loadingDesktopShortcuts() {
            g2d.drawImage(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-app-samba.png"), 235, 230, null);
            g2d.drawString("Loading Desktop Shortcuts", 235, 300);
            bw.getDesktop().paintImmediately(225, 210, 351, 201);
        }
        
        
        
        public void loadingTaskBar() {
            g2d.drawImage(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-action-menu_new.png", 48, 48, ImageManipulator.ICON_SCALE_TYPE).getImage(), 305, 230, null);
            g2d.drawString("Loading TaskBar", 235, 320);
            bw.getDesktop().paintImmediately(225, 210, 351, 201);
        }  
        
        
        
        public void loadingMessenger() {
            g2d.drawImage(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-action-kopeteavailable.png"), 375, 230, null);
            g2d.drawString("Initializing Messenger", 235, 340);
            bw.getDesktop().paintImmediately(225, 210, 351, 201);
        }
        
        
        
        public void loadingBitExchanger() {            
            g2d.drawImage(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-randr.png", 48, 48, ImageManipulator.ICON_SCALE_TYPE).getImage() , 445, 230, null);
            g2d.drawString("Initializing BitExchanger", 235, 360);
            bw.getDesktop().paintImmediately(225, 210, 351, 201);
        }
        
        
        
        public void loadingVirtualThreadingDebugger() {         
            g2d.drawImage(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-app-kbounce.png"), 515, 230, null);
            g2d.drawString("Refreshing VirtualThreadingDebugger", 235, 380);
            bw.getDesktop().paintImmediately(225, 210, 351, 201);
        }
        
        
        
        public void close() {
            g2d.drawImage(bw.getDesktop().getDesktopImage(), 220, 205, 580, 415, 220, 205, 580, 415, null);
            bw.getDesktop().paintImmediately(225, 210, 351, 201);
        }                      
        
        
        
    }       
  
    

}