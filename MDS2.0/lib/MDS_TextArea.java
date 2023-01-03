/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.io.*;
import java.awt.datatransfer.*;
import javax.swing.event.*;
import java.util.*;




public class MDS_TextArea extends JTextArea implements ActionListener, MouseListener, DropTargetListener {



    MDS_PopupMenu popup;
    JMenuItem mniUndo;
    JMenuItem mniCut;
    JMenuItem mniCopy;
    JMenuItem mniPaste;
    JMenuItem mniDelete;
    JMenuItem mniSelect_All;
    
    MDS_Frame frm;
    
    boolean dropEnabled = false;
	boolean popupMenuEnabled = false;
    
    private EventListenerList fileDropListenerList = new EventListenerList();
    private EventListenerList textListenerList = new EventListenerList();    

    


    public MDS_TextArea() {
        super();
        this.addMouseListener(this);
        
        new DropTarget(this, this); 
        
        popup = new MDS_PopupMenu("Mahesh");
        
        mniUndo = new JMenuItem("Undo", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"undo.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        mniUndo.addActionListener(this);
        mniUndo.setMnemonic('U');
        popup.add(mniUndo);
        popup.addSeparator();
        mniCut = new JMenuItem("Cut", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"cut.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        mniCut.addActionListener(this);
        mniCut.setMnemonic('t');
        popup.add(mniCut);
        mniCopy = new JMenuItem("Copy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"copy.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        mniCopy.addActionListener(this);
        mniCopy.setMnemonic('C');
        popup.add(mniCopy);
        mniPaste = new JMenuItem("Paste", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"paste.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        mniPaste.addActionListener(this);
        mniPaste.setMnemonic('P');
        popup.add(mniPaste);
        mniDelete = new JMenuItem("Delete", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"delete.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        mniDelete.addActionListener(this);
        mniDelete.setMnemonic('D');
        popup.add(mniDelete);
        popup.addSeparator();
        mniSelect_All = new JMenuItem("      Select All");
        mniSelect_All.addActionListener(this);
        mniSelect_All.setMnemonic('A');
        popup.add(mniSelect_All);
        initialize();
        this.add(popup);

    }
	
	public MDS_TextArea(String text) 
	{
		this();
		this.setText(text);
	}
    
    
    
    public MDS_TextArea(MDS_Frame f) {
        this();
        frm = f;
    }
    
    
    
    private void initialize() {
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                fireTextChanged();
            }
        });    
        this.addMouseListener(new MouseAdapter() {public void
            mouseClicked(MouseEvent e) {}});    
    }    



    private void refresh_Editing() {
        mniUndo.setEnabled(false);
        if(this.getSelectedText() == null) {
            mniCut.setEnabled(false);
            mniCopy.setEnabled(false);
            mniDelete.setEnabled(false);
        } else {
            mniCut.setEnabled(true);
            mniCopy.setEnabled(true);
            mniDelete.setEnabled(true);
        }

        if(this.isEditable()) {
            JTextField jtxtDummy = new JTextField();
            jtxtDummy.paste();
            if(jtxtDummy.getText().equals("")) {
                mniPaste.setEnabled(false);
            } else {
                mniPaste.setEnabled(true);
            }
            
            jtxtDummy = null;
            
        } else {
            mniCut.setEnabled(false);
            mniDelete.setEnabled(false);
            mniPaste.setEnabled(false);
        } 

        if(this.getText().equals("")) {
            mniSelect_All.setEnabled(false);
        } else {
            mniSelect_All.setEnabled(true);
        }

    }
    
    
    
    public void setDropEnabled(boolean b) {
        dropEnabled = b;
    }
    
    
    
    public boolean isDropEnabled() {
        return dropEnabled;
    }
    
    
    
    public void textFile_Drop() {}
    
    
    
    public void addTextFileDropListener(TextFileDropListener l) {
        fileDropListenerList.add(TextFileDropListener.class, l);
    }
    
    
    
    public void removeTextFileDropListener(TextFileDropListener l) {
        fileDropListenerList.remove(TextFileDropListener.class, l);
    }
    
    
    
    public void addTextListener(MDS_TextListener l) {
        textListenerList.add(MDS_TextListener.class, l);    
    }
    
    
    
    public void removeTextListener(MDS_TextListener l) {
        textListenerList.remove(MDS_TextListener.class, l);    
    }
    
    
    
    private void fireTextChanged() {
        Object[] listeners = textListenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {                   
            if (listeners[i]==MDS_TextListener.class) {
                ((MDS_TextListener)listeners[i+1]).textChanged(new MDS_TextEvent(listeners[i+1]));                                                                          
            }                       
        }          
    }
    
    
    
    public void setText(String text) {
        super.setText(text);
        fireTextChanged();      
    }
    
    
    
    public void cut() {
        super.cut();
        fireTextChanged(); 
    }
    
    
    
    public void paste() {
        super.paste();
        fireTextChanged(); 
    } 
	
	
	
	public void setPopupMenuEnabled(boolean enabled) {
		popupMenuEnabled = enabled;
	}   
	
	
	
	public boolean isPopupMenuEnabled() {
		return popupMenuEnabled;
	}   	
    
    
    
    private void loadFile(MDS_TextArea ta, File f) {
    
    
    
        class LoadFile implements Runnable {
        
        
        
            MDS_TextArea txta;
            Thread thrLoad;
            File file;

        
        
            public LoadFile(MDS_TextArea ta, File f) {
                txta = ta;
                file = f;
                thrLoad = new Thread(this);
                thrLoad.start();        
            }
            
            
            
            public void run() {
                try {
                    int off = 0;
                    int len = 1024;        
                    long fLen = file.length();        
                    long cfLen = 0;      
                    
                    txta.setText("");
                             
                    DataInputStream disIn = new DataInputStream(new FileInputStream(file));                
                    while(disIn.available() != 0) {            
                        if((fLen - cfLen) <= len) {
                            String slen = String.valueOf(fLen - cfLen); 
                            Integer i = new Integer(slen); 
                            len = i.intValue();   
                        } else {            
                            cfLen = cfLen+len;
                        }
            
                        byte b[] = new byte[len];
                        disIn.read(b,off,len);
                        txta.append(new String(b));                       
                    } 
                    
                    disIn.close(); 
                    
                    Object[] listeners = fileDropListenerList.getListenerList();
                    for (int i = listeners.length-2; i>=0; i-=2) {                   
                        if (listeners[i]==TextFileDropListener.class) {
                            ((TextFileDropListener)listeners[i+1]).textFileDrop(file); 
                        }
                    }                      

                    
                } catch (Exception ex) {}                                   
            }
          
            
            
        }
        
        new LoadFile(ta, f);
        
                    
    }    



    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Undo")) {

        }
        else if(e.getActionCommand().equals("Cut")) {
            this.cut();
        }
        else if(e.getActionCommand().equals("Copy")) {
            this.copy();
        }
        else if(e.getActionCommand().equals("Paste")) {
            this.paste();
        }
        else if(e.getActionCommand().equals("Delete")) {
            StringBuffer text = new StringBuffer(this.getText());
            int start = this.getSelectionStart();
            int end = this.getSelectionEnd();
            String unDeletedText = text.delete(start, end).toString();
            this.setText(unDeletedText);
        }
        else if(e.getActionCommand().equals("      Select All")) {
            this.selectAll();
        }
    }



