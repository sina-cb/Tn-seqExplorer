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

import org.jdesktop.swingx.JXLabel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Font;

@SuppressWarnings("serial")
public class PlotViewer extends JFrame {

	public JLabel plotInfo = new JXLabel("New label");
	public boolean hasChartListener = false;
	private JPanel contentPane;
	private JPanel plotPanel = new JPanel();
	private JTextField xStartTxt;
	private JTextField xEndTxt;
	private JTextField yStartTxt;
	private JTextField yEndTxt;
	private JPanel parametersPanel = new JPanel();
	private JPanel panel = new JPanel();
	
	/**
	 * Create the frame.
	 */
	public PlotViewer() {
		setResizable(false);
		setTitle("Plot");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 769, 702);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		plotPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		plotPanel.setBounds(10, 36, 743, 480);
		contentPane.add(plotPanel);
		
		JLabel lblResultPlot = new JLabel("Result plot:");
		lblResultPlot.setBounds(10, 11, 96, 14);
		contentPane.add(lblResultPlot);
		
		parametersPanel.setBounds(10, 575, 743, 89);
		contentPane.add(parametersPanel);
		parametersPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("155px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(47dlu;default)"),
				ColumnSpec.decode("67px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("59px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(41dlu;default)"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("61px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("23px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("32px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("89px"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("23px"),}));
		
		JLabel lblChangePlotRanges = new JLabel("Change plot ranges:");
		parametersPanel.add(lblChangePlotRanges, "1, 2, left, center");
		
		JLabel lblXAxisStart = new JLabel("X Axis start:");
		parametersPanel.add(lblXAxisStart, "3, 4, left, center");
		
		xStartTxt = new JTextField();
		parametersPanel.add(xStartTxt, "4, 4, left, top");
		xStartTxt.setText("0.0");
		xStartTxt.setColumns(10);
		
		JLabel lblXAxisEnd = new JLabel("X Axis end:");
		parametersPanel.add(lblXAxisEnd, "8, 4, left, center");
		
		xEndTxt = new JTextField();
		parametersPanel.add(xEndTxt, "10, 4, left, top");
		xEndTxt.setText("2000.0");
		xEndTxt.setColumns(10);
		
		JLabel lblYAxisStart = new JLabel("Y Axis start:");
		parametersPanel.add(lblYAxisStart, "3, 6, left, center");
		
		yStartTxt = new JTextField();
		parametersPanel.add(yStartTxt, "4, 6, left, top");
		yStartTxt.setText("0.0");
		yStartTxt.setColumns(10);
		
		JLabel lblYAxisEnd = new JLabel("Y Axis end:");
		parametersPanel.add(lblYAxisEnd, "8, 6, left, center");
		
		yEndTxt = new JTextField();
		parametersPanel.add(yEndTxt, "10, 6, right, center");
		yEndTxt.setText("300.0");
		yEndTxt.setColumns(10);
		
		JButton replotBtn = new JButton("Re-Plot");
		parametersPanel.add(replotBtn, "16, 6, left, top");
		
		panel.setBounds(10, 527, 743, 37);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		plotInfo.setFont(new Font("Tahoma", Font.BOLD, 13));
		plotInfo.setText("Hover Points in the chart to get some information about it if available.");
		
		panel.add(plotInfo, BorderLayout.CENTER);
		((JXLabel)plotInfo).setLineWrap(true);
		replotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replot();
			}
		});
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
		if (chart1.getPlot() instanceof XYPlot){
			XYPlot plot = chart1.getXYPlot();
			Range range = plot.getDomainAxis().getRange();
			xStartTxt.setText(range.getLowerBound() + "");
			xEndTxt.setText(range.getUpperBound() + "");
			range = plot.getRangeAxis().getRange();
			yStartTxt.setText(range.getLowerBound() + "");
			yEndTxt.setText(range.getUpperBound() + "");
		}else{
			for (int i = 0; i < parametersPanel.getComponentCount(); i++){
				parametersPanel.getComponent(i).setEnabled(false);
			}
		}
		
		if (!hasChartListener){
			panel.setVisible(false);
		}
	}
	
	public void setPlotName(String title){
		setTitle(title);
	}
}
