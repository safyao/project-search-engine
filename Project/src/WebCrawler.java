import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class WebCrawler
{
	private final WorkQueue queue;

	private final InvertedIndex index;

	private final ArrayList<URL> allLinks;

	private final int limit;

	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	public WebCrawler(WorkQueue queue, InvertedIndex index, int limit) {
		this.queue = queue;
		this.index = index;
		this.limit = limit;
		allLinks = new ArrayList<URL>();
	}

	public void crawl(String seed) throws MalformedURLException {
		URL url = LinkParser.clean(new URL(seed));
		allLinks.add(url);
		Task task = new Task(url);
		queue.execute(task);
		queue.finish();
	}

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

	private class Task implements Runnable {

		private final URL url;

		public Task(URL url) {
			this.url = url;
		}

		@Override
		public void run() {
			String html = HtmlFetcher.fetch(url, 3);
			html = HtmlCleaner.stripBlockElements(html);

			try {
				URL base = new URL("http://www.example.com");
				ArrayList<URL> links = LinkParser.listLinks(base, html);
				for (URL link : links)
				{
					synchronized(allLinks) {
						if (allLinks.size() >= limit)
						{
							break;
						}
						else if (!allLinks.contains(url))
						{
							allLinks.add(link);
							Task task = new Task(link);
							queue.execute(task);
						}
					}
				}
				html = HtmlCleaner.stripHtml(html);
				addUrl(url, html);
			}
			catch (MalformedURLException e) {
				System.err.println("Unable to fetch HTML content for: " + url.toString());
			}
		}
	}
}