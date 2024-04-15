import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// SERVER PARALLELO

public class Capitalizer implements Runnable
{
	private Socket socket;

	Capitalizer(Socket socket)
	{
		this.socket = socket;
	}

	public void run()
	{
		System.out.println("Connected: " + socket);

		try(
			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true); )
		{
			while(in.hasNextLine())
				out.println(in.nextLine().toUpperCase());
		}
		catch(Exception e)
		{
			System.out.println("Error: " + socket);
		}
	}

	public static void main(String[] args) throws Exception
	{
		try(ServerSocket listener = new ServerSocket(10000))
		{
			try
			{
				InetAddress ip = InetAddress.getLocalHost();
				System.out.println("IP address: " + ip.getHostAddress());
			}
			catch(UnknownHostException e)
			{
				e.printStackTrace();
			}

			System.out.println("The capitalization server is running...");
			ExecutorService pool = Executors.newFixedThreadPool(20);
			while(true)
				pool.execute(new Capitalizer(listener.accept()));
		}
	}
}