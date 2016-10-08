package model;

import java.util.HashMap;

import utils.Constants;

/**
 * Contains a candidate's data and number of votes
 * @author Max Buster
 */

public class Candidate {
	private final int candidate_number;
	private final int candidate_party;
	private final int ideal_point;
	private HashMap<String, Integer> votes; // Map from round name to # votes candidate received

	public Candidate(int candidate_number, int candidate_party, int ideal_point) {
		this.candidate_number = candidate_number;
		this.candidate_party = candidate_party;
		this.ideal_point = ideal_point;
		
		this.votes = new HashMap<String, Integer>();
		// Initialize all rounds to 0 votes
		votes.put(Constants.STRAW_VOTE, 0);
		votes.put(Constants.FIRST_VOTE, 0);
		votes.put(Constants.FINAL_VOTE, 0);
	}
	
	public int get_candidate_number() {
		return this.candidate_number;
	}
	
	public int get_candidate_party() {
		return this.candidate_party;
	}
	
	public int get_candidate_ideal_point() {
		return this.ideal_point;
	}
	
	/**
	 * Add one vote for this candidate for the round
	 * @param round to add a value for
	 */
	public synchronized void increment_round_votes(String round) {
		int round_votes = votes.get(round);
		round_votes += 1;
		votes.put(round, round_votes);
	}
	
	public synchronized int get_round_votes(String round) {
		return votes.get(round);
	}
}
