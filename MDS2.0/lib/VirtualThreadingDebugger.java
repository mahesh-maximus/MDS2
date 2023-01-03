
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;



public final class VirtualThreadingDebugger implements ActionListener {
    
    
    
    private static VirtualThreadingDebugger vtdbg= new VirtualThreadingDebugger();
    
    static boolean frmSCT_Debug_Visibility = false;
    static boolean frmSST_Debug_Visibility = false;
         
    long sstLastCheckTime = 0;    
    long sctLastCheckTime = 0;
    boolean sstDebugOn = false;
    boolean sctDebugOn = false;
        
    MDS_Frame frmSCT_Debug;
    MDS_Frame frmSST_Debug;
        
    VirtualThreading.SystemCycleThread sct;
    VirtualThreading.SystemSchedulerThread sst;
        
    Exception exSCT;
    Exception exSST;
        
        
        
    
    private VirtualThreadingDebugger() {}
    
    
    
    public static VirtualThreadingDebugger getCurrent_VTDBG() {
        return vtdbg;
    }
        
        
        
    public void refresh() {
        if(exSCT != null) {
            fireDebug_SystemCycleThread(sct, exSCT);
        }         
            
        if(exSST != null) {
            fireDebug_SystemSchedulerThread(sst, exSST);
        }         
    }
        
        
     
        
    public void fireDebug_SystemSchedulerThread(VirtualThreading.SystemSchedulerThread ssThread, Exception ex) {
        sst = ssThread;
        BaseWindow bw = MDS.getBaseWindow();
        if(bw != null) {
            if(bw.isDesktopLoaded()) {
                if(!frmSST_Debug_Visibility) {
                    frmSST_Debug_Visibility = true;
                    exSST = ex;
                    showSSTDebugFrame();
                }
            } else {
                exSST = ex;
            }
        } else {
            exSST = ex;
        }
    } 
        
        
        
    public void fireDebug_SystemCycleThread(VirtualThreading.SystemCycleThread scThread, Exception ex) {
        sct = scThread;
        BaseWindow bw = MDS.getBaseWindow();
        if(bw != null) {
            if(bw.isDesktopLoaded()) {
                if(!frmSCT_Debug_Visibility) {
                    frmSCT_Debug_Visibility = true;
                    exSCT = ex;
                    showSCTDebugFrame();          
                }            
            } else {
                exSCT = ex;
            }
        } else {
            exSCT = ex;
        }            
    }        
        
        
        
