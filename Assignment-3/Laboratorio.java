import java.util.*;

// SIMULAZIONE LABORATORIO DI INFORMATICA

public class Laboratorio
{
	public final int numeroComputer = 20; // numero di computer nel laboratorio
	public final int idComputerTesisti = 19; // id del computer richiesto dai tesisti

	private List<Thread> thread; // lista in cui vengono memorizzati i riferimenti ai thread
	private boolean computer[]; // l'elemento i-esimo è false sse il computer è libero
	private int profWaiting = 0; // numero di professori in attesa di entrare nel laboratorio
	private int tesiWaiting = 0; // numero di tesisti in attesa di entrare nel laboratorio

	// il costruttore inizializza le strutture dati
	public Laboratorio()
	{
		thread = new ArrayList<>();
		computer = new boolean[numeroComputer];
	}

	public void start(int numProf, int numTesisti, int numStudenti)
	{
		System.out.println("Laboratorio aperto.");

		// creo gli utenti, un thread per ognuno di essi
		for(int i = 0; i < numProf; i++)
			thread.add(new Thread(new User(Categoria.PROFESSORE, i, this)));
		for(int i = 0; i < numTesisti; i++)
			thread.add(new Thread(new User(Categoria.TESISTA, i, this)));
		for(int i = 0; i < numStudenti; i++)
			thread.add(new Thread(new User(Categoria.STUDENTE, i, this)));

		// simulo l'arrivo degli utenti in ordine casuale eseguendo uno shuffle della lista di thread
		Collections.shuffle(thread, new Random(System.currentTimeMillis()));

		for(Thread t : thread)
			t.start(); // mette il thread in stato di pronto

		// si attende la terminazione di tutti i thread usando la join
		for(Thread t : thread)
			try
			{
				t.join();
			}
			catch(InterruptedException e)
			{
				System.err.println("Interruzione durante la join dei thread!");
			}
		System.out.println("Laboratorio chiuso.");
	}

	public synchronized List<Integer> entrata(User u) throws InterruptedException
	{
		List<Integer> assegnati = new ArrayList<>();
		System.out.printf("%s con id=%d in attesa di entrare.\n", u.categoria.name(), u.id);

		/* I professori hanno priorità su tutti, i tesisti hanno priorità sugli studenti.
		 * Nessuno può essere interrotto mentre sta usando un computer. */
		switch(u.categoria)
		{
			// i professori attendono finché tutti i computer non sono disponibili e occupano tutto il lab
			case PROFESSORE:
				profWaiting++;
				while(!libero())
					wait();
				profWaiting--;
				for(int i = 0; i < computer.length; i++)
				{
					computer[i] = true;
					assegnati.add(i);
				}
				break;
			// i tesisti occupano sempre uno specifico computer
			case TESISTA:
				tesiWaiting++;
				while(profWaiting > 0 || computer[idComputerTesisti])
					wait();
				tesiWaiting--;
				computer[idComputerTesisti] = true;
				assegnati.add(idComputerTesisti);
				break;
			// gli studenti occupano il primo computer libero
			case STUDENTE:
				int id = primoComputerLibero();
				/* Lo studente attende finché ci sono professori che stanno aspettando, oppure se non ci
				 * sono computer disponibili, oppure se il computer assegnato è quello dei tesisti e ci 
				 * sono già tesisti prenotati per l'entrata. */
				while(profWaiting > 0 || id == -1 || (tesiWaiting > 0 && id == idComputerTesisti))
				{
					wait();
					id = primoComputerLibero();
				}
				computer[id] = true;
				assegnati.add(id);
				break;
			default:
				break;
		}
		System.out.printf("%s con id=%d entrato.\n", u.categoria.name(), u.id);
		return assegnati;
	}

	// metodo invocato dall'utente all'uscita del laboratorio
	public synchronized void uscita(User u, List<Integer> occupati)
	{
		for(Integer id : occupati) // si liberano tutti i computer occupati
			computer[id] = false;
		
		/* Risveglio tutti gli utenti in attesa.
		 * N.B. al risveglio ogni utente controlla la validità della sua condizione di attesa. */
		notifyAll();
		System.out.printf("%s con id=%d uscito.\n", u.categoria.name(), u.id);
	}

	// FUNZIONI DI UTILITÀ

	// restituisce true sse tutto il laboratorio è libero
	private boolean libero()
	{
		for(int i = 0; i < computer.length; i++)
			if(computer[i])
				return false;
		return true;
	}

	// restituisce l'id del primo computer libreo
	private int primoComputerLibero()
	{
		for(int i = 0; i < computer.length; i++)
			if(!computer[i])
				return i;
		return -1;
	}

	public static void main(String args[])
	{
		if(args.length < 3)
		{
			System.err.println("usage: Laboratorio " + "<numProf> <numTesisti> <numStudenti>");
			System.exit(1);
		}
		
		int numProf = Integer.parseInt(args[0]),
			numTesisti = Integer.parseInt(args[1]),
			numStudenti = Integer.parseInt(args[2]);
		
		// creo il laboratorio e faccio entrare gli utenti
		Laboratorio lab = new Laboratorio();
		lab.start(numProf, numTesisti, numStudenti);
	}
}