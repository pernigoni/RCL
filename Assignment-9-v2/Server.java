import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/*
 * ATTENZIONE: MANCA CICLO WHILE PER CAPIRE SE È STATO LETTO TUTTO.
 */

public class Server
{
	public static final int bufSize = 2048; // dimensione del buffer di risposta

	// NON è una buona pratica lanciare le eccezioni così a livello del main, meglio un mega try-catch!
	public static void main(String[] args) throws IOException
	{
		if(args.length < 1)
		{
			System.err.println("Usage: Server <port>");
			System.exit(1);
		}

		int port = Integer.parseInt(args[0]);

		// apro il selettore e inizializzo il canale relativo al ServerSocket
		Selector selector = Selector.open();
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.bind(new InetSocketAddress("localhost", port));
		serverSocket.configureBlocking(false);
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);

		// alloco un buffer avente la dimensione fissata
		ByteBuffer buffer = ByteBuffer.allocate(bufSize);
		System.out.println("Server pronto su porta " + port);
		while(true)
		{
			selector.select();
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iter = selectedKeys.iterator();
			while(iter.hasNext())
			{
				SelectionKey key = iter.next();

				// controllo se sul canale associato alla chiave è possibile accettare una nuova connessione
				if(key.isAcceptable())
				{
					// accetto la connessione e registro il canale ottenuto sul selettore
					SocketChannel client = serverSocket.accept();
					System.out.println("Nuova connessione ricevuta");
					client.configureBlocking(false);
					client.register(selector, SelectionKey.OP_READ);
				}

				// se il canale associato alla chiave è leggibile, allora procedo con l'invio del messaggio di risposta
				if(key.isReadable())
					sendEcho(key, buffer);
				
				iter.remove();
			}
		}
	}

	private static void sendEcho(SelectionKey key, ByteBuffer buffer) throws IOException
	{
		SocketChannel client = (SocketChannel) key.channel();

		// leggo i dati dal canale e li scrivo nel buffer
		buffer.clear();
		client.read(buffer);
		buffer.flip();

		// estraggo il contenuto del buffer (lunghezza messaggio, messaggio)
		int receivedLength = buffer.getInt();
		byte[] receivedBytes = new byte[receivedLength];
		buffer.get(receivedBytes);
		String receivedStr = new String(receivedBytes);
		System.out.println("Ricevuto: " + receivedStr);

		// preparo il messaggio di risposta
		String replyStr = new String(receivedStr + " (echoed by server)");
		byte[] replyBytes = replyStr.getBytes();

		// preparo il buffer per la scrittura
		buffer.clear();

		// scrivo nel buffer la lunghezza della risposta e poi la risposta
		buffer.putInt(replyBytes.length);
		buffer.put(replyBytes);

		// preparo il buffer per la lettura
		buffer.flip();

		// quindi scrivo il contenuto del buffer nel canale
		client.write(buffer);

		// chiudo la connessione con il client
		client.close();
	}
}