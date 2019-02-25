
import java.util.*;
import java.awt.SystemTray;
import java.io.*;
import java.net.*;

/**
 * @author Faizan Zafar
 *
 */
public abstract class Main extends TimerTask {
	//
	// Setting up all the variables that are used in this file
	//
	// Location of the folders that are to be searched
	static File secondFolder = new File("C:\\Users\\username\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs");
	static File startFiles = new File("C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs");				
	//location and name of the files created
	static String inputPath = "C:/ProgramData/RunnerProgramsListUnsorted.txt";
	static String filePath = "C:/ProgramData/RunnerProgramsList.txt";
	static FileFinder filefinder = new FileFinder();
	static AlphabeticallySortLinesOfTextInFile sort = new AlphabeticallySortLinesOfTextInFile();
	// URL of website where the IFTTT webhooks points to
	static String webSite = "http://web.site/app.txt";
	static String reSetUrl = "http://web.site/reset.php";
	static Map<String, String> programs = new HashMap<String, String>();
	
	//
	// Grabs the data from the txt file from the website
	//
	public static String getHTML(String urlToRead) throws Exception {
		  out("Welcome To Runner");
	      StringBuilder result = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      return result.toString();
	   }

	public static void main(String[] args) throws Exception 
	{
		// Get Name from website
		
		String webtxt = getHTML(webSite);
		out(webtxt);
		programList();
		// splits the website data but this can be left out if you chose not to put a timestamp in the webhook
		
		String[] arrOfStr = webtxt.split(";", 5);
		String rawinput = arrOfStr[0];
		String input = rawinput.replaceAll("\\s+","").toLowerCase();
		//System.out.println(Arrays.toString(arrOfStr) + "2");
		
		if(!input.equals("quit") && !input.isEmpty() && !input.equals("")) 
		{
				String program = programs.get(input);
				System.out.println(program);
				if (program != null) {
				run(program);
				  if (SystemTray.isSupported()) {
			            Notifacation td = new Notifacation();
			            td.displayTray("Runner", "Now Running "+rawinput);
			        }
				}
				else {
					  if (SystemTray.isSupported()) {
				            Notifacation td = new Notifacation();
				            td.displayTray("Runner Error", "There is no program with name "+rawinput);
					  }
					 }
		}else if (input.equals("quit")) 
		{
			System.exit(0);
		}
	
		//run the reset url wait 4 seconds then restart the main
		reset();
		Thread.sleep(4000);
		main(args);	
	}
	/*
	 * open the program
	 */
	public static void run(String path) throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder("cmd", "/c", path);
		Process process = pb.start();
	}
	/*
	 * creates a file with name and loction of all the apps in the start menu
	 */
	public static void addToList() throws Exception 
	{
		FileFinder.displayDirectoryContents(startFiles,inputPath);
		FileFinder.displayDirectoryContents(secondFolder,inputPath);
		//FileFinder.displayDirectoryContents(ANYFOLDERLOCATION, inputPath);
		
		//organizes the file for later use 
		sort.main(inputPath,filePath);
		reset();
	}
	/*
	 * Reads the file and creates a hash map that is used to grab the location of the file given the name
	 */
	public static void programList() throws Exception
	{
		File f = new File(filePath);
		if(f.exists() && !f.isDirectory() && f.length() > 0) { 
				try {
					// Reads programs file to check for names and paths
					BufferedReader readFromFile = new BufferedReader(new FileReader(filePath));
					String rawInput;
				    while ((rawInput = readFromFile.readLine()) != null) {
				    	String[] splitInput = rawInput.split("\\[\\`\\!\\`\\]");
				  		if (splitInput.length != 2) {
				  			System.out.println("Invalid data entry read: " + rawInput);
				    		continue;
				  		}
				        try {
				        	programs.put(splitInput[0], splitInput[1]);
				  		} catch (Exception ex) {
				            System.out.println("Invalid data entry read (value is not an integer): " + splitInput[1]);
				  		}
				  	}
				    readFromFile.close();
				} catch (IOException ex) {
				    System.out.println("IOException caught while reading file: " + ex.getMessage());
				}
			}
		else {
				addToList();
				programList();
		}
	}
/*
 * resets the txt page so it is blank
 */
	public static void reset() throws Exception
	{
		String reset = getHTML(reSetUrl);
		out(reset);
	}	
	// Basic print command to make it easier to test
	public static void out(String out) 
	{
		System.out.println(out);
	}

	

}
