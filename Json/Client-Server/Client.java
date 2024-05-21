import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
	// Invio di una singola entità

	public static void main(String[] args)
	{
		if(args.length != 2)
			return ;
		String host = args[0];
		int port = Integer.parseInt(args[1]);

		DataOutputStream os;
		try(Socket s = new Socket(host, port))
		{
			os = new DataOutputStream(s.getOutputStream());
			List<RestaurantMenuItem> menu = new ArrayList<>();
			
			menu.add(new RestaurantMenuItem("Spaghetti", 9.99f));
			menu.add(new RestaurantMenuItem("Steak", 14.99f));
			menu.add(new RestaurantMenuItem("Salad", 6.99f));
			
			RestaurantWithMenu restaurant = new RestaurantWithMenu("AllWhatYouCanEat", menu);
	
			Gson gson = new Gson();
			// invio oggetto JSON sullo stream (una stringa)
			String restaurantJson = gson.toJson(restaurant);
			os.writeUTF(restaurantJson);
		}
		catch(Exception e)
		{ }
	}
}
