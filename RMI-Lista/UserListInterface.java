import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// operazioni che il server espone al client

public interface UserListInterface extends Remote
{
	/**
	 * Aggiunge un utente alla lista.
	 * @param username
	 * @throws RemoteException
	 */
	public void addUser(String username) throws RemoteException;

	/**
	 * Restituisce la lista degli utenti attualmente registrati.
	 * @return lista degli utenti registrati
	 * @throws RemoteException
	 */
	public List<String> getList() throws RemoteException;
}