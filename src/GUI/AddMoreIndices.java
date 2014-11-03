package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXLabel;
import org.jfree.chart.ChartPanel;

import CustomGUIComponent.BoldCellRenderer;
import CustomGUIComponent.IntegerInputVerifier;
import CustomGUIComponent.MyChartMouseListener;
import CustomGUIComponent.PercentageInputVerifier;
import essgenes.AddColumns;
import essgenes.Messages;
import essgenes.PlotData;
import essgenes.ProjectInfo;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class AddMoreIndices extends JFrame {

	private Logger logger = Logger.getLogger(AddMoreIndices.class.getName());
	private String tableName = "";
	private boolean logPlot = false;
	private ProjectInfo info = null;
	@SuppressWarnings("unused")
	private JFrame parentFrame = null;

	private JRadioButton step2UniqueRadio = new JRadioButton("Count only unique insertions");
	private JRadioButton step2AllRadio = new JRadioButton("Count all sequence reads");
	private JButton step2PlotBtn = new JButton("Plot");
	private JComboBox<String> step2ColumnCombo = new JComboBox<String>();
	private JLabel step2ErrorLbl = new JLabel("Please enter a valid value in the red fields.");
	private JLabel step2WaitLbl = new JLabel("Please wait...");
	private JComboBox<String> step2Combo = new JComboBox<String>();	
	private JLabel errorMsg3Lbl = new JLabel("Enter a valid value.");
	private JLabel errorMsg2Lbl = new JLabel("Please enter a valid value in the red fields.");
	private JLabel errorMsg1Lbl = new JLabel("Please enter a valid value in the red fields.");
	private JPanel contentPane;
	private JTextField addWinLenTxt;
	private JTextField addStepTxt;
	private JTextField adjustStartTxt;
	private JTextField adjustEndTxt;
	private JTextField addSeqLenTxt;
	private JComboBox<String> addLibraryCombo = new JComboBox<String>();
	private JTextField tableNameTxt;
	private JLabel lblPleaseWait = new JLabel("Please wait...");
	private JButton addMoreColumnsBtn = new JButton("Execute");
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JComboBox<String> countInsLibCombo = new JComboBox<String>();
	private JTextField countInsAdjStartTxt;
	private JTextField countInsAdjEndTxt;
	private JButton countInsBtn = new JButton("Count");
	private JLabel countInsPleaseWaitLbl = new JLabel("Please wait...");
	private JTable compareTable;
	private JScrollPane scrollPane = new JScrollPane();
	private JTextField compareMaxInsTxt;
	private JComboBox<String> columnOneCombo = new JComboBox<>();
	private JComboBox<String> columnTwoCombo = new JComboBox<>();
	private JButton compareBtn = new JButton("Compare");
	private JLabel compareWaitLbl = new JLabel("Please wait...");
	private JCheckBox logPlotCheck = new JCheckBox("Logarithmic Plot");
	private JRadioButton newDataUniqueInsertionRadio = new JRadioButton("Count only unique insertions");
	private JRadioButton newDataAllReadsRadio = new JRadioButton("Count all sequence reads");
	private JRadioButton countInsUniqueInsertionRadio = new JRadioButton("Count only unique insertions");
	private JRadioButton countInsAllReadsRadio = new JRadioButton("Count all sequence reads");
	private JCheckBox randomizePlotDataChk = new JCheckBox("Randomize data");
	private JProgressBar progressBar = new JProgressBar();
	private JTextField step2AdjustStart;
	private JTextField step2AdjustEnd;
	private JTextField step2AverageTxt;
	
	/**
	 * Create the frame.
	 */
	public AddMoreIndices(String tableName, ProjectInfo info, JFrame parentFrame){
		this();

		this.tableName = tableName;
		tableNameTxt.setText(tableName);
		this.info = info;
		this.parentFrame = parentFrame;

		setTitle("Add new essentiality indices to \"" + tableName + "\" table");
		setLocationRelativeTo(parentFrame);
				
		initializeAddPanel();
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedTab = tabbedPane.getSelectedIndex();

				if (selectedTab == 0){
					initializeAddPanel();
				}

				if (selectedTab == 1){
					initializeDensityInsertions();
				}
				
				if (selectedTab == 2){
					initializeCountInsertions();
				}

				if(selectedTab == 3){
					initializeCompare();
				}			
			}
		});
		
		ButtonGroup newData_group = new ButtonGroup();
		newData_group.add(newDataAllReadsRadio);
		newData_group.add(newDataUniqueInsertionRadio);
		
		ButtonGroup countIns_group = new ButtonGroup();
		countIns_group.add(countInsAllReadsRadio);
		countIns_group.add(countInsUniqueInsertionRadio);
	}
	
	private void initializeCompare(){
		compareWaitLbl.setVisible(false);
		
		try {
			DefaultTableModel model = AddColumns.getHeaderData(tableName, info);
			compareTable = new JTable(model);
		} catch (IOException e) {
			logger.error("There was an error while creating the header table.");
			return;
		}
		//compareTable.setBounds(0, 0, 536 - 10, 159 - 11);
		compareTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		compareTable.setCellSelectionEnabled(false);
		compareTable.getColumnModel().getColumn(0).setCellRenderer(new BoldCellRenderer());
		compareTable.getTableHeader().setFont(new Font("Arial" , Font.BOLD, 11));

		//scrollPane.setBounds(10, 11, 536, 159);
		scrollPane.add(compareTable);
		scrollPane.setViewportView(compareTable);
		
		int firstCombo = columnOneCombo.getSelectedIndex();
		int secondCombo = columnTwoCombo.getSelectedIndex();
		
		columnOneCombo.removeAllItems();
		columnTwoCombo.removeAllItems();
		for (int i = 0; i < compareTable.getColumnCount(); i++){
			if(i == 0){
				columnOneCombo.addItem("");
				columnTwoCombo.addItem("");
			}else{
				columnOneCombo.addItem(i + "");
				columnTwoCombo.addItem(i + "");
			}
		}
	
		columnOneCombo.setSelectedIndex(firstCombo);
		columnTwoCombo.setSelectedIndex(secondCombo);
		
	}
	
	private void initializeDensityInsertions(){
		BufferedReader br = null;
		if(step2Combo != null){
			step2Combo.removeAllItems();
		}
		
		step2WaitLbl.setVisible(false);
		step2ErrorLbl.setVisible(false);
		
		try{
			br = new BufferedReader(new FileReader(info.getFile()));

			String line = br.readLine();
			br.readLine();
			br.readLine();

			while(line != null){
				line = br.readLine();

				if (line != null)
					step2Combo.addItem(line.substring(0, line.length() - 6));

				line = br.readLine();
				line = br.readLine();
				line = br.readLine();
			}

			br.close();
		}catch(IOException e){
			logger.error(e.getMessage());
			return;
		}
		
		try{
			br = new BufferedReader(new FileReader(new File(info.getPath() + tableName + ".table.xls")));
			
			br.readLine();
			br.readLine();
			String[] titles = br.readLine().split("\t");
			
			int count = 0;
			step2ColumnCombo.removeAllItems();
			for (String title : titles){
				if (title.contains("density")){
					step2ColumnCombo.addItem(count + ":\t" + title);
				}
				count++;
			}
			br.close();
		}catch (IOException e){
			logger.error(e.getMessage());
			return;
		}catch (NullPointerException e1) {
			JOptionPane.showMessageDialog(null, "");
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void initializeCountInsertions(){

		BufferedReader br = null;

		if(countInsLibCombo != null){
			countInsLibCombo.removeAllItems();
		}
		
		countInsPleaseWaitLbl.setVisible(false);

		try{
			br = new BufferedReader(new FileReader(info.getFile()));

			String line = br.readLine();
			br.readLine();
			br.readLine();

			while(line != null){
				line = br.readLine();

				if (line != null)
					countInsLibCombo.addItem(line.substring(0, line.length() - 6));

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
	
	}
	
	public AddMoreIndices() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 767, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane.setBounds(10, 11, 743, 469);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Add new essentiality indices", null, panel, null);
		panel.setLayout(null);

		JLabel lblChooseALibrary = new JLabel("Choose a library:");
		lblChooseALibrary.setBounds(10, 63, 137, 14);
		panel.add(lblChooseALibrary);
		addLibraryCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				BufferedReader br = null;
				try{
					File winInfo = new File(info.getPath() + addLibraryCombo.getSelectedItem() + ".data");
					if (winInfo.exists()){
						br = new BufferedReader(new FileReader(winInfo));
						String line = br.readLine();
						int tempLen = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.length()));
						line = br.readLine();
						int tempStep = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.length()));
						br.close();
						
						addWinLenTxt.setText(tempLen + "");
						addStepTxt.setText(tempStep + "");
						
					}
				}catch (IOException e){
					logger.fatal(e.getMessage());
				}	
			}
		});
		addLibraryCombo.setToolTipText("Choose a library to use for adding datat to the table");

		addLibraryCombo.setBounds(157, 60, 569, 20);
		panel.add(addLibraryCombo);

		addWinLenTxt = new JTextField();
		addWinLenTxt.setToolTipText("Window length to used for adding data");
		addWinLenTxt.setText("1000");
		addWinLenTxt.setColumns(10);
		addWinLenTxt.setBounds(157, 122, 86, 20);
		addWinLenTxt.setInputVerifier(new IntegerInputVerifier(errorMsg1Lbl));
		panel.add(addWinLenTxt);

		addStepTxt = new JTextField();
		addStepTxt.setToolTipText("Window steps to used for adding data");
		addStepTxt.setText("10");
		addStepTxt.setColumns(10);
		addStepTxt.setBounds(512, 124, 86, 20);
		addStepTxt.setInputVerifier(new IntegerInputVerifier(errorMsg1Lbl));
		panel.add(addStepTxt);

		JLabel lblWindowLength = new JLabel("Window length:");
		lblWindowLength.setBounds(10, 125, 137, 14);
		panel.add(lblWindowLength);

		JLabel label_3 = new JLabel("Step:");
		label_3.setBounds(387, 127, 137, 14);
		panel.add(label_3);

		adjustStartTxt = new JTextField();
		adjustStartTxt.setToolTipText("You can also omit percent sign and use regular number");
		adjustStartTxt.setText("-5%");
		adjustStartTxt.setColumns(10);
		adjustStartTxt.setInputVerifier(new PercentageInputVerifier(errorMsg1Lbl, addMoreColumnsBtn));
		adjustStartTxt.setBounds(157, 153, 86, 20);
		panel.add(adjustStartTxt);

		JLabel lblAdjustGeneStart = new JLabel("Adjust gene start:");
		lblAdjustGeneStart.setBounds(10, 156, 137, 14);
		panel.add(lblAdjustGeneStart);

		adjustEndTxt = new JTextField();
		adjustEndTxt.setToolTipText("You can also omit percent sign and use regular number");
		adjustEndTxt.setText("-20%");
		adjustEndTxt.setColumns(10);
		adjustEndTxt.setBounds(512, 155, 86, 20);
		adjustEndTxt.setInputVerifier(new PercentageInputVerifier(errorMsg1Lbl, addMoreColumnsBtn));
		panel.add(adjustEndTxt);

		JLabel lblAdjustGeneEnd = new JLabel("Adjust gene end:");
		lblAdjustGeneEnd.setBounds(387, 158, 137, 14);
		panel.add(lblAdjustGeneEnd);

		addSeqLenTxt = new JTextField();
		addSeqLenTxt.setToolTipText("Loaded from the main project");
		addSeqLenTxt.setEditable(false);
		addSeqLenTxt.setColumns(10);
		addSeqLenTxt.setBounds(157, 184, 86, 20);
		panel.add(addSeqLenTxt);

		JLabel lblSequenceLength = new JLabel("Sequence length:");
		lblSequenceLength.setBounds(10, 187, 137, 14);
		panel.add(lblSequenceLength);

		addMoreColumnsBtn.setToolTipText("Start adding data to the specific table");
		addMoreColumnsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBtnAction();
			}
		});
		addMoreColumnsBtn.setBounds(606, 412, 120, 23);
		panel.add(addMoreColumnsBtn);
		
		tableNameTxt = new JTextField();
		tableNameTxt.setToolTipText("Selected data table in the main project");
		tableNameTxt.setEditable(false);
		tableNameTxt.setBounds(157, 91, 569, 20);
		panel.add(tableNameTxt);
		tableNameTxt.setColumns(10);
		
		JLabel lblSelectedTable = new JLabel("Selected table:");
		lblSelectedTable.setBounds(10, 94, 137, 14);
		panel.add(lblSelectedTable);
		
		lblPleaseWait.setIcon(new ImageIcon(AddMoreIndices.class.getResource("/resources/load.gif")));
		lblPleaseWait.setBounds(205, 416, 185, 14);
		lblPleaseWait.setVisible(false);
		panel.add(lblPleaseWait);
		
		JLabel label = new JLabel("(?)");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "Specifies the window size in base pairs. Larger window sizes differentiate more accurately between\n"
						+ "essential and non-essential genes for genes larger than the window size but they can miss essential\n"
						+ "genes smaller than the window size.");
			}
		});
		label.setToolTipText("Click me!");
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label.setBounds(253, 125, 88, 14);
		panel.add(label);
		
		JLabel label_1 = new JLabel("(?)");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "This parameter automatically adjusts the start coordinates for all genes. To leave the gene start\n"
						+ "coordinates as annotated, use zero. To shorten the gene at the 5� end by a specific number of\n"
						+ "nucleotides use a negative number (for example, -50 will eliminate first 50 base pairs of each gene). The\n"
						+ "value 50 or +50 would add 50 upstream nucleotides to the gene. Including the % sign will make the\n"
						+ "adjustments relative to the gene length. For example, -5% will shorten the gene by 5% of its length at\n"
						+ "the 5� end. Generally, you may want to exclude a short segment at the start of the gene to account for\n"
						+ "possibly misannotated gene start sites.");
			}
		});
		label_1.setToolTipText("Click me!");
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_1.setBounds(253, 156, 88, 14);
		panel.add(label_1);
		
		JLabel label_8 = new JLabel("(?)");
		label_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "The step at which the window is moved along the sequence. Shorter step sizes provide better resolution\n"
						+ "(1 is optimal) but the calculations with smaller step sizes will take longer.");	
			}
		});
		label_8.setToolTipText("Click me!");
		label_8.setForeground(Color.BLUE);
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_8.setBounds(608, 127, 88, 14);
		panel.add(label_8);
		
		JLabel label_9 = new JLabel("(?)");
		label_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "This parameter automatically adjusts the end coordinates for all genes. To leave the gene end\n"
						+ "coordinates as annotated, use zero. To shorten the gene at the 3� end by a specific number of\n"
						+ "nucleotides use a negative number (for example, -50 will eliminate last 50 base pairs of each gene). The\n"
						+ "value 50 or +50 would add 50 downstream nucleotides to the gene. Including the % sign will make the\n"
						+ "adjustments relative to the gene length. For example, -20% will shorten the gene by 20% of its length at\n"
						+ "the 3� end. Generaly, you may want to exclude the 3� end of the gene from the analysis because\n"
						+ "transposon insertions near the end of the gene are less likely to be deletrious.");
			}
		});
		label_9.setToolTipText("Click me!");
		label_9.setForeground(Color.BLUE);
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_9.setBounds(608, 158, 88, 14);
		panel.add(label_9);
		
		newDataUniqueInsertionRadio.setSelected(true);
		newDataUniqueInsertionRadio.setBounds(387, 182, 281, 23);
		panel.add(newDataUniqueInsertionRadio);
		
		newDataAllReadsRadio.setBounds(387, 208, 281, 23);
		panel.add(newDataAllReadsRadio);
		
		JXLabel lblThisToolWill = new JXLabel("This tool will add essentiality indices for all genes in the selected table. The lower the essentiality index the more likely it is that the given gene is essential.");
		lblThisToolWill.setBounds(10, 11, 716, 42);
		lblThisToolWill.setLineWrap(true);
		panel.add(lblThisToolWill);
		
		progressBar.setBounds(10, 416, 185, 14);
		panel.add(progressBar);
		
		errorMsg1Lbl.setForeground(Color.RED);
		errorMsg1Lbl.setBounds(10, 391, 434, 14);
		errorMsg1Lbl.setVisible(false);
		panel.add(errorMsg1Lbl);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Add insertion density", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel label_6 = new JLabel("This tool adds a new data column with the count of insertions or read counts in each gene.");
		label_6.setBounds(10, 11, 716, 14);
		panel_1.add(label_6);
		
		JLabel label_7 = new JLabel("Choose a library:");
		label_7.setBounds(10, 39, 137, 14);
		panel_1.add(label_7);
		
		step2Combo.setToolTipText("Choose a library to use for adding datat to the table");
		step2Combo.setBounds(157, 36, 569, 20);
		panel_1.add(step2Combo);
		
		JLabel label_11 = new JLabel("Adjust gene start:");
		label_11.setBounds(10, 67, 137, 14);
		panel_1.add(label_11);
		
		step2AdjustStart = new JTextField();
		step2AdjustStart.setToolTipText("You can also omit percent sign and use regular number");
		step2AdjustStart.setText("-5%");
		step2AdjustStart.setColumns(10);
		step2AdjustStart.setBounds(157, 64, 86, 20);
		panel_1.add(step2AdjustStart);
		
		JLabel label_13 = new JLabel("(?)");
		label_13.setToolTipText("Click me!");
		label_13.setForeground(Color.BLUE);
		label_13.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_13.setBounds(253, 67, 88, 14);
		panel_1.add(label_13);
		
		JLabel label_14 = new JLabel("Adjust gene end:");
		label_14.setBounds(350, 67, 137, 14);
		panel_1.add(label_14);
		
		step2AdjustEnd = new JTextField();
		step2AdjustEnd.setToolTipText("You can also omit percent sign and use regular number");
		step2AdjustEnd.setText("-20%");
		step2AdjustEnd.setColumns(10);
		step2AdjustEnd.setBounds(475, 64, 86, 20);
		panel_1.add(step2AdjustEnd);
		
		JLabel label_15 = new JLabel("(?)");
		label_15.setToolTipText("Click me!");
		label_15.setForeground(Color.BLUE);
		label_15.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_15.setBounds(571, 67, 88, 14);
		panel_1.add(label_15);
		
		step2UniqueRadio.setSelected(true);
		step2UniqueRadio.setBounds(10, 88, 281, 23);
		panel_1.add(step2UniqueRadio);

		step2AllRadio.setBounds(10, 114, 281, 23);
		panel_1.add(step2AllRadio);
		
		ButtonGroup step2BtnGroup = new ButtonGroup();
		step2BtnGroup.add(step2UniqueRadio);
		step2BtnGroup.add(step2AllRadio);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcInsertionDensity();
			}
		});
		btnAdd.setBounds(637, 152, 89, 23);
		panel_1.add(btnAdd);
		
		step2WaitLbl.setIcon(new ImageIcon(AddMoreIndices.class.getResource("/resources/load.gif")));
		step2WaitLbl.setBounds(10, 415, 164, 15);
		panel_1.add(step2WaitLbl);
		
		step2ErrorLbl.setForeground(Color.RED);
		step2ErrorLbl.setBounds(10, 388, 370, 15);
		panel_1.add(step2ErrorLbl);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 187, 716, 2);
		panel_1.add(separator);
		
		JLabel lblPlotDensity = new JXLabel("Plot density / number of insertions:");
		lblPlotDensity.setBounds(10, 201, 716, 15);
		panel_1.add(lblPlotDensity);
		
		JLabel lblChooseAColumn = new JLabel("Choose a column in the table:");
		lblChooseAColumn.setBounds(10, 228, 281, 15);
		panel_1.add(lblChooseAColumn);
		
		step2ColumnCombo.setBounds(253, 223, 473, 24);
		panel_1.add(step2ColumnCombo);
		
		step2PlotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotDensities();
			}
		});
		step2PlotBtn.setBounds(637, 288, 89, 25);
		panel_1.add(step2PlotBtn);
		
		JLabel lblAverageNumberOf = new JLabel("Average number of densites per  bucket:");
		lblAverageNumberOf.setBounds(9, 261, 234, 14);
		panel_1.add(lblAverageNumberOf);
		
		step2AverageTxt = new JTextField();
		step2AverageTxt.setText("20");
		step2AverageTxt.setBounds(253, 258, 86, 20);
		panel_1.add(step2AverageTxt);
		step2AverageTxt.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Add insertion counts", null, panel_3, null);
		panel_3.setLayout(null);
		
		JLabel label_10 = new JLabel("Choose a library:");
		label_10.setBounds(10, 35, 137, 14);
		panel_3.add(label_10);
		
		countInsLibCombo.setToolTipText("Choose a library to use for adding datat to the table");
		countInsLibCombo.setBounds(157, 32, 569, 20);
		panel_3.add(countInsLibCombo);
		
		JLabel lblAdjustGeneStart_1 = new JLabel("Adjust gene start:");
		lblAdjustGeneStart_1.setBounds(10, 63, 137, 14);
		panel_3.add(lblAdjustGeneStart_1);
		
		countInsAdjStartTxt = new JTextField();
		countInsAdjStartTxt.setToolTipText("You can also omit percent sign and use regular number");
		countInsAdjStartTxt.setText("-5%");
		countInsAdjStartTxt.setColumns(10);
		countInsAdjStartTxt.setBounds(157, 60, 86, 20);
		countInsAdjStartTxt.setInputVerifier(new PercentageInputVerifier(errorMsg2Lbl, countInsBtn));
		panel_3.add(countInsAdjStartTxt);
		
		JLabel lblTest = new JLabel("Adjust gene end:");
		lblTest.setBounds(350, 63, 137, 14);
		panel_3.add(lblTest);
		
		countInsAdjEndTxt = new JTextField();
		countInsAdjEndTxt.setToolTipText("You can also omit percent sign and use regular number");
		countInsAdjEndTxt.setText("-20%");
		countInsAdjEndTxt.setColumns(10);
		countInsAdjEndTxt.setBounds(475, 60, 86, 20);
		countInsAdjEndTxt.setInputVerifier(new PercentageInputVerifier(errorMsg2Lbl, countInsBtn));
		panel_3.add(countInsAdjEndTxt);
		
		countInsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				countIns();
			}
		});
		countInsBtn.setBounds(637, 412, 89, 23);
		panel_3.add(countInsBtn);
		countInsPleaseWaitLbl.setIcon(new ImageIcon(AddMoreIndices.class.getResource("/resources/load.gif")));
		
		countInsPleaseWaitLbl.setBounds(10, 416, 177, 14);
		panel_3.add(countInsPleaseWaitLbl);
		
		countInsUniqueInsertionRadio.setSelected(true);
		countInsUniqueInsertionRadio.setBounds(10, 84, 281, 23);
		panel_3.add(countInsUniqueInsertionRadio);
		
		countInsAllReadsRadio.setBounds(10, 110, 281, 23);
		panel_3.add(countInsAllReadsRadio);
		
		JLabel label_2 = new JLabel("(?)");
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "This parameter automatically adjusts the start coordinates for all genes. To leave the gene start\n"
						+ "coordinates as annotated, use zero. To shorten the gene at the 5� end by a specific number of\n"
						+ "nucleotides use a negative number (for example, -50 will eliminate first 50 base pairs of each gene). The\n"
						+ "value 50 or +50 would add 50 upstream nucleotides to the gene. Including the % sign will make the\n"
						+ "adjustments relative to the gene length. For example, -5% will shorten the gene by 5% of its length at\n"
						+ "the 5� end. Generally, you may want to exclude a short segment at the start of the gene to account for\n"
						+ "possibly misannotated gene start sites.");
			}
		});
		label_2.setToolTipText("Click me!");
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_2.setBounds(253, 63, 88, 14);
		panel_3.add(label_2);
		
		JLabel label_4 = new JLabel("(?)");
		label_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "This parameter automatically adjusts the end coordinates for all genes. To leave the gene end\n"
						+ "coordinates as annotated, use zero. To shorten the gene at the 3� end by a specific number of\n"
						+ "nucleotides use a negative number (for example, -50 will eliminate last 50 base pairs of each gene). The\n"
						+ "value 50 or +50 would add 50 downstream nucleotides to the gene. Including the % sign will make the\n"
						+ "adjustments relative to the gene length. For example, -20% will shorten the gene by 20% of its length at\n"
						+ "the 3� end. Generaly, you may want to exclude the 3� end of the gene from the analysis because\n"
						+ "transposon insertions near the end of the gene are less likely to be deletrious.");
			}
		});
		label_4.setToolTipText("Click me!");
		label_4.setForeground(Color.BLUE);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_4.setBounds(571, 63, 88, 14);
		panel_3.add(label_4);
		
		JLabel lblThisToolAdds = new JLabel("This tool adds a new data column with the count of insertions or read counts in each gene.");
		lblThisToolAdds.setBounds(10, 11, 716, 14);
		panel_3.add(lblThisToolAdds);
		
		errorMsg2Lbl.setForeground(Color.RED);
		errorMsg2Lbl.setBounds(10, 391, 384, 14);
		errorMsg2Lbl.setVisible(false);
		panel_3.add(errorMsg2Lbl);

		JPanel compareScrollPanel = new JPanel();
		tabbedPane.addTab("Compare data", null, compareScrollPanel, null);
		compareScrollPanel.setLayout(null);
		
		scrollPane.setBounds(10, 64, 716, 117);
		compareScrollPanel.add(scrollPane);
		
		compareTable = new JTable();
		scrollPane.setViewportView(compareTable);
		
		compareBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compare();
			}
		});
		compareBtn.setBounds(623, 407, 103, 23);
		compareScrollPanel.add(compareBtn);
		
		JLabel lblMaximumInsertions = new JLabel("Maximum insertions:");
		lblMaximumInsertions.setBounds(10, 192, 268, 14);
		compareScrollPanel.add(lblMaximumInsertions);
		
		compareMaxInsTxt = new JTextField();
		compareMaxInsTxt.setBounds(237, 189, 103, 20);
		compareMaxInsTxt.setInputVerifier(new IntegerInputVerifier(errorMsg3Lbl));
		compareScrollPanel.add(compareMaxInsTxt);
		compareMaxInsTxt.setColumns(10);
		
		JLabel label_12 = new JLabel("(?)");
		label_12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, "Maximum Insertions specifies a maximum number of insertions\n"
						+ "If it is higher in any of the compared columns it is adjusted to this number");
			}
		});
		label_12.setToolTipText("Click me!");
		label_12.setForeground(Color.BLUE);
		label_12.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_12.setBounds(350, 192, 88, 14);
		compareScrollPanel.add(label_12);
		
		JLabel lblSelectColumn = new JLabel("Select column 1 for compare:");
		lblSelectColumn.setBounds(10, 220, 310, 14);
		compareScrollPanel.add(lblSelectColumn);
		
		JLabel lblSelectColumn_1 = new JLabel("Select column 2 for compare:");
		lblSelectColumn_1.setBounds(10, 248, 310, 14);
		compareScrollPanel.add(lblSelectColumn_1);
		
		columnOneCombo.setBounds(288, 220, 52, 20);
		compareScrollPanel.add(columnOneCombo);
		
		columnTwoCombo.setBounds(288, 245, 52, 20);
		compareScrollPanel.add(columnTwoCombo);
		
		compareWaitLbl.setIcon(new ImageIcon(AddMoreIndices.class.getResource("/resources/load.gif")));
		compareWaitLbl.setBounds(10, 411, 112, 14);
		compareScrollPanel.add(compareWaitLbl);
		
		JButton plotTwoColBtn = new JButton("Plot");
		plotTwoColBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				plotColumns();
			}
		});
		plotTwoColBtn.setBounds(515, 407, 103, 23);
		compareScrollPanel.add(plotTwoColBtn);
		
		logPlotCheck.setBounds(450, 220, 171, 23);
		compareScrollPanel.add(logPlotCheck);
		
		randomizePlotDataChk.setBounds(450, 248, 151, 23);
		compareScrollPanel.add(randomizePlotDataChk);
		
		JXLabel lblNewLabel = new JXLabel("The 'Compare' adds a new column that shows the difference between the data in two previously created columns; the 'Plot' button plots data in two previously created columns.");
		lblNewLabel.setBounds(10, 11, 716, 42);
		lblNewLabel.setLineWrap(true);
		compareScrollPanel.add(lblNewLabel);
		
		JLabel label_5 = new JLabel("(?)");
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, "This option will add a small random number to each value. This could be beneficial when plotting data where several\n"
						+ "genes often have the same values in both compared columns. As a result, such genes will appear as a cloud of data\n"
						+ "points in the plot rather than a single point.");
			}
		});
		label_5.setToolTipText("Click me!");
		label_5.setForeground(Color.BLUE);
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_5.setBounds(605, 253, 88, 14);
		compareScrollPanel.add(label_5);
		
		errorMsg3Lbl.setForeground(Color.RED);
		errorMsg3Lbl.setBounds(368, 192, 178, 14);
		errorMsg3Lbl.setVisible(false);
		compareScrollPanel.add(errorMsg3Lbl);
	}

	protected void plotDensities() {
		
		String item = (String) step2ColumnCombo.getSelectedItem();
		
		final String columnName = item.substring(item.indexOf("\t") + 1);
		final int columnIndex = Integer.parseInt(item.substring(0, item.indexOf(":")));
		final int averageBucket = Integer.parseInt(step2AverageTxt.getText());
		
		step2WaitLbl.setVisible(true);
		step2PlotBtn.setEnabled(false);
		
		this.logPlot = logPlotCheck.isSelected();
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				String tempTitle = "Table: " + tableName + ", Column: " + columnName + " (#" + columnIndex + "), Average densities per bucket: " + averageBucket;
				
				ChartPanel panel;
				try {
					panel = new ChartPanel(PlotData.plotInsertionDensities(new File(info.getPath() + tableName + ".table.xls"), columnName, columnIndex, tempTitle, averageBucket, info));
				} catch (IOException e) {
					logger.error("Some error while creating the plot!");
					return;
				}
				
				PlotViewer frame = new PlotViewer();					
				frame.setPlotName(tempTitle);
				frame.setVisible(true);
				frame.addPlot(panel);
				
				step2WaitLbl.setVisible(false);
				step2PlotBtn.setEnabled(true);
				initializeDensityInsertions();
			}
		})).start();
		
	}

	protected void calcInsertionDensity() {
		final String libraryName = (String) step2Combo.getSelectedItem(); 
		final String adjStart = step2AdjustStart.getText();
		final String adjEnd = step2AdjustEnd.getText();
		
		countInsPleaseWaitLbl.setVisible(true);
		countInsBtn.setEnabled(false);
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if (AddColumns.calcInsertionDensity(libraryName, tableName, adjStart, adjEnd, step2UniqueRadio.isSelected(), info).compareTo(Messages.successMsg) == 0){
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(AddMoreIndices.this, "There was some problem, data was not added!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					countInsPleaseWaitLbl.setVisible(false);
					countInsBtn.setEnabled(true);
					initializeDensityInsertions();
				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				}
			}
		})).start();
	}

	private void plotColumns(){
		
		String firstCol = (String) columnOneCombo.getSelectedItem();
		String secondCol = (String) columnTwoCombo.getSelectedItem();
		
		if (firstCol == null || firstCol.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please select columns to compare.");
			return;
		}
		
		if (secondCol == null || secondCol.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please select columns to compare.");
			return;
		}
		
		final int first = Integer.parseInt(firstCol);
		final int second = Integer.parseInt(secondCol);
		
		if (first == second){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please select different columns to compare.");
			return;
		}
		
		compareWaitLbl.setVisible(true);
		compareBtn.setEnabled(false);
		
		this.logPlot = logPlotCheck.isSelected();
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				String tempTitle = "";
				if (!randomizePlotDataChk.isSelected())
					tempTitle = tableName + ", Columns: " + first + " v.s. " + second;
				else
					tempTitle = tableName + ", Columns: " + first + " v.s. " + second + " (Randomized data)";
				
				ChartPanel panel;
				PlotViewer frame = new PlotViewer();
				try {
					ArrayList<String> geneInfo = new ArrayList<>();
					panel = new ChartPanel(PlotData.plotColumns(tableName, first, second, AddMoreIndices.this.logPlot, tempTitle, randomizePlotDataChk.isSelected(), geneInfo, info));
					panel.addChartMouseListener(new MyChartMouseListener(geneInfo, frame.plotInfo));
					frame.hasChartListener = true;
				} catch (IOException e) {
					logger.error("Some error while creating the plot!");
					return;
				}
				
				frame.setPlotName(tempTitle);
				frame.setVisible(true);
				frame.addPlot(panel);
				
				compareWaitLbl.setVisible(false);
				compareBtn.setEnabled(true);
				initializeCompare();
			}
		})).start();
		
	}
	
	private void compare(){
		
		String maxInsString = compareMaxInsTxt.getText();
		String firstCol = (String) columnOneCombo.getSelectedItem();
		String secondCol = (String) columnTwoCombo.getSelectedItem();
		
		if (maxInsString == null || maxInsString.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please enter Max Insertion.");
			return;
		}
		
		if (firstCol == null || firstCol.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please select columns to compare.");
			return;
		}
		
		if (secondCol == null || secondCol.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please select columns to compare.");
			return;
		}
		
		final int first = Integer.parseInt(firstCol);
		final int second = Integer.parseInt(secondCol);
		if (first == second){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please select different columns to compare.");
			return;
		}
		
		compareWaitLbl.setVisible(true);
		compareBtn.setEnabled(false);
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					int maxIns = Integer.parseInt(compareMaxInsTxt.getText());
					
					if (AddColumns.compareColumns(tableName, first, second, maxIns, info).compareTo(Messages.successMsg) == 0){
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(AddMoreIndices.this, "The data could not be written to the table.\n"
								+ "Check to see if they are not in use by other programs!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					compareWaitLbl.setVisible(false);
					compareBtn.setEnabled(true);
					initializeCompare();
				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				}catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(AddMoreIndices.this, "Please enter valid numbers for the highlighted fields.");
					compareWaitLbl.setVisible(false);
					compareBtn.setEnabled(true);
					return;
				}
			}
		})).start();
		
	}
	
	private void countIns(){
		
		final String libraryName = (String) addLibraryCombo.getSelectedItem(); 
		final String adjStart = adjustStartTxt.getText();
		final String adjEnd = adjustEndTxt.getText();
		
		countInsPleaseWaitLbl.setVisible(true);
		countInsBtn.setEnabled(false);
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if (AddColumns.countInsertions(libraryName, tableName, adjStart, adjEnd, countInsUniqueInsertionRadio.isSelected(), info).compareTo(Messages.successMsg) == 0){
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(AddMoreIndices.this, "There was some problem, data was not added!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					countInsPleaseWaitLbl.setVisible(false);
					countInsBtn.setEnabled(true);
				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				}
			}
		})).start();
		
	}
	
	private void initializeProgressBar(){		
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		
		progressBar.setValue(0);
		progressBar.setVisible(false);
	}
	
	private void addBtnAction(){

		final String libraryName = (String) addLibraryCombo.getSelectedItem();
		final String adjStart = adjustStartTxt.getText();
		final String adjEnd = adjustEndTxt.getText();
		final int seqLen = info.getSequenceLen();

		initializeProgressBar();
		lblPleaseWait.setVisible(true);
		progressBar.setVisible(true);
		addMoreColumnsBtn.setEnabled(false);
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int windowLen = 0;
					int step = 0;
					try{
						windowLen = Integer.parseInt(addWinLenTxt.getText());
						step = Integer.parseInt(addStepTxt.getText());
					}catch(NumberFormatException e){
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Please enter valid numbers for the highlighted fields.");
						progressBar.setVisible(false);
						lblPleaseWait.setVisible(false);
						addMoreColumnsBtn.setEnabled(true);
						return;
					}
					
					if (AddColumns.add(libraryName, tableName, windowLen, step, adjStart, adjEnd, seqLen, newDataUniqueInsertionRadio.isSelected(), progressBar, info).compareTo(Messages.successMsg) == 0){
						progressBar.setVisible(false);
						lblPleaseWait.setVisible(false);
						addMoreColumnsBtn.setEnabled(true);
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(AddMoreIndices.this, "There was some problem, data was not added!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}

				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				}
			}
		})).start();
		
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

			br.close();
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

		try{
			File winInfo = new File(info.getPath() + addLibraryCombo.getSelectedItem() + ".data");
			if (winInfo.exists()){
				br = new BufferedReader(new FileReader(winInfo));
				String line = br.readLine();
				int tempLen = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.length()));
				line = br.readLine();
				int tempStep = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.length()));
				br.close();
				
				addWinLenTxt.setText(tempLen + "");
				addStepTxt.setText(tempStep + "");
				
			}
		}catch (IOException e){
			logger.fatal(e.getMessage());
		}
		
		addSeqLenTxt.setText(info.getSequenceLen() + "");
		initializeProgressBar();
	}
}