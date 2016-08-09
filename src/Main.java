import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;
import java.util.prefs.Preferences;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Main.java - Written by Jacob Schultz 12/2015
* This class handles the initial load of the program.
* It detects the OS, and then opens the text interface for linux and a GUI for Windows and Mac.
* Can be run with a -h flag to open the text interface on Mac and Windows
* [STUB] If ran with the -m flag it will start a POC for the health monitor.
*/

public class Main {
    //This preferences bundle stores the location of the configuration XML
    private static Preferences prefs = Preferences.userNodeForPackage(UserPrompt.class);

    public static void main(String[] args) throws java.lang.Exception{
        Boolean HTMLInterface = false;
        if (HTMLInterface){
            HealthReport2.launchHealthReport2(args);
        } else {

            //Start the util normally
            if (args.length == 0) {
                //If it is a linux server, don't prompt, and use the text interface
                if (System.getProperty("os.name").contains("Linux")) {
                    new UserPromptHeadless();
                    //Present the GUI
                } else {
                    ConfigurationController con = new ConfigurationController();
                    //Loop until you can get a valid config xml path.
                    while (!con.canGetFile()) {
                        if (!(con.attemptAutoDiscover())) {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setDialogTitle("Select Configuration XML File");
                            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                    "XML Files", "xml");
                            chooser.setFileFilter(filter);
                            int returnVal = chooser.showOpenDialog(null);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                //Update the path
                                prefs.put("config_xml_path", chooser.getSelectedFile().getAbsolutePath());
                            } else {
                                System.exit(0);
                            }
                        }
                    }
                    try {
                        //Open the GUI for Windows and Mac
                        new UserPrompt();
                    } catch (java.lang.Exception e) {
                        //Catch any errors encountered, display them in a popup and print them.
                        System.out.print(e);
                        JOptionPane.showMessageDialog(new JFrame(), "A fatal error has occurred.\n" + e, "Fatal Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }


                }
                //They provided the args to start the headless version OR the monitor
            } else {
                if (args[0].equals("-h")) {
                    new UserPromptHeadless();
                }
                if (args[0].equals("-m")) {
                    Scanner scanner = new Scanner(System.in);
                    ConfigurationController con = new ConfigurationController();
                    while (!con.canGetFile()) {
                        if (!(con.attemptAutoDiscover())) {
                            System.out.println("Path to Config.xml not found. Please type the full path below or type 'exit' to close the program. ");
                            String path = scanner.next();
                            //If they type exit to grab the XML, exit.
                            if (path.equals("exit")) {
                                System.exit(0);
                            } else {
                                prefs.put("config_xml_path", path);
                            }
                        }
                    }

                    String xml_path = prefs.get("config_xml_path", "Path to file '/Users/user/desktop/config.xml'");
                    SAXBuilder builder = new SAXBuilder();
                    File xmlFile = new File(xml_path);
                    try {
                        Document document = (Document) builder.build(xmlFile);
                        Element root = document.getRootElement();
                        HealthCheck hc = new HealthCheck();
                        int mobile_device_length = hc.checkAPILength(root.getChild("jss_url").getValue(), root.getChild("jss_username").getValue(), root.getChild("jss_password").getValue(), "mobiledevicecommands");
                        int computer_length = hc.checkAPILength(root.getChild("jss_url").getValue(), root.getChild("jss_username").getValue(), root.getChild("jss_password").getValue(), "computercommands");
                        writeLogEntry(mobile_device_length, computer_length);
                    } catch (java.lang.Exception e) {
                        System.out.print(e);
                        System.out.println("Config XML file damaged. Unable to run monitor.");
                    }

                }
                if (args[0].equals("-g")) {
                    new MonitorGraph("");
                }
            }
        }

    }
    private static void writeLogEntry(int mobileDeviceLength, int computerLength){
        try {
            FileWriter fw = new FileWriter("",true);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm");
            Date date = new Date();
            fw.write(df.format(date) + " " + mobileDeviceLength + " " + computerLength + " " + Runtime.getRuntime().freeMemory() + " " + Runtime.getRuntime().totalMemory() + "\n");
            fw.close();
        }
        catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }


}
