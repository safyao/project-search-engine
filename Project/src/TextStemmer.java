import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	 * Parses, stems, removes duplicates, sorts, and returns elements of a single
	 * query line as a list of Strings.
	 *
	 * @param line the line to parse
	 * @return list of parsed query words
	 */
	public static List<String> queryParser(String line) {

		List<String> parsedQuery = new ArrayList<>();
		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		String[] parsedLine = TextParser.parse(line);

		for (String word : parsedLine) {
			String stemmedWord = stemmer.stem(word).toString();

			/*
			 * TODO This is VERY inefficient to call on a list. Remove this method.
			 * If you want sorted unique stems, re-add the uniqueStems methods from
			 * the homework that use a TreeSet instead.
			 */
			if (!parsedQuery.contains(stemmedWord)) {
				parsedQuery.add(stemmedWord);
			}
		}

		Collections.sort(parsedQuery);
		return parsedQuery;
	}
}
