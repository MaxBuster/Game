package model;

import java.util.HashMap;

import utils.Constants;

/**
 * Holds the info and candidates for a single game
 * @author Max Buster
 */

public class Game {
	private final int game_number;
	private final Distribution distribution; // Distribution of voters
	private final int budget; // Info purchase budget
	private HashMap<Integer, Candidate> candidates; // List of candidates in this game
	
	private int payoff_multiplier;
	private int payoff_intercept;
	
	private int[] payoff_dist;
	
	public Game(int game_number, 
				HashMap<Integer, Candidate> candidates, 
				Distribution distribution, 
				int budget,
				int[] payoff_dist) {
		this.game_number = game_number;
		this.candidates = candidates;
		this.distribution = distribution;
		this.budget = budget;
		this.payoff_dist = payoff_dist;
	}
	
	public void set_payoff_equation(int payoff_multiplier, int payoff_intercept) {
		this.payoff_multiplier = payoff_multiplier;
		this.payoff_intercept = payoff_intercept;
	}
	
	public int[] get_payoff_dist() {
		return payoff_dist;
	}
	
	/**
	 * Calculates winnings from the current game by taking the leftover budget and
	 * multiplying by a constant, adding the diff between the player's ideal point 
	 * and the candidate's (delta), and adding a constant factor
	 */
	public int calculate_payoffs(int delta, int leftover) {
		// FIXME equation?
		int payoffs = payoff_intercept - (delta * payoff_multiplier) + leftover;
		return payoffs;
	}

	public int getGameNumber() {
		return game_number;
	}

	public HashMap<Integer, Candidate> getCandidates() {
		return candidates;
	}
	
	public int[] get_votes(int round_num) {
		String round = Constants.LIST_OF_ROUNDS[round_num];
		int array_length = (candidates.size()*2) + 1;
		int[] votes = new int[array_length];
		int total_votes = 0;
		for (int i=0, j=1; i<candidates.size(); i++, j+=2) {
			Candidate c = candidates.get(i);
			votes[j-1] = c.get_candidate_number(); // FIXME numbering wrong?
			votes[j] = c.get_round_votes(round);
			total_votes += c.get_round_votes(round);
		}
		for (int i=1; i<votes.length; i+=2) {
			votes[i] = (votes[i]/total_votes)*100;
		}
		votes[votes.length-1] = round_num;
		return votes;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public int getBudget() {
		return budget;
	}
}
