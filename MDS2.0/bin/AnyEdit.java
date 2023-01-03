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
import java.util.*;
import java.io.*;



public class AnyEdit extends MDS_Frame implements ActionListener, MenuListener, TextFileDropListener  {


    
    MDS_User usr = MDS.getUser();
     
    JComponent contentPane;
    JMenuBar mnb = new JMenuBar();
    
    MDS_Menu mnuFile = new MDS_Menu("File", KeyEvent.VK_F);
    JMenuItem mniNew = usr.createMenuItem("New", this, MDS_KeyStroke.getNew(), KeyEvent.VK_N);
    JMenuItem mniOpen = usr.createMenuItem("Open", this, MDS_KeyStroke.getOpen(), KeyEvent.VK_O);
    JMenuItem mniClose = usr.createMenuItem("Close", this);
    JMenuItem mniSave = usr.createMenuItem("Save", this, MDS_KeyStroke.getSave(), KeyEvent.VK_S);
    JMenuItem mniSaveAs = usr.createMenuItem("Save As", this,  KeyEvent.VK_A);
    JMenuItem mniPageSetup = usr.createMenuItem("Page Setup", this);
    JMenuItem mniPrint = usr.createMenuItem("Print", this, MDS_KeyStroke.getPrint(), KeyEvent.VK_P);
    JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    
    MDS_Menu mnuEdit = new MDS_Menu("Edit", KeyEvent.VK_E);
    JMenuItem mniUndo = usr.createMenuItem("Undo", this, MDS_KeyStroke.getUndo(), KeyEvent.VK_Z);
    JMenuItem mniCut = usr.createMenuItem("Cut", this, MDS_KeyStroke.getCopy(), KeyEvent.VK_X);
    JMenuItem mniCopy = usr.createMenuItem("Copy", this, MDS_KeyStroke.getCut(), KeyEvent.VK_C);
    JMenuItem mniPaste = usr.createMenuItem("Paste", this, MDS_KeyStroke.getPaste(), KeyEvent.VK_V);
    JMenuItem mniDelete = usr.createMenuItem("Delete", this, MDS_KeyStroke.getDelete(), KeyEvent.VK_D);
    JMenuItem mniSelectAll = usr.createMenuItem("Select All", this, MDS_KeyStroke.getSelectAll(), KeyEvent.VK_A);
    
    MDS_Menu mnuFormat = new MDS_Menu("Format", KeyEvent.VK_F);
    JMenuItem mniFont = usr.createMenuItem("Font", this, KeyEvent.VK_F);
    JMenuItem mniColor = usr.createMenuItem("Color", this, KeyEvent.VK_C);   
     
    MDS_Menu mnuHelp = new MDS_Menu("Help", KeyEvent.VK_H);
    JMenuItem mniAbout = usr.createMenuItem("About", this, KeyEvent.VK_A);
    
    MDS_ToolBar tlbAE = new MDS_ToolBar();
    
