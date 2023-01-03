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
import java.util.*;



public class DesktopShortcut extends JLabel implements MouseListener, MouseMotionListener, ActionListener, PopupMenuListener {


    
    FileManager fm = MDS.getFileManager();
    MDS_User usr = MDS.getUser();
    
    boolean resetIcon = true;
    //File iconFile; 
    String text;
    Image subImage;
    BufferedImage surfaceImage;
    Graphics2D g2dSI;
    Image icon;
    Image stcIcon;
    Image alphaIcon;
    FontMetrics ftmtx;
    Font font = new Font("Dialog", Font.BOLD, 12);
    int w = 90;
    int h = 72;
    int x,y = 0;
    int icon_X = 0;
    int icon_Y = 0;
    int font_X = 0;
    
    Hashtable htInfo;
    File file;
    File shtFile;
    
    MDS_PopupMenu pm = new MDS_PopupMenu();
    JMenuItem mniOpen = usr.createMenuItem("Open", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"open.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    JMenuItem mniDelete = usr.createMenuItem("Delete", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"delete.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
    JMenuItem mniProperties = usr.createMenuItem("Properties", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"file-properties.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
   
    
    
    EventListenerList listenerList = new EventListenerList();


    
    public DesktopShortcut(File f, File sf, Image sub,MDS_Point location) {
        try {
            //htInfo = ht;
            subImage = sub;
            shtFile = sf;
            //iconFile = f;
            //file = (File)htInfo.get("file");
            file = f;
            text = file.getName();
            x = location.getX();
            y = location.getY();
            this.setOpaque(true);
            this.setVerticalAlignment(SwingConstants.TOP);
            //this.setHorizontalAlignment(SwingConstants.CENTER);
            surfaceImage = ImageIO.read(new File(ImageManipulator.MDS_PICTURE_PATH+"di_bg.jpg"));
            g2dSI = surfaceImage.createGraphics(); 
            g2dSI.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2dSI.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);            
            //icon = ((ImageIcon)htInfo.get("icon")).getImage();
            icon = ((ImageIcon)fm.getFileType_Icon(file)).getImage();
            //alphaIcon = ((ImageIcon)htInfo.get("icon")).getImage();
            alphaIcon = ((ImageIcon)fm.getFileType_Icon(file)).getImage();
            this.setIcon(new ImageIcon((Image)surfaceImage));
            
            g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null);
            
            icon_X = (w-icon.getWidth(null))/2;
            if(icon.getHeight(null)>=48) {
                icon_Y = 2;
            } else {
                icon_Y = (icon.getHeight(null))/2;
            }
            g2dSI.drawImage(icon, icon_X, icon_Y, null);
            stcIcon = ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"shortcut.png");
            drawArrow();
            g2dSI.setFont(font);
            ftmtx = g2dSI.getFontMetrics();
            
            if(ftmtx.stringWidth(text) > 90) {
                font_X = 10;
                int w = 10000;
                int c = text.length();
                String nText = "";
                while(w > 70) {
                    nText = text.substring(0, c);  
                    w = ftmtx.stringWidth(nText);  
                    c--;
                }
                text = nText.concat(" ...");
                g2dSI.drawString(text, font_X, 65);        
            } else {
                font_X = Math.abs((ftmtx.stringWidth(text)-w)/2);
                g2dSI.drawString(text, font_X, 65);
            }
                           
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.setSize(w, h);
            this.setLocation(location.getX(), location.getY());
            
            pm.add(mniOpen);
            pm.addSeparator();
            pm.add(mniDelete);
            pm.addSeparator();
            pm.add(mniProperties);
            
            pm.addPopupMenuListener(this);
            
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        
        } catch (Exception ex) {
             //System.out.println(ex.toString());
             ex.printStackTrace();
        }

    }
    
    
    
    public DesktopShortcut(Image sub, File f, String t, MDS_Point location, String toolTipText) {
        //this(sub, f, t, location);
        //this.setToolTipText(toolTipText);
    }
    
    
    
    public String getName() {
        return text;
    }
    
    
    
    private void drawArrow() {
        float alpha = .99f;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2dSI.setComposite(ac);         
        g2dSI.drawImage(stcIcon, 20, 30, null);     
    }
    
    
    
    private void setResetIcon() {
        this.setIcon(new ImageIcon((Image)surfaceImage));           
        g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null); 
        float alpha = .99f;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2dSI.setComposite(ac);                
        g2dSI.drawImage(icon, icon_X, icon_Y, null); 
        g2dSI.drawString(text, font_X, 65); 
        drawArrow();     
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open")) {
            MDS.getFileManager().executeFile(file);
        } else if(e.getActionCommand().equals("Delete")) {
            if(JOptionPane.showInternalConfirmDialog(MDS.getBaseWindow().getDesktop(), "Are you sure you want to delete '"+ file.getName()+"'", "Confirm File Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if(shtFile.delete()) MDS.getBaseWindow().getDesktop().refreshDesktop();
            }
        } else if(e.getActionCommand().equals("Properties")) {
            new DesktopShortcutProperties(shtFile);        
        }
    }
  
    
    
    
    public void mouseClicked(MouseEvent e){}



    public void mouseEntered(MouseEvent e){
        this.setIcon(new ImageIcon((Image)surfaceImage));            
        g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null);
        g2dSI.drawString(text, font_X, 65);
        float alpha = 0.29999996f;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2dSI.setComposite(ac);            
        g2dSI.drawImage(icon, icon_X, icon_Y, null); 
