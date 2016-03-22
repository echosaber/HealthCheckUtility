import java.util.*;

public class JSSSummary {
    private String summary_string = "";
    private String[][] summary;
    private int password_info_index = 0;
    private int clustered_index = 0;
    private int activation_code_index = 0;
    private int change_managment_index = 0;
    private int tomcat_index = 0;
    private int log_flushing_index = 0;
    private int push_cert_index = 0;
    private int login_logout_hooks_index = 0;
    private int table_sizes = 0;
    private int os_index = 0;
    private int java_version_index = 0;
    private boolean received401Error = false;
    private int table_row_counts = 0;

    public JSSSummary(String summary){
        if (summary == null){
            if (System.getProperty("os.name").contains("Linux")){
                System.out.println("The tool was unable to get the JSS Summary with this account. Please try again with another account that has read access and access to the JSS Summary. (HTTP 401)");
            } else {
                new JSSSummaryPasteHandler();
                System.out.println("The tool was unable to get the JSS Summary with this account. Please try again with another account that has read access and access to the JSS Summary. (HTTP 401)");
            }
            System.exit(0);
        }
        this.summary_string = summary;
        generateArrays(summary);
        generateIndexes();
    }

    public boolean get401ErrorStatus(){
        return this.received401Error;
    }


    public void setJSSSummary(String summary){
        this.summary_string = summary;
        generateArrays(summary);
        generateIndexes();
    }

    private void generateArrays(String summary){
        if (this.summary_string.contains("java.io.IOException: Server returned HTTP response code: 401 for URL:") || this.summary_string == null){
            if (System.getProperty("os.name").contains("Linux")){
                System.out.println("The tool was unable to get the JSS Summary with this account. Please try again with another account that has read access and access to the JSS Summary. (HTTP 401)");
            } else {
                new JSSSummaryPasteHandler();
                System.out.println("The tool was unable to get the JSS Summary with this account. Please try again with another account that has read access and access to the JSS Summary. (HTTP 401)");
            }
            this.received401Error = true;
            System.exit(0);
        }
        String[] sections = summary.split("==========================================================================================");
        String[][] sum = new String[sections.length][1];
        for (int i = 0; i < sections.length; i++){
            sum[i] = sections[i].split("----------------------------------------------------------------------------------");
        }
        this.summary = sum;
    }

    private void generateIndexes(){
        for (int i = 0; i < this.summary.length; i++){
            if (this.summary[i].length > 0 && this.summary[i] != null){
                if (this.summary[i][0].equals("Password Policy")){
                    password_info_index = i;
                }
                if (this.summary[i][0].equals("Clustering")){
                    clustered_index = i;
                }
                if (this.summary[i][0].equals("Activation Code")){
                    activation_code_index = i;
                }
                if (this.summary[i][0].equals("Change Management")){
                    change_managment_index = i;
                }
                if (this.summary[i][0].equals("Apache Tomcat Settings")){
                    tomcat_index = i;
                }
                if (this.summary[i][0].equals("Log Flushing")){
                    log_flushing_index = i;
                }
                if (this.summary[i][0].equals("Push Certificates")){
                    push_cert_index = i;
                }
                if (this.summary[i][0].equals("Check-In")){
                    login_logout_hooks_index = i;
                }
                if (this.summary[i][0].equals("Table sizes")){
                    table_sizes = i;
                }
                if (this.summary[i][0].equals("Table row counts")){
                    table_row_counts = i;
                }
            }
        }
    }

    public String getWebAppDir(){
        String[] el = this.summary[1][3].split("\t");
        String check = "Web App Installed To";
        String with_chars = el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim();
        String sub1 = with_chars.replaceAll(":", "");
        String sub2 = sub1.replaceAll("\\\\","/");
        return sub2;
    }

    public String getJavaVendor(){
        String[] el = this.summary[1][4].split("\t");
        String check = "Java Vendor";
        return el[3].substring(el[3].indexOf(check) + check.length(), el[3].length()).trim();
    }

    public String getTableRowCounts(){
        String[] el = this.summary[table_row_counts+1][0].split("\t");
        String computers_denormalized = "";
        String computers = "";
        String mobile_devices = "";
        String mobile_devices_denormalized = "";
        for (int i = 0; i < el.length; i++){
            if (el[i].contains("computers")){
                if (el[i].contains("computers_denormalized")){
                    String check = "computers_denormalized";
                    computers_denormalized = el[i].substring(el[i].indexOf(check) + check.length(), el[i].length()).trim().replace(".","");
                } else {
                    String check = "computers";
                    computers = el[i].substring(el[i].indexOf(check) + check.length(), el[i].length()).trim().replace(".","");
                }
            }

            if (el[i].contains("mobile_devices")){
                if (el[i].contains("mobile_devices_denormalized")){
                    String check = "mobile_devices_denormalized";
                    mobile_devices_denormalized = el[i].substring(el[i].indexOf(check) + check.length(), el[i].length()).trim().replace(".","");
                } else {
                    String check = "mobile_devices";
                    mobile_devices = el[i].substring(el[i].indexOf(check) + check.length(), el[i].length()).trim().replace(".","");
                }
            }

        }
        return (computers + "," + computers_denormalized + "," + mobile_devices + "," + mobile_devices_denormalized).trim();
    }

    public String getJavaVersion(){
        String[] el = this.summary[1][4].split("\t");
        String check = "Java Version";
        return el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim();
    }

    public String getOperatingSystem(){
        String[] el = this.summary[1][2].split("\t");
        String check = "Operating System";
        return el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim();
    }

    public String getMySQLVersion(){
        String[] el = this.summary[1][9].split("\t");
        String check = "version";
        String v = el[20].substring(el[20].indexOf(check) + check.length(), el[20].length()).trim();
        return v.replace("....................", "");
    }

