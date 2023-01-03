
public interface BuiltInConsoleCommand {



	public String getCommandName();
	
	
	
	public String getCommandHelp();



	public boolean executeCommand(MDS_Console console, String command);
	
	

}