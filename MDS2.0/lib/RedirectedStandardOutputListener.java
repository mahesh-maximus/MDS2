/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.util.*;



public interface RedirectedStandardOutputListener extends EventListener {



    public void rsdoNewOutputText(String text);
    
    
    
    public void rsdoErrorText(String text);



}