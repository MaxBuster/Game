package model;

import java.util.HashMap;

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
