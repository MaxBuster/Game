/**
 * Starting point for the game server
 * Reads config, intializes data model, server GUI and accepts client connections
 * @author Max Buster
 */

package server;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Game;
import model.Model;

import org.json.JSONException;
import utils.FileIO.ConfigReader;

public class ServerIO {
	private final PropertyChangeSupport pcs;
	@SuppressWarnings("unused")
	private static ServerGUI gui;
	private Model model;
	private ServerSocket server_socket;

	public ServerIO(ArrayList<Game> games) throws IOException {
		this.pcs = new PropertyChangeSupport(this);
		this.gui = new ServerGUI(pcs, games.size()); // FIXME close gui if server_socket fails
		this.model = new Model(games, pcs); // FIXME catch errors?
		open_socket(10501);
	}

	/**
	 * @param port - the port on the computer that accepts the socket connection
	 * @throws IOException - If socket creation fails
	 */
	public void open_socket(int port) throws IOException{
		this.server_socket = new ServerSocket(port);
	}

	/**
	 * Continually loops to accept clients and pass them off to client handler threads
	 * Rejects a client connection if the game has already begun
	 * @throws IOException - If accepting a client fails
	 */
	public void run() throws IOException {
		while (true) {
			Socket client_socket = server_socket.accept();
			if (!model.game_started()) {
				start_client_handler_thread(client_socket);
			} else {
				// TODO write a message
				client_socket.close();
			}
		} 
	}

	/**
	 * Begins a new thread for client socket IO
	 * @param client_socket - the socket with which the new thread should communicate with
	 */
	public void start_client_handler_thread(final Socket client_socket) {
		Thread thread = new Thread() {
			public void run() {
				ServerIOHandler server_IO_handler = new ServerIOHandler(model, pcs, client_socket);
				server_IO_handler.handleIO();
			}
		};
		thread.start();
	}

	public static void main(String[] args) {
		String filename = "test_resources/test_config.json"; // FIXME change location
		ArrayList<Game> games;

		try {
			games = ConfigReader.get_games_from_config(filename);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problem finding config file: " + filename);
			return;
		} catch (JSONException j) {
			JOptionPane.showMessageDialog(null, "Problem parsing json format of file");
			return;
		}

		try {
			ServerIO server = new ServerIO(games);
			server.run();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception running server");
		}
	}

}
