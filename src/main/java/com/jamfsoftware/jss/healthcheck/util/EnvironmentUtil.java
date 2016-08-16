package com.jamfsoftware.jss.healthcheck.util;

public final class EnvironmentUtil {
	
	private EnvironmentUtil() {
	}
	
	public static boolean isLinux() {
		return System.getProperty("os.name").contains("Linux");
	}
	
	public static boolean isMac() {
		return System.getProperty("os.name").contains("OS X") ||
				System.getProperty("os.name").contains("macOS");
	}
	
	public static boolean isWindows() {
		return System.getProperty("os.name").contains("Windows");
	}
	
}
