import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class Driver {
	
	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {
		
		// Initialize ArgumentParser, InvertedIndex, and the list of files for a given path.
		ArgumentParser parser = new ArgumentParser(args);
		InvertedIndex index = new InvertedIndex();
		List<Path> files = new ArrayList<>(); // TODO Move into -path if statement
		
		if (parser.hasFlag("-path")) {
			try {	
				Path path = parser.getPath("-path");
				
				// Store files in an ArrayList<Path>.
				DirectoryTraverser traverse = new DirectoryTraverser();
				files = traverse.traverseDirectory(path);
				
				// TODO Create a builder class that knows how to build an index from a text file
				// Store elements into InvertedIndex.
				for (Path item : files) {
					/*
					 * TODO Efficiency issue... read the file into a list
					 * Then you loop through the list to add to the index
					 * Causes more time and space than necessary if you didn't have the list
					 * 
					 * addPath(Path path, InvertedIndex index) method
					 * 		- creates a stemmer object
					 * 		- opens up a file and reads line by line
					 * 		- for each line parses the line into words
					 * 		- for each word, stem and add immediately to an index
					 * 
					 *    - update the count here
					 */
					List<String> stems = TextStemmer.uniqueStems(item);
					for (int i = 0; i < stems.size(); i++) {
						index.add(stems.get(i), item.toString(), i+1);
					}
				}
			}
			catch (NullPointerException e) {
				System.err.println("Please enter a valid path argument.");
			}
			catch (IOException e) {
				System.err.println("Unable to traverse and stem the given file(s).");
			}
		}
		
		if (parser.hasFlag("-index")) {
			Path indexPath = parser.getPath("-index", Path.of("index.json"));
			
			try {
				// Writes index in pretty Json format to file.
				index.writeIndex(indexPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the inverted index to a JSON file at: \n" + indexPath);
			}
		}
		
		if (parser.hasFlag("-counts")) {
			Path countsPath = parser.getPath("-counts", Path.of("counts.json"));
			
			try {
				/*
				 * TODO totally makes sense for project 1
				 * 
				 * For project 2 you always need the word count to calculate search results
				 * 
				 * Store the counts map in your inverted index class (will need it there for project 2)
				 * 
				 * index.writeCounts(countsPath);
				 */
				Map<String, Integer> counts = TextParser.countPathWords(files);
				JsonWriter.asObject(counts, countsPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the word counts to a JSON file at: \n" + countsPath);
			}
		}
	}
}
