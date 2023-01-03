/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;



public class E_Mail extends MDS_Frame implements ActionListener {



    JComponent contentPane;
    MDS_TextField txfdFrom = new MDS_TextField();
    MDS_TextField txfdTo = new MDS_TextField();
    MDS_TextField txfdSMTP_Server = new MDS_TextField();
    
    MDS_Panel pnl1 = new MDS_Panel();
    MDS_Panel pnl2 = new MDS_Panel();
    MDS_Panel pnl3 = new MDS_Panel();
    
    MDS_Panel pnl4 = new MDS_Panel();
    
    MDS_TextArea txtaMessage = new MDS_TextArea();
    MDS_TextArea txtaResponse = new MDS_TextArea();
    
    MDS_Panel pnl5 = new MDS_Panel();
    
    MDS_Button btnSend = new MDS_Button("Send");
    MDS_Button btnClose = new MDS_Button("Close");
    
    private BufferedReader in;
    private PrintWriter out;  
    
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();	  
    
    
    
    public E_Mail() {
        super("Mail", true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-email.png"));
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        pnl1.setLayout(new GridLayout(3,1));
        pnl1.add(new MDS_Label("From"));
        pnl1.add(new MDS_Label("To"));
        pnl1.add(new MDS_Label("SMTP Server"));
        pnl3.setLayout(new BorderLayout());
        pnl3.add(pnl1, BorderLayout.WEST);
        
        pnl2.setLayout(new GridLayout(3,1));               
        pnl2.add(txfdFrom);
        pnl2.add(txfdTo);
        txfdSMTP_Server.setEditable(false);
        pnl2.add(txfdSMTP_Server);
        pnl3.add(pnl2, BorderLayout.CENTER);
              
        contentPane.add(pnl3, BorderLayout.NORTH);
        
        pnl4.setLayout(new GridLayout(2, 1));
        pnl4.add(new JScrollPane(txtaMessage));
        txtaResponse.setEditable(false);
        pnl4.add(new JScrollPane(txtaResponse));
        
        contentPane.add(pnl4, BorderLayout.CENTER);
        
        pnl5.setLayout(new FlowLayout(FlowLayout.TRAILING));
        
        btnSend.addActionListener(this);
        pnl5.add(btnSend);
        btnClose.addActionListener(this); 
        pnl5.add(btnClose);
        
        contentPane.add(pnl5, BorderLayout.SOUTH);
        
        MDS_Property propDp = ppm.getProperty(MDS_Network.PROPERTY_NAME);
        txfdSMTP_Server.setText(propDp.getSupProperty(MDS_Network.PROPERTY_SMTP_SEVER));
        
        this.setSize(450,400);
        this.setCenterScreen();
        this.setVisible(true);      
    }
    
    
    
    public void sendMail() {
        try {
            Socket s = new Socket(txfdSMTP_Server.getText(), 25);

            out = new PrintWriter(s.getOutputStream());
            in = new BufferedReader(new
            InputStreamReader(s.getInputStream()));

            String hostName = InetAddress.getLocalHost().getHostName();

            send(null);
            send("HELO " + hostName);
            send("MAIL FROM: " + txfdFrom.getText());
            send("RCPT TO: " + txfdTo.getText());
            send("DATA");
            out.println(txtaMessage.getText());
            send(".");
            s.close();
        } catch (IOException ex) {
            txtaResponse.append("Error: " + ex);
        }
    }
    
    

    public void send(String s) throws IOException { 
     
        if (s != null) {
            txtaResponse.append(s + "\n");
            out.println(s);
            out.flush();
         }
         
         String line;
         if ((line = in.readLine()) != null)
             txtaResponse.append(line + "\n");
    }    
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Send")) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    sendMail();
                }
            });        
        } else if(e.getActionCommand().equals("Close")) {
            this.dispose();    
        }     
    }
    
    
    
    public static void MDS_Main(String arg[]) {  
        new E_Mail();
    }
        
    
    
    
}    