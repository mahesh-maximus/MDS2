/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;



public class MDS_TextField extends JTextField implements ActionListener, MouseListener {



    private MDS_PopupMenu popup;
    private JMenuItem mniUndo;
    private JMenuItem mniCut;
    private JMenuItem mniCopy;
    private JMenuItem mniPaste;
    private JMenuItem mniDelete;
    private JMenuItem mniSelect_All;
    
    private EventListenerList textListenerList = new EventListenerList();    
    


    public MDS_TextField() {
        super();
        this.addMouseListener(this);
        
        this.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fireTextChanged();
            }
        });    
        
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
    
    
    
    public MDS_TextField(String text) {
        this();  
        this.setText(text);
    }
    
    
    
    private void initialize() {}      



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
                ((MDS_TextListener)listeners[i+1]).textChanged(new MDS_TextEvent(listeners[i+1] ,this.getText()));                                                                          
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
                refresh_Editing();
                popup.show(this, e.getX(), e.getY());
            }
        }
    }



    public void mouseReleased(MouseEvent e) {

    } 


}