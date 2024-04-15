import java.util.*;

public class CountPrimesThread extends Thread
{
	int id; // id del thread
	int MAX; // numeri da controllare

	public CountPrimesThread(int id, int MAX)
	{
		this.id = id;
		this.MAX = MAX;
	}

	private static boolean isPrime(int x)
	{
		assert x > 1;
		int top = (int) Math.sqrt(x);
		for(int i = 2; i <= top; i++)
			if(x % i == 0)
				return false;
		return true;
	}

	private static int countPrimes(int min, int max)
	{
		int count = 0;
		for(int i = min; i <= max; i++)
			if(isPrime(i))
				count++;
		return count;
	}

	public void run()
	{
		long startTime = System.currentTimeMillis();
		int count = countPrimes(2, MAX);
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("Thread " + id + " counted " + count + " primes in " + (elapsedTime/1000.0) + " seconds.");
	}
}