/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.plaf.metal.*;



public final class MDS_UIManager {



    public static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    public static final String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
    public static final String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    public static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    private static MDS_UIManager uim = new MDS_UIManager(MDS.getBaseWindow());
    BaseWindow bw;

    Vector vctContainerIds = new Vector();
    
    MDS_PropertiesManager prop = MDS.getMDS_PropertiesManager();
    
    
    
    private MDS_UIManager(BaseWindow baseWindow) {
        bw = baseWindow;
        //if(baseWindow == null) Console.println("** NULL **");
        
        //setLaF(windows);
        
        JDialog.setDefaultLookAndFeelDecorated(true); 
        	
        	
    }
    
    
    
    public static MDS_UIManager getMDS_UIManager() {
        return uim;
    }
    
    
    
    public void addFrame(MDS_Frame frame) {
        bw.getDesktop().add(frame);

        //vctFrames.addElement(frame);
    }
    
    
    
    public void removeFrame(MDS_Frame frame) {
        bw.getDesktop().remove(frame);
        
        //vctFrames.addElement(frame);
    }
    
    
    
    public Vector getAllFrames() {

        Vector vctFrames = new Vector();
        JInternalFrame[] frames = bw.getDesktop().getAllFrames();
      
        for(int x= 0; x < frames.length; x++) {
            if(frames[x] instanceof MDS_Frame) {
                vctFrames.addElement(frames[x]);
            }
        }
        
        return vctFrames;
        
    }
    
    
    
    public JInternalFrame getFrame(int id) {
        JInternalFrame[] frames = bw.getDesktop().getAllFrames();
        JInternalFrame frm = null;
        for(int x= 0; x < frames.length; x++) {
            if(((MDS_Frame)frames[x]).getFrameId() == id) {
                frm = frames[x];
            }    
        }    
        return frm;
    }
    
    
    
    public int getContainerId() {
        return createIdentifier();
    }
    
    
    
    private int createIdentifier() {
        boolean doIt = true;
        int idntifier = 0;
        Random rand = new Random();
        Integer temp = new Integer(0);
        while(doIt) {
            if(!vctContainerIds.contains(temp = new Integer(Math.abs(rand.nextInt())))) {
                doIt = false;
            }
        }
        return idntifier = temp.intValue();            
    }  
    
    
    
    public synchronized static void halt_While_Visible(JInternalFrame f) {
        try{
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
                while (f.isVisible()) {
                    // This is essentially the body of EventDispatchThread
                    AWTEvent event = theQueue.getNextEvent();
                    Object src = event.getSource();
                    // can't call theQueue.dispatchEvent, so I pasted its body here
                    if (event instanceof ActiveEvent) {
                        ((ActiveEvent) event).dispatch();
                    } else if (src instanceof Component) {
                        ((Component) src).dispatchEvent(event);
                    } else if (src instanceof MenuComponent) {
                        ((MenuComponent) src).dispatchEvent(event);
                    } else {
                        System.err.println("unable to dispatch event: " + event);
                    }
                }
            } else        
                while (f.isVisible())
                    f.wait(); 
        } catch(InterruptedException e){}  
             
    }         
    
       
    
    
    public static void setComponentTreeEnabled(boolean b, Container c) {

        Component components[] = c.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component leafComponent = null;
            leafComponent = components[i];
            if (((Container)leafComponent).getComponentCount() != 0) {
                leafComponent.setEnabled(b);
                setComponentTreeEnabled(b,(Container)leafComponent);              
            } else {
                leafComponent.setEnabled(b);
            }
        }     
                    
    } 
    
    
    
    public static void setMenuBarEnabled(boolean b, MDS_Frame f) {
        JMenuBar mb = f.getJMenuBar();
        if(mb != null) {
            for (int i = 0; i < mb.getMenuCount(); i++) {
                mb.getMenu(i).setEnabled(b);
            }    
        }
    }
    
    
    
    public static void setChildFrameLayer_EqualTo_Parent(MDS_Frame child, MDS_Frame parent) {
 
        Integer parentLayer = new Integer(parent.getLayer());
        MDS.getBaseWindow().getDesktop().remove(child);
        MDS.getBaseWindow().getDesktop().add(child , parentLayer);        
        
    }
    
    
    
    public static void setComponentTreeCursor(Container c, Cursor cursor) {
        Component components[] = c.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component leafComponent = null;
            leafComponent = components[i];
            if (((Container)leafComponent).getComponentCount() != 0) {
                leafComponent.setCursor(cursor);
                setComponentTreeCursor((Container)leafComponent, cursor);              
            } else {
                leafComponent.setCursor(cursor);
            }
        }    
    }   
    
    
    
    public static void setFrameCursor(MDS_Frame f, Cursor cursor) {
        JMenuBar mb = f.getJMenuBar();
        if(mb != null) {
            for (int i = 0; i < mb.getMenuCount(); i++) {
                mb.getMenu(i).setCursor(cursor);
            }
            mb.setCursor(cursor);   
        }    
        
        setComponentTreeCursor(f.getContentPane(), cursor);
        
    }
    
    
    
    public static void setTheme(String name) {
        if(name.equals("MDS Default")) {
////            MetalLookAndFeel.setCurrentTheme(new MDS_DefaultTheme());    
        }                       
        if(name.equals("Steel")) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());     
        }
        if(name.equals("Emerald")) {
            MetalLookAndFeel.setCurrentTheme(new EmeraldTheme());     		    
        }   
        if(name.equals("Oxide")) {
            MetalLookAndFeel.setCurrentTheme(new AquaMetalTheme());    
        }
        if(name.equals("Sandstone")) {
            MetalLookAndFeel.setCurrentTheme(new KhakiMetalTheme());       
        }
        if(name.equals("Ruby")) {
            MetalLookAndFeel.setCurrentTheme(new RubyTheme());                
        }
        if(name.equals("High Contrast")) {
            MetalLookAndFeel.setCurrentTheme(new ContrastMetalTheme());           
        }
        if(name.equals("Charcol")) {
            MetalLookAndFeel.setCurrentTheme(new CharcoalTheme());      
        } 
        if(name.equals("OceanTheme")) {
        	MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }
        
        MDS_UIManager.setLaF(metal);
        	   
    }  
    
    
    
    public static void setLaF(String LaF) {
        try {
        	//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(LaF);        
            //SwingUtilities.updateComponentTreeUI(dpnThemePreview);
            SwingUtilities.updateComponentTreeUI(MDS.getBaseWindow());
            MDS.getBaseWindow().setTaskBarDividerSize_To_Default();
        } catch(Exception ex) {
            //Console.printException(ex, this);
        }  
    }       



}