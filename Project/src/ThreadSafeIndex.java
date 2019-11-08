import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ThreadSafeIndex  extends InvertedIndex {

	private final SimpleReadWriteLock lock;

	/**
	 * Initializes a thread-safe InvertedIndex.
	 */
	public ThreadSafeIndex() {
		super();
		lock = new SimpleReadWriteLock();
	}

	/**
	 * Adds elements to map if the given element does not already exist for the corresponding
	 * key(s). If a new word or path is being entered, also initializes it's associated value.
	 * Also updates the word count for a given path if position is not a duplicate.
	 *
	 * @param word the word to add if it's not already in the map
	 * @param path the path to add if it's not already mapped to the corresponding word
	 * @param position the position to add if it's not already mapped to the corresponding word and path
	 */
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

	/**
	 * Writes index in pretty Json format.
	 *
	 * @param path the path to write the index to
	 * @throws IOException if unable to access path
	 */
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

	/**
	 * Writes word counts in pretty Json format.
	 *
	 * @param path the path to write counts to
	 * @throws IOException if unable to access path
	 */
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

	/**
	 * Returns an unmodifiable set view of the keys contained in countsMap.
	 *
	 * @return unmodifiable set of Strings
	 */
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

	/**
	 * Returns the count for a given path in countsMap.
	 *
	 * @param path the path to get count from
	 * @return value mapped to path in countsMap
	 */
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

	/**
	 * Returns an unmodifiable set view of the keys (words) in the map.
	 *
	 * @return unmodifiable view of set of Strings
	 */
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

	/**
	 * Returns an unmodifiable set view of the locations mapped to the given word.
	 *
	 * @param word the word the locations are mapped to
	 * @return unmodifiable view of set of Strings
	 */
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

	/**
	 * Returns an unmodifiable view of the inner set mapped to a given word and location in the map.
	 *
	 * @param word the word the set is mapped to
	 * @param location the location the set is mapped to
	 * @return unmodifiable view of set of Integers
	 */
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

	/**
	 * Returns true if countsMap contains the given path.
	 *
	 * @param path the path to verify is in the map
	 * @return true if the path is in the map as key
	 */
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

	/**
	 * Returns true if the map contains the given word as key.
	 *
	 * @param word the word to verify is in the map
	 * @return true if the word is in the map
	 */
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

	/**
	 * Returns true if the map contains the given location mapped to the given word.
	 *
	 * @param word the word that location is mapped to
	 * @param location the location to verify is in the map
	 * @return true if the location is in the map
	 */
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

	/**
	 * Returns true if the map contains the given position mapped to the given word and location.
	 *
	 * @param word the word that position is mapped to
	 * @param location the location that position is mapped to
	 * @param position the position to verify is in the map
	 * @return true if the position is in the map
	 */
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

		public ThreadSafeSearchResult(String where) {
			super(where);
		}

		/**
		 * Compares two SearchResult objects based on score, then count, then location.
		 * @param other the other object to compare to
		 * @return a negative int, zero, or a positive int as the first object is less than, equal to, or greater than the second
		 */
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

		/**
		 * Returns the location of a search result.
		 *
		 * @return location of search result
		 */
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

		/**
		 * Returns word count of a search result.
		 *
		 * @return word count of search result
		 */
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

		/**
		 * Return score of a search result.
		 *
		 * @return score of search result
		 */
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

	/**
	 * Calls partial or exact search depending on input.
	 *
	 * @param queries the queries to search for
	 * @param exactSearch the boolean to decide whether to perform partial or exact search
	 * @return a list of search results
	 */
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