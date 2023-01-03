/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import java.awt.*;



public class Mouse {


   
//    public native int getMousePointerPosX();
//    
//    
//    
//    public native int getMousePointerPosY();
//    
//    
//    
//    public static native long getValidationCode();



    public int getMousePointerPosX() {
    	return new Double(MouseInfo.getPointerInfo().getLocation().getX()).intValue();
    }
    
    
    
    public int getMousePointerPosY() {
    	return new Double(MouseInfo.getPointerInfo().getLocation().getY()).intValue();
    }
    
    
    
    public MDS_Point getMousePointerPos() {
        //return new MDS_Point(getMousePointerPosX(), getMousePointerPosY());
        return new MDS_Point(new Double(MouseInfo.getPointerInfo().getLocation().getX()).intValue(), new Double(MouseInfo.getPointerInfo().getLocation().getY()).intValue());
    }

    
}
 