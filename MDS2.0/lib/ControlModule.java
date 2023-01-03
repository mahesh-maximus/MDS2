/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;



public class ControlModule extends MDS_Frame implements ActionListener {

    
    private static Vector AllModules = new Vector();
    private JComponent contentPane;    
    private MDS_Panel pnlButtonContainer = new MDS_Panel();
    private MDS_Button btnOk;
    private MDS_Button btnCancel;
    


    public ControlModule(String title) {
        super(title, false, true);

        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
            
        contentPane.add(new MDS_Label("  "), BorderLayout.WEST);
        contentPane.add(new MDS_Label("  "), BorderLayout.EAST);
            
        pnlButtonContainer.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            
        btnOk = new MDS_Button("Ok");
        btnOk.addActionListener(this);
        //this.getRootPane().setDefaultButton(btnOk);
        pnlButtonContainer.add(btnOk);
            
        btnCancel = new MDS_Button("Cancel");
        btnCancel.addActionListener(this);
        pnlButtonContainer.add(btnCancel);
             
        contentPane.add(pnlButtonContainer, BorderLayout.SOUTH);
                               
    }
    
    
    
    public ControlModule(String title, ImageIcon i) {
        super(title, false, true, false, false, i);

        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
            
        contentPane.add(new MDS_Label("  "), BorderLayout.WEST);
        contentPane.add(new MDS_Label("  "), BorderLayout.EAST);
            
        pnlButtonContainer.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            
        btnOk = new MDS_Button("Ok");
        btnOk.addActionListener(this);
        //this.getRootPane().setDefaultButton(btnOk);
        pnlButtonContainer.add(btnOk);
            
        btnCancel = new MDS_Button("Cancel");
        btnCancel.addActionListener(this);
        pnlButtonContainer.add(btnCancel);
            
        contentPane.add(pnlButtonContainer, BorderLayout.SOUTH);
                                       
    }
    
    
 
    public JComponent get_CM_ContentPane() {
        return contentPane;
    }
    
    
    
    public void addPanel(MDS_Panel p) {
        contentPane.add(p, BorderLayout.CENTER);    
    }
    
    
    
    public void addJComponent(JComponent c) {
        contentPane.add(c, BorderLayout.CENTER); 
    }
    
    
    
    public void setEnabled_Ok(boolean b) {
        btnOk.setEnabled(b);
    }
    
    
    
    public void setEnabled_Cancel(boolean b) {
        btnCancel.setEnabled(b);
    }
    
   
    
    public boolean isEnabled_Ok() {
        return btnOk.isEnabled();
    }  
    
    
    
    public boolean isEnabled_Cancel() {
        return btnCancel.isEnabled();
    }  
    
    
    
    public void setVisible(boolean vs) {
        super.setVisible(vs);
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
    
    }
    
    
    
}    