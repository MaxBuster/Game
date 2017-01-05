/**
 * Contains a single vote for a candidate from a single player
 * Created by Max Buster
 */

package model.Votes;

public class Vote {
    private int candidate_num;
    private int player_num;

    public Vote(int candidate_num, int player_num) {
        this.candidate_num = candidate_num;
        this.player_num = player_num;
    }

    public int get_candidate_num() { return candidate_num; }

    public int get_player_num() { return player_num; }
}
