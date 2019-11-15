import java.io.IOException;
import java.nio.file.Path;

/**
 * A multi-threaded version of {@link SearchBuilder} using a read/write lock.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class MultithreadedSearchBuilder extends SearchBuilder {

	/** The work queue used by this class. */
	private WorkQueue queue;

	/**
	 * Initializes the ThreadSafeIndex and work queue.
	 *
	 * @param index the index to initialize
	 * @param queue the queue to initialize
	 */
	public MultithreadedSearchBuilder(ThreadSafeIndex index, WorkQueue queue) {
		super(index);
		this.queue = queue;
	}

	@Override
	public void buildSearch (Path queryPath, boolean exact) throws IOException {
		super.buildSearch(queryPath, exact);
		queue.finish();
	}

	@Override
	public void searchLine(String line, boolean exact) {
		queue.execute(new Task(line, exact));

	}

	@Override
	public void writeQuery(Path path) throws IOException {
		super.writeQuery(path);
	}

	/**
	 * Static nested class that implements Runnable and runs tasks.
	 */
	private class Task implements Runnable {

		/** The line of queries search. */
		private final String line;

		/** The type of search to perform. */
		private final boolean exact;

		/**
		 * Initializes the members of this class.
		 *
		 * @param line the line to initialize
		 * @param exact the exact or partial search to initialize
		 */
		public Task(String line, boolean exact) {
			this.line = line;
			this.exact = exact;
		}

		@Override
		public void run() {
			searchLine(line, exact);
		}
	}
}