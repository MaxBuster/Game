/**
 * Contains all runtime data for the voting game and allows all the client handlers
 * to work together
 * @author Max Buster
 */

package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import model.Game.Game;
import model.Player.Player;
import model.Votes.GameVotes;
import model.Votes.VoteResults;
import utils.Constants.Constants;
import utils.IO.DataWriter;

public class Model {
	private ArrayList<Game> games;
	private PropertyChangeSupport pcs;

	private ArrayList<Player> players;
	private int next_player_num;

	private ArrayList<GameVotes> votes;

	private int num_games;
	private int current_game_num;

	private int current_round_index;

	public Model(ArrayList<Game> games, PropertyChangeSupport pcs) {
		this.games = games;
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());

		this.players = new ArrayList<Player>();
		this.next_player_num = 0;

		this.votes = make_vote_list();

		this.num_games = games.size();
		this.current_game_num = Constants.NOT_STARTED;

		this.current_round_index = Constants.NOT_STARTED;
	}

	// ------------------------- Player Access ------------------------------ //

	/**
	 * Generates a player, alerts the PCS and returns the player
	 * @return the generated player
	 */
	public synchronized Player new_player() {
		Player player = new Player(next_player_num);
		next_player_num++;
		players.add(player);
		pcs.firePropertyChange(Constants.NEW_PLAYER, player.get_player_number(), null);
		return player;
	}

	/**
	 * @return whether all players have completed the current round
	 */
	public boolean all_players_done() {
		for (Player p : players) {
			if (!p.is_done()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Resets the "done" field for all players so they can continue the game
	 */
	public void set_players_to_not_done() {
		for (Player p : players) {
			p.set_done(false);
		}
	}

	public synchronized ArrayList<Player> get_players() { return players; }

	// ------------------------- Vote Handling ----------------------------- //

	/**
	 * @return A list of blank GameVote objects - one for each game
	 */
	private ArrayList<GameVotes> make_vote_list() {
		ArrayList<GameVotes> game_votes = new ArrayList<GameVotes>();
		for (int i=0; i<games.size(); i++) {
			game_votes.add(new GameVotes(i));
		}
		return game_votes;
	}

	/**
	 * Adds a vote to the current round of the current game for the specified candidate
	 * @param candidate_num - the candidate to vote for
	 * @param player_num - the player sending the vote
	 */
	public synchronized void vote(int candidate_num, int player_num) {
		String current_round_name = Constants.LIST_OF_ROUNDS[current_round_index];
		votes.get(current_game_num).vote(current_round_name, candidate_num, player_num);
	}

	/**
	 * @param game_num - game you want votes for
	 * @param round_name - round you want votes for
	 * @return A map of candidate numbers to their vote results of the specified round
	 */
	public HashMap<Integer, VoteResults> get_round_vote_results(int game_num, String round_name) {
		return votes.get(game_num).get_round_votes(round_name).get_vote_results();
	}

	// ------------------------- Game Acccess ------------------------------ //

	/**
	 * @return boolean defining whether voting game is currently running
	 */
	public synchronized boolean experiment_started() {
		if (current_game_num == Constants.NOT_STARTED) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Called when "Start Game" is pressed on the server
	 * Increments the game number and round number and alerts listeners
	 */
	public synchronized void start_experiment() {
		start_new_game();
		start_next_round();
	}

	public synchronized int get_num_games() { return num_games; }

	public synchronized Game get_current_game() { return games.get(current_game_num); }
	
	public synchronized int get_current_game_num() { return current_game_num; }

	// ------------------------- Round Handling ------------------------------ //

	/**
	 * If all players are done continues to new round/game, else does nothing
	 */
	public synchronized void continue_game_if_all_players_done() {
		if (!all_players_done()) {
			return;
		}

		end_round();
		if (game_is_over()) {
			if (more_games_left()) {
				start_new_game();
			} else {
				end_all_games();
				return;
			}
		}
		start_next_round();
		set_players_to_not_done();
	}

	/**
	 * Alerts server handlers and server gui that a round ended
	 */
	private synchronized void end_round() {
		pcs.firePropertyChange(Constants.ROUND_OVER, current_round_index, get_current_game());
	}

	/**
	 * Calculates the index of the next round and alerts server handlers and gui
	 */
	private synchronized void start_next_round() {
		current_round_index++;
		current_round_index %= Constants.LIST_OF_ROUNDS.length;
		pcs.firePropertyChange(Constants.NEW_ROUND, current_round_index, null);
	}

	/**
	 * Increments the game number and alerts listeners
	 */
	private void start_new_game() {
		current_game_num++;
		pcs.firePropertyChange(Constants.NEW_GAME, current_game_num, null);
	}

	private void end_all_games() {
		pcs.firePropertyChange(Constants.END_ALL_GAMES, null, null);
	}

	/**
	 * @return boolean of whether the last round of the game has ended
	 */
	private boolean game_is_over() {
		int next_round_index = (current_round_index + 1) % Constants.LIST_OF_ROUNDS.length;
		if (next_round_index < current_round_index) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return a boolean signifying that the current game number exceeds the total number of games
	 */
	private boolean more_games_left() {
		if (current_game_num < num_games - 1) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized int get_current_round_index() { return current_round_index; }

	// ------------------------- PCS Handling ------------------------------ //

	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			if (event == Constants.START_GAME) {
				start_experiment();
			} else if (event == Constants.REMOVE_PLAYER || event == Constants.IO_REMOVE_PLAYER) {
				// FIXME put in synchronized method
				int player_viewable_num = Integer.parseInt((String) PCE.getOldValue());
				int player_num = player_viewable_num-1;
				for (int i=0; i<players.size(); i++) {
					if (players.get(i).get_player_number() == player_num) {
						players.remove(i);
						if (experiment_started()) {
							continue_game_if_all_players_done();
						}
						return;
					}
				}
			} else if (event == Constants.END_ALL_GAMES) {
				DataWriter.write_data("data.csv", Model.this);
			} else if (event == Constants.WRITE_DATA) {
				DataWriter.write_data("data.csv", Model.this);
			}
		}
	}
}