    private void showSSTDebugFrame() {
        frmSST_Debug = new MDS_Frame();
        frmSST_Debug.showInTaskBar(false); 
        frmSST_Debug.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        MDS.getBaseWindow().getDesktop().remove(frmSST_Debug); 
        MDS.getBaseWindow().getDesktop().add(frmSST_Debug ,JLayeredPane.DRAG_LAYER);            
        JComponent contentPane = (JComponent) frmSST_Debug.getContentPane();
        contentPane.setLayout(new BorderLayout());
        MDS_Label lblTitle = new MDS_Label("Virtual Threading Debugger");
        lblTitle.setFont(new Font(lblTitle.getFont().getName(), lblTitle.getFont().getStyle(), 20));
        MDS_Panel pnlTitle = new MDS_Panel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.add(lblTitle);
        contentPane.add(pnlTitle, BorderLayout.NORTH);
        MDS_Label lblIcon = new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"stop1.png"));
        contentPane.add(lblIcon, BorderLayout.WEST);
        MDS_TextArea txtaMessage = new MDS_TextArea();
        txtaMessage.setBackground(UIManager.getColor("Label.background"));
        txtaMessage.setEditable(false);
            
        SystemSchedulerThreadListener currentVirtualThread = sst.getCurrentVirtualThread();
        MDS_Hashtable vtInfo = sst.getVirualThreadInfo();
        String currentVirtualThreadName = "N/A";
        if(vtInfo.containsValue(currentVirtualThread)) {
            currentVirtualThreadName = String.valueOf(vtInfo.getKey(currentVirtualThread));
        }
            
        String message = "Due to an exception thrown by virtual thread called '"+currentVirtualThreadName+"' the\nparent native thread has "+
                         "terminated. So all the virtual threads of\n'System Scheduler' native thread is paused till you restart this\nparent "+
                         "thread.\nYou can restart 'System Scheduler' parent thread by pressing\n'Restart' or if you press 'Close' button "+
                         "parent thread will not restart\nand all of it's virtual thread will not work as well.\nIF THE PROBLEM PERSISTS YOU"+
                         " CAN UNREGISTER THE\nVIRTUAL THREAD WHICH THROWS THIS EXCEPTION USING\n'VT MANAGER'.\n\nException Summery which "+
                         "caused the termination of 'System Scheduler'.\n========================================================\n"; 
           
        StackTraceElement[] stElement = exSST.getStackTrace();
            
        message = message.concat("Class Name : "+stElement[0].getClassName()+"\n");
        message = message.concat("File Name : "+stElement[0].getFileName()+"\n");
        message = message.concat("Line Number : "+String.valueOf(stElement[0].getLineNumber())+"\n");
        message = message.concat("Method Name : "+stElement[0].getMethodName()+"\n");
        message = message.concat("Exception : "+exSST.toString()+"\n");

        txtaMessage.append(message);
        //JScrollPane scrlp = new JScrollPane(txtaMessage, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(new JScrollPane(txtaMessage), BorderLayout.CENTER);
        contentPane.add(new MDS_Label("   "), BorderLayout.EAST);
        MDS_Panel pnlButtons = new MDS_Panel();
        pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        MDS_Button btnRestart = new MDS_Button("Restart");
        btnRestart.setActionCommand("Restart_SST");
        btnRestart.addActionListener(this);
        pnlButtons.add(btnRestart);
        MDS_Button btnClose = new MDS_Button("Close");   
        btnClose.setActionCommand("Close_SST");
        btnClose.addActionListener(this);         
        pnlButtons.add(btnClose);
        MDS_Button btnVTM = new MDS_Button("VT Manager");   
        btnVTM.setActionCommand("VT Manager");
        btnVTM.addActionListener(this);         
        pnlButtons.add(btnVTM);                
        contentPane.add(pnlButtons, BorderLayout.SOUTH);
        frmSST_Debug.setSize(440, 340);
        frmSST_Debug.setCenterScreen();
        frmSST_Debug.setVisible(true);   
        MDS.getSound().playSound(new File("Media\\Sound\\Beep_Exception.wav"));
    }     
        
        
        
    private void showSCTDebugFrame() {
        frmSCT_Debug = new MDS_Frame();
        frmSCT_Debug.showInTaskBar(false); 
        frmSCT_Debug.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        MDS.getBaseWindow().getDesktop().remove(frmSCT_Debug); 
        MDS.getBaseWindow().getDesktop().add(frmSCT_Debug ,JLayeredPane.DRAG_LAYER);            
        JComponent contentPane = (JComponent) frmSCT_Debug.getContentPane();
        contentPane.setLayout(new BorderLayout());
        MDS_Label lblTitle = new MDS_Label("Virtual Threading Debugger");
        lblTitle.setFont(new Font(lblTitle.getFont().getName(), lblTitle.getFont().getStyle(), 20));
        MDS_Panel pnlTitle = new MDS_Panel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.add(lblTitle);
        contentPane.add(pnlTitle, BorderLayout.NORTH);
        MDS_Label lblIcon = new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"stop1.png"));
        contentPane.add(lblIcon, BorderLayout.WEST);
        MDS_TextArea txtaMessage = new MDS_TextArea();
        txtaMessage.setBackground(UIManager.getColor("Label.background"));
        txtaMessage.setEditable(false);
            
        SystemCycleThreadListener currentVirtualThread = sct.getCurrentVirtualThread();
        MDS_Hashtable vtInfo = sct.getVirualThreadInfo();
        String currentVirtualThreadName = "N/A";
        if(vtInfo.containsValue(currentVirtualThread)) {
            currentVirtualThreadName = String.valueOf(vtInfo.getKey(currentVirtualThread));
        }
            
        String message = "Due to an exception thrown by virtual thread called '"+currentVirtualThreadName+"' the\nparent native thread has "+
                         "terminated. So all the virtual threads of\n'System Cycle' native thread is paused till you restart this\nparent "+
                         "thread.\nYou can restart 'System Cycle' parent thread by pressing\n'Restart' or if you press 'Close' button "+
                         "parent thread will not restart\nand all of it's virtual thread will not work as well.\nIF THE PROBLEM PERSISTS YOU"+
                         " CAN UNREGISTER THE\nVIRTUAL THREAD WHICH THROWS THIS EXCEPTION USING\n'VT MANAGER'.\n\nException Summery which "+
                         "caused the termination of 'System Cycle'.\n========================================================\n"; 
          
        StackTraceElement[] stElement = exSCT.getStackTrace();
          
        message = message.concat("Class Name : "+stElement[0].getClassName()+"\n");
        message = message.concat("File Name : "+stElement[0].getFileName()+"\n");
        message = message.concat("Line Number : "+String.valueOf(stElement[0].getLineNumber())+"\n");
        message = message.concat("Method Name : "+stElement[0].getMethodName()+"\n");
        message = message.concat("Exception : "+exSCT.toString()+"\n");

        txtaMessage.append(message);
        //JScrollPane scrlp = new JScrollPane(txtaMessage, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(new JScrollPane(txtaMessage), BorderLayout.CENTER);
        contentPane.add(new MDS_Label("   "), BorderLayout.EAST);
        MDS_Panel pnlButtons = new MDS_Panel();
        pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        MDS_Button btnRestart = new MDS_Button("Restart");
        btnRestart.setActionCommand("Restart_SCT");
        btnRestart.addActionListener(this);
        pnlButtons.add(btnRestart);
        MDS_Button btnClose = new MDS_Button("Close");   
        btnClose.setActionCommand("Close_SCT");
        btnClose.addActionListener(this);         
        pnlButtons.add(btnClose);
        MDS_Button btnVTM = new MDS_Button("VT Manager");   
        btnVTM.setActionCommand("VT Manager");
        btnVTM.addActionListener(this);         
        pnlButtons.add(btnVTM);            
        contentPane.add(pnlButtons, BorderLayout.SOUTH);
        frmSCT_Debug.setSize(440, 340);
        frmSCT_Debug.setCenterScreen();
        frmSCT_Debug.setVisible(true); 
        MDS.getSound().playSound(new File("Media\\Sound\\Beep_Exception.wav"));
    }           
        
        
        
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Restart_SST")) {                
            sstDebugOn = false;
            sst.restart();
            frmSST_Debug.dispose();
            frmSST_Debug_Visibility = false;
        } else if(e.getActionCommand().equals("Close_SST")) {
            frmSST_Debug.dispose();
            frmSST_Debug_Visibility = false;
        } else if(e.getActionCommand().equals("Restart_SCT")) {                
            sctDebugOn = false;
            sct.restart();
            frmSCT_Debug.dispose();
            frmSCT_Debug_Visibility = false;
        } else if(e.getActionCommand().equals("Close_SCT")) {
            frmSCT_Debug.dispose();
            frmSCT_Debug_Visibility = false;
        }  else if(e.getActionCommand().equals("VT Manager")) {
            MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"VirtualThreadingManager"));
        }
    }
        
        
        
} 