
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.plaf.*;
import java.io.*;


public class MaheshTaskBar extends MDS_Window implements SystemCycleThreadListener, SystemSchedulerThreadListener
{
	
	
	
	Graphics2D g2d;
	private TaskBarPopupMenu tbpm = new TaskBarPopupMenu();
    private AppTitleViewer aptv = new AppTitleViewer(tbpm);
    private Mouse mouse = new Mouse();
    private SystemTray sty = new SystemTray(tbpm);
    private Clock clock = new Clock(tbpm);
    
    private TaskBarStartMenu tsMenu = new TaskBarStartMenu();
	
	
	
	public MaheshTaskBar() 
	{
		super(JLayeredPane.POPUP_LAYER);
		this.setSize(1024, 55);
		this.setLocation(0, 713);
		this.setLayout(new BorderLayout());
		this.add(new QuickLauncher(), BorderLayout.WEST); 
		this.add(aptv, BorderLayout.CENTER);
		this.add(new SystemTrayClock(), BorderLayout.EAST);	
		this.setOpaque(false);
		this.setVisible(true);
        //MDS.getSystemCycleThread().addSystemCycleThreadListener(this, "TaskBar");
        VirtualThreading.create_SCT_VT(this, "TaskBar");
        //MDS.getSystemSchedulerThread().addSystemSchedulerThreadListener(this, "Task Bar Clock");
        VirtualThreading.create_SST_VT(this, "TaskBar Clock");		
	}
	
	
	public boolean isStartMenuVisible() {;
        return tsMenu.isVisible();
    }
    
    
    
    public boolean isAnyAppTitleButtonPopupMenuVisible() {   
        return aptv.getPopupMenuVisibility();
    } 
    
    
    
    public boolean isTaskBarPopupMenuVisible() {
        return tbpm.getTaskBarPopupMenuVisibility();
    }      
	
	
	
	public long getSystemCycle_EventInterval() {
        return 100;
    }
    
    
    
    public void autoExecuteSubRoutine() {
		
        aptv.refreshAppTitleViewer();
		
        if(mouse.getMousePointerPosY()>= 763) {
            //this.setSize(800, 55);   
            //this.setLocation(0, 545); 
            this.setSize(1024, 55);
            this.setLocation(0, 713);
			this.validate();
        } else if(mouse.getMousePointerPosY()<= 708) {
            if(!isStartMenuVisible() && !isAnyAppTitleButtonPopupMenuVisible() && !isTaskBarPopupMenuVisible()) {
                //this.setSize(800, 5); 
                //this.setLocation(0, 595);
                this.setSize(1024, 5);
                this.setLocation(0, 763);           
            }
        }
    }  
    
    
    
    public long getSystemScheduler_EventInterval() {
        return 1000;
    }
    


    public void systemSchedulerEvent() {
        //clock.updateTime();            
    } 
	
	
	
	public void paintComponent(Graphics g) {
     	super.paintComponent(g);
		g2d = (Graphics2D)g;
		drawWindow();
		//createShortcuts();
		g2d.setColor(Color.white);
		//g2d.drawString(" WASANA WASANA WASANA WASANA WASANA WASANA WASANA WASANA",20,20);
	}
	
	
	
	private void drawWindow() 
	{
		Color c1 = new Color(255, 255, 255, 60);
		g2d.setColor(c1);
		g2d.fill3DRect(0, 0, 1024, 55, true);
		//bw.getDesktop().paintImmediately(249,-1,501,71);
	}	
	
	
	
	public static void MDS_Main(String[] a) 
	{
		new MaheshTaskBar();
	}
	
	
	
	
	
	private class QuickLauncher  extends MDS_Panel implements MouseListener
	{
		
		
		
		QL_Icon qiStart = new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-mds.png"));
		QL_Icon qiCommander = new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-rdso-1.png"));
		QL_Icon qiShowDesktop = new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-desktop.png"));
		QL_Icon qiFileManager = new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-filesys-folder_html.png"));
		
		
		public QuickLauncher()
		{
			this.setLayout(new GridLayout(0, 4));
			this.setPreferredSize(new Dimension(210, 58));
			this.setOpaque(false);
			this.add(qiStart);
			qiStart.addMouseListener(this);
			this.add(qiCommander);
			qiCommander.addMouseListener(this);
			this.add(qiShowDesktop);
			qiShowDesktop.addMouseListener(this);
			this.add(qiFileManager);
			qiFileManager.addMouseListener(this);			
		}
		
		
		
