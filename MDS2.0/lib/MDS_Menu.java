/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;



public class MDS_Menu extends JMenu {



    public MDS_Menu(String s, Icon i) {
        super(s);
        this.setIcon(i);
    }
    
    
    
    public MDS_Menu(String s) {
        super(s);
    }
    
    
    
    public MDS_Menu(String s, int mnemonic) {
        super(s);
        this.setMnemonic(mnemonic);
    } 
    
    
    
    public MDS_Menu(String s, Icon i, int mnemonic) {
        super(s);
        this.setIcon(i);
        this.setMnemonic(mnemonic);
    }       

}