package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Random;

public class Model {
	private PropertyChangeSupport pcs;
	private int num_games;
	private int current_game;
	private String current_round;
	private ArrayList<String> list_of_rounds;
	
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
		
		initialize_rounds();
		initialize_cands_per_round();
	}
	
	public synchronized Player new_player() {
		Player player = new Player(next_player_num, games.length, candidates_per_round);
		players.add(player);
		next_player_num++;
		return player;
	}
	
	public synchronized void set_player_info(Player player) {
		int ideal_point = 0;
		char party;
		
		Distribution dist = games[current_game].getDistribution();
		int[] cdf = dist.getCDF();
		int sum = cdf[cdf.length-1];
		int random_point = new Random().nextInt(sum); // FIXME should this be an int, double, float?
		for (int i=0; i<cdf.length; i++) {
			if (cdf[i] <= random_point) {
				ideal_point = cdf[i];
				break;
			}
		}
		if (ideal_point < .5) {
			party = Constants.Party_2;
		} else {
			party = Constants.PARTY_1;
		}
		player.setPlayerInfo(ideal_point, party);
	}
	
	public void initialize_rounds() {
		this.list_of_rounds = new ArrayList<String>();
		list_of_rounds.add(Constants.NOT_STARTED);
		list_of_rounds.add(Constants.FIRST_BUY);
		list_of_rounds.add(Constants.STRAW_VOTE);
		list_of_rounds.add(Constants.FIRST_VOTE);
		list_of_rounds.add(Constants.SECOND_BUY);
		list_of_rounds.add(Constants.FINAL_VOTE);
		list_of_rounds.add(Constants.ALL_FINISHED);
	}
	
	public void initialize_cands_per_round() {
		candidates_per_round = new int[games.length];
		for (Game game : games) {
			int num_candidates_in_game = game.getCandidates().size();
			candidates_per_round[game.getGameNumber()] = num_candidates_in_game;
		}
	}
	
	public synchronized boolean game_started() {
		if (current_round == Constants.NOT_STARTED) {
			return false;
		} else {
			return true;
		}
	}
	
	public synchronized void increment_game() {
		current_game++;
	}
	
	public synchronized void increment_round() {
		int current_round_pos = list_of_rounds.indexOf(current_round);
		int next_round_pos = current_round_pos + 1;
		if (current_game < num_games) {
			next_round_pos %= list_of_rounds.size() - 1;
		}
		if (next_round_pos < current_round_pos) {
			next_round_pos++;
		}
		this.current_round = list_of_rounds.get(next_round_pos);
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
			 * New player
			 * New round
			 * New game
			 * Game over
			 * Write data
			 * End game
			 * All games over
			 */
		}
	}
}
