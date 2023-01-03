/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.event.*;
import java.util.*;



public class MDS_List extends JList {



    public MDS_List() {
        super();
        initialize();
    }
    
    
    
    public MDS_List(ListModel dataModel) {
        super(dataModel);
        initialize();
    }
    
    
    
    public MDS_List(Object[] listData) {
        super(listData);
        initialize();
    }
    
    
    
    public MDS_List(Vector listData) {
        super(listData);
        initialize();
    }
    
    
    
    private void initialize() {
   
    }
    
    
}    