public class RestaurantMenuItem
{
	String description;
	float price;

	public RestaurantMenuItem(String description, float price)
	{
		this.description = description;
		this.price = price;
	}

	public String toString()
	{
		return description + " " + price;
	}
}