import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.File;
import java.io.FileWriter;
import java.util.prefs.Preferences;

/*
* ConfigurationController.java - Written by Jacob Schultz 1/2016
* This class handles loading the config XML and the values from it.
* Reads the path from the location stored in the prefs.
*/
public class ConfigurationController {
    private String config_xml_path;
    private Preferences prefs = Preferences.userNodeForPackage(UserPrompt.class);
    private boolean isSet;
    private Element root;

    /**
     * Default constructor that just sets the path
     */
    public ConfigurationController(){
        this.config_xml_path = this.prefs.get("config_xml_path","Path to file '/Users/user/desktop/config.xml'");
    }

    /**
     * Constructor that optionally loads the XML file.
     */
    public ConfigurationController(boolean shouldLoadXML){
        if (shouldLoadXML){
            //Read the XML File path.
            this.config_xml_path = this.prefs.get("config_xml_path","Path to file '/Users/user/desktop/config.xml'");
            //Check to make sure they have a path set and the file can be read.
            if (pathIsSet() && canGetFile()){
                //Try loading the XML file and it's values. Setup the class level root var.
                try {
                    SAXBuilder builder = new SAXBuilder();
                    File xmlFile = new File(this.config_xml_path);
                    Document document = (Document) builder.build(xmlFile);
                    this.root = document.getRootElement();
                } catch (Exception e){
                    System.out.print(e);
                }
            }
        }
    }

    public String getConfig_xml_path(){
        return this.prefs.get("config_xml_path","Path to file '/Users/user/desktop/config.xml'");
    }

    /**
     * This method attempts to find the config XML in the same directory that the tool is
     * currently executing from. If it can find a config.xml file, it then verifies it is
     * in the format the tool is expecting. Returns false if can't be found or not properly formatted.
     */
    public boolean attemptAutoDiscover(){
        try {
            String current_path_full =  new File(ConfigurationController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toString();
            if (current_path_full.lastIndexOf("healthcheck.jar") == -1){
                System.out.println("Unable to auto discover healthcheck config.xml file. Prompting user.");
                return false;
            }
            String base_path = current_path_full.substring(0,current_path_full.lastIndexOf("healthcheck.jar"));
            String final_path = base_path + "config.xml";
            if (canGetFile(final_path)){
                prefs.put("config_xml_path", final_path);
                return true;
            } else {
                System.out.println("Unable to auto discover healthcheck config.xml file. Prompting user.");
                return false;
            }
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    /**
     * Checks to see if the config.xml is in the default location.
     */
    public boolean pathIsSet(){
        if (this.config_xml_path.equals("Path to file '/Users/user/desktop/config.xml'")){
            this.isSet = false;
            return false;
        } else {
            this.isSet = true;
            return true;
        }
    }

    /**
     * This method checks if the file can be read.
     * Just supplying any old XML file will cause this method to return false.
     * It checks it can read elements like the JSS_URL, it is important that the XML if formatted correctly.
     */
    public boolean canGetFile(){
        File xmlFile = new File(this.prefs.get("config_xml_path","Path to file '/Users/user/desktop/config.xml'"));
        if (xmlFile.exists()){
            SAXBuilder builder = new SAXBuilder();
            //Try to get the JSS URL element to make sure it is the correct XML file
            try {
                Document document = builder.build(xmlFile);
                Element root = document.getRootElement();
                root.getChild("jss_url").getValue();
                return true;
            }
            catch (Exception e ){
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This method checks if the file can be read.
     * Just supplying any old XML file will cause this method to return false.
     * It checks it can read elements like the JSS_URL, it is important that the XML if formatted correctly.
     * This method takes in a string path, instead of what is stored in the user pref.
     */
    public boolean canGetFile(String path){
        File xmlFile = new File(path);
        if (xmlFile.exists()){
            SAXBuilder builder = new SAXBuilder();
            //Try to get the JSS URL element to make sure it is the correct XML file
            try {
                Document document = builder.build(xmlFile);
                Element root = document.getRootElement();
                root.getChild("jss_url").getValue();
                return true;
            }
            catch (Exception e ){
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @return the config xml path.
     */
    public String getPath(){
        return config_xml_path;
    }

    /**
     * Sets the config XML path.
     */
    public void setPath(String new_path){
        prefs.put("config_xml_path", new_path);
    }


    /**
     * This method reads in a CSV path to keys and a CSV string of keys.
     * It loops down the path given, and then searches for all of the keys.
     * @return string array of all of the found keys.
     */
    public String[] getValue(String path_string, String keys_string){
        Element object = null;
        //Make two arrays out of the path given and a final content array that will be filled.
        String[] path = path_string.split(",");
        String[] keys = keys_string.split(",");
        String[] content = new String[keys.length];
        //If the path is not the root XML
        if (path.length > 1){
            //Loop through the path
            for (int i = 0; i < path.length; i++){
                //Get the first child from the root initialized by the constructor
                if (i == 0){
                    //Set object equal to the root.
                    object = this.root.getChild(path[i]);
                //Its not the root, so use the object.
                } else {
                    object = object.getChild(path[i]);
                }
            }
        //If the path is only one in length then just get elements from the root
        } else {
            object = this.root;
        }

        //Loop through the keys and fill the content array.
        for (int i = 0; i < keys.length; i++){
            content[i] = object.getChild(keys[i]).getValue();
        }

        return content;
    }

    /**
     * This method updates XML values from the Health Check GUI.
     * Not all items are supported. If it can't find the XML file,
     * it will print the error message. Could cause errors if the structure
     * of the XML file has been modified.
     */
    public void updateXMLValue(String item, String value){
        if (item.equals("jss_url")){
            this.root.getChildren().get(0).setText(value);
        } else if (item.equals("jss_username")){
            this.root.getChildren().get(1).setText(value);
        } else if (item.equals("jss_password")){
            this.root.getChildren().get(2).setText(value);
        } else if (item.equals("smart_groups")){
            this.root.getChildren().get(5).getChildren().get(1).setText(value);
        } else if (item.equals("extension_attributes")){
            this.root.getChildren().get(5).getChildren().get(2).getChildren().get(0).setText(value);
            this.root.getChildren().get(5).getChildren().get(2).getChildren().get(1).setText(value);
        }

        try {
            XMLOutputter o = new XMLOutputter();
            o.setFormat(Format.getPrettyFormat());
            o.output(this.root,new FileWriter(getConfig_xml_path()));
        } catch (Exception e){
            System.out.println("Unable to update XML file.");
            System.out.println(e);
        }

    }


}
