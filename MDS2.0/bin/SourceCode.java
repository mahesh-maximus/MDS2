/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class SourceCode extends ScreenSaver implements Runnable {



    private static final int NUMPTS = 6;
    private static Color greenBlend = new Color(0, 255, 0, 100);
    private static Font font = new Font("serif", Font.PLAIN, 16);
    private static Color blueBlend = new Color(0, 0, 255, 100);
    private static BasicStroke bs = new BasicStroke(3.0f);
    private static Image hotj_img;
    private static BufferedImage img;
    private static final int UP = 0;
    private static final int DOWN = 1;

    private float animpts[] = new float[NUMPTS * 2];
    private float deltas[] = new float[NUMPTS * 2];
    private BufferedReader reader;      
    private int nStrs;          
    private int strH;
    private int yy, ix, iy, imgX;
    private Vector vector, appletVector;
    private float alpha = 0.2f;
    private int alphaDirection;
    protected boolean doImage, doShape, doText;
    protected boolean buttonToggle;
    Thread thr;
    boolean terminate = false;
    Image image;
    
    
    public SourceCode() {
        setBackground(Color.black);
        image = ImageManipulator.getImage(ImageManipulator.MDS_PICTURE_PATH+"SourceCode.gif");
   
        for (int i = 0; i < animpts.length; i += 2) {
            animpts[i + 0] = (float) (Math.random() * 1024);
            animpts[i + 1] = (float) (Math.random() * 768);
            deltas[i + 0] = (float) (Math.random() * 6.0 + 4.0);
            deltas[i + 1] = (float) (Math.random() * 6.0 + 4.0);
            if (animpts[i + 0] > 1024 / 2.0f) {
                deltas[i + 0] = -deltas[i + 0];
            }
            if (animpts[i + 1] > 768 / 2.0f) {
                deltas[i + 1] = -deltas[i + 1];
            }
        }
        
        FontMetrics fm = getFontMetrics(font);
        strH = fm.getAscent()+fm.getDescent();
        nStrs = 768/strH+2;//768
        vector = new Vector(nStrs);
        ix = (int) (Math.random() * (1024 - 323));
        iy = (int) (Math.random() * (768 - 323));       
        
        this.setVisible(true);
        thr = new Thread(this, "Source Code");
        thr.setPriority(Thread.MIN_PRIORITY);
        thr.start();        
    }   
    
    
    
    public void getFile() {
        try {
            String fName = "common\\srcd_text.x";
            if ((reader = new BufferedReader(new FileReader(fName))) != null) {
                getLine();
            }
        } catch (Exception e) { 
            reader = null;
            throw new RuntimeException(e);
            //MDS.getExceptionManager().showException(e);
        }

        buttonToggle = true;
    }



    public String getLine() {
        String str = null;
        if (reader != null) {
            try {
                if ((str = reader.readLine()) != null) {
                    if (str.length() == 0) {
                        str = " ";
                    }
                    vector.addElement(str);
                }
            } catch (Exception e) { 
                e.printStackTrace(); 
                reader = null; 
            }
        } else {
            if (appletVector.size() != 0) {
                vector.addElement(str = (String) appletVector.remove(0));
            }
        }
        return str;
    }

    
    
    public void terminate_Scr() {
        terminate = true;
        this.dispose();
    } 
    
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; 
        //g2.setRenderingHints(MDS.getMDS_Graphics().getRenderingHints());
              
        g2.setColor(Color.lightGray);
        g2.setFont(font);
        float y = yy;
        for (int i = 0; i < vector.size(); i++) {
            g2.drawString((String)vector.get(i), 7, y += strH);
        }

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(ac);
        g2.drawImage(image, ix, iy, this);
       
    }        
    
    
    
    public void run() {
        try {
            while(!terminate) {
            
                thr.sleep(10);
            
                if (vector.size() == 0) {
                    getFile();
                }
            
                String s = getLine();
                if (s == null || vector.size() == nStrs && vector.size() != 0) {
                    vector.removeElementAt(0);
                }
                
                yy = (s == null) ? 0 : 768 - vector.size() * strH;
       
                if (alphaDirection == UP) {
                    if ((alpha += 0.025) > .99) {
                        alphaDirection = DOWN;
                        alpha = 1.0f;
                    }
                } else if (alphaDirection == DOWN) {
                    if ((alpha -= .02) < 0.01) {
                        alphaDirection = UP;
                        alpha = 0;
                        ix = (int) (Math.random() * (1024 - 323));
                        iy = (int) (Math.random() * (768 - 323));
                    }
                }

                if ((imgX += 80) == 1024) {
                    imgX = 0;
                }
          
                repaint(); 
                
            }
        }catch(InterruptedException ex) {
        
        }         
    }      
    
    
    
    public static void MDS_Main(String arg[]) {  
        new SourceCode();
    }      
    
    
    
}    
    