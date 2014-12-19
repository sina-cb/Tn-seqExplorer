package CompareUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class Compare extends JFrame {

	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(Compare.class.getName());
	private static final long serialVersionUID = -2458273832974069254L;
	private JPanel contentPane;
	private JTextField input1Txt;
	private JTextField header1Txt;
	private JTextField position1Txt;
	private JTextField gene1Txt;
	private JTextField index1Txt;
	private JLabel lblInputDataSet_1;
	private JTextField input2Txt;
	private JLabel lblHeaderSkipLines_1;
	private JTextField header2Txt;
	private JButton input1Btn;
	private JButton input2Btn;
	private JLabel lblPositionColumns_1;
	private JLabel lblGeneNameColumns;
	private JLabel lblEssentialityIndexColumn_1;
	private JTextField position2Txt;
	private JTextField gene2Txt;
	private JTextField index2Txt;
	private JLabel lblOutputFolder;
	private JTextField outputTxt;
	private JButton outputBtn;
	private JButton processBtn;
	private JCheckBox normalizeChk;
	private JLabel lblOtheColumns;
	private JTextField otherColumnsTxt;
	private JLabel lblOtherColumns;
	private JTextField otherColumns2Txt;

	/**
	 * Create the frame.
	 */
	public Compare() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 667, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(87dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblInputDataSet = new JLabel("Input data set one");
		contentPane.add(lblInputDataSet, "2, 2, left, default");
		
		input1Txt = new JTextField();
		input1Txt.setText("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\Compare\\table 550.table.xls");
		contentPane.add(input1Txt, "4, 2, fill, default");
		input1Txt.setColumns(10);
		
		input1Btn = new JButton("Browse");
		input1Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Utilities.browseForInputFile(input1Txt, "xls", "Any text file with any format", Compare.this);
			}
		});
		contentPane.add(input1Btn, "6, 2");
		
		JLabel lblHeaderSkipLines = new JLabel("Header skip lines");
		contentPane.add(lblHeaderSkipLines, "2, 4, left, default");
		
		header1Txt = new JTextField();
		header1Txt.setText("5");
		contentPane.add(header1Txt, "4, 4, fill, default");
		header1Txt.setColumns(10);
		
		JLabel lblPositionColumns = new JLabel("Position columns");
		contentPane.add(lblPositionColumns, "2, 6, left, default");
		
		position1Txt = new JTextField();
		position1Txt.setText("0 1");
		contentPane.add(position1Txt, "4, 6, fill, default");
		position1Txt.setColumns(10);
		
		JLabel lblGeneNameColumn = new JLabel("Locus tag column");
		contentPane.add(lblGeneNameColumn, "2, 8, left, default");
		
		gene1Txt = new JTextField();
		gene1Txt.setText("5");
		contentPane.add(gene1Txt, "4, 8, fill, default");
		gene1Txt.setColumns(10);
		
		JLabel lblEssentialityIndexColumn = new JLabel("Essentiality index column");
		contentPane.add(lblEssentialityIndexColumn, "2, 10, left, default");
		
		index1Txt = new JTextField();
		index1Txt.setText("8");
		contentPane.add(index1Txt, "4, 10, fill, default");
		index1Txt.setColumns(10);
		
		lblOtheColumns = new JLabel("Othe columns");
		contentPane.add(lblOtheColumns, "2, 12");
		
		otherColumnsTxt = new JTextField();
		otherColumnsTxt.setText("3 6 7");
		contentPane.add(otherColumnsTxt, "4, 12, fill, default");
		otherColumnsTxt.setColumns(10);
		
		lblInputDataSet_1 = new JLabel("Input data set two");
		contentPane.add(lblInputDataSet_1, "2, 16, left, default");
		
		input2Txt = new JTextField();
		input2Txt.setText("C:\\Users\\sina\\Desktop\\Essential Genes\\data\\Compare\\essentialgenes_alloutputmerged.tsv");
		contentPane.add(input2Txt, "4, 16, fill, default");
		input2Txt.setColumns(10);
		
		input2Btn = new JButton("Browse");
		input2Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utilities.browseForInputFile(input2Txt, "tsv", "Any text file with any format", Compare.this);
			}
		});
		contentPane.add(input2Btn, "6, 16");
		
		lblHeaderSkipLines_1 = new JLabel("Header skip lines");
		contentPane.add(lblHeaderSkipLines_1, "2, 18, left, default");
		
		header2Txt = new JTextField();
		header2Txt.setText("1");
		contentPane.add(header2Txt, "4, 18, fill, default");
		header2Txt.setColumns(10);
		
		lblPositionColumns_1 = new JLabel("Position columns");
		contentPane.add(lblPositionColumns_1, "2, 20, left, default");
		
		position2Txt = new JTextField();
		position2Txt.setText("10 11");
		contentPane.add(position2Txt, "4, 20, fill, default");
		position2Txt.setColumns(10);
		
		lblGeneNameColumns = new JLabel("Locus tag column");
		contentPane.add(lblGeneNameColumns, "2, 22, left, default");
		
		gene2Txt = new JTextField();
		gene2Txt.setText("1");
		contentPane.add(gene2Txt, "4, 22, fill, default");
		gene2Txt.setColumns(10);
		
		lblEssentialityIndexColumn_1 = new JLabel("Essentiality index column");
		contentPane.add(lblEssentialityIndexColumn_1, "2, 24, left, default");
		
		index2Txt = new JTextField();
		index2Txt.setText("7");
		contentPane.add(index2Txt, "4, 24, fill, default");
		index2Txt.setColumns(10);
		
		lblOtherColumns = new JLabel("Other Columns");
		contentPane.add(lblOtherColumns, "2, 26, left, default");
		
		otherColumns2Txt = new JTextField();
		otherColumns2Txt.setText("8");
		contentPane.add(otherColumns2Txt, "4, 26, fill, default");
		otherColumns2Txt.setColumns(10);
		
		lblOutputFolder = new JLabel("Output folder");
		contentPane.add(lblOutputFolder, "2, 30, left, default");
		
		outputTxt = new JTextField();
		contentPane.add(outputTxt, "4, 30, fill, default");
		outputTxt.setColumns(10);
		
		outputBtn = new JButton("Browse");
		outputBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CompareMain.DEBUG){
					outputTxt.setText("C:\\Users\\Sina\\Desktop\\" + "results" + System.currentTimeMillis() 
							+ File.separator);
				}else{
					outputTxt.setText(Utilities.browseForFolder(Compare.this) + "results" + System.currentTimeMillis() 
							+ File.separator);
				}
			}
		});
		contentPane.add(outputBtn, "6, 30");
		
		processBtn = new JButton("Process");
		processBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					startTheProcess();
				} catch (IOException e1) {
					logger.error(e1.getStackTrace());
				}
			}
		});
		
		normalizeChk = new JCheckBox("Normalize");
		contentPane.add(normalizeChk, "4, 34, right, default");
		contentPane.add(processBtn, "6, 34");
		
		this.setLocationRelativeTo(null);
	}

	private void startTheProcess() throws IOException{
		
		File input1 = new File(input1Txt.getText());
		File input2 = new File(input2Txt.getText());
		
		int headerSkip = Integer.parseInt(header1Txt.getText());
		int geneIndex = Integer.parseInt(gene1Txt.getText());
		int essentiality = Integer.parseInt(index1Txt.getText());
		
		String[] positionsString = position1Txt.getText().split(" ");
		List<Integer> positions = new ArrayList<Integer>();
		for (String pos : positionsString){
			positions.add(Integer.parseInt(pos));
		}
		
		String[] otherString = otherColumnsTxt.getText().split(" ");
		List<Integer> others = new ArrayList<Integer>();
		for (String other : otherString){
			others.add(Integer.parseInt(other));
		}
		
		BufferedReader br = new BufferedReader(new FileReader(input1));
		for (int i = 0; i < headerSkip; i++){
			br.readLine();
		}
		
		List<GeneHolder> genes1 = new ArrayList<GeneHolder>();
		
		String line = "";
		line = br.readLine();
		while (line != null){
			String[] temp = line.split("\t");
			
			GeneHolder gene = new GeneHolder();
			gene.setStart(Integer.parseInt(temp[positions.get(0)]));
			gene.setEnd(Integer.parseInt(temp[positions.get(1)]));
			gene.setEssentialityIndex(Double.parseDouble(temp[essentiality]));
			gene.setLocus_tag(temp[geneIndex]);
			gene.setLength(Integer.parseInt(temp[others.get(0)]));
			gene.setGene_symbol(temp[others.get(1)]);
			gene.setDescription(temp[others.get(2)]);
			genes1.add(gene);		
			
			line = br.readLine();
		}
		br.close();
		
		headerSkip = Integer.parseInt(header2Txt.getText());
		geneIndex = Integer.parseInt(gene2Txt.getText());
		essentiality = Integer.parseInt(index2Txt.getText());
		
		positionsString = position2Txt.getText().split(" ");
		positions = new ArrayList<Integer>();
		for (String pos : positionsString){
			positions.add(Integer.parseInt(pos));
		}
		
		br = new BufferedReader(new FileReader(input2));
		for (int i = 0; i < headerSkip; i++){
			br.readLine();
		}
		
		otherString = otherColumns2Txt.getText().split(" ");
		others = new ArrayList<Integer>();
		for (String other : otherString){
			others.add(Integer.parseInt(other));
		}
		
		List<GeneHolder> genes2 = new ArrayList<GeneHolder>();
		
		line = br.readLine();
		while (line != null){
			String[] temp = line.split("\t");
			
			GeneHolder gene = new GeneHolder();
			gene.setStart(Integer.parseInt(temp[positions.get(0)]));
			gene.setEnd(Integer.parseInt(temp[positions.get(1)]));
			if (temp[essentiality].equals("NA")){
				gene.setEssentialityIndex(Double.NaN);
			}else{
				gene.setEssentialityIndex(Double.parseDouble(temp[essentiality]));
			}
			gene.setpValue(temp[others.get(0)]);
			gene.setLocus_tag(temp[geneIndex]);
			genes2.add(gene);	
			
			line = br.readLine();
		}
		br.close();
		
		if (normalizeChk.isSelected()){
			
			double sum = 0;
			for (GeneHolder gene : genes1){
				if (!gene.getEssentialityIndex().equals(Double.NaN)){
					sum += gene.getEssentialityIndex();
				}
			}
			
			for (GeneHolder gene : genes1){
				gene.setEssentialityIndex(gene.getEssentialityIndex() / sum);
			}
			
			sum = 0;
			for (GeneHolder gene : genes2){
				if (!gene.getEssentialityIndex().equals(Double.NaN)){
					sum += gene.getEssentialityIndex();
				}
			}
			
			for (GeneHolder gene : genes2){
				gene.setEssentialityIndex(gene.getEssentialityIndex() / sum);
			}
		}
		
		File outputFolder = new File(outputTxt.getText());
		if (!outputFolder.exists()){
			outputFolder.mkdir();
		}
		
		File output = new File(outputTxt.getText() + "compare_results.xls");
		output.createNewFile();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		bw.write("Start\tEnd\tLength\tLocus_Tag\tGene_symbol\tDescription\tMiddle\tEI\tlogFC\tPValue\n");
		for (int i = 0; i < genes1.size(); i++){

			int j = 0;
			for (j = 0; j < genes2.size(); j++){
				if (genes2.get(j).getStart() == genes1.get(i).getStart() 
						&& genes2.get(j).getEnd() == genes1.get(i).getEnd()){
					break;
				}
			}
			
			StringBuilder toWrite = new StringBuilder();
			toWrite.append(String.format("%d\t%d\t", genes1.get(i).getStart(), genes1.get(i).getEnd()));
			toWrite.append(String.format("%d\t", genes1.get(i).getLength()));
			toWrite.append(String.format("%s\t", genes1.get(i).getLocus_tag()));
			toWrite.append(String.format("%s\t", genes1.get(i).getGene_symbol()));
			toWrite.append(String.format("%s\t", genes1.get(i).getDescription()));
			toWrite.append(String.format("%d\t", (genes1.get(i).getStart() + genes1.get(i).getEnd()) / 2));
			
			//NumberFormat formatter = new DecimalFormat("0.##E0");
			toWrite.append(String.format("%s\t", genes1.get(i).getEssentialityIndex()));
			
			if (genes2.get(j).getEssentialityIndex().equals(Double.NaN)){
				toWrite.append("0");				
			}else{
				
				toWrite.append(String.format("%s\t", genes2.get(j).getEssentialityIndex()));
			}
			toWrite.append(String.format("%s", genes2.get(j).getpValue()));
			bw.write(toWrite + "\n");
		}
		
		bw.close();
		
		JOptionPane.showMessageDialog(this, "Done!");
	}
	
	private class GeneHolder{
		private int start;
		private int end;
		private double essentialityIndex;
		private String locus_tag;
		private String gene_symbol;
		private String description;
		private int length;
		private String pValue;
		
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
		public Double getEssentialityIndex() {
			return new Double(essentialityIndex);
		}
		public void setEssentialityIndex(double essentialityIndex) {
			this.essentialityIndex = essentialityIndex;
		}
		public String getLocus_tag() {
			return locus_tag;
		}
		public void setLocus_tag(String locus_tag) {
			this.locus_tag = locus_tag;
		}
		public String getGene_symbol() {
			return gene_symbol;
		}
		public void setGene_symbol(String gene_symbol) {
			this.gene_symbol = gene_symbol;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public String getpValue() {
			return pValue;
		}
		public void setpValue(String pValue) {
			this.pValue = pValue;
		}
	}
	
}
