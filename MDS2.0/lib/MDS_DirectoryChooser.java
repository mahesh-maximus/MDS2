
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;



public final class MDS_DirectoryChooser extends MDS_Panel implements TreeExpansionListener, TreeSelectionListener
                              , TreeWillExpandListener, ActionListener {
    
    
    static final int CANCEL_OPTION = 32;   
    static final int APPROVE_OPTION = 442;
    static final int ERROR_OPTION = 752;
    
    JComponent contentPane;
    MDS_Dialog dlg;
    MDS_Panel pnlTop = new MDS_Panel();
    MDS_TextField txfdLocation;
    MDS_Panel pnlCenter = new MDS_Panel();
    MDS_Tree treFolders;
    DefaultTreeModel dtmFolders; 
    MDS_TreeCellRenderer tcRenderer;
    Vector vctVisitedLocations = new Vector();  
    MDS_Panel pnlBottom = new MDS_Panel();  
    MDS_Button btnOk;
    MDS_Button btnCancel;  
    
    int closedOption = -1;
    
    final String driveEmpty = "< Drive is Empty >";
    
    private FileManager fm = MDS.getFileManager();
    
    
    
    
    public MDS_DirectoryChooser() {
        //super("DirectoryChooser", true, true);
        
        
        
        //contentPane = (JComponent) this.getContentPane();
        this.setLayout(new BorderLayout());
        
        create_pnlTop();
        create_pnlCenter();
        create_pnlBottom();
        
        //this.setSize(350, 375);
        //this.setCenterScreen();
                   
    }
    
    
    
    public int showDiaog(Component parentComponent) {
    /*
        MDS_Frame parentFrame = (MDS_Frame)parentComponent;
        parentFrame.setInternalFrameDialog(this);
        MDS_UIManager.setParentComponent_Disabled(true, parentFrame.getContentPane());
        this.setVisible(true);
        MDS_UIManager.halt_While_Visible(this);
        MDS_UIManager.setParentComponent_Disabled(false, parentFrame.getContentPane());     
    */    
        MDS_Frame parentFrame = (MDS_Frame)parentComponent;
               
        dlg = new MDS_Dialog(parentFrame, "Directory Chooser");
        JComponent contentPane = (JComponent) dlg.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        dlg.setSize(350, 375);
        dlg.setCenterScreen();
        
        dlg.setResizable(true);
                 
        dlg.showDialog();            
        
        return closedOption;   
    }
    
    
    
    private void create_pnlTop() {
        pnlTop.setLayout(new GridLayout(2, 1));
        pnlTop.add(new MDS_Label("Location :"));
        txfdLocation = new MDS_TextField();
        txfdLocation.setEditable(false);
        pnlTop.add(txfdLocation);
        this.add(pnlTop, BorderLayout.NORTH);
    }
    
    
    
    private void create_pnlCenter() {
        pnlCenter.setLayout(new BorderLayout());
        Initialize_FolderTreeViewer();
        pnlCenter.add(new JScrollPane(treFolders), BorderLayout.CENTER); 
        this.add(pnlCenter, BorderLayout.CENTER);
    }
    
    
    
    private void create_pnlBottom() {
        pnlBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnOk = new MDS_Button("Ok");
        btnOk.addActionListener(this);
        pnlBottom.add(btnOk);
        btnCancel = new MDS_Button("Cancel");
        btnCancel.addActionListener(this);
        pnlBottom.add(btnCancel);
        this.add(pnlBottom, BorderLayout.SOUTH);

        
    }
    
    
    
    private void Initialize_FolderTreeViewer() {
        TreeNode root = create_FolderTree_Roots();
        dtmFolders = new DefaultTreeModel(root);
        treFolders = new MDS_Tree(dtmFolders);
        treFolders.setEditable(true);
        //treFolders.addMouseListener(this);
        
        tcRenderer = new MDS_TreeCellRenderer();
        
        tcRenderer.setClosedIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-filesys-folder.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        tcRenderer.setOpenIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-filesys-folder_open.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        tcRenderer.setLeafIcon(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-filesys-folder.png", 20, 20, ImageManipulator.ICON_SCALE_TYPE));
        treFolders.setCellRenderer(tcRenderer);            
        
        treFolders.addTreeExpansionListener(this);
        treFolders.addTreeSelectionListener(this);
        treFolders.addTreeWillExpandListener(this);
        
    }
    
    
    
    public String getPath() {
        return txfdLocation.getText();
    }
    
    
    
    public int getClosedOption() {
        return closedOption;
    }   
    
    
    
    private TreeNode create_FolderTree_Roots() {
    
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Disk Dirves");
        
        String[] rDrives = fm.getRootDrives_AsStrings();
        
        for(int x= 0; x < rDrives.length; x++) {
            DefaultMutableTreeNode  dmtn = new DefaultMutableTreeNode(rDrives[x]); 
            dmtn.add(new DefaultMutableTreeNode(driveEmpty));
            root.add(dmtn);    
        }
        
        return root;

    } 
    
    
    
    private String convertTreePath_ToActualPath(Object[] nodes) {

        String path = "";

        for(int countNodes = 0; countNodes < nodes.length; countNodes++) {

            if (countNodes >= 1) {
                if(countNodes ==1) {
                    path = path.concat(String.valueOf(nodes[countNodes]));
                } else {
                    path = path.concat(String.valueOf(nodes[countNodes]+"\\"));
           
                }
            }

        }
    
        return path;
    }        
    
    
    
    public void treeCollapsed(TreeExpansionEvent e) {
    
    }
    
    
    
    public void treeExpanded(TreeExpansionEvent e) {
                                
    }
    
    
    
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode dmtnLocation = (DefaultMutableTreeNode)treFolders.getLastSelectedPathComponent();
        txfdLocation.setText(convertTreePath_ToActualPath(dmtnLocation.getPath()));
        //set_cboDirectory_Location_Text(current_Directory_Location);
                 
        //set_tblContent(current_Directory_Location);
        
    }
    
    
    
    public void treeWillCollapse(TreeExpansionEvent event) {
    
    }
    
    
    
    public void treeWillExpand(TreeExpansionEvent e) {
    
        try {
    
            Object[] count = e.getPath().getPath();
        
            if(count.length == 1) return;
    
            File[] f =  fm.getContent_Directories(convertTreePath_ToActualPath(e.getPath().getPath())); 
                    
            boolean goOut = false; 
                    
            if(vctVisitedLocations.contains(convertTreePath_ToActualPath(e.getPath().getPath()))) {
                goOut = true; 
            } else {
                vctVisitedLocations.addElement(convertTreePath_ToActualPath(e.getPath().getPath()));
            }
                    
            if(goOut) return;                                   
                
            DefaultMutableTreeNode removalNode = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
            removalNode.removeAllChildren();   
                 
            DefaultMutableTreeNode selectedNode = null;
            DefaultMutableTreeNode newNode = null;
            DefaultMutableTreeNode newNode2= null;
        
            for(int x = 0; x < f.length; x++) {  
                            
                selectedNode = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();               
                newNode = new DefaultMutableTreeNode(f[x].getName());                
            
                File[] f2 = fm.getContent_Directories(convertTreePath_ToActualPath(e.getPath().getPath())+f[x].getName()+"\\");
                
                for(int y = 0; y < f2.length; y++) {                               
                    newNode2 = new DefaultMutableTreeNode(f2[y].getName());
                    newNode.add(newNode2);
                }
                          
                dtmFolders.insertNodeInto(newNode, selectedNode,selectedNode.getChildCount());
            }
        
            DefaultMutableTreeNode del =(DefaultMutableTreeNode) selectedNode.getFirstChild();
           
            if(del.getUserObject().equals(driveEmpty)) {
                selectedNode.remove(del);         
            }
        
        } catch(Exception ex) {
        
            if(ex instanceof NullPointerException) {
                //JOptionPane.showInternalMessageDialog(this,"Unable to access the location", "File Manager", JOptionPane.INFORMATION_MESSAGE);
                MDS_OptionPane.showMessageDialog(dlg ,"Unable to access the specified device.", "Directory Chooser", JOptionPane.INFORMATION_MESSAGE);
            } else {
            	throw new RuntimeException(ex);
                //MDS.getExceptionManager().showException(ex);
            }
        
        }
        
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Ok")) {
            if(txfdLocation.getText().endsWith(driveEmpty+"\\")) {
                MDS_OptionPane.showMessageDialog(dlg, "Specified Drive is empty", "Directory Chooser", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            closedOption = APPROVE_OPTION;    
        } else if(e.getActionCommand().equals("Cancel")) {
            closedOption = CANCEL_OPTION;
        }
        
        dlg.dispose();
    }          
    
    
    
}     
    