    MDS_Button btnNew = new MDS_Button(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"new.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE));     
    MDS_Button btnOpen = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"open.png"));     
    MDS_Button btnSave = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"save.png"));     
    MDS_Button btnSaveAs = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"save_as.png"));     
    MDS_Button btnCut = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"cut.png"));     
    MDS_Button btnCopy = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"copy.png"));     
    MDS_Button btnPaste = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"paste.png"));     
    MDS_Button btnDelete = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"delete.png"));     
    MDS_Button btnSearch = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"search.png"));     
    MDS_Button btnPrint = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"print.png"));     
    
    MDS_TextArea txta;
    
    boolean fileContentChanged = false;
    File currentFile = null;
    AnyEdit ae;
    

    public AnyEdit() {
        super("Any Edit",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "anyedit.png"));       
        contentPane = (JComponent) this.getContentPane();   
        txta = new MDS_TextArea(this);
        mnuFile.add(mniNew);
        mnuFile.add(mniOpen);
        //mnuFile.add(mniClose);
        mnuFile.add(mniSave);
        mnuFile.add(mniSaveAs);
        mnuFile.addSeparator();
        mnuFile.add(mniPageSetup);
        mnuFile.add(mniPrint);
        mnuFile.addSeparator();
        mnuFile.add(mniExit);
        mnuFile.addMenuListener(this);
        mnb.add(mnuFile);
        
        mnuEdit.add(mniUndo);
        mnuEdit.add(mniCut);
        mnuEdit.add(mniCopy);
        mnuEdit.add(mniPaste);
        mnuEdit.add(mniDelete);
        mnuEdit.addSeparator();
        mnuEdit.add(mniSelectAll);
        //mnuEdit.add();
        //mnuEdit.add();
        //mnuEdit.add();
        mnuEdit.addMenuListener(this);
        mnb.add(mnuEdit);
        
        mnuFormat.add(mniFont);
        mnuFormat.add(mniColor);
        mnuFormat.addMenuListener(this);
        mnb.add(mnuFormat);
        
        mnuHelp.add(mniAbout);
        mnuHelp.addMenuListener(this);
        mnb.add(mnuHelp);
        
        tlbAE.setFloatable(false);
        btnNew.setActionCommand("New");
        btnNew.addActionListener(this);
        tlbAE.add(btnNew);
        btnOpen.setActionCommand("Open");
        btnOpen.addActionListener(this);
        tlbAE.add(btnOpen);
        btnSave.setActionCommand("Save");
        btnSave.addActionListener(this);
        tlbAE.add(btnSave);
        btnSaveAs.setActionCommand("Save As");
        btnSaveAs .addActionListener(this);       
        tlbAE.add(btnSaveAs);
        tlbAE.addSeparator();
        //tlbAE.add(btnCut);
        //tlbAE.add(btnCopy);
        //tlbAE.add(btnPaste);
        //tlbAE.add(btnDelete);
        tlbAE.add(btnSearch);
        tlbAE.addSeparator();
        tlbAE.add(btnPrint);
        
        contentPane.add(tlbAE, BorderLayout.NORTH);
        txta.setDropEnabled(true);

        txta.addTextFileDropListener(this);

        contentPane.add(new JScrollPane(txta), BorderLayout.CENTER);
        
        txta.addKeyListener(new KeyAdapter() {public void
            keyPressed(KeyEvent e) {fileContentChanged = true;}});
        
        ae = this;
            
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                ae.setDefaultCloseOperation(MDS_Frame.DO_NOTHING_ON_CLOSE);               
                if(fileContentChanged) {
                    int r = MDS_OptionPane.showConfirmDialog(ae, "The text in the untitled file has changed.\n\nDo you want to save the changes?", "Any Edit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(r == JOptionPane.YES_OPTION) {
                        if(currentFile == null) {
                            String path = showFileSaveDialog();
                            if(path != null) {
                                saveFile(new File(path));
                                txta.setText("");
                                currentFile = null;  
                                fileContentChanged = false;
                                ae.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE);                                           
                            }
                        } else {
                            saveFile(currentFile);
                            fileContentChanged = false;
                            ae.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE); 
                        }                       
                    } else if(r == JOptionPane.NO_OPTION) {
                        ae.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE); 
                    } else if(r == JOptionPane.CANCEL_OPTION) {
                        
                    }                      
                } else {
                    ae.setDefaultCloseOperation(MDS_Frame.DISPOSE_ON_CLOSE); 
                }                               
                
            }
        });            
        
        this.setJMenuBar(mnb);
        this.setBounds(0, 0, 800, 600);
        this.setVisible(true); 
        
        //loadFile(this, new File("E:\\radeonfb.c"));   
        //loadFile(this, new File("E:\\ts\\SHELL32.DLL"));   
           
    }
    
    
    
    public AnyEdit(File f) {
        this();
        this.loadFile(this, f);
    }
    
    
    
    private void saveFile(File file) {
        try {
            //Console.println("Wrting");
            FileWriter fw = new FileWriter(file);
            fw.write(txta.getText(), 0, txta.getText().length());
            fw.close();  
////            MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, file));
            fileContentChanged = false;  
        } catch (Exception ex) {
            //ex.printStackTrace();
            MDS_OptionPane.showMessageDialog(this, ex.toString(), "Any Edit", JOptionPane.ERROR_MESSAGE);
        }
    } 
    
    
    
    private void openFile() {
        txta.setText("");
        MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
        Vector v = new Vector();
        v.add("txt");
        v.add("java");
        v.add("cpp");
        v.add("cxx");
        v.add("c");
        v.add("h");
        v.add("html");
        v.add("htm");
        fmfc.setFilter(v);
        if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {   
            loadFile(this, new File(fmfc.getPath()));       
            currentFile = new File(fmfc.getPath());  
            fileContentChanged = false;                        
        }    
    }
    
    
    
    private String showFileSaveDialog() {
        MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.SAVE_DIALOG);          
        Vector v = new Vector();
        v.add("txt");
        v.add("java");
        v.add("cpp");
        v.add("c");
        v.add("html");
        fmfc.setFilter(v);
        if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) { 
            return fmfc.getPath();
        } else {
            return null;
        }    
    }    
    
    
    
    public void appendCurruntText(String text) {
        txta.append(text);
    }
    
    
    
    public void setCurrentFile(File f) {
        currentFile = f;
    }
    
    
    
    private void refreshMenuBar() {
        //mniUndo.setEnabled(false);
        if(txta.getSelectedText() == null) {
            mniCut.setEnabled(false);
            mniCopy.setEnabled(false);
            mniDelete.setEnabled(false);
        } else {
            mniCut.setEnabled(true);
            mniCopy.setEnabled(true);
            mniDelete.setEnabled(true);
        }

        JTextField jtxtDummy = new JTextField();
        jtxtDummy.paste();
        if(jtxtDummy.getText().equals("")) {
            mniPaste.setEnabled(false);
        } else {
            mniPaste.setEnabled(true);
        }

        jtxtDummy = null;

        if(txta.getText().equals("")) {
            mniSelectAll.setEnabled(false);
        } else {
            mniSelectAll.setEnabled(true);
        }

    }    
    
    
    
    private void loadFile(MDS_Frame ae, File f) {
    
    
    
        class LoadFile extends MDS_Dialog implements Runnable, ActionListener {
        
        
        
            private Thread thrLoad = new Thread(this);
            private JComponent contentPane;
            private MDS_ProgressBar prgbLoad;
            private MDS_Button btnCancel = new MDS_Button("Cancel");
            private File f;
            private AnyEdit anyEdit;
            private boolean cancel = false;
        
        
            public LoadFile(MDS_Frame parent, File file) {
                super(parent, "Loading..");
                this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
                this.setClosable(false);
                f = file;
                anyEdit =(AnyEdit)parent;
                contentPane = (JComponent) this.getContentPane();   
                contentPane.setLayout(null);
                String slen = String.valueOf(file.length()/1024);
                Integer i = new Integer(slen);
                int max = i.intValue();                
                prgbLoad = new MDS_ProgressBar(0, max);
                prgbLoad.setBounds(10, 20, 264, 20);
                contentPane.add(prgbLoad);
                btnCancel.setBounds(195, 60, 80, 28); 
                btnCancel.addActionListener(this);
                contentPane.add(btnCancel);
                this.setSize(300, 110);
                this.setCenterScreen();
                this.setVisible(true); 
                //thrLoad.setPriority(Thread.MIN_PRIORITY);
                thrLoad.start();        
            }
            
            
            
            public void run() {
                try {
                
                    int off = 0;
                    int len = 1024;        
                    long fLen = f.length();        
                    long cfLen = 0;
                    long cfLen2 = 0;       
                             
                    DataInputStream disIn = new DataInputStream(new FileInputStream(f));                
                    while(disIn.available() != 0 && !cancel) {            
                        if((fLen - cfLen) <= len) {
                            String slen = String.valueOf(fLen - cfLen); 
                            Integer i = new Integer(slen); 
                            len = i.intValue(); 
                            cfLen2 = cfLen2 + i.intValue();    
                        } else {            
                            cfLen = cfLen+len;
                            cfLen2 = cfLen2 + len;
                        }
            
                        byte b[] = new byte[len];
                        disIn.read(b,off,len);
                        anyEdit.appendCurruntText(new String(b));
                        
                        String slen = String.valueOf(cfLen/1024); 
                        Integer i = new Integer(slen); 
                        int plen = i.intValue();                      
                        prgbLoad.setValue(plen);                        
                        
                    } 
                    
                    disIn.close();                   
                    anyEdit.setCurrentFile(f);
                    this.dispose();
                    
                } catch (Exception ex) {}                                   
            }
            
            
            
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Cancel")) {
                    cancel = true;
                }
            }
            
            
        }
        
        new LoadFile(ae, f);
        
                    
    }
    
    
    
    public static void MDS_Main(String arg[]) {  
        if(arg.length == 1) {
            new AnyEdit(new File(arg[0]));
        } else {
            new AnyEdit();
        }
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("New")) {
            if(fileContentChanged) {
                int r = MDS_OptionPane.showConfirmDialog(this, "The text in the untitled file has changed.\n\nDo you want to save the changes?", "Any Edit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(r == JOptionPane.YES_OPTION) {
                    if(currentFile == null) {
                        String path = showFileSaveDialog();
                        if(path != null) {
                            saveFile(new File(path));
                            txta.setText("");
                            currentFile = null;  
                            fileContentChanged = false;                             
                        }                       
                    } else {
                        //Console.println("OWW");
                        saveFile(currentFile);
                        txta.setText("");
                        currentFile = null;  
                        fileContentChanged = false; 
                    } 
                } else if(r == JOptionPane.NO_OPTION) {
                    txta.setText("");
                    currentFile = null;  
                    fileContentChanged = false;
                }else if(r == JOptionPane.CANCEL_OPTION) {
                    return;
                }                     
            } else {
                txta.setText("");
                currentFile = null;  
                fileContentChanged = false;                
            }
                    
        } else if(e.getActionCommand().equals("Open")) {
            if(fileContentChanged) {
                int r = MDS_OptionPane.showConfirmDialog(this, "The text in the untitled file has changed.\n\nDo you want to save the changes?", "Any Edit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(r == JOptionPane.YES_OPTION) {
                    if(currentFile == null) {
                        String path = showFileSaveDialog();
                        if(path != null) {
                            saveFile(new File(path));
                            openFile();
                        }                       
                    } else {
                        saveFile(currentFile);
                        openFile();
                    } 
                } else if(r == JOptionPane.NO_OPTION) {
                    txta.setText("");                    
                    openFile();                    
                }else if(r == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            } else {
                openFile();
            }
            

                         
        } else if(e.getActionCommand().equals("Font")) {
            MDS_FontChooser fc = new MDS_FontChooser(txta.getFont());
            if(fc.showFontChooser(this) == fc.APPROVE_OPTION) {
                txta.setFont(fc.getFont());
            }
        } else if(e.getActionCommand().equals("Color")) {
            MDS_ColorChooser cc = new MDS_ColorChooser(txta.getForeground());
            if(cc.showColorChooser(this) == cc.APPROVE_OPTION) {
                txta.setForeground(cc.getColor());
            }
        } else if(e.getActionCommand().equals("Save")) {
            if(currentFile == null) {
                String path = showFileSaveDialog();
                if(path != null) {                    
                    saveFile(new File(path));                   
                    currentFile = new File(path);
                }                 
            } else {   
                saveFile(currentFile);    
            }
        } else if(e.getActionCommand().equals("Save As")) {
            String path = showFileSaveDialog();
            /*
            if(path != null) {
                boolean b = fileContentChanged;
                saveFile(new File(path));
                fileContentChanged = b;
            } */                
        } else if(e.getActionCommand().equals("Cut")) {
            txta.cut();
        } else if(e.getActionCommand().equals("Copy")) {
            txta.copy();
        } else if(e.getActionCommand().equals("Paste")) {
            txta.paste();
        } else if(e.getActionCommand().equals("Delete")) {
            StringBuffer text = new StringBuffer(txta.getText());
            int start = txta.getSelectionStart();
            int end = txta.getSelectionEnd();
            String unDeletedText = text.delete(start, end).toString();
            txta.setText(unDeletedText);            
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "AnyEdit", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"anyedit.png"), MDS.getAbout_Mahesh());
        } else if(e.getActionCommand().equals("Exit")) {   
            this.dispose();
        }

    }
    
    
    
    public void menuCanceled(MenuEvent e) {}
    
    
    
    public void menuDeselected(MenuEvent e) {}
    
    
    
    public void menuSelected(MenuEvent e) {
        refreshMenuBar();    
    }
    
    
    
    public void textFileDrop(File f) {
        currentFile = f;  
        fileContentChanged = false; 
    }
    
    
}      