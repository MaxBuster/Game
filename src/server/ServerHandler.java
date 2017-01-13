/**
 * Handles interaction between client and the data model
 * @author Max Buster
 */

package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import model.Candidate.Candidate;
import model.Candidate.CandidateInfo;
import model.Votes.VoteResults;
import utils.Distributions.VoterDistribution;
import model.Game.Game;
import model.Model;
import model.Player.Player;
import model.Player.PlayerGameInfo;

import utils.Constants.Constants;
import utils.IO.MessageTranscriber;
import utils.IO.SocketIO;

public class ServerHandler {
	private Model model;
	private PropertyChangeSupport pcs;

	private SocketIO socketIO;

	private boolean thread_alive;
	private Player player;
	private PlayerGameInfo pgi;
	private Game current_game;

	public ServerHandler(Model model, PropertyChangeSupport pcs, Socket socket) throws IOException {
		this.model = model;
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());
		this.player = model.new_player();

		this.thread_alive = true;

		this.socketIO = new SocketIO(socket);
	}

	public void handleIO() {
		try {
			write_initial_info();
			while (true) {
				socketIO.wait_for_message(); // FIXME have this check that socket is alive

				int message_type = socketIO.read_message_type();
				Object message_body = socketIO.read_message_body();

				switch(message_type) {
					case Constants.REQUEST_INFO:
						handle_info_request(message_body);
						break;
					case Constants.END_ROUND:
						handle_end_round();
						break;
					case Constants.VOTE:
						handle_vote(message_body);
						break;
					default: break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			remove_me();
			return;
		} catch (InterruptedException ie) {
			// TODO handle
		} catch (ClassNotFoundException ce) {
			// TODO handle
		}
	}

	private void start_new_game() throws IOException {
		current_game = model.get_current_game();
		pgi = player.new_pgi(current_game);

		write_player_info();
		write_new_game_info();
		write_voter_dist();
		write_candidate_info();
		write_round_num();
	}

	private void remove_me() {
		socketIO.close_socket();
		pcs.firePropertyChange(Constants.IO_REMOVE_PLAYER, Integer.toString(player.get_player_number()+1), null);
	}

	// -------------------------------- Client Reads ----------------------------------- //

	/**
	 * Purchases the bias for the specified candidate and writes updated candidate payoffs to client
	 * @param message_body - A hashmap containing the candidate number they wish to purchase info for
	 */
	private void handle_info_request(Object message_body) throws IOException {
		HashMap<String, Integer> converted_message = (HashMap<String, Integer>) message_body;
		int candidate_num = converted_message.get("Candidate Number");

		pgi.purchase_bias(candidate_num);
		write_candidate_info();
	}

	/**
	 * Signifies that player is done with this round, and attempts to continue the game if all other
	 * players are done as well
	 */
	private void handle_end_round() {
		player.set_done(true);
		model.continue_game_if_all_players_done();
	}

	/**
	 * Accepts a vote from the client and ends the round for this player
	 * @param message_body - A hashmap contianing the candidate they wish to vote for
	 */
	private void handle_vote(Object message_body) {
		HashMap<String, Integer> converted_message = (HashMap<String, Integer>) message_body;
		int candidate_vote = converted_message.get("Candidate Number");
		model.vote(candidate_vote, player.get_player_number());

		handle_end_round();
	}

	// -------------------------------- Server Writes --------------------------------- //

	private void write_initial_info() throws IOException {
		int player_num = player.get_player_number();
		int num_games = model.get_num_games();

		int message_type = Constants.START_INFO;
		Object message_body = MessageTranscriber.encode_initial_info(player_num, num_games);
		socketIO.write_message(message_type, message_body);
	}

	private void write_player_info() throws IOException {
		int player_position = pgi.get_voter_position();

		int message_type = Constants.PLAYER_INFO;
		Object message_body = MessageTranscriber.encode_player_info(player_position);
		socketIO.write_message(message_type, message_body);
	}

	private void write_new_game_info() throws IOException {
		int game_num = current_game.get_game_num();
		int budget = current_game.get_budget();
		int max_bias = current_game.get_bias_distribution().get_max_bias();

		int message_type = Constants.GAME_INFO;
		Object message_body = MessageTranscriber.encode_game_info(game_num, budget, max_bias);
		socketIO.write_message(message_type, message_body);
	}

	private void write_voter_dist() throws IOException {
		VoterDistribution current_dist = current_game.get_voter_distribution();
		int std_dev_1 = current_dist.get_std_dev_1();
		int mean_1 = current_dist.get_mean_1();
		int std_dev_2 = current_dist.get_std_dev_2();
		int mean_2 = current_dist.get_mean_2();

		int message_type = Constants.VOTER_DIST;
		Object message_body = MessageTranscriber.encode_voter_info(std_dev_1, mean_1, std_dev_2, mean_2);
		socketIO.write_message(message_type, message_body);
	}

	private void write_candidate_info() throws IOException {
		ArrayList<CandidateInfo> candidate_info = pgi.get_expected_payoffs(); // FIXME rename this and change location

		int message_type = Constants.CANDIDATE_INFO;
		Object message_body = MessageTranscriber.encode_candidate_info(candidate_info);
		socketIO.write_message(message_type, message_body);
	}

	private void write_round_num() throws IOException {
		int round_num = model.get_current_round_index();

		int message_type = Constants.ROUND_NUMBER;
		Object message_body = MessageTranscriber.encode_round_info(round_num);
		socketIO.write_message(message_type, message_body);
	}

	private void write_vote_results(String previous_round_name, Game current_game) throws IOException {
		int game_num = current_game.get_game_num();
		HashMap<Integer, VoteResults> vote_results = model.get_round_vote_results(game_num, previous_round_name);

		int message_type = Constants.VOTE_RESULTS;
		Object message_body = MessageTranscriber.encode_vote_results(vote_results);
		socketIO.write_message(message_type, message_body);
	}

	private void write_end_of_game_info(Candidate winning_candidate) throws IOException {
		int winning_candide_num = winning_candidate.get_candidate_num();
		int game_winnings = pgi.get_winnings();
		int player_winnings = player.get_winnings();
		int game_num = current_game.get_game_num();

		int message_type = Constants.WINNINGS;
		Object message_body = MessageTranscriber.encode_end_of_game(winning_candide_num, game_winnings, player_winnings, game_num);
		socketIO.write_message(message_type, message_body);
	}

	private void write_terminating_message() throws IOException {
		int message_type = Constants.END_OF_GAME;
		Object message_body = MessageTranscriber.encode_terminate_game();
		socketIO.write_message(message_type, message_body);
	}

	// --------------------------------- Handle PCS Events ------------------------------------ //

	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			try {
				String event = PCE.getPropertyName();
				if (event == Constants.ROUND_OVER) {
					handle_finished_round_event(PCE);
				} else if (event == Constants.NEW_ROUND) {
					write_round_num();
				} else if (event == Constants.NEW_GAME) {
					start_new_game();
				} else if (event == Constants.END_ALL_GAMES) {
					write_terminating_message();
				} else if (event == Constants.REMOVE_PLAYER) {
					handle_player_removal_event(PCE);
				}
			} catch (IOException e) {
				// TODO remove_me?
			}
		}
	}

	private void handle_finished_round_event(PropertyChangeEvent PCE) throws IOException {
		int previous_round = (Integer) PCE.getOldValue();
		String previous_round_name = Constants.LIST_OF_ROUNDS[previous_round];
		Game current_game = (Game) PCE.getNewValue();
		if (was_the_election(previous_round_name)) {
			Candidate winning_candidate = model.get_winning_candidate(current_game.get_game_num());
			set_winnings(winning_candidate, current_game);
			write_end_of_game_info(winning_candidate);
		} else if (was_a_vote_round(previous_round_name)) {
			write_vote_results(previous_round_name, current_game);
		}
	}

	private void set_winnings(Candidate winning_candidate, Game current_game) {
		int game_winnings = pgi.get_expected_payoff(winning_candidate);
		pgi.set_winnings(game_winnings);
		if (current_game.get_game_num() != 0) {
			player.add_winnings(game_winnings);
			pcs.firePropertyChange(Constants.PLAYER_WINNINGS, player.get_player_number(), player.get_winnings());
		}
	}

	private void handle_player_removal_event(PropertyChangeEvent PCE) {
		int player_viewable_num = Integer.parseInt((String) PCE.getOldValue());
		int player_num = player_viewable_num-1;
		if (player_num == player.get_player_number()) {
			remove_me();
		}
	}

	private boolean was_the_election(String round_name) {
		if (round_name == Constants.ELECTION) {
			return true;
		} else {
			return false;
		}
	}

	private boolean was_a_vote_round(String round_name) {
		if (round_name == Constants.POLL) {
			return true;
		} else if (round_name == Constants.PRIMARY) {
			return true;
		} else {
			return false;
		}
	}
}
