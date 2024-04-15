/* 
 * NIO ECHO SERVER
 * 
 * Scrivere un programma echo server usando la libreria Java NIO, in particolare selector e canali in
 * modalità non bloccante.
 * 
 * Scrivere un programma echo client usando NIO, va bene anche in modalità bloccante.
 * 
 * Il server accetta richieste di connessioni dai client, riceve messaggi inviati dai client e li
 * rispedisce (eventualmente aggiungendo "echoed by server" al messaggio ricevuto).
 * 
 * Il client legge il messaggio da inviare da console, lo invia al server e visualizza quanto ricevuto
 * dal server.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.Key;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/* Una volta accettata la connessione con un client, il server riceve una sequenza di messaggi aventi una
 * lunghezza massima prefissata.
 * Le operazioni di lettura e scrittura vengono effettuate in modalità non bloccante. */

public class Server
{
	public static final String configFile = "server.properties"; // path del file di configutazione
	public static int port; // porta di ascolto del server
	public static int bufSize; // dimensione in byte del buffer di risposta
	public static String exitMessage; // messaggio di terminazione (se ricevuto dal client, chiude la connessione)
	public static String echoString; // stringa da aggiungere ai messaggi di risposta inviati ai client

	public static void main(String[] args)
	{
		try
		{
			readConfig();
		}
		catch(Exception e)
		{
			System.err.println("[SERVER] Errore durante la lettura del file di configurazione");
			e.printStackTrace();
			System.exit(1);
		}

		// quindi apro il ServerSocketChannel e il selettore per ricevere e monitorare le connessioni dei client
		try(
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			Selector selector = Selector.open(); )
		{
			serverSocketChannel.bind(new InetSocketAddress(port));
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.printf("[SERVER] In ascolto su porta %d\n", port);

			/* Il server entra in un ciclo infinito nel quale:
			 * (1) attende (e accetta) richieste di connessione da parte dei client,
			 * (2) controlla, tramite il selettore, se ci sono canali pronti per essere letti o scritti. */

			while(true)
			{
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();

			

				while(iter.hasNext())
				{
					SelectionKey key = iter.next();

					/* Controllo se sul canale associato alla chiave esiste la possibilità di accettare
					 * una nuova connessione. Nel caso, la accetto e registro il canale sul selettore. */
					if(key.isAcceptable())
					{
						SocketChannel client = serverSocketChannel.accept();
						System.out.println("[SERVER] Nuova connessione ricevuta");
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_READ, new ReadState(bufSize));
					}
					else if(key.isReadable()) // sul canale ci sono dati pronti per essere letti
					{
// ATTENZIONE QUI
						/* SE NEL CLIENT FACCIO ^C, IL SERVER CONTINUA A PASSARE DA QUI INFINITE VOLTE
						 * PER COLPA DI
						 * 		if(state.count < Integer.BYTES)
						 * 			return ;
						 * IN handleRead()
						 */

						//System.out.println("[SERVER] Dati pronti per la lettura");
						handleRead(selector, key);
					}
					else if(key.isWritable()) // il canale è pronto per la scrittura, posso inviare la risposta
					{
						System.out.println("[SERVER] Dati pronti per la scrittura");
						handleWrite(selector, key);
					}
					iter.remove();
				}
			}
		}
		catch(Exception e)
		{
			System.err.println("[SERVER] Errore!");
			e.printStackTrace();
		}
	}

	public static void handleRead(Selector selector, SelectionKey key) throws IOException
	{
		SocketChannel channel = (SocketChannel) key.channel();
		ReadState state = (ReadState) key.attachment();

		// leggo dati dal canale
		state.count += channel.read(state.buffer);

		// Se non ho (ancora) letto i 4 byte della lunghezza, termino.
		if(state.count < Integer.BYTES)
			return ;
		
		/* Altrimenti controllo se ho già estratto la lunghezza del messaggio dal buffer nel caso in cui
		 * non lo abbia fatto, la memorizzo nello stato. */
		if(state.length == 0)
		{
			state.buffer.flip();
			state.length = state.buffer.getInt();
			System.out.printf("[SERVER] Ricevuta lunghezza: %d\n", state.length);
			// compact() sposta gli eventuali byte rimanenti all'inizio del buffer
			state.buffer.compact();
		}

		if(state.count < Integer.BYTES + state.length)
			return ;
		
		/* Se sono qui, invece, posso estrarre il messaggio dal buffer.
		 * Conosco già state.length poiché l'ho letta in precedenza. */
		state.buffer.flip();
		byte[] messageBytes = new byte[state.length];
		state.buffer.get(messageBytes);
		String messageString = new String(messageBytes);
		System.out.printf("[SERVER] Ricevuto messaggio: %s\n", messageString);

		/* Controllo se il messaggio che ho ricevuto corrisponde alla stringa di terminazione. In caso
		 * affermativo chiudo la connessione con il client. */
		if(messageString.equalsIgnoreCase(exitMessage))
		{
			channel.close(); // la chiusura del canale cancella anche la chiave
			System.out.println("[SERVER] Connessione con il client chiusa");
			return ;
		}

		// preparo la risposta e registro il canale per la scrittura
		ByteBuffer buffer = buildReplyBuffer(messageString);
		channel.register(selector, SelectionKey.OP_WRITE, buffer);
	}

	public static void handleWrite(Selector selector, SelectionKey key) throws IOException
	{
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		
		// provo a scriver i dati sul canale
		channel.write(buffer);

		/* Se dopo la write() i dati nel buffer non sono stati consumati completamente, allora termino
		 * e riprovo più tardi. */
		if(buffer.hasRemaining())
			return ;

		/* Altrimenti sono riuscito a mandare tutto il messaggio di risposta e posso nuovamente
		 * registrare il canale in lettura. */
		System.out.println("[SERVER] Risposta inviata");
		channel.register(selector, SelectionKey.OP_READ, new ReadState(bufSize));
	}

	/**
	 * Costruisce la stringa di risposta e la inserisce in un ByteBuffer pronto per la scrittura sul canale.
	 * @param message la stringa ricevuta dal client
	 * @return un buffer contenente la lunghezza (in byte) e i byte della stringa di risposta
	 */
	public static ByteBuffer buildReplyBuffer(String message)
	{
		String replyString = String.format("%s (%s)", message, echoString);
		byte[] replyBytes = replyString.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + replyBytes.length);

		// NOTA: la flip() rende il buffer pronto per essere scritto sul canale
		buffer.putInt(replyBytes.length).put(replyBytes).flip();
		return buffer;
	}

	/**
	 * Legge il file di configurazione del server.
	 * @throws FileNotFoundException se il file non esiste
	 * @throws IOException se si verifica un errore durante la lettura
	 */
	private static void readConfig() throws FileNotFoundException, IOException
	{
		InputStream input = new FileInputStream(configFile);
		Properties prop = new Properties();
		prop.load(input);
		port = Integer.parseInt(prop.getProperty("port"));
		bufSize = Integer.parseInt(prop.getProperty("bufSize"));
		exitMessage = prop.getProperty("exitMessage");
		echoString = prop.getProperty("echoString");
		input.close();
	}
}