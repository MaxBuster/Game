package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import utils.Constants;
import utils.ClientGuiInfo;

/**
 * Intermediary between the client UI and the server data stream
 * @author Max Buster
 */

public class ClientIOHandler {
	private final PropertyChangeSupport pcs;
	private DataInputStream in;
	private DataOutputStream out;
	private ClientGUI gui;
	private int budget;

	public ClientIOHandler(Socket socket) {
		pcs = new PropertyChangeSupport(this);
		pcs.addPropertyChangeListener(new ChangeListener());
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Removed due to IOException");
			return;
		}
		gui = new ClientGUI(pcs);
		budget = 0;
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
						budget = message[1];
						break;
					case Constants.VOTER_DIST:
						gui.add_voter_data_to_graph(message);
						break;
					case Constants.ALL_CANDIDATES:
						gui.add_candidates(message);
						break;
					case Constants.ROUND_NUMBER:
						gui.set_round(message);
						break;
					case Constants.WINNINGS:
						gui.set_winnings(message);
						show_payoff_popup(message);
						break;
					case Constants.CANDIDATE_PAYOFF:
						gui.update_expected_payoff(message);
						break;
					case Constants.STRAW_VOTES:
						gui.add_votes(3, message);
						break;
					case Constants.FIRST_VOTES:
						gui.add_votes(4, message);
						break;
					case Constants.CANDIDATE_NUMS:
						gui.set_action_tables(message);
						break;
					case Constants.END_OF_GAME:
						gui.end_game();
						break;
					default: break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Removed due to IOException");
				return;
			}
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
	 * Write a message through the output stream to the server
	 */
	private boolean write_message(int message_type, int[] messages) {
		try {
			out.writeInt(Constants.MESSAGE_START);
			out.writeInt(message_type);
			out.writeInt(messages.length);
			for (int i=0; i<messages.length; i++) {
				out.writeInt(messages[i]);
			}
			out.flush();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private void show_payoff_popup(int[] payoff_and_winner) {
		int winner = payoff_and_winner[1];
		int winner_viewable = winner + 1;
		int payoff = payoff_and_winner[2];
		JOptionPane.showMessageDialog(null, "The winner is: " + winner_viewable + 
				"\n You got: " + payoff + " units");
	}
	
	/**
	 * Listens to UI events and passes messages to the server
	 * through the output stream
	 */
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			if (event.equals("Buy")) { // FIXME Constant
				int candidate_num = Integer.parseInt((String) PCE.getOldValue()) - 1;
//				int price = Integer.parseInt((String) PCE.getNewValue());
				int price = Constants.INFO_PRICE;
				// TODO parse into method
				if (price <= budget) {
					budget -= price;
					gui.set_budget(budget);
					int[] message = new int[]{candidate_num, price};
					write_message(Constants.REQUEST_INFO, message);
				} else {
					JOptionPane.showMessageDialog(null, "Insufficient Funds.");
				}
			} else if (event.equals(Integer.toString(Constants.END_ROUND))) {
				gui.set_visible_panels(Constants.END_ROUND_VISIBILITY);
				gui.set_info_text(ClientGuiInfo.WAIT);
				write_message(Constants.END_ROUND, new int[0]);
			} else if (event.equals("Vote")) {
				gui.set_visible_panels(Constants.END_ROUND_VISIBILITY);
				gui.set_info_text(ClientGuiInfo.WAIT);
				int candidate_num = Integer.parseInt((String) PCE.getOldValue()) - 1;
				int[] message = new int[]{candidate_num};
				write_message(Constants.VOTE, message);
			}
		}
	}
}
