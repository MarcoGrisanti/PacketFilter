import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) {

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
			ServerSocket serverSocket = new ServerSocket(20000);
			System.out.println("In attesa di connessioni in entrata...");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				clientSocket.setSoTimeout(20000);
				ServerConnection conn = new ServerConnection(clientSocket);
				conn.start();
			}
		}
		catch (Exception e) {
			System.out.println("Errore! Uscita...");
			System.exit(1);
		}
	}
}