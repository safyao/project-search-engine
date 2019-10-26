import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

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

	/** Stores arguments in key = value pairs regarding query search results. **/
	private final Map<String, List<InvertedIndex.SearchResult>> queryMap;

	/**
	 * Initializes the argument maps.
	 */
	public InvertedIndex() {
		map = new TreeMap<>();
		countsMap = new TreeMap<>();
		queryMap = new TreeMap<>();
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
	 * Data structure class that houses a single search result and implements Comparable.
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
		 * @param count the word count of the search result
		 * @param score the score of the search result
		 */
		public SearchResult(String where, int count, double score) {
			this.where = where;
			this.count = count;
			this.score = score;
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
		 * Updates word count of a search result.
		 *
		 * @param newCount the new count to store as count
		 */
		public void setCount(Integer newCount) {
			count = newCount;

			// TODO Anytime you see yourself get the current value, increase it, and then set that value, make an addCount(...) method instead!
		}

		/**
		 * Updates score of a search result.
		 *
		 * @param newScore the new score to store as score
		 */
		public void setScore(double newScore) {
			score = newScore;

			// TODO It is possible for score and count to get out of date with each other
		}
	}

	/**
	 * Builds a sorted list of search results from a query file and stores results in queryMap.
	 *
	 * @param queryPath the path to parse for queries
	 * @param exact the boolean to perform an exact search
	 * @throws IOException in unable to access file
	 */
	public void buildSearch(Path queryPath, boolean exact) throws IOException {

		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		// Reads query file line by line.
		try (
				BufferedReader reader = Files.newBufferedReader(queryPath, StandardCharsets.UTF_8);
			) {
			String line = null;

			while ((line = reader.readLine()) != null) {

				// Stems line of queries.
				Set<String> querySet = TextStemmer.uniqueStems(line, stemmer);

				// Completes an exact or partial search based on given arguments.
				if (exact) {
					exactSearch(querySet);
				}
				else {
					partialSearch(querySet);
				}
			}
		}
	}

	/**
	 * Searches an index for exact word matches of a given list of queries.
	 *
	 * @param query the list of queries to find in index
	 */
	public void exactSearch(Set<String> query) {

		List<SearchResult> results = new ArrayList<>();
		List<String> paths = new ArrayList<>();

		// Finds all locations a query word appears in and performs an exact search through them.
		for (String word : query) {
			if (contains(word)) {
				Set<String> locations = getLocations(word);
				searchLocations(results, paths, locations, word);
			}
		}

		// Sorts the search results for the list of queries and adds it into QueryMap.
		Collections.sort(results);
		if (!query.isEmpty()){
			queryMap.putIfAbsent(String.join(" ", query), results);
		}
	}

	/**
	 * Searches an index for partial word matches of a given list of queries.
	 *
	 * @param query the list of queries to find in index
	 */
	public void partialSearch(Set<String> query) {

		List<SearchResult> results = new ArrayList<>();
		List<String> paths = new ArrayList<>();
		Set<String> wordKeys = getWords();

		// Finds all locations a query word partially appears in and performs a partial search through them.
		for (String word : query) {

			// Searches through set of keys in index for words that start with the query.
			for (String wordKey : wordKeys) {
				if (wordKey.startsWith(word)) {
					Set<String> locations = getLocations(wordKey);
					searchLocations(results, paths, locations, wordKey);
				}
			}
		}

		// Sorts the search results for the list of queries and adds it into QueryMap.
		Collections.sort(results);
		if (!query.isEmpty()){
			queryMap.putIfAbsent(String.join(" ", query), results);
		}
	}

	/**
	 * Searches list of locations for appearances of query word and stores total word count for each location.
	 * Also stores each search result in list and updates the score if more than one query word appears in a single location.
	 *
	 * @param results the list of search results for a single query line
	 * @param paths the paths (or locations) that have already been searched
	 * @param locations the list of locations a query word appears in
	 * @param word the query word to find
	 */
	public void searchLocations(List<SearchResult> results, List<String> paths, Set<String> locations, String word) {

		for (String location : locations) {
			if (!paths.contains(location)) {
				paths.add(location);
				addResult(results, word, location);
			}
			else {

				for (SearchResult item : results) {
					if (item.getWhere() == location) {
						updateScore(item, word, location);
					}
				}
			}
		}
	}

	/**
	 * Adds a search result for a single query word to list.
	 *
	 * @param results the list of search results for a single query line
	 * @param word the query word found
	 * @param location the location the word was found in
	 */
	public void addResult(List<SearchResult> results, String word, String location) {
		//Calculates score for query word.
		int count = getPositions(word, location).size();
		double totalCount = getCount(location);
		double score = count/totalCount;

		//Stores result in list.
		SearchResult result = new SearchResult(location, count, score);
		results.add(result);
	}

	/**
	 * Updates word count and score for a line of queries.
	 *
	 * @param previous the previous SearchResult data for the line of queries
	 * @param word the word in a line of queries
	 * @param location the location the word was found in
	 */
	public void updateScore(SearchResult previous, String word, String location) {
		int newCount = previous.getCount() + getPositions(word, location).size();
		previous.setCount(newCount);

		double totalCount = getCount(location);
		double newScore = newCount/totalCount;
		previous.setScore(newScore);

		/*
		 * TODO After moving the SearchResult class into your index, make this something
		 * built into the SearchResult class itself. It will have direct access to the
		 * index and the word counts!
		 */
	}

	/**
	 * Writes query search results as pretty JSON object to file.
	 *
	 * @param path the path to write search results to
	 * @throws IOException if unable to access path
	 */
	public void writeQuery(Path path) throws IOException {
		JsonWriter.asQueryObject(queryMap, path);
	}
}