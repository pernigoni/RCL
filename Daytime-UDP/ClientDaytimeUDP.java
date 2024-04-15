import java.io.*;
import java.net.*;

public class ClientDaytimeUDP
{
	// Daytime Protocol RFC-867

	private final static int PORT = 13;
	// private final static String HOSTNAME = "test.rebex.net";

	public static void main(String[] args)
	{
		if(args.length > 1)
		{
			System.out.println("Usage: ClientDaytimeUDP [hostname]");
			System.exit(1);
		}

		String HOSTNAME;
		if(args.length == 1)
			HOSTNAME = args[0]; // "localhost"
		else // args.length == 0
			HOSTNAME = "test.rebex.net";

		// apro il socket (se metto porta 0 il sistema sceglie una porta libera effimera)
		try(DatagramSocket socket = new DatagramSocket(0))
		{
			// imposto un timeout sul socket, opzionale ma consigliato
			socket.setSoTimeout(15000);

			// costruisco due pacchetti: uno per inviare la richiesta al server, uno per ricevere la risposta
			InetAddress host = InetAddress.getByName(HOSTNAME);
			DatagramPacket request = new DatagramPacket(new byte[1], 1, host, PORT);
			DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

			// mando la richiesta e aspetto la risposta
			socket.send(request);
			socket.receive(response);
			/* La receive() è bloccante ma posso settare un timeout specificando il tempo massimo che
			 * voglio attendere, dopo di cui vado avanti.
			 * 
			 * Si può anche intercettare la SocketTimeoutException() che viene sollevata quando sto
			 * bloccato sulla receive() e scatta il timeout. */

			// estraggo i byte dalla risposta e li converto in String
			String daytime = new String(response.getData(), 0, response.getLength(), "US-ASCII");
			System.out.println(daytime);
			/* Con getData() prendo i dati dal DatagramPacket. Li prendo a partire dal displacement 0 per
			 * una lunghezza pari a response.getLength().
			 * 
			 * Qui è facile perché il costruttore String(), dato un array di byte, costruisce automaticamente
			 * la stringa che è rappresentata da quei byte.
			 * Mandare una stringa è facile, mandare altri oggetti più complicati no. */
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

/*
	> java ClientDaytimeUDP
	Fri, 05 Apr 2024 10:01:48 GMT

	> java ClientDaytimeUDP localhost
	Fri Apr 05 12:01:54 CEST 2024
 */