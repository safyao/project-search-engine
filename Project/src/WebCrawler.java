import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Crawls a URL and builds an index using its HTML content.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class WebCrawler {

	/** The work queue used by this class. */
	private final WorkQueue queue;

	/** Creates an instance of InvertedIndex. */
	private final InvertedIndex index;

	/** Stores a list of links found from the URL. */
	private final ArrayList<URL> allLinks;

	/** The limit to the amount of links possible for allLinks. */
	private final int limit;

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Initializes the members of this class
	 *
	 * @param index the index to initialize
	 * @param queue the queue to initialize
	 * @param limit the limit to initialize
	 */
	public WebCrawler(WorkQueue queue, InvertedIndex index, int limit) {
		this.queue = queue;
		this.index = index;
		this.limit = limit;
		allLinks = new ArrayList<URL>();
	}


	/**
	 * Crawls the given URL using a work queue.
	 *
	 * @param seed the seed URL to crawl
	 * @throws MalformedURLException
	 */
	public void crawl(String seed) throws MalformedURLException {
		URL url = LinkParser.clean(new URL(seed));
		allLinks.add(url);
		Task task = new Task(url);
		queue.execute(task);
		queue.finish();
	}

	/**
	 * Adds the HTML content of a URL to the InvertedIndex.
	 *
	 * @param url the URL to add to the index
	 * @param html the HTML content to add to the index
	 */
	public void addUrl(URL url, String html) {

		int positionCount = 0;

		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		String urlName = url.toString();

		String[] parsedHtml = TextParser.parse(html);

		for (String word : parsedHtml) {
			String stemmedWord = stemmer.stem(word).toString();
			index.add(stemmedWord, urlName, ++positionCount);
		}
	}

	/**
	 * Static nested class that implements Runnable and runs tasks.
	 */
	private class Task implements Runnable {

		/** Creates an instance of the URL to fetch. */
		private final URL url;

		/**
		 * Initializes the members of this class.
		 *
		 * @param url the URL to initialize
		 */
		public Task(URL url) {
			this.url = url;
		}

		@Override
		public void run() {
			String html = HtmlFetcher.fetch(url, 3);

			if (html != null) {
				html = HtmlCleaner.stripComments(html);
				ArrayList<URL> links = LinkParser.listLinks(url, html);

				for (URL link : links) {

					synchronized(allLinks) {
						if (allLinks.size() >= limit) {
							break;
						}
						else if (!allLinks.contains(link)) {
							allLinks.add(link);
							Task task = new Task(link);
							queue.execute(task);
						}
					}
				}
				html = HtmlCleaner.stripHtml(html);
				addUrl(url, html);
			}
		}
	}
}