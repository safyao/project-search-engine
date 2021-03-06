import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Utility class for parsing and stemming text and text files into sets of
 * stemmed words.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 *
 * @see TextParser
 */
public class TextStemmer {

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;


	/**
	 * Returns a list of cleaned and stemmed words (not unique) parsed from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @return a list of cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #listStems(String, Stemmer)
	 */
	public static List<String> listStems(String line) {
		return listStems(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a list of cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static List<String> listStems(String line, Stemmer stemmer) {
		String[] parsedText = TextParser.parse(line);
		List<String> stemmedList = new ArrayList<>();

		for (String word : parsedText) {
			String stemmedWord = stemmer.stem(word).toString();
			stemmedList.add(stemmedWord);
		}

		return stemmedList;
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a list.
	 *
	 * @param inputFile the input file to parse
	 * @return a list of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see #listStems(String)
	 * @see TextParser#parse(String)
	 */
	public static List<String> listStems(Path inputFile) throws IOException {

		List<String> stemmedLines = new ArrayList<>();
		Stemmer stemmer = new SnowballStemmer(DEFAULT);

		try (
				BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);
		) {

			String line = null;

			while ((line = reader.readLine()) != null) {

				List<String> stemmedLine = listStems(line, stemmer);

				for (String item : stemmedLine) {
					stemmedLines.add(item);
				}
			}
		}
		return stemmedLines;
	}

	/**
	 * Returns a set of unique (no duplicates) cleaned and stemmed words parsed
	 * from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @return a sorted set of unique cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #uniqueStems(String, Stemmer)
	 */
	public static TreeSet<String> uniqueStems(String line) {
		return uniqueStems(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Returns a set of unique (no duplicates) cleaned and stemmed words parsed
	 * from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a sorted set of unique cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> uniqueStems(String line, Stemmer stemmer) {

		String[] parsedText = TextParser.parse(line);
		TreeSet<String> stemmedSet = new TreeSet<>();

		for (String word : parsedText) {
			String stemmedWord = (String)stemmer.stem(word);
			stemmedSet.add(stemmedWord);
		}

		return stemmedSet;
	}
}
