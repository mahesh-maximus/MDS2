/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;



public class MDS_DesktopIcon extends JLabel implements MouseListener, MouseMotionListener, ActionListener {


    
    File iconFile; 
    String text;
    Image subImage;
    BufferedImage surfaceImage;
    Graphics2D g2dSI;
    Image icon;
    Image alphaIcon;
    FontMetrics ftmtx;
    Font font = new Font("Dialog", Font.BOLD, 12);
    int w = 90;
    int h = 72;
    int x,y = 0;
    int icon_X = 0;
    int font_X = 0;
    
    EventListenerList listenerList = new EventListenerList();
    
    Timer resetTimer = new Timer(1000, this);
    boolean resetIcon = true;


    
    public MDS_DesktopIcon(Image sub, File f, String t, MDS_Point location) {
        try {
            subImage = sub;
            iconFile = f;
            text = t;
            x = location.getX();
            y = location.getY();
            this.setOpaque(true);
            this.setVerticalAlignment(SwingConstants.TOP);
            //this.setHorizontalAlignment(SwingConstants.CENTER);
            surfaceImage = ImageIO.read(new File(ImageManipulator.MDS_PICTURE_PATH+"di_bg.jpg"));
            g2dSI = surfaceImage.createGraphics(); 
            g2dSI.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dSI.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);            
            icon = ImageManipulator.getImage(iconFile.getPath());
            alphaIcon = ImageManipulator.getImage(iconFile.getPath());
            this.setIcon(new ImageIcon((Image)surfaceImage));
            
            g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null);
            
            icon_X = (w-48)/2;
            g2dSI.drawImage(icon, icon_X, 2, null);
            
            g2dSI.setFont(font);
            ftmtx = g2dSI.getFontMetrics();
            font_X = Math.abs((ftmtx.stringWidth(text)-w)/2);
            g2dSI.drawString(text, font_X, 65);
            
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.setSize(w, h);
            this.setLocation(location.getX(), location.getY());
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        
        } catch (Exception ex) {
             System.out.println(ex.toString());
        }

    }
    
    
    
    public MDS_DesktopIcon(Image sub, File f, String t, MDS_Point location, String toolTipText) {
        this(sub, f, t, location);
        this.setToolTipText(toolTipText);
    }
    
    
    
    public String getName() {
        return text;
    }
    
    
    
    public void addDesktopIconListener(DesktopIconListener l) {
        listenerList.add(DesktopIconListener.class ,l);
    }
    
    
    
    public void removeDesktopIconListener(DesktopIconListener l) {
        listenerList.remove(DesktopIconListener.class ,l);
    }
    
    
    
    private void fireDesktopIcon_Clicked() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==DesktopIconListener.class) {
                ((DesktopIconListener)listeners[i+1]).desktopIcon_Clicked(new DesktopIconEvent(this));
            }
        }     
    }
    
    
    
    private void setResetIcon() {
        this.setIcon(new ImageIcon((Image)surfaceImage));           
        g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null);      
        float alpha = .99f;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2dSI.setComposite(ac);                
        g2dSI.drawImage(icon, icon_X, 2, null); 
        g2dSI.drawString(text, font_X, 65);      
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        setResetIcon();
        resetIcon = true;
        resetTimer.stop();        
    }
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){
        this.setIcon(new ImageIcon((Image)surfaceImage));            
        g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null);
        float alpha = 0.29999996f;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2dSI.setComposite(ac);            
        g2dSI.drawImage(icon, icon_X, 2, null); 
        g2dSI.drawString(text, font_X, 65);       
    }



    public void mouseExited(MouseEvent e){
        if(resetIcon) setResetIcon();   
    }



    public void mousePressed(MouseEvent e){
        if(e.getButton()==e.BUTTON1) {
            
            if(!resetIcon) return;
            resetIcon = false;
            resetTimer.start();
        /*
            this.setIcon(new ImageIcon((Image)surfaceImage));           
            g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null);      
            float alpha = .99f;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2dSI.setComposite(ac);                
            g2dSI.drawImage(icon, icon_X, 2, null); 
            g2dSI.drawString(text, font_X, 65);
      */      
            MDS.getSound().playSound(new File("Media\\Sound\\click.wav"));
            
            fireDesktopIcon_Clicked();
                                      
        }        
        //System.out.println("CC");   
        /*
        BufferedImage srcImg = 
            new BufferedImage(48, 48, BufferedImage.TYPE_INT_RGB);
        Graphics2D srcG = srcImg.createGraphics();
        srcG.drawImage(img, 0, 0, null);   
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp theOp = new ColorConvertOp(cs, MDS.getMDS_Graphics().getRenderingHints());

        BufferedImage dstImg = 
                new BufferedImage(48, 48, BufferedImage.TYPE_INT_RGB);
        theOp.filter(srcImg, dstImg);
        
        this.setIcon(new ImageIcon(dstImg));
         */             
              
    }
        
        
        
    public void mouseReleased(MouseEvent e) {} 
    
    
    
    public void mouseDragged(MouseEvent e) {}
    
    
    
    public void mouseMoved(MouseEvent e) {
        
    }
     
    
          
 
}    