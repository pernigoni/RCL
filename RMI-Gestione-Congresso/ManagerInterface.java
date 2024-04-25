import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ManagerInterface extends Remote
{
	/**
	 * Metodo per registrare uno speaker nel congresso.
	 * @param sessionId identificativo della sessione
	 * @param name nome dello speaker
	 */
	public void addSpeaker(int sessionId, String name) throws RemoteException;

	/**
	 * Metodo che restituisce la lista degli speaker per una sessione.
	 * @param sessionId identificativo della sessione
	 * @return la lista degli speaker per quella sessione
	 */
	public List<String> getSpeakers(int sessionId) throws RemoteException;
}