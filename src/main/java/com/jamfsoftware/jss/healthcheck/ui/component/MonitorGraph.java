package com.jamfsoftware.jss.healthcheck.ui.component;

/**
 * Created by jacobschultz on 3/21/16.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorGraph {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorGraph.class);
	
	private String[][] LOG_DATA;
	
	public MonitorGraph(String logFilePath) {
		parseLogData(logFilePath);
	}
	
	private void parseLogData(String logFilePath) {
		try {
			try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
				List<String> lines = new ArrayList<>();
				
				String line;
				while ((line = br.readLine()) != null) {
					lines.add(line);
				}
				
				this.LOG_DATA = new String[lines.size()][6];
				for (int i = 0; i < lines.size(); i++) {
					String[] data = lines.get(i).split(" ");
					System.arraycopy(data, 0, this.LOG_DATA[i], 0, data.length);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Unable to find log file.", e);
		}
		
	}
	
	public void showGraph() {
		
	}
	
}
