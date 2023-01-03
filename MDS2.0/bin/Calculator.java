/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Calculator extends MDS_Frame implements ActionListener {



    MDS_User usr = MDS.getUser();
    
    JComponent contentPane;
    JMenuBar mnbCalculator = new JMenuBar();
    MDS_Menu mnuFile = new MDS_Menu("File", 'F');
    JMenuItem mniExit = usr.createMenuItem("Exit", this, KeyEvent.VK_X);    
    MDS_Menu mnuEdit = new MDS_Menu("Edit", 'E');
    JMenuItem mniCopy = usr.createMenuItem("Copy", this, MDS_KeyStroke.getCut(), KeyEvent.VK_C);    
    JMenuItem mniPaste = usr.createMenuItem("Paste", this, MDS_KeyStroke.getPaste(), KeyEvent.VK_V);    
    MDS_Menu mnuHelp = new MDS_Menu("Help", 'H');
    JMenuItem mniAbout = usr.createMenuItem("About ...", this, KeyEvent.VK_A);    
    MDS_Panel plnButtons = new MDS_Panel();
    MDS_TextField txfdDisplay = new MDS_TextField("0");
    
    String buttons = "789/456*123-0.=+";
    private double arg = 0;
    private String op = "=";
    private boolean start = true;
    
    
    
    public Calculator() {
        super("Calculator", false, true, false, true, ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "calculator.png"));
        contentPane = (JComponent) this.getContentPane();
        contentPane.setLayout(new BorderLayout()); 
        plnButtons.setLayout(new GridLayout(4, 4));
        txfdDisplay.setEditable(false);
        contentPane.add(txfdDisplay, BorderLayout.NORTH);

        for (int i = 0; i < buttons.length(); i++) {
            addButton(plnButtons, buttons.substring(i, i + 1));
        }
        
        contentPane.add(plnButtons, BorderLayout.CENTER);
        
        mnuFile.add(mniExit);
        mnbCalculator.add(mnuFile);
        mnuEdit.add(mniCopy);        
        mnuEdit.add(mniPaste);
        mnbCalculator.add(mnuEdit);
        mnuHelp.add(mniAbout);
        mnbCalculator.add(mnuHelp);
        this.setJMenuBar(mnbCalculator);  
        this.setSize(250,250);
        this.setCenterScreen();
        this.setVisible(true);
               
    }
    
    
    
    private void addButton(Container c, String s) {
        MDS_Button b = new MDS_Button(s);
        c.add(b);
        b.addActionListener(this);
    }
    
    
    
    public void calculate(double n) {
        if (op.equals("+")) arg += n; 
        else if (op.equals("-")) arg -= n;
        else if (op.equals("*")) arg *= n;
        else if (op.equals("/")) arg /= n;
        else if (op.equals("=")) arg = n;
        txfdDisplay.setText("" + arg);
   }        
   
   
   
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if ('0' <= s.charAt(0) && s.charAt(0) <= '9' || s.equals(".")) {
            if (start) {
                txfdDisplay.setText(s);
            } else { 
                txfdDisplay.setText(txfdDisplay.getText() + s);
            }    
         start = false;
      } else {  
          if (start) {
              if (s.equals("-")) {
                  txfdDisplay.setText(s); start = false; 
              } else {
                  op = s;
              }    
           } else {  
               double x = Double.parseDouble(txfdDisplay.getText());
               calculate(x);
               op = s;
               start = true;
          }
      }
      
      if(e.getActionCommand().equals("Exit")) {     
          this.dispose();
      } else if(e.getActionCommand().equals("Copy")) {
          
      } else if(e.getActionCommand().equals("Paste")) {
          
      } else if(e.getActionCommand().equals("About ...")) {
          usr.showAboutDialog(this, "Calculator", ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH+"calculator.png"), MDS.getAbout_Mahesh()); 
      }

       
    }
        
    
    
    public static void MDS_Main(String arg[]) {  
        new Calculator();
    }
    
    
    
}    