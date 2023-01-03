
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;



public class Magnifier extends MDS_Frame implements Runnable {


 
    private static Magnifier mag;
    private static boolean mag_Visibility = false;
    JComponent contentPane;
    Thread thread;
    Mouse mouse = new Mouse();
    ImageIcon imgMag;
    ImageIcon tmpIcon;
    Robot rbt;
    MDS_Label lblImage = new MDS_Label();



    public Magnifier() {
        super("Magnifier",false, true, false, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kappfinder.png"));
        contentPane = (JComponent)this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(lblImage, BorderLayout.CENTER);
        this.setSize(300, 300);
        this.setLocation(724, 0);
        MDS.getBaseWindow().getDesktop().remove(this); 
        MDS.getBaseWindow().getDesktop().add(this ,JLayeredPane.MODAL_LAYER);        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) { 
                if(thread != null) {
                    thread.interrupt();
                }         
                thread = null;
                mag_Visibility = false;
                mag = null;
            }
        });         
        this.setVisible(true);
        mag_Visibility = true;
        mag = this;
        try {
            rbt = new Robot();
            new JButton();
            thread = new Thread(this, "Magnifier");
            thread.start();
        } catch(Exception ex) {}
    }
    
    
    
    public void run() {
        int mX = 0;
        int mY = 0;
        int x = 0;
        int y = 0;
        int w = 200;
        int h = 200;
        
        try {
            while(true) { 
                thread.sleep(50);
                mX = mouse.getMousePointerPosX();
                mY = mouse.getMousePointerPosY();
                x = mX-100;
                y = mY-100;
                
                if(x < 0) x = 0;
                if(y < 0) y = 0;
                
                if(x > 600) x = 600;
                if(y > 400) y = 400;
                Rectangle rect = new Rectangle(x, y, w, h);
                               
                tmpIcon = new ImageIcon((Image)rbt.createScreenCapture(rect));
                imgMag = new ImageIcon(tmpIcon.getImage().getScaledInstance(300, -1, Image.SCALE_FAST));
                lblImage.setIcon(imgMag);
            }               
        } catch(InterruptedException ex) {
        
        }
    }
    
    
    
    public static void MDS_Main(String arg[]) {       
        if(!Magnifier.mag_Visibility) {
            new Magnifier();
        } else {
            try{
                Magnifier.mag.setIcon(false);
                Magnifier.mag.setSelected(true);
            } catch(Exception ex) {}
        }              
    }

    
    
    
}    