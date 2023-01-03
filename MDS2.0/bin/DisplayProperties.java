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
import javax.swing.border.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.util.*;
import javax.swing.table.*;
import java.io.*;
import java.util.logging.*;



public class DisplayProperties extends ControlModule implements ItemListener, ActionListener {



    private static DisplayProperties dp;
    private static boolean dp_Visibility = false;
    
    // Possible Look & Feels
    private final String mac      =
            "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    private final String metal    =
            "javax.swing.plaf.metal.MetalLookAndFeel";
    private final String motif    =
            "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    private final String windows  =
            "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            
    final String newWallpaperName = "New Wallpaper";            
    
    JComponent contentPane;
    MDS_TabbedPane tbpDisplay;
    MDS_Panel pnlTheme = new MDS_Panel();
    MDS_Panel pnlThemePreview = new MDS_Panel();
    JDesktopPane dpnThemePreview = new JDesktopPane();
    JInternalFrame ifrmThemePreview = new JInternalFrame("Active Frame", true, true, true, true);
    MDS_Panel pnlDesktop = new MDS_Panel();
    MDS_Panel pnlScreenSaver = new MDS_Panel();
    MDS_Panel pnlApearance = new MDS_Panel();
    MDS_Panel pnlSettings = new MDS_Panel();
    
    MDS_ComboBox jcboThemes;
    MDS_Button btnThemeBrowse;
    
    MDS_ComboBox cboScreenSaver;
    MDS_Button btnScreenSaverSettings;
    MDS_Button btnScreenSaverPreview;
    MDS_List lstScreenSaverWait;
    
    MDS_TableModel MDS_tbm;
    JTable tblDisplayModes;
    
    MDS_ComboBox cboWallPapers;
    MDS_Button btnWallPaperBrowse;	
    MDS_Label lblWallPaperPanel;
    Hashtable dpData;
    Hashtable dpDefaultData;
    
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    
    Logger log = Logger.getLogger("DisplayProperties");
    
    public static final String PROPERTY_NAME = "DisplayProperties";
    public static final String PROPERTY_WALLPAPER_FILE_PATH = "wallpaperFile";
    public static final String PROPERTY_SCREEN_SAVER_NAME = "screenSaverName";
    public static final String PROPERTY_SCREEN_SAVER_LAUNCH_WAIT_TIME = "screenSaverLaunchWaitTime";
    public static final String PROPERTY_THEME_NAME = "themeName";
    
    private MDS_Spinner spnScreenSaverLaunchWaitTime;

