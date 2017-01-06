package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	private Socket socket;

	/**
	 * Initialize a socket connection to the server through the specified
	 * port to the specified ip address
	 */
	public Client(String ip, int port) {
		try {
			// On PC go to Command Prompt; look for IPv4 after ipconfig
			// On Mac go to Terminal; look for inet after ifconfig |grep inet
			// Ignore 127.0.0.1
			// Lab main laptop: 128.252.177.166
			socket = new Socket(ip, port); // Create socket connection through port 10501
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Couldn't find server");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Problem");
		}
	}

	/**
	 * Start the client responses to messages from the server
	 */
	private void run() {
		ClientHandler handler = new ClientHandler(socket);
		handler.handleIO();
	}

	public static void main(String[] args) {
		Client client = new Client("localhost", 10501);
		client.run();
	}
}
