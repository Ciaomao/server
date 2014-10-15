import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSide {
	private static ServerSide server;
	private static ServerSocket serverSocket = null;
	private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

	// ExcutorService provide a way to manage threads

	public static void main(String[] args) throws IOException {

	int port = Integer.parseInt(args[0]); // parsing the port number from
												// the command line args

		serverSocket = new ServerSocket(2222);

		server = new ServerSide();
		server.acceptClients();
	}

	public void acceptClients() throws IOException {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				// wait for a client to connect
				Socket clientSocket = serverSocket.accept();
				// create a handler object for that socket
				threadPool.submit(new ClientHandler(clientSocket));
			}
		} finally {
			threadPool.shutdown();// shut down thread manager
		}
	}

	public void stop() throws IOException {
		serverSocket.close();
	}

	// this inner class handles each client connection
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;

		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				DataOutputStream out = new DataOutputStream(
						clientSocket.getOutputStream());
				while (true) {
					String n = in.readLine();

					if (n.equals("KILL_SERVICE")) {
						out.writeBytes("Server terminated \n");
						out.flush();
						System.exit(1);
					} else if (stringProcessor(n).equals("HELO")) {
						out.writeBytes(n + " IP : "
								+ clientSocket.getLocalAddress() + "\n"+" Port: "
								+ clientSocket.getLocalPort() +"\n"
								+ "  StudentID: 14303154 \n");
						out.flush();
					}

					else {
						out.writeBytes(n + "\n");
						out.flush();
					}

				}
			} // end try
			catch (IOException ex) {
				System.out.println("error");
			}
		} // end while

		  public String stringProcessor(String n)
		  {
		      String word;
		      if(n.contains(" ")){
		            word= n.substring(0, n.indexOf(" ")); }
		            else{
		            word =n;}
		      return word;
		  }  
	}
}
