/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



public final class MDS_FontChooser extends JPanel implements ActionListener {




    static final int CANCEL_OPTION = 32;   
    static final int APPROVE_OPTION = 442;
    static final int ERROR_OPTION = 752;
        
    MDS_Dialog dlg;
    
    String[] fontNameList;
    
    JLabel lblFont = new JLabel("Font:");
    JLabel lblFontStyle = new JLabel("Style:");
    JLabel lblFontSize = new JLabel("Size:");
    
    MDS_ComboBox cboFont;
    MDS_ComboBox cboFontStyle;
    MDS_ComboBox cboFontSize;
    
    JLabel lblSampleFont = new JLabel("AB ab cde 1234 @ & % * NJKU ytew");
    
    MDS_Button btnOk = new MDS_Button("Ok");
    MDS_Button btnCancel = new MDS_Button("Cancel");
    
    Font currentFont;
    
    int closedOption = CANCEL_OPTION;



    public MDS_FontChooser() {
        //super("Font Chooser", false, true, false, false, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "clipBoard.png"));
        //contentPane = (JComponent) this.getContentPane();
        this.setLayout(null);
        
        lblFont.setBounds(20, 10, 50, 30);
        this.add(lblFont);
        
        cboFont = new MDS_ComboBox();
        cboFont.setActionCommand("Font");
        //cboFont.addActionListener(this);
        JScrollPane scrlp_cboFont = new JScrollPane(cboFont);
        scrlp_cboFont.setBounds(20, 40, 200, 28);
        this.add(scrlp_cboFont);
         
        fontNameList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();        
        for (int i = 0; i < fontNameList.length; i++) {
            cboFont.addItem(fontNameList[i]);        
        }
        
        lblFontStyle.setBounds(250, 10, 50, 30);
        this.add(lblFontStyle);
                
        cboFontStyle = new MDS_ComboBox();
        cboFontStyle.setActionCommand("Style");
        //cboFontStyle.addActionListener(this);        
        cboFontStyle.addItem("PLAIN");
        cboFontStyle.addItem("BOLD");
        cboFontStyle.addItem("ITALIC");
        cboFontStyle.addItem("BOLD - ITALIC");
        JScrollPane scrlp_cboFontStyle = new JScrollPane(cboFontStyle);
        scrlp_cboFontStyle.setBounds(250, 40, 120, 28);
        this.add(scrlp_cboFontStyle); 
        
        lblFontSize.setBounds(400, 10, 50, 30);
        this.add(lblFontSize);
        
        Vector vctSize = new Vector();
        for(int x =0; x<=100; x++) {
            vctSize.addElement(String.valueOf(x));
        }
        
        cboFontSize = new MDS_ComboBox(vctSize);
        cboFontSize.setSelectedItem("12");
        cboFontSize.setActionCommand("Size");
        //cboFontSize.addActionListener(this);          
        JScrollPane scrlp_cboFontSize = new JScrollPane(cboFontSize);
        scrlp_cboFontSize.setBounds(400, 40, 70, 28);
        this.add(scrlp_cboFontSize);
        /*        
        txfdSize.setBounds(400, 40, 50, 28);
        contentPane.add(txfdSize);     
        
        */
        lblSampleFont.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Sample Font"));  
        lblSampleFont.setBounds(20, 90, 450, 100);
        lblSampleFont.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblSampleFont);
        
        btnOk.setBounds(300, 210, 75, 30);
        btnOk.addActionListener(this);
        this.add(btnOk);

        btnCancel.setBounds(390, 210, 75, 30);
        btnCancel.addActionListener(this);
        this.add(btnCancel);        
        
        this.setSize(500, 296);               
    }
    
    
    
    public MDS_FontChooser(Font f) {
        this();
        cboFont.setSelectedItem(f.getName());
        cboFontStyle.setSelectedItem(String.valueOf(f.getStyle()));
        cboFontSize.setSelectedItem(String.valueOf(f.getSize()));
        lblSampleFont.setFont(currentFont);
    }
    
    
    
    public Font getFont() {
        return currentFont;
    }
    
    
    
    public int showFontChooser(Component parentComponent) {
        MDS_Frame parentFrame = (MDS_Frame)parentComponent;
        cboFont.addActionListener(this);
        cboFontStyle.addActionListener(this); 
        cboFontSize.addActionListener(this);        
        dlg = new MDS_Dialog(parentFrame, "Font Chooser");
        dlg.getContentPane().setLayout(null);
        dlg.getContentPane().add(this);
        dlg.setSize(500, 296);
        dlg.setCenterScreen();
        dlg.showDialog();
        return closedOption;
    }
    
    
    
    private int getSelectedFontStyle() {
    
        int style = 0;

        if(cboFontStyle.getSelectedItem().equals("PLAIN")) {
            style = Font.PLAIN;    
        } else if(cboFontStyle.getSelectedItem().equals("BOLD")) {
            style = Font.BOLD;
        } else if(cboFontStyle.getSelectedItem().equals("ITALIC")) {
            style = Font.ITALIC;
        } else if(cboFontStyle.getSelectedItem().equals("BOLD - ITALIC")) {
            style = Font.BOLD + Font.ITALIC;
        }  

        return style;
        
    }
    
    
    
    private int getSelectedFontSize() {
        
        int size = 0;
        size = Integer.parseInt((String)cboFontSize.getSelectedItem());
        return size;
        
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Font")) {
            currentFont = new Font((String)cboFont.getSelectedItem(), getSelectedFontStyle(), getSelectedFontSize());
            lblSampleFont.setFont(currentFont);
        } else if(e.getActionCommand().equals("Style")) {
            currentFont = new Font((String)cboFont.getSelectedItem(), getSelectedFontStyle(), getSelectedFontSize());
            lblSampleFont.setFont(currentFont);
        } else if(e.getActionCommand().equals("Size")) {
            currentFont = new Font((String)cboFont.getSelectedItem(), getSelectedFontStyle(), getSelectedFontSize());
            lblSampleFont.setFont(currentFont);	
        } else if(e.getActionCommand().equals("Ok")) {
            closedOption = APPROVE_OPTION;
            dlg.dispose();            
        } else if(e.getActionCommand().equals("Cancel")) {
            closedOption = CANCEL_OPTION;
            dlg.dispose();
        }
        
        
    }

}