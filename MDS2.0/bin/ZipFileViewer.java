
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.jar.*;
import java.util.zip.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;




public class ZipFileViewer extends MDS_Frame implements ActionListener {



    MDS_User usr = MDS.getUser();
    JComponent contentPane;
    JMenuBar mnbJFV = new JMenuBar();
    JMenu mnuFile = new JMenu("File");
    JMenuItem mniOpen = usr.createMenuItem("Open Zip File", this, MDS_KeyStroke.getOpen(), KeyEvent.VK_O);
    JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    JMenu mnuHelp = new JMenu("Help");
    JMenuItem mniAbout = usr.createMenuItem("About ...", this, KeyEvent.VK_A);
    
    MDS_Label lblDecompressing = new MDS_Label("Decompressing ...");
    ZipFileContent zfc = new ZipFileContent(this);


    public ZipFileViewer() {
        super("Zip File Viewer",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_tar.png"));  
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        mnuFile.add(mniOpen);
        mnuFile.add(mniExit);
        mnuFile.setMnemonic('F');
        mnbJFV.add(mnuFile);
        mnuHelp.add(mniAbout);
        mnuHelp.setMnemonic('H');
        mnbJFV.add(mnuHelp); 
        this.setJMenuBar(mnbJFV);

        contentPane.add(new JScrollPane(zfc), BorderLayout.CENTER);

        this.setBounds(0,0,800,600);
        this.setVisible(true); 
            
    }
    
    
    
    private void loadFile(File f) {
        zfc.loadZipFileInfo(f);
    }

    

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open Zip File")) {
            zfc.openZipFile(this);
        } else if(e.getActionCommand().equals("About ...")) {
            MDS.getUser().showAboutDialog(this, "Zip File Viewer", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"mds-zip.png"), MDS.getAbout_Mahesh());
        } else if(e.getActionCommand().equals("Exit")) {
            this.dispose();
        }    
    } 
        
    
    
    public static void MDS_Main(String arg[]) {
        ZipFileViewer zfv = new ZipFileViewer(); 
        if(arg.length == 1) {
            zfv.loadFile(new File(arg[0]));
        }    
    }
    
    
    
}    