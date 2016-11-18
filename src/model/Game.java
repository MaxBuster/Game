package model;

import java.util.Arrays;
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
	private ValenceGenerator valenceGenerator;
	
	private HashMap<String, Integer[]> candidate_votes; // Keeps track of candidate votes each game
	
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
		this.valenceGenerator = new ValenceGenerator(payoff_dist[0], payoff_dist[1]);
		
		// Initialize vote map for each round
		candidate_votes = new HashMap<String, Integer[]>();
		int num_candidates = candidates.size();
		Integer[] empty_array = new Integer[num_candidates];
		Arrays.fill(empty_array, 0);
		candidate_votes.put(Constants.POLL, empty_array.clone());
		candidate_votes.put(Constants.PRIMARY, empty_array.clone());
		candidate_votes.put(Constants.ELECTION, empty_array.clone());
	}
	
	// Increments the number of votes for the candidate that round
	public synchronized void vote(String round, int candidate_num) {
		Integer[] round_votes = candidate_votes.get(round);
		round_votes[candidate_num] = round_votes[candidate_num]+1;
	}
	
	// Get vote array mostly for testing
	public synchronized Integer[] get_round_votes(String round) {
		return candidate_votes.get(round).clone();
	}
	
	// Deep copy votes for that round
	public synchronized int[] get_round_votes_percent(String round) {
		Integer[] integer_round_votes = candidate_votes.get(round);
		int vote_sum = 0;
		for (int i=0; i<integer_round_votes.length; i++) {
			vote_sum += integer_round_votes[i];
		}
		int[] int_round_votes = new int[integer_round_votes.length];
		for (int i=0; i<int_round_votes.length; i++) {
			int_round_votes[i] = (integer_round_votes[i]*100)/vote_sum;
		}
		return int_round_votes;
	}
	
	// Gets top x candidates by votes for a specific round
	public synchronized int[] get_top_x_candidates(int num_candidates, String round) {
		int[] round_votes = get_round_votes_percent(round);
		int[] top_x = new int[num_candidates];
		for (int i=0; i<num_candidates; i++) { 
			int current_max = -1;
			int current_candidate = -1;
			for (int j=0; j<round_votes.length; j++) { // Find current max value 
				if (round_votes[j] > current_max) {
					current_max = round_votes[j];
					current_candidate = j;
				}
			}
			round_votes[current_candidate] = -1; // Set previous max value to -1
			top_x[i] = current_candidate;
		}
		return top_x;
	}
	
	public ValenceGenerator get_val_gen() {
		return valenceGenerator;
	}
	
	public void set_payoff_equation(int payoff_multiplier, int payoff_intercept) {
		this.payoff_multiplier = payoff_multiplier;
		this.payoff_intercept = payoff_intercept;
	}
	
	public int get_max() {
		return payoff_intercept;
	}
	
	public int get_multiplier() {
		return payoff_multiplier;
	}
	
	public int[] get_payoff_dist() {
		return payoff_dist;
	}

	public int getGameNumber() {
		return game_number;
	}

	public HashMap<Integer, Candidate> getCandidates() {
		return candidates;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public int getBudget() {
		return budget;
	}
}
