/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;



public class MDS_StartUp_Window {



    private int pos = 0;
    
    private MDS_FullScreenWindow fsw;
    private JTextPane txtpOut = new JTextPane();
    private StyledDocument stldmt = txtpOut.getStyledDocument();



    public MDS_StartUp_Window() { 
        txtpOut.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        txtpOut.setBackground(Color.BLACK);
        txtpOut.setForeground(Color.WHITE);
        txtpOut.setSelectionColor(Color.BLACK);  
        txtpOut.setSelectedTextColor(Color.WHITE);  
        //txtpOut.setFont(new Font("Dialog", Font.PLAIN, 12));
        //txtpOut.setFont(new Font("Dialog", Font.PLAIN, 13));
        JScrollPane scrpFullScreen = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER ,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);    
        fsw = new MDS_FullScreenWindow();
        fsw.setLayout(null);
        fsw.setBackground(Color.black);
        scrpFullScreen.setViewportView(txtpOut); 
        txtpOut.setBorder(null); 
        scrpFullScreen.setBorder(null); 
        scrpFullScreen.setBounds(0, 0, 800, 400);
        fsw.add(new about_MDS());          
        fsw.add(scrpFullScreen);         
    }
    
    
    
    public void load() {
        fsw.setVisible(true); 
    }
    
    
    
    public void print(String text) {
        pos += text.length();        
        txtpOut.setCaretPosition(stldmt.getLength());
        txtpOut.replaceSelection("    "+text);
        //System.out.println(text);
    }
    


    public void println(String text) {
        pos += text.length();        
        txtpOut.setCaretPosition(stldmt.getLength());
        txtpOut.replaceSelection("    "+text+"\n"); 
        //System.out.println(text+"\n");
    }
    
    
    
    public void dispose() {
        fsw.dispose();
    } 
    
        
    
    
    
    private class about_MDS extends JPanel {
    
    
    
        Graphics2D g2;   
               
    
    
        public about_MDS() {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            this.setLayout(null);
            this.setBackground(Color.black);
            this.setBounds(0, 400, 800, 200);        
            this.add(new DE_Name());
        }
        
        
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g); 
            g2 = (Graphics2D)g;
            g2.drawImage(ImageManipulator.getImage(ImageManipulator.MDS_PICTURE_PATH + "MDS_Logo.gif"), 630, 0, 170, 200, null);           
            //g2.setRenderingHints(MDS.getMDS_Graphics().getRenderingHints()); 
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);            
            //g2.setPaint(new Color(255, 249, 4));
            g2.setFont(new Font("Impact", Font.PLAIN, 100));
            //g2.drawString("MDS", 30, 100);
                                
            g2.setFont(new Font("Impact", Font.PLAIN, 20));
            //g2.drawString("1.0", 220, 100);
            g2.setFont(new Font("Impact", Font.PLAIN, 25));
            g2.setPaint(new Color(255, 249, 250));
            g2.drawString("Mahesh Desktop System Developer's Edition", 50, 160);           
        }
        
        
        
        
        
        private class DE_Name extends JPanel {
        
        
        
            int w = 250;
            int h = 150;       
        
        
        
            public DE_Name() {
                this.setBounds(10, 10, 300, 110);
                this.setBackground(Color.black);
                //this.setOpaque(false);
                this.repaint();
            }
            
            
            
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2D = (Graphics2D)g;
                
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);                   
                
                g2D.setFont(new Font("Impact", Font.PLAIN, 100));
                FontRenderContext frc = g2D.getFontRenderContext();
                TextLayout tl = new TextLayout("MDS", g2D.getFont(), frc);
                float sw = (float) tl.getBounds().getWidth();
                float sh = (float) tl.getBounds().getHeight();
                Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(20,80));
                g2D.setColor(Color.gray);
                g2D.draw(sha);
                g2D.setPaint(new GradientPaint(0,0,new Color(255, 167, 62),w,h,new Color(255, 221, 181)));

                g2D.fill(sha);                
            }
                      
        
        }
        
        
        
    }         
    
    
    
}    