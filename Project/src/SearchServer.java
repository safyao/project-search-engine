import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Demonstrates how to create a simple message board using Jetty and servlets,
 * as well as how to initialize servlets when you need to call its constructor.
 */
public class SearchServer {

	/** The port to run this server. */
	public final int PORT;

	private final SearchBuilderInterface searchBuilder;

	public SearchServer(int port, SearchBuilderInterface searchBuilder)
	{
		this.PORT = port;
		this.searchBuilder = searchBuilder;
	}
	/**
	 * Sets up a Jetty server with different servlet instances.
	 *
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(new SearchServlet(searchBuilder)), "/");
		handler.addServletWithMapping(new ServletHolder(new ResultsServlet(searchBuilder)), "/results");

		server.setHandler(handler);
		server.start();
		server.join();
	}
}