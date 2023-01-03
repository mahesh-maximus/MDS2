
import java.util.*;



public class StringTable {



    private static Hashtable htSt = new Hashtable();
    private static final char QUTE = '"';
    
    
    
    static {
        htSt.put("1", "File not found.");
        htSt.put("2", "Please verify the corrent file name was given.");
        htSt.put("3", "You cannot save in the folder you specified. Please choose another location.");
        htSt.put("4", "Type new directory name.");
        htSt.put("5", "Unable to create the directory.");
        htSt.put("6", "A file name cannot contain any of the following charactors:\n     \\ / : "+String.valueOf(QUTE)+" ? "+String.valueOf(QUTE)+" < > | ");
        htSt.put("7", "File already exits.");
        htSt.put("8", "Do you want to save the changes?");
        htSt.put("9", "Cannot delete");
        htSt.put("10", "It is being used by another person or program.");
        htSt.put("11", "Close any programs that you might be using ther file and try again.");
        htSt.put("12", "MDS cannot find executable class");
        htSt.put("13", "Are you sure you want to delete");
		htSt.put("14", "File already exits do you want to open it.");
        //htSt.put("", "");
    }



    public StringTable() {}
    
    
    
    public String get(int no) {
        return String.valueOf(htSt.get(String.valueOf(no)));    
    }



}