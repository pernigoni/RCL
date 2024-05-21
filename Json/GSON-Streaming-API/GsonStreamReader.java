import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GsonStreamReader
{
	public static void main(String[] args)
	{
		JsonReader reader;
		try
		{
			reader = new JsonReader(new FileReader("result.json"));

			reader.beginObject();
			while(reader.hasNext())
			{
				String name = reader.nextName();
				if("name".equals(name))
					System.out.println(reader.nextString());
				else if("surname".equals(name))
					System.out.println(reader.nextString());
				else if("birthyear".equals(name))
					System.out.println(reader.nextString()); 
				else if("skills".equals(name))
				{
					reader.beginArray();
					while(reader.hasNext())
						System.out.println("\t" + reader.nextString());
					reader.endArray();
				}
				else
					reader.skipValue();
			}
			reader.endObject();

			reader.close();
		}
		catch(FileNotFoundException e)
		{
			System.err.print(e.getMessage());
		}
		catch
		(IOException e)
		{
			System.err.print(e.getMessage());
		}
	}
}

/* 
STAMPA PRODOTTA DAL PROGRAMMA

Steve
Jobs
1955
	JAVA
 	Python
	Rust
*/
