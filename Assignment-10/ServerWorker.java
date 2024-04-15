import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

public class ServerWorker implements Runnable
{
	public final int bufferSize; // dimensione (in byte) del buffer usato per l'invio dei pacchetti
	public final int maxDelay; // tempo massimo di attesa (in ms) prima di inviare una risposta
	private DatagramSocket socket; // socket per comunicare con il client
	private DatagramPacket packet; // pacchetto ricevuto dal client
	private Random gen; // generatore di numeri pseudocasuali

	public ServerWorker(DatagramSocket socket, DatagramPacket packet, long seed, int bufferSize, int maxDelay)
	{
		this.socket = socket;
		this.packet = packet;
		this.gen = new Random(seed);
		this.bufferSize = bufferSize;
		this.maxDelay = maxDelay;
	}

	public void run()
	{
		// indirizzo e porta del client da aggiungere alle stampe
		String ID_CLIENT = packet.getAddress().toString() + ":" + packet.getPort();

		// estraggo l'identificativo assocciato al pacchetto ricevuto
		int id = Integer.parseInt(new String(packet.getData()).split(" ")[1]);
		System.out.printf("[WORKER] Ricevuto: id=%d,\t\tclient %s\n", id, ID_CLIENT);

		/* Decido se scartare o tenere il pacchetto.
		 * Genero un intero nell'intervallo [1,100], se è minore o uguale a 25 lo scarto. */
		int r = gen.nextInt(100);
		// System.out.println(r);
		if(r + 1 <= 25)
		{
			System.out.printf("[WORKER] Scartato: id=%d,\t\tclient %s\n", id, ID_CLIENT);
			return ;
		}

		/* Decido quanto attendere prima di inviare il pacchetto di risposta al client.
		 * Scelgo un tempo casuale nell'intervallo [0,maxDelay]. */
		long delay = gen.nextInt(maxDelay + 1);
		try
		{
			Thread.sleep(delay);
		}
		catch(InterruptedException e)
		{
			System.err.println("[WORKER] Interruzione durante l'attesa");
			return ;
		}

		// preparo il pacchetto di risposta e lo invio
		long timestamp = System.currentTimeMillis();
		byte[] content = String.format("PONG %d %d", id, timestamp).getBytes();
		DatagramPacket reply = new DatagramPacket(content, content.length, packet.getAddress(), packet.getPort());
		try
		{
			socket.send(reply);
			System.out.printf("[WORKER] Inviato: id=%d dopo %d ms,\tclient %s\n", id, delay, ID_CLIENT);
		}
		catch(IOException e)
		{
			System.err.printf("[WORKER] Errore di I/O: %s\n", e.getMessage());
			e.printStackTrace();
		}
	}
}