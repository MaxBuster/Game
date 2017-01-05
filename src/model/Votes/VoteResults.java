/**
 * Contains vote results and analysis for one candidate for one round
 * Created by Max Buster
 */

package model.Votes;

import java.util.Comparator;
import java.util.Random;

public class VoteResults {
    private int candidate_num;
    private int num_votes;
    private int total_votes;

    public VoteResults(int candidate_num) {
        this.candidate_num = candidate_num;
        this.num_votes = 0;
        this.total_votes = 0;
    }

    public void add_vote() {
        num_votes++;
    }

    public void set_total_votes(int sum_of_votes) {
        this.total_votes = sum_of_votes;
    }

    public int get_percent_votes() {
        return (num_votes*100)/total_votes;
    }

    public int get_candidate_num() { return candidate_num; }

    public int get_num_votes() { return num_votes; }

    public static Comparator<VoteResults> get_vote_desc_comparator() {
        return new Comparator<VoteResults>() {
            public int compare(VoteResults a, VoteResults b) {
                if (a.num_votes < b.num_votes) {
                    return 1;
                } else if (a.num_votes > b.num_votes) {
                    return -1;
                } else {
                    /* Break tie randomly */
                    if (new Random().nextBoolean()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        };
    }

    public static Comparator<VoteResults> get_cand_num_asc_comparator() {
        return new Comparator<VoteResults>() {
            public int compare(VoteResults a, VoteResults b) {
                if (a.candidate_num > b.candidate_num) {
                    return 1;
                } else if (a.candidate_num < b.candidate_num) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
    }
}
