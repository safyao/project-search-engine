/*
 * TODO Since a search result is pretty specific to an inverted index, and the result
 * depends on specific data within that index (like total count), it makes sense to
 * make this an non-static inner class within the InvertedIndex class itself!
 */

/**
 * Data structure class that houses a single search result and implements Comparable.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchResult implements Comparable<SearchResult> {

	/** Stores location of search result. */
	private String where; // TODO Make this one final---should not be able to change the location for a result.

	/** Stores word count of search result for given location. */
	private Integer count; // TODO Go ahead and make this an int to avoid too much autoboxing/unboxing

	/** Stores score of search result. */
	private String score; // TODO Make this a double, again to avoid a bunch of conversion back and forth

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

	/**
	 * Compares two SearchResult objects based on score, then count, then location.
	 * @param other the other object to compare to
	 * @return a negative int, zero, or a positive int as the first object is less than, equal to, or greater than the second
	 */
	@Override
	public int compareTo(SearchResult other) {
		if (other.getScore().compareTo(this.score) != 0) {
			return (other.getScore().compareTo(this.score));
		}
		else if (other.getCount().compareTo(this.count) != 0) {
			return (other.getCount().compareTo(this.count));
		}
		return (this.where.compareTo(other.getWhere()));
	}

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

		// TODO Anytime you see yourself get the current value, increase it, and then set that value, make an addCount(...) method instead!
	}

	/**
	 * Updates score of a search result.
	 *
	 * @param newScore the new score to store as score
	 */
	public void setScore(String newScore) {
		score = newScore;

		// TODO It is possible for score and count to get out of date with each other
	}
}
