import java.io.*;
import java.net.*;

public class PFFConnection extends Thread {

	private Socket clientSocket;
	private Socket serverSocket;
	private BufferedReader inClient, inServer;
	private DataOutputStream outClient, outServer;
	private int port;
	private RuleBase rb;

	public PFFConnection(Socket clientSocket, int port, RuleBase rb) {
		super();
		this.clientSocket = clientSocket;
		this.port = port;
		this.rb = rb;
	}

	private void closeAllStreams() {
		try {
			inClient.close();
			inServer.close();
			outClient.close();
			outServer.close();
			clientSocket.close();
			serverSocket.close();
		}
		catch(IOException e) {
			System.out.println("Errore sullo Stream! Uscita...");
			System.exit(1);
		}
	}

	public void run() {
		try {
			System.out.println("Connessione stabilita con il Client " + clientSocket.getInetAddress());

			if (!rb.checkRequest(clientSocket.getInetAddress().toString())) {
				System.out.println("Rilevato IP nella BlackList.");
				clientSocket.close();
				return;
			}

			inClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outClient = new DataOutputStream(clientSocket.getOutputStream());
			serverSocket = new Socket(InetAddress.getByName("100.0.0.10"), port);
			serverSocket.setSoTimeout(20000);
			System.out.println("Connessione stabilita con il Server " + serverSocket.getInetAddress());
			inServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			outServer = new DataOutputStream(serverSocket.getOutputStream());
			
			while (true) {
				String payloadReceivedFromClient = inClient.readLine();
				if (payloadReceivedFromClient == null) {
					closeAllStreams();
					return;
				}

				System.out.println("Paylaod Ricevuto da " + clientSocket.getInetAddress() + ": " + payloadReceivedFromClient);
				if (!rb.checkRequest(clientSocket.getInetAddress().toString(), payloadReceivedFromClient)) {
					System.out.println("Rilevato Payload dannoso. IP " + clientSocket.getInetAddress() + " aggiunto alla BlackList.");
					closeAllStreams();
					return;
				}

				System.out.println("Inoltro del Payload al Server...");
				outServer.writeBytes(payloadReceivedFromClient + "\n");
				
				String payloadReceivedFromServer = inServer.readLine();
				System.out.println("Payload Ricevuto dal Server: " + payloadReceivedFromServer);
				if (!rb.checkResponse(payloadReceivedFromServer)) {
					payloadReceivedFromServer = "Default";
					System.out.println("Rilevata informazione sensibile. Invio informazione predefinita al Client...");
				}

				System.out.println("Inoltro del Payload al Client...");
				outClient.writeBytes(payloadReceivedFromServer + " " + clientSocket.getInetAddress().toString() + "\n");
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