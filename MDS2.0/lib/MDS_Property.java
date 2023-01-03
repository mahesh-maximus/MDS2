/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.util.*;
import java.io.*;



public class MDS_Property implements Serializable {



    Hashtable supPropertyValues = new Hashtable();
    String propertyName = "";
    


    public MDS_Property(String propertyName) {
        this.propertyName = propertyName;
    }
    
    
    
    public String getPropertyName() {
        return propertyName;
    }
    
    
    
    public void setSupProperty(String key, String value) {
        supPropertyValues.put(key, value);
    }
    
    
    
    public String getSupProperty(String key) {
        return (String)supPropertyValues.get(key);
    }
    
    
   
    public Vector getSupPropertyKeys() {
    
        Enumeration emr = supPropertyValues.keys(); 
        Vector vct = new Vector();
        
        while(emr.hasMoreElements()) {
            vct.add(emr.nextElement());
        }
        
        return vct;    
    }
    
    
    
    
}    