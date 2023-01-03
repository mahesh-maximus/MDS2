import javax.swing.*;



public class Test {



    public static void main(String a[]) {
        WinProcess com = new WinProcess();
/*
        try {   
            com.createMutex("Ma");   
        } catch(Exception ex) {
            System.out.println("MUTEX EX");
            ex.printStackTrace();
        }
        long id = com.createProcess("FexMnt.exe");
        System.out.println(id); 
        JOptionPane.showMessageDialog(null, "dd", "dd",JOptionPane.PLAIN_MESSAGE);
        System.out.println(com.terminateProcess(id));
        System.out.println("ValidationCode = "+String.valueOf(com.getValidationCode())); 
*/
        com.executeExe("OutputRedirector.exe", "javac.exe", com.SW_SHOW);        
    }

}     