import com.google.gson.*;
import com.google.gson.reflect.*;
import java.lang.reflect.*;

public class RestaurantReflection
{
	public static void main(String[] args)
	{
		try
		{
			String jsonRestaurant =
				"{\"name\":\"AllWhatYouCanEat\",\"menu\":"
				+ "[{\"description\":\"Spaghetti\",\"price\":9.99},"
				+ "{\"description\":\"Steak\",\"price\":14.99},"
				+ "{\"description\":\"Salad\",\"price\":6.99}]}";

			Gson gson = new Gson();

			// REFLECTION
			/*
			 * Capacità di analizzare ed interagire a runtime con le classi.
			 * 
			 * Gson usa l'informazione sul tipo dell'oggetto Java a cui un JSON text
			 * deve essere mappato. Tuttavia, usando i generici, questa informazione
			 * viene persa durante la serializzazione.
			 * Necessario l'uso della classe com.google.gson.reflect.TypeToken per
			 * memorizzare il tipo del generico oggetto.
			 */
			Type restaurantType = new TypeToken<RestaurantWithMenu>() {}.getType();
			RestaurantWithMenu rm = gson.fromJson(jsonRestaurant, restaurantType);

			System.out.println("Restaurant: " + rm.name + ", Menu: " + rm.menu.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}