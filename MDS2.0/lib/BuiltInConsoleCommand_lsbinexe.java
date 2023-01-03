
import java.util.*;
import java.io.*;


public class BuiltInConsoleCommand_lsbinexe implements BuiltInConsoleCommand {



	private static final String COMMAND_NAME = "lsbinexe";
		



	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	
	
	public String getCommandHelp() {
		return "Lists the execuatable files in bin directory.";
	}



	public boolean executeCommand(MDS_Console console, String command) {
		if (command.equals(COMMAND_NAME)) {
			Vector vFilter = new Vector();
			vFilter.addElement("class");
			vFilter.addElement("jar");
			Vector vFiles = FileManager.getFileManager().getContent_Files(MDS.getBinaryPath(), vFilter);

			console.println(String.valueOf(vFiles.size()));
			for(int x=0;x<=vFiles.size()-1;x++) {
				File f = (File)vFiles.elementAt(x);
				if(f.getName().indexOf("$") <= 0) { 
					if(ProcessManager.getProcessManager().isMDS_Executable(f)) {
						console.println(f.getName());
					}
				}
			}
	  		return true;
		}
		return false;
	}
	
}