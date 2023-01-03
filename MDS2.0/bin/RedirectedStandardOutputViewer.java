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
import java.util.*;
import javax.swing.text.*; 



public class RedirectedStandardOutputViewer extends MDS_Frame implements RedirectedStandardOutputListener, ActionListener {


    private static boolean isLoaded = false;
    private static RedirectedStandardOutputViewer rstdov = null;
    private MDS_User usr = MDS.getUser();
    
    private JComponent contentPane;
    private MDS_TabbedPane tbpOutputs = new MDS_TabbedPane();

    private MDS_TextArea txtaOutput = new MDS_TextArea();
    private MDS_TextArea txtaErrorOutput = new MDS_TextArea();
    
    private MDS_Menu mnuFile = new MDS_Menu("File", KeyEvent.VK_F);
    private JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    
    private MDS_Menu mnuEdit = new MDS_Menu("Edit", KeyEvent.VK_E);
    private JMenuItem mniCopy = usr.createMenuItem("Copy", this, MDS_KeyStroke.getCut(), KeyEvent.VK_C);
    private JMenuItem mniSelectAll = usr.createMenuItem("Select All", this, MDS_KeyStroke.getSelectAll(), KeyEvent.VK_A);
    
    private MDS_Menu mnuFormat = new MDS_Menu("Format", KeyEvent.VK_F);
    private JMenuItem mniFont = usr.createMenuItem("Font", this, KeyEvent.VK_F);
    private JMenuItem mniBackgroundColor = usr.createMenuItem("Background Color", this);   
    private JMenuItem mniForegroundColor = usr.createMenuItem("Foreground Color", this);   
     
    private MDS_Menu mnuHelp = new MDS_Menu("Help", KeyEvent.VK_H);
    private JMenuItem mniAbout = usr.createMenuItem("About", this, KeyEvent.VK_A);    




    public RedirectedStandardOutputViewer() {
        super("Redirected Standard Output Viewer", true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-terminal.png"));    

        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        txtaOutput.setFont(new Font("Courier", Font.PLAIN, 12)); 
        txtaOutput.setBackground(Color.black);
        txtaOutput.setForeground(Color.green);
        txtaOutput.setEditable(false);
        txtaOutput.append("MDS 2.0\nMDS [Standard output]\n\n");
        tbpOutputs.addTab("Output", new JScrollPane(txtaOutput)); 
        txtaErrorOutput.setFont(new Font("Courier", Font.PLAIN, 12));
        txtaErrorOutput.setEditable(false);    
        txtaErrorOutput.setBackground(Color.black);
        txtaErrorOutput.setForeground(Color.green);   
        txtaErrorOutput.append("MDS 2.0\nMDS [Standard error output]\n\n");     
        tbpOutputs.addTab("Error Output",new JScrollPane(txtaErrorOutput));
        contentPane.add(tbpOutputs, BorderLayout.CENTER);
        
        JMenuBar mnb =new JMenuBar();
        
        mnuFile.add(mniExit);
        mnb.add(mnuFile);
        
        mnuEdit.add(mniCopy);
        mnuEdit.addSeparator();
        mnuEdit.add(mniSelectAll);
        mnb.add(mnuEdit);
        
        mnuFormat.add(mniFont);
        mnuFormat.add(mniForegroundColor);
        mnuFormat.add(mniBackgroundColor);
        mnb.add(mnuFormat);
        
        mnuHelp.add(mniAbout);
        mnb.add(mnuHelp);        
        this.setJMenuBar(mnb);
        
        /*
        MDS_Frame frmLoading = new MDS_Frame("Loading output history");
        frmLoading.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        frmLoading.setSize(300, 150);
        frmLoading.getContentPane().setLayout(new BorderLayout());
        MDS_Label lblMsg = new MDS_Label("Loading output history", JLabel.CENTER);
        lblMsg.setFont(new Font(lblMsg.getFont().getFontName(), lblMsg.getFont().getStyle(), 20));
        frmLoading.getContentPane().add(lblMsg, BorderLayout.CENTER);
        frmLoading.setCenterScreen();       
        frmLoading.setVisible(true); 
        */
        Vector vector = MDS.getRedirectedStandardOutput().getOutputHistory();
        for(int x=0;x<=vector.size()-1;x++) {
            txtaOutput.append(String.valueOf(vector.elementAt(x)));
        }
        vector = MDS.getRedirectedStandardOutput().getErrorOutputHistory();
        for(int x=0;x<=vector.size()-1;x++) {
            txtaErrorOutput.append(String.valueOf(vector.elementAt(x)));
        }
        
        //frmLoading.dispose();        
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                isLoaded = false;
//                rstdov = null;                      
            }
        });        
        
        this.setSize(600, 400);
        this.setCenterScreen();
        this.setVisible(true); 
