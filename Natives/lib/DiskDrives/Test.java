

public class Test {

    public static void main(String []a) {

        //System.out.println("Code  "+String.valueOf(DiskDrives.getValidationCode()));
        DiskDrives dn = new DiskDrives();/*
        int t=dn.getDriveType("C:\\");
        System.out.println(t); 
        System.out.println();
        dn.getDriveVolumeLabel("D:\\"); 
        System.out.println(dn.getFileSystem("D:\\")); 
        dn.getFileSystem("D:\\");
        System.out.println("*************************"); 
        System.out.println(dn.getDiskFreeSpace("E:\\"));

               
        dn.setDriveVolumeLabel("T:\\", "JH");*/

        System.out.println(dn.getDriveVolumeLabel("F:\\"));
        System.out.println(dn.getDiskFreeSpace("U:\\"));
    }
}