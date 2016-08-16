package com.jamfsoftware.jss.healthcheck.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jamfsoftware.jss.healthcheck.TrustModifier;
import com.jamfsoftware.jss.healthcheck.util.SSLUtilities;

/*
* HTTPController.java
* This class handles all of the HTTP Connections and API calls.
*/

public class HTTPController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPController.class);
	private static final String USER_AGENT = "Mozilla/5.0";
	
	private String username;
	private String password;
	
	public HTTPController(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String doGet(String URL) throws Exception {
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		URL obj = new URL(URL);
		//Relax host checking for Self Signed Certs
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		TrustModifier.relaxHostChecking(con);
		//Setup the Connection.
		con.setRequestMethod("GET");
		con.setRequestProperty("User_Agent", USER_AGENT);
		Base64 b = new Base64();
		String encoding = b.encodeAsString((username + ":" + password).getBytes());
		con.setRequestProperty("Authorization", "Basic " + encoding);
		
		int responseCode = con.getResponseCode();
		LOGGER.debug("Sending 'GET' request to URL : " + URL);
		LOGGER.debug("Response Code : " + responseCode);
		
		//Get the response
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
	
	//Preforms an API GET to a URL and returns only the response code as an int.
	public int returnGETResponseCode(String URL) throws Exception {
		URL obj = new URL(URL);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		TrustModifier.relaxHostChecking(con);
		
		con.setRequestMethod("GET");
		con.setRequestProperty("User_Agent", USER_AGENT);
		Base64 b = new Base64();
		String encoding = b.encodeAsString((username + ":" + password).getBytes());
		con.setRequestProperty("Authorization", "Basic " + encoding);
		
		return con.getResponseCode();
	}
	
}
