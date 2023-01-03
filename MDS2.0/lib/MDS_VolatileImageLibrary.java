/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.logging.*;


public final class MDS_VolatileImageLibrary {

	
    
    private static MDS_VolatileImageLibrary vil = new MDS_VolatileImageLibrary(); 
    private FileManager fm = MDS.getFileManager();
    private Hashtable htImages_48x48 = new Hashtable();
    private Hashtable htImages_24x24 = new Hashtable();

	public final static int ICON_SIZE_48x48 = 1;
	public final static int ICON_SIZE_24x24 = 2;
	
	private Logger log = Logger.getLogger("MDS_VolatileImageLibrary");
	
	

    private MDS_VolatileImageLibrary() { 
    	log.info("setup48x48Icons");   
    	setup48x48Icons();
		log.info("setup24x24_Icons"); 
    	setup24x24_Icons();
    	
    }
    
    private void setup48x48Icons() {
        Vector vctFileExtentions = fm.getSupportedFileType_Extentions();
        
        //htImages.put("Directory",fm.getDefault_Directory_Icon());
		htImages_48x48.put("Directory",fm.getDefault_Directory_Icon_Large());
        
        htImages_48x48.put("Default", fm.getDefault_FileType_Icon());
        
        //htImages_48x48.put("MDS_Executable", getDefined_Size_Icon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-exec.png")));
        htImages_48x48.put("MDS_ExecutableClass",ImageManipulator.createScaledImageIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"64-filesys-exec.png"),48,48,ImageManipulator.ICON_SCALE_TYPE));
        
        htImages_48x48.put("MDS_ExecutableJar", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-package_system.png"));
        
        for(int x=0;x<=vctFileExtentions.size()-1;x++) {
            File f = new File("Test."+String.valueOf(vctFileExtentions.elementAt(x)));
            htImages_48x48.put(vctFileExtentions.elementAt(x), fm.getFileType_Icon(f));    
        }         
    }
    
    private void setup24x24_Icons() {
        Vector vctFileExtentions = fm.getSupportedFileType_Extentions();
        
        //htImages.put("Directory",fm.getDefault_Directory_Icon());
		htImages_24x24.put("Directory",ImageManipulator.createScaledImageIcon(fm.getDefault_Directory_Icon_Large(),24,24,ImageManipulator.ICON_SCALE_TYPE));
        
        htImages_24x24.put("Default",ImageManipulator.createScaledImageIcon(fm.getDefault_FileType_Icon(),24,24,ImageManipulator.ICON_SCALE_TYPE));
        
        //htImages_24x24.put("MDS_Executable", getDefined_Size_Icon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-exec.png")));
        htImages_24x24.put("MDS_ExecutableClass",ImageManipulator.createScaledImageIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-exec.png"),24,24,ImageManipulator.ICON_SCALE_TYPE));
        
        htImages_24x24.put("MDS_ExecutableJar",ImageManipulator.createScaledImageIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-package_system.png"),24,24,ImageManipulator.ICON_SCALE_TYPE));
        
        for(int x=0;x<=vctFileExtentions.size()-1;x++) {
            File f = new File("Test."+String.valueOf(vctFileExtentions.elementAt(x)));
            htImages_24x24.put(vctFileExtentions.elementAt(x), ImageManipulator.createScaledImageIcon(fm.getFileType_Icon(f),24 ,24 ,ImageManipulator.ICON_SCALE_TYPE));    
        }         
    }
    
    
    
    public static MDS_VolatileImageLibrary getMDS_VolatileImageLibrary() {
        return vil;
    }
    
    
    
    private ImageIcon getDefined_Size_Icon(ImageIcon i) {
        return ImageManipulator.createScaledImageIcon(i,31 ,35 ,ImageManipulator.ICON_SCALE_TYPE);
    }
    
    
    
    
    
    
    public ImageIcon getDefaultIcon_For_File(File f, int iconSize) {
    
        ImageIcon i = null;
        
        if(f.isDirectory()) {
        	if(iconSize == ICON_SIZE_48x48) { 
            	i = (ImageIcon)htImages_48x48.get("Directory");
        	} else if(iconSize == ICON_SIZE_24x24) {
        		i = (ImageIcon)htImages_24x24.get("Directory");
        	}
        } else {  
            if(f.exists()) {
                if(f.getName().endsWith(".class")) {
                    if(f.getName().indexOf("$") < 0) {
	                    if(MDS.getProcessManager().isMDS_Executable(f)) {
	                        //i = (ImageIcon)htImages.get("MDS_Executable");
				        	if(iconSize == ICON_SIZE_48x48) { 
				            	i = (ImageIcon)htImages_48x48.get("MDS_ExecutableClass");
				        	} else if(iconSize == ICON_SIZE_24x24) {
				        		i = (ImageIcon)htImages_24x24.get("MDS_ExecutableClass");
				        	}	                        
	                    } else {
	                        i = getDefaultIcon(f, iconSize);
	                    }
	                } else {
	                    i = getDefaultIcon(f, iconSize);    
	                }
	            } else {
	            	if(f.getName().endsWith(".jar")) {
	                    if(MDS.getProcessManager().isMDS_Executable(f)) {
				        	if(iconSize == ICON_SIZE_48x48) { 
				            	i = (ImageIcon)htImages_48x48.get("MDS_ExecutableJar");
				        	} else if(iconSize == ICON_SIZE_24x24) {
				        		i = (ImageIcon)htImages_24x24.get("MDS_ExecutableJar");
				        	}	                        
	                    } else {
	                        i = getDefaultIcon(f, iconSize);
	                    }	                	
	            	} else {
	            		i = getDefaultIcon(f, iconSize);
	            	}
	            }
	        } else {
	            i = getDefaultIcon(f, iconSize);
	        }
        }    
        
        return i;
        
    }
    
    
    
    private ImageIcon getDefaultIcon(File f, int iconSize) {
        ImageIcon i = null;
        if(iconSize == ICON_SIZE_48x48) { 
	        if(htImages_48x48.containsKey(fm.getFileExtension(f.getName()))) {
	            i = (ImageIcon)htImages_48x48.get(fm.getFileExtension(f.getName()));
	        } else {
	            i = (ImageIcon)htImages_48x48.get("Default");
	        }    
        } else if(iconSize == ICON_SIZE_24x24) {
	        if(htImages_24x24.containsKey(fm.getFileExtension(f.getName()))) {
	            i = (ImageIcon)htImages_24x24.get(fm.getFileExtension(f.getName()));
	        } else {
	            i = (ImageIcon)htImages_24x24.get("Default");
	        }          
        }     
        return i;
    }
    
    
    
}    