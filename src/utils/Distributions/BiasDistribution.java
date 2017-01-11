/**
 * Creates a normal distribution which can be sampled to get candidate biases
 * Created by Max Buster
 */

package utils.Distributions;

import model.Player.Bias;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;

public class BiasDistribution {
    private int std_dev;
    private int mean;
    private NormalDistribution bias_distribution;

    public BiasDistribution(int std_dev, int mean) {
        this.std_dev = std_dev;
        this.mean = mean;
        this.bias_distribution = new NormalDistribution(mean, std_dev);
    }

    /**
     * @param num_biases - The number of biases to generate
     * @return a list of bias objects chosen from this distribution
     */
    public ArrayList<Bias> generate_list_of_biases(int num_biases) {
        ArrayList<Bias> biases = new ArrayList<Bias>();
        for (int candidate_num=0; candidate_num<num_biases; candidate_num++) {
            biases.add(generate_bias(candidate_num));
        }
        return biases;
    }

    /**
     * @return a randomly chosen bias from the normal distribution, truncated at 3 std devs
     */
    private Bias generate_bias(int candidate_num) {
        int sample = (int) bias_distribution.sample();
        int upper_bound = mean + (3*std_dev);
        int lower_bound = mean - (3*std_dev);

        /* Truncate sample if outside bounds */
        if (sample > upper_bound) {
            sample = upper_bound;
        } else if (sample < lower_bound){
            sample = lower_bound;
        }
        return new Bias(candidate_num, sample);
    }
}
