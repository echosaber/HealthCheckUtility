package com.jamfsoftware.jss.healthcheck;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import java.io.ByteArrayInputStream;

/*
* JSSConnectionTest.java - Written by Jacob Schultz 1/2016
* This class uses the HTTPController to check the connection to the JSS.
* To verify the connection is uses a simple GET to the /JSSResource/jssuser object.
*/

public class JSSConnectionTest {

    private HTTPController api;
    private String url;

    //Setup the HTTP Connection
    public JSSConnectionTest(String url, String username, String password){
        this.api = new HTTPController(username,password);
        if ((url.lastIndexOf("/") + 1) == url.length()) {
            this.url = url.substring(0, url.length() - 1);
        } else {
            this.url = url;
        }
    }

    //Simple method to check if a connection to the JSS can be made.
    //Will return true if the result response code is 200.
    public boolean canConnect(){
        try {
            int result = api.returnGETResponseCode(url + "/JSSResource/jssuser");
            if (result == 200){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    //This method is a 99.9% way to check for a cloud JSS.
    //Hits the /jssuser object and then checks for "c" in the JSS Version
    //This would indiciate a cloud build.
    public boolean isCloudJSS(){
        try {
            String xml_as_string = api.sendGet(url + "/JSSResource/jssuser");
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new ByteArrayInputStream(xml_as_string.getBytes("UTF-8")));
            String result = doc.getRootElement().getChild("version").getValue();
            if (result.contains("c")){
                return true;
            } else {
                return false;
            }

        } catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    public String errorCheck(){
        return null;
    }

}
