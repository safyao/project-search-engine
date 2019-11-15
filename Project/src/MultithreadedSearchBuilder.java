import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A multi-threaded version of {@link SearchBuilder} using a read/write lock.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class MultithreadedSearchBuilder extends SearchBuilder{

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
		buildSearch(queryPath, exact, queue);
	}

	/**
	 * Builds a sorted list of search results from a query file by assigning a task to each line of queries.
	 *
	 * @param queryPath the path to parse for queries
	 * @param exact the boolean to perform an exact search
	 * @param queue the queue to store work to be done
	 * @throws IOException in unable to access file
	 */
	public void buildSearch(Path queryPath, boolean exact, WorkQueue queue) throws IOException {
		// Reads query file line by line.
		try (
				BufferedReader reader = Files.newBufferedReader(queryPath, StandardCharsets.UTF_8);
			) {

			String line = null;

			while ((line = reader.readLine()) != null) {
				Task task = new Task(line, exact, this);
				queue.execute(task);
			}
			queue.finish();
		}
	}

	@Override
	public void searchLine(String line, boolean exact) {
		super.searchLine(line, exact);

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

		/** An instance of this class. */
		private final MultithreadedSearchBuilder builder;

		/**
		 * Initializes the members of this class.
		 *
		 * @param line the line to initialize
		 * @param exact the exact or partial search to initialize
		 * @param builder the search builder to initialize
		 */
		public Task(String line, boolean exact, MultithreadedSearchBuilder builder) {
			this.line = line;
			this.exact = exact;
			this.builder = builder;
		}

		@Override
		public void run() {
			synchronized (builder) {
				searchLine(line, exact);
			}
		}
	}
}