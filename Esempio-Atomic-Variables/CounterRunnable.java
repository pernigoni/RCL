import java.util.concurrent.atomic.AtomicInteger;

class CounterRunnable implements Runnable
{
	AtomicInteger atomicInt;
	
	public CounterRunnable(AtomicInteger atomicInt)
	{
		this.atomicInt = atomicInt;
	}

	@Override
	public void run()
	{
		System.out.println("[THREAD id=" + Thread.currentThread().threadId() + "] " + atomicInt.incrementAndGet());
	}
}