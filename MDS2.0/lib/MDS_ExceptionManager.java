/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.util.logging.*;


public final class MDS_ExceptionManager implements Runnable {



    private static MDS_ExceptionManager em = new MDS_ExceptionManager();
    private Logger log = Logger.getLogger("MDS_ExceptionManager");
    private Thread emThread = new Thread(this, "ExceptionManager");
    int previousThreadCount = 0;



    private MDS_ExceptionManager() {
    	emThread.setPriority(Thread.MIN_PRIORITY);
		emThread.start();	
    }
    
    
    
    public static MDS_ExceptionManager getMDS_ExceptionManager() {
        return em;
    }
    
    
    
    public void run() {
    	while(true) {
    		try {
		    	if(Thread.activeCount() != previousThreadCount) {
		    		previousThreadCount = Thread.activeCount();
		    		
					Thread threads[] = new Thread[Thread.activeCount()];
					Thread.enumerate(threads);
//					log.info("Active Thread count : "+Thread.activeCount());
			    	for(Thread thread : threads) {
//			    		log.info("Thread Name : "+thread.getName());
			    		if(thread.getUncaughtExceptionHandler() instanceof UncaughtExceptionHandler) {}
			    		else {	 
			    			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler());
			    		}
			    	}    		
		    	}
		    	Thread.sleep(200);
    		} catch(Exception ex) {
    			System.out.println(ex.toString());	
    		}
    	}
    }
    
    
    
    public void showErrorMessage(Component parent, String msg, String title) {
        MDS_OptionPane.showMessageDialog(parent, msg, title, JOptionPane.ERROR_MESSAGE);
    }
    
    
    
    
    private class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    	
    	
    	
    	public void uncaughtException(Thread t, Throwable e) {
    		new ExceptionFrame(e, t);
    	}
    
    }
    
    
    
    
    
    private class ExceptionFrame extends MDS_Frame implements ActionListener {
    
    
    
        MDS_Label txtaHeader = new MDS_Label();
        JComponent contentPane;
        MDS_Panel pnlExceptionSummery = new MDS_Panel();
        MDS_Panel pnlButtonHolder = new MDS_Panel();
        
        MDS_Button btnView = new MDS_Button("View Stack Trace");
        MDS_Button btnSend = new MDS_Button("Send Exception Report");
        MDS_Button btnDn_Send = new MDS_Button("Don't Send");
        
        JLabel lblDummy = new JLabel();
        
        MDS_TextArea txtaExceptionSummery = new MDS_TextArea();
        
        StackTraceElement[] stElement;
        
        Throwable exception;
        Thread thread;
    
    
        public ExceptionFrame(Throwable e, Thread thread) {
            super("Exception", true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-error.png"));
            MDS.getBaseWindow().getDesktop().remove(this); 
            MDS.getBaseWindow().getDesktop().add(this ,JLayeredPane.MODAL_LAYER);               
            this.showInTaskBar(false);
            this.setIconifiable(false);
            exception = e;
            this.thread = thread;
            stElement = exception.getStackTrace();
            contentPane = (JComponent)this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            txtaHeader.setFont(lblDummy.getFont());
            //txtaHeader.setEditable(false);
            txtaHeader.setIcon(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-error.png"));
            txtaHeader.setText("Uncaught Exception Handler");
            JScrollPane scrlHeader = new JScrollPane(txtaHeader, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            contentPane.add(scrlHeader, BorderLayout.NORTH);
            
            pnlExceptionSummery.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Exception Summery"));
            pnlExceptionSummery.setLayout(new BorderLayout());
            txtaExceptionSummery.setBackground(UIManager.getColor("Label.background"));
            txtaExceptionSummery.setEditable(false);
            pnlExceptionSummery.add(new JScrollPane(txtaExceptionSummery), BorderLayout.CENTER);
            
            contentPane.add(pnlExceptionSummery, BorderLayout.CENTER);
            
            pnlButtonHolder.setLayout(new FlowLayout());
            btnView.addActionListener(this);
            pnlButtonHolder.add(btnView);
            btnSend.addActionListener(this);
            pnlButtonHolder.add(btnSend);
            btnDn_Send.addActionListener(this);
            pnlButtonHolder.add(btnDn_Send);
            contentPane.add(pnlButtonHolder, BorderLayout.SOUTH);
             
            createExceptionSummery(); 
             
            this.setSize(470, 300);
            this.setCenterScreen(); 
            this.setVisible(true);
            MDS.getSound().playSound(new File("Media\\Sound\\Beep_Exception.wav"));
                                          
        }
        
        
        
        private void createExceptionSummery() {
        	txtaExceptionSummery.append("Exception\n");
        	txtaExceptionSummery.append("---------------\n");
            txtaExceptionSummery.append("Class Name : "+stElement[0].getClassName()+"\n");     
            txtaExceptionSummery.append("File Name : "+stElement[0].getFileName()+"\n"); 
            txtaExceptionSummery.append("Line Number : "+String.valueOf(stElement[0].getLineNumber())+"\n");
            txtaExceptionSummery.append("Method Name : "+stElement[0].getMethodName()+"\n");
            txtaExceptionSummery.append("Exception : "+exception.toString()+"\n\n");
            txtaExceptionSummery.append("Thread\n");
            txtaExceptionSummery.append("---------------\n");
        	txtaExceptionSummery.append("Thread Id : "+thread.getId()+"\n");   
        	txtaExceptionSummery.append("Thread Name : "+thread.getName()+"\n");  
        	txtaExceptionSummery.append("Thread Priority : "+thread.getPriority()+"");      
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("View Stack Trace")) {
                MDS_Dialog dlg = new MDS_Dialog(this, "Stack Trace");
                MDS.getBaseWindow().getDesktop().remove(dlg); 
                MDS.getBaseWindow().getDesktop().add(dlg ,JLayeredPane.MODAL_LAYER);                     
                MDS_TextArea txta = new MDS_TextArea();
                txta.setBackground(Color.black);
                txta.setForeground(new Color(250, 175, 3));
                txta.setEditable(false);
                dlg.getContentPane().setLayout(new BorderLayout());
                dlg.getContentPane().add(new JScrollPane(txta), BorderLayout.CENTER);
                for(int count = 0;count<stElement.length;count++) {
                    txta.append(stElement[count].toString()+"\n");        
                }                    
                dlg.setSize(480, 400);
                dlg.setCenterScreen();
                dlg.setResizable(true);
                dlg.setVisible(true);
            } else if(e.getActionCommand().equals("Don't Send")) {
                this.dispose();
            } else if(e.getActionCommand().equals("Send Exception Report")) {
                
            }
        }
        
    }    
    
    
    
}    