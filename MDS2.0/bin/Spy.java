
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Spy extends MDS_Frame implements Runnable, ActionListener{



    MDS_User usr = MDS.getUser();

    JComponent contentPane;
    private MDS_TextArea txtaOutput = new MDS_TextArea();
    private EventQueue theQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    
    private MDS_ListModel lstmdl = new MDS_ListModel();
    private MDS_List lstOut= new MDS_List(lstmdl);
    private Thread thread;
    
    JMenuBar mnb = new JMenuBar();
    MDS_Menu mnuFile = new MDS_Menu("File", KeyEvent.VK_F); 
    JMenuItem mniClose = usr.createMenuItem("Close", this);
    
    MDS_Menu mnuSpy = new MDS_Menu("Spy", KeyEvent.VK_S);
    JMenuItem mniSpy = usr.createMenuItem("Start", this);
    
    MDS_Menu mnuHelp = new MDS_Menu("Help", KeyEvent.VK_H);
    JMenuItem mniAbout = usr.createMenuItem("About", this, KeyEvent.VK_A);    



    public Spy() {
        super("Spy",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-ktuberling.png"));       
        contentPane = (JComponent) this.getContentPane();  
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(lstOut), BorderLayout.CENTER);
        this.setSize(600, 400);
        this.setCenterScreen();
        
        mnuFile.add(mniClose);
        mnb.add(mnuFile);
        
        mnuSpy.add(mniSpy);
        mnb.add(mnuSpy);
        
        mnuHelp.add(mniAbout);
        mnb.add(mnuHelp);
        
        this.setJMenuBar(mnb);
        this.setVisible(true);
    }
    
    
    
    public static void MDS_Main(String arg[]) { 
        new Spy();
    }
    
    
    
    public void run() {
        try {
            while(true) {
                thread.sleep(50);
                AWTEvent ae = theQueue.peekEvent();
                if(ae != null) {
                    //txtaOutput.append(ae.toString()+"\n"); 
                    lstmdl.addElement(ae.toString());    
                    lstOut.ensureIndexIsVisible(lstmdl.indexOf(ae.toString()));
                    //lstmdl.setSelectedValue(ae.toString()); 
                    if(lstmdl.getSize() > 50) {
                       lstmdl.removeElementAt(0); 
                    }       
                }          
            }
        } catch(InterruptedException ex) {
        
        } catch(Exception ex1) {
            ex1.printStackTrace();
        }
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")) {
            thread = new Thread(this);
            thread.start();      
            mniSpy.setText("Stop");      
        } else if(e.getActionCommand().equals("Stop")) {
            if(thread != null) {
                thread.interrupt();
            }
            thread = null;
            mniSpy.setText("Start"); 
        } else if(e.getActionCommand().equals("Close")) {
            if(thread != null) {
                thread.interrupt();
            }
            thread = null;
            this.dispose();        
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "Spy", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"anyedit.png"), MDS.getAbout_Mahesh());
        }
    }
    
    
    
}    