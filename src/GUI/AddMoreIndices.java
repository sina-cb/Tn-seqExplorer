package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import essgenes.ProjectInfo;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.apache.log4j.Logger;

public class AddMoreIndices extends JFrame {

	private Logger logger = Logger.getLogger(AddMoreIndices.class.getName());
	private String tableName = "";
	private ProjectInfo info = null;
	private JFrame parentFrame = null;

	private JPanel contentPane;
	private JTextField addWinLenTxt;
	private JTextField addStepTxt;
	private JTextField adjustStartTxt;
	private JTextField adjustEndTxt;
	private JTextField addSeqLenTxt;
	private JComboBox<String> addLibraryCombo = new JComboBox<String>();

	/**
	 * Create the frame.
	 */
	public AddMoreIndices(String tableName, ProjectInfo info, JFrame parentFrame){
		this();

		this.tableName = tableName;
		this.info = info;
		this.parentFrame = parentFrame;

		setTitle("Add new essentiality indices to \"" + tableName + "\" table");
		setLocationRelativeTo(parentFrame);
		
		initializeAddPanel();
	}

	public AddMoreIndices() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 588, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 561, 340);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Add new data", null, panel, null);
		panel.setLayout(null);

		JLabel label = new JLabel("Choose your library:");
		label.setBounds(10, 14, 137, 14);
		panel.add(label);

		addLibraryCombo.setBounds(157, 11, 383, 20);
		panel.add(addLibraryCombo);

		addWinLenTxt = new JTextField();
		addWinLenTxt.setText("1000");
		addWinLenTxt.setColumns(10);
		addWinLenTxt.setBounds(157, 39, 86, 20);
		panel.add(addWinLenTxt);

		addStepTxt = new JTextField();
		addStepTxt.setText("100");
		addStepTxt.setColumns(10);
		addStepTxt.setBounds(454, 39, 86, 20);
		panel.add(addStepTxt);

		JLabel label_2 = new JLabel("Window Length:");
		label_2.setBounds(10, 42, 137, 14);
		panel.add(label_2);

		JLabel label_3 = new JLabel("Step:");
		label_3.setBounds(307, 42, 137, 14);
		panel.add(label_3);

		adjustStartTxt = new JTextField();
		adjustStartTxt.setText("%-20");
		adjustStartTxt.setColumns(10);
		adjustStartTxt.setBounds(157, 70, 86, 20);
		panel.add(adjustStartTxt);

		JLabel label_4 = new JLabel("Adjust Start:");
		label_4.setBounds(10, 73, 137, 14);
		panel.add(label_4);

		adjustEndTxt = new JTextField();
		adjustEndTxt.setText("%-20");
		adjustEndTxt.setColumns(10);
		adjustEndTxt.setBounds(454, 70, 86, 20);
		panel.add(adjustEndTxt);

		JLabel label_5 = new JLabel("Adjust End:");
		label_5.setBounds(307, 73, 137, 14);
		panel.add(label_5);

		addSeqLenTxt = new JTextField();
		addSeqLenTxt.setEditable(false);
		addSeqLenTxt.setColumns(10);
		addSeqLenTxt.setBounds(157, 101, 86, 20);
		panel.add(addSeqLenTxt);

		JLabel label_6 = new JLabel("Sequence Length:");
		label_6.setBounds(10, 104, 137, 14);
		panel.add(label_6);

		JButton addMoreColumnsBtn = new JButton("Add");
		addMoreColumnsBtn.setBounds(454, 100, 89, 23);
		panel.add(addMoreColumnsBtn);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Other", null, panel_1, null);
	}

	private void initializeAddPanel(){

		//Initializing Libraries ComboBox
				BufferedReader br = null;

				if(addLibraryCombo != null){
					addLibraryCombo.removeAllItems();
				}

				try{
					br = new BufferedReader(new FileReader(info.getFile()));

					String line = br.readLine();
					br.readLine();
					br.readLine();

					while(line != null){
						line = br.readLine();

						if (line != null)
							addLibraryCombo.addItem(line.substring(0, line.length() - 6));

						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
					}

				}catch(IOException e){
					logger.error(e.getMessage());
					return;
				}finally{
					try{
						br.close();
					}catch(IOException e){
						logger.error(e.getMessage());
						return;
					}
				}
				
				addSeqLenTxt.setText(info.getSequenceLen() + "");
	}

}
