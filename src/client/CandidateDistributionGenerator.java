package client;

import org.apache.commons.math3.distribution.BetaDistribution;

public class CandidateDistributionGenerator {
	
	public static double[] generate_data(int[] candidate_tokens) {
		int total_tokens = candidate_tokens[0];
		int one_tokens = candidate_tokens[1];
		BetaDistribution beta_dist = new BetaDistribution(one_tokens+1, total_tokens-one_tokens+1);
		double[] dataset = new double[100];
		for (int i = 0; i < dataset.length; i++) {
			double num = beta_dist.density((double)(i+.001)/100); // FIXME what was this .001 for?
			dataset[i] = num;
		}
		return dataset;
	}
}
