
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;




public final class VirtualThreading {


    private static VirtualThreadingDebugger vtdbg;;
    private static SystemCycleThread sct;
    private static SystemSchedulerThread sst;



    static {
        vtdbg = VirtualThreadingDebugger.getCurrent_VTDBG(); 
        sct = new SystemCycleThread();   
        sst = new SystemSchedulerThread(); 
    }



    private VirtualThreading() {}
    
    
    
    public static void create_SCT_VT(SystemCycleThreadListener l, String name) {
        sct.addSystemCycleThreadListener(l, name);
    }
    
    
    
    public static void terminate_SCT_VT(SystemCycleThreadListener l) {
        sct.removeSystemCycleThreadListener(l);
    }    
    
    
    
    public static void resume_SCT_VT(SystemCycleThreadListener l) {
        sct.resume_VT(l);
    } 
    
    
    
    public static void suspend_SCT_VT(SystemCycleThreadListener l) {
        sct.suspend_VT(l);
    } 
    
    
    
    public static void resume_SCT_VT(String name) {
        sct.resume_VT(name);
    } 
    
    
    
    public static void suspend_SCT_VT(String name) {
        sct.suspend_VT(name);
    } 
    
    
    
    public static SystemCycleThreadListener getCurrent_SCT_VT() {
        return sct.getCurrentVirtualThread();
    } 
    
    
    
    public static boolean is_SCT_VT_Running(SystemCycleThreadListener l) {
        return sct.is_VT_Running(l);
    }
    
    
    
    public static boolean is_SCT_VT_Running(String name) {
        return sct.is_VT_Running(name);
    } 
    
    
    
    public static String getCurrent_SCT_VT_Name() {
        return sct.getCurrentVirtualThreadName();
    }
    
    
    
    public static MDS_Hashtable get_SCT_VT_Info() {
        return sct.getVirualThreadInfo(); 
    }  
    
//====================================================================

    public static void create_SST_VT(SystemSchedulerThreadListener l, String name) {
        sst.addSystemSchedulerThreadListener(l, name);
    }
    
    
    
    public static void terminate_SST_VT(SystemSchedulerThreadListener l) {
        sst.removeSystemSchedulerThreadListener(l);
    }    
    
    
    
    public static void resume_SST_VT(SystemSchedulerThreadListener l) {
        sst.resume_VT(l);
    } 
    
    
    
    public static void suspend_SST_VT(SystemSchedulerThreadListener l) {
        sst.suspend_VT(l);
    } 
    
    
    
    public static void resume_SST_VT(String name) {
        sst.resume_VT(name);
    } 
    
    
    
    public static void suspend_SST_VT(String name) {
        sst.suspend_VT(name);
    } 
    
    
    
    public static SystemSchedulerThreadListener getCurrent_SST_VT() {
        return sst.getCurrentVirtualThread();
    } 
    
    
    
    public static boolean is_SST_VT_Running(SystemSchedulerThreadListener l) {
        return sst.is_VT_Running(l);
    }
    
    
    
    public static boolean is_SST_VT_Running(String name) {
        return sst.is_VT_Running(name);
    } 
    
    
    
    public static String getCurrent_SST_VT_Name() {
        return sst.getCurrentVirtualThreadName();
    }
    
    
    
    public static MDS_Hashtable get_SST_VT_Info() {
        return sst.getVirualThreadInfo();
    }     
    
    
    
    
    
    static final class SystemCycleThread implements Runnable {
    
    
    
        private static EventListenerList listenerList = new EventListenerList();        
        private static Thread thread;
        private static Hashtable last_fire_TimeList = new Hashtable();
        private static MDS_Hashtable vtInfo = new MDS_Hashtable();
        private static SystemCycleThreadListener currentVirtualThread;
        private static Object currentVirtualThreadName;
        private static Vector vctSuspentList = new Vector();
        
        private static volatile boolean goFlag = false;
        
    
    
        public SystemCycleThread() {
            thread  = new Thread(this, "SystemCycle"); 
            thread.start(); 
        }
        
        
        
        public void restart() {
            if(thread != null) {
                thread.interrupt();              
            }         
            thread = null;
            thread = new Thread(this, "SystemCycle"); 
            thread.start(); 

            if(listenerList.getListenerList().length == 0) {
                goFlag = false;
            }    
        }
        
        
        
