import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/* Il client esegue un ciclo nel quale:
 * (1) Legge l'input dell'utente da tastiera.
 * (2) Invia il messaggio letto al server.
 * (3) Riceve (e interpreta) la risposta del server.
 * 
 * I comandi supportati dal client sono:
 * (1) fight  - Combatte contro il mostro.
 * (2) drink  - Beve una certa quantità di pozione.
 * (3) potion - Visualizza la quantità di pozione rimanente.
 * (4) leave  - Abbandona la partita corrente. */

public class Client
{
	public static final String configFile = "client.properties";
	public static Status status = Status.PLAYING; // variabile globale che rappresenta lo stato corrente
	public static String hostname; // nome host del server (localhost)
	public static int port; // porta del server (12000)

	// socket e relativi stream di input/output.
	private static Scanner scanner = new Scanner(System.in);
	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;

	public static void main(String[] args)
	{
		try
		{
			readConfig();
			socket = new Socket(hostname, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			while(true)
			{
				game();

				// se la partita è terminata con una sconfitta oppure
				// è stata interrotta volontariamente dall'utente, esco dal ciclo
				if(status == Status.LOSE || status == Status.INTERRUPTED)
					break;

				// altrimenti chiedo se si vuole effettuare una nuova partita e invio la risposta al server
				System.out.printf("Nuova partita [y/n]?\n> ");
				String command = scanner.nextLine();
				out.println(command);

				if(!command.toLowerCase().equals("y"))
					break;
				
				status = Status.PLAYING;
			}
		}
		catch(Exception e)
		{
			System.err.printf("Errore: %s\n", e.getMessage());
			System.exit(1);
		}
	}

	public static void game() throws IOException
	{
		System.out.println("Partita iniziata... Inserisci un comando");

		while(status == Status.PLAYING)
		{
			System.out.printf("> ");
			String command = scanner.nextLine();
			out.println(command);
			String reply = in.readLine(); 
			String[] parts = reply.split(",");

			switch(parts[0])
			{
				case "WIN":
					status = Status.WIN;
					break;
				case "LOSE":
					status = Status.LOSE;
					break;
				case "DRAW":
					status = Status.DRAW;
					break;
				case "INTERRUPTED":
					status = Status.INTERRUPTED;
					break;
				default:
					break;
			}

			System.out.println(parts[1]);
		}

		System.out.println("Partita terminata");
	}

	public static void readConfig() throws FileNotFoundException, IOException
	{
		InputStream input = Server.class.getResourceAsStream(configFile);
		Properties prop = new Properties();
		prop.load(input);
		port = Integer.parseInt(prop.getProperty("port"));
		hostname = prop.getProperty("hostname");
		input.close();
	}
}