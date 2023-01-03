
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;


public class QuickLauncher extends MDS_Window implements SystemCycleThreadListener, MouseListener
{


	Graphics2D g2d;
	BaseWindow bw = MDS.getBaseWindow();
	Mouse mouse = new Mouse();
	
	boolean alwaysShow = false;
	int xa = 0;
	int ya = 1;
	


	public QuickLauncher() 
	{
		super(JLayeredPane.POPUP_LAYER);
		
		this.addMouseListener(this);
		
		this.setLayout(new GridLayout(0, 9));
		this.setSize(540, 60);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (d.getWidth()/2) - (this.getWidth()/2);
		xa = x;  
		this.setLocation(x, 1);
		this.setOpaque(false);
		this.setBackground(Color.red);
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-app-kfm_home.png"), "MatrixFileBrowser.class"));
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-app-phppg.png"), "Maximus.class"));
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-action-kopeteavailable.png"), "Messenger.class"));
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-app-package_network.png"), "WebBrowser.class"));
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-mime-gf.png"), "ApacheTomcatServerLauncher.class"));
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-app-ksplash.png"), "JavaClassBrowser.class"));
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-device-blockdevice.png"), "ClassConnectionWalker.class"));
		this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-app-kaboodle.png"), "MediaPlayer.class"));
        this.add(new QL_Icon(ImageManipulator.getImage(ImageManipulator.MDS_ICONS_PATH+"48-device-system.png"), "MatrixFileBrowser.class"));
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
		VirtualThreading.create_SCT_VT(this, "QuickLauncher");
		this.setVisible(true);
	
	} 
	
	
	
	public long getSystemCycle_EventInterval() {
        return 250;
    }
    


    public void autoExecuteSubRoutine() {
        MDS_UIManager uim = MDS.getMDS_UIManager();
        Vector vctFrames = uim.getAllFrames();
		boolean show = true;
		
		if(mouse.getMousePointerPosY()<2) {
			alwaysShow = true;
			show = true;
		} else {
			if(!alwaysShow) {
		        for(int x=0;x<=vctFrames.size()-1;x++) {
		            MDS_Frame frm = (MDS_Frame)vctFrames.elementAt(x);
					if (frm.getY()<65) {
						show = false;
						break;
						//this.setSize(540, 2);
					} else {
						show = true;
						//this.setSize(540, 60);
						//this.validate();
					}
		        }
			}
		}	      
		
		if(show) {
			this.setSize(540, 60);
			this.validate();			
		} else {
			this.setSize(540, 2);
		}
		
		if(mouse.getMousePointerPosX()<xa) {
			alwaysShow = false;
		} else if(mouse.getMousePointerPosX()>xa+540) { 
			alwaysShow = false;
		} else if(mouse.getMousePointerPosY()> 61) {
			alwaysShow = false;
		}
		
		        
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
		Color c1 = new Color(255, 255, 255, 40);
		g2d.setColor(c1);
		g2d.fill3DRect(0,0,540,60,true);
		//bw.getDesktop().paintImmediately(249,-1,501,71);
	}
	
	
	
	public static void MDS_Main(String[] a) 
	{
		new QuickLauncher();
	}
	
	
	
	public void mouseClicked(MouseEvent e) {System.out.println("Exit");}
	
	
	
	public void mouseEntered(MouseEvent e) {System.out.println("Exit");}
	
	
	
	public void mouseExited(MouseEvent e) {
		alwaysShow = false;
		System.out.println("Exit");
	}
	
	
	
	public void mousePressed(MouseEvent e) {System.out.println("Exit");}	
	
	
	
	public void mouseReleased(MouseEvent e) {System.out.println("Exit");}	
	
	
	
	
	
	
	
	
	
	class QL_Icon extends MDS_Panel implements MouseListener
	{
	
	
	    
		private int ilf = 0;
		private static final int alfa = 1; 
		private static final int wrap = 2;
		private static final int normal = 3;
		private Image icon;
		private IconToolTipText toolTipText = new IconToolTipText("MAHESHXXXXXXXXXXXXXXx");
		private File execFilePath;
	
	
		public QL_Icon(Image i, File f) 
		{
			execFilePath = f;
			icon = i;
			toolTipText.setLocation(300, 300);
			ilf = normal;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.setOpaque(false);
			this.addMouseListener(this);
			Random r = new Random();
			this.setBackground(new Color(Math.abs(r.nextInt()%250),Math.abs(r.nextInt()%250),Math.abs(r.nextInt()%250)));
		}
		
		public QL_Icon(Image i, String className) {
			this(i, new File(MDS.getBinaryPath(), className));
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
			System.out.println("ApacheTomcatServerLauncher.class");
			ProcessManager prm = MDS.getProcessManager();
			prm.execute(execFilePath);	
		}
		
		
		
		public void mouseEntered(MouseEvent e) 
		{
			ilf = alfa;
			this.repaint();
			toolTipText.setVisible(true);
		}
		
		
		
		public void mouseExited(MouseEvent e) 
		{
			ilf = normal;
			this.repaint();
			toolTipText.setVisible(false);
		}
		
		
		
		public void mousePressed(MouseEvent e) 
		{			
		}	
		
		
		
		public void mouseReleased(MouseEvent e)  
		{
		}
		
		
		
		
		
		class IconToolTipText extends MDS_Window
		{
		
		
			private String text;
			private int width = 0;
		
			
			public IconToolTipText(String t) 
			{
				super(JLayeredPane.POPUP_LAYER);
				text = t;
				this.setSize(0, 0);
				//this.setOpaque(false);
				repaint();
			}
	
			
			
			
			public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;
				
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                                
				Font font = new Font("Arial", Font.BOLD, 20);				
                g2d.setFont(font);
				
				FontMetrics fm = g.getFontMetrics(font);
				width = fm.stringWidth(text);
				System.out.println(width);				
				
                FontRenderContext frc = g2d.getFontRenderContext();
                TextLayout tl = new TextLayout(text, g2d.getFont(), frc);
                Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(5, 20));
                g2d.setColor(Color.black);
                g2d.draw(sha);
                g2d.setColor(Color.white);
                g2d.fill(sha);    			
			}
		}
	}
}
  