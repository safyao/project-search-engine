import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryResults {
	private final Map<List<String>, List<SearchResult>> queryResults;

	public QueryResults()
	{
		queryResults = new HashMap<>();
	}

	public void add(List<String> query, List<SearchResult> searchResults)
	{
		queryResults.putIfAbsent(query, searchResults);
	}

	public List<SearchResult> getSearch(List<String> query)
	{
		return queryResults.get(query);
	}

	public Set<List<String>> getQueries()
	{
		return queryResults.keySet();
	}

}
