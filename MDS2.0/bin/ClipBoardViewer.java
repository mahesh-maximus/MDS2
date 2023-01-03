/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;



public class ClipBoardViewer extends MDS_Frame implements ActionListener, ClipBoardListener {


    
    MDS_User usr = MDS.getUser();
    JComponent contentPane;
    MDS_TextArea txtaViewer = new MDS_TextArea();
    JMenuBar mnbCalculator = new JMenuBar();
    MDS_Menu mnuFile = new MDS_Menu("File", 'F');
    JMenuItem mniPString = usr.createMenuItem("Put String", this, 'S');
    JMenuItem mniPFile = usr.createMenuItem("Put File", this, 'l');
    JMenuItem mniRemove = usr.createMenuItem("Remove Content", this, 'C');
    JMenuItem mniExit = usr.createMenuItem("Exit", this, 'X');
    MDS_Menu mnuEdit = new MDS_Menu("Edit", 'E');
    JMenuItem mniRefresh = usr.createMenuItem("Refresh", this);
    MDS_Menu mnuHelp = new MDS_Menu("Help", 'H');
    JMenuItem mniAbout = usr.createMenuItem("About", this);
    MDS_Label lblContentType = new MDS_Label("");
    MDS_Dialog dlgStr;
    MDS_TextArea txtaInputText;
    
    MDS_Clipboard clipBoard = MDS.getClipboard(); 
    int currentContentType = 0;
    
    
        
    public ClipBoardViewer() {
        super("Clip Board Viewer", false, true, false, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "clipBoard.png"));
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout()); 
        txtaViewer.setEditable(false);
        contentPane.add(new JScrollPane(txtaViewer), BorderLayout.CENTER);    
        mnuFile.add(mniPString);
        mnuFile.add(mniPFile);
        mnuFile.add(mniRemove);
        mnuFile.add(mniExit);
        mnbCalculator.add(mnuFile);
        mnuEdit.add(mniRefresh);
        mnbCalculator.add(mnuEdit);
        mnuHelp.add(mniAbout);
        mnbCalculator.add(mnuHelp);
        contentPane.add(lblContentType, BorderLayout.SOUTH);
        this.setJMenuBar(mnbCalculator);           
        this.setSize(450,450);
        this.setCenterScreen();
        this.setVisible(true); 
        clipBoard.addClipBoardListener(this);
        refresh();
               
    }
    
    
    
    private void refresh() {
        if(!clipBoard.isEmpty()) {
            currentContentType = clipBoard.getCurrentContentType();
            if(currentContentType == clipBoard.CONTENT_TYPE_FILE) {
                txtaViewer.setText((String)clipBoard.getContent_Summery()); 
                if(clipBoard.getCurrentContentType() == clipBoard.STATUS_COPIED) {  
                    lblContentType.setText("Current content = File, Mode = Copy");   
                } else if(clipBoard.getCurrentContentType() == clipBoard.STATUS_MOVED) {
                    lblContentType.setText("Current content = File, Mode = Move");
                }
            } else if(currentContentType == clipBoard.CONTENT_TYPE_STRING) {
                txtaViewer.setText((String)clipBoard.getContent_Summery());
                if(clipBoard.getCurrentContentType() == clipBoard.STATUS_COPIED) {  
                    lblContentType.setText("Current content = Text, Mode = Copy");   
                } else if(clipBoard.getCurrentContentType() == clipBoard.STATUS_MOVED) {
                    lblContentType.setText("Current content = Text, Mode = Move");
                }
            }
        }
    }    
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Exit")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Refresh")) {
             refresh();
        } else if(e.getActionCommand().equals("About")) {
            MDS.getUser().showAboutDialog(this, "Clip Boarder Viewer", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"clipBoard.png"), MDS.getAbout_Mahesh());        
        } else if(e.getActionCommand().equals("Put String")) {
            dlgStr = new MDS_Dialog(this, "String");
            dlgStr.setSize(300, 200);
            dlgStr.setCenterScreen();
            JComponent cp = (JComponent)dlgStr.getContentPane();
            cp.setLayout(new BorderLayout());
            txtaInputText = new MDS_TextArea();
            cp.add(new JScrollPane(txtaInputText));
            JPanel pnlBtns = new JPanel();
            pnlBtns.setLayout(new FlowLayout(FlowLayout.TRAILING)); 
            MDS_Button btnOk = new MDS_Button("Ok");
            btnOk.addActionListener(new MDS_ActionAdapter() {
                public void actionPerformed(ActionEvent e) {
                    if(!txtaInputText.getText().equals("")) {
                        clipBoard.setContent(txtaInputText.getText(), clipBoard.STATUS_COPIED);    
                    }
                    dlgStr.dispose();
                }
            });    
            pnlBtns.add(btnOk);
            MDS_Button btnCancel = new MDS_Button("Cancel");
            btnCancel.addActionListener(new MDS_ActionAdapter() {
                public void actionPerformed(ActionEvent e) {
                    dlgStr.dispose();
                }
            });    
            pnlBtns.add(btnCancel);
            cp.add(pnlBtns, BorderLayout.SOUTH);
            dlgStr.setVisible(true);
        } else if(e.getActionCommand().equals("Put File")) {
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {       
                clipBoard.setContent(fmfc.getPath(), clipBoard.STATUS_COPIED);
            }    
        } else if(e.getActionCommand().equals("Remove Content")) {
            if(clipBoard.isEmpty()) {
                MDS_OptionPane.showMessageDialog(this, "Clip Board is already empty.", "Clip Board", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int r = MDS_OptionPane.showConfirmDialog(this, "Are you sure you want to empty the Clip Board.", "Clip Board", JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE);
                if(r == JOptionPane.YES_OPTION) {
                    clipBoard.romoveContent();
                }    
            }
        }

    }  
    
    
    
    public void clipBoard_CopyTo() {
        refresh();  
    }
    
    
    
    public void clipBoard_MoveTo() {
        refresh();  
    }
    
    
    
    public void clipBoard_Paste_Copied() {
        refresh();  
    }
    
    
    
    public void clipBoard_Paste_Moved() {
        txtaViewer.setText("");
        lblContentType.setText(""); 
    }
    
    
    
    public void clipBoard_Empty() {
        txtaViewer.setText("");
        lblContentType.setText("");     
    }    
    
    
    
    public static void MDS_Main(String arg[]) { 
        new ClipBoardViewer();
    }    
    
    
    
}    