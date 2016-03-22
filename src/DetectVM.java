import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * DetectVM.java Created by Jacob Schultz on 9.2.16.
 * This class checks the operating system version, then
 * runs a system command to check with relative certainty
 * if the host is a VM.
 *
 */
public class DetectVM {

    private boolean isVM;

    /**
     * Constructor that detects the OS Type,
     * then attempts to determine if it is a VM.
     */
    public DetectVM(){
        if(System.getProperty("os.name").contains("Linux")){
            this.isVM = getVMStatusLinux();
        } else if (System.getProperty("os.name").contains("Windows")){
            this.isVM = getVMStatusWindows();
        } else if (System.getProperty("os.name").contains("OS X")){
            this.isVM = getVMStatusOSX();
        } else {
            System.out.println("Unable to detect OS type.");
        }
    }

    public DetectVM(String root_password){
        if(System.getProperty("os.name").contains("Linux")){
            this.isVM = getVMStatusLinuxBetter(root_password);
        } else if (System.getProperty("os.name").contains("Windows")){
            this.isVM = getVMStatusWindows();
        } else if (System.getProperty("os.name").contains("OS X")){
            this.isVM = getVMStatusOSX();
        } else {
            System.out.println("Unable to detect OS type.");
        }
    }

    /**
     * Method that returns private VM boolean.
     * @return boolean of if it is a VM or Not
     */
    public boolean getIsVM(){
        return this.isVM;
    }

    private boolean getVMStatusLinux(){
        String[] command = {"/bin/sh", "-c","ls -l /dev/disk/by-id/"};
        String value = executeCommand(command);
        if (value.contains("QEMU") || value.contains("VMware") || value.contains("VirtualBox") || value.contains("KVM") || value.contains("Bochs") || value.contains("Parallels")){
            return true;
        } else {
            return false;
        }
    }

    private boolean getVMStatusLinuxBetter(String root_password){
        String[] command =  {"echo " + root_password + " | sudo -S dmidecode -s system-product-name"};
        String value = executeCommand(command);
        if (value.contains("VMware Virtual Platform") || value.contains("VirtualBox") || value.contains("KVM") || value.contains("Bochs") || value.contains("Parallels")){
            return true;
        }
        String[] command2 =  {"echo " + root_password + " | sudo -S dmidecode egrep -i 'manufacturer|product'"};
        value = executeCommand(command2);
        if (value.contains("Microsoft Corporation") && value.contains("Virtual Machine")){
            return true;
        }
        return false;
    }

    private boolean getVMStatusWindows(){
        String[] command =  {"SYSTEMINFO"};
        String value = executeCommand(command);
        if (value.contains("VMWare") || value.contains("VirtualBox") || value.contains("KVM") || value.contains("Bochs") || value.contains("Parallels")){
            return true;
        } else {
            return false;
        }
    }

    private boolean getVMStatusOSX(){
        String[] command =  {"/bin/sh", "-c","ioreg -l | grep -e Manufacturer -e 'Vendor Name'"};
        String value = executeCommand(command);
        if (value.contains("VirtualBox") || value.contains("VMware") || value.contains("Oracle") || value.contains("Bochs") || value.contains("Parallels")){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method executes a command on the host system.
     * @return Command output
     */
    private String executeCommand(String[] command){
        String s; String output = "";
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null){
                output += s;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return output;
    }
}

