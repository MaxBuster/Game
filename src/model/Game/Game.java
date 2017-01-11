/**
 * Holds the info and candidates for a single game
 * @author Max Buster
 */

package model.Game;

import java.util.ArrayList;

import model.Candidate.Candidate;
import utils.Distributions.BiasDistribution;
import utils.Distributions.VoterDistribution;

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

	//--------------------- Game field getters -------------------------//

	public int get_game_num() { return game_num; }

	public ArrayList<Candidate> get_candidates() { return candidates; }
	
	public VoterDistribution get_voter_distribution() { return voter_distribution; }

	public BiasDistribution get_bias_distribution() { return bias_distribution; }

	public int get_budget() { return budget; }

	public int get_payoff_multiplier() { return payoff_multiplier; }

	public int get_payoff_max() { return payoff_max; }
}
