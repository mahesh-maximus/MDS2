/*
 * Title:        MDS 
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 
 
import java.io.*;
import java.util.*;



public class Win32WMI_Classes implements Runnable {


    private static Win32WMI_Classes w32_cls = new Win32WMI_Classes();
    private FileManager fm = MDS.getFileManager();
    private File vbsFile;
    
    private final String QUOTE = String.valueOf('"');
    Thread t = new Thread(this, "VBScript");
    
    MDS_Hashtable htblWin32_Processor = new MDS_Hashtable();
    MDS_Hashtable htblWin32_MotherboardDevice = new MDS_Hashtable();
    MDS_Hashtable htblWin32_PhysicalMemory = new MDS_Hashtable();
    MDS_Hashtable htblWin32_SoundDevice = new MDS_Hashtable();
    MDS_Hashtable htblWin32_VideoController = new MDS_Hashtable();
    MDS_Hashtable htblWin32_CDROMDrive = new MDS_Hashtable();
    MDS_Hashtable htblWin32_DiskDrive = new MDS_Hashtable();
    MDS_Hashtable htblWin32_FloppyDrive = new MDS_Hashtable();
    MDS_Hashtable htblWin32_LogicalDisk = new MDS_Hashtable();
    MDS_Hashtable htblWin32_BIOS = new MDS_Hashtable();
    MDS_Hashtable htblWin32_Keyboard = new MDS_Hashtable();
    MDS_Hashtable htblWin32_PointingDevice = new MDS_Hashtable();
    MDS_Hashtable htblWin32_POTSModem = new MDS_Hashtable();
    MDS_Hashtable htblWin32_ComputerSystem = new MDS_Hashtable();
    MDS_Hashtable htblWin32_OperatingSystem = new MDS_Hashtable();
    
    MDS_StartUp_Window stpw;

    private  Win32WMI_Classes() {
        //t.setPriority(Thread.MIN_PRIORITY);
        //t.start();
        //stpw = sw;
        //stpw.println("");
        creat_VBScript_File();
        execute_VBScript_File();
    }
    
    
    
    public static Win32WMI_Classes getWin32WMI_Classes() {
        return w32_cls;
    }
    
    
    
    private void creat_VBScript_File() {
        try {
            
            System.out.println("Creating VBScript File....");
            vbsFile = fm.createTempFile("tmp_sysInfo", "vbs");
            PrintWriter pw = new PrintWriter(new FileWriter(vbsFile));
            //pw.println("Set objcol = GetObject("+QUOTE+"WinMgmts:"+QUOTE+").InstancesOf("+QUOTE+"Win32_Processor"+QUOTE);
            //"+QUOTE+"="+QUOTE+" & 
            
            pw.println("set wmi = GetObject("+QUOTE+"WinMgmts:"+QUOTE+")");
            pw.println("WScript.Echo "+QUOTE+"Win32_Processor"+QUOTE); 
            pw.println("count = 1");
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_Processor"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"Address Width="+QUOTE+" & obj.AddressWidth");
            pw.println("WScript.Echo "+QUOTE+"Architecture="+QUOTE+" & obj.Architecture");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            //pw.println("WScript.Echo "+QUOTE+"cpu Status="+QUOTE+" & obj.CpuStatus");
            pw.println("WScript.Echo "+QUOTE+"Data Width="+QUOTE+" & obj.DataWidth");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"Device ID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"L2 Cache Size="+QUOTE+" & obj.L2CacheSize");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Max Clock Speed="+QUOTE+" & obj.MaxClockSpeed");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"Socket Designation="+QUOTE+" & obj.SocketDesignation");
            //pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("WScript.Echo "+QUOTE+"Version="+QUOTE+" & obj.Version");                       
            pw.println("count = count+1");
            pw.println("next");
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_MotherboardDevice"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_MotherboardDevice"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"PrimaryBusType="+QUOTE+" & obj.PrimaryBusType");
            pw.println("WScript.Echo "+QUOTE+"SecondaryBusType="+QUOTE+" & obj.SecondaryBusType");
            pw.println("count = count+1");
            pw.println("next");
            
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_PhysicalMemory"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_PhysicalMemory"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"Bank Label="+QUOTE+" & obj.BankLabel");
            pw.println("WScript.Echo "+QUOTE+"Capacity="+QUOTE+" & obj.Capacity");
            pw.println("WScript.Echo "+QUOTE+"Data Width="+QUOTE+" & obj.DataWidth");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"Device Locator="+QUOTE+" & obj.DeviceLocator");
            pw.println("WScript.Echo "+QUOTE+"Form Factor="+QUOTE+" & obj.FormFactor");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Memory Type="+QUOTE+" & obj.MemoryType");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"Serial Number="+QUOTE+" & obj.SerialNumber");
            pw.println("WScript.Echo "+QUOTE+"Speed="+QUOTE+" & obj.Speed");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("WScript.Echo "+QUOTE+"TotalWidth="+QUOTE+" & obj.TotalWidth");
            pw.println("count = count+1");
            pw.println("next");            
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_SoundDevice"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_SoundDevice"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"DMABufferSize="+QUOTE+" & obj.DMABufferSize");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"ProductName="+QUOTE+" & obj.ProductName");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("count = count+1");
            pw.println("next"); 
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_VideoController"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_VideoController"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"Adapter Compatibility="+QUOTE+" & obj.AdapterCompatibility");
            pw.println("WScript.Echo "+QUOTE+"Adapter DAC Type="+QUOTE+" & obj.AdapterDACType");
            pw.println("WScript.Echo "+QUOTE+"Adapter RAM="+QUOTE+" & obj.AdapterRAM");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            pw.println("WScript.Echo "+QUOTE+"Current Bits PerPixel="+QUOTE+" & obj.CurrentBitsPerPixel");
            pw.println("WScript.Echo "+QUOTE+"Current Number Of Colors="+QUOTE+" & obj.CurrentNumberOfColors");
            pw.println("WScript.Echo "+QUOTE+"Current Refresh Rate="+QUOTE+" & obj.CurrentRefreshRate");
            pw.println("WScript.Echo "+QUOTE+"Current Scan Mode="+QUOTE+" & obj.CurrentScanMode");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"Device ID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"MaxMemory Supported="+QUOTE+" & obj.MaxMemorySupported");
            pw.println("WScript.Echo "+QUOTE+"Max Refresh Rate="+QUOTE+" & obj.MaxRefreshRate");
            pw.println("WScript.Echo "+QUOTE+"Min Refresh Rate="+QUOTE+" & obj.MinRefreshRate");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("WScript.Echo "+QUOTE+"Video Architecture="+QUOTE+" & obj.VideoArchitecture");
            pw.println("WScript.Echo "+QUOTE+"Video Memory Type="+QUOTE+" & obj.VideoMemoryType");
            pw.println("WScript.Echo "+QUOTE+"Video Processor="+QUOTE+" & obj.VideoProcessor");
            pw.println("count = count+1");
            pw.println("next");     
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_CDROMDrive"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_CDROMDrive"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            //pw.println("WScript.Echo "+QUOTE+"CompressionMethod="+QUOTE+" & obj.CompressionMethod");
            //pw.println("WScript.Echo "+QUOTE+"DefaultBlockSize="+QUOTE+" & obj.DefaultBlockSize");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"Drive="+QUOTE+" & obj.Drive");
           // pw.println("WScript.Echo "+QUOTE+"DriveIntegrity="+QUOTE+" & obj.DriveIntegrity");
            pw.println("WScript.Echo "+QUOTE+"Id="+QUOTE+" & obj.Id");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            //pw.println("WScript.Echo "+QUOTE+"MaxBlockSize="+QUOTE+" & obj.MaxBlockSize");
            pw.println("WScript.Echo "+QUOTE+"MaximumComponentLength="+QUOTE+" & obj.MaximumComponentLength");
            //pw.println("WScript.Echo "+QUOTE+"MaxMediaSize="+QUOTE+" & obj.MaxMediaSize");
            //pw.println("WScript.Echo "+QUOTE+"MediaLoaded="+QUOTE+" & obj.MediaLoaded");
            pw.println("WScript.Echo "+QUOTE+"MediaType="+QUOTE+" & obj.MediaType");
            //pw.println("WScript.Echo "+QUOTE+"MinBlockSize="+QUOTE+" & obj.MinBlockSize");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            //pw.println("WScript.Echo "+QUOTE+"NeedsCleaning="+QUOTE+" & obj.NeedsCleaning");
            //pw.println("WScript.Echo "+QUOTE+"Size="+QUOTE+" & obj.Size");
            //pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            //pw.println("WScript.Echo "+QUOTE+"TransferRate="+QUOTE+" & obj.TransferRate");
            //pw.println("WScript.Echo "+QUOTE+"VolumeName="+QUOTE+" & obj.VolumeName");
            //pw.println("WScript.Echo "+QUOTE+"VolumeSerialNumber="+QUOTE+" & obj.VolumeSerialNumber");
            pw.println("count = count+1");
            pw.println("next");     
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_DiskDrive"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_DiskDrive"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            pw.println("WScript.Echo "+QUOTE+"Bytes Per Sector="+QUOTE+" & obj.BytesPerSector");
            pw.println("WScript.Echo "+QUOTE+"Compression Method="+QUOTE+" & obj.CompressionMethod");
            pw.println("WScript.Echo "+QUOTE+"Default Block Size="+QUOTE+" & obj.DefaultBlockSize");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"Device ID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"Interface Type="+QUOTE+" & obj.InterfaceType");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Max Block Size="+QUOTE+" & obj.MaxBlockSize");
            pw.println("WScript.Echo "+QUOTE+"Media Type="+QUOTE+" & obj.MediaType");
            pw.println("WScript.Echo "+QUOTE+"Min Block Size="+QUOTE+" & obj.MinBlockSize");
            pw.println("WScript.Echo "+QUOTE+"Model="+QUOTE+" & obj.Model");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            //pw.println("WScript.Echo "+QUOTE+"Needs Cleaning="+QUOTE+" & obj.NeedsCleaning");
            pw.println("WScript.Echo "+QUOTE+"Partitions="+QUOTE+" & obj.Partitions");
            pw.println("WScript.Echo "+QUOTE+"Sectors Per Track="+QUOTE+" & obj.SectorsPerTrack");
            pw.println("WScript.Echo "+QUOTE+"Size="+QUOTE+" & obj.Size");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("WScript.Echo "+QUOTE+"Total Heads="+QUOTE+" & obj.TotalHeads");
            pw.println("WScript.Echo "+QUOTE+"Total Sectors="+QUOTE+" & obj.TotalSectors");
            pw.println("WScript.Echo "+QUOTE+"Total Tracks="+QUOTE+" & obj.TotalTracks");
            pw.println("WScript.Echo "+QUOTE+"Tracks Per Cylinder="+QUOTE+" & obj.TracksPerCylinder");
            pw.println("count = count+1");
            pw.println("next");    
            
            /*
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_FloppyDrive"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_FloppyDrive"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            pw.println("WScript.Echo "+QUOTE+"BytesPerSector="+QUOTE+" & obj.BytesPerSector");
            pw.println("WScript.Echo "+QUOTE+"CompressionMethod="+QUOTE+" & obj.CompressionMethod");
            pw.println("WScript.Echo "+QUOTE+"DefaultBlockSize="+QUOTE+" & obj.DefaultBlockSize");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"MaxBlockSize="+QUOTE+" & obj.MaxBlockSize");
            pw.println("WScript.Echo "+QUOTE+"MediaType="+QUOTE+" & obj.MediaType");
            pw.println("WScript.Echo "+QUOTE+"MinBlockSize="+QUOTE+" & obj.MinBlockSize");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"NeedsCleaning="+QUOTE+" & obj.NeedsCleaning");
            pw.println("WScript.Echo "+QUOTE+"Partitions="+QUOTE+" & obj.Partitions");
            pw.println("WScript.Echo "+QUOTE+"SectorsPerTrack="+QUOTE+" & obj.SectorsPerTrack");
            pw.println("WScript.Echo "+QUOTE+"Size="+QUOTE+" & obj.Size");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("count = count+1");
            pw.println("next");       
            */            
            
            
            /*                                               
            String[] rDrives = MDS.getFileManager().getRootDrives_AsStrings();
            
            //pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_LogicalDisk"+QUOTE+")");
            
            int count = 1;
            
            for(int x= 0; x < rDrives.length; x++) {
                String drive = rDrives[x].substring(0,2);
                DiskDrives dd = new DiskDrives();
                if(dd.getDriveType(drive)!= dd.DRIVE_REMOVABLE) {
                    Console.println(rDrives[x]);
                    pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & "+count);
                    pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").Availability)");
                    
                    pw.println("WScript.Echo "+QUOTE+"BlockSize="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").BlockSize)");
                    pw.println("WScript.Echo "+QUOTE+"Compressed="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").Compressed)");
                    pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").Description)");
                    pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").DeviceID)");
                    pw.println("WScript.Echo "+QUOTE+"DriveType="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").DriveType)");
                    pw.println("WScript.Echo "+QUOTE+"FileSystem="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").FileSystem)");
                    pw.println("WScript.Echo "+QUOTE+"FreeSpace="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").FreeSpace)");
                    pw.println("WScript.Echo "+QUOTE+"MaximumComponentLength="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").MaximumComponentLength)");
                    pw.println("WScript.Echo "+QUOTE+"MediaType="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").MediaType)");
                    pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").Name)");
                    pw.println("WScript.Echo "+QUOTE+"NumberOfBlocks="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").NumberOfBlocks)");
                    pw.println("WScript.Echo "+QUOTE+"Size="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").Size)");
                    pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").Status)");
                    pw.println("WScript.Echo "+QUOTE+"VolumeName="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").VolumeName)");
                    pw.println("WScript.Echo "+QUOTE+"VolumeSerialNumber="+QUOTE+" & (GetObject("+QUOTE+"WinMgmts:Win32_LogicalDisk='"+drive+"'"+QUOTE+").VolumeSerialNumber)");
                    
                    count++;
                    
                }
            }
            */
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_BIOS"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_BIOS"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"CurrentLanguage="+QUOTE+" & obj.CurrentLanguage");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"SerialNumber="+QUOTE+" & obj.SerialNumber");
            pw.println("WScript.Echo "+QUOTE+"SMBIOSBIOSVersion="+QUOTE+" & obj.SMBIOSBIOSVersion");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("WScript.Echo "+QUOTE+"Version="+QUOTE+" & obj.Version");
            pw.println("count = count+1");
            pw.println("next");     
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_Keyboard"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_Keyboard"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"IsLocked="+QUOTE+" & obj.IsLocked");
            pw.println("WScript.Echo "+QUOTE+"Layout="+QUOTE+" & obj.Layout");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"NumberOfFunctionKeys="+QUOTE+" & obj.NumberOfFunctionKeys");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("count = count+1");
            pw.println("next");   
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_PointingDevice"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_PointingDevice"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"IsLocked="+QUOTE+" & obj.IsLocked");
            pw.println("WScript.Echo "+QUOTE+"DeviceInterface="+QUOTE+" & obj.DeviceInterface");
            pw.println("WScript.Echo "+QUOTE+"DoubleSpeedThreshold="+QUOTE+" & obj.DoubleSpeedThreshold");
            pw.println("WScript.Echo "+QUOTE+"Handedness="+QUOTE+" & obj.Handedness");
            pw.println("WScript.Echo "+QUOTE+"HardwareType="+QUOTE+" & obj.HardwareType");
            pw.println("WScript.Echo "+QUOTE+"IsLocked="+QUOTE+" & obj.IsLocked");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"NumberOfButtons="+QUOTE+" & obj.NumberOfButtons");
            pw.println("WScript.Echo "+QUOTE+"PointingType="+QUOTE+" & obj.PointingType");
            pw.println("WScript.Echo "+QUOTE+"QuadSpeedThreshold="+QUOTE+" & obj.QuadSpeedThreshold");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("count = count+1");
            pw.println("next");  
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_POTSModem"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_POTSModem"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"AttachedTo="+QUOTE+" & obj.AttachedTo");
            //pw.println("WScript.Echo "+QUOTE+"Availability="+QUOTE+" & obj.Availability");
            pw.println("WScript.Echo "+QUOTE+"BlindOff="+QUOTE+" & obj.BlindOff");
            pw.println("WScript.Echo "+QUOTE+"BlindOn="+QUOTE+" & obj.BlindOn");
            pw.println("WScript.Echo "+QUOTE+"CountrySelected="+QUOTE+" & obj.CountrySelected");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"DeviceID="+QUOTE+" & obj.DeviceID");
            pw.println("WScript.Echo "+QUOTE+"DeviceType="+QUOTE+" & obj.DeviceType");
            pw.println("WScript.Echo "+QUOTE+"DialType="+QUOTE+" & obj.DialType");
            pw.println("WScript.Echo "+QUOTE+"Model="+QUOTE+" & obj.Model");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"Status="+QUOTE+" & obj.Status");
            pw.println("WScript.Echo "+QUOTE+"Terminator="+QUOTE+" & obj.Terminator");
            pw.println("count = count+1");
            pw.println("next");   
            
            
            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_ComputerSystem"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_ComputerSystem"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"Bootup State="+QUOTE+" & obj.BootupState");
            pw.println("WScript.Echo "+QUOTE+"Description="+QUOTE+" & obj.Description");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Model="+QUOTE+" & obj.Model");
            pw.println("WScript.Echo "+QUOTE+"Number Of Processors="+QUOTE+" & obj.NumberOfProcessors");
            pw.println("WScript.Echo "+QUOTE+"System Type="+QUOTE+" & obj.SystemType");
            pw.println("WScript.Echo "+QUOTE+"Total Physical Memory="+QUOTE+" & obj.TotalPhysicalMemory");
            pw.println("count = count+1");
            pw.println("next");             


            pw.println("count = 1");
            pw.println("WScript.Echo "+QUOTE+"Win32_OperatingSystem"+QUOTE);
            pw.println("set objs = wmi.InstancesOf("+QUOTE+"Win32_OperatingSystem"+QUOTE+")");
            pw.println("for each obj in objs");
            pw.println("WScript.Echo "+QUOTE+"#DeviceCount="+QUOTE+" & count");
            pw.println("WScript.Echo "+QUOTE+"BootDevice="+QUOTE+" & obj.BootDevice");
            pw.println("WScript.Echo "+QUOTE+"BuildType="+QUOTE+" & obj.BuildType");
            //pw.println("WScript.Echo "+QUOTE+"FreePhysicalMemory="+QUOTE+" & obj.FreePhysicalMemory");
            //pw.println("WScript.Echo "+QUOTE+"FreeSpaceInPagingFiles="+QUOTE+" & obj.FreeSpaceInPagingFiles");
            //pw.println("WScript.Echo "+QUOTE+"FreeVirtualMemory="+QUOTE+" & obj.FreeVirtualMemory");
            pw.println("WScript.Echo "+QUOTE+"Manufacturer="+QUOTE+" & obj.Manufacturer");
            pw.println("WScript.Echo "+QUOTE+"Name="+QUOTE+" & obj.Name");
            pw.println("WScript.Echo "+QUOTE+"SerialNumber="+QUOTE+" & obj.SerialNumber");
            pw.println("WScript.Echo "+QUOTE+"SystemDevice="+QUOTE+" & obj.SystemDevice");
            pw.println("WScript.Echo "+QUOTE+"SystemDrive="+QUOTE+" & obj.SystemDrive");
            //pw.println("WScript.Echo "+QUOTE+"TotalSwapSpaceSize="+QUOTE+" & obj.TotalSwapSpaceSize");
            //pw.println("WScript.Echo "+QUOTE+"TotalVirtualMemorySize="+QUOTE+" & obj.TotalVirtualMemorySize");
            pw.println("WScript.Echo "+QUOTE+"Version="+QUOTE+" & obj.Version");
            pw.println("count = count+1");
            pw.println("next");                         
            
            pw.println("WScript.Echo "+QUOTE+"**END**"+QUOTE);                          
                                   
            pw.close();            
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    
    
    private void execute_VBScript_File() {
        try {
        
            System.out.println("Executing VBScript File...");
            System.out.println("    Creating process: "+"cscript "+vbsFile.getPath());
            Runtime rt = Runtime.getRuntime();
            Process prc = rt.exec("cscript "+QUOTE+vbsFile.getPath()+QUOTE);
            
            System.out.println("    Connecting(Redirecting) to InputSream");
            BufferedReader bfrd = new BufferedReader(new InputStreamReader(prc.getInputStream()));
            
            String line = "";
            String currentClass = "";
            
            System.out.println("    Collecting System information");
            
            while(!line.equals("**END**")) {
                line = bfrd.readLine();
                
                if(line.equals("Win32_Processor")) {
                    System.out.println("    Loading information about : Processor");
                    currentClass = line;
                } else if(line.equals("Win32_MotherboardDevice")) {
                    System.out.println("    Loading information about : MotherboardDevice");
                    currentClass = line;
                } else if(line.equals("Win32_PhysicalMemory")) {
                    System.out.println("    Loading information about : PhysicalMemory");
                    currentClass = line;
                } else if(line.equals("Win32_SoundDevice")) {
                    System.out.println("    Loading information about : SoundDevice");
                    currentClass = line;
                } else if(line.equals("Win32_VideoController")) {
                    System.out.println("    Loading information about : VideoController");
                    currentClass = line;
                } else if(line.equals("Win32_CDROMDrive")) {
                    System.out.println("    Loading information about : CDROMDrive");
                    currentClass = line;
                } else if(line.equals("Win32_DiskDrive")) {
                    System.out.println("    Loading information about : DiskDrive");
                    currentClass = line;
                } else if(line.equals("Win32_FloppyDrive")) {
                    System.out.println("    Loading information about : FloppyDrive");
                    currentClass = line;
                } else if(line.equals("Win32_LogicalDisk")) {
                    System.out.println("    Loading information about : LogicalDisk");
                    currentClass = line;
                } else if(line.equals("Win32_BIOS")) {
                    System.out.println("    Loading information about : BIOS");
                    currentClass = line;
                } else if(line.equals("Win32_Keyboard")) {
                    System.out.println("    Loading information about : Keyboard");
                    currentClass = line;
                } else if(line.equals("Win32_PointingDevice")) {
                    System.out.println("    Loading information about : PointingDevice");
                    currentClass = line;
                } else if(line.equals("Win32_POTSModem")) {
                    System.out.println("    Loading information about : POTSModem");
                    currentClass = line;
                } else if(line.equals("Win32_ComputerSystem")) {
                    System.out.println("    Loading information about : ComputerSystem");
                    currentClass = line;
                } else if(line.equals("Win32_OperatingSystem")) {
                    System.out.println("    Loading information about : OperatingSystem");
                    currentClass = line;
                }


                
                if(currentClass.equals("Win32_Processor")) {
                    if(!line.equals("Win32_Processor") && !line.equals("**END**")) { 
                        htblWin32_Processor.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }     
                } else if(currentClass.equals("Win32_MotherboardDevice")) {
                    if(!line.equals("Win32_MotherboardDevice") && !line.equals("**END**")) { 
                        htblWin32_MotherboardDevice.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_PhysicalMemory")) {
                    if(!line.equals("Win32_PhysicalMemory") && !line.equals("**END**")) { 
                        htblWin32_PhysicalMemory.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_SoundDevice")) {
                    if(!line.equals("Win32_SoundDevice") && !line.equals("**END**")) { 
                        htblWin32_SoundDevice.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_VideoController")) {
                    if(!line.equals("Win32_VideoController") && !line.equals("**END**")) { 
                        htblWin32_VideoController.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_CDROMDrive")) {
                    if(!line.equals("Win32_CDROMDrive") && !line.equals("**END**")) { 
                        htblWin32_CDROMDrive.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_DiskDrive")) {
                    if(!line.equals("Win32_DiskDrive") && !line.equals("**END**")) { 
                        htblWin32_DiskDrive.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_FloppyDrive")) {
                    if(!line.equals("Win32_FloppyDrive") && !line.equals("**END**")) { 
                        htblWin32_FloppyDrive.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_LogicalDisk")) {
                    if(!line.equals("Win32_LogicalDisk") && !line.equals("**END**")) { 
                        htblWin32_LogicalDisk.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_BIOS")) {
                    if(!line.equals("Win32_BIOS") && !line.equals("**END**")) { 
                        htblWin32_BIOS.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_Keyboard")) {
                    if(!line.equals("Win32_Keyboard") && !line.equals("**END**")) { 
                        htblWin32_Keyboard.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_PointingDevice")) {
                    if(!line.equals("Win32_PointingDevice") && !line.equals("**END**")) { 
                        htblWin32_PointingDevice.put(getItem(line), getValue(line));
                        //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_POTSModem")) {
                    if(!line.equals("Win32_POTSModem") && !line.equals("**END**")) { 
                       htblWin32_POTSModem.put(getItem(line), getValue(line));
                       //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_ComputerSystem")) {
                    if(!line.equals("Win32_ComputerSystem") && !line.equals("**END**")) { 
                       htblWin32_ComputerSystem.put(getItem(line), getValue(line));
                       //Console.println(getItem(line)+" = "+getValue(line));
                    }
                } else if(currentClass.equals("Win32_OperatingSystem")) {
                    if(!line.equals("Win32_OperatingSystem") && !line.equals("**END**")) { 
                       htblWin32_OperatingSystem.put(getItem(line), getValue(line));
                       //Console.println(getItem(line)+" = "+getValue(line));
                    }
                }  

            }       
             
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }    
    }
    
    
    
    public static String getItem(String line) {
        int x =0;   
        String name = "";
        while(line.charAt(x)!='=') {
            name = name.concat(String.valueOf(line.charAt(x)));
            x++;
        } 
        
        return name;  
    }
    
    
    
    public static String getValue(String line) {
        int x = line.length();   
        String value = "";
        
        do {
            value = line.substring(x, line.length());
            x--;
        } while(line.charAt(x)!='=');
        
        return value;
    }
    
    
    
    public MDS_Hashtable getWin32_Processor() {
        return htblWin32_Processor;
    }
    
    
    
    public MDS_Hashtable getWin32_MotherboardDevice() {
        return htblWin32_MotherboardDevice;
    }    
    
    
    
    public MDS_Hashtable getWin32_PhysicalMemory() {
        return htblWin32_PhysicalMemory;
    }    
    
    
    
    public MDS_Hashtable getWin32_SoundDevice() {
        return htblWin32_SoundDevice;
    }     
    
    
    
    public MDS_Hashtable getWin32_VideoController() {
        return htblWin32_VideoController;
    }      
    
    
    
    public MDS_Hashtable getWin32_CDROMDrive() {
        return htblWin32_CDROMDrive;
    }     
    
    
    
    public MDS_Hashtable getWin32_DiskDrive() {
        return htblWin32_DiskDrive;
    }     
    
    
    
    public MDS_Hashtable getWin32_FloppyDrive() {
        return htblWin32_FloppyDrive;
    }     
    
    
    
    public MDS_Hashtable getWin32_LogicalDisk() {
        return htblWin32_LogicalDisk;
    }     
    
    
    
    public MDS_Hashtable getWin32_BIOS() {
        return htblWin32_BIOS;
    }     
    
    
    
    public MDS_Hashtable getWin32_Keyboard() {
        return htblWin32_Keyboard;
    }     
    
    
    
    public MDS_Hashtable getWin32_PointingDevice() {
        return htblWin32_PointingDevice;
    }     
    
    
    
    public MDS_Hashtable getWin32_POTSModem() {
        return htblWin32_POTSModem;
    }    
    
    
    
    public MDS_Hashtable getWin32_ComputerSystem() {
        return htblWin32_ComputerSystem;
    }  
    
    
    
    public MDS_Hashtable getWin32_OperatingSystem() {
        return htblWin32_OperatingSystem;
    }
    
    
    
    public String STRUCT_getArchitecture(int value) {
        String rtv = "";
        switch (value) {
            case 0:
                rtv = "x86";
                break;    
            case 1:
                rtv = "MIPS";
                break; 
            case 2:
                rtv = "Alpha";
                break; 
            case 3:
                rtv = "PowerPC";
                break; 
        }
        
        return rtv;
                                                                           
    } 
    
    
    
    public String STRUCT_getFormFactor(int value) {
        String rtv = "";
        switch (value) {
            case 0:
                rtv = "Unknown";
                break;    
            case 1:
                rtv = "Other";
                break; 
            case 2:
                rtv = "SIP";
                break; 
            case 3:
                rtv = "DIP";
                break; 
            case 4:
                rtv = "ZIP";
                break;
            case 5:
                rtv = "SOJ";
                break;
            case 6:
                rtv = "Proprietary";
                break;
            case 7:
                rtv = "SIMM";
                break;
            case 8:
                rtv = "DIMM";
                break;
            case 9:
                rtv = "TSOP";
                break;
            case 10:
                rtv = "PGA";
                break;
            case 11:
                rtv = "RIMM";
                break;
            case 12:
                rtv = "SODIMM";
                break;
                                          
        }
        
        return rtv;
                                                                           
    }
    
    
    
    public String STRUCT_getMemoryType(int value) {
        String rtv = "";
        switch (value) {
            case 1:
                rtv = "Unknown";
                break;    
            case 2:
                rtv = "Other";
                break; 
            case 3:
                rtv = "DRAM";
                break; 
            case 4:
                rtv = "Synchronous DRAM";
                break; 
            case 5:
                rtv = "Cache DRAM";
                break;
            case 6:
                rtv = "EDO";
                break;
            case 7:
                rtv = "EDRAM";
                break;
            case 8:
                rtv = "VRAM";
                break;
            case 9:
                rtv = "SRAM";
                break;
            case 10:
                rtv = "RAM";
                break;
            case 11:
                rtv = "ROM";
                break;
            case 12:
                rtv = "Flash";
                break;
            case 13:
                rtv = "EEPROM";
                break;
            case 14:
                rtv = "FEPROM";
                break;
            case 15:
                rtv = "EPROM";
                break;
            case 16:
                rtv = "CDRAM";
                break;
            case 17:
                rtv = "3DRAM";
                break;
            case 18:
                rtv = "SDRAM";
                break;
            case 19:
                rtv = "SGRAM";
                break;                      
                                          
        }
        
        return rtv;
                                                                           
    } 
    
    
    
    public String STRUCT_DeviceInterface(int value) {
        String rtv = "";
        switch (value) {
            case 1:
                rtv = "Other";
                break;    
            case 2:
                rtv = "Unknown";
                break; 
            case 3:
                rtv = "Serial";
                break; 
            case 4:
                rtv = "PS/2";
                break; 
            case 5:
                rtv = "Infrared";
                break;
            case 6:
                rtv = "HP-HIL";
                break;
            case 7:
                rtv = "Bus mouse";
                break;
            case 8:
                rtv = "ADB (Apple Desktop Bus)";
                break;
            case 160:
                rtv = "Bus mouse DB-9";
                break;
            case 161:
                rtv = "Bus mouse micro-DIN";
                break;
            case 162:
                rtv = "USB";
                break;
                                          
        }
        
        return rtv;
                                                                           
    }  
    
    
    
    public String STRUCT_getHandedness(int value) {
        String rtv = "";
        switch (value) {
            case 0:
                rtv = "Unknown";
                break;    
            case 1:
                rtv = "Not Applicable";
                break; 
            case 2:
                rtv = "Right Handed Operation";
                break; 
            case 3:
                rtv = "Left Handed Operation";
                break; 
        }
        
        return rtv;
                                                                           
    } 
    
    
    
    public String STRUCT_getPointingType(int value) {
        String rtv = "";
        switch (value) {
            case 1:
                rtv = "Other";
                break;    
            case 2:
                rtv = "Unknown";
                break; 
            case 3:
                rtv = "Mouse";
                break; 
            case 4:
                rtv = "Track Ball";
                break; 
            case 5:
                rtv = "Track Point";
                break;
            case 6:
                rtv = "Glide Point";
                break;
            case 7:
                rtv = "Touch Pad";
                break;
            case 8:
                rtv = "Touch Screen";
                break;
            case 9:
                rtv = "Mouse - Optical Sensor";
                break;
                                          
        }
        
        return rtv;
                                                                           
    } 
    
    
    
    public String STRUCT_getScanMode(int value) {
        String rtv = "";
        switch (value) {
            case 1:
                rtv = "Other";
                break;    
            case 2:
                rtv = "Unknown";
                break; 
            case 3:
                rtv = "Interlaced";
                break; 
            case 4:
                rtv = "Non Interlaced";
                break; 
        }
        
        return rtv;
                                                                           
    } 
    
    
    
    public String STRUCT_getVideoMemoryType(int value) {
        String rtv = "";
        switch (value) {
            case 1:
                rtv = "Other";
                break;    
            case 2:
                rtv = "Unknown";
                break; 
            case 3:
                rtv = "VRAM";
                break; 
            case 4:
                rtv = "DRAM";
                break; 
            case 5:
                rtv = "SRAM";
                break;
            case 6:
                rtv = "WRAM";
                break;
            case 7:
                rtv = "EDO RAM";
                break;
            case 8:
                rtv = "Burst Synchronous DRAM";
                break;
            case 9:
                rtv = "Pipelined Burst SRAM";
                break;
            case 10:
                rtv = "CDRAM";
                break;
            case 11:
                rtv = "3DRAM";
                break;
            case 12:
                rtv = "SDRAM";
                break;
            case 13:
                rtv = "SGRAM";
                break;                      
                                          
        }
        
        return rtv;
                                                                           
    } 
    
    
    
    public String STRUCT_getVideoArchitecture(int value) {
        String rtv = "";
        switch (value) {
            case 1:
                rtv = "Other";
                break;    
            case 2:
                rtv = "Unknown";
                break; 
            case 3:
                rtv = "CGA";
                break; 
            case 4:
                rtv = "EGA";
                break; 
            case 5:
                rtv = "VGA";
                break;
            case 6:
                rtv = "SVGA";
                break;
            case 7:
                rtv = "MDA";
                break;
            case 8:
                rtv = "HGC";
                break;
            case 9:
                rtv = "MCGA";
                break;
            case 10:
                rtv = "8514A";
                break;
            case 11:
                rtv = "XGA";
                break;
            case 12:
                rtv = "Linear Frame Buffer";
                break;
            case 160:
                rtv = "PC-98";
                break;                      
                                          
        }
        
        return rtv;
                                                                           
    }     
                                    
           
    
    
    public void run() {
        try {        
            creat_VBScript_File();
            execute_VBScript_File();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }   
    }



}