import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;

/**
 * Parses and stores command-line arguments into simple key = value pairs.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class ArgParser {

	/**
	 * Stores command-line arguments in key = value pairs.
	 */
	private final Map<String, String> map;

	/**
	 * Initializes this argument map.
	 */
	public ArgParser() {
		this.map = new HashMap<>();
	}

	/**
	 * Initializes this argument map and then parsers the arguments into flag/value
	 * pairs where possible. Some flags may not have associated values. If a flag is
	 * repeated, its value is overwritten.
	 *
	 * @param args the command line arguments to parse
	 */
	public ArgParser(String[] args) {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		this();
		parse(args);
	}

	/**
	 * Parses the arguments into flag/value pairs where possible. Some flags may not
	 * have associated values. If a flag is repeated, its value is overwritten.
	 *
	 * @param args the command line arguments to parse
	 */
	public void parse(String[] args) {
		
		for (int i = 0; i < args.length; i++) 
		{
			if (isFlag(args[i])) 
			{
				if (!hasFlag(args[i])) 
				{
					map.put(args[i], null);
				}
				else 
				{
					map.replace(args[i], null);
				}
				if ((i+1) != args.length) 
				{
					if (isValue(args[i+1])) 
					{
						map.replace(args[i], args[i+1]);
					}
				}
			}
			
			if (!hasValue("-index"))
			{
				map.replace("-index", "index.json");
			}
			if (!hasValue("-counts"))
			{
				map.replace("-counts", "counts.json");
			}
		}
	}

	/**
	 * Determines whether the argument is a flag. Flags start with a dash "-"
	 * character, followed by at least one other character.
	 *
	 * @param arg the argument to test if its a flag
	 * @return {@code true} if the argument is a flag
	 *
	 * @see String#startsWith(String)
	 * @see String#length()
	 */
	public static boolean isFlag(String arg) {
		
		if (arg == null)
		{
			return false;
		}
		if (arg.startsWith("-") && (arg.length() > 1))
		{
			return true;
		}
		return false;
	}

	/**
	 * Determines whether the argument is a value. Values do not start with a dash
	 * "-" character, and must consist of at least one character.
	 *
	 * @param arg the argument to test if its a value
	 * @return {@code true} if the argument is a value
	 *
	 * @see String#startsWith(String)
	 * @see String#length()
	 */
	public static boolean isValue(String arg) {
		
		if (arg == null)
		{
			return false;
		}
		if (!arg.startsWith("-") && (arg.length() > 0))
		{
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of unique flags.
	 *
	 * @return number of unique flags
	 */
	public int numFlags() {
		return map.size();
	}

	/**
	 * Determines whether the specified flag exists.
	 *
	 * @param flag the flag to search for
	 * @return {@code true} if the flag exists
	 */
	public boolean hasFlag(String flag) {
		return map.containsKey(flag);
	}

	/**
	 * Determines whether the specified flag is mapped to a non-null value.
	 *
	 * @param flag the flag to search for
	 * @return {@code true} if the flag is mapped to a non-null value
	 */
	public boolean hasValue(String flag) {
		if (!hasFlag(flag))
		{
			return false;
		}
		return (getString(flag) != null);
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link String},
	 * or null if there is no mapping for the flag.
	 *
	 * @param flag the flag whose associated value is to be returned
	 * @return the value to which the specified flag is mapped, or {@code null} if
	 *         there is no mapping for the flag
	 */
	public String getString(String flag) {
		if (hasFlag(flag))
		{
			return map.get(flag);
		}
		return null;
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link String},
	 * or the default value if there is no mapping for the flag.
	 *
	 * @param flag         the flag whose associated value is to be returned
	 * @param defaultValue the default value to return if there is no mapping for
	 *                     the flag
	 * @return the value to which the specified flag is mapped, or the default value
	 *         if there is no mapping for the flag
	 */
	public String getString(String flag, String defaultValue) {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		String value = getString(flag);
		return value == null ? defaultValue : value;
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link Path}, or
	 * {@code null} if the flag does not exist or has a null value.
	 *
	 * @param flag the flag whose associated value is to be returned
	 * @return the value to which the specified flag is mapped, or {@code null} if
	 *         the flag does not exist or has a null value
	 *
	 * @see Path#of(String, String...)
	 */
	public Path getPath(String flag) {
		if (hasFlag(flag))
		{
			if (getString(flag) != null)
			{
				return Path.of(getString(flag));
			}
		}
		return null;
	}

	/**
	 * Returns the value the specified flag is mapped as a {@link Path}, or the
	 * default value if the flag does not exist or has a null value.
	 *
	 * @param flag         the flag whose associated value will be returned
	 * @param defaultValue the default value to return if there is no valid mapping
	 *                     for the flag
	 * @return the value the specified flag is mapped as a {@link Path}, or the
	 *         default value if there is no valid mapping for the flag
	 */
	public Path getPath(String flag, Path defaultValue) {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		Path value = getPath(flag);
		return value == null ? defaultValue : value;
	}

	@Override
	public String toString() {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		return this.map.toString();
	}

	/**
	 * A simple main method that parses the command-line arguments provided and
	 * prints the result to the console.
	 *
	 * @param args the command-line arguments to parse
	 */
	
	//TODO Remove when complete
//	public static void main(String[] args) {
//		String[] test = new String[] {"-a", "ant", "-b", "bee", "-b", "bat", "cat", "-d", "-e", "elk", "-f", "-index", "-counts"};
//		var map = new ArgParser(test);
//		System.out.println(map);
//	}
}