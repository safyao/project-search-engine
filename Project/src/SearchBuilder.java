import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Utility class that stores path data into Inverted Index data structure.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchBuilder implements SearchBuilderInterface {

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

	@Override
	public void searchLine(String line, boolean exact) {
		//Stems line of queries
		Set<String> querySet = TextStemmer.uniqueStems(line);
		String joined = String.join(" ", querySet);

		if (!querySet.isEmpty() && !queryMap.containsKey(joined)) {
			List<InvertedIndex.SearchResult> results = index.search(querySet, exact);
 			queryMap.put(joined, results);
 		}
	}

	@Override
	public void writeQuery(Path path) throws IOException {
		JsonWriter.asQueryObject(queryMap, path);
	}
}