	 	public void mouseClicked(MouseEvent e) 
		{
			ProcessManager prm = MDS.getProcessManager();
			
			if(e.getSource() == qiStart) {
				tsMenu.show();	
			} else if(e.getSource() == qiCommander) {
				prm.execute(new File(MDS.getBinaryPath(), "Commander.class"));
			} else if(e.getSource() == qiShowDesktop) {
		        MDS_UIManager uim = MDS.getMDS_UIManager();
		        Vector vctFrames = uim.getAllFrames();
		        for(int x=0;x<=vctFrames.size()-1;x++) {
		            MDS_Frame frm = (MDS_Frame)vctFrames.elementAt(x);
		            try {
		            	frm.setIcon(true);
		            } catch(Exception ex) {}
		        }				
			} else if(e.getSource() == qiFileManager) {
				prm.execute(new File(MDS.getBinaryPath(), "MatrixFileBrowser.class"));
			}		
		}
		
		
		
		public void mouseEntered(MouseEvent e) 
		{
		}
		
		
		
		public void mouseExited(MouseEvent e) 
		{
		}
		
		
		
		public void mousePressed(MouseEvent e) 
		{
		}	
		
		
		
		public void mouseReleased(MouseEvent e)  
		{
		}		
		
		
		
		
	}
	
	
	
	
	
	class QL_Icon extends MDS_Panel implements MouseListener
	{
	
	
	    
		private int ilf = 0;
		private static final int alfa = 1; 
		private static final int wrap = 2;
		private static final int normal = 3;
		private Image icon;
		//private IconToolTipText toolTipText = new IconToolTipText("MAHESHXXXXXXXXXXXXXXx");
	
	
	
		public QL_Icon(Image i) 
		{
			icon = i;
			//toolTipText.setLocation(300, 300);
			ilf = normal;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.setOpaque(false);
			this.addMouseListener(this);
			Random r = new Random();
			this.setBackground(new Color(Math.abs(r.nextInt()%250),Math.abs(r.nextInt()%250),Math.abs(r.nextInt()%250)));
		}
		
		
	 	public void paintComponent(Graphics g) {
    	 	super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			float alpha;
			AlphaComposite ac;
			switch (ilf) 
			{
			case alfa:
        		alpha = 0.29999996f;
        		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        		g2d.setComposite(ac);   			
				g2d.drawImage(icon , 5, 5, null);
				break;
			case wrap:
			
				break;
			case normal:
        		alpha = .99f;
        		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        		g2d.setComposite(ac); 						
				g2d.drawImage(icon , 5, 5, null);
				break;								
				
			}
			
	 	}
	 	
	 	
	 	
	 	public void mouseClicked(MouseEvent e) 
		{
			ilf = wrap;
		}
		
		
		
		public void mouseEntered(MouseEvent e) 
		{
			ilf = alfa;
			this.repaint();
			//toolTipText.setVisible(true);
		}
		
		
		
		public void mouseExited(MouseEvent e) 
		{
			ilf = normal;
			this.repaint();
			//toolTipText.setVisible(false);
		}
		
		
		
		public void mousePressed(MouseEvent e) 
		{
		}	
		
		
		
		public void mouseReleased(MouseEvent e)  
		{
		}
	}
	
	
	
	
	
    class AppTitleViewer extends MDS_Panel implements MouseListener {
    
    
    
        Vector vctFrames;
        Vector vctFrameIds = new Vector();
        Vector vctCurrentFrameIds = new Vector();
        Vector vctAppTitleButtonList = new Vector();
        MDS_Frame frame;
        ButtonGroup btg = new ButtonGroup();
        MDS_Panel pnlHolder_1 = new MDS_Panel(new GridLayout(1,0));
        MDS_Panel pnlHolder_2 = new MDS_Panel(new GridLayout(1,0));
        boolean addToTop = true;
        AppTitleViewer aptv;
        TaskBarPopupMenu tbPopupMenu;
        
    
    
