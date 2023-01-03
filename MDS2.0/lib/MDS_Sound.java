/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

import java.net.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.util.*;
import java.util.logging.*;


public final class MDS_Sound implements MDS_PropertyChangeListener {


    
    private static MDS_Sound snd = new MDS_Sound();
    double gain = 0;
    int pan = 0;
    
    Sequencer sequencer;
    Synthesizer synthesizer;
    MidiChannel channels[];
	
	DestroyClip dscp = new DestroyClip();
    
    Vector vctCurrentSounds = new Vector();
    Vector vctCurrentSequencers = new Vector();
    
    private boolean mute = false;
    
    private MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    
    private Logger log = Logger.getLogger("MDS_Sound");



    private MDS_Sound() {      
        try {
        	log.info("Init.");       
        	sequencer = MidiSystem.getSequencer();           
	        if (sequencer instanceof Synthesizer) {
            	synthesizer = (Synthesizer)sequencer;
	            channels = synthesizer.getChannels();
	      	} 
	      	sequencer.open();	
	      	synthesizer = MidiSystem.getSynthesizer();
	      	synthesizer.open();
//	      	sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
	        channels = synthesizer.getChannels();	
        } catch (Exception ex) { 
            ex.printStackTrace();
        } 
        log.info("Loading sound properties.");	
        getPropeties();
        ppm.addMDS_PropertyChangeListener(this);
        	   
    }
    
    
    
    public static MDS_Sound getMDS_Sound() {
        return snd;
    }
    
    
    private void getPropeties() {
    	
        MDS_Property propDp = ppm.getProperty(SoundProperties.PROPERTY_NAME);
        setGain(Integer.parseInt(propDp.getSupProperty(SoundProperties.PROPERTY_GAIN)));
        setPan(Integer.parseInt(propDp.getSupProperty(SoundProperties.PROPERTY_PAN)));
        
        mute = Boolean.parseBoolean(propDp.getSupProperty(SoundProperties.PROPERTY_MUTE));
    }    
    
    
    
    public File getDefaultSoundDirectory() {
    	return new File("resources\\sound\\");
    }    
    
    
    

    
    
    
    public synchronized Object loadSound(File f, Sequencer sequencer) {
    
        Object currentSound = null;
        
        try {
            currentSound = AudioSystem.getAudioInputStream(f);
           
        } catch(Exception ex) {
            //System.out.println("ERROR1  "+ex.toString());
 
            try { 
                currentSound = MidiSystem.getSequence(f);
            } catch (Exception e2) { 
                try { 
                    FileInputStream is = new FileInputStream(f);
                    currentSound = new BufferedInputStream(is, 1024);
                } catch (Exception e3) { 
                    //System.out.println("ERROR2  "+ex.toString()); 
			              currentSound = null;  
                }
            }          
                  
        }
        
        if(currentSound instanceof AudioInputStream) {
            try { 
                AudioInputStream stream = (AudioInputStream) currentSound;
                AudioFormat format = stream.getFormat();

                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                    AudioFormat tmp = new AudioFormat(
                                                      AudioFormat.Encoding.PCM_SIGNED, 
                                                      format.getSampleRate(),
                                                      format.getSampleSizeInBits() * 2,
                                                      format.getChannels(),
                                                      format.getFrameSize() * 2,
                                                      format.getFrameRate(),
                                                      true);
                    stream = AudioSystem.getAudioInputStream(tmp, stream);
                    format = tmp;
                }
            
                DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));

