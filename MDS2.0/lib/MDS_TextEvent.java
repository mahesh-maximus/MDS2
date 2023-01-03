
import java.util.*;



public class MDS_TextEvent extends EventObject {



    String text = "";
    Object source;
    
    
    
    public MDS_TextEvent(Object soc) {
        super(soc);
        source = soc;    
    }    



    public MDS_TextEvent(Object soc, String t) {
        super(soc);
        text = t;
        source = soc;    
    }
    
    
    
    public String getText() {
        return text;
    }
    
    
    
}    