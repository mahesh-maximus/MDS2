/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.io.*;
import javax.swing.event.*;
import java.util.*;



public final class RedirectedStandardOutput extends OutputStream {


    
    private static RedirectedStandardOutput rso = new RedirectedStandardOutput();
    private EventListenerList listenerList = new EventListenerList();
    private Vector vctHistory_Output = new Vector(); 
    private Vector vctHistory_ErrorOutput = new Vector(); 



    private RedirectedStandardOutput() {  
        System.setOut(new PrintStream(this));
        System.setErr(new PrintStream(new ErrorOutputStream(listenerList)));     
    }
    
    
    
    public static RedirectedStandardOutput getRedirectedStandardOutput() {
        return rso;
    }
    
    
    
    public void addRedirectedStandardOutputListener(RedirectedStandardOutputListener l) {
        listenerList.add(RedirectedStandardOutputListener.class, l);
    }
    
    
    
    public void removeRedirectedStandardOutputListener(RedirectedStandardOutputListener l) {
        listenerList.remove(RedirectedStandardOutputListener.class, l);
    }
    
    
    
    public Vector getOutputHistory() {
        return vctHistory_Output;
    }
    
    
    
    public Vector getErrorOutputHistory() {
        return vctHistory_ErrorOutput;
    }
    
    
    
    public void write(byte[] b) {
        try {
            //super.write(b);
        }catch(Exception ex) {
            
        }
    }  
        
        
        
    public void write(byte[] b, int off, int len) {
        try {
            //super.write(b, off, len);
            
            String out = "";
            for(int x = 0; x < len; x++) {
                out = out.concat(Character.toString((char)b[x]));
            }
            
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2) {                   
                if (listeners[i]==RedirectedStandardOutputListener.class) {
                    ((RedirectedStandardOutputListener)listeners[i+1]).rsdoNewOutputText(out);                                                                          
                }                       
            }            

            if(vctHistory_Output.size()>200) {
                vctHistory_Output.removeElementAt(0);
            } 
                    
            vctHistory_Output.addElement(out);            
            
        }catch(Exception ex) {
            
        }        
    } 
    
    
    
    public void write(int b) {
    
    }
    
    
    
    
    
    class ErrorOutputStream extends OutputStream {
    
    
    
        private EventListenerList listenerList;
    
    
    
        public ErrorOutputStream(EventListenerList ll) {
            listenerList = ll;
        }
        
        
        
        public void write(byte[] b) {
            try {
                //super.write(b);
            }catch(Exception ex) {
            
            }
        }  
        
        
        
        public void write(byte[] b, int off, int len) {
            try {
                //super.write(b, off, len);
                String out = "";
                for(int x = 0; x < len; x++) {
                    out = out.concat(Character.toString((char)b[x]));
                }
                //txtaOut.append(out);
                
                //if(MDS.isDebugMode()) MDS.getMDS_Debugger().print(out);
                 
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {                   
                    if (listeners[i]==RedirectedStandardOutputListener.class) {
                        ((RedirectedStandardOutputListener)listeners[i+1]).rsdoErrorText(out);                 
                    }                                                                               
                }    
                
                if(vctHistory_ErrorOutput.size()>200) {
                    vctHistory_ErrorOutput.removeElementAt(0);
                } 
                    
                vctHistory_ErrorOutput.addElement(out);                         
            
            }catch(Exception ex) {
            
            }        
        } 
    
    
    
        public void write(int b) {

        }        
    
    }       
    
    
    
}    