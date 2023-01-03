import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;


public class TaskBarStartMenu implements ActionListener, MouseListener {
	
	
	
	private MDS_PopupMenu startMenu = new MDS_PopupMenu();
	
	
	
	public TaskBarStartMenu() {
		startMenu.setDefaultLightWeightPopupEnabled(true);
		//startMenu.setOpaque(false);
		Color c1 = new Color(255, 255, 255, 40);
		//startMenu.setBackground(Color.green);
//		startMenu.setBackground(c1);
		createStartMenu();
	}
	
	
	
    private void createStartMenu() {
    
        MDS_Menu mnuSub = null; 
        JMenuItem mni = null;
         
        mnuSub = new MDS_Menu("Accessibility", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-access.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Magnifier", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kappfinder.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);     
        //mni = createStartMenuItem("On Screen Keyboard", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "keyboard.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        //mnuSub.add(mni);           
        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Editors", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-action-editplots.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Any Edit", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kedit.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Creates and edits text file using basic text formatting.");
        mnuSub.add(mni);         
        startMenu.add(mnuSub);
        
//        mnuSub = new MDS_Menu("Games", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-package_games.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
//        mni = createStartMenuItem("Tic Tac Toe", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "TicTacToe.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
//        mnuSub.add(mni);  
//        mni = createStartMenuItem("Tile Puzzle", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "Tile Puzzle.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));     
//        mnuSub.add(mni);         
//        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Graphics", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-colors.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Electronic Eyes", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-xeyes.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays photos.");
        mnuSub.add(mni);
        mni = createStartMenuItem("Screen Capture", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-ksnapshot.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);      
        mni = createStartMenuItem("Sun Glasses", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "sun-glasses.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays and edit photos.");
        mnuSub.add(mni);               
        startMenu.add(mnuSub);
         
        mnuSub =new MDS_Menu("Networking", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-network.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Web Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-package_network.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays Web sites on the Internet.");
        mnuSub.add(mni);
        mni = createStartMenuItem("Mail", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-email.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);
        mni = createStartMenuItem("Messanger", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-action-kopeteavailable.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("You can send messages to other computes.");
        mnuSub.add(mni); 
        mni = createStartMenuItem("Bit Exchanger", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-randr.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("You can send files to other computers.");
        mnuSub.add(mni);                  
        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Multimedia", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kscd.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));         
        mni = createStartMenuItem("Media Player", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-noatun.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Plays your digital media.");
        mnuSub.add(mni);
////        mni = createStartMenuItem("Mp3 Player", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-noatun.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
////        mni.setToolTipText("Plays your mp3 media.");
////        mnuSub.add(mni);         
        startMenu.add(mnuSub);

        mnuSub = new MDS_Menu("Programming", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-package_development.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));         
        mni = createStartMenuItem("Neo", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-action-metacontact_online.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        mni = createStartMenuItem("Maximus", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-phppg.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);
        mni = createStartMenuItem("Java Class Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-ksplash.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        mni = createStartMenuItem("Jar File Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-fsview.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);   
        mni = createStartMenuItem("Class Connection Walker", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-device-blockdevice.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);     
        mni = createStartMenuItem("Jar File Creator", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-action-knewstuff.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);         	   	      	                	
        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Utilities", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-package_utilities.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE)); 
        mni = createStartMenuItem("System Manager", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-systemtray.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        mni = createStartMenuItem("Clip Board Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-klipper.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);     
        mni = createStartMenuItem("Printer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-printer1.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));       
        mni.setToolTipText("Print douments.");
        mnuSub.add(mni); 
        mni = createStartMenuItem("File Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_crystal.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);                                 
        mni = createStartMenuItem("Calculator", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-xcalc.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Performs basic arithmetic task.");
        mnuSub.add(mni);    
//        mni = createStartMenuItem("Registry Editor", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "RegistryEditor.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
//        mnuSub.add(mni);  
        mni = createStartMenuItem("Redirected Standard Output Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-terminal.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);   
        mni = createStartMenuItem("System Information", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-hwinfo.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays current System Information.");
        mnuSub.add(mni);   
        mni = createStartMenuItem("VT Diagnose", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kwin4.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);    
        mni = createStartMenuItem("VT Manager", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kbounce.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        mni = createStartMenuItem("Spy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-ktuberling.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);     
        mni = createStartMenuItem("Object Creator", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kbackgammon_engine.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);                                                                    
        mni = createStartMenuItem("Zip File Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_tar.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);      
        mni = createStartMenuItem("Jar File Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_tar.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);          	  
        mni = createStartMenuItem("Matrix File Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_html.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        mni = createStartMenuItem("Properties Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-taskbar.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);   
        mni = createStartMenuItem("Commander", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-terminal.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);         	     	 

//        mni = createStartMenuItem("User Accounts", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-user-accounts.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
//        mnuSub.add(mni);					           
        startMenu.add(mnuSub);  
         
        mni = createStartMenuItem("Control Center", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kcontrol.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        startMenu.add(mni);                       
         
        mni = createStartMenuItem("Find Files", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kfind.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));      
        startMenu.add(mni);                                   
         
        mni = createStartMenuItem("Help", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-khelpcenter.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        startMenu.add(mni);
         
        startMenu.addSeparator(); 
         
        mni = createStartMenuItem("Run Command", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-filesys-exec.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        startMenu.add(mni);
         
        startMenu.addSeparator(); 
         
//        mni = createStartMenuItem("Lock Screen", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-lockscreen.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
//        startMenu.add(mni);         
         
        //mni = createStartMenuItem("Log Out", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-logout.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));                
        //startMenu.add(mni);        
         
        mni = createStartMenuItem("Close MDS", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-action-exit.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));     
        startMenu.add(mni);
                           
    }
    
    
    
    public JMenuItem createStartMenuItem(String text, ImageIcon i) {
        JMenuItem mni= new JMenuItem(text, i);
		//mni.setOpaque(false);
//		Color c1 = new Color(255, 255, 255, 40);
//		mni.setBackground(c1);        
        mni.addActionListener(this);
//        mni.addMouseListener(this);
        return mni;    
    }    
    	
    	
    	
    public void show() {
    	startMenu.show(MDS.getBaseWindow().getDesktop(),10,350);
    }
    
    
    
    public boolean isVisible() {
    	return startMenu.isVisible();
    }	
    	
    	
    	
    public void mouseClicked(MouseEvent e) {}
    
    
    
    public void mouseEntered(MouseEvent e) {}
    
    
    
    public void mouseExited(MouseEvent e) {
    	((JMenuItem)e.getSource()).repaint();
    	((JMenuItem)e.getSource()).validate();
    }
    
    
    
    public void mousePressed(MouseEvent e) {}
    
    
    
    public void mouseReleased(MouseEvent e) {}	
    
    
    
 	public void actionPerformed(ActionEvent e) {	
    
        ProcessManager prm = MDS.getProcessManager();
    
        if(e.getActionCommand().equals("StartMenu")) {
            startMenu.show(MDS.getBaseWindow().getDesktop(),10,298);
        } else if(e.getActionCommand().equals("FileBrowser")) {
		
            //prm.execute(new File(MDS.getBinaryPath(), "FileBrowser.class"));
			//prm.execute(new File(MDS.getBinaryPath(), "QuickLauncher.class"));
			//prm.execute(new File(MDS.getBinaryPath(), "Maximus.class"));
			/*MDS_Frame frm = new MDS_Frame("W", true, true);
			frm.setBounds(200,200,200,200);
			frm.setVisible(true);*/
			/*
			class Tmt extends Thread{
				public Tmt(){
					super("Tmt");
					this.start();
				}
				public void run() {
					while(true) {
						int x = 0;
					}
				}
			}
			*/
			//new Tmt();
			
			
			
			//System.out.println("TaskBar");
			//prm.exe();
			
			
			
			//throw new RuntimeException("Ms");
			//prm.execute(new File(MDS.getBinaryPath(), "UserAccounts.class"));
			
			
			//System.out.println("HalfLife");
			
			//new HalfLife();
			
			//new HalfLifeClient("3");
			
			prm.execute(new File(MDS.getBinaryPath(), "JarFileCreator.class"));
			
        } else if(e.getActionCommand().equals("RSDO")) {   
			prm.execute(new File(MDS.getBinaryPath(), "Commander.class"));		         
            //prm.execute(new File(MDS.getBinaryPath(), "RedirectedStandardOutputViewer.class")); 
            //prm.execute(new File("E:\\Temp\\TsExe1.class"));    
            //MDS.getMDS_Debugger().println("Mahesh XXXX 25");
            //Console.println("Mahesh 911");
            //throw new RuntimeException("L:K:LK:LKL:");
            
        } else if(e.getActionCommand().equals("ShowDesktop")) {          
            MDS_UIManager uim = MDS.getMDS_UIManager();
            Vector vctFrames = uim.getAllFrames();
            for(int x=0;x<=vctFrames.size()-1;x++) {
                MDS_Frame frm = (MDS_Frame)vctFrames.elementAt(x);
                try {
                	frm.setIcon(true);
                } catch(Exception ex) {}
            }
        }
        
        
        if(e.getActionCommand().equals("Close MDS")) {
            MDS.close(); 	
        } else if(e.getActionCommand().equals("Log Out")) { 
    		
        } else if(e.getActionCommand().equals("Control Center")) {
            prm.execute(new File(MDS.getBinaryPath(), "ControlCenter.class"));
        } else if(e.getActionCommand().equals("Media Player")) {
            prm.execute(new File(MDS.getBinaryPath(), "MediaPlayer.class"));
        } else if(e.getActionCommand().equals("Mp3 Player")) {
            prm.execute(new File(MDS.getBinaryPath(), "Mp3Player.class"));
        } else if(e.getActionCommand().equals("Electronic Eyes")) {
            prm.execute(new File(MDS.getBinaryPath(), "ElectronicEyes.class"));
            //prm.execute("AnyEdit");
        } else if(e.getActionCommand().equals("Find Files")) {
            prm.execute(new File(MDS.getBinaryPath(), "FindFiles.class"));
        } else if(e.getActionCommand().equals("Class Connection Walker")) {
            prm.execute(new File(MDS.getBinaryPath(), "ClassConnectionWalker.class"));
        } else if(e.getActionCommand().equals("Deep Blue Sea")) {
            prm.execute(new File(MDS.getBinaryPath(), "DeepBlueSea.class"));
        } else if(e.getActionCommand().equals("System Manager")) {
            prm.execute(new File(MDS.getBinaryPath(), "SystemManager.class"));
        } else if(e.getActionCommand().equals("Run Command")) {
            MDS.getProcessManager().showRunCommandDialog();
        } else if(e.getActionCommand().equals("Calculator")) {
            prm.execute(new File(MDS.getBinaryPath(), "Calculator.class"));
        } else if(e.getActionCommand().equals("Clip Board Viewer")) {
            prm.execute(new File(MDS.getBinaryPath(), "ClipBoardViewer.class"));
        } else if(e.getActionCommand().equals("Registry Editor")) {
            prm.execute(new File(MDS.getBinaryPath(), "RegistryEditor.class"));
        } else if(e.getActionCommand().equals("Messanger")) { 
            MDS.getMessenger().launch();
        } else if(e.getActionCommand().equals("Bit Exchanger")) { 
            MDS.getBitExchanger().launch();
        } else if(e.getActionCommand().equals("Redirected Standard Output Viewer")) {
            prm.execute(new File(MDS.getBinaryPath(), "RedirectedStandardOutputViewer.class"));
        } else if(e.getActionCommand().equals("Screen Capture")) {
            prm.execute(new File(MDS.getBinaryPath(), "ScreenCapture.class"));
        } else if(e.getActionCommand().equals("Sun Glasses")) {
            prm.execute(new File(MDS.getBinaryPath(), "SunGlasses.class"));
        } else if(e.getActionCommand().equals("Any Edit")) {  
            prm.execute(new File(MDS.getBinaryPath(), "AnyEdit.class"));
        } else if(e.getActionCommand().equals("System Information")) {  
            prm.execute(new File(MDS.getBinaryPath(), "SystemInformation.class"));          
        } else if(e.getActionCommand().equals("Mail")) {  
            prm.execute(new File(MDS.getBinaryPath(), "E_Mail.class"));          
        } else if(e.getActionCommand().equals("File Browser")) {
             prm.execute(new File(MDS.getBinaryPath(), "FileBrowser.class"));
        } else if(e.getActionCommand().equals("Printer")) {
            MDS.getPrinter().showPrintDialog();
        } else if(e.getActionCommand().equals("Help")) {
            String[] fName ={"User\\Help\\MDS_Help.htm"};
            prm.execute(new File(MDS.getBinaryPath(),"WebBrowser"), fName);           
        } else if(e.getActionCommand().equals("Web Browser")) {
            prm.execute(new File(MDS.getBinaryPath(), "WebBrowser.class"));  
        } else if(e.getActionCommand().equals("VT Diagnose")) {
            prm.execute(new File(MDS.getBinaryPath(), "VT_Diagnose.class"));
        } else if(e.getActionCommand().equals("VT Manager")) {
            prm.execute(new File(MDS.getBinaryPath(), "VirtualThreadingManager.class"));
        } else if(e.getActionCommand().equals("Tile Puzzle")) {
            //prm.execute("TilePuzzle");
        } else if(e.getActionCommand().equals("Magnifier")) {
            prm.execute(new File(MDS.getBinaryPath(), "Magnifier.class"));
        } else if(e.getActionCommand().equals("On Screen Keyboard")) {
            //prm.execute("OnScreenKeyboard");
        } else if(e.getActionCommand().equals("Spy")) {
            prm.execute(new File(MDS.getBinaryPath(), "Spy.class"));
        } else if(e.getActionCommand().equals("Lock Screen")) {
        	throw new NullPointerException();
            //MDS.getExceptionManager().showException(new NullPointerException()); 
        } else if(e.getActionCommand().equals("Object Creator")) {
            prm.execute(new File(MDS.getBinaryPath(), "ObjectInstantiater.class"));
        } else if(e.getActionCommand().equals("Jar File Viewer")) {
            prm.execute(new File(MDS.getBinaryPath(), "JarFileViewer.class"));
        } else if(e.getActionCommand().equals("Zip File Viewer")) {
            prm.execute(new File(MDS.getBinaryPath(), "ZipFileViewer.class"));
        } else if(e.getActionCommand().equals("Neo")) {
            prm.execute(new File(MDS.getBinaryPath(), "Neo.class"));
        } else if(e.getActionCommand().equals("Maximus")) {
            prm.execute(new File(MDS.getBinaryPath(), "Maximus.class"));    
        } else if(e.getActionCommand().equals("Matrix File Browser")) {
            prm.execute(new File(MDS.getBinaryPath(), "MatrixFileBrowser.class"));
        } else if(e.getActionCommand().equals("Java Class Browser")) {
			prm.execute(new File(MDS.getBinaryPath(), "JavaClassBrowser.class"));		
        } else if(e.getActionCommand().equals("User Accounts")) {
        	prm.execute(new File(MDS.getBinaryPath(), "UserAccounts.class"));
		} else if(e.getActionCommand().equals("Jar File Creator")) {
        	prm.execute(new File(MDS.getBinaryPath(), "JarFileCreator.class"));
		} else if(e.getActionCommand().equals("Properties Viewer")) {
        	prm.execute(new File(MDS.getBinaryPath(), "MDS_PropertiesViewer.class"));
		}  else if(e.getActionCommand().equals("Commander")) {
        	prm.execute(new File(MDS.getBinaryPath(), "Commander.class"));
		}      


    }     
    		
	
}
