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
	private String current_round;
	
	private Game[] games;
	private int[] candidates_per_round;
	private int next_player_num;
	private ArrayList<Player> players;

	public Model(Game[] games, PropertyChangeSupport pcs) {
		this.pcs = pcs;
		this.pcs.addPropertyChangeListener(new ChangeListener());
		this.num_games = games.length;
		this.current_game = 0;
		this.current_round = Constants.NOT_STARTED;
		this.games = games;
		this.next_player_num = 0;
		this.players = new ArrayList<Player>();
		
		initialize_cands_per_round();
	}
	
	public synchronized Player new_player() {
		Player player = new Player(next_player_num, games.length, candidates_per_round);
		players.add(player);
		next_player_num++;
		return player;
	}
	
	public synchronized void set_player_info(Player player) {
		Distribution dist = games[current_game].getDistribution();
		int[] cdf = dist.getCDF();
		
		int ideal_point = 100;
		char party;
		
		int sum = cdf[cdf.length-1];
		int random_point = new Random().nextInt(sum); 
		for (int i=0; i<cdf.length; i++) {
			if (cdf[i] > random_point) {
				ideal_point = i-1; // FIXME corner cases?
				break;
			}
		}
		if (ideal_point < 50) {
			party = Constants.Party_2;
		} else {
			party = Constants.PARTY_1;
		}
		player.setPlayerInfo(ideal_point, party);
	}
	
	public synchronized void set_player_done(Player player) {
		player.setDone(true);
		for (Player p : players) {
			if (!p.isDone()) {
				return;
			}
		}
		end_round();
	}
	
	public synchronized void end_round() {
		String previous_round = current_round;
		increment_round();
		pcs.firePropertyChange(Constants.ROUND_OVER, previous_round, null);
		for (Player p : players) {
			p.setDone(false);
		}
	}
	
	public void initialize_cands_per_round() {
		candidates_per_round = new int[games.length];
		for (Game game : games) {
			int num_candidates_in_game = game.getCandidates().size();
			candidates_per_round[game.getGameNumber()] = num_candidates_in_game;
		}
	}
	
	public synchronized void vote_for_candidate(int[] vote_message) {
		int candidate_number = vote_message[0];
		Candidate candidate = games[current_game].getCandidates().get(candidate_number);
		candidate.increment_round_votes(current_round);
	}
	
	public synchronized boolean game_started() {
		if (current_round == Constants.NOT_STARTED) {
			return false;
		} else {
			return true;
		}
	}
	
	public synchronized int get_round_num() {
		for (int i=0; i<Constants.LIST_OF_ROUNDS.length; i++) {
			if (Constants.LIST_OF_ROUNDS[i].equals(current_round)) {
				return i;
			}
		}
		return -1;
	}
	
	public synchronized int get_num_games() {
		return num_games;
	}
	
	public synchronized Game get_current_game() {
		return games[current_game];
	}
	
	public synchronized String get_current_round() {
		return current_round;
	}
	
	public synchronized void increment_round() {
		int current_round_pos = 0;
		for (int i=0; i<Constants.LIST_OF_ROUNDS.length; i++) {
			if (Constants.LIST_OF_ROUNDS[i] == current_round) {
				current_round_pos = i;
			}
		}
		int next_round_pos = current_round_pos + 1;
		if (current_game < num_games) {
			next_round_pos %= Constants.LIST_OF_ROUNDS.length - 1;
		}
		if (next_round_pos < current_round_pos) {
			current_game++;
			next_round_pos++;
			pcs.firePropertyChange(Constants.NEW_GAME, Integer.toString(current_game), null);
		}
		this.current_round = Constants.LIST_OF_ROUNDS[next_round_pos];
		pcs.firePropertyChange(Constants.NEW_ROUND, next_round_pos, null);
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
	
	class ChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent PCE) {
			String event = PCE.getPropertyName();
			if (event == Constants.START_GAME) {
				increment_round();
			}
			/**
			 * TODO:
			 * Remove player
			 * Player removed due to io
			 */
		}
	}
}
