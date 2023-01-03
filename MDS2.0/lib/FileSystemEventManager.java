/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.util.*;
import javax.swing.event.*;
import java.io.*;
import java.util.logging.*;



public final class FileSystemEventManager {


    
    private static FileSystemEventManager fsem = new FileSystemEventManager();
    private EventListenerList listenerList = new EventListenerList();
    private Vector vctListenerList = new Vector();
    
    private Hashtable htThreads = new Hashtable();
    
    private FileManager fm = FileManager.getFileManager();

	private Logger log = Logger.getLogger("FileSystemEventManager");

    private FileSystemEventManager() {}

    
    
    public static FileSystemEventManager getFileSystemEventManager() {
        return fsem;
    }
    
    
    
    public void addFileSystemListener(FileSystemListener l, File dir) {
    	if(!dir.isDirectory()) throw new IllegalArgumentException(dir.getPath()+" should be directory.");
        listenerList.add(FileSystemListener.class, l);
        vctListenerList.addElement(l);
        setChangeNotificationDirectory(l, dir);
    }   
    
    
    
    public void addFileSystemListener(FileSystemListener l) {
        listenerList.add(FileSystemListener.class, l);
        vctListenerList.addElement(l);
    }
    
    
    
    public void removeFileSystemListener(FileSystemListener l) {
        listenerList.remove(FileSystemListener.class, l);
		if(htThreads.containsKey(l)) {
			((NotifyFileSystemListener)htThreads.get(l)).stop();
			htThreads.remove(l);       
		}
        vctListenerList.removeElement(l);
        
        log.info("FileSystemListener Removed.");
        
    }
    
    
    public synchronized void setChangeNotificationDirectory(FileSystemListener l, File dir) {
    	if(vctListenerList.contains(l)) {
	    	if(dir != null) {
		    	if(dir.isDirectory()) {
		    		if(htThreads.containsKey(l)) {
		    			((NotifyFileSystemListener)htThreads.get(l)).stop();
		    			htThreads.remove(l);
		    			htThreads.put(l, new NotifyFileSystemListener(dir));
		    		} else {
		    			htThreads.put(l, new NotifyFileSystemListener(dir));
		    		} 
		    	} else {
		    		throw new IllegalArgumentException(dir.getPath()+" should be directory.");
		    	}
	    	} else {
	    		throw new NullPointerException("Directory cannot be null.");
	    	}
    	} else {
    		throw new IllegalArgumentException("FileSystemListener not found.");
    	}

    }
    
    
    private boolean containsListener(FileSystemListener l) {
		Object[] listeners = listenerList.getListenerList();
        
        for (int i = listeners.length-2; i>=0; i-=2) {  
            if (listeners[i]==FileSystemListener.class) {	
            	if(((FileSystemListener)listeners[i]).equals(l)) {
            		System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");	
					return true;			
            	}
            }       	
        }   
        System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");	
        return false;	 	
    }
    
    
    
