/**
 * @(#)MDS_JarFile.java
 *
 *
 * @author 
 * @version 1.00 2007/10/8
 */

import java.util.*;
import java.util.jar.*;
import java.io.*;
import java.util.zip.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class MDS_JarFile {
	
	
	
	private static FileManager fm = MDS.getFileManager();
	
	

    public MDS_JarFile() {
    	
    }
    
    
    
    public static void creatJarFile(Vector vClassFiles, Manifest mf, File jarFilePath) {
    	new Compress_Multiple(vClassFiles, mf, jarFilePath);  
    }
    
    
    
    
    
    private static class Compress_Multiple extends MDS_Frame implements Runnable ,ActionListener {
    
    
    
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
    
        Vector vctClassFileList = new Vector();
        Manifest mf;
        File jarFilePath;      
                     
        
        
        public Compress_Multiple(Vector vClassFiles, Manifest mf, File jarFilePath) {
        	
            super("Compressing...", false, true); 
            
            this.mf = mf;
            this.jarFilePath = jarFilePath;
            this.vctClassFileList = vClassFiles;                                     
        	
            this.setBounds(20,20,400,220);
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new JLabel("Source :          ");
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
        
            System.out.println("Create Jar File ... ");
            double status;
            
            try {
            	DataOutputStream dosOut = new DataOutputStream(new FileOutputStream(jarFilePath));
                CheckedOutputStream csum = new CheckedOutputStream(dosOut, new Adler32());
                JarOutputStream out = new JarOutputStream(new BufferedOutputStream(csum), mf);
                out.setComment("A test of Java Zipping");
                
                for(Enumeration eClassFile = vctClassFileList.elements(); eClassFile.hasMoreElements();) {
                	File classFile = new File(String.valueOf(eClassFile.nextElement()));
                	totalFileLength = totalFileLength + classFile.length();
                }
                
                for(Enumeration eClassFile = vctClassFileList.elements(); eClassFile.hasMoreElements();) {
                	if(!continueCompression) break;
                	File classFile = new File(String.valueOf(eClassFile.nextElement()));
                    DataInputStream disIn = new DataInputStream(new FileInputStream(classFile));
                    BufferedInputStream bfisIn = new BufferedInputStream(disIn); 
                    
                    ZipEntry ze = null;
                    System.out.println("CJAR Class File   :  "+fm.getFileName(classFile.getPath()));
                    ze = new ZipEntry(fm.getFileName(classFile.getPath()));
                    ze.setSize(classFile.length());
                    
                    out.putNextEntry(ze);
                   
                    int c;
                    
                    while((c = bfisIn.read()) != -1) {
                    	if(!continueCompression) break;
                    	//Thread.sleep(500);
                        out.write(c);
                        currentTotalFileLength = currentTotalFileLength + 1;      
	                    status = (currentTotalFileLength / totalFileLength) * 100;
	                    Double val = new Double(status);
	                    prgsStatus.setValue(Math.abs(val.intValue()));
	                    Double val2 = new Double(currentTotalFileLength);
	                    Double val3 = new Double(totalFileLength);
	                    lblCurrentStatus.setText(MDS.getFileManager().getFormatedFileSize(val2.longValue())+" of "+MDS.getFileManager().getFormatedFileSize(val3.longValue())+" Complete.");                    
                    }     
                    	
                    bfisIn.close();
                    disIn.close();                    	        
                    	               	
                }
                
                out.close(); 
                this.dispose();	
                
            } catch(Exception ex) {
            	ex.printStackTrace();
            }
            
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                continueCompression = false;
                this.dispose();    
            }
        }        
        
        
        
    }    
    
    
}