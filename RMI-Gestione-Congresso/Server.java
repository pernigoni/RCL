import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class Server
{
	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Usage: Server <serviceName> <port>");
			System.exit(1);
		}

		String serviceName = args[0]; // nome del servizio offerto dal server
		int port = Integer.parseInt(args[1]); // numero di porta per il registry RMI

		try
		{
			// creo e inizializzo l'oggetto remoto
			Manager manager = new Manager();

			// esporto l'oggetto, ottenendo lo stub corrispondente
			ManagerInterface stub = (ManagerInterface) UnicastRemoteObject.exportObject(manager, 0);

			// creazione di un registry sulla porta specificata
			LocateRegistry.createRegistry(port);
			Registry r = LocateRegistry.getRegistry(port);

			// pubblicazione dello stub nel registry
			r.rebind(serviceName, stub);
			System.out.printf(
				"Server pronto (nome-servizio=%s, porta-registry=%d)\n", serviceName, port);
		}
		catch(RemoteException e)
		{
			System.err.println("Errore: " + e.getMessage());
		}
	}
}