import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

class EchoServer
{
	private final int BUFFER_DIMENSION = 1024; // dimensione del buffer per la lettura
	private final String EXIT_CMD = "exit"; // comando per comunicare la fine della comunicazione
	private final int port; // porta su cui aprire il listening socket
	private final String ADD_ANSWER = "echoed by server";

	public int numberActiveConnections; // numero di client con i quali è aperta una connessione

	public EchoServer(int port)
	{
		this.port = port;
	}

	public void start()
	{
		this.numberActiveConnections = 0;

		try(ServerSocketChannel s_channel = ServerSocketChannel.open();)
		{
			s_channel.socket().bind(new InetSocketAddress(this.port));
			s_channel.configureBlocking(false);
			Selector sel = Selector.open();
			s_channel.register(sel, SelectionKey.OP_ACCEPT);
			System.out.printf("[SERVER] In attesa di connessioni sulla porta %d\n", this.port);

			while(true)
			{
				if(sel.select() == 0)
					continue;
				
				Set<SelectionKey> selectedKeys = sel.selectedKeys(); // insieme delle chiavi corrispondenti a canali pronti
				Iterator<SelectionKey> iter = selectedKeys.iterator();
				while(iter.hasNext())
				{
					SelectionKey key = iter.next();
					iter.remove();

					// utilizzo try-catch per gestire la terminazione improvvisa del client
					try
					{
						if(key.isAcceptable())
						{
							/* Accetto una nuova connessione creando un SocketChannel per la comunicazione
							 * con il client che la richiede. */

							ServerSocketChannel server = (ServerSocketChannel) key.channel();
							SocketChannel c_channel = server.accept();
							c_channel.configureBlocking(false);
							System.out.println("[SERVER] Accettata una nuova connessione: " + c_channel.getRemoteAddress());
							System.out.printf("[SERVER] %d connessioni aperte\n", ++this.numberActiveConnections);

							this.registerRead(sel, c_channel);
						}
						else if(key.isReadable())
							this.readClientMessage(sel, key);

						if(key.isWritable())
							this.echoAnswer(sel, key);
					}
					catch(IOException e) // terminazione improvvisa del client
					{
						e.printStackTrace();
						key.channel().close();
						key.cancel();
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Registra l'interesse all'operazione di READ sul selettore.
	 * @param sel selettore utilizzato dal server
	 * @param c_channel socket channel relativo al client
	 * @throws IOException
	 */
	private void registerRead(Selector sel, SocketChannel c_channel) throws IOException
	{
		// creo il buffer
		ByteBuffer length = ByteBuffer.allocate(Integer.BYTES);
		ByteBuffer message = ByteBuffer.allocate(BUFFER_DIMENSION);
		ByteBuffer[] bfs = {length, message};

		/* Aggiungo il canale del client al selector con l'operazione OP_READ.
		 * Aggiungo l'array di bytebuffer [length, message] come attachment. */
		c_channel.register(sel, SelectionKey.OP_READ, bfs);
	}

	/**
	 * Legge il messaggio inviato dal client e registra l'interesse all'operazione di WRITE sul selettore.
	 * @param sel selettore utilizzato dal server
	 * @param key chiave di selezione
	 * @throws IOException
	 */
	private void readClientMessage(Selector sel, SelectionKey key) throws IOException
	{
		// accetto una nuova connessione creando un SocketChannel per la comunicazione con il client
		SocketChannel c_channel = (SocketChannel) key.channel();

		// recupera l'array di bytebuffer (attachment)
		ByteBuffer[] bfs = (ByteBuffer[]) key.attachment();

		c_channel.read(bfs);
		if(!bfs[0].hasRemaining())
		{
			bfs[0].flip();
			int l = bfs[0].getInt();

			if(bfs[1].position() == l)
			{
				bfs[1].flip();
				String msg = new String(bfs[1].array()).trim();
				System.out.printf("[SERVER] Ricevuto %s\n", msg);
				if(msg.equals(this.EXIT_CMD))
				{
					System.out.println("[SERVER] Connessione chiusa con il client " + c_channel.getRemoteAddress());
					c_channel.close();
					key.cancel();
				}
				else
					/* Aggiunge il canale del client al selector con l'operazione OP_WRITE.
					 * Aggiunge il messaggio ricevuto come attachment (aggiungendo la risposta addizionale) */
					c_channel.register(sel, SelectionKey.OP_WRITE, msg + " " + this.ADD_ANSWER);
			}
		}
	}

	/**
	 * Scrive il buffer sul canale del client.
	 * @param sel selettore utilizzato dal server
	 * @param key chiave di selezione
	 * @throws IOException
	 */
	private void echoAnswer(Selector sel, SelectionKey key) throws IOException
	{
		SocketChannel c_channel = (SocketChannel) key.channel();
		String echoAnsw = (String) key.attachment();
		ByteBuffer bbEchoAnsw = ByteBuffer.wrap(echoAnsw.getBytes());
		c_channel.write(bbEchoAnsw);
		System.out.println("[SERVER] " + echoAnsw + " inviato al client " + c_channel.getRemoteAddress());
		if(!bbEchoAnsw.hasRemaining())
		{
			bbEchoAnsw.clear();
			this.registerRead(sel, c_channel);
		}
	}
}