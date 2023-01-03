/**
 * @(#)JarFileCreator.java
 *
 *
 * @author 
 * @version 1.00 2007/10/6
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import java.util.jar.*;



public class JarFileCreator extends MDS_Frame {
	
	
	private ClassFiles cfs = new ClassFiles();
	private JarFile jf = new JarFile();
	
	private MDS_Frame frame;
	
	private FileManager fm = MDS.getFileManager();
	private ProcessManager pm = MDS.getProcessManager();
	

    public JarFileCreator() {
    	 super("jar File Creator",true, true, true, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"32-action-knewstuff.png")); 
    	 
    	 frame = this;
    	 
    	 this.setSize(700, 400);
    	 this.setCenterScreen();
    	 
    	 this.setLayout(new BorderLayout());
    	 
    	 this.add(cfs, BorderLayout.CENTER);
    	 this.add(jf, BorderLayout.SOUTH);
    	 this.setVisible(true);		
    }
    
    public static void MDS_Main(String a[]) {
    	new JarFileCreator();
    }
    
    
    
    
    
    private class ClassFiles extends MDS_Panel implements ActionListener {
    	
    	
    	
    	MDS_TableModel tbmlClassFiles = new MDS_TableModel();
    	JTable tblClassFiles = new JTable(tbmlClassFiles);
    	MDS_Panel pnl1 = new MDS_Panel(new GridLayout(3, 1));
    	MDS_Panel pnl2 = new MDS_Panel(new BorderLayout());
    	MDS_Button btnAdd = new MDS_Button("Add");
    	MDS_Button btnRemove = new MDS_Button("Remove");
    	MDS_Button btnAddAll = new MDS_Button("Add All");
    	
    	
    	
    	public ClassFiles() {
    		super();
    		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Select Class Files"));
    		tbmlClassFiles.addColumn("No");
    		tbmlClassFiles.addColumn("File Name");
    		tbmlClassFiles.addColumn("Path");
    		
    		tblClassFiles.getColumn("No").setWidth(20);
    		tblClassFiles.getColumn("File Name").setWidth(50);
    		
    		tblClassFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		
    		this.setLayout(new BorderLayout());
    		
    		this.add(new MDS_ScrollPane(tblClassFiles), BorderLayout.CENTER);
    		btnAdd.addActionListener(this);
    		pnl1.add(btnAdd);
    		btnRemove.addActionListener(this);
    		pnl1.add(btnRemove);
    		btnAddAll.addActionListener(this);
    		pnl1.add(btnAddAll);
    		
    		pnl2.add(pnl1, BorderLayout.NORTH);
    		this.add(pnl2, BorderLayout.EAST);
    		
    		
    	}
    	
    	
    	
    	public Vector getClassFiles() {
    		Vector v1 = tbmlClassFiles.getDataVector();
    		Vector vClassFiles = new Vector();
    		for(Enumeration e = v1.elements(); e.hasMoreElements();) {
    			Vector v2 = (Vector)e.nextElement();
				vClassFiles.addElement(v2.elementAt(2));
    		}   
    		return vClassFiles;	 	
    	}
    	
    	
    	
    	private void addToClassList(String cPath) {
    		if(!alreadyAdded(cPath)) {
	    		Vector v = new Vector();
	    		v.addElement(tbmlClassFiles.getRowCount()+1);
	    		v.addElement(fm.getFileName(cPath));
	    		v.addElement(cPath);
	    		tbmlClassFiles.addRow(v);
	    		
	    		if(pm.isMDS_Executable(new File(cPath))) {
	    			jf.addMainClass(fm.getFileName_WithoutExtention(fm.getFileName(cPath)));
	    		}
	    		
    		}	 
    	}
    	
    	
    	
    	private boolean alreadyAdded(String cPath) {
    		boolean r = false;
    		Vector v1 = tbmlClassFiles.getDataVector();
    		for(Enumeration e = v1.elements(); e.hasMoreElements();) {
    			Vector v2 = (Vector)e.nextElement();
    			if(v2.contains(cPath)) {
    				r = true;
    				break;
    			}
    		}
    		
    		return r;
    		 
    	}
    	
    	
    	
    	private void AddAllClasses(String cPath) {
    		if(!alreadyAdded(cPath)) {
    			addToClassList(cPath);
    			String mcn = fm.getFileName_WithoutExtention(fm.getFileName(cPath));
    			Vector vflt = new Vector();
    			vflt.addElement("class");
    			Vector vfs = fm.getContent_Files(fm.getFilePathOnly(cPath), vflt);
    			System.out.println("LEN  :  "+vfs.size());
    			System.out.println("mcn:  "+mcn);
    			for(Enumeration e = vfs.elements(); e.hasMoreElements();) {
    				String fn = String.valueOf(e.nextElement());
    				if(fm.getFileName(fn).startsWith(mcn)) {
    					addToClassList(fn);	
    				}
    			}
    		}	
    	}
    	
    	
    	
    	public void actionPerformed(ActionEvent e) {
    		if(e.getActionCommand() == "Add") {
		    	MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
		        Vector v = new Vector();
		        v.add("class");
		        fmfc.setFilter(v);
		        if(fmfc.showDiaog(frame) ==  fmfc.APPROVE_OPTION) { 
		            addToClassList(fmfc.getPath());
		        }
    		} else if(e.getActionCommand() == "Add All") {
		    	MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);          
		        Vector v = new Vector();
		        v.add("class");
		        fmfc.setFilter(v);
		        if(fmfc.showDiaog(frame) ==  fmfc.APPROVE_OPTION) { 
		            AddAllClasses(fmfc.getPath());
		        }
    		} else if(e.getActionCommand() == "Remove") {
    			if(tblClassFiles.getSelectedRow() >= 0) {
    				System.out.println("CLS Remove : "+String.valueOf(tbmlClassFiles.getValueAt(tblClassFiles.getSelectedRow(), 1)));
	    			jf.removeMainClass(fm.getFileName_WithoutExtention(String.valueOf(tbmlClassFiles.getValueAt(tblClassFiles.getSelectedRow(), 1))));
	    			tbmlClassFiles.removeRow(tblClassFiles.getSelectedRow());
    			}
    		}
    	}
    																	
    }
    
    
    
    
    
    private class JarFile extends MDS_Panel implements ActionListener {
    	
    	
    	
    	MDS_Panel pnlJarSavePath = new MDS_Panel(new BorderLayout());
    	MDS_TextField txfdJarSavePath = new MDS_TextField();
    	MDS_Button btnJarSavePath = new MDS_Button("Path");
    	
    	MDS_Panel pnlManifest = new MDS_Panel(new BorderLayout());
    	MDS_ComboBox cboMainClass = new MDS_ComboBox();
    	MDS_Panel pnl1 = new MDS_Panel(new BorderLayout());
    	
    	MDS_Panel pnlJarFileInfo = new MDS_Panel(new FlowLayout(FlowLayout.RIGHT));
    	MDS_Button btnGenerateJarFile = new MDS_Button("Generate");
    	
    	
    	
    	public JarFile() {
    		super(new BorderLayout());
    		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Jar File"));
    		
    		pnlJarSavePath.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Jar File Save Path"));
    		btnJarSavePath.addActionListener(this);
    		txfdJarSavePath.setEditable(false);
    		pnlJarSavePath.add(txfdJarSavePath, BorderLayout.CENTER);
    		pnlJarSavePath.add(btnJarSavePath, BorderLayout.EAST);
    		this.add(pnlJarSavePath, BorderLayout.NORTH);
    		
    		pnlManifest.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Manifest File"));
    		pnlManifest.add(new MDS_Label("Main Class "), BorderLayout.WEST);
    		
    		MDS_ScrollPane scrp = new MDS_ScrollPane(cboMainClass, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    		pnlManifest.add(scrp, BorderLayout.CENTER);
    		
    		this.add(pnlManifest, BorderLayout.CENTER);
    		
    		btnGenerateJarFile.addActionListener(this);
    		pnlJarFileInfo.add(btnGenerateJarFile);
    		
    		this.add(pnlJarFileInfo, BorderLayout.SOUTH);
    			
    	}
    	
    	
    	
    	public void addMainClass(String mc) {
    		cboMainClass.addItem(mc);
    	}
    	
    	
    	
    	public void removeMainClass(String mc) {
    		cboMainClass.removeItem(mc);
    	}
    	
    	
    	
    	public void actionPerformed(ActionEvent e) {
    		if(e.getActionCommand() == "Path") {
//	    		MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
//	            if(fmdc.showDiaog(frame) ==  fmdc.APPROVE_OPTION) {
//	                txfdJarSavePath.setText(fmdc.getPath()); 
//	            }  
		    	MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.SAVE_DIALOG);          
		        Vector v = new Vector();
		        v.add("jar");
		        fmfc.setFilter(v);
		        if(fmfc.showDiaog(frame) ==  fmfc.APPROVE_OPTION) { 
		            txfdJarSavePath.setText(fmfc.getPath()); 
		        }
    		} else if(e.getActionCommand() == "Generate") {
    			if(cfs.getClassFiles().size() > 0) {
    				if(cboMainClass.getSelectedIndex() >= 0) {
		    			try {
		    				File fManifest = new File(fm.getTempDir(), "MANIFEST.MF");
		    				PrintWriter pw = new PrintWriter(fManifest);
		    				pw.println("Manifest-Version: 1.0");
		    				pw.println("Created-By: 1.6.0 (Sun Microsystems Inc.)");
		    				pw.println("Main-Class: "+String.valueOf(cboMainClass.getSelectedItem()));
		    				pw.close();
			    			Manifest man = new Manifest(new FileInputStream(fManifest));
		
		//					Manifest man = new Manifest();
		//	    			((Map)man.getEntries()).put("Manifest-Version", "1.0");
		//	    			((Map)man.getEntries()).put("Main-Class", "Mahesh");
		//	    			//System.out.println(man.);
			    			String path = txfdJarSavePath.getText();
			    			if(!path.endsWith(".jar")) path = path+".jar"; 
			    			MDS_JarFile.creatJarFile(cfs.getClassFiles(), man, new File(path));
		    			} catch(Exception ex) {
		    				ex.printStackTrace();
		    			}
    				} else {
    					MDS_OptionPane.showMessageDialog(frame, "Select a Main Class", "Jar File Creator", JOptionPane.ERROR_MESSAGE);
    				}
    			} else {
    				MDS_OptionPane.showMessageDialog(frame, "Class File count cannot be zero.", "Jar File Creator", JOptionPane.ERROR_MESSAGE);
    			}
    		}	
    	}
    	
    	
    }
    
    
    
}