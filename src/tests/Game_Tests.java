package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Candidate;
import model.Constants;
import model.Distribution;
import model.Game;

public class Game_Tests {
	private Game game;

	@Before
	public void setUp() throws Exception {
		Candidate candidate = new Candidate(1, 'R', 45);
		HashMap<Integer, Candidate> candidates = new HashMap<Integer, Candidate>();
		candidates.put(candidate.get_candidate_number(), candidate);
		Distribution dist = new Distribution(new int[]{15, 5, 80, 10});
		game = new Game(1, candidates, dist, 100);
	}

	@After
	public void tearDown() throws Exception {
		game = null;
	}

	@Test
	public void test() {
		HashMap<Integer, Candidate> candidates = game.getCandidates();
		Candidate first = candidates.get(1);
		first.increment_round_votes(Constants.STRAW_VOTE);
		
		HashMap<Integer, Candidate> updatedCandidates = game.getCandidates();
		Candidate updatedFirst = updatedCandidates.get(1);
		
		assert updatedFirst.get_round_votes(Constants.STRAW_VOTE) == 1;
	}

}
