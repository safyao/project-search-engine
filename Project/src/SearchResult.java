import java.util.Comparator;

/**
 * Data structure class that houses a single search result and implements Comparable.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchResult implements Comparable<SearchResult> {

	/** Stores location of search result. */
	private String where;

	/** Stores word count of search result for given location. */
	private Integer count;

	/** Stores score of search result. */
	private String score;

	/**
	 * Initializes the search result.
	 *
	 * @param where where the search result was found
	 * @param count the word count of the search result
	 * @param score the score of the search result
	 */
	public SearchResult(String where, int count, String score) {
		this.where = where;
		this.count = count;
		this.score = score;
	}

	@Override
	public int compareTo(SearchResult other) {
		if (this.score.compareTo(other.getScore()) != 0) {
			return (this.score.compareTo(other.getScore()));
		}
		else if (this.count.compareTo(other.getCount()) != 0) {
			return (this.count.compareTo(other.getCount()));
		}
		else {
			return (other.getWhere().compareTo(this.where));
		}
	}

	/**
	 * Anonymous inner class that compares two SearchResults.
	 */
	public static Comparator<SearchResult> SearchComparator = new Comparator<SearchResult>() {

		@Override
		public int compare(SearchResult one, SearchResult two) {
			return two.compareTo(one);
		}
	};

	/**
	 * Returns the location of a search result.
	 *
	 * @return location of search result
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * Returns word count of a search result.
	 *
	 * @return word count of search result
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * Return score of a search result.
	 *
	 * @return score of search result
	 */
	public String getScore() {
		return score;
	}

	/**
	 * Updates word count of a search result.
	 *
	 * @param newCount the new count to store as count
	 */
	public void setCount(Integer newCount) {
		count = newCount;
	}

	/**
	 * Updates score of a search result.
	 *
	 * @param newScore the new score to store as score
	 */
	public void setScore(String newScore) {
		score = newScore;
	}
}
