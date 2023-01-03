/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.io.*;



public class FileSystemEvent {

    
    public static final int FILE_CREATED = 0;
    public static final int FILE_DELETED = 1;
    public static final int FILE_RENAMED = 2;
    
    private int type = -1;
    	
    private File oldFile;
    private File newFile;	 


    
    
    
    public FileSystemEvent(int t, File f) {
        if(t < 0 || t > 2) {
            throw new IllegalArgumentException("Invalid File system event type.");
        } else {
            type = t;
        }       
        this.newFile = f;  
        this.oldFile = f;  
    }
    
    
    
    public FileSystemEvent(int t, File oldFile, File newFile) {
        if(t < 0 || t > 2) {
            throw new IllegalArgumentException("Invalid File system event type.");
        } else {
            type = t;
        }       
        this.newFile = newFile;
        this.oldFile = oldFile; 
    }
    
    
    
    public int getType() {
        return type;
    }
    
    
    
    public File getDirectory() {
    	File d = null;
    	if(oldFile != null) d = oldFile.getParentFile();
    	else if(newFile != null) d = newFile.getParentFile();
    
    	return d;	
    	
    }
    
    
    
    public File getNewFile() throws NullPointerException {
        return newFile;
    }
    
    
    
    public File getOldFile() throws NullPointerException {
        return oldFile;
    }
    
    
    
}    