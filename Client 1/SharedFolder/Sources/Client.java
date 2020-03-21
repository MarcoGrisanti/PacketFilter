import java.util.*;
import java.net.*;
import java.io.*;

public class Client {

	private static List<String> dangerousPayloads = Arrays.asList("11100011", "10010110", "11111100");

	private static String generateRandomBinaryString(int length) {
		String str = "";
		for (int i = 0; i < length; i++) {
			int x = (int) (Math.random() * 2);
			str = str + new Integer(x).toString();
		}
		return str;
	}

	public static void main(String[] args) {

		Socket clientSocket;
		BufferedReader in;
		DataOutputStream out;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					Thread.sleep(200);
					System.out.println(" Fine");

				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});

		try {
			clientSocket = new Socket(InetAddress.getByName("10.0.0.20"), 20000);
			clientSocket.setSoTimeout(20000);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new DataOutputStream(clientSocket.getOutputStream());

			while (true) {
				int payloadChoice = (int) (Math.random() * 10 + 1);
				String payloadToSend = "";
				if (payloadChoice == 1) payloadToSend = dangerousPayloads.get((int) (Math.random() * dangerousPayloads.size()));
				else payloadToSend = generateRandomBinaryString(8);

				System.out.println("Invio del Pacchetto...");
				out.writeBytes(payloadToSend + "\n");
				System.out.println("Invio Completato.");
				System.out.println("Attesa della Risposta...");
				String payloadReceived = in.readLine();
				if (payloadReceived == null) System.out.println("Ricevuto Payload Vuoto.");
				else System.out.println("Risposta dal Server: " + payloadReceived);
				Thread.sleep(10000);
			}
		}
		catch (SocketException e) {
			System.out.println("Errore sulla Socket! Uscita...");
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println("Errore! Uscita...");
			System.exit(1);
		}

	}

}