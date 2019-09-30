import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for traversing a directory and adding all the text
 * files into a list.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class DirectoryTraverser {
	
	/**
	 * Stores the text files in the directory as paths in a list.
	 */
	public static List<Path> allFiles;
	
	/**
	 * Initializes this list.
	 */
	public DirectoryTraverser() {
		allFiles = new ArrayList<>();
	}
	
	/**
	 * Traverses through a given path and adds text files to list.
	 * 
	 * @param path the path to traverse
	 * @return the list of text files from the directory
	 * @throws IOException if unable to access directory
	 */
	public List<Path> traverseDirectory (Path path) throws IOException {
		
		if (Files.isDirectory(path)) {
			
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
		    	   
	           for (Path entry: stream) {
	        	   // Use recursion to loop through nested directories.
	        	   if (Files.isDirectory(entry)) {
	        		   traverseDirectory (entry);
	        	   }
	        	   else {
	        		   //Only add text files to list.
	        		   if (isTextFile(entry)) {
		            	   allFiles.add(entry);
		               }
	        	   }
	           }
			}
		}
		else {
			if (isTextFile(path)) {
				allFiles.add(path);
            }
		}
       return allFiles;
	}
	
	
	/**
	 * Determines whether the file is a text file if it ends in ".txt" or ".text".
	 * 
	 * @param file the file to test if its a text file
	 * @return {@code true} if the argument is a text file
	 */
	public static boolean isTextFile (Path file) {
		// Converts path to String in lowercase.
		String name = file.getFileName().toString().toLowerCase();
		// Tests if it ends in text file format.
		if (name.endsWith(".txt") || name.endsWith(".text")) {
			return true;
		}
		return false;
	}
}