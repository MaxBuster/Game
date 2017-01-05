/**
 * Counts and analyzes the votes for a given round
 * Created by Max Buster
 */

package model.Votes;

import java.util.HashMap;

public class VoteCounter {

    /**
     * @param round_votes - the RoundVotes result for the current round
     * @return a map of candidate nums to round results
     */
    public static HashMap<Integer, VoteResults> get_vote_counts(RoundVotes round_votes) {
        HashMap<Integer, VoteResults> round_results = new HashMap<>();
        int sum_of_votes = 0;
        for (Vote vote : round_votes.get_votes()) {
            int cand_num = vote.get_candidate_num();

            /* Initialize key in map if it doesn't exist already */
            if (!round_results.containsKey(cand_num)) {
                round_results.put(cand_num, new VoteResults(cand_num));
            }

            round_results.get(cand_num).add_vote();
            sum_of_votes++;
        }

        /* Set the total votes in the result object so it can analyze them */
        for (VoteResults result : round_results.values()) {
            result.set_total_votes(sum_of_votes);
        }
        return round_results;
    }
}
