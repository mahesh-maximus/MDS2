
import java.util.*;


public class BuiltInConsoleCommand_date implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "date";
	
    private Runtime rt = Runtime.getRuntime();
    private FileManager fm = MDS.getFileManager();	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Displays the date.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			//Calendar calendar = new GregorianCalendar();
			console.printInfo(new Date().toString());
	  		return true;
		}
		return false;
	}
	
}