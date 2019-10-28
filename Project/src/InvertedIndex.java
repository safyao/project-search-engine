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

	/*
	 * TODO Move back to a SearchBuilder
	 */
	/** Stores arguments in key = value pairs regarding query search results. **/
	private final Map<String, List<InvertedIndex.SearchResult>> queryMap;

	
	/*
	 * TODO
	 * 
	 * public class SearchBuilder {
	 * 		private final InvertedIndex index;
	 * 		private final Map<String, List<InvertedIndex.SearchResult>> queryMap;
	 * 
	 * 		public SearchBuilder(InvertedIndex index) {
	 * 			this.index = index;
	 * 		}
	 * 
	 * 		
	 * }
	 */
	
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
		 * @param count the word count of the search result
		 * @param score the score of the search result
		 */
		// public SearchResult(String where) {
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

		/*
		 * TODO 
		 * Project 3: Make addCount private
		 * Also rename to update(...)
		 * Also make addCount(String word)... use the location stored
		 * in the search result
		 * int amount = getPositions(word, where).size();
		 */
		/**
		 * Updates word count and score of a search result.
		 *
		 * @param word the amount to increment count by
		 * @param location the location the search result was found
		 */
		public void addCount(String word, String location) {
			int amount = getPositions(word, location).size();
			count += amount;
			double totalCount = InvertedIndex.this.getCount(location);
			score = count / totalCount;
		}
	}


	/**
	 * Builds a sorted list of search results from a query file and stores results in queryMap.
	 *
	 * @param queryPath the path to parse for queries
	 * @param exact the boolean to perform an exact search
	 * @throws IOException in unable to access file
	 */
	public void buildSearch(Path queryPath, boolean exact) throws IOException { // TODO Move to SearchBuilder

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
					exactSearch(querySet); // TODO index.exactSearch(...)
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
	public void partialSearch(Set<String> query) { // TODO Set<String> queries

		List<SearchResult> results = new ArrayList<>(); // TODO Keep
		List<String> paths = new ArrayList<>(); // TODO Remove
		Set<String> wordKeys = getWords(); // TODO Remove
		
		// TODO Add Map<String (location), SearchResult> lookup = ...

		// Finds all locations a query word partially appears in and performs a partial search through them.
		for (String word : query) { // TODO for (String query : queries)

			// Searches through set of keys in index for words that start with the query.
			// TODO for (String word : map.keySet() <- kind of....) ...
			for (String wordKey : wordKeys) {
				if (wordKey.startsWith(word)) {
					/*
					 * TODO 
					 * for each location
					 *    if lookup has this location as a key
					 *        get that search result and call update on it
					 *        lookup.get(location).update(word) (or your addCount)
					 *    else 
					 *    		SearchResult result = new SearchResult(...)
					 *    		add this result to the list
					 *    		add this same result to the map
					 * 
					 */
					Set<String> locations = getLocations(wordKey);
					searchLocations(results, paths, locations, wordKey);
				}
			}
		}

		// Sorts the search results for the list of queries and adds it into QueryMap.
		Collections.sort(results);
		
		// TODO Return the search results
		if (!query.isEmpty()){
			queryMap.putIfAbsent(String.join(" ", query), results);
		}
		
		/*
		 * TODO Take advantage of tree structure to make search (partial) faster
		 * https://github.com/usf-cs212-fall2019/lectures/blob/master/Data%20Structures/src/FindDemo.java#L146-L163
		 */
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
			if (!paths.contains(location)) { // TODO Expensive contains (linear search)
				paths.add(location);
				addResult(results, word, location);
			}
			else {

				for (SearchResult item : results) { // TODO Expensive, linear search
					if (item.getWhere() == location) {
						item.addCount(word, location);
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
	public void addResult(List<SearchResult> results, String word, String location) { // TODO Remove method
		//Calculates score for query word.
		int count = getPositions(word, location).size();
		double totalCount = getCount(location);
		double score = count/totalCount;

		//Stores result in list.
		SearchResult result = new SearchResult(location, count, score);
		// TODO SearchResult result = new SearchResult(location); result.update(word);
		results.add(result);
	}

	/**
	 * Writes query search results as pretty JSON object to file.
	 *
	 * @param path the path to write search results to
	 * @throws IOException if unable to access path
	 */
	public void writeQuery(Path path) throws IOException { // TODO Move back to SearchBuilder
		JsonWriter.asQueryObject(queryMap, path);
	}
}