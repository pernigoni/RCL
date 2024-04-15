import java.io.IOException;
import java.net.*;

/*
 * SSDP (SIMPLE SERVICE DISCOVERY PROTOCOL)
 * 
 * Su una rete locale sono attivi molti protocolli che usano il multicast.
 * 
 * SSDP è attivo sulla porta 1900 associato all'indirizzo di multicast 239.255.255.250, è un protocollo di
 * discovery usato per scoprire quali servizi sono disponibili in una rete.
 * 
 * Il server SSDP invia pacchetti di advertisement sul gruppo di multicast.
 * 
 * Formato di un pacchetto:
 * 
 * 		HTTP/1.1 200 OK
 * 		CACHE-CONTROL: _______
 * 		ST: __________________
 * 		USN: uuid:____________
 * 		EXT: _________________
 * 		SERVER: ______________
 * 		LOCATION: ____________
 * 
 * USN (Unique Service Name), UUID (Universally Unique Identifier) indica un device o un servizio.
 * LOCATION punta ad un URL, in cui l'utente può trovare una descrizione XML delle capability del servizio.
 */

public class MulticastSniffer
{
	public static void main(String[] args)
	{
		InetAddress group = null;
		int port = 0;

		// java MulticastSniffer 239.255.255.250 1900
		try
		{
			group = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
		}
		catch(ArrayIndexOutOfBoundsException | NumberFormatException | UnknownHostException e)
		{
			System.err.println("Usage: MulticastSniffer <multicastAddress> <port>");
			System.exit(1);
		}

		MulticastSocket ms = null;
		try
		{
			ms = new MulticastSocket(port);

			/* Per ricevere pacchetti da un gruppo di multicast occorre unirsi al gruppo con joinGroup(),
			 * per inviarli no. */
			ms.joinGroup(group); // deprecata

			byte[] buffer = new byte[8192];
			while(true)
			{
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ms.receive(dp); // bloccante
				String s = new String(dp.getData(), "8859_1");
				System.out.println(s);
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
		finally
		{
			if(ms != null)
				try
				{
					ms.leaveGroup(group); // deprecata
					ms.close();
				}
				catch(IOException e)
				{ }
		}
	}
}