                Clip clip = (Clip) AudioSystem.getLine(info);
                //clip.addLineListener(this);
                clip.open(stream);
                currentSound = clip;
                //seekSlider.setMaximum((int) stream.getFrameLength());         
            }catch(Exception ex) {
     
            }
        
        } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
            try {
                          
                sequencer.open();
                if (currentSound instanceof Sequence) {
                    sequencer.setSequence((Sequence) currentSound);
                } else {
                    sequencer.setSequence((BufferedInputStream) currentSound);
                } 
                //currentSound = sequencer;           
            }catch(Exception ex) {
                //System.out.println("ERROR2  "+ex.toString());
            }
        }
        
        if(currentSound != null) {;
            vctCurrentSounds.addElement(currentSound);
        }
        
        if(sequencer != null) {
            vctCurrentSounds.addElement(sequencer);
            //vctCurrentSequencers.addElement(sequencer);
        }       
		
		dscp.add(currentSound); 
        
        return currentSound;
    }
    
    
    
    public synchronized void playSound(Object sound, Sequencer s) {
	    if (sound instanceof Clip) { 
	        Clip clip = (Clip) sound;  
	        clip.start();    
	    } else if(sound instanceof Sequence || sound instanceof BufferedInputStream) {
	        s.start();
	    }
    }
    
    
    
    public synchronized void playSound(File f) {
        MDS_Sound snd = MDS.getSound();
        Object o = snd.loadSound(f, null);
        snd.playSound(o,null);  
		dscp.add(o);
    }
    
    
    
    public Sequencer getSequencer() {
        /*
        Sequencer sequencer = null;
        
        try {
            sequencer = MidiSystem.getSequencer();
            if(sequencer instanceof Synthesizer) {
                //synthesizer = (Synthesizer)sequencer;
	          //channels = synthesizer.getChannels();
	          }  
        }catch(Exception ex) {
	          System.out.println(ex.toString());
	      }
	  */ 
        return sequencer; 
	       
    }
    
    
    
    public boolean isMute() {
    	return mute;
    }
    
    
    
    public synchronized void setMute(boolean mute) {
    	this.mute = mute; 	
    }
    
    
    
    public synchronized void setGain(double val) {
                
        gain = val;        
        double value = val / 100.0;
                
        for(int x=0;x<=vctCurrentSounds.size()-1;x++) { 
            Object currentSound = vctCurrentSounds.elementAt(x);    
            if (currentSound instanceof Clip) {
                try {
                    Clip clip = (Clip) currentSound;
					if(clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
	                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	                    float dB = (float) (Math.log(value==0.0?0.0001:value)/Math.log(10.0)*20.0);
	                    gainControl.setValue(dB);
					}
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
////                for (int i = 0; i < channels.length; i++) {                
////		        	channels[i].controlChange(7, (int)(value * 127.0));
////		        	System.out.println(channels[i].toString());
////		    	}
            }        
        }  
    }
    
    
    
    public synchronized void setPan(int val) {
        pan = val;
        int value = val;

        for(int x=0;x<=vctCurrentSounds.size()-1;x++) { 
            Object currentSound = vctCurrentSounds.elementAt(x);  
            if (currentSound instanceof Clip) {
                try {
                    Clip clip = (Clip) currentSound;
                    if(clip.isControlSupported(FloatControl.Type.PAN)) {
	                    FloatControl panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
	                    panControl.setValue(value/100.0f);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
////                for (int i = 0; i < channels.length; i++) {                
////		        	channels[i].controlChange(10, (int)(((double)value + 100.0) / 200.0 *  127.0));
////                }															 
            } 
        }   
    }
    
    
    
    public void propertyChanged(MDS_PropertyChangeEvent e) {
////    	if(e.getPropertyName().equals(SoundProperties.PROPERTY_NAME)) {
////    		getPropeties();
////    	}	
    } 
	
	
	
	
	
	class DestroyClip extends Thread
	{
	
	
		private Vector vctCurrentSounds = new Vector();
	
	
	    public DestroyClip() 
	    {
			super("Destory Clip");
			this.setPriority(Thread.MIN_PRIORITY);
			this.start();
	    }
		
		
		
		public void add(Object sound) 
		{
			if(!vctCurrentSounds.contains(sound)) {
				vctCurrentSounds.addElement(sound);	
			}
		}
		
		
		public void run() 
		{
			while (true) 
			{
				try 
				{
					Thread.sleep(500);
				} catch(InterruptedException  ex) {}
				
				for(int x=0;x<=vctCurrentSounds.size()-1;x++) {
					Object sound = vctCurrentSounds.elementAt(x);
   		     		if (sound instanceof Clip) { 
  		      		    Clip clip = (Clip) sound;
						if (!clip.isActive()) 
						{
						    clip.stop();	
     				        clip.close();  
                            vctCurrentSounds.remove(sound);	
						}						  
     			   	} else if(sound instanceof Sequence || sound instanceof BufferedInputStream) {
     			       //System.out.println("Sequencer....");
//					   Sequencer s = (Sequencer)sound;
//					   if (!s.isRunning()) 
//					   {
//						   s.stop();
//						   s.close();
//						   vctCurrentSounds.remove(sound);	
//					   }
				   
     			   }
				}
			}
		}
		
		
	}   
    
    
    
    public static void main(String a[]) {
        MDS_Sound s = MDS_Sound.getMDS_Sound();
        Sequencer sq = s.getSequencer();
        //Object o1 = s.loadSound(new File("E:\\Temp1\\Mission_Impossible.mid"),sq);
        Object o1 = s.loadSound(new File("E:\\MDS2.0\\MDS2.0\\resources\\sound\\MDS_Startup.wav"),sq);        
        s.playSound(o1,sq);
        /*
        Sequencer sq1 = s.getSequencer();
        Object o2 = s.loadSound(new File("E:\\Temp1\\mpostman.mid"), sq1);
        s.playSound(o2,sq1); 
        
        Object o3 = s.loadSound(new File("E:\\Temp1\\sounds\\KDE_Startup.wav"), null);
        s.playSound(o3,null);
        */
    }
    
    
    
}     