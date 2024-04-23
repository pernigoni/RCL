import java.io.*;
import java.net.*;

public class Server
{
	public static final int port = 12345; // porta di ascolto del server
	public static final String echoString = " [echoed by server]";

	public static void main(String[] args) throws IOException
	{
		try(ServerSocket serverSocket = new ServerSocket(port))
		{
			System.out.println("Server in ascolto sulla porta " + port);

			while(true)
			{
				try(
					Socket clientSocket = serverSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true))
				{
					System.out.println("Connessione accettata");

					String inputLine;
					while((inputLine = in.readLine()) != null)
					{
						System.out.println("Ricevuto dal client: " + inputLine);
						out.println(inputLine + echoString);
					}
				}
			}
		}
	}
}