package model;

import org.apache.commons.math3.distribution.NormalDistribution;

public class PayoffGenerator {
	NormalDistribution dist;
	
	public PayoffGenerator(double mean, double std_dev) {
		dist = new NormalDistribution(mean, std_dev);
	}
	
	public double get_payoff() {
		return dist.sample();
	}
}
