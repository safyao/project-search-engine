import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import java.util.List;
import java.util.ArrayList;
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

	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// store initial start time
		Instant start = Instant.now();

		System.out.println(Arrays.toString(args));

		ArgParser parsedArgs = new ArgParser(args);
		Path path = parsedArgs.getPath("-path");
		List<Path> file = new ArrayList<>();
		
		DirectoryTraverser traverse = new DirectoryTraverser();
		if (Files.isDirectory(path))
		{
			file = traverse.traverseDirectory(path);
		}
		else
		{
			file.add(path);
		}
		
		
		InvertedIndex index = new InvertedIndex();
		for (Path item : file)
		{
			List<String> stems = TextStemmer.uniqueStems(item);
			for (int i = 0; i < stems.size(); i++)
			{
				index.add(stems.get(i), item.toString(), i+1);
			}
		}

		if (parsedArgs.getPath("-index", Path.of("index.json")) != null)
		{
			JsonWriter.asDoubleObject(index.getIndex(), parsedArgs.getPath("-index", Path.of("index.json")));
		}
		if (parsedArgs.getPath("-counts", Path.of("counts.json")) != null)
		{
			Map<String, Integer> counts = new TreeMap<>();
			
			for (Path someFile : file)
			{
				int wordCount = TextParser.parseFile(someFile).size();
				if (wordCount != 0)
				{
					counts.put(someFile.toString(), wordCount);
				}
			}
			JsonWriter.asObject(counts, parsedArgs.getPath("-counts", Path.of("counts.json")));
		}
		
		
		
		
		//Exceptions
		//Clean Up
		//Javadoc
		
		
		
		
		
		
		
		
		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}
