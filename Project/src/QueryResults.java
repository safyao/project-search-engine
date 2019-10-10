import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class QueryResults {
	private Map<String, List<SearchResult>> queryResults;

	public QueryResults()
	{
		queryResults = new TreeMap<>();
	}

	public void add(String query, List<SearchResult> searchResults)
	{
		queryResults.putIfAbsent(query, searchResults);
	}

	public List<SearchResult> getSearch(String query)
	{
		return queryResults.get(query);
	}

	public Set<String> getQueries()
	{
		return queryResults.keySet();
	}

	public void writeQuery(Path path) throws IOException
	{
		JsonWriter.asQueryObject(queryResults, path);
	}

//	public Map<List<String>, List<SearchResult>> sorted()
//	{
//		TreeMap<List<String>, List<SearchResult>> sortedResults = new TreeMap<>();
//		sortedResults.putAll(queryResults);
//		queryResults = sortedResults;
//		return queryResults;
//	}
}
