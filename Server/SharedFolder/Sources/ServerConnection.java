import java.io.*;
import java.net.*;

public class ServerConnection extends Thread {

	private Socket clientSocket;

	public ServerConnection(Socket clientSocket) {
		super();
		this.clientSocket = clientSocket;
	}

	private static String generateRandomBinaryString(int length) {
		String str = "";
		for (int i = 0; i < length; i++) {
			int x = (int) (Math.random() * 2);
			str = str + new Integer(x).toString();
		}
		return str;
	}

	public void run() {
		try {
			System.out.println("Connessione stabilita con " + clientSocket.getInetAddress());
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			while (true) {
				String payloadReceived = in.readLine();
				if (payloadReceived == null) {
					in.close();
					out.close();
					clientSocket.close();
					return;
				}
				System.out.println("Payload Ricevuto da " + clientSocket.getInetAddress() + ": " + payloadReceived);
				String payloadToSend = generateRandomBinaryString(8);
				System.out.println("Invio Risposta: " + payloadToSend);
				out.writeBytes(payloadToSend + "\n");
			}
		}
		catch (SocketException e) {
			System.out.println("Errore in una Socket! Uscita...");
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println("Errore! Uscita...");
			System.exit(1);
		}
	}

}