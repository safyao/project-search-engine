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
	
	public void addKey(String word, String path, Integer position)
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
	
//	public TreeMap<String, TreeSet<Integer>> getValue(String key)
//	{
//		return map.get(key);
//	}
//	
	public Map<String, TreeMap<String, TreeSet<Integer>>> getIndex()
	{
		return map;
	}
//	
//	public Set<String> getKeys()
//	{
//		return map.keySet();
//	}
	
	public String toString()
	{
		return map.toString();
	}
}