    public DisplayProperties(int initialTabNo) {
        super("Display Properties", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-samba.png"));
        UIManager.addPropertyChangeListener(new UISwitchListener((JComponent)this.getRootPane()));
            
        contentPane = this.get_CM_ContentPane();
        
        tbpDisplay = new MDS_TabbedPane();
        
        createThemePanel();     
        tbpDisplay.add("Theme", pnlTheme);
        
        createDesktopPanel();	
        tbpDisplay.add("Desktop", pnlDesktop);
        
        createScreenSaverPanel();
        tbpDisplay.add("Screen Saver", pnlScreenSaver);
        
        tbpDisplay.add("Appearance", pnlApearance);
        createSettingsPanel();
        tbpDisplay.add("Settings", pnlSettings);
       
        contentPane.add(tbpDisplay, BorderLayout.CENTER);
            
        get_Reg_Data();
        
        jcboThemes.addItemListener(this);
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                dp_Visibility = false;
                dp = null;                      
            }
        });        
        
        this.setSize(500, 400);
        this.setCenterScreen();
        this.setVisible(true);
            
        dp_Visibility = true;
        dp = this;

    }
    
    
    
    private void createThemePanel() {
        pnlTheme.setLayout(null);
        
        jcboThemes = new MDS_ComboBox();
        //jcboThemes.addItemListener(this);
        jcboThemes.setName("Theme");
        jcboThemes.setBounds(20, 20, 180, 28);
        jcboThemes.addItem("OceanTheme");
        jcboThemes.addItem("Steel");
        jcboThemes.addItem("MDS Default");
        jcboThemes.addItem("Emerald");
        jcboThemes.addItem("Oxide");
        jcboThemes.addItem("Sandstone");
        jcboThemes.addItem("Ruby");
        jcboThemes.addItem("High Contrast");
        jcboThemes.addItem("Charcol");
        pnlTheme.add(jcboThemes); 
        //jcboThemes.addItemListener(this);
        //jcboThemes.setSelectedItem(reg.getValue("CurrentTheme"));
              
        btnThemeBrowse  = new MDS_Button("Browse");
        btnThemeBrowse.setBounds(230, 20, 100, 28);
        pnlTheme.add(btnThemeBrowse);
        
        pnlThemePreview.setLayout(null);
        pnlThemePreview.setBounds(20, 60, 430, 235);
        pnlThemePreview.setLayout(new BorderLayout());
        pnlThemePreview.setBorder(BorderFactory.createLoweredBevelBorder());
        
        ifrmThemePreview.setBounds(50,50, 300,150);
        ifrmThemePreview.setDefaultCloseOperation(ifrmThemePreview.DO_NOTHING_ON_CLOSE);
        ifrmThemePreview.getContentPane().add(new JTextArea("Text"));
        ifrmThemePreview.setVisible(true);
        try{
            ifrmThemePreview.setSelected(true);
        } catch(Exception ex) {}        
        dpnThemePreview.add(ifrmThemePreview);
        pnlThemePreview.add(dpnThemePreview, BorderLayout.CENTER);
        pnlTheme.add(pnlThemePreview);     
    }
    
    
    
    public void createDesktopPanel() {
        pnlDesktop.setLayout(null);
        MDS_Label lblMonitor = new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_PICTURE_PATH+"monitor.gif"));
        lblMonitor.setLayout(null);
        lblMonitor.setBounds(140, 10, 200, 190);
        pnlDesktop.add(lblMonitor);
        
        //ImageManipulator.createScaledImageIcon(MDS.getBaseWindow().getDesktop().getWallPaper() , 145, -1, Image.SCALE_FAST);
        lblWallPaperPanel = new MDS_Label();
        //lblWallPaperPanel.setOpaque(true);
        lblWallPaperPanel.setBounds(22,18 ,148, 113);
        lblMonitor.add(lblWallPaperPanel);
        
        MDS_Panel pnlWallPaper = new MDS_Panel();
        pnlWallPaper.setLayout(null);
        pnlWallPaper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Background Wallpaper"));  
        pnlWallPaper.setBounds(10, 200, 450, 100);
        Vector v = new Vector();
        v.add("png");
        v.add("gif");
        v.add("jpg");
        v.add("jpeg");        
        Vector vctWallPapers = MDS.getFileManager().getContent_Files(ImageManipulator.MDS_WALLPAPER_PATH, v);
        Vector vctWallPaperNames = new Vector();
        for(int x=0;x<=vctWallPapers.size()-1;x++) {
            vctWallPaperNames.add(((File)vctWallPapers.elementAt(x)).getName());
        }
        cboWallPapers = new MDS_ComboBox(vctWallPaperNames);
        cboWallPapers.addItemListener(this);
        cboWallPapers.setName("WallPapers");
        
        //cboWallPapers.setSelectedItem(reg.getValue("WallPaper"));
        
        JScrollPane scrp = new JScrollPane(cboWallPapers);
        scrp.setBounds(20, 40, 250, 50);
        pnlWallPaper.add(scrp);
        btnWallPaperBrowse = new MDS_Button("Browse");
        btnWallPaperBrowse.setBounds(330, 40, 90, 30);
        btnWallPaperBrowse.addActionListener(this);
        btnWallPaperBrowse.setActionCommand("WallPaperBrowse");
        pnlWallPaper.add(btnWallPaperBrowse);
        pnlDesktop.add(pnlWallPaper);              
    }
    
    
    
    public void createScreenSaverPanel() {
        pnlScreenSaver.setLayout(null);
        
        MDS_Label lblMonitor = new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_PICTURE_PATH+"monitor.gif"));
        lblMonitor.setBounds(140, 10, 200, 190);
        pnlScreenSaver.add(lblMonitor);
 
        MDS_Label lblScreenSaverPanel = new MDS_Label("No preview available");
        lblScreenSaverPanel.setBounds(22,18 ,148, 113);
        lblScreenSaverPanel.setBackground(Color.black);
        lblScreenSaverPanel.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblMonitor.add(lblScreenSaverPanel);               
        
        MDS_Panel pnl_scr_Properties = new MDS_Panel();   
        pnl_scr_Properties.setLayout(null);
        pnl_scr_Properties.setBounds(20, 200, 430, 100);
        pnl_scr_Properties.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Screen Saver Properties"));
             
        
        cboScreenSaver = new MDS_ComboBox();
        cboScreenSaver.addItemListener(this);
        cboScreenSaver.setBounds(20, 20, 180, 28);
        cboScreenSaver.addItem("SourceCode");
        cboScreenSaver.addItem("Matrix");
        cboScreenSaver.addItem("ScreenSaver_MDS");
        cboScreenSaver.addItem("RealDream");  
        pnl_scr_Properties.add(cboScreenSaver);
        
        //System.out.println("GET   "+reg.getValue("ScreenSaver"));
        //cboScreenSaver.setSelectedItem(reg.getValue("ScreenSaver"));
        
        btnScreenSaverSettings = new MDS_Button("Settings");
        btnScreenSaverSettings.setBounds(210, 20, 90, 28);
        pnl_scr_Properties.add(btnScreenSaverSettings);
        
        btnScreenSaverPreview = new MDS_Button("Preview");
        btnScreenSaverPreview.addActionListener(this);
        btnScreenSaverPreview.setBounds(310, 20, 80, 28);
        pnl_scr_Properties.add(btnScreenSaverPreview); 
        
        MDS_Label lblWait = new MDS_Label("Wait");
        lblWait.setBounds(20, 55, 30, 28);
        pnl_scr_Properties.add(lblWait);
        
        
