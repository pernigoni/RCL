/*
 * Questa classe contiene lo stato della comunicazione tra client e server durante la ricezione del
 * messaggio da parte di quest'ultimo.
 * Le variabili qui contenute vengono usate per tenere traccia del numero di byte letti dal server e della
 * dimensione del messaggio inviato dal client.
 */

import java.nio.ByteBuffer;

public class ReadState
{
	public int count; // numero totale di byte letti
	public int length; // dimensione del messaggio da ricevere
	public ByteBuffer buffer; // buffer per memorizzare il messaggio e la sua lunghezza

	public ReadState(int bufSize)
	{
		this.count = 0;
		this.length = 0;
		this.buffer = ByteBuffer.allocate(bufSize);
	}
}