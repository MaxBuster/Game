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
	private String round; // The name of the current round
	
	public Game(int game_number, 
				HashMap<Integer, Candidate> candidates, 
				Distribution distribution, 
				int budget) {
		this.game_number = game_number;
		this.candidates = candidates;
		this.distribution = distribution;
		this.budget = budget;
		this.round = Constants.FIRST_BUY;
	}

	public int getGameNumber() {
		return game_number;
	}

	public HashMap<Integer, Candidate> getCandidates() {
		return candidates;
	}
	
	public int[] get_votes(String round) {
		int[] votes = new int[candidates.size()*2];
		int total_votes = 0;
		for (int i=0, j=1; i<candidates.size(); i++, j+=2) {
			Candidate c = candidates.get(i);
			votes[i] = c.get_candidate_number();
			votes[j] = c.get_round_votes(round);
			total_votes += c.get_round_votes(round);
		}
		for (int i=1; i<votes.length; i+=2) {
			votes[i] = (votes[i]/total_votes)*100;
		}
		return votes;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public int getBudget() {
		return budget;
	}

	public synchronized String getRound() {
		return round;
	}
	
	public synchronized void setRound(String round) {
		this.round = round;
	}
}
