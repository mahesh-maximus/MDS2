
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;



public class ObjectInstantiater extends MDS_Frame implements ActionListener {



    JComponent contentPane;
    String idText = "Enter the name of the class you want to instantiate \n\n you can not instantiate MDS system classes.";
    JLabel lblIcon = new JLabel(ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"object-creator.png"));
    JLabel lblText1 = new JLabel("Enter the name of the class you want to instantiate");
    JLabel lblText2 = new JLabel("you can not instantiate MDS system classes.");
    JLabel lblText3 = new JLabel("Class Name");
    MDS_TextField txfdCommand = new MDS_TextField();
            
    MDS_Button btnInstantiate = new MDS_Button("Instantiate");
    MDS_Button btnCancel = new MDS_Button("Cancel");
            
    MDS_Frame me;
            
            
    public ObjectInstantiater() {
        //super("Run Command", false ,true);
        super("Object Creator",false, true, false, false, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "32-app-kbackgammon_engine.png"));
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(null);
        lblIcon.setBounds(0, 20, 48, 48);
        contentPane.add(lblIcon);
                
        lblText1.setBounds(50, 20, 300, 25);
        contentPane.add(lblText1);
                
        lblText2.setBounds(50, 40, 300, 25);
        contentPane.add(lblText2);
                
        lblText3.setBounds(8, 80, 70, 25);
        contentPane.add(lblText3);  
                
        me = this;
        txfdCommand.setBounds(80, 80, 240, 25);
        txfdCommand.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == e.VK_ENTER) {
                    createObject();  
                }
            }
        });                
        
        contentPane.add(txfdCommand); 
                
        btnCancel.setBounds(240, 120, 80, 30);
        btnCancel.addActionListener(this);
        contentPane.add(btnCancel);
                
        btnInstantiate.setBounds(130, 120, 100, 30);
        btnInstantiate.addActionListener(this);
        contentPane.add(btnInstantiate);
                
        this.setBounds(8, 390 ,350, 200);
        this.setVisible(true);
    }
    
    
    
    private void createObject() {
        
        Class cls = null;
        
        try {
            Object[] ar = null;
            cls = Class.forName(txfdCommand.getText());
            Constructor ct = cls.getConstructor(null);
            MDS.getProcessManager().instantiate(ct, ar);
            this.dispose();
        } catch (Exception ex) {
            if(ex instanceof ClassNotFoundException) {
                MDS_OptionPane.showMessageDialog("There is no class called '"+txfdCommand.getText()+"' in class path.", "Class Creator", JOptionPane.ERROR_MESSAGE);                  
            } else {
            	throw new RuntimeException(ex);
                //MDS.getExceptionManager().showException(ex);
            }   
        }           
    }
            
            
            
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Instantiate")) {
            createObject(); 
        } else if(e.getActionCommand().equals("Cancel")) {
            this.dispose();
        }
    }
    
    
    
    public static void MDS_Main(String arg[]) {
        new ObjectInstantiater();
    }    
    
    
    
}    