/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import java.io.*;
import java.util.zip.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;



public class MDS_Compression {



    public static int COMPRESS_SINGLE_FILE = 45534534;



    public void compress_Single_GZIP(File in, File out) {
        new Compress_Single(in, out);
    }
    
    
    
    public void compress_Single_GZIP(File in) {    
        new Compress_Single(in, new File(MDS.getFileManager().getFilePathOnly(in.getPath())+"\\"+MDS.getFileManager().getFileName_WithoutExtention(in.getName())+".gzip"));
    }
    
    
    
    public void decompress_Single_GZIP(File in, File out) {    
        new Decompress_Single(in, out);   
    }
    
    
    
    public void decompress_Single_GZIP(File in) {
        File out = new File(MDS.getFileManager().getFilePathOnly(in.getPath())+"\\"+MDS.getFileManager().getFileName_WithoutExtention(in.getName())+".jpg");    
        new Decompress_Single(in, out);
    } 
    
    
    
    public void compress_Multiple_ZIP(File in, File out) {
        new Compress_Multiple(in, out);
    }    
    
    
    
    public void compress_Multiple_ZIP(File in) {
        File out = null; 
    
        if(in.isDirectory()) {
            out = new File(in.getParentFile()+"\\"+in.getName()+".zip");
        } else {
            out = new File(MDS.getFileManager().getFilePathOnly(in.getPath())+"\\"+MDS.getFileManager().getFileName_WithoutExtention(in.getName())+".zip");
        }
        
        new Compress_Multiple(in, out);    
    }
    
    
    
    public void decompress_Multiple_ZIP(File in) {
        new Decompress_Multiple(in);
    }  
    
    
    
    public void showZipEntryProperties(ZipEntry ze) {
        new ZipEntryProperties(ze);
    }      
    
    
    
    
    
    public class Compress_Single extends MDS_Frame implements Runnable, ActionListener {
    
    
        
        int mode = COMPRESS_SINGLE_FILE;
        DataInputStream disIn;
        DataOutputStream dosOut;    
        File inFile;
        File outFile;
        
        JComponent contentPane;
        JProgressBar prgsStatus;
        JLabel lblSource;
        JLabel lblDestination;
        JLabel lblCurrentStatus;
        
        MDS_Button btnCancel;  
        
        Thread thrdCompress = new Thread(this);  
        
        boolean continueCompression = true;
        
        
        
        public Compress_Single(File in, File out) {
            super("Compressing...", false, true);
                                    
            try {
                inFile = in;
                outFile = out;
            }catch(Exception ex) {
                 
            }        
        
            this.setBounds(20,20,400,220);
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new JLabel("Source :          "+inFile.getPath());
            lblSource.setBounds(10,0,370,40);
            contentPane.add(lblSource);

            lblDestination = new JLabel("Destination :   "+outFile.getPath());
            lblDestination.setBounds(10,20,370,40);
            contentPane.add(lblDestination);
                      
            prgsStatus = new JProgressBar(0, 100);
            prgsStatus.setBounds(10,65,370, 20);
            prgsStatus.setStringPainted(true);
            
            lblCurrentStatus = new JLabel("");
            lblCurrentStatus.setBounds(10, 85, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.setBounds(280, 125, 100, 30);
            btnCancel.addActionListener(this);
            contentPane.add(btnCancel);
            contentPane.add(prgsStatus);   
            
            this.setVisible(true);   
            
            thrdCompress.start();              
        }
        
        
        
        public void run() {
        
            double currentLen = 0;
            double status;
            int currentPoint = 0;
            final int point = 1024;
  
            try {
                            
                BufferedReader bfrIn = new BufferedReader(new FileReader(inFile));
                BufferedOutputStream bfoOut = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(outFile)));
                
                DataInputStream disIn = new DataInputStream(new FileInputStream(inFile));
                DataOutputStream dosOut = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(outFile)));
                                
                int off = 0;
                int len = 1024;
                long fLen = inFile.length();
        
                long cfLen = 0;
                long cfLen2 = 0;
                                 	                
