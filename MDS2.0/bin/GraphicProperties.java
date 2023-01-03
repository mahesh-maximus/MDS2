/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;



public class GraphicProperties extends ControlModule implements ActionListener {



    private static GraphicProperties gp;
    private static boolean gp_Visibility = false;
    
    JComponent contentPane;
    JPanel pnlOptions = new JPanel();
    MDS_CheckBox chkbAlphaInterpolation = makeCheckBox("Alpha Interpolation");
    MDS_CheckBox chkbAntialiasing = makeCheckBox("Antialiasing");
    MDS_CheckBox chkbColorRendering = makeCheckBox("Color Rendering");
    MDS_CheckBox chkbDithering = makeCheckBox("Dithering");
    MDS_CheckBox chkbFractionalmetrics = makeCheckBox("Fractionalmetrics");
    MDS_CheckBox chkbInterpolation = makeCheckBox("Interpolation");
    MDS_CheckBox chkbRendering = makeCheckBox("Rendering Speed");
    MDS_CheckBox chkbStrokeControl = makeCheckBox("Stroke Control");
    MDS_CheckBox chkbTextAntialiasing = makeCheckBox("Text Antialiasing");    
    
    String items [] = {"ALPHA_INTERPOLATION_DEFAULT", "ALPHA_INTERPOLATION_QUALITY", "ALPHA_INTERPOLATION_SPEED"};
    MDS_ComboBox cboAlphaInterpolation = makeComboBox(items);
    
    RenderingView rv = new RenderingView();
    
    
    
