import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Nested data structure class that houses Strings, TreeMaps, TreeSets, and Integers in one Map 
 * and Strings and Integers in another.
 * 
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class InvertedIndex {
	
	/** Stores arguments in key = value pairs. **/
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> map;
	
	/** Stores arguments in key = value pairs. **/
	private final TreeMap<String, Integer> countsMap;
	
	
	/**
	 * Initializes the argument maps.
	 */
	public InvertedIndex() {
		map = new TreeMap<>();
		countsMap = new TreeMap<>();
	}
	
	/**
	 * Adds elements to map if the given element does not already exist for the corresponding 
	 * key(s). If a new word or path is being entered, also initializes it's associated value.
	 * 
	 * @param word the word to add if it's not already in the map
	 * @param path the path to add if it's not already mapped to the corresponding word
	 * @param position the position to add if it's not already mapped to the corresponding word and path
	 */
	public void add(String word, String path, Integer position) {
		map.putIfAbsent(word, new TreeMap<String, TreeSet<Integer>>());
		map.get(word).putIfAbsent(path, new TreeSet<Integer>());
		map.get(word).get(path).add(position);
	}
	
	/**
	 * Adds String element and its corresponding Integer element to countsMap.
	 * 
	 * @param path the path to add to map
	 * @param count the count to add to corresponding path in map
	 */
	public void addCount(String path, Integer count) {
		if (count > 0) {
			countsMap.put(path, count);
		}
	}
	
	
	/**
	 * Writes index in pretty Json format.
	 * 
	 * @param path the path to write the index to
	 * @throws IOException if unable to access path
	 */
	public void writeIndex(Path path) throws IOException {
		JsonWriter.asDoubleObject(map, path);
	}
	
	/**
	 * Writes word counts in pretty Json format.
	 * 
	 * @param path the path to write counts to
	 * @throws IOException if unable to access path
	 */
	public void writeCounts(Path path) throws IOException {
		JsonWriter.asObject(countsMap, path);
	}
	
	@Override
	public String toString() {
		return map.toString();
	}

}