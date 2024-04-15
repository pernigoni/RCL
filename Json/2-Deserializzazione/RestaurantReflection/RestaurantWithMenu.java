import java.util.List;

public class RestaurantWithMenu
{
	String name;
	List<RestaurantMenuItem> menu;

	public RestaurantWithMenu(String name, List<RestaurantMenuItem> menu)
	{
		this.name = name;
		this.menu = menu;
	}
}