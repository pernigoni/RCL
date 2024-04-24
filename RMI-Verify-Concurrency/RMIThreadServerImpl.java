import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class RMIThreadServerImpl extends UnicastRemoteObject implements RMIThreadServer
{
	private volatile int counter = 0;
	private final int MAXCOUNT = 10000;

	public RMIThreadServerImpl() throws RemoteException
	{
		super(); // QUI IL SERVER VIENE ESPORTATO

		/* Questo è un altro modo per esportare l'oggetto.
		 * 
		 * Bisogna aver definito una classe che implementi i metodi dell'interfaccia remota ed
		 * ESTENDA LA CLASSE UnicastRemoteObject.
   		 * 
		 * Il costruttore di UnicastRemoteObject:
		 * * Esporta automaticamente l'oggetto remoto.
		 * * Crea automaticamente un server socket per ricevere le invocazioni di metodi remoti.
		 * * Effettua l'overriding di alcuni metodi della classe Object (equals, toString...)
   		 *   per adattarli al comportamento degli oggetti remoti. */
	}

	public void update()
	{
		int i;
		Thread p = Thread.currentThread();
		System.out.println("Server entering critical section - " + p.getName());

		for(i = 0; i < MAXCOUNT; i++)
		{
			this.counter++;
			try
			{
				Thread.sleep(1);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		for(i = 0; i < MAXCOUNT; i++)
		{
			this.counter--;
			try
			{
				Thread.sleep(1);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		System.out.println("Server leaving critical section - " + p.getName());
	}

	public int read()
	{
		return this.counter;
	}

	public static void main(String [] args)
	{
		try
		{
			RMIThreadServerImpl localObject = new RMIThreadServerImpl();
			int port = 12345;
			LocateRegistry.createRegistry(port);
			Registry r = LocateRegistry.getRegistry(port);
			r.rebind("RMIThreadServer", localObject);
		}
		catch(RemoteException e)
		{
			System.out.println("RemoteException" + e);
		}
		catch(Exception e)
		{
			System.out.println("Exception" + e);
		}
	}
}

/*

> java RMIThreadServerImpl
Server entering critical section - RMI TCP Connection(4)-127.0.0.1
Server entering critical section - RMI TCP Connection(6)-127.0.0.1
Server leaving critical section - RMI TCP Connection(4)-127.0.0.1
Server leaving critical section - RMI TCP Connection(6)-127.0.0.1

> java RMIThreadClient
Client before critical section - 1300
Client after critical section - 287

> java RMIThreadClient
Client before critical section - 0
Client after critical section - 1583

*/
