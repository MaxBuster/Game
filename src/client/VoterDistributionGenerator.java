package client;

import org.apache.commons.math3.distribution.NormalDistribution;

public class VoterDistributionGenerator {

	public static double[] generate_data(int[] voter_dist) {
		int mean1 = voter_dist[0];
		int stdDev1 = voter_dist[1];
		int mean2 = voter_dist[2];
		int stdDev2 = voter_dist[3];
		
		double[] data = new double[100];
		NormalDistribution normal1 = new NormalDistribution(mean1, stdDev1);
		NormalDistribution normal2 = new NormalDistribution(mean2, stdDev2);
		for (int i = 0; i < 100; i++) {
			double num = (25*normal1.density(i) + 25*normal2.density(i)); // FIXME multipliers?
			data[i] = num;
		}
		return data;
	}
}
