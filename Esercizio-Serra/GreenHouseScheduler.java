import java.time.LocalTime;
import java.util.concurrent.*;

public class GreenHouseScheduler
{
	private class LightOn implements Runnable
	{
		public void run()
		{ System.out.println("Turning on lights"); }
	}

	private class LightOff implements Runnable
	{
		public void run()
		{ System.out.println("Turning off lights"); }
	}

	private class WaterOn implements Runnable
	{
		public void run()
		{ System.out.println("Turning water on"); }
	}

	private class WaterOff implements Runnable
	{
		public void run()
		{ System.out.println("Turning water off"); }
	}

	private class ThermostatNight implements Runnable
	{
		public void run()
		{ System.out.println("Thermostat to night setting"); }
	}

	private class ThermostatDay implements Runnable
	{
		public void run()
		{ System.out.println("Thermostat to day setting"); }
	}

	private class Bell implements Runnable
	{
		public void run()
		{ System.out.println("Bing!"); }
	}

	private class Terminate implements Runnable
	{
		ScheduledThreadPoolExecutor scheduler;

		public Terminate(ScheduledThreadPoolExecutor scheduler)
		{
			this.scheduler = scheduler;
		}

		public void run()
		{
			System.out.println("Terminating");
			scheduler.shutdownNow();
		}
	}

	public static void schedule(ScheduledThreadPoolExecutor scheduler, Runnable event, long delay, TimeUnit tu)
	{
		scheduler.schedule(event, delay, tu);
	}

	public static void repeat(ScheduledThreadPoolExecutor scheduler, Runnable event, long initialDelay, long period, TimeUnit tu)
	{
		scheduler.scheduleAtFixedRate(event, initialDelay, period, tu);
	}

	public static void main(String[] args)
	{
		GreenHouseScheduler gh = new GreenHouseScheduler();
		ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(10);

		schedule(scheduler, gh.new Terminate(scheduler), 10000, TimeUnit.MILLISECONDS);
		repeat(scheduler, gh.new Bell(), 0, 1000, TimeUnit.MILLISECONDS);
		repeat(scheduler, gh.new LightOn(), 0, 200, TimeUnit.MILLISECONDS);
		repeat(scheduler, gh.new LightOff(), 100, 200, TimeUnit.MILLISECONDS);
		repeat(scheduler, gh.new WaterOn(), 0, 600, TimeUnit.MILLISECONDS);
		repeat(scheduler, gh.new WaterOff(), 300, 600, TimeUnit.MILLISECONDS);

		LocalTime now = LocalTime.now();
		LocalTime morning = LocalTime.of(7, 0, 0);
		LocalTime night= LocalTime.of(19, 0, 0);
		Boolean light = now.isAfter(morning) && now.isBefore(night);
		int interval = 86400;
		if(light)
		{
			int seconds = night.toSecondOfDay() - now.toSecondOfDay();
			repeat(scheduler, gh.new ThermostatDay(), 0, interval, TimeUnit.SECONDS);
			repeat(scheduler, gh.new ThermostatNight(), seconds, interval, TimeUnit.SECONDS);
		}
		else
		{
			int seconds = morning.toSecondOfDay() - now.toSecondOfDay();
			repeat(scheduler, gh.new ThermostatNight(), 0, interval, TimeUnit.SECONDS);
			repeat(scheduler, gh.new ThermostatDay(), seconds, interval, TimeUnit.SECONDS);
		}
	}
}
