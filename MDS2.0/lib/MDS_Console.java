

import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import java.util.*;

import javax.swing.*;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.util.Iterator;
import java.util.List;



public class MDS_Console extends JScrollPane{




	public static final int DISPLAY_MODE_WINDOW = 99;
	public static final int DISPLAY_MODE_FULL_SCREEN = 423;
	
	private int currentDisplayOption;
	
	private MDS_Frame frmConsole;
	private MDS_Panel pnlConsole;
	private JScrollPane scrollPane;
	private String current;
	private Document outputDocument;
	private ConsoleTextPane textArea;
	private ConsoleCommandHistory historyModel = new ConsoleCommandHistory(25);
	
	/**This is the point from where starts the text the user can edit. The text 
	* before was either output or (from the user but accepted with &lt;Enter&gt;)*/
	private int userLimit = 0;
	
	/**This is where the user-typed text that hasn't still be accepted ends. If it's
	* before the document length, the user cannot type.*/
	private int typingLocation = 0;
	
	/**If the command is taken from the history, this is its position inside it:
	* 0 for the last command, 1 for the one before and so on; if it's -1, the command
	* doesn't come from the history.*/
	private int index = -1;

	// colors
	public Color errorColor = Color.red;
	public Color promptColor = Color.blue;
	public Color outputColor = Color.black;
	public Color infoColor = new Color(0, 165, 0);

	// prompt
	private boolean displayPath;
	private String prompt, hostName, oldPath = new String();

	private Vector vctBuiltInCommands = new Vector();
  
 	private String currentDirectory = System.getProperty("user.dir"); 

	private int CarriageReturnPoint = 0;
	private int CarriageReturnLength = 0;
	
	private boolean promptVisible = true;
	
	//private boolean lineRead = true;
	private MDS_Console console = this;
	
	
	

	public MDS_Console() {
		this(false, DISPLAY_MODE_WINDOW);
	}
  
  
  
	public MDS_Console(boolean display, int displayOption1) {	
		this(display, true, false, displayOption1);
	}
	
	
	
	public MDS_Console(boolean display, boolean vScrollBar, boolean hScrollBar, int displayOption) { 

	 	//super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				
		scrollPane = new JScrollPane();
		
		if(vScrollBar) this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		else scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		if(hScrollBar) this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		else scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);		
		
	    textArea = new ConsoleTextPane(this);
	    //textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textArea.setFont(new Font("Courier", Font.BOLD, 14));
	    outputDocument = textArea.getDocument();
		append("MDS 2.0 Developers Edition\nDesigned and Developed by Mahesh Dharmasena.\n\n", infoColor, false, true);

	    if (display) displayPrompt();

	    scrollPane.getViewport().setView(textArea);
	    FontMetrics fm = getFontMetrics(textArea.getFont());
	    scrollPane.setPreferredSize(new Dimension(40 * fm.charWidth('m'), 6 * fm.getHeight()));
	    scrollPane.setMinimumSize(getPreferredSize());
	    scrollPane.setMaximumSize(getPreferredSize());
	    scrollPane.setBorder(null);
		
		currentDisplayOption = displayOption;

		if(displayOption == DISPLAY_MODE_WINDOW) {
			frmConsole = new MDS_Frame("Commander",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-terminal.png")); 
			frmConsole.setLayout(new BorderLayout());		
			frmConsole.add(scrollPane, BorderLayout.CENTER);
			frmConsole.setSize(700, 500);
			frmConsole.setCenterScreen();
			frmConsole.setVisible(true);
		} else if(displayOption == DISPLAY_MODE_FULL_SCREEN) {
			pnlConsole = new MDS_Panel(new BorderLayout());
			pnlConsole.add(scrollPane, BorderLayout.CENTER);
			MDS.getBaseWindow().setFullScreenWindow(pnlConsole);
		} else {
			throw new IllegalArgumentException("Invalid screen size.");
		}	

	    initBuiltInCommand();
	    
	    help();
		
		textArea.grabFocus();	
	}
	
	
	
	public void dispose() {
		if(currentDisplayOption == DISPLAY_MODE_WINDOW) {
			frmConsole.dispose();
		} else if(currentDisplayOption == DISPLAY_MODE_FULL_SCREEN) {
			MDS.getBaseWindow().removeFullScreenWindow(pnlConsole);
		}	
	}
	
	
	
