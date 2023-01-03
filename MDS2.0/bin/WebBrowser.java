


import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.io.*;



public class WebBrowser extends MDS_Frame implements ActionListener, HyperlinkListener {

    
    
    MDS_User usr = MDS.getUser();
    JMenuBar mnb = new JMenuBar();
    
    JMenu mnuFile = new JMenu("File");
    JMenuItem mniOpenFile = usr.createMenuItem("Open File", this, KeyEvent.VK_F);
    JMenuItem mniOpenURL = usr.createMenuItem("Open URL", this, KeyEvent.VK_U);    
    JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);
    
    JMenu mnuHelp = new JMenu("Help");
    JMenuItem mniAbout = usr.createMenuItem("About..", this, KeyEvent.VK_A);    
        
    JComponent contentPane;   
    
    MDS_Panel pnl1 = new MDS_Panel();
    
    MDS_ToolBar toolBar = new MDS_ToolBar();
    
    MDS_Button btnOpenURL = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-filesys-network.png"));
    MDS_Button btnOpenFile = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-fileopen.png"));
    MDS_Button btnBack = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-previous.png"));
    MDS_Button btnFoward = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-next.png"));
    MDS_Button btnRefresh = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "22-action-reload.png"));
    
    MDS_Panel pnl2 = new MDS_Panel();
    MDS_TextField txfdAddress = new MDS_TextField();
    MDS_Button btnLoad = new MDS_Button("Load");
    
    MDS_EditorPane editorPane = new MDS_EditorPane();
    
    private Stack urlStack = new Stack();
    
    Thread thrdLoading;
    
    java.util.List history = new ArrayList();
	int currentHistoryPage = -1;
	public static final int  MAX_HISTORY = 50;
	
	MDS_Label lblStatus = new MDS_Label(" ");
	MDS_Panel pnlStatus = new MDS_Panel(new BorderLayout());
	MDS_ProgressBar prgbLoading = new MDS_ProgressBar();
    
    
    
    public WebBrowser() {
        super("Web Browser",true, true, true, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-package_network.png"));    
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        btnOpenURL.setActionCommand("Open URL");
        btnOpenURL.addActionListener(this);
        toolBar.add(btnOpenURL);
        btnOpenFile.setActionCommand("Open File");
        btnOpenFile.addActionListener(this);
        toolBar.add(btnOpenFile);
        btnBack.setActionCommand("Back");
        btnBack.addActionListener(this);
        btnBack.setEnabled(false);
        toolBar.add(btnBack);
        btnFoward.setActionCommand("Foward");
        btnFoward.addActionListener(this);
        btnFoward.setEnabled(false);
        toolBar.add(btnFoward);        
        btnRefresh.setActionCommand("Refresh");
        btnRefresh.addActionListener(this);
        toolBar.add(btnRefresh);
        toolBar.setFloatable(false);
        pnl1.setLayout(new BorderLayout());
        pnl1.add(toolBar, BorderLayout.NORTH);
        
        pnl2.setLayout(new BorderLayout());
        pnl2.add(new JLabel("Address"), BorderLayout.WEST);
        txfdAddress.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		displayPage(txfdAddress.getText());
        	}
        });
        pnl2.add(txfdAddress, BorderLayout.CENTER);
        btnLoad.addActionListener(this);
        pnl2.add(btnLoad, BorderLayout.EAST);
        pnl1.add(pnl2, BorderLayout.CENTER);
        
        contentPane.add(pnl1, BorderLayout.NORTH);
        
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(this);
        contentPane.add(new JScrollPane(editorPane), BorderLayout.CENTER);
        
        pnlStatus.add(lblStatus, BorderLayout.CENTER);
        prgbLoading.setPreferredSize(new Dimension(200, 20));
        pnlStatus.add(prgbLoading, BorderLayout.EAST);
        contentPane.add(pnlStatus, BorderLayout.SOUTH);
        
        mnuFile.add(mniOpenURL);
        mnuFile.add(mniOpenFile);
        mnuFile.add(mniExit);
        mnb.add(mnuFile);
        
        mnuHelp.add(mniAbout);
        mnb.add(mnuHelp);
        
        this.setJMenuBar(mnb);
        this.setLocation(0, 0);
        this.setSize(800,600);
        this.setVisible(true);
    }
    
    
    
    private boolean visit(URL url) {
    	try {
    		prgbLoading.setIndeterminate(true);
	    	txfdAddress.setText(url.toString()); 
	        editorPane.setPage(url); 
	        prgbLoading.setIndeterminate(false);	
	        return true;	   		
    	} catch(Exception ex) {
    		prgbLoading.setIndeterminate(false);
			lblStatus.setText("Error: " + ex.toString());	
			return false;
       } 
    }
    
    
    public String getHome() {
    	return "http:127.0.0.1:8080";
    }
    
    
    
    private void displayPage(String url) {
    	try {
    		displayPage(new URL(url));
    	} catch(Exception ex) {
    		lblStatus.setText("Error: " + ex.toString());	
    	}
    }
    
    
    
    private void displayPage(URL url) {
		if(visit(url)) {
	        history.add(url);
	        int numentries = history.size();
	        if(numentries > MAX_HISTORY + 10) {
	        	history = history.subList(numentries - MAX_HISTORY,  numentries);
	        	numentries = MAX_HISTORY;
	        }
	        currentHistoryPage = numentries - 1;
	        if(currentHistoryPage > 0) btnBack.setEnabled(true); 
		}  
	   	
    }
    
    
    
    private void goBack() {
    	if(currentHistoryPage > 0)
    		visit((URL)history.get(--currentHistoryPage));
    	btnBack.setEnabled((currentHistoryPage > 0));
    	btnFoward.setEnabled((currentHistoryPage < history.size() - 1));	
    }
    
    
    
    private void goForward() {
    	if(currentHistoryPage < history.size() -1)
    		visit((URL)history.get(++currentHistoryPage));
    	btnBack.setEnabled((currentHistoryPage > 0));
    	btnFoward.setEnabled((currentHistoryPage < history.size() -1));	
    }
    
    
    
    private void reload() {
    	if(currentHistoryPage != -1) {
    		editorPane.setDocument(new javax.swing.text.html.HTMLDocument());
    	}
    	visit((URL)history.get(currentHistoryPage));
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open URL")) {
            String url = "";
            url = MDS_OptionPane.showInputDialog(this, "Type the URL you want to open", "Web Browser", JOptionPane.QUESTION_MESSAGE);
            try {
                if(url == null) url = "";
                if(!url.equals("")) {
					displayPage(url);
                }
            } catch(Exception ex) {
                lblStatus.setText("Error: " + e.toString());
            }             
        } else if(e.getActionCommand().equals("Open File")) {
            try {
                MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
                Vector v = new Vector();
                v.add("html");
                v.add("htm");
                fmfc.setFilter(v);                
                if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {       
					displayPage("file:" + fmfc.getPath());		
                }    
            } catch(Exception ex) {
                lblStatus.setText("Error: " + e.toString());
            }             
        } else if(e.getActionCommand().equals("Refresh")) {
			reload();           
        } else if(e.getActionCommand().equals("Back")) {
			goBack();
        } else if(e.getActionCommand().equals("Foward")) {
        	goForward();    		                          
        } else if(e.getActionCommand().equals("Load")) {
            displayPage(txfdAddress.getText());                 
        } else if(e.getActionCommand().equals("Exit")) {   
            this.dispose();
        } else if(e.getActionCommand().equals("About..")) {
            usr.showAboutDialog(this, "Web Browser", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"web-browser.png"), MDS.getAbout_Mahesh());
        }  
        
    }
    
    
    
    public void hyperlinkUpdate(HyperlinkEvent e) {

		HyperlinkEvent.EventType type = e.getEventType();
		if(type == HyperlinkEvent.EventType.ACTIVATED) {
			displayPage(e.getURL());
		} else if(type == HyperlinkEvent.EventType.ENTERED) {
			lblStatus.setText(e.getURL().toString());
		} else if(type == HyperlinkEvent.EventType.EXITED) {
			lblStatus.setText(" ");
		}
               
    } 
    
    
    
    public static void MDS_Main(String args[]) {  
        WebBrowser wb = new WebBrowser();

		wb.displayPage((args.length > 0) ? args[0] : wb.getHome());
       
    }
    
    
    
}    