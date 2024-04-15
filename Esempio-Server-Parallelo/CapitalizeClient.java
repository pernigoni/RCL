import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// CLIENT

public class CapitalizeClient
{
	public static void main(String[] args) throws Exception
	{
		if(args.length != 1)
		{
			System.err.println("Pass the server IP as the sole command line argument");
			return ;
		}

		Scanner scanner = null, in = null;
		
		try(Socket socket = new Socket(args[0], 10000))
		{
			System.out.println("Enter lines of thext then 'exit' to quit");
			scanner = new Scanner(System.in);
			in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			boolean end = false;
			while(!end)
			{
				String line = scanner.nextLine();
				if(line.contentEquals("exit"))
					end = true;
				out.println(line);
				System.out.println(in.nextLine());
			}
		}
		finally
		{
			scanner.close();
			in.close();
		}
	}
}