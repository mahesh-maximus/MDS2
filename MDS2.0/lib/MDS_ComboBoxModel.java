/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;



public class MDS_ComboBoxModel extends DefaultComboBoxModel {



    public MDS_ComboBoxModel() {}
    
    
    
    public void addElement(ImageIcon i, String text) {
        addElement(new Object[]{text, i});
    
    }
    
    
    
}    