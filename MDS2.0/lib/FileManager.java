/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.*;



public final class FileManager {


    
    private static FileManager fm = new FileManager();
    private File tempDir;
    private File mds_Dir;
    private File mds_dskShortcuts_Dir;
    private File mds_Download_Dir;
    private File mds_System_Dir;
    
    

    private FileManager() {     
        checkMDS_Dir();
        checkTempDir();   
    }
    
    
    
    public static FileManager getFileManager() {
        return fm;
    }
    
    
    
    public void setFileCopy_BufferSize(int size) {
    
    }  
    
    
    
    public int getFileCopy_BufferSize() {
        return -1;
    }      
      
    

    public File[] getContent_Directories(String path) {

        File content[] = this.getContent(path);
        File contentDirectories[];
        int numberOfDirectories = 0;
        int countDirectories = 0;

        for(int count = 0; count < content.length; count++) {
            if(content[count].isDirectory()) {
                numberOfDirectories++;
            }
        }

        contentDirectories = new File[numberOfDirectories--];

        for(int count = 0; count < content.length; count++) {
            if(content[count].isDirectory()) {
                contentDirectories[countDirectories] = new File(content[count].getPath());
                countDirectories++;
            }
        }

        return contentDirectories;
    }
    
    
    

    public File[] getContent_Files(String path) {

        File content[] = this.getContent(path);
        File contentFiles[];
        int numberOfFiles = 0;
        int countFiles = 0;

        for(int count = 0; count < content.length; count++) {
            if(content[count].isFile()) {
                numberOfFiles++;
            }
        }

        contentFiles = new File[numberOfFiles--];
        for(int count = 0; count < content.length; count++) {
            if(content[count].isFile()) {
                contentFiles[countFiles] = new File(content[count].getPath());
                countFiles++;
            }
        }

        return contentFiles;

    }   
    
    
    
    public Vector getContent_Files(String path, Vector filter) {

        File content[] = this.getContent_Files(path);
        Vector vctFiltered = new Vector();
        
        for(int count = 0; count < content.length; count++) {
            if(filter.contains(getFileExtension(content[count].getName()))) {
                vctFiltered.add(content[count]);
            }
            
        }
        
        return vctFiltered; 

    } 
    
    
    
    public Vector getContent_FileNames(String path, Vector filter) {

        File content[] = this.getContent_Files(path);
        Vector vctFiltered = new Vector();
        
        for(int count = 0; count < content.length; count++) {
            if(filter.contains(getFileExtension(content[count].getName()))) {
                vctFiltered.add(content[count].getName());
            }
            
        }
        
        return vctFiltered; 

    }       
	
	
	
	public static boolean isBeginsWithRoot(String path) {
		if (path.length() == 0)
			return false;

		File file = new File(path);
		File[] roots = file.listRoots();
		for (int i = 0; i < roots.length; i++)
			if (path.regionMatches(true, 0, roots[i].getPath(), 0, roots[i].getPath().length()))
				return true;
		return false;
	}	 
    
    
    
    public File[] getContent(String path) {
    	
    	
    	if(!path.endsWith("\\")) path = path.concat("\\");

        File temp = new File(path);

        String content[] = temp.list();

        File newContent[] = new File[content.length];
        temp = null;

        for(int count = 0;count<content.length;count++) {
            newContent[count] = new File(path + content[count]);
            //System.out.println(newContent[count].getPath());
        }

        return newContent;

    }    
    
    
    
    public File[] getRootDrives() {
        
        File f = new File("c:\\");
        return f.listRoots();

    }  
    
    
    
   public String[] getRootDrives_AsStrings() {

      return this.convertToString(this.getRootDrives());

   }
   
   
   
    public String[] convertToString(File names[]) {

        String content[] = new String[names.length];

        for(int count = 0; count < names.length; count++) {
            content[count] = new String(names[count].getPath());
        }

        return content;

    } 
    
    
    
    public boolean hasFileExtention(File f) {
        String fn = f.getName();
        int index = 0;
        boolean found = false;
        
        while(index <= fn.length()-1 && !found) {
            if(fn.charAt(index) == '.') {
                found = true; 
            }    
            
            index++;
            
        }
        
        return found;
        
    }
    
    
    
    public boolean isLegalFileName(File f) {
        String name = f.getName();
        int index = 0;
        boolean legal = true;
        while(index <= name.length()-1 && legal) {
            if(name.charAt(index) == '\\') legal = false;
            else if(name.charAt(index) == '/') legal = false;
            else if(name.charAt(index) == ':') legal = false;
            else if(name.charAt(index) == '"') legal = false;
            else if(name.charAt(index) == '?') legal = false;
            else if(name.charAt(index) == '"') legal = false;      
            else if(name.charAt(index) == '<') legal = false; 
            else if(name.charAt(index) == '>') legal = false;
            else if(name.charAt(index) == '|') legal = false; 
            
            index++;
                            
        }  
        
        return legal;
        
    }
    
    
    
    public String getFileType(String name) {
        
        String type = "";
        String fx = getFileExtension(name);
        
        if(fx.equals("dll")) {
            type = "Dinamic Link Libray";
        } else if(fx.equals("exe")) {
            type = "Win32 Executable";			
        } else if(fx.equals("rtf")) {
            type = "Rich Text Format";
        } else if(fx.equals("gzip")) {
            type = "GZip File";
        } else if(fx.equals("Zip")) {
            type = "Zip File";
        } else if(fx.equals("class")) {
            type = "Java Class File";
        } else if(fx.equals("txt")) {
            type = "Text Document";
        } else if(fx.equals("AU")) {
            type = "AU Format Sound";
        } else if(fx.equals("aiff")) {
            type = "AIFF Format Sound";
        } else if(fx.equals("mid")) {
            type = "MIDI Sequence";
        } else if(fx.equals("wav")) {
            type = "Wave Sound";
        } else if(fx.equals("mp3")) {
            type = "MP3 Format Sound";            
        } else if(fx.equals("gif")) {
            type = "GIF Image";
        } else if(fx.equals("jpg") || fx.equals("jpeg")) {
            type = "JPEG Image";
        } else if(fx.equals("png")) {
            type = "PNG Image";
        } else if(fx.equals("html") || fx.equals("htm")) {
            type = "HTML Document";
        } else if(fx.equals("c")) {
            type = "C Source File";
        } else if(fx.equals("cpp") || fx.equals("cxx")) {
            type = "C++ Source File";
        } else if(fx.equals("h")) {
            type = "C Header File";
        } else if(fx.equals("java")) {
            type = "Java Source File";
        } else if(fx.equals("jar")) {
            type = "Jar File"; 
        } else if(fx.equals("neo")) {
            type = "Neo File";
        } else if(fx.equals("dkst")) {
            type = "Desktop Shortcut File";   
        } else if(fx.equals("Max")) {
            type = "Maximus File";   	 
        } else {
            type = "File";
        } 

        
        return type;
    }
    
    
    
    public Vector getSupportedFileType_Extentions() {
        Vector vct = new Vector();
        
        vct.addElement("dll");
		vct.addElement("exe");
        vct.addElement("rtf");
        vct.addElement("gzip");
        vct.addElement("zip");
        vct.addElement("class");
        vct.addElement("txt");
        vct.addElement("AU");
        vct.addElement("aiff");
        vct.addElement("mid");
        vct.addElement("wav");
        vct.addElement("mp3");
        vct.addElement("gif");
        vct.addElement("jpg");
        vct.addElement("jpeg");
        vct.addElement("png");
        vct.addElement("html");
        vct.addElement("htm");
        vct.addElement("c");
        vct.addElement("cpp");
        vct.addElement("cxx");
        vct.addElement("h");
        vct.addElement("java");
        vct.addElement("jar");
        vct.addElement("neo");
        vct.addElement("dkst");
		vct.addElement("max");
                               
        return vct;  
    }
    
    
    
    public boolean isSupportedFileType(String name) {
        boolean r = false;
        String fx = getFileExtension(name);
        if(getSupportedFileType_Extentions().contains(fx)) {
            r = true;
        }
        
        return r;
        
    }
    
    
    
    public ImageIcon getDefault_FileType_Icon() {
        return ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-unknown.png");
    }
    
    

