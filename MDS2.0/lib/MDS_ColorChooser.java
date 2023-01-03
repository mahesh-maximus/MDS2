/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;



public final class MDS_ColorChooser extends JPanel implements ChangeListener, ActionListener {



    static final int CANCEL_OPTION = 32;   
    static final int APPROVE_OPTION = 442;
    static final int ERROR_OPTION = 752;
        
    MDS_Dialog dlg;
    JPanel pnlSliders = new JPanel();
    MDS_Slider sldRed;
    MDS_Slider sldGreen;
    MDS_Slider sldBlue;
    MDS_Slider sldAlpha;
    MDS_List lstRed;
    MDS_List lstGreen;
    MDS_List lstBlue;
    MDS_List lstAlpha;
    JPanel pnlPalette = new JPanel();
    JTextArea txtaPalette = new JTextArea();
    MDS_Button btnOk = new MDS_Button("Ok");
    MDS_Button btnCancel = new MDS_Button("Cancel");
    
    Color color = new Color(0, 0, 0);
    int closedOption = CANCEL_OPTION;
    


    public MDS_ColorChooser() {
        //super("Color Chooser", false, true, false, false, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "clipBoard.png"));
        //contentPane = (JComponent) this.getContentPane();
        this.setLayout(null);    
        pnlSliders.setLayout(new GridLayout(3, 0));
        
        JLabel lblRed = new JLabel("Red");
        lblRed.setBounds(20, 20, 40, 25);
        this.add(lblRed);        
        sldRed = new MDS_Slider(0, 255, 0);
        sldRed.setName("Red");
        sldRed.addChangeListener(this);
        sldRed.setBounds(60, 20, 250, 50);
        sldRed.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        sldRed.setPaintTicks(true);
        sldRed.setPaintLabels(true);
        sldRed.setMajorTickSpacing(85);        
        this.add(sldRed);
        
        Vector vctValues = new Vector();
        for(int x =0; x<=255; x++) {
            vctValues.addElement(String.valueOf(x));
        }        
        
        lstRed = new MDS_List(vctValues);
        lstRed.setSelectionBackground(Color.white);
        JScrollPane scrl_lstRed  = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        scrl_lstRed.setBounds(320, 23, 55, 20);
        scrl_lstRed.setViewportView(lstRed);
        this.add(scrl_lstRed);
        
        
        JLabel lblGreen = new JLabel("Green");
        lblGreen.setBounds(20, 70, 40, 25);
        this.add(lblGreen);        
        sldGreen = new MDS_Slider(0, 255, 0);
        sldGreen.setName("Green");
        sldGreen.addChangeListener(this);
        sldGreen.setBounds(60, 70, 250, 50);
        sldGreen.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        sldGreen.setPaintTicks(true);
        sldGreen.setPaintLabels(true);
        sldGreen.setMajorTickSpacing(85);        
        this.add(sldGreen); 
        
        lstGreen = new MDS_List(vctValues);
        lstGreen.setSelectionBackground(Color.white);
        JScrollPane scrl_lstGreen = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);        
        scrl_lstGreen.setBounds(320, 73, 55, 20);
        scrl_lstGreen.setViewportView(lstGreen);
        this.add(scrl_lstGreen);               


        JLabel lblBlue = new JLabel("Blue");
        lblBlue.setBounds(20, 120, 40, 25);
        this.add(lblBlue); 
        sldBlue = new MDS_Slider(0, 255, 0);
        sldBlue.setName("Blue");
        sldBlue.addChangeListener(this);
        sldBlue.setBounds(60, 120, 250, 50);
        sldBlue.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        sldBlue.setPaintTicks(true);
        sldBlue.setPaintLabels(true);
        sldBlue.setMajorTickSpacing(85);        
        this.add(sldBlue); 

