/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;



public class ScreenCapture extends MDS_Frame implements Runnable, ActionListener {



    JComponent contentPane;
    JPanel pnlLocation = new JPanel();
    MDS_TextField txfdPath = new MDS_TextField(System.getProperty("user.home")+"\\");
    MDS_Button btnBrowse = new MDS_Button("Browse");
    
    JPanel pnlCapture = new JPanel();
    MDS_Button btnGrab = new MDS_Button("Grab");
    MDS_ComboBox cboDelay;
    
    Thread thread = null;
    
    

    public ScreenCapture() {
        super("Clip Board Viewer", false, true, false, false, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-ksnapshot.png"));
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        pnlLocation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Path"));
        pnlLocation.setLayout(new BorderLayout());
        pnlLocation.add(txfdPath, BorderLayout.CENTER);
        btnBrowse.addActionListener(this);
        pnlLocation.add(btnBrowse, BorderLayout.EAST);
        contentPane.add(pnlLocation, BorderLayout.NORTH);       
        pnlCapture.setLayout(new BorderLayout());
        pnlCapture.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Capture"));
        btnGrab.setBounds(10, 20, 60, 28);
        btnGrab.addActionListener(this);
        pnlCapture.add(btnGrab, BorderLayout.CENTER);
               
        Vector vctDelay = new Vector();
        for(int x =0; x<=20; x++) {
            vctDelay.addElement(String.valueOf(x));
        }        
        cboDelay = new MDS_ComboBox(vctDelay);
        JScrollPane scrlpDelay = new JScrollPane(cboDelay);
        scrlpDelay.setBounds(200, 20, 30, 30);
        
        pnlCapture.add(scrlpDelay, BorderLayout.EAST);
        contentPane.add(pnlCapture, BorderLayout.CENTER);
                
        this.setSize(400, 150);
        this.setCenterScreen();
        this.setVisible(true);           
    }
    
    
    
    public void run() {
        try {
            int fileNo = 1;
            boolean done = false;
            thread.sleep(Integer.parseInt((String)cboDelay.getSelectedItem()) * 1000);
            File f = new File(txfdPath.getText()+String.valueOf(fileNo)+".jpg");
            
            while(!done) {
                if(f.exists()) {
                    fileNo++;
                    f = new File(txfdPath.getText()+String.valueOf(fileNo)+".jpg");    
                } else {
                    done = true;
                }
            }
            
            //System.out.println(f.getPath());
            
            Robot robot = new Robot();
            BufferedImage bfi = robot.createScreenCapture(new Rectangle(0, 0, 800,600));                     
            ImageIO.write((RenderedImage)bfi, "jpeg", f);            
                                   
            btnGrab.setEnabled(true);
            this.setVisible(true);            
        } catch(InterruptedException ex) {
        
        } catch(Exception ex) {
        
        }
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Grab")) {
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            btnGrab.setEnabled(false);
            this.setVisible(false);
            thread.start();    
        } else if(e.getActionCommand().equals("Browse")) {
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this) ==  fmdc.APPROVE_OPTION) {
                txfdPath.setText(fmdc.getPath());
            }              
        }
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        new ScreenCapture(); 
    }
    
    
    
}    