/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;



public class ApacheTomcatConfig extends ControlModule implements ActionListener {



    private static ApacheTomcatConfig np;
    private static boolean np_Visibility = false;
    
    MDS_TextField txfdTomcatPath = new MDS_TextField("");
    MDS_Button btnBrowse = new MDS_Button("Browse");
    MDS_TextField txfdTomcatPort = new MDS_TextField("");
    
     MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    
    

    public ApacheTomcatConfig() {
        super("Configure Tomcat", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-gf.png"));
        MDS_Panel panel = new MDS_Panel(new GridLayout(2, 1));
        MDS_Panel pnl1 = new MDS_Panel(new BorderLayout());
        pnl1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Tomcat Server Path"));
        pnl1.add(new MDS_Label("Path          "), BorderLayout.WEST);
        txfdTomcatPath.setEditable(false);
        pnl1.add(txfdTomcatPath, BorderLayout.CENTER);
        btnBrowse.addActionListener(this);
        pnl1.add(btnBrowse, BorderLayout.EAST);
        panel.add(pnl1);
        
        MDS_Panel pnl2 = new MDS_Panel(new BorderLayout());
        pnl2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Tomcat Port"));
        pnl2.add(new MDS_Label("Port          "), BorderLayout.WEST);
        txfdTomcatPort.addKeyListener(new KeyAdapter() {
        	public void keyTyped(KeyEvent e) {
        		if(!Character.isDigit(e.getKeyChar()))
        			e.consume();
        	}
        });
        pnl2.add(txfdTomcatPort, BorderLayout.CENTER);
        panel.add(pnl2);        
        
                
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                np_Visibility = false;
                np = null;                      
            }
        });                
        
        try {        
	        MDS_Property propDp = ppm.getProperty(ApacheTomcatServerManager.PROPERTY_NAME);
	        txfdTomcatPath.setText(propDp.getSupProperty(ApacheTomcatServerManager.PROPERTY_SERVER_PATH));
	        txfdTomcatPort.setText(propDp.getSupProperty(ApacheTomcatServerManager.PROPERTY_SERVER_PORT));                
        } catch(Exception ex) {
        	
        }        
                
        this.addPanel(panel);
        this.setSize( 500, 190);
        this.setCenterScreen();
        
        //Console.println(String.valueOf(this.getX())+"  "+String.valueOf(this.getY())+"  "+String.valueOf(this.getWidth())+"  "+String.valueOf(this.getHeight()));
        
        this.setVisible(true);     
        
        np_Visibility = true;
        np = this;          
            
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Ok")) {
        	if(txfdTomcatPath.getText().equals("")) {
        		MDS_OptionPane.showMessageDialog(this, "Tomcat Server path cannot be empty.", "Configure Tomcat", JOptionPane.ERROR_MESSAGE);
        	} else if(txfdTomcatPort.getText().equals("")) {
         		MDS_OptionPane.showMessageDialog(this, "Tomcat Server port number cannot be empty.", "Configure Tomcat", JOptionPane.ERROR_MESSAGE);       
        	} else {
		    	MDS_Property prop = new MDS_Property(ApacheTomcatServerManager.PROPERTY_NAME);	
		    	prop.setSupProperty(ApacheTomcatServerManager.PROPERTY_SERVER_PATH, txfdTomcatPath.getText());	
				prop.setSupProperty(ApacheTomcatServerManager.PROPERTY_SERVER_PORT, txfdTomcatPort.getText());
		    	ppm.setProperty(prop);
		    	this.dispose();        		
        	}    
        } else if(e.getActionCommand().equals("Browse")) {
            MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
            if(fmdc.showDiaog(this) ==  fmdc.APPROVE_OPTION) {
                txfdTomcatPath.setText(fmdc.getPath()); 
            }         	
        }
    }  
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!np_Visibility) {
            new ApacheTomcatConfig();
        } else {
            try{
                ApacheTomcatConfig.np.setIcon(false);
                ApacheTomcatConfig.np.setSelected(true);
            } catch(Exception ex) {}
        }
    }       
    
    
    
}        