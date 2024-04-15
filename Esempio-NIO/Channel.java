import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

// COPIARE FILE

public class Channel
{
	public static void main(String[] args) throws IOException
	{
		ReadableByteChannel src1 = Channels.newChannel(new FileInputStream("in.txt"));
		ReadableByteChannel src2 = Channels.newChannel(new FileInputStream("in.txt"));

		WritableByteChannel dest1 = Channels.newChannel(new FileOutputStream("out1.txt"));
		WritableByteChannel dest2 = Channels.newChannel(new FileOutputStream("out2.txt"));

		channelCopy1(src1, dest1);
		channelCopy2(src2, dest2);

		src1.close();
		src2.close();
		dest1.close();
		dest2.close();
	}

	private static void channelCopy1(ReadableByteChannel src, WritableByteChannel dest) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

		while(src.read(buffer) != -1)
		{
			// mi preparo a leggere i byte che sono stati inseriti nel buffer
			buffer.flip();

			// scrittura nel file di destinazione, può essere bloccante
			dest.write(buffer);

			/* Non è detto che tutti i byte siano trasferiti, dipende da quanti byte la write() ha
			 * scaricato sul file di output.
			 * 
			 * Compatto i byte rimanenti all'inizio del buffer, se il buffer è stato completamente
			 * scaricato si comporta come clear(). */

			// compatto
			buffer.compact();
		}

		/* Quando si raggiunge l'EOF, è possibile che alcuni byte debbano essere ancora scritti nel file
		 * di output. */
		buffer.flip();
		while(buffer.hasRemaining())
			dest.write(buffer);
	}

	private static void channelCopy2(ReadableByteChannel src, WritableByteChannel dest) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

		while(src.read(buffer) != -1)
		{
			// mi preparo a leggere i byte che sono stati inseriti nel buffer
			buffer.flip();

			// una singola lettura potrebbe non aver scaricato tutti i dati
			while(buffer.hasRemaining())
				dest.write(buffer);
			
			/* A questo punto tutti i dati sono stati letti e scaricati sul file, preparo il buffer
			 * all'inserimento dei dati provenienti dal file. */
			buffer.clear();
		}
	}
}