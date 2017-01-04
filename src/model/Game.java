/**
 * Holds the info and candidates for a single game
 * @author Max Buster
 */

package model;

import java.util.ArrayList;
import java.util.Random;

import model.Distributions.BiasDistribution;
import model.Distributions.VoterDistribution;

public class Game {
	private int game_num;
	private ArrayList<Candidate> candidates;
	private VoterDistribution voter_distribution;
	private BiasDistribution bias_distribution;
	private int budget;

	private int payoff_multiplier;
	private int payoff_max;

	/**
	 * Constructor used to define a new game with all necessary parameters
	 */
	public Game(int game_num, ArrayList<Candidate> candidates, VoterDistribution voter_distribution,
				BiasDistribution bias_distribution, int budget) {
		this.game_num = game_num;
		this.candidates = candidates;
		this.voter_distribution = voter_distribution;
		this.bias_distribution = bias_distribution;
		this.budget = budget;

		// TODO set a vote object to keep track
	}

	/**
	 * Builder pattern method to set the payoff multiplier on instantiation
	 * @param payoff_multiplier
	 * @return self to add additional parameters
	 */
	public Game with_payoff_multiplier(int payoff_multiplier) {
		this.payoff_multiplier = payoff_multiplier;
		return this;
	}

	/**
	 * Builder pattern method to set the payoff max on instantiation
	 * @param payoff_max
	 * @return self to add additional parameters
	 */
	public Game with_payoff_max(int payoff_max) {
		this.payoff_max = payoff_max;
		return this;
	}

//	// Increments the number of votes for the candidate that round
//	public synchronized void vote(String round, int candidate_num) {
//		Integer[] round_votes = candidate_votes.get(round);
//		round_votes[candidate_num] = round_votes[candidate_num]+1;
//	}
//
//	// Get vote array mostly for testing
//	public synchronized Integer[] get_round_votes(String round) {
//		return candidate_votes.get(round).clone();
//	}
//
//	// Deep copy votes for that round
//	public synchronized int[] get_round_votes_percent(String round) {
//		Integer[] integer_round_votes = candidate_votes.get(round);
//		int vote_sum = 0;
//		for (int i=0; i<integer_round_votes.length; i++) {
//			vote_sum += integer_round_votes[i];
//		}
//		int[] int_round_votes = new int[integer_round_votes.length];
//		for (int i=0; i<int_round_votes.length; i++) {
//			int_round_votes[i] = (integer_round_votes[i]*100)/vote_sum;
//		}
//		return int_round_votes;
//	}
//
//	// Gets top x candidates by votes for a specific round
//	public synchronized int[] get_top_x_candidates(int num_candidates, String round) {
//		int[] round_votes = get_round_votes_percent(round);
//		int[] top_x = new int[num_candidates];
//		for (int i=0; i<num_candidates; i++) {
//			int current_max = -1;
//			int current_candidate = -1;
//			for (int j=0; j<round_votes.length; j++) { // Find current max value
//				if (round_votes[j] > current_max) {
//					current_max = round_votes[j];
//					current_candidate = j;
//				} else if (round_votes[j] == current_max) {
//					/* Pick one of the two candidates randomly if tied */
//					if (new Random().nextBoolean()) {
//						current_max = round_votes[j];
//						current_candidate = j;
//					}
//				}
//			}
//			round_votes[current_candidate] = -1; // Set previous max value to -1
//			top_x[i] = current_candidate;
//		}
//		return top_x;
//	}


	//--------------------- Game field getters -------------------------//

	public int get_game_num() { return game_num; }

	public ArrayList<Candidate> get_candidates() { return candidates; }
	
	public VoterDistribution get_voter_distribution() { return voter_distribution; }

	public BiasDistribution get_bias_distribution() { return bias_distribution; }

	public int get_budget() { return budget; }

	public int get_payoff_multiplier() { return payoff_multiplier; }

	public int get_payoff_max() { return payoff_max; }
}
