package model;

import java.util.HashMap;

import utils.Constants;

public class PlayerGameInfo {
	private int ideal_point;
	private int budget;
	private int[] valences;
	private boolean[] purchased_valences_1;
	private boolean[] purchased_valences_2;
	private HashMap<String, Integer> votes;
	private int winnings;

	public PlayerGameInfo(int ideal_point, int budget, int[] valences) {
		this.ideal_point = ideal_point;
		this.budget = budget;
		this.valences = valences;

		this.purchased_valences_1 = new boolean[valences.length];
		this.purchased_valences_2 = new boolean[valences.length];
		this.votes = new HashMap<String, Integer>();
		this.winnings = 0;
	}

	// Add indicator that valence for that candidate was purchased
	public void purchase_valence(String round, int candidate_num) {
		this.budget-=Constants.INFO_PRICE;
		if (round == Constants.FIRST_BUY) {
			purchased_valences_1[candidate_num] = true;
		} else {
			purchased_valences_2[candidate_num] = true;
		}
	}

	// Set the vote for that round with the candidate #
	public void vote(String round, int candidate_num) {
		votes.put(round, candidate_num);
	}
	
	public int[] get_valences() {
		return valences;
	}
	
	public boolean[] get_purchases(String round) {
		if (round == Constants.FIRST_BUY) {
			return purchased_valences_1;
		} else {
			return purchased_valences_2;
		}
	}
	
	public int[] get_votes() {
		int[] votes = new int[3];
		votes[0] = this.votes.get(Constants.POLL);
		votes[1] = this.votes.get(Constants.PRIMARY);
		votes[2] = this.votes.get(Constants.ELECTION);
		return votes;
	}
	
	public int get_winnings() {
		return winnings;
	}

	// Get ideal point for that round
	public int get_ideal_pt() {
		return this.ideal_point;
	}
	
	// Get the expected payoff for a specific candidate
	public int get_expected_payoff(Game game, int candidate_num) {
		int max = game.get_payoff_max();
		int multiplier = game.get_payoff_multiplier();
		Candidate candidate = game.get_candidates().get(candidate_num);
		int candidate_ideal = candidate.get_position();
		int delta = Math.abs(candidate_ideal-ideal_point);
		int valence = valences[candidate_num];
		return max - (delta*multiplier) + valence + budget;
	}

	// Get expected payoffs for each candidate given purchased valences
	public int[] get_expected_payoffs(Game game) {
		int[] expected_payoffs = new int[game.get_candidates().size()];
		for (Candidate c : game.get_candidates()) {
			int candidate_num = c.get_candidate_num();
			expected_payoffs[candidate_num] = get_expected_payoff(game, candidate_num);
		}
		return expected_payoffs;
	}

	// Calculate the winnings for the winning candidate
	public int set_winnings(Game game, int winning_candidate) {
		int max = game.get_payoff_max();
		int multiplier = game.get_payoff_multiplier();
		Candidate winner = game.get_candidates().get(winning_candidate);
		int candidate_num = winner.get_candidate_num();
		int candidate_ideal = winner.get_position();
		int delta = Math.abs(candidate_ideal-ideal_point);
		int valence = valences[candidate_num];
		this.winnings = max - (delta*multiplier) + valence + budget;
		return winnings;
	}
}
