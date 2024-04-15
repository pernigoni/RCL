
class EchoServerMain
{
	final static int DEFAULT_PORT = 9999;

	public static void main(String[] args)
	{
		int port = DEFAULT_PORT;
		if(args.length > 0)
			try
			{
				port = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e)
			{
				System.out.println("Usage: EchoClientMain <port>");
				System.exit(1);
			}
		
		// creo e avvio il server
		EchoServer server = new EchoServer(port);
		server.start();
	}
}