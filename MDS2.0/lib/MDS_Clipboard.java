/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.io.File;
import javax.swing.event.*;
import java.util.*;


public final class MDS_Clipboard {



    private static MDS_Clipboard cpb = new MDS_Clipboard();

    public static final int STATUS_MOVED = 6546;
    public static final int STATUS_COPIED = 76856;
    public static final int CONTENT_TYPE_FILE = 656;
    public static final int CONTENT_TYPE_STRING = 5675;
    
    private int currentContentType = 0;
    private int currentStatus = 0;
    
    private File currentFile = null;
    private String currentString = null;
    
    private EventListenerList listenerList = new EventListenerList();        
    
   
    
    private MDS_Clipboard() {}
    
    
    
    public static MDS_Clipboard getMDS_Clipboard() {
        return cpb;
    }
    
    
    
    public synchronized void setContent(File f, int status) {
        currentFile = f;    
        currentStatus = status;
        currentContentType = CONTENT_TYPE_FILE;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==ClipBoardListener.class) {
                if(status == STATUS_COPIED) {
                    ((ClipBoardListener)listeners[i+1]).clipBoard_CopyTo();                 
                } else if(status == STATUS_MOVED) {
                    ((ClipBoardListener)listeners[i+1]).clipBoard_MoveTo();  
                }
            }                       
        }        
    }
    
    
    
    public synchronized void setContent(String text, int status) {
        currentString = text;    
        currentStatus = status;
        currentContentType = CONTENT_TYPE_STRING;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==ClipBoardListener.class) {
                if(status == STATUS_COPIED) {
                    ((ClipBoardListener)listeners[i+1]).clipBoard_CopyTo();                 
                } else if(status == STATUS_MOVED) {
                    ((ClipBoardListener)listeners[i+1]).clipBoard_MoveTo();  
                }                 
            }                       
        }         
    }    
    
    
    
    public synchronized int getCurrentContentType() {
        return currentContentType;
    }
    
    
    
    public synchronized int getCurrentStatus() {
        return currentStatus;
    }
    
    
    
    public synchronized boolean isEmpty() {
        boolean b = false;
        if(currentFile == null && currentString == null) {
            b = true;
        }      
        return b;  
    }
    
    
    
    public synchronized void romoveContent() {
        currentFile = null;
        currentString = null;    
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==ClipBoardListener.class) {
                ((ClipBoardListener)listeners[i+1]).clipBoard_Empty();                 
            }                       
        }         
    }
    
    
    
    private void updatePasteListeners() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {        
            if(currentStatus == STATUS_COPIED) {            
                ((ClipBoardListener)listeners[i+1]).clipBoard_Paste_Copied();                 
            } else if(currentStatus == STATUS_MOVED) {
                ((ClipBoardListener)listeners[i+1]).clipBoard_Paste_Moved();  
            } 
        }        
    }
    
    
    
    public void addClipBoardListener(ClipBoardListener l) {
        listenerList.add(ClipBoardListener.class, l);
    }
    
    
    
    public void removeClipBoardListener(ClipBoardListener l) {
        listenerList.remove(ClipBoardListener.class, l);
    }
    
    
    
    public synchronized Object getContent() {
    
        Object content = null;
        
        if(currentFile != null || currentString != null) {
            if(currentContentType == CONTENT_TYPE_FILE) {
                content = currentFile;  
                if(currentStatus == STATUS_MOVED) {
                    currentFile = null;
                    updatePasteListeners();       
                }  
            } else if(currentContentType == CONTENT_TYPE_STRING) {
                content = currentString;
                if(currentStatus == STATUS_MOVED) {                   
                    currentString = null;
                    updatePasteListeners();
                }
            }
        } else {
            throw new NullPointerException("ClipBoard is Empty");    
        }
        
        return content;
        
    }
    
    
    
    public synchronized Object getContent_Summery() {
        Object content = null;   
        if(currentFile != null || currentString != null) {
            if(currentContentType == CONTENT_TYPE_FILE) {
                content = currentFile.getPath();
            } else if(currentContentType == CONTENT_TYPE_STRING) {
                content = currentString;
            }
        } 
        return content;
    }
    
    
    
}    