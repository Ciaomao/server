import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private Socket clientSocket = null;

	// Constructor
	public Server(Socket socket) {
		clientSocket = socket;
	}

	public static void main(String[] args) {
		
		if(args.length != 1) {
        System.err.println("Error! Incorrect arguments! Please try again by typing TCP port number as the only argument");
        System.exit(1);
       }
	int port = Integer.parseInt(args[0]);

		ExecutorService threadGenerator = Executors.newFixedThreadPool(10);// thread generator up to 10 threads
		Socket clientSocket = null;

		try (ServerSocket serverSocket = new ServerSocket(9876);) {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					clientSocket = serverSocket.accept();// create a client socket
					threadGenerator.submit(new Server(clientSocket));// submit a thread with that socket
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					threadGenerator.shutdown();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}//end catch
	}// end main
	// ------------------- Handling clients----------------
	@Override
	public void run() {
		try (BufferedReader fromClient = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
				PrintWriter toClient = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);) {
			while (true) {
				String clientText = fromClient.readLine(); // read client text
				if (clientText.equals("KILL_SERVICE")) { // end service if text contains KILL_SERVICE
					break;
				} else if (clientText.contains("HELO")) {
					String helo = clientText + "\n"
							+ "IP:"+ InetAddress.getLocalHost().getHostAddress() + "\n" 
							+"Port:"+ clientSocket.getLocalPort() + "\n"
							+ "StudentID:14303154";
					toClient.println(helo);
				} else { // Echo Client text
					toClient.println(clientText);
				}// end else
				
				clientSocket.close();
				fromClient.close();
				toClient.close();
				System.exit(0);
				
			}// end while
			
			
			
			
		} // end try
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("\nError in Server\n");
		}// end catch
	} // end run
}// 