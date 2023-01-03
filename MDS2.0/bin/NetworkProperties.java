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



public class NetworkProperties extends ControlModule implements ActionListener {



    private static NetworkProperties np;
    private static boolean np_Visibility = false;
    
    MDS_TextField txfdMessengerPort = new MDS_TextField("");
    MDS_TextField txfdBitExchangerPort = new MDS_TextField("");
    MDS_TextField txfdSMTP_Sever = new MDS_TextField("");
    
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    
    

    public NetworkProperties() {
        super("Netowrk Properties", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-network.png"));
        MDS_Panel panel = new MDS_Panel(new GridLayout(3, 1));
        MDS_Panel pnl1 = new MDS_Panel(new BorderLayout());
        pnl1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Messenger"));
        pnl1.add(new MDS_Label("Port          "), BorderLayout.WEST);
        txfdMessengerPort.addKeyListener(new KeyAdapter() {
        	public void keyTyped(KeyEvent e) {
        		if(!Character.isDigit(e.getKeyChar()))
        			e.consume();
        	}
        });        
        pnl1.add(txfdMessengerPort, BorderLayout.CENTER);
        panel.add(pnl1);
        
        MDS_Panel pnl2 = new MDS_Panel(new BorderLayout());
        pnl2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Bit Exchanger"));
        pnl2.add(new MDS_Label("Port          "), BorderLayout.WEST);
        txfdBitExchangerPort.addKeyListener(new KeyAdapter() {
        	public void keyTyped(KeyEvent e) {
        		if(!Character.isDigit(e.getKeyChar()))
        			e.consume();
        	}
        });          
        pnl2.add(txfdBitExchangerPort, BorderLayout.CENTER);
        panel.add(pnl2);        
        
        MDS_Panel pnl3 = new MDS_Panel(new BorderLayout());
        pnl3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"E-Mail"));
        pnl3.add(new MDS_Label("SMTP Server   "), BorderLayout.WEST);
        pnl3.add(txfdSMTP_Sever, BorderLayout.CENTER);
        panel.add(pnl3); 
                
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                np_Visibility = false;
                np = null;                      
            }
        });  
        	
        MDS_Property propDp = ppm.getProperty(MDS_Network.PROPERTY_NAME);
        txfdMessengerPort.setText(propDp.getSupProperty(MDS_Network.PROPERTY_MESSENGER_PORT));
        txfdBitExchangerPort.setText(propDp.getSupProperty(MDS_Network.PROPERTY_BIT_EXCHANGER_PORT));       	              
        txfdSMTP_Sever.setText(propDp.getSupProperty(MDS_Network.PROPERTY_SMTP_SEVER));
                
        this.addPanel(panel);
        this.setSize(400, 220);
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
			MDS_Property prop = new MDS_Property(MDS_Network.PROPERTY_NAME);	
	    	prop.setSupProperty(MDS_Network.PROPERTY_MESSENGER_PORT, txfdMessengerPort.getText());	
			prop.setSupProperty(MDS_Network.PROPERTY_BIT_EXCHANGER_PORT, txfdBitExchangerPort.getText());
	    	prop.setSupProperty(MDS_Network.PROPERTY_SMTP_SEVER, txfdSMTP_Sever.getText());
	    	ppm.setProperty(prop);
            this.dispose();
        }
    }  
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!np_Visibility) {
            new NetworkProperties();
        } else {
            try{
                NetworkProperties.np.setIcon(false);
                NetworkProperties.np.setSelected(true);
            } catch(Exception ex) {}
        }
    }       
    
    
    
}        