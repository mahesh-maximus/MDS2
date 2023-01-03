/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.util.logging.*;


public class MDS_PropertiesViewer extends MDS_Frame implements TreeSelectionListener, ActionListener, MDS_PropertyChangeListener {



    MDS_User usr = MDS.getUser();
    
    JComponent contentPane;
    JMenuBar mnbRegistryEditor = new JMenuBar();
    JMenu mnuFile = new JMenu("File");
    JMenuItem mniReset = usr.createMenuItem("Reset", this);
    JMenuItem mniClose = usr.createMenuItem("Close", this);
    JMenu mnuEdit = new JMenu("Edit");
    JMenuItem mniValue = usr.createMenuItem("Value", this);
    JMenuItem mniRefresh = usr.createMenuItem("Refresh", this);    
    JMenu mnuHelp = new JMenu("Help");
    JMenuItem mniAbout = usr.createMenuItem("About", this);
    
    JSplitPane sltp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);     

    MDS_Tree treKeys;
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("MDS Properties");
    DefaultTreeModel dtmdl = new DefaultTreeModel(root);
    MDS_TableModel tbmdl = new MDS_TableModel();
    MDS_Table tblValues;
    DefaultListSelectionModel dlsmdl;
    
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    Vector propertiesTable;
    
    MDS_Property currentProperty = null;
    
    Logger log = Logger.getLogger("MDS_PropertiesViewer");



    public MDS_PropertiesViewer() {
        super("Registry Editor",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-taskbar.png"));  
        contentPane = (JComponent) this.getContentPane();
         
        contentPane.setLayout(new BorderLayout());
        treKeys = new MDS_Tree(dtmdl);
        treKeys.addTreeSelectionListener(this);
        MDS_TreeCellRenderer tcRenderer = new MDS_TreeCellRenderer();
        tcRenderer.setClosedIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-fileopen.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        tcRenderer.setOpenIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-fileopen.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        tcRenderer.setLeafIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-fileopen.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        treKeys.setCellRenderer(tcRenderer);        
        sltp.setLeftComponent(new JScrollPane(treKeys));
        
        tbmdl.addColumn("Key");
        tbmdl.addColumn("Value");
        tblValues = new MDS_Table(tbmdl);
        dlsmdl = (DefaultListSelectionModel)tblValues.getSelectionModel(); 
        tblValues.setSelectionModel(dlsmdl); 
        tblValues.setRowSelectionAllowed(true);       
        sltp.setRightComponent(new JScrollPane(tblValues));
        contentPane.add(sltp, BorderLayout.CENTER);
        mnuFile.add(mniReset);
        mnuFile.add(mniClose);
        mnbRegistryEditor.add(mnuFile);
        mnuEdit.add(mniValue);
        mnuEdit.add(mniRefresh);
        mnbRegistryEditor.add(mnuEdit);
        mnuHelp.add(mniAbout);
        mnbRegistryEditor.add(mnuHelp);
        
        
        ppm.addMDS_PropertyChangeListener(this);
        
        this.setJMenuBar(mnbRegistryEditor);       
        this.setBounds(0, 0, 800, 600);
        this.setVisible(true);
        refresh_Tree();  
    }
    
    
    
    private void refresh_Tree() {
        propertiesTable = ppm.getPropertyKeys(); 
        
        for(int x=0;x<=propertiesTable.size()-1;x++) {
            DefaultMutableTreeNode  dmtn = new DefaultMutableTreeNode(((MDS_Property)propertiesTable.elementAt(x)).getPropertyName());
            root.add(dmtn);            
        }
        
        treKeys.expandRow(0);
               
    }
    
    
    
    private void set_Table_Content(MDS_Property prop) {
    
        Vector v = tbmdl.getDataVector();
        v.removeAllElements();    
        tblValues.clearSelection();
        
        Vector vctSupPropertyKeys = prop.getSupPropertyKeys();
        
        for(int x=0;x<=vctSupPropertyKeys.size()-1;x++) {
            Vector data = new Vector();
            data.addElement(vctSupPropertyKeys.elementAt(x));
            data.addElement(prop.getSupProperty((String)vctSupPropertyKeys.elementAt(x)));
            tbmdl.addRow(data);
        }        
    }
    
    
    public void propertyChanged(MDS_PropertyChangeEvent e) {
    	String proName = String.valueOf(treKeys.getLastSelectedPathComponent());
    	if(!proName.equals("")) {
			log.info("getLastSelectedPathComponent() = "+proName);
			if(e.getPropertyName().equals(proName)) {
				set_Table_Content(e.getProperty());
			}
    	}
    }
    
    
    
    public void valueChanged(TreeSelectionEvent e) {
        try {
            TreePath tp = e.getPath();
            Object[] o = tp.getPath();
            String keyName = String.valueOf(o[1]);
            currentProperty = ppm.getProperty(keyName);
            set_Table_Content(ppm.getProperty(keyName));
        } catch(Exception ex) {}    
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("About")) {    
            MDS.getUser().showAboutDialog(this, "PropertiesViewer", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"RegistryEditor.png"), "text");     
        } else if(e.getActionCommand().equals("Value")) {
            if(tblValues.getSelectedRow() != -1) {
                String val = MDS_OptionPane.showInputDialog(this, "Type new Value" ,"Edit Value", JOptionPane.INFORMATION_MESSAGE);
                if(val == null) return;
                if(!val.equals("")) {
                    throw new NullPointerException("Current reg key is null");
                    //MDS.getExceptionManager().showException(new NullPointerException("Current reg key is null"));
                    //currentRegKey.addValue((String)tblValues.getValueAt(tblValues.getSelectedRow(), 0), val);    
                    //reg.amendKey(currentRegKey);
                    //reg.update();      
                    //set_Table_Content(currentRegKey);
                }
            } else {
                MDS_OptionPane.showMessageDialog(this, "You have to select a Row", "Row Selection", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if(e.getActionCommand().equals("Close")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Refresh")) {
            set_Table_Content(currentProperty);
        }
    }
    
    public static void MDS_Main(String arg[]) {
        new MDS_PropertiesViewer();
    }
    
    
}    
    