    public void fireFileSystemEvent(FileSystemEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==FileSystemListener.class) {
                ((FileSystemListener)listeners[i+1]).fileSystemUpdated(e);                 
            }                       
        }     
    }
    
    
    
    private void _fireFileSystemEvent(FileSystemEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==FileSystemListener.class) {
            	if(htThreads.containsKey(listeners[i+1])) {
            		if(e.getDirectory().equals(((NotifyFileSystemListener)htThreads.get(listeners[i+1])).getDirectory())) {	
                		((FileSystemListener)listeners[i+1]).fileSystemUpdated(e);  
            		}
            	}               
            }                       
        }     
    }    
    
    
    
    private native void registerFileSystemListener(String directory, Object callBack, int id);
    
    
    
    private native void unRegisterFileSystemListener(int id);
    
    
    
    
    
    
    private class NotifyFileSystemListener {
    	
    	
    	File directory;
    	int id = Math.abs(new Random().nextInt());
    	
    	File[] oldDirs;
    	File[] oldFiles;
    
    	
    	
    	public NotifyFileSystemListener(File directory) { 
    		this.directory = directory;
    		this.oldDirs = fm.getContent_Directories(directory.getPath());
    		this.oldFiles = fm.getContent_Files(directory.getPath());
    		registerFileSystemListener(directory.getPath(), this, id);
    	}
    	
    	
    	
    	public int getId() {
    		return id;
    	}
    	
    	
    	
    	public File getDirectory() {
    		return this.directory;
    	}
    	
    	
	    public void stop() {
	    	unRegisterFileSystemListener(id);	 	
	    }    	
    	
    	
    	
    	public synchronized void fireFileSystemEvent(boolean directory) {	
	    		
	    	if(directory) {
				IdentifyFileSystemChangeType(oldDirs, directory);
	    	} else {
	    		IdentifyFileSystemChangeType(oldFiles, directory);	    		
	    	} 
	
    		System.out.println("XXXXXXXXXXXXXXX fireFileSystemEvent : "+Thread.currentThread().getName());
			
    	}
    	
    	
    	
    	public void IdentifyFileSystemChangeType(File[] oldFileDir, boolean directory) {

			File[] fileDir = null;

			if(directory) fileDir = fm.getContent_Directories(this.directory.getPath());
			else fileDir = fm.getContent_Files(this.directory.getPath());
			
    		
    		if(oldFileDir.length != fileDir.length) {
    			if(oldFileDir.length > fileDir.length) { //File removed
    				System.out.println("DIRECTORY REMOVED XPX");
    				for(int x = 0; x < oldFileDir.length; x++) {
    					System.out.println("DIRECTORY REMOVED LOOP");
    					File rf = contains(oldFileDir[x], fileDir);
						//File rf = null;
						if(rf != null) {
							System.out.println("DIRECTORY REMOVED : "+oldFileDir[x].getName());
							if(directory) { 
								_fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.FILE_DELETED, oldFileDir[x]));
								System.out.println("DIRECTORY REMOVED RELOADED D ");
								this.oldDirs = fileDir;
								break;
							} else {
								System.out.println("DIRECTORY REMOVED RELOADED F");
								_fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.FILE_DELETED, oldFileDir[x]));
								this.oldFiles = fileDir;
								break;
							}
						}
    				}
    			} else if(oldFileDir.length < fileDir.length) { // File Added
    				System.out.println("DIRECTORY ADDED");
    				for(int x = 0; x < fileDir.length; x++) {
						File af = contains(fileDir[x], oldFileDir);
						//File af = null;
						if(af != null) {
							System.out.println("DIRECTORY ADDED : "+fileDir[x].getName());
							if(directory) { 
								_fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.FILE_CREATED, fileDir[x]));
								oldDirs = fileDir;
								break;
							} else {
								_fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.FILE_CREATED, fileDir[x]));
								oldFiles = fileDir;
								break;
							}
						}	    			
    				}	
    			}																	
    		} else {
    			//System.out.println("DIRECTORY RENAMED");
    			File ofn = null;
    			File nfn = null;
    			for(int x = 0; x < fileDir.length; x++) {
    				System.out.println("DIRECTORY RENAMED");
    				if(contains_(fileDir[x], oldFileDir) == null) {
    					nfn = fileDir[x];
    					break;		    					
    				}
    			}
    			
    			for(int x = 0; x < oldFileDir.length; x++) {
    				System.out.println("DIRECTORY RENAMED");
    				if(contains_(oldFileDir[x], fileDir) == null) {
    					ofn = oldFileDir[x];	
    					break;
    				}
    			}		    					
    						
    				
    			if(ofn != null) {
					System.out.println("DIRECTORY OLD RENAMED : "+ofn.getName());
					System.out.println("DIRECTORY NEW RENAMED : "+nfn.getName());	
					//oldFileDir = fileDir;	
					if(directory) { 
						_fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.FILE_RENAMED, ofn, nfn));
						oldDirs = fileDir;
					} else {
						_fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.FILE_RENAMED, ofn, nfn));
						oldFiles = fileDir;
					}					
    			}
    			
    		}  	    	
    	}
    	
    	
    	
    	public File contains(File fd, File[] fds) {
    		boolean b = false;
    		for(int x = 0; x < fds.length; x++) {
    			if(!fds[x].equals(fd)) {
    				 b = false;

    			} else {
    				b = true;
    				return null;
    			} 				
    		}
    		if(b)
    			return null;
    		else
    			return fd;		
    	}
    	
    	
    	
    	public File contains_(File fd, File[] fds) {
    		for(int x = 0; x < fds.length; x++) {
    			if(fds[x].equals(fd)) {
    				 return fd;
    			}  				
    		}
    		return null;	
    	}    	
    	
    }
    
    
    
}    