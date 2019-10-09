import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SearchResults {


	public static List<SearchResult> search(List<String> query, InvertedIndex index)
	{
		List<SearchResult> results = new ArrayList<>();
		List<String> paths = new ArrayList<>();

		for (String word : query)
		{
			if (index.contains(word))
			{
				Set<String> locations = index.getLocations(word);

				for (String location : locations)
				{
					int totalCount = index.getCount(location);

					if (!paths.contains(location))
					{
						paths.add(location);
						int count = index.getPositions(word, location).size();
						String score = String.format("%.8f", (double)count/(double)totalCount);

						SearchResult item = new SearchResult(location, count, score);
						results.add(item);
					}
					else
					{
						for (SearchResult thing : results)
						{
							if (thing.getWhere() == location)
							{
								int newCount = thing.getCount() + index.getPositions(word, location).size();
								thing.setCount(newCount);

								String newScore = String.format("%.8f", (double)newCount/totalCount);
								thing.setScore(newScore);
							}
						}
					}
				}
			}
		}
		Collections.sort(results, SearchResult.SearchComparator);
		return results;
	}
}
