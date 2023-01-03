


public class BuiltInConsoleCommand_pwd implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "pwd";
	
    private Runtime rt = Runtime.getRuntime();
    private FileManager fm = MDS.getFileManager();	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Displays the name of the current directory.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			console.println(console.getCurrentDirectory());
	  		return true;
		}
		return false;
	}
	
}