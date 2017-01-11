/**
 * Configure and generate the bimodal voter distribution
 * Created by Max Buster
 */

package utils.Distributions;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Random;

public class VoterDistribution {
    private int std_dev_1;
    private int mean_1;
    private int std_dev_2;
    private int mean_2;

    private double[] pdf;
    private double[] cdf;
    private int pdf_integral;

    public VoterDistribution(int std_dev_1, int mean_1, int std_dev_2, int mean_2) {
        this.std_dev_1 = std_dev_1;
        this.mean_1 = mean_1;
        this.std_dev_2 = std_dev_2;
        this.mean_2 = mean_2;

        generate_data();
    }

    /**
     * @return A new position chosen randomly from the bimodal distribution
     */
    public int generate_voter_position() {
        int position = 100;
        int random_point = new Random().nextInt(pdf_integral);
        for (int i=1; i<cdf.length; i++) {
            if (cdf[i] > random_point) {
                position = i-1;
                break;
            }
        }
        return position;
    }

    /**
     * Combines two normal distributions with different parameters to generate the pdf and cdf of
     * a bimodal distribution
     */
    private void generate_data() {
        pdf = new double[100];
        cdf = new double[100];
        pdf_integral = 0;

        NormalDistribution normal1 = new NormalDistribution(mean_1, std_dev_1);
        NormalDistribution normal2 = new NormalDistribution(mean_2, std_dev_2);

        for (int i = 0; i < 100; i++) {
            double point_estimate = (25*normal1.density(i) + 25*normal2.density(i)); // FIXME multipliers?
            pdf_integral += point_estimate;
            pdf[i] = point_estimate;
            cdf[i] = pdf_integral;
        }
    }

    public double[] get_pdf() { return pdf; }

    public int get_std_dev_1() { return std_dev_1; }

    public int get_mean_1() { return mean_1; }

    public int get_std_dev_2() { return std_dev_2; }

    public int get_mean_2() { return mean_2; }
}