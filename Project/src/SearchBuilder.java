/*
 * TODO Search should be functionality WITHIN the inverted index class. (That is why
 * we create an inverted index... to search. It is considered core functionality,
 * but we broke it up into two projects to make it easier to think about.)
 *
 * Move the exact and partial search methods into the inverted index class. Then,
 * move the private final Map<String, List<SearchResult>> queryMap; from QueryMap
 * into this class.
 */

/**
 * Utility class that stores search result data into QueryResults data structure.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchBuilder {

//	private final Map<String, List<InvertedIndex.SearchResult>> queryMap;
//
//	public SearchBuilder() {
//		queryMap = new TreeMap<>();
//	}
//
//	public void buildSearch(Path queryPath, InvertedIndex index, boolean exact) throws IOException {
//		buildSearch(queryMap, queryPath, index, exact);
//	}

//	/**
//	 * Builds a sorted list of search results from a query file and stores results in QueryResults map.
//	 *
//	 * @param queryPath the path to parse for queries
//	 * @param index the index to search
//	 * @param exact the boolean to perform an exact search
//	 * @throws IOException in unable to access file
//	 */
//	public static void buildSearch(Map<String, List<InvertedIndex.SearchResult>> queryMap, Path queryPath, InvertedIndex index, boolean exact) throws IOException {
//
//		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
//
//		// Reads query file line by line.
//		try (
//				BufferedReader reader = Files.newBufferedReader(queryPath, StandardCharsets.UTF_8);
//			) {
//			String line = null;
//
//			while ((line = reader.readLine()) != null) {
//
//				// Stems line of queries.
//				Set<String> querySet = TextStemmer.uniqueStems(line, stemmer);
//
//				// Completes an exact or partial search based on given arguments.
//				if (exact) {
//					index.exactSearch(querySet);
//				}
//				else {
//					index.partialSearch(querySet, index);
//				}
//			}
//		}
//	}
//
//	/**
//	 * Searches list of locations for appearances of query word and stores total word count for each location.
//	 * Also stores each search result in list and updates the score if more than one query word appears in a single location.
//	 *
//	 * @param index the index to search
//	 * @param results the list of search results for a single query line
//	 * @param paths the paths (or locations) that have already been searched
//	 * @param locations the list of locations a query word appears in
//	 * @param word the query word to find
//	 */
//	public static void searchLocations(InvertedIndex index, List<InvertedIndex.SearchResult> results, List<String> paths, Set<String> locations, String word) {
//
//		for (String location : locations) {
//			double totalCount = index.getCount(location);
//
//			if (!paths.contains(location)) {
//				paths.add(location);
//				addResult(index, results, word, location, totalCount);
//			}
//			else {
//
//				for (InvertedIndex.SearchResult item : results) {
//					if (item.getWhere() == location) {
//						updateScore(index, item, word, location, totalCount);
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * Adds a search result for a single query word to list.
//	 *
//	 * @param index the index to search
//	 * @param results the list of search results for a single query line
//	 * @param word the query word found
//	 * @param location the location the word was found in
//	 * @param totalCount the total word count of the location
//	 */
//	public static void addResult(InvertedIndex index, List<InvertedIndex.SearchResult> results, String word, String location, double totalCount) {
//		//Calculates score for query word.
//		int count = index.getPositions(word, location).size();
//		double countCopy = count;
//		double score = countCopy/totalCount;
//
//		//Stores result in list.
//
//		InvertedIndex.SearchResult result = index.new SearchResult(location, count, score);
//		results.add(result);
//	}
//
//	/**
//	 * Updates word count and score for a line of queries.
//	 *
//	 * @param index the index to search
//	 * @param previous the previous SearchResult data for the line of queries
//	 * @param word the word in a line of queries
//	 * @param location the location the word was found in
//	 * @param totalCount the total word count of the location
//	 */
//	public static void updateScore(InvertedIndex index, InvertedIndex.SearchResult previous, String word, String location, double totalCount) {
//		int newCount = previous.getCount() + index.getPositions(word, location).size();
//		previous.setCount(newCount);
//
//		double newScore = newCount/totalCount;
//		previous.setScore(newScore);
//
//		/*
//		 * TODO After moving the SearchResult class into your index, make this something
//		 * built into the SearchResult class itself. It will have direct access to the
//		 * index and the word counts!
//		 */
//	}
//
//	public static void addQuery(Set<String> query, List<InvertedIndex.SearchResult> results)
//	{
//		Collections.sort(results);
//		if (!query.isEmpty()){
//			queryMap.putIfAbsent(String.join(" ", query), results);
//		}
//	}
//
//	public static Map<String, List<InvertedIndex.SearchResult>> getQueryMap() {
//		return Collections.unmodifiableMap(queryMap);
//	}
}
