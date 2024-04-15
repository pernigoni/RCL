import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer
{
	public static final long waitDelay = 2000; // intervallo di tempo (in ms) tra l'invio di un messaggio e l'altro

	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			// java TimeServer 224.1.0.0 60000
			System.err.println("Usage: TimeServer <indirizzo> <porta>");
			System.exit(1);
		}

		String address = args[0];
		int port = Integer.parseInt(args[1]);

		// creo un DatagramSocket per l'invio dei pacchetti
		try(DatagramSocket socket = new DatagramSocket())
		{
			// ottengo l'indirizzo del gruppo e ne controllo la validità
			InetAddress group = InetAddress.getByName(address);
			if(!group.isMulticastAddress())
				throw new IllegalArgumentException("Indirizo multicast non valido " + group.getHostAddress());
			
			// entro in un ciclo infinito in cui invio la data ad intervalli regolari
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while(true)
			{
				String message = df.format(new Date(System.currentTimeMillis()));
				byte[] content = message.getBytes();
				DatagramPacket packet = new DatagramPacket(content, content.length, group, port);

				// invio il pacchetto
				socket.send(packet);
				System.out.println("[SERVER] " + message);

				// attendo
				Thread.sleep(waitDelay);
			}
		}
		catch(Exception e)
		{
			System.err.println("[SERVER] Errore: " + e.getMessage());
		}
	}
}