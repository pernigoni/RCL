import java.io.*;
import java.util.concurrent.*;
import java.util.Map.Entry;

// CONTEGGIO OCCORRENZE

public class Main
{
	public static final int N_THREADS = 8; // numero di thread del pool
	public static ExecutorService pool = Executors.newFixedThreadPool(N_THREADS); // pool di dim. fissa
	public static final long poolTerminationDelay = 10000; // tempo massimo di attesa per la terminazione del pool

	// ConcurrentHashMap usata per registrare i contatori delle occorrenze
	public static ConcurrentHashMap<Character, Integer> counters = new ConcurrentHashMap<>();

	public static void main(String[] args)
	{
		// almeno due parametri in input: almeno un path di file di input e il path del file di output
		if(args.length < 2)
		{
			System.err.println("Usage: <inputFile1> ... <inputFileN> <outputFile>");
			System.exit(1);
		}

		for(int i = 0; i < args.length - 1; i++) // per ogni file di input
		{
			String inputFile = args[i];
			// apro il file e controllo che esista
			File file = new File(inputFile);
			if(!file.exists() || file.isDirectory())
			{
				System.err.printf("Error: %s is not a valid file!\n", inputFile);
				continue;
			}

			// creo un nuovo runnable e lo invio al pool
			pool.submit(new Counter(counters, file));
		}

		// terminazione del pool
		pool.shutdown();
		try
		{
			if(!pool.awaitTermination(poolTerminationDelay, TimeUnit.MILLISECONDS))
				pool.shutdownNow();
		}
		catch(Exception e)
		{
			System.err.println("Error: could not close thread pool!");
			System.exit(1);
		}

		// scrittura dei risultati sul file di output
		String outputFile = args[args.length - 1];
		try(PrintWriter out = new PrintWriter(outputFile))
		{
			// iteriamo su tutte le entry della ConcurrentHashMap
			for(Entry<Character, Integer> entry : counters.entrySet())
				out.printf("%c,%d\n", entry.getKey(), entry.getValue());
		}
		catch(Exception e)
		{
			System.err.println("Error: could not write output file!");
			System.exit(1);
		}
	}
}