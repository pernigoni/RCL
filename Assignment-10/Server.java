import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
	public static int port; // porta su cui attendere l'arrivo dei pacchetti
	public static long seed; // parametro per il generatore pseudocasuale
	public static final int bufferSize = 1024; // dimensione del buffer per ricezione e invio dei pacchetti
	public static final int maxDelay = 5000; // tempo massimo di attesa (in ms) prima di inviare una risposta
	
	// threadpool per servire le richieste in arrivo
	public static ExecutorService pool = Executors.newCachedThreadPool();

	public static void main(final String[] array)
	{
		if(array.length < 1)
		{
			System.err.println("Usage: Server <porta> [seed]");
			System.exit(1);
		}

		port = Integer.parseInt(array[0]);
		if(array.length > 1)
			seed = Long.parseLong(array[1]);
		else
			seed = System.currentTimeMillis();

		// apro il socket per la comunicazione
		try(DatagramSocket socket = new DatagramSocket(port))
		{
			// inizializzo il generatore di numeri pseudocasuali usando il seed
			Random random = new Random(seed);
			System.out.printf("[SERVER] pronto sulla porta %s\n", port);

			// ciclo infinito in cui ricevo un pacchetto dal client e lo gestisco in un thread apposito
			while(true)
			{
				byte[] buffer = new byte[bufferSize];
				DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
				socket.receive(pkt);
				pool.execute(new ServerWorker(socket, pkt, random.nextLong(), bufferSize, maxDelay));
			}
		}
		catch(Exception e)
		{
			System.err.printf("[SERVER] Errore: %s\n", e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			pool.shutdown();
		}
	}
}