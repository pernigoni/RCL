import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntExample
{
	public static void main(String[] args)
	{
		ExecutorService executor = Executors.newFixedThreadPool(2);
		AtomicInteger atomicInt = new AtomicInteger(); // inizializzato a 0 di default

		for(int i = 0; i < 10; i++)
		{
			CounterRunnable runnableTask = new CounterRunnable(atomicInt);
			executor.submit(runnableTask);
		}

		executor.shutdown();
	}
}