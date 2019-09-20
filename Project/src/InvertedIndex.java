import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;
//import java.nio.file.Files;
//import java.nio.file.Path;


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
	
	public void addKey(String key, TreeMap<String, TreeSet<Integer>> value)
	{
		if (!map.containsKey(key))
		{
			map.put(key, value);
		}
		else
		{
			map.replace(key, value);
		}
	}
	
	public TreeMap<String, TreeSet<Integer>> getValue(String key)
	{
		return map.get(key);
	}
	
	public Map<String, TreeMap<String, TreeSet<Integer>>> getIndex()
	{
		return map;
	}
	
	public Set<String> getKeys()
	{
		return map.keySet();
	}
	
	public String toString()
	{
		return map.toString();
	}
	
//	public void addStringValue(String key, String value, TreeSet<Integer> setValue)
//	{
//		if (map.containsKey(key))
//		{
//			TreeMap<String, TreeSet<Integer>> newValue = map.get(key);
//			if (newValue.containsKey(value))
//			{
//				newValue.replace(value, setValue);
//			}
//			else
//			{
//				
//			}
//			
//			map.replace(key, newValue);
//		}
//	}
//	public void addIntValue(String key, String value, Integer intValue)
//	{
//		if (map.containsKey(key))
//		{
//			TreeSet<Integer> newValue = map.get(key).get(value);	//sets dont allow duplicates anyway
//			newValue.add(intValue);
//
//		}
//	}
	
	
//	TODO Implement getters & setters

//	public addValue(TreeMap<String, TreeSet<Integer>> value);
//	public addIntValue(String key, String treeValue, Integer value);
	
//	public getElement();
//	public getKey();
//	public getValue();
//	public getIntValue();
//	public toString()

	
}