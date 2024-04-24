import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIThreadClient
{
	public static void main (String[] args)
	{
		try
		{
			RMIThreadServer remObj;
			int port = 12345;
			Registry r = LocateRegistry.getRegistry(port);
			remObj = (RMIThreadServer) r.lookup("RMIThreadServer");
			System.out.println("Client before critical section - " + remObj.read());
			remObj.update();
			System.out.println("Client after critical section - " + remObj.read());
		}
		catch(Exception e)
		{
			System.out.println("Client Exception" + e);
		}
	}
}