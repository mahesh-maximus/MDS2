
import java.io.*;

public class BuiltInConsoleCommand_cd implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "cd";
	
    private Runtime rt = Runtime.getRuntime();
    private FileManager fm = MDS.getFileManager();	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Changes the current directory.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.startsWith(COMMAND_NAME+ " ")) {
			String newPath = command.substring(3);
			if(newPath.equals("..")) {
				String parent = console.getCurrentDirectoryFile().getParent();
				if(parent != null)
					console.setCurrentDirectory(parent);
			} else {
				if(FileManager.getFileManager().isBeginsWithRoot(newPath)) {
					console.setCurrentDirectory(newPath);
				} else {
					File f = new File(console.getCurrentDirectory(), newPath);
					if(f.exists()) {
						console.setCurrentDirectory(f.getPath());	
					} else {
						console.printError("The system cannot find the path specified.");
					} 	
				} 		
			}
	  		return true;
		}
		return false;
	}
	
}