    public ImageIcon getDefault_Directory_Icon() {
        return ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_open.png", 32, 32, ImageManipulator.ICON_SCALE_TYPE);
    } 
	
	public ImageIcon getDefault_Directory_Icon_Large() {
		return ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_open.png");
	} 
      
    
/*   
    public String getFileExtension(String name) {
        
        int x = name.length();   
        String ex = "";
        
        do {
            ex = name.substring(x, name.length());
            x--;
        } while(name.charAt(x)!='.');
        
        return ex.toLowerCase() ;
        
    }
*/    



    public String getFileExtension(String name) {
        if(!hasFileExtention(new File(name))) {
            return "";    
        }  
        
        int x = name.length();   
        String ex = "";
        
        do {
            ex = name.substring(x, name.length());
            x--;
        } while(name.charAt(x)!='.');
        
        return ex.toLowerCase();               
    }    
    
    
    public String getFileName_WithoutExtention(String fName) {
        /*
        int x =0;   
        String name = "";
        
        while() {
            name = name.concat(String.valueOf(fName.charAt(x)));
            x++;
        } 
        
        return name;         
        */
        
        
        
        int x = fName.length()-1;   
        String name = "";
        
        while(fName.charAt(x)!='.') {
            x--;
            if(x == -1) break;
        } 
        
        if(x != -1) {
            name = fName.substring(0, x);    
        } else {
            name = fName;
        }
        
        return name;   
    }
    
    
    
    public String getFileName(String path) {
        int x = path.length();   
        String fName = "";
        
        do {
            fName = path.substring(x, path.length());
            x--;
        } while(path.charAt(x)!='\\');
        
        //return fName.toLowerCase();  
        return fName;
    }
    
    
    
    public String getFilePathOnly(String name) {
        int x = name.length();   
        String path = "";
        
        do {
            path = name.substring(0, x);
            x--;
        } while(name.charAt(x)!='\\');
        
        return path;   
    }
    
    
    
    public String getFilePathOnly(File f) {
        return getFilePathOnly(f.getPath());
    }
    
    
    
    public void showCreateNewFile_Dialog(MDS_Frame parentFrame, String location, String extension, String fileType) {
        String nfn = "";  
        nfn = MDS_OptionPane.showInputDialog(parentFrame, "New "+fileType+" file Name", "New "+fileType+" file", JOptionPane.QUESTION_MESSAGE);      
        if(!nfn.equals("")) {
            File f = new File(location, nfn);
            if(!nfn.endsWith(extension)) {
                f = new File(location, nfn+extension);
            }
            if(!f.exists()) {
                try {
                    f.createNewFile();
                    //this.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.ADD_FILE));
////                    MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f));
                } catch(Exception ex) {
                    MDS_OptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Error Creating File", JOptionPane.ERROR_MESSAGE); 
                }    
            } else {
                MDS_OptionPane.showMessageDialog(parentFrame, "File already exists ("+nfn+")", "File Creation", JOptionPane.INFORMATION_MESSAGE);    
            }
        }           
    }
    
    
    
    public String[] getParentList(String path) {
        File f = new File(path);
        int i = 0;
        Vector vctPrl = new Vector();
        String currentParent = f.getParent(); 
        while(!currentParent.equals("") && !currentParent.equals("\\")) {
            vctPrl.add(currentParent);
            File f1 = new File(currentParent);
            currentParent = f1.getParent();     
        }

        String[] prl = new String[vctPrl.size()];
        int y = 0;
        for(int x = vctPrl.size()-1; x>=0; x--) {
            prl[y] = (String)vctPrl.elementAt(x);
            y++;
        }

        return prl;    
    }
    
    
    
    public boolean isValidFileName(String name) {
        int index = 0;
        boolean valid = true;
        
        while(index <= name.length()-1) {
            if(name.charAt(index)== '\\') valid = false; 
            if(name.charAt(index)== '/') valid = false;
            if(name.charAt(index)== ':') valid = false;
            if(name.charAt(index)== '"') valid = false;
            if(name.charAt(index)== '?') valid = false;
            if(name.charAt(index)== '"') valid = false;
            if(name.charAt(index)== '<') valid = false;
            if(name.charAt(index)== '>') valid = false;
            if(name.charAt(index)== '?') valid = false;
            
            if(!valid) break;
            
            index++;
            
        }
        
        return valid;
                    
    }
    
    
    
