package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import essgenes.AddColumns;
import essgenes.Messages;
import essgenes.ProjectInfo;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
		addMoreColumnsBtn.setBounds(429, 135, 89, 23);
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
		lblPleaseWait.setBounds(244, 222, 121, 14);
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

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Compare", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Randomize", null, panel_2, null);
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
