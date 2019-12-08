import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An alternative implemention of the {@MessageServlet} class but using the
 * Bulma CSS framework.
 */
public class ResultsServlet extends HttpServlet {

	/** Identifier used for serialization (unused). */
	private static final long serialVersionUID = 1L;

	/** The title to use for this webpage. */
	private static final String TITLE = "Search";

	private final SearchBuilderInterface searchBuilder;

	/**
	 * Initializes this message board. Each message board has its own collection
	 * of messages.
	 */
	public ResultsServlet(SearchBuilderInterface searchBuilder) {
		super();
		this.searchBuilder = searchBuilder;
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
		out.printf("	      <h1 class=\"title\">%n");
		out.printf("	        Search Results%n");
		out.printf("	      </h1>%n");
		out.printf("	      <h2 class=\"subtitle\">%n");
		out.printf("					<i class=\"fas fa-calendar-alt\"></i>%n");
		out.printf("					&nbsp;Time: %s%n", getDate());
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
	 * Returns the date and time in a long format. For example: "12:00 am on
	 * Saturday, January 01 2000".
	 *
	 * @return current date and time
	 */
	private static String getDate() {
		String format = "hh:mm a 'on' EEEE, MMMM dd yyyy";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}

	private void writeHtml(PrintWriter out) {
		Map<String, List<InvertedIndex.SearchResult>> results = searchBuilder.getResults();
		Set<String> queries = results.keySet();

		List<InvertedIndex.SearchResult> links = null;
		for (String query : queries) {
			if (query != null)
			{
				links = results.get(query);
			}
		}

		if (results.isEmpty() || links == null || links.isEmpty()) {
			out.printf("				<p>No results.</p>%n");
		}
		else {

			var iterator = links.iterator();

			while (iterator.hasNext()) {
				out.printf("				<div class=\"box\">%n");
				String link = iterator.next().getWhere();
				out.printf("				<a href=\"%s\">%s</a>%n", link, link);
				out.printf("				</div>%n");
				out.printf("%n");
			}
		}
	}
}