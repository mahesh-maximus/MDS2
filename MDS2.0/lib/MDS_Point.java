/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
public class MDS_Point {



    int X = 0;
    int Y = 0;



    public MDS_Point(int x, int y) {
        X = x;
        Y = y;    
    }
    
    
    
    public MDS_Point(MDS_Point p) {
        X = p.getX();
        Y = p.getY();    
    }    
    
    
    
    public int getX() {
        return X;
    }
    
    
    
    public int getY() {
        return Y;
    }
    
    
    
    public boolean equals(MDS_Point p) {
        if(p.getX() == X && p.getY() == Y) {
            return true;
        } else {
            return false;
        }
    }
    
    
    
}    