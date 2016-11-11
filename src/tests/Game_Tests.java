package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Candidate;
import model.Distribution;
import model.Game;
import utils.Constants;

public class Game_Tests {
	private Game game;

	@Before
	public void setUp() throws Exception {
		HashMap<Integer, Candidate> candidates = new HashMap<Integer, Candidate>();
		candidates.put(0, new Candidate(0, 45));
		candidates.put(1, new Candidate(1, 20));
		candidates.put(2, new Candidate(2, 37));
		Distribution dist = new Distribution(new int[]{15, 5, 80, 10});
		game = new Game(1, candidates, dist, 100, new int[]{0,2});
		// Add votes
		game.vote(Constants.STRAW_VOTE, 0);
		game.vote(Constants.STRAW_VOTE, 0);
		game.vote(Constants.STRAW_VOTE, 0);
		game.vote(Constants.STRAW_VOTE, 2);
		game.vote(Constants.STRAW_VOTE, 2);
	}

	@After
	public void tearDown() throws Exception {
		game = null;
	}
	
	@Test
	public void test_vote_insertions() {
		Integer[] straw_votes = game.get_round_votes(Constants.STRAW_VOTE);
		assert straw_votes[0]==3 && straw_votes[1]==0 && straw_votes[2]==2;
		Integer[] first_votes = game.get_round_votes(Constants.FIRST_VOTE);
		assert first_votes[0]==0 && first_votes[1]==0 && first_votes[2]==0;
	}

	@Test
	public void test_straw_vote_percents() {
		int[] straw_votes = game.get_round_votes_percent(Constants.STRAW_VOTE);
		assert straw_votes[0]==((3*100)/5) && straw_votes[1]==0 && straw_votes[2]==((2*100)/5);
		int[] top_two = game.get_top_x_candidates(2, Constants.STRAW_VOTE);
		assert top_two[0]==0 && top_two[1]==2;
	}

}
