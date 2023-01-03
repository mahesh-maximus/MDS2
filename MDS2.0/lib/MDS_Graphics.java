/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import java.awt.*;
import java.awt.event.*;
import java.util.*;



public class MDS_Graphics {



    private static MDS_Graphics grf = new MDS_Graphics();



    private MDS_Graphics() {}
    
    
    
    public RenderingHints getRenderingHints() {
    
        RenderingHints hints = new RenderingHints(null);
        
        hints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      
        hints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_ENABLE);
        
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        
        hints.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);    
        
        return hints; 
               
     } 
     
     
     
     public static MDS_Graphics getMDS_Graphics() {
         return grf;
     } 

}