        public AppTitleViewer(TaskBarPopupMenu tbpm) {
            tbPopupMenu = tbpm;
            aptv = this;
            //this.setBorder(BorderFactory.createEtchedBorder());
            //this.setLocation(183, 3);
            //this.setSize(500, 48);
            //this.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 3));
            
            this.setPreferredSize(new Dimension(600, 58));
            
            this.setOpaque(false);
            
            this.setLayout(new GridLayout(2,0)); 
            this.addMouseListener(this);   
            this.add(pnlHolder_1);     
            pnlHolder_1.setOpaque(false);	  
            pnlHolder_1.addMouseListener(this);
            this.add(pnlHolder_2);      
            pnlHolder_2.setOpaque(false);
            pnlHolder_2.addMouseListener(this);                        
        }
        
        
        
        public boolean getPopupMenuVisibility() {
            boolean visibility = false;
            AppTitleButton button;
            for(int x=0;x<=vctAppTitleButtonList.size()-1;x++) {
                button = (AppTitleButton)vctAppTitleButtonList.elementAt(x);  
                if(button.isPopupVisible()) {
                    visibility = true;
                }      
            }

            return visibility;
            
        }
        
        
        
        public void refreshAppTitleViewer() {
        
            try{  
                 
                vctFrames = MDS.getMDS_UIManager().getAllFrames();
  
                for(int x=0;x<=vctFrames.size()-1;x++) {
                    frame = (MDS_Frame) vctFrames.get(x);
                    if(!vctFrameIds.contains(new Integer(frame.getFrameId()))) {
                        vctFrameIds.addElement(new Integer(frame.getFrameId()));
                        if(frame.isFrameTitle_Visible_In_TaskBar() == true)
                            createAppTitleButton(frame.getTitle(), frame);
                    }
  
                    vctCurrentFrameIds.addElement(new Integer(frame.getFrameId()));
        //        
                }

                for(int x=0;x<=vctFrameIds.size()-1;x++) {
                    if(!vctCurrentFrameIds.contains(vctFrameIds.elementAt(x))) {
                        removeAppTitleButton(new Integer(String.valueOf(vctFrameIds.elementAt(x))));
                        vctFrameIds.remove(x);
                    }             
                }            
 
                vctCurrentFrameIds.removeAllElements();
                
                for(int x=0;x<=vctAppTitleButtonList.size()-1;x++) {
                    MDS_Frame frm = ((AppTitleButton)vctAppTitleButtonList.elementAt(x)).getLinkedFrame();
                    AppTitleButton atb = (AppTitleButton)vctAppTitleButtonList.elementAt(x); 
                    if(frm.isSelected()) {
                        if(!atb.isSelected()) atb.setSelected(true);   
                    }
                }
                
                } catch(Exception ex) {
                    //System.out.println("refreshAppTitleViewer() ");
                    //System.out.println(ex.toString());
                }      
            
        }
        
        
        
        private void createAppTitleButton(String text, MDS_Frame frame) {
            AppTitleButton aptb = new AppTitleButton(text, frame);
            vctAppTitleButtonList.addElement(aptb);
            
            //btg.add(aptb);
            
            
            if(addToTop) {
                pnlHolder_1.add(aptb);
                addToTop=false;
            } else {
                pnlHolder_2.add(aptb);
                addToTop = true;
            }    
        }
        
        
        
        private void removeAppTitleButton(Integer frameId) {
        
            AppTitleButton removalButton;
            Integer removalFrameId;
             
            for(int x=0;x<=vctAppTitleButtonList.size()-1;x++) {
                removalButton = (AppTitleButton)vctAppTitleButtonList.elementAt(x);
                removalFrameId = removalButton.getLinkedFrameId();
                if(removalFrameId.equals(frameId)) {
                    pnlHolder_1.remove(removalButton);
                    pnlHolder_2.remove(removalButton);
                    this.repaint();
                    vctAppTitleButtonList.removeElementAt(x);
                } 
            }
            
        } 
        
        
        
        public void mouseClicked(MouseEvent e) {	               
            if(e.getButton()==e.BUTTON3) {
                if(e.getSource() == this) {
                    tbPopupMenu.showPopupMenu(this, e.getX(), e.getY());
                } else if(e.getSource() == pnlHolder_1) {
                    tbPopupMenu.showPopupMenu(pnlHolder_1, e.getX(), e.getY());
                } else if(e.getSource() == pnlHolder_2) {
                    tbPopupMenu.showPopupMenu(pnlHolder_2, e.getX(), e.getY()); 
                } 
            }    
        }
            
            
            
