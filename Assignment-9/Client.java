/*
 * Questa classe contiene l'implementazione del client NIO che:
 * (1) Richiede all'utente di inserire un messaggio.
 * (2) Invia il messaggio al server.
 * (3) Legge la risposta del server e la stampa su schermo.
 * (4) Chiude la connessione se riceve la stringa di terminazione.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Properties;
import java.util.Scanner;

public class Client
{
	public static final String configFile = "client.properties"; // nome del file di configurazione
	public static String hostname; // nome dell'host del server
	public static int port; // porta di ascolto del server
	public static int bufSize; // dimensione del buffer
	// messaggio di terminazione, se letto da input provoca la chiusura della connessione
	public static String exitMessage;

	public static void main(String[] args)
	{
		try
		{
			readConfig();
		}
		catch(Exception e)
		{
			System.err.println("[CLIENT] Errore durante la lettura del file di configurazione");
			e.printStackTrace();
			System.exit(1);
		}

		// alloco un buffer con la dimensione fissata per invio/ricezione di messaggi
		ByteBuffer buffer = ByteBuffer.allocate(bufSize);

		// apro uno Scanner per leggere l'input da tastiera
		// apro un SocketChannel per collegarmi al server
		try(
			Scanner scanner = new Scanner(System.in);
			SocketChannel sc = SocketChannel.open(new InetSocketAddress(hostname, port)); )
		{
			while(true)
			{
				// chiedo all'utente di inserire un messaggio da tastiera
				System.out.printf("[CLIENT] Inserisci il messaggio: ");
				String inputStr = scanner.nextLine();
				byte[] message = inputStr.getBytes();

				// preparo il buffer per la scrittura (inserisco dati nel buffer)
				buffer.clear();

				// inserisco la lunghezza del messaggio e il messaggio vero e proprio
				buffer.putInt(message.length);
				buffer.put(message);

				// preparo il buffer per la lettura e poi li scrivo sul canale
				buffer.flip();
				sc.write(buffer);

				/* Se il messaggio letto da tastiera corrisponde alla stringa di terminazione, esco dal ciclo.
				 * NOTA: il SocketChannel si chiuderà automaticamente. */
				if(inputStr.equalsIgnoreCase(exitMessage))
					break;
				
				/* A questo punto attendo la risposta del server.
				 * La risposta è costituita dalla coppia (lunghezza, messaggio). */
				buffer.clear();
				sc.read(buffer);
				buffer.flip();

				// estraggo la lunghezza e quindi leggo il contenuto del messaggio
				int replyLength = buffer.getInt();
				byte[] replyBytes = new byte[replyLength];
				buffer.get(replyBytes);
				System.out.printf("[CLIENT] Ricevuto: %s\n", new String(replyBytes));
			}
		}
		catch(Exception e)
		{
			System.err.println("[CLIENT] Errore!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Legge il file di configurazione del client.
	 * @throws FileNotFoundException se il file non esiste
	 * @throws IOException se si verifica un errore durante la lettura
	 */
	private static void readConfig() throws FileNotFoundException, IOException
	{
		InputStream input = new FileInputStream(configFile);
		Properties prop = new Properties();
		prop.load(input);
		hostname = prop.getProperty("hostname");
		port = Integer.parseInt(prop.getProperty("port"));
		bufSize = Integer.parseInt(prop.getProperty("bufSize"));
		exitMessage = prop.getProperty("exitMessage");
		input.close();
	}
}