//        g2dSI.drawString(text, font_X, 65); 

        drawArrow();     
        
    }



    public void mouseExited(MouseEvent e){
        if(resetIcon) setResetIcon();            
    }



    public void mousePressed(MouseEvent e){
        if(e.getButton()==e.BUTTON1) {
            this.setIcon(new ImageIcon((Image)surfaceImage));           
            g2dSI.drawImage(subImage, 0, 0, w, h, x, y, x+w, y+h, null);      
            drawArrow();
            float alpha = .99f;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2dSI.setComposite(ac);                
            g2dSI.drawImage(icon, icon_X, icon_Y, null); 
            g2dSI.drawString(text, font_X, 65);
            
            drawArrow();          
            
            MDS.getSound().playSound(new File("Media\\Sound\\click.wav"));
            
            MDS.getFileManager().executeFile(file);
                                                  
        } else if(e.getButton()==e.BUTTON3) {
            if(e.getClickCount() == 1) {
                pm.show(this, e.getX(), e.getY());
            }
        }                  
              
    }
        
        
        
    public void mouseReleased(MouseEvent e) {} 
    
    
    
    public void mouseDragged(MouseEvent e) {}
    
    
    
    public void mouseMoved(MouseEvent e) {}
    
    
    
    public void popupMenuCanceled(PopupMenuEvent e) {}
    
    
    
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        resetIcon = true;
        setResetIcon();        
    }
    
    
    
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        resetIcon = false;
    }
    
    
    
    
    
    class DesktopShortcutProperties extends ControlModule {
    
    
    
        File targetFile;
        File shtFile;
        MDS_TabbedPane tabbedPane = new MDS_TabbedPane(); 
    
    
    
        public DesktopShortcutProperties(File sf) {
            super("Properties");
            shtFile = sf;
            try {
                ObjectInputStream o_In = new ObjectInputStream(new FileInputStream(shtFile));         
                File targetFile = (File)o_In.readObject();
                tabbedPane.add("Shortcut", new ShortcutFilePanel(shtFile));          
                tabbedPane.add("General", new TargetFilePanel(targetFile));
                tabbedPane.setSelectedIndex(1);
                this.addJComponent(tabbedPane);
                this.setSize(300, 400);
                this.setCenterScreen();
                this.setVisible(true);  
            } catch(Exception ex) {ex.printStackTrace();}           
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Ok")) {
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                this.dispose();
            }
        }         
        
        
        
        
        
        class TargetFilePanel extends MDS_Panel {
        
        
        
            MDS_Label lblIcon;
                       
            MDS_TextField txfName;
            
            JSeparator sprt1 = new JSeparator(SwingConstants.HORIZONTAL);
            
            MDS_Label lblType = new MDS_Label("Type");
            MDS_Label lblF_Type = new MDS_Label();
   
            JSeparator sprt2 = new JSeparator(SwingConstants.HORIZONTAL);
            
            MDS_Label lblLocation = new MDS_Label("Location");
            MDS_Label lblF_Location = new MDS_Label("Location");
            MDS_Label lblSize = new MDS_Label("Size");
            MDS_Label lblF_Size = new MDS_Label();
            
            JSeparator sprt3 = new JSeparator(SwingConstants.HORIZONTAL);           
            
            MDS_Label lblLastModified = new MDS_Label("Last Modified");
            MDS_Label lblF_LastModified = new MDS_Label();
            
            JSeparator sprt4 = new JSeparator(SwingConstants.HORIZONTAL);            
            
            MDS_Label lblAttributes = new MDS_Label("Attributes:");
            
            MDS_CheckBox cboReadOnly = new MDS_CheckBox("Read Only");
            
            MDS_CheckBox cboHidden = new MDS_CheckBox("Hidden");
            File tf;

        
            public TargetFilePanel(File f) {
                tf = f;
                
                lblIcon = new MDS_Label(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(tf, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
                      
                txfName = new MDS_TextField(tf.getName());                
                
                this.setLayout(null);
                lblIcon.setBounds(10, 10, 40, 40);
                this.add(lblIcon);  
                
                txfName.setBounds(70, 20, 190, 25);
                this.add(txfName);
                
                sprt1.setBounds(10, 70, 250, 4);
                this.add(sprt1);
                
                lblType.setBounds(10, 80, 100, 25);
                this.add(lblType);
                
                lblF_Type.setText(fm.getFileType(tf.getName()));
                lblF_Type.setBounds(100, 80, 160, 25);
                this.add(lblF_Type); 
                
                sprt2.setBounds(10, 110, 250, 4);
                this.add(sprt2);   
                
                lblLocation.setBounds(10, 120, 100, 25);
                this.add(lblLocation);
                
                lblF_Location.setBounds(100, 120, 160, 25);
                lblF_Location.setText(tf.getPath());
                this.add(lblF_Location);               
                                            
                lblSize.setBounds(10, 145, 100, 25);
                this.add(lblSize);  
                
                lblF_Size.setBounds(100, 145, 160, 25);
                lblF_Size.setText(fm.getFormatedFileSize(tf.length()));
                this.add(lblF_Size); 
                
                lblLastModified.setBounds(10, 170, 100, 25);
                this.add(lblLastModified); 
 
                lblF_LastModified.setBounds(100, 170, 160, 25);
                lblF_LastModified.setText(fm.getLastModified_As_String(tf.lastModified()));
                this.add(lblF_LastModified);                
                
                sprt3.setBounds(10, 200, 250, 4);
                this.add(sprt3); 
                
                lblAttributes.setBounds(10, 210, 100, 25);
                this.add(lblAttributes);
                
                cboReadOnly.setBounds(15, 240, 100, 25);
                if(file.canWrite()) {
                    cboReadOnly.setSelected(false);
                } else {
                    cboReadOnly.setSelected(true);
                }    
                this.add(cboReadOnly);
                
                cboHidden.setBounds(130, 240, 100, 25);
                cboHidden.setSelected(tf.isHidden());
                cboHidden.setEnabled(false);
                this.add(cboHidden);                
                                  
            }
            
            
            
            public String getNewFileName() {
                return txfName.getText();
            }
            
            
            
        }
        
        
        
        
        
         class ShortcutFilePanel extends MDS_Panel {
        
        
        
            MDS_Label lblIcon;
                       
            MDS_TextField txfName;
            
            JSeparator sprt1 = new JSeparator(SwingConstants.HORIZONTAL);
            
            MDS_Label lblType = new MDS_Label("Type");
            MDS_Label lblF_Type = new MDS_Label();
   
            JSeparator sprt2 = new JSeparator(SwingConstants.HORIZONTAL);
            
            MDS_Label lblLocation = new MDS_Label("Location");
            MDS_Label lblF_Location = new MDS_Label("Location");
            MDS_Label lblSize = new MDS_Label("Size");
            MDS_Label lblF_Size = new MDS_Label();
            
            JSeparator sprt3 = new JSeparator(SwingConstants.HORIZONTAL);           
            
            MDS_Label lblLastModified = new MDS_Label("Last Modified");
            MDS_Label lblF_LastModified = new MDS_Label();
            
            JSeparator sprt4 = new JSeparator(SwingConstants.HORIZONTAL);            
            
            MDS_Label lblAttributes = new MDS_Label("Attributes:");
            
            MDS_CheckBox cboReadOnly = new MDS_CheckBox("Read Only");
            
            MDS_CheckBox cboHidden = new MDS_CheckBox("Hidden");
            File sf;

        
            public ShortcutFilePanel(File f) {
                sf = f;
                lblIcon = new MDS_Label(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(sf, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
                      
                txfName = new MDS_TextField(sf.getName());                
                this.setLayout(null);
                lblIcon.setBounds(10, 10, 40, 40);
                this.add(lblIcon);  
                
                txfName.setBounds(70, 20, 190, 25);
                this.add(txfName);
                
                sprt1.setBounds(10, 70, 250, 4);
                this.add(sprt1);
                
                lblType.setBounds(10, 80, 100, 25);
                this.add(lblType);
                
                lblF_Type.setText(fm.getFileType(sf.getName()));
                lblF_Type.setBounds(100, 80, 160, 25);
                this.add(lblF_Type); 
                
                sprt2.setBounds(10, 110, 250, 4);
                this.add(sprt2);   
                
                lblLocation.setBounds(10, 120, 100, 25);
                this.add(lblLocation);
                
                lblF_Location.setBounds(100, 120, 160, 25);
                lblF_Location.setText(sf.getPath());
                this.add(lblF_Location);               
                                            
                lblSize.setBounds(10, 145, 100, 25);
                this.add(lblSize);  
                
                lblF_Size.setBounds(100, 145, 160, 25);
                lblF_Size.setText(fm.getFormatedFileSize(sf.length()));
                this.add(lblF_Size); 
                
                lblLastModified.setBounds(10, 170, 100, 25);
                this.add(lblLastModified); 
 
                lblF_LastModified.setBounds(100, 170, 160, 25);
                lblF_LastModified.setText(fm.getLastModified_As_String(sf.lastModified()));
                this.add(lblF_LastModified);                
                
                sprt3.setBounds(10, 200, 250, 4);
                this.add(sprt3); 
                
                lblAttributes.setBounds(10, 210, 100, 25);
                this.add(lblAttributes);
                
                cboReadOnly.setBounds(15, 240, 100, 25);
                if(file.canWrite()) {
                    cboReadOnly.setSelected(false);
                } else {
                    cboReadOnly.setSelected(true);
                }    
                this.add(cboReadOnly);
                
                cboHidden.setBounds(130, 240, 100, 25);
                cboHidden.setSelected(sf.isHidden());
                cboHidden.setEnabled(false);
                this.add(cboHidden);                
                                  
            }
            
            
            
            public String getNewFileName() {
                return txfName.getText();
            }
            
            
            
        }               
        
        
        
    }    
     
    
          
 
}       