        public void mouseEntered(MouseEvent e) {}
            
            
            
        public void mouseExited(MouseEvent e) {}
            
            
            
        public void mousePressed(MouseEvent e) {}
            
            
            
        public void mouseReleased(MouseEvent e) {}          
        
        
        
        
        
        class AppTitleButton extends MDS_ToggleButton implements MouseListener, ItemListener, ActionListener  {
        
        
        
            Integer frameId;
            MDS_Frame frame;
            MDS_PopupMenu aptbPopupMenu;
            JMenuItem mniRestore;
            JMenuItem mniMinimize;
            JMenuItem mniMaximize;
            JMenuItem mniClose;
            
            
        
            public AppTitleButton(String appName, MDS_Frame f) {
                super(appName);
                frame = f;
                
                //this.setOpaque(false);
                
                this.setBackground(Color.red);
                
                this.setBorderPainted(false);
                
                this.setFocusPainted(false);
                
                this.setContentAreaFilled(false);
                  
                
                this.setFont(new Font("Dialog", Font.BOLD, 14));
                this.setIcon(frame.getFrameIcon());
                this.setSize(60,20);
                this.setHorizontalAlignment(SwingConstants.LEFT );
                this.setVerticalAlignment(SwingConstants.CENTER);
                aptbPopupMenu = new MDS_PopupMenu();
                mniRestore = createMenuItem("Restore");
                aptbPopupMenu.add(mniRestore);
                mniMinimize = createMenuItem("Minimize");
                aptbPopupMenu.add(mniMinimize);
                mniMaximize = createMenuItem("Maximize");
                aptbPopupMenu.add(mniMaximize);
                aptbPopupMenu.addSeparator();
                mniClose = createMenuItem("Close");
                aptbPopupMenu.add(mniClose);
                this.addMouseListener(this);
                this.addItemListener(this);
                this.setToolTipText(frame.getTitle()); 
            }
            
            
            
            public boolean isPopupVisible() {
                //mniMinimize.setEnabled(frame.isIconifiable());
                //mniMaximize.setEnabled(frame.isMaximizable());
                //mniClose.setEnabled(frame.isClosable());
                return aptbPopupMenu.isVisible();
            }
            
            
            
            private JMenuItem createMenuItem(String text) {
                 JMenuItem mni = new JMenuItem(text);
                 mni.addActionListener(this); 
                 return mni;    
            }
            
            
            
            public Integer getLinkedFrameId() {
                return new Integer(frame.getFrameId());
            }
            
            
            
            public MDS_Frame getLinkedFrame() {
                return frame;
            }
            
            
            
            public void mouseClicked(MouseEvent e) {
                try{
                    if(e.getButton()==e.BUTTON1) {
                        if(!frame.isSelected() || frame.isIcon()) { 
                            frame.setIcon(false);                           
                            frame.setSelected(true);
	                      } else if(frame.isSelected() && !frame.isIcon()) {
	                          if(frame.isIconifiable()) {
	                              frame.setIcon(true);
	                          }
	                      } 
	                                  	        
                    } else if(e.getButton()==e.BUTTON3) {
                        this.setSelected(true);
                        if(!frame.isIcon()) {
                            mniRestore.setEnabled(false);      
                        } else {
                            mniRestore.setEnabled(true); 
                        }
                        
                        if(frame.isMaximum()) {
                            mniMaximize.setEnabled(false);
                            mniRestore.setEnabled(true);
                        } else {
                            mniMaximize.setEnabled(true);
                            mniRestore.setEnabled(false);                       
                        }
                        
                         if(frame.isIcon()) {
                            mniMinimize.setEnabled(false);
                            mniRestore.setEnabled(true);
                        } else {
                            mniMinimize.setEnabled(true);
                        } 
                        
                        if(!frame.isIcon())
                        mniMinimize.setEnabled(frame.isIconifiable());
                        
                        if(!frame.isMaximum())
                        mniMaximize.setEnabled(frame.isMaximizable());
                        
                        mniClose.setEnabled(frame.isClosable());                                              
                        
                        if(!frame.isSelected()) frame.setSelected(true);
                        
                        aptbPopupMenu.show(this,e.getX(), e.getY());
                    } 
                } catch(Exception ex) {}       
            }
            
            
            
