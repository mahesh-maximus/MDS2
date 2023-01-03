


public class BuiltInConsoleCommand_exit implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "exit";
	
    private Runtime rt = Runtime.getRuntime();
    private FileManager fm = MDS.getFileManager();	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Quits the command interpreter.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			console.dispose();
	  		return true;
		}
		return false;
	}
	
}