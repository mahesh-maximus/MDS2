

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.jar.*;
import java.util.zip.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;
import java.lang.reflect.*;




public class JarFileContent extends MDS_Table implements ActionListener, MouseListener, Runnable {



    MDS_User usr = MDS.getUser();
   
    MDS_TableModel tmdlZip = new MDS_TableModel();
    
    MDS_PopupMenu popup = new MDS_PopupMenu("JFV");
    
    File jFile;
    File tempFile;
    JarFile jf;
    
    Vector vctZE = new Vector();
    ZipEntry currentZE;
    
    Thread thrdDecompress;
    
    boolean tempDecompressing = true;   
    String decompressDir = "";
    
    MDS_Frame frmParent;
    MDS_Frame frmLoading;
    MDS_Label lblText = new MDS_Label();
         


    public JarFileContent() {
        super();
        this.setModel(tmdlZip);
        this.addMouseListener(this);
        
        tmdlZip.addColumn("Name");
        tmdlZip.addColumn("Type");
        tmdlZip.addColumn("Size");
        tmdlZip.addColumn("Packed");         
        tmdlZip.addColumn("Modified");
        
        this.getColumn("Name").setMinWidth(300);       
        this.getColumn("Size").setMinWidth(80);
        this.getColumn("Size").setMaxWidth(80);
        this.getColumn("Packed").setMinWidth(80);
        this.getColumn("Packed").setMaxWidth(80);
        this.setRowHeight(54);
                
        popup.add(usr.createMenuItem("Open", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"open.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this));
        popup.addSeparator();
        popup.add(usr.createMenuItem("Decompress to", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds-zip.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this));
        popup.addSeparator();
        popup.add(usr.createMenuItem("Properties", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"file-properties.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this));
   
    }
    
    
    
    public JarFileContent(MDS_Frame f) {
        this();
        frmParent = f;
    }
    
    
    
    public void loadJarFileInfo(File f) {
    
        Vector v = tmdlZip.getDataVector();
        v.removeAllElements();    
        this.clearSelection(); 
        this.validate();    
        
        vctZE.removeAllElements();
      
        try {       
        
            jFile = f;            
            jf = new JarFile(f);         
            Enumeration enu = jf.entries();
            while(enu.hasMoreElements()) {
                ZipEntry ze = (ZipEntry)enu.nextElement();
                if(!ze.isDirectory()) {
                    Vector vct = new Vector();
                    vct.addElement(ze.getName());
                    vct.addElement(MDS.getFileManager().getFileType(ze.getName()));
                    vct.addElement(MDS.getFileManager().getFormatedFileSize(ze.getSize()));
                    vct.addElement(MDS.getFileManager().getFormatedFileSize(ze.getCompressedSize()));
                    vct.addElement(MDS.getFileManager().getLastModified_As_String(ze.getTime()));
                    tmdlZip.addRow(vct);
                    vctZE.addElement(ze);
                    
                    DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
	                    public void setValue(Object value) {
	                        String text = String.valueOf(value);
	                        this.setText(text);
	                        this.setToolTipText(text);
	                        /*
	                        if(!jFile.getName().equals("mds_classes.jar")) {
	                            this.setIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(new File("e:\\MDS\\"+text)));
	                            //this.setText(text);
	                            //this.setToolTipText(text);	  
	                        } else {
                                  try {
                                      if(MDS.getFileManager().getFileExtension(text).equals("class")) {
                                          Class cls = Class.forName(MDS.getFileManager().getFileName_WithoutExtention(text));
                                          //String args[] = {"1#"};    
                                          String args[] = new String[] {};
                                          Method m = cls.getMethod("MDS_Main", new Class[] { args.getClass() });
	                                    this.setIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds-executable.png",31 ,35 ,ImageManipulator.ICON_SCALE_TYPE));
	                                    //this.setText(text);
	                                    //this.setToolTipText(text);                                   
	                                } else {*/
	                                    this.setIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(new File(System.getProperty("user.dir")+"\\"+text), MDS_VolatileImageLibrary.ICON_SIZE_48x48));	                                    
	                                //}   
                                  //} catch(Exception ex) {
                                      /*if(ex instanceof NoSuchMethodException) {
                       	                  this.setIcon(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(new File(System.getProperty("user.dir")+"\\"+text)));	                                                           
                                      } else {
                      
                                      }*/
                                  //}	                                
	                        //}                      
	                    }
	                };                        
	                
                    TableColumn colorColumn = this.getColumn("Name");    
                    colorColumn.setCellRenderer(colorRenderer); 	                
	            }       
            }    
        } catch(Exception ex) {
        	throw new RuntimeException(ex);
            //MDS.getExceptionManager().showException(ex);
        }
    }
    
    
    
    public void openJarFile(MDS_Frame frm) {
        MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
        Vector v = new Vector();
        v.add("jar");
        fmfc.setFilter(v);
        if(fmfc.showDiaog(frm) ==  fmfc.APPROVE_OPTION) { 
            tempDecompressing = true;     
            loadJarFileInfo(new File(fmfc.getPath()));    
        }      
    }
    
    
    
    private void openZipEntry(int y) {
        currentZE = (ZipEntry)vctZE.elementAt(this.getRowForLocation(y));
        tempDecompressing = true; 
        thrdDecompress = new Thread(this);
        thrdDecompress.start();       
    }
    
    
    
    public void run() {
    
        DataOutputStream dosOut = null;      
          
        try {
        
            lblText.setText("Decompressing "+currentZE.getName());
            lblText.setHorizontalAlignment(SwingConstants.CENTER);
            frmLoading = MDS_OptionPane.getEmptyMessageFrame(frmParent, lblText, "Jar File Viewer", new Dimension(250,100));          
            
            File f = new File(currentZE.getName());
            DataInputStream disIn = new DataInputStream(jf.getInputStream(currentZE));     

            Random rand = new Random();
            if(tempFile != null) {
                if(tempFile.exists()) tempFile.delete();
            }
            
            if(tempDecompressing) {
                tempFile = MDS.getFileManager().createTempFile("JFV_"+String.valueOf(Math.abs(rand.nextInt())), "."+MDS.getFileManager().getFileExtension(f.getName()));
                dosOut = new DataOutputStream(new FileOutputStream(tempFile));
            } else {
                tempFile = new File(decompressDir + f.getName());
                dosOut = new DataOutputStream(new FileOutputStream(tempFile));                
            }
                        
            boolean b = true;
            while(b) {               
                
                dosOut.writeChar(disIn.readChar());

            }
            
            dosOut.close();
            
            frmLoading.dispose();
            
            if(tempDecompressing) MDS.getFileManager().executeFile(tempFile.getPath());            
        
        } catch(Exception ex) {
            if(ex instanceof EOFException) {    
                try {
                    dosOut.close();
                    if(tempDecompressing) MDS.getFileManager().executeFile(tempFile.getPath());
                } catch(Exception ex2) {}       
            } else {
                ex.printStackTrace();
            }
            
            if(frmLoading != null) frmLoading.dispose();
               
        } finally {
////            MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DECOMPRESS_FILE, tempFile));
        }                
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Decompress to")) {
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(frmParent) ==  fmdc.APPROVE_OPTION) {
                decompressDir = fmdc.getPath();
                tempDecompressing = false;
                currentZE = (ZipEntry)vctZE.elementAt(this.getSelectedRow());
                thrdDecompress = new Thread(this);
                thrdDecompress.start();                 
            }            
        } else if(e.getActionCommand().equals("Open")) {     
            currentZE = (ZipEntry)vctZE.elementAt(this.getSelectedRow());
            if(currentZE.getName().endsWith(".class")) {
                MDS_OptionPane.showMessageDialog(frmParent, "Unable to open class files.", "Jar File Content", MDS_OptionPane.INFORMATION_MESSAGE);
                return;
            }  
            tempDecompressing = true;               
            thrdDecompress = new Thread(this);
            thrdDecompress.start();         
        } else if(e.getActionCommand().equals("Properties")) {
            MDS_Compression com = new MDS_Compression();
            com.showZipEntryProperties((ZipEntry)vctZE.elementAt(this.getSelectedRow()));
        }
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){}



    public void mouseExited(MouseEvent e){}



    public void mousePressed(MouseEvent e) {
        
        this.setRowSelectionInterval(this.getRowForLocation(e.getY()), this.getRowForLocation(e.getY()));
        
        if(e.getClickCount() == 2) {
            String zeName = String.valueOf(this.getValueAt(this.getSelectedRow() , 0));
            if(zeName.endsWith(".class")) {
                MDS_OptionPane.showMessageDialog(frmParent, "Unable to open class files.", "Jar File Content", MDS_OptionPane.INFORMATION_MESSAGE);
                return;
            } 
             
            openZipEntry(e.getY()); 
            	
                           
        } else if(e.getClickCount() == 1) {
            if(e.getButton() == e.BUTTON3) {
                popup.show(this, e.getX(), e.getY());
            }
        }    
                
    }
        
        
        
    public void mouseReleased(MouseEvent e) {}  
    
    
    
}    