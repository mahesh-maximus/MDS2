/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;



public class MDS_ListModel extends DefaultListModel {



    public MDS_ListModel() {}
    
    
    
    public void addElement(ImageIcon i, String text) {
        addElement(new Object[]{text, i});
    
    }
    
    
    
    public void setElementAt(ImageIcon i, String text, int row) {
        setElementAt(new Object[]{text, i}, row);
    }
    
    
    
}  