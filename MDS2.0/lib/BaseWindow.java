/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;



public final class BaseWindow extends JFrame implements WindowListener, ComponentListener, KeyListener,
                                             MouseMotionListener, MouseListener {

    
    private static BaseWindow bw = new BaseWindow();
    private static boolean desktopInitiated = false;
    
    private final String metal = "javax.swing.plaf.metal.MetalLookAndFeel"; 

    public static final int TASKBAR_POSITION_TOP = 43;
    public static final int TASKBAR_POSITION_RIGHT = 65;
    public static final int TASKBAR_POSITION_BOTTOM = 87;
    public static final int TASKBAR_POSITION_LEFT = 21;

    private int currentTaskBarPos = 0;
    
    //MDS_Desktop dp = new MDS_Desktop();
    MDS_Desktop dp;
    
    boolean showDesktop = false;
    boolean isFullScreenWindowActive = false;
    boolean isDesktopLoaded = false;
    
    MDS_PropertiesManager prop = MDS.getMDS_PropertiesManager();
    
    
    
    public BaseWindow() {
                
        //MDS_RegKey rkdp = reg.getKey("DisplayProperties");           
        //MDS_UIManager.setTheme(rkdp.getValue("Theme"));
        //MDS_UIManager.setLaF(metal); 
             
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setTitle(MDS.getMDS_Name());
        this.setSize(tk.getScreenSize());
        this.setLocation(0,0);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.black);
		this.setBackground(Color.black);
        //dp.addMouseMotionListener(this);
                   
        //this.getContentPane().add(spiltPane); 
        this.addWindowListener(this);
        this.addComponentListener(this);
        this.addKeyListener(this); 
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setUndecorated(true);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
            
        MDS.checkDisplayMode(); 
        	
        this.setSize(800, 600);	
        
        gs.setFullScreenWindow(this);               
        this.setVisible(true);
		
		OperatingSystem.getOperatingSystem().setWindowsHook(OperatingSystem.WH_GETMESSAGE, "SunAwtFrame", MDS.getMDS_Name());
		
		//((Component)this).setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageManipulator.getImage("E:\\mouse-cursor-white-large.png"), new Point(0,0), "Wasana"));
		
		
        
        //MDS.fexMonitorID = WinProcess.createProcess("bin\\native\\FexMnt.exe 911");    
              
        //MDS.getSystemCycleThread().addSystemCycleThreadListener(this);      
    
    } 
    

    
    
    
    public static BaseWindow getBaseWindow() {
        return bw;
    }
    
    
    
    public void initiateDesktop() {
        if(desktopInitiated) return;
        desktopInitiated = true;   
        dp = MDS_Desktop.getMDS_Desktop();
    }
    
    
    
    public void showDesktop() {
        if(!showDesktop) {
            showDesktop = true;        
        }
    }
    
    
    
    public MDS_Desktop getDesktop() {
        return dp;
    }
    
    
    
    public void loadDesktop() {
        MDS_Property propdp = prop.getProperty("DisplayProperties");           
        //MDS_UIManager.setTheme(rkdp.getValue("Theme"));
        //MDS_UIManager.setLaF(metal);       
        MDS.getSound().playSound(new File("resources\\sound\\KDE_Startup.wav"));       
        this.getContentPane().removeAll();
        //dp = new MDS_Desktop();
        this.getContentPane().add(dp, BorderLayout.CENTER);
        //this.getContentPane().validate();
        //dp.loadDesktopShortcuts();
        dp.validate();
        dp.repaint();
        isDesktopLoaded = true;
        isFullScreenWindowActive = false;                               
    }
    
    
    
    public boolean isDesktopLoaded() {
        return isDesktopLoaded;    
    } 
    
    
    

    
    
    public void setTaskBarDividerSize_To_Default() {
    
    }
    


    public void setFullScreenWindow(MDS_Panel fWindow) {
        if(!isFullScreenWindowActive) {
            isFullScreenWindowActive = true;
            if(dp != null) this.getContentPane().remove(dp);
            this.getContentPane().add(fWindow, BorderLayout.CENTER);
            this.getContentPane().validate();
        //this.getContentPane().repaint();
        } else {
            
        }
    }
    
    
    
    public void removeFullScreenWindow(MDS_Panel fWindow) {
        if(isFullScreenWindowActive) {
            this.getContentPane().remove(fWindow);
            if(dp != null)this.getContentPane().add(dp, BorderLayout.CENTER);
            this.getContentPane().validate();
            this.getContentPane().repaint();
            isFullScreenWindowActive = false;
        }
    }
    
    
    

    
    

    public void windowActivated(WindowEvent e) {
        MDS.checkDisplayMode();
    }



    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }



    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }



    public void windowDeactivated(WindowEvent e) {
        MDS.getMDS_PropertiesManager().store();
    }



    public void windowDeiconified(WindowEvent e) {
        //MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.REFRESH));
    }



    public void windowIconified(WindowEvent e) {}
	


    public void windowOpened(WindowEvent e) {}



    public void componentHidden(ComponentEvent e) {}



    public void componentMoved(ComponentEvent e) {}



    public void componentResized(ComponentEvent e) {}



    public void componentShown(ComponentEvent e) {}



    public void keyPressed(KeyEvent e) {}



    public void keyReleased(KeyEvent e) {}



    public void keyTyped(KeyEvent e) {}
    
    
    
    public void mouseDragged(MouseEvent e) {}
    
    
    
    public void mouseMoved(MouseEvent e) {
        //System.out.println("X:  "+e.getX() +"          Y : "+ e.getY());        
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){}
        
        
        
    public void mouseReleased(MouseEvent e) {}      
    
    
    
    public void finalize() {
        //Console.println("BW ssFZ");
    }     
   
  
    
} // End of the class   
   
  
   
   