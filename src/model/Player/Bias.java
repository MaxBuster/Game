/**
 * Contains the bias towards a candidate that a single player has
 * Created by Max Buster
 */

package model.Player;

public class Bias {
    private int candidate_num;
    private int bias_amount;
    private boolean purchased;

    public Bias(int candidate_num, int bias_amount) {
        this.candidate_num = candidate_num;
        this.bias_amount = bias_amount;
        this.purchased = false;
    }

    public int get_candidate_num() { return candidate_num; }

    public int get_bias_amount() { return bias_amount; }

    public void purchase() { purchased = true; }

    public boolean is_purchased() { return purchased; }
}
