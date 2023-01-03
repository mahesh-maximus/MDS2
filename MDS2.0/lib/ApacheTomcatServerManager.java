import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.swing.text.*; 


public final class ApacheTomcatServerManager {
	
	/**
	 * Method ApacheTomcatServerManager
	 *
	 *
	 */
	 
	 
	private static ApacheTomcatServerManager atsm = new ApacheTomcatServerManager();
	private static boolean started = false; 
		
    public static final String PROPERTY_NAME = "ApacheTomcatServer";
    public static final String PROPERTY_SERVER_PATH = "serverPath";
	public static final String PROPERTY_SERVER_PORT = "port";
	
	private MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();	
	 
	 
	private ApacheTomcatServerManager() {
		// TODO: Add your code here
	}
	
	
	
	public static ApacheTomcatServerManager getApacheTomcatServerManager() {
		return atsm;
	}
	
	
	
	public boolean started() {return started;};
	
	
	
	public void start() throws IllegalStateException {
		if(started) 
			throw new IllegalStateException("Apache Tomcat Server is already running.");
		new ApacheTomcatServer("start");
		started = true;
	}
	
	
	
	public void stop() {
		new ApacheTomcatServer("stop");
		started = false;
	}
	
	
	
	public void start(MDS_TextArea txtaOutput) throws IllegalStateException {
		if(started) 
			throw new IllegalStateException("Apache Tomcat Server is already running.");
		new ApacheTomcatServer("start", txtaOutput);
		started = true;
	}	
	
	
	
	public void stop(MDS_TextArea txtaOutput) {
		new ApacheTomcatServer("stop", txtaOutput);
	}		
	
	
	
	
	private class ApacheTomcatServer extends Thread {
		
		
		private String param = "";
		private MDS_TextArea txtaOutput = null;
		
		public ApacheTomcatServer(String param) {
			super();
			this.param = param;
			start();
		}
		
	
		public ApacheTomcatServer(String param, MDS_TextArea txtaOutput) {
			super();
			this.param = param;
			this.txtaOutput = txtaOutput;
			start();
		}
				
	
		public void run() {
			try {
	            String jdkPath = MDS.getJdkPath()+"javaw.exe";
	            
	          	MDS_Property propDp = ppm.getProperty(ApacheTomcatServerManager.PROPERTY_NAME);
		        String tomcatPath = propDp.getSupProperty(ApacheTomcatServerManager.PROPERTY_SERVER_PATH);
				String tomcatBinPath = tomcatPath+ "bin\\";	
	            	
	            //String commandLine = " -cp "+ QUOTE + "E:\\Program Files\\Web Servers\\apache-tomcat-6.0.14\\bin\\bootstrap.jar" + QUOTE+" org.apache.catalina.startup.Bootstrap";
	            String ag1 = "-cp "+tomcatBinPath+"bootstrap.jar ";
	            String ag2 = "org.apache.catalina.startup.Bootstrap";
	            //System.out.println(commandLine);
	            String cp = tomcatBinPath+"bootstrap.jar";
	            String mainClass = "org.apache.catalina.startup.Bootstrap";
	            ProcessBuilder pb = new ProcessBuilder(jdkPath,"-cp", cp, mainClass, param);
	            //pb.directory(new File("D:\\Program Files\\Java\\jdk1.6.0_03\\bin"));
	            pb.directory(new File(tomcatPath));	
	             
	            Process prs = pb.start();	
	            BufferedReader is;  // reader for output of process
				String line;
				is = new BufferedReader(new InputStreamReader(prs.getInputStream()));
				
				while ((line = is.readLine()) != null) {
	  				//System.out.println("MAHESH\n");
	  				if(txtaOutput != null) {
		  				txtaOutput.append(line+"\n"); 
		  				Document doc = txtaOutput.getDocument();
	        			txtaOutput.setCaretPosition(doc.getLength());	
	  				} else {
	  					System.out.println(line);
	  				}
				}
	
	            prs.waitFor();	
	            
	            	
	            	
			} catch(Exception ex) {
        		ex.printStackTrace();
        	}  		
		}	
	}
	
	
	
	
	
	
	
	
	
	
		
}
