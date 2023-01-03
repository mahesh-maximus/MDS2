import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.swing.text.*; 



public class ApacheTomcatServerLauncher extends MDS_Frame implements ActionListener {
	

	private List<String> command;	

	String QUOTE = String.valueOf('"'); 
		
	MDS_User usr = MDS.getUser();	

    JMenuBar mnb = new JMenuBar();
    MDS_Menu mnuFile = new MDS_Menu("File", KeyEvent.VK_F); 
    JMenuItem mniClose = usr.createMenuItem("Close", this);
    
    MDS_Menu mnuTomcat = new MDS_Menu("Tomcat", KeyEvent.VK_T);
    JMenuItem mniTomcat = usr.createMenuItem("Start", this);
    JMenuItem mniTomcatHomePage = usr.createMenuItem("Tomcat Home Page", this);
    JMenuItem mniTomcatConfig = usr.createMenuItem("Configuration", this);
    
    MDS_Menu mnuHelp = new MDS_Menu("Help", KeyEvent.VK_H);
    JMenuItem mniAbout = usr.createMenuItem("About", this, KeyEvent.VK_A);  
    	
    private MDS_TabbedPane tbpOutputs = new MDS_TabbedPane();

    MDS_TextArea txtaOutput = new MDS_TextArea();
    MDS_TextArea txtaErrorOutput = new MDS_TextArea();  
    	
    private MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();	
    		  
    
	 
	public static void MDS_Main(String[] args) {
		// TODO: Add your code here
		new ApacheTomcatServerLauncher();
	}

	/**
	 * Method ApacheTomcatServerLauncher
	 *
	 *
	 */
	public ApacheTomcatServerLauncher() {
		// TODO: Add your code here
		
		super("Apache Tomcat Server Launcher",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-mime-gf.png")); 
		 this.setSize(700, 400);
    	 this.setCenterScreen();
    	 
    	 this.setLayout(new BorderLayout());
    	 
    	mnuFile.add(mniClose);
        mnb.add(mnuFile);
        
        mnuTomcat.add(mniTomcat);
        mnuTomcat.add(mniTomcatHomePage);
        mnuTomcat.add(mniTomcatConfig);
        mnb.add(mnuTomcat);
        
        mnuHelp.add(mniAbout);
        mnb.add(mnuHelp);
        
        this.setJMenuBar(mnb);
        
        txtaOutput.setFont(new Font("Courier", Font.PLAIN, 12)); 
        txtaOutput.setBackground(Color.black);
        txtaOutput.setForeground(Color.green);
        txtaOutput.setEditable(false);
        //txtaOutput.append("MDS 1.0\nMDS [Standard output]\n\n");
        tbpOutputs.addTab("Output", new JScrollPane(txtaOutput)); 
        txtaErrorOutput.setFont(new Font("Courier", Font.PLAIN, 12));
        txtaErrorOutput.setEditable(false);    
        txtaErrorOutput.setBackground(Color.black);
        txtaErrorOutput.setForeground(Color.green);   
        //txtaErrorOutput.append("MDS 1.0\nMDS [Standard error output]\n\n");     
        tbpOutputs.addTab("Error Output",new JScrollPane(txtaErrorOutput));
        this.add(tbpOutputs, BorderLayout.CENTER);        
    	 
    	this.setVisible(true);
    	 
	}	
		
		
	public  void actionPerformed(ActionEvent e) {
		ProcessManager prm = MDS.getProcessManager();
        if(e.getActionCommand().equals("Start")) {
        	mniTomcat.setText("Stop");    
            try {	
				ApacheTomcatServerManager.getApacheTomcatServerManager().start(txtaOutput);
            } catch(Exception ex) {
            	txtaOutput.append(ex.toString());
            }
        } else if(e.getActionCommand().equals("Stop")) {
        	mniTomcat.setText("Start");
			ApacheTomcatServerManager.getApacheTomcatServerManager().stop();
		} else if(e.getActionCommand().equals("Tomcat Home Page")) {
          	MDS_Property propDp = ppm.getProperty(ApacheTomcatServerManager.PROPERTY_NAME);
	        String tomcatPort = propDp.getSupProperty(ApacheTomcatServerManager.PROPERTY_SERVER_PORT);
			String[] filePath ={"http://127.0.0.1:"+tomcatPort};
			prm.execute(new File(MDS.getBinaryPath(), "WebBrowser.class"), filePath); 
        } else if(e.getActionCommand().equals("Close")) {
            this.dispose();        
        } else if(e.getActionCommand().equals("About")) {
            usr.showAboutDialog(this, "Tomcat", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"anyedit.png"), MDS.getAbout_Mahesh());
        } else if(e.getActionCommand().equals("Configuration")) {
        	prm.execute(new File(MDS.getBinaryPath(), "ApacheTomcatConfig.class")); 	
        }

		
	}
	
	
	
	
	
	class Tomcat extends Thread  {
		
		
		
		String param = "";
		
		
		
		public Tomcat(String param) {
			super();
			this.param = param;
			start();
		}

	}
	
	
	
	
   
	
		
}






