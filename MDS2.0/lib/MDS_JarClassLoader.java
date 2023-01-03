
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import java.util.jar.*;
import java.lang.reflect.*;


public class MDS_JarClassLoader extends ClassLoader {



	 ZipFile zipFile;
	 private Map classes = new HashMap();
	 
	 
	 
	public MDS_JarClassLoader(){} 



	public MDS_JarClassLoader(File jarFile) {

		try {
			System.out.println("Jar : "+jarFile);
			zipFile = new ZipFile(jarFile.getPath());
		} catch(Exception ex) {
			ex.printStackTrace();   
		}
	}
	
	
	
	public boolean isExecutable(File jf) {
		boolean b = true;
		if(jf.getName().endsWith(".jar")) {
			try {
				JarFile jarFile = new JarFile(jf);
				Manifest mf = jarFile.getManifest();
				String mainClassName = mf.getMainAttributes().getValue("Main-Class");
				if(mainClassName.length() == 0) b = false;
				else {//b = true;
					String argv[] = new String[] {};
	    			MDS_JarClassLoader jcls = new MDS_JarClassLoader(jf);
	    			Class cls = jcls.loadClass(mainClassName, false);
                    Method m = cls.getMethod("MDS_Main", new Class[] { argv.getClass() });	
                    b = true;					
				}
			} catch(Exception ex) {
				b = false;	
			}
			
		} else b = false;
		
		System.out.println("isExecutable : "+jf.getPath()+" : "+b);
		
		return b;
		
	}
	
	
	
	public final Class loadClass(String clazz, boolean resolve) throws ClassNotFoundException {
        Class cl = (Class)classes.get(clazz);
        Class sysCls = null;
        
		System.out.println("ClassName : "+clazz);
        
        if (cl == null) { 
			try {
	
				ZipEntry entry = zipFile.getEntry(clazz+".class");
				
				System.out.println("ZipEntry : "+entry); 
	
	     		if (entry == null)
	      		{
		            try {  
		                sysCls = findSystemClass(clazz);   
		                System.out.println("SystemClass : "+sysCls.getName());        
		                //if(checkForIllegalClasses) checkIllegalClass(sysCls.getName());
		                return sysCls;
		            }
		            catch (ClassNotFoundException e) {}
		            catch (NoClassDefFoundError e) {}
		            catch (IllegalClassException e) {  
		                //showIllegalClassInfo(sysCls, e);
						e.printStackTrace(); 
		                return null;
		            }
				
	      		}
				
				InputStream in = zipFile.getInputStream(entry);
	
				int len = (int) entry.getSize();
				byte[] data = new byte[len];
				int success = 0;
				int offset = 0;
				while (success < len)
				{
				  	len -= success;
					offset += success;
				    success = in.read(data, offset, len);
				    if (success == -1)
				    {
				      //String[] args = { clazz, zipFile.getName()};
				      //System.err.println(Jext.getProperty("jar.error.zip", args));
				      throw new ClassNotFoundException(clazz);
				    }
				}							
							
				
				cl = defineClass(clazz, data, 0, data.length);	
				
	            classes.put(clazz, cl);
			}catch(Exception ex) {
				ex.printStackTrace(); 
				throw new ClassNotFoundException(clazz); 
			}			
    	}
		
		//if (resolve) resolveClass(cl);	
		
		return cl;
	}
	
	
}