/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.util.*;



public class MDS_Hashtable {



    Vector vctKeys = new Vector();
    Vector vctValues = new Vector();



    public MDS_Hashtable() {
        
    }
    
    
    
    public void put(Object key, Object value) {
        vctKeys.addElement(key);
        vctValues.addElement(value);        
    }
    
    
    
    public Object get(Object key) {
        return vctValues.get(vctKeys.indexOf(key));
    }
    
    
    
    public Object getValue(Object key) {
        return vctValues.get(vctKeys.indexOf(key));
    }    
    
    
    
    public Object getKey(Object val) {
        return vctKeys.get(vctValues.indexOf(val));
    }    
    
    
    public Object getKey(int index) {
        return vctKeys.get(index);
    }
    
    
    
    public Object getValue(int index) {
        return vctValues.get(index);
    } 
    
    
    /*
    public Vector get_Vector(int index) {
        Vector data = new Vector();
        data.addElement(vctKeys.get(index));
        data.addElement(vctValues.get(index));
        return data;
    }   
    
    
    
    public Object[] get_Array(int index) {
        Object[] o = new Object[2];
        o[0] = vctKeys.get(index);
        o[1] = vctValues.get(index);
        return o;
    }
    */
    
    
    public boolean containsKey(Object key) {
        return vctKeys.contains(key);
    }
    
    
    
    public boolean containsValue(Object val) {
        return vctValues.contains(val);
    }    
    
    
    
    public void remove(int index) {
        vctKeys.removeElementAt(index);
        vctValues.removeElementAt(index);
    }
    
    
    
    public void remove(Object key) {
        vctValues.removeElementAt(vctKeys.indexOf(key));
        vctKeys.removeElement(key);
    }    
    
    
    
    public int size() {
        return vctKeys.size(); 
    }
    
    
    
}    