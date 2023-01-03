public class Test {

public static void main(String a[]) {
WinUI ui = new WinUI();
//System.out.println(ui.findWindow("w123","Mahesh"));
//ui.showWindow("w123","Mahesh"); 
//System.out.println(ui.getValidationCode());  
 
ui.sendMessage("ThunderRT6FormDC","WMP_Server For MDS", "PLAY>C:\\WINDOWS\\Desktop\\mp3\\Win_MP\\Metallica - Nothing else matters.mp3");
//ui.sendMessage("ThunderFormDC","Target", "STOP>");
//ui.sendMessage("ThunderFormDC","Target", "SEEK>99");
//ui.sendMessage("ThunderFormDC","Target", "VOLUME>500");
}

}