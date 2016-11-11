package model;

import java.util.HashMap;

import utils.Constants;

/**
 * Contains a candidate's data and number of votes
 * @author Max Buster
 */

public class Candidate {
	private final int candidate_number;
	private final int ideal_point;

	public Candidate(int candidate_number, int ideal_point) {
		this.candidate_number = candidate_number;
		this.ideal_point = ideal_point;
	}
	
	public int get_candidate_number() {
		return this.candidate_number;
	}
	
	public int get_candidate_ideal_point() {
		return this.ideal_point;
	}
}
