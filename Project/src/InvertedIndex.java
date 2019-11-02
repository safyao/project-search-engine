import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Nested data structure class that houses Strings, TreeMaps, TreeSets, and Integers in one Map
 * and Strings and Integers in another.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class InvertedIndex {

	/** Stores arguments in key = value pairs regarding index data. **/
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> map;

	/** Stores arguments in key = value pairs regarding file word counts. **/
	private final TreeMap<String, Integer> countsMap;

	/**
	 * Initializes the argument maps.
	 */
	public InvertedIndex() {
		map = new TreeMap<>();
		countsMap = new TreeMap<>();
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
	public void add(String word, String path, Integer position) {
		map.putIfAbsent(word, new TreeMap<String, TreeSet<Integer>>());
		map.get(word).putIfAbsent(path, new TreeSet<Integer>());

		if (map.get(word).get(path).add(position)) {
			int count = 1 + countsMap.getOrDefault(path, 0);
			countsMap.put(path, count);
		}
	}

	/**
	 * Writes index in pretty Json format.
	 *
	 * @param path the path to write the index to
	 * @throws IOException if unable to access path
	 */
	public void writeIndex(Path path) throws IOException {
		JsonWriter.asDoubleObject(map, path);
	}

	/**
	 * Writes word counts in pretty Json format.
	 *
	 * @param path the path to write counts to
	 * @throws IOException if unable to access path
	 */
	public void writeCounts(Path path) throws IOException {
		JsonWriter.asObject(countsMap, path);
	}

	/**
	 * Returns and unmodifiable set view of the keys contained in countsMap.
	 *
	 * @return unmodifiable set of Strings
	 */
	public Set<String> getPaths() {
		return Collections.unmodifiableSet(countsMap.keySet());
	}

	/**
	 * Returns the count for a given path in countsMap.
	 *
	 * @param path the path to get count from
	 * @return value mapped to path in countsMap
	 */
	public Integer getCount(String path) {
		return countsMap.get(path);
	}

	/**
	 * Returns an unmodifiable set view of the keys (words) in the map.
	 *
	 * @return unmodifiable view of set of Strings
	 */
	public Set<String> getWords() {
		return Collections.unmodifiableSet(map.keySet());
	}

	/**
	 * Returns an unmodifiable set view of the locations mapped to the given word.
	 *
	 * @param word the word the locations are mapped to
	 * @return unmodifiable view of set of Strings
	 */
	public Set<String> getLocations(String word) {
		if (map.containsKey(word)) {
			return Collections.unmodifiableSet(map.get(word).keySet());
		}
		else {
			return Collections.emptySet();
		}
	}

	/**
	 * Returns an unmodifiable view of the inner set mapped to a given word and location in the map.
	 *
	 * @param word the word the set is mapped to
	 * @param location the location the set is mapped to
	 * @return unmodifiable view of set of Integers
	 */
	public Set<Integer> getPositions(String word, String location) {
		if (map.containsKey(word) && map.get(word).containsKey(location)) {
			return Collections.unmodifiableSet(map.get(word).get(location));
		}
		else {
			return Collections.emptySet();
		}
	}

	/**
	 * Returns true if countsMap contains the given path.
	 *
	 * @param path the path to verify is in the map
	 * @return true if the path is in the map as key
	 */
	public boolean containsCount(String path) {
		return countsMap.containsKey(path);
	}

	/**
	 * Returns true if the map contains the given word as key.
	 *
	 * @param word the word to verify is in the map
	 * @return true if the word is in the map
	 */
	public boolean contains(String word) {
		return map.containsKey(word);
	}

	/**
	 * Returns true if the map contains the given location mapped to the given word.
	 *
	 * @param word the word that location is mapped to
	 * @param location the location to verify is in the map
	 * @return true if the location is in the map
	 */
	public boolean contains(String word, String location) {
		return (map.containsKey(word) && map.get(word).containsKey(location));
	}

	/**
	 * Returns true if the map contains the given position mapped to the given word and location.
	 *
	 * @param word the word that position is mapped to
	 * @param location the location that position is mapped to
	 * @param position the position to verify is in the map
	 * @return true if the position is in the map
	 */
	public boolean contains(String word, String location, Integer position) {
		return (map.containsKey(word) && map.get(word).containsKey(location) && map.get(word).get(location).contains(position));
	}

	@Override
	public String toString() {
		return map.toString();
	}


	/**
	 * Nested data structure class that houses a single search result and implements Comparable.
	 */
	public class SearchResult implements Comparable<SearchResult> {

		/** Stores location of search result. */
		private final String where;

		/** Stores word count of search result for given location. */
		private int count;

		/** Stores score of search result. */
		private double score;

		/**
		 * Initializes the search result.
		 *
		 * @param where where the search result was found
		 */
		// public SearchResult(String where) {
		public SearchResult(String where) {
			this.where = where;
			this.count = 0;
			this.score = 0;
		}

		/**
		 * Compares two SearchResult objects based on score, then count, then location.
		 * @param other the other object to compare to
		 * @return a negative int, zero, or a positive int as the first object is less than, equal to, or greater than the second
		 */
		@Override
		public int compareTo(SearchResult other) {
			if (Double.compare(other.getScore(), this.score) != 0) {
				return (Double.compare(other.getScore(), this.score));
			}
			else if (Integer.compare(other.getCount(), this.count) != 0) {
				return (Integer.compare(other.getCount(), this.count));
			}
			return (this.where.compareTo(other.getWhere()));
		}

		/**
		 * Returns the location of a search result.
		 *
		 * @return location of search result
		 */
		public String getWhere() {
			return where;
		}

		/**
		 * Returns word count of a search result.
		 *
		 * @return word count of search result
		 */
		public int getCount() {
			return count;
		}

		/**
		 * Return score of a search result.
		 *
		 * @return score of search result
		 */
		public double getScore() {
			return score;
		}

		/**
		 * Updates word count and score of a search result.
		 *
		 * @param word the amount to increment count by
		 */
		private void update(String word) {
			int amount = getPositions(word, where).size();
			count += amount;
			double totalCount = InvertedIndex.this.getCount(where);
			score = count / totalCount;
		}
	}

	/**
	 * Searches an index for exact word matches of a given list of queries.
	 *
	 * @param queries the list of queries to find in index
	 */
	public List<SearchResult> exactSearch(Set<String> queries) {

		List<SearchResult> results = new ArrayList<>();
		Map<String, SearchResult> lookup = new HashMap<>();

		// Finds all locations a query word appears in and performs an exact search through them.
		for (String query : queries) {
			if (contains(query))
			{
				Set<String> locations = getLocations(query);
				for (String location : locations)
				{
					if (lookup.containsKey(location))
					{
						lookup.get(location).update(query);
					}
					else
					{
						SearchResult result = new SearchResult(location);
						results.add(result);
						lookup.put(location, result);
						lookup.get(location).update(query);
					}
				}
			}
		}

		// Sorts the search results for the list of queries and returns them.
		Collections.sort(results);

		return results;
	}

	/**
	 * Searches an index for partial word matches of a given list of queries.
	 *
	 * @param queries the list of queries to find in index
	 */
	public List<SearchResult> partialSearch(Set<String> queries) {

		List<SearchResult> results = new ArrayList<>();

		Map<String, SearchResult> lookup = new HashMap<>();

		// Finds all locations a query word partially appears in and performs a partial search through them.
		for (String query : queries) {
			TreeSet<String> set = new TreeSet<>();
			set.addAll(map.keySet());
			// Searches through set of keys in index for words that start with the query.


			for (String word : set.tailSet(query)) {
				if (!word.startsWith(query)) {
					break;
				}
				else {
					Set<String> locations = getLocations(word);
					for (String location : locations)
					{
						if (lookup.containsKey(location))
						{
							lookup.get(location).update(word);
						}
						else
						{
							SearchResult result = new SearchResult(location);
							results.add(result);
							lookup.put(location, result);
							lookup.get(location).update(word);
						}
					}
				}
			}
		}

		// Sorts the search results and returns them.
		Collections.sort(results);

		return results;
	}
}