    public double getDatabaseSize(){
        String[] el = this.summary[1][5].split("\t");
        String check = "Database Size";
        String size_split = el[6].substring(el[6].indexOf(check) + check.length(), el[6].length()).trim();
        String size = size_split.substring(size_split.indexOf(" "), size_split.length());

        double size_in_mb = 0;
        if (size.contains("KB")){
            double size_in_kb = Double.parseDouble(size.substring(1,size.length()-3));
            size_in_mb = size_in_kb * 0.001;
        } else if (size.contains("MB")){
            size_in_mb = Double.parseDouble(size.substring(1,size.length()-3));
        } else if (size.contains("GB")){
            double size_in_gb = Double.parseDouble(size.substring(1,size.length()-3));
            size_in_mb = size_in_gb * 1000;
        }

        return size_in_mb;
    }

    public String[] getPasswordInformation(){
        String[] values = new String[4];
        String[] el = this.summary[password_info_index+1][1].split("\t");
        String check = "Require Uppercase";
        values[0] =  el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim();

        check = "Require Lowercase";
        values[1] =  el[2].substring(el[2].indexOf(check) + check.length(), el[2].length()).replace(".","").trim();

        check = "Require Number";
        values[2] =  el[3].substring(el[3].indexOf(check) + check.length(), el[3].length()).trim();

        check = "Require Special Characters";
        values[3] =  el[4].substring(el[4].indexOf(check) + check.length(), el[4].length()).replace(".","").trim();

        return values;
    }

    public String getIsClustered(){
        String[] el = this.summary[this.clustered_index+1][0].split("\t");
        String check = "Clustering Enabled";
        return el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim();
    }

    public String getActivationCodeExpiration(){
        String[] el = this.summary[this.activation_code_index+1][1].split("\t");
        String check = "Expires";
        return el[4].substring(el[4].indexOf(check) + check.length(), el[4].length()).replace(".","").trim();
    }

    public String[] getChangeManagementInfo(){
        String[] values = new String[2];
        String[] el = this.summary[this.change_managment_index+1][0].split("\t");
        String check = "Use Log File";
        values[0] =  el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim();

        check = "Location of Log File";
        values[1] =  el[2].substring(el[2].indexOf(check) + check.length(), el[2].length()).replace(".","").trim();

        return values;
    }

    public String[] getTomcatCert(){
        String[] values = new String[2];
        String[] el = this.summary[this.tomcat_index+1][0].split("\t");
        String check = "SSL Cert Issuer";

        values[0] = el[2].substring(el[2].indexOf(check) + check.length(), el[2].length()).replace(".","").trim();

        check = "SSL Cert Expires";
        values[1] =  el[3].substring(el[3].indexOf(check) + check.length(), el[3].length()).replace(".","").trim();

        return values;
    }

    public String getLogFlushingInfo(){
        String[] el = this.summary[this.log_flushing_index+1][0].split("\t");
        String check = "Time to Flush Logs Each Day";
        return el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim();
    }

    public String[] getPushCertInfo(){
        String[] el;
        String check = "";
        String[] values = new String[2];

        if (this.summary[this.push_cert_index+1][0].contains("MDM Push Notification Certificate")){
             el = this.summary[this.push_cert_index+1][0].split("\t");
             check = "Expires";
                values[0] = el[3].substring(el[3].indexOf(check) + check.length(), el[3].length()).trim();
        } else {
            values[0] = "No Data Available.";
        }

        if (this.summary[this.push_cert_index+2][0].contains("Push Proxy Authorization Token")){
             el = this.summary[this.push_cert_index+1][0].split("\t");
             check = "Expires";
             if (el.length > 3){
                 values[1] =  el[3].substring(el[3].indexOf(check) + check.length(), el[3].length()).trim();
             } else {
                 values[1] = "No Data Available.";
             }
        } else {
            values[1] = "No Data Available.";
        }

        return values;
    }

    public Boolean loginLogoutHooksEnabled(){
        String[] el = this.summary[this.login_logout_hooks_index+1][1].split("\t");
        String check = "Login/Logout Hooks";
        return Boolean.parseBoolean(el[1].substring(el[1].indexOf(check) + check.length(), el[1].length()).trim());

    }

    public ArrayList<String[]> getLargeMySQLTables(){
        ArrayList<String[]> tables = new ArrayList<>();
        String[] el = this.summary[this.table_sizes+1][0].split("\t");
        ArrayList<String[]> size_tracker = new ArrayList<>();
        for (int i = 1; i < el.length; i++){
            String[] name_values = el[i].split(" ");
            String name = name_values[0];
            double size_in_mb = 0;
            String[] vals = new String[2];
            if (name_values[name_values.length-1].contains("KB")){
                double size_in_kb = Double.parseDouble(name_values[name_values.length-2]);
                size_in_mb = size_in_kb * 0.001;
            } else if (name_values[name_values.length-1].contains("MB")){
                size_in_mb = Double.parseDouble(name_values[name_values.length-2]);
            } else if (name_values[name_values.length-1].contains("GB")){
                double size_in_gb = Double.parseDouble(name_values[name_values.length-2]);
                size_in_mb = size_in_gb * 1000;
            }
            vals[0] = name;
            vals[1] = Double.toString(size_in_mb);
            size_tracker.add(vals);
        }
        Collections.sort(size_tracker, new Comparator<String[]> () {
            @Override
            public int compare(String[] o1, String[] o2) {
                Double one = Double.parseDouble(o1[1]);
                Double two = Double.parseDouble(o2[1]);
                return one.compareTo(two);
            }
        });
        for (int i = size_tracker.size()-1; i > size_tracker.size()-11; i--){
            tables.add(size_tracker.get(i));
        }
        return tables;
    }


}
