import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientListener implements Runnable
{
	private DatagramSocket socket;
	private PacketMonitor[] monitors;
	private int bufferSize;

	/**
	 * Costruttore della classe ClientListener.
	 * @param socket riferimento al DatagramSocket su cui arrivano i pacchetti
	 * @param monitors riferimento all'array contenente i monitor per i pacchetti
	 * @param bufferSize dimensione del buffer usato per la ricezione dei pacchetti
	 */
	public ClientListener(DatagramSocket socket, PacketMonitor[] monitors, int bufferSize)
	{
		this.socket = socket;
		this.monitors = monitors;
		this.bufferSize = bufferSize;
	}

	public void run()
	{
		System.out.println("[CLIENT] Listener avviato");
		while(true)
		{
			byte[] buffer = new byte[bufferSize];
			DatagramPacket rpkt = new DatagramPacket(buffer, buffer.length);
			try
			{
				socket.receive(rpkt);
			}
			/* Questa eccezione viene sollevata quando il socket viene chiuso dall'esterno.
			 * NOTA: oltre al timeout, questo è l'unico modo per sbloccare un thread che è bloccato
			 * sulla receive(). */
			catch(IOException e)
			{
				break;
			}

			// ricavo l'identificativo del pacchetto appena arrivato
			int i = Integer.parseInt(new String(rpkt.getData()).split(" ")[1]);

			// aggiorno l'indicatore i-esimo segnalando al thread in attesa l'arrivo del pacchetto con questo id
			if(0 <= i && i < monitors.length)
				monitors[i].set();
		}
		System.out.println("[CLIENT] Listener terminato");
	}
}