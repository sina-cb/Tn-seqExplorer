package essgenes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class PlotData {

	private static Logger logger = Logger.getLogger(PlotData.class.getName());
	
	public static JFreeChart plotData(String libName, int windowLen, int windowStep, String title, ProjectInfo info, boolean onlyUniqueInsertions){
		
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
		
		XYDataset dataset = createDataset(xAxis, yAxis);
		JFreeChart chart = createChart(dataset, title, !onlyUniqueInsertions, null, null, null, null);
		
		try {
			saveToXls(xAxis, yAxis, info, libName, windowLen, windowStep, onlyUniqueInsertions);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return chart;
	}
	
	private static void saveToXls(Vector<Integer> xAxis, Vector<Integer> yAxis, ProjectInfo info, String libName, int len, int step, boolean unique) throws IOException{
		
		String fileName = libName + "_w" + len + "_s" + step;
		String xlsPath = "";
		
		if (unique)
			xlsPath = info.getPath() + fileName + " - Unique Insertions.xls";
		else
			xlsPath = info.getPath() + fileName + " - All Reads.xls";
		File xlsFile = new File(xlsPath);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(xlsFile));
		
		bw.write(String.format("%s\t%s\n", "Number of insertions within a window", "Number of windows with the given number of insertions"));
		
		for(int i = 0; i < xAxis.size(); i++){
			bw.write(String.format("%d\t%d\n", xAxis.get(i), yAxis.get(i)));
		}
		
		bw.close();
		
		String msg = String.format("An Excel file containg all the data for this chart has been created at this location: %s", xlsPath);
		JOptionPane.showMessageDialog(null, msg);
		
	}
	
	private static XYDataset createDataset(Vector<Integer> xAxis, Vector<Integer> yAxis){
		XYSeries series = new XYSeries("Plot");
		
		for(int i = 0; i < xAxis.size(); i++){
			series.add(xAxis.get(i), yAxis.get(i));
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		
		return dataset;
	}
	
	private static XYDataset createDataset(ArrayList<Double> xAxis, ArrayList<Double> yAxis){
		XYSeries series = new XYSeries("Plot");
		
		for(int i = 0; i < xAxis.size(); i++){
			series.add(xAxis.get(i), yAxis.get(i));
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		
		return dataset;
	}
	
	private static JFreeChart createChart(XYDataset dataset, String title, boolean ifReads, Double xStart, Double xEnd, Double yStart, Double yEnd){
	
		String xLabel;
		String yLabel;
		if (ifReads){
			xLabel = "Number of reads within a window";
			yLabel = "Number of windows with the given number of reads";
		}else{
			xLabel = "Number of insertions within a window";
			yLabel = "Number of windows with the given number of insertions";			
		}
		
		//		create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title,      // chart title
				xLabel,                      // x axis label
				yLabel,                      // y axis label
				dataset,                  // data
				PlotOrientation.VERTICAL,
				false,                     // include legend
				true,                     // tooltips
				false                     // urls
				);

		//		NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		//      final StandardLegend legend = (StandardLegend) chart.getLegend();
		//      legend.setDisplaySeriesShapes(true);

		//		get a reference to the plot for further customization...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		//		plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		if (xStart != null && xEnd != null){
			NumberAxis domain = (NumberAxis)plot.getDomainAxis();
			domain.setRange(xStart, xEnd);
		}
		
		if (yStart != null && yEnd != null){
			NumberAxis range = (NumberAxis)plot.getRangeAxis();
			range.setRange(yStart, yEnd);
		}
		
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesStroke(0, new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		//renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);

		//		change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//		OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	
	}
	
	public static JFreeChart plotColumns(String tableName, int firstCol, int secondCol, boolean logPlot, String title,boolean randomize, ProjectInfo info) throws IOException{
		
		File tableFile = new File(info.getPath() + tableName + ".table.xls"); //REPLACE
		BufferedReader br = new BufferedReader(new FileReader(tableFile));

		String xAxisName = "Column " + firstCol + " (";
		String yAxisName = "Column " + secondCol + " (";
		
		String line = br.readLine();
		ArrayList<String> tabs = AddColumns.tabsForCompare(line);
		if (tabs.get(firstCol + 7) != null && tabs.get(firstCol + 7).compareTo("") != 0){
			xAxisName = xAxisName + tabs.get(firstCol + 7) + ", "; 
		}
		
		if (tabs.get(secondCol + 7) != null && tabs.get(secondCol + 7).compareTo("") != 0){
			yAxisName = yAxisName + tabs.get(secondCol + 7) + ", ";
		}
		
		line = br.readLine();
		tabs = AddColumns.tabsForCompare(line);
		if (tabs.get(firstCol + 7) != null && tabs.get(firstCol + 7).compareTo("") != 0){
			xAxisName = xAxisName + tabs.get(firstCol + 7) + ", "; 
		}
		
		if (tabs.get(secondCol + 7) != null && tabs.get(secondCol + 7).compareTo("") != 0){
			yAxisName = yAxisName + tabs.get(secondCol + 7) + ", ";
		}
		
		line = br.readLine();
		tabs = AddColumns.tabsForCompare(line);
		if (tabs.get(firstCol + 7) != null && tabs.get(firstCol + 7).compareTo("") != 0){
			xAxisName = xAxisName + tabs.get(firstCol + 7) + ", "; 
		}
		
		if (tabs.get(secondCol + 7) != null && tabs.get(secondCol + 7).compareTo("") != 0){
			yAxisName = yAxisName + tabs.get(secondCol + 7) + ", ";
		}
		
		line = br.readLine();
		tabs = AddColumns.tabsForCompare(line);
		if (tabs.get(firstCol + 7) != null && tabs.get(firstCol + 7).compareTo("") != 0){
			xAxisName = xAxisName + tabs.get(firstCol + 7) + ", "; 
		}
		
		if (tabs.get(secondCol + 7) != null && tabs.get(secondCol + 7).compareTo("") != 0){
			yAxisName = yAxisName + tabs.get(secondCol + 7) + ", ";
		}
		
		line = br.readLine();
		tabs = AddColumns.tabsForCompare(line);
		if (tabs.get(firstCol + 7) != null && tabs.get(firstCol + 7).compareTo("") != 0){
			xAxisName = xAxisName + tabs.get(firstCol + 7) + ")"; 
		}
		
		if (tabs.get(secondCol + 7) != null && tabs.get(secondCol + 7).compareTo("") != 0){
			yAxisName = yAxisName + tabs.get(secondCol + 7) + ")";
		}

		ArrayList<Double> xAxis = new ArrayList<>();
		ArrayList<Double> yAxis = new ArrayList<>();
		
		//Reading Main Data and Process it
		line = br.readLine();
		while(line != null){
			tabs = AddColumns.tabsForCompare(line);
			
			double one = Double.parseDouble(tabs.get(firstCol + 7));
			double two = Double.parseDouble(tabs.get(secondCol + 7));
			
			xAxis.add(one);
			yAxis.add(two);
			
			line = br.readLine();
		}
		
		br.close();
		
		if (randomize){
			double lower = -0.25;
			double higher = 0.25;
			double constant = 1.0;
			double D = higher - lower;
			
			for (int i = 0; i < xAxis.size(); i++){
				double rand = (new Random()).nextDouble();
				double N = xAxis.get(i);
				N += (constant + rand * D + lower);
				xAxis.set(i, N);
			}

			for (int i = 0; i < yAxis.size(); i++){
				double rand = (new Random()).nextDouble();
				double N = yAxis.get(i);
				N += (constant + rand * D + lower);
				yAxis.set(i, N);
			}
		}
		
		XYDataset dataset = createDataset(xAxis, yAxis);
		JFreeChart chart = createScatterChart(dataset, logPlot, xAxisName, yAxisName, title);
		
		return chart;
	}

	private static JFreeChart createScatterChart(XYDataset dataset, boolean logPlot, String xName, String yName, String title) {
		final JFreeChart chart = ChartFactory.createScatterPlot(
				title,                  	// chart title
				xName,                      	// x axis label
				yName,                      	// y axis label
				dataset,                	// data
				PlotOrientation.VERTICAL,
				false,                     	// include legend
				true,                     	// Tool-tips
				false                     	// URLs
				);
		XYPlot plot = (XYPlot) chart.getPlot();
		
		if (logPlot){
			final LogAxis logAxisX = new LogAxis(xName);
			//logAxisX.setStandardTickUnits(LogAxis.createLogTickUnits(Locale.ENGLISH));
			//logAxisX.setRange(Math.log(maxX), Math.log(minX));
			plot.setDomainAxis(logAxisX);

			final LogAxis logAxisY = new LogAxis(yName);
			//logAxisY.setStandardTickUnits(LogAxis.createLogTickUnits(Locale.ENGLISH));
			//logAxisY.setRange(Math.log(maxY), Math.log(minY));
			plot.setRangeAxis(logAxisY);
		}
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShape(0, ShapeUtilities.createDiamond(1), true);
		plot.setRenderer(renderer);
		return chart;
	}
	
}
