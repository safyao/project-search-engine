import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Utility class that stores search result data into QueryResults data structure.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchBuilder {

	/**
	 * Builds a sorted list of search results from a query file and stores results in QueryResults map.
	 *
	 * @param queryMap the map to store all queries and search results in
	 * @param queryPath the path to parse for queries
	 * @param index the index to search
	 * @param exact the boolean to perform an exact search
	 * @throws IOException in unable to access file
	 */
	public static void buildSearch(QueryMap queryMap, Path queryPath, InvertedIndex index, boolean exact) throws IOException {

		// Reads query file line by line.
		try (
				BufferedReader reader = Files.newBufferedReader(queryPath, StandardCharsets.UTF_8);
			) {
			String line = null;

			while ((line = reader.readLine()) != null) {

				// Parses line of queries.
				List<String> queryList = TextStemmer.queryParser(line);

				// Completes an exact or partial search based on given arguments.
				if (exact) {
					SearchBuilder.exactSearch(queryMap, queryList, index);
				}
				else {
					SearchBuilder.partialSearch(queryMap, queryList, index);
				}
			}
		}
	}

	/**
	 * Searches an index for exact word matches of a given list of queries.
	 *
	 * @param queryResults the map to store all queries and search results in
	 * @param query the list of queries to find in index
	 * @param index the index to search
	 */
	public static void exactSearch(QueryMap queryResults, List<String> query, InvertedIndex index) {

		List<SearchResult> results = new ArrayList<>();
		List<String> paths = new ArrayList<>();

		// Finds all locations a query word appears in and performs an exact search through them.
		for (String word : query) {
			if (index.contains(word)) {
				Set<String> locations = index.getLocations(word);
				searchLocations(index, results, paths, locations, word);
			}
		}

		// Sorts the search results for the list of queries and adds it into QueryResults map.
		Collections.sort(results, SearchResult.SearchComparator);
		if (!query.isEmpty()){
			queryResults.add(queryAsString(query), results);
		}
	}

	/**
	 * Searches an index for partial word matches of a given list of queries.
	 *
	 * @param queryResults the map to store all queries and search results in
	 * @param query the list of queries to find in index
	 * @param index the index to search
	 */
	public static void partialSearch(QueryMap queryResults, List<String> query, InvertedIndex index) {

		List<SearchResult> results = new ArrayList<>();
		List<String> paths = new ArrayList<>();
		Set<String> wordKeys = index.getWords();

		// Finds all locations a query word partially appears in and performs a partial search through them.
		for (String word : query) {

			// Searches through set of keys in index for words that start with the query.
			for (String wordKey : wordKeys) {
				if (wordKey.startsWith(word)) {
					Set<String> locations = index.getLocations(wordKey);
					searchLocations(index, results, paths, locations, wordKey);
				}
			}
		}

		// Sorts the search results for the list of queries and adds it into QueryResults map.
		Collections.sort(results, SearchResult.SearchComparator);
		if (!query.isEmpty()) {
			queryResults.add(queryAsString(query), results);
		}
	}

	/**
	 * Searches list of locations for appearances of query word and stores total word count for each location.
	 * Also stores each search result in list and updates the score if more than one query word appears in a single location.
	 *
	 * @param index the index to search
	 * @param results the list of search results for a single query line
	 * @param paths the paths (or locations) that have already been searched
	 * @param locations the list of locations a query word appears in
	 * @param word the query word to find
	 */
	public static void searchLocations(InvertedIndex index, List<SearchResult> results, List<String> paths, Set<String> locations, String word) {

		for (String location : locations) {
			double totalCount = index.getCount(location);

			if (!paths.contains(location)) {
				paths.add(location);
				addResult(index, results, word, location, totalCount);
			}
			else {

				for (SearchResult item : results) {
					if (item.getWhere() == location) {
						updateScore(index, item, word, location, totalCount);
					}
				}
			}
		}
	}

	/**
	 * Adds a search result for a single query word to list.
	 *
	 * @param index the index to search
	 * @param results the list of search results for a single query line
	 * @param word the query word found
	 * @param location the location the word was found in
	 * @param totalCount the total word count of the location
	 */
	public static void addResult(InvertedIndex index, List<SearchResult> results, String word, String location, double totalCount) {
		//Calculates score for query word.
		Integer count = index.getPositions(word, location).size();
		double countCopy = count;
		String score = String.format("%.8f", countCopy/totalCount);
		//Stores result in list.
		SearchResult result = new SearchResult(location, count, score);
		results.add(result);
	}

	/**
	 * Updates word count and score for a line of queries.
	 *
	 * @param index the index to search
	 * @param previous the previous SearchResult data for the line of queries
	 * @param word the word in a line of queries
	 * @param location the location the word was found in
	 * @param totalCount the total word count of the location
	 */
	public static void updateScore(InvertedIndex index, SearchResult previous, String word, String location, double totalCount) {
		int newCount = previous.getCount() + index.getPositions(word, location).size();
		previous.setCount(newCount);

		String newScore = String.format("%.8f", newCount/totalCount);
		previous.setScore(newScore);
	}

	/**
	 * Writes a list of queries as a String.
	 *
	 * @param query the query line to write as String
	 * @return String of queries
	 */
	public static String queryAsString(List<String> query) {

		StringBuilder string = new StringBuilder();
		var iterator = query.iterator();

		if (iterator.hasNext()) {
			string.append(iterator.next());
		}

		while (iterator.hasNext()) {
			string.append(" ");
			string.append(iterator.next());
		}

		return string.toString();
	}
}