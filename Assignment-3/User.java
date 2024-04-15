import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// generico utente del laboratorio (studente, tesista, professore)

public class User implements Runnable
{
	public Categoria categoria;
	public int id;
	public int numeroAccessi; // numero di accessi previsti per l'utente
	public long workDelay; // tempo in cui l'utente utilizza il laboratorio
	public long breakDelay; // tempo che intercorre tra un accesso e l'altro
	public int maxAccessi = 5; // massimo numero di accessi
	public long maxWork = 5000; // massimo tempo di lavoro
	public long maxBreak = 2000; // massimo tempo di pausa
	private Laboratorio lab; // riferimento al laboratorio

	public User(Categoria categoria, int id, Laboratorio lab)
	{
		this.categoria = categoria;
		this.id = id;
		this.lab = lab;
		numeroAccessi = ThreadLocalRandom.current().nextInt(1, maxAccessi + 1);
		workDelay = ThreadLocalRandom.current().nextLong(maxWork + 1);
		breakDelay = ThreadLocalRandom.current().nextLong(maxBreak + 1);
	}

	/* Tutti gli utenti richiedono l'accesso al laboratorio, lo utilizzano per un certo intervallo di
	 * tempo e poi escono per fare una pausa. Il tutto viene ripetuto per numeroAccessi volte. */
	public void run()
	{
		try
		{
			for(int i = 0; i < numeroAccessi; i++)
			{
				List<Integer> assegnati = lab.entrata(this);
				Thread.sleep(workDelay);
				lab.uscita(this, assegnati);
				Thread.sleep(breakDelay);
			}
		}
		catch(InterruptedException e)
		{
			System.out.printf("%s con id=%d interrotto.\n", categoria.name(), id);
			return ;
		}

		System.out.printf("%s con id=%d ha terminato il lavoro.\n", categoria.name(), id);
	}
}