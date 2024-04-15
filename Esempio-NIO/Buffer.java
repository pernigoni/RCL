import java.nio.ByteBuffer;

// ANALIZZARE LE VARIABILI DI STATO

public class Buffer
{
	public static void main(String[] args)
	{
		ByteBuffer b = ByteBuffer.allocate(10);
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=0 lim=10 cap=10]

		b.putChar('a');
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=2 lim=10 cap=10]

		b.putInt(1);
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=6 lim=10 cap=10]

		b.flip();
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=0 lim=6 cap=10]

		System.out.println(b.getChar());
		// a

		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=2 lim=6 cap=10]

		b.compact();
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=4 lim=10 cap=10]

		b.putInt(2);
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=8 lim=10 cap=10]

		b.flip();
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=0 lim=8 cap=10]

		System.out.println(b.getInt());
		// 1

		System.out.println(b.getInt());
		// 2

		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=8 lim=8 cap=10]

		b.rewind();
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=0 lim=8 cap=10]

		System.out.println(b.getInt());
		// 1

		b.mark();

		System.out.println(b.getInt());
		// 2

		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=8 lim=8 cap=10]

		b.reset();
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=4 lim=8 cap=10]

		b.clear();
		System.out.println(b);
		// java.nio.HeapByteBuffer[pos=0 lim=10 cap=10]
	}
}