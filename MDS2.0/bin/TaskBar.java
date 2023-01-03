
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;



public class TaskBar extends MDS_Window implements SystemCycleThreadListener, SystemSchedulerThreadListener, ActionListener{


    
    private static boolean loaded = false;
    
    private TaskBarButton tbtnStart;
    private TaskBarButton tbtnStdo;    
    private TaskBarButton tbtnShowDesktop; 
    private TaskBarButton tbtnFileBrowser; 
    
    private MDS_PopupMenu startMenu = new MDS_PopupMenu();
    
    private Mouse mouse = new Mouse();
    
    private TaskBarPopupMenu tbpm = new TaskBarPopupMenu();
    private AppTitleViewer aptv = new AppTitleViewer(tbpm);
    private SystemTray sty = new SystemTray(tbpm);
    private Clock clock = new Clock(tbpm);


    public TaskBar() {
        super(JLayeredPane.POPUP_LAYER); 
        if(!loaded) {
            loaded = true;
            try {
 
                 this.setLayout(new BorderLayout());
                //this.add(aptv);  
                //this.add(sty);                  
                //this.add(clock);
                this.add(new QuickLauncher(this), BorderLayout.WEST); 
				this.add(aptv, BorderLayout.CENTER);  
				this.add(new SystemTrayClock(), BorderLayout.EAST);  
                createStartMenu();
            
                //this.setSize(800, 55);
                //this.setLocation(0, 545);
                this.setSize(1024, 55);
                this.setLocation(0, 713);
                this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
                this.setVisible(true);
               
                //MDS.getSystemCycleThread().addSystemCycleThreadListener(this, "TaskBar");
                VirtualThreading.create_SCT_VT(this, "TaskBar");
                //MDS.getSystemSchedulerThread().addSystemSchedulerThreadListener(this, "Task Bar Clock");
                VirtualThreading.create_SST_VT(this, "TaskBar Clock");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
    private void createStartMenu() {
    
        MDS_Menu mnuSub = null; 
        JMenuItem mni = null;
         
        mnuSub = new MDS_Menu("Accessibility", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "accessibility.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Magnifier", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "magnifier.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);     
        //mni = createStartMenuItem("On Screen Keyboard", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "keyboard.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        //mnuSub.add(mni);           
        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Editors", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gtkvim.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Any Edit", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "anyedit.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Creates and edits text file using basic text formatting.");
        mnuSub.add(mni);         
        startMenu.add(mnuSub);
        
        mnuSub = new MDS_Menu("Games", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-tigert.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Tic Tac Toe", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "TicTacToe.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);  
        mni = createStartMenuItem("Tile Puzzle", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "Tile Puzzle.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));     
        mnuSub.add(mni);         
        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Graphics", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-graphics.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Electronic Eyes", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "apple-red.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays photos.");
        mnuSub.add(mni);
        mni = createStartMenuItem("Screen Capture", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "screenshooter.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);      
        mni = createStartMenuItem("Sun Glasses", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "sun-glasses.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays and edit photos.");
        mnuSub.add(mni);               
        startMenu.add(mnuSub);
         
        mnuSub =new MDS_Menu("Networking", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "network.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni = createStartMenuItem("Web Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "web-browser.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays Web sites on the Internet.");
        mnuSub.add(mni);
        mni = createStartMenuItem("Mail", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "mail.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);
        mni = createStartMenuItem("Messanger", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "Messenger.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("You can send messages to other computes.");
        mnuSub.add(mni); 
        mni = createStartMenuItem("Bit Exchanger", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "BitExchanger.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("You can send files to other computers.");
        mnuSub.add(mni);                  
        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Multimedia", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-multimedia.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));         
        mni = createStartMenuItem("Media Player", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "media-player.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Plays your digital media.");
        mnuSub.add(mni);
        mni = createStartMenuItem("Mp3 Player", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "mp3-player.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Plays your mp3 media.");
        mnuSub.add(mni);         
        startMenu.add(mnuSub);

        mnuSub = new MDS_Menu("Programming", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "programming.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));         
        mni = createStartMenuItem("Neo", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "neo.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        startMenu.add(mnuSub);
         
        mnuSub = new MDS_Menu("Utilities", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-util.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE)); 
        mni = createStartMenuItem("Class Connection Walker", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "ClassConnectionWalkerpng.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);
        mni = createStartMenuItem("System Manager", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "TaskManager.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        mni = createStartMenuItem("Clip Board Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "clipBoard.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);     
        mni = createStartMenuItem("Printer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "printer.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));       
        mni.setToolTipText("Print douments.");
        mnuSub.add(mni); 
        mni = createStartMenuItem("File Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "file-browser.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);                                 
        mni = createStartMenuItem("Calculator", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "calculator.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Performs basic arithmetic task.");
        mnuSub.add(mni);    
        mni = createStartMenuItem("Registry Editor", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "RegistryEditor.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);  
        mni = createStartMenuItem("Redirected Standard Output Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "stdio.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);   
        mni = createStartMenuItem("System Information", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "System Information.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mni.setToolTipText("Displays current System Information.");
        mnuSub.add(mni);   
        mni = createStartMenuItem("VT Diagnose", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "vt-Diagnose.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);    
        mni = createStartMenuItem("VT Manager", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "virtual-threading.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni); 
        mni = createStartMenuItem("Spy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "spy.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);     
        mni = createStartMenuItem("Object Creator", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "object-creator.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);            
        mni = createStartMenuItem("Jar File Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "jar-file-viewer.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);                                                         
        mni = createStartMenuItem("Zip File Viewer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "mds-zip.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);        
        mni = createStartMenuItem("Matrix File Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "matrix-fileBrowser.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);  
        mni = createStartMenuItem("Java Class Browser", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "matrix-fileBrowser.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);
        mni = createStartMenuItem("User Accounts", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-user-accounts.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        mnuSub.add(mni);					           
        startMenu.add(mnuSub);  
         
        mni = createStartMenuItem("Control Center", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "settings.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        startMenu.add(mni);                       
         
        mni = createStartMenuItem("Find Files", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "search.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));      
        startMenu.add(mni);                                   
         
        mni = createStartMenuItem("Help", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-help.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        startMenu.add(mni);
         
        startMenu.addSeparator(); 
         
        mni = createStartMenuItem("Run Command", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "launcher-program.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        startMenu.add(mni);
         
        startMenu.addSeparator(); 
         
        mni = createStartMenuItem("Lock Screen", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-lockscreen.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        startMenu.add(mni);         
         
        //mni = createStartMenuItem("Log Out", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-logout.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));                
        //startMenu.add(mni);        
         
        mni = createStartMenuItem("Close MDS", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-shutdown.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));     
        startMenu.add(mni);
                           
    }
    
    
    
    public JMenuItem createStartMenuItem(String text, ImageIcon i) {
        JMenuItem mni= new JMenuItem(text, i);
        mni.addActionListener(this);
        return mni;    
    }    
    
    
    
    public boolean isStartMenuVisible() {;
        return startMenu.isVisible();
    } 
    
    
    
    public boolean isAnyAppTitleButtonPopupMenuVisible() {   
        return aptv.getPopupMenuVisibility();
    } 
    
    
    
    public boolean isTaskBarPopupMenuVisible() {
        return tbpm.getTaskBarPopupMenuVisibility();
    }               
    
    
    
    public static void MDS_Main(String arg[]) {
        new TaskBar();
    }
    
    
    
    public long getSystemCycle_EventInterval() {
        return 100;
    }
    
    
    
    public void autoExecuteSubRoutine() {
		
        aptv.refreshAppTitleViewer();
		
        if(mouse.getMousePointerPosY()>= 763) {
            //this.setSize(800, 55);   
            //this.setLocation(0, 545); 
            this.setSize(1024, 55);
            this.setLocation(0, 713);
			this.validate();
        } else if(mouse.getMousePointerPosY()<= 708) {
            if(!isStartMenuVisible() && !isAnyAppTitleButtonPopupMenuVisible() && !isTaskBarPopupMenuVisible()) {
                //this.setSize(800, 5); 
                //this.setLocation(0, 595);
                this.setSize(1024, 5);
                this.setLocation(0, 763);           
            }
        }
    }  
    
    
    
    public long getSystemScheduler_EventInterval() {
        return 1000;
    }
    


    public void systemSchedulerEvent() {
        //clock.updateTime();            
    } 
    
    
    
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
			
			
			System.out.println("HalfLife");
			
			//new HalfLife();
			
			//new HalfLifeClient("3");
			
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
            prm.execute(new File(MDS.getBinaryPath(),"WebBrowser.class"), fName);           
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
        } else if(e.getActionCommand().equals("Matrix File Browser")) {
            prm.execute(new File(MDS.getBinaryPath(), "MatrixFileBrowser.class"));
        } else if(e.getActionCommand().equals("Java Class Browser")) {
			prm.execute(new File(MDS.getBinaryPath(), "JavaClassBrowser.class"));		
        } else if(e.getActionCommand().equals("User Accounts")) {
        	prm.execute(new File(MDS.getBinaryPath(), "UserAccounts.class"));
		}   


    }         
    
    
    
    
    
    class TaskBarButton extends MDS_Button {
    
        
        
        ImageIcon icon;
        int iconWidth;
        int iconHeight;
    
    
    
        public TaskBarButton(ImageIcon i) {
            icon = i;
            iconWidth = i.getImage().getWidth(null);
            iconHeight = i.getImage().getHeight(null);
            initialize();
        }
        
        
        
        private void initialize() {
            this.setIcon(icon);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setContentAreaFilled(false);
            this.setRolloverIcon(ImageManipulator.createScaledImageIcon(icon, iconWidth+4, iconHeight+4, ImageManipulator.ICON_SCALE_TYPE));        
        }
        
        
    }
    
    
    
    
    
    class TaskBarPopupMenu implements ActionListener {
    
    
    
        MDS_PopupMenu taskBarPopupMenu;
    
    
    
        public TaskBarPopupMenu() {
             taskBarPopupMenu = new MDS_PopupMenu();
             CreatePopupMenuItem("Cascade Windows");
             taskBarPopupMenu.addSeparator();
             CreatePopupMenuItem("System Manager");
             CreatePopupMenuItem("Properties");
        }      
        
        
        
        public void showPopupMenu(Component invoker , int x, int y) {
            taskBarPopupMenu.show(invoker, x, y);
        }
        
        
        
        private void CreatePopupMenuItem(String text) {
            JMenuItem mni = new JMenuItem(text);
            mni.addActionListener(this);
            taskBarPopupMenu.add(mni);
        }
        
        
        
        public boolean getTaskBarPopupMenuVisibility() {
            return taskBarPopupMenu.isVisible();
        } 
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Properties")) {
                //new TaskBarPropertyPanel (false);
                throw new NullPointerException();
                //MDS.getExceptionManager().showException(new NullPointerException()); 
            } else if(e.getActionCommand().equals("System Manager")) {
                ProcessManager prm = MDS.getProcessManager();
                prm.execute(new File(MDS.getBinaryPath(),"SystemManager.class"));            
            }
        
        }
    }        
    
    
    
    
    
    class AppTitleViewer extends MDS_Panel implements MouseListener {
    
    
    
        Vector vctFrames;
        Vector vctFrameIds = new Vector();
        Vector vctCurrentFrameIds = new Vector();
        Vector vctAppTitleButtonList = new Vector();
        MDS_Frame frame;
        ButtonGroup btg = new ButtonGroup();
        MDS_Panel pnlHolder_1 = new MDS_Panel(new GridLayout(1,0));
        MDS_Panel pnlHolder_2 = new MDS_Panel(new GridLayout(1,0));
        boolean addToTop = true;
        AppTitleViewer aptv;
        TaskBarPopupMenu tbPopupMenu;
        
    
    
        public AppTitleViewer(TaskBarPopupMenu tbpm) {
            tbPopupMenu = tbpm;
            aptv = this;
            this.setBorder(BorderFactory.createEtchedBorder());
            //this.setLocation(183, 3);
            //this.setSize(500, 48);
            //this.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 3));
            this.setLayout(new GridLayout(2,0)); 
            this.addMouseListener(this);   
            this.add(pnlHolder_1);       
            pnlHolder_1.addMouseListener(this);
            this.add(pnlHolder_2);      
            pnlHolder_2.addMouseListener(this);                        
        }
        
        
        
        public boolean getPopupMenuVisibility() {
            boolean visibility = false;
            AppTitleButton button;
            for(int x=0;x<=vctAppTitleButtonList.size()-1;x++) {
                button = (AppTitleButton)vctAppTitleButtonList.elementAt(x);  
                if(button.isPopupVisible()) {
                    visibility = true;
                }      
            }

            return visibility;
            
        }
        
        
        
        public void refreshAppTitleViewer() {
        
            try{  
                 
                vctFrames = MDS.getMDS_UIManager().getAllFrames();
  
                for(int x=0;x<=vctFrames.size()-1;x++) {
                    frame = (MDS_Frame) vctFrames.get(x);
                    if(!vctFrameIds.contains(new Integer(frame.getFrameId()))) {
                        vctFrameIds.addElement(new Integer(frame.getFrameId()));
                        if(frame.isFrameTitle_Visible_In_TaskBar() == true)
                            createAppTitleButton(frame.getTitle(), frame);
                    }
  
                    vctCurrentFrameIds.addElement(new Integer(frame.getFrameId()));
        //        
                }

                for(int x=0;x<=vctFrameIds.size()-1;x++) {
                    if(!vctCurrentFrameIds.contains(vctFrameIds.elementAt(x))) {
                        removeAppTitleButton(new Integer(String.valueOf(vctFrameIds.elementAt(x))));
                        vctFrameIds.remove(x);
                    }             
                }            
 
                vctCurrentFrameIds.removeAllElements();
                
                for(int x=0;x<=vctAppTitleButtonList.size()-1;x++) {
                    MDS_Frame frm = ((AppTitleButton)vctAppTitleButtonList.elementAt(x)).getLinkedFrame();
                    AppTitleButton atb = (AppTitleButton)vctAppTitleButtonList.elementAt(x); 
                    if(frm.isSelected()) {
                        if(!atb.isSelected()) atb.setSelected(true);   
                    }
                }                
                
            } catch(Exception ex) {
                //System.out.println("refreshAppTitleViewer() ");
                //System.out.println(ex.toString());
            }      
            
        }
        
        
        
        private void createAppTitleButton(String text, MDS_Frame frame) {
            AppTitleButton aptb = new AppTitleButton(text, frame);
            vctAppTitleButtonList.addElement(aptb);
            btg.add(aptb);
            if(addToTop) {
                pnlHolder_1.add(aptb);
                addToTop=false;
            } else {
                pnlHolder_2.add(aptb);
                addToTop = true;
            }    
        }
        
        
        
        private void removeAppTitleButton(Integer frameId) {
        
            AppTitleButton removalButton;
            Integer removalFrameId;
             
            for(int x=0;x<=vctAppTitleButtonList.size()-1;x++) {
                removalButton = (AppTitleButton)vctAppTitleButtonList.elementAt(x);
                removalFrameId = removalButton.getLinkedFrameId();
                if(removalFrameId.equals(frameId)) {
                    pnlHolder_1.remove(removalButton);
                    pnlHolder_2.remove(removalButton);
                    this.repaint();
                    vctAppTitleButtonList.removeElementAt(x);
                } 
            }
            
        } 
        
        
        
        public void mouseClicked(MouseEvent e) {	               
            if(e.getButton()==e.BUTTON3) {
                if(e.getSource() == this) {
                    tbPopupMenu.showPopupMenu(this, e.getX(), e.getY());
                } else if(e.getSource() == pnlHolder_1) {
                    tbPopupMenu.showPopupMenu(pnlHolder_1, e.getX(), e.getY());
                } else if(e.getSource() == pnlHolder_2) {
                    tbPopupMenu.showPopupMenu(pnlHolder_2, e.getX(), e.getY()); 
                } 
            }    
        }
            
            
            
        public void mouseEntered(MouseEvent e) {}
            
            
            
        public void mouseExited(MouseEvent e) {}
            
            
            
        public void mousePressed(MouseEvent e) {}
            
            
            
        public void mouseReleased(MouseEvent e) {}          
        
        
        
        
        
        class AppTitleButton extends MDS_ToggleButton implements MouseListener, ItemListener, ActionListener  {
        
        
        
            Integer frameId;
            MDS_Frame frame;
            MDS_PopupMenu aptbPopupMenu;
            JMenuItem mniRestore;
            JMenuItem mniMinimize;
            JMenuItem mniMaximize;
            JMenuItem mniClose;
            
            
        
            public AppTitleButton(String appName, MDS_Frame f) {
                super(appName);
                frame = f;
                this.setFont(new Font("Dialog", Font.PLAIN, 11));
                this.setIcon(frame.getFrameIcon());
                this.setSize(60,20);
                this.setHorizontalAlignment(SwingConstants.LEFT );
                this.setVerticalAlignment(SwingConstants.CENTER);
                aptbPopupMenu = new MDS_PopupMenu();
                mniRestore = createMenuItem("Restore");
                aptbPopupMenu.add(mniRestore);
                mniMinimize = createMenuItem("Minimize");
                aptbPopupMenu.add(mniMinimize);
                mniMaximize = createMenuItem("Maximize");
                aptbPopupMenu.add(mniMaximize);
                aptbPopupMenu.addSeparator();
                mniClose = createMenuItem("Close");
                aptbPopupMenu.add(mniClose);
                this.addMouseListener(this);
                this.addItemListener(this);
                this.setToolTipText(frame.getTitle()); 
            }
            
            
            
            public boolean isPopupVisible() {
                //mniMinimize.setEnabled(frame.isIconifiable());
                //mniMaximize.setEnabled(frame.isMaximizable());
                //mniClose.setEnabled(frame.isClosable());
                return aptbPopupMenu.isVisible();
            }
            
            
            
            private JMenuItem createMenuItem(String text) {
                 JMenuItem mni = new JMenuItem(text);
                 mni.addActionListener(this); 
                 return mni;    
            }
            
            
            
            public Integer getLinkedFrameId() {
                return new Integer(frame.getFrameId());
            }
            
            
            
            public MDS_Frame getLinkedFrame() {
                return frame;
            }
            
            
            
            public void mouseClicked(MouseEvent e) {
                try{
                    if(e.getButton()==e.BUTTON1) {
                        if(!frame.isSelected() || frame.isIcon()) { 
                            frame.setIcon(false);                           
                            frame.setSelected(true);
	                      } else if(frame.isSelected() && !frame.isIcon()) {
	                          if(frame.isIconifiable()) {
	                              frame.setIcon(true);
	                          }
	                      } 
	                                  	        
                    } else if(e.getButton()==e.BUTTON3) {
                        this.setSelected(true);
                        if(!frame.isIcon()) {
                            mniRestore.setEnabled(false);      
                        } else {
                            mniRestore.setEnabled(true); 
                        }
                        
                        if(frame.isMaximum()) {
                            mniMaximize.setEnabled(false);
                            mniRestore.setEnabled(true);
                        } else {
                            mniMaximize.setEnabled(true);
                            mniRestore.setEnabled(false);                       
                        }
                        
                         if(frame.isIcon()) {
                            mniMinimize.setEnabled(false);
                            mniRestore.setEnabled(true);
                        } else {
                            mniMinimize.setEnabled(true);
                        } 
                        
                        if(!frame.isIcon())
                        mniMinimize.setEnabled(frame.isIconifiable());
                        
                        if(!frame.isMaximum())
                        mniMaximize.setEnabled(frame.isMaximizable());
                        
                        mniClose.setEnabled(frame.isClosable());                                              
                        
                        if(!frame.isSelected()) frame.setSelected(true);
                        
                        aptbPopupMenu.show(this,e.getX(), e.getY());
                    } 
                } catch(Exception ex) {}       
            }
            
            
            
            public void mouseEntered(MouseEvent e) {}
            
            
            
            public void mouseExited(MouseEvent e) {}
            
            
            
            public void mousePressed(MouseEvent e) {}
            
            
            
            public void mouseReleased(MouseEvent e) {}
            
            
            
            public void actionPerformed(ActionEvent e) {          
                try{       
                 
                    if( e.getActionCommand().equals("Close")){
                        frame.dispose();
                    } 
                    if(e.getActionCommand().equals("Maximize")) {
                        frame.setMaximum(true);
                    }
                    if(e.getActionCommand().equals("Minimize")) {
                        frame.setIcon(true);
                    }
                    if(e.getActionCommand().equals("Restore")) {
                        frame.setIcon(false);
                        frame.setMaximum(false);
                    }               
                
                }catch(Exception ex) {}                 
            }
            
            
            
            public void itemStateChanged(ItemEvent e) {
               // aptbPopupMenu.show(this,0,0);
            }
            
            
            
        }   
        
        
        
    } 
    
    
    
    
    
    class Clock extends MDS_Panel implements MouseListener {
    
    
    
        TaskBarPopupMenu tbpm;
    
    
        
        public Clock(TaskBarPopupMenu tbpMenu) {
            tbpm = tbpMenu;
            this.setLayout(null);  
            //this.setSize(70,48);
			this.setPreferredSize(new Dimension(70, 48));
            //this.setSize(50,50);
            this.setLocation(728,3);
            //this.setBackground(Color.red);
            this.setBorder(BorderFactory.createEtchedBorder());
            this.addMouseListener(this);
        }
        
        
        
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 =(Graphics2D)g;
            
            g2.setRenderingHints(MDS.getMDS_Graphics().getRenderingHints());
            
            drawDigitalClock(g2);
            //drawTraditionalClock(g2);
            //drawMiniDigitalClock(g2);
                        
        }
        
        
        
        private void drawDigitalClock(Graphics2D g2) {
        
            Calendar calendar = new GregorianCalendar();
            g2.setFont(new Font("Impact", Font.PLAIN, 20));          
            String hours = String.valueOf(calendar.get(Calendar.HOUR));
            String minutes = String.valueOf(calendar.get(Calendar.MINUTE)); 
            
            g2.drawString(hours  ,5,20); 
            
            g2.drawString(":",28, 17);  
            
            g2.drawString(minutes,38,20); 
            
            g2.setFont(new Font("Impact", Font.PLAIN, 15));
            
            String d = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String m = String.valueOf(calendar.get(Calendar.MONTH));
            String y = String.valueOf(calendar.get(Calendar.YEAR));
            String date = String.valueOf(d)+" : "+String.valueOf(m)+" : "+String.valueOf(y).substring(2);
                        
            g2.drawString(date,5,40);
                    
        }
        
        
        
        private void drawMiniDigitalClock(Graphics2D g2) {
        
            Calendar calendar = new GregorianCalendar();       
        
            String hours = String.valueOf(calendar.get(Calendar.HOUR));
            String minutes = String.valueOf(calendar.get(Calendar.MINUTE)); 
            
            g2.drawString(hours  ,5,17); 
            
            g2.drawString(":", 20, 16);
            
            g2.drawString(minutes, 29, 17);
 
            String d = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String m = String.valueOf(calendar.get(Calendar.MONTH));
            String date = String.valueOf(d)+" : "+String.valueOf(m);
                        
            g2.drawString(date,5,38); 
                                      
        }
        
        
        
        private void drawTraditionalClock(Graphics2D g2) {
        
            Calendar calendar = new GregorianCalendar();
            String hours = String.valueOf(calendar.get(Calendar.HOUR));
            g2.drawOval(3, 3, 43, 43);
            
            g2.drawLine(25, 25, 50, 3);
            
        }
        
        
   
        public void updateTime() {
            repaint();
        }
        
        
        
        public void mouseClicked(MouseEvent e) {	               
            if(e.getButton()==e.BUTTON3) {
                tbpm.showPopupMenu(this,e.getX(), e.getY());
            }    
        }
            
            
            
        public void mouseEntered(MouseEvent e) {}
            
            
            
        public void mouseExited(MouseEvent e) {}
            
            
            
        public void mousePressed(MouseEvent e) {}
            
            
            
        public void mouseReleased(MouseEvent e) {}            
        
        
        
    }
    
    
    
    
    
    class SystemTray extends MDS_Panel implements MouseListener {
    
    
    
        MDS_Label lblSound = new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"sound.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        MDS_Label lblDisplay = new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"display.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        TaskBarPopupMenu tbPopupMenu;
        
        
    
        public SystemTray(TaskBarPopupMenu tbpm) {
            tbPopupMenu = tbpm;
            this.setBorder(BorderFactory.createEtchedBorder());
            this.setLayout(new GridLayout(2,0));
            lblSound.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.addMouseListener(this);
            lblSound.addMouseListener(this);
            this.add(lblSound);
            lblDisplay.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblDisplay.addMouseListener(this);
            this.add(lblDisplay);
            //this.setLocation(684, 3);
            //this.setSize(42, 48);  
			this.setPreferredSize(new Dimension(42, 48));      
        }
        
        
        
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()==e.BUTTON1) {
                ProcessManager prm = MDS.getProcessManager();
                if(e.getSource() == lblSound) {
                     prm.execute(new File(MDS.getBinaryPath(),"SoundProperties.class"));
                 } else if(e.getSource() == lblDisplay) {
                     prm.execute(new File(MDS.getBinaryPath(),"DisplayProperties.class")); 
                 }        
            } else if(e.getButton()==e.BUTTON3) {
                if(e.getSource() == this) {
                    tbPopupMenu.showPopupMenu(this, e.getX(), e.getY());     
                } else if(e.getSource() == lblSound) {
                    tbPopupMenu.showPopupMenu(lblSound, e.getX(), e.getY()); 
                } else if(e.getSource() == lblDisplay) {
                    tbPopupMenu.showPopupMenu(lblDisplay, e.getX(), e.getY()); 
                }
            }
        }
            
            
            
        public void mouseEntered(MouseEvent e) {}
            
            
            
        public void mouseExited(MouseEvent e) {}
            
            
            
        public void mousePressed(MouseEvent e) {}
            
            
            
        public void mouseReleased(MouseEvent e) {}        
        
        
        
    }  
	
	
	
	
	
	private class QuickLauncher extends MDS_Panel 
	{
	
	
	
	    public QuickLauncher(TaskBar tb) 
	    {
			    this.setLayout(null);
                tbtnStart = new TaskBarButton(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-mds.png"));
                tbtnStart.setBounds(5, 1, 55, 55);
                tbtnStart.addActionListener(tb);
                tbtnStart.setActionCommand("StartMenu");
                this.add(tbtnStart);
                
                tbtnStdo = new TaskBarButton(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-rdso-1.png"));
                tbtnStdo.setBounds(55, 1, 55, 55);
                tbtnStdo.addActionListener(tb);
                tbtnStdo.setActionCommand("RSDO");
                this.add(tbtnStdo);  
        
                tbtnShowDesktop = new TaskBarButton(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-desktop.png"));
                tbtnShowDesktop.setBounds(105, 1, 55, 55);
                tbtnShowDesktop.addActionListener(tb);
                tbtnShowDesktop.setActionCommand("ShowDesktop");
                this.add(tbtnShowDesktop);
        
                tbtnFileBrowser = new TaskBarButton(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-fil-manager.png"));
                tbtnFileBrowser.setBounds(153 , 3, 55, 55);
                tbtnFileBrowser.addActionListener(tb);
                tbtnFileBrowser.setActionCommand("FileBrowser");
                this.add(tbtnFileBrowser); 
				
				this.setPreferredSize(new Dimension(210, 58));
	    }
		
	}	
	
	
	
	
	
	private class SystemTrayClock extends MDS_Panel 
	{
	
	
	
	    public SystemTrayClock() 
	    {
			this.setLayout(new BorderLayout());
			this.add(sty, BorderLayout.WEST);
			this.add(clock, BorderLayout.CENTER);
	    }
		
	
	}
	      
    
    
    
    
    
    
    
    
    
}    