import com.google.gson.*;
import java.io.*;
import java.util.*;

/*
 * Invece di passare dall'oggetto JSON all'oggetto Java, possiamo costruire una rappresentazione ad albero
 * dell'oggetto JSON (in un file .json) e poi navigarla.
 */

public class GSONComplexObject
{
	public static void main(String[] args)
	{
		File input = new File("restaurant.json");

		try
		{
			JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
			JsonObject fileObject = fileElement.getAsJsonObject(); // albero che rappresenta la struttura

			// ESTRAGGO i campi navigando con le get()

			String name = fileObject.get("name").getAsString(); // name
			System.out.println("Restaurant name: " + name);

			JsonArray jsonArrayOfItems = fileObject.get("menu").getAsJsonArray(); // menu
			List <RestaurantMenuItem> menuitems = new ArrayList <RestaurantMenuItem>();
			for(JsonElement menuElement : jsonArrayOfItems) // itero sul menu
			{
				JsonObject itemJsonObject = menuElement.getAsJsonObject();
				String desc = itemJsonObject.get("description").getAsString(); // description
				float price = itemJsonObject.get("price").getAsFloat(); // price
				RestaurantMenuItem restaurantel = new RestaurantMenuItem(desc, price);
				menuitems.add(restaurantel);
			}
			System.out.println("Items are " + menuitems);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}