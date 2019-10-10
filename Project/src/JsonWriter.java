import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Outputs several simple data structures in "pretty" JSON format where
 * newlines are used to separate elements and nested elements are indented.
 *
 * Warning: This class is not thread-safe. If multiple threads access this class
 * concurrently, access must be synchronized externally.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class JsonWriter {

	/**
	 * Writes the elements as a pretty JSON array.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException
	 */
	public static void asArray(Collection<Integer> elements, Writer writer, int level) throws IOException {

		writer.write("[");
		var iterator = elements.iterator();
		level++;

		if (iterator.hasNext()) {
			writer.write("\n");
			indent(iterator.next(), writer, level);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writer.write("\n");
			indent(iterator.next(), writer, level);
		}

		writer.write("\n");
		indent("]", writer, level - 1);
	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static void asArray(Collection<Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static String asArray(Collection<Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException
	 */
	public static void asObject(Map<String, Integer> elements, Writer writer, int level) throws IOException {

		writer.write("{");

		var iterator = elements.entrySet().iterator();
		level++;

		if (iterator.hasNext()) {
			writer.write("\n");
			writeObjectEntry(iterator.next(), writer, level);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writer.write("\n");
			writeObjectEntry(iterator.next(), writer, level);
		}

		writer.write("\n");
		indent("}", writer, level - 1);
	}

	/**
	 * Writes entries of element as key = value pair.
	 *
	 * @param element the element to write
	 * @param writer the writer to use
	 * @param level the level to indent
	 * @throws IOException
	 */
	private static void writeObjectEntry(Entry<String, Integer> element, Writer writer, int level) throws IOException {
		quote(element.getKey(), writer, level);
		writer.write(": ");
		writer.write(element.getValue().toString());
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static void asObject(Map<String, Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static String asObject(Map<String, Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a nested pretty JSON object. The generic notation used
	 * allows this method to be used for any type of map with any type of nested
	 * collection of integer objects.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException
	 */
	public static void asNestedObject(Map<String, ? extends Collection<Integer>> elements, Writer writer, int level) throws IOException {

		writer.write("{");
		var iterator = elements.entrySet().iterator();
		level++;

		if (iterator.hasNext()) {
			writer.write("\n");
			writeNestedEntry(iterator.next(), writer, level);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writer.write("\n");
			writeNestedEntry(iterator.next(), writer, level);
		}

		writer.write("\n");
		indent("}", writer, level - 1);

	}

	/**
	 * Writes entries of element as key = value (array) pair
	 *
	 * @param element the element to write
	 * @param writer the writer to use
	 * @param level the level to indent
	 * @throws IOException
	 */
	private static void writeNestedEntry(Entry<String, ? extends Collection<Integer>> element, Writer writer, int level) throws IOException {
		quote(element.getKey(), writer, level);
		writer.write(": ");
		asArray(element.getValue(), writer, 2);
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException
	 *
	 * @see #asNestedObject(Map, Writer, int)
	 */
	public static void asNestedObject(Map<String, ? extends Collection<Integer>> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(Map, Writer, int)
	 */
	public static String asNestedObject(Map<String, ? extends Collection<Integer>> elements) {
		try {
			StringWriter writer = new StringWriter();
			asNestedObject(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a double-nested pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException
	 */
	public static void asDoubleObject(Map<String, TreeMap<String, TreeSet<Integer>>> elements, Writer writer, int level) throws IOException {

		writer.write("{");
		var iterator = elements.entrySet().iterator();
		level++;

		if (iterator.hasNext()) {
			writer.write("\n");
			writeEntry(iterator.next(), writer, level);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writer.write("\n");
			writeEntry(iterator.next(), writer, level);
		}

		writer.write("\n");
		indent("}", writer, level - 1);
	}

	/**
	 * Writes entries of element as key = value (nested object) pair.
	 *
	 * @param element the element to write
	 * @param writer the writer to use
	 * @param level the level to indent
	 * @throws IOException
	 */
	private static void writeEntry(Entry<String, TreeMap<String, TreeSet<Integer>>> element, Writer writer, int level) throws IOException {
		quote(element.getKey(), writer, level);
		writer.write(": ");
		asNestedObject(element.getValue(), writer, 1);
	}

	/**
	 * Writes the elements as a double-nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException
	 */
	public static void asDoubleObject(Map<String, TreeMap<String, TreeSet<Integer>>> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asDoubleObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a double-nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 */
	public static String asDoubleObject(Map<String, TreeMap<String, TreeSet<Integer>>> elements) {
		try {
			StringWriter writer = new StringWriter();
			asDoubleObject(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}








	public static void asQueryObject(Map<List<String>, List<SearchResult>> results, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asQueryObject(results, writer, 0);
		}
	}

	public static void asQueryObject(Map<List<String>, List<SearchResult>> results, Writer writer, int level) throws IOException {

		writer.write("{");
		var iterator = results.entrySet().iterator();
		level++;

		if (iterator.hasNext()) {
			writer.write("\n");
			writeQueryEntry(iterator.next(), writer, level);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writer.write("\n");
			writeQueryEntry(iterator.next(), writer, level);
		}

		writer.write("\n");
		indent("}", writer, level - 1);
	}

	private static void writeQueryEntry(Entry<List<String>, List<SearchResult>> element, Writer writer, int level) throws IOException {
		quote(element.getKey(), writer);
		writer.write(": ");
		asNestedArray(element.getValue(), writer, 1);
	}

	public static void asNestedArray(Collection<SearchResult> elements, Writer writer, int level) throws IOException
	{
		writer.write("[");
		var iterator = elements.iterator();
		level++;

		if (iterator.hasNext()) {
			writer.write("\n");
			indent(writer, level);
			asSearchObject(iterator.next(), writer, level);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writer.write("\n");
			indent(writer, level);
			asSearchObject(iterator.next(), writer, level);
		}

		writer.write("\n");
		indent("]", writer, level - 1);
	}

	public static void asSearchObject(SearchResult element, Writer writer, int level) throws IOException
	{
		writer.write("{");
		level++;
		writer.write("\n");

		quote("where", writer);
		writer.write(": ");
		quote(element.getWhere(), writer);
		writer.write(",\n");

		quote("count", writer);
		writer.write(": ");
		writer.write(element.getCount());
		writer.write(",\n");

		quote("score", writer);
		writer.write(": ");
		writer.write(element.getScore());
		writer.write("\n");

		indent("}", writer, level - 1);
	}




























//	public static void asNestedArray(Collection<SearchResult> elements, Writer writer, int level) throws IOException {
//
//		writer.write("[");
//		var iterator = elements.iterator();
//		level++;
//
//		if (iterator.hasNext()) {
//			writer.write("\n");
//			indent(writer, level);
//			asSearchObject(iterator.next(), writer, level);
//		}
//
//		while (iterator.hasNext()) {
//			writer.write(",");
//			writer.write("\n");
//			indent(writer, level);
//			asSearchObject(iterator.next(), writer, level);
//		}
//
//		writer.write("\n");
//		indent("]", writer, level - 1);
//	}
//	public static void asNestedArray(Collection<SearchResult> elements, Path path) throws IOException {
//		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//			asNestedArray(elements, writer, 0);
//		}
//	}
//	public static String asNestedArray(Collection<SearchResult> elements) {
//		try {
//			StringWriter writer = new StringWriter();
//			asNestedArray(elements, writer, 0);
//			return writer.toString();
//		}
//		catch (IOException e) {
//			return null;
//		}
//	}
//	public static void asSearchObject(SearchResult element, Writer writer, int level) throws IOException
//	{
//		writer.write("{");
//		level++;
//		writer.write("\n");
//
//		quote("where", writer);
//		writer.write(": ");
//		quote(element.getWhere(), writer);
//		writer.write(",\n");
//
//		quote("count", writer);
//		writer.write(": ");
//		writer.write(element.getCount());
//		writer.write(",\n");
//
//		quote("score", writer);
//		writer.write(": ");
//		writer.write(element.getScore());
//		writer.write("\n");
//
//		indent("}", writer, level - 1);
//	}

//	public static void writeQuery(QueryResults queryResults, Path path) throws IOException
//	{
//		Set<List<String>> queries = queryResults.getQueries();
//
//		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//			for (List<String> query : queries)
//			{
//				var iterator = query.iterator();
//				if (iterator.hasNext()) {
//					writer.write('"');
//					writer.write(iterator.next());
//				}
//
//				while (iterator.hasNext()) {
//					writer.write(", ");
//					writer.write(iterator.next());
//				}
//				writer.write('"');
//				writer.write(": ");
//
//
//			}
//		}
//	}

//	public static void asDoubleObject(QueryResults elements, Writer writer, int level) throws IOException {
//
//		writer.write("{");
//		var iterator = elements.entrySet().iterator();
//		level++;
//
//		if (iterator.hasNext()) {
//			writer.write("\n");
//			writeEntry(iterator.next(), writer, level);
//		}
//
//		while (iterator.hasNext()) {
//			writer.write(",");
//			writer.write("\n");
//			writeEntry(iterator.next(), writer, level);
//		}
//
//		writer.write("\n");
//		indent("}", writer, level - 1);
//	}
//
//	private static void writeEntry(Entry<String, TreeMap<String, TreeSet<Integer>>> element, Writer writer, int level) throws IOException {
//		quote(element.getKey(), writer, level);
//		writer.write(": ");
//		asNestedObject(element.getValue(), writer, 1);
//	}
//
//	public static void asDoubleObject(Map<String, TreeMap<String, TreeSet<Integer>>> elements, Path path) throws IOException {
//		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//			asDoubleObject(elements, writer, 0);
//		}
//	}
//	private static void writeNestedEntry(Entry<String, ? extends Collection<Integer>> element, Writer writer, int level) throws IOException {
//		quote(element.getKey(), writer, level);
//		writer.write(": ");
//		asArray(element.getValue(), writer, 2);
//	}
















	/**
	 * Writes the {@code \t} tab symbol by the number of times specified.
	 *
	 * @param writer the writer to use
	 * @param times  the number of times to write a tab symbol
	 * @throws IOException
	 */
	public static void indent(Writer writer, int times) throws IOException {
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(String, Writer, int)
	 * @see #indent(Writer, int)
	 */
	public static void indent(Integer element, Writer writer, int times) throws IOException {
		indent(element.toString(), writer, times);
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(Writer, int)
	 */
	public static void indent(String element, Writer writer, int times) throws IOException {
		indent(writer, times);
		writer.write(element);
	}

	/**
	 * Writes the element surrounded by {@code " "} quotation marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @throws IOException
	 */
	public static void quote(String element, Writer writer) throws IOException {
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Indents and then writes the element surrounded by {@code " "} quotation
	 * marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(Writer, int)
	 * @see #quote(String, Writer)
	 */
	public static void quote(String element, Writer writer, int times) throws IOException {
		indent(writer, times);
		quote(element, writer);
	}

	public static void quote(List<String> element, Writer writer) throws IOException {
		writer.write('"');
		var iterator = element.iterator();

		if (iterator.hasNext()) {
			writer.write(iterator.next());
		}
		while (iterator.hasNext()) {
			writer.write(" ");
			writer.write(iterator.next());
		}
		writer.write('"');
	}
}
