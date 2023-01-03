/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;
import java.awt.*;



public class MDS_ListCellRenderer extends JLabel implements ListCellRenderer {



    MDS_ComboBoxModel model;
    


    public MDS_ListCellRenderer() {
        setOpaque(true);
    }
    
    
    public Component getListCellRendererComponent(JList jlist, Object obj, int index, boolean isSelected,boolean focus) {
        //MDS_ComboBoxModel model = (MDS_ComboBoxModel)jlist.getModel();
		
		setIcon((Icon)((Object[])obj)[1]);
        setText((String)((Object[])obj)[0]);
        

        if(!isSelected) {
            setBackground(jlist.getBackground());
            setForeground(jlist.getForeground());
        }
        else {
            setBackground(jlist.getSelectionBackground());
            setForeground(jlist.getSelectionForeground());
        }

        return this;
    }
    
}