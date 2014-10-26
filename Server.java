import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

		try (ServerSocket serverSocket = new ServerSocket(port);) {
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
		try (DataInputStream fromClient = new DataInputStream(
				clientSocket.getInputStream());
				DataOutputStream toClient = new DataOutputStream(
						clientSocket.getOutputStream());) {
			while (true) {
				String clientText = fromClient.readUTF(); // read client text
				if (clientText.equals("KILL_SERVICE")) { // end service if text contains KILL_SERVICE
					toClient.writeUTF("Server terminated \n");
					toClient.flush();
					System.exit(1);
				} else if (clientText.contains("HELO")) {
					String helo = clientText + "\n"
							+ " IP : "+ clientSocket.getLocalAddress() + "\n" 
							+"Port: "+ clientSocket.getLocalPort() + "\n"
							+ "StudentID: 14303154";
					toClient.writeUTF(helo);
					toClient.flush();
				} else { // Echo Client text
					toClient.writeUTF(clientText);
					toClient.flush();
				}// end else
			}// end while
		} // end try
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("\nError in Server\n");
		}// end catch
	} // end run
}// end class
