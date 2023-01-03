/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.font.*;
import java.awt.geom.*;


public class SunGlasses extends MDS_Frame implements ActionListener {


    
    MDS_User usr = MDS.getUser();
    FileManager fm = MDS.getFileManager();
    JMenuBar mnb = new JMenuBar();
    
    JMenu mnuFile = new JMenu("File");
    JMenuItem mniOpen = usr.createMenuItem("Open", this);
    JMenuItem mniSave = usr.createMenuItem("Save", this); 
    JMenuItem mniSaveAs = usr.createMenuItem("Save As", this);   
    JMenuItem mniClose = usr.createMenuItem("Close", this);
    JMenuItem mniExit = usr.createMenuItem("Exit", this);
    
    JMenu mnuEdit = new JMenu("Edit");
    JMenuItem mniUndo = usr.createMenuItem("Undo", this);
    JMenuItem mniRedo = usr.createMenuItem("Redo", this);
    
    JMenu mnuInsert = new JMenu("Insert");
    
    JMenu mnuText = new JMenu("Text");
    JMenuItem mniOutLine = usr.createMenuItem("Out Line", this);
    JMenuItem mniNormal = usr.createMenuItem("Normal", this);
    
    JMenu mnuShape = new JMenu("Shape");
    JMenuItem mniLine = usr.createMenuItem("Line", this);
    JMenuItem mniOval = usr.createMenuItem("Oval", this);
    JMenuItem mniRect = usr.createMenuItem("Rectangle", this);
    
    JMenuItem mniImage = usr.createMenuItem("Image", this);
    
    JMenu mnuImage = new JMenu("Image");
    JMenuItem mniRenderingHints = usr.createMenuItem("Rendering Hints", this);
    
    JMenu mnuHelp = new JMenu("Help");
    JMenuItem mniHelp = usr.createMenuItem("About", this);
    
    JComponent contentPane; 

    JLabel lblPalette = new JLabel();
    JScrollPane scrlPalette = new JScrollPane(lblPalette);
    
    BufferedImage currentBufferedImage;
    File imageFile;
    File imageFile_Current;
    Graphics2D currentImage_Graphics2D;
    SunGlasses sg;
    boolean imageEdited = false;
    
    int imgW = 0;
    int imgH = 0;
    
    

    public SunGlasses() {
        super("Sun Glasses", true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "sun-glasses.png"));
       
        mnuFile.add(mniOpen);
        mnuFile.add(mniSave);
        mnuFile.add(mniSaveAs);
        mnuFile.add(mniClose);
        mnuFile.add(mniExit);
        mnb.add(mnuFile);
        
        mniUndo.setEnabled(false);
        mnuEdit.add(mniUndo);
        mniRedo.setEnabled(false);
        mnuEdit.add(mniRedo);
        mnb.add(mnuEdit);
        
        mnuText.add(mniOutLine);
        mnuText.add(mniNormal);
        mnuInsert.add(mnuText);
        
        mnuShape.add(mniLine);
        mnuShape.add(mniOval);
        mnuShape.add(mniRect);
        mnuInsert.add(mnuShape);
        
        mnuInsert.add(mniImage);
        mnb.add(mnuInsert);
        
        mnuImage.add(mniRenderingHints);
        mnb.add(mnuImage);
        
