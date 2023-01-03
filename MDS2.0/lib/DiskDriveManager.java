
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.lang.reflect.*;


public final class DiskDriveManager {



    FileManager fm = MDS.getFileManager();
    OperatingSystem os = OperatingSystem.getOperatingSystem();
    private static DiskDriveManager ddm = new DiskDriveManager();
    
    
    
    private DiskDriveManager() {}
    
    
    
    public void showDrivePopupMenu(MDS_FilePopupMenuListener l,Component invoker ,int x, int y, File f) {
        new DrivePopupMenu(l, invoker, x, y, f);           
    }
    
    
    
    public static DiskDriveManager getDiskDriveManager() {
        return ddm;
    }
    
    
    
    public void showDriveProperties(File f) {
        new DriveProperties(f);
    }
    
    
    
    
    
    class DrivePopupMenu implements ActionListener {
    
    
    
        MDS_User usr = MDS.getUser();
        MDS_Clipboard clipBoard = MDS.getClipboard();
        
        MDS_PopupMenu pMenu = new MDS_PopupMenu();  
        
        DiskDrives dd = new DiskDrives();
        
        JMenuItem mniOpen = usr.createMenuItem("Open", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"open.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniFind = usr.createMenuItem("Find Files", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"search.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniCopy = usr.createMenuItem("Copy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"copy.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniCopyTo = usr.createMenuItem("Copy To", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"copy to.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);        
        JMenuItem mniEject = usr.createMenuItem("Eject", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"rename.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniRename = usr.createMenuItem("Rename", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"rename.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniProperties = usr.createMenuItem("Properties", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"file-properties.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        
        MDS_FilePopupMenuListener fpml;
        FileSystemEventManager fsem = MDS.getFileSystemEventManager();
        private File file; 
        final String DISK_DRIVES = "Disk Drives";         
    
    
    
        public DrivePopupMenu(MDS_FilePopupMenuListener l,Component invoker ,int x, int y, File f) {
            fpml = l;
            file = f;
            pMenu.add(mniOpen);
            pMenu.add(mniFind);
            pMenu.addSeparator();
            pMenu.add(mniCopy);
            pMenu.add(mniCopyTo);  
            pMenu.addSeparator();
            pMenu.add(mniEject);
            pMenu.addSeparator();
            
            switch(os.getDriveType(f.getPath())) {
                case OperatingSystem.DRIVE_UNKNOWN:     
                    break;
                 case OperatingSystem.DRIVE_NO_ROOT_DIR:
                    break;
                case OperatingSystem.DRIVE_REMOVABLE:
                    FileSystemView fsv = FileSystemView.getFileSystemView(); 
                    if(fsv.isFloppyDrive(f)) {                         
                        pMenu.add(mniRename);
                        pMenu.addSeparator();
                    }            
                    break;
                case OperatingSystem.DRIVE_FIXED:

                    pMenu.add(mniRename);  
                    pMenu.addSeparator();                                         
                    break;
                case OperatingSystem.DRIVE_REMOTE:         
                    break;
                case OperatingSystem.DRIVE_CDROM:                                    
                    break;
                case OperatingSystem.DRIVE_RAMDISK:         
                    break;        
                default:          
                    //MDS.getExceptionManager().showException(new RuntimeException("MDS_FileChooser roots initialization error."));           
            } 
            
            pMenu.add(mniProperties);  
            
            pMenu.show(invoker, x, y);         
              
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Properties")) {
                showDriveProperties(file);
            } else if(e.getActionCommand().equals("Rename")) {
                String ndl = "";  
                ndl = MDS_OptionPane.showInputDialog(fpml.getListener__MDS_Frame(), "Type new Volume Label.", "Drive Manager", JOptionPane.QUESTION_MESSAGE);      
                if(ndl == null) {
                    ndl="";
                }       
                if(!ndl.equals("")) {  
                    try {
                        os.setDriveVolumeLabel(file.getPath(), ndl);
////                        fsem.fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.RENAME_FILE, new File(DISK_DRIVES)));
                    } catch (Exception ex) {
                        MDS_OptionPane.showMessageDialog(fpml.getListener__MDS_Frame(), ex.getMessage(), "Drive Manager", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }                       
                }               
            } else if(e.getActionCommand().equals("Find Files")) {
                String[] absPath ={file.getPath()};
                MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"FindFiles"), absPath);
            } else if(e.getActionCommand().equals("Copy")) {
                MDS.getClipboard().setContent(file ,clipBoard.STATUS_COPIED);
            } else if(e.getActionCommand().equals("Copy To")) {
                String des = "";
                MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
                if(fmdc.showDiaog(fpml.getListener__MDS_Frame()) ==  fmdc.APPROVE_OPTION) {
                    des = fmdc.getPath();
                    if(file.isDirectory()) {
                        MDS.getFileManager().copyFile(file, new File(des), true);
                    } else {
                        MDS.getFileManager().copyFile(file, new File(des+file.getName()), true);    
                    }
                           
                }              
            } else if(e.getActionCommand().equals("Eject")) {  
            	try {
            		os.ejectRemovableMedia(file.getPath());
            	} catch(Exception ex) {
            		MDS_OptionPane.showMessageDialog(fpml.getListener__MDS_Frame(), ex.getMessage(), "Drive Manager", JOptionPane.ERROR_MESSAGE);
            	}
            }                
        }
                
    
    
    }
    
    
    
    
    
    class DriveProperties extends ControlModule implements ActionListener {
    
    
    
        private File file;
        JComponent contentPane;
        MDS_TabbedPane tbpDriveProperties = new MDS_TabbedPane();   
    
    
        public DriveProperties(File f) {
            super("["+FileSystemView.getFileSystemView().getSystemDisplayName(f)+"]"+"  Properties");
            file = f;
            contentPane = this.get_CM_ContentPane();
            tbpDriveProperties.add("General", new DrivePanel(f));
            contentPane.add(tbpDriveProperties, BorderLayout.CENTER);
            this.setSize(500, 300);
            this.setCenterScreen();
            this.setVisible(true);        
        }
        
        
        
        public DriveProperties(String s) {
            this(new File(s));    
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                this.dispose();
            } else if(e.getActionCommand().equals("Ok")) {
                this.dispose();
            }
        }         
        
        
        
        
        
        class DrivePanel extends MDS_Panel {
        
        
        
            private File f;
            private DiskDrives dd = new DiskDrives();
            private ImageIcon icon;
            private String type;
            private String fileSystem;
        
        
            public DrivePanel(File f) {
                super();
                this.setLayout(null);
                file = f;
                switch(os.getDriveType(f.getPath())) {
                    case OperatingSystem.DRIVE_UNKNOWN:     
                        break;
                    case OperatingSystem.DRIVE_NO_ROOT_DIR:
                        break;
                    case OperatingSystem.DRIVE_REMOVABLE:
                        FileSystemView fsv = FileSystemView.getFileSystemView(); 
                        if(fsv.isFloppyDrive(f)) { 
                            icon = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"fdd.png");
                            type = "Floppy Drive";
                        } else {
                            icon = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"jazdisk.png");
                            type = "Removable Drvie";
                        }              
                        break;
                    case OperatingSystem.DRIVE_FIXED:    
                        icon = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"hdd.png");                                      
                        type = "Local Drive";
                        break;
                    case OperatingSystem.DRIVE_REMOTE:   
                        icon = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"remote-drives.png");      
                        type = "Remote Drive";
                        break;
                    case OperatingSystem.DRIVE_CDROM:  
                        icon = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"cdd.png");                                 
                        type = "CD Drive";
                        break;
                    case OperatingSystem.DRIVE_RAMDISK:  
                        icon = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH +"jazdisk.png");      
                        type = "RAM Disk";
                        break;        
                    default:          
                    	new RuntimeException("MDS_FileChooser roots initialization error.");
                        //MDS.getExceptionManager().showException(new RuntimeException("MDS_FileChooser roots initialization error."));           
                } 
                
                MDS_Label lblIcon = new MDS_Label(icon);
                lblIcon.setBounds(10, 10, 50, 50);
                this.add(lblIcon);   
                
                MDS_TextField txtfVolumeLabel = new MDS_TextField(FileSystemView.getFileSystemView().getSystemDisplayName(f));
                txtfVolumeLabel.setBounds(80, 25, 170, 25);
                this.add(txtfVolumeLabel);
                
                JSeparator sprt1 = new JSeparator(SwingConstants.HORIZONTAL);
                sprt1.setBounds(10, 70, 280, 4);
                this.add(sprt1);
                
                MDS_Label lblType = new MDS_Label("Type : "+type);
                lblType.setBounds(10, 75, 270, 25);
                this.add(lblType);
                
                MDS_Label lblFileSystem = new MDS_Label("File System : "+os.getFileSystemName(f.getPath()));
                lblFileSystem.setBounds(10, 95, 270, 25);
                this.add(lblFileSystem);                
       
                JSeparator sprt2 = new JSeparator(SwingConstants.HORIZONTAL);
                sprt2.setBounds(10, 120, 280, 4);
                this.add(sprt2);
                
                long val = f.getTotalSpace() - f.getFreeSpace();
                MDS_Label lblUsedSpace = new MDS_Label("Used space : "+String.valueOf(val)+" bytes   "+fm.getFormatedFileSize(val));
                lblUsedSpace.setBounds(10, 125, 270, 25);
                this.add(lblUsedSpace);                 

                val = f.getFreeSpace();
                MDS_Label lblFreeSpace = new MDS_Label("Free space : "+String.valueOf(val)+" bytes   "+fm.getFormatedFileSize(val));
                lblFreeSpace.setBounds(10, 150, 270, 25);
                this.add(lblFreeSpace); 
                
                val = f.getTotalSpace();
                MDS_Label lblCapacity = new MDS_Label("Capacity : "+String.valueOf(val)+" bytes   "+fm.getFormatedFileSize(val));
                lblCapacity.setBounds(10, 175, 270, 25);                
                this.add(lblCapacity);
                
                JSeparator sprt3 = new JSeparator(SwingConstants.VERTICAL);
                sprt3.setBounds(300, 8, 4, 190);
                this.add(sprt3);                

                //Double dVal = new Double(Math.abs((dd.getDiskTotalSpace(f.getPath()) - dd.getDiskFreeSpace(f.getPath()))/dd.getDiskTotalSpace(f.getPath())*100));
