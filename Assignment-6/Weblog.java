import java.io.*;
import java.net.InetAddress;
import java.security.Security;

// ANALISI DI UN WEBLOG (VERSIONE SINGLE-THREADED)

// ci mette un mucchio di tempo (circa 3 minuti)

public class Weblog
{
	public static final int cachingTime = 600; // tempo di permanenza in cache
	public static final String stringCachingTime = String.format("%s", cachingTime);

	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Usage: Weblog <inputfile> <outputfile>");
			System.exit(1);
		}

		String inputfile = args[0], outputfile = args[1];

		// imposto il tempo di permanenza in cache degli indirizzi tradotti
		Security.setProperty("networkaddress.cache.ttl", stringCachingTime);
		long start = System.currentTimeMillis();
		int count = 0;

		try(
			BufferedReader in = new BufferedReader(new FileReader(inputfile));
			PrintWriter out = new PrintWriter(outputfile); )
		{
			String line = null;
			// per ogni riga estraggo l'indirizzo IP e lo traduco
			while((line = in.readLine()) != null)
			{
				String[] parts = line.split("-", 2);
				String address = parts[0].trim();
				String hostname = InetAddress.getByName(address).getHostName();
				/*
				 * String java.net.InetAddress.getHostName()
				 * Returns:
				 * the host name for this IP address, or if the operation is not allowed by the security
				 * check, the textual representation of the IP address. */

				// scrivo la riga tradotta sul file di output
				out.printf("%s -%s\n", hostname, parts[1]);
				count++;
			}
		}
		catch(Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}

		// stampo il numero di righe lette e il tempo impiegato (in ms)
		long end = System.currentTimeMillis();
		System.out.printf("N. of lines\t: %d\nElapsed time\t: %d ms\n", count, end - start);
	}
}