import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Nested data structure class that houses Integers, TreeSets, Strings, and TreeMaps in a Map.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class InvertedIndex {
	/**
	 * Stores arguments in key = value pairs.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> map;
	
	/**
	 * Initializes this argument map.
	 */
	public InvertedIndex() {
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
	public void add(String word, String path, Integer position) {
		map.putIfAbsent(word, new TreeMap<String, TreeSet<Integer>>());
		map.get(word).putIfAbsent(path, new TreeSet<Integer>());
		map.get(word).get(path).add(position);
	}

	// TODO Still breaks encapsulation... see: 
	// https://github.com/usf-cs212-fall2019/lectures/blob/master/OOP%20Principles/src/PrefixMap.java#L222-L230
	// https://github.com/usf-cs212-fall2019/lectures/blob/master/OOP%20Principles/src/PrefixDemo.java#L72-L78

	@Override
	public String toString() {
		return map.toString();
	}

	public void writeIndex(Path path) throws IOException {
		JsonWriter.asDoubleObject(map, path);
	}

}