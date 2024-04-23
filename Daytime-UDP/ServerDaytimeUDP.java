import java.io.*;
import java.net.*;
import java.util.Date;

public class ServerDaytimeUDP
{
	// Daytime Protocol RFC-867

	private final static int PORT = 13;

	public static void main(String[] args)
	{
		// apro un DatagramSocket su una porta nota (well known port)
		/*
		 * Porta nota perché i client devono inviare i pacchetti a quella destinazione.
		 * A differenza di TCP, stesso tipo di socket sia per il client che per il server. */
		try(DatagramSocket socket = new DatagramSocket(PORT))
		{
			while(true)
			{
				try
				{
					// creo un pacchetto in cui ricevere la richiesta del client
					DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
					socket.receive(request);
					System.out.println("Ricevuto un pacchetto da " + request.getAddress() + " " + request.getPort());

					// creo un pacchetto di risposta
					String daytime = new Date().toString();
					byte[] data = daytime.getBytes("US-ASCII");
					DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
					// invio la risposta usando lo stesso socket da cui si è ricevuto il pacchetto
					socket.send(response);
				}
				catch(IOException | RuntimeException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
