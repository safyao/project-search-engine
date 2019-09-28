import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class Driver {

	/*
	 * TODO One style of programming is to declare a bunch of variables at the start.
	 * Here for memory and style reasons...
	 * 
	 * Declare and define where you intend to use the object.
	 * 
	 * ArgumentParser parser = new ArgrumentParser(args);
	 * InvertedIndex index = new InvertedIndex();
	 * 
	 * if (parser.hasFlag("-path")) {
	 * 
	 * }
	 * 
	 * if (parser.hasFlag("-index")) {
	 * 
	 * }
	 * 
	 * if (parser.hasFlag("-counts")) {
	 * 		Path path = parser.getPath("-counts", Path.of("counts.json"));
	 * 
	 * 		try {
	 * 			output the counts to json
	 * 		}
	 * 		catch (IOException e) {
	 * 			System.out.println("Unable to write the word counts to a JSON file at: " + path);
	 * 		}
	 * }
	 */
	
	/*
	 * TODO Keep Driver small, move some of the directory traversing and word count code outside of Driver.
	 * Driver is the only programmer specific class that will not be shared with other developers.
	 * So all "useful" code needs to be outside of Driver to be reused.
	 */
	
	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {
		// Store initial start time.
		Instant start = Instant.now();

		System.out.println(Arrays.toString(args)); // TODO Remove
		
		// Parse and store flag arguments.
		ArgParser parsedArgs = new ArgParser(args); // TODO Refactor to ArgumentParser... ArgumentMap
		Path path = parsedArgs.getPath("-path");
		Path indexPath = parsedArgs.getPath("-index", Path.of("index.json"));
		Path countsPath = parsedArgs.getPath("-counts", Path.of("counts.json"));
		List<Path> file = new ArrayList<>();
		
		try
		{
			// Store files in an ArrayList<Path>.
			DirectoryTraverser traverse = new DirectoryTraverser();
			
			if (Files.isDirectory(path)){
				file = traverse.traverseDirectory(path);
			}
			else{
				file.add(path);
			}
			
			// Initialize and store elements into InvertedIndex.
			InvertedIndex index = new InvertedIndex();
			for (Path item : file) {
				List<String> stems = TextStemmer.uniqueStems(item);
				for (int i = 0; i < stems.size(); i++) {
					index.add(stems.get(i), item.toString(), i+1);
				}
			}
	
			// If necessary, write index and count to files in pretty Json format.
			if (indexPath != null) {
				JsonWriter.asDoubleObject(index.getIndex(), indexPath);
			}
			if (countsPath != null) {
				Map<String, Integer> counts = new TreeMap<>();
				
				for (Path someFile : file) {
					int wordCount = TextParser.parseFile(someFile).size();
					if (wordCount != 0) {
						counts.put(someFile.toString(), wordCount);
					}
				}
				JsonWriter.asObject(counts, countsPath);
			}
		}
		catch (IOException e) {
			System.err.println("The file cannot be found.");
		}
		catch (NullPointerException e) {
			System.err.println("Please enter a valid path argument.");
		}
		finally {
			try {
				// Create an empty file if -index flag is provided.
				if (indexPath != null && path == null) {
					Files.createFile(indexPath);
				}
				
				// Calculate time elapsed and output.
				Duration elapsed = Duration.between(start, Instant.now());
				double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
				System.out.printf("Elapsed: %f seconds%n", seconds);
			}
			catch (IOException e)
			{
				System.err.println("The file cannot be found.");
			}
		}
	}
}
