import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;
import java.net.*;

/*
* HTTPController.java
* This class handles all of the HTTP Connections and API calls.
*/

public class HTTPController {

    private final String USER_AGENT = "Mozilla/5.0";
    private String username;
    private String password;

    //Set the Username and Password for the JSS.
    public HTTPController(String username, String password){
        this.username = username;
        this.password = password;
    }

    public HTTPController(){
        System.out.println("No username/password defined.");
    }

    //Send a GET request to a URL. Returns the response as a String.
    public String sendGet(String URL) throws Exception {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        URL obj = new URL(URL);
        //Relax host checking for Self Signed Certs
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        TrustModifier.relaxHostChecking(con);
        //Setup the Connection.
        con.setRequestMethod("GET");
        con.setRequestProperty("User_Agent", USER_AGENT);
        Base64 b = new Base64();
        String encoding = b.encodeAsString(new String(username + ":" + password).getBytes());
        con.setRequestProperty  ("Authorization", "Basic " + encoding);

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + URL);
        //System.out.println("Response Code : " + responseCode);

        //Get the response
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //Return the response as a string.
        return response.toString();
    }

    public String getJSSSumary() throws Exception{
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        String URL = "https://support.q.jamfsw.corp:8443/summary.html?2=on&3=on&4=on&6=on&5=on&9=on&7=on&313=on&24=on&350=on&22=on&26=on&23=on&24=on&25=on&28=on&27=on&312=on&53=on&54=on&54=on&255=on&24=on&51=on&65=on&80=on&136=on&135=on&133=on&134=on&137=on&221=on&166=on&72=on&141=on&124=on&125=on&158=on&252=on&163=on&310=on&381=on&90=on&91=on&92=on&96=on&95=on&94=on&93=on&74=on&75=on&76=on&82=on&81=on&122=on&118=on&119=on&73=on&117=on&123=on&83=on&11=on&77=on&171=on&128=on&86=on&131=on&314=on&169=on&87=on&41=on&42=on&43=on&360=on&44=on&45=on&tableRowCounts=on&tableSize=on&Action=Create&username=admin&password=jamf1234";
        URL url = new URL(URL);
            String output = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
                output+= line;
            }
        }
        return output;
    }

    //Preforms an API GET to a URL and returns only the response code as an int.
    public int returnGETResponseCode(String URL) throws Exception{
        URL obj = new URL(URL);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        TrustModifier.relaxHostChecking(con);

        con.setRequestMethod("GET");
        con.setRequestProperty("User_Agent", USER_AGENT);
        Base64 b = new Base64();
        String encoding = b.encodeAsString(new String(username + ":" + password).getBytes());
        con.setRequestProperty  ("Authorization", "Basic " + encoding);

        return con.getResponseCode();
    }

}
