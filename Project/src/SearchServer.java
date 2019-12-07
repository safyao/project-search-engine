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

	public SearchServer(int port)
	{
		this.PORT = port;
	}
	/**
	 * Sets up a Jetty server with different servlet instances.
	 *
	 * @param args unused
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(new Servlet()), "/pie");
		handler.addServletWithMapping(new ServletHolder(new Servlet()), "/cake");
		handler.addServletWithMapping(new ServletHolder(new Servlet()), "/bulma");

		server.setHandler(handler);
		server.start();
		server.join();
	}
}