
public class SearchResult
{
	private String where;
	private int count;
	private String score;

	public SearchResult(String where, int count, String score)
	{
		this.where = where;
		this.count = count;
		this.score = score;
	}

	public String getWhere()
	{
		return where;
	}

	public int getCount()
	{
		return count;
	}

	public String getScore()
	{
		return score;
	}
}
