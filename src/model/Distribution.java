package model;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Distribution {
	private int mean1;
	private int std_dev1;
	private int mean2;
	private int std_dev2;
	
	private int[] cdf;
	private int sumOfDists;

	public Distribution(int mean1, int std_dev1, 
						int mean2, int std_dev2) {
		this.mean1 = mean1;
		this.mean2 = mean2;
		this.std_dev1 = std_dev1;
		this.std_dev2 = std_dev2;
		
		NormalDistribution normal1 = new NormalDistribution(mean1, std_dev1);
		NormalDistribution normal2 = new NormalDistribution(mean2, std_dev2);
		
		cdf = new int[101];
		sumOfDists = 0;
		
		for (int i = 0; i < 101; i++) {
			int distributionPoint = (int) (100*(50*normal1.density(i) + 50*normal2.density(i)));
			sumOfDists += distributionPoint;
			cdf[i] = sumOfDists;
		}
	}
	
	public int[] getCDF() {
		return cdf;
	}
	
	public int getSumOfDists() {
		return sumOfDists;
	}
}
