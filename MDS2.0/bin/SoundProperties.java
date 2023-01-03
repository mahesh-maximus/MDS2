/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;



public class SoundProperties extends ControlModule implements ChangeListener, ActionListener{



    private static SoundProperties sp;
    private static boolean sp_Visibility = false;
    
    MDS_Slider sldGain;
    MDS_Slider sldPan;
    
    MDS_Slider sldVolume;
    MDS_Slider sldBalance;  
    	
    MDS_CheckBox chkMute = new MDS_CheckBox("Mute");	  
    
    MDS_TabbedPane tbpPlayers = new MDS_TabbedPane();
    
    MDS_Sound snd = MDS.getSound();
    MDS_PropertiesManager ppm = MDS.getMDS_PropertiesManager();
    
    public static final String PROPERTY_NAME = "SoundProperties";
    public static final String PROPERTY_GAIN = "gain";
    public static final String PROPERTY_PAN = "pan";
    public static final String PROPERTY_MUTE = "mute";

    public SoundProperties() {
        super("Sound Properties", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-action-equalizer.png"));
        
        MDS_Panel pnlMediaPlayer = new MDS_Panel();
        //pnlMediaPlayer.setLayout(new GridLayout(2,0, 5, 5));
        pnlMediaPlayer.setLayout(new BorderLayout());
        
        MDS_Panel pnlGain  = new MDS_Panel();
        pnlGain.setLayout(new BorderLayout());
        pnlGain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Gain"));
        sldGain = new MDS_Slider(MDS_Slider.VERTICAL, 0, 100, 80);
        sldGain.setPaintTicks(true);
        sldGain.setMajorTickSpacing(10);
        sldGain.setMinorTickSpacing(5);
        sldGain.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        sldGain.addChangeListener(this);
        pnlGain.add(sldGain, BorderLayout.CENTER);
        pnlMediaPlayer.add(pnlGain, BorderLayout.WEST);
 
 
        MDS_Panel pnlPan  = new MDS_Panel();
        pnlPan.setLayout(new BorderLayout());
        pnlPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Pan"));       
        MDS_Panel pnlPictures_Pan = new MDS_Panel(new BorderLayout());
        pnlPictures_Pan.add(new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"sound.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE)), BorderLayout.WEST);
        pnlPictures_Pan.add(new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"left-speaker.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE)), BorderLayout.EAST);
        pnlPan.add(pnlPictures_Pan, BorderLayout.NORTH);
        sldPan = new MDS_Slider(MDS_Slider.HORIZONTAL, -100, 100, 0);
        sldPan.setPaintTicks(true);
        sldPan.setMajorTickSpacing(20);
        sldPan.setMinorTickSpacing(10);        
        sldPan.addChangeListener(this);
        pnlPan.add(sldPan, BorderLayout.CENTER);
        pnlMediaPlayer.add(pnlPan, BorderLayout.SOUTH);  
        	
        	
        MDS_Panel pnlMute = new MDS_Panel(new BorderLayout());
        pnlMute.add(new MDS_Label(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "128-app-multimedia.png")), BorderLayout.CENTER);
       	chkMute.addActionListener(this);
       	pnlMute.add(chkMute, BorderLayout.SOUTH);        	
        pnlMediaPlayer.add(pnlMute, BorderLayout.CENTER); 	
        	
        	      
        tbpPlayers.addTab("Java Sound", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"media-player.png",16 ,16 ,ImageManipulator.ICON_SCALE_TYPE), pnlMediaPlayer);
        
        
        
//////////        MDS_Panel pnlMp3Player = new MDS_Panel();
//////////        pnlMp3Player.setLayout(new GridLayout(2,0, 5, 5));
//////////        
//////////        MDS_Panel pnlVolume  = new MDS_Panel();
//////////        pnlVolume.setLayout(new BorderLayout());
//////////        pnlVolume.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Volume"));
//////////        sldVolume = new MDS_Slider(MDS_Slider.HORIZONTAL, -1500, 0, -640);
//////////        sldVolume.setPaintTicks(true);
//////////        sldVolume.setMajorTickSpacing(100);
//////////        sldVolume.setMinorTickSpacing(50);
//////////        sldVolume.putClientProperty("JSlider.isFilled", Boolean.TRUE);
//////////        sldVolume.addChangeListener(this);
//////////        pnlVolume.add(sldVolume, BorderLayout.CENTER);
//////////        pnlMp3Player.add(pnlVolume);
////////// 
//////////        MDS_Panel pnlBalance  = new MDS_Panel();
//////////        pnlBalance.setLayout(new BorderLayout());
//////////        pnlBalance.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Balance"));       
//////////        MDS_Panel pnlPictures_Bal = new MDS_Panel(new BorderLayout());
//////////        pnlPictures_Bal.add(new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"sound.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE)), BorderLayout.WEST);
//////////        pnlPictures_Bal.add(new MDS_Label(ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"left-speaker.png", 24, 24, ImageManipulator.ICON_SCALE_TYPE)), BorderLayout.EAST);
//////////        pnlBalance.add(pnlPictures_Bal, BorderLayout.NORTH);
//////////        sldBalance = new MDS_Slider(MDS_Slider.HORIZONTAL, -1500, 1500, 0);
//////////        sldBalance.setPaintTicks(true);
//////////        sldBalance.setMajorTickSpacing(200);
//////////        sldBalance.setMinorTickSpacing(100);        
//////////        sldBalance.addChangeListener(this);
//////////        pnlBalance.add(sldBalance, BorderLayout.CENTER);
//////////        pnlMp3Player.add(pnlBalance);        
//////////        tbpPlayers.addTab("Mp3 Player", ImageManipulator.createScaledImageIcon(ImageManipulator.MDS_ICONS_PATH+"mp3-player.png",16 ,16 ,ImageManipulator.ICON_SCALE_TYPE), pnlMp3Player);        
        
        
        this.addJComponent(tbpPlayers);
        
       	getReg_Data();
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                sp_Visibility = false;
                sp = null;                      
            }
        });           
              
        this.setSize(450, 350);
        this.setCenterScreen();
        this.setVisible(true);    
        
        sp_Visibility = true;
        sp = this;   
                
    }
    
    
    
   
    
    
    
    private void getReg_Data() {
        MDS_Property propDp = ppm.getProperty(PROPERTY_NAME);
        //Integer intGain = new Integer(propDp.getSupProperty(PROPERTY_GAIN));
        sldGain.setValue(Integer.parseInt(propDp.getSupProperty(PROPERTY_GAIN)));
        //Integer intPan = new Integer(propDp.getSupProperty(PROPERTY_PAN));
        sldPan.setValue(Integer.parseInt(propDp.getSupProperty(PROPERTY_PAN)));
        
        //chkMute.setSelected(Boolean.parseBoolean(propDp.getSupProperty(PROPERTY_MUTE)));
        chkMute.setSelected(false);
        chkMute.setEnabled(false);
    }
    
    private void saveProperty() {
    	MDS_Property prop = ppm.getProperty(PROPERTY_NAME);	
    	prop.setSupProperty(PROPERTY_GAIN, String.valueOf(sldGain.getValue()));	
    	prop.setSupProperty(PROPERTY_PAN, String.valueOf(sldPan.getValue()));
    	prop.setSupProperty(PROPERTY_MUTE, String.valueOf(chkMute.isSelected()));
    	ppm.setProperty(prop);
    }
    
    
    
    private void sendMessage_WMP(String msg) {
//        WinUI.sendMessage("ThunderRT6FormDC","WMP_Server For MDS", msg);
    }    
    
    
    
    public void stateChanged(ChangeEvent e) {
        if(e.getSource().equals(sldGain)) {
            snd.setGain(sldGain.getValue()); 
        } else if(e.getSource().equals(sldPan)) {
            snd.setPan(sldPan.getValue());
        }
////////         else if(e.getSource().equals(sldVolume)) {
////////            sendMessage_WMP("VOLUME>"+String.valueOf(sldVolume.getValue()));
////////        } else if(e.getSource().equals(sldBalance)) {
////////            if(sldBalance.getValue() >= 0) {
////////                sendMessage_WMP("RIGHT_SPEAKER>"+String.valueOf(sldBalance.getValue())); 
////////            } else {
////////                sendMessage_WMP("LEFT_SPEAKER>"+String.valueOf(sldBalance.getValue())); 
////////            }    
////////        }
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else if(e.getActionCommand().equals("Ok")) {
        	saveProperty();
            this.dispose();
        } else if(e.getActionCommand().equals("Mute")) {
        	snd.setMute(chkMute.isSelected());
        }
    }  
    
    
    
    public static void MDS_Main(String arg[]) {
        if(!sp_Visibility) {
            new SoundProperties();
        } else {
            try{
                SoundProperties.sp.setIcon(false);
                SoundProperties.sp.setSelected(true);
            } catch(Exception ex) {}
        }
    }       
    
    
    
}    