        mnuHelp.add(mniHelp);
        mnb.add(mnuHelp);        
        
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout()); 
        contentPane.add(scrlPalette, BorderLayout.CENTER);      
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                if(imageFile_Current != null) {
                    if(imageFile_Current.exists()) {
                        imageFile_Current.delete();
                    } 
                }
            }
        });        
             
        this.setJMenuBar(mnb);
        this.setSize(800, 600);
        this.setVisible(true);
        setMenuBarEnabled(false);
        sg = this;
                   
    }
    
    
    
    public SunGlasses(File f) {
        this();
        openImageFile(f);
    }
    
    
    
    public void getCurrentImage() {
        try {
            currentBufferedImage = ImageIO.read(imageFile_Current);
            lblPalette.setIcon(new ImageIcon((Image)currentBufferedImage));
            currentImage_Graphics2D = currentBufferedImage.createGraphics();                            
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    
    
    public void updateCurrentImage() {
        try {
            ImageIO.write((RenderedImage)currentBufferedImage, "jpg", imageFile_Current);
            currentBufferedImage = ImageIO.read(imageFile_Current);
            lblPalette.setIcon(new ImageIcon((Image)currentBufferedImage));
            currentImage_Graphics2D = currentBufferedImage.createGraphics();                            
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    
    
    private void setMenuBarEnabled(boolean b) {
        mniSave.setEnabled(b);
        mniSaveAs.setEnabled(b);
        if(!b) {
            mniClose.setEnabled(false);
        } else {
            mniClose.setEnabled(true);
        }    
        mniOutLine.setEnabled(b);
        mniNormal.setEnabled(b);
        mnuText.setEnabled(b);
        mniLine.setEnabled(b);
        mniOval.setEnabled(b);
        mniRect.setEnabled(b);
        mnuShape.setEnabled(b);
        mniImage.setEnabled(b);
        mniRenderingHints.setEnabled(b); 
    }
    
    
    
    private void openImageFile(File f) {
        try {        
            imageFile = new File(f.getPath());
            currentBufferedImage = ImageIO.read(imageFile);
            if(currentBufferedImage.getWidth() > 800 || currentBufferedImage.getHeight() > 600) {
                MDS_OptionPane.showMessageDialog(this, "You can only edit pictures with the size less than or equal 800 by 600", "Sun Glasses", JOptionPane.INFORMATION_MESSAGE);                                 
                return;
            }
                    
            imgW = currentBufferedImage.getWidth();
            imgH = currentBufferedImage.getHeight();
                    
            lblPalette.setIcon(new ImageIcon((Image)currentBufferedImage));
            currentImage_Graphics2D = currentBufferedImage.createGraphics();
            imageFile_Current = fm.createTempFile("SG_currentBfi", null);
            ImageIO.write((RenderedImage)currentBufferedImage, "jpg", imageFile_Current);              
            setMenuBarEnabled(true);
            imageEdited = false;
            } catch (Exception ex) {}      
    }    
    
    


    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open")) {
            if(imageFile != null) {
                if(imageEdited) {
                    int r = MDS_OptionPane.showConfirmDialog(this, "The image file has changed.\n\nDo you want to save the changes?", "Sun Glasses", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(r == JOptionPane.YES_OPTION) {
                        try {
                            imageFile.delete();
                            ImageIO.write((RenderedImage)currentBufferedImage, "jpg", imageFile);    
                         } catch(Exception ex) {}                            
                     } else if(r == JOptionPane.NO_OPTION) {
                         lblPalette.setIcon(null);
                         imageFile = null; 
                     } else if(r == JOptionPane.CANCEL_OPTION) {
                         return;
                     }  
                 }                     
            }        
            
            lblPalette.setIcon(null);
            
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            Vector v = new Vector();
            v.add("png");
            v.add("gif");
            v.add("jpg");
            v.add("jpeg");
            fmfc.setFilter(v);
            fmfc.setPicturePreviewerVisible(true);     
            MDS_OptionPane.showMessageDialog(this, "You can only edit pictures with the size less than or equal 800 by 600", "Sun Glasses", JOptionPane.INFORMATION_MESSAGE);         
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {
                openImageFile(new File(fmfc.getPath()));
            }                     
        } else if(e.getActionCommand().equals("Normal")) {        
            new Insert_Text();
        } else if(e.getActionCommand().equals("Image")) { 
            new Insert_Image();
        } else if(e.getActionCommand().equals("Out Line")) {    
            new Insert_OutLineText();
        } else if(e.getActionCommand().equals("Line")) {
            new Insert_Line();   
        } else if(e.getActionCommand().equals("Oval")) {
            new Insert_Oval();
        } else if(e.getActionCommand().equals("Rectangle")) {
            new Insert_Rectangle();
        } else if(e.getActionCommand().equals("Rendering Hints")) {
            new Rendering_Hints();
        } else if(e.getActionCommand().equals("Save")) {
            try {
                imageFile.delete();
                ImageIO.write((RenderedImage)currentBufferedImage, "jpg", imageFile);    
            } catch(Exception ex) {}
        } else if(e.getActionCommand().equals("Save As")) {
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.SAVE_DIALOG);          
            Vector v = new Vector();
            v.add("jpg");
            v.add("jpeg");
            v.add("png");
            v.add("gif");
            fmfc.setFilter(v);
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) { 
                File file = new File(fmfc.getPath());
                String extension = MDS.getFileManager().getFileExtension(file.getPath());
                if(!v.contains(extension.toLowerCase())) {
                    MDS_OptionPane.showMessageDialog(this, "Unknown file extension '"+extension+"'.\n\nSupported file extensions jpg, jpeg, gif, png.", "Sun Glasses", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        ImageIO.write((RenderedImage)currentBufferedImage, extension.toLowerCase(), file);                
                    } catch(Exception ex) {
                    
                    }
                }
            }                
        } else if(e.getActionCommand().equals("Close")) {
            if(!imageEdited) {
                lblPalette.setIcon(null);
                imageFile = null; 
                return;                
            }
            int r = MDS_OptionPane.showConfirmDialog(this, "The image file has changed.\n\nDo you want to save the changes?", "Sun Glasses", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(r == JOptionPane.YES_OPTION) {
                try {
                    imageFile.delete();
                    ImageIO.write((RenderedImage)currentBufferedImage, "jpg", imageFile);    
                } catch(Exception ex) {}                            
            } else if(r == JOptionPane.NO_OPTION) {
                lblPalette.setIcon(null);
                imageFile = null;                
            } else if(r == JOptionPane.CANCEL_OPTION) {
                return;
            }         
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "Sun Glasses", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"sun-glasses.png"), MDS.getAbout_Mahesh());
        }

    }
    
   
    
    public static void MDS_Main(String arg[]) { 
        if(arg.length == 1) new SunGlasses(new File(arg[0])); 
        else new SunGlasses();
    }      
    
    
    
    
    private class SG_Dialog extends MDS_Dialog implements ActionListener {
    
    
    
        JComponent contentPane; 
        MDS_Panel pnlButtonContainer_1 = new MDS_Panel();
        MDS_Panel pnlButtonContainer_2 = new MDS_Panel();
        MDS_Button btnBack = new MDS_Button("Back");
        MDS_Panel pnlButtonContainer_3 = new MDS_Panel();
        MDS_Button btnOk = new MDS_Button("Ok");
        MDS_Button btnCancel = new MDS_Button("Cancel");
        MDS_Button btnApply = new MDS_Button("Apply");
        
        MDS_Panel panel = new MDS_Panel();

    
    
        public SG_Dialog(String name) {
            //super("Sun Glasses", false, true, false, false, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "sun-glasses.png"));
            //this.setTitle_Visible_In_TaskBar(false);
            super(sg, name);
            
            lblPalette.setEnabled(true);
            scrlPalette.getHorizontalScrollBar().setEnabled(true);
            scrlPalette.getVerticalScrollBar().setEnabled(true);
            
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(new BorderLayout()); 
            panel.setLayout(null);
            contentPane.add(panel, BorderLayout.CENTER);
            pnlButtonContainer_1.setLayout(new BorderLayout());
            pnlButtonContainer_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            btnBack.addActionListener(this);
            pnlButtonContainer_2.add(btnBack);
            pnlButtonContainer_1.add(pnlButtonContainer_2 ,BorderLayout.WEST);
            
            pnlButtonContainer_3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            pnlButtonContainer_3.add(btnOk);
            btnOk.addActionListener(this);
            pnlButtonContainer_3.add(btnCancel);
            btnCancel.addActionListener(this);
            btnApply.addActionListener(this);
            pnlButtonContainer_3.add(btnApply);
            
            pnlButtonContainer_1.add(pnlButtonContainer_3, BorderLayout.CENTER);
            
            contentPane.add(pnlButtonContainer_1, BorderLayout.SOUTH);
            
            this.setClosable(false);
            //this.setSize(400, 300);
            //this.setCenterScreen();
            //this.setVisible(true);            
                                                    
        }
        
        
        
        public MDS_Panel getPanel() {
            return panel;
        }
        
        
        
        public void actionPerformed(ActionEvent e) {}
        
        
        
    }     
    
    
    
    
    
    private class Insert_Image extends SG_Dialog implements ActionListener, ChangeListener {
    
    
    
        MDS_Panel basePanel;
        MDS_Label lblPath = new MDS_Label("Path:");
        MDS_TextField txfdPath = new MDS_TextField();
        MDS_Button btnBrowse = new MDS_Button("Browse");
        MDS_Slider sldAlpha;
        MDS_List lstAlpha; 
        MDS_Label lblX = new MDS_Label("X:");
        MDS_Label lblY = new MDS_Label("Y:");
        MDS_TextField txfdX = new MDS_TextField();
        MDS_TextField txfdY = new MDS_TextField(); 
        
        MDS_ComboBox cboX;     
        MDS_ComboBox cboY;   
        
        Vector vctAlpha = new Vector();
        //int c = 40;  
        Float alpha = new Float("0");
        
        File insertFile;
        
    
    
        public Insert_Image() {
            super("Insert Image");
            basePanel = this.getPanel();    
            lblPath.setBounds(10, 10, 50, 25);
            basePanel.add(lblPath);
            
            txfdPath.setEditable(false);
            txfdPath.setBounds(10, 40, 250, 25);
            basePanel.add(txfdPath);

            btnBrowse.setBounds(270, 40, 90, 25);
            btnBrowse.addActionListener(this);
            basePanel.add(btnBrowse);
            
            MDS_Label lblAlpha = new MDS_Label("Alpha");
            lblAlpha.setBounds(10, 90, 40, 25);
            basePanel.add(lblAlpha); 
            sldAlpha = new MDS_Slider(0, 39, 0);
            sldAlpha.setName("Alpha");
            sldAlpha.addChangeListener(this);
            sldAlpha.setBounds(10, 110, 250, 50);
            sldAlpha.putClientProperty("JSlider.isFilled", Boolean.TRUE);
            sldAlpha.setPaintTicks(true);
            sldAlpha.setPaintLabels(true);
            sldAlpha.setMinorTickSpacing(1);       
            basePanel.add(sldAlpha); 

            for(float x = 0; x <= .99; x += 0.025) {
                vctAlpha.addElement(String.valueOf(x));
            }
            

            lstAlpha = new MDS_List(vctAlpha);
            lstAlpha.setSelectionBackground(Color.white);
            JScrollPane scrl_lstAlpha = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);                
            scrl_lstAlpha.setBounds(270, 116, 90, 24);
            scrl_lstAlpha.setViewportView(lstAlpha);
            basePanel.add(scrl_lstAlpha);           
            
            lblX.setBounds(10, 170, 20, 25);
            basePanel.add(lblX);   
            
            Vector vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
                //c++;
            }
            cboX = new MDS_ComboBox(vctXY);
            cboX.setBounds(30, 170, 60, 25);
            basePanel.add(cboX);              
    
            lblY.setBounds(110, 170, 20, 25);
            basePanel.add(lblY); 
            
            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
                //c++;
            }              
            
            cboY = new MDS_ComboBox(vctXY);
            cboY.setBounds(130, 170, 60, 25);
            basePanel.add(cboY);
            
            JSeparator sprt1 = new JSeparator();
            sprt1.setBounds(10, 225, 360, 3);
            basePanel.add(sprt1);            
                                               
            this.setSize(390, 300);  
            this.setCenterScreen(); 
            this.setVisible(true);   
        }     
        
        
        
        private void updatePalette() {
            getCurrentImage();
            if(txfdPath.getText().equals("")) {
                MDS_OptionPane.showMessageDialog(this, "You have to select an image file to insert", "Sun Glasses", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.floatValue());
            currentImage_Graphics2D.setComposite(ac);
            currentImage_Graphics2D.drawImage(ImageManipulator.getImage(insertFile.getPath()), Integer.parseInt(String.valueOf(cboX.getSelectedItem())), Integer.parseInt(String.valueOf(cboY.getSelectedItem())), null);                
            lblPalette.repaint();  
            imageEdited = true;      
        }          
    
    
    
        public void actionPerformed(ActionEvent e) {
            if(e.	getActionCommand().equals("Browse")) {
                MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
                Vector v = new Vector();
                v.add("png");
                v.add("gif");
                v.add("jpg");
                v.add("jpeg");
                fmfc.setFilter(v);              
                if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {
                    try {
                        insertFile = new File(fmfc.getPath());
                        txfdPath.setText(insertFile.getPath());
                    } catch (Exception ex) {
                
                    }    
                }                
            } else if(e.getActionCommand().equals("Apply")) {
                updatePalette();
            } else if(e.getActionCommand().equals("Back")) {
                getCurrentImage(); 
            } else if(e.getActionCommand().equals("Ok")) {
                updatePalette();
                updateCurrentImage();
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                getCurrentImage();
                this.dispose();
            }
        }
        
        
        
        public void stateChanged(ChangeEvent e) {
            JSlider sldEvt = (JSlider)e.getSource();        
            if(sldEvt.getName().equals("Alpha")) {
                lstAlpha.setSelectedValue(String.valueOf(vctAlpha.elementAt(sldEvt.getValue())), true);
                alpha = new Float(String.valueOf(lstAlpha.getSelectedValue()));
                //System.out.println(alpha.floatValue());
            }
        }
        
    }
    
    
    
    
    
    private class Insert_OutLineText extends SG_Dialog implements ActionListener {
    
    
    
        MDS_Panel basePanel;
        MDS_Label lblText = new MDS_Label("Text:");
        MDS_TextField txfdText = new MDS_TextField();
        MDS_Label lblX = new MDS_Label("X:");
        MDS_Label lblY = new MDS_Label("Y:");
        MDS_TextField txfdX = new MDS_TextField();
        MDS_TextField txfdY = new MDS_TextField();
        MDS_Button btnFont = new MDS_Button("Font");
        MDS_Button btnColor_start = new MDS_Button("Start Color");
        MDS_Button btnColor_end = new MDS_Button("End Color");
        MDS_Button btnColor_outLine = new MDS_Button("Outline Color");

        MDS_ComboBox cboX;     
        MDS_ComboBox cboY;
        
        Color startColor = Color.black; 
        Color endColor = Color.black;
        Color outColor; 
        Font font;
              
    
    
        public Insert_OutLineText() {
            super("Insert Outline Text");
            basePanel = this.getPanel(); 
            lblText.setBounds(10, 10, 30, 25); 
            basePanel.add(lblText);  
            
            txfdText.setBounds(10, 40, 365, 25);
            basePanel.add(txfdText);
            
            lblX.setBounds(10, 90, 20, 25);
            basePanel.add(lblX);   

            Vector vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboX = new MDS_ComboBox(vctXY);
            cboX.setBounds(30, 90, 60, 25);
            basePanel.add(cboX);

            lblY.setBounds(110, 90, 20, 25);
            basePanel.add(lblY);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboY = new MDS_ComboBox(vctXY);
            cboY.setBounds(130, 90, 60, 25);
            basePanel.add(cboY);
            
            btnColor_start.setBounds(40, 140, 100, 28);
            btnColor_start.addActionListener(this);
            basePanel.add(btnColor_start);            
            
            btnColor_end.setBounds(150, 140, 100, 28);
            btnColor_end.addActionListener(this);
            basePanel.add(btnColor_end);    
            
            btnColor_outLine.setBounds(260, 140, 110, 28);
            btnColor_outLine.addActionListener(this);
            basePanel.add(btnColor_outLine);                    
                                    
            btnFont.setBounds(300, 90, 70, 28);
            btnFont.addActionListener(this);
            basePanel.add(btnFont); 
 
                       
            JSeparator sprt1 = new JSeparator();
            sprt1.setBounds(10, 195, 370, 3);
            basePanel.add(sprt1);
            
            this.setSize(400, 270); 
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        private void updatePalette() {
            getCurrentImage();
            int w = 800;
            int h = 600;
            if(font != null) 
            currentImage_Graphics2D.setFont(font); 
            FontRenderContext frc = currentImage_Graphics2D.getFontRenderContext();
            TextLayout tl = new TextLayout(txfdText.getText(), currentImage_Graphics2D.getFont(), frc);
            float sw = (float) tl.getBounds().getWidth();
            float sh = (float) tl.getBounds().getHeight();
            //Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(w/2-sw/2,h*0.5+sh/2));
            Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(Integer.parseInt(String.valueOf(cboX.getSelectedItem())), Integer.parseInt(String.valueOf(cboY.getSelectedItem()))));
            if(outColor != null)
            currentImage_Graphics2D.setColor(outColor);
            currentImage_Graphics2D.draw(sha);
            //currentImage_Graphics2D.setColor(Color.red);
            currentImage_Graphics2D.setPaint(new GradientPaint(0,0,startColor,w,h,endColor));
            currentImage_Graphics2D.fill(sha);                
            //currentImage_Graphics2D.drawString(txfdText.getText(), Integer.parseInt(txfdX.getText()), Integer.parseInt(txfdY.getText()));        
            lblPalette.repaint();
            imageEdited = true;
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Font")) {
                MDS_FontChooser fchs = new MDS_FontChooser(currentImage_Graphics2D.getFont());    
                if(fchs.showFontChooser(this) == fchs.APPROVE_OPTION) {
                    font = fchs.getFont();
                }
            } else if(e.getActionCommand().equals("Start Color")) {
                MDS_ColorChooser cc = new MDS_ColorChooser();
                if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                    startColor = cc.getColor();
                }   
            } else if(e.getActionCommand().equals("End Color")) {
                MDS_ColorChooser cc = new MDS_ColorChooser();
                if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                    endColor = cc.getColor();
                }                  
            } else if(e.getActionCommand().equals("Apply")) {
                updatePalette();
            } else if(e.getActionCommand().equals("Back")) {
                getCurrentImage();
            } else if(e.getActionCommand().equals("Outline Color")) {
                MDS_ColorChooser cc = new MDS_ColorChooser();
                if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                    outColor = cc.getColor();
                }                   
            } else if(e.getActionCommand().equals("Ok")) {
                updatePalette();
                updateCurrentImage();
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                getCurrentImage();
                this.dispose();
            }
        }
        
        
        
    }
    
    
    
    
    
    private class Insert_Text extends SG_Dialog implements ActionListener {
    
    
    
        MDS_Panel basePanel;
        MDS_Label lblText = new MDS_Label("Text:");
        MDS_TextField txfdText = new MDS_TextField();
        MDS_Label lblX = new MDS_Label("X:");
        MDS_Label lblY = new MDS_Label("Y:");
        MDS_TextField txfdX = new MDS_TextField();
        MDS_TextField txfdY = new MDS_TextField();
        MDS_Button btnFont = new MDS_Button("Font");
        MDS_Button btnColor = new MDS_Button("Color");

        MDS_ComboBox cboX;     
        MDS_ComboBox cboY;
        
        Color fColor;
        Font font; 
              
    
    
        public Insert_Text() {
            super("Insert Outline Text");
            basePanel = this.getPanel(); 
            lblText.setBounds(10, 10, 30, 25); 
            basePanel.add(lblText);  
            
            txfdText.setBounds(10, 40, 365, 25);
            basePanel.add(txfdText);
            
            lblX.setBounds(10, 90, 20, 25);
            basePanel.add(lblX);   

            Vector vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboX = new MDS_ComboBox(vctXY);
            cboX.setBounds(30, 90, 60, 25);
            basePanel.add(cboX);

            lblY.setBounds(110, 90, 20, 25);
            basePanel.add(lblY);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboY = new MDS_ComboBox(vctXY);
            cboY.setBounds(130, 90, 60, 25);
            basePanel.add(cboY);
            
            btnColor.setBounds(220, 90, 70, 28);
            btnColor.addActionListener(this);
            basePanel.add(btnColor);           
                                    
            btnFont.setBounds(300, 90, 70, 28);
            btnFont.addActionListener(this);
            basePanel.add(btnFont); 
 
                       
            JSeparator sprt1 = new JSeparator();
            sprt1.setBounds(10, 145, 370, 3);
            basePanel.add(sprt1);
            
            this.setSize(400, 220); 
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        private void updatePalette() {
            getCurrentImage();
            if(fColor != null) {
                currentImage_Graphics2D.setColor(fColor);
            }
            if(font != null) {
                currentImage_Graphics2D.setFont(font);
            }    
            currentImage_Graphics2D.drawString(txfdText.getText(), Integer.parseInt(String.valueOf(cboX.getSelectedItem())), Integer.parseInt(String.valueOf(cboY.getSelectedItem())));
            lblPalette.repaint();
            imageEdited = true;
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Font")) {
                MDS_FontChooser fchs = new MDS_FontChooser(currentImage_Graphics2D.getFont());    
                if(fchs.showFontChooser(this) == fchs.APPROVE_OPTION) {
                    font = fchs.getFont();
                }
            }else if(e.getActionCommand().equals("Color")) {
                MDS_ColorChooser cc = new MDS_ColorChooser(currentImage_Graphics2D.getColor());
                if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                    fColor = cc.getColor();
                }                
            } else if(e.getActionCommand().equals("Apply")) {
                updatePalette();
            } else if(e.getActionCommand().equals("Back")) {
                getCurrentImage();
            } else if(e.getActionCommand().equals("Ok")) {
                updatePalette();
                updateCurrentImage();
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                getCurrentImage();
                this.dispose();
            }
        }
        
        
        
    }     
    
    
    
    
    
    private class Insert_Line extends SG_Dialog implements ActionListener {
    
    
    
        JPanel basePanel;
        MDS_Label lblX1 = new MDS_Label("X1:");
        MDS_Label lblY1 = new MDS_Label("Y1:");
        MDS_Label lblX2 = new MDS_Label("X2:");
        MDS_Label lblY2 = new MDS_Label("Y2:");        
        MDS_TextField txfdX1 = new MDS_TextField();
        MDS_TextField txfdY1 = new MDS_TextField();
        MDS_TextField txfdX2 = new MDS_TextField();
        MDS_TextField txfdY2 = new MDS_TextField();        
        MDS_Button btnFont = new MDS_Button("Font");
        MDS_Button btnColor = new MDS_Button("Color");
    
        MDS_ComboBox cboX1;     
        MDS_ComboBox cboY1;
        MDS_ComboBox cboX2;     
        MDS_ComboBox cboY2;   
        
        Color color;
                 
    
        public Insert_Line() {
            super("Insert Line");
            basePanel = this.getPanel(); 

            
            lblX1.setBounds(10, 10, 20, 25);
            basePanel.add(lblX1);   

            Vector vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboX1 = new MDS_ComboBox(vctXY);
            cboX1.setBounds(30, 10, 45, 25);
            basePanel.add(cboX1);

            lblY1.setBounds(90, 10, 20, 25);
            basePanel.add(lblY1);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }            
            cboY1 = new MDS_ComboBox(vctXY);
            cboY1.setBounds(110, 10, 45, 25);
            basePanel.add(cboY1);

            lblX2.setBounds(170, 10, 20, 25);
            basePanel.add(lblX2);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }            
            cboX2 = new MDS_ComboBox(vctXY);
            cboX2.setBounds(190, 10, 45, 25);
            basePanel.add(cboX2);

            lblY2.setBounds(250, 10, 20, 25);
            basePanel.add(lblY2);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }            
            cboY2 = new MDS_ComboBox(vctXY);
            cboY2.setBounds(270, 10, 45, 25);
            basePanel.add(cboY2);
 
            btnColor.setBounds(240, 60, 70, 28);
            btnColor.addActionListener(this);
            basePanel.add(btnColor);           
                                                
                        
            JSeparator sprt1 = new JSeparator();
            sprt1.setBounds(10, 125, 320, 3);
            basePanel.add(sprt1);
            
            this.setSize(350, 200);  
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        private void updatePalette() {
            getCurrentImage();
            if(color != null) {
                currentImage_Graphics2D.setColor(color);
            }   
            currentImage_Graphics2D.drawLine(Integer.parseInt(String.valueOf(cboX1.getSelectedItem())), Integer.parseInt(String.valueOf(cboY1.getSelectedItem())), Integer.parseInt(String.valueOf(cboX2.getSelectedItem())), Integer.parseInt(String.valueOf(cboY2.getSelectedItem())));
            lblPalette.repaint();
            imageEdited = true;
        }       
        
        
        
        public void actionPerformed(ActionEvent e) {

            if(e.getActionCommand().equals("Color")) {
                MDS_ColorChooser cc = new MDS_ColorChooser();
                if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                    color = cc.getColor();
                }                
            } else if(e.getActionCommand().equals("Apply")) {
                updatePalette(); 
            } else if(e.getActionCommand().equals("Back")) {
                getCurrentImage();
            } else if(e.getActionCommand().equals("Ok")) {
                updatePalette();
                updateCurrentImage();
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                getCurrentImage();
                this.dispose();
            }

        }
        
        
        
    } 
    
    
    
    
    
    private class Insert_Oval extends SG_Dialog implements ActionListener {
    
    
    
        JPanel basePanel;
        JLabel lblX = new JLabel("X:");
        JLabel lblY = new JLabel("Y:");
        JLabel lblWidth = new JLabel("Width:");
        JLabel lblHeight = new JLabel("Height:");        
        MDS_TextField txfdX = new MDS_TextField();
        MDS_TextField txfdY = new MDS_TextField();
        MDS_TextField txfdWidth = new MDS_TextField();
        MDS_TextField txfdHeight = new MDS_TextField(); 
        MDS_CheckBox chbFill = new MDS_CheckBox("Fill Oval");       
        MDS_Button btnFont = new MDS_Button("Font");
        MDS_Button btnColor = new MDS_Button("Color");
        
        MDS_ComboBox cboX;     
        MDS_ComboBox cboY;       

        MDS_ComboBox cboW;     
        MDS_ComboBox cboH; 
        
        Color color;  
        
        
    
        public Insert_Oval() {
            super("Insert Oval");
            basePanel = this.getPanel(); 

            
            lblX.setBounds(10, 10, 20, 25);
            basePanel.add(lblX);  
            
            Vector vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboX = new MDS_ComboBox(vctXY);             
            cboX.setBounds(30, 10, 45, 25);
            basePanel.add(cboX);

            lblY.setBounds(90, 10, 20, 25);
            basePanel.add(lblY);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboY = new MDS_ComboBox(vctXY); 
            cboY.setBounds(110, 10, 45, 25);
            basePanel.add(cboY);

            lblWidth.setBounds(170, 10, 50, 25);
            basePanel.add(lblWidth);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboW = new MDS_ComboBox(vctXY); 
            cboW.setBounds(210, 10, 45, 25);
            basePanel.add(cboW);

            lblHeight.setBounds(270, 10, 50, 25);
            basePanel.add(lblHeight);   
           
            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboH = new MDS_ComboBox(vctXY);             
            cboH.setBounds(313, 10, 45, 25);
            basePanel.add(cboH);
            
            chbFill.setBounds(30, 60, 100, 28);
            basePanel.add(chbFill);
 
            btnColor.setBounds(240, 60, 70, 28);
            btnColor.addActionListener(this);
            basePanel.add(btnColor);           
                                                
                        
            JSeparator sprt1 = new JSeparator();
            sprt1.setBounds(10, 125, 420, 3);
            basePanel.add(sprt1);
            
            this.setSize(400, 200);  
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        private void updatePalette() {
            getCurrentImage();
            if(color != null) {
                currentImage_Graphics2D.setColor(color);
            } 
            if(chbFill.isSelected()) {   
                currentImage_Graphics2D.fillOval(Integer.parseInt(String.valueOf(cboX.getSelectedItem())), Integer.parseInt(String.valueOf(cboY.getSelectedItem())), Integer.parseInt(String.valueOf(cboW.getSelectedItem())), Integer.parseInt(String.valueOf(cboH.getSelectedItem())));                        
            } else if(!chbFill.isSelected()) {
                currentImage_Graphics2D.drawOval(Integer.parseInt(String.valueOf(cboX.getSelectedItem())), Integer.parseInt(String.valueOf(cboY.getSelectedItem())), Integer.parseInt(String.valueOf(cboW.getSelectedItem())), Integer.parseInt(String.valueOf(cboH.getSelectedItem())));            
            }
            lblPalette.repaint();
            imageEdited = true;
        }        
        
        
        
        public void actionPerformed(ActionEvent e) {

            if(e.getActionCommand().equals("Color")) {
                MDS_ColorChooser cc = new MDS_ColorChooser();
                if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                    color = cc.getColor();
                }                
            } else if(e.getActionCommand().equals("Apply")) {
                updatePalette();
            } else if(e.getActionCommand().equals("Back")) {
                getCurrentImage();
            } else if(e.getActionCommand().equals("Ok")) {
                updatePalette();
                updateCurrentImage();
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                getCurrentImage();
                this.dispose();
            }
        }
        
        
        
    }
    
    
    
    
    
    private class Insert_Rectangle extends SG_Dialog implements ActionListener {
    
    
    
        JPanel basePanel;
        JLabel lblX = new JLabel("X:");
        JLabel lblY = new JLabel("Y:");
        JLabel lblWidth = new JLabel("Width:");
        JLabel lblHeight = new JLabel("Height:");
        JLabel lblArcWidth = new JLabel("Arc Width:");
        JLabel lblArcHeight = new JLabel("Acr Height:");                       
        MDS_TextField txfdX = new MDS_TextField();
        MDS_TextField txfdY = new MDS_TextField();
        MDS_TextField txfdWidth = new MDS_TextField();
        MDS_TextField txfdHeight = new MDS_TextField();
        MDS_TextField txfdArcWidth = new MDS_TextField();
        MDS_TextField txfdArcHeight = new MDS_TextField();           
        JCheckBox chb3D = new JCheckBox("3D Rectangle");
        JCheckBox chbFill = new JCheckBox("Fill Rectangle");       
        MDS_Button btnFont = new MDS_Button("Font");
        MDS_Button btnColor = new MDS_Button("Color");
        
        MDS_ComboBox cboX;     
        MDS_ComboBox cboY;       

        MDS_ComboBox cboW;     
        MDS_ComboBox cboH;         

        MDS_ComboBox cboArcWidth;     
        MDS_ComboBox cboArcHeight; 
        
        Color color;    
    
        public Insert_Rectangle() {
            super("Insert Rectangle");
            basePanel = this.getPanel(); 

            
            lblX.setBounds(10, 10, 20, 25);
            basePanel.add(lblX);   

            Vector vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboX = new MDS_ComboBox(vctXY);             
            cboX.setBounds(30, 10, 45, 25);
            basePanel.add(cboX);

            lblY.setBounds(90, 10, 20, 25);
            basePanel.add(lblY);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboY = new MDS_ComboBox(vctXY); 
            cboY.setBounds(110, 10, 45, 25);
            basePanel.add(cboY);

            lblWidth.setBounds(170, 10, 50, 25);
            basePanel.add(lblWidth);   

            vctXY = new Vector();
            
            for(int x = 0; x <= imgW; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboW = new MDS_ComboBox(vctXY); 
            cboW.setBounds(210, 10, 45, 25);
            basePanel.add(cboW);

            lblHeight.setBounds(270, 10, 50, 25);
            basePanel.add(lblHeight);   
           
            vctXY = new Vector();
            
            for(int x = 0; x <= imgH; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboH = new MDS_ComboBox(vctXY);             
            cboH.setBounds(313, 10, 45, 25);
            basePanel.add(cboH);
            /*
            chb3D.setBounds(10, 50, 100, 28);
            //basePanel.add(chb3D);
            
            lblArcWidth.setBounds(130, 50, 60, 28);
            //basePanel.add(lblArcWidth);
            
            vctXY = new Vector();
            
            for(int x = 0; x <= 800; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboArcWidth = new MDS_ComboBox(vctXY);             
 
            cboArcWidth.setBounds(192, 50, 45, 25);
            //basePanel.add(cboArcWidth);
            
            lblArcHeight.setBounds(250, 50, 70, 28);
            //basePanel.add(lblArcHeight);
            
            vctXY = new Vector();
            
            for(int x = 0; x <= 800; x ++) {
                vctXY.addElement(String.valueOf(x));
            }
            
            cboArcHeight = new MDS_ComboBox(vctXY);                          

            cboArcHeight.setBounds(317, 50, 45, 25);
            //basePanel.add(cboArcHeight);
            */                                   
            chbFill.setBounds(30, 60, 100, 28);
            basePanel.add(chbFill);
 
            btnColor.setBounds(240, 60, 70, 28);
            btnColor.addActionListener(this);
            basePanel.add(btnColor);           
                                                
                        
            JSeparator sprt1 = new JSeparator();
            sprt1.setBounds(10, 125, 370, 3);
            basePanel.add(sprt1);
            
            this.setSize(400, 200);  
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        private void updatePalette() {
            getCurrentImage();
            if(color != null) {
                currentImage_Graphics2D.setColor(color);
            } 
            if(chbFill.isSelected()) {   
                currentImage_Graphics2D.fillRect(Integer.parseInt(String.valueOf(cboX.getSelectedItem())), Integer.parseInt(String.valueOf(cboY.getSelectedItem())), Integer.parseInt(String.valueOf(cboW.getSelectedItem())), Integer.parseInt(String.valueOf(cboH.getSelectedItem())));                        
            } else if(!chbFill.isSelected()) {
                currentImage_Graphics2D.drawRect(Integer.parseInt(String.valueOf(cboX.getSelectedItem())), Integer.parseInt(String.valueOf(cboY.getSelectedItem())), Integer.parseInt(String.valueOf(cboW.getSelectedItem())), Integer.parseInt(String.valueOf(cboH.getSelectedItem())));            
            }
            lblPalette.repaint();
            imageEdited = true;
        }        
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Color")) {
                MDS_ColorChooser cc = new MDS_ColorChooser();
                if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                    color = cc.getColor();
                }                
            } else if(e.getActionCommand().equals("Apply")) {
                updatePalette();
            } else if(e.getActionCommand().equals("Back")) {
                getCurrentImage();
            } else if(e.getActionCommand().equals("Ok")) {
                updatePalette();
                updateCurrentImage();
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                getCurrentImage();
                this.dispose();
            }
        }
        
        
        
    }
    
    
    
    private class Rendering_Hints extends MDS_Dialog implements ActionListener {
    
    
    
        JComponent contentPane;
    
        MDS_CheckBox chkbAlphaInterpolation = makeCheckBox("Alpha Interpolation");
        MDS_CheckBox chkbAntialiasing = makeCheckBox("Antialiasing");
        MDS_CheckBox chkbColorRendering = makeCheckBox("Color Rendering");
        MDS_CheckBox chkbDithering = makeCheckBox("Dithering");
        MDS_CheckBox chkbFractionalmetrics = makeCheckBox("Fractionalmetrics");
        MDS_CheckBox chkbInterpolation = makeCheckBox("Interpolation");
        MDS_CheckBox chkbRendering = makeCheckBox("Rendering Speed");
        MDS_CheckBox chkbStrokeControl = makeCheckBox("Stroke Control");
        MDS_CheckBox chkbTextAntialiasing = makeCheckBox("Text Antialiasing");     
    
        MDS_Button btnOk = new MDS_Button("Ok");
        MDS_Button btnCancel = new MDS_Button("Cancel");
    
    
    
        public Rendering_Hints() {
            super(sg, "Rendering Hints");
            
            lblPalette.setEnabled(true);
            scrlPalette.getHorizontalScrollBar().setEnabled(true);
            scrlPalette.getVerticalScrollBar().setEnabled(true);            
            
            contentPane = (JComponent)this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            MDS_Panel pnlOptions = new MDS_Panel(new GridLayout(6, 1));
            
            if(currentImage_Graphics2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING).equals(RenderingHints.VALUE_ANTIALIAS_ON))
            chkbAntialiasing.setSelected(true);
            pnlOptions.add(chkbAntialiasing); 
            
            //if(currentImage_Graphics2D.getRenderingHint(RenderingHints.KEY_COLOR_RENDERING).equals(RenderingHints.VALUE_COLOR_RENDER_QUALITY))
            //chkbColorRendering.setSelected(true);
            chkbColorRendering.setEnabled(false);
            pnlOptions.add(chkbColorRendering); 
            
            //if(currentImage_Graphics2D.getRenderingHint(RenderingHints.KEY_DITHERING).equals(RenderingHints.VALUE_DITHER_ENABLE)) 
            //chkbDithering.setSelected(true);
            chkbColorRendering.setEnabled(false);
            pnlOptions.add(chkbDithering);  
            
            if(currentImage_Graphics2D.getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS).equals(RenderingHints.VALUE_FRACTIONALMETRICS_ON))
            chkbFractionalmetrics.setSelected(true);
            pnlOptions.add(chkbFractionalmetrics);  
            
            if(currentImage_Graphics2D.getRenderingHint(RenderingHints.KEY_RENDERING).equals(RenderingHints.VALUE_RENDER_QUALITY))
            chkbRendering.setSelected(true);
            pnlOptions.add(chkbRendering); 
            
            if(currentImage_Graphics2D.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING).equals(RenderingHints.VALUE_TEXT_ANTIALIAS_ON))
            chkbTextAntialiasing.setSelected(true);
            pnlOptions.add(chkbTextAntialiasing);
            
            contentPane.add(pnlOptions, BorderLayout.CENTER);
            
            MDS_Panel pnlButton = new MDS_Panel(new FlowLayout(FlowLayout.RIGHT));
            btnOk.addActionListener(this);
            pnlButton.add(btnOk);
            btnCancel.addActionListener(this);
            pnlButton.add(btnCancel);
            contentPane.add(pnlButton, BorderLayout.SOUTH);
            this.setSize(220, 300);
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        private MDS_CheckBox makeCheckBox(String text) {
            MDS_CheckBox chkb = new MDS_CheckBox(text);
            chkb.addActionListener(this);
            return chkb;          
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Ok")) {
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                this.dispose();
            } else if(e.getActionCommand().equals("Alpha Interpolation")) {
                if(chkbAlphaInterpolation.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                } else {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
                }
            } else if(e.getActionCommand().equals("Antialiasing")) {
                if(chkbAntialiasing.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                } else {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                }
            }  else if(e.getActionCommand().equals("Color Rendering")) {
                if(chkbColorRendering.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY); 
                } else {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
                }
            }  else if(e.getActionCommand().equals("Dithering")) {
                if(chkbDithering.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                } else {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
                }
            }  else if(e.getActionCommand().equals("Fractionalmetrics")) {
                if(chkbFractionalmetrics.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                } else {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
                }
            }  else if(e.getActionCommand().equals("Interpolation")) {
                if(chkbInterpolation.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
                } else {
            
                }
            }  else if(e.getActionCommand().equals("Rendering Speed")) {
                if(chkbRendering.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                } else {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                }
            }  else if(e.getActionCommand().equals("Stroke Control")) {
                if(chkbStrokeControl.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE); 
               } else {
            
                }
            }  else if(e.getActionCommand().equals("Text Antialiasing")) {
                if(chkbTextAntialiasing.isSelected()) {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                } else {
                    currentImage_Graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
 
                }
            }         
        }        
        
        
        
    }    
    
    
}    