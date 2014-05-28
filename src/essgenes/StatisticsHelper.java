package essgenes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.util.Pair;
import org.apache.log4j.Logger;

import com.google.common.primitives.Doubles;

public class StatisticsHelper {
	
	private static Logger logger = Logger.getLogger(StatisticsHelper.class.getName());
	
	/**
	 * Computes the best fitting power law distribution for the input x and y arrays.
	 * This is done using the method explained here,
	 * 		Weisstein, Eric W. "Least Squares Fitting--Power Law." From MathWorld--A Wolfram Web Resource. 
	 * 		http://mathworld.wolfram.com/LeastSquaresFittingPowerLaw.html
	 * 
	 * @param x x values
	 * @param y y values
	 * @return The fitness of the input values compared to the best fitting power law distribution
	 */
	public static Double powerLawLeastSquareFitting(double[] x, double[] y){
		Double fitness = 0.0;
		
		// Check data compatibility
		if (!compabilityCheck(x, y)){
			return 1.0;
		}
		
		// If they are compatible, remove all zeros in both x and y arrays
		Pair<double[], double[]> result = removeZeros(x, y);
		x = result.getFirst();
		y = result.getSecond();

		// Temp Variables
		Double t1 = 0.0;
		Double t2 = 0.0;
		Double t3 = 0.0;
		Double t4 = 0.0;
		Double t5 = 0.0;
		Double N = (double) x.length;
		
		// First, calculate B
		for (int i = 0; i < N; i++){
			Double lnx = Math.log(x[i]);
			Double lny = Math.log(y[i]);
			
			t1 += (lnx * lny);
			t2 += (lnx);
			t3 += (lny);
			t4 += (Math.pow(lnx, 2));
			t5 += (lnx);
		}
		
		Double B = 0.0;
		B = (N * t1 - t2 * t3) / (N * t4 - Math.pow(t5, 2));
		
		// Calculate A
		Double A = Math.pow(Math.E, (t3 - B * t2) / (N));
		
		logger.debug("y = Ax^B \n\tA: " + A + "\n\tB: " + B);
		
		Double R2 = 0.0;
		
		for (int i = 0; i < N; i++){
			Double temp = A * Math.pow(x[i], B);
			R2 += (Math.pow(y[i] - temp, 2));
		}
		
		fitness = 1 / R2;
		
		logger.debug("\tR2: " + R2);
		logger.debug("\tFitness: " + fitness);
		
		return fitness;
	}
	
	/**
	 * Computes the best fitting exponential distribution for the input x and y arrays.
	 * This is done using the method explained here,
	 * 		Weisstein, Eric W. "Least Squares Fitting--Exponential." From MathWorld--A Wolfram Web Resource.
	 * 		http://mathworld.wolfram.com/LeastSquaresFittingExponential.html
	 * 
	 * This method implements formulas (3) and (4) in the referenced webpage.
	 * 
	 * @param x x values
	 * @param y y values
	 * @return The fitness of the input values compared to the best fitting exponential distribution
	 */
	public static Double exponentialLeastSquareFitting(double[] x, double[] y){
		// Check data compatibility
		if (!compabilityCheck(x, y)){
			return 1.0;
		}

		// If they are compatible, remove all zeros in both x and y arrays
		Pair<double[], double[]> result = removeZeros(x, y);
		x = result.getFirst();
		y = result.getSecond();
		
		// Temp Variables
		Double t1 = 0.0;
		Double t2 = 0.0;
		Double t3 = 0.0;
		Double t4 = 0.0;
		Double t5 = 0.0;
		Double t6 = 0.0;
		Double t7 = 0.0;
		Double N = (double) x.length;
		
		for (int i = 0; i < N; i++){
			Double lny = Math.log(y[i]);
			
			t1 += (Math.pow(x[i], 2) * y[i]);
			t2 += (y[i] * lny);
			t3 += (x[i] * y[i]);
			t4 += (x[i] * y[i] * lny);
			t5 += (y[i]);
			t6 += (Math.pow(x[i], 2) * y[i]);
			t7 += (x[i] * y[i]);
		}
		
		Double A = Math.exp((t1 * t2 - t3 * t4) / (t5 * t6 - Math.pow(t7, 2)));
		Double B = (t5 * t4 - t3 * t2) / (t5 * t6 - Math.pow(t7, 2));
		
		logger.debug("y = A e ^ Bx");
		logger.debug("\tA: " + A);
		logger.debug("\tB: " + B);

		Double mean = (new Mean()).evaluate(y);
		Double SStot = 0.0;
		Double SSres = 0.0;
		Double R2 = 0.0;
		
		for (int i = 0; i < N; i++){
			Double f = A * Math.exp(B * x[i]);
			SStot += Math.pow(y[i] - mean, 2);
			SSres += Math.pow(y[i] - f, 2);
		}
		
		R2 = 1 - SSres / SStot;
		
		logger.debug("\tR2: " + R2);
		
		return R2;
	}
	
	public static Double exponentialLeastSquareFitting(List<Integer> x, List<Integer> y){
		return exponentialLeastSquareFitting(Doubles.toArray(x), Doubles.toArray(y));
	}
		
