/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;
import java.awt.event.*;
import javax.swing.event.*; 
import java.io.*;



public class MDS_Tree extends JTree implements TreeWillExpandListener{



    public MDS_Tree() {
        super();
        initialize();
    }
    
    
    
    public MDS_Tree(Hashtable value) {
        super(value);
        initialize();
    }
    
    
    
    public MDS_Tree(Object[] value) {
        super(value);
        initialize();
    }
    
    
    
    public MDS_Tree(TreeModel newModel) {
        super(newModel);
        initialize();
    }
    
    
    
    public MDS_Tree(TreeNode root) {
        super(root);
        initialize();
    }
    
    
    
    public MDS_Tree(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        initialize();
    }
    
    
    
    public MDS_Tree(Vector value) {
        super(value);
        initialize();
    } 
    
    
    
    private void initialize() {  
        this.addTreeWillExpandListener(this);    
    }
    
    
    
    public void treeWillCollapse(TreeExpansionEvent e) {
        MDS.getSound().playSound(new File("Media\\Sound\\Mouse Move.wav"));         
    }
        
        
        
    public void treeWillExpand(TreeExpansionEvent e) {       
        MDS.getSound().playSound(new File("Media\\Sound\\Mouse Move.wav")); 
    }
    
    
}    