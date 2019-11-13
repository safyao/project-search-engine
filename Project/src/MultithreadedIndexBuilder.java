import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * A multi-threaded version of {@link IndexBuilder} using a read/write lock.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class MultithreadedIndexBuilder { // TODO extends IndexBuilder

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/** The work queue used by this class. */
	private final WorkQueue queue;

	/** An instance of the thread-safe InvertedIndex. */
	private final ThreadSafeIndex index;

	/**
	 * Initializes the MultithreadedIndexBuilder.
	 *
	 * @param index the index to initialize
	 * @param queue the queue to initialize
	 */
	public MultithreadedIndexBuilder(ThreadSafeIndex index, WorkQueue queue) {
		// TODO super(index);
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
		// TODO super.buildIndex(path);
		// TODO queue.finish();
	}

	/**
	 * Calls on addPath(...) using non-static reference.
	 *
	 * @param path the path to gather data from for index
	 * @throws IOException if unable to access file
	 */
	public void addPath (Path path) throws IOException {
		// TODO queue.execute(new Task(path));
		addPath(path, this.index);
	}

	// TODO Remove
	/**
	 * Adds path element into ThreadSafeIndex data structure.
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

	// TODO Remove
	/**
	 * Traverses directory and assigns each text file to a task that's added to the queue.
	 *
	 * @param path the path to traverse
	 * @param index the index to store data into
	 * @param queue the queue to store work to be done
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

	/**
	 * Static nested class that implements Runnable and runs tasks.
	 */
	private static class Task implements Runnable { // TODO Make a non-static class

		/** The item to add to the index. */
		private final Path item;

		/** The index to build. */
		private final ThreadSafeIndex index; // TODO Remove, access index directly


		/**
		 * Initializes the item and index.
		 *
		 * @param item the item to initialize
		 * @param index the index to initialize
		 */
		public Task(Path item, ThreadSafeIndex index) {
			this.item = item;
			this.index = index;
		}

		@Override
		public void run() {
			synchronized (index) { // TODO If your index is thread safe, do not need to synchronize
				try {
					addPath(item, index);
					
					/* TODO 
					InvertedIndex local = new InvertedIndex();
					addPath(item, local);
					index.addAll(local);
					*/
				} catch (IOException e) {
					System.err.println("Unable to add file contents to InvertedIndex at: " + item);
				}
			}
		}
	}
}
