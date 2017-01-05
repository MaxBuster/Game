/**
 * Contains votes for all candidates for a single round
 * Created by Max Buster
 */

package model.Votes;

import java.util.ArrayList;

public class RoundVotes {
    private String round_name;
    private ArrayList<Vote> votes;

    public RoundVotes(String round_name) {
        this.round_name = round_name;
        this.votes = new ArrayList<>();
    }

    public void add_vote(Vote vote) {
        votes.add(vote);
    }

    public ArrayList<Vote> get_votes() {
        return votes;
    }
}
