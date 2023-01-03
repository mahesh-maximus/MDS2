
import java.util.*;
import java.io.*;


public class BuiltInConsoleCommand_ls implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "ls";
	
    private Runtime rt = Runtime.getRuntime();
    private FileManager fm = MDS.getFileManager();	



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Displays a list of files and subdirectories in a directory.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			//Calendar calendar = new GregorianCalendar();
			//console.printInfo(new Date().toString());
			File dir = console.getCurrentDirectoryFile();
			File[] subDirs = dir.listFiles();
			for(File f : subDirs) {
				String type = "<DIR>";
				if(!f.isDirectory()) type = "";
				console.printInfo(new Date(f.lastModified()).toString() + "  "+ type + "  " + f.length()+ "  "+f.getName());	
			}
	  		return true;
		}
		return false;
	}
	
}