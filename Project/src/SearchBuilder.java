import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Utility class that stores path data into Inverted Index data structure.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchBuilder {

	/** Creates an instance of InvertedIndex. */
	private final InvertedIndex index;

	/** Stores arguments in key = value pairs regarding query search results. **/
	private final Map<String, List<InvertedIndex.SearchResult>> queryMap;

	/**
	 * Initializes the InvertedIndex and queryMap.
	 *
	 * @param index the index to initialize
	 */
	public SearchBuilder(InvertedIndex index) {
		this.index = index;
		queryMap = new TreeMap<>();
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
				List<InvertedIndex.SearchResult> results;

				// Completes an exact or partial search based on given arguments.
				if (exact) {
					results = index.exactSearch(querySet);
				}
				else {
					results = index.partialSearch(querySet);
				}

				// Adds result to queryMap.
				if (!querySet.isEmpty()) {
					queryMap.putIfAbsent(String.join(" ", querySet), results);
				}
			}
		}
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