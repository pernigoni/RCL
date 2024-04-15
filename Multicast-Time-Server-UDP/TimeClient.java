import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TimeClient
{
	public static final int size = 1024; // dimensione del buffer per la ricezione dei messaggi
	public static final int count = 10; // numero di messaggi da ricevere

	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Usage: TimeClient <indirizzo> <porta>");
			System.exit(1);
		}

		String address = args[0];
		int port = Integer.parseInt(args[1]);

		// apro un MulticastSocket per la ricezione dei messaggi
		try(MulticastSocket socket = new MulticastSocket(port))
		{
			// ottengo l'indirizzo del gruppo e ne controllo la validità
			InetAddress group = InetAddress.getByName(address);
			if(!group.isMulticastAddress())
				throw new IllegalArgumentException("Indirizzo multicast non valido " + group.getHostAddress());
			
			// mi unisco al gruppo multicast
			socket.joinGroup(group);

			// ricevo 'count' messaggi dal server prima di terminare
			for(int i = 0; i < count; i++)
			{
				DatagramPacket packet = new DatagramPacket(new byte[size], size);

				// ricevo il pacchetto
				socket.receive(packet);
				System.out.println(
					"[CLIENT] " + new String(packet.getData(), packet.getOffset(), packet.getLength()));
			}
		}
		catch(Exception e)
		{
			System.err.println("[CLIENT] Errore: " + e.getMessage());
		}
	}
}