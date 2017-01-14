/**
 * A container for all candidate info to be transferred from server to client
 * Created by Max Buster
 */

package model.Candidate;

import java.io.Serializable;

public class CandidateInfo implements Serializable {
    public int candidate_num;
    public int candidate_position;
    public int expected_value;
    public boolean value_purchased;

    public CandidateInfo(int candidate_num, int candidate_position, int expected_value, boolean value_purchased) {
        this.candidate_num = candidate_num;
        this.candidate_position = candidate_position;
        this.expected_value = expected_value;
        this.value_purchased = value_purchased;
    }
}
