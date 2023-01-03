/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.logging.*;
 


public final class MDS_Desktop extends MDS_DesktopPane implements MouseListener, ActionListener, DesktopIconListener, SystemSchedulerThreadListener {


    
    private static MDS_Desktop dsk = new MDS_Desktop();
    private static boolean desktopShortcutsLoaded = false;
    private static boolean desktopRefreshing = false;
    
    final int diWidth = 110;
    final int diHeight = 70;   
    
    int currentDiX = 0;
    int currentDiY = 10;
    
    JLabel lblDesktopItemContainer = new JLabel();
    MDS_PopupMenu desktopPopupMenu = new MDS_PopupMenu();
    BufferedImage bfiDesktopImage;
    Graphics2D g2dDesktopImage; 
    Image subImage;
    
    MDS_DesktopIcon diDiskDrives;
	MDS_DesktopIcon diUserHome;
    MDS_DesktopIcon diPrinter;
    MDS_DesktopIcon diWebBrowser;
    MDS_DesktopIcon diBitExchanger;
    MDS_DesktopIcon diMessenger;
    MDS_DesktopIcon diNeo;
    
    MDS_Point currentIconLocation;
     
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    
    boolean tipsOfMinuteVisibility = false;
    
    Logger log = Logger.getLogger("MDS_Desktop");
    
    

    private MDS_Desktop() {
    
        super(); 
        	
////        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageManipulator.getImage("E:\\MDS2.0\\MDS2.0\\resources\\graphics\\CURSERS\\mouse-cursor-normal.png"),new Point(0,0), "Mahesh")); 	     
        
        this.setLayout(null);
        get_Reg_Data();
        lblDesktopItemContainer.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        lblDesktopItemContainer.setLocation(0,0);
        this.add(lblDesktopItemContainer);

/*        
        createDesktopIcons();   
        this.addMouseListener(this);
        createDesktopPopupMenuItem("Run Command", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "launcher-program.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        createDesktopPopupMenuItem("System Manager", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "TaskManager.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));        
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("Refresh", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "refresh.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));        
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("Lock Screen", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-lockscreen.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        //createDesktopPopupMenuItem("Log Out", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-logout.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("New Shortcut", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "shortcut.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));        
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("Properties", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "display.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("About MDS", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "MDS.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));        
*/        
		//thrdTip.setPriority(Thread.MIN_PRIORITY);
        //thrdTip.start();
        
        //loadDesktopShortcuts();
        
        //MDS.getSystemSchedulerThread().addSystemSchedulerThreadListener(this, "Tip of the Minute");
        //VirtualThreading.create_SST_VT(this, "Tip of the Minute");
    }
	
	
	
