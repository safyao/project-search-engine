import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Demonstrates how to create a simple message board using Jetty and servlets,
 * as well as how to initialize servlets when you need to call its constructor.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SearchServer {

	/** The port to run this server. */
	public final int PORT;

	/** The search builder used by this class. */
	private final SearchBuilderInterface searchBuilder;

	/** The index used by this class. */
	private final InvertedIndex index;

	/**
	 * Initializes the port and SearchBuilderInterface.
	 *
	 * @param port the port to initialize
	 * @param searchBuilder the searchBuilder to initialize
	 * @param index the index to initialize
	 */
	public SearchServer(int port, SearchBuilderInterface searchBuilder, InvertedIndex index) {
		this.PORT = port;
		this.searchBuilder = searchBuilder;
		this.index = index;
	}

	/**
	 * Sets up a Jetty server with different servlet instances.
	 *
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();
		// Displays home page.
		handler.addServletWithMapping(new ServletHolder(new SearchServlet(searchBuilder)), "/");
		// Displays results page.
		handler.addServletWithMapping(new ServletHolder(new ResultsServlet(searchBuilder)), "/results");
		// Displays index word counts page.
		handler.addServletWithMapping(new ServletHolder(new IndexServlet(index)), "/index-counts");

		server.setHandler(handler);
		server.start();
		server.join();
	}
}