         public boolean isRunning() {
            if(thread != null) {
                return thread.isAlive();
            } else {
                return false;
            }
        }               
        
        
        
        synchronized public void addSystemCycleThreadListener(SystemCycleThreadListener l, String name) {
            listenerList.add(SystemCycleThreadListener.class, l);
            vtInfo.put(name, l);
            goFlag = true;
            notify();
        }
        
        
        
        public void removeSystemCycleThreadListener(SystemCycleThreadListener l) {
            listenerList.remove(SystemCycleThreadListener.class, l);
            vtInfo.remove(vtInfo.getKey(l));
            if(listenerList.getListenerList().length == 0) {
                goFlag = false;
            }
        }
        
        
        
        public void resume_VT(SystemCycleThreadListener l) {
            vctSuspentList.removeElement(l);
        }         
        
        
        
        public void suspend_VT(SystemCycleThreadListener l) {
            vctSuspentList.addElement(l);
        } 
        
        
        
        public void resume_VT(String name) {
            vctSuspentList.removeElement(vtInfo.getValue(name));
        }         
        
        
        
        public void suspend_VT(String name) {
            vctSuspentList.addElement(vtInfo.getValue(name));
        }        
        
        
        
        public SystemCycleThreadListener getCurrentVirtualThread() {
            return currentVirtualThread;
        } 
        
        
        
        public boolean is_VT_Running(SystemCycleThreadListener l) {
            boolean b = true;
            if(vctSuspentList.contains(l)) {
                b = false;    
            }    
            return b;
        } 
        
        
        
        public boolean is_VT_Running(String name) {
            boolean b = true;
            if(vctSuspentList.contains(vtInfo.getValue(name))) {
                b = false;    
            }    
            return b;            
        }         
        
        
        
        public String getCurrentVirtualThreadName() {            
            return String.valueOf(vtInfo.get(currentVirtualThreadName));
        }
        
        
        
        public MDS_Hashtable getVirualThreadInfo() {
            return vtInfo;
        }       
        
                
        
        public void run() {
        
            try {
            
                long fireTime = 0;
                long interval = 0;
                
                for (;;) {
                    thread.sleep(100); 
                    Object[] listeners = listenerList.getListenerList();
                    synchronized(this) {
                        while(!goFlag) {
                            wait();
                            listeners = listenerList.getListenerList();
                        }
                    }
                    for (int i = listeners.length-2; i>=0; i-=2) {
                        if (listeners[i]==SystemCycleThreadListener.class) {
                            
                            if(!vctSuspentList.contains(listeners[i+1])) {
                                interval = ((SystemCycleThreadListener)listeners[i+1]).getSystemCycle_EventInterval();                           
                            
                                if(last_fire_TimeList.isEmpty() ) {
                                    last_fire_TimeList.put(listeners[i+1], new Long(System.currentTimeMillis() + interval));
                                }
                             
                                if(!last_fire_TimeList.containsKey(listeners[i+1])) {
                                    last_fire_TimeList.put(listeners[i+1], new Long(System.currentTimeMillis() + interval));
                                }

                                fireTime = ((Long)last_fire_TimeList.get(listeners[i+1])).longValue() + interval;
                                if(System.currentTimeMillis() >= fireTime) {                           
                                    currentVirtualThread = (SystemCycleThreadListener)listeners[i+1];
                                    ((SystemCycleThreadListener)listeners[i+1]).autoExecuteSubRoutine();
                                    last_fire_TimeList.put(listeners[i+1], new Long(System.currentTimeMillis()));
                                }
                            }
                        }
                    }                                       
                }                                                   
                         
            }catch(InterruptedException ex) {
                ex.printStackTrace(); 
             	System.out.println("Msi   "+ex.toString());
             	   		
            }catch(Exception ex) {
            	System.out.println("Ms   "+ex.toString());
                vtdbg.fireDebug_SystemCycleThread(this, ex);
            }
        
        }
        
               
    } 
    
    
    
    
    
    static final class SystemSchedulerThread implements Runnable {
    
    
    
        private static EventListenerList listenerList = new EventListenerList();
        private static Thread thread;
        private static Hashtable last_fire_TimeList = new Hashtable();
        private static MDS_Hashtable vtInfo = new MDS_Hashtable();
        private static SystemSchedulerThreadListener currentVirtualThread;
        private static Vector vctSuspentList = new Vector();
         
