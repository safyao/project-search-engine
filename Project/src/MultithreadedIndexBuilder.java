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
public class MultithreadedIndexBuilder {

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	private final WorkQueue queue;

	private final ThreadSafeIndex index;

	/**
	 * Initializes the InvertedIndex.
	 *
	 * @param index the index to initialize
	 */
	public MultithreadedIndexBuilder(ThreadSafeIndex index, WorkQueue queue) {
		this.index = index;
		this.queue = queue;
	}

	/**
	 * Calls on buildIndex(...) using non-static reference.
	 *
	 * @param path the path to traverse
	 * @throws IOException if unable to access file
	 */
	public void buildIndex (Path path) throws IOException {
		buildIndex(path, this.index, this.queue);
	}

	/**
	 * Calls on addPath(...) using non-static reference.
	 *
	 * @param path the path to gather data from for index
	 * @throws IOException if unable to access file
	 */
	public void addPath (Path path) throws IOException {
		addPath(path, this.index);
	}

	/**
	 * Adds path element into Inverted Index data structure.
	 *
	 * @param path the path to gather data from for index
	 * @param index the index to store data into
	 * @throws IOException if unable to access path
	 */
	public static void addPath (Path path, ThreadSafeIndex index) throws IOException {
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

				// Stems word and adds each word to index with associated position.
				for (String word : parsedLine) {
					String stemmedWord = stemmer.stem(word).toString();
					index.add(stemmedWord, location, ++positionCount);
				}
			}
		}
	}

	/**
	 * Traverses directory and adds each text file to index.
	 *
	 * @param path the path to traverse
	 * @param index the index to store data into
	 * @throws IOException if unable to access file
	 */
	public static void buildIndex (Path path, ThreadSafeIndex index, WorkQueue queue) throws IOException {
		List<Path> files = DirectoryTraverser.traverseDirectory(path);

		for (Path item : files) {
			Task task = new Task(item, index);
			queue.execute(task);
		}

		queue.finish();
	}

	private static class Task implements Runnable {
		private final Path item;
		private final ThreadSafeIndex index;

		public Task(Path item, ThreadSafeIndex index) {
			this.item = item;
			this.index = index;
		}

		@Override
		public void run() {
			synchronized (index) {
				try {
					addPath(item, index);
				} catch (IOException e) {
					System.err.println("Unable to add file contents to InvertedIndex at: " + item);
				}
			}
		}
	}
}
