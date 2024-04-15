import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client
{
	public static final int numPing = 10; // numero di richieste (pacchetti UDP) inviate dal client
	public static final long timeout = 8000; // tempo max di attesa prima di considerare un pacchetto come perso
	public static final int bufferSize = 1024; // dimensione del buffer usato per la ricezione

	// array di indicatori contenenti informazioni sullo stato dei pacchetti inviati
	public static PacketMonitor[] monitors = new PacketMonitor[numPing];

	// parametri usati per il calcolo delle statistiche
	public static long rttTot = 0;
	public static long rttMin = Long.MAX_VALUE;
	public static long rttMax = Long.MIN_VALUE;
	public static int numReceived = 0;

	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Usage: Client <hostname> <port>");
			System.exit(1);
		}

		String address = args[0];
		int port = Integer.parseInt(args[1]);

		// inizializzo l'array con i monitor
		for(int i = 0; i < numPing; i++)
			monitors[i] = new PacketMonitor();

		/* Inizializzo il socket per inviare le richieste.
		 * Il socket viene chiuso automaticamente all'uscita dal blocco. Di conseguenza anche il thread
		 * listener bloccato sulla receive() può terminare. */
		try(DatagramSocket socket = new DatagramSocket())
		{
			// avvio il thread listener che attende i pacchetti di risposta
			Thread listener = new Thread(new ClientListener(socket, monitors, bufferSize));
			listener.start();

			// ottengo l'indirizzo del server
			InetAddress addr = InetAddress.getByName(address);

			for(int i = 0; i < numPing; i++)
			{
				long startTime = System.currentTimeMillis();
				String contentStr = String.format("PING %d %d", i, startTime);
				byte[] content = contentStr.getBytes();
				DatagramPacket pkt = new DatagramPacket(content, content.length, addr, port);

				// invio il pacchetto
				socket.send(pkt);
				System.out.printf("[CLIENT] Inviato: id=%d\n", i);

				// attendo l'arrivo sul monitor i-esimo per un massimo di timeout millisecondi
				if(monitors[i].get(timeout))
				{
					System.out.printf("[CLIENT] Ricevuto: id=%d\n", i);
					long rtt = System.currentTimeMillis() - startTime;
					rttTot += rtt;
					rttMin = Long.min(rttMin, rtt);
					rttMax = Long.max(rttMax, rtt);
					numReceived++;
				}
				else // se sono qui vuol dire che non ho ricevuto il pacchetto
					System.out.printf("[CLIENT] Non ho ricevuto: id=%d\n", i);
			}
		}
		catch(Exception e)
		{
			System.err.printf("[CLIENT] Errore: %s\n", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		printStats();
	}

	private static void printStats()
	{
		System.out.printf(
			"---- PING Statistics ----\n%d packets transmitted, %d packets received, %d%% packet loss\n",
			numPing, numReceived, 100 * (numPing - numReceived) / numPing);
		
		if(numReceived != 0)
		{
			System.out.printf(
				"round-trip (ms) min/avg/max = %d / %.2f / %d\n",
				rttMin, ((double) rttTot / (double) numReceived), rttMax);
		}
	}
}