import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Client
{
	public static final int port = 12120; // numero di porta per il registry RMI
	public static final String serviceName = "RMIUserList"; // nome del servizio offerto dal server

	public static void main(String[] args)
	{
		// leggo il parametro (nome utente da aggiungere) da riga di comando
		if(args.length < 1)
		{
			System.err.println("Usage: Client <username>");
			System.exit(1);
		}
		String username = args[0];

		try
		{
			// ottengo un riferimento al registry
			Registry r = LocateRegistry.getRegistry(port);

			// ottengo un riferimento alla lista remota
			UserListInterface list = (UserListInterface) r.lookup(serviceName);

			// invoco il metodo remoto per aggiungere l'utente alla lista
			list.addUser(username);
			System.out.println("Aggiunto l'utente: " + username);

			// stampo la lista di utenti, uno per riga
			System.out.println("Utenti attualmente registrati: ");

			// ottengo la lista tramite invocazione del metodo remoto
			List<String> users = list.getList();
			for(String u : users)
				System.out.println("  " + u);
		}
		catch(Exception e)
		{
			System.err.println("Errore: " + e.getMessage());
		}
	}
}