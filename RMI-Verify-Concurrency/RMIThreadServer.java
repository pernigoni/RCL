import java.rmi.*;

public interface RMIThreadServer extends Remote
{
	public void update() throws RemoteException;

	public int read() throws RemoteException;
}