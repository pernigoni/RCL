import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.*;

public class Server
{
	public static final int port = 12120; // numero di porta per il registry RMI
	public static final String serviceName = "RMIUserList"; // nome del servizio offerto dal server

	public static void main(String[] args)
	{
		try
		{
			// creo e inizializzo la lista degli utenti
			UserList list = new UserList();

			// esporto l'oggetto ottenendo lo stub corrispondente
			UserListInterface stub = (UserListInterface) UnicastRemoteObject.exportObject(list, 0);

			// creazione di un registry sulla porta specificata
			LocateRegistry.createRegistry(port);
			Registry r = LocateRegistry.getRegistry(port);

			// pubblicazione dello stub nel registry
			r.rebind(serviceName, stub);
			System.out.printf("Server pronto (nome-servizio=%s porta-registry=%d)\n", serviceName, port);
		}
		catch(RemoteException e)
		{
			System.err.println("Errore: " + e.getMessage());
		}

		/* Qui il server termina ma resta in esecuzione il thread di RMI che ascolta le richieste, quindi il
		 * programma non termina. */
	}
}