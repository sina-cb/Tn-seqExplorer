package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

import CustomGUIComponent.BoldCellRenderer;
import essgenes.AddColumns;
import essgenes.Messages;
import essgenes.ProjectInfo;

@SuppressWarnings("serial")
public class AddMoreIndices extends JFrame {

	private Logger logger = Logger.getLogger(AddMoreIndices.class.getName());
	private String tableName = "";
	private ProjectInfo info = null;
	@SuppressWarnings("unused")
	private JFrame parentFrame = null;

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
	private JTable rndTable;
	private JTextField rndLowerBoundTxt;
	private JTextField rndHigherBoundTxt;
	private JTextField rndConstantTxt;
	private JButton randomizeBtn = new JButton("Randomize");
	private JLabel lblPleaseWait_1 = new JLabel("Please wait...");
	private JComboBox<String> columnSelectCombo = new JComboBox<String>();
	private JScrollPane scrollPane_1 = new JScrollPane();
	
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
					initializeCountInsertions();
				}

				if(selectedTab == 2){
					initializeCompare();
				}

				if(selectedTab == 3){
					initializeRandomize();
				}				
			}
		});
	}

	private void initializeRandomize(){
		lblPleaseWait_1.setVisible(false);
		
		try {
			DefaultTableModel model = AddColumns.getHeaderData(tableName, info);
			rndTable = new JTable(model);
		} catch (IOException e) {
			logger.error("There was an error while creating the header table.");
			return;
		}
		//compareTable.setBounds(0, 0, 536 - 10, 159 - 11);
		rndTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		rndTable.setCellSelectionEnabled(false);
		rndTable.getColumnModel().getColumn(0).setCellRenderer(new BoldCellRenderer());
		rndTable.getTableHeader().setFont(new Font("Arial" , Font.BOLD, 11));

		//scrollPane.setBounds(10, 11, 536, 159);
		scrollPane_1.add(rndTable);
		scrollPane_1.setViewportView(rndTable);
		
		columnSelectCombo.removeAllItems();
		for (int i = 0; i < rndTable.getColumnCount(); i++){
			if(i == 0){
				columnSelectCombo.addItem("");
			}else{
				columnSelectCombo.addItem(i + "");
			}
		}
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
		setBounds(100, 100, 588, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane.setBounds(10, 11, 561, 340);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Add new data", null, panel, null);
		panel.setLayout(null);

		JLabel lblChooseALibrary = new JLabel("Choose a library:");
		lblChooseALibrary.setBounds(10, 14, 137, 14);
		panel.add(lblChooseALibrary);
		addLibraryCombo.setToolTipText("Choose a library to use for adding datat to the table");

		addLibraryCombo.setBounds(157, 11, 361, 20);
		panel.add(addLibraryCombo);

		addWinLenTxt = new JTextField();
		addWinLenTxt.setToolTipText("Window length to used for adding data");
		addWinLenTxt.setText("1000");
		addWinLenTxt.setColumns(10);
		addWinLenTxt.setBounds(157, 73, 86, 20);
		panel.add(addWinLenTxt);

		addStepTxt = new JTextField();
		addStepTxt.setToolTipText("Window steps to used for adding data");
		addStepTxt.setText("10");
		addStepTxt.setColumns(10);
		addStepTxt.setBounds(432, 73, 86, 20);
		panel.add(addStepTxt);

		JLabel label_2 = new JLabel("Window Length:");
		label_2.setBounds(10, 76, 137, 14);
		panel.add(label_2);

		JLabel label_3 = new JLabel("Step:");
		label_3.setBounds(307, 76, 137, 14);
		panel.add(label_3);

		adjustStartTxt = new JTextField();
		adjustStartTxt.setToolTipText("You can also omit percent sign and use regular number");
		adjustStartTxt.setText("-5%");
		adjustStartTxt.setColumns(10);
		adjustStartTxt.setBounds(157, 104, 86, 20);
		panel.add(adjustStartTxt);

		JLabel label_4 = new JLabel("Adjust Start:");
		label_4.setBounds(10, 107, 137, 14);
		panel.add(label_4);

		adjustEndTxt = new JTextField();
		adjustEndTxt.setToolTipText("You can also omit percent sign and use regular number");
		adjustEndTxt.setText("-20%");
		adjustEndTxt.setColumns(10);
		adjustEndTxt.setBounds(432, 104, 86, 20);
		panel.add(adjustEndTxt);

		JLabel label_5 = new JLabel("Adjust End:");
		label_5.setBounds(307, 107, 137, 14);
		panel.add(label_5);

		addSeqLenTxt = new JTextField();
		addSeqLenTxt.setToolTipText("Loaded from the main project");
		addSeqLenTxt.setEditable(false);
		addSeqLenTxt.setColumns(10);
		addSeqLenTxt.setBounds(157, 135, 86, 20);
		panel.add(addSeqLenTxt);

		JLabel label_6 = new JLabel("Sequence Length:");
		label_6.setBounds(10, 138, 137, 14);
		panel.add(label_6);

		addMoreColumnsBtn.setToolTipText("Start adding data to the specific table");
		addMoreColumnsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBtnAction();
			}
		});
		addMoreColumnsBtn.setBounds(426, 278, 120, 23);
		panel.add(addMoreColumnsBtn);
		
		tableNameTxt = new JTextField();
		tableNameTxt.setToolTipText("Selected data table in the main project");
		tableNameTxt.setEditable(false);
		tableNameTxt.setBounds(157, 42, 361, 20);
		panel.add(tableNameTxt);
		tableNameTxt.setColumns(10);
		
		JLabel lblSelectedTable = new JLabel("Selected table:");
		lblSelectedTable.setBounds(10, 45, 137, 14);
		panel.add(lblSelectedTable);
		
		lblPleaseWait.setIcon(new ImageIcon(AddMoreIndices.class.getResource("/resources/load.gif")));
		lblPleaseWait.setBounds(231, 282, 185, 14);
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
		label.setBounds(253, 76, 88, 14);
		panel.add(label);
		
		JLabel label_1 = new JLabel("(?)");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "This parameter automatically adjusts the start coordinates for all genes. To leave the gene start\n"
						+ "coordinates as annotated, use zero. To shorten the gene at the 5’ end by a specific number of\n"
						+ "nucleotides use a negative number (for example, -50 will eliminate first 50 base pairs of each gene). The\n"
						+ "value 50 or +50 would add 50 upstream nucleotides to the gene. Including the % sign will make the\n"
						+ "adjustments relative to the gene length. For example, -5% will shorten the gene by 5% of its length at\n"
						+ "the 5’ end. Generally, you may want to exclude a short segment at the start of the gene to account for\n"
						+ "possibly misannotated gene start sites.");
			}
		});
		label_1.setToolTipText("Click me!");
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_1.setBounds(253, 107, 88, 14);
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
		label_8.setBounds(528, 76, 88, 14);
		panel.add(label_8);
		
		JLabel label_9 = new JLabel("(?)");
		label_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, ""
						+ "This parameter automatically adjusts the end coordinates for all genes. To leave the gene end\n"
						+ "coordinates as annotated, use zero. To shorten the gene at the 3’ end by a specific number of\n"
						+ "nucleotides use a negative number (for example, -50 will eliminate last 50 base pairs of each gene). The\n"
						+ "value 50 or +50 would add 50 downstream nucleotides to the gene. Including the % sign will make the\n"
						+ "adjustments relative to the gene length. For example, -20% will shorten the gene by 20% of its length at\n"
						+ "the 3’ end. Generaly, you may want to exclude the 3’ end of the gene from the analysis because\n"
						+ "transposon insertions near the end of a gene may not always be deleterious.");
			}
		});
		label_9.setToolTipText("Click me!");
		label_9.setForeground(Color.BLUE);
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_9.setBounds(528, 107, 88, 14);
		panel.add(label_9);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Count Insertions", null, panel_3, null);
		panel_3.setLayout(null);
		
		JLabel label_10 = new JLabel("Choose a library:");
		label_10.setBounds(10, 14, 137, 14);
		panel_3.add(label_10);
		
		countInsLibCombo.setToolTipText("Choose a library to use for adding datat to the table");
		countInsLibCombo.setBounds(157, 11, 361, 20);
		panel_3.add(countInsLibCombo);
		
		JLabel label_11 = new JLabel("Adjust Start:");
		label_11.setBounds(10, 42, 137, 14);
		panel_3.add(label_11);
		
		countInsAdjStartTxt = new JTextField();
		countInsAdjStartTxt.setToolTipText("You can also omit percent sign and use regular number");
		countInsAdjStartTxt.setText("-5%");
		countInsAdjStartTxt.setColumns(10);
		countInsAdjStartTxt.setBounds(157, 39, 86, 20);
		panel_3.add(countInsAdjStartTxt);
		
		JLabel lblTest = new JLabel("Adjust End:");
		lblTest.setBounds(307, 42, 137, 14);
		panel_3.add(lblTest);
		
		countInsAdjEndTxt = new JTextField();
		countInsAdjEndTxt.setToolTipText("You can also omit percent sign and use regular number");
		countInsAdjEndTxt.setText("-20%");
		countInsAdjEndTxt.setColumns(10);
		countInsAdjEndTxt.setBounds(432, 39, 86, 20);
		panel_3.add(countInsAdjEndTxt);
		
		countInsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				countIns();
			}
		});
		countInsBtn.setBounds(457, 278, 89, 23);
		panel_3.add(countInsBtn);
		
		countInsPleaseWaitLbl.setBounds(245, 282, 177, 14);
		panel_3.add(countInsPleaseWaitLbl);

		JPanel compareScrollPanel = new JPanel();
		tabbedPane.addTab("Compare", null, compareScrollPanel, null);
		compareScrollPanel.setLayout(null);
		
		scrollPane.setBounds(10, 11, 536, 159);
		compareScrollPanel.add(scrollPane);
		
		compareTable = new JTable();
		scrollPane.setViewportView(compareTable);
		
		compareBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compare();
			}
		});
		compareBtn.setBounds(443, 278, 103, 23);
		compareScrollPanel.add(compareBtn);
		
		JLabel lblMaximumInsertions = new JLabel("Maximum Insertions:");
		lblMaximumInsertions.setBounds(10, 181, 268, 14);
		compareScrollPanel.add(lblMaximumInsertions);
		
		compareMaxInsTxt = new JTextField();
		compareMaxInsTxt.setBounds(237, 178, 103, 20);
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
		label_12.setBounds(350, 181, 88, 14);
		compareScrollPanel.add(label_12);
		
		JLabel lblSelectColumn = new JLabel("Select column 1 for compare:");
		lblSelectColumn.setBounds(10, 209, 310, 14);
		compareScrollPanel.add(lblSelectColumn);
		
		JLabel lblSelectColumn_1 = new JLabel("Select column 2 for compare:");
		lblSelectColumn_1.setBounds(10, 237, 310, 14);
		compareScrollPanel.add(lblSelectColumn_1);
		
		columnOneCombo.setBounds(288, 209, 52, 20);
		compareScrollPanel.add(columnOneCombo);
		
		columnTwoCombo.setBounds(288, 234, 52, 20);
		compareScrollPanel.add(columnTwoCombo);
		
		compareWaitLbl.setIcon(new ImageIcon(AddMoreIndices.class.getResource("/resources/load.gif")));
		compareWaitLbl.setBounds(228, 282, 112, 14);
		compareScrollPanel.add(compareWaitLbl);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Randomize", null, panel_2, null);
		panel_2.setLayout(null);
		
		scrollPane_1.setBounds(10, 11, 536, 159);
		panel_2.add(scrollPane_1);
		
		rndTable = new JTable();
		scrollPane_1.setViewportView(rndTable);
		
		JLabel lblRandomLowerBound = new JLabel("Random lower bound:");
		lblRandomLowerBound.setBounds(10, 184, 268, 14);
		panel_2.add(lblRandomLowerBound);
		
		rndLowerBoundTxt = new JTextField();
		rndLowerBoundTxt.setColumns(10);
		rndLowerBoundTxt.setBounds(237, 181, 103, 20);
		panel_2.add(rndLowerBoundTxt);
		
		JLabel label_13 = new JLabel("(?)");
		label_13.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, "Lower bound of the random adjustment");
			}
		});
		label_13.setToolTipText("Click me!");
		label_13.setForeground(Color.BLUE);
		label_13.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_13.setBounds(350, 184, 88, 14);
		panel_2.add(label_13);
		
		JLabel lblRandomHigherBound = new JLabel("Random higher bound:");
		lblRandomHigherBound.setBounds(10, 212, 268, 14);
		panel_2.add(lblRandomHigherBound);
		
		rndHigherBoundTxt = new JTextField();
		rndHigherBoundTxt.setColumns(10);
		rndHigherBoundTxt.setBounds(237, 209, 103, 20);
		panel_2.add(rndHigherBoundTxt);
		
		JLabel label_14 = new JLabel("(?)");
		label_14.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, "Higher bound of the random adjustment");
			}
		});
		label_14.setToolTipText("Click me!");
		label_14.setForeground(Color.BLUE);
		label_14.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_14.setBounds(350, 212, 88, 14);
		panel_2.add(label_14);
		
		JLabel lblSelectAColumn = new JLabel("Select a column:");
		lblSelectAColumn.setBounds(10, 268, 310, 14);
		panel_2.add(lblSelectAColumn);
		
		columnSelectCombo.setBounds(288, 265, 52, 20);
		panel_2.add(columnSelectCombo);
		
		JLabel lblEnterRandomConstant = new JLabel("Enter a constant:");
		lblEnterRandomConstant.setBounds(10, 240, 268, 14);
		panel_2.add(lblEnterRandomConstant);
		
		rndConstantTxt = new JTextField();
		rndConstantTxt.setColumns(10);
		rndConstantTxt.setBounds(237, 237, 103, 20);
		panel_2.add(rndConstantTxt);
		
		JLabel label_15 = new JLabel("(?)");
		label_15.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(AddMoreIndices.this, "This constant is added to the numbers.");
			}
		});
		label_15.setToolTipText("Click me!");
		label_15.setForeground(Color.BLUE);
		label_15.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_15.setBounds(350, 240, 88, 14);
		panel_2.add(label_15);
		randomizeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				randomize();
			}
		});
		
		randomizeBtn.setBounds(434, 278, 112, 23);
		panel_2.add(randomizeBtn);
		
		lblPleaseWait_1.setIcon(new ImageIcon(AddMoreIndices.class.getResource("/resources/load.gif")));
		lblPleaseWait_1.setBounds(244, 293, 180, 14);
		panel_2.add(lblPleaseWait_1);
	}

	private void randomize(){
		String lowerS = rndLowerBoundTxt.getText();
		String higherS = rndHigherBoundTxt.getText();
		String consS = rndConstantTxt.getText();
		String comboS = (String) columnSelectCombo.getSelectedItem();
		
		if (lowerS == null || lowerS.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please enter the lower bound of randomization.");
			return;
		}
		
		if (higherS == null || higherS.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please enter the higher bound of randomization.");
			return;
		}
		
		if (consS == null || consS.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please enter the constant to be added.");
			return;
		}
		
		if (comboS == null || comboS.compareTo("") == 0){
			JOptionPane.showMessageDialog(AddMoreIndices.this, "Please select a column.");
			return;
		}
		
		final double lower = Double.parseDouble(lowerS);
		final double higher = Double.parseDouble(higherS);
		final double constant = Double.parseDouble(consS);
		final int column = Integer.parseInt(comboS);
		
		lblPleaseWait_1.setVisible(true);
		randomizeBtn.setEnabled(false);
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if (AddColumns.randomize(tableName, column, lower, higher, constant, info).compareTo(Messages.successMsg) == 0){
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(AddMoreIndices.this, "There was some problem, data was not added!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					lblPleaseWait_1.setVisible(false);
					randomizeBtn.setEnabled(true);
					initializeRandomize();
				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				}
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
		
		final int maxIns = Integer.parseInt(maxInsString);
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
					if (AddColumns.compareColumns(tableName, first, second, maxIns, info).compareTo(Messages.successMsg) == 0){
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(AddMoreIndices.this, "There was some problem, data was not added!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					compareWaitLbl.setVisible(false);
					compareBtn.setEnabled(true);
					initializeCompare();
				} catch (IOException e) {
					logger.error(e.getMessage());
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
					if (AddColumns.countInsertions(libraryName, tableName, adjStart, adjEnd, info).compareTo(Messages.successMsg) == 0){
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
	
	private void addBtnAction(){

		final String libraryName = (String) addLibraryCombo.getSelectedItem();
		final int windowLen = Integer.parseInt(addWinLenTxt.getText());
		final int step = Integer.parseInt(addStepTxt.getText());
		final String adjStart = adjustStartTxt.getText();
		final String adjEnd = adjustEndTxt.getText();
		final int seqLen = info.getSequenceLen();

		lblPleaseWait.setVisible(true);
		addMoreColumnsBtn.setEnabled(false);

		(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (AddColumns.add(libraryName, tableName, windowLen, step, adjStart, adjEnd, seqLen, info).compareTo(Messages.successMsg) == 0){
						JOptionPane.showMessageDialog(AddMoreIndices.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(AddMoreIndices.this, "There was some problem, data was not added!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}

					lblPleaseWait.setVisible(false);
					addMoreColumnsBtn.setEnabled(true);
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