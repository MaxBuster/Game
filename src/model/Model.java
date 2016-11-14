package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Random;

import utils.Constants;

/**
 * Keep track of game info and current game status
 * @author Max Buster
 */

public class Model {
	private PropertyChangeSupport pcs;
	private int num_games;
	private int current_game;
	private int current_round_index;

	private Game[] games;
	private int next_player_num;
	private ArrayList<Player> players;

	public Model(Game[] games, PropertyChangeSupport pcs) {
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());
		this.num_games = games.length;
		this.current_game = Constants.NOT_STARTED;
		this.current_round_index = Constants.LIST_OF_ROUNDS.length - 1; // This allows a new game to start
		this.games = games;
		this.next_player_num = 0;
		this.players = new ArrayList<Player>();
	}

	// ------------------------- Player Stuff ------------------------------ //

	public synchronized Player new_player() {
		Player player = new Player(next_player_num);
		players.add(player);
		next_player_num++;
		pcs.firePropertyChange(Constants.NEW_PLAYER, player.getPlayer_number(), null);
		return player;
	}

	// Get list of all players
	public synchronized ArrayList<Player> get_players() {
		return players;
	}

	// ------------------------- Game Stuff ------------------------------ //

	public synchronized Game[] get_all_games() {
		return games;
	}

	public synchronized int get_num_games() {
		return num_games;
	}

	public synchronized Game get_current_game() {
		return games[current_game];
	}

	public synchronized boolean game_started() {
		if (current_game == Constants.NOT_STARTED) { 
			return false;
		} else {
			return true;
		}
	}

	// ------------------------- Rounds ------------------------------ //

	public synchronized int get_current_round_index() {
		return current_round_index;
	}

	public synchronized String get_current_round() {
		return Constants.LIST_OF_ROUNDS[current_round_index];
	}

	public synchronized void attempt_end_round() {
		for (Player p : players) {
			if (!p.isDone()) {
				return;
			}
		}
		end_round(); // Ends round if all players are done
	}

	public synchronized void end_round() {
		pcs.firePropertyChange(Constants.ROUND_OVER, current_round_index, get_current_game());
		increment_round();
		for (Player p : players) { // reset players
			p.setDone(false);
		}
	}

	public synchronized void increment_round() {
		int previous_round_index = current_round_index;
		current_round_index++;
		current_round_index %= Constants.LIST_OF_ROUNDS.length; 

		if (current_round_index < previous_round_index) {
			if (current_game < num_games - 1) {
				current_game++;
				pcs.firePropertyChange(Constants.NEW_GAME, current_game, null);
			} else {
				pcs.firePropertyChange(Constants.END_ALL_GAMES, null, null);
				return;
			}
		}
		pcs.firePropertyChange(Constants.NEW_ROUND, current_round_index, null);
	}

	// ------------------------- Listener Stuff ------------------------------ //

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
			}
		}
	}
}
