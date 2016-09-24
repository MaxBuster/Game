package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ClientIO {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	public ClientIO() {
		try {
			// On PC go to Command Prompt; look for IPv4 after ipconfig
			// On Mac go to Terminal; look for inet after ifconfig |grep inet
			// Ignore 127.0.0.1
			// Lab main laptop: 128.252.177.166
			socket = new Socket("localhost", 10501);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ClientIOHandler handler = new ClientIOHandler(in, out);
			handler.handleIO();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Couldn't find server");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Problem");
		}
	}

	public static void main(String[] args) {
		ClientIO client = new ClientIO();
	}
}
