/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.io.IOException;
import java.lang.management.*;
import static java.lang.management.ManagementFactory.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;




public class SystemManager extends MDS_Frame implements ActionListener, SystemSchedulerThreadListener {



    private final String QUOTE = String.valueOf('"');
    private static boolean sm_Visibility = false;
    private static SystemManager sm = null;

    JComponent contentPane;
    JMenuBar menuBar = new JMenuBar();
    JMenu mnuFile = new JMenu("File");
    JMenu mnuOptions = new JMenu("Options");
    JMenu mnuShutDown = new JMenu("Shut Down"); 
    JMenu mnuHelp = new JMenu("Help");
    MDS_TabbedPane tbdp = new MDS_TabbedPane();  
    
    MDS_User usr = MDS.getUser();
    
    ActiveFrameList aflst= new ActiveFrameList();
    Performance pfc = new Performance();
    Threads threads = new Threads();
    VirtualThreads vt = new VirtualThreads();
    
    boolean active = true;
    


    public SystemManager() {
        super("System Manager", true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-device-system.png"));
        //this.setDefaultCloseOperation(this.HIDE_ON_CLOSE );
        sm = this;
        MDS.getBaseWindow().getDesktop().remove(this); 
        MDS.getBaseWindow().getDesktop().add(this ,JLayeredPane.MODAL_LAYER);
        contentPane = (JComponent) this.getContentPane();
        tbdp.add("Active Windows", aflst);
        tbdp.add("Performance", pfc);
        tbdp.add("Threads", threads);
        tbdp.add("Virtual Threads", vt); 
        contentPane.add(tbdp, BorderLayout.CENTER);
        mnuFile.add(usr.createMenuItem("New Task [Run Command]", this));
        mnuFile.add(usr.createMenuItem("Exit", this));            
        menuBar.add(mnuFile);
        mnuOptions.add(usr.createMenuItem("Reshresh Rate", this));
        menuBar.add(mnuOptions);
        mnuShutDown.add(usr.createMenuItem("Close", this));
        mnuShutDown.add(usr.createMenuItem("Log Off", this));
        menuBar.add(mnuShutDown);
        mnuHelp.add(usr.createMenuItem("Help Content", this));
        mnuHelp.add(usr.createMenuItem("About", this));
        menuBar.add(mnuHelp);  
        
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                //MDS.getSystemSchedulerThread().removeSystemSchedulerThreadListener(sm);           
                VirtualThreading.terminate_SST_VT(sm);
                active = false;
                sm_Visibility = false;
                sm = null;
            }
        });        
          
        this.setJMenuBar(menuBar);
        this.setSize(430, 410);
        this.setCenterScreen();
        this.setVisible(true);
        //MDS.getSystemSchedulerThread().addSystemSchedulerThreadListener(this, "System Manager");
        VirtualThreading.create_SST_VT(this, "System Manager");
        sm_Visibility = true;
        //sm = this;
        
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("GC")) {
               
        } else if(e.getActionCommand().equals("New Task [Run Command]")) {
            MDS.getProcessManager().showRunCommandDialog();
        } else if(e.getActionCommand().equals("Exit")) {
            this.setVisible(false);
        } else if(e.getActionCommand().equals("About")) {
            MDS.getUser().showAboutDialog(this, "System Manager", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"TaskManager.png"), MDS.getAbout_Mahesh());
            //MDS_OptionPane.showMessageDialog(this, "", "", JOptionPane.ERROR_MESSAGE);
        } else if(e.getActionCommand().equals("Close")	) {
            MDS.close();
        }
    }
    
    
    
    public long getSystemScheduler_EventInterval() {
        return 200;   
    }



    public void systemSchedulerEvent() {
        aflst.refresh();
        pfc.refresh();
        threads.refresh();   
        vt.refresh(); 
    }    
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!SystemManager.sm_Visibility) {
            new SystemManager();
        } else {
            try{
                SystemManager.sm.setIcon(false);
                SystemManager.sm.setSelected(true);
            } catch(Exception ex) {}
        }        
    }
    
    
    
     

    class Performance extends MDS_Panel implements  ActionListener, Runnable {
                  
        MDS_Panel pnlBase = new MDS_Panel();
        MDS_Panel pnlGC = new MDS_Panel();
        MDS_Panel pnlMemory = new MDS_Panel();
        MDS_Panel pnlCPU = new MDS_Panel();
        MDS_Panel pnlFreeMemory = new MDS_Panel();
        MDS_Label lblFreeMenmory = new MDS_Label();
        MDS_Label lblTotalMemory = new MDS_Label();
        MDS_Label lblMaxMemory = new MDS_Label();
        MDS_Label lblActiveThreadCount = new MDS_Label();
        MDS_Label lblNoProcessors = new MDS_Label();
        MDS_Label lblAvg_cpuUsage = new MDS_Label();
        MDS_ProgressBar pgsbFreeMemory = new MDS_ProgressBar(0, 100); 
        MDS_ProgressBar pgsbCPU_Usage = new MDS_ProgressBar(0, 100); 
        MDS_Button btnGC = new MDS_Button("GC");        
            
        Thread thrRefresh;
            
        Runtime rt = Runtime.getRuntime();
        FileManager fm = MDS.getFileManager();
            
        int threadCount = -1;
        
        private final String QUOTE = String.valueOf('"');
        Thread thread = new Thread(this, "VBScript_cpuUsage");  

		private com.sun.management.OperatingSystemMXBean sunOperatingSystemMXBean = null;	
		
		private long prevUpTime, prevProcessCpuTime;
		
		public long upTime;
		public long processCpuTime; 
		public long nCPUs = 1;
		public long timeStamp; 
			
		Vector_CPU_Usage vct_cpu = new Vector_CPU_Usage();	
        
        
    
        public Performance() {
            this.setLayout(new BorderLayout());  
            pnlBase.setLayout(new GridLayout(2, 1));  
            pnlMemory.setLayout(new GridLayout(4, 1));
            pnlMemory.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Memory Usage"));
            pnlMemory.add(lblFreeMenmory);
            pnlMemory.add(lblTotalMemory);
            pnlMemory.add(lblMaxMemory); 
            pgsbFreeMemory.setStringPainted(true);
            pnlMemory.add(pgsbFreeMemory);
            pnlBase.add(pnlMemory);
                
            pnlCPU.setLayout(new GridLayout(4, 1));
            pnlCPU.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"CPU"));
            pnlCPU.add(lblActiveThreadCount);
            pnlCPU.add(lblNoProcessors);
            pnlCPU.add(lblAvg_cpuUsage);
            pgsbCPU_Usage.setStringPainted(true);
            pnlCPU.add(pgsbCPU_Usage);
            pnlBase.add(pnlCPU);
                
            this.add(pnlBase, BorderLayout.CENTER);                
            pnlGC.setLayout(new BorderLayout());                
            btnGC.addActionListener(this);              
            pnlGC.add(btnGC, BorderLayout.EAST);              
            this.add(pnlGC, BorderLayout.SOUTH);
            
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();            
            
        }
        
        
              
            
            
            
        public void refresh() {
               
            lblFreeMenmory.setText("Available Free Memory "+fm.getFormatedFileSize(rt.freeMemory()));
            lblTotalMemory.setText("Available Total Memory "+fm.getFormatedFileSize(rt.totalMemory()));
            lblMaxMemory.setText("Available Maximum Memory "+fm.getFormatedFileSize(rt.maxMemory()));
            double u = rt.freeMemory()-rt.totalMemory() ;
            double t = rt.totalMemory() ;
            Double val = new Double(Math.abs(u/t*100));
                     
            pgsbFreeMemory.setValue(val.intValue());
            pgsbFreeMemory.setString("Memory in Usage "+String.valueOf(val.intValue() + "%"));
                  
            lblActiveThreadCount.setText("Number of active Threads "+String.valueOf(Thread.activeCount()));
            lblNoProcessors.setText("Number of processors available "+rt.availableProcessors()); 
            	
            
           	 	
            	    
            
       }  
       	
       	
       	
       private void updateCPU_Usage() throws Exception {
	 		upTime = ManagementFactory.getRuntimeMXBean().getUptime();
			processCpuTime = getSunOperatingSystemMXBean().getProcessCpuTime();
			nCPUs = 1;
			timeStamp = System.currentTimeMillis();      
				

		    //if (prevUpTime > 0L && upTime > prevUpTime) {
			// elapsedCpu is in ns and elapsedTime is in ms.
			long elapsedCpu = processCpuTime - prevProcessCpuTime;
			long elapsedTime = upTime - prevUpTime;
			// cpuUsage could go higher than 100% because elapsedTime
			// and elapsedCpu are not fetched simultaneously. Limit to
			// 99% to avoid Plotter showing a scale from 0% to 200%.
			float cpuUsage =
			    Math.min(99F,
				     elapsedCpu / (elapsedTime * 10000F * nCPUs));		     
	
			//getPlotter().addValues(result.timeStamp, Math.round(cpuUsage));
			//getInfoLabel().setText(getText(cpuUsageFormat, String.format("%.2f", cpuUsage)));
			//System.out.println("CPU Usage : "+String.format("%.2f", cpuUsage));
			
			Float fcpuUsage = new Float(cpuUsage);
			pgsbCPU_Usage.setValue(fcpuUsage.intValue());
            pgsbCPU_Usage.setString("CPU in Usage "+String.valueOf(fcpuUsage.intValue() + "%"));
            vct_cpu.addElement(fcpuUsage.intValue());
            lblAvg_cpuUsage.setText("Average CPU Usage  "+String.valueOf(vct_cpu. getAVG_Usage())+"%");		   
		   
		   
		   
		    prevUpTime = upTime;
		    prevProcessCpuTime =processCpuTime;
				
       }
       
       
    public synchronized com.sun.management.OperatingSystemMXBean 
        getSunOperatingSystemMXBean() throws IOException {
        try {
			sunOperatingSystemMXBean  = (com.sun.management.OperatingSystemMXBean)ManagementFactory.newPlatformMXBeanProxy(ManagementFactory.getPlatformMBeanServer() ,OPERATING_SYSTEM_MXBEAN_NAME , com.sun.management.OperatingSystemMXBean.class);
        } catch (Exception e) {
			e.printStackTrace();	
             return null; // should never reach here
        }
		return sunOperatingSystemMXBean;
    }       	      	  
       
       
       
       public void run() {
       
       
           try {
                
                while(active) {
//                                pgsbCPU_Usage.setString("CPU in Usage "+String.valueOf(d.intValue() + "%"));
//                                vct_cpu.addElement(currentTotal);
//                                lblAvg_cpuUsage.setText("Average CPU Usage  "+String.valueOf(vct_cpu. getAVG_Usage())+"%");
					updateCPU_Usage();
					Thread.sleep(500);
                } 
            
            } catch (Exception ex) {
                //Console.println(ex.toString());
                ex.printStackTrace();
            } finally {

            }        
       }
            
            
            
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("GC")) {
                rt.gc();    
            }
        }       
        
        
        
        
        
        class Vector_CPU_Usage extends Vector {
        
        
        
            public Vector_CPU_Usage() {
                super();
            }
            
            
            
            public void addElement(int val) {
                super.addElement(new Integer(val));
                if(this.size()>=10) {
                    this.remove(0);
                }
            } 
            
            
            
            public int getAVG_Usage() {
                int total = 0;
                int count = 1;
                for(int x=0;x<=this.size()-1;x++) {
                    count++;
                    total = total+Integer.parseInt(String.valueOf(this.get(x)));
                }
                
                return total/count;
                
            }

            
            
            
        }         
            
           
    }
    
    
    
    
    
    class Threads extends MDS_Panel {
    
    
    
        MDS_TableModel tbmdl = new MDS_TableModel();
        MDS_Table tblThreads = new MDS_Table(tbmdl); 
        
        int threadCount = 0;  
        int previousThreadCount = 0;
        int priority = 0;
        
        
    
        public Threads() {
            this.setLayout(new BorderLayout());
            tbmdl.addColumn("Name");
            tbmdl.addColumn("Priority"); 
            tbmdl.addColumn("CPU(sec)");	
            tblThreads.getColumn("Name").setMinWidth(300); 
            tblThreads.getColumn("Name").setMaxWidth(600);           
            this.add(new JScrollPane(tblThreads), BorderLayout.CENTER);
        }
        
        
        
        public void refresh() {
            threadCount = Thread.activeCount();  
            Thread lst[] = new Thread[threadCount];
            int c = Thread.enumerate(lst);
            //Console.println(threadCount);
            //Console.println(Thread.activeCount());
            
            ThreadMXBean tmbean = ManagementFactory.getThreadMXBean();
            tmbean.setThreadCpuTimeEnabled(true);
            
            if(threadCount != previousThreadCount) {
                previousThreadCount = threadCount;
                Vector v = tbmdl.getDataVector();
                v.removeAllElements(); 
                for(int count = 0; count < c; count++) {               
                    Vector data = new Vector();
                    data.add(lst[count].getName());
                    //data.add(new Integer(lst[count].getPriority()));
                    priority = lst[count].getPriority();
                    
                    if(priority >= 10) {
                        data.add("Maximum");
                    } else if(priority >= 5) {
                        data.add("Normal");
                    } else if(priority >= 1) {
                        data.add("Minimum");
                    }
                    
                    Double d = new Double(tmbean.getThreadCpuTime(lst[count].getId())/1000000000);
                    
                    data.add(d);
                    
                    tbmdl.addRow(data);
                                                                                           
                }
                
                DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
	              public void setValue(Object value) {
	                  String s = (String) value;
                        if(value.equals("Maximum")) {
                            setForeground(Color.RED); 
                            setText("Maximum");
                        } else if(value.equals("Normal")) {
                            setForeground(Color.BLUE); 
                            setText("Normal");
                        } else if(value.equals("Minimum")) {
                            setForeground(new Color(0, 128, 0)); 
                            setText("Minimum");
                        }	                      
	              }
                };                    
                    
                TableColumn colorColumn = tblThreads.getColumn("Priority");    
                colorColumn.setCellRenderer(colorRenderer);                  
                
            }
           
           
           
        }
        
        
        
    }
    
    
    
    
    
    class VirtualThreads extends MDS_Panel implements ActionListener {
    
    
    
        int virtualThreadCount = 0;  
        int previousVirtualThreadCount = 0;
        //MDS.SystemSchedulerThread sst = MDS.getSystemSchedulerThread();
        //MDS.SystemCycleThread ssct = MDS.getSystemCycleThread();
            
        MDS_TableModel tbmdl = new MDS_TableModel();
        MDS_Table tblThreads = new MDS_Table(tbmdl); 
        MDS_Button btnVTM = new MDS_Button("VT Manager");   
    
    
    
        public VirtualThreads() {            
            this.setLayout(new BorderLayout());
            tbmdl.addColumn("Name");        
            tbmdl.addColumn("Parent Thread");   
            this.add(new JScrollPane(tblThreads), BorderLayout.CENTER); 
            MDS_Panel pnlVTM = new MDS_Panel(new FlowLayout(FlowLayout.RIGHT));
            btnVTM.addActionListener(this);
            pnlVTM.add(btnVTM);
            this.add(pnlVTM, BorderLayout.SOUTH);                   
        }
        
        
        
        public void refresh() {
            //MDS.SystemSchedulerThread sst = MDS.getSystemSchedulerThread().
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
                    data.add("SystemSchedulerThread");                    
                    tbmdl.addRow(data); 
                }   
                  
                for(int x=0;x<=sctVirtualThreads.size()-1;x++) {
                    Vector data = new Vector();
                    data.add(sctVirtualThreads.getKey(x));
                    data.add("SystemCycleThread");                    
                    tbmdl.addRow(data); 
                }                            
                             
            }  
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("VT Manager")) {
                MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"VirtualThreadingManager"));
            }
        }
        
        
        
        
    
    } 
    
    
    
    
    
    class ActiveFrameList extends MDS_Panel {
    
    
    
        MDS_TableModel tbmdl = new MDS_TableModel();
        MDS_Table tblFrames = new MDS_Table(tbmdl); 
        DefaultListSelectionModel dlsmdl;
        int frameCount = 0;    
        int currentFrameID = 0;
    
    
        public ActiveFrameList() {
            this.setLayout(new BorderLayout());
            dlsmdl = (DefaultListSelectionModel)tblFrames.getSelectionModel(); 
            tblFrames.setSelectionModel(dlsmdl);  
            tbmdl.addColumn("");
            tbmdl.addColumn("ID");  
            tbmdl.addColumn("Title");   
            tblFrames.getColumn("").setMaxWidth(20);
            tblFrames.getColumn("ID").setMaxWidth(120); 
            //tblFrames.setRowHeight(50);
            tblFrames.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == e.BUTTON3) {
                        Integer i = new Integer(String.valueOf(tblFrames.getValueAt(tblFrames.getRowForLocation(e.getY()), 1)));
                        int rowNo = tblFrames.getRowForLocation(e.getY());
                        dlsmdl.setSelectionInterval(rowNo, rowNo);
                        currentFrameID = i.intValue();
                        JPopupMenu popup = createPopupMenu();     
                        popup.show(tblFrames, e.getX(), e.getY());
                    }                   
                }
            });       
            this.add(new JScrollPane(tblFrames), BorderLayout.CENTER);            
        }
        
        
        
        private JPopupMenu createPopupMenu() {
            MDS_UIManager uim = MDS.getMDS_UIManager();
            MDS_Frame frm =(MDS_Frame)uim.getFrame(currentFrameID);
            JPopupMenu popup = new JPopupMenu();
            JMenuItem mniBringToFront = new JMenuItem("Bring To Front");
            mniBringToFront.addActionListener(new MDS_ActionAdapter() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        MDS_Frame frm =(MDS_Frame)MDS.getMDS_UIManager().getFrame(currentFrameID); 
                        frm.setIcon(false);
                        frm.setSelected(true);
                    } catch(Exception ex) {}       
                }                
            });
            popup.add(mniBringToFront);
            popup.addSeparator();
            JMenuItem mniMinimize = new JMenuItem("Minimize");
            mniMinimize.addActionListener(new MDS_ActionAdapter() {
                public void actionPerformed(ActionEvent e) {
                    try {    
                        MDS_Frame frm =(MDS_Frame)MDS.getMDS_UIManager().getFrame(currentFrameID); 
                        frm.setIcon(true); 
                    } catch(Exception ex) {}                        
                }                
            });
            popup.add(mniMinimize);
            JMenuItem mniMaximize = new JMenuItem("Maximize");
            mniMaximize.addActionListener(new MDS_ActionAdapter() {
                public void actionPerformed(ActionEvent e) {
                    try {    
                        MDS_Frame frm =(MDS_Frame)MDS.getMDS_UIManager().getFrame(currentFrameID); 
                        frm.setMaximum(true); 
                    } catch(Exception ex) {}                      
                }                
            });
            popup.add(mniMaximize); 
            JMenuItem mniClose = new JMenuItem("Close");
            mniClose.addActionListener(new MDS_ActionAdapter() {
                public void actionPerformed(ActionEvent e) {
                    MDS_Frame frm =(MDS_Frame)MDS.getMDS_UIManager().getFrame(currentFrameID); 
                    frm.dispose();                     
                }                
            });
            popup.add(mniClose);
            popup.addSeparator();
            JMenuItem mniDestroy = new JMenuItem("Destroy"); 
            mniDestroy.addActionListener(new MDS_ActionAdapter() {
                public void actionPerformed(ActionEvent e) {
                    MDS_Frame frm =(MDS_Frame)MDS.getMDS_UIManager().getFrame(currentFrameID); 
                    frm.dispose();                     
                }                
            });   
            popup.add(mniDestroy);
                     
            if(!frm.isIconifiable()) {
                mniMinimize.setEnabled(false);                   
            }
            
            if(!frm.isMaximizable()) {
                mniMaximize.setEnabled(false);         
            }
            
            if(!frm.isClosable()) {
                mniClose.setEnabled(false);
            }
                        
            return popup;   
        }
        
        
        
        public void refresh() {
            JInternalFrame Frames[]  = MDS.getBaseWindow().getDesktop().getAllFrames(); 
            
            if(frameCount != Frames.length) {
                frameCount = Frames.length;
                Vector v = tbmdl.getDataVector();
                v.removeAllElements();
                
                for(int count = 0; count < Frames.length; count++) { 
                    try { 
                        Vector data = new Vector();
                        //data.add("");
                        //ImageManipulator.createScaledImage(i, 14, 16, ImageManipulator.ICON_SCALE_TYPE)
                        //ImageIcon i = (ImageIcon)((MDS_Frame)Frames[count]).getFrameIcon();
                        data.add((ImageIcon)((MDS_Frame)Frames[count]).getFrameIcon()); 
                        data.add(new Integer(((MDS_Frame)Frames[count]).getFrameId()));
                        data.add(Frames[count].getTitle());
                        tbmdl.addRow(data);      
                    } catch(Exception ex) {}          
                }    
                             
            }
        }
        
        
        
    }                    
        
        
        
             
    
    
    
}   