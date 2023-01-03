/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.awt.print.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;



public final class MDS_Printer {



    private static MDS_Printer prt = new MDS_Printer();


    private MDS_Printer() {}
    
    
    
    public static MDS_Printer getMDS_Printer() {
        return prt;    
    }
    
    
    
    public void print(ImageIcon i) {
        new PrintImage(i);
    }
    
    
    
    public void showPrintDialog() {
        new PrintDialog();
    }
    
    
    
    
    
    private class PrintDialog extends MDS_Frame implements ActionListener{
    
    
        private PrinterJob printJob = PrinterJob.getPrinterJob();
        private JComponent contentPane;
        private JLabel lblPrinter  = new JLabel("Current print service : "+printJob.getPrintService());
        private JPanel pnl1 = new JPanel();
        private MDS_TextField txtfPath = new MDS_TextField();
        private MDS_Button btnBrowse = new MDS_Button("Browse");
        private JPanel pnl2 = new JPanel();
        private MDS_Button btnPrint = new MDS_Button("Print");
        
    
    
        public PrintDialog() {
            super("Print",true, true, false, true ,ImageManipulator.getImageIcon(ImageManipulator.MDS_ICONS_PATH + "printer.png"));
            contentPane = (JComponent)this.getContentPane();
            contentPane.setLayout(new GridLayout(3, 1));
            contentPane.add(lblPrinter);
            pnl1.setLayout(new BorderLayout());
            pnl1.add(txtfPath, BorderLayout.CENTER);
            btnBrowse.addActionListener(this);
            pnl1.add(btnBrowse, BorderLayout.EAST);
            contentPane.add(pnl1);
            pnl2.setLayout(new FlowLayout(FlowLayout.TRAILING));
            btnPrint.addActionListener(this);
            pnl2.add(btnPrint);
            contentPane.add(pnl2);
            this.setSize(400, 130);
            this.setCenterScreen();
            this.setVisible(true);
        }
        
        
        
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Browse")) {
                MDS_FileChooser fmfc = new MDS_FileChooser(MDS_FileChooser.OPEN_DIALOG);
                Vector v = new Vector();
                v.add("png");
                v.add("gif");
                v.add("jpg");
                v.add("jpeg");
                fmfc.setFilter(v); 
                fmfc.setPicturePreviewerVisible(true);
                if(fmfc.showDiaog(this) ==  fmfc.APPROVE_OPTION) {
                    txtfPath.setText(fmfc.getPath());
                }                
            } else if(e.getActionCommand().equals("Print")) {
                if(!txtfPath.getText().equals("")) {
                    File f = new File(txtfPath.getText());
                    if(!f.exists()) {
                        MDS_OptionPane.showMessageDialog(this, "file not found.\n\n"+"'"+f.getPath()+"'", "Printer", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    MDS.getPrinter().print(new ImageIcon(txtfPath.getText()));
                }
            }    
        }
    
    }
    
    
    
    
    
    private class PrintImage implements Printable {
    
    
    
        private ImageIcon img;
    
    
    
        public PrintImage(ImageIcon ii) {
            try {
                img = ii;
                PrinterJob printJob = PrinterJob.getPrinterJob();
                printJob.setPrintable(this);
                printJob.print();                    
            } catch(Exception ex) {
                MDS_OptionPane.showMessageDialog(ex.getMessage(), "Printer Access Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    
    
        public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
            if(pi >= 1) {
                return Printable.NO_SUCH_PAGE;
            }
        
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.drawImage(img.getImage(), 10, 10, null);
                
            return Printable.PAGE_EXISTS;
         
        }    
    
    }   
    
    
    
    
    
    private class PrintFile implements Printable {
    
    
    
        public PrintFile() {
        
        }
    
    
    
        public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
            if(pi >= 1) {
                return Printable.NO_SUCH_PAGE;
            }
        
            Graphics2D g2d = (Graphics2D) g;
        
        
            return Printable.PAGE_EXISTS;
         
        }    
    
    }    

}