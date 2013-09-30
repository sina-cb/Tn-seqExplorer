package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.jfree.chart.ChartPanel;

public class PlotViewer extends JFrame {

	private JPanel contentPane;
	private JPanel plotPanel = new JPanel();
	
	/**
	 * Create the frame.
	 */
	public PlotViewer() {
		setResizable(false);
		setTitle("Plot");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 729, 554);
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
	}
	
	public void addPlot(ChartPanel chart){
		plotPanel.setLayout(new BorderLayout());
		plotPanel.add(chart, BorderLayout.CENTER);
		plotPanel.validate();
	}
	
	public void setPlotName(String title){
		setTitle(title);
	}
}
