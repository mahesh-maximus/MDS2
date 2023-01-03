
import javax.swing.*;
import javax.swing.text.*;



public class MDS_PasswordField extends JPasswordField {



    public MDS_PasswordField() {
        super();
    }
    
    
    
    public MDS_PasswordField(Document doc, String txt, int columns) {
        super(doc, txt, columns);
    }
    
    
    
    public MDS_PasswordField(int columns) {
        super(columns);
    }
    
    
    
    public MDS_PasswordField(String text) {
        super(text);
    }
    
    
    
    public MDS_PasswordField(String text, int columns) {
        super(text, columns);
    }
    
    
    
}