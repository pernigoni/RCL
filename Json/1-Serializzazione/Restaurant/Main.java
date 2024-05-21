import com.google.gson.*;
import java.util.*;

public class Main
{
	public static void main(String[] args)
	{
		List<RestaurantMenuItem> menu = new ArrayList<>();
		menu.add(new RestaurantMenuItem("Spaghetti", 9.99f));
		menu.add(new RestaurantMenuItem("Steak", 14.99f));
		menu.add(new RestaurantMenuItem("Salad", 6.99f));

		RestaurantWithMenu restaurant = new RestaurantWithMenu("AllWhatYouCanEat", menu);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String restaurantJson = gson.toJson(restaurant);
		System.out.println(restaurantJson);
	}
}
