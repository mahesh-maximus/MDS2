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
import java.io.*;



public class VT_Diagnose extends MDS_Frame implements ActionListener {



    private static boolean vtd_Visibility = false;
    private static VT_Diagnose vtd;
    JComponent contentPane;
    MDS_Button btnException_SST = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"launcher-program.png"));
    MDS_Button btnException_SCT = new MDS_Button(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"launcher-program.png"));


    public VT_Diagnose() {
        super("Virtual Threading Diagnose", false, true, false, false, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kwin4.png")); 
        contentPane = (JComponent)this.getContentPane();
        contentPane.setLayout(new GridLayout(2,1));
        
        MDS_Panel pnlDiagnose_SST = new MDS_Panel(new BorderLayout());
        pnlDiagnose_SST.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Test System Scheduler Thread"));
        MDS_ToolBar tlbSST = new MDS_ToolBar(SwingConstants.VERTICAL);
        tlbSST.setFloatable(false);  
        btnException_SST.setActionCommand("SST");
        btnException_SST.addActionListener(this);    
        tlbSST.add(btnException_SST);
        pnlDiagnose_SST.add(tlbSST, BorderLayout.WEST);
        MDS_TextArea txtaSST = new MDS_TextArea();
        pnlDiagnose_SST.add(txtaSST, BorderLayout.CENTER);
        txtaSST.setEditable(false);    
        txtaSST.setBackground(UIManager.getColor("Label.background"));   
        String text = "    When you click this button it starts a new Virtual\n    Threaded program which throws an exception during\n    it's"+
                      " execution(Output of this program can be seen using\n    the utility cald Redirected Standard Output). Once"+
                      "\n    the exception is thrown all the Virtual Threads belongs\n    to System Scheduler Thread will be paused and Virtual"+
                      "\n    Threading Debugger will be launched. Now you can see\n    for example some items of the System Manager will\n    not refresh.";
        txtaSST.append(text);        
        
        contentPane.add(pnlDiagnose_SST);
             
        
        MDS_Panel pnlDiagnose_SCT = new MDS_Panel(new BorderLayout());
        pnlDiagnose_SCT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Test System Cycle Thread"));
        MDS_ToolBar tlbSCT = new MDS_ToolBar(SwingConstants.VERTICAL);
        tlbSCT.setFloatable(false);    
        btnException_SCT.setActionCommand("SCT");
        btnException_SCT.addActionListener(this);           
        tlbSCT.add(btnException_SCT);
        pnlDiagnose_SCT.add(tlbSCT, BorderLayout.WEST);
        MDS_TextArea txtaSCT = new MDS_TextArea();
        pnlDiagnose_SCT.add(txtaSCT, BorderLayout.CENTER);   
        txtaSCT.setEditable(false);  
        txtaSCT.setBackground(UIManager.getColor("Label.background"));
        text = "    When you click this button it starts a new Virtual\n    Threaded program which throws an exception during\n    it's"+
               " execution(Output of this program can be seen using\n    the utility cald Redirected Standard Output). Once"+
               "\n    the exception is thrown all the Virtual Threads belongs\n    to System Cycle Thread will be paused and Virtual"+
               "\n    Threading Debugger will be launched.\n    Now for example you can see the Task Bar will not\n    refresh.";
        txtaSCT.append(text);
        contentPane.add(pnlDiagnose_SCT);   
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                vtd_Visibility = false;
                vtd = null;                      
            }
        });                  
        
        this.setSize(410, 380);
        this.setCenterScreen();
        this.setVisible(true);  
        vtd_Visibility = true;
        vtd = this;
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!vtd_Visibility) {
            new VT_Diagnose();
        } else {
            try{
                VT_Diagnose.vtd.setIcon(false);
                VT_Diagnose.vtd.setSelected(true);
            } catch(Exception ex) {}
        }
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("SST")) {
            btnException_SST.setEnabled(false);
            
            class Sst implements SystemSchedulerThreadListener {
            
            
    
                int c = 0;
            
            
            
                public Sst() {
                    VirtualThreading.create_SST_VT(this, "SST");
                    System.out.println("Registering virtual thread SST");
                    System.out.println("[SST] an exception will be thrown when the count equals 10");
                }
                
                
                
                public long getSystemScheduler_EventInterval() {
                    return 500;
                }
                
                
                
                public void systemSchedulerEvent() {                    
                    System.out.println("[SST] count = "+String.valueOf(c));  
                    if(c == 10) {
                        c++;
                        throw new RuntimeException("");
                    }
                    
                    if(c == 12) {
                        System.out.println("Unregistering virtual thread SST");
                        VirtualThreading.terminate_SST_VT(this);
                        btnException_SST.setEnabled(true);
                    }                    
                    
                    c++;
                    
                }
                
                
                
                public void finalize() {
                    btnException_SST.setEnabled(true);
                }
            }
            
            MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"RedirectedStandardOutputViewer.class"));             
            new Sst();  
            
            
                      
        } else if(e.getActionCommand().equals("SCT")) {
            btnException_SCT.setEnabled(false);
        
        
            class Sct implements SystemCycleThreadListener {
            
            
    
                int c = 0;
            
            
            
                public Sct() {
                    VirtualThreading.create_SCT_VT(this, "SCT");
                    System.out.println("Registering virtual thread SCT");
                    System.out.println("[SCT] an exception will be thrown when the count equals 20");
                }
                
                
                
                public long getSystemCycle_EventInterval() {
                    return 500;
                }                
                
                
                             
                public void autoExecuteSubRoutine() {                   
                    System.out.println("[SCT] count = "+String.valueOf(c));  
                    if(c == 20) {
                        c++;
                        throw new RuntimeException("");
                    }
                    
                    if(c == 22) {
                        System.out.println("Unregistering virtual thread SCT");
                        VirtualThreading.terminate_SCT_VT(this);
                        btnException_SCT.setEnabled(true);
                    }
                    
                    c++;
                    
                }
            }
            
            MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"RedirectedStandardOutputViewer.class"));           
            new Sct();  
                    
        }
    }
    
    
    
}    