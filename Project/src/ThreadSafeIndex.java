import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * A thread-safe version of {@link InvertedIndex} using a read/write lock.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class ThreadSafeIndex  extends InvertedIndex {

	/** The lock used to protect concurrent access to the underlying set. */
	private final SimpleReadWriteLock lock;

	/**
	 * Initializes a thread-safe InvertedIndex.
	 */
	public ThreadSafeIndex() {
		super();
		lock = new SimpleReadWriteLock();
	}

	@Override
	public void add(String word, String path, Integer position) {
		lock.writeLock().lock();

		try {
			super.add(word, path, position);
		}
		finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void writeIndex(Path path) throws IOException {
		lock.readLock().lock();

		try {
			super.writeIndex(path);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void writeCounts(Path path) throws IOException {
		lock.readLock().lock();

		try {
			super.writeCounts(path);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<String> getPaths() {
		lock.readLock().lock();

		try {
			return super.getPaths();
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Integer getCount(String path) {
		lock.readLock().lock();

		try {
			return super.getCount(path);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<String> getWords() {
		lock.readLock().lock();

		try {
			return super.getWords();
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<String> getLocations(String word) {
		lock.readLock().lock();

		try {
			return super.getLocations(word);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<Integer> getPositions(String word, String location) {
		lock.readLock().lock();

		try {
			return super.getPositions(word, location);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsCount(String path) {
		lock.readLock().lock();

		try {
			return super.containsCount(path);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String word) {
		lock.readLock().lock();

		try {
			return super.contains(word);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String word, String location) {
		lock.readLock().lock();

		try {
			return super.contains(word, location);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String word, String location, Integer position) {
		lock.readLock().lock();

		try {
			return super.contains(word, location, position);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public String toString() {
		lock.readLock().lock();

		try {
			return super.toString();
		}
		finally {
			lock.readLock().unlock();
		}
	}


	/**
	 * Nested data structure class that houses a single search result and implements Comparable.
	 */
	public class ThreadSafeSearchResult extends SearchResult implements Comparable<SearchResult> {

		/**
		 * Initializes a thread-safe SearchResult.
		 *
		 * @param where where the search result was found
		 */
		public ThreadSafeSearchResult(String where) {
			super(where);
		}

		@Override
		public int compareTo(SearchResult other) {
			lock.readLock().lock();

			try {
				return super.compareTo(other);
			}
			finally {
				lock.readLock().unlock();
			}
		}

		@Override
		public String getWhere() {
			lock.readLock().lock();

			try {
				return super.getWhere();
			}
			finally {
				lock.readLock().unlock();
			}
		}

		@Override
		public int getCount() {
			lock.readLock().lock();

			try {
				return super.getCount();
			}
			finally {
				lock.readLock().unlock();
			}
		}

		@Override
		public double getScore() {
			lock.readLock().lock();

			try {
				return super.getScore();
			}
			finally {
				lock.readLock().unlock();
			}
		}
	}

	@Override
	public List<SearchResult> search(Set<String> queries, boolean exactSearch) {
		lock.readLock().lock();

		try {
			return super.search(queries, exactSearch);
		}
		finally {
			lock.readLock().unlock();
		}
	}
}