	public void createPrimaryItems() {
        createDesktopIcons();   
        this.addMouseListener(this);
        createDesktopPopupMenuItem("Run Command", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-filesys-exec.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        createDesktopPopupMenuItem("System Manager", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-system.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));        
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("Refresh", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-action-reload.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));        
//        desktopPopupMenu.addSeparator();
//        createDesktopPopupMenuItem("Lock Screen", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-lockscreen.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        //createDesktopPopupMenuItem("Log Out", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-logout.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("New Shortcut", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "shortcut.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));        
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("Properties", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-samba.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));
        desktopPopupMenu.addSeparator();
        createDesktopPopupMenuItem("About MDS", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-tux.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));  	
	}
    
    
    
    public static MDS_Desktop getMDS_Desktop() {
        return dsk;
    }
    
    
    
    public void loadDesktopShortcuts() {
        
        if(!desktopShortcutsLoaded) desktopShortcutsLoaded = true;
        else if(!desktopRefreshing) return;
        
        try {
            String shtPath = MDS.getFileManager().getMDS_DesktopShortcuts_Dir().getPath();
            if(!shtPath.endsWith("\\")) shtPath = shtPath.concat("\\"); 
            Vector v = new Vector();
            v.addElement("dkst");
            Vector vctDkst = MDS.getFileManager().getContent_Files(shtPath, v);
            for(int x=0;x<=vctDkst.size()-1;x++) {
                File f = (File)vctDkst.elementAt(x); 
                ObjectInputStream o_In = new ObjectInputStream(new FileInputStream(f.getPath()));        
                //Hashtable ht = (Hashtable)o_In.readObject();  
                File stf = (File)o_In.readObject();     
                DesktopShortcut ds = new DesktopShortcut(stf, f, subImage, getNextIconLocation());
                o_In.close();
                lblDesktopItemContainer.add(ds);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    private MDS_Point getNextIconLocation() {
        if(currentIconLocation != null) {    
            if(currentIconLocation.getY() > 600) { //500
                currentIconLocation = new MDS_Point(currentIconLocation.getX()+100, 10);    
            } else {
                currentIconLocation = new MDS_Point(currentIconLocation.getX(), currentIconLocation.getY()+100);                
            }
        } else {
            currentIconLocation = new MDS_Point(10, 10);
        }
        
        return currentIconLocation;
        
    }
    
    
    
    private void createDesktopPopupMenuItem(String text, ImageIcon i) {
       JMenuItem mni = new JMenuItem(text, i);
       mni.addActionListener(this);
       desktopPopupMenu.add(mni);
    }
    
    
    
    public void refreshDesktop() {
        desktopRefreshing = true;
        lblDesktopItemContainer.removeAll();
        currentIconLocation = null;
        createDesktopIcons();
        loadDesktopShortcuts();
        this.validate();
        this.repaint();
        desktopRefreshing = false;
    }
	
	
	private void refreshDesktop_shallow() {
        this.validate();
        this.repaint();		
	}
    
    
    
    private void createDesktopIcons() {
        
//        diDiskDrives = new MDS_DesktopIcon(subImage, new File(ImageManipulator.MDS_ICONS_PATH + "48-app-systemtray.png"), "Disk Drives", getNextIconLocation(), "Shows the disk drives and hardware connected to this computer.");
//        diDiskDrives.addDesktopIconListener(this);
//        lblDesktopItemContainer.add(diDiskDrives);  
//		
//		diUserHome = new MDS_DesktopIcon(subImage, new File(ImageManipulator.MDS_ICONS_PATH + "48-app-kfm_home.png"), "User Home", getNextIconLocation(), "User Home directory.");
//        diUserHome.addDesktopIconListener(this);
//        lblDesktopItemContainer.add(diUserHome);  
//        
//        diPrinter = new MDS_DesktopIcon(subImage, new File(ImageManipulator.MDS_ICONS_PATH + "48-device-printer1.png"), "Printer", getNextIconLocation(), "Print douments.");
//        diPrinter.addDesktopIconListener(this);
//        lblDesktopItemContainer.add(diPrinter);      
//        
//        diWebBrowser = new MDS_DesktopIcon(subImage, new File(ImageManipulator.MDS_ICONS_PATH + "48-app-package_network.png"), "Web Browser", getNextIconLocation(), "Displays Web sites on the Internet.");
//        diWebBrowser.addDesktopIconListener(this);
//        lblDesktopItemContainer.add(diWebBrowser); 
//        
//        diBitExchanger = new MDS_DesktopIcon(subImage, new File(ImageManipulator.MDS_ICONS_PATH + "BitExchanger.png"), "Bit Exchanger", getNextIconLocation(), "You can send files to other computers.");
//        diBitExchanger.addDesktopIconListener(this);
//        lblDesktopItemContainer.add(diBitExchanger); 
//        
//        diMessenger = new MDS_DesktopIcon(subImage, new File(ImageManipulator.MDS_ICONS_PATH + "48-action-metacontact_online.png"), "Messenger", getNextIconLocation(), "You can send messages to other computes.");
//        diMessenger.addDesktopIconListener(this);
//        lblDesktopItemContainer.add(diMessenger); 
        
//        diNeo = new MDS_DesktopIcon(subImage, new File(ImageManipulator.MDS_ICONS_PATH + "neo.png"), "Neo", getNextIconLocation(), "You create MDS compatible java applications.");
//        diNeo.addDesktopIconListener(this);
//        lblDesktopItemContainer.add(diNeo);         
        
        //MDS_Label lb = new MDS_Label("KHHHHHHHHHHHHHHHHHHH");
        //lb.setBounds(200,200,200,200);
        //lblDesktopItemContainer.add(lb);
                                               
    }
    
    
    
    private void removeDesktopIcons() {

        diDiskDrives.removeDesktopIconListener(this);
        lblDesktopItemContainer.remove(diDiskDrives);    
        
        diPrinter.removeDesktopIconListener(this);
        lblDesktopItemContainer.remove(diPrinter);      
        
        diWebBrowser.removeDesktopIconListener(this);
        lblDesktopItemContainer.remove(diWebBrowser); 
        
        diBitExchanger.removeDesktopIconListener(this);
        lblDesktopItemContainer.remove(diBitExchanger); 
        
        diMessenger.removeDesktopIconListener(this);
        lblDesktopItemContainer.remove(diMessenger);
        
        diNeo.removeDesktopIconListener(this);
        lblDesktopItemContainer.remove(diNeo);   
      
    }

    
    
    public ImageIcon getWallPaper() {
        return (ImageIcon)lblDesktopItemContainer.getIcon();
    }
    
    
    
    private void setWallPaper(ImageIcon i) {
        if(i.getIconWidth() == 1024 && i.getIconHeight() == 768) {
            lblDesktopItemContainer.setIcon(i);
        } else {
            MDS_OptionPane.showMessageDialog("Invalid image size, Image size should be 1024 by 768.", "Desktop Wallpaper", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    public Image getDesktopImage() {
        return subImage;
    }
    
    
    
    public BufferedImage getDesktopBufferedImage() {
        return bfiDesktopImage;
    }    
    
    
    
    public void setWallPaper(File f) {
        try {                                           
            bfiDesktopImage = ImageIO.read(f);  
            g2dDesktopImage = bfiDesktopImage.createGraphics();         
            setWallPaper(new ImageIcon((Image)bfiDesktopImage));
            
            subImage = ImageManipulator.getImage(f.getPath());
            
            removeDesktopIcons();
            
            createDesktopIcons();
            
            tipsOfMinuteVisibility = false;
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }            
    }
    
    
    
    private void get_Reg_Data() {
        try {
            MDS_Property propdp = ppm.getProperty(DisplayProperties.PROPERTY_NAME);
            File f = new File(propdp.getSupProperty(DisplayProperties.PROPERTY_WALLPAPER_FILE_PATH));  
			//File f = new File(ImageManipulator.MDS_WALLPAPER_PATH+"stelvio-1024-768.JPG");
            
            if(ImageManipulator.isImage(f.getPath())) { 
            	log.fine("Load Wallpaper path = "+f.getPath());                                          
                bfiDesktopImage = ImageIO.read(f);  
                g2dDesktopImage = bfiDesktopImage.createGraphics();         
                setWallPaper(new ImageIcon((Image)bfiDesktopImage));          
                subImage = ImageManipulator.getImage(f.getPath());
            } else {
            	log.warning("Load Wallpaper failed path = "+f.getPath()); 
                bfiDesktopImage = createDefaltWallPaper();
                g2dDesktopImage = bfiDesktopImage.createGraphics(); 
                setWallPaper(new ImageIcon((Image)bfiDesktopImage));
                subImage = (Image)createDefaltWallPaper();
            }    
            	
            String theme = propdp.getSupProperty(DisplayProperties.PROPERTY_THEME_NAME);
            MDS_UIManager.setTheme(theme);  
            //MDS_UIManager.setLaF(MDS_UIManager.metal);	
            
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
     
    }
    
    
    
    private BufferedImage createDefaltWallPaper() {
        BufferedImage bfi = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics g2d = bfi.createGraphics();
        //g2d.setColor(UIManager.getColor("ToolTip.background"));
        g2d.setColor(this.getBackground());
        g2d.fillRect(0, 0, 800, 600);
        return bfi;
    }
    
    
    
    private void drawTip() {/*
        Random rand = new Random();
        int tpNo = (Math.abs(rand.nextInt()))%10;
        //int x = 9;
		
		int x = 734;
        
            
        if(tpNo == 0) {
            g2dDesktopImage.drawString("MDS is Designed and Developed by Mahesh ", x, 45);
            g2dDesktopImage.drawString("Dharmasena, and it is programmed 99% using", x, 60);
            g2dDesktopImage.drawString("Java and 1% using 'C' (Native Libraries).", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);   
        } else if(tpNo == 1) {
            g2dDesktopImage.drawString("You can transfer binary files from one", x, 45);
            g2dDesktopImage.drawString("computer to another using 'Bit Exchanger',", x, 60);
            g2dDesktopImage.drawString("it uses my own file transfer machanism.", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);           
        } else if(tpNo == 2) {
            g2dDesktopImage.drawString("Class Connection Walker allows you to see", x, 45);
            g2dDesktopImage.drawString("constructor, methods and members ect, of a", x, 60);
            g2dDesktopImage.drawString("specified class.", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);           
        } else if(tpNo == 3) {        
            g2dDesktopImage.drawString("Great successes never come without risks.", x, 45);
            g2dDesktopImage.drawString("", x, 60);
            g2dDesktopImage.drawString("", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);                  
        } else if(tpNo == 4) {        
            g2dDesktopImage.drawString("The great pleasure in life is doing what people", x, 45);
            g2dDesktopImage.drawString("say you cannot do.", x, 60);
            g2dDesktopImage.drawString("", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105); 
        } else if(tpNo == 5) {                 
            g2dDesktopImage.drawString("Imagination is more important than the", x, 45);
            g2dDesktopImage.drawString("knowledge.", x, 60);
            g2dDesktopImage.drawString("", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);    
        } else if(tpNo == 6) {                                        
            g2dDesktopImage.drawString("Accept the challenges, so that you may feel the", x, 45);
            g2dDesktopImage.drawString("exhilaration of victory.", x, 60);
            g2dDesktopImage.drawString("", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);  
        } else if(tpNo == 7) {                         
            g2dDesktopImage.drawString("Achievements provides the only real pleasure in", x, 45);
            g2dDesktopImage.drawString("life.", x, 60);
            g2dDesktopImage.drawString("", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);
        } else if(tpNo == 8) {                          
            g2dDesktopImage.drawString("An interpreter is a program that translates", x, 45);
            g2dDesktopImage.drawString("instructions one line at a time, a compiler works", x, 60);
            g2dDesktopImage.drawString("by translating entire program at one time, Java", x, 75);
            g2dDesktopImage.drawString("has both the interpreter and compiler.", x, 90);
            g2dDesktopImage.drawString("", x, 105);   
        } else if(tpNo == 9) {                         
            g2dDesktopImage.drawString("You can use 'Neo' to develope MDS compatiable", x, 45);
            g2dDesktopImage.drawString("Java applications", x, 60);
            g2dDesktopImage.drawString("", x, 75);
            g2dDesktopImage.drawString("", x, 90);
            g2dDesktopImage.drawString("", x, 105);
        }    */                                 
    }
    
      
        
    
    public void mouseClicked(MouseEvent e) {	
        if(e.getButton()==e.BUTTON1) {
              
        } else if(e.getButton()==e.BUTTON3) {
            desktopPopupMenu.show(this,e.getX(), e.getY());
        } 
           
    }
            
            
            
    public void mouseEntered(MouseEvent e) {}
            
            
            
    public void mouseExited(MouseEvent e) {}
            
            
            
    public void mousePressed(MouseEvent e) {}
            
            
            
    public void mouseReleased(MouseEvent e) {}  
    
    
    
    public void actionPerformed(ActionEvent e) {
        ProcessManager prm = MDS.getProcessManager();
        
        if(e.getActionCommand().equals("Properties")) {
            //prm.execute("DisplayProperties");
			prm.execute(new File(MDS.getBinaryPath(), "DisplayProperties.class"));
        } else if(e.getActionCommand().equals("Run Command")) {
            MDS.getProcessManager().showRunCommandDialog();
        } else if(e.getActionCommand().equals("Refresh")) {
            refreshDesktop();
        } else if(e.getActionCommand().equals("System Manager")) {           
            //prm.execute("SystemManager");
			prm.execute(new File(MDS.getBinaryPath(), "SystemManager.class"));
        } else if(e.getActionCommand().equals("About MDS")) {
            MDS.About_MDS();
        } else if(e.getActionCommand().equals("Lock Screen")) {
            throw new NullPointerException(); 
        } else if(e.getActionCommand().equals("New Shortcut")) {
            //Console.println("df");
            //prm.execute("CreateShortcut");
			prm.execute(new File(MDS.getBinaryPath(), "CreateShortcut.class"));
        }
    }
    
    
    
    public void desktopIcon_Clicked(DesktopIconEvent e) {
        
        if(e.getIconName().equals("Disk Drives")) {
            MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"MatrixFileBrowser.class"));
        } else if(e.getIconName().equals("Bit Exchanger")) {
            MDS.getBitExchanger().launch();    
        } else if(e.getIconName().equals("Messenger")) {
            MDS.getMessenger().launch();
        } else if(e.getIconName().equals("Web Browser")) {
            ProcessManager prm = MDS.getProcessManager();
            prm.execute(new File(MDS.getBinaryPath(),"WebBrowser"));
        } else if(e.getIconName().equals("Printer")) {
        /*
            FileManager.FileChooser fmfc = MDS.getFileManager().getFileChooser(FileManager.FileChooser.OPEN_DIALOG);
            Vector v = new Vector();
            v.add("png");
            v.add("gif");
            v.add("jpg");
            v.add("jpeg");
            fmfc.setFilter(v);            
            if(fmfc.showDiaog(null) ==  fmfc.APPROVE_OPTION) {
                MDS.getPrinter().print(new ImageIcon(fmfc.getPath()));
            }*/
            
            MDS.getPrinter().showPrintDialog();
        } else if(e.getIconName().equals("Neo")) {
            MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"Neo.class"));    
        }
        
    }
    
    
    
    public long getSystemScheduler_EventInterval() {
        if(!tipsOfMinuteVisibility) {
            return 100;
        } else {
            return 60000;
            //return 500;
        }
    }



    public void systemSchedulerEvent() {
        //Console.println("schdle");
        /*
        g2dDesktopImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2dDesktopImage.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Color c1 = new Color(13, 89, 255, 50);
                    
        //g2dDesktopImage.drawImage(subImage, 495, 5, 795, 115, 495, 5, 795, 115, null);
        
		g2dDesktopImage.drawImage(subImage, 719, 5, 1019, 115, 719, 5, 1019, 115, null); 
		       
        g2dDesktopImage.drawRoundRect(724, 10, 290, 100, 10, 10);
        g2dDesktopImage.setColor(c1);
        g2dDesktopImage.fillRoundRect(724, 10, 291, 101, 10, 10);
        
        g2dDesktopImage.setColor(new Color(255, 255, 255));
        g2dDesktopImage.setFont(new Font("Dialog", Font.BOLD, 15));
        g2dDesktopImage.drawString("Tip Of The Minute ! ", 734, 25);
        
        g2dDesktopImage.setFont(new Font("Dialog", Font.BOLD, 12));
        
        drawTip();
        
        this.paintImmediately(724, 10, 290, 100);
        //MDS.getBaseWindow().getDesktop().repaint(); 
        
        tipsOfMinuteVisibility = true;            */    
        
    }  
    
    
    
    public void finalize() {

    }       
    
    
    
    
     

}