/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import javax.swing.*;
import javax.swing.table.*;
import java.util.*;



public class MDS_TableModel extends DefaultTableModel {
        
        
        
    public MDS_TableModel() {
        super();
    }
        
         
        
    public MDS_TableModel(Object[][] data, Object[] columns) {
        super(data, columns);
    }
      
    public boolean isCellEditable(int row, int col) {
        return false;
    }
        
    public Class getColumnClass(int column) {
        Vector v = (Vector) dataVector.elementAt(0);
        
        return v.elementAt(column).getClass();
    }
        
        
}