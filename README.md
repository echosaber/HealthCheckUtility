# JSS Health Check Utility

The JSS Health Check Utility is a lightweight tool for JSS administrators to run inside of their environments to perform automated health checks. It performs checks for things that commonly appear during scheduled health checks, like having too little RAM, expiring items, bad scripts, etc. Simply provide the tool the JSS URL, Username, and Password. It will perform API GETs to the JSS, get the JSS Summary, as well as run some commands on the system, and then display a report to the end user. Nothing is ever edited, only read. The goal of this tool is to help administrators idenitify issues with their environment before the issues assire.

## Getting Started

The latest release will always be available in the "Releases" tab above. 

The tool runs cross platform on Mac, Windows and headless linux, with the linux version using a text interface. It requires Java 7+. There is a single binary that runs across all platforms. A configuration XML file is read by the tool to find values to use in health calculation. There is a sample configuration file, but the settings can be tweaked if you know what you are doing. Simply point the tool to the path to this file, on first run, and then everything is complete. (If the xml file is in the same directory as the jar, it will auto-discover the xml file). 

### Downloading and Running

* Grab the latest release of the healthcheck.jar as well as the config.xml. 
* If you are Windows or Mac OS X simply double click the tool to open it.
* If you are running Linux, or would like more debugging available, run the tool with this command:

```
java -jar healthcheck.jar
```

Everything generated by the tool can be outputted by starting it with this command: 

```
java -jar healthcheck.jar > output.txt
```

If you would like to run the headless, text only version on OSX or Windows run the program with the -h flag:

```
java -jar healthcheck.jar -h
```

* Provide a JSS URL and user account that has, at least, full read permissions.
* Click "Begin Health Check"!

## What does the tool check for?

- OS, Java, and Tomcat Version
- The amount of free/max memory and the amount of free/max space on disk
- **Below data is pulled from the JSS Summary**
- Web App Directory, Java Vendor, Database size and large SQL Tables
- Password Strength, Clustering and Activation Code Expiration
- Change Managment Info, Tomcat Info, Log Flushign Settings and Login/Logout Hook Info
- **Below data is pulled from the JSS API**
- GETs Activation code to be displayed in the interface
- GETs the computer checkin frequency and ensures it is not too high for the enviornment size
- GETs LDAP Server Information to Display 
- GETs GSX and Managed Preference profile information and checks if it is being used
- GETs Computer, Mobile and User group information and warns of high criteria count or nested smart groups
- GETs VPP Accounts and checks for expiring tokens
- GETs all of the scripts, and checks for the old binary version or several other unsafe commands
- GETs printer information, and warns if the printer has a large driver package
- GETs Mobile and Computer Extension Attributes and warns if there are a large number of them
- GETs Network Segments and the SMTP server to display to the user
- GETs all policies, and checks for policies that contain an update inventory with recurring checkin enabled

After all of this data is pulled, the tool will parse the data, and display important items to the end user. The items that releate to the system are not displayed with cloud hosted JSSs. 

### Screenshots
![Screen One](http://i.imgur.com/Meu8rmm.png "Screen One")
![Screen Two](http://i.imgur.com/wDGidaO.png "Screen Two")
![Screen Thre](http://i.imgur.com/S40Ni8T.png "Screen Three")

## Libraries Used and Acknowledgments

* Apache Commons Codec
* Google JSON (gson)
* Java X JSON
* JDOM

## Authors

* **Jake Schultz (JAMF Software)** -Development
* **Carlton Brumbelow (JAMF Software)** -Test Design and Interface 
* **Matt Hegge (JAMF Software)** -Test Design and Interface 

## Support

This tool is provided AS IS, and thus no support from JAMF Software, or any other entities is garunteed. While in it's beta stage, please contact Jake Schultz with any questions or bugs. (jacob.schultz@jamfsoftware.com) Please include any output from the tool in any emails. 

## License

**This program is distributed "as is" by JAMF Software, LLC.**
Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this list of
conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, this list of
conditions and the following disclaimer in the documentation and/or other materials
provided with the distribution.

* Neither the name of the JAMF Software, LLC nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior written
permission.

THIS SOFTWARE IS PROVIDED BY JAMF SOFTWARE, LLC "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JAMF SOFTWARE, LLC BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
DAMAGE.

