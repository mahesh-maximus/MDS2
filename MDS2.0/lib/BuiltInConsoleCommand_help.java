
import java.util.*;


public class BuiltInConsoleCommand_help implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "help";
		



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Displays the internal commands.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			console.help();
	  		return true;
		}
		return false;
	}
	
}