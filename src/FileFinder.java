
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class FileFinder {
	public static void displayDirectoryContents(File directory, String filePath) {
		File f = new File(filePath);
		File[] fList = directory.listFiles();
		System.out.println(fList.length);
		try {
		for (int i = 0; i < fList.length; i++) {
		    if (fList[i].isFile()) {
		    	if(fList[i].getAbsolutePath().endsWith(".lnk") || fList[i].getAbsolutePath().endsWith(".url")) {
		       // System.out.println(fList[i].getAbsolutePath());
		       // System.out.println(fList[i].getName());
		        String name = fList[i].getName();
		   		String[] newProgramName = name.split("\\.");
		  		if (newProgramName.length != 2) {
		  			System.out.println("Invalid data entry read: " + newProgramName);
		    		continue;
		  		}
				String FinalProgramName = newProgramName[0].toLowerCase() + "[`!`]" + fList[i].getAbsolutePath();
				try {
					    FileWriter fw = new FileWriter(filePath,true); //the true will append the new data
					    fw.write(FinalProgramName+"\n");//appends the string to the file
					    fw.close();
				}catch (IOException e) {
					//System.out.println("atl error");
				    //exception handling left as an exercise for the reader
				}
		          } 
		       
		    } else if (fList[i].isDirectory()) {
		    	//System.out.println(file.getAbsolutePath());
		    	displayDirectoryContents(fList[i], filePath); 
		    	
		    }
		}
		}catch (Exception ex) {
		}
	}
}