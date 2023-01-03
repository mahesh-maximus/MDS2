/*
 * Title:        MDS
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */



public class DiskDrives {



    static {
        try {
            System.loadLibrary("DiskDrives");
        } catch (UnsatisfiedLinkError ex) {
 
        }
    }



    public native int getDriveType(String drive);



    public native String getDriveVolumeLabel(String drive); 



    public native String getFileSystem(String drive); 



    public native long getDiskFreeSpace(String drive);



    public native long getDiskTotalSpace(String drive);



    public native long getDiskTotalUserSpace(String drive);



    public native void setDriveVolumeLabel(String drive , String label);



    public native long getValidationCode();



}
    