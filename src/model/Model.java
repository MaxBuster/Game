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
		this.current_game = -1;
		this.current_round_index = Constants.LIST_OF_ROUNDS.length-1;
		this.games = games;
		this.next_player_num = 0;
		this.players = new ArrayList<Player>();
	}
	
	// ------------------------- Player Stuff ------------------------------ //
	
	public synchronized Player new_player() {
		Player player = new Player(next_player_num, games.length);
		players.add(player);
		next_player_num++;
		pcs.firePropertyChange(Constants.NEW_PLAYER, player.getPlayer_number(), null);
		return player;
	}
	
	public synchronized void init_player(Player player) {
		int ideal_point = get_ideal_point();
		int num_games = get_current_game().getCandidates().size();
		int[] valences = new int[num_games];
		for (int i=0; i<num_games; i++) {
			valences[i] = get_payoff_valence();
		}
		player.setPlayerInfo(ideal_point, valences);
	}
	
	public synchronized int get_ideal_point() {
		Distribution dist = games[current_game].getDistribution();
		int[] cdf = dist.getCDF();
		int ideal_point = 100;
		int sum = cdf[cdf.length-1];
		int random_point = new Random().nextInt(sum); 
		for (int i=0; i<cdf.length; i++) {
			if (cdf[i] > random_point) {
				ideal_point = i-1; // FIXME corner cases?
				break;
			}
		}
		return ideal_point;
	}
	
	// ------------------------- Candidate Stuff ------------------------------ //
	
	public synchronized void vote_for_candidate(int[] vote_message) {
		int candidate_number = vote_message[0];
		Candidate candidate = games[current_game].getCandidates().get(candidate_number);
		String current_round = Constants.LIST_OF_ROUNDS[current_round_index];
		candidate.increment_round_votes(current_round);
	}
	
	public int get_token(int candidate_num) {
		Candidate c = get_current_game().getCandidates().get(candidate_num);
		int ideal_pt = c.get_candidate_ideal_point();
		int random_num = new Random().nextInt(100);
		if (random_num < ideal_pt) {
			return 0; // FIXME make constant and ensure these are the correct tokens
		} else {
			return 1;
		}
	}
	
	// ------------------------- Game Stuff ------------------------------ //

	public synchronized int get_num_games() {
		return num_games;
	}
	
	public synchronized Game get_current_game() {
		return games[current_game];
	}
	
	public synchronized boolean game_started() {
		if (Constants.LIST_OF_ROUNDS[current_round_index]
				== Constants.NOT_STARTED) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Use the distribution of valences from the game's config to 
	 * generate a new valence
	 */
	public int get_payoff_valence() {
		int[] payoff_dist = get_current_game().get_payoff_dist();
		PayoffGenerator generator = new PayoffGenerator(payoff_dist[0], payoff_dist[1]);
		int valence = (int) generator.get_payoff(); // FIXME truncate if too low or too high
		return valence;
	}
	
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
		// Skip edge rounds if there are more games
		if (current_game < num_games) {
			current_round_index %= Constants.LIST_OF_ROUNDS.length - 2; // FIXME simplify
		}
		// If round overflowed then there is a new game
		if (current_round_index < previous_round_index) {
			current_round_index = 0;
			current_game++;
			// FIXME what changes to fire if all games are over
			pcs.firePropertyChange(Constants.NEW_GAME, current_game, null);
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
			} else if (event == Constants.REMOVE_PLAYER) {
				// TODO
			}
		}
	}
}
