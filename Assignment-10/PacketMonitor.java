/*
La classe PacketMonitor rappresenta lo stato corrente della ricezione di un determinato pacchetto.
La classe contiene una variabile booleana che vale true se e solo se il pacchetto è stato ricevuto.
La classe contiene inoltre due metodi synchronized, denominati rispettivamente set() e get().
Il primo viene invocato dal thread ClientListener per settare a true la variabile indicatrice e segnalare
l'arrivo di un pacchetto.
Il secondo invece viene eseguito dal thread main della classe Client per attendere l'arrivo del pacchetto.
I due metodi usano i meccanismi di wait() e notify() invocati sull'oggetto corrente per sospendere e
risvegliare i thread. In particolare, la wait() che usiamo in questa soluzione è una variante di quella
vista a lezione, nel senso che prevede un ulteriore parametro, detto timeout, che consente di specificare
il tempo massimo di attesa. In questo modo, il thread che esegue la wait() rimane quindi sospeso finché un
altro thread non esegue la notify() oppure fino allo scadere del timeout.
*/

public class PacketMonitor
{
	boolean received = false; // vale true sse il pacchetto è stato ricevuto

	/**
	 * Imposta a true il flag per l'arrivo del pacchetto, risvegliando anche eventuali thread in attesa.
	 */
	public synchronized void set()
	{
		received = true;
		notify();
	}

	/**
	 * Metodo bloccante che restituisce il valore del flag per l'arrivo del pacchetto.
	 * Il thread che lo invoca resta sospeso al più per timeout millisecondi.
	 */
	public synchronized boolean get(long timeout) throws InterruptedException
	{
		long expiration = System.currentTimeMillis() + timeout;

		while(!received)
		{
			// controllo quanto mi rimane da aspettare
			long remaining = expiration - System.currentTimeMillis();

			if(remaining <= 0) // tempo scaduto
				break;

			/* Altrimenti mi metto in attesa sul monitor per il tempo che mi rimane usando una wait() con
			 * timeout.
			 * Come al solito, se un thread mi risveglia con una notify(), controllo la guardia del while
			 * e verifico se è arrivato il pacchetto che aspettavo. In tal caso posso terminare. */
			wait(remaining);
		}
		return received;
	}
}