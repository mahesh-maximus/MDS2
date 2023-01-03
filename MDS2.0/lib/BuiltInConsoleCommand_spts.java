
import java.util.*;



public class BuiltInConsoleCommand_spts implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "spts";
	
    private Runtime rt = Runtime.getRuntime();
    private FileManager fm = MDS.getFileManager();	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Displays the system properties.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			Properties prop = System.getProperties();
			Enumeration enu = prop.propertyNames();
			while(enu.hasMoreElements()) {
				String name = String.valueOf(enu.nextElement());
				console.println(name+ " : " +prop.getProperty(name));
			} 
	  		return true;
		}
		return false;
	}
	
}