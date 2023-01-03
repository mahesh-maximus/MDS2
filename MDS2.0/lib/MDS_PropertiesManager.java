/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.logging.*;
import javax.swing.event.*;




public final class MDS_PropertiesManager {



    private static MDS_PropertiesManager ppm = new MDS_PropertiesManager();
    //final String REGISTRY_FILE_PATH = "System\\MDS_Registry.rdos";
    private File propertiesFile = new File(MDS.getFileManager().getMDS_System_Dir().getPath(), "system");
    final String PROPERTIES_FILE_PATH = propertiesFile.getPath();
    private Hashtable propertiesTable;
    
    boolean newPropertiesFileCreated = false;
    
    ObjectOutputStream o_Out;
    ObjectInputStream o_In;
    
    Logger log = Logger.getLogger("MDS_Desktop");  
    	
    private EventListenerList listenerList = new EventListenerList();
    	    
    
      
    private MDS_PropertiesManager() {
        try {
        
            File f = new File(PROPERTIES_FILE_PATH);
            
            if(!f.exists()) {
                f.createNewFile(); 
                newPropertiesFileCreated = true; 
                reset();
                //System.out.println(" New File");  
            } else {
            	log.info("Load properties ... ");
                o_In = new ObjectInputStream(new FileInputStream(PROPERTIES_FILE_PATH));
                propertiesTable = (Hashtable)o_In.readObject();
                if(propertiesTable == null) {
                    //regTable = new Hashtable();
                    log.severe("Load properties failed");
                    reset();
                }            
            }
            
            
        } catch(Exception ex) {
            System.out.println("1 "+ex.toString());
            if(ex instanceof InvalidClassException) {
                reset();
            }
        }     
    }
    
    
    
    public void addMDS_PropertyChangeListener(MDS_PropertyChangeListener l) {
    	listenerList.add(MDS_PropertyChangeListener.class, l); 
    }
    
    
    
    public void removeMDS_PropertyChangeListener(MDS_PropertyChangeListener l) {
    	listenerList.remove(MDS_PropertyChangeListener.class, l);
    }
    
    
    private void firePropertyChangeEvent(MDS_PropertyChangeEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==MDS_PropertyChangeListener.class) {
                ((MDS_PropertyChangeListener)listeners[i+1]).propertyChanged(e);                                                                          
            }                       
        }         
    }    
    
    
    
    public static MDS_PropertiesManager getMDS_PropertiesManager() {
        return ppm;
    }
    
    
    
    public void setProperty(MDS_Property prop) {    
//        if(regTable.containsKey(rk.getName())) {
//            throw new RegKeyAlreadyExistsException("Duplicate Keys are not allowed  : "+rk.getName());
//        } else {
//            regTable.put(rk.getName(), rk);
//        }  
        propertiesTable.put(prop.getPropertyName(), prop);
        firePropertyChangeEvent(new MDS_PropertyChangeEvent(prop.getPropertyName(), prop, MDS_PropertyChangeEvent.PROPERTY_SET));	 
        log.info("PropertyName : "+prop.getPropertyName());	    
    }
    
    
    
//    public void amendKey(MDS_Property rk) {
//        regTable.put(rk.getName(), rk);
//    }    
    
    
    
    public MDS_Property getProperty(String key) {
        return (MDS_Property)propertiesTable.get(key);
    }	
    
    
    
    public Vector getPropertyKeys() {
        Enumeration emr = propertiesTable.elements(); 
        Vector vct = new Vector();
        
        while(emr.hasMoreElements()) {
            vct.add(emr.nextElement());
        }
        
        return vct;
    }
    
    
    
    public void store() {
        try {
            if(o_In != null) {
                o_In.close();
            }
                        
            o_Out= new ObjectOutputStream(new FileOutputStream(PROPERTIES_FILE_PATH));
            o_Out.writeObject(propertiesTable);
            o_Out.close();
            
            o_In = new ObjectInputStream(new FileInputStream(PROPERTIES_FILE_PATH));
            propertiesTable = (Hashtable)o_In.readObject();
                        
        } catch(Exception ex) {
            System.err.println(ex.toString());
            //MDS_OptionPane.showMessageDialog(ex.toString(), "MDS Registry", JOptionPane.ERROR_MESSAGE);
        }    
    }
    
    
    
    public void close() {
        try {
            o_In.close();
            o_Out.close();
        } catch(Exception ex) {
        
        }    
    }
    
    
    
    public void reset() {
    	
    	log.warning("Reseting properties ... ");

        propertiesTable = new Hashtable();
 
        MDS_Property propDisplayProperties = new MDS_Property(DisplayProperties.PROPERTY_NAME);
        propDisplayProperties.setSupProperty(DisplayProperties.PROPERTY_WALLPAPER_FILE_PATH, ImageManipulator.MDS_WALLPAPER_PATH+"stelvio.jpg");
        propDisplayProperties.setSupProperty(DisplayProperties.PROPERTY_SCREEN_SAVER_NAME, "RealDream");
        propDisplayProperties.setSupProperty(DisplayProperties.PROPERTY_SCREEN_SAVER_LAUNCH_WAIT_TIME, "1");
        propDisplayProperties.setSupProperty(DisplayProperties.PROPERTY_THEME_NAME, "OceanTheme");
        this.setProperty(propDisplayProperties); 
                                   
        MDS_Property propSoundProperties = new MDS_Property(SoundProperties.PROPERTY_NAME);
        propSoundProperties.setSupProperty(SoundProperties.PROPERTY_GAIN, "32");
        propSoundProperties.setSupProperty(SoundProperties.PROPERTY_PAN, "54");
		propSoundProperties.setSupProperty(SoundProperties.PROPERTY_MUTE, "true");
        this.setProperty(propSoundProperties);  
        	
        MDS_Property propNetworkProperties = new MDS_Property(MDS_Network.PROPERTY_NAME);
        propNetworkProperties.setSupProperty(MDS_Network.PROPERTY_MESSENGER_PORT, "5151");
        propNetworkProperties.setSupProperty(MDS_Network.PROPERTY_BIT_EXCHANGER_PORT, "4343");
		propNetworkProperties.setSupProperty(MDS_Network.PROPERTY_SMTP_SEVER, "myserver.com");
        this.setProperty(propNetworkProperties);         	      	
		                            
        
        this.store();
    }
    
    
    /*
    public static void main(String a[]) {
        MDS_Registry g = new MDS_Registry();
        
        g.reset();
        
        MDS_RegKey rkDisplayProperties = new MDS_RegKey("Sound");
        rkDisplayProperties.addValue("Vol", "43");
        rkDisplayProperties.addValue("Side", "32");
        rkDisplayProperties.addValue("Wave", "32");
        rkDisplayProperties.addValue("Midi", "32");
        g.saveKey(rkDisplayProperties);
        g.update();    
        g.close();     
    }
    */  
    
}    