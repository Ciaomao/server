import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerSide implements Runnable {
	private final Socket clientSocket;

	public ServerSide(Socket clientSocket) { // contractor takes a socket
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
							+ clientSocket.getLocalAddress() + "\n" + " Port: "
							+ clientSocket.getLocalPort() + "\n"
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

	public String stringProcessor(String n) {
		String word;
		if (n.contains(" ")) {
			word = n.substring(0, n.indexOf(" "));
		} else {
			word = n;
		}
		return word;
	}
}