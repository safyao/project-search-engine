import java.util.Comparator;

public class SearchResult implements Comparable<SearchResult>
{
	private String where;
	private Integer count;
	private String score;

	public SearchResult(String where, int count, String score)
	{
		this.where = where;
		this.count = count;
		this.score = score;
	}

	@Override
	public int compareTo(SearchResult other)
	{
		if (this.score.compareTo(other.getScore()) != 0)
		{
			return (this.score.compareTo(other.getScore()));
		}
		else if (this.count.compareTo(other.getCount()) != 0)
		{
			return (this.count.compareTo(other.getCount()));
		}
		else
		{
			return (other.getWhere().compareTo(this.where));
		}
	}

	public static Comparator<SearchResult> SearchComparator = new Comparator<SearchResult>()
	{
		@Override
		public int compare(SearchResult one, SearchResult two)
		{
			return two.compareTo(one);
		}
	};

	public String getWhere()
	{
		return where;
	}

	public Integer getCount()
	{
		return count;
	}

	public String getScore()
	{
		return score;
	}

	public void setCount(Integer newCount)
	{
		count = newCount;
	}

	public void setScore(String newScore)
	{
		score = newScore;
	}
}
