import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class that stores path data into Inverted Index data structure.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class MultithreadedSearchBuilder extends SearchBuilder{

	private WorkQueue queue;

	/**
	 * Initializes the InvertedIndex and queryMap.
	 *
	 * @param index the index to initialize
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
	 * Builds a sorted list of search results from a query file and stores results in queryMap.
	 *
	 * @param queryPath the path to parse for queries
	 * @param exact the boolean to perform an exact search
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

	/**
	 * Searches for a single line of queries and places result in queryMap.
	 *
	 * @param line the line to search for
	 * @param exact boolean on whether to perform exact or partial search
	 */
	@Override
	public void searchLine(String line, boolean exact) {
		//Stems line of queries
		super.searchLine(line, exact);

	}

	/**
	 * Writes query search results as pretty JSON object to file.
	 *
	 * @param path the path to write search results to
	 * @throws IOException if unable to access path
	 */
	@Override
	public void writeQuery(Path path) throws IOException {
		super.writeQuery(path);
	}

	private class Task implements Runnable {
		private final String line;
		private final boolean exact;
		private final MultithreadedSearchBuilder builder;

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