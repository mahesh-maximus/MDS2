/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;



public class ScreenSaver_MDS extends ScreenSaver implements Runnable {



    Image img;
    Thread thr;
    
    double w = 5;
    double h = 5;
    double x = 0;
    double y = 0;
    
    boolean terminate = false;
    
    Rectangle r = new Rectangle();
    
    boolean x_Plus = true;
    boolean y_Plus = true;
    
    boolean up = true;


    public ScreenSaver_MDS() {
        //img = ImageManipulator.getImage(ImageManipulator.MDS_WALLPAPER_PATH+"98490_wallpaper_liberogrande_international_02_1024[1].jpeg");
        img = MDS_Desktop.getMDS_Desktop().getWallPaper().getImage();
        this.setBackground(Color.black);
        this.setVisible(true);
        thr = new Thread(this, "Screen Server MDS");
        thr.setPriority(Thread.MIN_PRIORITY);
        thr.start();        
    }
    
    
    
    public void terminate_Scr() {
        //clipMatrix.stop();
        terminate = true;
        //thr.destroy();
    }    
    
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;    
        //g2.setRenderingHints(MDS.getMDS_Graphics().getRenderingHints());  
        FontRenderContext frc = g2.getFontRenderContext();
        Font f = new Font("sansserif",Font.BOLD,32);
        String s = new String("MDS");
        TextLayout tl = new TextLayout(s, f, frc);
   /*    
        if(up) {
            w = w + 0.01;
            h = h + 0.01;
            if(w > 10) {
                up = false;
            }    
        } else if(!up) {
            w = w - 0.01;
            h = h - 0.01;
            if(w < 2) {
                up = true;
            }              
        }
*/

        AffineTransform Tx = AffineTransform.getScaleInstance(w, h);
        Shape shape = tl.getOutline(Tx);

        Tx = AffineTransform.getTranslateInstance(x, y);
        shape = Tx.createTransformedShape(shape);

        r = shape.getBounds();
       
        g2.clip(shape);           
        
        g2.drawImage(img, 0, 0, 1024, 768, null);
        g2.setClip(new Rectangle(0, 0, 1024, 768));

        g2.setColor(Color.gray);
        g2.draw(shape); 
        
////        if(!thr.isAlive()) {
////            thr.start();
////        }       
             
    }
    
    
    
    public void run() {
        try {
            x = 1;
            y = r.getHeight()+1;

            
            while(!terminate) {
            
                thr.sleep(5);
              
              if(x >0 && x_Plus) {
                  x++;
              }
              
              if(y > r.getHeight()  && y_Plus) {
                  y++;
              }
              
              if(y == 768) {
                  y_Plus=false;
              }
              
              if(!y_Plus) {
                  y--;
              }
              
              if(x == 1024 - r.getWidth()) {
                  x_Plus = false;
              }
              
              if(!x_Plus) {
                  x--;
              }
              
              if(y == r.getHeight() && !y_Plus) {
                  y_Plus = true;
                  y++;
              }
              
              if(x == 0 && !x_Plus) {
                  x_Plus = true;
                  x++;
              }
                
             
              repaint();
            }
            
            this.dispose();
            
        } catch(InterruptedException ex) {
        
        }         
    }        



    
    public static void MDS_Main(String arg[]) {  
        new ScreenSaver_MDS();
    }    
    
    
    
}    