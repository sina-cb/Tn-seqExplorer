package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;

@SuppressWarnings("serial")
public class PlotViewer extends JFrame {

	private JPanel contentPane;
	private JPanel plotPanel = new JPanel();
	private JTextField xStartTxt;
	private JTextField xEndTxt;
	private JTextField yStartTxt;
	private JTextField yEndTxt;
	
	/**
	 * Create the frame.
	 */
	public PlotViewer() {
		setResizable(false);
		setTitle("Plot");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 728, 655);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		plotPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		plotPanel.setBounds(10, 36, 700, 480);
		contentPane.add(plotPanel);
		
		JLabel lblResultPlot = new JLabel("Result plot:");
		lblResultPlot.setBounds(10, 11, 96, 14);
		contentPane.add(lblResultPlot);
		
		JLabel lblChangePlotRanges = new JLabel("Change plot ranges:");
		lblChangePlotRanges.setBounds(10, 527, 216, 14);
		contentPane.add(lblChangePlotRanges);
		
		JLabel lblXAxisStart = new JLabel("X Axis start:");
		lblXAxisStart.setBounds(20, 552, 111, 14);
		contentPane.add(lblXAxisStart);
		
		xStartTxt = new JTextField();
		xStartTxt.setText("0.0");
		xStartTxt.setBounds(140, 549, 86, 20);
		contentPane.add(xStartTxt);
		xStartTxt.setColumns(10);
		
		JLabel lblXAxisEnd = new JLabel("X Axis end:");
		lblXAxisEnd.setBounds(20, 580, 111, 14);
		contentPane.add(lblXAxisEnd);
		
		xEndTxt = new JTextField();
		xEndTxt.setText("2000.0");
		xEndTxt.setColumns(10);
		xEndTxt.setBounds(140, 577, 86, 20);
		contentPane.add(xEndTxt);
		
		JLabel lblYAxisStart = new JLabel("Y Axis start:");
		lblYAxisStart.setBounds(283, 555, 111, 14);
		contentPane.add(lblYAxisStart);
		
		yStartTxt = new JTextField();
		yStartTxt.setText("0.0");
		yStartTxt.setColumns(10);
		yStartTxt.setBounds(403, 552, 86, 20);
		contentPane.add(yStartTxt);
		
		JLabel lblYAxisEnd = new JLabel("Y Axis end:");
		lblYAxisEnd.setBounds(283, 577, 111, 14);
		contentPane.add(lblYAxisEnd);
		
		yEndTxt = new JTextField();
		yEndTxt.setText("300.0");
		yEndTxt.setColumns(10);
		yEndTxt.setBounds(403, 574, 86, 20);
		contentPane.add(yEndTxt);
		
		JButton replotBtn = new JButton("Re-Plot");
		replotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replot();
			}
		});
		replotBtn.setBounds(623, 576, 89, 23);
		contentPane.add(replotBtn);
	}
	
	public void replot(){
		
		Double xStart = null;
		
		if (xStartTxt.getText() != null || xStartTxt.getText().compareTo("") != 0){
			xStart = Double.parseDouble(xStartTxt.getText());
		}
		
		Double xEnd = null;
		
		if (xEndTxt.getText() != null || xEndTxt.getText().compareTo("") != 0){
			xEnd = Double.parseDouble(xEndTxt.getText());
		}
		
		Double yStart = null;
		
		if (yStartTxt.getText() != null || yStartTxt.getText().compareTo("") != 0){
			yStart = Double.parseDouble(yStartTxt.getText());
		}
		
		Double yEnd = null;
		
		if (yEndTxt.getText() != null || yEndTxt.getText().compareTo("") != 0){
			yEnd = Double.parseDouble(yEndTxt.getText());
		}
		
		ChartPanel panel = (ChartPanel) plotPanel.getComponent(0);
		JFreeChart chart = panel.getChart();
		XYPlot plot = chart.getXYPlot();
		
		if (xStart != null && xEnd != null){
			plot.getDomainAxis().setRange(xStart, xEnd);
		}
		
		if (yStart != null && yEnd != null){
			plot.getRangeAxis().setRange(yStart, yEnd);
		}
	}
	
	public void addPlot(ChartPanel chart){
		plotPanel.setLayout(new BorderLayout());
		plotPanel.add(chart, BorderLayout.CENTER);
		plotPanel.validate();
		
		//Initializing the default values for ranges
		JFreeChart chart1 = chart.getChart();
		XYPlot plot = chart1.getXYPlot();
		Range range = plot.getDomainAxis().getRange();
		xStartTxt.setText(range.getLowerBound() + "");
		xEndTxt.setText(range.getUpperBound() + "");
		range = plot.getRangeAxis().getRange();
		yStartTxt.setText(range.getLowerBound() + "");
		yEndTxt.setText(range.getUpperBound() + "");
	}
	
	public void setPlotName(String title){
		setTitle(title);
	}
}
