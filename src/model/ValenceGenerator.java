package model;

import org.apache.commons.math3.distribution.NormalDistribution;

public class ValenceGenerator {
	NormalDistribution dist;
	
	public ValenceGenerator(double mean, double std_dev) {
		dist = new NormalDistribution(mean, std_dev);
	}
	
	public double get_payoff() {
		return dist.sample();
	}
}
