import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchResults {

//	public static List<HashMap<String, Object>> search(List<String> query, InvertedIndex index)
//	{
//		List<HashMap<String, Object>> results = new ArrayList<>();
//
//		for (String word : query)
//		{
//			if (index.contains(word))
//			{
//				Set<String> locations = index.getLocations(word);
//				for (String location : locations)
//				{
//					boolean added = false;
//					for (HashMap<String, Object> item : results)
//					{
//						if (item.containsValue(location))
//						{
//							int count = (Integer)item.get("count") + index.getPositions(word, location).size();
//							item.put("count", count); //might need to put back into list...
//							added = true;
//						}
//					}
//					if (!added)
//					{
//						HashMap<String, Object> result = new HashMap<>();
//						result.put("where", location);
//
//						int count = index.getPositions(word, location).size();
//						result.put("count", count);
//
//					}
//					double score = index.getCount(location);
//					String formatted = String.format("%.8f", Math.PI);
//					results.get("score", formatted);
//
//
//				}
//			}
//		}
//		return results;
//	}


	public static List<SearchResult> search(List<String> query, InvertedIndex index)
	{
		List<SearchResult> results = new ArrayList<>();

		for (String word : query)
		{
			if (index.contains(word))
			{
				Set<String> locations = index.getLocations(word);
				for (String location : locations)
				{
					int count = index.getPositions(word, location).size();
					int totalCount = index.getCount(location);
					double score = count/totalCount;
					String formatted = String.format("%.8f", score);

					SearchResult item = new SearchResult(location, count, formatted);
					results.add(item);
				}
			}
		}
		return results;
	}
}
