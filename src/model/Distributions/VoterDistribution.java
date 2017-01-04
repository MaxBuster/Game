package model.Distributions;

/**
 * Created by Max Buster on 1/3/2017.
 */
public class VoterDistribution {
    private int std_dev_1;
    private int mean_1;
    private int std_dev_2;
    private int mean_2;

    public VoterDistribution(int std_dev_1, int mean_1, int std_dev_2, int mean_2) {
        this.std_dev_1 = std_dev_1;
        this.mean_1 = mean_1;
        this.std_dev_2 = std_dev_2;
        this.mean_2 = mean_2;
    }
}
