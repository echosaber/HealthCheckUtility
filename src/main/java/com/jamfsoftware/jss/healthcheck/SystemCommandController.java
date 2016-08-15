package com.jamfsoftware.jss.healthcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

/*
* SystemCommandController.java - Written by Jacob Schultz 12/2015
* This class handles running system and MySQL commands on Mac/Windows/Linux.
*/

public class SystemCommandController {

    //Default constructor - nothing to setup.
    public SystemCommandController(){}

    //Runs MySQL queries to get tables larger than 1GB
    //Returns them in an String ArrrayList
    public ArrayList<String> getLargeTableSizes(String MySQLUsername, String MySQLPassword){
        ArrayList<String> tables = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Assume the tool is being run on the server and they are using the JAMF Software table.
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost",MySQLUsername,MySQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT table_name AS 'Table',  round(((data_length + index_length) / 1024 / 1024), 2) as tsize   FROM information_schema.TABLES  WHERE table_schema = 'jamfsoftware';");
            while(result.next()){
                String table_name = result.getString("Table");
                double table_size = result.getDouble("tsize");
                if (table_size > 1024){
                    tables.add(table_name + "," + table_size);
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
        //Print out the large tables, can be blank.
        return tables;
    }

    //Runs a MySQL command to get the total DB size. Returns 0 if the command fails.
    public String getDatbaseSize(String MySQLUsername, String MySQLPassword){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost",MySQLUsername,MySQLPassword);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT table_schema 'db_name', sum( data_length + index_length ) / 1024 / 1024 'db_size', sum( data_free )/ 1024 / 1024 'free_space' FROM information_schema.TABLES WHERE table_schema = 'jamfsoftware' GROUP BY table_schema ;");
            result.next();
            return result.getString("db_size");
        } catch (Exception e){
            System.out.println(e);
            return "0";
        }
    }

    //Runs a shell command on the server from a string
    public String executeCommand(String command){
        String s;
        String output = "";
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null){
                output += s;
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        return output;
    }

    //Runs a number of commands on the server from a string array.
    public String executeCommand(String[] command){
        String s;
        String output = "";
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null){
                output += s;
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        return output;
    }

    //Runs a command to get and parse the tomcat memory.
    public String getJavaMemory(){
        String[] cmd = {"/bin/sh", "-c","ps -ef |grep tomcat"};
        String result = executeCommand(cmd);
        String startup = result.substring(result.indexOf("-Xms") + 4, result.indexOf("-Xmx"));
        String max = result.substring(result.indexOf("-Xmx")+4,result.indexOf("-Xmx")+9);


        return "Startup Memory: " + startup + " Max Memory: " + max;
    }

    //Returns the OS
    public String returnOS(){
        return System.getProperty("os.name");
    }

    //Returns the Version of Java being run
    public String returnJavaVersion(){
        return System.getProperty("java.version");
    }

    /* Total number of processors or cores available to the JVM */
    public int getProcCores(){
        return Runtime.getRuntime().availableProcessors();
    }

    /* Total amount of free memory available to the JVM */
    public long getFreeMem(){
        return Runtime.getRuntime().freeMemory();
    }

    public long getMaxMemory(){
       return Runtime.getRuntime().maxMemory();
    }

    public long getMemoryInUse(){
       return Runtime.getRuntime().totalMemory();
    }

    public long[] getSpaceDetails(){
        long[] values = new long[3];
         /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        for (File root : roots) {
            values[0] = root.getTotalSpace();
            values[1] = root.getFreeSpace();
            values[2] = root.getUsableSpace();
        }
        return values;
    }





}