    public GraphicProperties() {
        super("Graphic Properties", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-package_graphics.png"));
        contentPane = this.get_CM_ContentPane();
        
        pnlOptions.setLayout(new GridLayout(9, 1));
        //pnlOptions.add(chkbAlphaInterpolation);  
        pnlOptions.add(chkbAntialiasing);  
        pnlOptions.add(chkbColorRendering);  
        pnlOptions.add(chkbDithering);  
        pnlOptions.add(chkbFractionalmetrics);  
        //pnlOptions.add(chkbInterpolation);  
        pnlOptions.add(chkbRendering);  
        //pnlOptions.add(chkbStrokeControl);  
        pnlOptions.add(chkbTextAntialiasing);
        
        //pnlOptions.add(cboAlphaInterpolation);
        //pnlOptions.add();
        //pnlOptions.add();
        //pnlOptions.add();
        //pnlOptions.add();
        //pnlOptions.add();
        
        contentPane.add(pnlOptions, BorderLayout.WEST);  
        
        contentPane.add(new JScrollPane(rv), BorderLayout.CENTER);       
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                gp_Visibility = false;
                gp = null;                      
            }
        }); 
        
        this.setSize(550, 400);
        this.setCenterScreen();
        this.setVisible(true);  
        
        gp_Visibility = true;
        gp = this;            
              
    }
    
    
    
    private MDS_CheckBox makeCheckBox(String text) {
        MDS_CheckBox chkb = new MDS_CheckBox(text);
        chkb.addActionListener(this);
        return chkb;          
    }
    
    
    
    private MDS_ComboBox makeComboBox(Object[] items) {
        MDS_ComboBox cbo = new MDS_ComboBox(items);
        cbo.addActionListener(this);
        return cbo;          
    }    
        
        
        
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Ok")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Alpha Interpolation")) {
            if(chkbAlphaInterpolation.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            } else {
                rv.set_RV_RenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            }
        } else if(e.getActionCommand().equals("Antialiasing")) {
            if(chkbAntialiasing.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            } else {
                rv.set_RV_RenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
        }  else if(e.getActionCommand().equals("Color Rendering")) {
            if(chkbColorRendering.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY); 
            } else {
                rv.set_RV_RenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            }
        }  else if(e.getActionCommand().equals("Dithering")) {
            if(chkbDithering.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            } else {
                rv.set_RV_RenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
            }
        }  else if(e.getActionCommand().equals("Fractionalmetrics")) {
            if(chkbFractionalmetrics.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            } else {
                rv.set_RV_RenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            }
        }  else if(e.getActionCommand().equals("Interpolation")) {
            if(chkbInterpolation.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
            } else {
            
            }
        }  else if(e.getActionCommand().equals("Rendering Speed")) {
            if(chkbRendering.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            } else {
                rv.set_RV_RenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            }
        }  else if(e.getActionCommand().equals("Stroke Control")) {
            if(chkbStrokeControl.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE); 
            } else {
            
            }
        }  else if(e.getActionCommand().equals("Text Antialiasing")) {
            if(chkbTextAntialiasing.isSelected()) {
                rv.set_RV_RenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            } else {
                rv.set_RV_RenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
 
            }
        } 
      
                   
    }
    
    
    
    
    
    class RenderingView extends MDS_Panel {
    
    
    
        BufferedImage bfi;
        Graphics2D g2d;
        MDS_Label lblImage;
    
    
    
        public RenderingView() {
            super(new BorderLayout());
            try {          
                bfi = ImageIO.read(new File(ImageManipulator.MDS_PICTURE_PATH+"rvp.jpg"));
                g2d = bfi.createGraphics();   
                lblImage = new MDS_Label(new ImageIcon((Image)bfi));
                this.add(lblImage);
                g2d.setFont(new Font("Dialog", Font.BOLD+Font.ITALIC, 40));
                g2d.drawString("MDS Graphics", 70, 50);
                g2d.setColor(new Color(202, 21, 26, 150));
                g2d.fillRect(180, 240, 200, 80); 
                g2d.setColor(new Color(22, 147, 0, 150));
                g2d.fillRect(20, 150, 200, 80);  
                g2d.setColor(new Color(0, 0, 0));
                g2d.drawOval(20, 20, 350, 250);  
                
                Image zoomSeg = (Image)bfi.getSubimage(70, 15, 90, 40);
                
                g2d.drawImage(zoomSeg.getScaledInstance(120, -1, Image.SCALE_SMOOTH), 65, 65, null);  
                     
            } catch(Exception ex) {
            
            }                     
        }
        
        
        private void setup() {
            try {   
             
                lblImage.setIcon(new ImageIcon((Image)bfi));    
            } catch(Exception ex) {
            
            }         
        
        }
        
        
        
        public void set_RV_RenderingHint(RenderingHints.Key hintKey, Object hintValue) {
            try {   
                this.removeAll();
                bfi = ImageIO.read(new File(ImageManipulator.MDS_PICTURE_PATH+"rvp.jpg"));
                g2d = bfi.createGraphics();   
                g2d.setRenderingHint(hintKey, hintValue);  
                g2d.setFont(new Font("Dialog", Font.BOLD+Font.ITALIC, 40));                
                g2d.drawString("MDS Graphics", 70, 50);
                g2d.setColor(new Color(202, 21, 26, 150));
                g2d.fillRect(180, 240, 200, 80); 
                g2d.setColor(new Color(22, 147, 0, 150));
                g2d.fillRect(20, 150, 200, 80);  
                g2d.setColor(new Color(0, 0, 0));
                g2d.drawOval(20, 20, 350, 250);  
                
                Image zoomSeg = (Image)bfi.getSubimage(70, 15, 90, 40);
                
                g2d.drawImage(zoomSeg.getScaledInstance(120, -1, Image.SCALE_SMOOTH), 65, 65, null);                
                
                lblImage = new MDS_Label(new ImageIcon((Image)bfi));
                this.add(lblImage, BorderLayout.CENTER); 
                this.validate();
            } catch(Exception ex) {
            
            }             
       
        }

       
    }  
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!gp_Visibility) {
            new GraphicProperties();
        } else {
            try{
                GraphicProperties.gp.setIcon(false);
                GraphicProperties.gp.setSelected(true);
            } catch(Exception ex) {}
        }
    }      

                
}