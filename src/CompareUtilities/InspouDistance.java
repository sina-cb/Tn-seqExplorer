package CompareUtilities;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class InspouDistance {

	public static void main(String[] args) {
		InspouDistance obj = new InspouDistance();
		obj.run();
	}

	public void run() {

		for (int i = 1; i <= 12; i++){
			String libName = String.format("lib%d-1", i);

			String extension = ".inspou";
			File input = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\inspou\\" + libName + extension);

			String extAll = " - all.xls"; 
			File outputAll = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\inspou\\results\\" + libName + extAll);

			String extSame = " - same.xls"; 
			File outputSame = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\inspou\\results\\" + libName + extSame);

			String extDiff = " - diff.xls"; 
			File outputDiff = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\inspou\\results\\" + libName + extDiff);

			processAll(input, outputAll, libName);
			processSame(input, outputSame, libName);
			processDiff(input, outputDiff, libName);

			input = new File("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\inspou\\" + libName + extension);
			String windowInsertions = " - windows with %d insertions.xls";
			String tempOutput = "C:\\Users\\sina\\Desktop\\Essential Genes\\data\\inspou\\results\\" + libName + windowInsertions;
			processWindow(input, tempOutput);
		}
	}

	private void processWindow(File input, String outputPath) {
		File output = null;
		try{
			BufferedReader br = new BufferedReader(new FileReader(input));

			String line = "";
			Vector<Integer> numberOfInsertions = new Vector<Integer>();
			Vector<Integer> positions = new Vector<Integer>();

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
			br.close();

			Vector<Integer> insertions = new Vector<Integer>();
			Vector<Integer> windowsCoordinates = new Vector<Integer>();
			int currentPosition = 0;

			int windowLen = 200;
			int windowStep = 200;
			while(currentPosition < positions.get(positions.size() - 1)){
				int tempCount = 0;

				for (int i = 0; i < numberOfInsertions.size(); i++){
					int tempPos = positions.get(i);
					if(tempPos >= currentPosition && tempPos < currentPosition + windowLen){
						tempCount++;
					}

					if(tempPos > currentPosition + windowLen){
						break;
					}
				}

				insertions.add(tempCount);
				windowsCoordinates.add(currentPosition);
				currentPosition += windowStep; 
			}

			for (int j = 0; j < 5; j++){
				output = new File(String.format(outputPath, j));
				output.delete();
				output.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(output));
				bw.write("Start..End\tNumber of Reads\n");
				for (int i = 0; i < windowsCoordinates.size(); i++){
					if (insertions.get(i) == j){
						bw.write(windowsCoordinates.get(i) + ".." + (windowsCoordinates.get(i) + 200) + "\t" + insertions.get(i) + "\n");
					}
				}

				bw.close();

			}

		}catch(IOException e){
			//System.out.println("IOException");
			if (output != null)
				output.delete();
		}
	}

	private void processDiff(File input, File output, String libname) {
		try{
			output.delete();
			output.createNewFile();

			BufferedReader br = new BufferedReader(new FileReader(input));
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));

			String line1 = br.readLine();
			String line2 = br.readLine();

			while(line2 != null && findSign(line1).equals(findSign(line2))){
				line1 = line2;
				line2 = br.readLine();
			}

			int[] counts = new int[1000000];
			int maxDiff = Integer.MIN_VALUE;
			while(line2 != null){
				String num1String = line1.substring(0, line1.indexOf("\t"));
				String num2String = line2.substring(0, line2.indexOf("\t"));

				int num1 = Integer.parseInt(num1String);
				int num2 = Integer.parseInt(num2String);

				int diff = num2 - num1;

				counts[diff]++;

				if (diff > maxDiff){
					maxDiff = diff;
				}

				line1 = line2;
				line2 = br.readLine();

				while(line2 != null && findSign(line1).equals(findSign(line2))){
					line1 = line2;
					line2 = br.readLine();
				}
			}

			bw.write("Diff.\tCount\n");
			for (int i = 0; i <= maxDiff; i++){
				bw.write(i + "\t" + counts[i] + "\n");
			}

			br.close();
			bw.close();

			saveChart(output, libname);
		}catch(IOException e){
			System.out.println("IOException");
			output.delete();
		}
	}

	private void processSame(File input, File output, String libname) {
		try{
			output.delete();
			output.createNewFile();

			BufferedReader br = new BufferedReader(new FileReader(input));
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));

			// Process Plus Signs
			String line1 = br.readLine();
			while (line1 != null && findSign(line1).equals("-")){
				line1 = br.readLine();
			}
			String line2 = br.readLine();
			while (line2 != null && findSign(line2).equals("-")){
				line2 = br.readLine();
			}

			int[] countsPlus = new int[1000000];
			int maxDiffPlus = Integer.MIN_VALUE;
			while(line2 != null){
				String num1String = line1.substring(0, line1.indexOf("\t"));
				String num2String = line2.substring(0, line2.indexOf("\t"));

				int num1 = Integer.parseInt(num1String);
				int num2 = Integer.parseInt(num2String);

				int diff = num2 - num1;

				countsPlus[diff]++;

				if (diff > maxDiffPlus){
					maxDiffPlus = diff;
				}

				line1 = line2;
				line2 = br.readLine();
				while (line2 != null && findSign(line2).equals("-")){
					line2 = br.readLine();
				}
			}

			br.close();
			bw.close();

			br = new BufferedReader(new FileReader(input));
			bw = new BufferedWriter(new FileWriter(output));

			// Process Plus Signs
			line1 = br.readLine();
			while (line1 != null && findSign(line1).equals("+")){
				line1 = br.readLine();
			}
			line2 = br.readLine();
			while (line2 != null && findSign(line2).equals("+")){
				line2 = br.readLine();
			}

			int[] countsMinus = new int[1000000];
			int maxDiffMinus = Integer.MIN_VALUE;
			while(line2 != null){
				String num1String = line1.substring(0, line1.indexOf("\t"));
				String num2String = line2.substring(0, line2.indexOf("\t"));

				int num1 = Integer.parseInt(num1String);
				int num2 = Integer.parseInt(num2String);

				int diff = num2 - num1;

				countsMinus[diff]++;

				if (diff > maxDiffMinus){
					maxDiffMinus = diff;
				}

				line1 = line2;
				line2 = br.readLine();
				while (line2 != null && findSign(line2).equals("+")){
					line2 = br.readLine();
				}
			}

			int maxDiff = maxDiffPlus > maxDiffMinus ? maxDiffPlus : maxDiffMinus;

			bw.write("Diff.\tCount\n");
			for (int i = 0; i <= maxDiff; i++){
				bw.write(i + "\t" + (countsPlus[i] + countsMinus[i]) + "\n");
			}

			br.close();
			bw.close();

			saveChart(output, libname);
		}catch(IOException e){
			System.out.println("IOException");
			output.delete();
		}
	}

	private void processAll(File input, File output, String libname) {
		try{
			output.delete();
			output.createNewFile();

			BufferedReader br = new BufferedReader(new FileReader(input));
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));

			String line1 = br.readLine();
			String line2 = br.readLine();

			int[] counts = new int[1000000];
			int maxDiff = Integer.MIN_VALUE;
			while(line2 != null){
				String num1String = line1.substring(0, line1.indexOf("\t"));
				String num2String = line2.substring(0, line2.indexOf("\t"));

				int num1 = Integer.parseInt(num1String);
				int num2 = Integer.parseInt(num2String);

				int diff = num2 - num1;

				counts[diff]++;

				if (diff > maxDiff){
					maxDiff = diff;
				}

				line1 = line2;
				line2 = br.readLine();
			}

			bw.write("Diff.\tCount\n");
			for (int i = 0; i <= maxDiff; i++){
				bw.write(i + "\t" + counts[i] + "\n");
			}

			br.close();
			bw.close();

			saveChart(output, libname);
		}catch(IOException e){
			System.out.println("IOException");
			output.delete();
		}
	}

	private String findSign(String line){
		String sign = line.substring(line.indexOf("\t") + 1);
		sign = sign.substring(0, sign.indexOf("\t"));
		return sign;
	}

	private void saveChart(File input, String libname) {
		String outPath = input.getAbsolutePath().substring(0, input.getAbsolutePath().indexOf(".xls")) + ".png";
		File output = null;
		try {
			output = new File(outPath);
			output.delete();
			output.createNewFile();

			BufferedReader br = new BufferedReader(new FileReader(input));
			String line = br.readLine();
			line = br.readLine();

			ArrayList<Double> xAxis = new ArrayList<>();
			ArrayList<Double> yAxis = new ArrayList<>();

			int i = 0;
			int max = 500;
			while(line != null){
				xAxis.add(Double.parseDouble(line.substring(0, line.indexOf("\t"))));
				line = line.substring(line.indexOf("\t") + 1);
				yAxis.add(Double.parseDouble(line));
				line = br.readLine();
				i++;

				if (i > max){
					break;
				}
			}
			br.close();

			/*HistogramDataset dataset = new HistogramDataset();
			dataset.setType(HistogramType.FREQUENCY);
			dataset.addSeries("", Doubles.toArray(yAxis), xAxis.size());*/

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (i = 0; i < yAxis.size(); i++){
				dataset.addValue(yAxis.get(i), "Column", i + "");
			}

			String plotTitle = "Histogram for library " + libname; 
			String xaxis = "Distance";
			String yaxis = "Count"; 

			final JFreeChart chart = ChartFactory.createBarChart(
					plotTitle, 
					xaxis, 
					yaxis, 
					dataset,                 
					PlotOrientation.VERTICAL,
					false,
					false,
					false 
					);

			chart.setBackgroundPaint(Color.white);

			// get a reference to the plot for further customisation...
			final CategoryPlot plot = chart.getCategoryPlot();
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinesVisible(false);
			plot.setRangeGridlinesVisible(false);
			

			int width = max * 20;
			int height = max * 10; 

			ChartUtilities.saveChartAsPNG(output, chart, width, height);
		} catch (IOException e) {
			if (output != null){
				output.delete();
			}
		}
	}		

}
