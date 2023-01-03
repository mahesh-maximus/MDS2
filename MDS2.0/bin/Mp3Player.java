
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;




public class Mp3Player extends MDS_Frame implements Runnable, ActionListener, ChangeListener, DropTargetListener {


    
    private static boolean mp3pLoaded = false;
    private static Mp3Player mp3p;
    
    JComponent contentPane;
    Thread thread;
    public static Process wmp;
    String message;
    
    MDS_Label lblDisplay = new MDS_Label("MP3 ...");
    MDS_Slider sldSeek = new MDS_Slider(0, 100);
    MDS_ToolBar tlbControls = new MDS_ToolBar();
    MDS_Button btnPrevious = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"previous_button.png"));
    MDS_Button btnNext = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"next_button.png"));
    MDS_Button btnPlay_Stop = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"play_button.png")); 
    MDS_Button btnPause = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"pause_button.png"));
    MDS_Button btnOpen = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"eject_button.png"));
    
    DefaultListModel lstmdl = new DefaultListModel();
    MDS_List lstFiles = new MDS_List(lstmdl);
    
    MDS_PopupMenu pmnuOpen = new MDS_PopupMenu();
    JMenuItem mniAddFile  = new JMenuItem("Add File");
    JMenuItem mniAddDirectory  = new JMenuItem("Add Directory");
    
    String currentPath;
    
    Vector vctFilter = new Vector();

    
    

    public Mp3Player() {
        super("Mp3 Player", false, true, false, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"mp3-player.png"));
        contentPane = (JComponent) this.getContentPane();  
        contentPane.setLayout(new BorderLayout());
        MDS_Panel pnl1 = new MDS_Panel(new BorderLayout());
        lblDisplay.setOpaque(true);
        lblDisplay.setForeground(Color.green);
        lblDisplay.setBackground(Color.black);
        lblDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        pnl1.add(lblDisplay, BorderLayout.NORTH);
        tlbControls.add(btnPrevious);
        btnPlay_Stop.addActionListener(this);
        btnPlay_Stop.setActionCommand("Play");
        tlbControls.add(btnPlay_Stop);
        btnPause.addActionListener(this);
        btnPause.setActionCommand("Pause");        
        tlbControls.add(btnPause);
        tlbControls.add(btnNext);
        btnOpen.addActionListener(this);
        btnOpen.setActionCommand("Open");
        tlbControls.add(btnOpen);
        tlbControls.setFloatable(false);
        MDS_Panel pnl2 = new MDS_Panel(new FlowLayout(FlowLayout.CENTER));
        pnl2.add(tlbControls);
        sldSeek.setValue(0);
        sldSeek.addChangeListener(this);
        pnl1.add(sldSeek, BorderLayout.CENTER);
        pnl1.add(pnl2, BorderLayout.SOUTH);
        contentPane.add(pnl1, BorderLayout.NORTH);
        
        lstFiles.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    play_Sound_File(new File(currentPath+lstFiles.getSelectedValue()));    
                }
            }
        });     
          
        contentPane.add(new JScrollPane(lstFiles), BorderLayout.CENTER);
        
        mniAddFile.addActionListener(this);
        pmnuOpen.add(mniAddFile);
        mniAddDirectory.addActionListener(this);
        pmnuOpen.add(mniAddDirectory);
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                if(wmp != null) {
                    wmp.destroy();   
                }  
                
                mp3pLoaded = false;
                mp3p = null;
                 
            }
        });
        
        vctFilter.add("mp3"); 
        
        new DropTarget(lstFiles, this);
               
        this.setSize(300, 380);
        lblDisplay.setPreferredSize(new Dimension(this.getWidth(), 50));
        this.setCenterScreen();
        this.setVisible(true);
        
        mp3pLoaded = true;
        mp3p = this;
    }
    
    
    
    public static void MDS_Main(String arg[]) {
////        if(mp3pLoaded) {
////            try {
////                Mp3Player.mp3p.setIcon(false);         
////                Mp3Player.mp3p.setSelected(true);
////            } catch(Exception ex) {}
////        } else {
////            Mp3Player mp3p = new Mp3Player();
////            if(arg.length == 1) {             
////                 File f = new File(arg[0]);
////                 mp3p.lstmdl.removeAllElements();
////                 mp3p.currentPath = f.getParent()+"\\";
////                 mp3p.lstmdl.addElement(f.getName());            
////                 mp3p.play_Sound_File(f);                      
////            } else if(arg.length == 0) {     
////                 File f = new File("User\\app\\mp3 Player\\Metallica - Nothing else matters.mp3");
////                 mp3p.lstmdl.removeAllElements();
////                 mp3p.currentPath = f.getParent()+"\\";
////                 mp3p.lstmdl.addElement(f.getName());            
////                 //mp3p.play_Sound_File(f);                 
////            }  
////        }              
    }    
    
    
    
    private synchronized void play_Sound_File(File f) {
        btnPlay_Stop.setActionCommand("Stop");
        btnPlay_Stop.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"stop_button.png"));
        if(thread != null) {
            thread.interrupt();
        }    
        
        thread = null;
        
        thread = new Thread(this);
        message = "PLAY>"+f.getPath();
        thread.start();
        lblDisplay.setText(f.getName());
    }
    
    
    
    private void sendMessage_WMP(String msg) {
//        WinUI.sendMessage("ThunderRT6FormDC","WMP_Server For MDS", msg);
    }
    
    
    
    public void run() {
//        if(!WinUI.findWindow("ThunderRT6FormDC", "WMP_Server For MDS")) {
//            try {
//                wmp = Runtime.getRuntime().exec("bin\\native\\Wmps.exe 8080981313244_Posha_987870871312376");
//            } catch(Exception ex) {
//                System.err.println("Error occourred while trying to execute 'Wmps.exe' - Mp3Player");
//                //ex.printStackTrace();
//                //MDS_OptionPane.showMessageDialog(mp3p, "Error occourred while trying to execute 'Wmps.exe'", "MDS", MDS_OptionPane.ERROR_MESSAGE);
//            }
//        }
//        
//        while(!WinUI.findWindow("ThunderRT6FormDC", "WMP_Server For MDS")) {
//            try {
//                thread.sleep(100);    
//            } catch(InterruptedException ex) {
//            
//            }
//        }
//
//        sendMessage_WMP(message); 
                
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open")) {
            pmnuOpen.show(tlbControls, btnOpen.getX(), btnOpen.getY()+btnOpen.getHeight());
        } else if(e.getActionCommand().equals("Add File")) {
            MDS_FileChooser fc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            Vector vctFilter = new Vector();
            vctFilter.add("mp3");
            fc.setFilter(vctFilter);
            if(fc.showDiaog(this) ==  fc.APPROVE_OPTION) {  
                 File f = new File(fc.getPath());
                 lstmdl.removeAllElements();
                 currentPath = f.getParent()+"\\";
                 lstmdl.addElement(f.getName());            
                 play_Sound_File(f);                           
            }             
        } else if(e.getActionCommand().equals("Add Directory")) {

            MDS_DirectoryChooser dc = new MDS_DirectoryChooser();
            if(dc.showDiaog(this) ==  dc.APPROVE_OPTION) {            
                lstmdl.removeAllElements();
                currentPath = dc.getPath()+"\\";
                Vector vctFiles = MDS.getFileManager().getContent_Files(dc.getPath(), vctFilter);
                for(int x=0;x<=vctFiles.size()-1;x++) {
                    lstmdl.addElement(((File)vctFiles.elementAt(x)).getName());               
                }
                                          
            }              
        } else if(e.getActionCommand().equals("Play")) {
            if(lstFiles.getSelectedIndex() != -1) {
                File f = new File(String.valueOf(currentPath+lstFiles.getSelectedValue()));  
                play_Sound_File(f);              
            }
        } else if(e.getActionCommand().equals("Stop")) {
            sendMessage_WMP("STOP>");  
            btnPlay_Stop.setActionCommand("Play");
            btnPlay_Stop.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"play_button.png"));            
            btnPause.setActionCommand("Pause");
            btnPause.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"pause_button.png"));          
        } else if(e.getActionCommand().equals("Pause")) {
            sendMessage_WMP("PAUSE>true"); 
            btnPause.setActionCommand("UnPause");
            btnPause.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"play_button.png")); 
        } else if(e.getActionCommand().equals("UnPause")) {
            sendMessage_WMP("PAUSE>false"); 
            btnPause.setActionCommand("Pause");
            btnPause.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"pause_button.png"));         
        }
    }
    
    
    
    public void stateChanged(ChangeEvent e) {
        sendMessage_WMP("SEEK>"+String.valueOf(sldSeek.getValue()));
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
                    lstmdl.removeAllElements();
                    currentPath = f.getPath()+"\\";
                    Vector vctFiles = MDS.getFileManager().getContent_Files(f.getPath()+"\\", vctFilter);
                    for(int x=0;x<=vctFiles.size()-1;x++) {
                        lstmdl.addElement(((File)vctFiles.elementAt(x)).getName());               
                    }
                    
                    lstFiles.setSelectedIndex(0);     
                
                    File f1 = new File(String.valueOf(currentPath+lstFiles.getSelectedValue()));  
                    play_Sound_File(f1);                   
                    
                } else {
                    if(MDS.getFileManager().getFileExtension(f.getName()).equals("mp3")) {
                        lstmdl.removeAllElements();
                        currentPath = f.getParent();
                        lstmdl.addElement(f.getName());
                        lstFiles.setSelectedIndex(0);
                        //File f1 = new File(String.valueOf(currentPath+lstFiles.getSelectedValue()));  
                        play_Sound_File(f);                              
                    } else {
                        e.rejectDrop();
                        MDS_OptionPane.showMessageDialog(this, "'"+f.getPath()+"'"+" is not a valid image file", "Mp3 Player", JOptionPane.ERROR_MESSAGE);
                    }         
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    
    
    public void dropActionChanged(DropTargetDragEvent dtde) {}       


}