import com.google.gson.stream.JsonWriter;
import java.io.FileWriter;
import java.io.IOException;

// GSON Streaming offre metodi per il caricamento incrementale di parti dell'oggetto a basso livello.

public class GsonStreamWriter
{
	public static void main(String[] args)
	{
		JsonWriter writer;
		try
		{
			writer = new JsonWriter(new FileWriter("result.json"));

			writer.beginObject();					// {
			writer.name("name").value("Steve");		// "name": "Steve"
			writer.name("surname").value("Jobs");	// "surneme": "Job"
			writer.name("birthyear").value(1955);	// "birthyear": 1955
			writer.name("skills");					// "skills":
			writer.beginArray();					// [
			writer.value("JAVA");					// "JAVA"
			writer.value("Python");					// "Python"
			writer.value("Rust");					// "Rust"
			writer.endArray();						// ]
			writer.endObject();						// }

			writer.close();
		}
		catch(IOException e)
		{
			System.err.print(e.getMessage());
		}
	}
}