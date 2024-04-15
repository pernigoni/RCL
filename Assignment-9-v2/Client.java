import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/* Client versione non interattiva: invia un messaggio letto da riga di comando, stampa quanto ricevuto dal
 * server e termina. */

/*
 * ATTENZIONE: MANCA CICLO WHILE PER CAPIRE SE È STATO LETTO TUTTO.
 */

public class Client
{
	public static final int bufSize = 2048; // dimensione del buffer di risposta

	// NON è una buona pratica lanciare le eccezioni così a livello del main, meglio un mega try-catch!
	public static void main(String[] args) throws Exception
	{
		if(args.length < 3)
		{
			System.err.println("Usage: Client <hostname> <port> <message>");
			System.exit(1);
		}

		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		byte[] message = args[2].getBytes(); // messaggio interpretato come una sequenza di byte

		// apro il SocketChannel per la comunicazione con il server
		SocketChannel sc = SocketChannel.open(new InetSocketAddress(hostname, port));

		// alloco un buffer con la dimensione fissata
		ByteBuffer buffer = ByteBuffer.allocate(bufSize);

		// preparo il buffer per la scrittura
		buffer.clear();

		// scrivo la lunghezza e poi il messaggio nel buffer
		buffer.putInt(message.length);
		buffer.put(message);

		// preparo il buffer per la lettura
		buffer.flip();

		// leggo il messaggio dal buffer e lo invio al server (lo scrivo sul canale)
		sc.write(buffer);

		// A questo punto devo attendere la risposta dal server.
		
		// preparo il buffer per la scrittura
		buffer.clear();

		// leggo il messaggio dal canale e lo scrivo nel buffer
		sc.read(buffer);

		// preparo il buffer per la lettura
		buffer.flip();

		// interpreto quello che ho ricevuto dal server
		int replyLength = buffer.getInt();
		byte[] replyBytes = new byte[replyLength];
		buffer.get(replyBytes);
		System.out.println("Ricevuto: " + new String(replyBytes));

		// chiudo il canale
		sc.close();
	}
}