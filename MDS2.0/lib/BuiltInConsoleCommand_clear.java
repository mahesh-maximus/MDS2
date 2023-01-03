
import javax.swing.text.*;



public class BuiltInConsoleCommand_clear implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "clear";



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Clears the screen.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
		try {
	    	console.getOutputDocument().remove(0, console.getOutputDocument().getLength());
	  	} catch (BadLocationException ble) { }
	  		return true;
		}
		
		return false;
		
	}
	
}