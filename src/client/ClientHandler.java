/**
 * Intermediary between the client UI and the server data stream
 * @author Max Buster
 */

package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import client.UIHelpers.ClientGuiInfo;
import model.Candidate.CandidateInfo;
import utils.Constants.Constants;
import utils.Distributions.VoterDistribution;
import utils.IO.MessageTranscriber;
import utils.IO.SocketIO;

public class ClientHandler {
	private final PropertyChangeSupport pcs;
	private SocketIO socket_IO;

	private ClientGUI gui;
	private int budget;
	private int[] purchases;
	private int max_valence;

	public ClientHandler(Socket socket) {
		this.pcs = new PropertyChangeSupport(this);
		this.pcs.addPropertyChangeListener(new ChangeListener());
		try {
			this.socket_IO = new SocketIO(socket);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Removed due to IOException");
			return;
		}
		this.gui = new ClientGUI(pcs);
		this.budget = 0;
	}

	public void handleIO() {
		while (true) {
			try {
				socket_IO.wait_for_message();

				int message_type = socket_IO.read_message_type();
				Object message_body = socket_IO.read_message_body();
				
				switch (message_type) {
					case Constants.START_INFO:
						handle_start_info(message_body);
						break;
					case Constants.PLAYER_INFO:
						handle_player_info(message_body);
						break;
					case Constants.GAME_INFO:
						handle_game_info(message_body);
						break;
					case Constants.VOTER_DIST:
						handle_voter_dist(message_body);
						break;
					case Constants.CANDIDATE_INFO:
						handle_candidate_info(message_body);
						break;
					case Constants.ROUND_NUMBER:
						handle_round_num(message_body);
						break;
					case Constants.VOTE_RESULTS:
						handle_vote_results(message_body);
						break;
					case Constants.WINNINGS:
						handle_winnings(message_body);
						break;
					case Constants.END_OF_GAME:
						handle_end_of_game(message_body);
						break;
					default: break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Removed from game due to IOException");
				gui.set_default_close();
				return;
			} catch (InterruptedException ie) {
				// TODO handle
			} catch (ClassNotFoundException c) {
				// TODO hanlde
			}
		}
	}

	// ---------------------------------------- Input Handlers ---------------------------------------------- //

	private void handle_start_info(Object message_body) {
		HashMap<String, Integer> decoded_message = MessageTranscriber.decode_initial_info(message_body);
		int player_num = decoded_message.get("Player Number");
		int num_games = decoded_message.get("Num Games");
		gui.set_start_info(player_num, num_games);
	}

	private void handle_player_info(Object message_body) {
		HashMap<String, Integer> decoded_message = MessageTranscriber.decode_player_info(message_body);
		int player_position = decoded_message.get("Player Position");
		gui.set_player_info(player_position);
	}

	private void handle_game_info(Object message_body) {
		HashMap<String, Integer> decoded_message = MessageTranscriber.decode_game_info(message_body);
		int current_game_num = decoded_message.get("Game Num");
		int budget = decoded_message.get("Budget");
		int max_bias = decoded_message.get("Max Bias");

		this.gui.set_game_info(current_game_num, budget, max_bias);
		this.max_valence = max_bias;
		this.budget = budget;
	}

	private void handle_voter_dist(Object message_body) {
		HashMap<String, Integer> decoded_message = MessageTranscriber.decode_voter_info(message_body);
		int std_dev_1= decoded_message.get("Std Dev 1");
		int mean_1 = decoded_message.get("Mean 1");
		int std_dev_2= decoded_message.get("Std Dev 2");
		int mean_2 = decoded_message.get("Mean 2");
		VoterDistribution voter_distribution = new VoterDistribution(std_dev_1, mean_1, std_dev_2, mean_2);
		gui.add_voter_data_to_graph(voter_distribution);
	}

	private void handle_candidate_info(Object message_body) {
		ArrayList<CandidateInfo> decoded_message = MessageTranscriber.decode_candidate_info(message_body);
		this.purchases = new int[message_body.length/3];
		gui.add_candidates(message_body, max_valence);
//		gui.update_expected_payoff(message_body, max_valence, purchases);
//		gui.set_buy_table(message_body);
//		gui.set_vote_table(message_body);
	}

	private void handle_round_num(Object message_body) {
		HashMap<String, Integer> decoded_message = MessageTranscriber.decode_round_info(message_body);
		int round_num = decoded_message.get("Round Num");
		gui.set_round(round_num);
	}

	private void handle_vote_results(Object message_body) {
		gui.add_votes(3, message_body); // Poll
		gui.add_votes(4, message_body); // Primary
		gui.set_vote_table(message_body); // Top two results
	}

	private void handle_winnings(Object message_body) {
		HashMap<String, Integer> decoded_message = MessageTranscriber.decode_end_of_game(message_body);
		int round_winnings = decoded_message.get("Round Winnings");
		int game_winnings = decoded_message.get("Game Winnings");
		int winning_candidate = decoded_message.get("Winning Candidate");
		gui.set_winnings(game_winnings);
		show_payoff_popup(winning_candidate, round_winnings);
	}

	private void handle_end_of_game(Object message_body) {
		gui.end_game();
	}
	
	private void show_payoff_popup(int winning_candidate, int round_winnings) {
		int winner_viewable = winning_candidate + 1;
		JOptionPane.showMessageDialog(null, "The winner is: " + winner_viewable + 
				"\n You got: " + round_winnings + " units");
	}

	// ---------------------------------------- Output Writers ---------------------------------------------- //

	private void write_info_request(int candidate_num) throws IOException {
		int message_type = Constants.REQUEST_INFO;
		Object message_body = MessageTranscriber.encode_info_request(candidate_num);
		socket_IO.write_message(message_type, message_body);
	}

	private void write_end_round() throws  IOException {
		int message_type = Constants.END_ROUND;
		Object message_body = MessageTranscriber.encode_end_round();
		socket_IO.write_message(message_type, message_body);
	}

	private void write_vote(int candidate_num) throws IOException {
		int message_type = Constants.VOTE;
		Object message_body = MessageTranscriber.encode_vote(candidate_num);
		socket_IO.write_message(message_type, message_body);
	}

	// -------------------------------- Property Change Event Handling ------------------------------- //

	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			try {
				if (event.equals("Buy")) { // FIXME Constant
					handle_purchase_event(PCE);
				} else if (event.equals(Integer.toString(Constants.END_ROUND))) {
					handle_end_round_event(PCE);
				} else if (event.equals("Vote")) {
					handle_vote_event(PCE);
				}
			} catch (IOException e) {
				// TODO close client?
			}
		}
	}

	private void handle_purchase_event(PropertyChangeEvent PCE) throws IOException {
		int candidate_num = Integer.parseInt((String) PCE.getOldValue()) - 1;
		int price = Constants.INFO_PRICE;
		if (price <= budget) {
			gui.remove_candidate_from_buy((String) PCE.getOldValue());
			budget -= price;
			gui.set_budget(budget);
			purchases[candidate_num] = 1;

			write_info_request(candidate_num);
		} else {
			JOptionPane.showMessageDialog(null, "Insufficient Funds.");
		}
	}

	private void handle_end_round_event(PropertyChangeEvent PCE) throws IOException {
		gui.set_visible_panels(Constants.END_ROUND_VISIBILITY);
		gui.set_info_text(ClientGuiInfo.WAIT);

		write_end_round();
	}

	private void handle_vote_event(PropertyChangeEvent PCE) throws IOException {
		gui.set_visible_panels(Constants.END_ROUND_VISIBILITY);
		gui.set_info_text(ClientGuiInfo.WAIT);
		int candidate_num = Integer.parseInt((String) PCE.getOldValue()) - 1;

		write_vote(candidate_num);
	}
}
