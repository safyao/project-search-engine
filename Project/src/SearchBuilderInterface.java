import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Utility interface that stores path data into Inverted Index data structure.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public interface SearchBuilderInterface {

	/**
	 * Builds a sorted list of search results from a query file and stores results in queryMap.
	 *
	 * @param queryPath the path to parse for queries
	 * @param exact the boolean to perform an exact search
	 * @throws IOException in unable to access file
	 */
	public default void buildSearch(Path queryPath, boolean exact) throws IOException {
		// Reads query file line by line.
		try (
				BufferedReader reader = Files.newBufferedReader(queryPath, StandardCharsets.UTF_8);
			) {
			String line = null;

			while ((line = reader.readLine()) != null) {
				searchLine(line, exact);
			}
		}
	}

	/**
	 * Searches for a single line of queries and places result in queryMap.
	 *
	 * @param line the line to search for
	 * @param exact boolean on whether to perform exact or partial search
	 */
	public abstract void searchLine(String line, boolean exact);

	/**
	 * Writes query search results as pretty JSON object to file.
	 *
	 * @param path the path to write search results to
	 * @throws IOException if unable to access path
	 */
	public abstract void writeQuery(Path path) throws IOException;

	/**
	 * Returns an unmodifiable view of queryMap.
	 *
	 * @return unmodifiable Map
	 */
	public abstract Map<String, List<InvertedIndex.SearchResult>> getResults();
}