////        Vector vctTimes = new Vector();
////        for(int x =0; x<=20; x++) {
////            vctTimes.addElement(String.valueOf(x));
////        }
////        lstScreenSaverWait = new MDS_List(vctTimes);
////        //lstScreenSaverWait.setBounds(65, 55, 30, 28);
////        JScrollPane scrpWait = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
////        scrpWait.setBounds(60, 60, 55, 20);
////        scrpWait.setViewportView(lstScreenSaverWait);
////        pnl_scr_Properties.add(scrpWait);
////        pnlScreenSaver.add(pnl_scr_Properties);

			spnScreenSaverLaunchWaitTime = new MDS_Spinner(new SpinnerNumberModel(0, 0, 20, 1));
			spnScreenSaverLaunchWaitTime.setBounds(60, 60, 55, 20);
	        pnl_scr_Properties.add(spnScreenSaverLaunchWaitTime);
	        pnlScreenSaver.add(pnl_scr_Properties);        
          
           
    }
    
    
    
    private void createSettingsPanel() {
        pnlSettings.setLayout(null);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice(); 
        
        MDS_Label lblAvailableAcceleratedMemory = new MDS_Label("Available Accelerated Memory : "+MDS.getFileManager().getFormatedFileSize(gs.getAvailableAcceleratedMemory()));           
        lblAvailableAcceleratedMemory.setBounds(10, 10, 300, 20);
        pnlSettings.add(lblAvailableAcceleratedMemory);
        
        MDS_Panel pnlScreenModes = new MDS_Panel();
        pnlScreenModes.setLayout(null);
        pnlScreenModes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Display Modes"));
        pnlScreenModes.setBounds(10, 40, 450, 250);
        MDS_tbm = new MDS_TableModel();
        JTable tblDisplayModes = new JTable(MDS_tbm);
        JScrollPane jscrp = new JScrollPane(tblDisplayModes);
        jscrp.setBounds(10,25,430, 210);
        MDS_tbm.addColumn("Width");
        MDS_tbm.addColumn("Height");
        MDS_tbm.addColumn("Bit Depth");
        MDS_tbm.addColumn("Refresh Rate");
        
        DisplayMode dm[] = gs.getDisplayModes();
        DisplayMode currentDM = gs.getDisplayMode();
        
        for(int x = 0; x < dm.length; x++) {
            Vector data = new Vector();
            
            String text = "";
            
            if(dm[x].equals(currentDM)) {
                text = "* ";    
            } else {
                text = "";
            }
            
            data.add(text+String.valueOf(dm[x].getWidth()));
            data.add(text+String.valueOf(dm[x].getHeight()));
            data.add(text+String.valueOf(dm[x].getBitDepth()));
            data.add(text+String.valueOf(dm[x].getRefreshRate()));
            
            MDS_tbm.addRow(data);
        }
                
        pnlScreenModes.add(jscrp);
        pnlSettings.add(pnlScreenModes);
        
    }
    
    
    
    private void get_Reg_Data() {
        MDS_Property propDp = ppm.getProperty(PROPERTY_NAME);
        jcboThemes.setSelectedItem(propDp.getSupProperty(PROPERTY_THEME_NAME));
        File f = new File(propDp.getSupProperty(PROPERTY_WALLPAPER_FILE_PATH));
        cboWallPapers.setSelectedItem(f.getName());
        cboScreenSaver.setSelectedItem(propDp.getSupProperty(PROPERTY_SCREEN_SAVER_NAME));
        spnScreenSaverLaunchWaitTime.setValue(Long.parseLong(propDp.getSupProperty(PROPERTY_SCREEN_SAVER_LAUNCH_WAIT_TIME)));
    }
    
    
    
    private void save_Reg_Data() {
        MDS_Property propDp = ppm.getProperty(PROPERTY_NAME);
        propDp.setSupProperty(PROPERTY_WALLPAPER_FILE_PATH, ImageManipulator.MDS_WALLPAPER_PATH+(String)cboWallPapers.getSelectedItem());
        propDp.setSupProperty(PROPERTY_SCREEN_SAVER_NAME, (String)cboScreenSaver.getSelectedItem());
        propDp.setSupProperty(PROPERTY_SCREEN_SAVER_LAUNCH_WAIT_TIME, String.valueOf(spnScreenSaverLaunchWaitTime.getValue()));
        propDp.setSupProperty(PROPERTY_THEME_NAME, (String)jcboThemes.getSelectedItem());
        ppm.setProperty(propDp);
    }
  
    
    
    public void itemStateChanged(ItemEvent e) {
        String name = "";
        name = ((MDS_ComboBox)e.getSource()).getName();
        
        if(e.getStateChange() == ItemEvent.SELECTED) {
            if(name != null) {          
                if(name.equals("Theme")) {  
                    MDS_UIManager.setTheme((String)jcboThemes.getSelectedItem());                   
                    //MDS_UIManager.setLaF(metal);
                } else if(name.equals("WallPapers")) {
                    lblWallPaperPanel.setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_WALLPAPER_PATH+cboWallPapers.getSelectedItem(), 148, -1, Image.SCALE_FAST));
                }
                
            }
       
        }
                
    }    
    
    
    
    public void actionPerformed(ActionEvent e) {
        ProcessManager prm = MDS.getProcessManager();
        
        if(e.getActionCommand().equals("WallPaperBrowse")) {
//////            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);
//////            Vector v = new Vector();
//////            v.add("png");
//////            v.add("gif");
//////            v.add("jpg");
//////            v.add("jpeg");
//////            fmfc.setFilter(v);            
//////            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {
//////                String scr = fmfc.getPath();
//////                String des = System.getProperty("user.dir")+"\\"+ImageManipulator.MDS_WALLPAPER_PATH;
//////                File f = new File(fmfc.getPath());
//////                String ext = "."+MDS.getFileManager().getFileExtension(f.getName());
//////                //MDS.getFileManager().copy(scr, des+newWallpaperName+ext, true, null);
//////                //lblWallPaperPanel.setIcon(ImageManipulator.createScaledImageIcon(fmfc.getPath(), 153, 108, Image.SCALE_FAST));
//////                //cboWallPapers.addItem(f.getName());
//////            } 
        } else if(e.getActionCommand().equals("Preview")) {
            //prm.execute(String.valueOf(cboScreenSaver.getSelectedItem()));    
            prm.execute(new File(MDS.getBinaryPath(), String.valueOf(cboScreenSaver.getSelectedItem()+".class")));
        } else if(e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Ok")) {
            
            //MDS.getBaseWindow().getDesktop().setWallPaper(ImageManipulator.getImageIcon(ImageManipulator.MDS_WALLPAPER_PATH+(String)cboWallPapers.getSelectedItem()));
            File tFile = new File(ImageManipulator.MDS_WALLPAPER_PATH+(String)cboWallPapers.getSelectedItem());
            ImageIcon tI = new ImageIcon(tFile.getPath());
            if(tI.getIconWidth() == 1024 && tI.getIconHeight() == 768) {        
                MDS.getBaseWindow().getDesktop().setWallPaper(new File(ImageManipulator.MDS_WALLPAPER_PATH+(String)cboWallPapers.getSelectedItem()));            
                save_Reg_Data();
            } else {
                MDS_OptionPane.showMessageDialog("Invalid image size, Image size should be 1024 by 768.", "Desktop Wallpaper", JOptionPane.ERROR_MESSAGE); 
            }
            
            MDS.getBaseWindow().getDesktop().refreshDesktop();
            
            this.dispose();
        }  
        
       

    }
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!dp_Visibility) {
            new DisplayProperties(3);
        } else {
            try{
                DisplayProperties.dp.setIcon(false);
                DisplayProperties.dp.setSelected(true);
            } catch(Exception ex) {}
        }
    }    
    
}    