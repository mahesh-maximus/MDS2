


public class BuiltInConsoleCommand_gc implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "gc";
	
    private Runtime rt = Runtime.getRuntime();
    private FileManager fm = MDS.getFileManager();	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Runs the java garbage collector.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			console.printInfo("Available Free Memory [Before the GC is called] "+fm.getFormatedFileSize(rt.freeMemory()));
			System.gc();
			console.printInfo("Available Free Memory [After the GC is called]"+fm.getFormatedFileSize(rt.freeMemory()));
	  		return true;
		}
		return false;
	}
	
}