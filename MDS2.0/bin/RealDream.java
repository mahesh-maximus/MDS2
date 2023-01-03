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
import javax.swing.border.*;



public class RealDream extends ScreenSaver {



    private boolean terminate = false;
    private static boolean running = false;
    private Text text;
    
    

    public RealDream() {
        this.setBackground(Color.black);
        this.setVisible(true);
        this.setLayout(null);
        text = new Text();
        this.add(text);  
        running = true; 
    }
    
    
    
    private void teminate() {
        terminate = true;
        this.dispose();
        running = false;
        text.stop();
    }  
    
    
    
    public void terminate_Scr() {
        teminate();
    } 
    
    
    
    public static boolean isRunning() {
        return running;
    }      
    
    
    
    public static void MDS_Main(String arg[]) {  
        if(!RealDream.isRunning()) {
            new RealDream();
        }
    } 
    
    
    
    
    
    class Text extends JPanel implements Runnable {
    
    
    
        Thread thr;
    
    
    
        public Text() {
            this.setBackground(Color.black);
            this.setForeground(Color.green);
            Random rnd = new Random();
            int x = Math.abs(rnd.nextInt()) % (1024 - 300);
            int y = Math.abs(rnd.nextInt()) % (768 - 210);
            this.setBounds(x , y, 300, 210);  
            this.setFont(new Font("Dialog", Font.PLAIN, 14)); 
            thr = new Thread(this, "Real Dream");
            thr.setPriority(Thread.MIN_PRIORITY);
            thr.start();             
        }
        
        
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g; 
            g2.drawString("Have you ever had a dream, that you were so", 10, 10);
            g2.drawString("sure was real. What if you were unable to", 10, 30);  
            g2.drawString("wake from that dream? How would you know", 10, 50);
            g2.drawString("difference between dream world and the", 10, 70);
            g2.drawString("real world", 10, 90);       
            
            g2.drawString("How do you define 'Real'? If you are taking", 10, 120);   
            g2.drawString("about what you can feel, what you can smell,", 10, 140);
            g2.drawString("what you can taste and see, then real is", 10, 160);
            g2.drawString("simply electrical signals interpreted by", 10, 180);
            g2.drawString("your brain.", 10, 200);    
        }
        
        
        
        public void stop() {
            if(thr != null) {
                thr.interrupt();            
            }    
            thr = null;         
        } 
        
        
        
        public void run() {
            try {
                while(true) {
                    Random rnd = new Random();
                    int x = Math.abs(rnd.nextInt()) % (1024 - 300);
                    int y = Math.abs(rnd.nextInt()) % (768 - 210);
                    this.setBounds(x , y, 300, 210);  
                    this.repaint();  
                    thr.sleep(10000);                    
                }
            } catch(InterruptedException ex) {
        
            }        
        }
        
        
        
        
        
    }         
    
    
}