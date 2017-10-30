import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class ServerStartupMerchantsEntry {
	private Server server;

	public static void main(String[] args) {
		ServerStartupMerchantsEntry web = new ServerStartupMerchantsEntry();
		web.startup();
	}

	public ServerStartupMerchantsEntry() {
	}

	public void startup() {
		server = new Server();

		int wapPort = 82;
		Connector connector = new SelectChannelConnector();
		connector.setPort(wapPort);
		connector.setHost("0.0.0.0");
		server.setConnectors(new Connector[] { connector });

		WebAppContext context = new WebAppContext("src/main/webapp", "/");

		server.addHandler(context);
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		server.addHandler(requestLogHandler);
		try {
			server.setStopAtShutdown(true);
			server.setSendServerVersion(true);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