//                double dUsed = dd.getDiskTotalSpace(f.getPath()) - dd.getDiskFreeSpace(f.getPath());
//                double dFree = dd.getDiskFreeSpace(f.getPath());
//                double dTotal = dd.getDiskTotalSpace(f.getPath());

                double dUsed = f.getTotalSpace() - f.getFreeSpace();
                double dFree = f.getFreeSpace();
                double dTotal = f.getTotalSpace();              
                
                
                double dVal = (dUsed / dTotal)*100;
                Double rdVal = new Double(dVal);
                MDS_Label lblUsed = new MDS_Label("Used : "+String.valueOf(Math.abs(rdVal.intValue()))+"%", new Legend(Color.blue));
                lblUsed.setBounds(310, 155, 270, 25);                
                this.add(lblUsed);
                
                dVal = (dFree / dTotal) * 100;
                Double rdFree = new Double(dVal);
                MDS_Label lblFree = new MDS_Label("Free : "+String.valueOf(Math.abs(rdFree.intValue()))+"%", new Legend(Color.green));
                lblFree.setBounds(310, 175, 270, 25);                
                this.add(lblFree);                
                                                         
                this.add(new PieChart(dTotal, dUsed));
            }
            
            
            
            
            
            class Legend implements Icon {
            
            
            
                Color color;
            
            
            
                public Legend(Color c) {
                    color = c;
                }
            
            
            
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    g.setColor(color);
                    g.fillRect(0, 5, 12, 12);
                }
                
                
                
                public int getIconWidth() {
                    return 15;
                }
        
        

                public int getIconHeight() {
                    return 15;
                } 
            
            }
            
            
            
            
            
            class PieChart extends MDS_Label {
            
            
            
                BufferedImage bfi = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d;
                Arc2D pieArc = new Arc2D.Float(Arc2D.PIE);
            
            
                public PieChart(double total, double used) {
                    g2d = bfi.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    
                    g2d.setColor(Color.green); 
                    g2d.fillOval(0, 0, 145, 145);
                    pieArc.setFrame(0, 0, 145, 145);
                    pieArc.setAngleStart(0);
                    
                    pieArc.setAngleExtent((used / total) * 360);
                    AffineTransform at = AffineTransform.getTranslateInstance(0, 0); 
                    g2d.setColor(Color.blue);
                    g2d.fill(at.createTransformedShape(pieArc));
                    g2d.setColor(Color.black); 
                    //g2d.drawOval(0, 0, 180, 180);
                    //this.setOpaque(true);
                    //this.setBackground(Color.black);
                    this.setBounds(315, 10, 150, 150);
                    this.setHorizontalAlignment(SwingConstants.CENTER);    
                    this.setIcon(new ImageIcon((Image)bfi));                                              
                }
                                
            }    
                       
        }    
       
    }    
        
}    
   