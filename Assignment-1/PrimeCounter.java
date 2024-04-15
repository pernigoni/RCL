import java.util.*;

// NON È TUTTO ORO QUEL CHE LUCCICA

public class PrimeCounter
{
	private final static int MAX = 10_000_000;

	/* Ogni thread esegue la stessa computazione.
	 * Ogni thread conta i numeri primi compresi tra 2 e MAX.
	 * Il numero di thread, tra 2 e 30, deve essere dato in input dall'utente. */

	public static void main(String[] args)
	{
		int numberOfThreads = 0;
		Scanner sc = new Scanner(System.in);
		System.out.println("Inserisci il numero di thread (da 1 a 30) ");
		numberOfThreads = sc.nextInt();
		while(numberOfThreads < 1 || numberOfThreads > 30)
		{
			if(numberOfThreads < 1 || numberOfThreads > 30)
				System.out.println("Inserisci un numero tra 1 e 30! ");
			numberOfThreads = sc.nextInt();
		}

		CountPrimesThread worker[] = new CountPrimesThread[numberOfThreads];
		for(int i = 0; i < numberOfThreads; i++)
			worker[i] = new CountPrimesThread(i, MAX);
		for(int i = 0; i < numberOfThreads; i++)
			worker[i].start();
		System.out.println("Thread creati e avviati.");
		sc.close();
	}
}