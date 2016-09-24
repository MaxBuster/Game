package client;

import org.apache.commons.math3.distribution.NormalDistribution;

public class VoterDistribution {

	public static double[] getData(int[] graphData) {
		int mean1 = graphData[0];
		int stdDev1 = graphData[1];
		int mean2 = graphData[2];
		int stdDev2 = graphData[3];
		
		double[] data = new double[100];
		NormalDistribution normal1 = new NormalDistribution(mean1, stdDev1);
		NormalDistribution normal2 = new NormalDistribution(mean2, stdDev2);
		for (int i = 0; i < 100; i++) {
			double num = (50*normal1.density(i) + 50*normal2.density(i)); // FIXME multipliers?
			data[i] = num;
		}
		return data;
	}
}