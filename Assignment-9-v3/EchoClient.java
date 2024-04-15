import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoClient
{
	private final int BUFFER_DIMENSION = 1024; // dimensione del buffer per la lettura
	private final String EXIT_CMD = "exit"; // comando per comunicare la fine della comunicazione
	private final int port; // porta su cui il server è in ascolto
	private boolean exit; // vale true se il client è in terminazione, false altrimenti

	public EchoClient(int port)
	{
		this.port = port;
		this.exit = false;
	}

	public void start()
	{
		try(SocketChannel client = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(), port));)
		{
			BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("[CLIENT] Connesso, digita 'exit' per uscire");

			while(!this.exit)
			{
				String msg = consoleReader.readLine().trim();

				if(msg.equals(this.EXIT_CMD))
				{
					this.exit = true;
					continue;
				}

				// Creo il messaggio da inviare al server.

				// la prima parte del messaggio contiene la lunghezza del messaggio
				ByteBuffer length = ByteBuffer.allocate(Integer.BYTES);
				length.putInt(msg.length());
				length.flip();
				client.write(length);
				length.clear();

				// la seconda parte del messaggio contiene il messaggio da inviare
				ByteBuffer readBuffer = ByteBuffer.wrap(msg.getBytes());
				client.write(readBuffer);
				readBuffer.clear();

				ByteBuffer reply = ByteBuffer.allocate(BUFFER_DIMENSION);
				client.read(reply);
				reply.flip();
				System.out.printf("[CLIENT] Ricevuto %s\n", new String(reply.array()).trim());
				reply.clear();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}