import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

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
		// store initial start time
		Instant start = Instant.now();

		// TODO Fill in and modify this method as necessary.
		System.out.println(Arrays.toString(args));
		
		
		//Take argument - includes file location, output file location, count location
		ArgParser parsedArgs = new ArgParser();
		
		//For path in parsedargs, get file locations
		//Taverse the directory for all the text files - gives an ArrayList of Paths
		DirectoryTraverser traverse = new DirectoryTraverser();
		//for each file in allFiles, parse and stem the words - returns TreeSet of Strings
		//Create InvertedIndex data structure (similar to ArgParser)
		//Enter keys into index, with the same file location, and incrementor for line number
		
		//Write the index into files as Json object
		//If index then output file:
		//parsedArgs.getPath("-index", Path.of("index.json").normalize());
		//Somehow figure out the word counts iff
		//parsedArgs.getPath("-counts", Path.of("counts.json").normalize());

		

		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}
