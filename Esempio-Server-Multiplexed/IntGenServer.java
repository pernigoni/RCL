/* 
 * NIO MULTIPLEXED INTEGER GENERATION SERVICE
 * 
 * Sviluppare un servizio di generazione di una sequenza di interi il cui scopo è testare l'affidabilità
 * della rete mediante generazione di numeri binari.
 * 
 * Quando il server è contattato dal client, esso invia al client una sequenza di interi rappresentati su
 * 4 byte: 0, 1, 2, 3, 4, 5, ...
 * 
 * Il server genera una sequenza infinita di interi.
 * 
 * Il client interrompe la comunicazione quando ha ricevuto suffcienti informazioni.
 */

import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

// SERVER MULTIPLEXED

public class IntGenServer
{
	public static int DEFAULT_PORT = 1919;

	public static void main(String[] args)
	{
		int port;
		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch(RuntimeException e)
		{
			port = DEFAULT_PORT;
		}
		System.out.println("Listening for connections on port " + port);

		ServerSocketChannel serverChannel;
		Selector selector;
		try
		{
			// apro il ServerSocketChannel
			serverChannel = ServerSocketChannel.open();

			// prendo il ServerSocket associato al ServerSocketChannel
			ServerSocket ss = serverChannel.socket();

			// creo una InetSocketAddress con la porta, per default su localhost
			InetSocketAddress address = new InetSocketAddress(port);

			// bind del ServerSocket a quell'indirizzo
			ss.bind(address);

			// setto il ServerSocketChannel come non bloccante
			serverChannel.configureBlocking(false);

			// apro il selettore
			selector = Selector.open();

			// registro il canale sul selettore con l'operazione OP_ACCEPT
			// essendo il server, per ora non posso fare altro che accettare connessioni
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			// ho registrato il mio interesse su quel canale di accettare connessioni
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return ;
		}

		while(true)
		{
			try
			{
				// questa volta l'ho fatta bloccante perché la prima cosa che devo fare è vedere se
				// qualcuno mi contatta
				selector.select();
				// ad ogni iterazione con la select() vado a vedere quali sono i canali pronti
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			}

			// a questo punto la select() si è sbloccata

			// select() sbloccata vuol dire che c'è qualcosa di pronto, in questo caso vuol dire che
			// si è connesso un client (ho accettato una connessione)
			Set<SelectionKey> readyKeys = selector.selectedKeys();

			Iterator<SelectionKey> iterator = readyKeys.iterator();
			while(iterator.hasNext())
			{
				// prendo una chiave
				SelectionKey key = iterator.next();

				// rimuovo la chiave perché altrimenti continua ad esserci nelle prossime iterazioni
				iterator.remove();
				// chiave rimossa dal Selected Set ma non dal Registered Set

				try
				{
					if(key.isAcceptable())
					{
						// prendo il riferimento al canale che avevo registrato con channel()
						ServerSocketChannel server = (ServerSocketChannel) key.channel();

						// accept() sul ServerSocketChannel
						SocketChannel client = server.accept(); // mi collega a quel particolare client

						System.out.println("Accepted connection from " + client);

						// setto il SocketChannel come non bloccante
						client.configureBlocking(false);

						// registro il mio interesse di scrivere sul canale con OP_WRITE
						// sto registrando sul selettore il socket che mi collega a quel particolare client
						SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);

						// alloco un buffer di 4 byte perché voglio scrivere un intero
						ByteBuffer output = ByteBuffer.allocate(4);

						// inserisco 0 nel buffer
						output.putInt(0);
						// position=4

						// con flip() metto position=0, limit=4
						output.flip();

						// attacco il buffer come attachment al canale, quando sarà pronta la scrittura la farò
						key2.attach(output);

						// Mi sono messo in condizione di scrivere, ma devo aspettare che il selettore
						// mi dica che posso scrivere.
					}
					else if(key.isWritable()) // la scrittura è pronta
					{
						// prendo il riferimento del client
						SocketChannel client = (SocketChannel) key.channel();

						// prendo l'attachment preparato prima
						ByteBuffer output = (ByteBuffer) key.attachment();

						/* Voglio scrivere un intero quindi 4 byte.
						 * 
						 * Non è detto che tutti i byte vengano scritti sul canale in una volta sola.
						 * 
						 * Quando key.isWritable() significa che c'è uno spazio nel canale (a livello di
						 * SO), posso scrivere qualcosa ma non è detto che ce la faccia a scaricare
						 * tutti i byte.
						 * 
						 * Potrebbe succedere, ad esempio, che scrivo uno dei 4 byte alla volta. */

						// hasRemaining() dice quanti byte ci sono tra position e limit
						// quindi se c'è ancora qualcosa nella parte del buffer significativa da scrivere
						if(!output.hasRemaining()) // da questo if potrei passarci più volte
						{
							// se ho scritto 777, con rewind() rileggo il 777
							output.rewind();

							// con getInt() prendo il valore che c'era nel buffer
							int value = output.getInt();

							output.clear();

							// putInt(777 + 1)
							output.putInt(value + 1);

							output.flip();
						}

						// faccio la write()
						client.write(output);
					}
				}
				catch(IOException e)
				{
					key.cancel();
					try
					{
						key.channel().close();
					}
					catch(IOException ce)
					{ }
				}
			}
		}
	}
}