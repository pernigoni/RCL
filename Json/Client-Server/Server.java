import com.google.gson.*;
import com.google.gson.reflect.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;

public class Server
{
	// Ricezione di una singola entità

	public static void main(String[] args)
	{
		if(args.length != 1)
			return ;
		int port = Integer.parseInt(args[0]);

		try(ServerSocket s = new ServerSocket(port))
		{
			DataInputStream is = new DataInputStream(s.accept().getInputStream());
			System.out.println("Accettato");

			String json = is.readUTF();
			
			Gson gson = new Gson();
			Type restaurantType = new TypeToken<RestaurantWithMenu>() {}.getType();
			RestaurantWithMenu rm = gson.fromJson(json, restaurantType);
			System.out.println(rm.name + " " + rm.menu.toString());
		}
		catch(Exception e) 
		{ }
	}
}
