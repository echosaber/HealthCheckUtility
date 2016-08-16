package com.jamfsoftware.jss.healthcheck;

import java.io.ByteArrayInputStream;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jamfsoftware.jss.healthcheck.controller.HTTPController;

/*
* JSSConnectionTest.java - Written by Jacob Schultz 1/2016
* This class uses the HTTPController to check the connection to the JSS.
* To verify the connection is uses a simple GET to the /JSSResource/jssuser object.
*/

public class JSSConnectionTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JSSConnectionTest.class);
	
	private HTTPController api;
	private String url;
	
	//Setup the HTTP Connection
	public JSSConnectionTest(String url, String username, String password) {
		this.api = new HTTPController(username, password);
		if (url.endsWith("/")) {
			this.url = url.substring(0, url.length() - 1);
		} else {
			this.url = url;
		}
	}
	
	//Simple method to check if a connection to the JSS can be made.
	//Will return true if the result response code is 200.
	public boolean canConnect() {
		try {
			return api.returnGETResponseCode(url + "/JSSResource/jssuser") == 200;
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return false;
	}
	
	//This method is a 99.9% way to check for a cloud JSS.
	//Hits the /jssuser object and then checks for "c" in the JSS Version
	//This would indicate a cloud build.
	public boolean isCloudJSS() {
		try {
			String xml_as_string = api.doGet(url + "/JSSResource/jssuser");
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(new ByteArrayInputStream(xml_as_string.getBytes("UTF-8")));
			String result = doc.getRootElement().getChild("version").getValue();
			
			return result.contains("c");
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return false;
	}
	
}
