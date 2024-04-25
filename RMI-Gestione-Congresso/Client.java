import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

/* Il client RMI è implementato in maniera interattiva.
 * 
 * Ci sono tre comandi possibili:
 * (1) 'exit' per far terminare il programma.
 * (2) 'add <sessionId> <speaker>' per aggiungere un utente a una sessione.
 * (3) 'show <sessionId>' per visualizzare i partecipanti a una sessione. */

public class Client
{
	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Usage: Client <serviceName> <port>");
			System.exit(1);
		}

		String serviceName = args[0]; // nome del servizio offerto dal server
		int port = Integer.parseInt(args[1]); // numero di porta per il registry RMI

		try
		{
			// ottengo un riferimento al registry
			Registry r = LocateRegistry.getRegistry(port);

			// ottengo un riferimento alla lista remota
			ManagerInterface m = (ManagerInterface) r.lookup(serviceName);

			Scanner inputScanner = new Scanner(System.in);
			while(true)
			{
				System.out.println("Inserisci un comando:");

				// leggo l'input dell'utente
				String line = inputScanner.nextLine();

				// se il comando dato è 'exit', allora termino
				if(line.equals("exit"))
					break;

				String[] parts = line.split(" ");

				// interpreto il comando 'add <sessionId> <speaker>'
				if(parts[0].equals("add") && parts.length == 3)
				{
					int sessionId = Integer.parseInt(parts[1]);
					try
					{
						m.addSpeaker(sessionId, parts[2]);
						System.out.printf("Aggiunto l'utente %s nella sessione %d\n", parts[2], sessionId);
					}
					catch(RemoteException e)
					{
						System.err.println("Errore lato server: " + e.getMessage());
					}
				}
				// interpreto il comando 'show <sessionId>'
				else if(parts[0].equals("show") && parts.length == 2)
				{
					int sessionId = Integer.parseInt(parts[1]);
					try
					{
						List<String> speakers = m.getSpeakers(sessionId);
						System.out.printf("Sessione %d: ", sessionId);
						for(String s : speakers)
							System.out.printf("%s ", s);
						System.out.print("\n");
					}
					catch(RemoteException e)
					{
						System.err.println("Errore lato server: " + e.getMessage());
					}
				}
				// comando non riconosciuto oppure comando con sintassi non corretta
				else
					System.err.println("Errore: sintassi del comando non valida");
			}
		}
		catch(Exception e)
		{
			System.err.println("Errore: " + e.getMessage());
		}
	}
}