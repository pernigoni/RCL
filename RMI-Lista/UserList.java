import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class UserList implements UserListInterface
{
	private List<String> users = new ArrayList<String>(); // lista degli utenti implementata con un ArrayList

	public void addUser(String username) throws RemoteException
	{
		users.add(username);
	}

	public List<String> getList() throws RemoteException
	{
		return users;
	}
}