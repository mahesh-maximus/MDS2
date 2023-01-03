/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;



public class VirtualThreadingManager extends MDS_Frame implements Runnable, ActionListener {



    private static boolean vtm_Loaded = false;
    private static VirtualThreadingManager vtm;
    JComponent contentPane;
    DefaultListSelectionModel dlsmdl;
    MDS_TableModel tbmdl = new MDS_TableModel();
    MDS_Table table = new MDS_Table(tbmdl);
    Thread thread;
    boolean done = false;
    MDS_PopupMenu pMenu = new MDS_PopupMenu();
    JMenuItem mniUnregister = new JMenuItem("Unregister");
    JMenuItem mniSuspend = new JMenuItem("Suspend");
    JMenuItem mniResume = new JMenuItem("Resume");
    
    

    public VirtualThreadingManager() {
        super("Virtual Threading Manager", true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-kbounce.png"));
        
        contentPane = (JComponent)this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        mniUnregister.addActionListener(this);
        pMenu.add(mniUnregister);
        pMenu.addSeparator();
        pMenu.add(mniSuspend);
        mniSuspend.addActionListener(this);
        pMenu.add(mniResume);
        mniResume.addActionListener(this);
        dlsmdl = (DefaultListSelectionModel)table.getSelectionModel();
        tbmdl.addColumn("Name");    
        tbmdl.addColumn("Running");
        tbmdl.addColumn("Sleep time");    
        tbmdl.addColumn("Parent Thread");
        table.getColumn("Name").setMinWidth(200); 
        table.getColumn("Name").setMaxWidth(300);   
         
        table.getColumn("Running").setMinWidth(70); 
        table.getColumn("Running").setMaxWidth(70);     
        
        table.getColumn("Sleep time").setMinWidth(100); 
        table.getColumn("Sleep time").setMaxWidth(150);  
                           
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        contentPane.add(new MDS_Label("Press right mouse button on the requaire VT."), BorderLayout.SOUTH);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == e.BUTTON3) {
                    mniSuspend.setEnabled(false);
                    mniResume.setEnabled(false); 
                    int rowNo = table.getRowForLocation(e.getY());
                    dlsmdl.setSelectionInterval(rowNo, rowNo); 
                    String vtName = String.valueOf(table.getValueAt(rowNo, 0)); 
                    //MDS.SystemSchedulerThread sst = MDS.getSystemSchedulerThread();
                    //MDS.SystemCycleThread sct = MDS.getSystemCycleThread();  
                    MDS_Hashtable sstVirtualThreads = VirtualThreading.get_SST_VT_Info();
                    MDS_Hashtable sctVirtualThreads = VirtualThreading.get_SCT_VT_Info(); 
                    if(sstVirtualThreads.containsKey(vtName)) {
                        if(VirtualThreading.is_SST_VT_Running(vtName)) {
                            mniSuspend.setEnabled(true);
                            mniResume.setEnabled(false);
                        } else {
                            mniSuspend.setEnabled(false);
                            mniResume.setEnabled(true);
                        }
                    } else if(sctVirtualThreads.containsKey(vtName)) {
                        if(VirtualThreading.is_SCT_VT_Running(vtName)) {
                            mniSuspend.setEnabled(true);
                            mniResume.setEnabled(false);
                        } else {
                            mniSuspend.setEnabled(false);
                            mniResume.setEnabled(true);
                        }                   
                    }        
                    pMenu.show(table, e.getX(), e.getY());    
                }    
            }
        }); 
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                vtm_Loaded = false;
                vtm = null;                      
            }
        });                            
        this.setSize(550, 300);
        this.setCenterScreen();
        this.setVisible(true);
        
        vtm = this;
        vtm_Loaded = true;
        
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        
    }
    
    
    
    public void run() {
        int virtualThreadCount = 0;  
        int previousVirtualThreadCount = 0;
        //MDS.SystemSchedulerThread sst = MDS.getSystemSchedulerThread();
        //MDS.SystemCycleThread sct = MDS.getSystemCycleThread();

        try {
            while(!done) {
                thread.sleep(200);
                MDS_Hashtable sstVirtualThreads = VirtualThreading.get_SST_VT_Info();
                MDS_Hashtable sctVirtualThreads = VirtualThreading.get_SCT_VT_Info(); 
                virtualThreadCount = sstVirtualThreads.size()+sctVirtualThreads.size();      
            
                if(virtualThreadCount != previousVirtualThreadCount) {
                    previousVirtualThreadCount = virtualThreadCount;  
                    Vector v = tbmdl.getDataVector();
                    v.removeAllElements(); 
                
                    for(int x=0;x<=sstVirtualThreads.size()-1;x++) {
                        Vector data = new Vector();
                        data.add(sstVirtualThreads.getKey(x));
                        if(VirtualThreading.is_SST_VT_Running((SystemSchedulerThreadListener)sstVirtualThreads.getValue(x))) {
                            data.add(new Boolean(true));
                        } else {
                            data.add(new Boolean(false));
                        }
                        data.add(new Long(((SystemSchedulerThreadListener)sstVirtualThreads.getValue(x)).getSystemScheduler_EventInterval()));
                        data.add("SystemSchedulerThread");                    
                        tbmdl.addRow(data); 
                    }   
                     
                    for(int x=0;x<=sctVirtualThreads.size()-1;x++) {
                        Vector data = new Vector();
                        data.add(sctVirtualThreads.getKey(x));
                        if(VirtualThreading.is_SCT_VT_Running((SystemCycleThreadListener)sctVirtualThreads.getValue(x))) {
                            data.add(new Boolean(true));
                        } else {
                            data.add(new Boolean(false));
                        }
                        data.add(new Long(100));                        
                        data.add("SystemCycleThread");                    
                        tbmdl.addRow(data); 
                    }                                                      
                }             
                                
            }  
        } catch(InterruptedException ex) {}      
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        MDS_Hashtable sstVirtualThreads = VirtualThreading.get_SST_VT_Info();
        MDS_Hashtable sctVirtualThreads = VirtualThreading.get_SCT_VT_Info();  
        String vtName = String.valueOf(table.getValueAt(table.getSelectedRow(), 0));           
   
        if(e.getActionCommand().equals("Unregister")) {
            int r = MDS_OptionPane.showConfirmDialog(this, "WARNING: terminating a Virtual Thread will cause a termination of the relative system service.\nAre you sure you want to terminate the Virtual Thread.", "Unregister Virtual Thread", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); 
            if(r == JOptionPane.YES_OPTION) {
                if(sstVirtualThreads.containsKey(vtName)) {
                    VirtualThreading.terminate_SST_VT((SystemSchedulerThreadListener)sstVirtualThreads.getValue(vtName));
                } else if(sctVirtualThreads.containsKey(vtName)) {
                    VirtualThreading.terminate_SCT_VT((SystemCycleThreadListener)sctVirtualThreads.getValue(vtName));
                }  
            }      
        } else if(e.getActionCommand().equals("Suspend")) {
            if(sstVirtualThreads.containsKey(vtName)) {
                VirtualThreading.suspend_SST_VT((SystemSchedulerThreadListener)sstVirtualThreads.getValue(vtName));
                table.setValueAt(new Boolean(false), table.getSelectedRow(), 1);
            } else if(sctVirtualThreads.containsKey(vtName)) {
                VirtualThreading.suspend_SCT_VT((SystemCycleThreadListener)sctVirtualThreads.getValue(vtName));
                table.setValueAt(new Boolean(false), table.getSelectedRow(), 1);
            }
        } else if(e.getActionCommand().equals("Resume")) {
            if(sstVirtualThreads.containsKey(vtName)) {
                VirtualThreading.resume_SST_VT((SystemSchedulerThreadListener)sstVirtualThreads.getValue(vtName));
                table.setValueAt(new Boolean(true), table.getSelectedRow(), 1);
            } else if(sctVirtualThreads.containsKey(vtName)) {
                VirtualThreading.resume_SCT_VT((SystemCycleThreadListener)sctVirtualThreads.getValue(vtName));
                table.setValueAt(new Boolean(true), table.getSelectedRow(), 1);
            }       
        }
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        if(VirtualThreadingManager.vtm_Loaded) {
            try{
                VirtualThreadingManager.vtm.setIcon(false);
                VirtualThreadingManager.vtm.setSelected(true);
            } catch(Exception ex) {}            
        } else {
            new VirtualThreadingManager();
        } 
    }
    
    
    
}    