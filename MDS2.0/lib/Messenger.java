/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Messenger extends Thread {


    
    private boolean alloc_Port = false;
    //private Socket incoming;
    ServerSocket server;
    
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    int port;



    public Messenger() {
    
        if(alloc_Port) {
            //MDS.fireIlligalOperation("Illigal system object creation 'Messenger'.");
            return;            
        }    
    
        try {        
        	MDS_Property propDp = ppm.getProperty(MDS_Network.PROPERTY_NAME);   
        	port = Integer.parseInt(propDp.getSupProperty(MDS_Network.PROPERTY_MESSENGER_PORT));	
            server = new ServerSocket(port); 
            this.start();
            this.setPriority(Thread.MIN_PRIORITY);
            alloc_Port = true;
        } catch(Exception ex) {
             
        }     
    }
    
    
    
    public void launch() {
        new Client();
    }
    
    
  
    public void run() {
        try {    
            for (;;) {  
                Socket incoming = server.accept( );
                new Client_Handler(incoming).start();
            }
        } catch (Exception ex) {
        
        }        
    }
    
    
    
    
    
    private class Client_Handler extends Thread implements ActionListener {
    
    
        
        private Socket incoming;
        BufferedReader in;
        PrintWriter out;
        
        MDS_Frame frame  = new MDS_Frame("Messenger",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-action-kopeteavailable.png"));
        JComponent contentPane;
        DefaultListModel dflstmdl = new DefaultListModel();
        JList lstOut = new JList(dflstmdl);
        MDS_TextField txfdIn = new MDS_TextField();
        MDS_ToolBar toolBar = new MDS_ToolBar();
        
        MDS_Button btnDisconect = new MDS_Button("Disconnect");
        MDS_Button btnSend = new MDS_Button("Send");
        
        boolean done = false;
        
        
    
         public Client_Handler(Socket i) {
             try {
                 incoming = i;
                 in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
                 out = new PrintWriter(incoming.getOutputStream(), true /* autoFlush */);         
                 InetAddress id = incoming.getLocalAddress();
                 frame.setTitle("Messenger "+id.getHostAddress());
                 
                 frame.setSize(500, 400);
                 frame.setLocation(100, 70);
                 //frame.setCenterScreen();
                 frame.setFrameIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"Messenger.png")); 
                 contentPane = (JComponent) frame.getContentPane();
                 contentPane.setLayout(new BorderLayout());
                 contentPane.add(new JScrollPane(lstOut), BorderLayout.CENTER);
                 MDS_Panel pnlSubContainer = new MDS_Panel();
                 pnlSubContainer.setLayout(new BorderLayout());
		         txfdIn.addKeyListener(new KeyAdapter() {
		        	 public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == e.VK_ENTER) {
							if(btnSend.isEnabled()) {
				                 if(!txfdIn.getText().equals("")) {
				                     out.println(txfdIn.getText());
				                     txfdIn.setText("");
				                 }								
							}
						}			
		        	 }
		         });      
                 pnlSubContainer.add(txfdIn, BorderLayout.NORTH);               
                 toolBar.addSeparator();
                 btnDisconect.addActionListener(this);
                 btnDisconect.setActionCommand("Disconnect");
                 toolBar.add(btnDisconect);
                 toolBar.addSeparator();
                 btnSend.addActionListener(this);
                 btnSend.setActionCommand("Send");
                 toolBar.add(btnSend);
                 toolBar.setFloatable(false);
                 pnlSubContainer.add(toolBar, BorderLayout.CENTER);
                 contentPane.add(pnlSubContainer, BorderLayout.SOUTH);
                 
                 frame.setVisible(true);
                 InetAddress id2 = InetAddress.getByName("localhost");
                 out.println("Connection Established with "+id2.getHostAddress());
             } catch (Exception ex) {
             
             }    
         }    
         
         
         
         public void run() {
             try {
                 this.sleep(100);
                 while (!done || !incoming.isClosed()) {
                     String line = in.readLine();
                     if(!line.equals("")) {
                         dflstmdl.addElement(line);
                     }    
                 }
                 //System.out.println("XXXXXXXXc");
                 incoming.close();
                 //System.out.println("XXXXXXXXc");
                 
             } catch (InterruptedException e) {
                 
             } catch(Exception ex) {
             
             }
         }
         
         
         
         public void actionPerformed(ActionEvent e) {
             if(e.getActionCommand().equals("Send")) {
                 if(!txfdIn.getText().equals("")) {
                     out.println(txfdIn.getText());
                     txfdIn.setText("");
                 }
             } else if(e.getActionCommand().equals("Disconnect")) {
                 try {    
                     incoming.close();
                     txfdIn.setEnabled(false);
                     btnSend.setEnabled(false);
                     btnDisconect.setEnabled(false);      
                 
                 } catch(Exception ex) {
                   
                 }
             }
         }
         
    } 
    
    
    
    
    
    private class Client extends MDS_Frame implements ActionListener, Runnable {
    
    
    
        BufferedReader in;
        PrintWriter out;
        
        Socket socket;
        
        JComponent contentPane;
        DefaultListModel dflstmdl = new DefaultListModel();
        JList lstOut = new JList(dflstmdl);
        MDS_TextField txfdIn = new MDS_TextField();
        JToolBar toolBar = new JToolBar();
        
        MDS_Button btnConnect = new MDS_Button("Connect");
        MDS_Button btnSend = new MDS_Button("Send"); 
        
        MDS_TextField txfdIP = new MDS_TextField("127.0.0.1");   
        MDS_TextField txfdPort = new MDS_TextField(String.valueOf(port));
        
        MDS_Button btnOk = new MDS_Button("Ok");
        MDS_Button btnCancel = new MDS_Button("Cancel");
        
        MDS_Dialog dlgIP;
        
        Thread thread;
        
    
    
        public Client() {
            super("Messenger",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-action-kopeteavailable.png"));
            this.setSize(500, 400);
            this.setCenterScreen();
            this.setFrameIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"Messenger.png")); 
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(new JScrollPane(lstOut), BorderLayout.CENTER);
            MDS_Panel pnlSubContainer = new MDS_Panel();
            pnlSubContainer.setLayout(new BorderLayout());
	         txfdIn.addKeyListener(new KeyAdapter() {
	        	 public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == e.VK_ENTER) {
						if(btnSend.isEnabled()) {
			                 if(!txfdIn.getText().equals("")) {
			                     out.println(txfdIn.getText());
			                     txfdIn.setText("");
			                 }								
						}
					}			
	        	 }
	         });             
            pnlSubContainer.add(txfdIn, BorderLayout.NORTH);               
            toolBar.addSeparator();
            btnConnect.addActionListener(this);
            btnConnect.setActionCommand("Connect");
            toolBar.add(btnConnect);
            toolBar.addSeparator();
            btnSend.addActionListener(this);
            btnSend.setActionCommand("Send");
            btnSend.setEnabled(false);
            toolBar.add(btnSend);
            toolBar.setFloatable(false);
            pnlSubContainer.add(toolBar, BorderLayout.CENTER);
            contentPane.add(pnlSubContainer, BorderLayout.SOUTH);
                 
            this.setVisible(true);            
        }
        
        
        
         public void actionPerformed(ActionEvent e) {
             try {    
                 if(e.getActionCommand().equals("Send")) {
                     if(!txfdIn.getText().equals("")) {
                         out.println(txfdIn.getText());
                         txfdIn.setText("");
                     }
                 } else if(e.getActionCommand().equals("Connect")) {  
                     String ip = ""; 
                     MDS_Panel panel = new MDS_Panel();
                     panel.setLayout(null);
                     MDS_Label lblIP = new MDS_Label("IP");
                     lblIP.setBounds(20, 10, 30,25);
                     panel.add(lblIP);               
                     txfdIP.setBounds(50, 10, 200, 25);
                     panel.add(txfdIP); 
                     
                     MDS_Label lblPort = new MDS_Label("Port");
                     lblPort.setBounds(20, 40, 30,25);
                     panel.add(lblPort);               
                     txfdPort.setBounds(50, 40, 200, 25);
                     txfdPort.setEditable(false);
                     panel.add(txfdPort); 
                     
                     btnOk.setBounds(70, 80, 50, 28);
                     btnOk.addActionListener(this);
                     panel.add(btnOk); 

                     btnCancel.setBounds(140, 80, 78, 28);
                     btnCancel.addActionListener(this);
                     panel.add(btnCancel);
                                                                    
                     //frmIP = MDS_OptionPane.getEmptyMessageFrame(this, panel,"Rc", new Dimension(280, 160));
                     
                     dlgIP = new MDS_Dialog(this, "Messenger");
                     dlgIP.setSize(280, 160);
                     dlgIP.setCenterScreen();
                     dlgIP.getContentPane().setLayout(new BorderLayout());
                     dlgIP.getContentPane().add(panel, BorderLayout.CENTER);
                     dlgIP.setVisible(true);                      
                                                
                 } else if(e.getActionCommand().equals("Disconnect")) {  
                     socket.close(); 
                     btnConnect.setText("Connect");
                     btnConnect.setActionCommand("Connect");
                     btnSend.setEnabled(false);
                 } else if(e.getActionCommand().equals("Ok")) { 
                     try {
                         socket = new Socket(txfdIP.getText(), Integer.parseInt(txfdPort.getText()));
                         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         out = new PrintWriter(socket.getOutputStream(), true);                          
                         thread = new Thread(this);
                         thread.setPriority(Thread.MIN_PRIORITY);
                         thread.start();
                         dlgIP.dispose();
                         btnConnect.setText("Disconnect");
                         btnConnect.setActionCommand("Disconnect");
                     } catch (Exception ex) {
                         //System.out.println(ex.toString());
                     }    
                 } else if(e.getActionCommand().equals("Cancel")) { 
                     dlgIP.dispose();
                     btnSend.setEnabled(false);
                 }
                     
             } catch(Exception ex) {
                   
             }    

         }
         
         
         
         public void run() {
             String line;
             try {
                 while((line = in.readLine())!= null) {
                     dflstmdl.addElement(line);
                 }    
             } catch (Exception ex) {
             
             }
         }        
    
    }  
    
    
    
}    