	public void setDisplayMode(int displayOption) {
	
	}
	
	
	
	public void _grabFocus() {
		textArea.grabFocus();
	}
	
	
	
	private void initBuiltInCommand() {
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_clear());
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_gc());		
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_pwd());	
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_cd());
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_desktop());	
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_exit());
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_date());
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_ls());	
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_lsbinexe());
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_eject());
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_spts());	
		vctBuiltInCommands.addElement(new BuiltInConsoleCommand_help());	
	}
	
    

	public Document getOutputDocument() {
		return outputDocument;
	}
	
	
	public void help() {
		printInfo("\n");
		for(int x=0;x<=vctBuiltInCommands.size()-1;x++) {
			BuiltInConsoleCommand bic = (BuiltInConsoleCommand)vctBuiltInCommands.elementAt(x);
			printInfo(bic.getCommandName() + " : " + bic.getCommandHelp());
		}
		//displayPrompt();	
	}
	


	/**
	* Return true if command is built-in. If command is built-in,
	* it is also executed.
	* @param command Command to check and execute
	*/
	private boolean builtInCommand(String command) {
		boolean ret = false;
		for(int x=0;x<=vctBuiltInCommands.size()-1;x++) {
			BuiltInConsoleCommand bic = (BuiltInConsoleCommand)vctBuiltInCommands.elementAt(x);
			if(bic.executeCommand(this, command)) {
				ret = true;
			}
		}
		return ret;
	}
	
	
	
	public String getCurrentDirectory() {
		return currentDirectory;
	}
	
	
	
	public File getCurrentDirectoryFile() {
		return new File(currentDirectory);
	}	
	
	
	
	public void setCurrentDirectory(String newPath) {
		File f = new File(newPath);
		if(f.exists() && f.isDirectory()) 
			currentDirectory = newPath;
	}
	
	
	
	public void setConsoleFont(Font font) {
		textArea.setFont(font);	
	}	



	public void setBgColor(Color color) {
		textArea.setBackground(color);
	}



	public void setErrorColor(Color color) {
		errorColor = color;
	}



	public void setPromptColor(Color color) {
		promptColor = color;
	}



	public void setOutputColor(Color color) {
		outputColor = color;
		textArea.setForeground(color);
		textArea.setCaretColor(color);
	}



	public void setInfoColor(Color color) {
		infoColor = color;
	}



	public void setSelectionColor(Color color) {
		textArea.setSelectionColor(color);
	}
	
	
	
	public void setPromptVisible(boolean b) {
		promptVisible = b;	
		if(b) displayPrompt();	
	}
	
	
	
	public void setDisplayOnly(boolean b) {
		textArea.setDisplayOnly(b);
	}
	
	

	/**
	* Displays the prompt according to the current selected
	* prompt type.
	*/
	public void displayPrompt() {
		if (prompt == null || displayPath)
	  		if(promptVisible) buildPrompt();

	  	// $$$ append('\n' + prompt, promptColor);
	  	append(prompt, promptColor);
		typingLocation = userLimit = outputDocument.getLength();
	}
	
	

  	// builds the prompt according to the prompt pattern
	private void buildPrompt() {
		prompt = System.getProperty("user.name")+"$ ";
  	}
	
	
	

	private class Appender implements Runnable {
		private String text;
		private Color color;
		private boolean italic, bold;

		Appender(String _text, Color _color, boolean _italic, boolean _bold) {
	  		text = _text;
	  		color = _color;
	  		italic = _italic;
	  		bold = _bold;
		}

		public void run() {
	  		SimpleAttributeSet style = new SimpleAttributeSet();
	  		if (color != null)
	  		style.addAttribute(StyleConstants.Foreground, color);
	  		StyleConstants.setBold(style, bold);
	  		StyleConstants.setItalic(style, italic);

	  		try {
				if(CarriageReturnPoint > 0) {
					outputDocument.remove(CarriageReturnPoint, CarriageReturnLength);
					CarriageReturnPoint = 0;
				}
				
				boolean cr = false;
				
				//if(text.endsWith("/r")) {
				if(text != null && text.endsWith("/r")) {
			 		cr = true;
					text = text.substring(0, text.length()-2);
					CarriageReturnPoint = outputDocument.getLength();
				}

				outputDocument.insertString(outputDocument.getLength(), text, style);
				
				if(cr) {
					
					CarriageReturnLength = text.length();
					
				}
	  		} catch(BadLocationException bl) { }

	  		textArea.setCaretPosition(outputDocument.getLength());
		}
	}
	
	

	/**
	* This method appends text in the text area.
	* @param text The text to append
	* @param color The color of the text
	* @param italic Set to true will append italic text
	* @param bold Set to true will append bold text
	*/
	private void append(String text, Color color, boolean italic, boolean bold) {
		Runnable appender = new Appender(text, color, italic, bold);
		//if (SwingUtilities.isEventDispatchThread()) {
	  		appender.run();
		//} else {
	  		//SwingUtilities.invokeLater(appender);
		//}
	}
	
	

	/**
	* This method appends text in the text area.
	* @param text The text to append in the text area
	* @apram color The color of the text to append
	*/
	public void append(String text, Color color) {
		append(text, color, false, false);
	}
	
	

	/**
	* Adds a command to the history.
	* @param command Command to add in the history
	*/
	public void addHistory(String command) {
		historyModel.addItem(command);
		index = -1;
	}
	
	

	/**
	* Remove a char from current command line.
	* Stands for BACKSPACE action.
	*/
	public void removeChar() {
		try {
	  		//if (typingLocation != userLimit)
	  		//  outputDocument.remove(--typingLocation, 1);
	  		int pos = textArea.getCaretPosition();
	  		if (pos <= typingLocation && pos > userLimit) {
	    		outputDocument.remove(pos - 1, 1);
	    		typingLocation--;
	  		}
		} catch (BadLocationException ble) { }
	}
	
	

	/**
	* Delete a char from command line.
	* Stands for DELETE action.
	*/
	public void deleteChar() {
		try {
	  		int pos = textArea.getCaretPosition();
	  		if (pos == outputDocument.getLength()) return;
	  		if (pos < typingLocation && pos >= userLimit) {
	    		outputDocument.remove(pos, 1);
	    		typingLocation--;
	  		}
		} catch (BadLocationException ble) { }
	}
	
	

	/**
	* Adds a <code>String</code> to the current command line.
	* @param add <code>String</code> to be added
	*/
	public void add(String add) {
		try {
	  		int pos = textArea.getCaretPosition();
	  		if (pos <= typingLocation && pos >= userLimit)
	    		outputDocument.insertString(pos, add, null);
	  		typingLocation += add.length();
		} catch (BadLocationException ble) { }
	}
	
	

	/**
	* Returns the position in characters at which
	* user is allowed to type his commands.
	* @return Beginning of user typing space
	*/
	public int getUserLimit() {
		return userLimit;
	}

	/**
	* Returns the position of the end of the console prompt.
	*/
	public int getTypingLocation() {
		return typingLocation;
	}



	/**
	* Get previous item in the history list.
	*/
	public void historyPrevious() {
		if (index == historyModel.getSize() - 1)
 	 		getToolkit().beep();
		else if (index == -1) {
 	 		current = getText();
 	 		setText(historyModel.getItem(0));
 	 		index = 0;
		} else {
			int newIndex = index + 1;
  			setText(historyModel.getItem(newIndex));
  			index = newIndex;
		}
	}
	
	

	/**
	* Get next item in the history list.
	*/
	public void historyNext() {
		if (index == -1)
	  		getToolkit().beep();
		else if (index == 0)
		  setText(current);
		else {
	  		int newIndex = index - 1;
	  		setText(historyModel.getItem(newIndex));
	  		index = newIndex;
		}
	}
	
	

	/**
	* Set user's command line content.
	* @param text Text to be put on command line.
	*/
	public void setText(String text) {
		try {
  			outputDocument.remove(userLimit, typingLocation - userLimit);
  			outputDocument.insertString(userLimit, text, null);
 			typingLocation = outputDocument.getLength();
  			index = -1;
		} catch (BadLocationException ble) { }
	}



	/**
	* Returns current command line.
	*/
	public String getText() {
		try {
	  		return outputDocument.getText(userLimit, typingLocation - userLimit);
		} catch (BadLocationException ble) { }
		return null;
	}



	/**
	* Display a message using information color.
	* @since Jext3.2pre4
	* @param display <code>String</code> to be displayed
	*/
	//public void info(String display) {
	public void printInfo(String display) {
		//append('\n' + display, infoColor, false, false);
		append(display + '\n', infoColor, false, false);
	}
	
	

	/**
	* Display a message using help color.
	* @param display <code>String</code> to be displayed
	*/
	//public void help(String display) {
	public void printHelp(String display) {
		//append('\n' + display, infoColor, true, true);
		append(display + '\n', infoColor, true, true);
	}
	
	

	/**
	* Display a message using error color.
	* @param display <code>String</code> to be displayed
	*/
	//public void error(String display) {
	public void printError(String display) {
		//append('\n' + display, errorColor, false, false);
		append(display + '\n', errorColor, false, false);
	}



	/**
	* Display a message using output color.
	* @param display <code>String</code> to be displayed
	*/
	//public void output(String display) {
	public void println(String display) {
		//append('\n' + display, outputColor, false, false);
		append(display + '\n', outputColor, false, false);
	}
	
	

	public void println(String display, Color color) {
		append(display + '\n', color, false, false);
	}	
	
	
	
	public void print(String display) {
		//append('\n' + display, outputColor, false, false);
		append(display, outputColor, false, false);
	}
	
	
	
	public void print(String display, Color color) {
		append(display, color, false, false);
	}
	
	
	
	//public String readLine() {}		
	
	

	/**
	* Stops current task.
	*/
	public void stop() {
		/* $$$
		if (cProcess != null) {
		  cProcess.stop();
	 	 cProcess = null;
		}*/
	}



	/**
	* Execute command. First parse it then check if command
	* is built-in. At last, a process is created and threads
	* which handle output streams are started.
	* @param command Command to be execute
	*/
	public void execute(String command) {
////////		if (command == null)
////////	  		return;
////////		stop();
////////
////////		printInfo("");
////////
////////		command = command.trim();
////////
////////		if (command == null || command.length() == 0 || builtInCommand(command)) {
////////	  		displayPrompt();
////////	  		return;
////////		}
////////		// $$$$	
////////		//#####################################################################################
////////		MDS.getProcessManager().execute(command, this);
////////		append("\n> " + command+"\n", infoColor);
////////		printError("is not recognized as an internal or external command");
////////		displayPrompt();	
////////		//##################################################################################### 
		
		
		
		class Execute extends Thread {
			
			
			
			String command;
			
			
			
			public Execute(String command) {
				super();
				this.command = command;
				start();
			}
			
			
			public void run() {
				printInfo("");
		
				command = command.trim();
		
				if (command == null || command.length() == 0 || builtInCommand(command)) {
			  		displayPrompt();
			  		return;
				}

				MDS.getProcessManager().execute(new File(getCurrentDirectory(), command), console);
				
			}
		}
		
		new Execute(command);
		
	}
	
	
	
	
	
	private class ConsoleTextPane extends JTextPane implements KeyListener, DropTargetListener {
	
	
	
		private MDS_Console parent;
  		private MouseAdapter _mouseListener;
		private boolean displayOnly = false;
  


		public ConsoleTextPane(MDS_Console parent) {
			super();
			this.parent = parent;

			new DropTarget(this, this);
			addKeyListener(this);
			
			_mouseListener = new MouseAdapter() {
			 	public void mousePressed(MouseEvent evt) {
					//System.out.print("TPL : "+parent.getTypingLocation());
					//evt.consume();
					//if (getCaretPosition() < ConsoleTextPane.this.parent.getUserLimit())
		  				//setCaretPosition(getDocument().getLength());
				}
			};
			addMouseListener(_mouseListener);
			
		}
		
		
		
		public void setDisplayOnly(boolean b) {
			displayOnly = b;	
		}
		
		
		
		public void keyPressed(KeyEvent evt) {
	
			int key = evt.getKeyCode();
			
			if(displayOnly) {
				switch (key) {
					case KeyEvent.VK_ENTER:                     // we execute command
						MDS.setInterface(MDS.GUI);
						evt.consume();
						break;
					case KeyEvent.VK_F8:  
						MDS.setInterface(MDS.CONSOLE);
						break;
				}
				
				evt.consume();
				return;
				
									
			}
			
			if (evt.isControlDown()) { // event of the form Ctrl+KEY
			
				switch (key) {
					case KeyEvent.VK_C:                       // Ctrl+C copies selected text
						return;
					case KeyEvent.VK_D:                       // Ctrl+D kills current task
						parent.stop();
						// we wait 1 second otherwise the prompt could not display as expected
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) { }
						parent.displayPrompt();
						break;
				}
				evt.consume();
				return;
			} else if (evt.isShiftDown()) {
				if (key == KeyEvent.VK_TAB) {
					// $$$ ######################## parent.doBackwardSearch();
					evt.consume();
					return;
				}
			}

			switch (key) {
				case KeyEvent.VK_DELETE:                    // we delete a char
					parent.deleteChar();
					evt.consume();
					break;
				case KeyEvent.VK_BACK_SPACE:                // we delete a char
					parent.removeChar();
					evt.consume();
					break;
				case KeyEvent.VK_ENTER:                     // we execute command
					String command = parent.getText();
					if (!command.equals("")) {
						parent.addHistory(command);
					}
					parent.execute(command);					
					// @@@
					MDS.setInterface(MDS.GUI);
					evt.consume();
					break;
				case KeyEvent.VK_UP:                        // we get previous typed command
					parent.historyPrevious();
					evt.consume();
					break;
				case KeyEvent.VK_DOWN:                      // we get next typed command
					parent.historyNext();
					evt.consume();
				case KeyEvent.VK_LEFT:                      // we override the press on LEFT                                // to ensure caret is not on prompt
					if (getCaretPosition() > parent.getUserLimit())
						setCaretPosition(getCaretPosition() - 1);
					evt.consume();
					break;
				case KeyEvent.VK_TAB:                       // complete filename
					// $$$ ##################### parent.doCompletion(); //doBackwardSearch();
					evt.consume();
					break;
				case KeyEvent.VK_HOME:                      // implementation of HOME to go to
					setCaretPosition(parent.getUserLimit());  // beginning of prompt and not line
					evt.consume();
					break;
				case KeyEvent.VK_END:                       // see HOME
					setCaretPosition(parent.getTypingLocation());
					evt.consume();
					break;
				case KeyEvent.VK_ESCAPE:                   // delete line
					parent.setText("");
					evt.consume();
					break;
				// @@@
				case KeyEvent.VK_F8:  
					MDS.setInterface(MDS.CONSOLE);
					break;
			}			
		}
		
		
		
		public void keyReleased(KeyEvent e) {
		
		}
		
		
		
		public void keyTyped(KeyEvent evt) {
			
			if (parent.getTypingLocation() < getDocument().getLength()) { //this forbids from
			//inserting text until prompt is reshown or Enter is pressed(maybe to remove?)
				evt.consume();
				return;
			}
			
			if (getCaretPosition() < parent.getUserLimit())
				setCaretPosition(parent.getUserLimit());

			char c = evt.getKeyChar();

			if (c != KeyEvent.CHAR_UNDEFINED && !evt.isAltDown()) {
				if (c >= 0x20 && c != 0x7f)
					parent.add(String.valueOf(c));
			}
			
			evt.consume();
					
		}
		
		
		
		public void dragEnter(DropTargetDragEvent evt) { }
		
		
		
		public void dragOver(DropTargetDragEvent evt) { }
		
		
		
		public void dragExit(DropTargetEvent evt) { }
		
		
		
		public void dragScroll(DropTargetDragEvent evt) { }
		
		
		
		public void dropActionChanged(DropTargetDragEvent evt) { }
		
		

		public void drop(DropTargetDropEvent evt) {
			DataFlavor[] flavors = evt.getCurrentDataFlavors();
			if (flavors == null)
				return;

			boolean dropCompleted = false;
			for (int i = flavors.length - 1; i >= 0; i--) {
				if (flavors[i].isFlavorJavaFileListType()) {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					Transferable transferable = evt.getTransferable();
					try {
						StringBuffer buf = new StringBuffer();
						Iterator iterator = ((List) transferable.getTransferData(flavors[i])).iterator();
						while (iterator.hasNext())
			  				buf.append(' ').append(((File) iterator.next()).getPath());
						parent.add(buf.toString());
						dropCompleted = true;
					} catch (Exception e) { }
				}
			}
			evt.dropComplete(dropCompleted);
		}
				
		
		
		
			
	

	
  
  
	}	

	
 

}
