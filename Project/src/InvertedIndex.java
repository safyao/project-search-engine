import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Nested data structure class that houses Integers, TreeSets, Strings, and TreeMaps in a Map.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class InvertedIndex
{
	/**
	 * Stores arguments in key = value pairs.
	 */
	private final Map<String, TreeMap<String, TreeSet<Integer>>> map;
	
	/**
	 * Initializes this argument map.
	 */
	public InvertedIndex()
	{
		map = new TreeMap<>();
	}
	
	/**
	 * Adds elements to map if the given element does not already exist for the corresponding 
	 * key(s). If a new word or path is being entered, also initializes it's associated value.
	 * 
	 * @param word the word to add if it's not already in the map
	 * @param path the path to add if it's not already mapped to the corresponding word
	 * @param position the position to add if it's not already mapped to the corresponding word and path
	 */
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

	/**
	 * Returns the argument map.
	 * 
	 * @return the map
	 */
	public Map<String, TreeMap<String, TreeSet<Integer>>> getIndex()
	{
		return map;
	}

	@Override
	public String toString()
	{
		return map.toString();
	}
}