/**
 * All votes for a single game
 * Created by Max Buster
 */

package model.Votes;

import utils.Constants;

import java.util.HashMap;

public class GameVotes {
    private int game_num;
    private HashMap<String, RoundVotes> round_votes;

    public GameVotes(int game_num) {
        this.game_num = game_num;
        this.round_votes = new HashMap<>();

        this.round_votes.put(Constants.POLL, new RoundVotes(Constants.POLL));
        this.round_votes.put(Constants.PRIMARY, new RoundVotes(Constants.PRIMARY));
        this.round_votes.put(Constants.ELECTION, new RoundVotes(Constants.ELECTION));
    }

    public synchronized void vote(String round, int candidate_num, int player_num) {
        Vote vote = new Vote(candidate_num, player_num);
        round_votes.get(round).add_vote(vote);
    }

    public int get_game_num() { return game_num; }

    public RoundVotes get_round_votes(String which_round) {
        return round_votes.get(which_round);
    }
}