	private static Pair<double[], double[]> removeZeros(double[] x, double[] y) {
		List<Double> tempX = new ArrayList<>();
		List<Double> tempY = new ArrayList<>();
		
		for (int i = 0; i < x.length; i++){
			if (x[i] == 0){
				continue;
			}else if(y[i] == 0){
				break;
				/*tempX.add(x[i]);
				tempY.add(0.2);*/
			}else{
				tempX.add(x[i]);
				tempY.add(y[i]);
			}
		}
		
		return new Pair<double[], double[]>(Doubles.toArray(tempX), Doubles.toArray(tempY));
	}

	private static boolean compabilityCheck(double[] x, double[] y) {
		if (x.length != y.length){
			return false;
		}
		
		/*int zeroCount = 0;
		for (int i = 0; i < x.length; i++){
			if (x[i] == 0 || y[i] == 0){
				zeroCount++;
			}
		}*/
		
		/*if (((double)zeroCount / x.length) > 0.2){
			return false;
		}*/
		
		return true;
	}

	public static Double powerLawLeastSquareFitting(List<Integer> x, List<Integer> y){
		return powerLawLeastSquareFitting(Doubles.toArray(x), Doubles.toArray(y));
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

	public static Pair<Integer, Integer> findOptimalLength(String libName, ProjectInfo info, boolean onlyUniqueInsertions) {

		double maxTests = 200;
		int maxWinStepSize = 100;
		
		double lowerWinLen = 400;
		double higherWinLen = 1200;
		double winStep = 50;
		double lowerBoundThreshold = 0.80;
		double higherBoundThreshold = 0.92;
		
		double thresholdStep = (higherBoundThreshold - lowerBoundThreshold) / ((higherWinLen - lowerWinLen) / winStep);
		
		
		BufferedReader br = null;
		String line = "";
		Vector<Integer> numberOfInsertions = new Vector<Integer>();
		Vector<Integer> positions = new Vector<Integer>();
		
		try{
			
			br = new BufferedReader(new FileReader(info.getPath() + libName + ".inspou"));
			
			line = br.readLine();
			while(line != null){
				String tempLine = new String(line);
				int tempPos = Integer.parseInt(tempLine.substring(0, tempLine.indexOf("\t")));
				tempLine = tempLine.substring(tempLine.indexOf("\t") + 1);
				int tempNumOfIns = Integer.parseInt(tempLine.substring(tempLine.indexOf("\t") + 1));
			
				positions.add(tempPos);
				numberOfInsertions.add(tempNumOfIns);
				
				line = br.readLine();
			}
			
		}catch(IOException e){
			logger.error(e.getMessage());
			return null;
		}finally{
			try{
				br.close();
			}catch(IOException e){
				logger.error(e.getMessage());
				return null;
			}
		}
		
		int testCount = 0;
		int tempWinLen = 50;
		int tempWinStep = 10 < (tempWinLen / 4) ? (tempWinLen / 4) : 10;
		Double R2 = 1.0;
		
		Double threshold = lowerBoundThreshold;
		while(R2 > threshold){
			logger.debug("Winlen == " + tempWinLen);
			logger.debug("Winstep == " + tempWinStep);
			
			Pair<double[], double[]> data = processData(positions, numberOfInsertions, tempWinLen, tempWinStep, onlyUniqueInsertions);
			R2 = exponentialLeastSquareFitting(data.getFirst(), data.getSecond());
			
			testCount++;
			
			if (testCount > maxTests){
				break;
			}
			
			tempWinLen += winStep;
			tempWinStep = 10 < (tempWinLen / 4) ? (tempWinLen / 4) : 10;
			tempWinStep = tempWinStep > maxWinStepSize ? maxWinStepSize : tempWinStep;
			
			threshold = tempWinLen > 400 ? (threshold + thresholdStep) : threshold;
			threshold = tempWinLen > 1400 ? higherBoundThreshold : threshold;
			logger.debug("Threshhold #" + testCount + ": " + threshold);
		}
		
		tempWinStep = 10 > (tempWinLen / 10) ? (tempWinLen / 10) : 10;
		
		return new Pair<>(tempWinLen, tempWinStep);
	}
	
	private static Pair<double[], double[]> processData(List<Integer> positions, List<Integer> numberOfInsertions, int windowLen, int windowStep, boolean onlyUniqueInsertions){
		Vector<Integer> insertions = new Vector<Integer>();
		int maxInsertions = 0;
		int currentPosition = 0;
		
		while(currentPosition < positions.get(positions.size() - 1)){
			int tempCount = 0;
			
			for (int i = 0; i < numberOfInsertions.size(); i++){
				int tempPos = positions.get(i);
				if(tempPos >= currentPosition && tempPos < currentPosition + windowLen){
					if (onlyUniqueInsertions)
						tempCount++;
					else
						tempCount += numberOfInsertions.get(i);
				}
				
				if(tempPos > currentPosition + windowLen){
					break;
				}
			}
			
			insertions.add(tempCount);
			if(tempCount > maxInsertions){
				maxInsertions = tempCount;
			}
			
			currentPosition += windowStep; 
		}
		
		Vector<Integer> xAxis = new Vector<Integer>();
		Vector<Integer> yAxis = new Vector<Integer>();
		
		for (int i = 0; i < maxInsertions + 1; i++){
			yAxis.addElement(0);
		}
		
		for(int i = 0; i < insertions.size(); i++){
			yAxis.setElementAt(yAxis.get(insertions.get(i)) + 1, insertions.get(i));
		}
		
		for(int i = 0; i < yAxis.size(); i++){
			xAxis.add(i);
		}
		
		return new Pair<double[], double[]>(Doubles.toArray(xAxis), Doubles.toArray(yAxis));
	}
	
}
