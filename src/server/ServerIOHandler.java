package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import model.Candidate;
import model.Distribution;
import model.Game;
import model.Model;
import model.Player;
import model.PlayerGameInfo;
import model.ValenceGenerator;
import utils.Constants;

/**
 * Handles incoming client messages and Server UI events
 * @author Max Buster
 */

public class ServerIOHandler {
	private Model model;
	private PropertyChangeSupport pcs;
	private DataInputStream in;
	private DataOutputStream out;
	private Player player;
	private PlayerGameInfo pgi;
	private Game current_game;
	private String current_round;

	public ServerIOHandler(Model model, PropertyChangeSupport pcs, Socket socket) {
		this.model = model;
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());
		this.player = model.new_player();

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Interact with client connection
	 */
	public void handleIO() {
		try {
			write_start_info();
			while (true) {
				int c = in.readInt();
				while (c != Constants.MESSAGE_START) {
					c = (char) in.readChar();
				}
				int type = in.readInt();
				int[] message = read_message();
				switch(type) {
				case Constants.REQUEST_INFO:
					int candidate_num = message[0];
					pgi.purchase_valence(current_round, candidate_num);
					write_message(Constants.CANDIDATE_PAYOFF, new int[]{candidate_num, pgi.get_expected_payoff(current_game, candidate_num)});
					break;
				case Constants.END_ROUND:
					player.setDone(true);
					model.attempt_end_round();
					break;
				case Constants.VOTE:
					int candidate = message[0];
					current_game.vote(current_round, candidate);
					pgi.vote(current_round, candidate);
					player.setDone(true);
					model.attempt_end_round();
					break;
				default: break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			remove_me();
			return;
		}
	}

	/**
	 * Writes the player # and num games to start all games
	 */
	private void write_start_info() {
		int[] message = new int[]{player.getPlayer_number(), model.get_num_games()};
		write_message(Constants.START_INFO , message);
	}
	
	private void start_new_game() {
		current_game = model.get_current_game();
		pgi = player.new_pgi(current_game);
		write_player_info();
		write_game_info(); 
		write_voter_dist();
		write_candidate_info();
		write_round_num();
	}

	/**
	 * Writes player info for the start of a game
	 */
	private void write_player_info() {
		int[] message = new int[] {
				pgi.get_ideal_pt()
		};
		write_message(Constants.PLAYER_INFO, message);
	}

	/**
	 * Writes the general info for a new game
	 */
	private void write_game_info() {
		Game current_game = model.get_current_game();
		int[] message = new int[] {
				current_game.getGameNumber(), current_game.getBudget()
		};
		write_message(Constants.GAME_INFO, message);
	}

	/**
	 * Writes the current game's voter distribution
	 */
	private void write_voter_dist() {
		Game current_game = model.get_current_game();
		Distribution current_dist = current_game.getDistribution();
		int[] message = current_dist.get_dist();
		write_message(Constants.VOTER_DIST, message);
	}

	/**
	 * Writes out candidate numbers and parties for the current game
	 */
	private void write_candidate_info() {
		Game current_game = model.get_current_game();
		HashMap<Integer, Candidate> candidates = current_game.getCandidates();
		int[] candidate_nums = new int[candidates.size()];
		int[] message = new int[candidates.size() * 3]; // Room to add cand #, party and ideal point
		int i = 0;
		for (Candidate c : candidates.values()) {
			candidate_nums[c.get_candidate_number()] = c.get_candidate_number();
			message[i] = c.get_candidate_number();
			message[i+1] = c.get_candidate_ideal_point();
			message[i+2] = pgi.get_expected_payoff(current_game, c.get_candidate_number());
			i += 3;
		}
		write_message(Constants.ALL_CANDIDATES, message);
		write_message(Constants.CANDIDATE_NUMS, candidate_nums);
	}

	private void write_winnings(int winning_candidate, Game current_game) {
		int game_winnings = pgi.set_winnings(current_game, winning_candidate);
		
		int[] message = new int[]{player.getWinnings(), winning_candidate, game_winnings};
		write_message(Constants.WINNINGS, message);
		int player_viewable_num = player.getPlayer_number() + 1; // FIXME don't set viewable here
		pcs.firePropertyChange(Constants.PLAYER_WINNINGS, player_viewable_num, player.getWinnings());
	}

	private void write_round_num() {
		int[] message = new int[]{model.get_current_round_index()};
		write_message(Constants.ROUND_NUMBER, message);
	}
	
	private void remove_me() {
		pcs.firePropertyChange(Constants.SERVERIO_REMOVE, player, null);
	}

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
	 * Listen to changes from the GUI
	 */
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			if (event == Constants.ROUND_OVER) { // 
				int previous_round = (Integer) PCE.getOldValue();
				String previous_round_name = Constants.LIST_OF_ROUNDS[previous_round];
				Game current_game = (Game) PCE.getNewValue();
				write_votes(previous_round_name, current_game); // Writes votes if it was a vote round
			} else if (event == Constants.NEW_ROUND) { // Write round num
				int round_pos = (Integer) PCE.getOldValue();
				current_round = Constants.LIST_OF_ROUNDS[round_pos];
				write_message(Constants.ROUND_NUMBER, new int[]{round_pos});
			} else if (event == Constants.NEW_GAME) {
				start_new_game(); 
			} else if (event == Constants.END_ALL_GAMES) {
				write_message(Constants.END_OF_GAME, Constants.EMPTY_MESSAGE);
			} else if (event == Constants.GUI_REMOVE) {
				int player_to_remove = (Integer) PCE.getOldValue();
				if (player_to_remove == player.getPlayer_number()) {
					// TODO end thread somehow
				}
			}
		}
	}
	
	/**
	 * Writes the vote outcomes if the previous round was a vote or just returns otherwise
	 */
	private void write_votes(String previous_round_name, Game current_game) {
		if (previous_round_name == Constants.STRAW_VOTE) {
			int[] round_votes = current_game.get_round_votes_percent(previous_round_name);
			write_message(Constants.STRAW_VOTES, round_votes);
		} else if (previous_round_name == Constants.FIRST_VOTE) {
			int[] round_votes = current_game.get_round_votes_percent(previous_round_name);
			write_message(Constants.FIRST_VOTES, round_votes);
			// Write the top two that will move on
			int[] top_two = current_game.get_top_x_candidates(2, previous_round_name);
			Arrays.sort(top_two);
			write_message(Constants.CANDIDATE_NUMS, top_two);
		} else if (previous_round_name == Constants.FINAL_VOTE) {
			int winning_candidate = current_game.get_top_x_candidates(1, previous_round_name)[0];
			write_winnings(winning_candidate, current_game);
		} else {
			return;
		}
	}
}
