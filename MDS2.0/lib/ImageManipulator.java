/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;



public class ImageManipulator {



    public static String MDS_ICONS_PATH = "resources\\Graphics\\Icons\\";
    public static String MDS_PICTURE_PATH = "resources\\Graphics\\Pictures\\";
    public static String MDS_WALLPAPER_PATH = "resources\\Graphics\\Wallpapers\\";
    
    public static final int ICON_SCALE_TYPE = Image.SCALE_SMOOTH;
    


    public ImageManipulator() {

    }



    public static ImageIcon getImageIcon(String path) {
        Image img = null;
        JLabel jlblDummy = new JLabel();
        MediaTracker tracker = new MediaTracker(jlblDummy);
        img = Toolkit.getDefaultToolkit().createImage(path);
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
            if (tracker.isErrorAny()) {
                System.out.println("Error loading image " + path);
            }
        } catch (Exception ex) {
            //Console.printException(ex);
        }

        return new ImageIcon(path);
    }
    
    
    
    public static boolean isImage(String path) {
        boolean r = true;
        Image img = null;
        JLabel jlblDummy = new JLabel();
        MediaTracker tracker = new MediaTracker(jlblDummy);
        img = Toolkit.getDefaultToolkit().createImage(path);
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
            if(tracker.isErrorAny()) {
                r = false;
            }    
        } catch (Exception ex) {
            //Console.printException(ex);
        }            
        return r;
    }    



    public static Image getImage(String path) {
        return getImageIcon(path).getImage();  
    }   



    public static synchronized ImageIcon createScaledImageIcon(String fileName, int width, int height ,int scaleType) {

        ImageIcon ImageIconScaled=null;
        
        try {
            Toolkit tlk;
            Image nonScaledImage = Toolkit.getDefaultToolkit().createImage(fileName);
            ImageIconScaled  = new ImageIcon(nonScaledImage.getScaledInstance(width, height, scaleType));
        } catch(ClassCastException ex) {
            System.out.println(ex.toString());
        }
         catch(ArrayIndexOutOfBoundsException ex) {
            System.out.println(ex.toString());
        }
         catch(UnsupportedOperationException ex) {
            System.out.println(ex.toString());
        }
        

        return ImageIconScaled;
    }



    public static synchronized ImageIcon createScaledImageIcon(ImageIcon img, int width, int height, int scaleType) {

        ImageIcon ImageIconScaled=null;
        
        try {
            Image nonScaledImage = img.getImage();
            ImageIconScaled  = new ImageIcon(nonScaledImage.getScaledInstance(width, height, scaleType));
        } catch(ClassCastException ex) {
            System.out.println(ex.toString());
        }
         catch(ArrayIndexOutOfBoundsException ex) {
            System.out.println(ex.toString());
        }
         catch(UnsupportedOperationException ex) {
            System.out.println(ex.toString());
        }
        

        return ImageIconScaled;
    }
    
    
    
    public static synchronized Image createScaledImage(String fileName, int width, int height, int scaleType) {
        return createScaledImageIcon(fileName, width, height, scaleType).getImage();
    }
    


    public static Image createGrayScaledImage(Image img, int w, int h) {

        int pixels[] = new int[w * h]; 
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);

        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {}

        for (int loop_index = 0; loop_index < w * h; loop_index++){
            int p = pixels[loop_index];
            int red = (0xff & (p >> 16));
            int green = (0xff & (p >> 8));
            int blue = (0xff & p);
            int avg = (int) ((red + green + blue) / 3);
            pixels[loop_index] = (0xff000000 | avg << 16 | avg << 8 | avg);
        }

      return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, pixels, 0 , w));

    }
    
    
    
    public static ImageIcon createGrayScaledImageIcon(ImageIcon img, int w, int h) {
    
        return new ImageIcon(createGrayScaledImage(img.getImage(), w, h));
        
    }
    
    
    
    public static Image createBrightenScaledImage(Image img, int w, int h) {
        int pixels[] = new int[w * h]; 
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);

        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {}

        for(int loop_index =0; loop_index<w*h;loop_index++) {

            int p =pixels[loop_index];
            int red=(0xff &(p>>16))+20;
            int green= (0xff &(p>>8))+20;
            int blue = (0xff &p) + 20;
            if(red>255) red = 255;
            if(green>255) green = 255;
            if(blue>255) blue = 255;
            pixels[loop_index]=(0xff000000 |red<<16|green<<8|blue);

        }
        
        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, pixels, 0 , w));
        
                    
    }
    
    
    
    public static ImageIcon createBrightenScaledImageIcon(ImageIcon img, int w, int h) {
    
        return new ImageIcon(createBrightenScaledImage(img.getImage(), w, h));
        
    }
    
    
    
    public static void writeImage(BufferedImage bfi, String formatName, File output) {
    
        class WriteImage implements Runnable {
        
        
        
            private Thread thrd = new Thread(this);
            private RenderedImage rim; 
            private String formatName;
            File output;
            
            
            public WriteImage(RenderedImage im, String fmtName, File out) {
                rim = im;
                formatName = fmtName;
                output = out; 
                thrd.setPriority(Thread.MIN_PRIORITY);
                thrd.start();
            }
            
            
            
            public void run() {
                try {
                    ImageIO.write(rim, formatName, output);
                } catch (Exception ex) {
                
                }   
    
            } 
            
        }    
        
        new WriteImage((RenderedImage)bfi, formatName, output); 
        
                 
    }        


}