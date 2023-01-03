/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 


public class MDS_KeyStroke {



    public static KeyStroke getNew() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
    }
    
    
    
    public static KeyStroke getOpen() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK);
    }
    
    
    
    public static KeyStroke getSave() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
    }
    
    
    
    public static KeyStroke getPrint() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
    }
    
    
    
    public static KeyStroke getUndo() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
    }
    
    
    
    public static KeyStroke getCut() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK);
    }
    
    
    
    public static KeyStroke getCopy() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
    }   
    
    
    
    public static KeyStroke getPaste() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK);
    }    
    
    
    
    public static KeyStroke getDelete() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_DELETE , Event.CTRL_MASK);
    }  
    
    
    
    public static KeyStroke getFind() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_F , Event.CTRL_MASK);
    }  
    
    
    
    public static KeyStroke getSelectAll() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_A , Event.CTRL_MASK);
    }                               



}