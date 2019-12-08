import java.util.concurrent.ConcurrentLinkedDeque;

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

	/** The index used by this class. */
	private final InvertedIndex index;

	/** The queue of queries used by this class. */
	private final ConcurrentLinkedDeque<String> queries;

	/**
	 * Initializes the port and SearchBuilderInterface.
	 *
	 * @param port the port to initialize
	 * @param index the index to initialize
	 */
	public SearchServer(int port, InvertedIndex index) {
		this.PORT = port;
		this.index = index;
		this.queries = new ConcurrentLinkedDeque<>();
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
		handler.addServletWithMapping(new ServletHolder(new SearchServlet(queries)), "/");
		// Displays results page.
		handler.addServletWithMapping(new ServletHolder(new ResultsServlet(index, queries)), "/results");
		// Displays index word counts page.
		handler.addServletWithMapping(new ServletHolder(new IndexServlet(index)), "/index");

		server.setHandler(handler);
		server.start();
		server.join();
	}
}