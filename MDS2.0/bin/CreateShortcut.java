
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;



public class CreateShortcut extends ControlModule implements ActionListener {



    private MDS_Panel pnlFile = new MDS_Panel(new BorderLayout());
    

    private MDS_TextField txtfFilePath = new MDS_TextField();
    private MDS_Button btnFileBrowse = new MDS_Button("Browse");
    
    private File exeFile = null;
    private File file = null;
    
    

    public CreateShortcut() {
        super("Create Shorcut", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-samba.png"));
    
        pnlFile.add(txtfFilePath, BorderLayout.CENTER);
        btnFileBrowse.setActionCommand("Browse");
        btnFileBrowse.addActionListener(this);
        pnlFile.add(btnFileBrowse, BorderLayout.EAST);
        pnlFile.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"File"));    
            
        this.addPanel(pnlFile);
        this.setSize(370, 125);
        this.setCenterScreen();
        
        setEnabledBrowe_File(false); 
        
        this.setVisible(true);        
            
    }
    
    
    
    private void setEnabledBrowe_File(boolean b) {
        /*txtfFilePath.setEnabled(b);
        btnFileBrowse.setEnabled(b);   
        txtfExePath.setEnabled(!b);
        btnExeBrowse.setEnabled(!b);*/       
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
               
        if(e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Ok")) {
            try {
            file = new File(txtfFilePath.getText());
            if(!file.exists()) {
                txtfFilePath.selectAll();
                MDS_OptionPane.showMessageDialog(this, "File Not Found \n\n"+file.getPath(), "Create Shortcut", JOptionPane.ERROR_MESSAGE);    
                return;
            }            
            Hashtable ht = new Hashtable();
            ht.put("icon", ImageManipulator.createScaledImageIcon(MDS.getFileManager().getFileType_Icon(file),48 ,-1 ,ImageManipulator.ICON_SCALE_TYPE));
            ht.put("file", file);
            Random rand = new Random();
            ObjectOutputStream o_Out= new ObjectOutputStream(new FileOutputStream(MDS.getFileManager().getMDS_DesktopShortcuts_Dir()+"\\"+String.valueOf(Math.abs(rand.nextLong()))+".dkst"));
            o_Out.writeObject(file);
            o_Out.close();  
            MDS.getBaseWindow().getDesktop().refreshDesktop();          
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            
            this.dispose();
        } else if(e.getActionCommand().equals("Browse")) {
            MDS_FileChooser fc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            if(fc.showDiaog(this) ==  fc.APPROVE_OPTION) {         
                txtfFilePath.setText(fc.getPath());                         
            }            
        }
    }    
    
    
    
    public static void MDS_Main(String arg[]) {
        new CreateShortcut();
    }
    
    
    
}    