//        rstdov = this; 
        
        MDS.getRedirectedStandardOutput().addRedirectedStandardOutputListener(this);     
        
        isLoaded = true;
         
    }
    
    
    
    private void setSelectedTabIndex(int x) {
        if(x == 1 || x == 0) tbpOutputs.setSelectedIndex(x);
    }
    

    
    public void rsdoNewOutputText(String text) {
        txtaOutput.append(text); 
        Document doc = txtaOutput.getDocument();
        txtaOutput.setCaretPosition(doc.getLength());
    }
    
    
    
    public void rsdoErrorText(String text) {
        txtaErrorOutput.append(text);
        Document doc = txtaOutput.getDocument();
        txtaOutput.setCaretPosition(doc.getLength());     
    }  
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Exit")) {
            this.dispose();          
        } else if(e.getActionCommand().equals("Font")) {
            MDS_FontChooser fc;
            if(tbpOutputs.getSelectedIndex() == 0) {
                fc = new MDS_FontChooser(txtaOutput.getFont());
            } else {
                fc = new MDS_FontChooser(txtaErrorOutput.getFont());
            }    
            if(fc.showFontChooser(this) == fc.APPROVE_OPTION) {
                if(tbpOutputs.getSelectedIndex() == 0) {
                    txtaOutput.setFont(fc.getFont());
                } else {
                    txtaErrorOutput.setFont(fc.getFont());
                }                
            }
        } else if(e.getActionCommand().equals("Foreground Color")) {
            MDS_ColorChooser cc = new MDS_ColorChooser();
            if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                if(tbpOutputs.getSelectedIndex() == 0) {
                    txtaOutput.setForeground(cc.getColor());
                } else {
                    txtaErrorOutput.setForeground(cc.getColor());
                }
            }   
        } else if(e.getActionCommand().equals("Background Color")) {
            MDS_ColorChooser cc = new MDS_ColorChooser();
            if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                if(tbpOutputs.getSelectedIndex() == 0) {
                    txtaOutput.setBackground(cc.getColor());
                } else {
                    txtaErrorOutput.setBackground(cc.getColor());
                }
            }                         
        } else if(e.getActionCommand().equals("Copy")) {
            if(tbpOutputs.getSelectedIndex() == 0) {
                txtaOutput.copy();
            } else {
                txtaErrorOutput.copy();
            }       
        } else if(e.getActionCommand().equals("Select All")) {
            if(tbpOutputs.getSelectedIndex() == 0) {
                txtaOutput.selectAll();
            } else {
                txtaErrorOutput.selectAll();
            }                    
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "Redirected Standard Output Viewer", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"stdio.png"), MDS.getAbout_Mahesh());
        }        
    }
    
    
    

   
    
    
    public static void MDS_Main(String arg[]) { 
//        if(isLoaded) {
//            try {
//                if(rstdov.isIcon()) rstdov.setIcon(false);
//                rstdov.setSelected(true);
//                if(arg.length == 1) rstdov.setSelectedTabIndex(Integer.parseInt(arg[0]));    
//            } catch(Exception ex) {}
//        } else {
//
//            isLoaded = true;
//            new RedirectedStandardOutputViewer(); 
//            if(arg.length == 1) rstdov.setSelectedTabIndex(Integer.parseInt(arg[0])); 
//        }
		
//		try {	
//			if(rstdov == null) {
//				rstdov = new RedirectedStandardOutputViewer();
//				if(arg.length == 1) rstdov.setSelectedTabIndex(Integer.parseInt(arg[0])); 
//			} else {
//	            if(rstdov.isIcon()) rstdov.setIcon(false);
//	            rstdov.setSelected(true);
//	            if(arg.length == 1) rstdov.setSelectedTabIndex(Integer.parseInt(arg[0]));  			
//			}
//		} catch(Exception ex) {}   


		new RedirectedStandardOutputViewer();
	

    }  
    	
    	
    	
    
    
    
}    