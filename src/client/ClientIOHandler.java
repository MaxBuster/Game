package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import utils.Constants;

/**
 * Intermediary between the client UI and the server data stream
 * @author Max Buster
 */

public class ClientIOHandler {
	private final PropertyChangeSupport pcs;
	private DataInputStream in;
	private DataOutputStream out;
	private ClientGUI gui;

	public ClientIOHandler(Socket socket) {
		pcs = new PropertyChangeSupport(this);
		pcs.addPropertyChangeListener(new ChangeListener());
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO error message, close client
			e.printStackTrace();
		}
		gui = new ClientGUI(pcs);
	}

	/**
	 * Listens to the input stream from the server and responds to
	 * messages by updating the UI
	 */
	public void handleIO() {
		while (true) {
			try {
				int c = in.readInt();
				while (c != Constants.MESSAGE_START) {
					c = (char) in.readChar();
				}
				int type = in.readInt();
				int[] message = read_message();
				switch (type) {
					case Constants.START_INFO: 
						gui.set_start_info(message);
						break;
					case Constants.PLAYER_INFO:
						gui.set_player_info(message);
						break;
					case Constants.GAME_INFO:
						gui.set_game_info(message);
						break;
					case Constants.VOTER_DIST:
						gui.add_voter_data(message);
						break;
					case Constants.ALL_CANDIDATES:
						gui.add_candidates(message);
						break;
					case Constants.ROUND_NUMBER:
						gui.set_round(message);
						break;
					case Constants.WINNINGS:
						gui.set_winnings(message);
						break;
					default: break;
				}
			} catch (IOException e) {

			}
			/**
			 * TODO
			 * Beginning info
			 * Game start
			 * New game info
			 * Purchased info
			 * Start round: Buy1, Straw, Vote1, Buy2, Final
			 * Vote count received: Straw, Vote1, Final
			 * Candidates that moved on: Vote1 results
			 * Candidate won + winnings 
			 * Games over
			 * Removed from game
			 */
		}
	}
	
	/**
	 * Reads the message from the io stream
	 * @throws IOException if read from io fails
	 */
	private int[] read_message() throws IOException {
		int message_length = in.readInt();
		int[] message = new int[message_length];
		for (int i=0; i<message_length; i++) {
			message[i] = in.readInt();
			System.out.println(message[i]);
		}
		return message;
	}

	/**
	 * Listens to UI events and passes messages to the server
	 * through the output stream
	 */
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			/**
			 * Purchase info about candidate
			 * Done with buy round
			 * Vote
			 */
		}
	}

}
