package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import model.Constants;
import model.Game;
import model.Model;

public class ServerIO {
	private final PropertyChangeSupport pcs;
	private Model model;
	private static ServerSocket socket;
	private static ServerGUI gui;
	private HashMap<Integer, ServerIOHandler> clientHandlers;
	private HashMap<Integer, Socket> clientSockets;

	public ServerIO(Game[] games) {
		this.pcs = new PropertyChangeSupport(this);
//		this.pcs.addPropertyChangeListener(new ChangeListener()); FIXME remove if not needed
		gui = new ServerGUI(pcs); // FIXME close gui if socket fails

		this.clientHandlers = new HashMap<Integer, ServerIOHandler>();
		this.clientSockets = new HashMap<Integer, Socket>();
		this.model = new Model(games, pcs); // FIXME catch errors?
	}

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
				newThread(clientSocket);
			} catch (IOException e) {
				e.printStackTrace();
				return Constants.IOEXCEPTION;
			}
		} 
	}

	public void newThread(final Socket clientSocket) {
		Thread thread = new Thread() {
			public void run() {
				try {
					// FIXME just pass the iohandler the socket and let them deal with it
					DataInputStream in = new DataInputStream(clientSocket.getInputStream());
					DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
					ServerIOHandler serverIOHandler = new ServerIOHandler(model, in, out);
					clientHandlers.put(serverIOHandler.getPlayerNum(), serverIOHandler);
					clientSockets.put(serverIOHandler.getPlayerNum(), clientSocket);
					serverIOHandler.handleIO();
				} catch (EOFException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "EOF Problem");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "IO Problem");
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

//	// FIXME don't need a listener here if handlers, model and gui have them
//	class ChangeListener implements PropertyChangeListener {
//		@Override
//		public void propertyChange(PropertyChangeEvent PCE) {
//			/**
//			 * FIXME should some of this get thrown from model and retrieved from gui?
//			 * Game started
//			 * Remove player
//			 * Player removed due to io
//			 * New player
//			 * New round
//			 * New game
//			 * Game over
//			 * Write data
//			 * End game
//			 * All games over
//			 */
//		}
//	}

	public static void main(String[] args) {
		ConfigReader config = new ConfigReader();
		boolean config_read_successfully = config.read_config();
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
