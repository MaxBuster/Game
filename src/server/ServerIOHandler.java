package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import model.Candidate;
import model.Distribution;
import model.Game;
import model.Model;
import model.Player;
import model.PlayerPurchasedInfo;
import utils.Constants;

/**
 * Handles incoming client messages and Server UI events
 * @author Max Buster
 */

public class ServerIOHandler {
	private Model model;
	private PropertyChangeSupport pcs;
	private Player player;
	private PlayerPurchasedInfo info;
	private DataInputStream in;
	private DataOutputStream out;

	public ServerIOHandler(Model model, PropertyChangeSupport pcs, Socket socket) {
		this.model = model;
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());
		this.player = model.new_player();
		this.info = player.getPpi();

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			// FIXME crash the client handler
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
					get_token(message);
					break;
				case Constants.END_ROUND:
					player.setDone(true);
					model.attempt_end_round();
					break;
				case Constants.VOTE:
					model.vote_for_candidate(message);
					player.setDone(true);
					model.attempt_end_round();
					break;
				default: break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// FIXME close server socket?
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
		model.init_player(player);
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
				player.getPlayer_party(), player.getIdeal_point()
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
		int[] message = new int[candidates.size() * 2]; // Room to add cand # and party for each
		int i = 0;
		for (Candidate c : candidates.values()) {
			message[i] = c.get_candidate_number();
			message[i+1] = c.get_candidate_party(); 
			i += 2;
		}
		write_message(Constants.ALL_CANDIDATES, message);
	}

	private void write_winnings() {
		int[] message = new int[]{player.getWinnings()};
		write_message(Constants.WINNINGS, message);
	}

	private void get_token(int[] request) {
		int game = model.get_current_game().getGameNumber();
		int candidate = request[0];
		String round = model.get_current_round();
		int token = model.get_token(candidate);
		info.add_token(game, token, round, candidate);
		int[] all_tokens = info.get_tokens(game, candidate);
		write_message(Constants.TOKENS, all_tokens);
	}

	private void write_round_num() {
		int[] message = new int[]{model.get_current_round_index()};
		write_message(Constants.ROUND_NUMBER, message);
	}

	// TODO write end game

	// TODO write end all games

	// FIXME don't catch error or respond to the error by removing client
	private boolean write_message(int message_type, int[] messages) {
		try {
			out.writeInt(Constants.MESSAGE_START);
			out.writeInt(message_type);
			out.writeInt(messages.length);
			for (int i=0; i<messages.length; i++) {
				out.writeInt(messages[i]);
			}
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
				Game current_game = (Game) PCE.getNewValue();
				String round_name = Constants.LIST_OF_ROUNDS[previous_round];
				write_votes(previous_round, round_name, current_game);
			} else if (event == Constants.NEW_ROUND) { // Write round num
				int round_pos = (Integer) PCE.getOldValue();
				write_message(Constants.ROUND_NUMBER, new int[]{round_pos});
			} else if (event == Constants.NEW_GAME) {
				start_new_game(); 
			}
		}
	}
	
	private void write_votes(int previous_round, String round_name, Game current_game) {
		if (round_name == Constants.STRAW_VOTE) {
			write_message(Constants.VOTES, current_game.get_votes(previous_round));
		} else if (round_name == Constants.FIRST_VOTE) {
			write_message(Constants.VOTES, current_game.get_votes(previous_round));
		} else if (round_name == Constants.FINAL_VOTE) {
			write_message(Constants.VOTES, current_game.get_votes(previous_round));
//			int diff_to_candidate = player.getIdeal_point() - 
			// FIXME send payoffs + winner? + end of game message
		} 
	}
}
