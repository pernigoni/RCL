import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Counter implements Runnable
{
	public static final int BUFSIZE = 32768; // dimensione del buffer
	private Map<Character, Integer> counters; // riferimento alla hash map globale
	private Map<Character, Integer> localCounters; // riferimento alla hash map locale
	private File file; // riferimento al file di input

	public Counter(Map<Character, Integer> counters, File file)
	{
		this.counters = counters;
		this.localCounters = new HashMap<>();
		this.file = file;
	}

	public void run()
	{
		// apro il file di input per la lettura
		try(BufferedReader in = new BufferedReader(new FileReader(file), BUFSIZE))
		{
			int current = -1;
			while((current = in.read()) != -1)
			{
				char key = (char) current;
				// ignorare i caratteri non alfabetici
				if(!Character.isAlphabetic(key))
					continue;
				// normalizzare il carattere rimuovendo eventuali segni diacritici
				key = normalize(Character.toLowerCase(key));
				// se il carattere è alfabetico aggiorno atomicamente la hash map
				localCounters.compute(key, (k, v) -> ((v == null) ? 1 : v + 1));
				/* key: carattere normalizzato
				 * value: per ogni coppia (k, v)
				 * 		(1) se la chiave la stiamo inserendo per la prima volta allora v = 1.
				 * 		(2) altrimenti v = v + 1. */

			} // finita lettura dal file, non ci sono più caratteri da leggere

			// scrivo i risultati della hash map locale nella hash map globale
			for(Entry<Character, Integer> entry : localCounters.entrySet())
			{
				char key = entry.getKey();
				int count = entry.getValue();
				counters.compute(key, (k, v) -> ((v == null) ? count : v + count));
			}
		}
		catch(Exception e) // generica Exception perché usando i buffer, le map, i file ne abbiamo diverse
		{
			System.err.printf("Error while reading file %s\n", file.getName());
			e.printStackTrace();
		}
	}

	public static char normalize(char x)
	{
		// classe Normalizer su cui chiamiamo il metodo statico normalize
		String s = Normalizer.normalize(
			String.valueOf(x),
			Normalizer.Form.NFKD).replaceAll("\\p{M}", "");

		return s.charAt(0);
	}
}