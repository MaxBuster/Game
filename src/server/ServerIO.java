package server;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Game;
import model.Model;
import utils.ConfigReader;
import utils.Constants;

/**
 * Create a server socket, server GUI, data model 
 * and accepts client connections
 * @author Max Buster
 */

public class ServerIO {
	private final PropertyChangeSupport pcs;
	private Model model;
	private static ServerSocket socket;
	@SuppressWarnings("unused")
	private static ServerGUI gui;

	public ServerIO(Game[] games) {
		this.pcs = new PropertyChangeSupport(this);
		gui = new ServerGUI(pcs, games.length); // FIXME close gui if socket fails
		this.model = new Model(games, pcs); // FIXME catch errors?
	}

	/**
	 * Initialize the server socket and connect to clients
	 */
	public int run() {
		try {
			socket = new ServerSocket(10501);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problem opening server");
			e.printStackTrace();
			return Constants.IOEXCEPTION;
		}
		Socket clientSocket = null;
		while (true) {
			try {
				clientSocket = socket.accept();
				if (!model.game_started()) {
					newThread(clientSocket);
				} else {
					clientSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return Constants.IOEXCEPTION; 
			}
		} 
	}

	/**
	 * Start a new thread to handle a client connection
	 */
	public void newThread(final Socket clientSocket) {
		Thread thread = new Thread() {
			public void run() {
				ServerIOHandler serverIOHandler = new ServerIOHandler(model, pcs, clientSocket);
				serverIOHandler.handleIO();
			}
		};
		thread.start();
	}

	public static void main(String[] args) {
		ConfigReader config = new ConfigReader();
		boolean config_read_successfully = config.read_config("config.csv");
		if (config_read_successfully) {
			ArrayList<Game> games = config.get_games();
			Game[] games_array = games.toArray(new Game[games.size()]);
			ServerIO server = new ServerIO(games_array);
			server.run();
		} else {
			JOptionPane.showMessageDialog(null, "Problem reading config file");
		}
	}

}
