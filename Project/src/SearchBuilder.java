import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SearchBuilder {

	public static void buildSearch(QueryResults queryResults, Path queryPath, InvertedIndex index, boolean exact) throws IOException
	{
		try (
				BufferedReader reader = Files.newBufferedReader(queryPath, StandardCharsets.UTF_8);
			) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					List<String> queryList = TextStemmer.queryParser(line);
					if (exact)
					{
						List<SearchResult> results = SearchBuilder.exactSearch(queryResults, queryList, index);
					}
					else
					{
						List<SearchResult> results = SearchBuilder.partialSearch(queryResults, queryList, index);
					}
				}
//				queryResults.sorted();

			}
	}

	public static List<SearchResult> exactSearch(QueryResults queryResults, List<String> query, InvertedIndex index)
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
					double totalCount = index.getCount(location);

					if (!paths.contains(location))
					{
						paths.add(location);
						Integer count = index.getPositions(word, location).size();
						double countCopy = count;
						String score = String.format("%.8f", countCopy/totalCount);

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

								String newScore = String.format("%.8f", newCount/totalCount);
								thing.setScore(newScore);
							}
						}
					}
				}
			}
		}
		Collections.sort(results, SearchResult.SearchComparator);
		if (!query.isEmpty())
		{
			queryResults.add(queryAsString(query), results);
		}

		return results;
	}

	public static String queryAsString(List<String> query)
	{
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








	public static List<SearchResult> partialSearch(QueryResults queryResults, List<String> query, InvertedIndex index)
	{
		List<SearchResult> results = new ArrayList<>();
		List<String> paths = new ArrayList<>();
		Set<String> wordKeys = index.getWords();

		for (String word : query)
		{
			for (String wordKey : wordKeys)
			{
				if (wordKey.startsWith(word))
				{
					Set<String> locations = index.getLocations(wordKey);

					for (String location : locations)
					{
						double totalCount = index.getCount(location);

						if (!paths.contains(location))
						{
							paths.add(location);
							Integer count = index.getPositions(wordKey, location).size();
							double countCopy = count;
							String score = String.format("%.8f", countCopy/totalCount);

							SearchResult item = new SearchResult(location, count, score);
							results.add(item);
						}
						else
						{
							for (SearchResult thing : results)
							{
								if (thing.getWhere() == location)
								{
									int newCount = thing.getCount() + index.getPositions(wordKey, location).size();
									thing.setCount(newCount);

									String newScore = String.format("%.8f", newCount/totalCount);
									thing.setScore(newScore);
								}
							}
						}
					}
				}
			}
		}
		Collections.sort(results, SearchResult.SearchComparator);
		if (!query.isEmpty())
		{
			queryResults.add(queryAsString(query), results);
		}
		return results;
	}
}
