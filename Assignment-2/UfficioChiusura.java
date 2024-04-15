import java.util.concurrent.*;

// SIMULAZIONE UFFICIO POSTALE

/* Versione che prevede (1) la chiusura attiva degli sportelli in caso di inattività,
 * (2) attesa attiva degli utenti che devono entrare nella seconda stanza. */

public class UfficioChiusura
{
	// queuePrimaSala --> coda illimitata della prima sala (gestita esplicitamente del programma)
	// queueSportelli --> coda limitata della seconda sala davanti agli sportelli (implicita del pool)

	public static final int numeroSportelli = 4; // numero di sportelli dell'ufficio
	public static final int numeroClienti = 500; // numero di clienti da far entrare nell'ufficio
	public static final int dimQueueSportelli = 10; // dimensione coda davanti agli sportelli
	// tempo di attesa per ritentare di entrare nella seconda stanza se la coda sportelli è piena
	public static final long queueDelay = 500;
	public static final long terminationDelay = 5_000; // tempo di attesa per la terminazione del pool
	public static final long closingDelay = 60_000; // tempo di inattività prima della chiusura di uno sportello

	public static void main(String[] args)
	{
		int count = 0; // contatore delle persone servite
		System.out.println("Ufficio aperto!");

		// creazione coda per la prima sala (no limiti di dimensione)
		BlockingQueue<Runnable> queuePrimaSala = new LinkedBlockingQueue<Runnable>();

		// creazione pool di thread personalizzato usando AbortPolicy come politica di rifiuto
		// (viene sollevata una RejectedExecutionException quando la coda del pool è piena)
		ThreadPoolExecutor pool = new ThreadPoolExecutor(
			numeroSportelli,
			numeroSportelli,
			closingDelay,
			TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(dimQueueSportelli),
			new ThreadPoolExecutor.AbortPolicy());

		pool.allowCoreThreadTimeOut(true); // impostata la possibilità di chiusura degli sportelli

		// entrare nella prima sala
		for(int i = 0; i < numeroClienti; i++)
			queuePrimaSala.add(new Persona(i));

		// entrare nella seconda sala
		while(!queuePrimaSala.isEmpty())
		{
			Persona p = (Persona) queuePrimaSala.peek(); // recupera ma non rimuove la testa della coda
			try
			{
				pool.execute(p);
				queuePrimaSala.poll(); // recupera e rimuove la testa della cosa
				count++;
			}
			catch(RejectedExecutionException e)
			{
				/* Se entro qui significa che la coda davanti agli sportelli (ovvero la coda del pool) è
				 * piena. Aspetto un certo intervallo di tempo affinché si svuoti e poi ritendo. */
				
				System.out.printf("Coda sportelli piena. Il cliente con id=%d Resta in attesa.\n", p.id);
				try
				{
					Thread.sleep(queueDelay);
				}
				catch(InterruptedException e2)
				{
					System.err.println("Interruzione su sleep.");
				}
			}
		}

		/* A questo punto si chiude l'ufficio:
		 * 1) si attende un certo intervallo di tempo affinché tutti i thread possano terminare;
		 * 2) passato l'intervallo, l'esecuzione del pool viene interrotta immediatamente. */

		pool.shutdown(); // i precedenti task sottomessi vengono eseguiti, ma nessun nuovo task sarà accettato
		try
		{
			if(!pool.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS))
				pool.shutdownNow();
		}
		catch(InterruptedException e)
		{
			pool.shutdownNow();
		}

		System.out.printf("Ufficio chiuso. Persone servite: %d\n", count);
	}
}