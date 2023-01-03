
import java.awt.*;



public class Commander extends MDS_Frame {



	public Commander(boolean display) {
		super("Commander",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-terminal.png")); 
		this.setLayout(new BorderLayout());
		MDS_Console console = new MDS_Console(display, MDS_Console.DISPLAY_MODE_WINDOW);
		this.add(console);    
		this.setSize(700, 500);
		this.setCenterScreen();
		this.setVisible(true);
		console._grabFocus();
	}
	
	
	
	public static void MDS_Main(String arg[]) { 
	    //new Commander(true);
	    MDS_Console console = new MDS_Console(true, MDS_Console.DISPLAY_MODE_WINDOW);
	}
	
}	