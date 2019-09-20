import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;


/**
 * @author Safya Osman
 *
 */
public class InvertedIndex
{
	private final Map<String, TreeMap<String, TreeSet<Integer>>> map;
	
	public InvertedIndex()
	{
		map = new TreeMap<>();
	}
	
	public void add(String word, String path, Integer position)
	{
		if (!map.containsKey(word))
		{
			map.put(word, new TreeMap<String, TreeSet<Integer>>());
		}
		if (!map.get(word).containsKey(path))
		{
			map.get(word).put(path, new TreeSet<Integer>());
		}
		if (!map.get(word).get(path).contains(position))
		{
			map.get(word).get(path).add(position);
		}
	}

	public Map<String, TreeMap<String, TreeSet<Integer>>> getIndex()
	{
		return map;
	}

	
	public String toString()
	{
		return map.toString();
	}
}