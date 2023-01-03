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
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

    
    
public class MediaPlayer extends MDS_Frame implements Runnable, ActionListener, ChangeListener, DropTargetListener {



    MDS_User usr = MDS.getUser();
    
    Vector vctFilter = new Vector();
    
    JComponent contentPane;
    JMenuBar mnbMediaPlayer = new JMenuBar();
    JMenu mnuFile = new JMenu("File");
    JMenuItem mniAddFolder = usr.createMenuItem("Add Folder", this);
    JMenuItem mniAddFile = usr.createMenuItem("Add File", this);
    JMenuItem mniRemoveSelected = usr.createMenuItem("Remove Selected", this);
    JMenuItem mniRemoveAll = usr.createMenuItem("Remove All", this);
    JMenuItem mniExit = usr.createMenuItem("Exit", this);
    
    JMenu mnuTools = new JMenu("Tools");
    JMenuItem mniSoundProperties = usr.createMenuItem("Sound Properties", this);
    JMenu mnuHelp = new JMenu("Help");
    JMenuItem mniAbout = usr.createMenuItem("About Media Player", this);
    
    MDS_Panel pnlControls = new MDS_Panel();
    
    MDS_Button btnPrevTrack  = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-prev.png"));
    MDS_Button btnNextTrack  = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-next.png"));
    MDS_Button btnPause  = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-pause.png"));
    MDS_Button btnPlay_Stop = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-play.png"));
    MDS_Button btnAddSound = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"open.png"));
    
    MDS_Panel pnlDisplay  = new MDS_Panel();
    
    JSlider sldSeek; 
    
    MDS_ToolBar tlbControls = new MDS_ToolBar();
    
    JProgressBar prgbSeek = new JProgressBar();
    MDS_Label lblLength = new MDS_Label("0.0");
    MDS_Label lblCurrentLength = new MDS_Label("0.0");
    MDS_Label lblInfo = new MDS_Label();
    MDS_Label lblName = new MDS_Label();
    
    MDS_Panel pnlPlayList = new MDS_Panel();
    MDS_Table tblPlayList;
    MDS_TableModel dtmlPlayList;
    
    MDS_Sound snd = MDS.getSound();
    
    Object currentSound;
    Sequencer sequencer;
    String currentPath;
    
    Thread thrdSeek;
    
    Calendar calendar = new GregorianCalendar();
    
    MediaPlayer mdp;
    
    MDS_PopupMenu pmnuOpen = new MDS_PopupMenu();
    JMenuItem pmniAddFile  = new JMenuItem("Add File");
    JMenuItem mniAddDirectory  = new JMenuItem("Add Directory");    
    


    public MediaPlayer() {
        super("Media Player", false, true, false, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-app-noatun.png"));
        
        vctFilter.add("mid");
        vctFilter.add("wav");
        vctFilter.add("au");
        vctFilter.add("rmf");
        vctFilter.add("aiff"); 
        
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(null);
        
        mniAddFolder.setActionCommand("OpenSound");
        mnuFile.add(mniAddFolder);
        mnuFile.add(mniAddFile);
        mniRemoveSelected.setEnabled(false);
        mnuFile.add(mniRemoveSelected);
        mniRemoveAll.setEnabled(false);
        mnuFile.add(mniRemoveAll);
        mnuFile.addSeparator();
        mnuFile.add(mniExit);
        mnbMediaPlayer.add(mnuFile);
        
        mnuTools.add(mniSoundProperties);
        mnbMediaPlayer.add(mnuTools);
        
        mnuHelp.add(mniAbout);
        mnbMediaPlayer.add(mnuHelp);    
        
        pmniAddFile.addActionListener(this);
        pmniAddFile.setActionCommand("Add File P");
        pmnuOpen.add(pmniAddFile);
        mniAddDirectory.addActionListener(this);
        mniAddDirectory.setActionCommand("Add Directory P");
        pmnuOpen.add(mniAddDirectory);        
        
        create_pnlControls();
        create_pnlPlayList(); 
            
        thrdSeek = new Thread(this, "Media Player");
        thrdSeek.setPriority(Thread.MIN_PRIORITY);   
        thrdSeek.start(); 
        
        /*
        ThreadGroup tg = thrdSeek.getThreadGroup();
        if(tg != null) {
            Console.println("SUCkS");
            Console.println(tg.getName());
        }*/
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                terminateMDP();        
            }
        });        
        
        new DropTarget(this, this); 
        
        this.setJMenuBar(mnbMediaPlayer); 
        //this.setFrameIcon();                 
        this.setSize(550,530);
        this.setCenterScreen();
        this.setVisible(true);
        mdp = this;
    }
    
    
    
    private void create_pnlControls() {
    
        pnlControls.setLayout(null);
        pnlControls.setBorder(BorderFactory.createEtchedBorder());
        pnlControls.setBounds(20, 20, 500, 220);
        
        pnlDisplay.setLayout(null);
        pnlDisplay.setBackground(Color.black);
        prgbSeek.setBounds(10, 80, 435, 10);
        prgbSeek.setBackground(Color.black);
        pnlDisplay.add(prgbSeek);
        
        lblName = new MDS_Label();
        lblName.setBounds(10, 10, 340, 35);
        lblName.setForeground(prgbSeek.getForeground());
        //lblName.setBackground(Color.white);
        //lblName.setOpaque(true);
        Font f = lblName.getFont(); 
        lblName.setFont(new Font(f.getFontName(), f.getStyle(), 25));        
        pnlDisplay.add(lblName);        
        
        lblCurrentLength.setBounds(370, 15, 80, 35);
        lblCurrentLength.setForeground(prgbSeek.getForeground());
        //lblCurrentLength.setBackground(Color.white);
        //lblCurrentLength.setOpaque(true);
        Font f2 = lblCurrentLength.getFont(); 
        lblCurrentLength.setFont(new Font(f2.getFontName(), f2.getStyle(), 25));
        pnlDisplay.add(lblCurrentLength);
                
        lblLength.setBounds(400, 55, 50, 20);
        lblLength.setForeground(prgbSeek.getForeground());
        //lblLength.setBackground(Color.white);
        //lblLength.setOpaque(true);
        pnlDisplay.add(lblLength);
        
        lblInfo.setBounds(10, 55, 360, 20);
        lblInfo.setForeground(prgbSeek.getForeground());
        //lblInfo.setBackground(Color.white);
        //lblInfo.setOpaque(true);
        pnlDisplay.add(lblInfo);
        
        //pnlDisplay.setBorder(BorderFactory.createLoweredBevelBorder());        
        pnlDisplay.setBounds(20,20,460, 100);
        pnlControls.add(pnlDisplay);
                
        sldSeek = new JSlider();
        sldSeek.setValue(0);
        sldSeek.addChangeListener(this);
        sldSeek.setBounds(20, 120, 460, 50);
        pnlControls.add(sldSeek);
        
        tlbControls.add(btnPrevTrack);
        
        btnPlay_Stop.addActionListener(this);
        btnPlay_Stop.setActionCommand("Play");
        tlbControls.add(btnPlay_Stop);

        btnPause.addActionListener(this);
        btnPause.setActionCommand("Pause");        
        tlbControls.add(btnPause);
        
        tlbControls.add(btnNextTrack);
        tlbControls.addSeparator();
        
        btnAddSound.setActionCommand("OpenSound");
        btnAddSound.addActionListener(this);
        tlbControls.add(btnAddSound);
        //tblControls.setBackground(Color.red);
        tlbControls.setFloatable(false);
        tlbControls.setBounds(160, 185,210,50);
        
        contentPane.add(tlbControls);
        
        contentPane.add(pnlControls);    
    }
    
    
    
    private void create_pnlPlayList() {
        
        //pnlPlayList.setBorder(BorderFactory.createEtchedBorder());
        pnlPlayList.setLayout(new BorderLayout()); 
        pnlPlayList.setBounds(20, 250, 500, 200);
        
        dtmlPlayList = new MDS_TableModel();
        tblPlayList = new MDS_Table(dtmlPlayList);
        tblPlayList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == e.BUTTON1 && e.getClickCount()==2) {
                    play_Sound_File(new File(currentPath+tblPlayList.getValueAt(tblPlayList.getSelectedRow(), 1)));                      
                }    
            }
        });    
        
        dtmlPlayList.addColumn("No");
        dtmlPlayList.addColumn("Name");
        dtmlPlayList.addColumn("Duration");
        
        
        pnlPlayList.add(new JScrollPane(tblPlayList), BorderLayout.CENTER);
        
        contentPane.add(pnlPlayList);
        
    }
    
    
    
    private void addDefaultSoundFiles(boolean b) {
        if(b) {
            /* 
            Vector vctFilter = new Vector();
            vctFilter.add("mid");
            vctFilter.add("wav");
            vctFilter.add("au");
            vctFilter.add("rmf");
            vctFilter.add("aiff"); */       
        
            Vector v = dtmlPlayList.getDataVector();
            v.removeAllElements();    
            tblPlayList.clearSelection();             
              
            currentPath = "User\\app\\Media Player\\";
            Vector vctFiles = MDS.getFileManager().getContent_Files("User\\app\\Media Player\\", vctFilter);
            for(int x=0;x<=vctFiles.size()-1;x++) {
                Vector data = new Vector();
                data.add(String.valueOf(x+1));
                data.add(((File)vctFiles.elementAt(x)).getName());
                sequencer = snd.getSequencer();
                //currentSound = snd.loadSound((File)vctFiles.elementAt(x), sequencer);
                //data.add(new Double(getDuration()));
                data.add("");
                dtmlPlayList.addRow(data);                  
            }
        }
    }
    
    
    
    public double getDuration() {
        double duration = 0.0;
        
        if (currentSound instanceof Sequence) {
            duration = ((Sequence) currentSound).getMicrosecondLength() / 1000000.0;
        }  else if (currentSound instanceof BufferedInputStream) {
			      duration = sequencer.getMicrosecondLength() / 1000000.0;
		     } else if (currentSound instanceof Clip) {
            Clip clip = (Clip) currentSound;
            duration = clip.getBufferSize() / (clip.getFormat().getFrameSize() * clip.getFormat().getFrameRate());
        } 
        
        return duration;
    }
    
    
    
    public double getSeconds() {
        double seconds = 0.0;
        
        if (currentSound instanceof Clip) {
            Clip clip = (Clip) currentSound;
            seconds = clip.getFramePosition() / clip.getFormat().getFrameRate();
        } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
            try {
                seconds = sequencer.getMicrosecondPosition() / 1000000.0;
            } catch (IllegalStateException e){
            	throw new RuntimeException(e);
                //MDS.getExceptionManager().showException(e); 
            }
        }
        
        return seconds;
    }   
    
    
    
    private void play_Sound_File(File file) {

        if(!validateSoundFile(file)) {
            MDS_OptionPane.showMessageDialog(this, "Unrecognized sound file format.", "Media Player", JOptionPane.ERROR_MESSAGE);
            return;           
        }
        
        
        
        
        
        class Load_Play extends Thread {
    
    
    
            File f;
    
    
            public Load_Play(File ff) {
                super("Media Player [Load Sound]");
                f = ff;
                this.start();
            }
            
            
        
            public void run() {                       
                MDS_UIManager.setFrameCursor(mdp, new Cursor(Cursor.WAIT_CURSOR));       
                sequencer = snd.getSequencer();
                currentSound = snd.loadSound(f, sequencer);
                tblPlayList.setValueAt(String.valueOf(getDuration()), tblPlayList.getSelectedRow(), 2);    
                lblName.setText(f.getName());
            
                if (currentSound instanceof Clip) {
                    sldSeek.setMaximum((int) ((Clip)currentSound).getFrameLength());
                    prgbSeek.setMaximum(sldSeek.getMaximum());
                } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
                    sldSeek.setMaximum((int)(sequencer.getMicrosecondLength() / 1000));
                    prgbSeek.setMaximum(sldSeek.getMaximum());	
                }
        
                sldSeek.setValue(0);    
                                 
                snd.playSound(currentSound, sequencer);
        
                MDS_UIManager.setFrameCursor(mdp, new Cursor(Cursor.DEFAULT_CURSOR));
        
            
                btnPlay_Stop.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-stop.png"));
                btnPlay_Stop.setActionCommand("Stop"); 
        
            }
            
        }
        
        new Load_Play(file);    
        
               
    }
    
    
    
    public void addSound(File f) {
        if(!validateSoundFile(f)) {
            //MDS_OptionPane.showMessageDialog(this, "Unrecognized sound file format." "Media Player", JOptionPane.ERROR_MESSAGE);
            return;           
        } 
        
        Vector v = dtmlPlayList.getDataVector();
        v.removeAllElements();    
        tblPlayList.clearSelection();         
        
        currentPath = MDS.getFileManager().getFilePathOnly(f.getPath());
        Vector data = new Vector();
        data.add(String.valueOf(1));
        data.add(f.getName());
        sequencer = snd.getSequencer();
        //currentSound = snd.loadSound((File)vctFiles.elementAt(x), sequencer);
        //data.add(new Double(getDuration()));
        data.add("");
        dtmlPlayList.addRow(data);  
        tblPlayList.setRowSelectionInterval(0, 0);            
    }     
    
    
    
    private boolean validateSoundFile(File f) {
        /*
        Vector vctExtensions = new Vector();
        vctExtensions.add("mid");
        vctExtensions.add("wav");
        vctExtensions.add("au");
        vctExtensions.add("rmf");
        vctExtensions.add("aiff");*/
        
        if(vctFilter.contains(MDS.getFileManager().getFileExtension(f.getPath()))) {
            return true;
        } else {
            return false;
        }        
    }
    
    
    
    private void terminateMDP() {
        if (currentSound instanceof Clip) {
            ((Clip)currentSound).stop();    
        } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
            sequencer.stop();
        }                    
                
        if (sequencer != null) {
            sequencer.close();
        }
        
        if(thrdSeek != null) {
            thrdSeek.interrupt();            
        }
        
        thrdSeek = null;
                  
    }    
    
    
    
    public void run() {
        try {    
            while(true) {
                thrdSeek.sleep(500);
                if (currentSound instanceof Clip) {
                    prgbSeek.setValue((int)((Clip)currentSound).getFramePosition()); 
                    lblInfo.setText(((Clip)currentSound).getFormat().toString());
                } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
                    prgbSeek.setValue((int)sequencer.getMicrosecondPosition() / 1000); 
                    lblInfo.setText("");                  
                } 
                
                String d = String.valueOf(getDuration());
                d =  d.substring(0,d.indexOf('.')+2);            
                lblLength.setText(d);

                String s = String.valueOf(getSeconds());
                s =  s.substring(0,s.indexOf('.')+2);            
                lblCurrentLength.setText(s);
                       
            }  
        } catch(InterruptedException ex) {
        
        }
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("OpenSound")) {            
            pmnuOpen.show(tlbControls, btnAddSound.getX(), btnAddSound.getY()+btnAddSound.getHeight());           
        } else if(e.getActionCommand().equals("Add File P")) {
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            
            fmfc.setFilter(vctFilter);
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {  
                //Vector v = dtmlPlayList.getDataVector();
                //v.removeAllElements();    
                //tblPlayList.clearSelection(); 
                            
                File f = new File(fmfc.getPath());  
                addSound(f);
                play_Sound_File(f);                           
            }                  
        } else if(e.getActionCommand().equals("Add Directory P")) {
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this) ==  fmdc.APPROVE_OPTION) {
                
                Vector v = dtmlPlayList.getDataVector();
                v.removeAllElements();    
                tblPlayList.clearSelection();             
                
                currentPath = fmdc.getPath();
                Vector vctFiles = MDS.getFileManager().getContent_Files(fmdc.getPath(), vctFilter);
                for(int x=0;x<=vctFiles.size()-1;x++) {
                    Vector data = new Vector();
                    data.add(String.valueOf(x+1));
                    data.add(((File)vctFiles.elementAt(x)).getName());
                    sequencer = snd.getSequencer();
                    //currentSound = snd.loadSound((File)vctFiles.elementAt(x), sequencer);
                    //data.add(new Double(getDuration()));
                    data.add("");
                    dtmlPlayList.addRow(data);                  
                }
            }                 
        } else if(e.getActionCommand().equals("Play")) {
            
            if(tblPlayList.getRowCount() == 0) {
                return;
            }
            
            if(tblPlayList.getSelectedRow() == -1) { 
                tblPlayList.setRowSelectionInterval(0, 0);         
            }
            
            play_Sound_File(new File(currentPath+tblPlayList.getValueAt(tblPlayList.getSelectedRow(), 1)));            

            
        } else if(e.getActionCommand().equals("Stop")) {
            if (currentSound instanceof Clip) {
                ((Clip)currentSound).stop();    
            } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
                sequencer.stop();
            }
            btnPlay_Stop.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-play.png"));
            btnPlay_Stop.setActionCommand("Play"); 
            
            if(btnPause.getActionCommand().equals("Resume")) {
                btnPause.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-pause.png"));
                btnPause.setActionCommand("Pause");     
            }  
                                                           
        } else if(e.getActionCommand().equals("Pause")) {
            if (currentSound instanceof Clip) {
                ((Clip)currentSound).stop();    
            } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
                sequencer.stop();
            }
            if(btnPlay_Stop.getActionCommand().equals("Stop")) {
                btnPause.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-play.png"));
                btnPause.setActionCommand("Resume");     
            }                    
        } else if(e.getActionCommand().equals("Resume")) {
            if (currentSound instanceof Clip) {
                ((Clip)currentSound).start();    
            } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
                sequencer.start();
            }
            btnPause.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-pause.png"));
            btnPause.setActionCommand("Pause");         
        } else if(e.getActionCommand().equals("About Media Player")) {
            MDS.getUser().showAboutDialog(this, "Media Player", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-player.png"), MDS.getAbout_Mahesh()); 
        } else if(e.getActionCommand().equals("Exit")) {
            this.dispose();    
        } else if(e.getActionCommand().equals("Remove All")) {
        /*
            Vector v = dtmlPlayList.getDataVector();
            v.removeAllElements();    
            tblPlayList.clearSelection(); 
            if (currentSound instanceof Clip) {
                ((Clip)currentSound).stop();    
            } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
                sequencer.stop();
            }
        */                           
        } else if(e.getActionCommand().equals("Add File")) {
            MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
            
            fmfc.setFilter(vctFilter);
            if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {  
                //Vector v = dtmlPlayList.getDataVector();
                //v.removeAllElements();    
                //tblPlayList.clearSelection(); 
                            
                File f = new File(fmfc.getPath());  
                addSound(f);
                play_Sound_File(f);                           
            }            
        } else if(e.getActionCommand().equals("Sound Properties")) {
            new SoundProperties();
        }
    }
    
    
    
    public void stateChanged(ChangeEvent e) {
        int value = sldSeek.getValue();
        prgbSeek.setValue(value);
        if (currentSound instanceof Clip) {
            ((Clip) currentSound).setFramePosition(value);
        } else if (currentSound instanceof Sequence) {
            long dur = ((Sequence) currentSound).getMicrosecondLength();
            sequencer.setMicrosecondPosition(value * 1000);
        } else if (currentSound instanceof BufferedInputStream) {
            long dur = sequencer.getMicrosecondLength();
		        sequencer.setMicrosecondPosition(value * 1000);
        }
   
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        MediaPlayer mdp = new MediaPlayer();
        if(arg.length == 1) {
            try {               
                File f = new File(arg[0]);
                mdp.addSound(f);
                mdp.play_Sound_File(f);                       
            } catch (Exception ex) {
                //wb.editorPane.setText("Error: " + ex.toString());
            }              
        } else {
            mdp.addDefaultSoundFiles(true);
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
                    /*
                    Vector vctFilter = new Vector();
                    vctFilter.add("mid");
                    vctFilter.add("wav");
                    vctFilter.add("au");
                    vctFilter.add("rmf");
                    vctFilter.add("aiff"); */                
                
                    Vector v = dtmlPlayList.getDataVector();
                    v.removeAllElements();    
                    tblPlayList.clearSelection();             
                
                    currentPath = f.getPath()+"\\";
                    Vector vctFiles = MDS.getFileManager().getContent_Files(f.getPath()+"\\", vctFilter);
                    for(int x=0;x<=vctFiles.size()-1;x++) {
                        Vector data = new Vector();
                        data.add(String.valueOf(x+1));
                        data.add(((File)vctFiles.elementAt(x)).getName());
                        sequencer = snd.getSequencer();
                        //currentSound = snd.loadSound((File)vctFiles.elementAt(x), sequencer);
                        //data.add(new Double(getDuration()));
                        data.add("");
                        dtmlPlayList.addRow(data); 
                        
                        if(tblPlayList.getRowCount() == 0) {
                            return;
                        }
            
                        if(tblPlayList.getSelectedRow() == -1) { 
                            tblPlayList.setRowSelectionInterval(0, 0);         
                        }
            
                        play_Sound_File(new File(currentPath+tblPlayList.getValueAt(tblPlayList.getSelectedRow(), 1)));                          
                                         
                    }
                } else {
                    if(validateSoundFile(f) == true) {
                        addSound(f);
                        play_Sound_File(f);               
                    } else {
                        e.rejectDrop();
                        MDS_OptionPane.showMessageDialog(this, "'"+f.getPath()+"'"+" is not a valid image file", "Media Player", JOptionPane.ERROR_MESSAGE);
                    }         
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    
    
    public void dropActionChanged(DropTargetDragEvent dtde) {}
    
    
    
    
}    