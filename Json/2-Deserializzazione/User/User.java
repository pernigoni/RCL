import com.google.gson.*;

public class User
{
	private final String firstName;
	private final String lastName;

	public User(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String toString()
	{
		return new StringBuilder()
			.append("Utente->(")
			.append("Nome: ").append(firstName)
			.append(", Cognome: ").append(lastName)
			.append(")").toString();
	}

	public static void main(String[] args)
	{
		String json_string = "{\"firstName\":\"Akira\", \"lastName\": \"Kurosawa\"}";
		Gson gson = new Gson();
		User user = gson.fromJson(json_string, User.class);
		System.out.println(user);
		// Utente->(Nome: Akira, Cognome: Kurosawa)
	}
}