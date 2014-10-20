import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunService {
	private static RunService server;
	private static ServerSocket serverSocket = null;
	private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
// ExcutorService provide a way to manage threads, it will create up to 10
	// threads here

	public static void main(String[] args) throws IOException {

		int port = Integer.parseInt(args[0]); // parsing the port number from
												// the command line args

		serverSocket = new ServerSocket(port);

		server = new RunService();
		server.acceptClients();
	}

	public void acceptClients() throws IOException {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				// wait for a client to connect
				Socket clientSocket = serverSocket.accept();
				// create a socket by accepting the connection, accept() returns
				// a socket
				threadPool.submit(new ServerSide(clientSocket)); // submit an
																	// anonymous
																	// runnable
																	// of type
																	// ServerSide
																	// to the
																	// excutor
			}
		} finally {
			threadPool.shutdown();// shut down thread manager
		}
	}

	public void stop() throws IOException {
		serverSocket.close();
	}

}
