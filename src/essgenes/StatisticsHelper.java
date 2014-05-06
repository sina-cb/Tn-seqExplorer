package essgenes;

import java.util.List;

import com.google.common.primitives.Doubles;

public class StatisticsHelper {
	
	public static Double powerLawFitness(double[] sample){
		Double fitness = 0.0;

		
		
		return fitness;
	}
	
	public static Double powerLawFitness(List<Integer> sample){
		return powerLawFitness(Doubles.toArray(sample));
	}
	
	public static Double bimodalityCoefficient(double[] sample){
		Double b = 0.0;
		
		Double g = sampleSkewness(sample);
		Double k = sampleExcessKurtosis(sample);
		
		Double temp = (3 * Math.pow(sample.length - 1, 2)) / ((sample.length - 2) * (sample.length - 3));
		b = (Math.pow(g, 2) + 1) / (k + temp);
		
		return b;
	}
	
	public static Double bimodalityCoefficient(List<Integer> sample){
		return bimodalityCoefficient(Doubles.toArray(sample));
	}
	
	public static Double sampleExcessKurtosis(double[] sample){
		Double g = 0.0;
		
		Double mean = mean(sample);
		
		Double m4 = 0.0;
		Double m2 = 0.0;
		for (Double d : sample){
			m4 += Math.pow(d - mean, 4);
			m2 += Math.pow(d - mean, 2);
		}
		m4 *= (1.0 / sample.length);
		m2 *= (1.0 / sample.length);
		m2 = Math.pow(m2, 2);
		
		g = (m4 / m2) - 3;
		
		return g;
	}
	
	public static Double sampleExcessKurtosis(List<Integer> sample){
		return sampleExcessKurtosis(Doubles.toArray(sample));
	}
	
	public static Double sampleSkewness(double[] sample){
		Double g = 0.0;
		Double mean = mean(sample);
		
		Double m3 = 0.0;
		Double m2 = 0.0;
		for (Double d : sample){
			m3 += Math.pow(d - mean, 3);
			m2 += Math.pow(d - mean, 2);
		}
		m3 *= (1.0 / sample.length);
		m2 *= (1.0 / sample.length);
		m2 = Math.pow(m2, (3.0 / 2.0));
		
		g = m3 / m2;
		
		return g;
	}
	
	public static Double sampleSkewness(List<Integer> sample){
		return sampleSkewness(Doubles.toArray(sample));
	}
	
	private static Double mean(double[] sample){
		Double mean = 0.0;
		
		for (Double d : sample){
			mean += d;
		}
		
		mean /= ((double) sample.length);
		
		return mean;
	}
	
}
