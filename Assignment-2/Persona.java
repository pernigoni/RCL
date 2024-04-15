import java.util.concurrent.*;

public class Persona implements Runnable // CLIENTE
{
	public final int id; // id del cliente
	public final long minDelay = 0; // minimo intervallo di tempo per le operazioni del cliente
	public final long maxDelay = 1000; // massimo intervallo di tempo per le operazioni del cliente

	public Persona(int id)
	{
		this.id = id;
	}

	/* Metodo contenente la logica del cliente.
	 * Ogni cliente dell'ufficio genera un intervallo di tempo casuale e attende per tale numero di
	 * millisecondi prima di terminare. */
	@Override
	public void run()
	{
		// System.out.printf("Cliente %d arrivato allo sportello.\n", id);
		System.out.println("Cliente " + id + " arrivato allo sportello " + Thread.currentThread().threadId() + ".");
		long delay = ThreadLocalRandom.current().nextLong(minDelay, maxDelay);
		try 
		{
			Thread.sleep(delay);
		}
		catch(InterruptedException e)
		{
			System.err.println("Interruzione su sleep.");
			return ;
		}
	}
}