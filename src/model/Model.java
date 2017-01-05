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

import model.Votes.GameVotes;
import utils.Constants;
import utils.FileIO.DataWriter;

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

		this.players = new ArrayList<>();
		this.next_player_num = 0;

		// TODO initialize game votes object

		this.num_games = games.size();
		this.current_game_num = Constants.NOT_STARTED;

		this.current_round_index = Constants.NOT_STARTED; // FIXME does this still work?
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
		pcs.firePropertyChange(Constants.NEW_PLAYER, player.getPlayer_number(), null);
		return player;
	}

	public synchronized ArrayList<Player> get_players() { return players; }

	// ------------------------- Game Acccess ------------------------------ //

	/**
	 * @return boolean defining whether voting game is currently running
	 */
	public synchronized boolean game_started() {
		if (current_game_num == Constants.NOT_STARTED) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized int get_num_games() { return num_games; }

	public synchronized Game get_current_game() { return games.get(current_game_num); }
	
	public synchronized int get_current_game_num() { return current_game_num; }

	// ------------------------- Round Handling ------------------------------ //

	/**
	 * Ends round if all players are done, otherwise returns
	 */
	public synchronized void attempt_end_round() {
		for (Player p : players) {
			if (!p.isDone()) {
				return;
			}
		}
		end_round(); // All players are done with the round
	}

	/**
	 * Alerts all that game is over and continues game
	 */
	public synchronized void end_round() {
		pcs.firePropertyChange(Constants.ROUND_OVER, current_round_index, get_current_game());
		increment_round(); // FIXME race condition if serverhandlers respond to prop change
		for (Player p : players) { // reset players
			p.setDone(false);
		}
	}

	/**
	 * Increments the round and game if applicable, checking that all games are not over
	 */
	public synchronized void increment_round() { // FIXME split into multiple methods
		int previous_round_index = current_round_index;
		/* Initialize the next round */
		current_round_index++;
		current_round_index %= Constants.LIST_OF_ROUNDS.length; 

		/* Check if the previous game ended */
		if (current_round_index < previous_round_index) {
			if (current_game_num < num_games - 1) {
				/* Game ended and there is a new game */
				current_game_num++;
				pcs.firePropertyChange(Constants.NEW_GAME, current_game_num, null);
			} else {
				/* Game ended and all the games are over */
				pcs.firePropertyChange(Constants.END_ALL_GAMES, null, null);
				return;
			}
		}
		/* We are still within the same game */
		pcs.firePropertyChange(Constants.NEW_ROUND, current_round_index, null);
	}

	public synchronized int get_current_round_index() { return current_round_index; }

	// ------------------------- PCS Handling ------------------------------ //

	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			if (event == Constants.START_GAME) {
				increment_round();
			} else if (event == Constants.REMOVE_PLAYER || event == Constants.IO_REMOVE_PLAYER) {
				// FIXME put in synchronized method
				int player_viewable_num = Integer.parseInt((String) PCE.getOldValue());
				int player_num = player_viewable_num-1;
				for (int i=0; i<players.size(); i++) {
					if (players.get(i).getPlayer_number() == player_num) {
						players.remove(i);
						if (game_started()) {
							attempt_end_round();
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
