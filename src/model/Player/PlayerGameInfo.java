/**
 * Contains player info for a single game
 */

package model.Player;

import java.util.ArrayList;

import model.Candidate.Candidate;
import model.Candidate.CandidateInfo;
import model.Game.Game;
import utils.Constants.Constants;

public class PlayerGameInfo {
	private Game game;
	private int voter_position;
	private int budget;
	private ArrayList<Bias> biases;
	private int winnings;

	public PlayerGameInfo(Game game) {
		this.game = game;
		this.voter_position = game.get_voter_distribution().generate_voter_position();
		this.biases = game.get_bias_distribution().generate_list_of_biases(game.get_candidates().size());
		this.budget = game.get_budget();
		this.winnings = 0;
	}

	public void purchase_bias(int candidate_num) {
		this.budget -= Constants.INFO_PRICE;
		this.biases.get(candidate_num).purchase(); // FIXME need round, need map?
	}

    public ArrayList<CandidateInfo> get_expected_payoffs() {
		ArrayList<CandidateInfo> candidate_info = new ArrayList<CandidateInfo>();
        for (Candidate candidate : game.get_candidates()) {
            int candidate_num = candidate.get_candidate_num();
            int candidate_position = candidate.get_position();
            int expected_payoff = get_expected_payoff(candidate);
            boolean bias_purchased = biases.get(candidate_num).is_purchased();

            candidate_info.add(new CandidateInfo(candidate_num, candidate_position, expected_payoff, bias_purchased));
        }
        return candidate_info;
    }

    public int get_expected_payoff(Candidate candidate) {
        int max = game.get_payoff_max();
        int multiplier = game.get_payoff_multiplier();
        int delta = Math.abs(candidate.get_position() - voter_position);

        Bias bias = biases.get(candidate.get_candidate_num());
        int bias_amount = bias.is_purchased() ? bias.get_bias_amount() : 0;

        return max - (delta*multiplier) + bias_amount + budget;
    }

	public int get_voter_position() { return voter_position; }

	public void set_winnings(int winnings) { this.winnings = winnings; }
	
	public int get_winnings() { return winnings; }
}