            public void mouseEntered(MouseEvent e) {}
            
            
            
            public void mouseExited(MouseEvent e) {}
            
            
            
            public void mousePressed(MouseEvent e) {}
            
            
            
            public void mouseReleased(MouseEvent e) {}
            
            
            
            public void actionPerformed(ActionEvent e) {          
                try{       
                 
                    if( e.getActionCommand().equals("Close")){
                        frame.dispose();
                    } 
                    if(e.getActionCommand().equals("Maximize")) {
                        frame.setMaximum(true);
                    }
                    if(e.getActionCommand().equals("Minimize")) {
                        frame.setIcon(true);
                    }
                    if(e.getActionCommand().equals("Restore")) {
                        frame.setIcon(false);
                        frame.setMaximum(false);
                    }               
                
                }catch(Exception ex) {}                 
            }
            
            
            
            public void itemStateChanged(ItemEvent e) {
               // aptbPopupMenu.show(this,0,0);
            }
            
            
            
        }   
        
        
        
    } 	
	
	
	
	
	
	class TaskBarPopupMenu implements ActionListener {
    
    
    
        MDS_PopupMenu taskBarPopupMenu;
    
    
    
        public TaskBarPopupMenu() {
             taskBarPopupMenu = new MDS_PopupMenu();
             CreatePopupMenuItem("Cascade Windows");
             taskBarPopupMenu.addSeparator();
             CreatePopupMenuItem("System Manager");
             CreatePopupMenuItem("Properties");
        }      
        
        
        
        public void showPopupMenu(Component invoker , int x, int y) {
            taskBarPopupMenu.show(invoker, x, y);
        }
        
        
        
        private void CreatePopupMenuItem(String text) {
            JMenuItem mni = new JMenuItem(text);
            mni.addActionListener(this);
            taskBarPopupMenu.add(mni);
        }
        
        
        
