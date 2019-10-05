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
	
	/*
	 * TODO Configure Eclipse to always convert between tabs or spaces
	 */
	
	/**
	 * Stores the text files in the directory as paths in a list.
	 */
//	private List<Path> allFiles = new ArrayList<>(); // TODO Static means shared, public means not encapsulated

	/**
	 * Traverses through a given path and adds text files to list.
	 * 
	 * @param path the path to traverse
	 * @return the list of text files from the directory
	 * @throws IOException if unable to access directory
	 */
	public static List<Path> traverseDirectory(Path path) throws IOException {
		List<Path> allFiles = new ArrayList<>();
		traverseDirectory(path, allFiles);
		return allFiles;
	}
	
	private static void traverseDirectory(Path path, List<Path> allFiles) throws IOException {
		if (Files.isDirectory(path)) {

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {

				for (Path entry : stream) {
					// Use recursion to loop through nested directories.
					traverseDirectory(entry, allFiles);
				}
			}
		}
		// Only adds text files to list.
		else if (isTextFile(path)) {
			allFiles.add(path);
		}
	}
	
	
	/**
	 * Determines whether the file is a text file if it ends in ".txt" or ".text".
	 * 
	 * @param file the file to test if its a text file
	 * @return {@code true} if the argument is a text file
	 */
	public static boolean isTextFile (Path file) {
		// Converts path to String in lower case.
		String name = file.getFileName().toString().toLowerCase();
		// Tests if it ends in text file format.
		if (name.endsWith(".txt") || name.endsWith(".text")) {
			return true;
		}
		return false;
		
		// TODO return (name.endsWith(".txt") || name.endsWith(".text"));
	}
}