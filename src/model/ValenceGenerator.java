package model;

import org.apache.commons.math3.distribution.NormalDistribution;

public class ValenceGenerator {
	NormalDistribution dist;
	double mean;
	double std_dev;
	
	public ValenceGenerator(double mean, double std_dev) {
		dist = new NormalDistribution(mean, std_dev);
		this.mean = mean;
		this.std_dev = std_dev;
	}
	
	public double get_std_dev() {
		return std_dev;
	}
	
	public int get_max() {
		return (int) (3*std_dev);
	}
	
	public double get_payoff() {
		double sample = dist.sample();
		double upper_bound = mean + (3*std_dev);
		double lower_bound = mean - (3*std_dev);
		// Truncate sample if outside bounds
		if (sample > upper_bound) {
			sample = upper_bound;
		} else if (sample < lower_bound){
			sample = lower_bound;
		}
		return sample;
	}
	
	public double valence_prob(int valence) {
		return dist.cumulativeProbability(valence);
	}
}
