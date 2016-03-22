import java.io.Console;
import java.util.Scanner;
import java.util.prefs.Preferences;

/*
* UserPromptHeadless.java - Written 1/2016 by Jacob Schultz
* This class handles taking input from the user in the text based or headless interface.
* It prompts for the JSS Username, Password and URL as well as the MySQL information.
* It is designed to run on linux servers, but also is supported on Mac and Windows.
* The tool will run against a cloud JSS, but it is not recommenced, as the system checks will not be accurate.
*/

public class UserPromptHeadless {
    //Prefs that store the config.xml.
    private static Preferences prefs = Preferences.userNodeForPackage(UserPrompt.class);

    //Default constrictor. Called from Main.java.
    public UserPromptHeadless(){
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        //Create a new config controller to handle loading config.xml values.
        ConfigurationController con = new ConfigurationController();
        //Loop to prompt the user for a valid config.xml file. If it is not found, loop back through.
        while(!con.canGetFile()){
            if (!(con.attemptAutoDiscover())){
                System.out.println("Path to Config.xml not found. Please type the full path below or type 'exit' to close the program. ");
                String path = scanner.next();
                //If they type exit to grab the XML, exit.
                if (path.equals("exit")){
                    System.exit(0);
                } else {
                    prefs.put("config_xml_path", path);
                }
            }
        }
        //Print a welcome message.
        System.out.println("Welcome to the JSS Health Check Tool. \nThis tool will generate a report that can be printed to a text file.\nThe headless version of this tool is not designed to work with a cloud hosted JSS.");
        //Read in all of the user information securely.
        String jssURL = console.readLine("\nEnter JSS URL: ");
        String jssUsername = console.readLine("Enter JSS Username: ");
        String jssPassword = new String(console.readPassword("Enter JSS Password: "));

        //Setup a new connection test to make sure we can connect before starting the test.
        final JSSConnectionTest test = new JSSConnectionTest(jssURL,jssUsername,jssPassword);
        while (!test.canConnect()){
            System.out.println("Unable to connect to the JSS. Prompting again.\nType 'exit' to leave the program.");
            jssURL = console.readLine("\nEnter JSS URL: ");
            if (jssURL.equals("exit")){
                System.exit(0);
            }
            jssUsername = console.readLine("Enter JSS Username: ");
            jssPassword = new String(console.readPassword("Enter JSS Password: "));
            if (jssUsername.equals("exit") || jssPassword.equals("exit")){
                System.exit(0);
            }
        }

        if (test.isCloudJSS()){
            System.out.println("The headless version of this tool is not intended to run against a cloud JSS.");
            System.out.println("Please run the tool on Mac OSX or Windows to view a health report.");
        }

        //Perform a new Health Check
        HealthCheck newHealthCheck = new HealthCheck(true,jssURL,jssUsername,jssPassword);

        //Generate a new headless report
        HealthReportHeadless report = new HealthReportHeadless(newHealthCheck.getJSONAsString());
        try {
            //Print the report
            report.printReport();
        } catch (Exception e){
            System.out.println("Encountered a fatal error.");
            e.printStackTrace();
        }
    }
}