        private static volatile boolean goFlag = false;
        
        
    
        public SystemSchedulerThread() {
            thread = new Thread(this, "SystemScheduler");
            thread.start();
        }
        
        
        
        public void restart() {
            if(thread != null) {
                thread.interrupt();              
            }
            
            thread = null;
            thread = new Thread(this, "SystemScheduler"); 
            thread.start(); 
            
            if(listenerList.getListenerList().length == 0) {
                goFlag = false;
            }             
                   
        }
        
        
        
        public boolean isRunning() {
            if(thread != null) {
                return thread.isAlive();
            } else {
                return false;
            }
        }
        
        
        
        synchronized public void addSystemSchedulerThreadListener(SystemSchedulerThreadListener l, String name) {
            listenerList.add(SystemSchedulerThreadListener.class, l);
            vtInfo.put(name, l);
            goFlag = true;
            notify();           
        } 
        
        
        
        public void removeSystemSchedulerThreadListener(SystemSchedulerThreadListener l) {
            listenerList.remove(SystemSchedulerThreadListener.class, l);
            vtInfo.remove(vtInfo.getKey(l));
            if(listenerList.getListenerList().length == 0) {
                goFlag = false;
            }            
        }
        
        
        
        public void resume_VT(SystemSchedulerThreadListener l) {
            vctSuspentList.removeElement(l);
        }         
        
        
        
        public void suspend_VT(SystemSchedulerThreadListener l) {
            vctSuspentList.addElement(l);
        } 
        
        
        
        public void resume_VT(String name) {
            vctSuspentList.removeElement(vtInfo.getValue(name));
        }         
        
        
        
        public void suspend_VT(String name) {
            vctSuspentList.addElement(vtInfo.getValue(name));
        }  
        
        
        
        public boolean is_VT_Running(SystemSchedulerThreadListener l) {
            boolean b = true;
            if(vctSuspentList.contains(l)) {
                b = false;    
            }    
            return b;
        } 
        
        
        
        public boolean is_VT_Running(String name) {
            boolean b = true;
            if(vctSuspentList.contains(vtInfo.getValue(name))) {
                b = false;    
            }    
            return b;            
        }                 
        
        
        
        public MDS_Hashtable getVirualThreadInfo() {
            return vtInfo;
        }
        
        
        
        public SystemSchedulerThreadListener getCurrentVirtualThread() {
            return currentVirtualThread;
        }
        
        
        
        public String getCurrentVirtualThreadName() {            
            return String.valueOf(vtInfo.get(currentVirtualThread));
        }        
          
        
        
        public void run() {
            try {
            
                long fireTime = 0;
                long interval = 0;
                
                for (;;) {
                    thread.sleep(100);   
                    Object[] listeners = listenerList.getListenerList();
                    synchronized(this) {
                        while(!goFlag) {
                            wait();
                            listeners = listenerList.getListenerList();
                        }
                    }                    
                    for (int i = listeners.length-2; i>=0; i-=2) {
                        if (listeners[i]==SystemSchedulerThreadListener.class) {
                            
                            if(!vctSuspentList.contains(listeners[i+1])) {
                                interval = ((SystemSchedulerThreadListener)listeners[i+1]).getSystemScheduler_EventInterval();                           
                            
                                if(last_fire_TimeList.isEmpty() ) {
                                    last_fire_TimeList.put(listeners[i+1], new Long(System.currentTimeMillis() + interval));
                                }
                             
                                if(!last_fire_TimeList.containsKey(listeners[i+1])) {
                                    last_fire_TimeList.put(listeners[i+1], new Long(System.currentTimeMillis() + interval));
                                }

                                fireTime = ((Long)last_fire_TimeList.get(listeners[i+1])).longValue() + interval;
                                if(System.currentTimeMillis() >= fireTime) {                           
                                    currentVirtualThread = (SystemSchedulerThreadListener)listeners[i+1];
                                    ((SystemSchedulerThreadListener)listeners[i+1]).systemSchedulerEvent();
                                    last_fire_TimeList.put(listeners[i+1], new Long(System.currentTimeMillis()));
                                }
                            }
                        }
                    }                                       
                }
                
            }catch(InterruptedException ex) {
                ex.printStackTrace();
            }catch(Exception ex) {
                vtdbg.fireDebug_SystemSchedulerThread(this, ex);
            }        
        }
        
        
        
    }
    
    

}