        public boolean getTaskBarPopupMenuVisibility() {
            return taskBarPopupMenu.isVisible();
        } 
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Properties")) {
                //new TaskBarPropertyPanel (false);
                throw new NullPointerException();
                //MDS.getExceptionManager().showException(new NullPointerException()); 
            } else if(e.getActionCommand().equals("System Manager")) {
                ProcessManager prm = MDS.getProcessManager();
                prm.execute(new File(MDS.getBinaryPath(),"SystemManager"));            
            }
        
        }
    }
    
    
    
    
    
 	class SystemTray extends MDS_Panel implements MouseListener {
    
    
    
        MDS_Label lblSound = new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-action-equalizer.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        MDS_Label lblDisplay = new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-app-samba.png", 16, 16, ImageManipulator.ICON_SCALE_TYPE));
        TaskBarPopupMenu tbPopupMenu;
        
        
    
        public SystemTray(TaskBarPopupMenu tbpm) {
            tbPopupMenu = tbpm;
            this.setOpaque(false);
            //this.setBorder(BorderFactory.createEtchedBorder());
            this.setLayout(new GridLayout(2,0));
            lblSound.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.addMouseListener(this);
            lblSound.addMouseListener(this);
            this.add(lblSound);
            lblDisplay.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblDisplay.addMouseListener(this);
            this.add(lblDisplay);
            //this.setLocation(684, 3);
            //this.setSize(42, 48);  
			this.setPreferredSize(new Dimension(42, 48));      
        }
        
        
        
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()==e.BUTTON1) {
                ProcessManager prm = MDS.getProcessManager();
                if(e.getSource() == lblSound) {
                     //prm.execute("SoundProperties");
                     prm.execute(new File(MDS.getBinaryPath(), "SoundProperties.class"));
                 } else if(e.getSource() == lblDisplay) {
                     //prm.execute("DisplayProperties"); 
                     prm.execute(new File(MDS.getBinaryPath(), "DisplayProperties.class"));
                 }        
            } else if(e.getButton()==e.BUTTON3) {
                if(e.getSource() == this) {
                    tbPopupMenu.showPopupMenu(this, e.getX(), e.getY());     
                } else if(e.getSource() == lblSound) {
                    tbPopupMenu.showPopupMenu(lblSound, e.getX(), e.getY()); 
                } else if(e.getSource() == lblDisplay) {
                    tbPopupMenu.showPopupMenu(lblDisplay, e.getX(), e.getY()); 
                }
            }
        }
            
            
            
        public void mouseEntered(MouseEvent e) {}
            
            
            
        public void mouseExited(MouseEvent e) {}
            
            
            
        public void mousePressed(MouseEvent e) {}
            
            
            
        public void mouseReleased(MouseEvent e) {}        
        
        
        
    }  
    	
    	
    	
    	
    	
    class Clock extends MDS_Panel implements MouseListener {
    
    
    
        TaskBarPopupMenu tbpm;
    
    
        
        public Clock(TaskBarPopupMenu tbpMenu) {
        	this.setOpaque(false);
            tbpm = tbpMenu;
            this.setLayout(null);  
            //this.setSize(70,48);
			this.setPreferredSize(new Dimension(70, 48));
            //this.setSize(50,50);
            this.setLocation(728,3);
            //this.setBackground(Color.red);
            //this.setBorder(BorderFactory.createEtchedBorder());
            this.addMouseListener(this);
        }
        
        
        
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 =(Graphics2D)g;
            
            g2.setRenderingHints(MDS.getMDS_Graphics().getRenderingHints());
            
            drawDigitalClock(g2);
            //drawTraditionalClock(g2);
            //drawMiniDigitalClock(g2);
                        
        }
        
        
        
        private void drawDigitalClock(Graphics2D g2) {
        
            Calendar calendar = new GregorianCalendar();
            g2.setFont(new Font("Impact", Font.PLAIN, 20));          
            String hours = String.valueOf(calendar.get(Calendar.HOUR));
            String minutes = String.valueOf(calendar.get(Calendar.MINUTE)); 
            
            g2.drawString(hours  ,5,20); 
            
            g2.drawString(":",28, 17);  
            
            g2.drawString(minutes,38,20); 
            
            g2.setFont(new Font("Impact", Font.PLAIN, 15));
            
            String d = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String m = String.valueOf(calendar.get(Calendar.MONTH));
            String y = String.valueOf(calendar.get(Calendar.YEAR));
            String date = String.valueOf(d)+" : "+String.valueOf(m)+" : "+String.valueOf(y).substring(2);
                        
            g2.drawString(date,5,40);
                    
        }
        
        
        
        private void drawMiniDigitalClock(Graphics2D g2) {
        
            Calendar calendar = new GregorianCalendar();       
        
            String hours = String.valueOf(calendar.get(Calendar.HOUR));
            String minutes = String.valueOf(calendar.get(Calendar.MINUTE)); 
            
            g2.drawString(hours  ,5,17); 
            
            g2.drawString(":", 20, 16);
            
            g2.drawString(minutes, 29, 17);
 
            String d = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String m = String.valueOf(calendar.get(Calendar.MONTH));
            String date = String.valueOf(d)+" : "+String.valueOf(m);
                        
            g2.drawString(date,5,38); 
                                      
        }
        
        
        
        private void drawTraditionalClock(Graphics2D g2) {
        
            Calendar calendar = new GregorianCalendar();
            String hours = String.valueOf(calendar.get(Calendar.HOUR));
            g2.drawOval(3, 3, 43, 43);
            
            g2.drawLine(25, 25, 50, 3);
            
        }
        
        
   
        public void updateTime() {
            repaint();
        }
        
        
        
        public void mouseClicked(MouseEvent e) {	               
            if(e.getButton()==e.BUTTON3) {
                tbpm.showPopupMenu(this,e.getX(), e.getY());
            }    
        }
            
            
            
        public void mouseEntered(MouseEvent e) {}
            
            
            
        public void mouseExited(MouseEvent e) {}
            
            
            
        public void mousePressed(MouseEvent e) {}
            
            
            
        public void mouseReleased(MouseEvent e) {}            
        
        
        
    }    	
    	
    	
    	
    	
    	
	private class SystemTrayClock extends MDS_Panel 
	{
	
	
	
	    public SystemTrayClock() 
	    {
	    	this.setOpaque(false);
			this.setLayout(new BorderLayout());
			this.add(sty, BorderLayout.WEST);
			this.add(clock, BorderLayout.CENTER);
	    }
		
	
	}    	
	
	
	
}
