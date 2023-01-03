/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;



public class SystemInformation extends MDS_Frame implements ActionListener, MouseListener {



    MDS_User usr = MDS.getUser();
    JComponent contentPane;
    JMenuBar menuBar = new JMenuBar();
    JMenu mnuFile = new JMenu("File");
    JMenuItem mniExit = usr.createMenuItem("Exit", this);
    JMenu mnuHelp = new JMenu("Help");
    JMenuItem mniAbout = usr.createMenuItem("About", this);
    
    JSplitPane sltp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
    
    MDS_ListModel lstmdl = new MDS_ListModel();
    MDS_List lstDevices = new MDS_List(lstmdl);
    
    MDS_TableModel tblmdl = new MDS_TableModel();
    MDS_Table table = new MDS_Table(tblmdl);
    
    Win32WMI_Classes wcls = MDS.getWin32WMI_Classes();
    FileManager fm = MDS.getFileManager();
    
    DefaultTableCellRenderer dftcrd;
    
    int currentIndex = -100;



    public SystemInformation() {
        super("System Information", true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "System Information.png"));
        try {
	        contentPane = (JComponent) this.getContentPane();
	        contentPane.setLayout(new BorderLayout()); 
	        mnuFile.add(mniExit);
	        menuBar.add(mnuFile);
	        mnuHelp.add(mniAbout);
	        menuBar.add(mnuHelp);
	        this.setJMenuBar(menuBar); 	 
	        lstDevices.addMouseListener(this);  
	        lstDevices.setCellRenderer(new MDS_ListCellRenderer());	
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "mds.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "MDS");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-system.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Computer System");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-mime-exec_wine.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Operating System");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-kcmprocessor.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Processor");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-kcmmemory.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Memory");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-linuxconf.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Mother Board");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-linuxconf.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "BIOS");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-device-hdd_unmount.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Hard Drive");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-cdrom_unmount.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "CD Rom");
	        //lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "fdd.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Floppy Drve");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-network.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Modem");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-printer1.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Printer");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-keyboard.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Keyboard");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-mouse.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Pointing Device");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-multimedia.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Sound Device");
	        lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-samba.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE), "Display Device");
	        sltp.setLeftComponent(new JScrollPane(lstDevices));
	        tblmdl.addColumn("Item");
	        tblmdl.addColumn("Value");
	        table.getColumn("Item").setMinWidth(200);
	        table.getColumn("Item").setMaxWidth(300);
	        sltp.setRightComponent(new JScrollPane(table)); 
	        contentPane.add(sltp, BorderLayout.CENTER);      
	        this.setSize(800,600);
	        this.setCenterScreen();
	        this.setVisible(true);   
        } catch(Exception ex) {
        	System.out.println(ex.toString());
        }
           
    }
    
    
    
    private void getComputerSystem_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_ComputerSystem();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                if(String.valueOf(ht.getKey(x)).equals("Total Physical Memory")) {
                    Vector vctData = new Vector();
                    vctData.addElement(ht.getKey(x));
                    vctData.addElement(fm.getFormatedFileSize(Long.parseLong((String)ht.getValue(x))));
                    tblmdl.addRow(vctData);                
                } else {
                    Vector vctData = new Vector();
                    vctData.addElement(ht.getKey(x));
                    vctData.addElement(ht.getValue(x));
                    tblmdl.addRow(vctData);                 
                }
            }
                                                 
        }  
    }
    
    
    
    private void getOperatingSystem_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_OperatingSystem();
               
        for(int x=0;x<=ht.size()-1;x++) {
        
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            }                   
                                                            
        }  
    }    
    
    
    
    private void getProcessor_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_Processor();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement("#Processor Count");
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);               
            } else if(String.valueOf(ht.getKey(x)).equals("Architecture")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(wcls.STRUCT_getArchitecture(Integer.parseInt((String)ht.getValue(x))));
                tblmdl.addRow(vctData);                                   
            } else if(String.valueOf(ht.getKey(x)).equals("Address Width")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x)+" bit");
                tblmdl.addRow(vctData);     
            } else if(String.valueOf(ht.getKey(x)).equals("Data Width")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x)+" bit");
                tblmdl.addRow(vctData);     
            } else if(String.valueOf(ht.getKey(x)).equals("L2 Cache Size")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x)+" kb");
                tblmdl.addRow(vctData);
            } else if(String.valueOf(ht.getKey(x)).equals("Max Clock Speed")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x)+" MHz");
                tblmdl.addRow(vctData);                                     
            } else {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);                
            } 
                                                 
        }  
    }  
    
    //==
    
    private void getMotherboardDevice_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_MotherboardDevice();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } 
                                                 
        }  
    }  
    
    
    
    private void getPhysicalMemory_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_PhysicalMemory();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement("#Memory card Count");
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);    
            } else if(String.valueOf(ht.getKey(x)).equals("Form Factor")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(wcls.STRUCT_getFormFactor(Integer.parseInt((String)ht.getValue(x))));
                tblmdl.addRow(vctData);            
            } else if(String.valueOf(ht.getKey(x)).equals("Memory Type")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(wcls.STRUCT_getMemoryType(Integer.parseInt((String)ht.getValue(x))));
                tblmdl.addRow(vctData);                            
            } else if(String.valueOf(ht.getKey(x)).equals("Capacity")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(fm.getFormatedFileSize(Long.parseLong((String)ht.getValue(x))));
                tblmdl.addRow(vctData);              
            } else {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);           
            } 
                                                 
        }  
    }
    
    
    
    private void getSoundDevice_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_SoundDevice();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } else {
                Vector vctData = new Vector();
                vctData.addElement("#Sound Device Count");
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);             
            } 
                                                 
        }  
    }
    
    
    
    private void getVideoController_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_VideoController();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement("#Graphic Device Count");
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } else if(String.valueOf(ht.getKey(x)).equals("Adapter RAM")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(fm.getFormatedFileSize(Long.parseLong((String)ht.getValue(x))));
                tblmdl.addRow(vctData);       
            } else if(String.valueOf(ht.getKey(x)).equals("Current Scan Mode")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(wcls.STRUCT_getScanMode(Integer.parseInt((String)ht.getValue(x))));
                tblmdl.addRow(vctData);             
            } else if(String.valueOf(ht.getKey(x)).equals("Video Architecture")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(wcls.STRUCT_getVideoArchitecture(Integer.parseInt((String)ht.getValue(x))));
                tblmdl.addRow(vctData);             
            } else if(String.valueOf(ht.getKey(x)).equals("Video Memory Type")) { 
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(wcls.STRUCT_getVideoMemoryType(Integer.parseInt((String)ht.getValue(x))));
                tblmdl.addRow(vctData);                                          
            } else {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);            
            } 
                                                 
        }  
    }
    
    
    
    private void getCDROMDrive_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_CDROMDrive();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } else {
                Vector vctData = new Vector();
                vctData.addElement("#CD ROM Count");
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);             
            } 
                                                 
        }  
    }
    
    
    
    private void getDiskDrive_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_DiskDrive();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement("#Disk Drive Count");
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } else if(String.valueOf(ht.getKey(x)).equals("Size")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(fm.getFormatedFileSize(Long.parseLong((String)ht.getValue(x))));
                tblmdl.addRow(vctData);                 
            } else {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);        
            } 
                                                 
        }  
    }
    
    
    
    private void get_FloppyDrive_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_FloppyDrive();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } 
                                                 
        }  
    }
    
    
    
    private void getLogicalDisk_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_LogicalDisk();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } 
                                                 
        }  
    }
    
    
    
    private void getBIOS_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_BIOS();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } 
                                                 
        }  
    }
    
    
    
    private void getKeyboard_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_Keyboard();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } 
                                                 
        }  
    }
    
    
    
    private void getPointingDevice_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_PointingDevice();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                if(String.valueOf(ht.getKey(x)).equals("DeviceInterface")) {
                    Vector vctData = new Vector();
                    vctData.addElement(ht.getKey(x));
                    vctData.addElement(wcls.STRUCT_DeviceInterface(Integer.parseInt((String)ht.getValue(x))));
                    tblmdl.addRow(vctData);                    
                } else if(String.valueOf(ht.getKey(x)).equals("Handedness")) {
                    Vector vctData = new Vector();
                    vctData.addElement(ht.getKey(x));
                    vctData.addElement(wcls.STRUCT_getHandedness(Integer.parseInt((String)ht.getValue(x))));
                    tblmdl.addRow(vctData);                     
                } else if(String.valueOf(ht.getKey(x)).equals("PointingType")) {
                    Vector vctData = new Vector();
                    vctData.addElement(ht.getKey(x));
                    vctData.addElement(wcls.STRUCT_getPointingType(Integer.parseInt((String)ht.getValue(x))));
                    tblmdl.addRow(vctData);                  
                } else {
                    Vector vctData = new Vector();
                    vctData.addElement(ht.getKey(x));
                    vctData.addElement(ht.getValue(x));
                    tblmdl.addRow(vctData); 
                }
            } 
                                                 
        }  
    }
    
    
    
    private void getPOTSModem_Info() {
    
        MDS_Hashtable ht = wcls.getWin32_POTSModem();
               
        for(int x=0;x<=ht.size()-1;x++) {           
            
            if(!String.valueOf(ht.getKey(x)).equals("#DeviceCount")) {
                Vector vctData = new Vector();
                vctData.addElement(ht.getKey(x));
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData); 
            } else {
                Vector vctData = new Vector();
                vctData.addElement("#Modem Count");
                vctData.addElement(ht.getValue(x));
                tblmdl.addRow(vctData);             
            } 
                                                 
        }  
    } 
    
    
    
    private void getMDS_Info() {
    
        addTableRow("mds.author", "Mahesh Dharmasena");
        addTableRow("mds.author.e-mail", "mahesh_ksl@yahoo.com");
        addTableRow("mds.author.address", "4F 'Sampatha' Madawala Road Polgolla Kandy");
        addTableRow("mds.version", "2.0");
        
        Properties prt = System.getProperties();
        Enumeration enumNames = prt.propertyNames();
        
        while(enumNames.hasMoreElements()) {
            String prtName = String.valueOf(enumNames.nextElement());
            Vector vctData = new Vector();
            vctData.addElement(prtName);
            vctData.addElement(prt.getProperty(prtName));
            tblmdl.addRow(vctData);
        }
    }
    
    
    
    private void getPrinter_Info() {
        addTableRow("N/A", "N/A");
    }
    
    
    
    private void addTableRow(String name, String value) {
        Vector vctData = new Vector();
        vctData.addElement(name);
        vctData.addElement(value);
        tblmdl.addRow(vctData);    
    }                                                        
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("About")) {
            MDS.getUser().showAboutDialog(this, "System Information", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"System Information.png"), MDS.getAbout_Mahesh());
        } else if(e.getActionCommand().equals("Exit")) {
            this.dispose();
        }
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e){
    
        Vector v = tblmdl.getDataVector();
        v.removeAllElements();        
    
        int index = lstDevices.getSelectedIndex();
        
        if(index != currentIndex) {
            currentIndex = index;
            try {
                table.repaint();
                if(index == 0) {
                    getMDS_Info();
                } else if(index == 1) {
                    getComputerSystem_Info();     
                } else if(index == 2) {
                    getOperatingSystem_Info();
                } else if(index == 3) {
                    getProcessor_Info();     
                } else if(index == 4) {
                    getPhysicalMemory_Info();    
                } else if(index == 5) {
                    getMotherboardDevice_Info();
                } else if(index == 6) {
                    getBIOS_Info();
                } else if(index == 7) {
                    getDiskDrive_Info(); 
                } else if(index == 8) {
                    getCDROMDrive_Info();
                } else if(index == 9) {
                    getPOTSModem_Info();
                } else if(index == 10) {
                    getPrinter_Info();
                } else if(index == 11) {
                    getKeyboard_Info(); 
                } else if(index == 12) {
                    getPointingDevice_Info(); 
                } else if(index == 13) {
                    getSoundDevice_Info();
                } else if(index == 14) {
                    getVideoController_Info();
                } else if(index == 15) {
                    getVideoController_Info();
                }   
            } catch(Exception ex) {}     
            
        }                                          
    }
        
        
        
    public void mouseReleased(MouseEvent e) {}      
    
    
    
    public static void MDS_Main(String arg[]) {
    	try { 
        	new SystemInformation();
    	} catch(Exception ex) {
    		System.out.println(ex.toString());
    	}
    }        
    
    
    
}    