/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;



public class Matrix extends ScreenSaver implements Runnable {


    
    private static boolean running = false;
     
    FontMetrics fm;
    int lineFilledLength = 14;
    int filledLengthVirtical = 20;
    boolean terminate = false;
    Thread thr;
    Vector vctPoints = new Vector();
    Font ft;
    Clip clipMatrix;


    public Matrix() {
    
        boolean goOut = false;
        
        while(!goOut) {        
            vctPoints.addElement(new MDS_Point(lineFilledLength,filledLengthVirtical));
            lineFilledLength = lineFilledLength + 20;
        
            if(lineFilledLength>= 1020) {//790
                lineFilledLength = 14;
                filledLengthVirtical = filledLengthVirtical +30;            
            } 
        
            if(filledLengthVirtical>=768) {//600
                lineFilledLength = 14;
                filledLengthVirtical = 20;  
                goOut = true;         
            }        
        } 
        
        this.setVisible(true);
        this.setBackground(Color.black);
        this.setForeground(new Color(22, 146, 29));
        ft = new Font("Impact", Font.PLAIN, 18);
        this.setFont(ft);
        
//        MDS_Sound snd = MDS.getSound();
//        //Object o = snd.loadSound(new File("Media\\Sound\\Matrix.wav"), null);
//        Object o = snd.loadSound(new File(MDS.getSound().getDefaultSoundDirectory(), "KDE_Close_Window.wav"), null);
//        clipMatrix = (Clip) o;        
//        clipMatrix.start();
//        clipMatrix.loop(Clip.LOOP_CONTINUOUSLY);
        thr = new Thread(this, "Matrix");
        thr.setPriority(Thread.MIN_PRIORITY);
        thr.start();
        running = true;
    }
    
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;  
        g2.setRenderingHints(MDS.getMDS_Graphics().getRenderingHints());
        Random rand = new Random();
        for(int x=0;x<=vctPoints.size()-1;x++) {
            MDS_Point p = (MDS_Point)vctPoints.elementAt(x);           
            int r = Math.abs(rand.nextInt() % 10);
            g2.drawString(String.valueOf(r),p.getX(),p.getY());    
        }              
    }
    
    
    
    public void terminate_Scr() {
        //clipMatrix.stop();
        terminate = true;
        this.dispose();
        running = false;
    } 
    
    
    
    public static boolean isRunning() {
        return running;
    } 
    
    
    
    public void run() {
        try {
            while(!terminate) {
                thr.sleep(50);
                repaint();  
            }
        }catch(InterruptedException ex) {
        
        }    
    }    
     
    
    
    public static void MDS_Main(String arg[]) { 
        if(!Matrix.isRunning()) { 
            new Matrix();
        }
    }
    
    
    
}    