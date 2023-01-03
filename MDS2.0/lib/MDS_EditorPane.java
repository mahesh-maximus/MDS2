/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.io.*;



public class MDS_EditorPane extends JEditorPane {



    public MDS_EditorPane() {
        super();
        initialize();
    }
    
    
    
    public MDS_EditorPane(String url) throws IOException {
        super(url);
        initialize();
    }
    
    
    
    public MDS_EditorPane(String type, String text) {
        super(type, text);
        initialize();
    }
    
    
    
    public MDS_EditorPane(URL initialPage) throws IOException {
        super(initialPage);
        initialize();
    }
    
    
    
    private void initialize() {
    
    }
	
	
/*	
	private void _setPage(URL page) throws Exception 
	{
		super.setPage(page);
	}
	
	
	
	public void setPage(URL page) 
	{
	
	
	System.out.println("DDDDDDDd");
		class LoadUrl extends Thread
		{
		
		
		
			URL url = null;
		
		
			public LoadUrl(URL u) 
			{
				super();
				url = u;
				this.start();
			}
			
			
			
			public void run() 
			{
				try 
				{
				//setPage(url);
				_setPage(url);
				System.out.println("DDDDDDDd");
				} catch(Exception ex) 
				{
					ex.printStackTrace();
				}
			}
			
		}
		
		new LoadUrl(page);
	}
	
	
	
	public void setPage(String url) throws MalformedURLException 
	{
	    setPage(new URL(url));
	}
    
*/    
    
}    