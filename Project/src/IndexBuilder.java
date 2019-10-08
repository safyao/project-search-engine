import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;


/**
 * Utility class that stores path data into Inverted Index data structure.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class IndexBuilder {
	/* TODO Use the instance version in Driver.
	private final InvertedIndex index;

	public IndexBuilder(InvertedIndex index) {
		this.index = index;
	}

	public void buildIndex (Path path) throws IOException {
		buildIndex(path, this.index);
	}

	public void addPath (Path path) throws IOException {
		addPath(path, this.index);
	}
	*/



	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Traverses directory and adds each text file to index.
	 *
	 * @param path the path to traverse
	 * @param index the index to store data into
	 * @throws IOException if unable to access file
	 */
	public static void buildIndex (Path path, InvertedIndex index) throws IOException {
		List<Path> files = DirectoryTraverser.traverseDirectory(path);

		for (Path item : files) {
			addPath (item, index);
		}
	}

	/**
	 * Adds path element into Inverted Index data structure.
	 *
	 * @param path the path to gather data from for index
	 * @param index the index to store data into
	 * @throws IOException if unable to access path
	 */
	public static void addPath (Path path, InvertedIndex index) throws IOException {
		int wordCount = 0;
		int positionCount = 0;

		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		String location = path.toString();

		// Reads text file line by line.
		try (
				BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		) {
			String line = null;

			while ((line = reader.readLine()) != null) {

				// Parses line and updates word count of text file.
				String[] parsedLine = TextParser.parse(line);
				wordCount = wordCount + parsedLine.length;

				// Stems word and adds each word to index with associated position.
				for (String word : parsedLine)
				{
					String stemmedWord = stemmer.stem(word).toString();
					index.add(stemmedWord, path.toString(), ++positionCount);
				}
			}
			// Adds word count to index's word count map.
			index.addCount(location, Integer.valueOf(wordCount)); // TODO Remove
		}

	}
}