    private void checkTempDir() {         
        
        try {
            
            Random rand = new Random();
            tempDir = new File(mds_Dir.getPath(),"temp");
            if(!tempDir.mkdir() && tempDir.exists()) {   
                File f[] = getContent_Files(tempDir.getPath()+"\\");
                for(int count = 0; count < f.length; count++) {
                    f[count].delete();
                }  
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
    } 
    
    
    
    private void checkMDS_Dir() {  
        File userHome = new File(System.getProperty("user.home")+"\\MDS 2.0"); 
        if(!userHome.exists()) userHome.mkdir();  
        mds_Dir = userHome; 
        
        mds_dskShortcuts_Dir = new File(userHome.getPath(), "desktopShortcuts");
        if(!mds_dskShortcuts_Dir.exists()) mds_dskShortcuts_Dir.mkdir();
        
        mds_Download_Dir = new File(userHome.getPath(), "download");
        if(!mds_Download_Dir.exists()) mds_Download_Dir.mkdir(); 
        
        mds_System_Dir = new File(userHome.getPath(), "system");
        if(!mds_System_Dir.exists()) mds_System_Dir.mkdir();       
        
    }
    
    
    
    public File getMDS_System_Dir() {
        if(!mds_System_Dir.exists()) {
            checkMDS_Dir();        
        }
              
        return mds_System_Dir;      
    }
    
    
    
    public File getMDS_Download_Dir() {
        if(!mds_Download_Dir.exists()) {
            checkMDS_Dir();        
        }
              
        return mds_Download_Dir;      
    }
    
    
    
    public File getMDS_DesktopShortcuts_Dir() {
        if(!mds_dskShortcuts_Dir.exists()) {
            checkMDS_Dir();        
        }
              
        return mds_dskShortcuts_Dir;    
    }
    
    
    
    public File getMDS_Dir() {
        if(!mds_Dir.exists()) {
            checkMDS_Dir();        
        }      
        return mds_Dir;    
    }   
    
    
    
    public File getTempDir() {
        
        if(!tempDir.exists()) {
            checkTempDir();        
        }
        
        return tempDir; 
               
    }
    
    
    
    public File createTempFile(String prefix, String suffix) {
        
        File f = null;
        
        try {
            if(suffix == null) suffix = ".tmp";  
            else if(!suffix.startsWith(".")) suffix = "."+suffix;      
            f = new File(tempDir.getPath()+"\\"+prefix+suffix); 
            f.createNewFile(); 
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
////            MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.NEW_FILE, f));
        }
        return f;           
    }
    
    
    
    public String getUserHomeDir() {
        String dir = System.getProperty("user.home");
        if(!dir.endsWith("\\")) dir = dir.concat("\\");
        return dir;
    }
    
    
    
    public ImageIcon getFileType_Icon(File f) {
    
        ImageIcon i = null;
        String fx = getFileExtension(f.getName());
        int w =31;
        int h =35;
        
        if(f.isDirectory()) {
            return getDefault_Directory_Icon();    
        }
        
        if(fx.equals("dll")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-source_moc.png");
        } else if(fx.equals("exe")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-exec_win32.png");       
		} else if(fx.equals("rtf")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-kword_kwd.png");
        } else if(fx.equals("gzip")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_tar.png");
        } else if(fx.equals("zip")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-folder_tar.png");
        } else if(fx.equals("class")) {
            if(f.exists()) {
                if(MDS.getProcessManager().isMDS_Executable(f)) {
                    i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-filesys-exec.png");     
                } else {
                    i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-binary.png");             
                }
            } else {
                i = getDefault_FileType_Icon();//ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-binary.png");                         
            }
        } else if(fx.equals("txt")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-txt.png");
        } else if(fx.equals("AU")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-midi.png");
        } else if(fx.equals("aiff")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-midi.png");
        } else if(fx.equals("mid")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-midi.png");
        } else if(fx.equals("wav")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-midi.png");
        } else if(fx.equals("mp3")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-midi.png");        
        } else if(fx.equals("gif")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-image.png");
        } else if(fx.equals("jpg") || fx.equals("jpeg")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-image.png");
        } else if(fx.equals("png")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-image.png");
        } else if(fx.equals("html") || fx.equals("htm")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-html.png");
        } else if(fx.equals("c")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-source_c.png");
        } else if(fx.equals("cpp") || fx.equals("cxx")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-source_cpp.png");
        } else if(fx.equals("h")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-source_h.png");
        } else if(fx.equals("java")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-source_java.png");
        } else if(fx.equals("jar")) {
        	if(f.exists()) {
	        	if(MDS.getProcessManager().isMDS_Executable(f)) {
	        		i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-package_system.png");
	        	} else {
	            	i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "gnome-mime-application-x-jar.png");
	        	} 
        	} else {
        		i = getDefault_FileType_Icon();//ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-binary.png");    
        	}           
        } else if(fx.equals("neo")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "neo.png"); 
        } else if(fx.equals("dkst")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "mds-shortcut.png");        
        } else if(fx.equals("max")) {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-app-phppg.png");         
		} else {
            i = ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "48-mime-unknown.png");
        } 
                
        return i;
    }  
    
                   
    
    /*
      function to start the application associated with a given document extension without knowing the name of 
      the associated application.
    */  
    public void executeFile(String name) {
        File f = new File(name);
        ProcessManager prm = MDS.getProcessManager();
        
        String fx = getFileExtension(f.getName());
        
        String[] filePath ={f.getPath()};
        
        if(fx.equals("jpg") || fx.equals("jpeg") || fx.equals("gif") || fx.equals("png")) {
            prm.execute(new File(MDS.getBinaryPath(), "ElectronicEyes"), filePath);  
        } else if(fx.equals("txt") || fx.equals("rtf") || fx.equals("c") || fx.equals("h") || fx.equals("cpp") || fx.equals("cxx") || fx.equals("java")) {
            prm.execute(new File(MDS.getBinaryPath(), "AnyEdit"), filePath);
        } else if(fx.equals("html") || fx.equals("htm")) {
        	String[] filePathA ={"file:"+f.getPath()};
            prm.execute(new File(MDS.getBinaryPath(), "WebBrowser"), filePathA);
        } else if(fx.equals("wav") || fx.equals("mid") || fx.equals("au") || fx.equals("rmf")|| fx.equals("aiff")) {
            prm.execute(new File(MDS.getBinaryPath(), "MediaPlayer"), filePath);
        } else if(fx.equals("mp3")) {
            prm.execute(new File(MDS.getBinaryPath(), "Mp3Player"), filePath);
        } else if(fx.equals("jar")) {
        	if(prm.isMDS_Executable(f)) {
        		System.out.println("execJar");
        		prm.executeJar(f);
        	} else {
        		System.out.println("Jar");
            	prm.execute(new File(MDS.getBinaryPath(), "JarFileViewer.class"), filePath); 
        	}
        } else if(fx.equals("zip")) {
            prm.execute(new File(MDS.getBinaryPath(), "ZipFileViewer"), filePath); 
        } else if(fx.equals("neo")) {
            prm.execute(new File(MDS.getBinaryPath(), "Neo"), filePath);
		} else if(fx.equals("max")) {
			prm.execute(new File(MDS.getBinaryPath(), "Maximus"), filePath);
        } else if(fx.equals("dkst")) {
            try {
                ObjectInputStream o_In = new ObjectInputStream(new FileInputStream(f));         
                File targetFile = (File)o_In.readObject();
                executeFile(targetFile);
            } catch(Exception ex) {ex.printStackTrace();}              
        } else if(fx.equals("class")) {
            if(prm.isMDS_Executable(f)) {
                prm.execute(f);
            } else {
                showOpenWith(f.getPath()); 
            }                            
        } else {
            showOpenWith(f.getPath());   
        }
        
    }
    
    
    
    public void executeFile(File f) {
        executeFile(f.getPath());
    }
    
    
    
    public void showOpenWith(String path) {
    
    
    
        class OpenWith extends ControlModule implements ActionListener {
        
        
        
            JComponent contentPane;
            MDS_Panel pnlHeader = new MDS_Panel(new BorderLayout());
            MDS_Panel pnlHeader_Text = new MDS_Panel(new GridLayout(2,1));
            
            MDS_ListCellRenderer lstcr = new MDS_ListCellRenderer();;
            MDS_ListModel lstmdl = new MDS_ListModel();                                 
            MDS_List lstApps = new MDS_List(lstmdl);
            File f;        
        
        
            public OpenWith(String filePath) {
                super("Open With", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "open-with.png"));       
                f = new File(filePath);
                contentPane = this.get_CM_ContentPane();  
                pnlHeader.add(new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "open-with.png")), BorderLayout.WEST);
                pnlHeader_Text.add(new MDS_Label("Choose the program you want to use to open this file:"));
                pnlHeader_Text.add(new MDS_Label("File : "+f.getName())); 
                pnlHeader.add(pnlHeader_Text, BorderLayout.CENTER);                
                contentPane.add(pnlHeader, BorderLayout.NORTH);
                contentPane.add(new JScrollPane(lstApps), BorderLayout.CENTER); 
                lstApps.setCellRenderer(lstcr); 
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "anyedit.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Any Edit");  
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "ClassConnectionWalkerpng.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Class Connection Walker"); 
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "mds-zip.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Zip File Viewer"); 
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "apple-red.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Electronic Eyes"); 
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "media-player.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Media Player");
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "mp3-player.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Mp3 Player");
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "sun-glasses.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Sun Glasses"); 
                lstmdl.addElement(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH + "executable-jar.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE), "Jar File Viewer"); 
                lstApps.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                        if(e.getButton() == e.BUTTON1 && e.getClickCount() == 2) {
                            if(lstApps.getSelectedIndex() != -1) {
                                executeApp();
                            }    
                        }
                    }
                });                
                this.setSize(400, 350);
                this.setCenterScreen();
                this.setVisible(true);         
            }
            
            
            
            private void executeApp() {
                ProcessManager prm = MDS.getProcessManager();
                String[] absPath ={f.getPath()};
                
                if(lstApps.getSelectedIndex() == 0) {
                    prm.execute(new File(MDS.getBinaryPath(),"AnyEdit"), absPath);
                } else if(lstApps.getSelectedIndex() == 1) {
                
                } else if(lstApps.getSelectedIndex() == 2) {
                    prm.execute(new File(MDS.getBinaryPath(),"ZipFileViewer"));
                } else if(lstApps.getSelectedIndex() == 3) {
                    prm.execute(new File(MDS.getBinaryPath(),"ElectronicEyes"), absPath);
                } else if(lstApps.getSelectedIndex() == 4) {
                    prm.execute(new File(MDS.getBinaryPath(),"MediaPlayer"), absPath);
                } else if(lstApps.getSelectedIndex() == 5) {
                    prm.execute(new File(MDS.getBinaryPath(),"Mp3Player"), absPath);
                } else if(lstApps.getSelectedIndex() == 6) {
                    prm.execute(new File(MDS.getBinaryPath(),"SunGlasses"));
                } else if(lstApps.getSelectedIndex() == 7) {
                    prm.execute(new File(MDS.getBinaryPath(),"JarFileViewer"));
                }
                this.dispose();

            }
            
            
            
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Ok")) {
                    if(lstApps.getSelectedIndex() != -1) {
                        executeApp();            
                    }                  
                } else if(e.getActionCommand().equals("Cancel")) {
                    this.dispose();
                }
            }
            
            
            
        }
        
        new OpenWith(path);
             
    }
    
    
        
    public synchronized String getLastModified_As_String(long timeInMillis) {
    
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeInMillis);	
        String h = String.valueOf(calendar.get(Calendar.HOUR));
        String min = String.valueOf(calendar.get(Calendar.MINUTE));
        String s = String.valueOf(calendar.get(Calendar.SECOND));
                    
        String am_pm = "";
                    
        if(calendar.get(Calendar.AM_PM) == Calendar.AM) {
            am_pm = "AM";
        } else if(calendar.get(Calendar.AM_PM) == Calendar.PM) {
            am_pm = "PM";
        } 
  
        String d = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String m = String.valueOf(calendar.get(Calendar.MONTH));
        String y = String.valueOf(calendar.get(Calendar.YEAR)); 
        
        return d+"/"+m+"/"+y+"  , "+h+":"+min+":"+s+""+am_pm;
           
    }
    
    
    
    public String getFormatedFileSize(long len) {
    
        String text;

        if(len >= 1073741824) {
            text = String.valueOf(len/1073741824)+" GB";
        } else if(len >= 1048576) {
            text = String.valueOf(len/1048576)+" MB";    
        } else if(len >= 1024) {
            text = String.valueOf(len/1024)+" KB";
        } else if(len < 1024) {
            text = String.valueOf(len)+" bytes";    
        } else {
            text = "";
        }
        
        return text;
        
    }
    
    
    /*
    public static synchronized void fileCopy(File sFile, File dFile) {
        try {
           DataInputStream disIn;
            DataOutputStream dosOut;     
            //File sFile = new File(src);
            //File dFile = new File(des);
            disIn = new DataInputStream(new FileInputStream(sFile));   
            dosOut = new DataOutputStream(new FileOutputStream(dFile));      
                int off = 0;
                int len = 1024;
                long fLen = sFile.length();
                long cfLen = 0;
                //System.out.println("FF");
                while(disIn.available() != 0) {
            
                    if((fLen - cfLen) <= len) {
                        String slen = String.valueOf(fLen - cfLen); 
                        Integer i = new Integer(slen); 
                        len = i.intValue();    
                    } else {            
                        cfLen = cfLen+len;

                    }   
                    byte b[] = new byte[len];
                    disIn.read(b,off,len);
                    dosOut.write(b,off,len);
                }
            
            dosOut.close();
            disIn.close();
                                                                 
        }catch(Exception ex) {
                 
        }            
    }*/
    
    
    
    public void copyFile(File scr, File des, boolean ow) {
	    if(scr.isDirectory()) {
	        new CopyDir(scr, des, ow);
	    } else {
	        new FileCopy(scr, des);
	    }
    }
    
    
    
    public void moveFile(File scr, File des, boolean ow) {
        if(scr.isDirectory()) {
        
        } else {
            new MoveFile(scr, des);
        }    
    }
    
    
    
    public void deleteDir(File dir) {
        if(dir.isDirectory()) {
            new DeleteDir(dir);
        } else {
            throw new IllegalArgumentException();
        }  
    }
    
    
    
    public void moveDir(File scr, File dir) {

    }
    
    
    /*
    public synchronized int copy(String src, String des, boolean ow) {
        
        try {
            
            File srcFile = new File(src);
            File desFile = new File(des);
            //DataInputStream disIn = new DataInputStream(new FileInputStream(src));
            
            //DataOutputStream dosOut = new DataOutputStream(new FileOutputStream(des));
            
            new FileCopy(srcFile, desFile);
                               
            }catch(Exception ex) {
                //System.out.println(ex);
            }     
        
        return 2;
    }
    
    
    
    public int copy_Dir(String scrDir, String desDir, boolean ow) {
        new DirCopy_Tree(new File(scrDir), new File(desDir), ow);
        return 2;
    }    
    
    
    
    public int delete(String name) {
        return 3;
    }
    
    
    
    public void delTree(String path) {
        new DelTree(path);
    }*/
   
    
    
    public void showFilePopupMenu(MDS_FilePopupMenuListener l, Component invoker ,int x, int y, File f) {
        new FilePopupMenu(l, invoker, x, y, f);
    }   
    
    
    
    public void showFileProperties(File f) {
        new FileProperties(f);
    }
	
	
	
	
	
	private class HL_CopyFile extends Thread {
	
	
	
		ObjectInputStream in; 
		ObjectOutputStream out;		
        File srcFile;
        File desFile;
        
        MDS_Frame fCopy;
        JComponent contentPane;
        MDS_ProgressBar cProgress;
        MDS_Label lblSource;
        MDS_Label lblDestination;
        MDS_Label lblCurrentStatus;
        MDS_Label lblBufferSize;
        
        MDS_Button btnCancel;
        
        //FileSystemListener fsl;	
	
	
		public HL_CopyFile(ObjectInputStream in, ObjectOutputStream out) {
			super();
			this.in = in;
			this.out = out;	
					
		}
		
		
		public void run() {
			
		}
		
	}
    
    
    
    
    
    class FileProperties extends ControlModule implements ActionListener {
    
    
    
        File file;
        JComponent contentPane;
        MDS_TabbedPane tbpFileProperties = new MDS_TabbedPane();
        MDS_Panel pnlGeneral = new MDS_Panel();
        MDS_Panel pnlFile = new MDS_Panel();
        //FilePanel fp = new FilePanel();
        
    
        public FileProperties(File f) {
            super(f.getName()+" Properties");
            file = f;
            contentPane = this.get_CM_ContentPane();
            tbpFileProperties.add("General", new FilePanel());
            contentPane.add(tbpFileProperties, BorderLayout.CENTER);
            this.setSize(300, 400);
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Ok")) {
                //if(!file.getName().equals(fp.getNewFileName())) {
                    //System.out.println("Rem");
                //}
                this.dispose();
            } else if(e.getActionCommand().equals("Cancel")) {
                this.dispose();
            }
        }        
        
        
        
        
        
        class FilePanel extends MDS_Panel {
        
        
        
            MDS_Label lblIcon = new MDS_Label(MDS.getMDS_VolatileImageLibrary().getDefaultIcon_For_File(file, MDS_VolatileImageLibrary.ICON_SIZE_48x48));
                       
            MDS_TextField txfName = new MDS_TextField(file.getName());
            
            JSeparator sprt1 = new JSeparator(SwingConstants.HORIZONTAL);
            
            MDS_Label lblType = new MDS_Label("Type");
            MDS_Label lblF_Type = new MDS_Label();
   
            JSeparator sprt2 = new JSeparator(SwingConstants.HORIZONTAL);
            
            MDS_Label lblLocation = new MDS_Label("Location");
            MDS_Label lblF_Location = new MDS_Label("Location");
            MDS_Label lblSize = new MDS_Label("Size");
            MDS_Label lblF_Size = new MDS_Label();
            
            JSeparator sprt3 = new JSeparator(SwingConstants.HORIZONTAL);           
            
            MDS_Label lblLastModified = new MDS_Label("Last Modified");
            MDS_Label lblF_LastModified = new MDS_Label();
            
            JSeparator sprt4 = new JSeparator(SwingConstants.HORIZONTAL);            
            
            MDS_Label lblAttributes = new MDS_Label("Attributes:");
            
            MDS_CheckBox cboReadOnly = new MDS_CheckBox("Read Only");
            
            MDS_CheckBox cboHidden = new MDS_CheckBox("Hidden");

        
            public FilePanel() {
                this.setLayout(null);
                lblIcon.setBounds(10, 10, 40, 40);
                this.add(lblIcon);  
                
                txfName.setBounds(70, 20, 190, 25);
                this.add(txfName);
                
                sprt1.setBounds(10, 70, 250, 4);
                this.add(sprt1);
                
                lblType.setBounds(10, 80, 100, 25);
                this.add(lblType);
                
                String type = "";
                
                if(file.isDirectory()) {
                    type = "Directory";
                } else {
                    type = getFileType(file.getName());
                }
                
                lblF_Type.setText(type);
                lblF_Type.setBounds(100, 80, 160, 25);
                this.add(lblF_Type); 
                
                sprt2.setBounds(10, 110, 250, 4);
                this.add(sprt2);   
                
                lblLocation.setBounds(10, 120, 100, 25);
                this.add(lblLocation);
                
                lblF_Location.setBounds(100, 120, 160, 25);
                lblF_Location.setText(file.getAbsolutePath());
                this.add(lblF_Location);               
                                            
                lblSize.setBounds(10, 145, 100, 25);
                this.add(lblSize);  
                
                lblF_Size.setBounds(100, 145, 160, 25);
                lblF_Size.setText(getFormatedFileSize(file.length()));
                this.add(lblF_Size); 
                
                lblLastModified.setBounds(10, 170, 100, 25);
                this.add(lblLastModified); 
 
                lblF_LastModified.setBounds(100, 170, 160, 25);
                lblF_LastModified.setText(getLastModified_As_String(file.lastModified()));
                this.add(lblF_LastModified);                
                
                sprt3.setBounds(10, 200, 250, 4);
                this.add(sprt3); 
                
                lblAttributes.setBounds(10, 210, 100, 25);
                this.add(lblAttributes);
                
                cboReadOnly.setBounds(15, 240, 100, 25);
                if(file.canWrite()) {
                    cboReadOnly.setSelected(false);
                } else {
                    cboReadOnly.setSelected(true);
                }    
                this.add(cboReadOnly);
                
                cboHidden.setBounds(130, 240, 100, 25);
                cboHidden.setSelected(file.isHidden());
                cboHidden.setEnabled(false);
                this.add(cboHidden);                
                                  
            }
            
            
            
            public String getNewFileName() {
                return txfName.getText();
            }
            
            
            
        }    
        
        
        
    }    
    
    
    
    
    
    class FilePopupMenu implements ActionListener {
        
        
        MDS_User usr = MDS.getUser();
        MDS_Clipboard clipBoard = MDS.getClipboard();
        ProcessManager prm = MDS.getProcessManager();
        
        MDS_PopupMenu pMenu = new MDS_PopupMenu();
            
        JMenuItem mniOpen = usr.createMenuItem("Open", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-fileopen.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniOpenWithCCW = usr.createMenuItem("Open With CCW", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"ClassConnectionWalkerpng.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniPrint = usr.createMenuItem("Print", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-device-printer1.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniOpenWithSG = usr.createMenuItem("Open with Sun Glasses", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"sun-glasses.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniFind = usr.createMenuItem("Find Files", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-app-kfind.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniCompress = usr.createMenuItem("Compress", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-folder_tar.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniDecompress = usr.createMenuItem("Decompress", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-folder_tar.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        MDS_Menu mnuOpenWith = new MDS_Menu("Open With");
        MDS_Menu mnuSendTo = new MDS_Menu("Send To", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-1rightarrow.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE));
        JMenuItem mniHomeDir = usr.createMenuItem("Home Directory", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-app-kfm_home.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniFloppy = usr.createMenuItem("3.5 Floppy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"64-device-3floppy.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniAnotherComputer = usr.createMenuItem("Another Computer", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-device-system.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        
        JMenuItem mniCut = usr.createMenuItem("Cut", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcut.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniCopy = usr.createMenuItem("Copy", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcopy.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniMoveTo = usr.createMenuItem("Move To", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcut.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniCopyTo = usr.createMenuItem("Copy To", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"22-action-editcopy.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);        
        JMenuItem mniDelete = usr.createMenuItem("Delete", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-filesys-trashcan_empty.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniRename = usr.createMenuItem("Rename", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"rename.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        JMenuItem mniProperties = usr.createMenuItem("Properties", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"48-mime-info.png", 16 ,16 , ImageManipulator.ICON_SCALE_TYPE), this);
        
        MDS_FilePopupMenuListener fpml;
        File file;

        

        
        public FilePopupMenu(MDS_FilePopupMenuListener l,Component invoker ,int x, int y, File f) {
            
            fpml = l;
                       
            file = f;
            String ex = getFileExtension(f.getName());
        
            if(f.isDirectory()) {
                pMenu.add(mniOpen); 
                pMenu.add(mniFind);
                pMenu.add(mniCompress);
                pMenu.addSeparator();
                pMenu.add(mnuSendTo);
                pMenu.addSeparator();
                pMenu.add(mniCut);
                pMenu.add(mniCopy);
                pMenu.add(mniMoveTo);
                pMenu.add(mniCopyTo);                
                pMenu.addSeparator();
                pMenu.add(mniDelete);
                pMenu.add(mniRename);
                pMenu.addSeparator();
                pMenu.add(mniProperties);   
                    
                pMenu.show(invoker, x, y);
                    
            } else {
            
                pMenu.add(mniOpen);
                
                if(f.getName().endsWith(".class")) {
                    pMenu.add(mniOpenWithCCW);
                }
                
                if(ex.equals("gif") || ex.equals("png") || ex.equals("jpg") || ex.equals("jpeg")) {
                    pMenu.add(mniOpenWithSG);
                    pMenu.add(mniPrint);
                }
                
                if(getFileExtension(f.getName()).equals("zip")) {
                    pMenu.add(mniDecompress);
                } else if(getFileExtension(f.getName()).equals("gzip")) {
                    pMenu.add(mniDecompress);
                } else {
                    pMenu.add(mniCompress);
                }
                
                pMenu.addSeparator();
                pMenu.add(mnuSendTo);
                pMenu.addSeparator();
                pMenu.add(mniCut);
                pMenu.add(mniCopy);
                pMenu.add(mniMoveTo);
                pMenu.add(mniCopyTo);                  
                pMenu.addSeparator();
                pMenu.add(mniDelete);
                pMenu.add(mniRename);
                pMenu.addSeparator();
                pMenu.add(mniProperties);     
                
                pMenu.show(invoker, x, y);
                            
            }  
            
            mnuSendTo.add(mniHomeDir);
            mnuSendTo.add(mniFloppy);
            mnuSendTo.add(mniAnotherComputer);
            
        }
            
            
            
        public void actionPerformed(ActionEvent e) {
                
            //try {
            
                if(e.getActionCommand().equals("Rename")) {
                    String nfn = "";  
                    nfn = MDS_OptionPane.showInputDialog(fpml.getListener__MDS_Frame(), "Type new file / directory name.", "Rename File / Directory", JOptionPane.QUESTION_MESSAGE);      
                    if(nfn == null) {
                        nfn="";
                    }
                    if(!nfn.equals("")) {
                        File fn = new File(getFilePathOnly(file.getPath())+nfn);
                        //System.out.println(fn.getPath());
                        if(!file.renameTo(fn)) {
                            MDS_OptionPane.showMessageDialog(fpml.getListener__MDS_Frame(), "Uable to rename the Directory.", "Error Renaming File / Directory", JOptionPane.ERROR_MESSAGE);      
                        } else {
                            //fsl.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.RENAME_FILE));    
////                            MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.RENAME_FILE, fn, file));
                        }        
                    }
                } else if(e.getActionCommand().equals("Properties")) {
                    showFileProperties(file);
                } else if(e.getActionCommand().equals("Delete")) {
                    if(MDS_OptionPane.showConfirmDialog(fpml.getListener__MDS_Frame(), "Are you sure you want to delete '"+ file.getName()+"'", "Confirm File Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if(file.isDirectory()) {
                            deleteDir(file);
                        } else {
                            if(!file.delete()) {
                                MDS_OptionPane.showMessageDialog(fpml.getListener__MDS_Frame(), "Uable to delete the file '"+ file.getName()+"'", "File Deletion", JOptionPane.ERROR_MESSAGE);                          
                            } else {
                                //fsl.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.DELETE_FILE));
////                                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DELETE_FILE, file));
                            }
                        }    
                    }    
                } else if(e.getActionCommand().equals("Copy To")) {
                    String des = "";
                    MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
                    if(fmdc.showDiaog(fpml.getListener__MDS_Frame()) ==  fmdc.APPROVE_OPTION) {
                        des = fmdc.getPath();
                        if(file.isDirectory()) {
                            MDS.getFileManager().copyFile(file, new File(des), true);
                        } else {
                            MDS.getFileManager().copyFile(file, new File(des+file.getName()), true);    
                        }
                            
                    }                      
                } else if(e.getActionCommand().equals("Move To")) {
                    String des = "";
                    MDS_DirectoryChooser fmdc = new MDS_DirectoryChooser();
                    if(fmdc.showDiaog(fpml.getListener__MDS_Frame()) ==  fmdc.APPROVE_OPTION) {
                        des = fmdc.getPath();
                        if(file.isDirectory()) {
                        	new ConcurrentModificationException("File / Directory deletion error. File / Directory is being used by another program or IO stream");
                            //MDS.getExceptionManager().showException(new ConcurrentModificationException("File / Directory deletion error. File / Directory is being used by another program or IO stream"));    
                        } else {
                            if(!file.canWrite()) {
                                if(MDS_OptionPane.showConfirmDialog(fpml.getListener__MDS_Frame(), "Are you sure you want move the Read Only file : "+file.getName(), "Confirm File Move",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {      
                                    MDS.getFileManager().moveFile(file, new File(des+file.getName()), true); 
                                }                                
                            } else {
                                MDS.getFileManager().moveFile(file, new File(des+file.getName()), true); 
                            }
                        }
                    }               
                } else if(e.getActionCommand().equals("Compress")) {
                    MDS_Compression cmpr = new MDS_Compression();
                    cmpr.compress_Multiple_ZIP(file);           
                } else if(e.getActionCommand().equals("Decompress")) {
                    MDS_Compression cmpr = new MDS_Compression();
                    cmpr.decompress_Multiple_ZIP(file);                   
                } else if(e.getActionCommand().equals("Open")) {        
                    fpml.openFile(file);   
                } else if(e.getActionCommand().equals("Open With CCW")) {
                    prm.execute(new File(MDS.getBinaryPath(), "ClassConnectionWalker.class"), new String[] {file.getPath()});                     
                } else if(e.getActionCommand().equals("Copy")) {
                    clipBoard.setContent(file ,clipBoard.STATUS_COPIED);
                } else if(e.getActionCommand().equals("Cut")) {
                    clipBoard.setContent(file ,clipBoard.STATUS_MOVED);               
                } else if(e.getActionCommand().equals("3.5 Floppy")) {
                    FileSystemView fsv = FileSystemView.getFileSystemView();
                    if(fsv.isFloppyDrive(new File("A:\\"))) {
                        if(file.isDirectory()) {
                            MDS.getFileManager().copyFile(file, new File("A:\\"),true);    
                        } else {
                            MDS.getFileManager().copyFile(file, new File("A:\\"), true);    
                        }                         
                    }                
                } else if(e.getActionCommand().equals("Home Directory")) {
                    if(file.isDirectory()) {
                        MDS.getFileManager().copyFile(file, new File(System.getProperty("user.home")+"\\"),true);    
                    } else {
                        MDS.getFileManager().copyFile(file, new File(System.getProperty("user.home")+"\\"+file.getName()), true);    
                    }                
                } else if(e.getActionCommand().equals("Find Files")) {
                    String[] absPath ={file.getPath()};
                    MDS.getProcessManager().execute(new File(MDS.getBinaryPath(),"FindFiles"), absPath);
                } else if(e.getActionCommand().equals("Another Computer")) {
                    MDS.getBitExchanger().launch(file);
                } else if(e.getActionCommand().equals("Open with Sun Glasses")) {
                    MDS.getProcessManager().execute(new File(MDS.getBinaryPath(), "SunGlasses.class"), new String[] {file.getPath()});    
                } else if(e.getActionCommand().equals("Print")) {
                    MDS.getPrinter().print(new ImageIcon(file.getPath()));
                }
            
                //target.updateFileBrowser();
                
            //} catch(Exception ex) {
                //MDS_OptionPane.showMessageDialog(fsl.getListener_Frame(), ex.toString(), "Error Occured", JOptionPane.INFORMATION_MESSAGE); 
                //ex.printStackTrace();   
            //}    
                
        }
            
            
            
    }     
    
    
    
    
    
    class FileCopy extends Thread implements ActionListener {
    
    
    
        DataInputStream disIn;
        DataOutputStream dosOut;
        File srcFile;
        File desFile;
        
        MDS_Frame fCopy;
        JComponent contentPane;
        MDS_ProgressBar cProgress;
        MDS_Label lblSource;
        MDS_Label lblDestination;
        MDS_Label lblCurrentStatus;
        MDS_Label lblBufferSize;
        
        MDS_Button btnCancel;
        
        //FileSystemListener fsl;
//        HalfLifeClient hc = null;
        
        
    
        public FileCopy(File sFile, File dFile) {
            super();
            try {
                srcFile = sFile;
                desFile = dFile;
            }catch(Exception ex) {
                 
            }
            //fsl = l;
            fCopy = new MDS_Frame("Copying", false, true);
            fCopy.setBounds(20,20,400,220);
            contentPane = (JComponent) fCopy.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new MDS_Label("Source :          "+srcFile.getPath());
            lblSource.setBounds(10,0,370,40);
            contentPane.add(lblSource);

            lblDestination = new MDS_Label("Destination :   "+desFile.getPath());
            lblDestination.setBounds(10,20,370,40);
            contentPane.add(lblDestination);
                        
            String slen = String.valueOf(srcFile.length()/1024);
            Integer i = new Integer(slen);
            int max = i.intValue();
                      
            cProgress = new MDS_ProgressBar(0,max);
            cProgress.setBounds(10,65,370, 20);
            cProgress.setStringPainted(true);
            
            lblCurrentStatus = new MDS_Label("");
            lblCurrentStatus.setBounds(10, 85, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            lblBufferSize = new MDS_Label("Buffer size 1024 bytes");
            lblBufferSize.setBounds(10, 105, 370, 40);
            contentPane.add(lblBufferSize);
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.addActionListener(this);
            btnCancel.setBounds(280, 145, 100, 30);
            contentPane.add(btnCancel);
            contentPane.add(cProgress);
            
            
            
            fCopy.setVisible(true);
            
            if(desFile.exists()) {
                String msg = "This folder already contains file named "+srcFile.getName();
                msg = msg.concat("\nWould you like to replace the existing"+"\n"+String.valueOf(desFile.length())+"\n"+getLastModified_As_String(desFile.lastModified()));
                msg = msg.concat("\nwith this one?");
                double size = srcFile.length();
                size = size/1024/1024;
                msg = msg.concat("\n"+String.valueOf(size)+"\n"+getLastModified_As_String(srcFile.lastModified())); 
                int result = MDS_OptionPane.showConfirmDialog(fCopy, msg,"Replace File Confirm", JOptionPane.YES_NO_OPTION);                
                if(result == JOptionPane.YES_OPTION) {
                    //if(dFile.delete()) MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DELETE_FILE, dFile));    
                	dFile.delete();
                } else { 
                    fCopy.dispose();
                }
                
            }
			
			
            try {
                disIn = new DataInputStream(new FileInputStream(sFile));   
                dosOut = new DataOutputStream(new FileOutputStream(dFile));      
                start();    
            }catch(Exception ex) {
                fCopy.dispose();  
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "File Copy", JOptionPane.ERROR_MESSAGE);      
            } 
            
        }

        public void run() {
        	
            try{
                int off = 0;
                int len = 1024;
        
                long fLen = srcFile.length();
        
                long cfLen = 0;
                long cfLen2 = 0;
            
                while(disIn.available() != 0) {
                    if((fLen - cfLen) <= len) {
                        String slen = String.valueOf(fLen - cfLen); 
                        Integer i = new Integer(slen); 
                        len = i.intValue(); 
                        cfLen2 = cfLen2 + i.intValue();    
                    } else {            
                        cfLen = cfLen+len;
                        cfLen2 = cfLen2 + len;
                    }
            
                    byte b[] = new byte[len];
                    disIn.read(b,off,len);
                    dosOut.write(b,off,len);
 
                    
                    String slen = String.valueOf(cfLen/1024); 
                    Integer i = new Integer(slen); 
                    int plen = i.intValue();
                        
                    cProgress.setValue(plen); 
                    double cl = cfLen2;
                    double l = fLen;
                    Double val = new Double(Math.abs(cl/l*100));
                    cProgress.setString(String.valueOf(val.intValue())+" %");                                 
                    
                    lblCurrentStatus.setText(getFormatedFileSize(cfLen2)+" of "+getFormatedFileSize(fLen)+" Complete.");
    			
                }
        
                dosOut.close();
	                
                
                fCopy.dispose();
                  
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.COPY_FILE, desFile));            
            } catch(Exception ex) {
                fCopy.dispose();
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "File Copy", JOptionPane.ERROR_MESSAGE); 
            }           
        
        } 
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
            	try {
//            		hc.disconnect();
                	this.interrupt();
                	fCopy.dispose();
            	} catch(Exception ex) {
            		ex.printStackTrace();
            	}
            }
        }       
        
        
    }
    
    
    
    
    
    class MoveFile extends Thread implements ActionListener {
    
    
    
        DataInputStream disIn;
        DataOutputStream dosOut;
        File srcFile;
        File desFile;
        
        MDS_Frame fCopy;
        JComponent contentPane;
        MDS_ProgressBar cProgress;
        MDS_Label lblSource;
        MDS_Label lblDestination;
        MDS_Label lblCurrentStatus;
        MDS_Label lblBufferSize;
        
        MDS_Button btnCancel;
        
        //FileSystemListener fsl;
        
        
    
        public MoveFile(File sFile, File dFile) {
            super();
            try {
                srcFile = sFile;
                desFile = dFile;
            }catch(Exception ex) {
                 
            }
            //fsl = l;
            fCopy = new MDS_Frame("Copying", false, true);
            fCopy.setBounds(20,20,400,220);
            contentPane = (JComponent) fCopy.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new MDS_Label("Source :          "+srcFile.getPath());
            lblSource.setBounds(10,0,370,40);
            contentPane.add(lblSource);

            lblDestination = new MDS_Label("Destination :   "+desFile.getPath());
            lblDestination.setBounds(10,20,370,40);
            contentPane.add(lblDestination);
                        
            String slen = String.valueOf(srcFile.length()/1024);
            Integer i = new Integer(slen);
            int max = i.intValue();
                      
            cProgress = new MDS_ProgressBar(0,max);
            cProgress.setBounds(10,65,370, 20);
            cProgress.setStringPainted(true);
            
            lblCurrentStatus = new MDS_Label("");
            lblCurrentStatus.setBounds(10, 85, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            lblBufferSize = new MDS_Label("Buffer size 1024 bytes");
            lblBufferSize.setBounds(10, 105, 370, 40);
            contentPane.add(lblBufferSize);
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.addActionListener(this);
            btnCancel.setBounds(280, 145, 100, 30);
            contentPane.add(btnCancel);
            contentPane.add(cProgress);
            
            
            
            fCopy.setVisible(true);
            
            if(desFile.exists()) {
                String msg = "This folder already contains file named "+srcFile.getName();
                msg = msg.concat("\nWould you like to replace the existing"+"\n"+String.valueOf(desFile.length())+"\n"+getLastModified_As_String(desFile.lastModified()));
                msg = msg.concat("\nwith this one?");
                double size = srcFile.length();
                size = size/1024/1024;
                msg = msg.concat("\n"+String.valueOf(size)+"\n"+getLastModified_As_String(srcFile.lastModified())); 
                int result = MDS_OptionPane.showConfirmDialog(fCopy, msg,"Replace File Confirm", JOptionPane.YES_NO_OPTION);                
                if(result == JOptionPane.YES_OPTION) {
                   
                } else { 
                    fCopy.dispose();
                }
                
            }

            try {
                disIn = new DataInputStream(new FileInputStream(sFile));   
                dosOut = new DataOutputStream(new FileOutputStream(dFile));      
                start();    
            }catch(Exception ex) {
                fCopy.dispose();  
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "File Copy", JOptionPane.ERROR_MESSAGE);      
            }            
            
            
        }

        public void run() {
        
            try{
                int off = 0;
                int len = 1024;
        
                long fLen = srcFile.length();
        
                long cfLen = 0;
                long cfLen2 = 0;
                
                while(disIn.available() != 0) {
            
                    if((fLen - cfLen) <= len) {
                        String slen = String.valueOf(fLen - cfLen); 
                        Integer i = new Integer(slen); 
                        len = i.intValue(); 
                        cfLen2 = cfLen2 + i.intValue();    
                    } else {            
                        cfLen = cfLen+len;
                        cfLen2 = cfLen2 + len;
                    }
            
                    byte b[] = new byte[len];
                    disIn.read(b,off,len);
                    dosOut.write(b,off,len);
 					
                    
                    String slen = String.valueOf(cfLen/1024); 
                    Integer i = new Integer(slen); 
                    int plen = i.intValue();
                        
                    cProgress.setValue(plen); 
                    double cl = cfLen2;
                    double l = fLen;
                    Double val = new Double(Math.abs(cl/l*100));
                    cProgress.setString(String.valueOf(val.intValue())+" %");                                 
                    																		
                    lblCurrentStatus.setText(getFormatedFileSize(cfLen2)+" of "+getFormatedFileSize(fLen)+" Complete.");
            		
                }
        
                dosOut.close();
                
                srcFile.delete();
                
                fCopy.dispose();
                  
////                MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.COPY_FILE, desFile));            
            } catch(Exception ex) {
                fCopy.dispose();
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "File Copy", JOptionPane.ERROR_MESSAGE); 
            }           
        
        } 
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                this.interrupt();
                fCopy.dispose();
            }
        }       
        
        
    }    
    
    
    
    
    
    class CopyDir extends MDS_Frame implements Runnable, ActionListener {
    
    
    
        DataInputStream disIn;
        DataOutputStream dosOut;
        
        JComponent contentPane;
        MDS_ProgressBar prgbFile;
        MDS_Label lblSource;
        MDS_Label lblDestination;
        MDS_Label lblCurrentFile;
        MDS_Label lblCurrentStatus;
        MDS_Label lblCurrentStatusAll;        
        MDS_Label lblBufferSize;
        MDS_ProgressBar prgbAll;
        JSeparator sptr = new JSeparator(SwingConstants.HORIZONTAL);
        
        MDS_Button btnCancel;
        
        boolean stop = false;
        
        Vector vctScrDirList = new Vector();    
        Vector vctScrFileList = new Vector();
        
        File scr = null;
        File des = null;
        
        Thread thr = new Thread(this);
        
        double totalFileLength = 0;
        double currentTotalFileLength = 0;
        //FileSystemListener fsl;
         
    
    
        public CopyDir(File scrDir, File desDir, boolean ow) {
            super("Preparing to Copy", false, true);
            //fsl = l;
            scr = scrDir;
            des = desDir;
            
            this.setBounds(20,20,400,290);
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(null);  
            
            lblSource = new MDS_Label("Source :          "+scrDir.getPath());
            lblSource.setBounds(10,0,370,40);
            contentPane.add(lblSource);

            lblDestination = new MDS_Label("Destination :   "+desDir.getPath());
            lblDestination.setBounds(10,20,370,40);
            contentPane.add(lblDestination);
            
            lblCurrentFile = new MDS_Label("Current file: ");
            lblCurrentFile.setBounds(10, 40, 370, 40);
            contentPane.add(lblCurrentFile);
                        
            String slen = String.valueOf(1024);
            Integer i = new Integer(slen);
            int max = i.intValue();
                      
            prgbFile = new MDS_ProgressBar(0, 100);
            prgbFile.setBounds(10,75,370, 20);
            prgbFile.setStringPainted(true);
            contentPane.add(prgbFile);
            
            lblCurrentStatus = new MDS_Label();
            lblCurrentStatus.setBounds(10, 90, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            sptr.setBounds(10, 120, 370, 4);
            contentPane.add(sptr);
            
            lblCurrentStatusAll = new MDS_Label();
            lblCurrentStatusAll.setBounds(10, 120, 370, 40);
            contentPane.add(lblCurrentStatusAll);
            
            prgbAll = new MDS_ProgressBar(0, 100);
            prgbAll.setBounds(10, 150, 370, 20);
            prgbAll.setStringPainted(true);
            contentPane.add(prgbAll);       
            
            lblBufferSize = new MDS_Label("Buffer size 1024 bytes");
            lblBufferSize.setBounds(10, 170, 370, 40);
            contentPane.add(lblBufferSize);
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.addActionListener(this);
            btnCancel.setBounds(280, 200, 100, 30);
            contentPane.add(btnCancel);
            
            
            this.setVisible(true);  
            
            thr.start(); 
                
        }
        
        
        
        public synchronized void dive(String name) {
            try {
                File file = new File(name);
                if (file != null && file.isDirectory()) {
                    String files[] = file.list();
                    for (int i = 0; i < files.length; i++) {
                        File leafFile = new File(file.getAbsolutePath(), files[i]);
                        if (leafFile.isDirectory()) {
                            // **identify(leafFile);   
                            vctScrDirList.add(leafFile);
                            if(!stop) {
                                dive(leafFile.getAbsolutePath());
                            }
                        } else {
                            // **identify(leafFile);
                            totalFileLength = totalFileLength + leafFile.length();
                            vctScrFileList.add(leafFile);
                        }
                    }
                } else if (file != null && file.exists()) {
                    // **identify(file);
                    totalFileLength = totalFileLength + file.length();
                    vctScrFileList.add(file);
                }
            } catch (SecurityException ex) {
                this.dispose();
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "Directory Copy Error", JOptionPane.ERROR_MESSAGE); 
            } catch (Exception ex) {
                this.dispose();
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "Directory Copy Error", JOptionPane.ERROR_MESSAGE); 
            }
            
        }        
        
        
        
        public void run() {
            try {
                dive(scr.getPath());   
                File csf = null;
                File cdf = null;
                int off = 0;
                int len = 1024;  
                int scrRootLen = scr.getPath().length();
                File desRoot = new File(des.getPath()+"\\"+scr.getName());
            
                if(!desRoot.mkdir()) {
                    throw new RuntimeException(desRoot.getPath()+"  Device not ready.\n\n Specified device may be read only.");
                }
       
            
                for(int x=0;x<=vctScrDirList.size()-1;x++) {
                    csf = (File)vctScrDirList.elementAt(x); 
                    cdf = new File(desRoot+csf.getPath().substring(scrRootLen ,csf.getPath().length()));
                    cdf.mkdir();
                 
                }  
            
                for(int x=0;x<=vctScrFileList.size()-1;x++) {
                    csf = (File)vctScrFileList.elementAt(x);
                    cdf = new File(desRoot+csf.getPath().substring(scrRootLen ,csf.getPath().length()));
                
                    lblCurrentFile.setText("Current file: "+cdf.getName());
                    lblCurrentStatusAll.setText(String.valueOf(x+1)+" Of "+String.valueOf(vctScrFileList.size())+" Files Complete.");
                               
                    disIn = new DataInputStream(new FileInputStream(csf));   
                    dosOut = new DataOutputStream(new FileOutputStream(cdf));                 
                    
                    long fLen = csf.length();
                    double fLen2 = csf.length();
                    long cfLen = 0;
                    double cfLen2 = 0;
                    
                    while(disIn.available() != 0) {
                        if((fLen - cfLen) <= len) {
                            String slen = String.valueOf(fLen - cfLen); 
                            Integer i = new Integer(slen); 
                            len = i.intValue();
                            cfLen2 = cfLen2+len;    
                        } else {            
                            cfLen = cfLen+len;
                            cfLen2 = cfLen2+len;
                        }
                                                
                        byte b[] = new byte[len];
                        disIn.read(b,off,len);
                        dosOut.write(b,off,len);
                        
                        lblCurrentStatus.setText(getFormatedFileSize(cfLen)+" Of "+getFormatedFileSize(fLen)+" Complete.");
                        
                        Double val = new Double((cfLen2/fLen2)*100);
                        
                        prgbFile.setValue(val.intValue());
                        
                        currentTotalFileLength = currentTotalFileLength + len;
                        val = new Double((currentTotalFileLength / totalFileLength) * 100);
                        prgbAll.setValue(val.intValue());                        
                                            
                    }
                    
                    
              
                }
                
                 dosOut.close();

////                 MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.COPY_FILE, desRoot));
                    
                 this.dispose();                  
            
            } catch(Exception ex) {        
                this.dispose();
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "Directory Copy Error", JOptionPane.ERROR_MESSAGE);                    
            }          
            
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                if(thr != null) {
                    thr.interrupt();
                }
                thr = null;
                this.dispose();
            }
        }          
        
        
        
        
           
    } 
    
    
    
    
    
    public class DeleteDir extends MDS_Frame implements Runnable, ActionListener {
    
    
    
        String delPath = null;
        double nItems = 0;
        double currentItemCount = 0;
        double status = 0;
         
        JComponent contentPane;
        MDS_Label lblSource;
        MDS_Label lblDestination;
        MDS_Label lblCurrentFile;
        MDS_Label lblCurrentStatus;
        MDS_Label lblCurrentStatusAll;        
        MDS_Label lblBufferSize;
        MDS_ProgressBar prgbDel;
        
        MDS_Button btnCancel;
        
        Thread thr = new Thread(this);
        
        final int ESTIMATION = 904823;
        final int DELETION_FILE = 234423;
        int mode = 0;
        
        boolean continueDeletion = true;
        
        Vector vctDirList = new Vector(); 
        
        //FileManager.FileBrowser target_Fb = null;
        //FileSystemListener fsl; 
    
    
        public DeleteDir(File dir) {
            super("Preparing to Delete", false, true);
            
            delPath = dir.getPath();
            //fsl = l;
            
            this.setBounds(20,20,400,200);
            contentPane = (JComponent) this.getContentPane();
            contentPane.setLayout(null);  
            
            lblCurrentStatus = new MDS_Label("Deleting ...");
            lblCurrentStatus.setBounds(10, 10, 370, 40);
            contentPane.add(lblCurrentStatus);
            
            
            prgbDel = new MDS_ProgressBar(0, 100);
            prgbDel.setBounds(10, 60, 370, 20);
            prgbDel.setStringPainted(true);
            contentPane.add(prgbDel);       
            
            btnCancel = new MDS_Button("Cancel");
            btnCancel.addActionListener(this);
            btnCancel.setBounds(280, 110, 100, 30);
            contentPane.add(btnCancel);
                       
            this.setVisible(true);  
            
            thr.start();         
        }
        
        
        
        public synchronized void dive(String name ,int currentMode) {
            try {
                File file = new File(name);
                if (file != null && file.isDirectory()) {
                    String files[] = file.list();
                    for (int i = 0; i < files.length; i++) {
                        File leafFile = new File(file.getAbsolutePath(), files[i]);
                        if (leafFile.isDirectory()) {
                            // **identify(leafFile);
                            if(currentMode == ESTIMATION) {   
                                nItems = nItems + 1;
                                vctDirList.add(leafFile);
                            } else if(currentMode == DELETION_FILE) {
                                leafFile.delete();
                                currentItemCount++;
                                setProgress();
                            }
                            if(continueDeletion) {
                                dive(leafFile.getAbsolutePath(), currentMode);
                            }
                        } else {
                            if(currentMode == ESTIMATION) {   
                                nItems = nItems + 1;
                                vctDirList.add(leafFile);
                            } else if(currentMode == DELETION_FILE) {
                                leafFile.delete();
                                currentItemCount++;
                                setProgress();
                            }
                        }
                    }
                } else if (file != null && file.exists()) {
                    if(currentMode == ESTIMATION) {   
                        nItems = nItems + 1;
                        vctDirList.add(file);
                    } else if(currentMode == DELETION_FILE) {
                        file.delete();
                        currentItemCount++;
                        setProgress();
                    }
                }
            } catch (SecurityException ex) {
                this.dispose();
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "Directory Deletion", JOptionPane.ERROR_MESSAGE);   
            } catch (Exception ex) {
                this.dispose();
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "Directory Deletion", JOptionPane.ERROR_MESSAGE);  
            }
            
        }    
        
        
        
        public void setProgress() {
            status = (currentItemCount / nItems) * 100;
            Double val = new Double(status);
            prgbDel.setValue(Math.abs(val.intValue()));
            //lblCurrentStatus.setText("Deleting "+String.valueOf(currentItemCount)+" of "+nItems);        
        }            
        
        
        
        public void run() {
            dive(delPath, ESTIMATION);
            nItems = nItems + 1;
            this.setTitle("Deletion");
            dive(delPath, DELETION_FILE);
            
            Vector vctDirList_InOrder = new Vector();
        
            for(int x = vctDirList.size()-1; x>=0; x--) {
                vctDirList_InOrder.add(vctDirList.elementAt(x));

            }
            
            for(int x=0;x<=vctDirList_InOrder.size()-1;x++) {
                if(((File)vctDirList_InOrder.elementAt(x)).delete()) {
                    //vctDirList_InOrder.removeElementAt(x);    
                }
                
                currentItemCount++;
                setProgress();                
                
            }
            
            File fileDelRoot = new File(delPath);
            fileDelRoot.delete();
            
            currentItemCount++;
            setProgress();            
            
            //target_Fb.updateFileBrowser();
            //fsl.fileSystemUpdated(new FileSystemEvent(FileSystemEvent.DELETE_FILE));
////            MDS.getFileSystemEventManager().fireFileSystemEvent(new FileSystemEvent(FileSystemEvent.DELETE_FILE, new File(delPath)));
            this.dispose();
            
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Cancel")) {
                if(thr != null) {
                    thr.interrupt();
                }
                
                thr = null;
                this.dispose();
            }
        }         
        
        
        
    }       
    
     
}    