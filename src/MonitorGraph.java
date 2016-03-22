/**
 * Created by jacobschultz on 3/21/16.
 *
 */
import java.io.*;

public class MonitorGraph {
    String[][] LOG_DATA;

    public MonitorGraph(String logFilePath){
        parseLogData(logFilePath);
    }

    public void parseLogData(String logFilePath){
        try {
            BufferedReader br = new BufferedReader(new FileReader(logFilePath));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String everything = sb.toString();
                String[] lines = everything.split("\n");
                this.LOG_DATA = new String[lines.length][6];
                for (int i = 0; i < lines.length; i++){
                    String[] data = lines[i].split(" ");
                    for (int a = 0; a < data.length; a++){
                        this.LOG_DATA[i][a] = data[a];
                    }
                }
                System.out.println("");
            } finally {
                br.close();
            }
        } catch (Exception e){
            System.out.println("Unable to find log file.");
        }

    }

    public void showGraph(){

    }


}