        lstBlue = new MDS_List(vctValues);
        lstBlue.setSelectionBackground(Color.white);
        JScrollPane scrl_lstBlue = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);                
        scrl_lstBlue.setBounds(320, 123, 55, 20);
        scrl_lstBlue.setViewportView(lstBlue);
        this.add(scrl_lstBlue);  
        
        
        
        JLabel lblAlpha = new JLabel("Alpha");
        lblAlpha.setBounds(20, 170, 40, 25);
        this.add(lblAlpha); 
        sldAlpha = new MDS_Slider(0, 255, 255);
        sldAlpha.setName("Alpha");
        sldAlpha.addChangeListener(this);
        sldAlpha.setBounds(60, 170, 250, 50);
        sldAlpha.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        sldAlpha.setPaintTicks(true);
        sldAlpha.setPaintLabels(true);
        sldAlpha.setMajorTickSpacing(85);        
        this.add(sldAlpha); 

        lstAlpha = new MDS_List(vctValues);
        lstAlpha.setSelectionBackground(Color.white);
        JScrollPane scrl_lstAlpha = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);                
        scrl_lstAlpha.setBounds(320, 173, 55, 20);
        scrl_lstAlpha.setViewportView(lstAlpha);
        lstAlpha.setSelectedValue(String.valueOf(255), true);
        this.add(scrl_lstAlpha); 
        
        txtaPalette.setEditable(false);        
        txtaPalette.setBackground(UIManager.getColor("Label.background"));
        JScrollPane scrlPalette = new JScrollPane(txtaPalette);
        scrlPalette.setBounds(20, 240, 355, 60);
        this.add(scrlPalette); 
        txtaPalette.setBackground(color);
        
        btnOk.setBounds(400, 20, 75, 30);
        btnOk.addActionListener(this);
        this.add(btnOk);

        btnCancel.setBounds(400, 60, 75, 30);
        btnCancel.addActionListener(this);
        this.add(btnCancel);
                  
        this.setSize(500, 350);
        //this.setCenterScreen();
                
    }
    
    
    
    public MDS_ColorChooser(Color c) {
        this();
        sldRed.setValue(c.getRed());
        sldGreen.setValue(c.getGreen());
        sldBlue.setValue(c.getBlue());
        sldAlpha.setValue(c.getAlpha());
    }
    
    
    
    public int showColorChooser(Component parentComponent) {
        MDS_Frame parentFrame = (MDS_Frame)parentComponent;
        dlg = new MDS_Dialog(parentFrame, "Color Chooser");
        dlg.getContentPane().setLayout(null);
        dlg.getContentPane().add(this);
        dlg.setSize(500, 350);
        dlg.setCenterScreen();  
        //Console.println(dlg.getX());
        //Console.println(dlg.getY());      
        dlg.showDialog();      
        return closedOption;
    }
    
    
    
    public Color getColor() {
        return color;
    }
    
    
    
    public void stateChanged(ChangeEvent e) {
        MDS_Slider sldEvt = (MDS_Slider)e.getSource();
        if(sldEvt.getName().equals("Red")) {
            lstRed.setSelectedValue(String.valueOf(sldEvt.getValue()), true);
        } else if(sldEvt.getName().equals("Green")) {
            lstGreen.setSelectedValue(String.valueOf(sldEvt.getValue()), true);
        } else if(sldEvt.getName().equals("Blue")) {
            lstBlue.setSelectedValue(String.valueOf(sldEvt.getValue()), true);
        } else if(sldEvt.getName().equals("Alpha")) {
            lstAlpha.setSelectedValue(String.valueOf(sldEvt.getValue()), true);
        }    
        
        color = new Color(sldRed.getValue(), sldGreen.getValue(), sldBlue.getValue(), sldAlpha.getValue());
        txtaPalette.setBackground(color);
        
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Ok")) {
            closedOption = APPROVE_OPTION;
            dlg.dispose();
        } else if(e.getActionCommand().equals("Cancel")) {
            closedOption = CANCEL_OPTION;
            dlg.dispose();
        }
    }
   

}