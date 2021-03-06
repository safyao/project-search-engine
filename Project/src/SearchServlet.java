import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;

/**
 * A servlet class that displays the home page of a search engine using the
 * Bulma CSS framework.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchServlet extends HttpServlet {

	/** Identifier used for serialization (unused). */
	private static final long serialVersionUID = 1L;

	/** The title to use for this webpage. */
	private static final String TITLE = "On the Hunt";

	/** The thread-safe data structure to use for storing queries. */
	private final ConcurrentLinkedDeque<String> queries;

	/** The thread-safe data structure to use for storing formatted queries. */
	private final ConcurrentLinkedQueue<String> history;

	/**
	 * Initializes this Servlet.
	 *
	 * @param queries the queries to initialize
	 */
	public SearchServlet(ConcurrentLinkedDeque<String> queries) {
		super();
		this.queries = queries;
		history = new ConcurrentLinkedQueue<>();
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
		out.printf("			</figure>");
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
		out.printf("			<h2 class=\"title\">Search</h2>%n");
		out.printf("%n");
		out.printf("			<form method=\"%s\" action=\"%s\">%n", "POST", request.getServletPath());
		out.printf("				<div class=\"field\">%n");
		out.printf("				  <label class=\"label\">Query</label>%n");
		out.printf("				  <div class=\"control\">%n");
		out.printf("				    <textarea class=\"textarea\" name=\"%s\" placeholder=\"Enter your query here.\"></textarea>%n", "query");
		out.printf("				  </div>%n");
		out.printf("				</div>%n");
		out.printf("%n");
		out.printf("				<div class=\"control\">%n");
		out.printf("			    <button class=\"button is-primary\" type=\"submit\">%n");
		out.printf("						<i class=\"fas fa-search\"></i>%n");
		out.printf("						&nbsp;%n");
		out.printf("						Search Query%n");
		out.printf("					</button>%n");
		out.printf("			  </div>%n");
		out.printf("			</form>%n");
		out.printf("		</div>%n");
		out.printf("	</section>%n");
		out.printf("%n");
		out.printf("	<section class=\"section\">%n");
		out.printf("		<div class=\"container\">%n");
		out.printf("			<h2 class=\"title\">Query History</h2>%n");
		out.printf("%n");

		// Outputs history of queries.
		if (history.isEmpty()) {
			out.printf("				<p>No queries.</p>%n");
		}
		else {
			for (String item : history) {
				out.printf("				<div class=\"box\">%n");
				out.printf(item);
				out.printf("				</div>%n");
				out.printf("%n");
			}
		}

		out.printf("			</div>%n");
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		String query = request.getParameter("query");

		query = query == null ? "" : query;

		// Avoids XSS attacks using Apache Commons Text.
		query = StringEscapeUtils.escapeHtml4(query);

		String formatted = String.format(
				"					<i class=\"fas fa-search-plus has-text-grey-light\"></i> %s %n" +
				"					<p class=\"has-text-grey is-size-7 has-text-right\"><em><i class=\"fas fa-history has-text-grey-light\"></i> Searched at %s.</em></p>%n",
				query,
				getDate()
		);

		// Adds query to list, and performs a search on that query.
		// Keep in mind multiple threads may access at once.
		history.add(formatted);
		queries.addLast(query);

		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect("/results");
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
}