import java.io.IOException;
import java.nio.file.Path;
//import java.time.Duration;
//import java.time.Instant;
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
	//Extras: Unmodifiable in InvertedIndex
	//Clean Up JsonWriter methods
	
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
		List<Path> files = new ArrayList<>();
		
		if (parser.hasFlag("-path")) {
			try {	
				Path path = parser.getPath("-path");
				
				// Store files in an ArrayList<Path>.
				DirectoryTraverser traverse = new DirectoryTraverser();
				files = traverse.traverseDirectory(path);
				
				// Store elements into InvertedIndex.
				for (Path item : files) {
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
				JsonWriter.asDoubleObject(index.getIndex(), indexPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the word counts to a JSON file at: \n" + indexPath);
			}
		}
		
		if (parser.hasFlag("-counts")) {
			Path countsPath = parser.getPath("-counts", Path.of("counts.json"));
			
			try {
				Map<String, Integer> counts = TextParser.countPathWords(files);
				JsonWriter.asObject(counts, countsPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the word counts to a JSON file at: \n" + countsPath);
			}
		}
	}
}
