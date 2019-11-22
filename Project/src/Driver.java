import java.io.IOException;
import java.nio.file.Path;

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

		// Initializes ArgumentParser, InvertedIndex, and WorkQueue for given command-line arguments.
		ArgumentParser parser = new ArgumentParser(args);
		ThreadSafeIndex index = new ThreadSafeIndex();
		WorkQueue queue = new WorkQueue(1); // TODO queue = null;

		/*
		 * TODO 

 		ArgumentParser parser = new ArgumentParser(args);
		InvertedIndex index;
		IndexBuilder builder;
		SearchBuilderInterface searchBuilder;
		WorkQueue queue = null;

 	if (-threads) {
 		ThreadSafeIndex threadSafe = new ThreadSafeIndex();
 		index = threadSafe;
 		
 		builder = new MulithreadedIndexBuilder(threadSafe, queue);
 		etc.
 	}
 	else {
 		index = new InvertedIndex();
 		builder = new IndexBuilder(index);
 		etc.
 	}
 
		 */
		
		if (parser.hasFlag("-threads")) {
			String threads = parser.getString("-threads", "5");
			try {
				int numThreads = Integer.parseInt(threads);
				if (numThreads <= 0) {
					numThreads = 5;
				}
				queue = new WorkQueue(numThreads);
			}
			catch (NumberFormatException e) {
				System.err.println("Please enter a valid argument for the number of threads.");
			}
		}

		// Initializes a multithreaded index builder and search builder.
		MultithreadedIndexBuilder builder = new MultithreadedIndexBuilder(index, queue);
		MultithreadedSearchBuilder searchBuilder = new MultithreadedSearchBuilder(index, queue);

		if (parser.hasFlag("-path")) {
			Path path = parser.getPath("-path");

			try {
				// Builds index and the word counts after traversing the given path.
				builder.buildIndex(path);
			}
			catch (NullPointerException e) {
				System.err.println("Please enter a valid path argument.");
			}
			catch (IOException e) {
				System.err.println("Unable to traverse and stem the given file(s) at: \n" + path);
			}
		}

		if (parser.hasFlag("-index")) {
			Path indexPath = parser.getPath("-index", Path.of("index.json"));

			try {
				// Writes index in pretty JSON format to file.
				index.writeIndex(indexPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the inverted index to a JSON file at: \n" + indexPath);
			}
		}

		if (parser.hasFlag("-counts")) {
			Path countsPath = parser.getPath("-counts", Path.of("counts.json"));

			try {
				// Writes counts in pretty JSON format to file.
				index.writeCounts(countsPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the word counts to a JSON file at: \n" + countsPath);
			}
		}

		if (parser.hasFlag("-query")) {
			Path queryPath = parser.getPath("-query");

			try {
				// Builds query search results from file of queries.
				searchBuilder.buildSearch(queryPath, parser.hasFlag("-exact"));
			}
			catch (NullPointerException e) {
				System.err.println("Please enter a valid query argument.");
			}
			catch (IOException e) {
				System.err.println("Unable to search index for words in query file at: " + queryPath);
			}
		}

		if (parser.hasFlag("-results")) {
			Path resultPath = parser.getPath("-results", Path.of("results.json"));

			try {
				// Writes search results in pretty JSON format to file.
				searchBuilder.writeQuery(resultPath);
			}
			catch (IOException e) {
				System.err.println("Unable to write the search results to a Json file at: " + resultPath);
			}
		}

		// TODO if (queue != null) then shutdown
		queue.shutdown();
	}
}
