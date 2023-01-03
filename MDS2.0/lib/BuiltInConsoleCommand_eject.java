
import java.util.*;
import java.io.*;


public class BuiltInConsoleCommand_eject implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "eject";
		



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Eject removable media (Ex: eject e:).";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.startsWith(COMMAND_NAME+ " ")) {
			String drive = command.substring(6);
			try {
				OperatingSystem.getOperatingSystem().ejectRemovableMedia(drive);
			} catch(Exception ex) {
				console.printError(ex.toString());
			}
	  		return true;
		}
		return false;
	}
	
}