                while(disIn.available() != 0) {
            
                    if((fLen - cfLen) <= len) {
                        String slen = String.valueOf(fLen - cfLen); 
                        Integer i = new Integer(slen); 
                        len = i.intValue(); 
                        cfLen2 = cfLen2 + i.intValue();    
                    } else {            
                        cfLen = cfLen+len;
                        cfLen2 = cfLen2 + len;
                    }
            
                    byte b[] = new byte[len];
                   
                    disIn.read(b,off,len);
                    dosOut.write(b,off,len);
                    
                    status = (cfLen2 / fLen) * 100;
                    Double val = new Double(status);
                    prgsStatus.setValue(Math.abs(val.intValue()));               
                    
                    Double val2 = new Double(currentLen);
                    Double val3 = new Double(fLen);
                    lblCurrentStatus.setText(MDS.getFileManager().getFormatedFileSize(val2.longValue())+" of "+MDS.getFileManager().getFormatedFileSize(val3.longValue())+" Complete.");
                  
                }
                         
                disIn.close();
                dosOut.close();                           
                
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.COMPRESS_FILE, outFile)); 
                
                this.dispose();              
                     
            }catch(Exception ex) {
        
            }  
              
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                continueCompression = false;    
            }
        }
        
        
        
    }
    
    
    
    
    
    public class Decompress_Single extends MDS_Frame implements Runnable, ActionListener {
    
    
        
        int mode = COMPRESS_SINGLE_FILE;
        DataInputStream disIn;
        DataOutputStream dosOut;    
        File inFile;
        File outFile;
        
        JComponent contentPane;
        JProgressBar prgsStatus;
        JLabel lblSource;
        JLabel lblDestination;
        JLabel lblCurrentStatus;
        
        MDS_Button btnCancel;  
        
        Thread thrdCompress = new Thread(this);  
        
        boolean continueCompression = true;
    
             
        
        public Decompress_Single(File in, File out) {      
          
            super("Decompressing...", false, true); 
        
            try {
                inFile = in;
                outFile = out;
            }catch(Exception ex) {
                 
            }        
        
            this.setBounds(20,20,400,220);
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new JLabel("Source :          "+inFile.getPath());
            lblSource.setBounds(10,0,370,40);
            contentPane.add(lblSource);

            lblDestination = new JLabel("Destination :   "+outFile.getPath());
            lblDestination.setBounds(10,20,370,40);
            contentPane.add(lblDestination);
                      
            prgsStatus = new JProgressBar(0, 100);
            prgsStatus.setBounds(10,65,370, 20);
            prgsStatus.setStringPainted(true);
            
            lblCurrentStatus = new JLabel("");
            lblCurrentStatus.setBounds(10, 85, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.setBounds(280, 125, 100, 30);
            btnCancel.addActionListener(this);
            contentPane.add(btnCancel);
            contentPane.add(prgsStatus);   
            
            this.setVisible(true);   
            
            thrdCompress.start();        
        }
        
        
        public void run() {
        
            double len = inFile.length();
            double currentLen = 0;
            double status;
            int currentPoint = 0;
            final int point = 1024;

        
            try {
            
                DataInputStream disIn = new DataInputStream(new GZIPInputStream(new FileInputStream(inFile)));
                DataOutputStream dosOut = new DataOutputStream(new FileOutputStream(outFile));  
                
                BufferedInputStream  bfisIn = new BufferedInputStream(disIn);
                BufferedOutputStream bfosOut = new BufferedOutputStream(dosOut);  

                int c;
                while((c = bfisIn.read()) != -1) {
                    bfosOut.write(c);
                    currentLen = currentLen+1;                       
                    currentPoint++;                      
                    if(currentPoint == point) {
                        currentPoint = 0;  
                        status = (currentLen / len) * 100;
                        Double val = new Double(status);
                        prgsStatus.setValue(Math.abs(val.intValue()));
                        Double val2 = new Double(currentLen);
                        lblCurrentStatus.setText(MDS.getFileManager().getFormatedFileSize(val2.longValue())+" Complete.");
                    }                    
                }
                
                bfisIn.close();
                bfosOut.close();
         
                disIn.close();
                dosOut.close();
                
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DECOMPRESS_FILE, outFile));                 
                                                  
                this.dispose();
                       
            }catch(Exception ex) {
        
            }  
              
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                continueCompression = false;    
            }
        }
        
        
        
    }    
    
    
    
    
    
    public class Compress_Multiple extends MDS_Frame implements Runnable ,ActionListener {
    
    
    
        File inFile = null;
        File outFile = null; 
        File content[] = null;      
        
        Thread thrdCompress = new Thread(this); 
        
        JComponent contentPane;
        JProgressBar prgsStatus;
        JLabel lblSource;
        JLabel lblDestination;
        JLabel lblCurrentFile;
        JLabel lblCurrentStatus;
        
        MDS_Button btnCancel;       
        
        double totalFileLength = 0;
        double currentTotalFileLength = 0;
        boolean continueCompression = true;

        Vector vctScrDirList = new Vector();    
        Vector vctScrFileList = new Vector();      
                     
        
        
        public Compress_Multiple(File in, File out) {
        
            super("Compressing...", false, true); 
                    
            inFile = in;
            outFile = out;                             
        
            this.setBounds(20,20,400,220);
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new JLabel("Source :          "+inFile.getPath());
            lblSource.setBounds(10,0,370,40);
            contentPane.add(lblSource);

            lblDestination = new JLabel("Destination :   "+outFile.getPath());
            lblDestination.setBounds(10,20,370,40);
            contentPane.add(lblDestination);
            
            lblCurrentFile = new JLabel("Current File :  ");
            lblCurrentFile.setBounds(10,40,370,40);
            contentPane.add(lblCurrentFile);
                                  
            prgsStatus = new JProgressBar(0, 100);
            prgsStatus.setBounds(10,85,370, 20);
            prgsStatus.setStringPainted(true);
            
            lblCurrentStatus = new JLabel("");
            lblCurrentStatus.setBounds(10, 105, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.setBounds(280, 145, 100, 30);
            btnCancel.addActionListener(this);
            contentPane.add(btnCancel);
            contentPane.add(prgsStatus);   
            
            this.setVisible(true);              
            
            thrdCompress.start();
        }   
        
        
        
        public synchronized void dive(String name) {
            try {
                File file = new File(name);
                if (file != null && file.isDirectory()) {
                    String files[] = file.list();
                    for (int i = 0; i < files.length; i++) {
                        File leafFile = new File(file.getAbsolutePath(), files[i]);
                        if (leafFile.isDirectory()) {
                            // **identify(leafFile);   
                            vctScrDirList.add(leafFile);
                            //if(!stop) {
                                dive(leafFile.getAbsolutePath());
                            //}
                        } else {
                            // **identify(leafFile);
                            totalFileLength = totalFileLength + leafFile.length();
                            vctScrFileList.add(leafFile);
                        }
                    }
                } else if (file != null && file.exists()) {
                    // **identify(file);
                    totalFileLength = totalFileLength + file.length();
                    vctScrFileList.add(file);
                }
            } catch (SecurityException ex) {
                System.out.println(ex.toString()); 
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
            
        }              
        
        
        
        public void run() {
        
            double len = 0;
            double currentLen = 0;
            double status;
            int currentPoint = 0;
            final int point = 1024;
            
            int scrRootLen = inFile.getPath().length();
            
            if(inFile.isDirectory()) {   
                dive(inFile.getPath());   
            } else {
                vctScrFileList.add(inFile);
            }  
                   
            try {
                DataOutputStream dosOut = new DataOutputStream(new FileOutputStream(outFile));
                CheckedOutputStream csum = new CheckedOutputStream(dosOut, new Adler32());
            
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
                out.setComment("A test of Java Zipping");
                
                
                for(int x=0;x<=vctScrFileList.size()-1;x++) {
                    DataInputStream disIn = new DataInputStream(new FileInputStream((File)vctScrFileList.elementAt(x)));
                    BufferedInputStream bfisIn = new BufferedInputStream(disIn);
                    
                    ZipEntry ze = null;
                    
                    if(inFile.isDirectory()) {
                        int beginIndex = scrRootLen;
                        int endIndex = ((File)vctScrFileList.elementAt(x)).getPath().length();
                        System.out.println("LAS    = "+((File)vctScrFileList.elementAt(x)).getPath().substring(beginIndex+1, endIndex));
                        ze = new ZipEntry(((File)vctScrFileList.elementAt(x)).getPath().substring(beginIndex, endIndex));
                        out.putNextEntry(ze);
                    } else {
                        ze = new ZipEntry(((File)vctScrFileList.elementAt(x)).getName());
                        ze.setSize(((File)vctScrFileList.elementAt(x)).length());
                        
                        out.putNextEntry(ze);
                    
                    }   
                     
                    lblCurrentFile.setText("Current File   : "+((File)vctScrFileList.elementAt(x)).getName());
                    int c;
                    
                    while((c = bfisIn.read()) != -1) {
                        out.write(c);
                        currentTotalFileLength = currentTotalFileLength + 1;                       
                        currentPoint++;                      
                        if(currentPoint == point) {
                            currentPoint = 0;  
                            status = (currentTotalFileLength / totalFileLength) * 100;
                            Double val = new Double(status);
                            prgsStatus.setValue(Math.abs(val.intValue()));
                            Double val2 = new Double(currentTotalFileLength);
                            Double val3 = new Double(totalFileLength);
                            lblCurrentStatus.setText(MDS.getFileManager().getFormatedFileSize(val2.longValue())+" of "+MDS.getFileManager().getFormatedFileSize(val3.longValue())+" Complete.");
                        }                       
                    }
                        
                    bfisIn.close();
                    disIn.close();
                    //i++;
                }
                             
                out.close();  
                
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.COMPRESS_FILE, outFile)); 
                this.dispose();   
                                             
            } catch(Exception ex) {
                
            }               
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                //continueCompression = false;    
            }
        }        
        
        
        
    }    
    
    
    
    
    
    public class Decompress_Multiple extends MDS_Frame implements Runnable, ActionListener {
    
    
    
        File inFile = null;
        Thread thrdCompress = new Thread(this); 
        
        JComponent contentPane;
        JProgressBar prgsStatus;
        JLabel lblSource;
        JLabel lblDestination;
        JLabel lblCurrentFile;
        JLabel lblCurrentStatus;
        
        MDS_Button btnCancel;         
        
        double totalFileLength = 0;
        double currentTotalFileLength = 0;
        boolean continueCompression = true;   
                
        
        
        public Decompress_Multiple(File in) {
        
            super("Decompressing...", false, true); 
                    
            inFile = in;
                   
        
            this.setBounds(20,20,400,220);
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new JLabel("Source :          "+inFile.getPath());
            lblSource.setBounds(10,0,370,40);
            contentPane.add(lblSource);

            lblDestination = new JLabel("Destination :   ");
            lblDestination.setBounds(10,20,370,40);
            contentPane.add(lblDestination);
            
            lblCurrentFile = new JLabel("Current File :  ");
            lblCurrentFile.setBounds(10,40,370,40);
            contentPane.add(lblCurrentFile);
                                  
            prgsStatus = new JProgressBar(0, 100);
            prgsStatus.setBounds(10,85,370, 20);
            prgsStatus.setStringPainted(true);
            
            lblCurrentStatus = new JLabel("");
            lblCurrentStatus.setBounds(10, 105, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.setBounds(280, 145, 100, 30);
            btnCancel.addActionListener(this);
            contentPane.add(btnCancel);
            contentPane.add(prgsStatus);   
            
            this.setVisible(true);              
            
            thrdCompress.start();        
        }    
        
        
        
        public void run() {
        
            double currentLength = 0;
            double status =0;
            int currentPoint = 0;
            final int point = 1024;
            double totalLength = 0;
            
            File rootDir = new File(MDS.getFileManager().getFilePathOnly(inFile.getPath())+"\\"+MDS.getFileManager().getFileName_WithoutExtention(inFile.getName()));
;
            rootDir.mkdir();
                   
            try {
            
                DataInputStream disIn = new DataInputStream(new FileInputStream(inFile));                
                CheckedInputStream csumi = new CheckedInputStream(disIn, new Adler32());            
                
                ZipInputStream zisIn = new ZipInputStream(new BufferedInputStream(csumi));  
                
                ZipEntry ze;
                        
                while((ze = zisIn.getNextEntry()) != null && continueCompression) { 
                    totalLength = totalLength + ze.getSize(); 
                }
                
                if(totalLength <= -1) {
                    totalLength = inFile.length();
                }
                
                zisIn.close();                    
                csumi.close();
                disIn.close();                       
            
                DataInputStream disIn1 = new DataInputStream(new FileInputStream(inFile));                
                CheckedInputStream csumi1 = new CheckedInputStream(disIn1, new Adler32());            
                
                ZipInputStream zisIn1 = new ZipInputStream(new BufferedInputStream(csumi1));  
                        
                while((ze = zisIn1.getNextEntry()) != null && continueCompression) {  
                    File f = new File(ze.getName());
                                      
                    if(f.getParent() != null) {
                        File f2 = new File(rootDir.getPath()+f.getParentFile());
                        f2.mkdirs();
                    }                    
                    
                    DataOutputStream dosOut = null;
                    
                    if((ze.getName()).startsWith("\\")) {
                        dosOut = new DataOutputStream(new FileOutputStream(new File(rootDir.getPath()+ze.getName())));   
                    } else {
                        dosOut = new DataOutputStream(new FileOutputStream(new File(rootDir.getPath()+"\\"+ze.getName())));                       
                    }
                    
                    BufferedOutputStream bfosOut = new BufferedOutputStream(dosOut);
                                 
                    int x;
                    while((x = zisIn1.read()) != -1 && continueCompression) {
                        bfosOut.write(x);
                        currentLength++;
                        currentPoint++;                      
                        if(currentPoint == point) {
                            currentPoint = 0;  
                            status = (currentLength / totalLength) * 100;
                            Double val = new Double(status);
                            prgsStatus.setValue(Math.abs(val.intValue()));
                            Double val2 = new Double(currentLength);
                            Double val3 = new Double(totalLength);
                            lblCurrentStatus.setText(MDS.getFileManager().getFormatedFileSize(val2.longValue())+" of "+MDS.getFileManager().getFormatedFileSize(val3.longValue())+" Complete.");                            
                        }                            
                    }
                    
                    bfosOut.close();                    
                    dosOut.close();                  
                }
                
                zisIn1.close();                    
                csumi1.close();
                disIn1.close();
                
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DECOMPRESS_FILE, rootDir)); 
                this.dispose();                  
                                             
            } catch(Exception ex) {
                
            }        
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                continueCompression = false;    
            }
        }         
        
        
        
    } 
    
    
    
    
    
    class ZipEntryProperties extends ControlModule implements ActionListener {
    
    
    
        ZipEntry ze;
        File f;
        MDS_Panel panel = new MDS_Panel(new BorderLayout());
        
        MDS_TableModel tmdlZE = new MDS_TableModel();
        MDS_Table tblZE = new MDS_Table(tmdlZE);        
    
    
        public ZipEntryProperties(ZipEntry z) {
            super("Compressed Item Properties");
            ze = z;
            f = new File(ze.getName());
            
            panel.add(new MDS_Label("  "), BorderLayout.NORTH);
            
            tmdlZE.addColumn("1");
            tmdlZE.addColumn("2");
            tblZE.setTableHeader(null);
            tmdlZE.addRow(new String[] {"Name", f.getName()}); 
            tmdlZE.addRow(new String[] {"Type", MDS.getFileManager().getFileType(ze.getName())});
            tmdlZE.addRow(new String[] {"Location", f.getParent()});
            tmdlZE.addRow(new String[] {"Size", MDS.getFileManager().getFormatedFileSize(ze.getSize())});
            tmdlZE.addRow(new String[] {"Date", MDS.getFileManager().getLastModified_As_String(ze.getTime())});
            
            String compression  = "";
            
            if(ze.getMethod() == ze.DEFLATED) {
                compression = "Deflated";    
            } else if(ze.getMethod() == ze.STORED) {
                compression = "Stored (Store only)";
            } else {
                compression = "N/A";
            }
            
            tmdlZE.addRow(new String[] {"Compression", compression});
            tmdlZE.addRow(new String[] {"Compressed Size", MDS.getFileManager().getFormatedFileSize(ze.getCompressedSize())});

            panel.add(new JScrollPane(tblZE), BorderLayout.CENTER);
            
            this.addPanel(panel);            
            this.setSize(400, 400);
            this.setCenterScreen();
            this.setVisible(true);                        
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Ok")) {
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                this.dispose();
            }
        }        
    
    }   
    
    
}    
    
    