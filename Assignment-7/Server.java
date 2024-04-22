import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/* Il server gestisce un pool di thread ed esegue un ciclo nel quale:
 * (1) Accetta richieste di connessione da parte dei vari client.
 * (2) Per ogni richiesta attiva un thread worker per interagire con il client. */

public class Server
{
	public static final String configFile = "server.properties";
	public static int port;
	public static int maxDelay;
	public static final ExecutorService pool = Executors.newCachedThreadPool();
	public static ServerSocket serverSocket;

	public static void main(String[] args) throws Exception
	{
		try
		{
			readConfig();
			serverSocket = new ServerSocket(port);

			Runtime.getRuntime().addShutdownHook(new TerminationHandler(maxDelay, pool, serverSocket));

			System.out.printf("[SERVER] In ascolto sulla porta: %d\n", port);

			while(true)
			{
				Socket socket = null;
				try
				{	// accetto le richieste provenienti dai client
					socket = serverSocket.accept();
				}
				catch(SocketException e)
				{
					break;
				}
				pool.execute(new Worker(socket));
			}
		}
		catch(Exception e)
		{
			System.err.printf("[SERVER]: %s\n",e.getMessage());
			System.exit(1);
		}
	}

	public static void readConfig() throws FileNotFoundException, IOException
	{
		InputStream input = Server.class.getResourceAsStream(configFile);
		Properties prop = new Properties();
		prop.load(input);
		port = Integer.parseInt(prop.getProperty("port"));
		maxDelay = Integer.parseInt(prop.getProperty("maxDelay"));
		input.close();
	}
}