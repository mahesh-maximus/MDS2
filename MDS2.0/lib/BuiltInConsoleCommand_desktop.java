


public class BuiltInConsoleCommand_desktop implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "desktop";	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Loads the Desktop.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			if(!MDS.isDesktopLoaded()) {
				console.printInfo("Loading desktop ...");
				console.setPromptVisible(false);
				//MDS.loadDesktop();
			} else {
				console.printInfo("Desktop already loaded.");
			}
	  		return true;
		}
		return false;
	}
	
}