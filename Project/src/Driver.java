import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

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

		// TODO Fill in and modify this method as necessary.
		System.out.println(Arrays.toString(args));
		
		
		
		
		

		ArgParser parsedArgs = new ArgParser(args);
		Path path = parsedArgs.getPath("-path");
		System.out.println(path);
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
			TreeSet<String> stems = TextStemmer.uniqueStems(item);
			
			for (String word : stems)
			{	
				TreeMap<String, TreeSet<Integer>> wordOccurrences = new TreeMap<>();
				
				for (Path element : file)
				{
					TreeSet<Integer> positions = TextStemmer.uniqueStemPositions(element, word);
					if (!positions.isEmpty())
					{
						wordOccurrences.putIfAbsent(element.normalize().toString(), positions);
					}
				}
				
				index.addKey(word, wordOccurrences);
			}		
		}
		
		if (parsedArgs.getPath("-index") != null)
		{
			JsonWriter.asDoubleObject(index.getIndex(), Path.of("index.json").normalize());
		}
		if (parsedArgs.getPath("-counts") != null)
		{
			Map<String, Integer> counts = new TreeMap<>();
			
			for (Path someFile : file)
			{
				int wordCount = TextParser.parseFile(someFile).size();
				counts.put(someFile.toString(), wordCount);
			}
			
			JsonWriter.asObject(counts, Path.of("counts.json").normalize());
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
