
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;



class MDS_FileTransferable implements Transferable {
        
        
        
    private DataFlavor[] flavors = {DataFlavor.stringFlavor};
    private String path;        



    public MDS_FileTransferable(String p) {
        path = p;
    }



    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }



    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return Arrays.asList(flavors).contains(flavor);
    }
         
         

    public synchronized Object getTransferData (DataFlavor flavor) throws UnsupportedFlavorException {
        if(flavor.equals(DataFlavor.stringFlavor)) {
            return path;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
        
} 