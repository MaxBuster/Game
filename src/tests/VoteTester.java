/**
 * Tests classes related to vote counting and analysis
 * Created by Max Buster
 */

package tests;

import model.Votes.GameVotes;
import model.Votes.RoundVotes;
import model.Votes.VoteCounter;
import model.Votes.VoteResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Constants.Constants;

import java.util.*;

public class VoteTester {
    private GameVotes game_votes;
    private int[] num_votes_per_candidate;
    private int total_votes;
    private RoundVotes poll_votes;
    private HashMap<Integer, VoteResults> poll_results;
    private List<VoteResults> list_results;

    @Before
    public void setup() {
        game_votes = new GameVotes(0);
        num_votes_per_candidate = new int[]{2,3,1,5};
        total_votes = 0;

        /* Add votes to the poll vote round */
        for (int i=0; i<num_votes_per_candidate.length; i++) {
            total_votes += num_votes_per_candidate[i];
            for (int j=0; j<num_votes_per_candidate[i]; j++) {
                game_votes.vote(Constants.POLL,i,0);
            }
        }

        poll_votes = game_votes.get_round_votes(Constants.POLL);
        poll_results = VoteCounter.get_vote_counts(poll_votes);

        list_results = new ArrayList<VoteResults>();
        list_results.addAll(poll_results.values());
    }

    @After
    public void teardown() {

    }

    @Test
    public void test_vote_count() {
        for (int i=0; i<num_votes_per_candidate.length; i++) {
            assert poll_results.get(i).get_num_votes() == num_votes_per_candidate[i];

            int actual_percent_votes = (100*num_votes_per_candidate[i])/total_votes;
            assert poll_results.get(i).get_percent_votes() == actual_percent_votes;
        }
    }

    @Test
    public void test_sort_by_votes() {
        Collections.sort(list_results, VoteResults.get_vote_desc_comparator());

        for (int i=1; i<list_results.size(); i++) {
            assert list_results.get(i).get_num_votes() <= list_results.get(i-1).get_num_votes();
        }
    }

    @Test
    public void test_sort_by_cand_num() {
        Collections.sort(list_results, VoteResults.get_cand_num_asc_comparator());

        for (int i=1; i<list_results.size(); i++) {
            assert list_results.get(i).get_candidate_num() >= list_results.get(i-1).get_candidate_num();
        }
    }
}
