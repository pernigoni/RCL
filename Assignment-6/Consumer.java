import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.List;

// CONSUMER TASK

/* Questa classe rappresenta il generico task Consumer che si occupa di effettuare la traduzione di un
 * insieme di righe del file originale. Le righe tradotte vengono inserite in una coda con priorità. */

public class Consumer implements Runnable
{
	private List<Element> batch;
	private BlockingQueue<Element> outputQueue;

	public Consumer(List<Element> batch, BlockingQueue<Element> outputQueue)
	{
		this.batch = batch;
		this.outputQueue = outputQueue;
	}

	public void run()
	{
		for(Element element : batch)
		{
			String[] parts = element.line.split("-", 2);
			String address = parts[0].trim();
			String hostname = null;

			try
			{
				hostname = InetAddress.getByName(address).getHostName();
			}
			catch(UnknownHostException e)
			{ }

			String translated = String.format("%s -%s\n", hostname, parts[1]);
			outputQueue.add(new Element(element.id, translated));
		}
	}
}