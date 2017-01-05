package model;

import model.Distributions.BiasDistribution;
import model.Distributions.VoterDistribution;

import java.util.ArrayList;
import java.util.Random;

public class Player {
	private final int player_number;
	private int winnings;
	private ArrayList<PlayerGameInfo> pgi_list;
	private boolean done_with_round;
	
	public Player(int player_number) {
		this.player_number = player_number;
		winnings = 0;
		pgi_list = new ArrayList<PlayerGameInfo>();
		done_with_round = false;
	}
	
	public PlayerGameInfo new_pgi(Game game) {
		int ideal_point = new_ideal_point(game);
		int num_candidates = game.get_candidates().size();
		int[] valences = new int[num_candidates];
		for (int i=0; i<num_candidates; i++) {
			valences[i] = get_payoff_valence(game);
		}
		int budget = game.get_budget();
		PlayerGameInfo pgi = new PlayerGameInfo(ideal_point, budget, valences);
		this.pgi_list.add(pgi);
		return pgi;
	}
	
	public PlayerGameInfo get_pgi(int game_num) {
		return pgi_list.get(game_num);
	}
	
	public int num_games_played() {
		return pgi_list.size();
	}
	
	public int new_ideal_point(Game game) {
		VoterDistribution voter_distribution = game.get_voter_distribution();
		double[] cdf = voter_distribution.get_cdf();
		double max_value = voter_distribution.get_pdf_integral();

		int ideal_point = 100;
		double random_point = new Random().nextInt((int) max_value); // FIXME ranges?
		for (int i=1; i<cdf.length; i++) {
			if (cdf[i] > random_point) {
				ideal_point = i-1; // FIXME corner cases?
				break;
			}
		}
		return ideal_point;
	}
	
	public int get_payoff_valence(Game game) {
		BiasDistribution bias_distribution = game.get_bias_distribution();
		int bias = (int) bias_distribution.generate_bias();
		return bias;
	}

	public int getPlayer_number() {
		return player_number;
	}
	
	public void addWinnings(int payoff) {
		winnings += payoff;
	}
	
	public int getWinnings() {
		return winnings;
	}
	
	public synchronized void setDone(boolean done) {
		this.done_with_round = done;
	}
	
	public synchronized boolean isDone() {
		return this.done_with_round;
	}
}
