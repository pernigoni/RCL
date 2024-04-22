import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/* Classe che implementa l'handler di terminazione per il server.
 * 
 * Questo thread viene avviato al momento della pressione dei tasti Ctrl+C.
 * Lo scopo è quello di far terminare il main del server bloccato sulla accept() in attesa di nuove
 * connessioni e chiudere il pool di thread. */

public class TerminationHandler extends Thread
{
	private int maxDelay;
	private ExecutorService pool;
	private ServerSocket serverSocket;

	public TerminationHandler(int maxDelay, ExecutorService pool, ServerSocket serverSocket)
	{
		this.maxDelay = maxDelay;
		this.pool = pool;
		this.serverSocket = serverSocket;
	}

	public void run()
	{
		// avvio la procedura di terminazione del server
		System.out.println("[SERVER] Avvio terminazione...");

		// chiudo il ServerSocket in modo tale da non accettare più nuove richieste
		try
		{
			serverSocket.close();
		}
		catch(IOException e)
		{
			System.err.printf("[SERVER] Errore: %s\n", e.getMessage());
		}

		// faccio terminare il pool di thread
		pool.shutdown();
		try
		{
			if(!pool.awaitTermination(maxDelay, TimeUnit.MILLISECONDS)) 
				pool.shutdownNow();
		}
		catch(InterruptedException e)
		{
			pool.shutdownNow();
		}

		System.out.println("[SERVER] Terminato");
	}
}