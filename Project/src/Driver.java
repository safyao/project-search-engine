import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class Driver {

	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {

		// Initialize ArgumentParser and InvertedIndex for given command-line arguments.
		ArgumentParser parser = new ArgumentParser(args);
		InvertedIndex index = new InvertedIndex();
		IndexBuilder builder = new IndexBuilder(index);

		if (parser.hasFlag("-path")) {
			try {
				// Builds index and the word counts after traversing the given path.
				builder.buildIndex(parser.getPath("-path"));
			}
			catch (NullPointerException e) {
				System.err.println("Please enter a valid path argument.");
			}
			catch (IOException e) {
				System.err.println("Unable to traverse and stem the given file(s).");
			}
		}

		if (parser.hasFlag("-index")) {
			Path indexPath = parser.getPath("-index", Path.of("index.json"));

			try {
				// Writes index in pretty Json format to file.
				index.writeIndex(indexPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the inverted index to a JSON file at: \n" + indexPath);
			}
		}

		if (parser.hasFlag("-counts")) {
			Path countsPath = parser.getPath("-counts", Path.of("counts.json"));

			try {
				// Writes counts in pretty Json format to file.
				index.writeCounts(countsPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the word counts to a JSON file at: \n" + countsPath);
			}
		}


		if (parser.hasFlag("-query")) {
			Path queryPath = parser.getPath("-query");
			try (
				BufferedReader reader = Files.newBufferedReader(queryPath, StandardCharsets.UTF_8);
			) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					List<String> queryList = TextStemmer.queryParser(line);
					System.out.println("Query Words: " + queryList);
					List<SearchResult> results = SearchBuilder.exactSearch(queryList, index);
					for (SearchResult item : results)
					{
						System.out.println("\twhere: " + item.getWhere());
						System.out.println("\tcount: " + item.getCount());
						System.out.println("\tscore: " + item.getScore());
					}
				}

			}
			catch (IOException e) {
				System.err.println("Unable to parse query line for: " + queryPath);
			}
		}
	}
}
