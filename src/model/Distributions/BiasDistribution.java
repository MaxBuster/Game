/**
 * Creates a normal distribution which can be sampled to get candidate biases
 * Created by Max Buster
 */

package model.Distributions;

import org.apache.commons.math3.distribution.NormalDistribution;

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
     * @return a randomly chosen bias from the normal distribution, truncated at 3 std devs
     */
    public double generate_bias() {
        double sample = bias_distribution.sample();
        double upper_bound = mean + (3*std_dev);
        double lower_bound = mean - (3*std_dev);

        /* Truncate sample if outside bounds */
        if (sample > upper_bound) {
            sample = upper_bound;
        } else if (sample < lower_bound){
            sample = lower_bound;
        }
        return sample;
    }
}
