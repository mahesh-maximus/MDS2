/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;



public class ElectronicEyes extends MDS_Frame implements ActionListener, DropTargetListener {



    JLabel lblImage = new JLabel();
    MDS_ToolBar tlbTools = new MDS_ToolBar();
    
    MDS_Button btnOpen = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-fileopen.png"));     
    MDS_Button btnPrevious = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-previous.png")); 
    MDS_Button btnNext = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-next.png"));
    MDS_Button btnActualSize = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"actual-size.png"));
    MDS_Button btnZoomIn = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-viewmag+.png"));
    MDS_Button btnZoomOut = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-viewmag-.png"));
    MDS_Button btnCopy = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcopy.png"));
    MDS_Button btnMove = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcut.png"));
    MDS_Button btnDelete = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-filesys-trashcan_empty.png"));
    MDS_Button btnPrint = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-device-printer1.png"));
    MDS_Button btnHelp = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-app-khelpcenter.png"));
    
    BufferedImage currentBufferedImage;
    Dimension initialDimension;
    
    int actualWidth = 0;
    int actualHeight = 0;
    int currentWidth = 0;
    int currentHeight = 0;
    
    String imageDir = "";
    Vector imageNames = new Vector();
    String currentImageName = "";
    int imageIndex = -1;
    int zoomStep = 0;
    Vector vctSupportedExtensions = new Vector();
    
    MDS_User usr = MDS.getUser();
    FileManager fm = MDS.getFileManager();
    MDS_Clipboard clipBoard = MDS.getClipboard(); 
    
    

    public ElectronicEyes() {
        super("Electronic Eyes",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-xeyes.png"));  
        
        vctSupportedExtensions.add("png");
        vctSupportedExtensions.add("jpg");
        vctSupportedExtensions.add("jpeg");
        vctSupportedExtensions.add("gif");   
        
        new DropTarget(lblImage, this);      
        
        JComponent contentPane;
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        JPanel jpnTooBarHolder = new JPanel();
        
        lblImage.setOpaque(true);
        lblImage.setBackground(Color.white);
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        
        contentPane.add(new JScrollPane(lblImage), BorderLayout.CENTER);
        
        btnOpen.addActionListener(this);
        btnOpen.setActionCommand("Open");       
        tlbTools.add(btnOpen);
        tlbTools.addSeparator();
        btnPrevious.addActionListener(this);
        btnPrevious.setActionCommand("Previous");         
        tlbTools.add(btnPrevious);
        btnNext.addActionListener(this);
        btnNext.setActionCommand("Next");         
        tlbTools.add(btnNext);
        tlbTools.addSeparator();
        btnActualSize.addActionListener(this);
        btnActualSize.setActionCommand("ActualSize"); 
        tlbTools.add(btnActualSize);
        btnZoomIn.addActionListener(this);
        btnZoomIn.setActionCommand("ZoomIn"); 
        tlbTools.add(btnZoomIn);
        btnZoomOut.addActionListener(this);
        btnZoomOut.setActionCommand("ZoomOut");        
        tlbTools.add(btnZoomOut);
        tlbTools.addSeparator();
        btnCopy.addActionListener(this);
        btnCopy.setActionCommand("Copy");         
        tlbTools.add(btnCopy);
        btnMove.addActionListener(this);
        btnMove.setActionCommand("Move");         
        tlbTools.add(btnMove);
        btnDelete.addActionListener(this);
        btnDelete.setActionCommand("Delete");         
        tlbTools.add(btnDelete);
        tlbTools.addSeparator();
        btnPrint.addActionListener(this);
        btnPrint.setActionCommand("Print"); 
        tlbTools.add(btnPrint);
        btnHelp.addActionListener(this);
        btnHelp.setActionCommand("Help"); 
        tlbTools.add(btnHelp);
        
        tlbTools.setFloatable(false);
        
        jpnTooBarHolder.add(tlbTools,BorderLayout.CENTER);
        
        contentPane.add(jpnTooBarHolder, BorderLayout.SOUTH);
        
        this.setBounds(0,0,800,600);
        this.setVisible(true);  
          
    }
    
    
    
    private MDS_Button create_ToolBar_Button(String imgName, String ac) {
        MDS_Button btn  = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+imgName ));
        btn.addActionListener(this);
        btn.setActionCommand(ac);
        return btn;         
    }
    
    
    
    public void setImage(String path) {
        if(path == null) {
            lblImage.setIcon(null);
            actualWidth = 0;
            return;
        }
        if(!validateImageFile(new File(path))) { 
            MDS_OptionPane.showMessageDialog(this, "Unrecognized image file format.", "Electronic Eyes", JOptionPane.ERROR_MESSAGE);
            return;
        }     
        ImageIcon i = new ImageIcon(path);
        lblImage.setIcon(i);
        actualWidth = i.getIconWidth();
        actualHeight = i.getIconHeight(); 
        currentWidth = actualWidth;  
        currentHeight = actualHeight;
        zoomStep = 0;
        File f = new File(path);
        this.setTitle(f.getName()+" - Electronic Eyes");
    }
    
    
    
    private void setCurrentPicture(String path) {
        try {
            currentBufferedImage = ImageIO.read(new File(path));
            lblImage.setIcon(new ImageIcon((Image)currentBufferedImage));
            initialDimension = new Dimension(currentBufferedImage.getWidth(), currentBufferedImage.getHeight()); 
        } catch (Exception ex) { 
                
        } 
    }
    
    
    
    private boolean validateImageFile(File f) {        
        if(vctSupportedExtensions.contains(MDS.getFileManager().getFileExtension(f.getPath()))) {
            return true;
        } else {
            return false;
        }        
    }
    
    
    
    public void initializeImageBrowsing(File f) {
        try {
            if(!f.isDirectory()) {
                setImage(f.getPath());   
                currentImageName = f.getName(); 
                imageDir = fm.getFilePathOnly(f.getPath()); 
                //Console.println(imageDir);
                imageNames = fm.getContent_FileNames(imageDir, vctSupportedExtensions);
                imageIndex = imageNames.indexOf(f.getName());
                //Console.println(imageIndex);
                //Console.println(f.getName());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }    
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open")) {
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            Vector v = new Vector();
            v.add("png");
            v.add("jpg");
            v.add("jpeg");
            v.add("gif");
            fmfc.setFilter(v);
            fmfc.setPicturePreviewerVisible(true);
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {      
                initializeImageBrowsing(new File(fmfc.getPath()));
            }      
        } else if(e.getActionCommand().equals("ZoomIn")) {
            if(zoomStep != 6) {
                if(actualWidth < 10) return;
                zoomStep++;
                currentWidth = currentWidth + (currentWidth/10);
                ImageIcon i = new ImageIcon(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));
                Image image = i.getImage().getScaledInstance(currentWidth, -1, Image.SCALE_FAST);
                lblImage.setIcon(new ImageIcon(image));
            }
        } else if(e.getActionCommand().equals("ZoomOut")) {
            if(zoomStep != -6) {
                if(actualWidth < 10) return;
                zoomStep--;
                currentWidth = currentWidth - (currentWidth/10);  
                ImageIcon i = new ImageIcon(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));
                Image image = i.getImage().getScaledInstance(currentWidth, -1, Image.SCALE_FAST);
                lblImage.setIcon(new ImageIcon(image));
            }          
        } else if(e.getActionCommand().equals("ActualSize")) {
            setImage(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));       
        } else if(e.getActionCommand().equals("Help")) {
            usr.showAboutDialog(this, "Electronic Eyes", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"apple-red.png"), MDS.getAbout_Mahesh());
        } else if(e.getActionCommand().equals("Next")) {
            if(imageNames.size() == 0) return;
            imageIndex++;
            if(imageIndex < imageNames.size()) {
                setImage(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));        
            } else {
                //if(imageNames.size() == 0) return;
                imageIndex = 0;
                setImage(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));
            }
        } else if(e.getActionCommand().equals("Previous")) {
            if(imageNames.size() == 0) return;
            imageIndex--;
            if(imageIndex > -1) {
                setImage(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));        
            } else {
                //if(imageNames.size() == 0) return;
                imageIndex = imageNames.size()-1;
                setImage(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));
            }        
        } else if(e.getActionCommand().equals("Print")) {
            if(imageNames.size()>0) {
                MDS.getPrinter().print(new ImageIcon(imageDir+String.valueOf(imageNames.elementAt(imageIndex)))); 
            }    
        } else if(e.getActionCommand().equals("Copy")) {
            clipBoard.setContent(new File(imageDir+String.valueOf(imageNames.elementAt(imageIndex))) ,clipBoard.STATUS_COPIED);
        } else if(e.getActionCommand().equals("Delete")) {
            File f = new File(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));
            if(MDS_OptionPane.showConfirmDialog(this, "Are you sure you want to delete "+ f.getName(), "Confirm File Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                if(f.isDirectory()) {
                
                } else {
                    if(!f.delete()) {
                        MDS_OptionPane.showMessageDialog(this, "Uable to Delete the file.", "Error Deleting file", JOptionPane.ERROR_MESSAGE);                          
                    } else {
                        imageNames.remove(f.getName());
                        imageIndex++;
                        if(imageIndex < imageNames.size()) {
                            setImage(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));        
                        } else {
                            if(imageNames.size() == 0) {
                                setImage(null);
////                                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DELETE_FILE, f));
                                return;
                            }                            
                            imageIndex = 0;
                            setImage(imageDir+String.valueOf(imageNames.elementAt(imageIndex)));
                        }                        
////                        MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DELETE_FILE, f));
                    }
                }    
            }             
        } else if(e.getActionCommand().equals("Move")) {
            //clipBoard.setContent(new File(imageDir+String.valueOf(imageNames.elementAt(imageIndex))) ,clipBoard.STATUS_MOVED);
        }
    }
    
    
    
    public void dragEnter(DropTargetDragEvent e){
        /*
        try { 
            Transferable transferable = e.getTransferable();
            File f = new File(String.valueOf(transferable.getTransferData(DataFlavor.stringFlavor)));
            if(f.exists()) {
                if(f.isDirectory()) {
                    e.rejectDrop();
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } */
               
    }
    
    
    
    public void dragExit(DropTargetEvent dte) {}
    
    
    
    public void dragOver(DropTargetDragEvent dtde) {}
    
    
    
    public void drop(DropTargetDropEvent e) {
        
        try { 
            Transferable transferable = e.getTransferable();
            File f = new File(String.valueOf(transferable.getTransferData(DataFlavor.stringFlavor)));
            if(f.exists()) {
                if(f.isDirectory()) {
                    e.rejectDrop();
                } else {
                    if(validateImageFile(f) == true) {
                        initializeImageBrowsing(f);       
                    } else {
                        e.rejectDrop();
                        MDS_OptionPane.showMessageDialog(this, "'"+f.getPath()+"'"+" is not a valid image file", "Electronic Eyes", JOptionPane.ERROR_MESSAGE);
                    }               
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    
    
    public void dropActionChanged(DropTargetDragEvent dtde) {}    
    
    
    
    public static void MDS_Main(String arg[]) {
        ElectronicEyes ee = new ElectronicEyes();
        if(arg.length == 1) {
            ee.initializeImageBrowsing(new File(arg[0]));
        }
    }



}