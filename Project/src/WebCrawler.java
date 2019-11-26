import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WebCrawler
{
	private final WorkQueue queue;

	private final InvertedIndex index;

	private final IndexBuilder builder;

	private final ArrayList<URL> allLinks;

	public WebCrawler(WorkQueue queue, ThreadSafeIndex index, IndexBuilder builder) {
		this.queue = queue;
		this.index = index;
		this.builder = builder;
		allLinks = new ArrayList<URL>();
	}

	public void crawl(String seed) throws MalformedURLException {
		URL url = LinkParser.clean(new URL(seed));
		if (!allLinks.contains(url) && allLinks.size() < 50)
		{
			allLinks.add(url);
		}
	}


	private class Task implements Runnable {

		public Task() {

		}

		@Override
		public void run() {

		}
	}





















//	public void crawl(String seed) throws MalformedURLException, IOException
//	{
//		List<URL> allLinks = new ArrayList<>();
//		URL base = new URL("http://www.example.com");
//		URL url = LinkParser.clean(new URL(seed));
//		if (!allLinks.contains(url) && allLinks.size() < 50)
//		{
//			allLinks.add(url);
//			String html = HtmlFetcher.fetch(url, 3);
//			ArrayList<URL> links = LinkParser.listLinks(base, html);
//		}
//
//	}

//	public static List<URL> crawl(String seed) throws MalformedURLException, IOException {
//		List<URL> allLinks = new ArrayList<>();
//		crawl(seed, allLinks);
//		return allLinks;
//	}
//
//	private static void crawl(String seed, List<URL> allLinks) throws MalformedURLException, IOException {
//
//		URL base = new URL("http://www.example.com");
//		URL url = LinkParser.clean(new URL(seed));
//		if (!allLinks.contains(url) && allLinks.size() < 50) {
//			allLinks.add(url);

//			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
//
//				for (Path entry : stream) {
//					// Use recursion to loop through nested directories.
//					traverseDirectory(entry, allFiles);
//				}
//			}
//		}
//		// Only adds text files to list.
//		else if (isTextFile(path)) {
//			allFiles.add(path);
//		}
//	}
}