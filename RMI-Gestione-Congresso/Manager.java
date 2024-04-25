import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Manager implements ManagerInterface
{
	public final int numDays = 3; // numero di giorni previsti per l'evento
	public final int numSessions = 12; // numero di sessioni per ogni giorno
	public final int numSpeakers = 5; // numero di speaker per ogni giorno

	/* Lista di liste per memorizzare i partecipanti al congresso.
	 * Ogni lista interna rappresenta una sessione. */
	private List<List<String>> congress;

	/**
	 * Costruttore della classe Manager che inizializza la struttura dati per la memorizzazione delle liste.
	 */
	public Manager()
	{
		congress = new ArrayList<>();
		for(int i = 0; i < (numDays * numSessions); i++)
			congress.add(new ArrayList<String>());

		/* 3 GIORNATE DA 12 SESSIONI
		 * 
		 * Giorno-1:  S0 ... S11
		 * Giorno-2: S12 ... S23
		 * Giorno-3: S24 ... S35
		 */
	}

	public void addSpeaker(int sessionId, String name) throws RemoteException
	{
		// controllo la validità della sessione
		if(0 <= sessionId && sessionId <= (congress.size() - 1))
		{
			// se lo slot è disponibile, registro lo speaker
			List<String> session = congress.get(sessionId);
			if(session.size() < numSpeakers)
			{
				session.add(name);
				return;
			}

			// altrimenti sollevo un'eccezione per notificare che lo slot è già occupato da un altro speaker
			throw new RemoteException("Sessione piena");
		}

		// se sono qui, il numero di sessione indicato non è valido
		throw new RemoteException("Sessione non valida");
	}

	public List<String> getSpeakers(int sessionId) throws RemoteException
	{
		// controllo se l'identificativo della sessione è valido
		if(0 <= sessionId && sessionId <= (congress.size() - 1))
			return congress.get(sessionId);

		// se non lo è, sollevo un'eccezione
		throw new RemoteException("Sessione non valida");
	}
}