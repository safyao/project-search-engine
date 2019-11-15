import java.io.IOException;
import java.nio.file.Path;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * A multi-threaded version of {@link IndexBuilder} using a read/write lock.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class MultithreadedIndexBuilder extends IndexBuilder {

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/** The work queue used by this class. */
	private final WorkQueue queue;

	/**
	 * Initializes the MultithreadedIndexBuilder.
	 *
	 * @param index the index to initialize
	 * @param queue the queue to initialize
	 */
	public MultithreadedIndexBuilder(ThreadSafeIndex index, WorkQueue queue) {
		super(index);
		this.queue = queue;
	}

	@Override
	public void buildIndex (Path path) throws IOException {
		super.buildIndex(path);
		queue.finish();
	}

	@Override
	public void addPath (Path path) throws IOException {
		queue.execute(new Task(path));

	}

	/**
	 * Nested class that implements Runnable and runs tasks.
	 */
	private class Task implements Runnable {

		/** The item to add to the index. */
		private final Path item;


		/**
		 * Initializes the item.
		 *
		 * @param item the item to initialize
		 */
		public Task(Path item) {
			this.item = item;
		}

		@Override
		public void run() {
			InvertedIndex local = new InvertedIndex();
			try {
				addPath(item, local);
				index.addAll(local);
			}
			catch (IOException e) {
				System.err.println("Unable to add file contents to InvertedIndex at: " + item);
			}
		}
	}
}
