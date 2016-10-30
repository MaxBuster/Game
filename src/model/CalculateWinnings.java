package model;

public class CalculateWinnings {
	public static int calculate_winnings(Player player, Candidate candidate, 
			Game game, int leftover_budget) {
		int player_ideal = player.getIdeal_point();
		int player_valence = player.get_valence_for_cand(candidate.get_candidate_number());
		int candidate_point = candidate.get_candidate_ideal_point();
		
		int delta = Math.abs(player_ideal - candidate_point) + player_valence;
		int payoff = game.calculate_payoffs(delta, leftover_budget);
		return payoff;
	}
}
