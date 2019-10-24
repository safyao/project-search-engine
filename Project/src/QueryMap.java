import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/*
 * TODO Go ahead and integrate this into SearchBuilder. It helps to have slightly
 * fewer classes to worry about before multithreading, and this doesn't add that 
 * much new functionality to just using queryMap directly. 
 */

/**
 * Nested data structure class that stores queries and their corresponding search results in a map..
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class QueryMap {

	/** Stores arguments in key = value pairs. **/
	private final Map<String, List<SearchResult>> queryMap;

	/**
	 * Initializes the argument map.
	 */
	public QueryMap() {
		queryMap = new TreeMap<>();
	}

	/**
	 * Adds element to map if given element is not already in the map.
	 *
	 * @param query the query to enter as key
	 * @param searchResults the list of SearchResult objects to enter as value
	 */
	public void add(String query, List<SearchResult> searchResults) {
		queryMap.putIfAbsent(query, searchResults);
	}

	/**
	 * Returns an unmodifiable view of list of search results for a given query.
	 *
	 * @param query the query to return the value of
	 * @return unmodifiable view of list of search results
	 */
	public List<SearchResult> getSearch(String query) {
		return Collections.unmodifiableList(queryMap.get(query));
	}

	/**
	 * Returns an unmodifiable view of set of queries in map.
	 *
	 * @return unmodifiable view of set of queries
	 */
	public Set<String> getQueries() {
		return Collections.unmodifiableSet(queryMap.keySet());
	}

	/**
	 * Writes map as pretty JSON object to file.
	 *
	 * @param path the path to write map to
	 * @throws IOException if unable to access file
	 */
	public void writeQuery(Path path) throws IOException {
		JsonWriter.asQueryObject(queryMap, path);
	}
}
