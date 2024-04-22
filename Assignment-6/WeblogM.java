import java.io.*;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// ANALISI DI UN WEBLOG (VERSIONE CON THREAD POOL)

// ci mette circa 30 secondi con maxBatchSize=10

public class WeblogM
{
	public static final int cachingTime = 600; // tempo di permanenza in cache
	public static final String stringCachingTime = String.format("%s", cachingTime);

	// coda con priorità in cui vengono memorizzate le righe tradotte
	public static BlockingQueue<Element> outputQueue = new PriorityBlockingQueue<>();
	/* Alla PriorityBlockingQueue possiamo passare degli oggetti che implementano Comparable.
	 * Nel nostro caso abbiamo un Comparable su intero (id delle righe). Questo fa in modo che quando poi
	 * li andiamo a prendere per scriverli nel file di output siamo sicuri che siano nello stesso ordine
	 * del file di input, ovvero crescente sugli id come specificato in compareTo(). */

	public static ExecutorService pool = Executors.newCachedThreadPool(); // thread pool
	public static final int maxDelay = 60000; // tempo massimo di attesa per la terminazione del pool

	public static void main(String[] args)
	{
		if(args.length < 3)
		{
			System.err.println("Usage: WeblogM <inputfile> <outputfile> <maxBatchSize>");
			System.exit(1);
		}

		int maxBatchSize = Integer.parseInt(args[2]), numLines = 0;

		// imposto il tempo di permanenza in cache degli indirizzi tradotti
		Security.setProperty("networkaddress.cache.ttl", stringCachingTime);
		long start = System.currentTimeMillis();

		// apro i file di input e di output
		try(
			BufferedReader in = new BufferedReader(new FileReader(args[0]));
			PrintWriter out = new PrintWriter(args[1]); )
		{
			/* Leggo il file di input riga per riga.
			 * Creo gruppi di 'batchSize' righe e per ciascun gruppo attivo un Consumer che traduce. */
			String line = null;
			List<Element> batch = new ArrayList<>();

			while((line = in.readLine()) != null)
			{
				// se la lista ha raggiunto la capacità massima, la passo al Consumer e la re-inizializzo
				if(batch.size() == maxBatchSize)
				{
					pool.execute(new Consumer(batch, outputQueue));
					batch = new ArrayList<>();
				}
			
				batch.add(new Element(numLines, line)); // aggiungo la nuova riga alla lista
				numLines++;
			}

			awaitPoolTermination(); // attendo la terminazione del pool

			/* Scrivo sul file di output le righe.
			 * Per ottenere le righe nell'ordine corretto, chiamo ripetutamente il metodo take() che
			 * aspetta finché non c'è un elemento in testa da prendere, a differenza di poll() che ha
			 * un timeout. */
			while(outputQueue.size() > 0)
			{
				Element element = outputQueue.take();
				out.print(element.line);
			}
		}
		catch(Exception e)
		{
			System.err.printf("Error: %s\n", e.getMessage());
		}

		// stampo il numero di righe lette e il tempo impiegato (in ms)
		long end = System.currentTimeMillis();
		System.out.printf("N. of lines\t: %d\nElapsed time\t: %d ms\n", numLines, end - start);
	}

	// avvio della procedura di terminazione del pool
	public static void awaitPoolTermination()
	{
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
	}
}