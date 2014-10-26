import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		  if(args.length != 1) {
	            System.err.println("Error! Incorrect arguments! Please try again by typing TCP port number as the only argument");
	            System.exit(1);
	        }
		int port = Integer.parseInt(args[0]);
		String serverText; // text to be received from the server
		try (Socket socket = new Socket("localhost", port);// create a socket
				BufferedReader fromUser = new BufferedReader(
						new InputStreamReader(System.in));
				DataInputStream fromServer = new DataInputStream(
						socket.getInputStream());
				DataOutputStream toServer = new DataOutputStream(
						socket.getOutputStream());) {
			while (true) {
				String userInput = fromUser.readLine();// read user input
				toServer.writeUTF(userInput);//send text to the server
				toServer.flush();//flush the stream
				serverText = fromServer.readUTF();//read text from the server
				System.out.println(serverText);//print server response
			} // end while
		}// end try
		catch (IOException e) {
			System.out.println("\nError in Client \n");
			e.printStackTrace();
		}// end catch
	}// end main
}// end class