    public void mouseClicked(MouseEvent e) {

    }



    public void mouseEntered(MouseEvent e) {

    }



    public void mouseExited(MouseEvent e) {

    }



    public void mousePressed(MouseEvent e) {
        if(e.getButton() == e.BUTTON3) {
            if(this.isEnabled()) {
				if(popupMenuEnabled) {
                	refresh_Editing();
                	popup.show(this, e.getX(), e.getY());
				}
            }
        }
    }



    public void mouseReleased(MouseEvent e) {

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
    
        if(!dropEnabled) {
            e.rejectDrop();
            return;
        }
    
        try { 
            Transferable transferable = e.getTransferable();
            File f = new File(String.valueOf(transferable.getTransferData(DataFlavor.stringFlavor)));
            if(f.exists()) {
                if(f.isDirectory()) {
                    e.rejectDrop();
                } else {
                    
                    Vector v = new Vector();
                    v.add("txt");
                    v.add("java");
                    v.add("cpp");
                    v.add("cxx");
                    v.add("c");
                    v.add("h");
                    v.add("html");
                    v.add("htm");
                    
                    if(v.contains(MDS.getFileManager().getFileExtension(f.getName()))) {
                        loadFile(this, f);                       
                    } else {
                        e.rejectDrop();
                        MDS_OptionPane.showMessageDialog(frm, "Unsupported file type "+f.getName()+".", "MDS_TextArea", JOptionPane.ERROR_MESSAGE); 
                    }
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    
    
    public void dropActionChanged(DropTargetDragEvent dtde) {}


}