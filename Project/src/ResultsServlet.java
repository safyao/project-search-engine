import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet class that displays the results of a search engine using the
 * Bulma CSS framework.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class ResultsServlet extends HttpServlet {

	/** Identifier used for serialization (unused). */
	private static final long serialVersionUID = 1L;

	/** The title to use for this webpage. */
	private static final String TITLE = "On the Hunt";

	/** The search builder used by this class. */
	private final InvertedIndex index;

	/** The queue of queries used by this class. */
	private final ConcurrentLinkedDeque<String> queries;

	/**
	 * Initializes this servlet.
	 *
	 * @param index the index to initialize
	 * @param queries the queue of queries to initialize
	 */
	public ResultsServlet(InvertedIndex index, ConcurrentLinkedDeque<String> queries) {
		super();
		this.index = index;
		this.queries = queries;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();

		out.printf("<!DOCTYPE html>%n");
		out.printf("<html>%n");
		out.printf("<head>%n");
		out.printf("	<meta charset=\"utf-8\">%n");
		out.printf("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">%n");
		out.printf("	<title>%s</title>%n", TITLE);
		out.printf("	<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css\">%n");
		out.printf("	<script defer src=\"https://use.fontawesome.com/releases/v5.3.1/js/all.js\"></script>%n");
		out.printf("</head>%n");
		out.printf("%n");
		out.printf("<body>%n");
		out.printf("	<section class=\"hero is-primary is-bold\">%n");
		out.printf("	  <div class=\"hero-body\">%n");
		out.printf("	    <div class=\"container\">%n");
		out.printf("			<figure class=\"image is-128x128\">%n");
		out.printf("				<img class=\"is-rounded\" src=\"https://free.clipartof.com/733-Free-Clipart-Of-Earth.jpg\">%n");
		out.printf("			</figure>%n");
		out.printf("	      <h1 class=\"title\">%n");
		out.printf("	        On the Hunt%n");
		out.printf("	      </h1>%n");
		out.printf("	      <h2 class=\"subtitle\">%n");
		out.printf("					<i class=\"fas fa-globe-americas\"></i>%n");
		out.printf("					&nbsp;If you haven't found it yet, keep looking.%n");
		out.printf("	      </h2>%n");
		out.printf("	    </div>%n");
		out.printf("	  </div>%n");
		out.printf("	</section>%n");
		out.printf("%n");
		out.printf("	<section class=\"section\">%n");
		out.printf("		<div class=\"container\">%n");
		out.printf("			<h2 class=\"title\">Results</h2>%n");
		out.printf("%n");

		writeHtml(out);

		out.printf("%n");
		out.printf("		</div>%n");
		out.printf("	</section>%n");
		out.printf("%n");
		out.printf("	<footer class=\"footer\">%n");
		out.printf("	  <div class=\"content has-text-centered\">%n");
		out.printf("	    <p>%n");
		out.printf("	      This request was handled by thread %s.%n", Thread.currentThread().getName());
		out.printf("	    </p>%n");
		out.printf("	  </div>%n");
		out.printf("	</footer>%n");
		out.printf("</body>%n");
		out.printf("</html>%n");

		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Writes search results for query in HTML format.
	 *
	 * @param out the PrintWriter to use
	 */
	private void writeHtml(PrintWriter out) {
		synchronized (queries) {
			if (!queries.isEmpty()) {
				// Performs the search.
				String query = queries.getLast();
				Set<String> querySet = TextStemmer.uniqueStems(query);
				List<InvertedIndex.SearchResult> results;
				if (!querySet.isEmpty()) {
					results = index.search(querySet, false);
					int total = results.size();

					// Prints each result and its stats.
					for (InvertedIndex.SearchResult result : results) {
						String link = result.getWhere();
						int count = result.getCount();
						double score = result.getScore();
						DecimalFormat df = new DecimalFormat("#.###");

						out.printf("				<div class=\"box\">%n");
						out.printf("				<a href=\"%s\">%s</a>: %s%n", link, link, count);
						out.printf("				<p class=\"has-text-grey is-size-7 has-text-right\">Score: %s</p>%n", df.format(score));
						out.printf("				</div>%n");
						out.printf("%n");
					}

					// Prints total number of results.
					out.printf("	<section class=\"section\">%n");
					out.printf("		<div class=\"container\">%n");
					out.printf("			<h2 class=\"title\">Total Number of Results: %s</h2>%n", total);
					out.printf("%n");
					out.printf("		</div>%n");
					out.printf("	</section>%n");
					out.printf("%n");
		 		}
				else {
					out.printf("				<p>No results.</p>%n");
				}
			}
			else {
				out.printf("				<p>No results.</p>%n");
			}
		}
	}
}