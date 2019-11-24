import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A multi-threaded version of {@link SearchBuilder} using a read/write lock.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class MultithreadedSearchBuilder implements SearchBuilderInterface {

	/** The work queue used by this class. */
	private final WorkQueue queue;

	/** Creates an instance of InvertedIndex. */
	private final InvertedIndex index;

	/** Stores arguments in key = value pairs regarding query search results. **/
	private final Map<String, List<InvertedIndex.SearchResult>> queryMap;

	/**
	 * Initializes the ThreadSafeIndex and work queue.
	 *
	 * @param index the index to initialize
	 * @param queue the queue to initialize
	 */
	public MultithreadedSearchBuilder(ThreadSafeIndex index, WorkQueue queue) {
		this.index = index;
		this.queue = queue;
		queryMap = new TreeMap<>();
	}

	@Override
	public void buildSearch (Path queryPath, boolean exact) throws IOException {
		SearchBuilderInterface.super.buildSearch(queryPath, exact);
		queue.finish();
	}

	@Override
	public void searchLine(String line, boolean exact) {
		Task task = new Task(line, exact, this);
		queue.execute(task);
	}

	@Override
	public void writeQuery(Path path) throws IOException {
		JsonWriter.asQueryObject(queryMap, path);
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
			//Stems line of queries
			Set<String> querySet = TextStemmer.uniqueStems(line);
			if (querySet.isEmpty() ) {
				return;
			}
			String joined = String.join(" ", querySet);

			synchronized (builder) {
				if (queryMap.containsKey(joined)) {
					return;
				}
				List<InvertedIndex.SearchResult> results = index.search(querySet, exact);
				queryMap.put(joined, results);
			}
			
			/* TODO Try this instead, so the search is OUTSIDE of any
			 * synchronized blocks of code.
			synchronized (builder) {
				if (queryMap.containsKey(joined)) {
					return;
				}
			}
			
			List<InvertedIndex.SearchResult> results = index.search(querySet, exact);
			
			synchronized (builder) {
				queryMap.put(joined, results);
			}
			*/
		}
	}
}