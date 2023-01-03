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
import java.util.*;



public class BitExchanger extends Thread {


    
    private boolean alloc_Port = false;
    public final String DOWNLOAD_ACCEPTED = "DownLoad Accepted"; 
    //private Socket incoming;
    ServerSocket server;
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    int port;


    public BitExchanger() {
    
        if(alloc_Port) {
            //MDS.fireIlligalOperation("Illigal system object creation 'BitExchanger'.");
            return;            
        }
    
        try {        
        	MDS_Property propDp = ppm.getProperty(MDS_Network.PROPERTY_NAME);   
        	port = Integer.parseInt(propDp.getSupProperty(MDS_Network.PROPERTY_BIT_EXCHANGER_PORT));        	   
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
    
    
    
    public void launch(File f) {
        new Client(f);
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
    
    
        
        final int START_DOWNLOAD = 43;
        final int REGECT_DOWNLOAD = 65;
        final int WAIT_DOWNLOAD = 87;  
        
        private Socket incoming;
        DataInputStream in;
        PrintWriter out;
        MDS_Frame frame  = new MDS_Frame("Bit Exchanger",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-randr.png"));
        JComponent contentPane;
        DefaultListModel dflstmdl = new DefaultListModel();
        JList lstOut = new JList(dflstmdl);
        MDS_TextField txfdIn = new MDS_TextField();
        MDS_TextArea txtaMsg = new MDS_TextArea();
        MDS_ToolBar toolBar = new MDS_ToolBar();
        
        MDS_Label lblFile = new MDS_Label("File");
        MDS_Label lblLen = new MDS_Label("Length");
        MDS_ProgressBar pgbr = new MDS_ProgressBar();
        
        MDS_Button btnDisconect = new MDS_Button("Disconnect");
        MDS_Button btnSend = new MDS_Button("Send");
        
        MDS_Dialog dlgConfirm;
        
        boolean done = false;
        boolean wait = true;
        int downLoadStatus = WAIT_DOWNLOAD; 
        
        File downLoadFile = null;
        
        
    
         public Client_Handler(Socket i) {
             try {
                 incoming = i;
                 in = new DataInputStream(incoming.getInputStream());
                 out = new PrintWriter(incoming.getOutputStream(), true /* autoFlush */);         
                 InetAddress id = incoming.getLocalAddress();
                 frame.setTitle("Bit Exchanger [Client] "+id.getHostAddress());
                 this.setName("Bit Exchanger [Client] "+id.getHostAddress());
                 frame.setSize(500, 400);
                 //frame.setCenterScreen();
                 frame.setLocation(200, 150);
                 frame.setFrameIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"BitExchanger.png")); 
                 contentPane = (JComponent) frame.getContentPane();
                 contentPane.setLayout(new BorderLayout());
                 contentPane.add(new JScrollPane(txtaMsg), BorderLayout.CENTER);
                 MDS_Panel pnlSubContainer = new MDS_Panel();
                 pnlSubContainer.setLayout(new BorderLayout());
                 MDS_Panel pnlIncoming = new MDS_Panel();
                 pnlIncoming.setLayout(new GridLayout(3,0));
                 pnlIncoming.add(lblFile);
                 pnlIncoming.add(lblLen);
                 pnlIncoming.add(pgbr);
                 pnlSubContainer.add(pnlIncoming, BorderLayout.NORTH);               
                 toolBar.addSeparator();
                 btnDisconect.addActionListener(this);
                 btnDisconect.setActionCommand("Disconnect");
                 toolBar.add(btnDisconect);
                 toolBar.addSeparator();
                 toolBar.setFloatable(false);
                 //btnSend.addActionListener(this);
                 //btnSend.setActionCommand("Send");
                 //toolBar.add(btnSend);
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
               
                 this.sleep(10);
                 
                 int seg = 1;
                 String fName = "";
                 long fLen = 0;
                 long lastModified = 0;
                 String msg = "";
                 String s = "";
                 int cfLen = 0;
                 int bfSize = 1024;
                 int off = 0;
                 boolean lock = false;
                 Character ch = null;
                 byte b = 0;
                 
                 Random rand = new Random();
                 String tName = String.valueOf(Math.abs(rand.nextLong()))+".temp";
                 File downFile = new File(MDS.getFileManager().getMDS_Download_Dir() ,tName);
                 /*
                 FileManager.DirectoryChooser fmdc = MDS.getFileManager().getDirectoryChooser();
                 if(fmdc.showDiaog(frame) ==  fmdc.APPROVE_OPTION) {
                     Random rand = new Random();
                     String tName = String.valueOf(Math.abs(rand.nextLong()))+".temp";
                     downFile = new File(fmdc.getPath()+tName);
                 } 
                 */                    
                 DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(downFile));
                 
                 
                 while (!done) {
                     
                     if(!lock) {
                         b = in.readByte();
                         ch = new Character((char)b);                     
                     }
                     
                     if(seg == 1) {
                         if((ch.toString()).equals(">")) {
                             seg = 2;    
                             lblFile.setText("File : "+fName);       
                         } else {
                             fName = fName.concat(ch.toString());
                         }  
                     } else if(seg == 2) {
                         if((ch.toString()).equals(">")) {
                             String slen = String.valueOf(fLen/1024);
                             Integer i = new Integer(slen);
                             int max = i.intValue();                          
                             pgbr.setMaximum(max);
                             pgbr.setMinimum(0);
                             lblLen.setText("Length : "+MDS.getFileManager().getFormatedFileSize(fLen));
                             seg = 3; 
                         } else {
                             Double d = new Double(String.valueOf(fLen)+ch.toString());
                             fLen = d.longValue();
                         }  
                     } else if(seg == 3) {
                         if((ch.toString()).equals(">")) {
                             seg = 4;          
                         } else {
                             Double d = new Double(String.valueOf(lastModified)+ch.toString());
                             lastModified = d.longValue();
                         }  
                     } else if(seg == 4) {
                         if((ch.toString()).equals(">")) {
                             seg = 5;  
                             
                             txtaMsg.setText(msg);
                             
                             MDS_Panel panel = new MDS_Panel();
                             panel.setLayout(null);
                             
                             MDS_Label lblFile = new MDS_Label("File Name : "+fName);
                             lblFile.setBounds(10,10,350,25);
                             panel.add(lblFile);

                             MDS_Label lblLen = new MDS_Label("Size : "+MDS.getFileManager().getFormatedFileSize(fLen));
                             lblLen.setBounds(10,30,350,25);
                             panel.add(lblLen);
 
                             MDS_Label lblLastModified = new MDS_Label("Last Modified : "+MDS.getFileManager().getLastModified_As_String(lastModified));
                             lblLastModified.setBounds(10,50,350,25);
                             panel.add(lblLastModified);
                             
                             MDS_Label lblScrIP = new MDS_Label("Source IP : ");
                             lblScrIP.setBounds(10,70,350,25);
                             panel.add(lblScrIP);                             

                             MDS_Label lblMessage = new MDS_Label("Do you want to download this file? : ");
                             lblMessage.setBounds(10,100,350,25);
                             //lblMessage.setOpaque(true);
                             //lblMessage.setBackground(Color.red);                             
                             panel.add(lblMessage);                             
                                                         
                             MDS_Button btnYes = new MDS_Button("Yes");
                             btnYes.setBounds(190, 150, 70, 28);
                             btnYes.addActionListener(this);
                             panel.add(btnYes);                               
                             
                             MDS_Button btnNo = new MDS_Button("No");
                             btnNo.setBounds(280, 150, 70, 28);
                             btnNo.addActionListener(this);
                             panel.add(btnNo);                              
                             //dlgConfirm = MDS_OptionPane.getEmptyMessageFrame(frame, panel,"Rc", new Dimension(380, 240));  
                             dlgConfirm = new MDS_Dialog(frame, "File Download");
                             dlgConfirm.setSize(380, 240);
                             dlgConfirm.setCenterScreen();
                             dlgConfirm.getContentPane().setLayout(new BorderLayout());
                             dlgConfirm.getContentPane().add(panel, BorderLayout.CENTER);
                             dlgConfirm.setVisible(true);
                             
                             while(wait) {
                                 if(downLoadStatus == START_DOWNLOAD) {
                                     out.println(DOWNLOAD_ACCEPTED);
                                     dlgConfirm.dispose();
                                     wait = false;                                          
                                 }
                             }
                                                         
                             lock = true;  
                               
                         } else {
                             msg = msg.concat(ch.toString());
                         }  
                     } else if(seg == 5) {  
                         
                         if((fLen - cfLen) <= bfSize) {
                             String slen = String.valueOf(fLen - cfLen); 
                             Integer i = new Integer(slen); 
                             bfSize = i.intValue();
                             done = true;     
                         } else {            
                             cfLen = cfLen+bfSize;
                         }
                    
                         byte ba[] = new byte[bfSize];
                         in.read(ba,off,bfSize);
                         fileOut.write(ba,off,bfSize);
                         
                         String slen1 = String.valueOf(cfLen/1024); 
                         Integer i2 = new Integer(slen1); 
                         int prgVal = i2.intValue();
                        
                         pgbr.setValue(prgVal);                          
                                                                                                                  
                     }                     
                      
                 }
                 
                 incoming.close();
                 fileOut.close();
                 //File f = new File("Net\\Received\\"+fName);
                 //Console.println(f.getPath());
                 File fTemp = new File(MDS.getFileManager().getMDS_Download_Dir(), fName);
                 if(fTemp.exists()) {
                     fTemp.delete();       
                 }
                 
                 File dfName = new File(MDS.getFileManager().getMDS_Download_Dir() ,fName);
                 
                 downFile.renameTo(dfName);
                 
////                 MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DOWNLOAD_FILE, dfName));
                 
                 downLoadFile = dfName;
                 
                 btnDisconect.setEnabled(false);
                 
                 //MDS_OptionPane.showMessageDialog(frame, "File download complete\n\n"+System.getProperty("user.home")+"\\"+fName, "BitExchanger", JOptionPane.INFORMATION_MESSAGE);
                 
                 txtaMsg.append("\n**********************************************************************\n");
                 txtaMsg.append("File download complete,\n\n File downloaded to :\n"+dfName.getPath());
                 txtaMsg.append("\n**********************************************************************");  
                 
                 toolBar.addSeparator();
                 MDS_Button btnOpen = new MDS_Button("Open");
                 btnOpen.addActionListener(this);
                 toolBar.add(btnOpen);
                 toolBar.addSeparator();
                 MDS_Button btnBrowse = new MDS_Button("Browse");
                 btnBrowse.addActionListener(this);
                 toolBar.add(btnBrowse);                 
                 
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
                     in.close();
                     out.close();
                     //txfdIn.setEnabled(false);
                     //btnSend.setEnabled(false);
                     btnDisconect.setEnabled(false);      
                 
                 } catch(Exception ex) {
                   
                 }
             } else if(e.getActionCommand().equals("Yes")) {
                 downLoadStatus = START_DOWNLOAD;
                 
             } else if(e.getActionCommand().equals("No")) {
                 try {
                     incoming.close();
                     dlgConfirm.dispose();
                     frame.dispose();
                 } catch(Exception ex) {}
                 
             } else if(e.getActionCommand().equals("Browse")) {
                 String[] path ={downLoadFile.getPath()};
                 MDS.getProcessManager().execute(new File(MDS.getBinaryPath(), "FileBrowser"));
             } else if(e.getActionCommand().equals("Open")) { 
                 MDS.getFileManager().executeFile(downLoadFile);
             }
         }      
         
    } 
    
    
    
    
    
    private class Client extends MDS_Frame implements ActionListener, Runnable {
    
    
    
        BufferedReader in;
        DataInputStream in_File;
        DataOutputStream out;
        
        Socket socket;
        
        JComponent contentPane;
        DefaultListModel dflstmdl = new DefaultListModel();
        JList lstOut = new JList(dflstmdl);
        MDS_TextField txfdIn = new MDS_TextField();
        MDS_Label lblIP = new MDS_Label(" IP : ");
        MDS_Label lblPort = new MDS_Label(" Port : "+port);
        MDS_TextArea txtaMsg = new MDS_TextArea();
        MDS_Label lblPath = new MDS_Label("Path  ");
        MDS_TextField txfdPath = new MDS_TextField();
        MDS_Button btnBrowse = new MDS_Button("Browse");
        JToolBar toolBar = new JToolBar();
        
        MDS_Button btnConnect = new MDS_Button("Connect");
        MDS_Button btnSend = new MDS_Button("Send"); 
        
        MDS_TextField txfdIP = new MDS_TextField("127.0.0.1");   
        MDS_TextField txfdPort = new MDS_TextField(String.valueOf(port));
        
        MDS_Button btnOk = new MDS_Button("Ok");
        MDS_Button btnCancel = new MDS_Button("Cancel");
        
        MDS_Dialog dlgIP;
        MDS_Dialog dlgUpLoad;
        MDS_Dialog dlgWait;
        Thread thread;
        
        File file;
        String ip;
        int Port; 
        boolean continueUpload = true;
        boolean accepted = false;
        
        
    
    
        public Client() {
            super("Bit Exchanger [Server]",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-randr.png"));
            this.setSize(500, 400);
            this.setCenterScreen();
            this.setFrameIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"BitExchanger.png")); 
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            MDS_Panel pnlSubContainer = new MDS_Panel();
            pnlSubContainer.setLayout(new BorderLayout());
            MDS_Panel pnlMsg = new MDS_Panel();
            pnlMsg.setLayout(new BorderLayout());
            pnlMsg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Message"));
            MDS_Panel pnlServerInfo = new MDS_Panel();
            pnlServerInfo.setLayout(new BorderLayout());
            pnlServerInfo.add(lblIP, BorderLayout.NORTH);
            pnlServerInfo.add(lblPort, BorderLayout.SOUTH);
            pnlSubContainer.add(pnlServerInfo, BorderLayout.NORTH);
            pnlMsg.add(new JScrollPane(txtaMsg), BorderLayout.CENTER);
            pnlSubContainer.add(pnlMsg, BorderLayout.CENTER);
            
            MDS_Panel pnlFile = new MDS_Panel();
            pnlFile.setLayout(new BorderLayout());
            pnlFile.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"File"));            
            pnlFile.add(lblPath, BorderLayout.WEST);
            pnlFile.add(txfdPath, BorderLayout.CENTER);
            btnBrowse.addActionListener(this);
            pnlFile.add(btnBrowse, BorderLayout.EAST);
            pnlSubContainer.add(pnlFile, BorderLayout.SOUTH);

            contentPane.add(pnlSubContainer, BorderLayout.CENTER);
            MDS_Panel pnlSubContainer1 = new MDS_Panel();
            pnlSubContainer1.setLayout(new BorderLayout());
            //pnlSubContainer1.add(txfdIn, BorderLayout.NORTH);  
                         
            toolBar.setFloatable(false);
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
            pnlSubContainer1.add(toolBar, BorderLayout.CENTER);
            contentPane.add(pnlSubContainer1, BorderLayout.SOUTH);
                 
            this.setVisible(true);            
        }
        
        
        
        public Client(File f) {
            this();
            txfdPath.setText(f.getPath());
        }
        
        
        
         public void actionPerformed(ActionEvent e) {
             try {    
                 if(e.getActionCommand().equals("Send")) {
                     if(!txfdPath.getText().equals("")) {
                         int i = txtaMsg.getText().indexOf(">");
                         if(i != -1) {
                             MDS_OptionPane.showMessageDialog(this, "A message cannot contains '<' charactor.", "BitExchanger", JOptionPane.INFORMATION_MESSAGE);  
                             txtaMsg.requestFocus();
                             txtaMsg.select(i, i+1);                             
                             return;  
                         }
                         try {
                             socket = new Socket(ip, Port);                             
                             in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                             out = new DataOutputStream(socket.getOutputStream());                          
                             file = new File(txfdPath.getText());
                             thread = new Thread(this);
                             thread.setPriority(Thread.MIN_PRIORITY);
                             thread.start();
                             btnSend.setEnabled(false);
                             btnConnect.setText("Disconnect");
                             btnConnect.setActionCommand("Disconnect");
                         } catch (Exception ex) {
                             //System.out.println("2"+ex.toString());
                         }                      
                     }
                 } else if(e.getActionCommand().equals("Connect")) {
                     File f = new File(txfdPath.getText());
                     if(!f.exists()) {
                         boolean b = btnSend.isEnabled();
                         MDS_OptionPane.showMessageDialog(this, "Upload file not found.\n\n"+"'"+f.getPath()+"'", "BitExchanger", JOptionPane.ERROR_MESSAGE);
                         if(!b) {
                             btnSend.setEnabled(false);
                         }
                         txfdPath.requestFocus();
                         txfdPath.selectAll();
                         return;
                     }  
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
                         
                     dlgIP = new MDS_Dialog(this, "BitExchanger");
                     dlgIP.setSize(280, 160);
                     dlgIP.setCenterScreen();
                     dlgIP.getContentPane().setLayout(new BorderLayout());
                     dlgIP.getContentPane().add(panel, BorderLayout.CENTER);
                     dlgIP.setVisible(true);                                                
                 
                 } else if(e.getActionCommand().equals("Disconnect")) {  
                     socket.close(); 
                     in.close();
                     out.close();
                     btnConnect.setText("Connect");
                     btnConnect.setActionCommand("Connect");
                 } else if(e.getActionCommand().equals("Ok")) { 
                     lblIP.setText(" IP : "+txfdIP.getText());
                     lblPort.setText(" Port : "+txfdPort.getText());
                     ip = txfdIP.getText();
                     Port = Integer.parseInt(txfdPort.getText());
                     dlgIP.dispose();   
                 } else if(e.getActionCommand().equals("Cancel")) { 
                     dlgIP.dispose();
                 } else if(e.getActionCommand().equals("Browse")) {
                     MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
                     if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {
                         txfdPath.setText(fmfc.getPath());
                     } 
                     btnSend.setEnabled(false);                     
                 } else if(e.getActionCommand().equals("Cancel Upload")) {
                     continueUpload = false;
                     socket.close(); 
                 } else if(e.getActionCommand().equals("!wait")) {
                     accepted = true;
                     dlgWait.dispose();
                     continueUpload = false;
                     socket.close();   
                     this.dispose();
                 }
                     
             } catch(Exception ex) {
                   
             }    

         }
         
         
         
        public void run() {
            try {
             
                thread.setName("Bit Exchanger [Server]");
             
                in_File = new DataInputStream(new FileInputStream(file));
                 
                out.writeBytes(file.getName());
                out.writeBytes(">");
                out.writeBytes(String.valueOf(file.length()));
                //System.out.println("fLen  1 "+file.length());
                out.writeBytes(">");
                out.writeBytes(String.valueOf(file.lastModified()));
                out.writeBytes(">");
                out.writeBytes(txtaMsg.getText()); 
                out.writeBytes(">");
                out.flush();
                
                MDS_Panel panel = new MDS_Panel();
                panel.setLayout(null);
                MDS_Label lblMessage = new MDS_Label("Waiting for the confermation to upload the file.");
                lblMessage.setBounds(10, 15, 280, 20);                
                panel.add(lblMessage);
                MDS_Button btnCancelWait = new MDS_Button("Cancel");
                btnCancelWait.setActionCommand("!wait");
                btnCancelWait.addActionListener(this);
                btnCancelWait.setBounds(185, 60, 88, 28);
                panel.add(btnCancelWait); 
                //frmWait = MDS_OptionPane.getEmptyMessageFrame(this, panel,"Waiting..", new Dimension(280, 160)); 
                               
                dlgWait = new MDS_Dialog(this, "File Upload");
                dlgWait.setSize(290, 130);
                dlgWait.setCenterScreen();
                dlgWait.getContentPane().setLayout(new BorderLayout());
                dlgWait.getContentPane().add(panel, BorderLayout.CENTER);
                dlgWait.setVisible(true);                                  
                               
                String reply = in.readLine();
                
                while(!accepted) {
                    if(reply.equals(DOWNLOAD_ACCEPTED)) {
                        accepted = true;
                        dlgWait.dispose();
                    }
                    if(!accepted) {
                        reply = in.readLine();
                    }

                }
                 
                panel = new MDS_Panel();
                panel.setLayout(null);
                MDS_Label lblIP1 = new MDS_Label("IP : "+txfdIP.getText());
                MDS_Label lblPort1 = new MDS_Label("Port : "+txfdPort.getText());
                MDS_Label lblFile = new MDS_Label("File : "+file.getName());
                //MDS_Label lblStatus = new MDS_Label("5MB of 8MB Tansfered");
                MDS_Label lblStatus = new MDS_Label("0 of "+MDS.getFileManager().getFormatedFileSize(file.length())+" Tansfered");
                String slen = String.valueOf(file.length()/1024);
                Integer i = new Integer(slen);
                int max = i.intValue();                
                MDS_ProgressBar pgbr = new MDS_ProgressBar(0, max);
                MDS_Button btnCancel = new MDS_Button("Cancel");
                btnCancel.addActionListener(this);
                btnCancel.setActionCommand("Cancel Upload");
                lblIP1.setBounds(10, 10, 250, 25);
                panel.add(lblIP1);
                lblPort1.setBounds(10, 30, 250, 25);
                panel.add(lblPort1); 
                lblFile.setBounds(10, 50, 250, 25);
                panel.add(lblFile);
                lblStatus.setBounds(10, 70, 250, 25);
                panel.add(lblStatus);                
                pgbr.setBounds(10, 100, 250, 20);
                panel.add(pgbr);
                btnCancel.setBounds(180, 130, 78, 28);
                panel.add(btnCancel);
                                                                                                              
                //frmUpLoad = MDS_OptionPane.getEmptyMessageFrame(this, panel,"Rc", new Dimension(280, 200));               
      
                dlgUpLoad = new MDS_Dialog(this, "File upload in progress");
                dlgUpLoad.setSize(280, 200);
                dlgUpLoad.setCenterScreen();
                dlgUpLoad.getContentPane().setLayout(new BorderLayout());
                dlgUpLoad.getContentPane().add(panel, BorderLayout.CENTER);
                dlgUpLoad.setVisible(true);      
                 
                int off = 0;
                int len = 1024;
                 
                long fLen = file.length();
        
                long cfLen = 0;
                

                
                while(in_File.available() != 0 && continueUpload) {
                    if((fLen - cfLen) <= len) {
                        String slen2 = String.valueOf(fLen - cfLen); 
                        Integer i2 = new Integer(slen2); 
                        len = i2.intValue();        
                    } else {            
                        cfLen = cfLen+len;
                    }
                     
                    byte b[] = new byte[len];
                    in_File.read(b,off,len);
                    out.write(b,off,len);
                    
                  
                    String slen3 = String.valueOf(cfLen/1024); 
                    Integer i3 = new Integer(slen3); 
                    int prgVal = i3.intValue();
                        
                    pgbr.setValue(prgVal); 
                    
                    lblStatus.setText(MDS.getFileManager().getFormatedFileSize(cfLen)+" of "+MDS.getFileManager().getFormatedFileSize(fLen)+" Complete.");                    
                }
                
                in_File.close();
                
                //out.close();
                out.flush();
                socket.close();
                
                dlgUpLoad.dispose();
                txtaMsg.setText("");
                txfdPath.setText("");
                btnSend.setEnabled(false);
                btnConnect.setText("Connect");
                btnConnect.setActionCommand("Connect");                
                            
            } catch (Exception ex) {
             
            } 
 

        }        
    
    } 
    
    
    
}    
 