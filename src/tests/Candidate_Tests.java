package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Candidate;
import utils.Constants;

public class Candidate_Tests {
	private Candidate candidate;

	@Before
	public void setUp() throws Exception {
		this.candidate = new Candidate(1, 45);
	}

	@After
	public void tearDown() throws Exception {
		this.candidate = null;
	}

	@Test
	public void testVotesIncrement() {
		int straw_votes = 3;
		int first_votes = 2;
		int final_votes = 4;
		for (int i=0; i<straw_votes; i++) {
			candidate.increment_round_votes(Constants.STRAW_VOTE);
		}
		for (int i=0; i<first_votes; i++) {
			candidate.increment_round_votes(Constants.FIRST_VOTE);
		}
		for (int i=0; i<final_votes; i++) {
			candidate.increment_round_votes(Constants.FINAL_VOTE);
		}
		
		assert candidate.get_round_votes(Constants.STRAW_VOTE) == straw_votes;
		assert candidate.get_round_votes(Constants.FIRST_VOTE) == first_votes;
		assert candidate.get_round_votes(Constants.FINAL_VOTE) == final_votes;
	}

}
